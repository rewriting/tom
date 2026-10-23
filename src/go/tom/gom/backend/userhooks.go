package backend

// User construction hooks of a Gom module, after Java Gom's
// HookTypeExpander and OperatorTemplate/VariadicOperatorTemplate:
//
//   - `op:make(args) {…}` on a non-variadic operator, and the rules of
//     `module M:rules() {…}` whose left-hand side it heads
//     (RuleExpander; the rules of every module generated into the same
//     Go package apply), run in declaration order at the head of its
//     make;
//   - `op:make_insert(e,l) {…}`, `op:make_empty() {…}` and the rules of a
//     variadic operator run at the head of Cons<op>.make / Empty<op>.make;
//   - the theory of a variadic operator whose domain is its codomain —
//     `op:FL()`, `op:AC()`, `op:AU() { neutral }`, `op:ACU() { neutral }`,
//     `op:Free()`, or FL by default when it has no construction hook —
//     normalises the lists (flattening, neutral element, ordering);
//   - `block` hooks (any scope) are host code that may hold Tom code
//     (Java Gom: HasTomCode true), emitted into the mapping;
//   - `javablock` hooks (any scope) are plain Go declarations without
//     Tom code (Java Gom's internal hook kind for a block without Tom
//     code: HasTomCode false, the class is not compiled by Tom; the Gom
//     grammar of the Java compiler does not accept it in a source, gomgo
//     does), emitted into the generated Go file; a method that a sort
//     javablock declares on every alternative of the sort is added to
//     the sort's interface (Java: a method of the sort class);
//   - `import` hooks must be empty in Go (imports belong to the
//     importing Go file).
//
// The construction hook bodies are Tom+Go code, emitted into the Tom
// mapping (see emitUserMakeHooksGo); the theories are generated in Go.

