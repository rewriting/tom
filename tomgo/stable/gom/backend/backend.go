// Package backend turns a parsed Gom module (a gomast.GomModule) into
// a self-contained Go package whose term constructors run through the
// shared-objects runtime (see package library/sharedobjects).
//
// Phase 3 of the porting plan replaced the hand-written V1 AST in
// internal/gom/ast.go with the canonical gomast AST. The backend now
// traverses gomast directly; no V1 types remain.
//
// Cross-module references (e.g. `imports Leaf` then a slot typed
// `l:Leaf`) are rendered as Go `any` in single-module mode because
// each module is generated as an isolated package. The batch mode
// (GenerateBatchToDir) tracks the union of sort names across modules
// and resolves slot types to local Go interfaces.
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

	"tom/tomgo/stable/library/gomast"
)

// Options controls code generation. Only the Go package name has a
// non-trivial default — by default it is derived from the last segment
// of the Gom module name (lower-cased, scrubbed of non-letters).
type Options struct {
	PackageName string // overrides the auto-derived package name
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
	for _, sec := range sectionList.Slots {
		pub, ok := sec.(*gomast.PublicSection)
		if !ok {
			continue
		}
		prodList := pub.ProductionList.(*gomast.ConcProductionProductionList)
		for _, p := range prodList.Slots {
			fn(p)
		}
	}
}

// sortName returns the name of the sort declared by a SortType production.
func sortName(prod *gomast.SortTypeProduction) string {
	return prod.Type.(*gomast.GomTypeGomType).Name
}

// sortAlternatives returns the alternatives of a SortType production
// in declaration order.
func sortAlternatives(prod *gomast.SortTypeProduction) []*gomast.AlternativeAlternative {
	altList := prod.AlternativeList.(*gomast.ConcAlternativeAlternativeList)
	out := make([]*gomast.AlternativeAlternative, 0, len(altList.Slots))
	for _, a := range altList.Slots {
		out = append(out, a.(*gomast.AlternativeAlternative))
	}
	return out
}

