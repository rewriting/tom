package smoke

import (
	"encoding/json"
	"os"
	"os/exec"
	"path/filepath"
	"sort"
	"strings"
	"testing"

	"tomtools/harness"
)

// prettyCases are ports of test/go compiled with -p and -O2 (which forces
// pretty): under Go, pretty does not break lines (Go would insert a ';');
// the generated file is formatted like gofmt does instead.
var prettyCases = []struct {
	rel    string
	goTest bool
}{
	{"Peano.t", false},
	{"TestStrategy.t", true},
	{"TestList.t", true},
}

// buildAndRun compiles src with the tom options args, builds and runs the
// result, and returns the generated Go and what the run printed (stdout
// of go run, or the sorted passed tests of go test).
func buildAndRun(t *testing.T, dist harness.Dist, src string, goTest bool, args ...string) (code, behaviour string) {
	t.Helper()
	out := t.TempDir()
	if msg, err := dist.Compile(t, src, out, append([]string{"-go", "-np"}, args...)...); err != nil {
		t.Fatalf("tom %v: %v\n%s", args, err, msg)
	} else if strings.Contains(msg, "WARNING") {
		t.Fatalf("tom %v: unexpected warning\n%s", args, msg)
	}
	name := strings.TrimSuffix(filepath.Base(src), ".t")
	gen := filepath.Join(out, name+".go")
	if goTest {
		if err := os.Rename(gen, filepath.Join(out, name+"_test.go")); err != nil {
			t.Fatal(err)
		}
		gen = filepath.Join(out, name+"_test.go")
	}
	b, err := os.ReadFile(gen)
	if err != nil {
		t.Fatal(err)
	}
	harness.SetupGoModule(t, out)
	if o, err := harness.RunIn(out, nil, "go", "vet", "."); err != nil {
		t.Fatalf("tom %v: go vet: %v\n%s", args, err, o)
	}
	if !goTest {
		o, err := harness.RunIn(out, nil, "go", "run", ".")
		if err != nil {
			t.Fatalf("tom %v: go run: %v\n%s", args, err, o)
		}
		return string(b), o
	}
	o, err := harness.RunIn(out, nil, "go", "test", "-json", "-count=1", ".")
	if err != nil {
		t.Fatalf("tom %v: go test: %v\n%s", args, err, o)
	}
	var passed []string
	for _, l := range strings.Split(o, "\n") {
		var ev struct{ Action, Test string }
		if json.Unmarshal([]byte(l), &ev) == nil && ev.Test != "" && ev.Action == "pass" {
			passed = append(passed, ev.Test)
		}
	}
	sort.Strings(passed)
	return string(b), strings.Join(passed, "\n")
}

func TestGoPretty(t *testing.T) {
	harness.LookPath(t, "go")
	gofmt := harness.LookPath(t, "gofmt")
	dist := harness.NewDist(t)
	root := harness.GoTestDir(harness.RepoRoot(t))
	_, haveTomgo := os.Stat(filepath.Join(dist.Home, "bin", "tomgo"))
	for _, c := range prettyCases {
		t.Run(c.rel, func(t *testing.T) {
			src := filepath.Join(root, c.rel)
			_, want := buildAndRun(t, dist, src, c.goTest)
			if want == "" {
				t.Fatal("the default run printed nothing")
			}
			for _, opt := range []string{"-p", "-O2"} {
				code, got := buildAndRun(t, dist, src, c.goTest, opt)
				if got != want {
					t.Errorf("%s: behaviour differs from the default run\n got: %q\nwant: %q", opt, got, want)
				}
				cmd := exec.Command(gofmt)
				cmd.Stdin = strings.NewReader(code)
				formatted, err := cmd.Output()
				if err != nil {
					t.Fatalf("%s: gofmt: %v", opt, err)
				}
				if string(formatted) != code {
					t.Errorf("%s: the generated Go is not gofmt-formatted", opt)
				}
				if opt == "-p" && haveTomgo == nil && os.Getenv(harness.GoFromEnv) == "" && os.Getenv(harness.GoCheckEnv) != "1" {
					// the Go backend (--goFrom Backend, go/format) writes the same bytes
					viaGo, _ := buildAndRun(t, dist, src, c.goTest, opt, "--goFrom", "Backend")
					if viaGo != code {
						t.Errorf("%s: the Go backend differs from the Java backend + gofmt", opt)
					}
				}
			}
		})
	}
}
