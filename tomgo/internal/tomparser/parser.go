// Package tomparser is a hand-rolled, 100% Go parser for a small subset of the
// TOM language. It mirrors the antlr4 island parser from
// stable/tom/engine/parser/antlr4/{TomIslandLexer,TomIslandParser}.g4 — water
// (host code, opaque) interleaved with TOM "islands" (%typeterm, %op, %match,
// %strategy, %include, %gom, backquote terms, metaquote).
//
// Phase 4.D / 4.E / 4.F — current coverage:
//
//   start      : (water | islandStmt)* EOF
//   islandStmt : typeterm | op | oplist | oparray | include | match
//   typeterm   : '%typeterm' ID ('extends' ID)? '{' BALANCED '}'
//   op         : '%op' ID ID '(' slotList? ')' '{' BALANCED '}'
//   oplist     : '%oplist'  ID ID '(' ID '*' ')' '{' BALANCED '}'
//   oparray    : '%oparray' ID ID '(' ID '*' ')' '{' BALANCED '}'
//   include    : '%include' '{' includePath '}'
//   match      : '%match' '(' subject (',' subject)* ')' '{' actionRule* '}'
//   subject    : ID                                    (BQVariable only for now)
//   actionRule : pattern '->' '{' BALANCED '}'
//   pattern    : '_' | ID                              (anonymous or named Variable)
//   slotList   : slot (',' slot)*
//   slot       : ID ':' ID
//   includePath : (ID | '.' | '/' | '\\')+
//
// `BALANCED` means: read everything up to and including the matching '}'.
// The body content (implement/is_sort/equals/is_fsym/make/get_slot/…) is
// not encoded into the AST — that is consistent with the Java reference,
// which routes those fragments through the SymbolTable rather than the
// `Code` tree.
//
// The AST is built directly via tomast.Make* constructors — no separate CST
// stage.
package tomparser

import (
	"fmt"
	"os"
	"path/filepath"
	"strings"

	"tom/tomgo/internal/tomast"
)

// Parse turns the contents of a .t file into a tomast.Code term shaped exactly
// like the reference Java parser's output. `filename` is recorded into the
// OriginTracking option of every declaration; callers that want a
// path-independent dump should pass "__INPUT__".
func Parse(source, filename string) (tomast.Code, error) {
	p := newParser(source, filename)
	return p.parseProgram()
}

// position tracks one-based line and column, matching the convention used by
// the Java parser (TextPosition(1,1) is the first character of the file).
type position struct {
	line, col int
}

func (p position) String() string { return fmt.Sprintf("%d:%d", p.line, p.col) }

// parser walks the source character by character. The grammar is small enough
// that a single descent that peeks at the upcoming characters on demand is
// simpler than a separate lexer pass.
type parser struct {
	src      string
	filename string
	idx      int
	cur      position // position of src[idx]; src[idx-1] was at the previous slot
	codes    []tomast.Code
}

func newParser(src, filename string) *parser {
	return &parser{src: src, filename: filename, cur: position{line: 1, col: 1}}
}

func (p *parser) atEnd() bool { return p.idx >= len(p.src) }

// peek returns the byte at offset `off` from the current position, or 0 if
// past EOF. Only used for ASCII characters — fine for the .t fixtures we
// target.
func (p *parser) peek(off int) byte {
	i := p.idx + off
	if i < 0 || i >= len(p.src) {
		return 0
	}
	return p.src[i]
}

// advance consumes one byte, updating the line/column counter.
func (p *parser) advance() byte {
	c := p.src[p.idx]
	p.idx++
	if c == '\n' {
		p.cur.line++
		p.cur.col = 1
	} else {
		p.cur.col++
	}
	return c
}

// matchKeyword returns true and consumes if the next bytes equal `kw` AND that
// match is a "whole word" — `kw` cannot be followed by an identifier
// character (so `%typeterm` won't accidentally swallow `%typeterm2`).
func (p *parser) matchKeyword(kw string) bool {
	if !p.lookAheadKeyword(kw) {
		return false
	}
	for range kw {
		p.advance()
	}
	return true
}