// alternativeFields returns the fields of an alternative.
func alternativeFields(alt *gomast.AlternativeAlternative) []gomast.Field {
	return alt.DomainList.(*gomast.ConcFieldFieldList).Slots
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
	goMod := fmt.Sprintf("module tomgen/%s\n\ngo 1.26\n\nrequire tom/tomgo v0.0.0\nreplace tom/tomgo => %s\n",
		opts.PackageName, locateTomgoRoot())
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

// GenerateBatchToDir compiles several Gom modules into a single Go
// package living at dir. Cross-module slot types resolve to the local
// Go interface because all sorts from every module share the same Go
// namespace. The resulting Go module has the path tomgen/<pkg>.
//
// This is the mode used to compile the real ADT in
// `src/tom/gom/adt/*.gom`, where every file imports symbols defined
// in the others.
func GenerateBatchToDir(modules []gomast.GomModule, opts Options, dir string) (string, error) {
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

	// Union of every sort across every module: cross-module slot type
	// resolution boils down to local Go-interface resolution.
	allSorts := map[string]bool{}
	owner := map[string]string{}
	// opSorts tracks the set of sorts each exported op name appears in.
	// If the same op (e.g. `Subterm`) is an alt of BOTH BQTerm and Term,
	// emitting Make<Op> without a sort suffix would collide.
	opSorts := map[string]map[string]bool{}
	for _, m := range modules {
		qname := moduleQualifiedName(m)
		for _, s := range moduleSorts(m) {
			name := sortName(s)
			if prev, dup := owner[name]; dup {
				return "", fmt.Errorf("sort %q is defined in both %s and %s", name, prev, qname)
			}
			owner[name] = qname
			allSorts[name] = true
			for _, alt := range sortAlternatives(s) {
				op := exportedField(alt.Name)
				if op == "" {
					op = "Op"
				}
				if opSorts[op] == nil {
					opSorts[op] = map[string]bool{}
				}
				opSorts[op][name] = true
			}
		}
	}
	makeCollides := map[string]bool{}
	for op, sorts := range opSorts {
		if len(sorts) > 1 {
			makeCollides[op] = true
		}
	}

	// Emit one .go file per module so that humans (and `git diff`) can
	// see which module produced what. The first file (lex order on
	// filename) carries the shared factory; the rest are pure types.
	var allRegistry []registryEntry
	for i, m := range modules {
		src, registry, gofmtErr := generateBatchBytes(m, allSorts, makeCollides, opts.PackageName, i == 0)
		// generateBatchBytes returns the unformatted source AND an error
		// when gofmt fails. Write the file regardless so the bug is
		// diagnosable, then propagate the error.
		name := strings.ToLower(safeFileName(moduleQualifiedName(m))) + ".go"
		if err := os.WriteFile(filepath.Join(abs, name), src, 0o644); err != nil {
			return "", err
		}
		if gofmtErr != nil {
			return "", fmt.Errorf("generating %s: %w", moduleQualifiedName(m), gofmtErr)
		}
		allRegistry = append(allRegistry, registry...)
	}
	if regBytes, err := emitRegistryGo(opts.PackageName, allRegistry); err != nil {
		return "", err
	} else if err := os.WriteFile(filepath.Join(abs, "registry_gen.go"), regBytes, 0o644); err != nil {
		return "", err
	}
	goMod := fmt.Sprintf("module tomgen/%s\n\ngo 1.26\n\nrequire tom/tomgo v0.0.0\nreplace tom/tomgo => %s\n",
		opts.PackageName, locateTomgoRoot())
	if err := os.WriteFile(filepath.Join(abs, "go.mod"), []byte(goMod), 0o644); err != nil {
		return "", err
	}
	return abs, nil
}

// emitRegistryGo writes the makeRegistry table that FromString uses to
// dispatch a printed op name to its Make<X> constructor. The result is
// gofmt'd; printed op names are sorted alphabetically for byte-stable
// output. Duplicate entries (same op name → same constructor) are
// silently de-duplicated.
func emitRegistryGo(pkgName string, entries []registryEntry) ([]byte, error) {
	seen := map[string]string{}
	for _, e := range entries {
		if prev, dup := seen[e.OpName]; dup && prev != e.MakeFn {
			return nil, fmt.Errorf("registry collision for op %q: %s vs %s", e.OpName, prev, e.MakeFn)
		}
		seen[e.OpName] = e.MakeFn
	}
	keys := make([]string, 0, len(seen))
	for k := range seen {
		keys = append(keys, k)
	}
	sort.Strings(keys)

	var buf bytes.Buffer
	fmt.Fprintln(&buf, "// Code generated by tom/tomgo backend. DO NOT EDIT.")
	fmt.Fprintln(&buf, "// Maps a term's printed operator name (the head of String()'s output)")
	fmt.Fprintln(&buf, "// to its Make<X> constructor. Used by FromString to rebuild a typed")
	fmt.Fprintln(&buf, "// term from its textual form.")
	fmt.Fprintf(&buf, "package %s\n\n", pkgName)
	fmt.Fprintln(&buf, "var makeRegistry = map[string]any{")
	for _, k := range keys {
		fmt.Fprintf(&buf, "\t%q: %s,\n", k, seen[k])
	}
	fmt.Fprintln(&buf, "}")
	return format.Source(buf.Bytes())
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

func generateBatchBytes(mod gomast.GomModule, allSorts map[string]bool, makeCollides map[string]bool, pkgName string, emitFactory bool) ([]byte, []registryEntry, error) {
	g := &gen{
		mod:          mod,
		ownSort:      allSorts,
		pkgName:      pkgName,
		makeCollides: makeCollides,
	}
	g.emitHeader()
	if emitFactory {
		g.emitFactory()
	}
	for _, s := range moduleSorts(mod) {
		g.emitSort(s)
	}
	formatted, err := format.Source(g.buf.Bytes())
	if err != nil {
		return g.buf.Bytes(), g.registry, fmt.Errorf("gofmt failed for %s: %w", moduleQualifiedName(mod), err)
	}
	return formatted, g.registry, nil
}

// locateTomgoRoot returns the absolute path to the tomgo/ module root,
// used in the generated `replace` directive. We rely on the well-known
// repository layout (the file we are reading from lives at
// `tomgo/internal/backend/backend.go`).
func locateTomgoRoot() string {
	// Walk up from the executable's working directory looking for a go.mod
	// whose module path is exactly "tom/tomgo".
	dir, err := os.Getwd()
	if err != nil {
		return ".."
	}
	for {
		gm, err := os.ReadFile(filepath.Join(dir, "go.mod"))
		if err == nil && bytes.Contains(gm, []byte("module tom/tomgo")) {
			return dir
		}
		parent := filepath.Dir(dir)
		if parent == dir {
			return ".."
		}
		dir = parent
	}
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
	if opts.PackageName == "" {
		opts.PackageName = defaultPackageName(mod)
	}
	g := &gen{
		mod:     mod,
		ownSort: map[string]bool{},
		pkgName: opts.PackageName,
	}
	for _, s := range moduleSorts(mod) {
		g.ownSort[sortName(s)] = true
	}
	g.emitHeader()
	g.emitFactory()
	for _, s := range moduleSorts(mod) {
		g.emitSort(s)
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

	// makeCollides reports whether the alt's exported op name appears
	// in more than one sort across the whole batch. When true, the
	// generator suffixes `Make<Op>` with `<Sort>` to disambiguate.
	// Computed once by GenerateBatchToDir and propagated into every
	// per-module gen. Nil ⇒ collision check disabled (single-file mode
	// where no batch-wide collision can arise anyway).
	makeCollides map[string]bool

	// registry collects (printedOpName → MakeFn) entries seen during
	// alternative emission. Used by GenerateBatchToDir to write a
	// companion registry_gen.go that powers FromString.
	registry []registryEntry
}

// registryEntry describes one row of the makeRegistry table.
type registryEntry struct {
	OpName string // printed name appearing in Term.String() output
	MakeFn string // identifier of the Make<X> constructor in this package
}

// reservedFieldName is the set of method names emitted on every alt
// struct (Hash, Equivalent, Duplicate, String). A Gom slot whose
// exported name collides with one of these would create a Go
// "field and method with the same name" error. We append "_" to the
// field name in that case.
var reservedFieldName = map[string]bool{
	"String":     true,
	"Hash":       true,
	"Equivalent": true,
	"Duplicate":  true,
}

func (g *gen) emitHeader() {
	fmt.Fprintf(&g.buf, "// Code generated by tom/tomgo backend from %s. DO NOT EDIT.\n", moduleQualifiedName(g.mod))
	fmt.Fprintf(&g.buf, "package %s\n\n", g.pkgName)
	fmt.Fprintln(&g.buf, "import (")
	fmt.Fprintln(&g.buf, `	"fmt"`)
	fmt.Fprintln(&g.buf, `	"strings"`)
	fmt.Fprintln(&g.buf, "")
	fmt.Fprintln(&g.buf, `	"tom/tomgo/stable/library/sharedobjects"`)
	fmt.Fprintln(&g.buf, `	sl "tom/tomgo/stable/library/sl"`)
	fmt.Fprintln(&g.buf, ")")
	fmt.Fprintln(&g.buf)
	fmt.Fprintln(&g.buf, "// underscore-prevent: tolerate unused imports if a module has no slots of these types.")
	fmt.Fprintln(&g.buf, "var _ = fmt.Sprintf")
	fmt.Fprintln(&g.buf, "var _ = strings.Join")
	fmt.Fprintln(&g.buf, "var _ sl.Strategy = nil")
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
	sortHooks := g.sortScopedHooks(name)

	fmt.Fprintf(&g.buf, "// %s is the Go interface backing the Gom sort %s.\n", name, name)
	fmt.Fprintf(&g.buf, "type %s interface {\n", name)
	fmt.Fprintln(&g.buf, "	sharedobjects.Term")
	fmt.Fprintf(&g.buf, "	is%s()\n", name)
	for _, h := range sortHooks {
		if decl := interfaceDeclForHook(h); decl != "" {
			fmt.Fprintf(&g.buf, "\t%s\n", decl)
		}
	}
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)
	for _, alt := range sortAlternatives(prod) {
		g.emitAlternative(name, alt)
		for _, h := range sortHooks {
			g.emitHookImplOnAlt(prod, alt, h)
		}
	}
}

// sortScopedHooks returns module-level hooks whose scope is "sort"
// and whose pointcut is the given sort name.
func (g *gen) sortScopedHooks(sortNm string) []*gomast.HookProduction {
	var out []*gomast.HookProduction
	for _, h := range moduleHooks(g.mod) {
		if hookScopeName(h) == "sort" && h.Name == sortNm {
			out = append(out, h)
		}
	}
	return out
}

// interfaceDeclForHook returns the Go method-set entry contributed by a
// known sort-scoped hook (e.g. `ContainsTomCode() bool`). Unknown hooks
// contribute nothing — the body Tom code is dropped and a `// unknown
// hook` marker is left in the impl section instead.
func interfaceDeclForHook(h *gomast.HookProduction) string {
	if k := recognisedKnownHook(h); k != nil {
		return k.interfaceDecl
	}
	return ""
}

// emitHookImplOnAlt emits the body of the method contributed by hook
// `h` on the struct generated for alt of sort prod. Unknown hooks emit
// a short comment so the gap is visible in the generated source.
func (g *gen) emitHookImplOnAlt(prod *gomast.SortTypeProduction, alt *gomast.AlternativeAlternative, h *gomast.HookProduction) {
	k := recognisedKnownHook(h)
	if k == nil {
		fmt.Fprintf(&g.buf, "// unsupported hook for sort %s alt %s: %s:%s (body dropped)\n\n",
			sortName(prod), alt.Name, h.Name, hookKindName(h))
		return
	}
	structName := exportedField(alt.Name) + sortName(prod)
	k.emitImpl(&g.buf, g, prod, alt, structName)
}

// knownHook bundles the metadata and emission routines for one
// recognised hook idiom. The table `knownHookTable` is the registry —
// extend it as more hooks become necessary.
//
// Two injection points are supported:
//
//  1. emitImpl     — adds an extra method on every alt struct of the
//                    point-cut sort. Used by sort-scope `block` hooks
//                    such as Objects/HookList:block which contributes
//                    `ContainsTomCode() bool`.
//  2. emitMakePrologue — injects Go code at the top of the alt's smart
//                    constructor (before the standard hash-cons body).
//                    The prologue may early-return, panic, or rebind
//                    `args` / parameters to a normalised form. Used by
//                    operator-scope `AU` / `make` / `make_insert` hooks
//                    and by rewritten module-scope `rules` hooks.
//
// hookSlot is the projection of the local `slot` struct used inside
// emitAlternative; it is passed to emitMakePrologue so the prologue
// can reason about the alt's shape without depending on package internals.
type knownHook struct {
	module        string // module the hook lives in, e.g. "Objects"
	scope         string // "sort"|"module"|"operator"
	pointCut      string // sort name (for sort scope) or alt name (for operator scope)
	kind          string // "block", "AU", "make", "make_insert", "rules", …
	interfaceDecl string // method signature for the sort interface (block hooks only)
	emitImpl      func(buf *bytes.Buffer, g *gen, prod *gomast.SortTypeProduction, alt *gomast.AlternativeAlternative, structName string)
	emitMakePrologue func(buf *bytes.Buffer, g *gen, alt *gomast.AlternativeAlternative, sortName, structName string, slots []hookSlot, isVariadic bool)
}

// hookSlot mirrors the local slot struct used by emitAlternative.
type hookSlot struct {
	Name   string // exported Go field name
	GoType string
	IsVar  bool
}

// recognisedKnownHook returns the entry from knownHookTable matching
// the hook, or nil. The module is taken from g.mod when available; for
// the V1 table we keep the match generous (module name is optional).
func recognisedKnownHook(h *gomast.HookProduction) *knownHook {
	scope := hookScopeName(h)
	kind := hookKindName(h)
	for i := range knownHookTable {
		k := &knownHookTable[i]
		if k.scope == scope && k.pointCut == h.Name && k.kind == kind {
			return k
		}
	}
	return nil
}

// makeProloguesForAlt returns every knownHookTable entry whose
// emitMakePrologue applies to the given alt. The match is by
// (module, pointCut) — module is matched against the current
// module's qualified name to keep entries from different ADTs from
// stepping on each other's toes.
func (g *gen) makeProloguesForAlt(alt *gomast.AlternativeAlternative) []*knownHook {
	modName := moduleQualifiedName(g.mod)
	var out []*knownHook
	for i := range knownHookTable {
		k := &knownHookTable[i]
		if k.emitMakePrologue == nil {
			continue
		}
		if k.module != "" && k.module != modName {
			continue
		}
		if k.pointCut != alt.Name {
			continue
		}
		out = append(out, k)
	}
	return out
}

// knownHookTable maps the (scope,pointCut,kind) triples this backend
// can lower to Go. The body Tom code is NOT parsed — we trust the
// table to describe what the Java reference would do.
//
// Entries fall in two groups:
//
//  - emitImpl          adds a method on every alt of the point-cut
//                      sort (sort-scope `block` hooks).
//  - emitMakePrologue  injects code at the top of the alt's smart
//                      constructor (operator-scope AU/make/make_insert,
//                      and module-scope `rules` hooks rewritten to
//                      per-operator prologues).
//
// The module-scope `rules` hooks in src/tom/engine/adt/{Code,TomExpression}.gom
// each compile to TWO entries (one per rewrite rule), because they're
// dispatched on the LHS root operator name.
var knownHookTable = []knownHook{
	{
		// `sort HookList:block()` from src/tom/gom/adt/Objects.gom adds
		// `boolean containsTomCode()` to every alternative of HookList.
		// In Go we expose it as a `ContainsTomCode() bool` method whose
		// body walks the children of a ConcHook list and returns true
		// as soon as one of them is a MakeHook/MakeBeforeHook/BlockHook
		// with HasTomCode == true.
		module:        "Objects",
		scope:         "sort",
		pointCut:      "HookList",
		kind:          "block",
		interfaceDecl: "ContainsTomCode() bool",
		emitImpl: func(buf *bytes.Buffer, g *gen, prod *gomast.SortTypeProduction, alt *gomast.AlternativeAlternative, structName string) {
			fmt.Fprintf(buf, "// ContainsTomCode reports whether any child hook is a\n")
			fmt.Fprintf(buf, "// MakeHook/MakeBeforeHook/BlockHook flagged HasTomCode=true.\n")
			fmt.Fprintf(buf, "// Lowered from `sort HookList:block()` in Objects.gom.\n")
			fmt.Fprintf(buf, "func (t *%s) ContainsTomCode() bool {\n", structName)
			if alternativeIsVariadic(alt) {
				fmt.Fprintf(buf, "\tfor _, h := range t.Slots {\n")
				fmt.Fprintf(buf, "\t\tswitch ht := h.(type) {\n")
				for _, target := range []string{"MakeHook", "MakeBeforeHook", "BlockHook"} {
					fmt.Fprintf(buf, "\t\tcase *%sHook:\n", target)
					fmt.Fprintf(buf, "\t\t\tif ht.HasTomCode { return true }\n")
				}
				fmt.Fprintf(buf, "\t\t}\n")
				fmt.Fprintf(buf, "\t}\n")
				fmt.Fprintf(buf, "\treturn false\n")
			} else {
				fmt.Fprintln(buf, "\treturn false")
			}
			fmt.Fprintln(buf, "}")
			fmt.Fprintln(buf)
		},
	},

	// --------------------------------------------------------------------
	// src/tom/engine/adt/TomConstraint.gom — AU hooks
	// --------------------------------------------------------------------
	{
		// AndConstraint:AU() { `TrueConstraint() }  — associative,
		// unit = TrueConstraint().
		module:           "TomConstraint",
		scope:            "operator",
		pointCut:         "AndConstraint",
		kind:             "AU",
		emitMakePrologue: emitAUPrologue("AndConstraint", "Constraint", "TrueConstraint"),
	},
	{
		// OrConstraint:AU() { `FalseConstraint() }  — associative,
		// unit = FalseConstraint().
		module:           "TomConstraint",
		scope:            "operator",
		pointCut:         "OrConstraint",
		kind:             "AU",
		emitMakePrologue: emitAUPrologue("OrConstraint", "Constraint", "FalseConstraint"),
	},
	{
		// OrConstraintDisjunction:AU() { }  — associative, NO unit.
		module:           "TomConstraint",
		scope:            "operator",
		pointCut:         "OrConstraintDisjunction",
		kind:             "AU",
		emitMakePrologue: emitAUPrologue("OrConstraintDisjunction", "Constraint", ""),
	},

	// --------------------------------------------------------------------
	// src/tom/engine/adt/Code.gom — module:rules() rewrites, split into
	// per-LHS-operator prologues.
	// --------------------------------------------------------------------
	{
		// InstructionToCode(CodeToInstruction(t)) -> t
		module:           "Code",
		scope:            "operator",
		pointCut:         "InstructionToCode",
		kind:             "rules-rewrite",
		emitMakePrologue: emitInversePairPrologue(
			"InstructionToCode", "AstInstruction",
			"CodeToInstruction", "Instruction", "Code",
		),
	},
	{
		// CodeToInstruction(InstructionToCode(t)) -> t
		// NB: the rule is declared in Code.gom (`module Code:rules()`),
		// but the LHS operator CodeToInstruction is an alt of
		// Instruction (defined in TomInstruction.gom). The module field
		// here is the operator's owning module so the prologue actually
		// fires when generating TomInstruction.gom.
		module:           "TomInstruction",
		scope:            "operator",
		pointCut:         "CodeToInstruction",
		kind:             "rules-rewrite",
		emitMakePrologue: emitInversePairPrologue(
			"CodeToInstruction", "Code",
			"InstructionToCode", "Code", "AstInstruction",
		),
	},

	// --------------------------------------------------------------------
	// src/tom/engine/adt/TomExpression.gom — similar rewrite pair.
	// --------------------------------------------------------------------
	{
		// BQTermToExpression(ExpressionToBQTerm(t)) -> t
		module:           "TomExpression",
		scope:            "operator",
		pointCut:         "BQTermToExpression",
		kind:             "rules-rewrite",
		emitMakePrologue: emitInversePairPrologue(
			"BQTermToExpression", "AstTerm",
			"ExpressionToBQTerm", "BQTerm", "Exp",
		),
	},
	{
		// ExpressionToBQTerm(BQTermToExpression(t)) -> t
		// Like the previous entry: declared in TomExpression.gom, but
		// the operator ExpressionToBQTerm is an alt of BQTerm (defined
		// in Code.gom), so the module filter points to Code.
		module:           "Code",
		scope:            "operator",
		pointCut:         "ExpressionToBQTerm",
		kind:             "rules-rewrite",
		emitMakePrologue: emitInversePairPrologue(
			"ExpressionToBQTerm", "Exp",
			"BQTermToExpression", "Expression", "AstTerm",
		),
	},

	// --------------------------------------------------------------------
	// src/tom/engine/adt/TomExpression.gom — Cast:make rejects "unknown type".
	// --------------------------------------------------------------------
	{
		module:   "TomExpression",
		scope:    "operator",
		pointCut: "Cast",
		kind:     "make",
		emitMakePrologue: func(buf *bytes.Buffer, g *gen, alt *gomast.AlternativeAlternative, sortName, structName string, slots []hookSlot, isVariadic bool) {
			if !g.ownSort["TomType"] {
				fmt.Fprintf(buf, "\t// Cast:make hook skipped: sort TomType not in this package.\n")
				return
			}
			fmt.Fprintf(buf, "\t// Cast:make hook (TomExpression.gom): reject Type(_,\"unknown type\",_)\n")
			fmt.Fprintf(buf, "\t// with empty TypeOptions and EmptyTargetLanguageType.\n")
			fmt.Fprintf(buf, "\tif t, ok := astType.(*TypeTomType); ok {\n")
			fmt.Fprintf(buf, "\t\tif opts, ok := t.TypeOptions.(*ConcTypeOptionTypeOptionList); ok && len(opts.Slots) == 0 {\n")
			fmt.Fprintf(buf, "\t\t\tif _, ok := t.TlType.(*EmptyTargetLanguageTypeTargetLanguageType); ok {\n")
			fmt.Fprintf(buf, "\t\t\t\tif t.TomType == \"unknown type\" {\n")
			fmt.Fprintf(buf, "\t\t\t\t\tpanic(\"bad cast\")\n")
			fmt.Fprintf(buf, "\t\t\t\t}\n")
			fmt.Fprintf(buf, "\t\t\t}\n")
			fmt.Fprintf(buf, "\t\t}\n")
			fmt.Fprintf(buf, "\t}\n")
		},
	},

	// --------------------------------------------------------------------
	// src/tom/engine/adt/TomInstruction.gom — concInstruction make_insert
	// flattens AbstractBlock(InstList) into the surrounding list.
	// --------------------------------------------------------------------
	{
		module:   "TomInstruction",
		scope:    "operator",
		pointCut: "concInstruction",
		kind:     "make_insert",
		emitMakePrologue: func(buf *bytes.Buffer, g *gen, alt *gomast.AlternativeAlternative, sortName, structName string, slots []hookSlot, isVariadic bool) {
			if !g.ownSort["Instruction"] {
				fmt.Fprintf(buf, "\t// concInstruction:make_insert hook skipped: Instruction not in package.\n")
				return
			}
			fmt.Fprintf(buf, "\t// concInstruction:make_insert hook (TomInstruction.gom): splice the\n")
			fmt.Fprintf(buf, "\t// children of any AbstractBlock arg into the surrounding list.\n")
			fmt.Fprintf(buf, "\t{\n")
			fmt.Fprintf(buf, "\t\tvar flat []Instruction\n")
			fmt.Fprintf(buf, "\t\tfor _, e := range args {\n")
			fmt.Fprintf(buf, "\t\t\tif ab, ok := e.(*AbstractBlockInstruction); ok {\n")
			fmt.Fprintf(buf, "\t\t\t\tif inner, ok := ab.InstList.(*ConcInstructionInstructionList); ok {\n")
			fmt.Fprintf(buf, "\t\t\t\t\tflat = append(flat, inner.Slots...)\n")
			fmt.Fprintf(buf, "\t\t\t\t\tcontinue\n")
			fmt.Fprintf(buf, "\t\t\t\t}\n")
			fmt.Fprintf(buf, "\t\t\t}\n")
			fmt.Fprintf(buf, "\t\t\tflat = append(flat, e)\n")
			fmt.Fprintf(buf, "\t\t}\n")
			fmt.Fprintf(buf, "\t\targs = flat\n")
			fmt.Fprintf(buf, "\t}\n")
		},
	},

	// --------------------------------------------------------------------
	// src/tom/engine/adt/TomName.gom — NameNumber:make simplifies
	// NameNumber(PositionName(concTomNumber([Position(_)]))) → Position(_).
	// --------------------------------------------------------------------
	{
		module:   "TomName",
		scope:    "operator",
		pointCut: "NameNumber",
		kind:     "make",
		emitMakePrologue: func(buf *bytes.Buffer, g *gen, alt *gomast.AlternativeAlternative, sortName, structName string, slots []hookSlot, isVariadic bool) {
			if !g.ownSort["TomName"] || !g.ownSort["TomNumber"] {
				fmt.Fprintf(buf, "\t// NameNumber:make hook skipped: TomName/TomNumber not in package.\n")
				return
			}
			fmt.Fprintf(buf, "\t// NameNumber:make hook (TomName.gom): when astName is\n")
			fmt.Fprintf(buf, "\t// PositionName(concTomNumber(p@Position[])), return p.\n")
			fmt.Fprintf(buf, "\tif pn, ok := astName.(*PositionNameTomName); ok {\n")
			fmt.Fprintf(buf, "\t\tif list, ok := pn.NumberList.(*ConcTomNumberTomNumberList); ok && len(list.Slots) == 1 {\n")
			fmt.Fprintf(buf, "\t\t\tif _, ok := list.Slots[0].(*PositionTomNumber); ok {\n")
			fmt.Fprintf(buf, "\t\t\t\treturn list.Slots[0]\n")
			fmt.Fprintf(buf, "\t\t\t}\n")
			fmt.Fprintf(buf, "\t\t}\n")
			fmt.Fprintf(buf, "\t}\n")
		},
	},

	// --------------------------------------------------------------------
	// src/tom/engine/adt/TomName.gom — concTomNumber:make_insert splices
	// NameNumber(PositionName(concTomNumber(p*))) into the list.
	// --------------------------------------------------------------------
	{
		module:   "TomName",
		scope:    "operator",
		pointCut: "concTomNumber",
		kind:     "make_insert",
		emitMakePrologue: func(buf *bytes.Buffer, g *gen, alt *gomast.AlternativeAlternative, sortName, structName string, slots []hookSlot, isVariadic bool) {
			if !g.ownSort["TomNumber"] || !g.ownSort["TomName"] {
				fmt.Fprintf(buf, "\t// concTomNumber:make_insert hook skipped: types not in package.\n")
				return
			}
			fmt.Fprintf(buf, "\t// concTomNumber:make_insert hook (TomName.gom): splice the children of\n")
			fmt.Fprintf(buf, "\t// NameNumber(PositionName(concTomNumber(p*))) into the surrounding list.\n")
			fmt.Fprintf(buf, "\t{\n")
			fmt.Fprintf(buf, "\t\tvar flat []TomNumber\n")
			fmt.Fprintf(buf, "\t\tfor _, e := range args {\n")
			fmt.Fprintf(buf, "\t\t\tif nn, ok := e.(*NameNumberTomNumber); ok {\n")
			fmt.Fprintf(buf, "\t\t\t\tif pn, ok := nn.AstName.(*PositionNameTomName); ok {\n")
			fmt.Fprintf(buf, "\t\t\t\t\tif inner, ok := pn.NumberList.(*ConcTomNumberTomNumberList); ok {\n")
			fmt.Fprintf(buf, "\t\t\t\t\t\tflat = append(flat, inner.Slots...)\n")
			fmt.Fprintf(buf, "\t\t\t\t\t\tcontinue\n")
			fmt.Fprintf(buf, "\t\t\t\t\t}\n")
			fmt.Fprintf(buf, "\t\t\t\t}\n")
			fmt.Fprintf(buf, "\t\t\t}\n")
			fmt.Fprintf(buf, "\t\t\tflat = append(flat, e)\n")
			fmt.Fprintf(buf, "\t\t}\n")
			fmt.Fprintf(buf, "\t\targs = flat\n")
			fmt.Fprintf(buf, "\t}\n")
		},
	},
}

// emitAUPrologue builds the make-prologue for `Op:AU() { unit }` hooks.
// op       = the variadic operator name (e.g. "AndConstraint").
// sort     = the sort name (e.g. "Constraint").
// unit     = the smart-constructor to call for an empty arg list
//            (e.g. "TrueConstraint"), or "" if there is no unit and the
//            empty case stays empty.
//
// The emitted prologue always flattens nested `op` arguments
// (associativity), and additionally short-circuits the zero-arg case
// to the unit when one is supplied.
func emitAUPrologue(op, sort, unit string) func(buf *bytes.Buffer, g *gen, alt *gomast.AlternativeAlternative, sortName, structName string, slots []hookSlot, isVariadic bool) {
	return func(buf *bytes.Buffer, g *gen, alt *gomast.AlternativeAlternative, sortName, structName string, slots []hookSlot, isVariadic bool) {
		if !g.ownSort[sort] {
			fmt.Fprintf(buf, "\t// %s:AU hook skipped: sort %s not in package.\n", op, sort)
			return
		}
		structForOp := op + sort
		fmt.Fprintf(buf, "\t// %s:AU hook (TomConstraint.gom): flatten nested %s arguments", op, op)
		if unit != "" {
			fmt.Fprintf(buf, ", unit = %s().\n", unit)
		} else {
			fmt.Fprintf(buf, " (no unit).\n")
		}
		fmt.Fprintf(buf, "\t{\n")
		fmt.Fprintf(buf, "\t\tvar flat []%s\n", sort)
		fmt.Fprintf(buf, "\t\tfor _, a := range args {\n")
		fmt.Fprintf(buf, "\t\t\tif nested, ok := a.(*%s); ok {\n", structForOp)
		fmt.Fprintf(buf, "\t\t\t\tflat = append(flat, nested.Slots...)\n")
		fmt.Fprintf(buf, "\t\t\t} else {\n")
		fmt.Fprintf(buf, "\t\t\t\tflat = append(flat, a)\n")
		fmt.Fprintf(buf, "\t\t\t}\n")
		fmt.Fprintf(buf, "\t\t}\n")
		if unit != "" {
			// Drop unit elements after flattening. Java's AU hook
			// (HookTypeExpander.java:569) applies this at each
			// Cons*.make: "if (head == userNeutral) return tail;
			// if (tail == userNeutral) return head;" — i.e. each
			// unit-equal element is absorbed. Mirror that at the
			// variadic level so AndConstraint(MC, TrueConstraint())
			// → MC, AndConstraint() → TrueConstraint(), etc.
			fmt.Fprintf(buf, "\t\tunit := Make%s()\n", unit)
			fmt.Fprintf(buf, "\t\tfiltered := flat[:0]\n")
			fmt.Fprintf(buf, "\t\tfor _, a := range flat {\n")
			fmt.Fprintf(buf, "\t\t\tif a != unit {\n")
			fmt.Fprintf(buf, "\t\t\t\tfiltered = append(filtered, a)\n")
			fmt.Fprintf(buf, "\t\t\t}\n")
			fmt.Fprintf(buf, "\t\t}\n")
			fmt.Fprintf(buf, "\t\tflat = filtered\n")
			fmt.Fprintf(buf, "\t\tif len(flat) == 0 {\n")
			fmt.Fprintf(buf, "\t\t\treturn unit\n")
			fmt.Fprintf(buf, "\t\t}\n")
			fmt.Fprintf(buf, "\t\tif len(flat) == 1 {\n")
			fmt.Fprintf(buf, "\t\t\treturn flat[0]\n")
			fmt.Fprintf(buf, "\t\t}\n")
		}
		fmt.Fprintf(buf, "\t\targs = flat\n")
		fmt.Fprintf(buf, "\t}\n")
	}
}

// emitInversePairPrologue builds the prologue for one rewrite of the
// form `LhsRoot(LhsInner(t)) -> t`, where:
//   lhsRoot       = name of the alt being constructed (e.g. "InstructionToCode")
//   lhsRootParam  = name of the single slot on lhsRoot (e.g. "AstInstruction")
//                   (this is the Go parameter name in the generated Make;
//                   we unexport it below)
//   lhsInner      = name of the inner alt (e.g. "CodeToInstruction")
//   lhsInnerSort  = sort name of lhsInner (e.g. "Instruction") — used to
//                   compose the struct type `<lhsInner><lhsInnerSort>`
//   innerSlot     = exported name of the slot on lhsInner that carries
//                   the t we want to return (e.g. "Code")
func emitInversePairPrologue(lhsRoot, lhsRootParam, lhsInner, lhsInnerSort, innerSlot string) func(buf *bytes.Buffer, g *gen, alt *gomast.AlternativeAlternative, sortName, structName string, slots []hookSlot, isVariadic bool) {
	return func(buf *bytes.Buffer, g *gen, alt *gomast.AlternativeAlternative, sortName, structName string, slots []hookSlot, isVariadic bool) {
		if !g.ownSort[lhsInnerSort] {
			fmt.Fprintf(buf, "\t// %s rewrite-rule hook skipped: sort %s not in package.\n", lhsRoot, lhsInnerSort)
			return
		}
		paramName := unexported(lhsRootParam)
		innerStruct := lhsInner + lhsInnerSort
		fmt.Fprintf(buf, "\t// Rewrite rule (module:rules()): %s(%s(t)) -> t.\n", lhsRoot, lhsInner)
		fmt.Fprintf(buf, "\tif inner, ok := %s.(*%s); ok {\n", paramName, innerStruct)
		fmt.Fprintf(buf, "\t\treturn inner.%s\n", innerSlot)
		fmt.Fprintf(buf, "\t}\n")
	}
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
	opGo := exportedField(alt.Name)
	if opGo == "" {
		opGo = "Op"
	}
	structName := opGo + sortNm
	opName := alt.Name
	// Disambiguate `Make<Op>` across sorts when the same Op appears in
	// multiple sorts (e.g. `Subterm` is an alt of both BQTerm and Term
	// in the TOM engine ADT). The unique struct name `<Op><Sort>` is
	// already correct; we just need to lift the suffix into the Make
	// function name too.
	makeName := "Make" + opGo
	if g.makeCollides != nil && g.makeCollides[opGo] {
		makeName = "Make" + opGo + sortNm
	}
	// Record the (printed op name → constructor) mapping so the package's
	// FromString helper can look up the right Make<X> at parse time.
	g.registry = append(g.registry, registryEntry{OpName: opName, MakeFn: makeName})

	// Lower the gomast Fields into a flat slot list that the rest of
	// this function manipulates directly. This shape keeps the emit
	// code straightforward and matches what the V1 backend used to
	// produce, by design.
	type slot struct {
		Name   string
		GoType string
		IsVar  bool
	}
	var slots []slot
	isVariadic := alternativeIsVariadic(alt)
	fields := alternativeFields(alt)
	if isVariadic {
		typeName := fieldTypeName(fields[0])
		slots = append(slots, slot{Name: "Slots", GoType: g.goType(typeName, false), IsVar: true})
	} else {
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
			slots = append(slots, slot{Name: name, GoType: g.goType(fieldTypeName(f), false)})
		}
	}

	// Struct definition.
	fmt.Fprintf(&g.buf, "// %s is the term type for the alternative `%s(...)` of sort %s.\n", structName, opName, sortNm)
	fmt.Fprintf(&g.buf, "type %s struct {\n", structName)
	if isVariadic {
		fmt.Fprintf(&g.buf, "\t%s []%s\n", slots[0].Name, slots[0].GoType)
	} else {
		for _, s := range slots {
			fmt.Fprintf(&g.buf, "\t%s %s\n", s.Name, s.GoType)
		}
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
	if !isVariadic && len(slots) == 0 {
		fmt.Fprintf(&g.buf, "\t_, ok := other.(*%s)\n", structName)
		fmt.Fprintln(&g.buf, "\treturn ok")
		fmt.Fprintln(&g.buf, "}")
		fmt.Fprintln(&g.buf)
		goto afterEquivalent
	}
	fmt.Fprintf(&g.buf, "\to, ok := other.(*%s)\n", structName)
	fmt.Fprintln(&g.buf, "\tif !ok {")
	fmt.Fprintln(&g.buf, "\t\treturn false")
	fmt.Fprintln(&g.buf, "\t}")
	if isVariadic {
		varName := slots[0].Name
		fmt.Fprintf(&g.buf, "\tif len(t.%s) != len(o.%s) {\n", varName, varName)
		fmt.Fprintln(&g.buf, "\t\treturn false")
		fmt.Fprintln(&g.buf, "\t}")
		fmt.Fprintf(&g.buf, "\tfor i := range t.%s {\n", varName)
		fmt.Fprintf(&g.buf, "\t\tif t.%s[i] != o.%s[i] { return false }\n", varName, varName)
		fmt.Fprintln(&g.buf, "\t}")
	} else {
		for _, s := range slots {
			fmt.Fprintf(&g.buf, "\tif t.%s != o.%s { return false }\n", s.Name, s.Name)
		}
	}
	fmt.Fprintln(&g.buf, "\treturn true")
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)
afterEquivalent:

	// Duplicate.
	fmt.Fprintf(&g.buf, "func (t *%s) Duplicate() sharedobjects.Term {\n", structName)
	if isVariadic {
		fmt.Fprintf(&g.buf, "\tcp := append([]%s(nil), t.%s...)\n", slots[0].GoType, slots[0].Name)
		fmt.Fprintf(&g.buf, "\treturn &%s{%s: cp, hash: t.hash}\n", structName, slots[0].Name)
	} else {
		fmt.Fprintf(&g.buf, "\tclone := *t\n")
		fmt.Fprintln(&g.buf, "\treturn &clone")
	}
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)

	// String. The format specifier per slot mirrors what the reference
	// Gom Java backend's toStringBuilder emits:
	//   - string → %s + sharedobjects.JavaEscape  (AT-format escapes;
	//              backtick goes to \140 etc., matches Java aterm output)
	//   - rune   → %q (close enough to Java's single-char quote)
	//   - else   → %v
	verbFor := func(goType string) string {
		switch goType {
		case "string":
			return "%s"
		case "rune":
			return "%q"
		}
		return "%v"
	}
	// argExpr returns the Go expression that should be passed to Sprintf
	// for a given slot. String slots go through sharedobjects.JavaEscape
	// so the output is byte-identical to Java's aterm.AFunImpl.toString().
	argExpr := func(name, goType string) string {
		if goType == "string" {
			return "sharedobjects.JavaEscape(" + name + ")"
		}
		return name
	}
	fmt.Fprintf(&g.buf, "func (t *%s) String() string {\n", structName)
	if isVariadic {
		v := verbFor(slots[0].GoType)
		fmt.Fprintf(&g.buf, "\tparts := make([]string, len(t.%s))\n", slots[0].Name)
		fmt.Fprintf(&g.buf, "\tfor i, v := range t.%s {\n", slots[0].Name)
		fmt.Fprintf(&g.buf, "\t\tparts[i] = fmt.Sprintf(%q, %s)\n", v, argExpr("v", slots[0].GoType))
		fmt.Fprintln(&g.buf, "\t}")
		fmt.Fprintf(&g.buf, "\treturn %q + \"(\" + strings.Join(parts, \",\") + \")\"\n", opName)
	} else if len(slots) == 0 {
		fmt.Fprintf(&g.buf, "\treturn %q + \"()\"\n", opName)
	} else {
		var verbs []string
		for _, s := range slots {
			verbs = append(verbs, verbFor(s.GoType))
		}
		format := opName + "(" + strings.Join(verbs, ",") + ")"
		fmt.Fprintf(&g.buf, "\treturn fmt.Sprintf(%q", format)
		for _, s := range slots {
			fmt.Fprintf(&g.buf, ", %s", argExpr("t."+s.Name, s.GoType))
		}
		fmt.Fprintln(&g.buf, ")")
	}
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)

	// Visitable methods (Phase 6.5). Emit Children/SetChildren/
	// ChildCount/ChildAt/SetChildAt so this struct can be walked by
	// strategies in stable/library/sl. The shape mirrors what Java's
	// Gom backend emits on the corresponding *.java files: every slot
	// is a child, primitives are returned as-is (Go's `any` already
	// accommodates them — no VisitableBuiltin boxing needed).
	//
	// SetChildAt / SetChildren rebuild the term via the smart
	// constructor so canonical sharing (hash-cons) is preserved.
	// Callers must supply children whose concrete types match the
	// slot signatures, exactly as Java's reflective Visitable contract
	// requires.
	visitableSlots := make([]hookSlot, 0, len(slots))
	for _, s := range slots {
		visitableSlots = append(visitableSlots, hookSlot{Name: s.Name, GoType: s.GoType, IsVar: s.IsVar})
	}
	g.emitVisitableMethods(structName, makeName, visitableSlots, isVariadic)

	// Smart constructor. The Go-side function name is exported (Pascal
	// case); the canonical symbol passed to the hash mixer keeps the
	// original spelling so sharing stays correct.
	fmt.Fprintf(&g.buf, "// %s builds the canonical (shared) %s term.\n", makeName, opName)
	// Convert slots to the exported hookSlot shape so prologues can
	// reason about the alt's structure.
	prologueSlots := make([]hookSlot, 0, len(slots))
	for _, s := range slots {
		prologueSlots = append(prologueSlots, hookSlot{Name: s.Name, GoType: s.GoType, IsVar: s.IsVar})
	}
	prologues := g.makeProloguesForAlt(alt)
	if isVariadic {
		fmt.Fprintf(&g.buf, "func %s(args ...%s) %s {\n", makeName, slots[0].GoType, sortNm)
		for _, p := range prologues {
			p.emitMakePrologue(&g.buf, g, alt, sortNm, structName, prologueSlots, isVariadic)
		}
		fmt.Fprintf(&g.buf, "\thashes := make([]uint32, 0, len(args))\n")
		fmt.Fprintf(&g.buf, "\tfor _, a := range args {\n")
		if g.isShared(slots[0].GoType) {
			fmt.Fprintln(&g.buf, "\t\thashes = append(hashes, a.Hash())")
		} else {
			fmt.Fprintf(&g.buf, "\t\thashes = append(hashes, sharedobjects.StringHash(fmt.Sprintf(\"%%v\", a)))\n")
		}
		fmt.Fprintln(&g.buf, "\t}")
		fmt.Fprintf(&g.buf, "\tproto := &%s{%s: args, hash: sharedobjects.MixSymbol(sharedobjects.StringHash(%q), hashes)}\n",
			structName, slots[0].Name, opName)
		fmt.Fprintf(&g.buf, "\treturn factory.Build(proto).(*%s)\n", structName)
		fmt.Fprintln(&g.buf, "}")
	} else {
		var params []string
		for _, s := range slots {
			params = append(params, fmt.Sprintf("%s %s", unexported(s.Name), s.GoType))
		}
		fmt.Fprintf(&g.buf, "func %s(%s) %s {\n", makeName, strings.Join(params, ", "), sortNm)
		for _, p := range prologues {
			p.emitMakePrologue(&g.buf, g, alt, sortNm, structName, prologueSlots, isVariadic)
		}
		fmt.Fprintf(&g.buf, "\thashes := []uint32{")
		first := true
		for _, s := range slots {
			if !first {
				fmt.Fprint(&g.buf, ", ")
			}
			first = false
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
		fmt.Fprintf(&g.buf, "\treturn factory.Build(proto).(*%s)\n", structName)
		fmt.Fprintln(&g.buf, "}")
	}
	fmt.Fprintln(&g.buf)
	g.emitStrategyClasses(sortNm, opGo, opName, structName, len(slots), isVariadic)
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
	if g.makeCollides != nil && g.makeCollides[opGo] {
		isName = "Is" + opGo + sortNm
		visitName = "Visit" + opGo + sortNm
	}

	// Is<Alt>: predicate strategy.
	fmt.Fprintf(&g.buf, "// %s is the `Is_%s` predicate strategy: succeeds (returns subject\n", isName, opName)
	fmt.Fprintf(&g.buf, "// unchanged) when subject has the `%s` shape, otherwise fails with\n", opName)
	fmt.Fprintln(&g.buf, "// sl.ErrVisitFailure.")
	fmt.Fprintf(&g.buf, "type %s struct{}\n\n", isName)
	fmt.Fprintf(&g.buf, "func (%s) VisitLight(subject any, _ sl.Introspector) (any, error) {\n", isName)
	fmt.Fprintf(&g.buf, "\tif _, ok := subject.(*%s); ok {\n", structName)
	fmt.Fprintln(&g.buf, "\t\treturn subject, nil")
	fmt.Fprintln(&g.buf, "\t}")
	fmt.Fprintln(&g.buf, "\treturn subject, sl.ErrVisitFailure")
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintf(&g.buf, "func (%s) ChildCount() int                  { return 0 }\n", isName)
	fmt.Fprintf(&g.buf, "func (%s) ChildAt(int) sl.Strategy          { panic(\"%s: no children\") }\n", isName, isName)
	fmt.Fprintf(&g.buf, "func (%s) SetChildAt(int, sl.Strategy)      { panic(\"%s: no children\") }\n", isName, isName)
	fmt.Fprintln(&g.buf)

	// Visit<Alt>: slot-visit strategy.
	fmt.Fprintf(&g.buf, "// %s is the `_%s` slot-visit strategy: when subject is `%s`,\n", visitName, opName, opName)
	fmt.Fprintf(&g.buf, "// applies each constituent strategy to the matching child slot and\n")
	fmt.Fprintf(&g.buf, "// rebuilds the term iff at least one child changed. Fails with\n")
	fmt.Fprintf(&g.buf, "// sl.ErrVisitFailure when subject isn't `%s`.\n", opName)
	fmt.Fprintf(&g.buf, "type %s struct {\n", visitName)
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
}

