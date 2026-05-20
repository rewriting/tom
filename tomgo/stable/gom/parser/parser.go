package gom

import (
	"fmt"
	"io"
	"os"
	"strings"

	"tom/tomgo/stable/library/gomast"
)

// Parse reads a Gom module source from r and returns its AST as a
// gomast.GomModule — the canonical V2 AST produced by tomgo itself
// when applied to src/tom/gom/adt/Gom.gom. The parser builds the
// gomast terms directly through their `Make*` constructors so that
// every sub-term is hash-consed inside the gomast factory.
//
// Hook constructs are recognised — their kind/point-cut/arglist and a
// raw (brace-balanced) body are stored in a `Hook` production. The
// body itself is NOT interpreted at parse time; lowering it to Go is
// the backend's job (see knownHookTable in internal/backend).
func Parse(r io.Reader) (gomast.GomModule, error) {
	src, err := io.ReadAll(r)
	if err != nil {
		return nil, err
	}
	return ParseBytes(src)
}

// ParseFile is a convenience wrapper around Parse for a file path.
func ParseFile(path string) (gomast.GomModule, error) {
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
func ParseBytes(src []byte) (gomast.GomModule, error) {
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

// expectKeyword consumes the current token if it is an ID with the
// given text (a contextual keyword). Returns the line where it appeared.
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
//	(sortDecl | hookConstruct)+
//
// The result is a gomast.GomModule wrapping an optional Imports
// section and a Public section that aggregates all SortType and Hook
// productions.
func (p *parser) parseModule() (gomast.GomModule, error) {
	if _, err := p.expectKeyword("module"); err != nil {
		return nil, err
	}
	nameParts, err := p.parseQualifiedName()
	if err != nil {
		return nil, err
	}
	moduleName := gomast.MakeGomModuleName(strings.Join(nameParts, "."))

	var sections []gomast.Section

	// Optional `imports` list.
	if p.tok.kind == tokID && p.tok.text == "imports" {
		if err := p.advance(); err != nil {
			return nil, err
		}
		var imports []gomast.GomModuleName
		for p.tok.kind == tokID && p.tok.text != "abstract" {
			imports = append(imports, gomast.MakeGomModuleName(p.tok.text))
			if err := p.advance(); err != nil {
				return nil, err
			}
		}
		if len(imports) > 0 {
			sections = append(sections, gomast.MakeImports(gomast.MakeConcImportedModule(imports...)))
		}
	}

	if _, err := p.expectKeyword("abstract"); err != nil {
		return nil, err
	}
	if _, err := p.expectKeyword("syntax"); err != nil {
		return nil, err
	}

	var prods []gomast.Production
	hasSort := false
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
			prods = append(prods, h)
		case tokEquals:
			if scope != "" {
				return nil, p.errf("scope keyword %q cannot precede a sort declaration", scope)
			}
			s, err := p.parseSortTypeProductionBody(first, line)
			if err != nil {
				return nil, err
			}
			prods = append(prods, s)
			hasSort = true
		default:
			return nil, p.errf("expected '=' (sort decl) or ':' (hook) after %q, got %s", first, p.tok.kind)
		}
	}
	if !hasSort {
		return nil, fmt.Errorf("module %s has no sort declaration", strings.Join(nameParts, "."))
	}
	sections = append(sections, gomast.MakePublic(gomast.MakeConcProduction(prods...)))
	return gomast.MakeGomModule(moduleName, gomast.MakeConcSection(sections...)), nil
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

// parseSortTypeProductionBody parses the tail of a sort declaration,
// starting from "=":
//
//	"=" ("|"? alt) ("|" alt)*
//
// The optional leading "|" lets sources be aligned vertically. The
// returned production is `SortType(Type, ConcAtom(), AlternativeList)`.
func (p *parser) parseSortTypeProductionBody(name string, line int) (gomast.Production, error) {
	if err := p.expect(tokEquals); err != nil {
		return nil, err
	}
	if p.tok.kind == tokAlt {
		if err := p.advance(); err != nil {
			return nil, err
		}
	}
	sortType := gomast.MakeGomType(gomast.MakeExpressionType(), name)
	var alts []gomast.Alternative
	for {
		alt, err := p.parseAlternative(sortType)
		if err != nil {
			return nil, err
		}
		alts = append(alts, alt)
		if p.tok.kind != tokAlt {
			break
		}
		if err := p.advance(); err != nil {
			return nil, err
		}
	}
	_ = line // Future: encode line as Option on the SortType when the
	// gomast layer grows an Option slot for it. Today it has none.
	return gomast.MakeSortType(sortType, gomast.MakeConcAtom(), gomast.MakeConcAlternative(alts...)), nil
}

// parseHookAfterPointCut consumes a hook construct, starting from the
// `:` token (the caller already consumed an optional scope keyword and
// the pointCut identifier). On success the next significant token
// (next sort/hook or EOF) is in p.tok.
func (p *parser) parseHookAfterPointCut(scope, pointCut string, line int) (gomast.Production, error) {
	if p.tok.kind != tokColon {
		return nil, p.errf("expected ':' after hook point-cut %q", pointCut)
	}
	if err := p.advance(); err != nil {
		return nil, err
	}
	kindStr, err := p.consumeID()
	if err != nil {
		return nil, err
	}
	if err := p.expect(tokLParen); err != nil {
		return nil, err
	}
	var args []gomast.Arg
	if p.tok.kind != tokRParen {
		for {
			a, err := p.consumeID()
			if err != nil {
				return nil, err
			}
			args = append(args, gomast.MakeArg(a))
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
	return gomast.MakeHook(
		scopeToIdKind(scope),
		pointCut,
		gomast.MakeHookKind(kindStr),
		gomast.MakeConcArg(args...),
		gomast.MakeHookCode(body),
		gomast.MakeOptionList(gomast.MakeOrigin(int64(line))),
	), nil
}

// parseAlternative parses:
//
//	OpName "(" (arg ("," arg)*)? ")"
//
// where arg is either `slot:Type` (named slot) or `Type*` (variadic).
// An alternative containing a variadic arg must contain ONLY that arg.
// The codomain of the produced Alternative is `sortType`, the GomType
// of the enclosing sort.
func (p *parser) parseAlternative(sortType gomast.GomType) (gomast.Alternative, error) {
	line := p.tok.line
	op, err := p.consumeID()
	if err != nil {
		return nil, err
	}
	if err := p.expect(tokLParen); err != nil {
		return nil, err
	}
	var fields []gomast.Field
	variadic := false
	if p.tok.kind != tokRParen {
		for {
			fld, isVariadic, err := p.parseArg()
			if err != nil {
				return nil, err
			}
			fields = append(fields, fld)
			if isVariadic {
				variadic = true
			}
			if p.tok.kind != tokComma {
				break
			}
			if err := p.advance(); err != nil {
				return nil, err
			}
		}
	}
	if variadic && len(fields) != 1 {
		return nil, fmt.Errorf("line %d: alternative %s has a variadic arg mixed with other args", line, op)
	}
	if err := p.expect(tokRParen); err != nil {
		return nil, err
	}
	return gomast.MakeAlternative(
		op,
		gomast.MakeConcField(fields...),
		sortType,
		gomast.MakeOptionList(gomast.MakeOrigin(int64(line))),
	), nil
}

// parseArg parses one of:
//
//	ID ":" ID         (named slot)
//	ID "*"            (anonymous variadic type)
//
// Returns the produced Field plus a boolean flagging the variadic
// case so parseAlternative can enforce the "alone in its arg list"
// invariant before building the gomast term.
func (p *parser) parseArg() (gomast.Field, bool, error) {
	first, err := p.consumeID()
	if err != nil {
		return nil, false, err
	}
	switch p.tok.kind {
	case tokColon:
		if err := p.advance(); err != nil {
			return nil, false, err
		}
		typ, err := p.consumeID()
		if err != nil {
			return nil, false, err
		}
		fld := gomast.MakeNamedField(first,
			gomast.MakeGomType(gomast.MakeExpressionType(), typ),
			gomast.MakeNone())
		return fld, false, nil
	case tokStar:
		if err := p.advance(); err != nil {
			return nil, false, err
		}
		fld := gomast.MakeStarredField(
			gomast.MakeGomType(gomast.MakeExpressionType(), first),
			gomast.MakeNone())
		return fld, true, nil
	}
	return nil, false, p.errf("expected ':' or '*' after argument identifier %q, got %s", first, p.tok.kind)
}

// scopeToIdKind maps the source-level scope keyword to the V2 IdKind
// term. The empty (unscoped) hook defaults to KindOperator, matching
// the ANTLR rule
//
//	hookConstruct : (hookScope)? pointCut=ID … -> ^( Hook ^( KindOperator ) … )
func scopeToIdKind(scope string) gomast.IdKind {
	switch scope {
	case "sort":
		return gomast.MakeKindSort()
	case "module":
		return gomast.MakeKindModule()
	default:
		return gomast.MakeKindOperator()
	}
}