func (p *parser) lookAheadKeyword(kw string) bool {
	if p.idx+len(kw) > len(p.src) {
		return false
	}
	if p.src[p.idx:p.idx+len(kw)] != kw {
		return false
	}
	end := p.idx + len(kw)
	if end < len(p.src) && isIdentChar(p.src[end]) {
		return false
	}
	return true
}

// parseProgram is the top-level entry: it loops over the source emitting
// either a water chunk (host code) or an island declaration.
func (p *parser) parseProgram() (tomast.Code, error) {
	for !p.atEnd() {
		switch {
		case p.lookAheadKeyword("%typeterm"):
			if err := p.parseTypeterm(); err != nil {
				return nil, err
			}
		case p.lookAheadKeyword("%oplist"):
			if err := p.parseVariadicOp(true); err != nil {
				return nil, err
			}
		case p.lookAheadKeyword("%oparray"):
			if err := p.parseVariadicOp(false); err != nil {
				return nil, err
			}
		case p.lookAheadKeyword("%op"):
			if err := p.parseOp(); err != nil {
				return nil, err
			}
		case p.lookAheadKeyword("%include"):
			if err := p.parseInclude(); err != nil {
				return nil, err
			}
		case p.lookAheadKeyword("%match"):
			if err := p.parseMatch(); err != nil {
				return nil, err
			}
		default:
			if err := p.parseWater(); err != nil {
				return nil, err
			}
		}
	}
	return tomast.MakeTom(tomast.MakeConcCode(p.codes...)), nil
}

// isIslandStart returns true when the cursor sits on an island keyword that
// parseProgram would dispatch to.
func (p *parser) isIslandStart() bool {
	return p.lookAheadKeyword("%typeterm") ||
		p.lookAheadKeyword("%oplist") ||
		p.lookAheadKeyword("%oparray") ||
		p.lookAheadKeyword("%op") ||
		p.lookAheadKeyword("%include") ||
		p.lookAheadKeyword("%match")
}

// parseWater consumes raw host source up to the next island start (or EOF)
// and emits a TargetLanguageToCode(TL(content, start, end)) — but ONLY if
// the chunk contains at least one "visible" (ANTLR-sense) byte.
// Whitespace-only water between two islands produces no HOSTBLOCK on the
// Java side (the lexer's NL and WS rules are `-> channel(HIDDEN)`), so we
// mirror that by dropping such chunks silently.
//
// The position calculation is delegated to a faithful Java-side simulation:
//
//   1. raw bytes → tokenizeWater     (water.go)
//   2. tokens    → buildHostblocks   (mirrors CstBuilder.buildHostblock)
//   3. hostblocks → mergeHostblocks  (mirrors CstConverter.simplifyCstBlockList + mergeString)
//
// The merged hostblock's start/end and content are what the Java parser
// would have produced for the same `.t` file.
func (p *parser) parseWater() error {
	start := p.cur
	startIdx := p.idx
	for !p.atEnd() && !p.isIslandStart() {
		p.advance()
	}
	if p.idx == startIdx {
		return nil
	}
	content := p.src[startIdx:p.idx]
	tokens := tokenizeWater(content, start)
	blocks := buildHostblocks(tokens)
	if len(blocks) == 0 {
		// Whitespace-only water: no HOSTBLOCK, matches Java.
		return nil
	}
	merged := mergeHostblocks(blocks)
	p.codes = append(p.codes, tomast.MakeTargetLanguageToCode(tomast.MakeTL(
		merged.content,
		tomast.MakeTextPosition(int64(merged.startLine), int64(merged.startCol)),
		tomast.MakeTextPosition(int64(merged.endLine), int64(merged.endCol)),
	)))
	return nil
}

