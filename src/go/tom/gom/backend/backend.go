// Package backend turns a parsed Gom module (a gomast.GomModule) into
// Go code whose term constructors run through the shared-objects runtime
// (see package library/sharedobjects), with the Java Gom API and list
// semantics (javaapi.go, cells.go), and into a Tom mapping (.tom) that
// declares the module's sorts and operators to Tom and holds the hooks
// written in Tom+Go (userhooks.go, rules.go).
//
// Every module goes through the same generation: one module alone
// (Generate, GenerateToDir: gomgo gom), a module and the modules it
// imports (gomgo gom-block), or several modules compiled into one Go
// package (GenerateBatchToDir: gomgo gom-batch, used for the compiler's
// own ADTs). The slots of a sort of another module of the same package
// (Options.ImportedSorts) are typed by its Go interface; a sort of a
// module generated separately is rendered as Go `any`.
package backend

import (
	"bytes"
	"fmt"
	"go/format"
	"io"
	"os"
	"path/filepath"
	"sort"
	"strings"
	"unicode"

	gomast "tom/gom/adt"
)

// Options controls code generation. Only the Go package name has a
// non-trivial default — by default it is derived from the last segment
// of the Gom module name (lower-cased, scrubbed of non-letters).
type Options struct {
	PackageName string // overrides the auto-derived package name
	// WithCongruenceStrategies adds to the Tom mapping the congruence
	// strategy operators `_<op>`, `When_<op>` and `Is_<op>` (Java Gom's
	// --withCongruenceStrategies, StratMappingTemplate).
	WithCongruenceStrategies bool
	// ImportedSorts are the sorts of the other user modules generated in
	// the same Go package (the modules a module imports, Java Gom's
	// `imports M`): slots of these sorts are typed by their Go
	// interface instead of `any`.
	ImportedSorts map[string]bool
	// NoSharing is Java Gom's --nosharing: terms are not maximally
	// shared (each Make<Op> allocates a new term), they are compared by
	// DeepEquals (a method of every sort) instead of ==, and the
	// mapping's equals uses DeepEquals.
	NoSharing bool
	// GoHooks (gomgo --gohooks): the hooks of the module are written in
	// plain Go, without Tom code, and are generated into its Go file
	// instead of the Tom mapping (see userhooks.go), so that the package
	// works without Tom; the rules are then refused.
	GoHooks bool
	// PackageModules are all the modules generated into the same Go
	// package, the generated one included (none: the module alone). The
	// rules of each of them apply to the operators of all of them, as
	// Java Gom's RuleExpander applies the rules of a module to the
	// operators of the whole module list (e.g. a rule of Code.gom whose
	// left-hand side is an operator of TomInstruction.gom).
	PackageModules []gomast.GomModule
}

// opNames tells apart the operators of a Go package whose Go names
// (exportedField) are the same, such as subterm (Il.gom) and Subterm
// (Code.gom): Java Gom puts them in different Java packages (one per
// sort); in Go, the identifiers derived from such an operator
// (Make<Op>, Cons<Op>, Hook<Make><Op>, Is<Op>, Visit<Op>, …) are
// suffixed with its sort. The term types <Op><Sort> are distinct anyway.
type opNames map[string]bool // Go names shared by several operators

// newOpNames returns the shared Go names of the operators of pkg.
func newOpNames(pkg []gomast.GomModule) opNames {
	count := map[string]int{}
	for _, m := range pkg {
		for _, prod := range moduleSorts(m) {
			for _, alt := range sortAlternatives(prod) {
				count[opBase(alt)]++
			}
		}
	}
	n := opNames{}
	for name, c := range count {
		if c > 1 {
			n[name] = true
		}
	}
	return n
}

// opBase is the exported Go name of the operator.
func opBase(alt *gomast.AlternativeAlternative) string {
	if op := exportedField(alt.Name); op != "" {
		return op
	}
	return "Op"
}

// of returns the Go identifier stem of the operator alt of sort sortNm.
func (n opNames) of(alt *gomast.AlternativeAlternative, sortNm string) string {
	if op := opBase(alt); n[op] {
		return op + sortNm
	} else {
		return op
	}
}

// packageModules returns opts.PackageModules, or mod alone.
func (opts Options) packageModules(mod gomast.GomModule) []gomast.GomModule {
	if len(opts.PackageModules) > 0 {
		return opts.PackageModules
	}
	return []gomast.GomModule{mod}
}

// ImportedUserModules returns the names of the non-builtin modules g
// imports (`imports Leaf` → "Leaf"), in declaration order.
func ImportedUserModules(g gomast.GomModule) []string {
	builtin := map[string]bool{"ATerm": true, "ATermList": true}
	for _, b := range builtinMappings {
		builtin[b.module] = true
	}
	var out []string
	gm := g.(*gomast.GomModuleGomModule)
	for _, sec := range elements[gomast.Section](gm.SectionList.(*gomast.ConcSectionSectionList)) {
		imp, ok := sec.(*gomast.ImportsSection)
		if !ok {
			continue
		}
		if l, ok := imp.ImportList.(*gomast.ConcImportedModuleImportList); ok {
			for _, m := range elements[gomast.GomModuleName](l) {
				if n, ok := m.(*gomast.GomModuleNameGomModuleName); ok && !builtin[n.Name] {
					out = append(out, n.Name)
				}
			}
		}
	}
	return out
}

// SortNames returns the names of the sorts declared by g.
func SortNames(g gomast.GomModule) []string {
	var out []string
	for _, prod := range moduleSorts(g) {
		out = append(out, sortName(prod))
	}
	return out
}

// GenerateGenerated is Generate for one module of a package that holds
// several modules (an importing module and the modules it imports):
// withFactory says whether this file declares the package's shared
// factory (exactly one file must).
func GenerateGenerated(mod gomast.GomModule, opts Options, withFactory bool, out io.Writer) error {
	src, err := generateBytesFactory(mod, opts, withFactory)
	if err != nil {
		return err
	}
	_, err = out.Write(src)
	return err
}

// ---------------------------------------------------------------------------
// gomast accessors
// ---------------------------------------------------------------------------

// QualifiedName is the exported alias for [moduleQualifiedName] —
// useful to scripts and tools (cmd/gentom) that need the module's
// dotted name without re-deriving from gomast.
func QualifiedName(g gomast.GomModule) string {
	return moduleQualifiedName(g)
}

// moduleQualifiedName returns the dotted module name carried by g.
func moduleQualifiedName(g gomast.GomModule) string {
	gm := g.(*gomast.GomModuleGomModule)
	return gm.ModuleName.(*gomast.GomModuleNameGomModuleName).Name
}

// moduleNameParts splits the qualified name on dots.
func moduleNameParts(g gomast.GomModule) []string {
	return strings.Split(moduleQualifiedName(g), ".")
}

// moduleSorts returns the SortType productions of g in declaration order.
func moduleSorts(g gomast.GomModule) []*gomast.SortTypeProduction {
	var out []*gomast.SortTypeProduction
	walkProductions(g, func(p gomast.Production) {
		if s, ok := p.(*gomast.SortTypeProduction); ok {
			out = append(out, s)
		}
	})
	return out
}

// moduleHooks returns the Hook productions of g in declaration order.
func moduleHooks(g gomast.GomModule) []*gomast.HookProduction {
	var out []*gomast.HookProduction
	walkProductions(g, func(p gomast.Production) {
		if h, ok := p.(*gomast.HookProduction); ok {
			out = append(out, h)
		}
	})
	return out
}

func walkProductions(g gomast.GomModule, fn func(gomast.Production)) {
	gm := g.(*gomast.GomModuleGomModule)
	sectionList := gm.SectionList.(*gomast.ConcSectionSectionList)
	for _, sec := range elements[gomast.Section](sectionList) {
		pub, ok := sec.(*gomast.PublicSection)
		if !ok {
			continue
		}
		prodList := pub.ProductionList.(*gomast.ConcProductionProductionList)
		for _, p := range elements[gomast.Production](prodList) {
			fn(p)
		}
	}
}

// elements returns the elements of a Gom list term, typed (Java Gom's
// toArray: the lists are cons cells).
func elements[T any](l interface{ ToArray() []any }) []T {
	arr := l.ToArray()
	out := make([]T, len(arr))
	for i, x := range arr {
		out[i] = x.(T)
	}
	return out
}

// sortName returns the name of the sort declared by a SortType production.
func sortName(prod *gomast.SortTypeProduction) string {
	return prod.Type.(*gomast.GomTypeGomType).Name
}

// sortAlternatives returns the alternatives of a SortType production
// in declaration order.
func sortAlternatives(prod *gomast.SortTypeProduction) []*gomast.AlternativeAlternative {
	return elements[*gomast.AlternativeAlternative](prod.AlternativeList.(*gomast.ConcAlternativeAlternativeList))
}

// alternativeFields returns the fields of an alternative.
func alternativeFields(alt *gomast.AlternativeAlternative) []gomast.Field {
	return elements[gomast.Field](alt.DomainList.(*gomast.ConcFieldFieldList))
}

// fieldTypeName returns the GomType name carried by a Field — works
// for both named and variadic fields.
func fieldTypeName(f gomast.Field) string {
	switch fld := f.(type) {
	case *gomast.NamedFieldField:
		return fld.FieldType.(*gomast.GomTypeGomType).Name
	case *gomast.StarredFieldField:
		return fld.FieldType.(*gomast.GomTypeGomType).Name
	}
	return ""
}

// alternativeIsVariadic reports whether the alternative is variadic
// (its single field is a StarredField).
func alternativeIsVariadic(alt *gomast.AlternativeAlternative) bool {
	fields := alternativeFields(alt)
	if len(fields) != 1 {
		return false
	}
	_, ok := fields[0].(*gomast.StarredFieldField)
	return ok
}

// hookScopeName returns "sort"/"module"/"operator" depending on the
// HookProduction.NameType.
func hookScopeName(h *gomast.HookProduction) string {
	switch h.NameType.(type) {
	case *gomast.KindSortIdKind:
		return "sort"
	case *gomast.KindModuleIdKind:
		return "module"
	case *gomast.KindOperatorIdKind:
		return "operator"
	}
	return ""
}

// hookKindName returns the kind ("block", "make", …) of a HookProduction.
func hookKindName(h *gomast.HookProduction) string {
	return h.HookType.(*gomast.HookKindHookKind).Kind
}

