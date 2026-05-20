package gom

import (
	"fmt"
	"unicode"
	"unicode/utf8"
)

type tokKind int

const (
	tokEOF tokKind = iota
	tokID
	tokDot
	tokEquals
	tokAlt
	tokLParen
	tokRParen
	tokComma
	tokColon
	tokStar
)

func (k tokKind) String() string {
	switch k {
	case tokEOF:
		return "EOF"
	case tokID:
		return "ID"
	case tokDot:
		return "."
	case tokEquals:
		return "="
	case tokAlt:
		return "|"
	case tokLParen:
		return "("
	case tokRParen:
		return ")"
	case tokComma:
		return ","
	case tokColon:
		return ":"
	case tokStar:
		return "*"
	}
	return "?"
}

type token struct {
	kind  tokKind
	text  string // populated for tokID
	line  int    // 1-based
	col   int    // 1-based, of the first rune of the token
}

type lexer struct {
	src  string
	pos  int
	line int
	col  int
}

func newLexer(src string) *lexer { return &lexer{src: src, line: 1, col: 1} }

// next advances by one rune, tracking line/col.
func (l *lexer) advance(r rune, w int) {
	l.pos += w
	if r == '\n' {
		l.line++
		l.col = 1
	} else {
		l.col++
	}
}

func (l *lexer) peek() (rune, int) {
	if l.pos >= len(l.src) {
		return 0, 0
	}
	return utf8.DecodeRuneInString(l.src[l.pos:])
}

// skipWhitespaceAndComments returns when the next char is significant.
// Supports `//` line comments and `/* … */` block comments.
func (l *lexer) skipWhitespaceAndComments() error {
	for {
		r, w := l.peek()
		if w == 0 {
			return nil
		}
		switch {
		case unicode.IsSpace(r):
			l.advance(r, w)
		case r == '/' && l.pos+1 < len(l.src) && l.src[l.pos+1] == '/':
			for {
				r, w := l.peek()
				if w == 0 || r == '\n' {
					break
				}
				l.advance(r, w)
			}
		case r == '/' && l.pos+1 < len(l.src) && l.src[l.pos+1] == '*':
			startLine, startCol := l.line, l.col
			l.advance(r, w)
			r2, w2 := l.peek()
			l.advance(r2, w2) // consume '*'
			closed := false
			for {
				r, w := l.peek()
				if w == 0 {
					break
				}
				if r == '*' {
					l.advance(r, w)
					r2, w2 := l.peek()
					if r2 == '/' {
						l.advance(r2, w2)
						closed = true
						break
					}
					continue
				}
				l.advance(r, w)
			}
			if !closed {
				return fmt.Errorf("unterminated block comment starting at line %d col %d", startLine, startCol)
			}
		default:
			return nil
		}
	}
}

func isIDStart(r rune) bool { return r == '_' || unicode.IsLetter(r) }
func isIDPart(r rune) bool  { return r == '_' || unicode.IsLetter(r) || unicode.IsDigit(r) }

// readBracedBody is invoked by the parser after it has consumed the
// closing ')' of a hook's argument list. It skips whitespace and
// comments, expects the opening '{', then reads raw characters while
// tracking brace depth — string literals (`"…"`) and comments are
// honoured so that braces inside them do not throw the counter off.
//
// Returns the body without the outer braces. On success the lexer is
// positioned just after the matching '}'.
func (l *lexer) readBracedBody() (string, error) {
	if err := l.skipWhitespaceAndComments(); err != nil {
		return "", err
	}
	r, w := l.peek()
	if w == 0 || r != '{' {
		return "", fmt.Errorf("line %d col %d: expected '{' to start hook body, got %q", l.line, l.col, r)
	}
	l.advance(r, w) // consume '{'
	startPos := l.pos
	depth := 1
	for depth > 0 {
		r, w := l.peek()
		if w == 0 {
			return "", fmt.Errorf("unterminated hook body (depth %d)", depth)
		}
		switch r {
		case '{':
			depth++
			l.advance(r, w)
		case '}':
			depth--
			if depth == 0 {
				body := l.src[startPos:l.pos]
				l.advance(r, w) // consume closing '}'
				return body, nil
			}
			l.advance(r, w)
		case '"':
			l.advance(r, w)
			for {
				rr, ww := l.peek()
				if ww == 0 {
					return "", fmt.Errorf("unterminated string literal in hook body")
				}
				l.advance(rr, ww)
				if rr == '\\' {
					rr2, ww2 := l.peek()
					if ww2 == 0 {
						return "", fmt.Errorf("unterminated escape in hook body")
					}
					l.advance(rr2, ww2)
					continue
				}
				if rr == '"' {
					break
				}
			}
		case '/':
			if l.pos+1 < len(l.src) {
				nx := l.src[l.pos+1]
				if nx == '/' {
					for {
						rr, ww := l.peek()
						if ww == 0 || rr == '\n' {
							break
						}
						l.advance(rr, ww)
					}
					continue
				}
				if nx == '*' {
					l.advance(r, w)
					l.advance('*', 1)
					for {
						rr, ww := l.peek()
						if ww == 0 {
							return "", fmt.Errorf("unterminated block comment in hook body")
						}
						if rr == '*' && l.pos+1 < len(l.src) && l.src[l.pos+1] == '/' {
							l.advance(rr, ww)
							l.advance('/', 1)
							break
						}
						l.advance(rr, ww)
					}
					continue
				}
			}
			l.advance(r, w)
		default:
			l.advance(r, w)
		}
	}
	return "", fmt.Errorf("unreachable")
}

// nextToken returns the next significant token, or tokEOF at end of input.
func (l *lexer) nextToken() (token, error) {
	if err := l.skipWhitespaceAndComments(); err != nil {
		return token{}, err
	}
	r, w := l.peek()
	if w == 0 {
		return token{kind: tokEOF, line: l.line, col: l.col}, nil
	}
	startLine, startCol := l.line, l.col
	switch {
	case isIDStart(r):
		startPos := l.pos
		for {
			r, w := l.peek()
			if w == 0 || !isIDPart(r) {
				break
			}
			l.advance(r, w)
		}
		return token{kind: tokID, text: l.src[startPos:l.pos], line: startLine, col: startCol}, nil
	case r == '.':
		l.advance(r, w)
		return token{kind: tokDot, line: startLine, col: startCol}, nil
	case r == '=':
		l.advance(r, w)
		return token{kind: tokEquals, line: startLine, col: startCol}, nil
	case r == '|':
		l.advance(r, w)
		return token{kind: tokAlt, line: startLine, col: startCol}, nil
	case r == '(':
		l.advance(r, w)
		return token{kind: tokLParen, line: startLine, col: startCol}, nil
	case r == ')':
		l.advance(r, w)
		return token{kind: tokRParen, line: startLine, col: startCol}, nil
	case r == ',':
		l.advance(r, w)
		return token{kind: tokComma, line: startLine, col: startCol}, nil
	case r == ':':
		l.advance(r, w)
		return token{kind: tokColon, line: startLine, col: startCol}, nil
	case r == '*':
		l.advance(r, w)
		return token{kind: tokStar, line: startLine, col: startCol}, nil
	}
	return token{}, fmt.Errorf("line %d col %d: unexpected character %q", startLine, startCol, r)
}
