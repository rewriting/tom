// Package bridge reads and writes the bundle the Tom+Java compiler hands
// over to the Go suffix (spec J4 §4.2): one datum per line, Gom terms by
// their String().
package bridge

import (
	"bufio"
	"errors"
	"fmt"
	"io"
	"strconv"
	"strings"

	"tom/engine/adt"
)

// Version is the bundle format version, the number of the first line.
const Version = 1

// Option is one option of the Java compiler (TomOptionManager).
type Option struct {
	Name  string
	Kind  string // "bool", "int" or "string"
	Value string // decoded value (strconv.Unquote for "string")

	raw string // the value as written, kept for an identical Write
}

// Bundle is the content of a bundle.
type Bundle struct {
	From    string         // plugin name, without spaces ("Backend")
	Input   string         // absolute path of the .t
	Output  string         // absolute path of the generated file
	Options []Option       // sorted by name
	Used    []adt.KeyEntry // sorted by String()
	Types   []adt.TomType  // in the order of the Java map keys
	Table   adt.TomSymbolTable
	Code    adt.Code
}

// Option returns the decoded value of an option and whether it exists.
func (b *Bundle) Option(name string) (string, bool) {
	for _, o := range b.Options {
		if o.Name == name {
			return o.Value, true
		}
	}
	return "", false
}

// order of the lines after the header; option, used and type repeat.
const (
	rOption = iota + 1
	rUsed
	rType
	rTable
	rCode
	rEnd
)

var rank = map[string]int{"option": rOption, "used": rUsed, "type": rType, "table": rTable, "code": rCode, "end": rEnd}

// Error is a malformed bundle; Line is the 1-based line number (0 when
// the error is not tied to a line).
type Error struct {
	Line int
	Msg  string
}

func (e *Error) Error() string { return fmt.Sprintf("line %d: %s", e.Line, e.Msg) }

// readLine returns the next line without its newline; a last line without
// newline is returned too (the missing "end" is detected by the caller).
func readLine(r *bufio.Reader) (string, error) {
	s, err := r.ReadString('\n')
	if err != nil && (err != io.EOF || s == "") {
		return "", err
	}
	return strings.TrimSuffix(s, "\n"), nil
}

// parseTerm runs a generated XFromString, which panics on a bad term.
func parseTerm[T any](line int, s string, f func(string) T) (v T, err error) {
	defer func() {
		if r := recover(); r != nil {
			err = &Error{line, fmt.Sprintf("unreadable term: %v", r)}
		}
	}()
	return f(s), nil
}