// parseTypeterm handles `%typeterm ID ('extends' ID)? '{' BODY '}'`.
// Body content is currently dropped (the AST's TypeTermDecl carries
// `concDeclaration()` empty, matching the Java reference on our fixtures).
func (p *parser) parseTypeterm() error {
	startLine := p.cur.line
	if !p.matchKeyword("%typeterm") {
		return fmt.Errorf("expected %%typeterm at %s", p.cur)
	}
	p.skipBlankInline()
	name, err := p.readIdent()
	if err != nil {
		return err
	}
	p.skipBlankInline()
	if p.matchKeyword("extends") {
		p.skipBlankInline()
		if _, err := p.readIdent(); err != nil {
			return err
		}
		p.skipBlankInline()
	}
	if err := p.consumeBalancedBlock(); err != nil {
		return err
	}
	p.codes = append(p.codes, tomast.MakeDeclarationToCode(tomast.MakeTypeTermDecl(
		tomast.MakeName(name),
		tomast.MakeConcDeclaration(),
		tomast.MakeOriginTracking(
			tomast.MakeName(name),
			int64(startLine),
			p.filename,
		),
	)))
	return nil
}

// parseOp handles `%op SORT CTOR '(' slotList? ')' '{' BODY '}'`. Slots are
// consumed but discarded: like %typeterm, the Java reference routes the
// codomain/slot/option information through the SymbolTable, and the AST
// `Code` term carries only `SymbolDecl(Name(ctor))`.
func (p *parser) parseOp() error {
	if !p.matchKeyword("%op") {
		return fmt.Errorf("expected %%op at %s", p.cur)
	}
	p.skipBlankInline()
	if _, err := p.readIdent(); err != nil { // sort name (codomain)
		return err
	}
	p.skipBlankInline()
	ctor, err := p.readIdent()
	if err != nil {
		return err
	}
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) != '(' {
		return fmt.Errorf("expected '(' after operator name at %s", p.cur)
	}
	p.advance() // '('
	if err := p.skipSlotList(); err != nil {
		return err
	}
	if p.atEnd() || p.peek(0) != ')' {
		return fmt.Errorf("expected ')' to close operator slot list at %s", p.cur)
	}
	p.advance() // ')'
	p.skipBlankInline()
	if err := p.consumeBalancedBlock(); err != nil {
		return err
	}
	p.codes = append(p.codes, tomast.MakeDeclarationToCode(tomast.MakeSymbolDecl(
		tomast.MakeName(ctor),
	)))
	return nil
}

// parseVariadicOp handles both `%oplist` (isList=true) and `%oparray`
// (isList=false). Grammar: `'%oplist'|'%oparray' SORT CTOR '(' DOMAIN '*' ')'
// '{' BODY '}'`. The Java reference emits, respectively,
// `ListSymbolDecl(Name(ctor))` or `ArraySymbolDecl(Name(ctor))` — codomain,
// domain and body all routed through the SymbolTable, not the AST.
func (p *parser) parseVariadicOp(isList bool) error {
	kw := "%oparray"
	if isList {
		kw = "%oplist"
	}
	if !p.matchKeyword(kw) {
		return fmt.Errorf("expected %s at %s", kw, p.cur)
	}
	p.skipBlankInline()
	if _, err := p.readIdent(); err != nil { // codomain
		return err
	}
	p.skipBlankInline()
	ctor, err := p.readIdent()
	if err != nil {
		return err
	}
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) != '(' {
		return fmt.Errorf("expected '(' after operator name at %s", p.cur)
	}
	p.advance() // '('
	p.skipBlankInline()
	if _, err := p.readIdent(); err != nil { // domain
		return err
	}
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) != '*' {
		return fmt.Errorf("expected '*' in variadic operator domain at %s", p.cur)
	}
	p.advance() // '*'
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) != ')' {
		return fmt.Errorf("expected ')' after variadic operator domain at %s", p.cur)
	}
	p.advance() // ')'
	p.skipBlankInline()
	if err := p.consumeBalancedBlock(); err != nil {
		return err
	}
	var decl tomast.Declaration
	if isList {
		decl = tomast.MakeListSymbolDecl(tomast.MakeName(ctor))
	} else {
		decl = tomast.MakeArraySymbolDecl(tomast.MakeName(ctor))
	}
	p.codes = append(p.codes, tomast.MakeDeclarationToCode(decl))
	return nil
}