// hookBody returns the hook's source body (the textual code between the
// outer braces) when the hook content is the HookCode variant.
func hookBody(h *gomast.HookProduction) string {
	if hc, ok := h.HookContent.(*gomast.HookCodeHookContent); ok {
		return hc.StringCode
	}
	return ""
}

// ---------------------------------------------------------------------------
// public entry points
// ---------------------------------------------------------------------------

// Generate writes the Go source for module mod to out. The output is
// already gofmt'd.
func Generate(mod gomast.GomModule, opts Options, out io.Writer) error {
	src, err := generateBytes(mod, opts)
	if err != nil {
		return err
	}
	_, err = out.Write(src)
	return err
}

// GenerateToDir creates dir if needed, writes go.mod and a single
// `<pkg>.go` source file, and returns the absolute package directory.
// Each generated module is a standalone Go module so it can be built
// and tested in isolation.
func GenerateToDir(mod gomast.GomModule, opts Options, dir string) (string, error) {
	tomRoot, err := locateTomModuleRoot()
	if err != nil {
		return "", err
	}
	if err := os.MkdirAll(dir, 0o755); err != nil {
		return "", err
	}
	if opts.PackageName == "" {
		opts.PackageName = defaultPackageName(mod)
	}
	src, err := generateBytes(mod, opts)
	if err != nil {
		return "", err
	}
	abs, err := filepath.Abs(dir)
	if err != nil {
		return "", err
	}
	if err := os.WriteFile(filepath.Join(abs, opts.PackageName+".go"), src, 0o644); err != nil {
		return "", err
	}
	goMod := fmt.Sprintf("module tomgen/%s\n\ngo 1.26\n\nrequire tom v0.0.0\nreplace tom => %s\n",
		opts.PackageName, tomRoot)
	if err := os.WriteFile(filepath.Join(abs, "go.mod"), []byte(goMod), 0o644); err != nil {
		return "", err
	}
	// Tom mapping (.tom) — other .t fixtures `%include` it to pick up
	// the sort/operator declarations defined by this Gom module.
	tomFile := EmitTomMapping(mod, opts)
	if err := os.WriteFile(filepath.Join(abs, opts.PackageName+".tom"), tomFile, 0o644); err != nil {
		return "", err
	}
	return abs, nil
}

// EmitTomMapping returns the Tom-mapping (.tom) source corresponding
// to a single Gom module. Other .t files import this via
// `%include { <pkg>.tom }` to learn about the module's sorts and
// operators. Per the Java gom reference, each sort gets one
// `%typeterm` declaration with implement/is_sort/equals hooks, each
// non-variadic alternative gets one `%op` with is_fsym/get_slot/make
// hooks, and each variadic alternative gets one `%oplist` with
// is_fsym/make_empty/make_insert/get_head/get_tail/is_empty hooks.
//
// The hook bodies reference Java types via the qualified path
// `<pkg>.types.<Sort>` (matching what `tom.gom.Gom` emits when
// compiling the same .gom file to Java). The Tom compiler later
// reads those bodies as opaque host-language code; only the
// signatures matter for type-checking client .t files.
func EmitTomMapping(mod gomast.GomModule, opts Options) []byte {
	if opts.PackageName == "" {
		opts.PackageName = defaultPackageName(mod)
	}
	pkg := opts.PackageName
	var buf bytes.Buffer
	for _, prod := range moduleSorts(mod) {
		sortNm := sortName(prod)
		jClass := pkg + ".types." + sortNm
		fmt.Fprintf(&buf, "%%typeterm %s {\n", sortNm)
		fmt.Fprintf(&buf, "  implement      { %s }\n", jClass)
		fmt.Fprintf(&buf, "  is_sort(t)     { ($t instanceof %s) }\n", jClass)
		fmt.Fprintf(&buf, "  equals(t1,t2)  { ($t1.equals($t2)) }\n")
		fmt.Fprintf(&buf, "}\n\n")
	}
	for _, prod := range moduleSorts(mod) {
		sortNm := sortName(prod)
		jSortClass := pkg + ".types." + sortNm
		for _, alt := range sortAlternatives(prod) {
			altNm := alt.Name
			jAltClass := pkg + ".types." + strings.ToLower(sortNm) + "." + altNm
			if alternativeIsVariadic(alt) {
				typeName := fieldTypeName(alternativeFields(alt)[0])
				fmt.Fprintf(&buf, "%%oplist %s %s( %s* ) {\n", sortNm, altNm, typeName)
				fmt.Fprintf(&buf, "  is_fsym(t)       { ($t instanceof %s) }\n", jSortClass)
				fmt.Fprintf(&buf, "  make_empty()     { new %s.Empty%s() }\n", jSortClass, altNm)
				fmt.Fprintf(&buf, "  make_insert(e,l) { new %s.Cons%s($e, $l) }\n", jSortClass, altNm)
				fmt.Fprintf(&buf, "  get_head(l)      { ((%s.Cons%s)$l).getHead%s() }\n", jSortClass, altNm, altNm)
				fmt.Fprintf(&buf, "  get_tail(l)      { ((%s.Cons%s)$l).getTail%s() }\n", jSortClass, altNm, altNm)
				fmt.Fprintf(&buf, "  is_empty(l)      { ($l instanceof %s.Empty%s) }\n", jSortClass, altNm)
				fmt.Fprintf(&buf, "}\n\n")
				continue
			}
			fields := alternativeFields(alt)
			slotsParts := make([]string, 0, len(fields))
			slotNames := make([]string, 0, len(fields))
			slotTypes := make([]string, 0, len(fields))
			for i, f := range fields {
				nf, ok := f.(*gomast.NamedFieldField)
				if !ok {
					continue
				}
				typeName := fieldTypeName(f)
				slotsParts = append(slotsParts, fmt.Sprintf("%s:%s", nf.Name, typeName))
				slotNames = append(slotNames, nf.Name)
				slotTypes = append(slotTypes, typeName)
				_ = i
			}
			fmt.Fprintf(&buf, "%%op %s %s(%s) {\n", sortNm, altNm, strings.Join(slotsParts, ", "))
			fmt.Fprintf(&buf, "  is_fsym(t)     { ($t != null) && ($t instanceof %s) }\n", jAltClass)
			for _, sn := range slotNames {
				fmt.Fprintf(&buf, "  get_slot(%s,t) { ((%s)$t).get%s() }\n", sn, jAltClass, exportedField(sn))
			}
			makeArgs := make([]string, len(slotNames))
			for i, sn := range slotNames {
				makeArgs[i] = "$" + sn
				_ = i
			}
			fmt.Fprintf(&buf, "  make(%s)       { new %s(%s) }\n",
				strings.Join(slotNames, ","),
				jAltClass, strings.Join(makeArgs, ","))
			fmt.Fprintf(&buf, "}\n\n")
		}
	}
	return buf.Bytes()
}

// builtinMappings lists, in the order Java Gom emits them
// (tom.gom.backend.shared.MappingTemplate), the builtin modules a Gom
// module may import and the Tom mapping that declares their sort.
var builtinMappings = []struct{ module, mapping string }{
	{"boolean", "boolean.tom"},
	{"String", "string.tom"},
	{"int", "int.tom"},
	{"char", "char.tom"},
	{"double", "double.tom"},
	{"long", "long.tom"},
	{"float", "float.tom"},
	{"ATerm", "aterm.tom"},
	{"ATermList", "aterm.tom"},
}

// importedModules returns the names of the modules imported by g.
func importedModules(g gomast.GomModule) map[string]bool {
	out := map[string]bool{}
	gm := g.(*gomast.GomModuleGomModule)
	for _, sec := range elements[gomast.Section](gm.SectionList.(*gomast.ConcSectionSectionList)) {
		imp, ok := sec.(*gomast.ImportsSection)
		if !ok {
			continue
		}
		if l, ok := imp.ImportList.(*gomast.ConcImportedModuleImportList); ok {
			for _, m := range elements[gomast.GomModuleName](l) {
				if n, ok := m.(*gomast.GomModuleNameGomModuleName); ok {
					out[n.Name] = true
				}
			}
		}
	}
	return out
}

// BuiltinIncludesGo returns the %include lines a mapping of g needs for
// the builtin modules it imports (int.tom for "imports int", …), as Java
// Gom writes them at the top of its generated mapping.
func BuiltinIncludesGo(g gomast.GomModule, more ...gomast.GomModule) []byte {
	var buf bytes.Buffer
	imported := importedModules(g)
	for _, m := range more {
		for name := range importedModules(m) {
			imported[name] = true
		}
	}
	included := map[string]bool{}
	for _, b := range builtinMappings {
		if imported[b.module] && !included[b.mapping] {
			included[b.mapping] = true
			fmt.Fprintf(&buf, "%%include { %s }\n", b.mapping)
		}
	}
	if buf.Len() > 0 {
		buf.WriteString("\n")
	}
	return buf.Bytes()
}

