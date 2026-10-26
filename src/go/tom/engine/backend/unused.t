package backend

// "_ = x": the variables Go would reject as unused (GoGenerator.t:904-1399,
// see the comment there for the rule). buildLet (filtrage.t) writes
// "_ = x" after "var x T = e" unless readsInstruction(body, x) is
// readRead; generateInstruction (generate.t) writes buildUnreadSubjects
// before a CompiledMatch.

import (
	"strings"

	. "tom/engine/adt"
	"tom/library/aterm"
)

%include { adt.tom }

// The answers of readsInstruction (GoGenerator.t:936): NO_READ, READ, UNSURE.
const (
	readNone   = 0
	readRead   = 1
	readUnsure = 2
)

// buildUnreadSubjects is GoGenerator.buildUnreadSubjects (GoGenerator.t:941):
// "_ = s; " for each host subject s of a CompiledMatch that its automata
// do not read.
func (g *goGenerator) buildUnreadSubjects(automata Instruction) {
	for _, name := range g.hostSubjects(automata) {
		if !g.classFields[name] && g.readsInstruction(automata, name) != readRead {
			g.output.Write("_ = " + name + "; ")
		}
	}
}

// hostSubjects is GoGenerator.hostSubjects (GoGenerator.t:965): the
// variables the automata of a CompiledMatch mention out of the actions
// without declaring them, in the order of a pre-order walk.
func (g *goGenerator) hostSubjects(automata Instruction) []string {
	var names []string
	g.collectHostSubjects(automata, map[string]bool{}, &names)
	return names
}

// children is the generic view of a term of the ADT (tom.library.sl.Visitable).
type children interface {
	ChildCount() int
	ChildAt(i int) any
}

// collectHostSubjects is GoGenerator.collectHostSubjects (GoGenerator.t:971).
// The children of a Java list (cons) are its head and its tail, those of a
// Go list its elements: the pre-order is the same.
func (g *goGenerator) collectHostSubjects(t any, declared map[string]bool, names *[]string) {
	switch term := t.(type) {
	case Instruction:
		%match(term) {
			RawAction[] -> { return }
			(Let|LetRef)(v, e, body) -> {
				g.collectHostSubjects(`e, declared, names)
				inner := make(map[string]bool, len(declared)+1)
				for k := range declared {
					inner[k] = true
				}
				inner[g.localName(`v)] = true
				g.collectHostSubjects(`body, inner, names)
				return
			}
		}
	case BQTerm:
		%match(term) {
			v@(BQVariable|BQVariableStar)[] -> {
				name := g.localName(`v)
				if !declared[name] && !containsString(*names, name) {
					*names = append(*names, name)
				}
				return
			}
		}
	case InstructionList, Expression, BQTermList, Code, CodeList, CompositeMember:
	default:
		return
	}
	c, ok := t.(children)
	if !ok {
		return
	}
	for i := 0; i < c.ChildCount(); i++ {
		g.collectHostSubjects(c.ChildAt(i), declared, names)
	}
}

func containsString(l []string, s string) bool {
	for _, e := range l {
		if e == s {
			return true
		}
	}
	return false
}