// emitVisitableMethods writes Children/SetChildren/ChildCount/
// ChildAt/SetChildAt onto the struct identified by structName.
// makeName is the smart-constructor function used by SetChildAt and
// SetChildren to rebuild a canonical (hash-consed) instance.
//
// Slot semantics:
//
//   - Non-variadic alts: one child per declared slot, in source order.
//     Slot Go types may be a generated sort interface (e.g. TomTerm),
//     a primitive (string, int64, …), or `any` for cross-module
//     references. Setters cast back from `any` to the slot type;
//     callers passing the wrong concrete type get a Go type-assertion
//     panic (mirroring Java's ClassCastException on setChildAt).
//   - Variadic alts: one child per element of the underlying slice.
//     ChildCount equals `len(t.Slots)`.
//
// The methods are emitted unconditionally — even on zero-slot
// constants — so every term has a uniform Visitable surface.
func (g *gen) emitVisitableMethods(structName, makeName string, slots []hookSlot, isVariadic bool) {
	// castExpr returns the Go expression to cast a `child any` to the
	// slot's static type. `any` slots need no cast.
	castExpr := func(name, goType string) string {
		if goType == "any" {
			return name
		}
		return fmt.Sprintf("%s.(%s)", name, goType)
	}

	if isVariadic {
		varName := slots[0].Name
		elemType := slots[0].GoType

		fmt.Fprintf(&g.buf, "func (t *%s) ChildCount() int { return len(t.%s) }\n\n", structName, varName)

		fmt.Fprintf(&g.buf, "func (t *%s) ChildAt(i int) any { return t.%s[i] }\n\n", structName, varName)

		fmt.Fprintf(&g.buf, "func (t *%s) SetChildAt(i int, child any) any {\n", structName)
		fmt.Fprintf(&g.buf, "\tdup := append([]%s(nil), t.%s...)\n", elemType, varName)
		fmt.Fprintf(&g.buf, "\tdup[i] = %s\n", castExpr("child", elemType))
		fmt.Fprintf(&g.buf, "\treturn %s(dup...)\n", makeName)
		fmt.Fprintln(&g.buf, "}")
		fmt.Fprintln(&g.buf)

		fmt.Fprintf(&g.buf, "func (t *%s) Children() []any {\n", structName)
		fmt.Fprintf(&g.buf, "\tout := make([]any, len(t.%s))\n", varName)
		fmt.Fprintf(&g.buf, "\tfor i, v := range t.%s {\n", varName)
		fmt.Fprintln(&g.buf, "\t\tout[i] = v")
		fmt.Fprintln(&g.buf, "\t}")
		fmt.Fprintln(&g.buf, "\treturn out")
		fmt.Fprintln(&g.buf, "}")
		fmt.Fprintln(&g.buf)

		fmt.Fprintf(&g.buf, "func (t *%s) SetChildren(children []any) any {\n", structName)
		fmt.Fprintf(&g.buf, "\targs := make([]%s, len(children))\n", elemType)
		fmt.Fprintln(&g.buf, "\tfor i, c := range children {")
		fmt.Fprintf(&g.buf, "\t\targs[i] = %s\n", castExpr("c", elemType))
		fmt.Fprintln(&g.buf, "\t}")
		fmt.Fprintf(&g.buf, "\treturn %s(args...)\n", makeName)
		fmt.Fprintln(&g.buf, "}")
		fmt.Fprintln(&g.buf)
		return
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
//	ATerm / ATermList   → any
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
	case "ATerm", "ATermList":
		return "any"
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
