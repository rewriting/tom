package aterm

import (
	"fmt"
	"strconv"
	"strings"
)

// ParseError is returned for malformed term inputs.
type ParseError struct {
	Offset  int
	Message string
}

func (e *ParseError) Error() string {
	return fmt.Sprintf("ATerm parse error at offset %d: %s", e.Offset, e.Message)
}

// parseTerm parses a textual ATerm from src, returning the
// constructed (hash-consed) term. Panics on syntax error so the
// resulting API stays as close to Java's `factory.parse(...)` as
// possible (which throws an unchecked ParseError).
func parseTerm(f *Factory, src string) ATerm {
	p := &textParser{src: src, factory: f}
	p.skipWhite()
	t := p.parseTerm()
	p.skipWhite()
	if p.pos != len(p.src) {
		panic(&ParseError{p.pos, "trailing input: " + snippetAround(p.src, p.pos)})
	}
	return t
}

func snippetAround(src string, off int) string {
	end := off + 20
	if end > len(src) {
		end = len(src)
	}
	return strconv.Quote(src[off:end])
}

type textParser struct {
	src     string
	pos     int
	factory *Factory
}

func (p *textParser) skipWhite() {
	for p.pos < len(p.src) {
		c := p.src[p.pos]
		if c == ' ' || c == '\t' || c == '\n' || c == '\r' {
			p.pos++
			continue
		}
		break
	}
}

func (p *textParser) peek() byte {
	if p.pos >= len(p.src) {
		return 0
	}
	return p.src[p.pos]
}

func (p *textParser) consume(c byte) {
	if p.peek() != c {
		panic(&ParseError{p.pos, fmt.Sprintf("expected %q, got %q", c, p.peek())})
	}
	p.pos++
}

// parseTerm parses one term — appl / list / placeholder / number /
// quoted string — and applies any trailing `{annos}` annotation
// block (which only attaches to appls in our port; the Java
// reference accepts them on every term type but we only need appls
// for Test2 parity).
func (p *textParser) parseTerm() ATerm {
	t := p.parseTermBare()
	p.skipWhite()
	if p.peek() == '{' {
		annos := p.parseAnnoBlock()
		if appl, ok := t.(*ATermAppl); ok {
			return appl.SetAnnotations(annos)
		}
	}
	return t
}

func (p *textParser) parseTermBare() ATerm {
	p.skipWhite()
	switch c := p.peek(); {
	case c == '[':
		return p.parseList()
	case c == '<':
		return p.parsePlaceholder()
	case c == '(':
		// `(<term>)` — grouping. Java's parser actually treats `()`
		// after no function as just `()` which is parsed as an
		// empty appl with an empty AFun. We follow that.
		return p.parseAppl("")
	case c == '"':
		name := p.parseQuotedString()
		return p.parseApplAfterName(name, true)
	case c == '-' || (c >= '0' && c <= '9'):
		return p.parseNumber()
	default:
		// identifier or unquoted appl
		name := p.parseIdent()
		return p.parseApplAfterName(name, false)
	}
}

// parseAnnoBlock reads `{ term (',' term)* }` after a term, returning
// the parsed annotations as an ATermList.
func (p *textParser) parseAnnoBlock() *ATermList {
	p.consume('{')
	p.skipWhite()
	var elems []ATerm
	if p.peek() != '}' {
		for {
			elems = append(elems, p.parseTerm())
			p.skipWhite()
			if p.peek() == ',' {
				p.pos++
				p.skipWhite()
				continue
			}
			if p.peek() == '}' {
				break
			}
			panic(&ParseError{p.pos, "expected ',' or '}' in annotation block"})
		}
	}
	p.consume('}')
	out := p.factory.emptyList
	for i := len(elems) - 1; i >= 0; i-- {
		out = p.factory.MakeList(elems[i], out)
	}
	return out
}

func (p *textParser) parseList() ATerm {
	p.consume('[')
	p.skipWhite()
	if p.peek() == ']' {
		p.pos++
		return p.factory.emptyList
	}
	var elems []ATerm
	for {
		elems = append(elems, p.parseTerm())
		p.skipWhite()
		if p.peek() == ',' {
			p.pos++
			p.skipWhite()
			continue
		}
		if p.peek() == ']' {
			p.pos++
			break
		}
		panic(&ParseError{p.pos, "expected ',' or ']' in list"})
	}
	result := p.factory.emptyList
	for i := len(elems) - 1; i >= 0; i-- {
		result = p.factory.MakeList(elems[i], result)
	}
	return result
}