// readsInstruction is GoGenerator.readsInstruction (GoGenerator.t:1004).
func (g *goGenerator) readsInstruction(inst Instruction, name string) int {
	%match(inst) {
		CodeToInstruction(c) -> { return g.readsCode(`c, name) }
		BQTermToInstruction(t) -> { return g.readsBQTerm(`t, name) }
		ExpressionToInstruction(e) -> { return g.readsExpression(`e, name) }
		If(c, s, f) -> {
			return max(g.readsExpression(`c, name), g.readsInstruction(`s, name), g.readsInstruction(`f, name))
		}
		DoWhile(i, c) -> { return max(g.readsInstruction(`i, name), g.readsExpression(`c, name)) }
		WhileDo(c, i) -> { return max(g.readsExpression(`c, name), g.readsInstruction(`i, name)) }
		(Let|LetRef)(v, e, body) -> {
			r := g.readsExpression(`e, name)
			if name == g.localName(`v) {
				return r
			}
			return max(r, g.readsInstruction(`body, name))
		}
		Assign(_, e) -> { return g.readsExpression(`e, name) }
		AssignArray(_, i, e) -> { return max(g.readsBQTerm(`i, name), g.readsExpression(`e, name)) }
		Return(t) -> { return g.readsBQTerm(`t, name) }
		(AbstractBlock|UnamedBlock)(l) -> { return g.readsInstructionList(`l, name) }
		NamedBlock(_, l) -> { return g.readsInstructionList(`l, name) }
		RawAction(i) -> { return g.readsInstruction(`i, name) }
		CompiledMatch(i, _) -> {
			r := g.readsInstruction(`i, name)
			if !g.classFields[name] && containsString(g.hostSubjects(`i), name) {
				r = max(r, readRead)
			}
			return r
		}
		CompiledPattern(_, i) -> { return g.readsInstruction(`i, name) }
	}
	return readNone
}

// readsInstructionList is GoGenerator.readsInstructionList (GoGenerator.t:1039).
//
// The host code TL/ITL instructions that follow one another are scanned as
// one text, since the island parser cuts the host code at every brace and
// backquote term (readsInstructionRuns, GoGenerator.t:1373).
func (g *goGenerator) readsInstructionList(l InstructionList, name string) int {
	return g.readsInstructionRuns(l, name)
}

// readsCode is GoGenerator.readsCode (GoGenerator.t:1047).
func (g *goGenerator) readsCode(code Code, name string) int {
	r := readNone
	%match(code) {
		(Tom|TomInclude)(concCode(_*, c, _*)) -> { r = max(r, g.readsCode(`c, name)) }
		TargetLanguageToCode(tl) -> { r = g.readsTargetLanguage(`tl, name) }
		InstructionToCode(i) -> { r = g.readsInstruction(`i, name) }
		BQTermToCode(t) -> { r = g.readsBQTerm(`t, name) }
	}
	return r
}

// readsTargetLanguage is GoGenerator.readsTargetLanguage (GoGenerator.t:1058).
func (g *goGenerator) readsTargetLanguage(tl TargetLanguage, name string) int {
	%match(tl) {
		TL(code, _, _) -> { return scanHostText(`code, name) }
		ITL(code) -> { return scanHostText(`code, name) }
	}
	return readNone
}

// readsBQTermList is GoGenerator.readsBQTermList (GoGenerator.t:1066).
func (g *goGenerator) readsBQTermList(l BQTermList, name string) int {
	r := readNone
	%match(l) {
		concBQTerm(_*, t, _*) -> { r = max(r, g.readsBQTerm(`t, name)) }
	}
	return r
}

