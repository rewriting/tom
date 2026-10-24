package j4

import (
	"fmt"
	"math/rand"
	"os"
	"path/filepath"
	"strconv"
	"strings"
	"testing"

	"tom/engine/bridge"
	"tom/engine/platform"
	"tom/engine/tools"
	"tomtools/harness"
)

// handScripts are the cases of the unit tests of tools.OutputCode, as
// scripts of OutputCodeScript.java.
var handScripts = []string{
	"script go false\ntl 0 3 0 \"f()\"\ntl 0 3 2 \"a := 1\\nreturn a\\n\"\ntl 0 4 1 \"x\\ny\"\nend\n",
	"script go false\nwrite \"f(x)\"\nsingle\ntl 0 1 1 \"\\ny\"\nunsingle\nend\n",
	"script go false\nsingle\ntl 0 1 1 \"a\\tb\\r\\nc\"\nend\n",
	"script string false\ntl 0 0 1 \"x\\ny\"\nint -42\nopen\ncoma\nclosebrace\ndeep 3 \"z\"\nwriteln\nend\n",
	"script go true\ntl 2 1 4 \"  a\\n\\n  b\\n\\n\"\nend\n",
	"script go true\ntl 2 1 2 \"\\n\\n\"\nend\n",
	"script go true\ntl 2 1 0 \"\"\nwriteln\nend\n",
}

var scriptPieces = []string{"x", "y1", "return", "break", "else", "func", "go", "a++", "b--", "f(x)", "m[i]", "}", "{",
	"\"s\"", "'c'", "`r`", ";", "; y := 2", "", "   ", "\t", "\r", "été", "x :=", "+", "if a {", "case 1:", "_ = s", "// c", "é"}

var scriptSeps = []string{"\n", " ", "\n\n", " \n ", "\r\n"}

// randomText is a piece of Go host code drawn with r, made to exercise
// goSingleLine: identifiers, keywords, closing brackets, literals, ++/--,
// explicit ';', blank lines, tabs, '\r', non-ASCII letters.
func randomText(r *rand.Rand) string {
	var b strings.Builder
	for k := 1 + r.Intn(4); k > 0; k-- {
		b.WriteString(scriptPieces[r.Intn(len(scriptPieces))])
		if k > 1 {
			b.WriteString(scriptSeps[r.Intn(len(scriptSeps))])
		}
	}
	if r.Intn(3) == 0 {
		b.WriteString("\n")
	}
	return b.String()
}

// randomScript is a script of OutputCodeScript.java drawn with r; the line
// numbers wander around the line counter so that both the padding and the
// single-line branches of WriteTL are taken.
func randomScript(r *rand.Rand, mode string, pretty bool) string {
	var b strings.Builder
	fmt.Fprintf(&b, "script %s %v\n", mode, pretty)
	line, single := int64(1), 0
	for n := 5 + r.Intn(20); n > 0; n-- {
		switch k := r.Intn(20); {
		case k < 6:
			fmt.Fprintf(&b, "write %s\n", bridge.JavaQuote(randomText(r)))
		case k < 14:
			s := randomText(r)
			length := int64(strings.Count(s, "\n"))
			if r.Intn(5) == 0 {
				length += int64(r.Intn(3)) - 1
			}
			line += int64(r.Intn(5)) - 1
			fmt.Fprintf(&b, "tl %d %d %d %s\n", r.Intn(3), line, length, bridge.JavaQuote(s))
		case k == 14:
			fmt.Fprintf(&b, "int %d\n", r.Intn(2000)-1000)
		case k == 15:
			fmt.Fprintf(&b, "deep %d %s\n", r.Intn(3), bridge.JavaQuote(randomText(r)))
		case k == 16:
			b.WriteString([]string{"writeln\n", "open\n", "closebrace\n", "coma\n"}[r.Intn(4)])
		case k < 19:
			if single < 2 {
				b.WriteString("single\n")
				single++
			}
		default:
			if single > 0 {
				b.WriteString("unsingle\n")
				single--
			}
		}
	}
	b.WriteString("end\n")
	return b.String()
}

func boolOption(name string, v bool) bridge.Option {
	return bridge.Option{Name: name, Kind: "bool", Value: strconv.FormatBool(v)}
}