// EmitTomMappingGo returns the Tom-mapping (.tom) source for a Gom
// module, with hook bodies written in **Go** syntax (mirror of
// [EmitTomMapping] which targets Java).
//
// Hook bodies reference the generated Go types directly:
//
//   - `implement` uses the sort interface (`L`, `E`, `T1`, …).
//   - `is_sort` / `is_fsym` use IIFE-wrapped type assertions
//     (`func() bool { _, ok := ($t).(*<Alt><Sort>); return ok }()`).
//     Go's `t.(*X)` is a statement, not an expression, so we wrap it
//     in a no-arg closure so the Tom backend's single-expression
//     hook emit holds.
//   - `make` / `get_slot` use the alt struct's factory + field
//     access (`MakeA()`, `($t).(*FT1).X`).
//
// Variadic alternatives emit Java Gom's `Empty<op>`/`Cons<op>`
// operators and a `%oplist` on the cons cells (emitVariadicMappingGo:
// O(1) get_head/get_tail/make_insert).
//
// Generated alt struct names follow the gom backend's existing
// scheme: `<ExportedAlt><Sort>` (e.g. `AT1` for alt `a` of sort
// `T1`). Slot field names are `exportedField(slotName)`.
func EmitTomMappingGo(mod gomast.GomModule, opts Options) []byte {
	var buf bytes.Buffer
	names := newOpNames(opts.packageModules(mod))
	for _, prod := range moduleSorts(mod) {
		sortNm := sortName(prod)
		fmt.Fprintf(&buf, "%%typeterm %s {\n", sortNm)
		fmt.Fprintf(&buf, "  implement      { %s }\n", sortNm)
		fmt.Fprintf(&buf, "  is_sort(t)     { func() bool { _, ok := ($t).(%s); return ok }() }\n", sortNm)
		if opts.NoSharing {
			// --nosharing: Java Gom's equals is deepEquals
			fmt.Fprintf(&buf, "  equals(t1,t2)  { ($t1).DeepEquals($t2) }\n")
		} else {
			fmt.Fprintf(&buf, "  equals(t1,t2)  { ($t1).Equivalent($t2) }\n")
		}
		fmt.Fprintf(&buf, "}\n\n")
	}
	for _, prod := range moduleSorts(mod) {
		sortNm := sortName(prod)
		for _, alt := range sortAlternatives(prod) {
			altNm := alt.Name
			structName := exportedField(altNm) + sortNm
			if alternativeIsVariadic(alt) {
				emitVariadicMappingGo(&buf, sortNm, alt, structName, names.of(alt, sortNm), fieldTypeName(alternativeFields(alt)[0]))
				continue
			}
			fields := alternativeFields(alt)
			slotsParts := make([]string, 0, len(fields))
			slotNames := make([]string, 0, len(fields))
			for _, f := range fields {
				nf, ok := f.(*gomast.NamedFieldField)
				if !ok {
					continue
				}
				typeName := fieldTypeName(f)
				slotsParts = append(slotsParts, fmt.Sprintf("%s:%s", nf.Name, typeName))
				slotNames = append(slotNames, nf.Name)
			}
			fmt.Fprintf(&buf, "%%op %s %s(%s) {\n", sortNm, altNm, strings.Join(slotsParts, ", "))
			fmt.Fprintf(&buf, "  is_fsym(t)     { func() bool { _, ok := ($t).(*%s); return ok }() }\n", structName)
			for _, sn := range slotNames {
				fmt.Fprintf(&buf, "  get_slot(%s,t) { ($t).(*%s).%s }\n", sn, structName, exportedField(sn))
			}
			makeArgs := make([]string, len(slotNames))
			for i, sn := range slotNames {
				makeArgs[i] = "$" + sn
			}
			fmt.Fprintf(&buf, "  make(%s)       { Make%s(%s) }\n",
				strings.Join(slotNames, ","),
				names.of(alt, sortNm), strings.Join(makeArgs, ","))
			fmt.Fprintf(&buf, "}\n\n")
		}
	}
	if !opts.GoHooks {
		emitUserMakeHooksGo(&buf, mod, opts.packageModules(mod), opts.ImportedSorts)
	}
	if opts.WithCongruenceStrategies {
		emitCongruenceMappingGo(&buf, mod, names)
	}
	return buf.Bytes()
}

// CheckHooks reports, as an error naming the hook and the module, a hook
// the Go backend cannot lower: instead of silently dropping it (and
// generating terms that do not behave as Java Gom's), gomgo refuses the
// module (see checkUserHooks for the supported hooks).
func CheckHooks(mod gomast.GomModule) error {
	return checkUserHooks([]gomast.GomModule{mod}, mod, false)
}

// hookArgNames returns the argument names of a hook (`make(i, j)`).
func hookArgNames(h *gomast.HookProduction) []string {
	var names []string
	if args, ok := h.Args.(*gomast.ConcArgArgList); ok {
		for _, a := range elements[gomast.Arg](args) {
			if aa, ok := a.(*gomast.ArgArg); ok {
				names = append(names, aa.Name)
			}
		}
	}
	return names
}

// emitUserMakeHooksGo writes, into the Tom mapping, the user
// construction hooks of the module (see userhooks.go) and its block
// hooks. Java Gom (OperatorTemplate.generateConstructor) puts the bodies
// of all the make hooks of an operator, in order, at the head of its
// make, each in its own block: a hook that returns gives the result;
// otherwise the arguments, which the hook may have reassigned, go on to
// the next hook (renamed when the next hook names them differently:
// `int j = i;`) and finally to realMake. Likewise for the make_insert
// (Cons<op>.make(head,tail)) and make_empty (Empty<op>.make()) hooks of
// a variadic operator.
//
// The bodies are Tom+Go code (they may use %match and backquotes on the
// module's operators), so they cannot go into the generated Go file:
// they are emitted as host code of the mapping, in an init function that
// sets the exported Hook<Make> variable the smart constructor consults.
// The function returns the hooks' result (nil when no hook returned)
// and the final values of the arguments:
//
//	HookMakeInt = func(i int64) (Wrapper, int64) {
//	  var j int64;
//	  tom__r := func() Wrapper { { i = i+1; }; j = i; { j = j*2; }; return nil; }();
//	  return tom__r, j;
//	};
//
// The mapping must be included by the Tom files that build terms of the
// module (the generated package itself does not know the hooks): this
// is the case of a %gom block, and of the program that includes the
// mapping of a standalone .gom (its package dot-imports the generated
// one, whose Hook<Make> variables are exported for that reason).
func emitUserMakeHooksGo(buf *bytes.Buffer, mod gomast.GomModule, pkg []gomast.GomModule, imported map[string]bool) {
	types := &gen{mod: mod, ownSort: map[string]bool{}}
	for _, prod := range moduleSorts(mod) {
		types.ownSort[sortName(prod)] = true
	}
	for s := range imported {
		types.ownSort[s] = true
	}
	for _, prod := range moduleSorts(mod) {
		sortNm := sortName(prod)
		for _, alt := range sortAlternatives(prod) {
			hooks := opHooks(pkg, mod, alt)
			opGo := newOpNames(pkg).of(alt, sortNm)
			if !alternativeIsVariadic(alt) {
				var slotTypes []string
				for _, f := range alternativeFields(alt) {
					if _, ok := f.(*gomast.NamedFieldField); ok {
						slotTypes = append(slotTypes, types.goType(fieldTypeName(f), false))
					}
				}
				emitHookFunc(buf, "HookMake"+opGo, sortNm, slotTypes, hooksOfKind(hooks, "make"), mappingHostCode)
				continue
			}
			elem := types.goType(fieldTypeName(alternativeFields(alt)[0]), false)
			emitHookFunc(buf, "HookMakeInsert"+opGo, sortNm, []string{elem, sortNm}, hooksOfKind(hooks, "make_insert"), mappingHostCode)
			emitHookFunc(buf, "HookMakeEmpty"+opGo, sortNm, nil, hooksOfKind(hooks, "make_empty"), mappingHostCode)
		}
	}
	// block hooks: host code (Go declarations) of the module
	for _, b := range blockHooks(mod) {
		fmt.Fprintf(buf, "%s\n;\n\n", mappingHostCode(b))
	}
}

// emitHookFunc writes the init function that sets the hook variable
// name to the hooks (none: nothing is written), for an operator of sort
// sortNm whose arguments have the Go types slotTypes; code prepares the
// bodies (mappingHostCode for the mapping).
func emitHookFunc(buf *bytes.Buffer, name, sortNm string, slotTypes []string, hooks []opHook, code func(string) string) {
	if len(hooks) == 0 {
		return
	}
	// argument names of each hook (missing ones: tom__argI)
	namesOf := func(h opHook) []string {
		names := make([]string, len(slotTypes))
		for i := range names {
			names[i] = fmt.Sprintf("tom__arg%d", i)
			if i < len(h.args) {
				names[i] = h.args[i]
			}
		}
		return names
	}
	first := namesOf(hooks[0])
	declared := map[string]bool{}
	var params []string
	for i, n := range first {
		params = append(params, n+" "+slotTypes[i])
		declared[n] = true
	}
	var decls []string
	for _, h := range hooks[1:] {
		for i, n := range namesOf(h) {
			if !declared[n] {
				declared[n] = true
				decls = append(decls, fmt.Sprintf("var %s %s;", n, slotTypes[i]))
			}
		}
	}
	results := append([]string{sortNm}, slotTypes...)
	// explicit semicolons: tom emits the host code of an included file
	// on a single line
	fmt.Fprintf(buf, "func init() {\n")
	fmt.Fprintf(buf, "  %s = func(%s) (%s) {\n", name, strings.Join(params, ", "), strings.Join(results, ", "))
	for _, d := range decls {
		fmt.Fprintf(buf, "    %s\n", d)
	}
	fmt.Fprintf(buf, "    tom__r := func() %s {\n", sortNm)
	cur := first
	for _, h := range hooks {
		next := namesOf(h)
		for i := range next {
			if next[i] != cur[i] {
				fmt.Fprintf(buf, "    %s = %s;\n", next[i], cur[i])
			}
		}
		cur = next
		fmt.Fprintf(buf, "    {\n%s\n    };\n", code(h.body))
	}
	fmt.Fprintf(buf, "    return nil;\n")
	fmt.Fprintf(buf, "    }();\n")
	fmt.Fprintf(buf, "    return %s;\n", strings.Join(append([]string{"tom__r"}, cur...), ", "))
	fmt.Fprintf(buf, "  };\n")
	fmt.Fprintf(buf, "};\n\n")
}

