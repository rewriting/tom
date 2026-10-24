package tools

import (
	"io"
	"strconv"
	"strings"
	"unicode"
	"unicode/utf8"

	"tom/engine/platform"
)

// OutputCode is tom.engine.tools.OutputCode (OutputCode.java) for the Go
// target (D5): the indentation of the C, Ada and Python targets (indent =
// pCode || aCode) and the #line directives of C are not ported, -go turns
// those options off.
type OutputCode struct {
	w           io.Writer
	sb          *strings.Builder // the StringWriter of NewStringOutputCode
	err         error            // the first write error
	lineCounter int64
	singleLine  int
	pretty      bool
	goCode      bool
	lineTail    string // Go only: the end of the output line being written (see goSingleLine)
}

// NewOutputCode is new OutputCode(file, optionManager) (OutputCode.java:49).
func NewOutputCode(w io.Writer, opts *platform.Options) *OutputCode {
	if opts.Bool("cCode") || opts.Bool("aCode") || opts.Bool("pCode") {
		panic(&platform.RuntimeError{Msg: "OutputCode: the Go suffix generates Go only (cCode, aCode or pCode is set)"})
	}
	return &OutputCode{w: w, lineCounter: 1, pretty: opts.Bool("pretty"), goCode: opts.Bool("goCode")}
}

// NewStringOutputCode is new OutputCode(new StringWriter())
// (OutputCode.java:60): no option is read, so neither pretty nor goCode.
func NewStringOutputCode() *OutputCode {
	sb := &strings.Builder{}
	return &OutputCode{w: sb, sb: sb, lineCounter: 1}
}

// SetSingleLine is setSingleLine (OutputCode.java:64).
func (o *OutputCode) SetSingleLine() { o.singleLine++ }

// UnsetSingleLine is unsetSingleLine (OutputCode.java:68).
func (o *OutputCode) UnsetSingleLine() { o.singleLine-- }

// raw writes s; port: Java prints "write error" and goes on, here the
// first error is kept for Close and the next writes are dropped.
func (o *OutputCode) raw(s string) {
	if o.err != nil {
		return
	}
	if _, err := io.WriteString(o.w, s); err != nil {
		o.err = err
	}
}

// WriteOpenBrace is writeOpenBrace (OutputCode.java:80).
func (o *OutputCode) WriteOpenBrace() { o.Write("(") }

// WriteCloseBrace is writeCloseBrace (OutputCode.java:84).
func (o *OutputCode) WriteCloseBrace() { o.Write(")") }

// WriteComa is writeComa (OutputCode.java:88).
func (o *OutputCode) WriteComa() { o.Write(",") }

// Write is write(String) (OutputCode.java:101).
func (o *OutputCode) Write(s string) {
	o.raw(s)
	o.track(s)
}

// WriteInt is write(int) (OutputCode.java:111).
func (o *OutputCode) WriteInt(n int64) { o.Write(strconv.FormatInt(n, 10)) }

// WriteDeep is write(int deep, String) (OutputCode.java:115); indent(deep)
// writes nothing with -go (indent = pCode || aCode).
func (o *OutputCode) WriteDeep(deep int, s string) { o.Write(s) }

// Writeln is writeln() (OutputCode.java:120): a newline in pretty mode,
// not tracked (Java writes it on the file directly).
func (o *OutputCode) Writeln() {
	if o.pretty {
		o.raw("\n")
	}
}

// writelnDeep is writeln(int deep, String) (OutputCode.java:131).
func (o *OutputCode) writelnDeep(deep int, s string) {
	o.WriteDeep(deep, s)
	o.Writeln()
}

// makeSingleLine is makeSingleLine (OutputCode.java:156).
func (o *OutputCode) makeSingleLine(s string) string {
	if o.goCode {
		s = goSingleLine(o.lineTail, s)
	}
	return strings.NewReplacer("\n", " ", "\r", " ", "\t", " ").Replace(s)
}

// goKeywords is GO_KEYWORDS (OutputCode.java:166): the keywords after
// which Go inserts no semicolon at the end of a line.
var goKeywords = map[string]bool{
	"case": true, "chan": true, "const": true, "default": true, "defer": true,
	"else": true, "for": true, "func": true, "go": true, "goto": true, "if": true,
	"import": true, "interface": true, "map": true, "package": true, "range": true,
	"select": true, "struct": true, "switch": true, "type": true, "var": true,
}

// goSingleLine is goSingleLine (OutputCode.java:182): Go code put on a
// single line keeps the semicolons Go inserts at the end of a line: a
// newline becomes "; " where the Go lexer would insert one, and a space
// elsewhere; the first line of s continues the output line written so
// far (before); no "; " is written when the next non-blank line starts
// with an explicit ';'.
func goSingleLine(before, s string) string {
	var res strings.Builder
	lines := strings.Split(s, "\n") // Java: s.split("\n", -1)
	for i, ln := range lines {
		res.WriteString(ln)
		if i < len(lines)-1 {
			line := ln
			if i == 0 {
				line = before + ln
			}
			semicolon := goInsertsSemicolon(line)
			if semicolon {
				for _, next := range lines[i+1:] {
					if n := javaTrim(next); n != "" {
						semicolon = n[0] != ';'
						break
					}
				}
			}
			if semicolon {
				res.WriteString("; ")
			} else {
				res.WriteString(" ")
			}
		}
	}
	return res.String()
}