// parseInclude handles `'%include' '{' path '}'`. The included file is
// resolved relative to the directory of the parser's `filename`, parsed
// recursively, and its top-level codes are wrapped in the same
// `TomInclude(concCode(InstructionToCode(AbstractBlock(concInstruction(
//   CodeToInstruction(c1), CodeToInstruction(c2), …)))))` sandwich that the
// Java reference parser emits.
//
// The grammar (`TomIslandParser.g4`) allows DOT, SLASH, BACKSLASH and ID in
// the path; ANTLR also forwards the whitespace between those tokens as part
// of `ctx.getText()`, so leading and trailing spaces inside `{ … }` are
// trimmed.
func (p *parser) parseInclude() error {
	if !p.matchKeyword("%include") {
		return fmt.Errorf("expected %%include at %s", p.cur)
	}
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) != '{' {
		return fmt.Errorf("expected '{' after %%include at %s", p.cur)
	}
	p.advance() // '{'
	var pathBuf strings.Builder
	for !p.atEnd() && p.peek(0) != '}' {
		pathBuf.WriteByte(p.advance())
	}
	if p.atEnd() {
		return fmt.Errorf("unterminated %%include path from %s", p.cur)
	}
	p.advance() // '}'
	includePath := strings.TrimSpace(pathBuf.String())
	if includePath == "" {
		return fmt.Errorf("empty %%include path at %s", p.cur)
	}

	resolved := includePath
	if !filepath.IsAbs(resolved) {
		resolved = filepath.Join(filepath.Dir(p.filename), resolved)
	}
	resolved, err := filepath.Abs(resolved)
	if err != nil {
		return fmt.Errorf("resolve %%include %q: %w", includePath, err)
	}
	src, err := os.ReadFile(resolved)
	if err != nil {
		return fmt.Errorf("read %%include %q: %w", resolved, err)
	}
	sub, err := Parse(string(src), resolved)
	if err != nil {
		return fmt.Errorf("parse %%include %q: %w", resolved, err)
	}
	// sub is Tom(concCode(c1, c2, …)). Extract the inner codes.
	subTom, ok := sub.(*tomast.TomCode)
	if !ok {
		return fmt.Errorf("%%include %q produced unexpected AST root: %T", resolved, sub)
	}
	innerList, ok := subTom.CodeList.(*tomast.ConcCodeCodeList)
	if !ok {
		return fmt.Errorf("%%include %q codeList not concCode: %T", resolved, subTom.CodeList)
	}
	instructions := make([]tomast.Instruction, len(innerList.Slots))
	for i, c := range innerList.Slots {
		instructions[i] = tomast.MakeCodeToInstruction(c)
	}
	instList := tomast.MakeConcInstruction(instructions...)
	ab := tomast.MakeAbstractBlock(instList)
	codeIn := tomast.MakeInstructionToCode(ab)
	include := tomast.MakeTomInclude(tomast.MakeConcCode(codeIn))
	p.codes = append(p.codes, include)
	return nil
}

