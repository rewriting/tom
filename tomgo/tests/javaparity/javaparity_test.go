// Package javaparity is T0: the extended compiler must produce, for the
// default Java target, exactly what the human-written compiler in legacy/
// produces on every test/ input.
package javaparity

import (
	"os"
	"path/filepath"
	"regexp"
	"strings"
	"testing"

	"tom/tomgo/tests/tomjava"
)

// timing matches the "123 ms" phase timings the compiler prints.
var timing = regexp.MustCompile(`\d+ ms`)

// tmpName matches the random temporary file names of generated .t files.
var tmpName = regexp.MustCompile(`/[^\s]*/tmp\d+\.t`)

func inputs(t *testing.T, root string) []string {
	var files []string
	err := filepath.Walk(filepath.Join(root, "test"), func(p string, info os.FileInfo, err error) error {
		if err == nil && !info.IsDir() && strings.HasSuffix(p, ".t") {
			files = append(files, p)
		}
		return err
	})
	if err != nil {
		t.Fatal(err)
	}
	return files
}

func TestJavaOutputIdenticalToLegacy(t *testing.T) {
	legacy, current := tomjava.LegacyDist(t), tomjava.NewDist(t)
	root := tomjava.RepoRoot(t)
	for _, src := range inputs(t, root) {
		rel, _ := filepath.Rel(root, src)
		t.Run(rel, func(t *testing.T) {
			outL, outN := t.TempDir(), t.TempDir()
			logL, errL := legacy.Compile(t, src, outL)
			logN, errN := current.Compile(t, src, outN)
			norm := func(s, out string) string {
				s = timing.ReplaceAllString(s, "N ms")
				s = tmpName.ReplaceAllString(s, "__TMP__.t")
				return tomjava.Normalize(s, map[string]string{out: "__OUT__", legacy.Home: "__HOME__", current.Home: "__HOME__"})
			}
			if (errL == nil) != (errN == nil) || norm(logL, outL) != norm(logN, outN) {
				t.Fatalf("diagnostics differ\nlegacy (%v):\n%s\nnew (%v):\n%s", errL, logL, errN, logN)
			}
			compareTrees(t, outL, outN)
		})
	}
}

func compareTrees(t *testing.T, a, b string) {
	t.Helper()
	seen := map[string]bool{}
	filepath.Walk(a, func(p string, info os.FileInfo, err error) error {
		if err != nil || info.IsDir() {
			return err
		}
		rel, _ := filepath.Rel(a, p)
		seen[rel] = true
		want, _ := os.ReadFile(p)
		got, err := os.ReadFile(filepath.Join(b, rel))
		if err != nil {
			t.Errorf("%s: missing in new output", rel)
		} else if string(want) != string(got) {
			t.Errorf("%s: content differs from legacy", rel)
		}
		return nil
	})
	filepath.Walk(b, func(p string, info os.FileInfo, err error) error {
		if err == nil && !info.IsDir() {
			if rel, _ := filepath.Rel(b, p); !seen[rel] {
				t.Errorf("%s: extra file in new output", rel)
			}
		}
		return err
	})
}
