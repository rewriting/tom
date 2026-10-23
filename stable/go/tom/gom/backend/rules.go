package backend

// Gom rules (`module M:rules() { lhs -> rhs [if cond] … }`), after Java
// Gom's tom.gom.expander.rule: the rules are parsed (Rule.g) and turned
// into construction hooks of the operators heading their left-hand
// sides (RuleExpander.expandRules), whose body is Tom code (a %match
// that returns the right-hand side). The Go backend generates the same
// Tom text, with Go host code, and runs it as a user make hook.

import (
	"fmt"
	"strings"
	"unicode"
)

// ruleTerm is a term of the rule language (Rule.gom's Term).
type ruleTerm struct {
	kind string // "Appl", "At", "Anti", "UnnamedVar", "UnnamedVarStar", "Var", "VarStar", "BuiltinInt", "BuiltinString"
	name string // symbol (Appl), variable name (At, Var, VarStar), literal text (Builtin*)
	args []*ruleTerm
	sub  *ruleTerm // At, Anti
}

// ruleCond is a condition of a conditional rule (Rule.gom's Condition).
type ruleCond struct {
	op     string // "==", "!=", "<=", "<", ">=", ">", "<<", "&&", "||"
	t1, t2 *ruleTerm
	conds  []*ruleCond // && and ||
}

// gomRule is one rule; cond is nil for an unconditional rule.
type gomRule struct {
	lhs, rhs *ruleTerm
	cond     *ruleCond
}

type ruleToken struct {
	kind string // "ID", "INT", "STRING", or the punctuation itself
	text string
}

// lexRules tokenises a rules hook body (Rule.g's lexer).
func lexRules(src string) ([]ruleToken, error) {
	var toks []ruleToken
	puncts := []string{"->", "&&", "||", "==", "!=", "<=", "<<", ">=", "&", "_", "*", "@", ":", "(", ")", ",", "!", "<", ">"}
	for i := 0; i < len(src); {
		c := src[i]
		switch {
		case c == ' ' || c == '\t' || c == '\n' || c == '\r':
			i++
		case strings.HasPrefix(src[i:], "//"):
			for i < len(src) && src[i] != '\n' && src[i] != '\r' {
				i++
			}
		case c >= '0' && c <= '9':
			j := i
			for j < len(src) && src[j] >= '0' && src[j] <= '9' {
				j++
			}
			toks = append(toks, ruleToken{"INT", src[i:j]})
			i = j
		case c == '"':
			j := i + 1
			for j < len(src) && src[j] != '"' {
				if src[j] == '\\' {
					j++
				}
				if j < len(src) && (src[j] == '\n' || src[j] == '\r') {
					return nil, fmt.Errorf("unterminated string in rules")
				}
				j++
			}
			if j >= len(src) {
				return nil, fmt.Errorf("unterminated string in rules")
			}
			toks = append(toks, ruleToken{"STRING", src[i : j+1]})
			i = j + 1
		case c == '_' && (i+1 >= len(src) || !isRuleIDChar(src[i+1])):
			toks = append(toks, ruleToken{"_", "_"})
			i++
		case isRuleIDStart(c):
			j := i
			for j < len(src) && isRuleIDChar(src[j]) {
				j++
			}
			if src[i:j] == "if" {
				toks = append(toks, ruleToken{"if", "if"})
			} else {
				toks = append(toks, ruleToken{"ID", src[i:j]})
			}
			i = j
		default:
			matched := false
			for _, p := range puncts {
				if strings.HasPrefix(src[i:], p) {
					toks = append(toks, ruleToken{p, p})
					i += len(p)
					matched = true
					break
				}
			}
			if !matched {
				return nil, fmt.Errorf("unexpected character %q in rules", c)
			}
		}
	}
	return toks, nil
}

func isRuleIDStart(c byte) bool {
	return c == '_' || unicode.IsLetter(rune(c))
}

func isRuleIDChar(c byte) bool {
	return isRuleIDStart(c) || (c >= '0' && c <= '9')
}

type ruleParser struct {
	toks []ruleToken
	pos  int
}

func (p *ruleParser) peek(k int) string {
	if p.pos+k < len(p.toks) {
		return p.toks[p.pos+k].kind
	}
	return "EOF"
}

func (p *ruleParser) next() ruleToken {
	t := p.toks[p.pos]
	p.pos++
	return t
}

func (p *ruleParser) expect(kind string) (ruleToken, error) {
	if p.peek(0) != kind {
		return ruleToken{}, fmt.Errorf("rules: expected %q, got %q", kind, p.peek(0))
	}
	return p.next(), nil
}

