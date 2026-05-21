package aterm

import (
	"bufio"
	"fmt"
	"io"
	"strconv"
	"strings"
	"unicode"
)

// TAF — "shared text format" — is the ATerm wire format that
// preserves hash-cons sharing across IO. Each subterm is assigned a
// base-64 index when first emitted; subsequent occurrences are
// emitted as `#<index>`. The reader maintains the same table so the
// round-trip reconstructs the exact term graph.
//
// This port follows aterm.pure.ATermWriter / PureFactory.parseAbbrev
// byte-for-byte on every shape the Java reference accepts.

// tobase64 mirrors the alphabet used by ATermWriter.
const tobase64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

// abbrevSize returns the number of bytes the encoder writes for the
// `#<base64>` form of `abbrev` (including the leading `#`). Matches
// PureFactory.abbrevSize exactly so writer/reader stay in agreement
// on whether a subterm is worth abbreviating.
func abbrevSize(abbrev int) int {
	if abbrev == 0 {
		return 2
	}
	size := 1
	for abbrev > 0 {
		size++
		abbrev /= 64
	}
	return size
}

// encodeAbbrev produces the base-64 textual form of `abbrev` (without
// the leading `#`). Digits are written most-significant-first; 0
// renders as a single "A".
func encodeAbbrev(abbrev int) string {
	if abbrev == 0 {
		return "A"
	}
	var b strings.Builder
	for abbrev > 0 {
		b.WriteByte(tobase64[abbrev%64])
		abbrev /= 64
	}
	// reverse
	s := b.String()
	out := make([]byte, len(s))
	for i := range s {
		out[len(s)-1-i] = s[i]
	}
	return string(out)
}

// decodeAbbrev is the inverse of encodeAbbrev. Returns the integer
// index and the number of bytes consumed (so the caller can advance
// its cursor). The first byte MUST be a base-64 digit — `#` is
// stripped by the caller.
func decodeAbbrev(src string) (int, int) {
	abbrev := 0
	n := 0
	for n < len(src) {
		c := src[n]
		if !isBase64Digit(c) {
			break
		}
		abbrev *= 64
		switch {
		case c >= 'A' && c <= 'Z':
			abbrev += int(c - 'A')
		case c >= 'a' && c <= 'z':
			abbrev += int(c-'a') + 26
		case c >= '0' && c <= '9':
			abbrev += int(c-'0') + 52
		case c == '+':
			abbrev += 62
		case c == '/':
			abbrev += 63
		}
		n++
	}
	return abbrev, n
}

func isBase64Digit(c byte) bool {
	return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
		(c >= '0' && c <= '9') || c == '+' || c == '/'
}

// WriteToSharedTextFile renders `t` in TAF format to `w`. Shared
// subterms are encoded as `#abbrev` after their first appearance.
func WriteToSharedTextFile(t ATerm, w io.Writer) error {
	tw := &tafWriter{w: w, table: map[ATerm]int{}}
	return tw.visitChild(t)
}

type tafWriter struct {
	w         io.Writer
	pos       int
	table     map[ATerm]int
	nextIndex int
}

func (tw *tafWriter) write(s string) error {
	n, err := io.WriteString(tw.w, s)
	tw.pos += n
	return err
}

func (tw *tafWriter) writeByte(c byte) error {
	return tw.write(string([]byte{c}))
}

func (tw *tafWriter) visitChild(child ATerm) error {
	if idx, ok := tw.table[child]; ok {
		if err := tw.writeByte('#'); err != nil {
			return err
		}
		return tw.write(encodeAbbrev(idx))
	}
	start := tw.pos

	if child.Type() == LIST {
		if err := tw.writeByte('['); err != nil {
			return err
		}
	}
	if err := tw.visit(child); err != nil {
		return err
	}
	if child.Type() == LIST {
		if err := tw.writeByte(']'); err != nil {
			return err
		}
	}
	// Annotations
	annos := child.GetAnnotations()
	if annos != nil && !annos.IsEmpty() {
		if err := tw.writeByte('{'); err != nil {
			return err
		}
		if err := tw.visit(annos); err != nil {
			return err
		}
		if err := tw.writeByte('}'); err != nil {
			return err
		}
	}

	length := tw.pos - start
	if length > abbrevSize(tw.nextIndex) {
		tw.table[child] = tw.nextIndex
		tw.nextIndex++
	}
	return nil
}

