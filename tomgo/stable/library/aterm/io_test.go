package aterm

import (
	"bytes"
	"testing"
)

// TestIO_RoundTrip — write a term out as text, read it back, and
// confirm we get the same canonical pointer.
func TestIO_RoundTrip(t *testing.T) {
	f := NewFactory()
	original := f.Parse(`f("hello",[1,2,3],<int>)`)

	var buf bytes.Buffer
	if _, err := WriteToTextFile(original, &buf); err != nil {
		t.Fatal(err)
	}

	roundtrip, err := f.ReadFromTextFile(&buf)
	if err != nil {
		t.Fatal(err)
	}
	if roundtrip != original {
		t.Errorf("round-trip changed identity:\n  original = %s\n  via IO   = %s",
			original.String(), roundtrip.String())
	}
}

// TestIO_RoundTrip_Sharing — IO doesn't fork the factory's hash
// table: every sub-term of the read-back result must already exist
// as a Go pointer from the original.
func TestIO_RoundTrip_Sharing(t *testing.T) {
	f := NewFactory()
	original := f.Parse("[1,2,3]")
	var buf bytes.Buffer
	WriteToTextFile(original, &buf)
	roundtrip, _ := f.ReadFromTextFile(&buf)
	if roundtrip != original {
		t.Errorf("list round-trip changed identity")
	}
}