// readsBQTerm is GoGenerator.readsBQTerm (GoGenerator.t:1074).
func (g *goGenerator) readsBQTerm(t BQTerm, name string) int {
	r := readNone
	%match(t) {
		v@(BQVariable|BQVariableStar)[] -> {
			if name == g.localName(`v) {
				return readRead
			}
			return readNone
		}
		FunctionCall(_, _, args) -> { return g.readsBQTermList(`args, name) }
		(BuildAppendList|BuildAppendArray)(_, h, tl) -> {
			return max(g.readsBQTerm(`h, name), g.readsBQTerm(`tl, name))
		}
		ExpressionToBQTerm(e) -> { return g.readsExpression(`e, name) }
		Composite(_*, CompositeBQTerm(b), _*) -> { r = max(r, g.readsBQTerm(`b, name)) }
		Composite(_*, CompositeTL(tl), _*) -> { r = max(r, g.readsTargetLanguage(`tl, name)) }
	}
	if !g.inline {
		%match(t) {
			BuildTerm(_, args, _) -> { return g.readsBQTermList(`args, name) }
			(BuildConsList|BuildConsArray)(_, h, tl) -> {
				return max(g.readsBQTerm(`h, name), g.readsBQTerm(`tl, name))
			}
			BuildEmptyArray(_, s) -> { return g.readsBQTerm(`s, name) }
		}
	}
	return r
}

// readsExpression is GoGenerator.readsExpression (GoGenerator.t:1100).
func (g *goGenerator) readsExpression(e Expression, name string) int {
	%match(e) {
		Code(code) -> { return scanHostText(`code, name) }
		BQTermToExpression(t) -> { return g.readsBQTerm(`t, name) }
		TomInstructionToExpression(i) -> { return g.readsInstruction(`i, name) }
		Negation(a) -> { return g.readsExpression(`a, name) }
		(And|Or|GreaterThan|GreaterOrEqualThan|LessThan|LessOrEqualThan)(a, b) -> {
			return max(g.readsExpression(`a, name), g.readsExpression(`b, name))
		}
		Conditional(c, a, b) -> {
			return max(g.readsExpression(`c, name), g.readsExpression(`a, name), g.readsExpression(`b, name))
		}
		Cast(_, a) -> { return g.readsExpression(`a, name) }
		EqualTerm(_, a, v@(Variable|VariableStar)[]) -> {
			r := g.readsBQTerm(`a, name)
			if name == g.getVariableNameAbstract(`v) {
				r = max(r, readRead)
			}
			return r
		}
		EqualTerm(_, a, _) -> { return g.readsBQTerm(`a, name) }
		EqualBQTerm(_, a, b) -> { return max(g.readsBQTerm(`a, name), g.readsBQTerm(`b, name)) }
		(AddOne|SubstractOne)(a) -> { return g.readsBQTerm(`a, name) }
		Substract(a, b) -> { return max(g.readsBQTerm(`a, name), g.readsBQTerm(`b, name)) }
		GetSliceList(_, a, b, c) -> {
			return max(g.readsBQTerm(`a, name), g.readsBQTerm(`b, name), g.readsBQTerm(`c, name))
		}
		GetSliceArray(_, a, b, c) -> {
			return max(g.readsBQTerm(`a, name), g.readsBQTerm(`b, name), g.readsBQTerm(`c, name))
		}
	}
	if !g.inline {
		%match(e) {
			GetSlot(_, _, _, a) -> { return g.readsBQTerm(`a, name) }
			IsFsym(_, a) -> { return g.readsBQTerm(`a, name) }
			GetHead(_, _, a) -> { return g.readsBQTerm(`a, name) }
			GetTail(_, a) -> { return g.readsBQTerm(`a, name) }
			IsEmptyList(_, a) -> { return g.readsBQTerm(`a, name) }
			GetSize(_, a) -> { return g.readsBQTerm(`a, name) }
			IsEmptyArray(_, a, b) -> { return max(g.readsBQTerm(`a, name), g.readsBQTerm(`b, name)) }
			GetElement(_, _, a, b) -> { return max(g.readsBQTerm(`a, name), g.readsBQTerm(`b, name)) }
		}
	}
	return readNone
}

// localName is GoGenerator.localName (GoGenerator.t:1143): the name of a
// variable without the %strategy receiver; "" (Java: null) for a
// non-variable.
func (g *goGenerator) localName(v BQTerm) string {
	%match(v) {
		(BQVariable|BQVariableStar)[] -> { return g.getVariableNameBQAbstract(v) }
	}
	return ""
}

// scanHostText is GoGenerator.scanHostText (GoGenerator.t:1173): whether
// the Go host code text surely reads the variable name.
func scanHostText(text, name string) int {
	if name == "" || !strings.Contains(text, name) {
		return readNone
	}
	toks, ok := goTokens(text)
	if !ok {
		return readUnsure
	}
	result := readNone
	var stack []string
	prevClosed := ""
	for i, tok := range toks {
		closed := ""
		switch {
		case tok == "(":
			prev := ""
			if i > 0 {
				prev = toks[i-1]
			}
			if prev == "func" || (prev == ")" && prevClosed == "f(") {
				stack = append(stack, "f(")
			} else if prev == "var" || prev == "const" || prev == "type" {
				stack = append(stack, "g(")
			} else {
				stack = append(stack, "(")
			}
		case tok == "{" && i > 0 && (toks[i-1] == "struct" || toks[i-1] == "interface"):
			stack = append(stack, "s{")
		case tok == "[" || tok == "{":
			stack = append(stack, tok)
		case tok == ")" || tok == "]" || tok == "}":
			if len(stack) > 0 {
				closed = stack[len(stack)-1]
				stack = stack[:len(stack)-1]
			}
		case tok == name:
			result = max(result, hostOccurrence(toks, i, stack))
			if result == readUnsure {
				return readUnsure
			}
		}
		prevClosed = closed
	}
	return result
}

// hostOccurrence is GoGenerator.hostOccurrence (GoGenerator.t:1208).
func hostOccurrence(toks []string, i int, stack []string) int {
	prev, next := "", ""
	hasNext := i+1 < len(toks)
	if i > 0 {
		prev = toks[i-1]
	}
	if hasNext {
		next = toks[i+1]
	}
	if containsString(stack, "f(") || containsString(stack, "g(") || containsString(stack, "s{") {
		return readUnsure
	}
	if prev == "." || prev == "goto" || prev == "break" || prev == "continue" {
		return readNone
	}
	if prev == "var" || prev == "const" || prev == "type" || prev == "func" {
		return readUnsure
	}
	start := i
	for start > 0 && !isStatementEnd(toks[start-1]) {
		start--
	}
	if first := toks[start]; first == "var" || first == "const" || first == "type" {
		return readUnsure
	}
	if !hasNext {
		return readRead
	}
	if next == ":=" || isGoIdentStart(next[0]) {
		return readUnsure
	}
	if next == ":" || next == "=" {
		return readNone
	}
	if next == "," {
		if top := stack; len(top) > 0 && (top[len(top)-1] == "(" || top[len(top)-1] == "[") {
			return readRead
		}
		depth := 0
		for j := i + 1; j < len(toks); j++ {
			t := toks[j]
			switch {
			case t == "(" || t == "[" || t == "{":
				depth++
			case t == ")" || t == "]" || t == "}":
				if depth == 0 {
					return readRead
				}
				depth--
			case depth == 0:
				if t == ":=" {
					return readUnsure
				} else if t == "=" {
					return readNone
				} else if t == ";" || (t == "\n" && toks[j-1] != ",") {
					return readRead
				}
			}
		}
		return readUnsure
	}
	return readRead
}

// isStatementEnd is GoGenerator.isStatementEnd (GoGenerator.t:1267).
func isStatementEnd(tok string) bool {
	return tok == ";" || tok == "\n" || tok == "{" || tok == "}"
}

// isGoIdentStart is GoGenerator.isGoIdentStart (GoGenerator.t:1271): a
// byte of a non-ASCII character counts, as a Java char >= 0x80 does.
func isGoIdentStart(c byte) bool {
	return c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c >= 0x80
}

// isGoIdentPart is GoGenerator.isGoIdentPart (GoGenerator.t:1275).
func isGoIdentPart(c byte) bool {
	return isGoIdentStart(c) || (c >= '0' && c <= '9')
}

// backquote is GoGenerator.BACKQUOTE (GoGenerator.t:1280): the delimiter
// of a Go raw string (written as a number: the character starts a Tom term).
const backquote = byte(96)

// goOperators is GoGenerator.GO_OPERATORS, longest first.
var goOperators = []string{
	"<<=", ">>=", "&^=", "...",
	":=", "==", "!=", "<=", ">=", "&&", "||", "<-", "++", "--",
	"+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", "<<", ">>", "&^",
}

// goTokens is GoGenerator.goTokens (GoGenerator.t:1294); ok is false where
// Java returns null (a literal or a comment is not terminated).
func goTokens(s string) (toks []string, ok bool) {
	n := len(s)
	i := 0
	for i < n {
		c := s[i]
		switch {
		case c == '\n':
			toks = append(toks, "\n")
			i++
		case c == ' ' || c == '\t' || c == '\r' || c == '\f':
			i++
		case c == '/' && i+1 < n && s[i+1] == '/':
			for i < n && s[i] != '\n' {
				i++
			}
		case c == '/' && i+1 < n && s[i+1] == '*':
			end := strings.Index(s[i+2:], "*/")
			if end < 0 {
				return nil, false
			}
			end += i + 2
			if strings.IndexByte(s[i+2:end], '\n') >= 0 {
				toks = append(toks, "\n")
			}
			i = end + 2
		case c == '"' || c == '\'':
			j := i + 1
			for j < n && s[j] != c && s[j] != '\n' {
				if s[j] == '\\' {
					j++
				}
				j++
			}
			if j >= n || s[j] != c {
				return nil, false
			}
			toks = append(toks, "0")
			i = j + 1
		case c == backquote:
			end := strings.IndexByte(s[i+1:], backquote)
			if end < 0 {
				return nil, false
			}
			toks = append(toks, "0")
			i += end + 2
		case isGoIdentStart(c):
			j := i
			for j < n && isGoIdentPart(s[j]) {
				j++
			}
			toks = append(toks, s[i:j])
			i = j
		case c >= '0' && c <= '9':
			for i < n && (isGoIdentPart(s[i]) || s[i] == '.') {
				i++
			}
			toks = append(toks, "0")
		default:
			op := s[i : i+1]
			for _, o := range goOperators {
				if strings.HasPrefix(s[i:], o) {
					op = o
					break
				}
			}
			toks = append(toks, op)
			i += len(op)
		}
	}
	return toks, true
}

// readsInstructionRuns is GoGenerator.readsInstructionRuns (GoGenerator.t:1373): the
// host code instructions that follow one another are scanned as one text.
func (g *goGenerator) readsInstructionRuns(l InstructionList, name string) int {
	r := readNone
	var run strings.Builder
	%match(l) {
		concInstruction(_*, i, _*) -> {
			if text, ok := hostText(`i); ok {
				run.WriteString(text)
			} else {
				r = max(r, scanHostText(run.String(), name))
				run.Reset()
				r = max(r, g.readsInstruction(`i, name))
			}
		}
	}
	return max(r, scanHostText(run.String(), name))
}

// hostText is GoGenerator.hostText (GoGenerator.t:1392).
func hostText(i Instruction) (string, bool) {
	%match(i) {
		CodeToInstruction(TargetLanguageToCode(TL(t, _, _))) -> { return `t, true }
		CodeToInstruction(TargetLanguageToCode(ITL(t))) -> { return `t, true }
	}
	return "", false
}

// The generated mapping code (adt.tom) names aterm types; this keeps the import used.
var _ = aterm.GetInstance
