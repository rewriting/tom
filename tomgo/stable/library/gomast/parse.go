// Term-string deserialiser: the inverse of String().
//
// Every TomAST term renders to a textual form `Op(arg1, arg2, …)` (or a
// bare string/number literal for primitive payloads — TL("…"),
// TextPosition(line, col), …). [FromString] tokenises that form and
// rebuilds the typed term by dispatching through [makeRegistry], the
// generated `Op → MakeOp` table. Round-trips with byte equality on
// hash-cons-stable terms.
//
// Primary use case: AST comparison. Given two strings produced by
// `term.String()` (one from the Go pipeline, one from Java's
// `tom --intermediate`), parse both and compare the resulting Term
// pointers — same shape → same hash-consed instance → ==.
package gomast

import (
	"fmt"
	"reflect"
	"strconv"
	"strings"
)

// FromString deserialises a Tom term in textual `Op(arg1, …)` form,
// returning the typed Term (the caller asserts to the desired
// interface, e.g. `Code`, `TomTerm`). String / number literals at
// the top level are returned as their Go primitive (string / int64).
func FromString(s string) (any, error) {
	p := &termParser{src: s}
	p.skipBlanks()
	v, err := p.parse()
	if err != nil {
		return nil, err
	}
	p.skipBlanks()
	if p.pos != len(p.src) {
		return nil, fmt.Errorf("trailing input at offset %d: %q", p.pos, snippet(p.src, p.pos))
	}
	return v, nil
}

type termParser struct {
	src string
	pos int
}

// parse reads one term: either a primitive literal or `Op(args)`.
func (p *termParser) parse() (any, error) {
	if p.pos >= len(p.src) {
		return nil, fmt.Errorf("unexpected end of input")
	}
	c := p.src[p.pos]
	// String literal.
	if c == '"' {
		return p.parseString()
	}
	// Numeric literal (optional leading -).
	if c == '-' || (c >= '0' && c <= '9') {
		return p.parseNumber()
	}
	// Identifier → op application.
	return p.parseApplication()
}

func (p *termParser) parseString() (string, error) {
	if p.src[p.pos] != '"' {
		return "", fmt.Errorf("expected '\"' at %d", p.pos)
	}
	p.pos++ // opening quote
	var sb strings.Builder
	for p.pos < len(p.src) {
		c := p.src[p.pos]
		if c == '"' {
			p.pos++
			return sb.String(), nil
		}
		if c == '\\' {
			// Decode the Java-style escapes Tom emits: \n, \t, \\,
			// \", \140 (octal — \140 = backtick), etc. We preserve
			// other escapes verbatim so we don't lose data.
			if p.pos+1 >= len(p.src) {
				return "", fmt.Errorf("dangling backslash at %d", p.pos)
			}
			nxt := p.src[p.pos+1]
			switch {
			case nxt == 'n':
				sb.WriteByte('\n')
				p.pos += 2
			case nxt == 't':
				sb.WriteByte('\t')
				p.pos += 2
			case nxt == 'r':
				sb.WriteByte('\r')
				p.pos += 2
			case nxt == '\\':
				sb.WriteByte('\\')
				p.pos += 2
			case nxt == '"':
				sb.WriteByte('"')
				p.pos += 2
			case nxt >= '0' && nxt <= '7':
				// Octal escape — Tom uses \140 etc.
				end := p.pos + 1
				for end < len(p.src) && end < p.pos+4 && p.src[end] >= '0' && p.src[end] <= '7' {
					end++
				}
				n, err := strconv.ParseInt(p.src[p.pos+1:end], 8, 32)
				if err != nil {
					return "", fmt.Errorf("bad octal escape at %d: %w", p.pos, err)
				}
				sb.WriteByte(byte(n))
				p.pos = end
			default:
				// Unknown escape — keep both bytes verbatim. Matches
				// String()'s "preserve what we don't recognise"
				// stance.
				sb.WriteByte('\\')
				sb.WriteByte(nxt)
				p.pos += 2
			}
			continue
		}
		sb.WriteByte(c)
		p.pos++
	}
	return "", fmt.Errorf("unterminated string at %d", p.pos)
}

func (p *termParser) parseNumber() (int64, error) {
	start := p.pos
	if p.src[p.pos] == '-' {
		p.pos++
	}
	for p.pos < len(p.src) && p.src[p.pos] >= '0' && p.src[p.pos] <= '9' {
		p.pos++
	}
	n, err := strconv.ParseInt(p.src[start:p.pos], 10, 64)
	if err != nil {
		return 0, fmt.Errorf("bad number at %d: %w", start, err)
	}
	return n, nil
}

