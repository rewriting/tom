package platform

import (
	"errors"
	"strings"
	"testing"

	"tom/engine/bridge"
	"tom/library/aterm"
)

func opts() *Options {
	return NewOptions([]bridge.Option{
		{Name: "encoding", Kind: "string", Value: "UTF-8"},
		{Name: "optLevel", Kind: "int", Value: "3"},
		{Name: "pretty", Kind: "bool", Value: "true"},
		{Name: "wall", Kind: "bool", Value: "false"},
	})
}

// panicMsg runs f and returns the message of the *RuntimeError it panics with.
func panicMsg(t *testing.T, f func()) string {
	t.Helper()
	var msg string
	func() {
		defer func() {
			v := recover()
			e, ok := v.(*RuntimeError)
			if !ok {
				t.Fatalf("panic %v (%T), want a *RuntimeError", v, v)
			}
			msg = e.Msg
		}()
		f()
	}()
	return msg
}

func TestOptions(t *testing.T) {
	o := opts()
	if !o.Bool("pretty") || o.Bool("wall") || o.Int("optLevel") != 3 || o.String("encoding") != "UTF-8" {
		t.Fatal("values")
	}
	if !o.Has("pretty") || o.Has("nope") {
		t.Fatal("Has")
	}
	if got := panicMsg(t, func() { o.Bool("nope") }); got != "TomOptionManager: Option 'nope' not found" {
		t.Fatalf("unknown option: %q", got)
	}
	if got := panicMsg(t, func() { o.Int("pretty") }); got != "option pretty is a bool, not a int" {
		t.Fatalf("kind: %q", got)
	}
}

func TestMessageText(t *testing.T) {
	cases := []struct {
		m    Message
		args []any
		want string
	}{
		{BackendIOException, []any{"a.go", "disk full"}, "BackendPlugin : IOException occurs writting to file 'a.go': disk full "},
		{TomGenerationPhase, []any{1234567}, "TOM generation phase (1,234,567 ms)"},
		{TomGenerationPhase, []any{int64(-12)}, "TOM generation phase (-12 ms)"},
		{ExceptionMessage, []any{"X.t"}, "X.t: Unhandled exception\n"},
		{OptionNotFound, []any{nil}, "TomOptionManager: Option 'null' not found"},
		{Message{"q", "'{0}' is {0}, {1}"}, []any{"x"}, "{0} is x, {1}"},
		{PlatformStopped, nil, "Platform stopped"},
	}
	for _, c := range cases {
		if got := c.m.Text(c.args...); got != c.want {
			t.Errorf("%s: %q, want %q", c.m.Name, got, c.want)
		}
	}
}

func TestReporter(t *testing.T) {
	var b strings.Builder
	r := NewReporter(&b, false)
	r.Error("X.t", 3, BackendIOException, "a\tb", "c\nd")
	r.Warning("X.t", 0, PlatformStopped)
	r.Info("X.t", 0, TomGenerationPhase, 5) // not verbose: nothing
	want := "ERROR\tX.t\t3\tBackendPlugin : IOException occurs writting to file 'a b': c d \n" +
		"WARNING\tX.t\t0\tPlatform stopped\n"
	if b.String() != want {
		t.Fatalf("got %q, want %q", b.String(), want)
	}
	if e, w := r.Counts(); e != 1 || w != 1 || !r.HasError() {
		t.Fatalf("counts %d %d", e, w)
	}
	b.Reset()
	NewReporter(&b, true).Info("X.t", 0, TomGenerationPhase, 5)
	if b.String() != "INFO\tX.t\t0\tTOM generation phase (5 ms)\n" {
		t.Fatalf("info: %q", b.String())
	}
}

func TestCatchRuntime(t *testing.T) {
	if !CatchRuntime(func() {}) {
		t.Fatal("no panic")
	}
	var nilMap map[string]*int
	for name, f := range map[string]func(){
		"RuntimeError": func() { panic(&RuntimeError{Msg: "x"}) },
		"GomError":     func() { panic(&aterm.GomError{Msg: "This TomName has no String"}) },
		"nil deref":    func() { _ = *nilMap["a"] },
		"assertion":    func() { var v any = 1; _ = v.(string) },
	} {
		if CatchRuntime(f) {
			t.Errorf("%s: ok, want caught", name)
		}
	}
	defer func() {
		if v := recover(); v == nil || !errors.Is(v.(error), errOther) {
			t.Fatalf("other panic: %v", v)
		}
	}()
	CatchRuntime(func() { panic(errOther) })
}

var errOther = errors.New("other")

func TestRun(t *testing.T) {
	var b strings.Builder
	r := NewReporter(&b, false)
	if Run(r, "X.t", func() { panic(&RuntimeError{Msg: "boom"}) }) {
		t.Fatal("Run ok")
	}
	if b.String() != "ERROR\tX.t\t-1\tX.t: Unhandled exception \ntomgo: boom\n" || !r.HasError() {
		t.Fatalf("got %q", b.String())
	}
	if !Run(r, "X.t", func() {}) {
		t.Fatal("Run without panic")
	}
}

func TestJavaString(t *testing.T) {
	if JavaString(nil) != "null" || JavaString(3) != "3" || JavaString("s") != "s" {
		t.Fatal("JavaString")
	}
}