// emitCongruenceMappingGo writes the congruence strategy operators of
// every alternative, mirroring Java Gom's StratMappingTemplate
// (SOpTemplate / IsOpTemplate mappings) on the Go strategies the
// backend generates (emitStrategyClasses):
//
//   - `_<op>(arg0..argN-1)` builds Visit<Op>: when the subject is <op>,
//     applies argI to its I-th child;
//   - `Is_<op>()` builds Is<Op>, which succeeds on <op> terms only;
//   - `When_<op>(s)` is `Sequence(Is_<op>(), s)`;
//   - `Make_<op>(…)` builds Build<Op> (non-variadic alternatives).
//
// A variadic alternative (cons cells, cells.go, but one Go type for
// Java's Cons<op> and Empty<op>): its `_<op>(sub)` builds Visit<Op>,
// which applies sub to every element. This is what Java's
// `Choice(When_Cons<op>(All(sub)), When_Empty<op>(Identity()))` does
// too: the children of a Java Gom Cons<op> term are all the elements of
// the list (its getChildren is the flattened list), not its head and
// tail (checked with legacy/, smoke fixture gom_congruence). There is no
// Make_Cons<op>/Make_Empty<op>.
func emitCongruenceMappingGo(buf *bytes.Buffer, mod gomast.GomModule, names opNames) {
	types := map[string]bool{}
	for _, prod := range moduleSorts(mod) {
		types[sortName(prod)] = true
	}
	fmt.Fprintf(buf, "%%include { sl.tom }\n\n")
	for _, prod := range moduleSorts(mod) {
		for _, alt := range sortAlternatives(prod) {
			opGo := names.of(alt, sortName(prod))
			isName, visitName := "Is"+opGo, "Visit"+opGo
			var params, argNames []string
			if alternativeIsVariadic(alt) {
				params, argNames = []string{"sub:Strategy"}, []string{"sub"}
			} else {
				for i, f := range alternativeFields(alt) {
					if _, ok := f.(*gomast.NamedFieldField); !ok {
						continue
					}
					params = append(params, fmt.Sprintf("arg%d:Strategy", i))
					argNames = append(argNames, fmt.Sprintf("arg%d", i))
				}
			}
			dollar := make([]string, len(argNames))
			for i, a := range argNames {
				dollar[i] = "$" + a
			}
			fmt.Fprintf(buf, "%%op Strategy _%s(%s) {\n", alt.Name, strings.Join(params, ", "))
			fmt.Fprintf(buf, "  is_fsym(t) { func() bool { _, ok := ($t).(*%s); return ok }() }\n", visitName)
			for i, a := range argNames {
				fmt.Fprintf(buf, "  get_slot(%s, t) { ($t).ChildAt(%d) }\n", a, i)
			}
			fmt.Fprintf(buf, "  make(%s) { New%s(%s) }\n", strings.Join(argNames, ", "), visitName, strings.Join(dollar, ", "))
			fmt.Fprintf(buf, "}\n\n")
			fmt.Fprintf(buf, "%%op Strategy When_%s(s:Strategy) {\n", alt.Name)
			fmt.Fprintf(buf, "  make(s) { sl.NewSequence(&%s{}, $s) }\n", isName)
			fmt.Fprintf(buf, "}\n\n")
			if alternativeIsVariadic(alt) {
				// Make_Cons<op>(head, tail) and Make_Empty<op>() (Java Gom's
				// MakeOpTemplate on the Cons<op>/Empty<op> operator classes)
				ety := fieldTypeName(alternativeFields(alt)[0])
				if types[ety] {
					ety = "Strategy"
				}
				cons, empty := "Cons"+alt.Name, "Empty"+alt.Name
				consGo, emptyGo := "Cons"+opGo, "Empty"+opGo
				fmt.Fprintf(buf, "%%op Strategy Make_%s(Make_%sarg0:%s, Make_%sarg1:Strategy) {\n", cons, cons, ety, cons)
				fmt.Fprintf(buf, "  is_fsym(t) { func() bool { _, ok := ($t).(*Build%s); return ok }() }\n", consGo)
				fmt.Fprintf(buf, "  get_slot(Make_%sarg0, t) { ($t).(*Build%s).Arg0 }\n", cons, consGo)
				fmt.Fprintf(buf, "  get_slot(Make_%sarg1, t) { ($t).(*Build%s).Arg1 }\n", cons, consGo)
				fmt.Fprintf(buf, "  make(a0, a1) { NewBuild%s($a0, $a1) }\n", consGo)
				fmt.Fprintf(buf, "}\n\n")
				fmt.Fprintf(buf, "%%op Strategy Make_%s() {\n", empty)
				fmt.Fprintf(buf, "  is_fsym(t) { func() bool { _, ok := ($t).(*Build%s); return ok }() }\n", emptyGo)
				fmt.Fprintf(buf, "  make() { NewBuild%s() }\n", emptyGo)
				fmt.Fprintf(buf, "}\n\n")
			}
			if !alternativeIsVariadic(alt) {
				var mparams, mnames, mdollar, slotTypes []string
				for i, f := range alternativeFields(alt) {
					if _, ok := f.(*gomast.NamedFieldField); !ok {
						continue
					}
					ty := fieldTypeName(f)
					if types[ty] {
						ty = "Strategy"
					}
					slotTypes = append(slotTypes, ty)
					mparams = append(mparams, fmt.Sprintf("Make_%sarg%d:%s", alt.Name, i, ty))
					mnames = append(mnames, fmt.Sprintf("a%d", i))
					mdollar = append(mdollar, fmt.Sprintf("$a%d", i))
				}
				fmt.Fprintf(buf, "%%op Strategy Make_%s(%s) {\n", alt.Name, strings.Join(mparams, ", "))
				fmt.Fprintf(buf, "  is_fsym(t) { func() bool { _, ok := ($t).(*Build%s); return ok }() }\n", opGo)
				for i := range slotTypes {
					fmt.Fprintf(buf, "  get_slot(Make_%sarg%d, t) { ($t).(*Build%s).Arg%d }\n", alt.Name, i, opGo, i)
				}
				fmt.Fprintf(buf, "  make(%s) { NewBuild%s(%s) }\n", strings.Join(mnames, ", "), opGo, strings.Join(mdollar, ", "))
				fmt.Fprintf(buf, "}\n\n")
			}
			fmt.Fprintf(buf, "%%op Strategy Is_%s() {\n", alt.Name)
			fmt.Fprintf(buf, "  is_fsym(t) { func() bool { _, ok := ($t).(*%s); return ok }() }\n", isName)
			fmt.Fprintf(buf, "  make() { &%s{} }\n", isName)
			fmt.Fprintf(buf, "}\n\n")
		}
	}
}

// GenerateBatchToDir compiles several Gom modules (which may import
// each other) into a single Go package living at dir, through the same
// generation as every other module (GenerateGenerated, as gom-block does
// for a module and the modules it imports): each module gets its
// <module>.go file (the first one also declares the package's factory),
// the slots of a sort of another module of the batch are typed by its
// Go interface, the rules of every module apply to the operators of all
// of them (Options.PackageModules), and the Tom mapping of the whole
// package, with the hooks of every module, is written to <pkg>.tom.
// The resulting Go module has the path tomgen/<pkg>.
//
// This is how the compiler's own ADTs are generated (gom/adt and
// engine/adt, from src/go/tom/{gom,engine}/adt/*.gom, with GoHooks: their
// hooks are plain Go; see magefiles Bootstrap/PromoteAdt).
func GenerateBatchToDir(modules []gomast.GomModule, opts Options, dir string) (string, error) {
	tomRoot, err := locateTomModuleRoot()
	if err != nil {
		return "", err
	}
	if len(modules) == 0 {
		return "", fmt.Errorf("GenerateBatchToDir: empty module list")
	}
	if opts.PackageName == "" {
		opts.PackageName = defaultPackageName(modules[0])
	}
	if err := os.MkdirAll(dir, 0o755); err != nil {
		return "", err
	}
	abs, err := filepath.Abs(dir)
	if err != nil {
		return "", err
	}
	if err := checkPackageNames(modules); err != nil {
		return "", err
	}
	opts.PackageModules = modules
	var names []string
	for _, m := range modules {
		names = append(names, moduleQualifiedName(m))
	}
	mapping := []byte(fmt.Sprintf("/* Code generated by gomgo gom-batch from the Gom modules %s. DO NOT EDIT. */\n\n", strings.Join(names, " ")))
	mapping = append(mapping, BuiltinIncludesGo(modules[0], modules[1:]...)...)
	for i, m := range modules {
		mopts := opts
		mopts.ImportedSorts = map[string]bool{}
		for j, o := range modules {
			if j != i {
				for _, s := range SortNames(o) {
					mopts.ImportedSorts[s] = true
				}
			}
		}
		var buf bytes.Buffer
		if err := GenerateGenerated(m, mopts, i == 0, &buf); err != nil {
			return "", fmt.Errorf("generating %s: %w", moduleQualifiedName(m), err)
		}
		name := strings.ToLower(safeFileName(moduleQualifiedName(m))) + ".go"
		if err := os.WriteFile(filepath.Join(abs, name), buf.Bytes(), 0o644); err != nil {
			return "", err
		}
		mapping = append(mapping, EmitTomMappingGo(m, mopts)...)
	}
	if err := os.WriteFile(filepath.Join(abs, opts.PackageName+".tom"), mapping, 0o644); err != nil {
		return "", err
	}
	goMod := fmt.Sprintf("module tomgen/%s\n\ngo 1.26\n\nrequire tom v0.0.0\nreplace tom => %s\n",
		opts.PackageName, tomRoot)
	if err := os.WriteFile(filepath.Join(abs, "go.mod"), []byte(goMod), 0o644); err != nil {
		return "", err
	}
	return abs, nil
}

// checkPackageNames reports a sort or an operator declared twice among
// the modules of one Go package (two operators whose Go names only
// differ by the case of their first letter, `subterm` and `Subterm`, are
// told apart by opNames).
func checkPackageNames(modules []gomast.GomModule) error {
	sorts := map[string]string{}
	ops := map[string]string{}
	for _, m := range modules {
		qname := moduleQualifiedName(m)
		for _, s := range moduleSorts(m) {
			name := sortName(s)
			if prev, dup := sorts[name]; dup {
				return fmt.Errorf("sort %q is defined in both %s and %s", name, prev, qname)
			}
			sorts[name] = qname
			for _, alt := range sortAlternatives(s) {
				if prev, dup := ops[alt.Name]; dup {
					return fmt.Errorf("operator %q is defined in both %s and %s", alt.Name, prev, qname+"."+name)
				}
				ops[alt.Name] = qname + "." + name
			}
		}
	}
	return nil
}

func safeFileName(qualName string) string {
	out := make([]byte, 0, len(qualName))
	for _, r := range qualName {
		if (r >= 'a' && r <= 'z') || (r >= 'A' && r <= 'Z') || (r >= '0' && r <= '9') {
			out = append(out, byte(r))
		} else {
			out = append(out, '_')
		}
	}
	if len(out) == 0 {
		return "module"
	}
	return string(out)
}

// locateTomModuleRoot returns the absolute path to the root of the Go
// module `tom` (src/go/tom/), used in the generated `replace` directive.
// It walks up from the working directory looking for a go.mod that has
// a line which is exactly "module tom", and fails when there is none
// (the generated module could not resolve tom/library/... otherwise).
func locateTomModuleRoot() (string, error) {
	start, err := os.Getwd()
	if err != nil {
		return "", fmt.Errorf("locating module tom: %w", err)
	}
	dir := start
	for {
		gm, err := os.ReadFile(filepath.Join(dir, "go.mod"))
		if err == nil && isTomModule(gm) {
			return dir, nil
		}
		parent := filepath.Dir(dir)
		if parent == dir {
			return "", fmt.Errorf("no go.mod declaring `module tom` found in %s or its parents; run from within src/go/tom", start)
		}
		dir = parent
	}
}

