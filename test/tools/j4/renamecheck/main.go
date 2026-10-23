// Command renamecheck proves that a regeneration of the T1 goldens
// differs from the committed ones by per-file renames only (J4.0b).
//
//	go run ./j4/renamecheck <old dir> <new dir>
//
// Both directories hold .go goldens of the same names. For each pair,
// the old file is transformed (the nine mapping helper functions removed,
// the package-level tom_* declarations renamed with the per-file prefix
// tom_<F>_, a removed helper taking its explicit ';' with it), the new
// file has the nine closures folded back into calls to the helpers, and
// the two Go token streams must be equal. Comments, positions and the
// semicolons the scanner inserts at a newline are ignored; explicit ';'
// tokens are compared, since the generated one-line Go depends on them
// (a '}' turned into '};' is a real change).
package main

import (
	"fmt"
	"go/ast"
	"go/parser"
	"go/scanner"
	"go/token"
	"os"
	"path/filepath"
	"regexp"
	"sort"
	"strings"
)

// helpers are the functions of the Go mappings that J4.0b turns into
// closures at the call site.
var helpers = []string{
	"tom_stringHead", "tom_stringTail", "concIntArrayAppend", "tom_intArrayEquals",
	"concArrayListAppend", "concLinkedListAppend", "concLinkedListgetTail",
	"tom_arrayListEquals", "tom_linkedListEquals",
}

// closures maps a helper to the text of its closure, up to and including
// the opening parenthesis of the arguments (the hook texts of the
// mappings in src/tom/library/mapping/go).
var closures = map[string]string{
	"tom_stringHead":        `func(s string) rune { for _, c := range s { return c }; return 0 }(`,
	"tom_stringTail":        `func(s string) string { for i := range s { if i > 0 { return s[i:] } }; return "" }(`,
	"tom_intArrayEquals":    `func(t1, t2 []int64) bool { if len(t1) != len(t2) { return false }; for i := range t1 { if t1[i] != t2[i] { return false } }; return true }(`,
	"concIntArrayAppend":    `func(o int64, array []int64) []int64 { tmp := make([]int64, len(array)+1); copy(tmp, array); tmp[len(array)] = o; return tmp }(`,
	"concArrayListAppend":   `func(o any, l []any) []any { res := make([]any, len(l), len(l)+1); copy(res, l); return append(res, o) }(`,
	"concLinkedListAppend":  `func(o any, l *list.List) *list.List { res := list.New(); res.PushBackList(l); res.PushFront(o); return res }(`,
	"concLinkedListgetTail": `func(l *list.List) *list.List { res := list.New(); res.PushBackList(l); res.Remove(res.Front()); return res }(`,
	"tom_arrayListEquals":   `func(l1, l2 []any) bool { var eq func(l1, l2 []any) bool; eq = func(l1, l2 []any) bool { if len(l1) != len(l2) { return false }; for i := range l1 { a, ok1 := l1[i].([]any); b, ok2 := l2[i].([]any); if ok1 || ok2 { if !ok1 || !ok2 || !eq(a, b) { return false }; continue }; if l1[i] != l2[i] { return false } }; return true }; return eq(l1, l2) }(`,
	"tom_linkedListEquals":  `func(l1, l2 *list.List) bool { if l1.Len() != l2.Len() { return false }; for e1, e2 := l1.Front(), l2.Front(); e1 != nil; e1, e2 = e1.Next(), e2.Next() { if e1.Value != e2.Value { return false } }; return true }(`,
}

var nonWord = regexp.MustCompile(`[^A-Za-z0-9_]`)

// filePrefix is the prefix tom_<F>_ of the declarations generated for a
// golden: F is the part of its name (without .go) after the last "_",
// with every character outside [A-Za-z0-9_] replaced by "_".
func filePrefix(base string) string {
	f := strings.TrimSuffix(filepath.Base(base), ".go")
	if i := strings.LastIndex(f, "_"); i >= 0 {
		f = f[i+1:]
	}
	return "tom_" + nonWord.ReplaceAllString(f, "_") + "_"
}

