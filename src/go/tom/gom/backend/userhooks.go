package backend

// User construction hooks of a Gom module (outside the compiler's own
// ADT, whose hooks the knownHookTable lowers), after Java Gom's
// HookTypeExpander and OperatorTemplate/VariadicOperatorTemplate:
//
//   - `op:make(args) {…}` on a non-variadic operator, and the rules of
//     `module M:rules() {…}` whose left-hand side it heads
//     (RuleExpander), run in declaration order at the head of its make;
//   - `op:make_insert(e,l) {…}`, `op:make_empty() {…}` and the rules of a
//     variadic operator run at the head of Cons<op>.make / Empty<op>.make;
//   - the theory of a variadic operator whose domain is its codomain —
//     `op:FL()`, `op:AU() { neutral }`, `op:ACU() { neutral }`,
//     `op:Free()`, or FL by default when it has no construction hook —
//     normalises the lists (flattening, neutral element, ordering);
//   - `block`/`javablock` hooks (any scope) are host code, emitted into
//     the mapping; `import` hooks must be empty in Go (imports belong
//     to the importing Go file).
//
// The hook bodies are Tom+Go code, emitted into the Tom mapping (see
// emitUserMakeHooksGo); the theories are generated in Go.

import (
	"fmt"
	"strings"

	gomast "tom/gom/adt"
)

// opHook is one construction hook of an operator.
type opHook struct {
	kind string // "make" (non-variadic), "make_insert" or "make_empty" (variadic)
	args []string
	body string // Tom+Go code
}

// opTheory is the theory of a variadic operator.
type opTheory struct {
	kind    string // "", "FL", "Free", "AU", "ACU"
	neutral string // the trimmed body of an AU/ACU hook (the neutral element), or ""
}

// moduleIsKnown reports whether mod is a module of the compiler's own
// ADT, whose hooks are lowered by the knownHookTable.
func moduleIsKnown(mod gomast.GomModule) bool {
	if mod == nil {
		return false
	}
	name := moduleQualifiedName(mod)
	for i := range knownHookTable {
		if knownHookTable[i].module == name {
			return true
		}
	}
	return false
}

// trimHookBody is Java Gom's trimBracket of the hook code (the parser
// already removed the outer braces).
func trimHookBody(h *gomast.HookProduction) string {
	return strings.TrimSpace(hookBody(h))
}

// altSlotSorts returns the Gom sort names of the named slots of alt.
func altSlotSorts(alt *gomast.AlternativeAlternative) []string {
	var out []string
	for _, f := range alternativeFields(alt) {
		if _, ok := f.(*gomast.NamedFieldField); ok {
			out = append(out, fieldTypeName(f))
		}
	}
	return out
}

// altCodomain returns the sort of alt in mod.
func altCodomain(mod gomast.GomModule, alt *gomast.AlternativeAlternative) string {
	for _, prod := range moduleSorts(mod) {
		for _, a := range sortAlternatives(prod) {
			if a == alt {
				return sortName(prod)
			}
		}
	}
	return ""
}

// sameSortVariadic reports whether alt is a variadic operator whose
// domain is its codomain (`T = conc(T*)`), the operators a theory
// applies to.
func sameSortVariadic(mod gomast.GomModule, alt *gomast.AlternativeAlternative) bool {
	return alternativeIsVariadic(alt) && fieldTypeName(alternativeFields(alt)[0]) == altCodomain(mod, alt)
}

// moduleRules parses the rules hooks of mod, in declaration order.
func moduleRules(mod gomast.GomModule) ([][]gomRule, error) {
	var out [][]gomRule
	for _, h := range moduleHooks(mod) {
		if hookScopeName(h) == "module" && hookKindName(h) == "rules" {
			rules, err := parseRules(hookBody(h))
			if err != nil {
				return nil, fmt.Errorf("module %s, rules hook: %w", moduleQualifiedName(mod), err)
			}
			out = append(out, rules)
		}
	}
	return out, nil
}

// realMakeName is the Go function inserting into a list of the variadic
// operator op without hooks (Java's Cons<op>.realMake).
func realMakeName(op string) string { return "RealMakeCons" + exportedField(op) }