// isTomModule reports whether a go.mod content declares `module tom`.
func isTomModule(gomod []byte) bool {
	for _, line := range strings.Split(string(gomod), "\n") {
		if strings.TrimSpace(line) == "module tom" {
			return true
		}
	}
	return false
}

func defaultPackageName(mod gomast.GomModule) string {
	parts := moduleNameParts(mod)
	last := ""
	if n := len(parts); n > 0 {
		last = parts[n-1]
	}
	var b strings.Builder
	for _, r := range last {
		if unicode.IsLetter(r) || unicode.IsDigit(r) {
			b.WriteRune(unicode.ToLower(r))
		}
	}
	out := b.String()
	if out == "" {
		out = "gen"
	}
	return out
}

func generateBytes(mod gomast.GomModule, opts Options) ([]byte, error) {
	return generateBytesFactory(mod, opts, true)
}

func generateBytesFactory(mod gomast.GomModule, opts Options, withFactory bool) ([]byte, error) {
	pkg := opts.packageModules(mod)
	if err := checkUserHooks(pkg, mod, opts.GoHooks); err != nil {
		return nil, err
	}
	if opts.PackageName == "" {
		opts.PackageName = defaultPackageName(mod)
	}
	g := &gen{
		mod:        mod,
		ownSort:    map[string]bool{},
		pkgName:    opts.PackageName,
		congruence: opts.WithCongruenceStrategies,
		noSharing:  opts.NoSharing,
		pkgMods:    pkg,
		opNames:    newOpNames(pkg),
		goHooks:    opts.GoHooks,
	}
	for _, s := range moduleSorts(mod) {
		g.ownSort[sortName(s)] = true
	}
	for s := range opts.ImportedSorts {
		g.ownSort[s] = true
	}
	g.emitHeader()
	if withFactory {
		g.emitFactory()
	}
	for _, s := range moduleSorts(mod) {
		g.emitSort(s)
	}
	if g.goHooks {
		g.emitGoHooks()
	}
	formatted, err := format.Source(g.buf.Bytes())
	if err != nil {
		// Return the raw source on a formatter error so the bug is debuggable.
		return g.buf.Bytes(), fmt.Errorf("gofmt failed: %w", err)
	}
	return formatted, nil
}

// ---------------------------------------------------------------------------
// generator state and emission
// ---------------------------------------------------------------------------

type gen struct {
	mod     gomast.GomModule
	ownSort map[string]bool
	pkgName string
	buf     bytes.Buffer

	// pkgMods are the modules generated into the same Go package
	// (Options.PackageModules, mod included): the rules of each of them
	// apply to the operators of all (Java Gom's RuleExpander works on the
	// whole module list).
	pkgMods []gomast.GomModule

	// opNames are the Go names shared by several operators of pkgMods.
	opNames opNames

	// goHooks is Options.GoHooks.
	goHooks bool

	// congruence is Options.WithCongruenceStrategies: the Build<Op>
	// (Java Make_<op>) strategies are generated only then.
	congruence bool

	// noSharing is Options.NoSharing (Java Gom's --nosharing).
	noSharing bool
}

// emitReturnProto ends a smart constructor: the canonical (shared) term,
// or, with --nosharing, the new term itself (Java Gom's
// `return new <Op>(…)`).
func (g *gen) emitReturnProto(structName string) {
	if g.noSharing {
		fmt.Fprintln(&g.buf, "\treturn proto")
		return
	}
	fmt.Fprintf(&g.buf, "\treturn factory.Build(proto).(*%s)\n", structName)
}

// reservedFieldName is the set of method names emitted on every alt
// struct (sharedobjects.Term, the Java Gom API, Visitable). A Gom slot
// whose exported name collides with one of these would create a Go
// "field and method with the same name" error (Java has no such clash:
// the getter of slot x is getx(), its methods are lower-case). We append
// "_" to the field name in that case.
var reservedFieldName = map[string]bool{
	"String":       true,
	"Hash":         true,
	"Equivalent":   true,
	"Duplicate":    true,
	"DeepEquals":   true,
	"SymbolName":   true,
	"Length":       true,
	"Reverse":      true,
	"CompareTo":    true,
	"CompareToLPO": true,
	"ToATerm":      true,
	"ChildCount":   true,
	"ChildAt":      true,
	"SetChildAt":   true,
	"Children":     true,
	"SetChildren":  true,
}

func (g *gen) emitHeader() {
	fmt.Fprintf(&g.buf, "// Code generated by tom/gom backend from %s. DO NOT EDIT.\n", moduleQualifiedName(g.mod))
	fmt.Fprintf(&g.buf, "package %s\n\n", g.pkgName)
	fmt.Fprintln(&g.buf, "import (")
	fmt.Fprintln(&g.buf, `	"fmt"`)
	fmt.Fprintln(&g.buf, `	"strings"`)
	fmt.Fprintln(&g.buf, "")
	fmt.Fprintln(&g.buf, `	"io"`)
	fmt.Fprintln(&g.buf, `	"sync/atomic"`)
	fmt.Fprintln(&g.buf, "")
	fmt.Fprintln(&g.buf, `	"tom/library/aterm"`)
	fmt.Fprintln(&g.buf, `	"tom/library/sharedobjects"`)
	fmt.Fprintln(&g.buf, `	sl "tom/library/sl"`)
	fmt.Fprintln(&g.buf, ")")
	fmt.Fprintln(&g.buf)
	fmt.Fprintln(&g.buf, "// underscore-prevent: tolerate unused imports if a module has no slots of these types.")
	fmt.Fprintln(&g.buf, "var _ = fmt.Sprintf")
	fmt.Fprintln(&g.buf, "var _ = strings.Join")
	fmt.Fprintln(&g.buf, "var _ sl.Strategy = nil")
	fmt.Fprintln(&g.buf, "var _ = io.EOF")
	fmt.Fprintln(&g.buf, "var _ = aterm.GetInstance")
	fmt.Fprintln(&g.buf, "var _ atomic.Bool")
	fmt.Fprintln(&g.buf)
}

func (g *gen) emitFactory() {
	fmt.Fprintln(&g.buf, "// factory is the per-package shared-objects factory backing every constructor.")
	fmt.Fprintln(&g.buf, "var factory = sharedobjects.NewFactory()")
	fmt.Fprintln(&g.buf)
	fmt.Fprintln(&g.buf, "// Factory exposes the underlying shared-objects factory for tests/stats.")
	fmt.Fprintln(&g.buf, "func Factory() *sharedobjects.Factory { return factory }")
	fmt.Fprintln(&g.buf)
}

func (g *gen) emitSort(prod *gomast.SortTypeProduction) {
	name := sortName(prod)
	api := g.sortAPIOf(prod)

	fmt.Fprintf(&g.buf, "// %s is the Go interface backing the Gom sort %s.\n", name, name)
	fmt.Fprintf(&g.buf, "type %s interface {\n", name)
	fmt.Fprintln(&g.buf, "	sharedobjects.Term")
	fmt.Fprintf(&g.buf, "	is%s()\n", name)
	if g.noSharing {
		fmt.Fprintln(&g.buf, "	// DeepEquals compares terms that are not maximally shared (--nosharing).")
		fmt.Fprintln(&g.buf, "	DeepEquals(o any) bool")
	}
	g.emitJavaAPIInterface(api)
	if methods := g.blockSortMethods(prod); len(methods) > 0 {
		fmt.Fprintf(&g.buf, "\t// methods of the block hooks of sort %s\n", name)
		for _, m := range methods {
			fmt.Fprintf(&g.buf, "\t%s\n", m)
		}
	}
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)
	g.emitJavaFromTerm(api)
	for _, alt := range sortAlternatives(prod) {
		g.emitAlternative(name, alt)
		g.emitJavaAPIMethods(api, alt)
	}
}

// hookSlot describes one slot of an alternative (exported Go field name,
// Go type, variadic) for the strategy and Visitable emitters.
type hookSlot struct {
	Name   string // exported Go field name
	GoType string
	IsVar  bool
}