// visit dispatches by term shape — applies / lists / placeholders /
// numerics. List handling is "elements separated by commas, no
// enclosing brackets" since visitChild already wrote those.
func (tw *tafWriter) visit(t ATerm) error {
	switch x := t.(type) {
	case *ATermAppl:
		if err := tw.write(x.fun.String()); err != nil {
			return err
		}
		if x.fun.arity > 0 || x.fun.name == "" {
			if err := tw.writeByte('('); err != nil {
				return err
			}
			for i, a := range x.args {
				if i > 0 {
					if err := tw.writeByte(','); err != nil {
						return err
					}
				}
				if err := tw.visitChild(a); err != nil {
					return err
				}
			}
			if err := tw.writeByte(')'); err != nil {
				return err
			}
		}
	case *ATermList:
		cur := x
		first := true
		for !cur.IsEmpty() {
			if !first {
				if err := tw.writeByte(','); err != nil {
					return err
				}
			}
			first = false
			if err := tw.visitChild(cur.head); err != nil {
				return err
			}
			cur = cur.tail
		}
	case *ATermPlaceholder:
		if err := tw.writeByte('<'); err != nil {
			return err
		}
		if err := tw.visitChild(x.placeholderType); err != nil {
			return err
		}
		if err := tw.writeByte('>'); err != nil {
			return err
		}
	case *ATermInt:
		return tw.write(strconv.FormatInt(int64(x.value), 10))
	case *ATermLong:
		return tw.write(strconv.FormatInt(x.value, 10))
	case *ATermReal:
		return tw.write(strconv.FormatFloat(x.value, 'g', -1, 64))
	}
	return nil
}

// ReadFromSharedTextFile parses a TAF stream into a term. The reader
// keeps the abbreviation table alive for the whole stream so any
// `#abbrev` reference resolves correctly.
func (f *Factory) ReadFromSharedTextFile(r io.Reader) (ATerm, error) {
	body, err := io.ReadAll(bufio.NewReader(r))
	if err != nil {
		return nil, err
	}
	tr := &tafReader{factory: f, src: string(body), table: nil}
	tr.table = []ATerm{} // initialize sharing
	tr.skipWS()
	t, err := tr.parseTerm()
	if err != nil {
		return nil, err
	}
	return t, nil
}

type tafReader struct {
	factory *Factory
	src     string
	pos     int
	table   []ATerm
}

func (tr *tafReader) peek() byte {
	if tr.pos >= len(tr.src) {
		return 0
	}
	return tr.src[tr.pos]
}

func (tr *tafReader) skipWS() {
	for tr.pos < len(tr.src) {
		c := tr.src[tr.pos]
		if c == ' ' || c == '\t' || c == '\n' || c == '\r' {
			tr.pos++
			continue
		}
		break
	}
}