// opHooks returns the construction hooks of alt, in the order Java Gom
// runs them. Rules that cannot be parsed are reported by CheckHooks.
func opHooks(mod gomast.GomModule, alt *gomast.AlternativeAlternative) []opHook {
	if mod == nil || moduleIsKnown(mod) {
		return nil
	}
	variadic := alternativeIsVariadic(alt)
	var out []opHook
	for _, h := range moduleHooks(mod) {
		switch {
		case hookScopeName(h) == "operator" && h.Name == alt.Name:
			kind := hookKindName(h)
			if (kind == "make" && !variadic) || ((kind == "make_insert" || kind == "make_empty") && variadic) {
				out = append(out, opHook{kind: kind, args: hookArgNames(h), body: hookBody(h)})
			}
		case hookScopeName(h) == "module" && hookKindName(h) == "rules":
			rules, err := parseRules(hookBody(h))
			if err != nil {
				continue
			}
			var mine []gomRule
			for _, r := range rules {
				if ruleHead(r) == alt.Name {
					mine = append(mine, r)
				}
			}
			if len(mine) == 0 {
				continue
			}
			if !variadic {
				sorts := altSlotSorts(alt)
				args := make([]string, len(sorts))
				for i := range args {
					args[i] = fmt.Sprintf("arg_%d", i+1)
				}
				out = append(out, opHook{kind: "make", args: args, body: ruleHookCode(sorts, mine)})
				continue
			}
			var insert []gomRule
			for _, r := range mine {
				if len(r.lhs.args) == 0 {
					out = append(out, opHook{kind: "make_empty", body: ruleHookCode(nil, []gomRule{r})})
				} else {
					insert = append(insert, r)
				}
			}
			if len(insert) > 0 {
				out = append(out, opHook{kind: "make_insert", args: []string{"head", "tail"}, body: ruleVariadicHookCode(realMakeName(alt.Name), insert)})
			}
		}
	}
	return out
}

// hooksOfKind filters hooks by kind.
func hooksOfKind(hooks []opHook, kind string) []opHook {
	var out []opHook
	for _, h := range hooks {
		if h.kind == kind {
			out = append(out, h)
		}
	}
	return out
}

// theoryOf returns the theory of a variadic alt (HookTypeExpander: an
// explicit FL/AU/ACU/Free hook, or FL by default for an operator whose
// domain is its codomain and that has no construction hook).
func theoryOf(mod gomast.GomModule, alt *gomast.AlternativeAlternative) opTheory {
	if mod == nil || moduleIsKnown(mod) || !alternativeIsVariadic(alt) {
		return opTheory{}
	}
	for _, h := range moduleHooks(mod) {
		if hookScopeName(h) != "operator" || h.Name != alt.Name {
			continue
		}
		switch k := hookKindName(h); k {
		case "FL", "Free":
			return opTheory{kind: k}
		case "AU", "ACU":
			return opTheory{kind: k, neutral: trimHookBody(h)}
		}
	}
	if sameSortVariadic(mod, alt) && len(opHooks(mod, alt)) == 0 {
		return opTheory{kind: "FL"}
	}
	return opTheory{}
}

// checkUserHooks validates the hooks of a user module (CheckHooks).
func checkUserHooks(mod gomast.GomModule) error {
	modName := moduleQualifiedName(mod)
	alts := map[string]*gomast.AlternativeAlternative{}
	sorts := map[string]bool{}
	for _, prod := range moduleSorts(mod) {
		sorts[sortName(prod)] = true
		for _, alt := range sortAlternatives(prod) {
			alts[alt.Name] = alt
		}
	}
	unsupported := func(h *gomast.HookProduction, why string) error {
		return fmt.Errorf("gomgo: hook %s %s:%s of module %s is not supported by the Go backend (%s)",
			hookScopeName(h), h.Name, hookKindName(h), modName, why)
	}
	if _, err := moduleRules(mod); err != nil {
		return err
	}
	for _, h := range moduleHooks(mod) {
		kind := hookKindName(h)
		switch kind {
		case "block", "javablock":
			continue
		case "import":
			if !onlyComments(hookBody(h)) {
				return unsupported(h, "Go imports belong to the importing file: an import hook must be empty")
			}
			continue
		}
		switch hookScopeName(h) {
		case "module":
			if kind == "rules" {
				continue
			}
		case "operator":
			alt, ok := alts[h.Name]
			if !ok {
				return unsupported(h, "unknown operator")
			}
			variadic := alternativeIsVariadic(alt)
			switch kind {
			case "make":
				if !variadic {
					continue
				}
			case "make_insert", "make_empty":
				if variadic {
					continue
				}
			case "FL", "Free", "AU", "ACU":
				if sameSortVariadic(mod, alt) {
					if err := checkTheoryOrder(mod, alt); err != nil {
						return unsupported(h, err.Error())
					}
					if kind == "AU" || kind == "ACU" {
						if _, err := neutralGo(mod, opTheory{kind: kind, neutral: trimHookBody(h)}); err != nil {
							return unsupported(h, err.Error())
						}
					}
					continue
				}
			}
		}
		return unsupported(h, "supported: make, make_insert, make_empty, rules, FL, AU, ACU, Free, block, empty import")
	}
	for _, prod := range moduleSorts(mod) {
		for _, alt := range sortAlternatives(prod) {
			if alternativeIsVariadic(alt) && sameSortVariadic(mod, alt) && len(opHooks(mod, alt)) > 0 && theoryOf(mod, alt).kind == "" {
				// Java Gom: mustSpecifyAssociatedTheoryForVarOp
				return fmt.Errorf("gomgo: the variadic operator %s of module %s has construction hooks but no theory (FL, AU, ACU or Free)", alt.Name, modName)
			}
		}
	}
	return nil
}