// emitAlternative emits the struct, hash/equiv/duplicate/string methods,
// and the smart constructor for one alternative.
//
// The operator name from the .gom source (e.g. `zero`, `suc`) is
// capitalized in the generated Go identifiers so all types and
// constructors are exported. The original spelling is preserved in the
// hash input and in the String() output so canonicalization and
// pretty-printing remain faithful to the source.
func (g *gen) emitAlternative(sortNm string, alt *gomast.AlternativeAlternative) {
	structName := opBase(alt) + sortNm
	opGo := g.opNames.of(alt, sortNm)
	opName := alt.Name
	makeName := "Make" + opGo

	fields := alternativeFields(alt)
	if alternativeIsVariadic(alt) {
		// Java Gom's shared cons cells (cells.go, javaapi.go)
		elem := g.goType(fieldTypeName(fields[0]), false)
		g.emitVariadicCells(sortNm, alt, structName, elem)
		g.emitJavaString(alt, structName)
		g.emitVariadicJava(sortNm, alt, structName, elem)
		g.emitStrategyClasses(sortNm, opGo, opName, structName, 1, true)
		if g.congruence {
			// Java Gom's Cons<op>/Empty<op> are operator classes with their
			// own Make_ strategies (StratMappingTemplate, MakeOpTemplate)
			g.emitBuildStrategy("Cons"+opGo, "Cons"+opName, "Cons"+opGo, []hookSlot{{Name: "HeadArg", GoType: elem}, {Name: "TailArg", GoType: sortNm}})
			g.emitBuildStrategy("Empty"+opGo, "Empty"+opName, "Empty"+opGo, nil)
		}
		return
	}

	// The slots of the (non-variadic) alternative, with their Go field
	// names.
	var slots []hookSlot
	seen := map[string]int{}
	for i, f := range fields {
		nf, ok := f.(*gomast.NamedFieldField)
		if !ok {
			// In a non-variadic alternative every field must be
			// named — anything else is a parser bug.
			continue
		}
		name := exportedField(nf.Name)
		if name == "" {
			name = fmt.Sprintf("Slot%d", i)
		}
		// Avoid colliding with the struct's own methods (String,
		// Hash, Equivalent, Duplicate). A Gom slot literally named
		// `String:String` (as in TomName.Name) would otherwise
		// produce `field and method with the same name`.
		if reservedFieldName[name] {
			name = name + "_"
		}
		if c := seen[name]; c > 0 {
			name = fmt.Sprintf("%s%d", name, c+1)
		}
		seen[name]++
		slots = append(slots, hookSlot{Name: name, GoType: g.goType(fieldTypeName(f), false)})
	}

	// Struct definition.
	fmt.Fprintf(&g.buf, "// %s is the term type for the alternative `%s(...)` of sort %s.\n", structName, opName, sortNm)
	fmt.Fprintf(&g.buf, "type %s struct {\n", structName)
	for _, s := range slots {
		fmt.Fprintf(&g.buf, "\t%s %s\n", s.Name, s.GoType)
	}
	fmt.Fprintf(&g.buf, "\thash uint32\n")
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)

	// Marker interface method.
	fmt.Fprintf(&g.buf, "func (*%s) is%s() {}\n\n", structName, sortNm)

	// Hash method.
	fmt.Fprintf(&g.buf, "func (t *%s) Hash() uint32 { return t.hash }\n\n", structName)

	// Equivalent. When the alternative has no slot, we don't bind `o`
	// (Go would refuse the unused identifier).
	fmt.Fprintf(&g.buf, "func (t *%s) Equivalent(other sharedobjects.Term) bool {\n", structName)
	if len(slots) == 0 {
		fmt.Fprintf(&g.buf, "\t_, ok := other.(*%s)\n", structName)
		fmt.Fprintln(&g.buf, "\treturn ok")
	} else {
		fmt.Fprintf(&g.buf, "\to, ok := other.(*%s)\n", structName)
		fmt.Fprintln(&g.buf, "\tif !ok {")
		fmt.Fprintln(&g.buf, "\t\treturn false")
		fmt.Fprintln(&g.buf, "\t}")
		for _, s := range slots {
			fmt.Fprintf(&g.buf, "\tif t.%s != o.%s { return false }\n", s.Name, s.Name)
		}
		fmt.Fprintln(&g.buf, "\treturn true")
	}
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)

	// DeepEquals (--nosharing): Java Gom's deepEquals, the structural
	// equality of terms that are not maximally shared.
	if g.noSharing {
		fmt.Fprintf(&g.buf, "func (t *%s) DeepEquals(other any) bool {\n", structName)
		if len(slots) == 0 {
			fmt.Fprintf(&g.buf, "\t_, ok := other.(*%s)\n", structName)
			fmt.Fprintln(&g.buf, "\treturn ok")
		} else {
			fmt.Fprintf(&g.buf, "\to, ok := other.(*%s)\n", structName)
			fmt.Fprintln(&g.buf, "\tif !ok {")
			fmt.Fprintln(&g.buf, "\t\treturn false")
			fmt.Fprintln(&g.buf, "\t}")
			for _, s := range slots {
				if g.isShared(s.GoType) {
					fmt.Fprintf(&g.buf, "\tif !t.%s.DeepEquals(o.%s) { return false }\n", s.Name, s.Name)
				} else {
					fmt.Fprintf(&g.buf, "\tif t.%s != o.%s { return false }\n", s.Name, s.Name)
				}
			}
			fmt.Fprintln(&g.buf, "\treturn true")
		}
		fmt.Fprintln(&g.buf, "}")
		fmt.Fprintln(&g.buf)
	}

	// Duplicate.
	fmt.Fprintf(&g.buf, "func (t *%s) Duplicate() sharedobjects.Term {\n", structName)
	fmt.Fprintf(&g.buf, "\tclone := *t\n")
	fmt.Fprintln(&g.buf, "\treturn &clone")
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)

	// String (Java Gom's toString).
	g.emitJavaString(alt, structName)

	// Visitable methods: Children/SetChildren/ChildCount/ChildAt/
	// SetChildAt, so that the term can be walked by the strategies of
	// tom/library/sl. Every slot is a child, builtin values included
	// (Go's any holds them, no VisitableBuiltin boxing). SetChildAt /
	// SetChildren rebuild the term via the smart constructor so canonical
	// sharing (hash-cons) is preserved; callers must supply children of
	// the slot types, as Java's Visitable contract requires.
	g.emitVisitableMethods(structName, makeName, slots)

	// Smart constructor. The Go-side function name is exported (Pascal
	// case); the canonical symbol passed to the hash mixer keeps the
	// original spelling so sharing stays correct.
	fmt.Fprintf(&g.buf, "// %s builds the canonical (shared) %s term.\n", makeName, opName)
	var params []string
	for _, s := range slots {
		params = append(params, fmt.Sprintf("%s %s", unexported(s.Name), s.GoType))
	}
	var types, names []string
	for _, s := range slots {
		types = append(types, s.GoType)
		names = append(names, unexported(s.Name))
	}
	hooked := len(hooksOfKind(opHooks(g.pkgMods, g.mod, alt), "make")) > 0
	if hooked {
		fmt.Fprintf(&g.buf, "// Hook%s runs the user `%s:make` hooks of the Gom module: it\n", makeName, opName)
		fmt.Fprintf(&g.buf, "// returns their result (nil if none returned) and the arguments\n")
		fmt.Fprintf(&g.buf, "// they may have reassigned. The Tom mapping generated with this file\n")
		fmt.Fprintf(&g.buf, "// sets it.\n")
		fmt.Fprintf(&g.buf, "var Hook%s func(%s) (%s)\n\n", makeName, strings.Join(types, ", "), strings.Join(append([]string{sortNm}, types...), ", "))
	}
	fmt.Fprintf(&g.buf, "func %s(%s) %s {\n", makeName, strings.Join(params, ", "), sortNm)
	if hooked {
		fmt.Fprintf(&g.buf, "\tif Hook%s != nil {\n", makeName)
		fmt.Fprintf(&g.buf, "\t\tvar hookResult %s\n", sortNm)
		fmt.Fprintf(&g.buf, "\t\t%s = Hook%s(%s)\n", strings.Join(append([]string{"hookResult"}, names...), ", "), makeName, strings.Join(names, ", "))
		fmt.Fprintln(&g.buf, "\t\tif hookResult != nil {")
		fmt.Fprintln(&g.buf, "\t\t\treturn hookResult")
		fmt.Fprintln(&g.buf, "\t\t}")
		fmt.Fprintln(&g.buf, "\t}")
	}
	fmt.Fprintf(&g.buf, "\thashes := []uint32{")
	for i, s := range slots {
		if i > 0 {
			fmt.Fprint(&g.buf, ", ")
		}
		if g.isShared(s.GoType) {
			fmt.Fprintf(&g.buf, "%s.Hash()", unexported(s.Name))
		} else {
			fmt.Fprintf(&g.buf, "sharedobjects.StringHash(fmt.Sprintf(\"%%v\", %s))", unexported(s.Name))
		}
	}
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintf(&g.buf, "\tproto := &%s{hash: sharedobjects.MixSymbol(sharedobjects.StringHash(%q), hashes)", structName, opName)
	for _, s := range slots {
		fmt.Fprintf(&g.buf, ", %s: %s", s.Name, unexported(s.Name))
	}
	fmt.Fprintln(&g.buf, "}")
	g.emitReturnProto(structName)
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)
	g.emitStrategyClasses(sortNm, opGo, opName, structName, len(slots), false)
	if g.congruence {
		g.emitBuildStrategy(opGo, opName, makeName, slots)
	}
}

