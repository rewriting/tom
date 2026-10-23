// Package coverage is T3: it inventories the Tom constructs used by the
// compiler's own sources and checks that each one is exercised by at
// least one Tom+Go port whose dashboard status is OK.
package coverage

import (
	"flag"
	"os"
	"path/filepath"
	"regexp"
	"sort"
	"strconv"
	"strings"
	"testing"

	"tomtest/harness"
)

var write = flag.Bool("write-coverage", false, "rewrite ../../doc/go/coverage.md")

type construct struct {
	Name string
	Re   *regexp.Regexp // matched in the compiler's Tom+Java sources
	// PortRe is matched in the (comment-stripped) Tom+Go ports; nil means
	// Re. Host-API usages need it because the port is translated Go.
	PortRe *regexp.Regexp
}

func (c construct) portRe() *regexp.Regexp {
	if c.PortRe != nil {
		return c.PortRe
	}
	return c.Re
}

// stripComments removes // line comments and /* */ blocks. Heuristic: it
// tracks "..." and '...' literals (with backslash escapes) so comment
// markers inside them survive; Go raw strings are not tracked because the
// backquote is also the Tom backquote.
func stripComments(src []byte) []byte {
	out := make([]byte, 0, len(src))
	for i := 0; i < len(src); {
		c := src[i]
		switch {
		case c == '"' || c == '\'':
			j := i + 1
			for j < len(src) && src[j] != c && src[j] != '\n' {
				if src[j] == '\\' {
					j++
				}
				j++
			}
			if j >= len(src) {
				j = len(src) - 1
			}
			out = append(out, src[i:j+1]...)
			i = j + 1
		case c == '/' && i+1 < len(src) && src[i+1] == '/':
			for i < len(src) && src[i] != '\n' {
				i++
			}
		case c == '/' && i+1 < len(src) && src[i+1] == '*':
			j := i + 2
			for j+1 < len(src) && !(src[j] == '*' && src[j+1] == '/') {
				j++
			}
			i = j + 2
			out = append(out, ' ')
		default:
			out = append(out, c)
			i++
		}
	}
	return out
}

var constructs = []construct{
	{"%match", regexp.MustCompile(`%match`), nil},
	{"%strategy", regexp.MustCompile(`%strategy`), nil},
	{"%gom", regexp.MustCompile(`%gom`), nil},
	{"%include sl.tom", regexp.MustCompile(`%include\s*\{[^}]*sl\.tom`), nil},
	{"%include Collection/List/Map mappings", regexp.MustCompile(`%include\s*\{[^}]*(Collection|List|Map|types/[A-Za-z]+)\.tom`), nil},
	{"%op/%oplist/%typeterm declarations", regexp.MustCompile(`%(op|oplist|oparray|typeterm)\s`), nil},
	{"variadic patterns (X*)", regexp.MustCompile(`\b[A-Za-z_]\w*\*\s*[,)\]]`), nil},
	{"anti-patterns (!p)", regexp.MustCompile(`[(,|]\s*!\s*[A-Za-z_]\w*\s*[(\[]`), nil},
	{"aliases (x@p)", regexp.MustCompile(`\b[a-z]\w*@[A-Za-z_(]`), nil},
	{"constraints (<< && ||)", regexp.MustCompile(`[\])\w]\s*<<\s*[` + "`" + `A-Za-z_]`), nil},
	{"backquote terms", regexp.MustCompile("`[A-Za-z_(]"), nil},
	{"%strategy with parameters", regexp.MustCompile(`%strategy\s+\w+\s*\(\s*\w`), nil},
	{"getEnvironment()", regexp.MustCompile(`getEnvironment\s*\(`), regexp.MustCompile(`\bGetEnvironment\s*\(`)},
	{"getPosition()", regexp.MustCompile(`getPosition\s*\(`), regexp.MustCompile(`\bGetPosition\s*\(`)},
	{"visit rules", regexp.MustCompile(`\bvisit\s+[A-Za-z_]\w*\s*\{`), nil},
	{"labelled rules (l:p -> a)", regexp.MustCompile(`\b[a-z]\w*\s*:\s*[A-Za-z_]\w*\(.*\)\s*->`), nil},
}

func count(t *testing.T, dirs []string, re *regexp.Regexp) int {
	n := 0
	for _, d := range dirs {
		if st, err := os.Stat(d); err != nil || !st.IsDir() {
			t.Fatalf("compiler source dir %s missing: %v", d, err)
		}
		files := 0
		err := filepath.Walk(d, func(p string, info os.FileInfo, err error) error {
			if err != nil {
				return err
			}
			if !info.IsDir() && strings.HasSuffix(p, ".t") {
				data, err := os.ReadFile(p)
				if err != nil {
					return err
				}
				files++
				// commented-out code and examples are not uses
				n += len(re.FindAllIndex(stripComments(data), -1))
			}
			return nil
		})
		if err != nil {
			t.Fatal(err)
		}
		if files == 0 {
			t.Fatalf("no .t files under %s", d)
		}
	}
	return n
}

func TestCoverage(t *testing.T) {
	root := harness.RepoRoot(t)
	compiler := []string{filepath.Join(root, "src", "tom", "engine"), filepath.Join(root, "src", "tom", "platform")}
	dashboard, err := os.ReadFile(filepath.Join(root, "doc", "go", "port-dashboard.md"))
	if err != nil {
		t.Skip("no port dashboard yet (run mage portGo)")
	}
	okCases := map[string]bool{}
	for _, line := range strings.Split(string(dashboard), "\n") {
		// Rows look like "| `rel` | OK | notes |".
		cols := strings.Split(line, "|")
		if len(cols) >= 3 && strings.TrimSpace(cols[2]) == "OK" {
			okCases[strings.Trim(cols[1], " `")] = true
		}
	}
	var ok []string
	for c := range okCases {
		ok = append(ok, c)
	}
	sort.Strings(ok)
	var b strings.Builder
	b.WriteString("# Couverture des constructions du compilateur (T3)\n\nGénéré par `mage coverage`.\n\n| Construction | Occurrences dans le compilateur | Cas OK qui l'exercent | Couvert |\n|---|---|---|---|\n")
	missing := 0
	for _, c := range constructs {
		uses := count(t, compiler, c.Re)
		var by []string
		for _, rel := range ok {
			data, err := os.ReadFile(filepath.Join(root, "test", "go", rel))
			if err != nil {
				t.Fatal(err)
			}
			if c.portRe().Match(stripComments(data)) {
				by = append(by, "`"+rel+"`")
			}
		}
		covered := uses == 0 || len(by) > 0
		if !covered {
			missing++
		}
		mark := "☐"
		if covered {
			mark = "☑"
		}
		b.WriteString("| " + c.Name + " | " + strconv.Itoa(uses) + " | " + strings.Join(by, ", ") + " | " + mark + " |\n")
	}
	b.WriteString("\nConstructions non couvertes : " + strconv.Itoa(missing) + "\n")
	if *write {
		if err := os.WriteFile(filepath.Join(root, "doc", "go", "coverage.md"), []byte(b.String()), 0o644); err != nil {
			t.Fatal(err)
		}
	}
	t.Logf("%d construct(s) not yet covered", missing)
}
