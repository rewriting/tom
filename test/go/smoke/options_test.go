package gosmoke

import (
	"path/filepath"
	"strings"
	"testing"

	"tomtest/harness"
)

// TestGoRefusesGenIntrospector: -go -gi is refused explicitly, even on an
// input without %strategy (nothing would otherwise reach the Go backend's
// guard).
func TestGoRefusesGenIntrospector(t *testing.T) {
	dist := harness.NewDist(t)
	src, err := filepath.Abs(filepath.Join("testdata", "smoke", "smoke.t"))
	if err != nil {
		t.Fatal(err)
	}
	out, err := dist.Compile(t, src, t.TempDir(), "-go", "-gi")
	if err == nil {
		t.Fatalf("tom -go -gi succeeded, want an error\n%s", out)
	}
	if want := "Option '--genIntrospector' is not supported with -go"; !strings.Contains(out, want) {
		t.Fatalf("tom -go -gi: missing %q in\n%s", want, out)
	}
}