// Read parses a bundle; any malformed, missing or out-of-order line, an
// unknown version or a missing "end" is an error naming the line number.
func Read(r io.Reader) (*Bundle, error) {
	br := bufio.NewReaderSize(r, 1<<20)
	n := 0
	next := func() (string, error) {
		s, err := readLine(br)
		if err == io.EOF {
			return "", &Error{n + 1, "unexpected end of input (missing \"end\"?)"}
		}
		if err != nil {
			return "", &Error{n + 1, err.Error()}
		}
		n++
		return s, nil
	}
	header := func(key string) (string, error) {
		s, err := next()
		if err != nil {
			return "", err
		}
		v, ok := strings.CutPrefix(s, key+" ")
		if !ok || v == "" {
			return "", &Error{n, fmt.Sprintf("expected %q line, got %.40q", key, s)}
		}
		return v, nil
	}
	s, err := next()
	if err != nil {
		return nil, err
	}
	if s != "tom-bridge "+strconv.Itoa(Version) {
		return nil, &Error{n, fmt.Sprintf("unsupported header %.40q (want \"tom-bridge %d\")", s, Version)}
	}
	b := &Bundle{}
	if b.From, err = header("from"); err != nil {
		return nil, err
	}
	if strings.ContainsAny(b.From, " \t") {
		return nil, &Error{n, "plugin name contains a space"}
	}
	if b.Input, err = header("input"); err != nil {
		return nil, err
	}
	if b.Output, err = header("output"); err != nil {
		return nil, err
	}
	cur := 0
	var haveTable, haveCode bool
	for {
		s, err := next()
		if err != nil {
			return nil, err
		}
		key, val, _ := strings.Cut(s, " ")
		rk, ok := rank[key]
		if !ok {
			return nil, &Error{n, fmt.Sprintf("unknown line %.40q", s)}
		}
		if rk < cur || (rk > rType && rk == cur) {
			return nil, &Error{n, fmt.Sprintf("%q line out of order", key)}
		}
		cur = rk
		switch rk {
		case rEnd:
			if val != "" || s != "end" {
				return nil, &Error{n, "malformed end line"}
			}
			if !haveTable || !haveCode {
				return nil, &Error{n, "missing table or code line before end"}
			}
			if extra, e := readLine(br); e == nil {
				return nil, &Error{n + 1, fmt.Sprintf("data after end: %.40q", extra)}
			}
			return b, nil
		case rOption:
			o, err := parseOption(n, val)
			if err != nil {
				return nil, err
			}
			b.Options = append(b.Options, o)
		case rUsed:
			v, err := parseTerm(n, val, adt.KeyEntryFromString)
			if err != nil {
				return nil, err
			}
			b.Used = append(b.Used, v)
		case rType:
			v, err := parseTerm(n, val, adt.TomTypeFromString)
			if err != nil {
				return nil, err
			}
			b.Types = append(b.Types, v)
		case rTable:
			if b.Table, err = parseTerm(n, val, adt.TomSymbolTableFromString); err != nil {
				return nil, err
			}
			haveTable = true
		case rCode:
			if !haveTable {
				return nil, &Error{n, "code line before table line"}
			}
			if b.Code, err = parseTerm(n, val, adt.CodeFromString); err != nil {
				return nil, err
			}
			haveCode = true
		}
	}
}

func parseOption(n int, s string) (Option, error) {
	f := strings.SplitN(s, " ", 3)
	if len(f) != 3 || f[0] == "" {
		return Option{}, &Error{n, fmt.Sprintf("malformed option line %.60q", s)}
	}
	o := Option{Name: f[0], Kind: f[1], raw: f[2]}
	switch f[1] {
	case "bool":
		if f[2] != "true" && f[2] != "false" {
			return o, &Error{n, fmt.Sprintf("option %s: bad bool %.30q", f[0], f[2])}
		}
		o.Value = f[2]
	case "int":
		if _, err := strconv.ParseInt(f[2], 10, 64); err != nil {
			return o, &Error{n, fmt.Sprintf("option %s: bad int %.30q", f[0], f[2])}
		}
		o.Value = f[2]
	case "string":
		v, err := strconv.Unquote(f[2])
		if err != nil {
			return o, &Error{n, fmt.Sprintf("option %s: bad string literal: %v", f[0], err)}
		}
		o.Value = v
	default:
		return o, &Error{n, fmt.Sprintf("option %s: unknown kind %.20q", f[0], f[1])}
	}
	return o, nil
}

// Write writes b in the format Read reads; Write(Read(x)) == x byte for
// byte for every bundle written by GoBridgePlugin.
func (b *Bundle) Write(w io.Writer) error {
	if b.Table == nil || b.Code == nil {
		return errors.New("bridge: bundle without table or code")
	}
	bw := bufio.NewWriterSize(w, 1<<20)
	fmt.Fprintf(bw, "tom-bridge %d\nfrom %s\ninput %s\noutput %s\n", Version, b.From, b.Input, b.Output)
	for _, o := range b.Options {
		raw := o.raw
		if raw == "" { // an Option built by hand
			raw = o.Value
			if o.Kind == "string" {
				raw = strconv.Quote(o.Value)
			}
		}
		fmt.Fprintf(bw, "option %s %s %s\n", o.Name, o.Kind, raw)
	}
	for _, u := range b.Used {
		fmt.Fprintf(bw, "used %s\n", fmt.Sprint(u))
	}
	for _, t := range b.Types {
		fmt.Fprintf(bw, "type %s\n", fmt.Sprint(t))
	}
	fmt.Fprintf(bw, "table %s\ncode %s\nend\n", fmt.Sprint(b.Table), fmt.Sprint(b.Code))
	return bw.Flush()
}