// track is track (OutputCode.java:209): lineTail is the end of the output
// line written so far. port: Java keeps its last 256 UTF-16 chars, this
// keeps the last 256 runes (the same on BMP text).
func (o *OutputCode) track(s string) {
	if !o.goCode {
		return
	}
	var t string
	if nl := strings.LastIndexByte(s, '\n'); nl >= 0 {
		t = s[nl+1:]
	} else {
		t = o.lineTail + s
	}
	if n := utf8.RuneCountInString(t); n > 256 {
		t = string([]rune(t)[n-256:])
	}
	o.lineTail = t
}

// javaSpace is the class \s of java.util.regex: [ \t\n\x0B\f\r].
const javaSpace = " \t\n\x0B\f\r"

// javaTrim is String.trim: it removes the characters <= ' ' at both ends.
func javaTrim(s string) string {
	return strings.TrimFunc(s, func(r rune) bool { return r <= ' ' })
}

// isJavaLetterOrDigit is Character.isLetterOrDigit on one UTF-16 char: a
// rune outside the BMP (a surrogate pair in Java) is neither.
func isJavaLetterOrDigit(r rune) bool {
	return r <= 0xFFFF && (unicode.IsLetter(r) || unicode.IsDigit(r))
}

// goInsertsSemicolon is goInsertsSemicolon (OutputCode.java:218): whether
// the Go lexer inserts a semicolon at the end of line (after an
// identifier that is not one of goKeywords, a literal, ++, --, or a
// closing bracket).
func goInsertsSemicolon(line string) bool {
	l := strings.TrimRight(line, javaSpace)
	if l == "" {
		return false
	}
	r, _ := utf8.DecodeLastRuneInString(l)
	switch r {
	case ')', ']', '}', '"', '\'', '`':
		return true
	}
	if strings.HasSuffix(l, "++") || strings.HasSuffix(l, "--") {
		return true
	}
	if isJavaLetterOrDigit(r) || r == '_' {
		i := len(l)
		for i > 0 {
			c, size := utf8.DecodeLastRuneInString(l[:i])
			if !isJavaLetterOrDigit(c) && c != '_' {
				break
			}
			i -= size
		}
		return !goKeywords[l[i:]]
	}
	return false
}

// javaSplit is s.split("\n") of Java: the trailing empty strings are
// removed, and "" gives [""].
func javaSplit(s string) []string {
	if s == "" {
		return []string{""}
	}
	parts := strings.Split(s, "\n")
	for len(parts) > 0 && parts[len(parts)-1] == "" {
		parts = parts[:len(parts)-1]
	}
	return parts
}

// WriteTL is write(int deep, String s, int line, int length)
// (OutputCode.java:240), the branches of the Go target: without pretty,
// host code is written at its source line (newlines are added up to
// line) or, when that line is behind or single-line mode is on, on a
// single line; with pretty, each line is written without its leading
// spaces. Java: the aCode branch and the #line of cCode are omitted (D5).
func (o *OutputCode) WriteTL(deep int, s string, line, length int64) {
	if !o.pretty {
		if o.singleLine > 0 || o.lineCounter > line {
			o.Write(o.makeSingleLine(s))
			return
		}
		for o.lineCounter < line {
			o.Write("\n")
			o.lineCounter++
		}
		o.Write(s)
		o.lineCounter += length
		return
	}
	lines := javaSplit(s)
	if len(lines) == 0 {
		o.WriteDeep(deep, strings.TrimLeft(s, javaSpace))
		return
	}
	for _, ln := range lines {
		o.writelnDeep(deep, strings.TrimLeft(ln, javaSpace))
	}
}

// Close is close (OutputCode.java:299): it flushes and closes the writer
// when it can, and returns the first error.
func (o *OutputCode) Close() error {
	if f, ok := o.w.(interface{ Flush() error }); ok {
		if err := f.Flush(); err != nil && o.err == nil {
			o.err = err
		}
	}
	if c, ok := o.w.(io.Closer); ok {
		if err := c.Close(); err != nil && o.err == nil {
			o.err = err
		}
	}
	return o.err
}

// StringDump is stringDump (OutputCode.java:309): the text written in an
// OutputCode of NewStringOutputCode. Java: InternalError otherwise.
func (o *OutputCode) StringDump() string {
	if o.sb == nil {
		panic(&platform.RuntimeError{Msg: "OutputCode does not contain any string"})
	}
	return o.sb.String()
}
