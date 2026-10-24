package bridge

import (
	"bytes"
	"os"
	"path/filepath"
	"strconv"
	"strings"
	"testing"
)

const (
	usedLine  = `used UsedSymbolConstructor(Symbol(Name("zero"),TypesToType(concTomType(),Type(concTypeOption(),"term",EmptyTargetLanguageType())),concPairNameDecl(),concOption()))`
	typeLine  = `type Type(concTypeOption(),"term",TLType(" aterm.ATerm "))`
	tableLine = `table Table(concTomEntry())`
)

func codeLine(t testing.TB) string {
	t.Helper()
	p := filepath.Join("..", "..", "..", "..", "..", "test", "go", "j4", "testdata", "adtuse", "term.txt")
	b, err := os.ReadFile(p)
	if err != nil {
		t.Fatalf("no term.txt: %v", err)
	}
	return "code " + strings.TrimRight(string(b), "\n") + "\n"
}

func sample(t testing.TB) string {
	return strings.Join([]string{
		"tom-bridge 1",
		"from Backend",
		"input /tmp/X.t",
		"output /tmp/X.go",
		`option destdir string "out\"dir\nline2\\"`,
		"option goCode bool true",
		"option optLevel int 3",
		usedLine,
		typeLine,
		tableLine,
	}, "\n") + "\n" + codeLine(t) + "end\n"
}

func TestRoundTrip(t *testing.T) {
	in := sample(t)
	b, err := Read(strings.NewReader(in))
	if err != nil {
		t.Fatal(err)
	}
	if b.From != "Backend" || b.Input != "/tmp/X.t" || b.Output != "/tmp/X.go" {
		t.Fatalf("header: %+v", b)
	}
	if len(b.Options) != 3 || len(b.Used) != 1 || len(b.Types) != 1 {
		t.Fatalf("counts: %d options %d used %d types", len(b.Options), len(b.Used), len(b.Types))
	}
	if v, ok := b.Option("destdir"); !ok || v != "out\"dir\nline2\\" {
		t.Fatalf("destdir = %q, %v", v, ok)
	}
	if v, ok := b.Option("goCode"); !ok || v != "true" {
		t.Fatalf("goCode = %q, %v", v, ok)
	}
	if _, ok := b.Option("nope"); ok {
		t.Fatal("unexpected option")
	}
	var out bytes.Buffer
	if err := b.Write(&out); err != nil {
		t.Fatal(err)
	}
	if out.String() != in {
		t.Fatalf("round trip differs:\n got %.300q\nwant %.300q", out.String(), in)
	}
}

func TestStringOptionRawPreserved(t *testing.T) {
	// \u0001 is valid for strconv.Unquote and must be written back as is
	in := strings.Replace(sample(t), `"out\"dir\nline2\\"`, `"a\u0001b"`, 1)
	b, err := Read(strings.NewReader(in))
	if err != nil {
		t.Fatal(err)
	}
	if v, _ := b.Option("destdir"); v != "a\x01b" {
		t.Fatalf("value %q", v)
	}
	var out bytes.Buffer
	if err := b.Write(&out); err != nil || out.String() != in {
		t.Fatalf("raw literal not preserved: %v", err)
	}
}

func TestReadErrors(t *testing.T) {
	good := sample(t)
	lines := strings.SplitAfter(good, "\n")
	swap := func(i, j int) string {
		l := append([]string(nil), lines...)
		l[i], l[j] = l[j], l[i]
		return strings.Join(l, "")
	}
	idx := func(prefix string) int {
		for i, l := range lines {
			if strings.HasPrefix(l, prefix) {
				return i
			}
		}
		t.Fatalf("no line %q", prefix)
		return -1
	}
	cases := []struct {
		name, in, want string
	}{
		{"version", strings.Replace(good, "tom-bridge 1", "tom-bridge 2", 1), "line 1"},
		{"empty", "", "line 1"},
		{"no end", strings.TrimSuffix(good, "end\n"), "end"},
		{"code before table", swap(idx("table"), idx("code")), "line "},
		{"bad term", strings.Replace(good, tableLine, "table Table(concTomEntry(", 1), "line " + strconv.Itoa(idx("table")+1)},
		{"bad option kind", strings.Replace(good, "option goCode bool true", "option goCode float 1", 1), "line " + strconv.Itoa(idx("option goCode")+1)},
		{"bad bool", strings.Replace(good, "bool true", "bool maybe", 1), "line " + strconv.Itoa(idx("option goCode")+1)},
		{"bad int", strings.Replace(good, "int 3", "int x", 1), "line " + strconv.Itoa(idx("option optLevel")+1)},
		{"bad string", strings.Replace(good, `string "out`, `string out`, 1), "line " + strconv.Itoa(idx("option destdir")+1)},
		{"unknown keyword", strings.Replace(good, "from Backend", "frm Backend", 1), "line 2"},
		{"missing table", strings.Replace(good, tableLine+"\n", "", 1), "table"},
		{"trailing data", good + "extra\n", "line "},
	}
	for _, c := range cases {
		_, err := Read(strings.NewReader(c.in))
		if err == nil {
			t.Errorf("%s: no error", c.name)
			continue
		}
		if !strings.Contains(err.Error(), c.want) {
			t.Errorf("%s: error %q does not contain %q", c.name, err, c.want)
		}
	}
}

// A term line may be very long (up to 310 KB observed).
func TestLongLine(t *testing.T) {
	long := "code Tom(concCode(TargetLanguageToCode(TL(\"" + strings.Repeat("x", 1<<20) + "\",TextPosition(1,1),TextPosition(1,2)))))\n"
	in := strings.Join([]string{"tom-bridge 1", "from Backend", "input /i", "output /o", tableLine}, "\n") + "\n" + long + "end\n"
	b, err := Read(strings.NewReader(in))
	if err != nil {
		t.Fatal(err)
	}
	var out bytes.Buffer
	if err := b.Write(&out); err != nil || out.String() != in {
		t.Fatalf("long line round trip failed: %v", err)
	}
}

// TOMGO_DUMPS names a directory of *.tfix.* dumps; each is re-read as the
// term its name says (the full comparison is Task 9).
func TestDumpsReadable(t *testing.T) {
	dir := os.Getenv("TOMGO_DUMPS")
	if dir == "" {
		t.Skip("TOMGO_DUMPS not set")
	}
	files, _ := filepath.Glob(filepath.Join(dir, "*.tfix.*"))
	if len(files) == 0 {
		t.Skipf("no *.tfix.* in %s", dir)
	}
	for _, f := range files {
		data, err := os.ReadFile(f)
		if err != nil {
			t.Fatal(err)
		}
		line := strings.TrimRight(string(data), "\n")
		var in string
		switch {
		case strings.HasSuffix(f, ".table"):
			in = "tom-bridge 1\nfrom X\ninput /i\noutput /o\ntable " + line + "\n" + "code Tom(concCode())\nend\n"
		default:
			in = "tom-bridge 1\nfrom X\ninput /i\noutput /o\ntable Table(concTomEntry())\ncode " + line + "\nend\n"
		}
		if _, err := Read(strings.NewReader(in)); err != nil {
			t.Errorf("%s: %v", filepath.Base(f), err)
		}
	}
}