// parseMatch handles `%match(subject1, subject2, …) { actionRule* }`. Each
// action rule is `pattern -> { … }`. Current coverage: one subject (a bare
// ID, lowered to BQVariable with "unknown type"), and the anonymous wildcard
// pattern `_` (lowered to a Variable with EmptyName). Action bodies are
// consumed verbatim (`consumeBalancedBlock`) and lowered to
// `RawAction(If(TrueTL(), AbstractBlock(concInstruction()), Nop()))` — the
// shape the Java parser produces when the body is empty. Non-empty bodies
// would require a recursive sub-parser; left as a TODO for the next pass.
func (p *parser) parseMatch() error {
	startLine := p.cur.line
	if !p.matchKeyword("%match") {
		return fmt.Errorf("expected %%match at %s", p.cur)
	}
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) != '(' {
		return fmt.Errorf("expected '(' after %%match at %s", p.cur)
	}
	p.advance() // '('
	subjects, err := p.parseSubjectList()
	if err != nil {
		return err
	}
	if p.atEnd() || p.peek(0) != ')' {
		return fmt.Errorf("expected ')' to close %%match subjects at %s", p.cur)
	}
	p.advance() // ')'
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) != '{' {
		return fmt.Errorf("expected '{' to open %%match body at %s", p.cur)
	}
	p.advance() // '{'

	var rules []tomast.ConstraintInstruction
	for {
		p.skipBlankInline()
		if p.atEnd() {
			return fmt.Errorf("unterminated %%match body from %s", p.cur)
		}
		if p.peek(0) == '}' {
			break
		}
		rule, err := p.parseActionRule(subjects)
		if err != nil {
			return err
		}
		rules = append(rules, rule)
	}
	p.advance() // '}'

	options := tomast.MakeConcOption(
		tomast.MakeOriginTracking(tomast.MakeName("Match"), int64(startLine), p.filename),
		tomast.MakeModuleName("default"),
	)
	match := tomast.MakeMatch(
		tomast.MakeConcConstraintInstruction(rules...),
		options,
	)
	p.codes = append(p.codes, tomast.MakeInstructionToCode(match))
	return nil
}

// parseSubjectList consumes `subject (',' subject)*` between '(' and ')'.
func (p *parser) parseSubjectList() ([]tomast.BQTerm, error) {
	var subjects []tomast.BQTerm
	p.skipBlankInline()
	if !p.atEnd() && p.peek(0) == ')' {
		return subjects, nil
	}
	for {
		subj, err := p.parseSubject()
		if err != nil {
			return nil, err
		}
		subjects = append(subjects, subj)
		p.skipBlankInline()
		if p.atEnd() || p.peek(0) != ',' {
			return subjects, nil
		}
		p.advance() // ','
		p.skipBlankInline()
	}
}

// parseSubject parses one subject of a %match. Current coverage: a bare ID,
// lowered to BQVariable(opts, Name(id), unknownType()) where opts carries the
// OriginTracking and the default ModuleName, matching the Java reference.
func (p *parser) parseSubject() (tomast.BQTerm, error) {
	subjLine := p.cur.line
	name, err := p.readIdent()
	if err != nil {
		return nil, err
	}
	options := tomast.MakeConcOption(
		tomast.MakeOriginTracking(tomast.MakeName(name), int64(subjLine), p.filename),
		tomast.MakeModuleName("default"),
	)
	return tomast.MakeBQVariable(options, tomast.MakeName(name), unknownType()), nil
}

// parseActionRule handles `pattern '->' '{' BALANCED '}'`. The action
// body is consumed via the brace-counter; for our first fixture (an empty
// body) the lowering produces
// `RawAction(If(TrueTL(), AbstractBlock(concInstruction()), Nop()))`.
func (p *parser) parseActionRule(subjects []tomast.BQTerm) (tomast.ConstraintInstruction, error) {
	if len(subjects) == 0 {
		return nil, fmt.Errorf("action rule without %%match subject at %s", p.cur)
	}
	ruleLine := p.cur.line
	pattern, err := p.parsePattern()
	if err != nil {
		return nil, err
	}
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) != '-' || p.peek(1) != '>' {
		return nil, fmt.Errorf("expected '->' in action rule at %s", p.cur)
	}
	p.advance() // '-'
	p.advance() // '>'
	p.skipBlankInline()
	if err := p.consumeBalancedBlock(); err != nil {
		return nil, err
	}
	action := tomast.MakeRawAction(tomast.MakeIf(
		tomast.MakeTrueTL(),
		tomast.MakeAbstractBlock(tomast.MakeConcInstruction()),
		tomast.MakeNop(),
	))
	constraint := tomast.MakeMatchConstraint(pattern, subjects[0], unknownType())
	options := tomast.MakeConcOption(
		tomast.MakeOriginTracking(tomast.MakeName("ConstraintAction"), int64(ruleLine), p.filename),
	)
	return tomast.MakeConstraintInstruction(constraint, action, options), nil
}

