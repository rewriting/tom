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