// replay runs one script on tools.OutputCode and returns what it wrote.
func replay(t *testing.T, script string) string {
	t.Helper()
	var oc *tools.OutputCode
	var buf strings.Builder
	unquote := func(q string) string {
		s, err := strconv.Unquote(q)
		if err != nil {
			t.Fatalf("bad quoted string %s: %v", q, err)
		}
		return s
	}
	atoi := func(s string) int64 {
		n, err := strconv.ParseInt(s, 10, 64)
		if err != nil {
			t.Fatal(err)
		}
		return n
	}
	for _, line := range strings.Split(strings.TrimSuffix(script, "\n"), "\n") {
		op, rest, _ := strings.Cut(line, " ")
		switch op {
		case "script":
			g := strings.Split(rest, " ")
			if g[0] == "go" {
				buf.Reset()
				oc = tools.NewOutputCode(&buf, platform.NewOptions([]bridge.Option{
					boolOption("aCode", false), boolOption("cCode", false), boolOption("goCode", true),
					boolOption("pCode", false), boolOption("pretty", g[1] == "true"),
				}))
			} else {
				oc = tools.NewStringOutputCode()
			}
		case "write":
			oc.Write(unquote(rest))
		case "int":
			oc.WriteInt(atoi(rest))
		case "deep":
			g := strings.SplitN(rest, " ", 2)
			oc.WriteDeep(int(atoi(g[0])), unquote(g[1]))
		case "tl":
			g := strings.SplitN(rest, " ", 4)
			oc.WriteTL(int(atoi(g[0])), unquote(g[3]), atoi(g[1]), atoi(g[2]))
		case "writeln":
			oc.Writeln()
		case "open":
			oc.WriteOpenBrace()
		case "closebrace":
			oc.WriteCloseBrace()
		case "coma":
			oc.WriteComa()
		case "single":
			oc.SetSingleLine()
		case "unsingle":
			oc.UnsetSingleLine()
		case "end":
			if strings.HasPrefix(script, "script go ") {
				if err := oc.Close(); err != nil {
					t.Fatal(err)
				}
				return buf.String()
			}
			return oc.StringDump()
		default:
			t.Fatalf("bad operation %q", line)
		}
	}
	t.Fatal("script without end")
	return ""
}

// TestOutputCodeJava replays the hand-written scripts and 400 random ones
// (fixed seed) on tom.engine.tools.OutputCode (Java, OutputCodeScript) and
// on tools.OutputCode (Go): the outputs must be identical byte for byte.
func TestOutputCodeJava(t *testing.T) {
	dist := harness.NewDist(t)
	cp := javaHelpers(t, dist)
	r := rand.New(rand.NewSource(1))
	scripts := append([]string(nil), handScripts...)
	for _, m := range []struct {
		mode   string
		pretty bool
		n      int
	}{{"go", false, 200}, {"go", true, 100}, {"string", false, 100}} {
		for i := 0; i < m.n; i++ {
			scripts = append(scripts, randomScript(r, m.mode, m.pretty))
		}
	}
	dir := t.TempDir()
	in, out := filepath.Join(dir, "scripts.txt"), filepath.Join(dir, "results.txt")
	if err := os.WriteFile(in, []byte(strings.Join(scripts, "")), 0o644); err != nil {
		t.Fatal(err)
	}
	if msg, err := harness.RunIn(dir, nil, "java", "-cp", cp, "OutputCodeScript", in, out); err != nil {
		t.Fatalf("java OutputCodeScript: %v\n%s", err, msg)
	}
	b, err := os.ReadFile(out)
	if err != nil {
		t.Fatal(err)
	}
	results := strings.Split(strings.TrimSuffix(string(b), "\n"), "\n")
	if len(results) != len(scripts) {
		t.Fatalf("%d results for %d scripts", len(results), len(scripts))
	}
	same, differ := 0, 0
	for i, s := range scripts {
		want, err := strconv.Unquote(strings.TrimPrefix(results[i], "result "))
		if err != nil {
			t.Fatalf("result %d: %v", i, err)
		}
		if got := replay(t, s); got != want {
			differ++
			if differ <= 3 {
				t.Errorf("script %d:\n%s\nGo   %q\nJava %q", i, s, got, want)
			}
			continue
		}
		same++
	}
	t.Logf("OutputCode: scripts %d, identiques %d, différents %d", len(scripts), same, differ)
}
