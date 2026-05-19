package gom

import (
	"fmt"
	"io"
	"os"
)

// Parse reads a Gom module source from r and returns its AST.
//
// The parser supports the subset of Gom needed for hook-free signature
// files (Phase 2 scope): a module declaration, optional imports, the
// `abstract syntax` keyword, and one or more sort declarations with
// named-slot or variadic alternatives.
//
// Encountering a hook construct or any other unsupported feature is an
// error — callers should pre-filter `.gom` files with `HasHookContent`
// when they want to skip hooked sources.
func Parse(r io.Reader) (*Module, error) {
	src, err := io.ReadAll(r)
	if err != nil {
		return nil, err
	}
	return ParseBytes(src)
}

// ParseFile is a convenience wrapper around Parse for a file path.
func ParseFile(path string) (*Module, error) {
	f, err := os.Open(path)
	if err != nil {
		return nil, err
	}
	defer f.Close()
	mod, err := Parse(f)
	if err != nil {
		return nil, fmt.Errorf("%s: %w", path, err)
	}
	return mod, nil
}

// ParseBytes parses Gom source from an in-memory byte slice.
func ParseBytes(src []byte) (*Module, error) {
	p := &parser{lex: newLexer(string(src))}
	if err := p.advance(); err != nil {
		return nil, err
	}
	return p.parseModule()
}

type parser struct {
	lex *lexer
	tok token
}

func (p *parser) advance() error {
	t, err := p.lex.nextToken()
	if err != nil {
		return err
	}
	p.tok = t
	return nil
}

func (p *parser) errf(format string, args ...any) error {
	return fmt.Errorf("line %d col %d: %s", p.tok.line, p.tok.col, fmt.Sprintf(format, args...))
}

// expectID consumes the current token if it is an ID with the given text
// (a contextual keyword). Returns the line where it appeared.
func (p *parser) expectKeyword(kw string) (int, error) {
	if p.tok.kind != tokID || p.tok.text != kw {
		return 0, p.errf("expected %q, got %s %q", kw, p.tok.kind, p.tok.text)
	}
	line := p.tok.line
	if err := p.advance(); err != nil {
		return 0, err
	}
	return line, nil
}

// consumeID returns the current ID's text and advances. Errors if the
// current token is not an ID.
func (p *parser) consumeID() (string, error) {
	if p.tok.kind != tokID {
		return "", p.errf("expected identifier, got %s", p.tok.kind)
	}
	text := p.tok.text
	if err := p.advance(); err != nil {
		return "", err
	}
	return text, nil
}

func (p *parser) expect(k tokKind) error {
	if p.tok.kind != k {
		return p.errf("expected %s, got %s %q", k, p.tok.kind, p.tok.text)
	}
	return p.advance()
}

// parseModule parses a full module:
//
//	"module" qualifiedName
//	("imports" ID+)?
//	"abstract" "syntax"
//	sortDecl+
func (p *parser) parseModule() (*Module, error) {
	if _, err := p.expectKeyword("module"); err != nil {
		return nil, err
	}
	name, err := p.parseQualifiedName()
	if err != nil {
		return nil, err
	}
	mod := &Module{Name: name}

	// Optional "imports" list.
	if p.tok.kind == tokID && p.tok.text == "imports" {
		if err := p.advance(); err != nil {
			return nil, err
		}
		for p.tok.kind == tokID && p.tok.text != "abstract" {
			mod.Imports = append(mod.Imports, p.tok.text)
			if err := p.advance(); err != nil {
				return nil, err
			}
		}
	}

	if _, err := p.expectKeyword("abstract"); err != nil {
		return nil, err
	}
	if _, err := p.expectKeyword("syntax"); err != nil {
		return nil, err
	}

	// Loop on top-level productions: sort declarations and hooks.
	for p.tok.kind != tokEOF {
		var scope string
		if p.tok.kind == tokID && isScopeKeyword(p.tok.text) {
			scope = p.tok.text
			if err := p.advance(); err != nil {
				return nil, err
			}
		}
		line := p.tok.line
		first, err := p.consumeID()
		if err != nil {
			return nil, err
		}
		switch p.tok.kind {
		case tokColon:
			h, err := p.parseHookAfterPointCut(scope, first, line)
			if err != nil {
				return nil, err
			}
			mod.Hooks = append(mod.Hooks, *h)
		case tokEquals:
			if scope != "" {
				return nil, p.errf("scope keyword %q cannot precede a sort declaration", scope)
			}
			s, err := p.parseSortDeclBody(first, line)
			if err != nil {
				return nil, err
			}
			mod.Sorts = append(mod.Sorts, *s)
		default:
			return nil, p.errf("expected '=' (sort decl) or ':' (hook) after %q, got %s", first, p.tok.kind)
		}
	}
	if len(mod.Sorts) == 0 {
		return nil, fmt.Errorf("module %s has no sort declaration", mod.QualifiedName())
	}
	return mod, nil
}

func isScopeKeyword(s string) bool {
	return s == "sort" || s == "module" || s == "operator"
}