// parseTerm is the TAF analogue of textParser.parseTerm — it dispatches
// on the lookahead byte, including the `#` abbrev case, and records
// each fully parsed term in the sharing table when the size exceeds
// the abbreviation cost.
func (tr *tafReader) parseTerm() (ATerm, error) {
	start := tr.pos
	tr.skipWS()
	c := tr.peek()
	if c == 0 {
		return nil, fmt.Errorf("aterm/taf: premature EOF")
	}
	var result ATerm
	switch {
	case c == '#':
		tr.pos++
		abbrev, n := decodeAbbrev(tr.src[tr.pos:])
		if n == 0 {
			return nil, fmt.Errorf("aterm/taf: expected base-64 digit after '#' at %d", tr.pos)
		}
		tr.pos += n
		if abbrev < 0 || abbrev >= len(tr.table) {
			return nil, fmt.Errorf("aterm/taf: abbrev #%d out of range", abbrev)
		}
		// Note: pre-existing terms aren't re-recorded — the table
		// entry already exists and a second copy would shift the
		// indices of every subsequent abbrev.
		return tr.table[abbrev], nil
	case c == '[':
		tr.pos++
		tr.skipWS()
		if tr.peek() == ']' {
			tr.pos++
			result = tr.factory.emptyList
		} else {
			elems, err := tr.parseTermList()
			if err != nil {
				return nil, err
			}
			if tr.peek() != ']' {
				return nil, fmt.Errorf("aterm/taf: expected ']' at %d", tr.pos)
			}
			tr.pos++
			list := tr.factory.emptyList
			for i := len(elems) - 1; i >= 0; i-- {
				list = tr.factory.MakeList(elems[i], list)
			}
			result = list
		}
	case c == '<':
		tr.pos++
		tr.skipWS()
		inner, err := tr.parseTerm()
		if err != nil {
			return nil, err
		}
		tr.skipWS()
		if tr.peek() != '>' {
			return nil, fmt.Errorf("aterm/taf: expected '>' at %d", tr.pos)
		}
		tr.pos++
		result = tr.factory.MakePlaceholder(inner)
	case c == '"':
		name, err := tr.parseQuotedString()
		if err != nil {
			return nil, err
		}
		tr.skipWS()
		result = tr.parseApplAfterName(name, true)
	case c == '(':
		result = tr.parseAppl("", false)
	case c == '-' || (c >= '0' && c <= '9'):
		var err error
		result, err = tr.parseNumber()
		if err != nil {
			return nil, err
		}
	default:
		if unicode.IsLetter(rune(c)) {
			name := tr.parseIdent()
			tr.skipWS()
			result = tr.parseApplAfterName(name, false)
		} else {
			return nil, fmt.Errorf("aterm/taf: illegal character %q at %d", c, tr.pos)
		}
	}

	// Optional `{annos}` block.
	tr.skipWS()
	if tr.peek() == '{' {
		tr.pos++
		tr.skipWS()
		if tr.peek() == '}' {
			tr.pos++
		} else {
			elems, err := tr.parseTermList()
			if err != nil {
				return nil, err
			}
			if tr.peek() != '}' {
				return nil, fmt.Errorf("aterm/taf: expected '}' at %d", tr.pos)
			}
			tr.pos++
			annos := tr.factory.emptyList
			for i := len(elems) - 1; i >= 0; i-- {
				annos = tr.factory.MakeList(elems[i], annos)
			}
			if appl, ok := result.(*ATermAppl); ok {
				result = appl.SetAnnotations(annos)
			}
		}
	}

	size := tr.pos - start
	if size > abbrevSize(len(tr.table)) {
		tr.table = append(tr.table, result)
	}
	return result, nil
}

// parseTermList reads `term (',' term)*` — the inner contents of a
// list, an arg-list, or an anno-block.
func (tr *tafReader) parseTermList() ([]ATerm, error) {
	var elems []ATerm
	for {
		tr.skipWS()
		t, err := tr.parseTerm()
		if err != nil {
			return nil, err
		}
		elems = append(elems, t)
		tr.skipWS()
		if tr.peek() != ',' {
			break
		}
		tr.pos++
	}
	return elems, nil
}

func (tr *tafReader) parseApplAfterName(name string, quoted bool) ATerm {
	if tr.peek() != '(' {
		fun := tr.factory.MakeAFun(name, 0, quoted)
		return tr.factory.MakeAppl(fun)
	}
	return tr.parseAppl(name, quoted)
}