// parseRules parses the body of a rules hook (Rule.g's ruleset).
func parseRules(src string) ([]gomRule, error) {
	toks, err := lexRules(src)
	if err != nil {
		return nil, err
	}
	p := &ruleParser{toks: toks}
	var rules []gomRule
	for p.peek(0) != "EOF" {
		lhs, err := p.pattern()
		if err != nil {
			return nil, err
		}
		if _, err := p.expect("->"); err != nil {
			return nil, err
		}
		rhs, err := p.term()
		if err != nil {
			return nil, err
		}
		r := gomRule{lhs: lhs, rhs: rhs}
		if p.peek(0) == "if" {
			p.next()
			if r.cond, err = p.condition(); err != nil {
				return nil, err
			}
		}
		rules = append(rules, r)
	}
	return rules, nil
}

// pattern: ID '(' terms ')' | ID '@' ID '(' terms ')' | '_' | '_' '*' | '!' pattern
func (p *ruleParser) pattern() (*ruleTerm, error) {
	switch {
	case p.peek(0) == "ID" && p.peek(1) == "(":
		return p.appl()
	case p.peek(0) == "ID" && p.peek(1) == "@":
		name := p.next().text
		p.next()
		if p.peek(0) != "ID" {
			return nil, fmt.Errorf("rules: expected a symbol after %s@", name)
		}
		a, err := p.appl()
		if err != nil {
			return nil, err
		}
		return &ruleTerm{kind: "At", name: name, sub: a}, nil
	case p.peek(0) == "_":
		p.next()
		if p.peek(0) == "*" {
			p.next()
			return &ruleTerm{kind: "UnnamedVarStar"}, nil
		}
		return &ruleTerm{kind: "UnnamedVar"}, nil
	case p.peek(0) == "!":
		p.next()
		sub, err := p.pattern()
		if err != nil {
			return nil, err
		}
		return &ruleTerm{kind: "Anti", sub: sub}, nil
	}
	return nil, fmt.Errorf("rules: unexpected %q in a pattern", p.peek(0))
}

func (p *ruleParser) appl() (*ruleTerm, error) {
	sym := p.next().text
	if _, err := p.expect("("); err != nil {
		return nil, err
	}
	t := &ruleTerm{kind: "Appl", name: sym}
	if p.peek(0) != ")" {
		for {
			a, err := p.term()
			if err != nil {
				return nil, err
			}
			t.args = append(t.args, a)
			if p.peek(0) != "," {
				break
			}
			p.next()
		}
	}
	if _, err := p.expect(")"); err != nil {
		return nil, err
	}
	return t, nil
}

// term: pattern | var | builtin
func (p *ruleParser) term() (*ruleTerm, error) {
	switch {
	case p.peek(0) == "ID" && (p.peek(1) == "(" || p.peek(1) == "@"):
		return p.pattern()
	case p.peek(0) == "ID":
		name := p.next().text
		if p.peek(0) == "*" {
			p.next()
			return &ruleTerm{kind: "VarStar", name: name}, nil
		}
		return &ruleTerm{kind: "Var", name: name}, nil
	case p.peek(0) == "INT":
		return &ruleTerm{kind: "BuiltinInt", name: p.next().text}, nil
	case p.peek(0) == "STRING":
		return &ruleTerm{kind: "BuiltinString", name: p.next().text}, nil
	}
	return p.pattern()
}

// condition: andcondition ('||' andcondition)*
func (p *ruleParser) condition() (*ruleCond, error) {
	c, err := p.andCondition()
	if err != nil {
		return nil, err
	}
	if p.peek(0) != "||" {
		return c, nil
	}
	or := &ruleCond{op: "||", conds: []*ruleCond{c}}
	for p.peek(0) == "||" {
		p.next()
		c, err := p.andCondition()
		if err != nil {
			return nil, err
		}
		or.conds = append(or.conds, c)
	}
	return or, nil
}

func (p *ruleParser) andCondition() (*ruleCond, error) {
	c, err := p.simpleCondition()
	if err != nil {
		return nil, err
	}
	if p.peek(0) != "&&" {
		return c, nil
	}
	and := &ruleCond{op: "&&", conds: []*ruleCond{c}}
	for p.peek(0) == "&&" {
		p.next()
		c, err := p.simpleCondition()
		if err != nil {
			return nil, err
		}
		and.conds = append(and.conds, c)
	}
	return and, nil
}

func (p *ruleParser) simpleCondition() (*ruleCond, error) {
	if p.peek(0) == "(" {
		p.next()
		c, err := p.condition()
		if err != nil {
			return nil, err
		}
		if _, err := p.expect(")"); err != nil {
			return nil, err
		}
		return c, nil
	}
	t1, err := p.term()
	if err != nil {
		return nil, err
	}
	op := p.peek(0)
	switch op {
	case "==", "!=", "<=", "<", ">=", ">", "<<":
		p.next()
	default:
		return nil, fmt.Errorf("rules: expected a comparison, got %q", op)
	}
	t2, err := p.term()
	if err != nil {
		return nil, err
	}
	return &ruleCond{op: op, t1: t1, t2: t2}, nil
}

