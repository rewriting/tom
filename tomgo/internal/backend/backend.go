// Package backend turns a parsed Gom module (see package gom) into a
// self-contained Go package whose term constructors run through the
// shared-objects runtime (see package library/sharedobjects).
//
// Scope (Phase 2c): only hook-free modules. Cross-module references
// (e.g. `imports Leaf` then a slot typed `l:Leaf`) are rendered as Go
// `any` because each module is generated as an isolated package. A
// later phase will wire cross-package types.
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

	"tom/tomgo/internal/gom"
)

// Options controls code generation. Only the Go package name has a
// non-trivial default — by default it is derived from the last segment
// of the Gom module name (lower-cased, scrubbed of non-letters).
type Options struct {
	PackageName string // overrides the auto-derived package name
}

// Generate writes the Go source for module mod to out. The output is
// already gofmt'd.
func Generate(mod *gom.Module, opts Options, out io.Writer) error {
	src, err := generateBytes(mod, opts)
	if err != nil {
		return err
	}
	_, err = out.Write(src)
	return err
}

// GenerateBatchToDir compiles several Gom modules into a single Go
// package living at dir. Cross-module slot types resolve to the local
// Go interface because all sorts from every module share the same Go
// namespace. The resulting Go module has the path tomgen/<pkg>.
//
// This is the mode used to compile the real ADT in
// `src/tom/gom/adt/*.gom`, where every file imports symbols defined
// in the others.
func GenerateBatchToDir(modules []*gom.Module, opts Options, dir string) (string, error) {
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
	for _, m := range modules {
		for _, s := range m.Sorts {
			if prev, dup := owner[s.Name]; dup {
				return "", fmt.Errorf("sort %q is defined in both %s and %s",
					s.Name, prev, m.QualifiedName())
			}
			owner[s.Name] = m.QualifiedName()
			allSorts[s.Name] = true
		}
	}

	// Emit one .go file per module so that humans (and `git diff`) can
	// see which module produced what. The first file (lex order on
	// filename) carries the shared factory; the rest are pure types.
	for i, m := range modules {
		src, err := generateBatchBytes(m, allSorts, opts.PackageName, i == 0)
		if err != nil {
			return "", fmt.Errorf("generating %s: %w", m.QualifiedName(), err)
		}
		name := strings.ToLower(safeFileName(m.QualifiedName())) + ".go"
		if err := os.WriteFile(filepath.Join(abs, name), src, 0o644); err != nil {
			return "", err
		}
	}
	goMod := fmt.Sprintf("module tomgen/%s\n\ngo 1.22\n\nrequire tom/tomgo v0.0.0\nreplace tom/tomgo => %s\n",
		opts.PackageName, locateTomgoRoot())
	if err := os.WriteFile(filepath.Join(abs, "go.mod"), []byte(goMod), 0o644); err != nil {
		return "", err
	}
	return abs, nil
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

// generateBatchBytes is the batch-mode counterpart of generateBytes.
// It uses a pre-computed cross-module sort registry rather than the
// per-module ownSort map, so a slot typed `Code` from Objects.gom
// resolves to the Go interface generated from Code.gom.
func generateBatchBytes(mod *gom.Module, allSorts map[string]bool, pkgName string, emitFactory bool) ([]byte, error) {
	g := &gen{
		mod:     mod,
		ownSort: allSorts,
		pkgName: pkgName,
	}
	g.emitHeader()
	if emitFactory {
		g.emitFactory()
	}
	for _, s := range mod.Sorts {
		g.emitSort(s)
	}
	formatted, err := format.Source(g.buf.Bytes())
	if err != nil {
		return g.buf.Bytes(), fmt.Errorf("gofmt failed for %s: %w", mod.QualifiedName(), err)
	}
	return formatted, nil
}

// GenerateToDir creates dir if needed, writes go.mod and a single
// `<pkg>.go` source file, and returns the absolute package directory.
// Each generated module is a standalone Go module so it can be built
// and tested in isolation.
func GenerateToDir(mod *gom.Module, opts Options, dir string) (string, error) {
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
	goMod := fmt.Sprintf("module tomgen/%s\n\ngo 1.22\n\nrequire tom/tomgo v0.0.0\nreplace tom/tomgo => %s\n",
		opts.PackageName, locateTomgoRoot())
	if err := os.WriteFile(filepath.Join(abs, "go.mod"), []byte(goMod), 0o644); err != nil {
		return "", err
	}
	return abs, nil
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

func defaultPackageName(mod *gom.Module) string {
	last := ""
	if n := len(mod.Name); n > 0 {
		last = mod.Name[n-1]
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

func generateBytes(mod *gom.Module, opts Options) ([]byte, error) {
	if opts.PackageName == "" {
		opts.PackageName = defaultPackageName(mod)
	}
	g := &gen{
		mod:     mod,
		ownSort: map[string]bool{},
		pkgName: opts.PackageName,
	}
	for _, s := range mod.Sorts {
		g.ownSort[s.Name] = true
	}
	g.emitHeader()
	g.emitFactory()
	for _, s := range mod.Sorts {
		g.emitSort(s)
	}
	formatted, err := format.Source(g.buf.Bytes())
	if err != nil {
		// Return the raw source on a formatter error so the bug is debuggable.
		return g.buf.Bytes(), fmt.Errorf("gofmt failed: %w", err)
	}
	return formatted, nil
}

type gen struct {
	mod     *gom.Module
	ownSort map[string]bool
	pkgName string
	buf     bytes.Buffer
}

func (g *gen) emitHeader() {
	fmt.Fprintf(&g.buf, "// Code generated by tom/tomgo backend from %s. DO NOT EDIT.\n", g.mod.QualifiedName())
	fmt.Fprintf(&g.buf, "package %s\n\n", g.pkgName)
	fmt.Fprintln(&g.buf, "import (")
	fmt.Fprintln(&g.buf, `	"fmt"`)
	fmt.Fprintln(&g.buf, `	"strings"`)
	fmt.Fprintln(&g.buf, "")
	fmt.Fprintln(&g.buf, `	"tom/tomgo/library/sharedobjects"`)
	fmt.Fprintln(&g.buf, ")")
	fmt.Fprintln(&g.buf)
	fmt.Fprintln(&g.buf, "// underscore-prevent: tolerate unused imports if a module has no slots of these types.")
	fmt.Fprintln(&g.buf, "var _ = fmt.Sprintf")
	fmt.Fprintln(&g.buf, "var _ = strings.Join")
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

func (g *gen) emitSort(s gom.SortDecl) {
	// Collect sort-scoped hooks targeting this sort. They contribute
	// both interface-level method declarations and per-alternative
	// implementations.
	sortHooks := g.sortScopedHooks(s.Name)

	fmt.Fprintf(&g.buf, "// %s is the Go interface backing the Gom sort %s.\n", s.Name, s.Name)
	fmt.Fprintf(&g.buf, "type %s interface {\n", s.Name)
	fmt.Fprintln(&g.buf, "	sharedobjects.Term")
	fmt.Fprintf(&g.buf, "	is%s()\n", s.Name)
	for _, h := range sortHooks {
		if decl := interfaceDeclForHook(h); decl != "" {
			fmt.Fprintf(&g.buf, "\t%s\n", decl)
		}
	}
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)
	for _, alt := range s.Alternatives {
		g.emitAlternative(s.Name, alt)
		for _, h := range sortHooks {
			g.emitHookImplOnAlt(s, alt, h)
		}
	}
}

// sortScopedHooks returns all module-level hooks whose pointcut is this
// sort and whose scope is "sort".
func (g *gen) sortScopedHooks(sort string) []gom.GomHook {
	if g.mod == nil {
		return nil
	}
	var out []gom.GomHook
	for _, h := range g.mod.Hooks {
		if h.Scope == "sort" && h.PointCut == sort {
			out = append(out, h)
		}
	}
	return out
}

// interfaceDeclForHook returns the Go method-set entry contributed by a
// known sort-scoped hook (e.g. `ContainsTomCode() bool`). Unknown hooks
// contribute nothing — the body Tom code is dropped and a `// unknown
// hook` marker is left in the impl section instead.
func interfaceDeclForHook(h gom.GomHook) string {
	if k := recognisedKnownHook(h); k != nil {
		return k.interfaceDecl
	}
	return ""
}

// emitHookImplOnAlt emits the body of the method contributed by hook
// `h` on the struct generated for alt of sort s. Unknown hooks emit a
// short comment so the gap is visible in the generated source.
func (g *gen) emitHookImplOnAlt(s gom.SortDecl, alt gom.Alternative, h gom.GomHook) {
	k := recognisedKnownHook(h)
	if k == nil {
		fmt.Fprintf(&g.buf, "// unsupported hook for sort %s alt %s: %s:%s (body dropped)\n\n",
			s.Name, alt.Op, h.PointCut, h.Kind)
		return
	}
	structName := exportedField(alt.Op) + s.Name
	k.emitImpl(&g.buf, g, s, alt, structName)
}

// knownHook bundles the metadata and emission routines for one
// recognised hook idiom. The table `knownHookTable` is the registry —
// extend it as more hooks become necessary.
type knownHook struct {
	module        string // module the hook lives in, e.g. "Objects"
	scope         string // "sort"|"module"|"operator"
	pointCut      string // sort name (for sort scope), …
	kind          string // "block", "make", …
	interfaceDecl string // method signature for the sort interface
	emitImpl      func(buf *bytes.Buffer, g *gen, s gom.SortDecl, alt gom.Alternative, structName string)
}

// recognisedKnownHook returns the entry from knownHookTable matching
// the hook, or nil. The module is taken from g.mod when available; for
// the V1 table we keep the match generous (module name is optional).
func recognisedKnownHook(h gom.GomHook) *knownHook {
	for i := range knownHookTable {
		k := &knownHookTable[i]
		if k.scope == h.Scope && k.pointCut == h.PointCut && k.kind == h.Kind {
			return k
		}
	}
	return nil
}

// knownHookTable maps the (scope,pointCut,kind) triples this backend
// can lower to Go. The body Tom code is NOT parsed — we trust the
// table to describe what the Java reference would do.
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
		emitImpl: func(buf *bytes.Buffer, g *gen, s gom.SortDecl, alt gom.Alternative, structName string) {
			fmt.Fprintf(buf, "// ContainsTomCode reports whether any child hook is a\n")
			fmt.Fprintf(buf, "// MakeHook/MakeBeforeHook/BlockHook flagged HasTomCode=true.\n")
			fmt.Fprintf(buf, "// Lowered from `sort HookList:block()` in Objects.gom.\n")
			fmt.Fprintf(buf, "func (t *%s) ContainsTomCode() bool {\n", structName)
			if alt.Variadic {
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
}

// emitAlternative emits the struct, hash/equiv/duplicate/string methods,
// and the smart constructor for one alternative.
//
// The operator name from the .gom source (e.g. `zero`, `suc`) is
// capitalized in the generated Go identifiers so all types and
// constructors are exported. The original spelling is preserved in the
// hash input and in the String() output so canonicalization and
// pretty-printing remain faithful to the source.
func (g *gen) emitAlternative(sortName string, alt gom.Alternative) {
	opGo := exportedField(alt.Op)
	if opGo == "" {
		opGo = "Op"
	}
	structName := opGo + sortName
	// Build the slot list.
	type slot struct {
		Name     string
		GoType   string
		IsVar    bool
	}
	var slots []slot
	if alt.Variadic {
		a := alt.Args[0]
		slots = append(slots, slot{Name: "Slots", GoType: g.goType(a.Type, false), IsVar: true})
	} else {
		seen := map[string]int{}
		for i, a := range alt.Args {
			name := exportedField(a.Name)
			if name == "" {
				name = fmt.Sprintf("Slot%d", i)
			}
			if c := seen[name]; c > 0 {
				name = fmt.Sprintf("%s%d", name, c+1)
			}
			seen[name]++
			slots = append(slots, slot{Name: name, GoType: g.goType(a.Type, false)})
		}
	}

	// Struct definition.
	fmt.Fprintf(&g.buf, "// %s is the term type for the alternative `%s(...)` of sort %s.\n", structName, alt.Op, sortName)
	fmt.Fprintf(&g.buf, "type %s struct {\n", structName)
	if alt.Variadic {
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
	fmt.Fprintf(&g.buf, "func (*%s) is%s() {}\n\n", structName, sortName)

	// Hash method.
	fmt.Fprintf(&g.buf, "func (t *%s) Hash() uint32 { return t.hash }\n\n", structName)

	// Equivalent. When the alternative has no slot, we don't bind `o`
	// (Go would refuse the unused identifier).
	fmt.Fprintf(&g.buf, "func (t *%s) Equivalent(other sharedobjects.Term) bool {\n", structName)
	if !alt.Variadic && len(slots) == 0 {
		fmt.Fprintf(&g.buf, "\t_, ok := other.(*%s)\n", structName)
		fmt.Fprintln(&g.buf, "\treturn ok")
		fmt.Fprintln(&g.buf, "}")
		fmt.Fprintln(&g.buf)
		// Skip the rest of the Equivalent body emission.
		goto afterEquivalent
	}
	fmt.Fprintf(&g.buf, "\to, ok := other.(*%s)\n", structName)
	fmt.Fprintln(&g.buf, "\tif !ok {")
	fmt.Fprintln(&g.buf, "\t\treturn false")
	fmt.Fprintln(&g.buf, "\t}")
	if alt.Variadic {
		varType := slots[0].GoType
		varName := slots[0].Name
		fmt.Fprintf(&g.buf, "\tif len(t.%s) != len(o.%s) {\n", varName, varName)
		fmt.Fprintln(&g.buf, "\t\treturn false")
		fmt.Fprintln(&g.buf, "\t}")
		fmt.Fprintf(&g.buf, "\tfor i := range t.%s {\n", varName)
		if g.isShared(varType) {
			fmt.Fprintf(&g.buf, "\t\tif t.%s[i] != o.%s[i] { return false }\n", varName, varName)
		} else {
			fmt.Fprintf(&g.buf, "\t\tif t.%s[i] != o.%s[i] { return false }\n", varName, varName)
		}
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
	if alt.Variadic {
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
	//   - String → "%q"   (quoted, Java-like escapes)
	//   - char   → "%q"   (single char in quotes — close enough to Java)
	//   - else   → "%v"
	verbFor := func(goType string) string {
		switch goType {
		case "string", "rune":
			return "%q"
		}
		return "%v"
	}
	fmt.Fprintf(&g.buf, "func (t *%s) String() string {\n", structName)
	if alt.Variadic {
		v := verbFor(slots[0].GoType)
		fmt.Fprintf(&g.buf, "\tparts := make([]string, len(t.%s))\n", slots[0].Name)
		fmt.Fprintf(&g.buf, "\tfor i, v := range t.%s {\n", slots[0].Name)
		fmt.Fprintf(&g.buf, "\t\tparts[i] = fmt.Sprintf(%q, v)\n", v)
		fmt.Fprintln(&g.buf, "\t}")
		fmt.Fprintf(&g.buf, "\treturn %q + \"(\" + strings.Join(parts, \",\") + \")\"\n", alt.Op)
	} else if len(slots) == 0 {
		fmt.Fprintf(&g.buf, "\treturn %q + \"()\"\n", alt.Op)
	} else {
		var verbs []string
		for _, s := range slots {
			verbs = append(verbs, verbFor(s.GoType))
		}
		format := alt.Op + "(" + strings.Join(verbs, ",") + ")"
		fmt.Fprintf(&g.buf, "\treturn fmt.Sprintf(%q", format)
		for _, s := range slots {
			fmt.Fprintf(&g.buf, ", t.%s", s.Name)
		}
		fmt.Fprintln(&g.buf, ")")
	}
	fmt.Fprintln(&g.buf, "}")
	fmt.Fprintln(&g.buf)

	// Smart constructor. The Go-side function name is exported (Pascal
	// case); the canonical symbol passed to the hash mixer keeps the
	// original spelling so sharing stays correct.
	fmt.Fprintf(&g.buf, "// Make%s builds the canonical (shared) %s term.\n", opGo, alt.Op)
	if alt.Variadic {
		fmt.Fprintf(&g.buf, "func Make%s(args ...%s) %s {\n", opGo, slots[0].GoType, sortName)
		fmt.Fprintf(&g.buf, "\thashes := make([]uint32, 0, len(args))\n")
		fmt.Fprintf(&g.buf, "\tfor _, a := range args {\n")
		if g.isShared(slots[0].GoType) {
			fmt.Fprintln(&g.buf, "\t\thashes = append(hashes, a.Hash())")
		} else {
			fmt.Fprintf(&g.buf, "\t\thashes = append(hashes, sharedobjects.StringHash(fmt.Sprintf(\"%%v\", a)))\n")
		}
		fmt.Fprintln(&g.buf, "\t}")
		fmt.Fprintf(&g.buf, "\tproto := &%s{%s: args, hash: sharedobjects.MixSymbol(sharedobjects.StringHash(%q), hashes)}\n",
			structName, slots[0].Name, alt.Op)
		fmt.Fprintf(&g.buf, "\treturn factory.Build(proto).(*%s)\n", structName)
		fmt.Fprintln(&g.buf, "}")
	} else {
		// Build the typed parameter list.
		var params []string
		for _, s := range slots {
			params = append(params, fmt.Sprintf("%s %s", unexported(s.Name), s.GoType))
		}
		fmt.Fprintf(&g.buf, "func Make%s(%s) %s {\n", opGo, strings.Join(params, ", "), sortName)
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
		fmt.Fprintf(&g.buf, "\tproto := &%s{hash: sharedobjects.MixSymbol(sharedobjects.StringHash(%q), hashes)", structName, alt.Op)
		for _, s := range slots {
			fmt.Fprintf(&g.buf, ", %s: %s", s.Name, unexported(s.Name))
		}
		fmt.Fprintln(&g.buf, "}")
		fmt.Fprintf(&g.buf, "\treturn factory.Build(proto).(*%s)\n", structName)
		fmt.Fprintln(&g.buf, "}")
	}
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
	// Avoid Go keyword collisions for the few that can occur as slot names.
	switch out {
	case "type", "func", "range", "var", "select", "default", "case", "chan",
		"const", "for", "go", "if", "import", "interface", "map", "package",
		"return", "struct", "switch":
		return out + "_"
	}
	return out
}

// SortedSortNames is a tiny helper used by tests when they want to
// iterate sorts deterministically.
func SortedSortNames(mod *gom.Module) []string {
	names := make([]string, 0, len(mod.Sorts))
	for _, s := range mod.Sorts {
		names = append(names, s.Name)
	}
	sort.Strings(names)
	return names
}
