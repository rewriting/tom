package aterm

import (
	"bufio"
	"fmt"
	"io"
)

// WriteToTextFile writes the term's textual form to w, using the
// canonical printer. Returns the bytes written and any I/O error.
func WriteToTextFile(t ATerm, w io.Writer) (int, error) {
	return fmt.Fprint(w, t.String())
}

// ReadFromTextFile slurps a textual term from r (up to the first
// EOF or invalid trailing input) and returns the parsed term.
func (f *Factory) ReadFromTextFile(r io.Reader) (ATerm, error) {
	body, err := io.ReadAll(bufio.NewReader(r))
	if err != nil {
		return nil, err
	}
	return f.Parse(string(body)), nil
}

// ReadFromFile is the path-shaped convenience over ReadFromTextFile.
// It's split out so callers can mock the io.Reader.
func (f *Factory) ReadFromFile(open func() (io.ReadCloser, error)) (ATerm, error) {
	r, err := open()
	if err != nil {
		return nil, err
	}
	defer r.Close()
	return f.ReadFromTextFile(r)
}
