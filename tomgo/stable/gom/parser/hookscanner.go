package gom

import (
	"bufio"
	"fmt"
	"io"
	"io/fs"
	"os"
	"path/filepath"
	"regexp"
	"sort"
	"strings"
)

// hookLine matches a Gom hook construct as defined by the ANTLR grammar:
//
//	hookConstruct : (hookScope)? pointCut=ID COLON hookType=ID arglist LBRACE
//	hookScope     : SORT | MODULE | OPERATOR
//
// We anchor the match at the start of the logical line (after stripping
// leading whitespace and an optional scope keyword) so we do not confuse a
// hook with a slot field appearing inside an alternative, e.g.
// `Not(b:Bool)` — there `b:Bool` is inside `(`…`)` and never at the start
// of a line.
var hookLine = regexp.MustCompile(`^[A-Za-z_][A-Za-z0-9_]*\s*:\s*[A-Za-z_][A-Za-z0-9_]*\s*\(`)

var scopeKeyword = regexp.MustCompile(`^(sort|module|operator)\s+`)

// HasHookContent reports whether the given Gom source contains at least one
// hook definition. It is robust to leading whitespace, the optional scope
// keyword, and single-line `//` comments. It does NOT attempt to strip
// `/* … */` block comments, which never wrap hooks in practice.
func HasHookContent(r io.Reader) (bool, error) {
	scanner := bufio.NewScanner(r)
	scanner.Buffer(make([]byte, 0, 64*1024), 1024*1024)
	for scanner.Scan() {
		line := scanner.Text()
		// Strip a trailing line comment.
		if i := strings.Index(line, "//"); i >= 0 {
			line = line[:i]
		}
		line = strings.TrimLeft(line, " \t")
		if line == "" {
			continue
		}
		// Optional scope keyword.
		if m := scopeKeyword.FindString(line); m != "" {
			line = line[len(m):]
		}
		if hookLine.MatchString(line) {
			return true, nil
		}
	}
	return false, scanner.Err()
}

// HasHookFile is HasHookContent for a path on disk.
func HasHookFile(path string) (bool, error) {
	f, err := os.Open(path)
	if err != nil {
		return false, err
	}
	defer f.Close()
	return HasHookContent(f)
}

// HookReport groups every .gom file found under a root by whether it
// contains hooks.
type HookReport struct {
	Root       string
	WithHooks  []string // sorted, paths relative to Root
	NoHooks    []string // sorted, paths relative to Root
	Errors     map[string]error
}

// Totals returns total, without-hooks, with-hooks counts.
func (r HookReport) Totals() (total, noHooks, withHooks int) {
	return len(r.WithHooks) + len(r.NoHooks), len(r.NoHooks), len(r.WithHooks)
}

// ScanDir walks root recursively, classifying every regular file whose name
// ends with `.gom`. Hidden directories and conventional build artefact
// folders are skipped (`.git`, `build`, `bin`, `dist`).
func ScanDir(root string) (HookReport, error) {
	rep := HookReport{Root: root, Errors: map[string]error{}}
	abs, err := filepath.Abs(root)
	if err != nil {
		return rep, err
	}
	err = filepath.WalkDir(abs, func(path string, d fs.DirEntry, err error) error {
		if err != nil {
			rep.Errors[path] = err
			return nil
		}
		if d.IsDir() {
			name := d.Name()
			if name == ".git" || name == "build" || name == "bin" || name == "dist" {
				return filepath.SkipDir
			}
			return nil
		}
		if !strings.HasSuffix(d.Name(), ".gom") {
			return nil
		}
		has, herr := HasHookFile(path)
		rel, rerr := filepath.Rel(abs, path)
		if rerr != nil {
			rel = path
		}
		if herr != nil {
			rep.Errors[rel] = herr
			return nil
		}
		if has {
			rep.WithHooks = append(rep.WithHooks, rel)
		} else {
			rep.NoHooks = append(rep.NoHooks, rel)
		}
		return nil
	})
	sort.Strings(rep.WithHooks)
	sort.Strings(rep.NoHooks)
	return rep, err
}

// PrintSummary writes a short textual summary of the report to w.
func (r HookReport) PrintSummary(w io.Writer) {
	total, noHooks, withHooks := r.Totals()
	fmt.Fprintf(w, "Scanned %d .gom file(s) under %s\n", total, r.Root)
	fmt.Fprintf(w, "  without hooks: %d\n", noHooks)
	fmt.Fprintf(w, "  with hooks:    %d\n", withHooks)
	if len(r.Errors) > 0 {
		fmt.Fprintf(w, "  errors:        %d\n", len(r.Errors))
		keys := make([]string, 0, len(r.Errors))
		for k := range r.Errors {
			keys = append(keys, k)
		}
		sort.Strings(keys)
		for _, k := range keys {
			fmt.Fprintf(w, "    %s: %v\n", k, r.Errors[k])
		}
	}
}