import (
	"fmt"
	"go/ast"
	"go/parser"
	"go/token"
	"sort"
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
	kind    string // "", "FL", "Free", "AC", "AU", "ACU"
	neutral string // the trimmed body of an AU/ACU hook (the neutral element), or ""
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

// opHooks returns the construction hooks of alt, an operator of mod, in
// the order Java Gom runs them: its operator hooks (declared in mod) and
// the rules, of any module of pkg, whose left-hand side it heads. Rules
// that cannot be parsed are reported by CheckHooks.
func opHooks(pkg []gomast.GomModule, mod gomast.GomModule, alt *gomast.AlternativeAlternative) []opHook {
	if mod == nil {
		return nil
	}
	var hooks []*gomast.HookProduction
	for _, m := range pkg {
		for _, h := range moduleHooks(m) {
			if m == mod || (hookScopeName(h) == "module" && hookKindName(h) == "rules") {
				hooks = append(hooks, h)
			}
		}
	}
	if len(pkg) == 0 {
		hooks = moduleHooks(mod)
	}
	variadic := alternativeIsVariadic(alt)
	var out []opHook
	for _, h := range hooks {
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
// explicit FL/AC/AU/ACU/Free hook, or FL by default for an operator whose
// domain is its codomain and that has no construction hook).
func theoryOf(pkg []gomast.GomModule, mod gomast.GomModule, alt *gomast.AlternativeAlternative) opTheory {
	if mod == nil || !alternativeIsVariadic(alt) {
		return opTheory{}
	}
	for _, h := range moduleHooks(mod) {
		if hookScopeName(h) != "operator" || h.Name != alt.Name {
			continue
		}
		switch k := hookKindName(h); k {
		case "FL", "Free", "AC":
			return opTheory{kind: k}
		case "AU", "ACU":
			return opTheory{kind: k, neutral: trimHookBody(h)}
		}
	}
	if sameSortVariadic(mod, alt) && len(opHooks(pkg, mod, alt)) == 0 {
		return opTheory{kind: "FL"}
	}
	return opTheory{}
}

// checkUserHooks validates the hooks of the module mod of the package
// whose modules are pkg (CheckHooks).
func checkUserHooks(pkg []gomast.GomModule, mod gomast.GomModule) error {
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
		case "block":
			continue
		case "javablock":
			if _, err := parseGoDecls(hookBody(h)); err != nil {
				return unsupported(h, "a javablock holds Go declarations without Tom code: "+err.Error())
			}
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
			case "FL", "Free", "AC", "AU", "ACU":
				if sameSortVariadic(mod, alt) {
					if err := checkTheoryOrder(pkg, mod, alt); err != nil {
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
		return unsupported(h, "supported: make, make_insert, make_empty, rules, FL, AC, AU, ACU, Free, block, empty import")
	}
	for _, prod := range moduleSorts(mod) {
		for _, alt := range sortAlternatives(prod) {
			if alternativeIsVariadic(alt) && sameSortVariadic(mod, alt) && len(opHooks(pkg, mod, alt)) > 0 && theoryOf(pkg, mod, alt).kind == "" {
				// Java Gom: mustSpecifyAssociatedTheoryForVarOp
				return fmt.Errorf("gomgo: the variadic operator %s of module %s has construction hooks but no theory (FL, AC, AU, ACU or Free)", alt.Name, modName)
			}
		}
	}
	return nil
}

// checkTheoryOrder: Java Gom runs the hooks of a variadic operator in
// declaration order (checked with legacy/'s Gom on a module mixing a
// theory, a make_insert hook and rules); the Go backend runs the theory
// (generated in Go) before the user hooks and the rules (Tom code of the
// mapping), which is Java's order only when the theory hook is declared
// before them. Another order is refused rather than silently changed.
func checkTheoryOrder(pkg []gomast.GomModule, mod gomast.GomModule, alt *gomast.AlternativeAlternative) error {
	seenUser := false
	var hooks []*gomast.HookProduction
	for _, m := range pkg {
		for _, h := range moduleHooks(m) {
			if m == mod || (hookScopeName(h) == "module" && hookKindName(h) == "rules") {
				hooks = append(hooks, h)
			}
		}
	}
	for _, h := range hooks {
		if hookScopeName(h) == "module" && hookKindName(h) == "rules" {
			if rules, err := parseRules(hookBody(h)); err == nil {
				for _, r := range rules {
					if ruleHead(r) == alt.Name {
						seenUser = true
					}
				}
			}
			continue
		}
		if hookScopeName(h) == "operator" && h.Name == alt.Name {
			switch hookKindName(h) {
			case "make_insert", "make_empty":
				seenUser = true
			case "FL", "Free", "AC", "AU", "ACU":
				if seenUser {
					return fmt.Errorf("the theory hook of %s must be declared before its make_insert/make_empty hooks and the rules of %s (Java Gom runs them in declaration order, the Go backend runs the theory first)", alt.Name, alt.Name)
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

// blockHooks returns the bodies of the block hooks of mod, in
// declaration order: host code (Tom+Go), emitted into the mapping.
func blockHooks(mod gomast.GomModule) []string {
	var out []string
	for _, h := range moduleHooks(mod) {
		if hookKindName(h) == "block" {
			out = append(out, hookBody(h))
		}
	}
	return out
}

// parseGoDecls parses the body of a javablock hook: Go declarations.
func parseGoDecls(body string) (*ast.File, error) {
	return parser.ParseFile(token.NewFileSet(), "javablock.go", "package p\n"+body, parser.SkipObjectResolution)
}

// emitJavaBlocks writes the javablock hooks of the module (Go
// declarations, in declaration order) at the end of its Go file.
func (g *gen) emitJavaBlocks() {
	for _, h := range moduleHooks(g.mod) {
		if hookKindName(h) != "javablock" {
			continue
		}
		fmt.Fprintf(&g.buf, "// javablock hook %s %s (Gom module %s)\n", hookScopeName(h), h.Name, moduleQualifiedName(g.mod))
		fmt.Fprintf(&g.buf, "%s\n\n", strings.TrimSpace(hookBody(h)))
	}
}

// javaBlockSortMethods returns the method specifications (`Name(params)
// results`, sorted by name) that the javablock hooks of the sort prod
// declare on every alternative of the sort: Java Gom puts the code of a
// sort hook in the class of the sort, so its methods apply to every term
// of the sort; in Go they are added to the sort's interface.
func (g *gen) javaBlockSortMethods(prod *gomast.SortTypeProduction) []string {
	sortNm := sortName(prod)
	alts := map[string]bool{}
	for _, alt := range sortAlternatives(prod) {
		opGo := exportedField(alt.Name)
		if opGo == "" {
			opGo = "Op"
		}
		alts[opGo+sortNm] = true
	}
	// method name -> receiver struct -> specification
	declared := map[string]map[string]string{}
	for _, h := range moduleHooks(g.mod) {
		if hookKindName(h) != "javablock" || hookScopeName(h) != "sort" || h.Name != sortNm {
			continue
		}
		body := "package p\n" + hookBody(h)
		fset := token.NewFileSet()
		f, err := parser.ParseFile(fset, "javablock.go", body, parser.SkipObjectResolution)
		if err != nil {
			continue // reported by CheckHooks
		}
		for _, d := range f.Decls {
			fd, ok := d.(*ast.FuncDecl)
			if !ok || fd.Recv == nil || len(fd.Recv.List) != 1 {
				continue
			}
			star, ok := fd.Recv.List[0].Type.(*ast.StarExpr)
			if !ok {
				continue
			}
			recv, ok := star.X.(*ast.Ident)
			if !ok || !alts[recv.Name] {
				continue
			}
			sig := body[fset.Position(fd.Type.Params.Pos()).Offset:fset.Position(fd.Type.End()).Offset]
			if declared[fd.Name.Name] == nil {
				declared[fd.Name.Name] = map[string]string{}
			}
			declared[fd.Name.Name][recv.Name] = fd.Name.Name + sig
		}
	}
	var out []string
	for _, byRecv := range declared {
		if len(byRecv) != len(alts) {
			continue
		}
		spec := ""
		same := true
		for _, sp := range byRecv {
			if spec != "" && sp != spec {
				same = false
			}
			spec = sp
		}
		if same {
			out = append(out, spec)
		}
	}
	sort.Strings(out)
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
