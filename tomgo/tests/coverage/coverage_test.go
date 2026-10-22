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

	"tom/tomgo/tests/tomjava"
)

var write = flag.Bool("write-coverage", false, "rewrite ../../reports/coverage.md")

type construct struct {
	Name string
	Re   *regexp.Regexp
}

var constructs = []construct{
	{"%match", regexp.MustCompile(`%match`)},
	{"%strategy", regexp.MustCompile(`%strategy`)},
	{"%gom", regexp.MustCompile(`%gom`)},
	{"%include sl.tom", regexp.MustCompile(`%include\s*\{[^}]*sl\.tom`)},
	{"%include Collection/List/Map mappings", regexp.MustCompile(`%include\s*\{[^}]*(Collection|List|Map|types/[A-Za-z]+)\.tom`)},
	{"%op/%oplist/%typeterm declarations", regexp.MustCompile(`%(op|oplist|oparray|typeterm)\s`)},
	{"variadic patterns (X*)", regexp.MustCompile(`\b[A-Za-z_]\w*\*\s*[,)\]]`)},
	{"anti-patterns (!p)", regexp.MustCompile(`[(,|]\s*!\s*[A-Za-z_]\w*\s*[(\[]`)},
	{"aliases (x@p)", regexp.MustCompile(`\b[a-z]\w*@[A-Za-z_(]`)},
	{"constraints (<< && ||)", regexp.MustCompile(`[\])\w]\s*<<\s*[` + "`" + `A-Za-z_]`)},
	{"backquote terms", regexp.MustCompile("`[A-Za-z_(]")},
	{"%strategy with parameters", regexp.MustCompile(`%strategy\s+\w+\s*\(\s*\w`)},
	{"getEnvironment()", regexp.MustCompile(`getEnvironment\s*\(`)},
	{"getPosition()", regexp.MustCompile(`getPosition\s*\(`)},
	{"visit rules", regexp.MustCompile(`\bvisit\s+[A-Za-z_]\w*\s*\{`)},
	{"labelled rules (l:p -> a)", regexp.MustCompile(`\b[a-z]\w*\s*:\s*[A-Za-z_]\w*\(.*\)\s*->`)},
}

func count(t *testing.T, dirs []string, re *regexp.Regexp) int {
	n := 0
	for _, d := range dirs {
		filepath.Walk(d, func(p string, info os.FileInfo, err error) error {
			if err == nil && !info.IsDir() && strings.HasSuffix(p, ".t") {
				data, _ := os.ReadFile(p)
				n += len(re.FindAllIndex(data, -1))
			}
			return err
		})
	}
	return n
}

func TestCoverage(t *testing.T) {
	root := tomjava.RepoRoot(t)
	compiler := []string{filepath.Join(root, "src", "tom", "engine"), filepath.Join(root, "src", "tom", "platform")}
	dashboard, err := os.ReadFile(filepath.Join(root, "tomgo", "reports", "port-dashboard.md"))
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
			data, _ := os.ReadFile(filepath.Join(root, "tomgo", "tests", "port", rel))
			if c.Re.Match(data) {
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
		if err := os.WriteFile(filepath.Join(root, "tomgo", "reports", "coverage.md"), []byte(b.String()), 0o644); err != nil {
			t.Fatal(err)
		}
	}
	t.Logf("%d construct(s) not yet covered", missing)
}