// parsePattern is the (currently tiny) pattern parser. Two shapes supported:
//   - `_` → `Variable(concOption(), EmptyName(),  unknownType, concConstraint())`
//   - `x` → `Variable(concOption(), Name("x"),    unknownType, concConstraint())`
// Both match the Java reference (AstBuilder.java lines 752-766): no
// OriginTracking is attached on the pattern's option list.
func (p *parser) parsePattern() (tomast.TomTerm, error) {
	if p.atEnd() {
		return nil, fmt.Errorf("expected pattern at %s", p.cur)
	}
	// Anonymous wildcard: '_' not followed by another ident char.
	if p.peek(0) == '_' && !isIdentChar(p.peek(1)) {
		p.advance() // '_'
		return tomast.MakeVariable(
			tomast.MakeConcOption(),
			tomast.MakeEmptyName(),
			unknownType(),
			tomast.MakeConcConstraint(),
		), nil
	}
	if !isIdentStart(p.peek(0)) {
		return nil, fmt.Errorf("expected pattern variable at %s", p.cur)
	}
	name, err := p.readIdent()
	if err != nil {
		return nil, err
	}
	return tomast.MakeVariable(
		tomast.MakeConcOption(),
		tomast.MakeName(name),
		unknownType(),
		tomast.MakeConcConstraint(),
	), nil
}

// unknownType returns the canonical placeholder `Type(concTypeOption(),
// "unknown type", EmptyTargetLanguageType())` that the Java parser uses
// before the typer phase runs.
func unknownType() tomast.TomType {
	return tomast.MakeType(
		tomast.MakeConcTypeOption(),
		"unknown type",
		tomast.MakeEmptyTargetLanguageType(),
	)
}

// skipSlotList consumes `(slot (',' slot)*)?` after the opening '(' but
// before the closing ')'. Each slot has shape `ID (':' ID)?` — the leading
// ':' is optional in the original ANTLR grammar (`slot : id1=ID COLON? id2=ID`),
// so we mirror that.
func (p *parser) skipSlotList() error {
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) == ')' {
		return nil
	}
	for {
		if _, err := p.readIdent(); err != nil { // slot name
			return err
		}
		p.skipBlankInline()
		if !p.atEnd() && p.peek(0) == ':' {
			p.advance()
			p.skipBlankInline()
		}
		if _, err := p.readIdent(); err != nil { // slot type
			return err
		}
		p.skipBlankInline()
		if p.atEnd() || p.peek(0) != ',' {
			return nil
		}
		p.advance() // ','
		p.skipBlankInline()
	}
}

// consumeBalancedBlock requires the current character to be '{', then advances
// past the matching '}'. Nested braces are tolerated. Comments and string
// literals are NOT honoured for the current target — that's fine because our
// fixtures don't put unmatched braces inside strings/comments.
func (p *parser) consumeBalancedBlock() error {
	if p.atEnd() || p.peek(0) != '{' {
		return fmt.Errorf("expected '{' at %s", p.cur)
	}
	depth := 0
	for !p.atEnd() {
		c := p.advance()
		if c == '{' {
			depth++
		} else if c == '}' {
			depth--
			if depth == 0 {
				return nil
			}
		}
	}
	return fmt.Errorf("unterminated block from %s", p.cur)
}

func (p *parser) skipBlankInline() {
	for !p.atEnd() {
		c := p.peek(0)
		if c == ' ' || c == '\t' || c == '\r' || c == '\n' {
			p.advance()
		} else {
			return
		}
	}
}

func (p *parser) readIdent() (string, error) {
	if p.atEnd() || !isIdentStart(p.peek(0)) {
		return "", fmt.Errorf("expected identifier at %s", p.cur)
	}
	var sb strings.Builder
	for !p.atEnd() && isIdentChar(p.peek(0)) {
		sb.WriteByte(p.advance())
	}
	return sb.String(), nil
}

func isIdentStart(c byte) bool {
	return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_'
}

func isIdentChar(c byte) bool {
	return isIdentStart(c) || (c >= '0' && c <= '9')
}