// parseQualifiedName reads ID ('.' ID)*.
func (p *parser) parseQualifiedName() ([]string, error) {
	first, err := p.consumeID()
	if err != nil {
		return nil, err
	}
	parts := []string{first}
	for p.tok.kind == tokDot {
		if err := p.advance(); err != nil {
			return nil, err
		}
		next, err := p.consumeID()
		if err != nil {
			return nil, err
		}
		parts = append(parts, next)
	}
	return parts, nil
}

// parseSortDeclBody parses the body of a sort declaration, starting
// from "=":
//
//	"=" ("|"? alt) ("|" alt)*
//
// The optional leading "|" lets sources be aligned vertically:
//
//	Wrapper = | Int(i:int)
//	          | IntBis(i:int)
//
// The caller already consumed the leading SortName identifier and
// passes it in as `name`.
func (p *parser) parseSortDeclBody(name string, line int) (*SortDecl, error) {
	if err := p.expect(tokEquals); err != nil {
		return nil, err
	}
	// Optional leading "|".
	if p.tok.kind == tokAlt {
		if err := p.advance(); err != nil {
			return nil, err
		}
	}
	sort := &SortDecl{Name: name, Line: line}
	for {
		alt, err := p.parseAlternative()
		if err != nil {
			return nil, err
		}
		sort.Alternatives = append(sort.Alternatives, *alt)
		if p.tok.kind != tokAlt {
			break
		}
		if err := p.advance(); err != nil {
			return nil, err
		}
	}
	return sort, nil
}

// parseHookAfterPointCut consumes a hook construct, starting from the
// `:` token (the caller already consumed an optional scope keyword and
// the pointCut identifier). On success the next significant token
// (next sort/hook or EOF) is in p.tok.
func (p *parser) parseHookAfterPointCut(scope, pointCut string, line int) (*GomHook, error) {
	if p.tok.kind != tokColon {
		return nil, p.errf("expected ':' after hook point-cut %q", pointCut)
	}
	if err := p.advance(); err != nil {
		return nil, err
	}
	kind, err := p.consumeID()
	if err != nil {
		return nil, err
	}
	if err := p.expect(tokLParen); err != nil {
		return nil, err
	}
	var args []string
	if p.tok.kind != tokRParen {
		for {
			a, err := p.consumeID()
			if err != nil {
				return nil, err
			}
			args = append(args, a)
			if p.tok.kind != tokComma {
				break
			}
			if err := p.advance(); err != nil {
				return nil, err
			}
		}
	}
	// We're sitting on ')'. Don't advance yet — the lexer must read
	// the brace body raw, not as tokens.
	if p.tok.kind != tokRParen {
		return nil, p.errf("expected ')' in hook arg list")
	}
	body, err := p.lex.readBracedBody()
	if err != nil {
		return nil, err
	}
	// Refresh the lookahead now that the body has been consumed.
	if err := p.advance(); err != nil {
		return nil, err
	}
	return &GomHook{
		Scope:    scope,
		PointCut: pointCut,
		Kind:     kind,
		Args:     args,
		Body:     body,
		Line:     line,
	}, nil
}

// parseAlternative parses:
//
//	OpName "(" (arg ("," arg)*)? ")"
//
// where arg is either `slot:Type` (named slot) or `Type*` (variadic).
// An alternative containing a variadic arg must contain ONLY that arg.
func (p *parser) parseAlternative() (*Alternative, error) {
	line := p.tok.line
	op, err := p.consumeID()
	if err != nil {
		return nil, err
	}
	if err := p.expect(tokLParen); err != nil {
		return nil, err
	}
	alt := &Alternative{Op: op, Line: line}
	if p.tok.kind == tokRParen {
		if err := p.advance(); err != nil {
			return nil, err
		}
		return alt, nil
	}
	for {
		arg, err := p.parseArg()
		if err != nil {
			return nil, err
		}
		alt.Args = append(alt.Args, arg)
		if arg.Variadic {
			alt.Variadic = true
		}
		if p.tok.kind != tokComma {
			break
		}
		if err := p.advance(); err != nil {
			return nil, err
		}
	}
	if alt.Variadic && len(alt.Args) != 1 {
		return nil, fmt.Errorf("line %d: alternative %s has a variadic arg mixed with other args", line, op)
	}
	if err := p.expect(tokRParen); err != nil {
		return nil, err
	}
	return alt, nil
}

// parseArg parses one of:
//
//	ID ":" ID         (named slot)
//	ID "*"            (anonymous variadic type)
func (p *parser) parseArg() (Arg, error) {
	first, err := p.consumeID()
	if err != nil {
		return Arg{}, err
	}
	switch p.tok.kind {
	case tokColon:
		if err := p.advance(); err != nil {
			return Arg{}, err
		}
		typ, err := p.consumeID()
		if err != nil {
			return Arg{}, err
		}
		return Arg{Name: first, Type: typ}, nil
	case tokStar:
		if err := p.advance(); err != nil {
			return Arg{}, err
		}
		return Arg{Type: first, Variadic: true}, nil
	}
	return Arg{}, p.errf("expected ':' or '*' after argument identifier %q, got %s", first, p.tok.kind)
}