// tok is a Go token with its offset in the source it comes from.
type tok struct {
	t   token.Token
	lit string
	off int
}

// tokens scans src, dropping comments and the automatically inserted
// semicolons (literal "\n"); explicit ';' tokens are kept.
func tokens(src []byte) ([]tok, error) {
	fset := token.NewFileSet()
	f := fset.AddFile("", fset.Base(), len(src))
	var errs []string
	var s scanner.Scanner
	s.Init(f, src, func(p token.Position, msg string) { errs = append(errs, fmt.Sprintf("%v: %s", p, msg)) }, 0)
	var out []tok
	for {
		pos, t, lit := s.Scan()
		if t == token.EOF {
			break
		}
		if t == token.SEMICOLON && lit == "\n" {
			continue
		}
		if lit == "" && t != token.IDENT {
			lit = t.String()
		}
		out = append(out, tok{t, lit, f.Offset(pos)})
	}
	if len(errs) > 0 {
		return nil, fmt.Errorf("scan: %s", errs[0])
	}
	return out, nil
}

// pkgDecl is a package-level declaration of the old file.
type pkgDecl struct {
	name     string
	from, to int // byte range of a function declaration (-1 if not a func)
}

func packageDecls(src []byte) ([]pkgDecl, error) {
	fset := token.NewFileSet()
	f, err := parser.ParseFile(fset, "", src, parser.SkipObjectResolution)
	if err != nil {
		return nil, err
	}
	var ds []pkgDecl
	for _, d := range f.Decls {
		switch d := d.(type) {
		case *ast.FuncDecl:
			if d.Recv == nil {
				ds = append(ds, pkgDecl{d.Name.Name, fset.Position(d.Pos()).Offset, fset.Position(d.End()).Offset})
			}
		case *ast.GenDecl:
			for _, sp := range d.Specs {
				switch sp := sp.(type) {
				case *ast.ValueSpec:
					for _, n := range sp.Names {
						ds = append(ds, pkgDecl{n.Name, -1, -1})
					}
				case *ast.TypeSpec:
					ds = append(ds, pkgDecl{sp.Name.Name, -1, -1})
				}
			}
		}
	}
	return ds, nil
}

func renamable(n string) bool {
	switch {
	case strings.HasPrefix(n, "tom__"), strings.HasPrefix(n, "tom_new_"):
		return false
	case strings.HasPrefix(n, "tom_"), n == "next_minimal_extract", strings.HasPrefix(n, "__"):
		return true
	}
	return false
}

func rename(n, prefix string) string {
	if strings.HasPrefix(n, "tom_") {
		return prefix + n[4:]
	}
	return prefix + n
}

func isHelper(n string) bool {
	for _, h := range helpers {
		if h == n {
			return true
		}
	}
	return false
}