func (tr *tafReader) parseAppl(name string, quoted bool) ATerm {
	tr.pos++ // (
	tr.skipWS()
	var args []ATerm
	if tr.peek() != ')' {
		var err error
		args, err = tr.parseTermList()
		if err != nil {
			return nil
		}
	}
	if tr.peek() == ')' {
		tr.pos++
	}
	fun := tr.factory.MakeAFun(name, len(args), quoted)
	return tr.factory.MakeAppl(fun, args...)
}

func (tr *tafReader) parseIdent() string {
	start := tr.pos
	for tr.pos < len(tr.src) {
		c := tr.src[tr.pos]
		if (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '-' || c == '+' || c == '*' || c == '$' {
			tr.pos++
			continue
		}
		break
	}
	return tr.src[start:tr.pos]
}

func (tr *tafReader) parseQuotedString() (string, error) {
	tr.pos++ // opening "
	var b strings.Builder
	for tr.pos < len(tr.src) {
		c := tr.src[tr.pos]
		if c == '"' {
			tr.pos++
			return b.String(), nil
		}
		if c == '\\' {
			if tr.pos+1 >= len(tr.src) {
				return "", fmt.Errorf("aterm/taf: unterminated quoted symbol")
			}
			nxt := tr.src[tr.pos+1]
			switch {
			case nxt == 'n':
				b.WriteByte('\n')
				tr.pos += 2
			case nxt == 't':
				b.WriteByte('\t')
				tr.pos += 2
			case nxt == 'r':
				b.WriteByte('\r')
				tr.pos += 2
			case nxt == '\\' || nxt == '"' || nxt == '\'':
				b.WriteByte(nxt)
				tr.pos += 2
			case nxt >= '0' && nxt <= '7':
				end := tr.pos + 1
				for end < len(tr.src) && end < tr.pos+4 && tr.src[end] >= '0' && tr.src[end] <= '7' {
					end++
				}
				n, _ := strconv.ParseInt(tr.src[tr.pos+1:end], 8, 32)
				b.WriteByte(byte(n))
				tr.pos = end
			default:
				b.WriteByte('\\')
				b.WriteByte(nxt)
				tr.pos += 2
			}
			continue
		}
		b.WriteByte(c)
		tr.pos++
	}
	return "", fmt.Errorf("aterm/taf: unterminated quoted symbol")
}

func (tr *tafReader) parseNumber() (ATerm, error) {
	start := tr.pos
	if tr.peek() == '-' || tr.peek() == '+' {
		tr.pos++
	}
	for tr.pos < len(tr.src) && tr.src[tr.pos] >= '0' && tr.src[tr.pos] <= '9' {
		tr.pos++
	}
	isReal := false
	if tr.peek() == '.' {
		isReal = true
		tr.pos++
		for tr.pos < len(tr.src) && tr.src[tr.pos] >= '0' && tr.src[tr.pos] <= '9' {
			tr.pos++
		}
	}
	if tr.peek() == 'e' || tr.peek() == 'E' {
		isReal = true
		tr.pos++
		if tr.peek() == '+' || tr.peek() == '-' {
			tr.pos++
		}
		for tr.pos < len(tr.src) && tr.src[tr.pos] >= '0' && tr.src[tr.pos] <= '9' {
			tr.pos++
		}
	}
	if isReal {
		v, err := strconv.ParseFloat(tr.src[start:tr.pos], 64)
		if err != nil {
			return nil, fmt.Errorf("aterm/taf: bad real: %w", err)
		}
		return tr.factory.MakeReal(v), nil
	}
	if tr.peek() == 'L' || tr.peek() == 'l' {
		tr.pos++
		v, _ := strconv.ParseInt(tr.src[start:tr.pos-1], 10, 64)
		return tr.factory.MakeLong(v), nil
	}
	v, _ := strconv.ParseInt(tr.src[start:tr.pos], 10, 64)
	return tr.factory.MakeInt(int(v)), nil
}