func (p *textParser) parsePlaceholder() ATerm {
	p.consume('<')
	inner := p.parseTerm()
	p.skipWhite()
	p.consume('>')
	return p.factory.MakePlaceholder(inner)
}

func (p *textParser) parseApplAfterName(name string, quoted bool) ATerm {
	p.skipWhite()
	if p.peek() != '(' {
		// nullary
		fun := p.factory.MakeAFun(name, 0, quoted)
		return p.factory.MakeAppl(fun)
	}
	return p.parseAppl(name)
}

// parseAppl handles the open-paren / args / close-paren shape.
// `name` is the head identifier (may be empty for `()` style).
func (p *textParser) parseAppl(name string) ATerm {
	p.consume('(')
	p.skipWhite()
	var args []ATerm
	if p.peek() != ')' {
		for {
			args = append(args, p.parseTerm())
			p.skipWhite()
			if p.peek() == ',' {
				p.pos++
				p.skipWhite()
				continue
			}
			if p.peek() == ')' {
				break
			}
			panic(&ParseError{p.pos, "expected ',' or ')' in application"})
		}
	}
	p.consume(')')
	quoted := false
	if name == "" {
		// `()` form
		quoted = false
	}
	fun := p.factory.MakeAFun(name, len(args), quoted)
	return p.factory.MakeAppl(fun, args...)
}

func (p *textParser) parseIdent() string {
	start := p.pos
	for p.pos < len(p.src) {
		c := p.src[p.pos]
		if (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '-' || c == '+' || c == '*' || c == '$' {
			p.pos++
			continue
		}
		break
	}
	return p.src[start:p.pos]
}

func (p *textParser) parseQuotedString() string {
	if p.peek() != '"' {
		panic(&ParseError{p.pos, "expected '\"'"})
	}
	p.pos++ // opening
	var b strings.Builder
	for p.pos < len(p.src) {
		c := p.src[p.pos]
		if c == '"' {
			p.pos++
			return b.String()
		}
		if c == '\\' {
			if p.pos+1 >= len(p.src) {
				panic(&ParseError{p.pos, "Unterminated quoted function symbol"})
			}
			nxt := p.src[p.pos+1]
			switch {
			case nxt == 'n':
				b.WriteByte('\n')
				p.pos += 2
			case nxt == 't':
				b.WriteByte('\t')
				p.pos += 2
			case nxt == 'r':
				b.WriteByte('\r')
				p.pos += 2
			case nxt == '\\' || nxt == '"' || nxt == '\'':
				b.WriteByte(nxt)
				p.pos += 2
			case nxt >= '0' && nxt <= '7':
				end := p.pos + 1
				for end < len(p.src) && end < p.pos+4 && p.src[end] >= '0' && p.src[end] <= '7' {
					end++
				}
				n, _ := strconv.ParseInt(p.src[p.pos+1:end], 8, 32)
				b.WriteByte(byte(n))
				p.pos = end
			default:
				b.WriteByte('\\')
				b.WriteByte(nxt)
				p.pos += 2
			}
			continue
		}
		b.WriteByte(c)
		p.pos++
	}
	panic(&ParseError{p.pos, "Unterminated quoted function symbol"})
}

// parseNumber handles ints, longs (suffix L), and reals (with decimal
// point or exponent).
func (p *textParser) parseNumber() ATerm {
	start := p.pos
	if p.peek() == '-' || p.peek() == '+' {
		p.pos++
	}
	for p.pos < len(p.src) && p.src[p.pos] >= '0' && p.src[p.pos] <= '9' {
		p.pos++
	}
	isReal := false
	if p.peek() == '.' {
		isReal = true
		p.pos++
		for p.pos < len(p.src) && p.src[p.pos] >= '0' && p.src[p.pos] <= '9' {
			p.pos++
		}
	}
	if p.peek() == 'e' || p.peek() == 'E' {
		isReal = true
		p.pos++
		if p.peek() == '+' || p.peek() == '-' {
			p.pos++
		}
		for p.pos < len(p.src) && p.src[p.pos] >= '0' && p.src[p.pos] <= '9' {
			p.pos++
		}
	}
	if isReal {
		v, err := strconv.ParseFloat(p.src[start:p.pos], 64)
		if err != nil {
			panic(&ParseError{start, "bad real: " + err.Error()})
		}
		return p.factory.MakeReal(v)
	}
	if p.peek() == 'L' || p.peek() == 'l' {
		p.pos++
		v, _ := strconv.ParseInt(p.src[start:p.pos-1], 10, 64)
		return p.factory.MakeLong(v)
	}
	v, _ := strconv.ParseInt(p.src[start:p.pos], 10, 64)
	return p.factory.MakeInt(int(v))
}