// checkPair reports nil when the new golden is the old one up to the
// renames and closures described in the package comment.
func checkPair(old, new []byte, prefix string) error {
	decls, err := packageDecls(old)
	if err != nil {
		return fmt.Errorf("old: %w", err)
	}
	oldToks, err := tokens(old)
	if err != nil {
		return fmt.Errorf("old: %w", err)
	}
	// ranges of the removed helper declarations, names to rename
	type rng struct{ from, to int }
	var removed []rng
	ren := map[string]string{}
	for _, d := range decls {
		if d.from >= 0 && isHelper(d.name) {
			removed = append(removed, rng{d.from, d.to})
			continue
		}
		if renamable(d.name) {
			ren[d.name] = rename(d.name, prefix)
		}
	}
	var a []tok
	dropSemi := false // the token after a removed helper is its ';'
next:
	for _, t := range oldToks {
		for _, r := range removed {
			if t.off >= r.from && t.off < r.to {
				dropSemi = true
				continue next
			}
		}
		if dropSemi {
			dropSemi = false
			if t.t == token.SEMICOLON {
				continue
			}
		}
		if t.t == token.IDENT {
			if n, ok := ren[t.lit]; ok {
				t.lit = n
			}
		}
		a = append(a, t)
	}

	newToks, err := tokens(new)
	if err != nil {
		return fmt.Errorf("new: %w", err)
	}
	// fold the closures back into helper calls
	type fold struct {
		name string
		toks []tok
	}
	var folds []fold
	names := make([]string, 0, len(closures))
	for n := range closures {
		names = append(names, n)
	}
	sort.Strings(names)
	for _, n := range names {
		ct, err := tokens([]byte(closures[n]))
		if err != nil {
			return fmt.Errorf("closure %s: %w", n, err)
		}
		folds = append(folds, fold{n, ct})
	}
	var b []tok
	for i := 0; i < len(newToks); {
		matched := false
		for _, f := range folds {
			if hasPrefix(newToks[i:], f.toks) {
				b = append(b, tok{token.IDENT, f.name, newToks[i].off}, tok{token.LPAREN, "(", newToks[i].off})
				i += len(f.toks)
				matched = true
				break
			}
		}
		if !matched {
			b = append(b, newToks[i])
			i++
		}
	}

	for i := 0; i < len(a) || i < len(b); i++ {
		if i < len(a) && i < len(b) && a[i].t == b[i].t && a[i].lit == b[i].lit {
			continue
		}
		return fmt.Errorf("token %d differs: old %s, new %s\n  old: %q\n  new: %q",
			i, show(a, i), show(b, i), context(old, a, i), context(new, b, i))
	}
	return nil
}

func hasPrefix(s, p []tok) bool {
	if len(s) < len(p) {
		return false
	}
	for i := range p {
		if s[i].t != p[i].t || s[i].lit != p[i].lit {
			return false
		}
	}
	return true
}

func show(ts []tok, i int) string {
	if i >= len(ts) {
		return "<end>"
	}
	return fmt.Sprintf("%q", ts[i].lit)
}

// context is 80 bytes of source on each side of token i.
func context(src []byte, ts []tok, i int) string {
	if i >= len(ts) {
		return "<end of file>"
	}
	o := ts[i].off
	lo, hi := max(o-80, 0), min(o+80, len(src))
	return string(src[lo:hi])
}

func run(oldDir, newDir string) (files, ok int, bad []string, err error) {
	entries, err := os.ReadDir(oldDir)
	if err != nil {
		return 0, 0, nil, err
	}
	seen := map[string]bool{}
	for _, e := range entries {
		if e.IsDir() || filepath.Ext(e.Name()) != ".go" {
			continue
		}
		seen[e.Name()] = true
		files++
		o, err := os.ReadFile(filepath.Join(oldDir, e.Name()))
		if err != nil {
			return 0, 0, nil, err
		}
		n, err := os.ReadFile(filepath.Join(newDir, e.Name()))
		if err != nil {
			bad = append(bad, e.Name()+": missing in new directory")
			continue
		}
		if err := checkPair(o, n, filePrefix(e.Name())); err != nil {
			bad = append(bad, e.Name()+": "+err.Error())
			continue
		}
		ok++
	}
	newEntries, err := os.ReadDir(newDir)
	if err != nil {
		return 0, 0, nil, err
	}
	for _, e := range newEntries {
		if !e.IsDir() && filepath.Ext(e.Name()) == ".go" && !seen[e.Name()] {
			files++
			bad = append(bad, e.Name()+": missing in old directory")
		}
	}
	return files, ok, bad, nil
}

func main() {
	if len(os.Args) != 3 {
		fmt.Fprintln(os.Stderr, "usage: renamecheck <old dir> <new dir>")
		os.Exit(2)
	}
	files, ok, bad, err := run(os.Args[1], os.Args[2])
	if err != nil {
		fmt.Fprintln(os.Stderr, "renamecheck:", err)
		os.Exit(2)
	}
	fmt.Printf("files %d, renames-only %d, other %d\n", files, ok, len(bad))
	for _, b := range bad {
		fmt.Println(b)
	}
	if len(bad) > 0 {
		os.Exit(1)
	}
}