// emitStrategyClasses writes two `sl.Strategy` implementations per
// alternative, mirroring Gom's Java backend output (the
// `_<Alt>.java` and `Is_<Alt>.java` files under .../adt/<sort>/strategy/).
//
//   - `Is<Alt>` is the predicate strategy: VisitLight returns the
//     subject unchanged if it has the `<Alt>` shape, ErrVisitFailure
//     otherwise. Useful for `Sequence(IsFoo{}, ...)` guards.
//
//   - `Visit<Alt>(s1, ..., sN)` is the slot-visit strategy: VisitLight
//     applies each substrategy to the corresponding child slot and
//     rebuilds the term iff a child changed. Returns ErrVisitFailure
//     when subject isn't `<Alt>`. Java calls this `_<Alt>`.
//
// For variadic alternatives (`concX(...)`) the slot-visit strategy
// takes a single sub-strategy applied to every element of the slot
// list — matching Java's Cons/Empty cons-cell traversal.
func (g *gen) emitStrategyClasses(sortNm, opGo, opName, structName string, slotCount int, isVariadic bool) {
	isName := "Is" + opGo
	visitName := "Visit" + opGo

	// Is<Alt>: predicate strategy.
	fmt.Fprintf(&g.buf, "// %s is the `Is_%s` predicate strategy: succeeds (returns subject\n", isName, opName)
	fmt.Fprintf(&g.buf, "// unchanged) when subject has the `%s` shape, otherwise fails with\n", opName)
	fmt.Fprintln(&g.buf, "// sl.ErrVisitFailure.")
	fmt.Fprintf(&g.buf, "type %s struct{ sl.AbstractStrategy }\n\n", isName)
	fmt.Fprintf(&g.buf, "func (%s) VisitLight(subject any, _ sl.Introspector) (any, error) {\n", isName)
	fmt.Fprintf(&g.buf, "\tif _, ok := subject.(*%s); ok {\n", structName)
	fmt.Fprintln(&g.buf, "\t\treturn subject, nil")
	fmt.Fprintln(&g.buf, "\t}")
	fmt.Fprintln(&g.buf, "\treturn subject, sl.ErrVisitFailure")
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintf(&g.buf, "func (%s) ChildCount() int                  { return 0 }\n", isName)
	fmt.Fprintf(&g.buf, "func (%s) ChildAt(int) sl.Strategy          { panic(\"%s: no children\") }\n", isName, isName)
	fmt.Fprintf(&g.buf, "func (%s) SetChildAt(int, sl.Strategy)      { panic(\"%s: no children\") }\n", isName, isName)
	// Environment-based visit (Java Is_<Alt>.visit(Introspector)).
	fmt.Fprintf(&g.buf, "func (s *%s) VisitEnv(sl.Introspector) int {\n", isName)
	fmt.Fprintf(&g.buf, "\tif _, ok := s.GetEnvironment().GetSubject().(*%s); ok {\n", structName)
	fmt.Fprintln(&g.buf, "\t\treturn sl.StatusSuccess")
	fmt.Fprintln(&g.buf, "\t}")
	fmt.Fprintln(&g.buf, "\treturn sl.StatusFailure")
	fmt.Fprintln(&g.buf, "}")
	g.emitStrategyVisitEntryPoints(isName)
	fmt.Fprintln(&g.buf)

	// Visit<Alt>: slot-visit strategy.
	fmt.Fprintf(&g.buf, "// %s is the `_%s` slot-visit strategy: when subject is `%s`,\n", visitName, opName, opName)
	fmt.Fprintf(&g.buf, "// applies each constituent strategy to the matching child slot and\n")
	fmt.Fprintf(&g.buf, "// rebuilds the term iff at least one child changed. Fails with\n")
	fmt.Fprintf(&g.buf, "// sl.ErrVisitFailure when subject isn't `%s`.\n", opName)
	fmt.Fprintf(&g.buf, "type %s struct {\n", visitName)
	fmt.Fprintln(&g.buf, "\tsl.AbstractStrategy")
	fmt.Fprintln(&g.buf, "\targs []sl.Strategy")
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)

	// Constructor New<VisitName>(args ...sl.Strategy)
	fmt.Fprintf(&g.buf, "// New%s builds the slot-visit strategy with the supplied per-slot\n", visitName)
	fmt.Fprintln(&g.buf, "// sub-strategies.")
	fmt.Fprintf(&g.buf, "func New%s(args ...sl.Strategy) *%s {\n", visitName, visitName)
	fmt.Fprintf(&g.buf, "\treturn &%s{args: args}\n", visitName)
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)

	// VisitLight implementation.
	fmt.Fprintf(&g.buf, "func (s *%s) VisitLight(subject any, intro sl.Introspector) (any, error) {\n", visitName)
	fmt.Fprintf(&g.buf, "\tif _, ok := subject.(*%s); !ok {\n", structName)
	fmt.Fprintln(&g.buf, "\t\treturn subject, sl.ErrVisitFailure")
	fmt.Fprintln(&g.buf, "\t}")
	if isVariadic {
		fmt.Fprintln(&g.buf, "\t// Variadic alt: visit every element with args[0] (Java's")
		fmt.Fprintln(&g.buf, "\t// `_concX` invokes the sub-strategy on each list element).")
		fmt.Fprintln(&g.buf, "\tif len(s.args) == 0 {")
		fmt.Fprintln(&g.buf, "\t\treturn subject, nil")
		fmt.Fprintln(&g.buf, "\t}")
		fmt.Fprintln(&g.buf, "\tcount := intro.GetChildCount(subject)")
		fmt.Fprintln(&g.buf, "\tvar newChildren []any")
		fmt.Fprintln(&g.buf, "\tfor i := 0; i < count; i++ {")
		fmt.Fprintln(&g.buf, "\t\toldChild := intro.GetChildAt(subject, i)")
		fmt.Fprintln(&g.buf, "\t\tnewChild, err := s.args[0].VisitLight(oldChild, intro)")
		fmt.Fprintln(&g.buf, "\t\tif err != nil {")
		fmt.Fprintln(&g.buf, "\t\t\treturn subject, err")
		fmt.Fprintln(&g.buf, "\t\t}")
		fmt.Fprintln(&g.buf, "\t\tif newChildren != nil {")
		fmt.Fprintln(&g.buf, "\t\t\tnewChildren[i] = newChild")
		fmt.Fprintln(&g.buf, "\t\t} else if newChild != oldChild {")
		fmt.Fprintln(&g.buf, "\t\t\tnewChildren = intro.GetChildren(subject)")
		fmt.Fprintln(&g.buf, "\t\t\tnewChildren[i] = newChild")
		fmt.Fprintln(&g.buf, "\t\t}")
		fmt.Fprintln(&g.buf, "\t}")
		fmt.Fprintln(&g.buf, "\tif newChildren != nil {")
		fmt.Fprintln(&g.buf, "\t\treturn intro.SetChildren(subject, newChildren), nil")
		fmt.Fprintln(&g.buf, "\t}")
		fmt.Fprintln(&g.buf, "\treturn subject, nil")
	} else if slotCount == 0 {
		fmt.Fprintln(&g.buf, "\t// Nullary alt: no children to visit.")
		fmt.Fprintln(&g.buf, "\treturn subject, nil")
	} else {
		fmt.Fprintf(&g.buf, "\tif len(s.args) != %d {\n", slotCount)
		fmt.Fprintf(&g.buf, "\t\treturn subject, sl.ErrVisitFailure\n")
		fmt.Fprintln(&g.buf, "\t}")
		fmt.Fprintln(&g.buf, "\tvar newChildren []any")
		fmt.Fprintf(&g.buf, "\tfor i := 0; i < %d; i++ {\n", slotCount)
		fmt.Fprintln(&g.buf, "\t\toldChild := intro.GetChildAt(subject, i)")
		fmt.Fprintln(&g.buf, "\t\tnewChild, err := s.args[i].VisitLight(oldChild, intro)")
		fmt.Fprintln(&g.buf, "\t\tif err != nil {")
		fmt.Fprintln(&g.buf, "\t\t\treturn subject, err")
		fmt.Fprintln(&g.buf, "\t\t}")
		fmt.Fprintln(&g.buf, "\t\tif newChildren != nil {")
		fmt.Fprintln(&g.buf, "\t\t\tnewChildren[i] = newChild")
		fmt.Fprintln(&g.buf, "\t\t} else if newChild != oldChild {")
		fmt.Fprintln(&g.buf, "\t\t\tnewChildren = intro.GetChildren(subject)")
		fmt.Fprintln(&g.buf, "\t\t\tnewChildren[i] = newChild")
		fmt.Fprintln(&g.buf, "\t\t}")
		fmt.Fprintln(&g.buf, "\t}")
		fmt.Fprintln(&g.buf, "\tif newChildren != nil {")
		fmt.Fprintln(&g.buf, "\t\treturn intro.SetChildren(subject, newChildren), nil")
		fmt.Fprintln(&g.buf, "\t}")
		fmt.Fprintln(&g.buf, "\treturn subject, nil")
	}
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)

	// Tree-introspection plumbing (ChildCount/ChildAt/SetChildAt).
	fmt.Fprintf(&g.buf, "func (s *%s) ChildCount() int                { return len(s.args) }\n", visitName)
	fmt.Fprintf(&g.buf, "func (s *%s) ChildAt(i int) sl.Strategy      { return s.args[i] }\n", visitName)
	fmt.Fprintf(&g.buf, "func (s *%s) SetChildAt(i int, v sl.Strategy) { s.args[i] = v }\n", visitName)
	fmt.Fprintln(&g.buf)

	// Environment-based visit (Java _<Alt>.visit(Introspector)).
	fmt.Fprintf(&g.buf, "func (s *%s) VisitEnv(intro sl.Introspector) int {\n", visitName)
	fmt.Fprintf(&g.buf, "\tif _, ok := s.GetEnvironment().GetSubject().(*%s); !ok {\n", structName)
	fmt.Fprintln(&g.buf, "\t\treturn sl.StatusFailure")
	fmt.Fprintln(&g.buf, "\t}")
	if !isVariadic && slotCount > 0 {
		fmt.Fprintf(&g.buf, "\tif len(s.args) != %d {\n", slotCount)
		fmt.Fprintln(&g.buf, "\t\treturn sl.StatusFailure")
		fmt.Fprintln(&g.buf, "\t}")
	}
	fmt.Fprintf(&g.buf, "\treturn sl.VisitCongruence(s.GetEnvironment(), intro, s.args, %t)\n", isVariadic)
	fmt.Fprintln(&g.buf, "}")
	g.emitStrategyVisitEntryPoints(visitName)
	fmt.Fprintln(&g.buf)
}

// emitBuildStrategy writes Build<Op>, the `Make_<op>` strategy of Java
// Gom (MakeOpTemplate): it builds an <op> term whose children are the
// results of its sub-strategies applied to the subject (one per slot of
// a sort of the module), builtin slots being given as values. The
// fields Arg<i> hold the per-slot strategies or values; the strategy
// children (ChildCount/ChildAt) are the strategy slots only.
func (g *gen) emitBuildStrategy(opGo, opName, makeName string, slots []hookSlot) {
	name := "Build" + opGo
	var strat []int
	for i, s := range slots {
		if g.isShared(s.GoType) {
			strat = append(strat, i)
		}
	}
	fieldType := func(s hookSlot) string {
		if g.isShared(s.GoType) {
			return "sl.Strategy"
		}
		return s.GoType
	}
	fmt.Fprintf(&g.buf, "// %s is the `Make_%s` strategy: it builds %s(…) from the results\n", name, opName, opName)
	fmt.Fprintln(&g.buf, "// of its sub-strategies on the subject (builtin slots are values).")
	fmt.Fprintf(&g.buf, "type %s struct {\n\tsl.AbstractStrategy\n", name)
	for i, s := range slots {
		fmt.Fprintf(&g.buf, "\tArg%d %s\n", i, fieldType(s))
	}
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)
	var params, inits []string
	for i, s := range slots {
		params = append(params, fmt.Sprintf("arg%d %s", i, fieldType(s)))
		inits = append(inits, fmt.Sprintf("Arg%d: arg%d", i, i))
	}
	fmt.Fprintf(&g.buf, "// New%s builds Make_%s.\n", name, opName)
	fmt.Fprintf(&g.buf, "func New%s(%s) *%s {\n\treturn &%s{%s}\n}\n\n", name, strings.Join(params, ", "), name, name, strings.Join(inits, ", "))
	args := make([]string, len(slots))
	// VisitLight: every sub-strategy is applied to the subject.
	fmt.Fprintf(&g.buf, "func (s *%s) VisitLight(subject any, intro sl.Introspector) (any, error) {\n", name)
	for i, sv := range slots {
		if !g.isShared(sv.GoType) {
			args[i] = fmt.Sprintf("s.Arg%d", i)
			continue
		}
		args[i] = fmt.Sprintf("new%d", i)
		fmt.Fprintf(&g.buf, "\ttmp%d, err := s.Arg%d.VisitLight(subject, intro)\n", i, i)
		fmt.Fprintln(&g.buf, "\tif err != nil {\n\t\treturn nil, err\n\t}")
		fmt.Fprintf(&g.buf, "\tnew%d, ok := tmp%d.(%s)\n", i, i, sv.GoType)
		fmt.Fprintln(&g.buf, "\tif !ok {\n\t\treturn nil, sl.ErrVisitFailure\n\t}")
	}
	fmt.Fprintf(&g.buf, "\treturn %s(%s), nil\n}\n\n", makeName, strings.Join(args, ", "))
	// VisitEnv: as Java, each sub-strategy visits in turn (its status
	// is ignored) and the current subject is taken as the child.
	fmt.Fprintf(&g.buf, "func (s *%s) VisitEnv(intro sl.Introspector) int {\n", name)
	fmt.Fprintln(&g.buf, "\tenv := s.GetEnvironment()")
	for i, sv := range slots {
		if !g.isShared(sv.GoType) {
			continue
		}
		fmt.Fprintf(&g.buf, "\ts.Arg%d.VisitEnv(intro)\n", i)
		fmt.Fprintf(&g.buf, "\tnew%d, ok := env.GetSubject().(%s)\n", i, sv.GoType)
		fmt.Fprintln(&g.buf, "\tif !ok {\n\t\treturn sl.StatusFailure\n\t}")
	}
	fmt.Fprintf(&g.buf, "\tenv.SetSubject(%s(%s))\n\treturn sl.StatusSuccess\n}\n\n", makeName, strings.Join(args, ", "))
	fmt.Fprintf(&g.buf, "func (s *%s) ChildCount() int { return %d }\n\n", name, len(strat))
	fmt.Fprintf(&g.buf, "func (s *%s) ChildAt(i int) sl.Strategy {\n\tswitch i {\n", name)
	for k, i := range strat {
		fmt.Fprintf(&g.buf, "\tcase %d:\n\t\treturn s.Arg%d\n", k, i)
	}
	fmt.Fprintf(&g.buf, "\t}\n\tpanic(\"%s: no child \" + fmt.Sprint(i))\n}\n\n", name)
	fmt.Fprintf(&g.buf, "func (s *%s) SetChildAt(i int, v sl.Strategy) {\n\tswitch i {\n", name)
	for k, i := range strat {
		fmt.Fprintf(&g.buf, "\tcase %d:\n\t\ts.Arg%d = v\n\t\treturn\n", k, i)
	}
	fmt.Fprintf(&g.buf, "\t}\n\tpanic(\"%s: no child \" + fmt.Sprint(i))\n}\n", name)
	g.emitStrategyVisitEntryPoints(name)
	fmt.Fprintln(&g.buf)
}

