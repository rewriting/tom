package main

import (
	"bytes"
	"os"
	"os/exec"
	"path/filepath"
	"strings"
	"testing"
)

func build(t *testing.T) string {
	t.Helper()
	bin := filepath.Join(t.TempDir(), "tomgo")
	if out, err := exec.Command("go", "build", "-o", bin, ".").CombinedOutput(); err != nil {
		t.Fatalf("go build: %v\n%s", err, out)
	}
	return bin
}

func sample(t *testing.T) string {
	t.Helper()
	term, err := os.ReadFile(filepath.Join("..", "..", "..", "..", "..", "..", "test", "go", "j4", "testdata", "adtuse", "term.txt"))
	if err != nil {
		t.Fatalf("no term.txt: %v", err)
	}
	return strings.Join([]string{
		"tom-bridge 1", "from Backend", "input /tmp/X.t", `inputname "my dir/X.t"`, "output /tmp/X.go",
		`option destdir string "out\"dir\nx"`,
		"option goCode bool true",
		"option level int 2",
		`type Type(concTypeOption(),"term",TLType(" aterm.ATerm "))`,
		"table Table(concTomEntry())",
		"code " + strings.TrimRight(string(term), "\n"),
		"end", "",
	}, "\n")
}

func runBin(t *testing.T, bin, stdin string, args ...string) (string, string, int) {
	t.Helper()
	cmd := exec.Command(bin, args...)
	cmd.Stdin = strings.NewReader(stdin)
	var so, se bytes.Buffer
	cmd.Stdout, cmd.Stderr = &so, &se
	err := cmd.Run()
	code := 0
	if ee, ok := err.(*exec.ExitError); ok {
		code = ee.ExitCode()
	} else if err != nil {
		t.Fatal(err)
	}
	return so.String(), se.String(), code
}

func TestEcho(t *testing.T) {
	bin := build(t)
	in := sample(t)
	out, errs, code := runBin(t, bin, in, "suffix", "--echo")
	if code != 0 || errs != "" {
		t.Fatalf("code %d stderr %q", code, errs)
	}
	if out != in {
		t.Fatalf("echo differs: got %.200q", out)
	}
}

func TestSuffixRefusesPhase(t *testing.T) {
	bin := build(t)
	_, errs, code := runBin(t, bin, sample(t), "suffix")
	want := "ERROR\tmy dir/X.t\t0\tphase Backend is not ported to Go yet\n"
	if code != 2 || errs != want {
		t.Fatalf("code %d stderr %q, want 2 %q", code, errs, want)
	}
}

func TestSuffixReadError(t *testing.T) {
	bin := build(t)
	for _, args := range [][]string{{"suffix"}, {"suffix", "--echo"}} {
		_, errs, code := runBin(t, bin, "tom-bridge 2\n", args...)
		if code != 1 || !strings.HasPrefix(errs, "ERROR\t-bundle-\t1\t") {
			t.Fatalf("%v: code %d stderr %q", args, code, errs)
		}
	}
}

func TestUsage(t *testing.T) {
	bin := build(t)
	for _, args := range [][]string{nil, {"nope"}, {"suffix", "--bad"}} {
		_, errs, code := runBin(t, bin, "", args...)
		if code != 2 || !strings.Contains(errs, "usage") {
			t.Fatalf("%v: code %d stderr %q", args, code, errs)
		}
	}
}