// checkTheoryOrder: the Go backend runs the theory of a variadic
// operator before its user hooks, which is Java Gom's order when the
// theory hook is declared first.
func checkTheoryOrder(mod gomast.GomModule, alt *gomast.AlternativeAlternative) error {
	seenUser := false
	for _, h := range moduleHooks(mod) {
		if hookScopeName(h) == "operator" && h.Name == alt.Name {
			switch hookKindName(h) {
			case "make_insert", "make_empty":
				seenUser = true
			case "FL", "Free", "AU", "ACU":
				if seenUser {
					return fmt.Errorf("the theory hook of %s must be declared before its make_insert/make_empty hooks", alt.Name)
				}
			}
		}
	}
	return nil
}

// onlyComments reports whether a hook body holds only blanks and Java/Go
// comments.
func onlyComments(body string) bool {
	for i := 0; i < len(body); {
		switch {
		case body[i] == ' ' || body[i] == '\t' || body[i] == '\n' || body[i] == '\r':
			i++
		case strings.HasPrefix(body[i:], "//"):
			for i < len(body) && body[i] != '\n' {
				i++
			}
		case strings.HasPrefix(body[i:], "/*"):
			end := strings.Index(body[i+2:], "*/")
			if end < 0 {
				return false
			}
			i += end + 4
		default:
			return false
		}
	}
	return true
}

// blockHooks returns the bodies of the block/javablock hooks of mod, in
// declaration order: host code, emitted into the mapping.
func blockHooks(mod gomast.GomModule) []string {
	if moduleIsKnown(mod) {
		return nil
	}
	var out []string
	for _, h := range moduleHooks(mod) {
		if k := hookKindName(h); k == "block" || k == "javablock" {
			out = append(out, hookBody(h))
		}
	}
	return out
}

// mappingHostCode prepares a hook body (Tom+Go code) for the mapping:
// Tom emits the host code of an included file on a single line, so a
// `// comment` would swallow the rest of the mapping; it becomes a
// `/* comment */`. String and rune literals are left alone (a Tom+Go
// body has no Go raw string: ` starts a Tom backquote term).
func mappingHostCode(body string) string {
	var b strings.Builder
	for i := 0; i < len(body); i++ {
		c := body[i]
		switch {
		case c == '"' || c == '\'':
			j := i + 1
			for j < len(body) && body[j] != c && body[j] != '\n' {
				if body[j] == '\\' {
					j++
				}
				j++
			}
			if j >= len(body) {
				j = len(body) - 1
			}
			b.WriteString(body[i : j+1])
			i = j
		case strings.HasPrefix(body[i:], "/*"):
			end := strings.Index(body[i+2:], "*/")
			if end < 0 {
				b.WriteString(body[i:])
				return b.String()
			}
			b.WriteString(body[i : i+2+end+2])
			i += 2 + end + 1
		case strings.HasPrefix(body[i:], "//"):
			j := i + 2
			for j < len(body) && body[j] != '\n' {
				j++
			}
			b.WriteString("/*" + strings.ReplaceAll(body[i+2:j], "*/", "* /") + " */")
			i = j - 1
		default:
			b.WriteByte(c)
		}
	}
	return b.String()
}