// emitStrategyVisitEntryPoints writes the Visit / VisitWith methods
// every sl.Strategy defines on top of its VisitEnv (Go has no virtual
// dispatch, so they cannot be inherited from sl.AbstractStrategy).
func (g *gen) emitStrategyVisitEntryPoints(name string) {
	fmt.Fprintf(&g.buf, "func (s *%s) Visit(subject any, intro sl.Introspector) (any, error) {\n", name)
	fmt.Fprintln(&g.buf, "\treturn sl.VisitStrategy(s, subject, intro)")
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintf(&g.buf, "func (s *%s) VisitWith(env *sl.Environment, intro sl.Introspector) (any, error) {\n", name)
	fmt.Fprintln(&g.buf, "\treturn sl.VisitStrategyWith(s, env, intro)")
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintf(&g.buf, "\nvar _ sl.Strategy = (*%s)(nil)\n", name)
}

// emitVisitableMethods writes Children/SetChildren/ChildCount/
// ChildAt/SetChildAt onto the struct identified by structName.
// makeName is the smart-constructor function used by SetChildAt and
// SetChildren to rebuild a canonical (hash-consed) instance.
//
// One child per declared slot of the (non-variadic) alternative, in
// source order (the variadic operators are cons cells, see cells.go).
// Slot Go types may be a generated sort interface (e.g. TomTerm), a
// primitive (string, int64, …), or `any` for cross-module references.
// Setters cast back from `any` to the slot type; callers passing the
// wrong concrete type get a Go type-assertion panic (mirroring Java's
// ClassCastException on setChildAt).
//
// The methods are emitted unconditionally — even on zero-slot
// constants — so every term has a uniform Visitable surface.
func (g *gen) emitVisitableMethods(structName, makeName string, slots []hookSlot) {
	// castExpr returns the Go expression to cast a `child any` to the
	// slot's static type. `any` slots need no cast.
	castExpr := func(name, goType string) string {
		if goType == "any" {
			return name
		}
		return fmt.Sprintf("%s.(%s)", name, goType)
	}

	// Non-variadic: child count is fixed at compile time.
	fmt.Fprintf(&g.buf, "func (t *%s) ChildCount() int { return %d }\n\n", structName, len(slots))

	if len(slots) == 0 {
		// Zero-slot alts (constants like EmptyName, TrueTL) get
		// degenerate ChildAt/SetChildAt that always panic, and trivial
		// Children/SetChildren. Strategies use ChildCount first, so
		// the panic paths only fire on misuse.
		fmt.Fprintf(&g.buf, "func (t *%s) ChildAt(i int) any {\n", structName)
		fmt.Fprintf(&g.buf, "\tpanic(fmt.Sprintf(%q, i))\n", structName+".ChildAt: index %d out of [0,0)")
		fmt.Fprintln(&g.buf, "}")
		fmt.Fprintln(&g.buf)

		fmt.Fprintf(&g.buf, "func (t *%s) SetChildAt(i int, child any) any {\n", structName)
		fmt.Fprintf(&g.buf, "\tpanic(fmt.Sprintf(%q, i))\n", structName+".SetChildAt: index %d out of [0,0)")
		fmt.Fprintln(&g.buf, "}")
		fmt.Fprintln(&g.buf)

		fmt.Fprintf(&g.buf, "func (t *%s) Children() []any { return nil }\n\n", structName)

		fmt.Fprintf(&g.buf, "func (t *%s) SetChildren(children []any) any { return t }\n\n", structName)
		return
	}

	// ChildAt: switch on the index.
	fmt.Fprintf(&g.buf, "func (t *%s) ChildAt(i int) any {\n", structName)
	fmt.Fprintln(&g.buf, "\tswitch i {")
	for i, s := range slots {
		fmt.Fprintf(&g.buf, "\tcase %d: return t.%s\n", i, s.Name)
	}
	fmt.Fprintln(&g.buf, "\t}")
	fmt.Fprintf(&g.buf, "\tpanic(fmt.Sprintf(%q, i))\n", structName+".ChildAt: index %d out of range")
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)

	// SetChildAt: switch on the index, rebuild via Make<Op>.
	fmt.Fprintf(&g.buf, "func (t *%s) SetChildAt(i int, child any) any {\n", structName)
	fmt.Fprintln(&g.buf, "\tswitch i {")
	for i := range slots {
		// Build the argument list to Make<Op>: t.<slot> for every
		// slot except `i`, which receives the cast `child`.
		var args []string
		for j, sj := range slots {
			if j == i {
				args = append(args, castExpr("child", sj.GoType))
			} else {
				args = append(args, "t."+sj.Name)
			}
		}
		fmt.Fprintf(&g.buf, "\tcase %d: return %s(%s)\n", i, makeName, strings.Join(args, ", "))
	}
	fmt.Fprintln(&g.buf, "\t}")
	fmt.Fprintf(&g.buf, "\tpanic(fmt.Sprintf(%q, i))\n", structName+".SetChildAt: index %d out of range")
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)

	// Children: return all slots as []any.
	fmt.Fprintf(&g.buf, "func (t *%s) Children() []any {\n", structName)
	parts := make([]string, len(slots))
	for i, s := range slots {
		parts[i] = "t." + s.Name
	}
	fmt.Fprintf(&g.buf, "\treturn []any{%s}\n", strings.Join(parts, ", "))
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)

	// SetChildren: rebuild via Make<Op> from a uniform []any.
	fmt.Fprintf(&g.buf, "func (t *%s) SetChildren(children []any) any {\n", structName)
	args := make([]string, len(slots))
	for i, s := range slots {
		args[i] = castExpr(fmt.Sprintf("children[%d]", i), s.GoType)
	}
	fmt.Fprintf(&g.buf, "\treturn %s(%s)\n", makeName, strings.Join(args, ", "))
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)
}

// goType maps a Gom type name to its Go counterpart.
//
//	int / long          → int64
//	boolean             → bool
//	String              → string
//	char                → rune
//	float               → float32
//	double              → float64
//	ATerm / ATermList   → aterm.ATerm / *aterm.ATermList
//	<own sort>          → that sort's Go interface
//	<other identifier>  → any  (cross-module import, deferred to a later phase)
func (g *gen) goType(name string, _ bool) string {
	switch name {
	case "int", "long":
		return "int64"
	case "boolean":
		return "bool"
	case "String":
		return "string"
	case "char":
		return "rune"
	case "float":
		return "float32"
	case "double":
		return "float64"
	case "ATerm":
		return "aterm.ATerm"
	case "ATermList":
		return "*aterm.ATermList"
	}
	if g.ownSort[name] {
		return name
	}
	return "any"
}

// isShared returns true if values of this Go type implement sharedobjects.Term —
// i.e. they are the interface type generated for one of our own sorts.
func (g *gen) isShared(goType string) bool {
	return g.ownSort[goType]
}

func exportedField(name string) string {
	if name == "" {
		return ""
	}
	runes := []rune(name)
	runes[0] = unicode.ToUpper(runes[0])
	return string(runes)
}

func unexported(name string) string {
	if name == "" {
		return name
	}
	runes := []rune(name)
	runes[0] = unicode.ToLower(runes[0])
	out := string(runes)
	// Avoid Go keyword collisions. The full list is the Go spec's
	// reserved words; any of these as a Gom slot name (e.g. `else` in
	// Conditional(cond:Expression, then:Expression, else:Expression))
	// must be renamed to `<keyword>_` for the smart constructor's
	// parameter list to be syntactically valid Go.
	if isGoKeyword(out) {
		return out + "_"
	}
	return out
}

func isGoKeyword(s string) bool {
	switch s {
	case "break", "case", "chan", "const", "continue", "default", "defer",
		"else", "fallthrough", "for", "func", "go", "goto", "if", "import",
		"interface", "map", "package", "range", "return", "select", "struct",
		"switch", "type", "var":
		return true
	}
	return false
}

// SortedSortNames is a tiny helper used by tests when they want to
// iterate sorts deterministically.
func SortedSortNames(mod gomast.GomModule) []string {
	sorts := moduleSorts(mod)
	names := make([]string, 0, len(sorts))
	for _, s := range sorts {
		names = append(names, sortName(s))
	}
	sort.Strings(names)
	return names
}
