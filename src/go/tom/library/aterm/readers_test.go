package aterm

import (
	"bytes"
	"strings"
	"testing"
)

// TestReaders mirrors aterm.test.TestReaders.java — exercise every
// reader on the same baseline term and confirm they all decode to
// the same canonical (hash-consed) pointer. The Java version reads
// from on-disk fixtures (test.trm / test.baf / test.taf); we build
// the baseline programmatically so the test is self-contained.
//
// BAF and SAF are out of scope (per the project doc).
func TestReaders(t *testing.T) {
	f := NewFactory()
	// Baseline term: deliberately full of repeated subtrees so the
	// TAF round-trip exercises both the writer (deciding which
	// subterms are worth abbreviating) and the reader (resolving
	// `#<base64>` back to the right table entry).
	baseline := f.Parse(`f(g(1,2,3),g(1,2,3),h(g(1,2,3),"long string here"))`)

	t.Run("Text", func(t *testing.T) {
		var buf bytes.Buffer
		if _, err := WriteToTextFile(baseline, &buf); err != nil {
			t.Fatal(err)
		}
		got, err := f.ReadFromTextFile(&buf)
		if err != nil {
			t.Fatal(err)
		}
		if got != baseline {
			t.Errorf("text reader mismatch: %s", got.String())
		}
	})

	t.Run("SharedText", func(t *testing.T) {
		var buf bytes.Buffer
		if err := WriteToSharedTextFile(baseline, &buf); err != nil {
			t.Fatal(err)
		}
		// The TAF form should be visibly shorter than the plain text
		// because `g(1,2,3)` appears three times. Pin that property
		// — without it we wouldn't be testing TAF, just text.
		var plainBuf bytes.Buffer
		WriteToTextFile(baseline, &plainBuf)
		if buf.Len() >= plainBuf.Len() {
			t.Errorf("TAF (%d bytes) should be shorter than text (%d): %q",
				buf.Len(), plainBuf.Len(), buf.String())
		}
		if !strings.Contains(buf.String(), "#") {
			t.Errorf("TAF should contain abbrev marker '#', got: %q", buf.String())
		}
		got, err := f.ReadFromSharedTextFile(&buf)
		if err != nil {
			t.Fatal(err)
		}
		if got != baseline {
			t.Errorf("TAF reader mismatch: %s vs baseline %s", got.String(), baseline.String())
		}
	})
}

// TestReaders_TAFLargeTree — drive the abbreviation table further
// past the single-digit base-64 boundary so we exercise multi-digit
// encoding ("BA", "CA", …).
func TestReaders_TAFLargeTree(t *testing.T) {
	f := NewFactory()
	// Build [g(0), g(1), …, g(99)] — 100 distinct subterms with a
	// shared parent shape, plus repeats so most are worth abbreviating.
	items := make([]ATerm, 100)
	g := f.MakeAFun("g", 1, false)
	for i := range items {
		items[i] = f.MakeAppl(g, f.MakeInt(i))
	}
	list := f.MakeListEmpty()
	for i := len(items) - 1; i >= 0; i-- {
		list = f.MakeList(items[i], list)
	}
	// Wrap so the same list appears twice → at least one abbrev hits.
	pair := f.MakeAFun("pair", 2, false)
	baseline := f.MakeAppl(pair, list, list)

	var buf bytes.Buffer
	if err := WriteToSharedTextFile(baseline, &buf); err != nil {
		t.Fatal(err)
	}
	got, err := f.ReadFromSharedTextFile(&buf)
	if err != nil {
		t.Fatalf("read failed: %v\nTAF=%s", err, buf.String())
	}
	if got != baseline {
		t.Errorf("large-tree round-trip failed:\n  TAF=%s\n  got=%s",
			buf.String(), got.String())
	}
}

// TestReaders_TAFAbbrevEncoding — pin the abbreviation arithmetic so
// regressions in encode/decode are caught by a small test that doesn't
// need a full term tree.
func TestReaders_TAFAbbrevEncoding(t *testing.T) {
	cases := []struct {
		index int
		want  string
	}{
		{0, "A"},
		{1, "B"},
		{25, "Z"},
		{26, "a"},
		{51, "z"},
		{52, "0"},
		{61, "9"},
		{62, "+"},
		{63, "/"},
		{64, "BA"},
		{128, "CA"},
		{4095, "//"},
		{4096, "BAA"},
	}
	for _, tc := range cases {
		got := encodeAbbrev(tc.index)
		if got != tc.want {
			t.Errorf("encodeAbbrev(%d) = %q, want %q", tc.index, got, tc.want)
		}
		decoded, n := decodeAbbrev(tc.want)
		if decoded != tc.index || n != len(tc.want) {
			t.Errorf("decodeAbbrev(%q) = (%d, %d), want (%d, %d)",
				tc.want, decoded, n, tc.index, len(tc.want))
		}
	}
}

// TestReaders_TAFAbbrevSize — the abbreviation cost is what decides
// whether a subterm is worth recording. The writer and reader use the
// same formula, so any drift breaks round-trip.
func TestReaders_TAFAbbrevSize(t *testing.T) {
	cases := []struct {
		idx, want int
	}{
		{0, 2}, // "#A"
		{1, 2}, // "#B"
		{63, 2},
		{64, 3}, // "#BA"
		{4095, 3},
		{4096, 4},
	}
	for _, c := range cases {
		if got := abbrevSize(c.idx); got != c.want {
			t.Errorf("abbrevSize(%d) = %d, want %d", c.idx, got, c.want)
		}
	}
}
