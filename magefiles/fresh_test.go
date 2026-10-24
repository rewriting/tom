//go:build mage

package main

import (
	"crypto/sha256"
	"encoding/hex"
	"os"
	"path/filepath"
	"strings"
	"testing"
)

func writeGen(t *testing.T, dir, srcText, hashedText string) (src, gen string) {
	t.Helper()
	src = filepath.Join(dir, "x.t")
	gen = src + ".go"
	if err := os.WriteFile(src, []byte(srcText), 0o644); err != nil {
		t.Fatal(err)
	}
	sum := sha256.Sum256([]byte(hashedText))
	body := tomSourceHashPrefix + hex.EncodeToString(sum[:]) + "\n\npackage x\n"
	if err := os.WriteFile(gen, []byte(body), 0o644); err != nil {
		t.Fatal(err)
	}
	return
}

func TestCheckGenFresh(t *testing.T) {
	src, gen := writeGen(t, t.TempDir(), "v1", "v1")
	if err := checkGenFresh(src, gen); err != nil {
		t.Fatalf("matching hash refused: %v", err)
	}
	// .t modified after generation (the .t.go is not older: mtime plays no role).
	if err := os.WriteFile(src, []byte("v2"), 0o644); err != nil {
		t.Fatal(err)
	}
	err := checkGenFresh(src, gen)
	if err == nil || !strings.Contains(err.Error(), "changed since its generated") {
		t.Fatalf("modified .t with old .t.go must be refused, got %v", err)
	}
}

func TestCheckGenFreshMissingOrHeaderless(t *testing.T) {
	src, gen := writeGen(t, t.TempDir(), "v1", "v1")
	if err := os.WriteFile(gen, []byte("package x\n"), 0o644); err != nil {
		t.Fatal(err)
	}
	if err := checkGenFresh(src, gen); err == nil {
		t.Fatal("generated file without header must be refused")
	}
	os.Remove(gen)
	if err := checkGenFresh(src, gen); err == nil || !strings.Contains(err.Error(), "no generated") {
		t.Fatalf("missing .t.go must be refused, got %v", err)
	}
}

// amorceTree builds src/ and amorce/ with one source x.t whose generated
// file is genSrc in src (nil: absent) and genAmorce in the amorce; each
// gen is the hash text it was generated from (the .t holds srcText).
func amorceTree(t *testing.T, srcText string, genSrc, genAmorce *string) (srcDir, amorceDir string) {
	t.Helper()
	root := t.TempDir()
	srcDir, amorceDir = filepath.Join(root, "src"), filepath.Join(root, "amorce")
	for _, d := range []string{srcDir, amorceDir} {
		if err := os.MkdirAll(filepath.Join(d, "engine", "tools"), 0o755); err != nil {
			t.Fatal(err)
		}
	}
	if err := os.WriteFile(filepath.Join(srcDir, "engine", "tools", "x.t"), []byte(srcText), 0o644); err != nil {
		t.Fatal(err)
	}
	put := func(dir string, hashed *string, tag string) {
		if hashed == nil {
			return
		}
		sum := sha256.Sum256([]byte(*hashed))
		body := tomSourceHashPrefix + hex.EncodeToString(sum[:]) + "\n\npackage x // " + tag + "\n"
		if err := os.WriteFile(filepath.Join(dir, "engine", "tools", "x.t.go"), []byte(body), 0o644); err != nil {
			t.Fatal(err)
		}
	}
	put(srcDir, genSrc, "src")
	put(amorceDir, genAmorce, "amorce")
	return
}

func readGen(t *testing.T, dir string) string {
	t.Helper()
	b, err := os.ReadFile(filepath.Join(dir, "engine", "tools", "x.t.go"))
	if err != nil {
		return ""
	}
	return string(b)
}

func TestTomGoFromAmorce(t *testing.T) {
	v1, v2, v0 := "v1", "v2", "v0"
	cases := []struct {
		name              string
		srcText           string
		genSrc, genAmorce *string
		want              string // "src", "amorce", or "" (no file)
	}{
		{"missing, amorce fresh", "v2", nil, &v2, "amorce"},
		{"fresh src kept", "v2", &v2, &v1, "src"},
		{"stale src, amorce matches the current .t: replaced", "v2", &v1, &v2, "amorce"},
		{"stale src, amorce stale too: left", "v2", &v1, &v0, "src"},
		{"stale src, no amorce copy: left", "v2", &v1, nil, "src"},
	}
	for _, c := range cases {
		srcDir, amorceDir := amorceTree(t, c.srcText, c.genSrc, c.genAmorce)
		if err := tomGoFromAmorceIn(srcDir, amorceDir); err != nil {
			t.Fatalf("%s: %v", c.name, err)
		}
		got := readGen(t, srcDir)
		if (c.want == "amorce") != strings.Contains(got, "// amorce") || (c.want == "src") != strings.Contains(got, "// src") {
			t.Errorf("%s: got %q, want the %s copy", c.name, got, c.want)
		}
	}
}