// genRuleTerm prints a term as RuleExpander.genTerm does.
func genRuleTerm(t *ruleTerm, out *strings.Builder) {
	switch t.kind {
	case "Appl":
		out.WriteString(t.name)
		out.WriteString("(")
		genRuleTermList(t.args, out)
		out.WriteString(")")
	case "At":
		out.WriteString(t.name)
		out.WriteString("@")
		genRuleTerm(t.sub, out)
	case "Anti":
		out.WriteString("!")
		genRuleTerm(t.sub, out)
	case "UnnamedVar":
		out.WriteString("_")
	case "UnnamedVarStar":
		out.WriteString("_*")
	case "Var":
		out.WriteString(t.name)
	case "VarStar":
		out.WriteString(t.name)
		out.WriteString("*")
	case "BuiltinInt", "BuiltinString":
		out.WriteString(t.name)
	}
}

func genRuleTermList(ts []*ruleTerm, out *strings.Builder) {
	for i, t := range ts {
		genRuleTerm(t, out)
		if i < len(ts)-1 {
			out.WriteString(", ")
		}
	}
}

// genRuleCond prints a condition as RuleExpander.genCondition does.
func genRuleCond(c *ruleCond, out *strings.Builder) {
	switch c.op {
	case "&&", "||":
		genRuleCondList(c.op, c.conds, out)
	default:
		genRuleTerm(c.t1, out)
		out.WriteString(" " + c.op + " ")
		genRuleTerm(c.t2, out)
	}
}

// genRuleCondList: CondAnd(head, tail*) is "(head && tail)" when the tail
// is not empty, head otherwise (likewise for ||).
func genRuleCondList(op string, cs []*ruleCond, out *strings.Builder) {
	if len(cs) == 1 {
		genRuleCond(cs[0], out)
		return
	}
	out.WriteString("(")
	genRuleCond(cs[0], out)
	out.WriteString(" " + op + " ")
	genRuleCondList(op, cs[1:], out)
	out.WriteString(")")
}

// ruleHookCode is RuleExpander.generateHookCode: the body of the make
// hook of a non-variadic operator (args arg_1…arg_n of the given sorts),
// or, with no sort, of the make_empty hook of a variadic one.
func ruleHookCode(sorts []string, rules []gomRule) string {
	var out strings.Builder
	if len(sorts) == 0 {
		for _, r := range rules {
			if r.cond == nil {
				out.WriteString("    return `")
				genRuleTerm(r.rhs, &out)
				out.WriteString(";\n")
			} else {
				out.WriteString("    %match{\n")
				genRuleCond(r.cond, &out)
				out.WriteString(" -> { return `")
				genRuleTerm(r.rhs, &out)
				out.WriteString("; }\n")
				out.WriteString("}\n")
			}
		}
		return out.String()
	}
	out.WriteString("    %match(")
	for i, s := range sorts {
		fmt.Fprintf(&out, "%s arg_%d", s, i+1)
		if i < len(sorts)-1 {
			out.WriteString(", ")
		}
	}
	out.WriteString(") {\n")
	for _, r := range rules {
		genRuleTermList(r.lhs.args, &out)
		if r.cond != nil {
			out.WriteString(" && ")
			genRuleCond(r.cond, &out)
		}
		out.WriteString(" -> { return `")
		genRuleTerm(r.rhs, &out)
		out.WriteString("; }\n")
	}
	out.WriteString("    }\n")
	return out.String()
}

// ruleVariadicHookCode is RuleExpander.generateVariadicHookCode: the
// body of the make_insert hook (args head, tail) of a variadic operator;
// realMake is the Go function that inserts without hooks (Java's
// Cons<op>.realMake).
func ruleVariadicHookCode(realMake string, rules []gomRule) string {
	var out strings.Builder
	fmt.Fprintf(&out, "    %%match(%s(head,tail)) {\n", realMake)
	for _, r := range rules {
		genRuleTerm(r.lhs, &out)
		if r.cond != nil {
			out.WriteString(" && ")
			genRuleCond(r.cond, &out)
		}
		out.WriteString(" -> { return `")
		genRuleTerm(r.rhs, &out)
		out.WriteString("; }\n")
	}
	out.WriteString("    }\n")
	return out.String()
}

// ruleHead returns the symbol heading the left-hand side of a rule, or
// "" when it is not an application: RuleExpander only attaches rules
// whose lhs is an Appl (the others are discarded with a warning).
func ruleHead(r gomRule) string {
	if r.lhs.kind == "Appl" {
		return r.lhs.name
	}
	return ""
}