// parseApplication reads `IDENT (LPAREN arg (COMMA arg)* RPAREN)?`.
// When there's no LPAREN, treats the identifier as a nullary op.
func (p *termParser) parseApplication() (any, error) {
	start := p.pos
	for p.pos < len(p.src) {
		c := p.src[p.pos]
		if (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' {
			p.pos++
			continue
		}
		break
	}
	if p.pos == start {
		return nil, fmt.Errorf("expected identifier at %d: %q", p.pos, snippet(p.src, p.pos))
	}
	op := p.src[start:p.pos]
	var args []any
	p.skipBlanks()
	if p.pos < len(p.src) && p.src[p.pos] == '(' {
		p.pos++ // '('
		p.skipBlanks()
		if p.pos < len(p.src) && p.src[p.pos] == ')' {
			p.pos++
		} else {
			for {
				a, err := p.parse()
				if err != nil {
					return nil, err
				}
				args = append(args, a)
				p.skipBlanks()
				if p.pos >= len(p.src) {
					return nil, fmt.Errorf("unterminated argument list for %s", op)
				}
				if p.src[p.pos] == ',' {
					p.pos++
					p.skipBlanks()
					continue
				}
				if p.src[p.pos] == ')' {
					p.pos++
					break
				}
				return nil, fmt.Errorf("expected ',' or ')' after argument for %s at offset %d: %q",
					op, p.pos, snippet(p.src, p.pos))
			}
		}
	}
	return dispatchMake(op, args)
}

func (p *termParser) skipBlanks() {
	for p.pos < len(p.src) {
		c := p.src[p.pos]
		if c == ' ' || c == '\t' || c == '\n' || c == '\r' {
			p.pos++
			continue
		}
		break
	}
}

// dispatchMake looks up the named operator in makeRegistry and
// invokes its `Make<Op>` constructor with the parsed args coerced
// to the expected parameter types. Returns the constructed term.
func dispatchMake(op string, args []any) (any, error) {
	maker, ok := makeRegistry[op]
	if !ok {
		return nil, fmt.Errorf("unknown TomAST operator %q (not in makeRegistry)", op)
	}
	fn := reflect.ValueOf(maker)
	ft := fn.Type()
	var callArgs []reflect.Value
	if ft.IsVariadic() {
		// Variadic Make<Op>(args ...X) — pack everything into one
		// reflected slice.
		eltType := ft.In(0).Elem()
		slice := reflect.MakeSlice(reflect.SliceOf(eltType), len(args), len(args))
		for i, a := range args {
			v, err := coerce(a, eltType)
			if err != nil {
				return nil, fmt.Errorf("%s arg %d: %w", op, i, err)
			}
			slice.Index(i).Set(v)
		}
		callArgs = []reflect.Value{slice}
		return fn.CallSlice(callArgs)[0].Interface(), nil
	}
	if ft.NumIn() != len(args) {
		return nil, fmt.Errorf("%s: expected %d args, got %d", op, ft.NumIn(), len(args))
	}
	for i, a := range args {
		v, err := coerce(a, ft.In(i))
		if err != nil {
			return nil, fmt.Errorf("%s arg %d: %w", op, i, err)
		}
		callArgs = append(callArgs, v)
	}
	return fn.Call(callArgs)[0].Interface(), nil
}

// coerce converts the parsed value `a` (a Go primitive or a Term
// interface) into a reflect.Value matching the target type `t`.
// Interface targets get the value directly (via Convert); primitive
// targets unbox to string/int64. Returns an error if `a` doesn't fit.
func coerce(a any, t reflect.Type) (reflect.Value, error) {
	av := reflect.ValueOf(a)
	switch t.Kind() {
	case reflect.String:
		if s, ok := a.(string); ok {
			return reflect.ValueOf(s), nil
		}
	case reflect.Int64:
		if n, ok := a.(int64); ok {
			return reflect.ValueOf(n), nil
		}
	case reflect.Int, reflect.Int32, reflect.Int16, reflect.Int8:
		if _, ok := a.(int64); ok {
			return av.Convert(t), nil
		}
	}
	if av.IsValid() && av.Type().AssignableTo(t) {
		return av, nil
	}
	if av.IsValid() && av.Type().ConvertibleTo(t) {
		return av.Convert(t), nil
	}
	return reflect.Value{}, fmt.Errorf("cannot coerce %T → %v", a, t)
}

// snippet returns up to 30 bytes of src starting at off, useful for
// error messages.
func snippet(src string, off int) string {
	end := off + 30
	if end > len(src) {
		end = len(src)
	}
	return src[off:end]
}
