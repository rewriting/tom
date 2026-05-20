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
//   actionRule : ruleSlot (',' ruleSlot)* '->' '{' BALANCED '}'
//   ruleSlot   : pattern ('<<' bqterm)?
//   pattern    : '!' pattern | basePattern ('@' ID)?
//   basePattern: '_' '*'? | ID '*'? | ID '(' (pattern (',' pattern)*)? ')'
//              | '(' ID ('|' ID)* ')' '(' (pattern (',' pattern)*)? ')'
//                                                       (Variable/VariableStar or TermAppl, anti-prefix and `@` annotation optional)
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

	"tom/tomgo/stable/library/tomast"
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

// newSubParser returns a parser scoped to `src` (typically the captured
// content of an action-rule body) whose position counter starts at
// `start`. It does NOT mutate or share state with any outer parser.
// Used by lowerActionBody to recover positional metadata when consuming
// backquote islands inside a host-code body.
func newSubParser(src, filename string, start position) *parser {
	return &parser{src: src, filename: filename, cur: start}
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

// parseBQTerm parses one backquote term. The grammar (from
// TomIslandParser.g4:122-128) accepts an optional leading `` ` `` followed by
// either:
//   - `ID '(' (bqterm (',' bqterm)*)? ')'`  → `BQAppl(opts, Name(ID), bqList)`
//   - `ID '*'?`                             → `BQVariable(opts, Name(ID), unknownType)`
//                                             (or `BQVariableStar` if `*` — TODO)
//
// Per `AstBuilder.java:551-570`, the option list carries an `OriginTracking`
// for the symbol/variable name plus the default `ModuleName("default")` —
// same shape as the parens-subject of `%match(...)`.
//
// Limits for this first jet: no codomain `ID:Type` prefix, no implicit-args
// records `Foo[a=b]`, no ITL composite, no BQVariableStar `x*`, no nested
// backquote scope semantics.
func (p *parser) parseBQTerm() (tomast.BQTerm, error) {
	startLine := p.cur.line
	if !p.atEnd() && p.peek(0) == '`' {
		p.advance() // '`'
		p.skipBlankInline()
	}
	if p.atEnd() || !isIdentStart(p.peek(0)) {
		return nil, fmt.Errorf("expected bqterm identifier at %s", p.cur)
	}
	name, err := p.readIdent()
	if err != nil {
		return nil, err
	}
	options := tomast.MakeConcOption(
		tomast.MakeOriginTracking(tomast.MakeName(name), int64(startLine), p.filename),
		tomast.MakeModuleName("default"),
	)
	save := p.idx
	saveCur := p.cur
	p.skipBlankInline()
	if !p.atEnd() && p.peek(0) == '(' {
		p.advance() // '('
		args, err := p.parseBQTermArgList()
		if err != nil {
			return nil, err
		}
		if p.atEnd() || p.peek(0) != ')' {
			return nil, fmt.Errorf("expected ')' to close bqterm application at %s", p.cur)
		}
		p.advance() // ')'
		return tomast.MakeBQAppl(options, tomast.MakeName(name), tomast.MakeConcBQTerm(args...)), nil
	}
	p.idx = save
	p.cur = saveCur
	return tomast.MakeBQVariable(options, tomast.MakeName(name), unknownType()), nil
}

// parseBQTermArgList consumes `bqterm (',' bqterm)*` between '(' and ')'.
func (p *parser) parseBQTermArgList() ([]tomast.BQTerm, error) {
	var args []tomast.BQTerm
	p.skipBlankInline()
	if !p.atEnd() && p.peek(0) == ')' {
		return args, nil
	}
	for {
		bq, err := p.parseBQTerm()
		if err != nil {
			return nil, err
		}
		args = append(args, bq)
		p.skipBlankInline()
		if p.atEnd() {
			return nil, fmt.Errorf("unterminated bqterm arg list at %s", p.cur)
		}
		if p.peek(0) == ')' {
			return args, nil
		}
		if p.peek(0) != ',' {
			return nil, fmt.Errorf("expected ',' or ')' in bqterm arg list at %s", p.cur)
		}
		p.advance() // ','
		p.skipBlankInline()
	}
}

// parseActionRule handles `pattern (',' pattern)* '->' '{' BALANCED '}'`.
// The number of patterns must match the number of %match subjects; the
// resulting constraint is the AndConstraint of the N MatchConstraints
// (with N==1 collapsing to a bare MatchConstraint via the AU hook's
// 1-element short-circuit). Action bodies are still consumed verbatim
// via the brace counter and lowered to
// `RawAction(If(TrueTL(), AbstractBlock(concInstruction()), Nop()))`.
func (p *parser) parseActionRule(subjects []tomast.BQTerm) (tomast.ConstraintInstruction, error) {
	if len(subjects) == 0 {
		return nil, fmt.Errorf("action rule without %%match subject at %s", p.cur)
	}
	ruleLine := p.cur.line

	// Each entry of `patterns` may carry an optional explicit subject from a
	// `pattern '<<' bqterm` form. If present, it overrides the i-th implicit
	// subject from the %match parens (cf. AstBuilder.java:683-698 —
	// Cst_MatchTermConstraint vs Cst_MatchArgumentConstraint).
	type ruleSlot struct {
		pat            tomast.TomTerm
		explicit       tomast.BQTerm // nil if implicit
	}
	var slots []ruleSlot
	for {
		pat, err := p.parsePattern()
		if err != nil {
			return nil, err
		}
		slot := ruleSlot{pat: pat}
		p.skipBlankInline()
		if !p.atEnd() && p.peek(0) == '<' && p.peek(1) == '<' {
			p.advance() // '<'
			p.advance() // '<'
			p.skipBlankInline()
			bq, err := p.parseBQTerm()
			if err != nil {
				return nil, fmt.Errorf("after '<<': %w", err)
			}
			slot.explicit = bq
			p.skipBlankInline()
		}
		slots = append(slots, slot)
		if p.atEnd() {
			return nil, fmt.Errorf("unterminated action rule at %s", p.cur)
		}
		if p.peek(0) == ',' {
			p.advance() // ','
			p.skipBlankInline()
			continue
		}
		break
	}
	if len(slots) != len(subjects) {
		return nil, fmt.Errorf("action rule has %d patterns but %%match has %d subjects at %s", len(slots), len(subjects), p.cur)
	}

	if p.atEnd() || p.peek(0) != '-' || p.peek(1) != '>' {
		return nil, fmt.Errorf("expected '->' in action rule at %s", p.cur)
	}
	p.advance() // '-'
	p.advance() // '>'
	p.skipBlankInline()
	bodyContent, bodyStart, err := p.captureBalancedBlock()
	if err != nil {
		return nil, err
	}
	bodyInstructions, err := lowerActionBody(bodyContent, bodyStart, p.filename)
	if err != nil {
		return nil, fmt.Errorf("action body: %w", err)
	}
	action := tomast.MakeRawAction(tomast.MakeIf(
		tomast.MakeTrueTL(),
		tomast.MakeAbstractBlock(tomast.MakeConcInstruction(bodyInstructions...)),
		tomast.MakeNop(),
	))

	matchConstraints := make([]tomast.Constraint, len(slots))
	for i, slot := range slots {
		subj := subjects[i]
		if slot.explicit != nil {
			subj = slot.explicit
		}
		matchConstraints[i] = tomast.MakeMatchConstraint(slot.pat, subj, unknownType())
	}
	// MakeAndConstraint with 1 arg returns the bare MatchConstraint (AU
	// hook); with >=2 args returns AndConstraint(MC1, MC2, …). Matches
	// Java's cons-reduced shape (HookTypeExpander.java:569 absorption).
	constraint := tomast.MakeAndConstraint(matchConstraints...)
	options := tomast.MakeConcOption(
		tomast.MakeOriginTracking(tomast.MakeName("ConstraintAction"), int64(ruleLine), p.filename),
	)
	return tomast.MakeConstraintInstruction(constraint, action, options), nil
}

// parsePattern parses one pattern, optionally prefixed by `!` (anti-pattern)
// and/or suffixed by `@ ID` (annotation). The leading `!` lowers to
// `AntiTerm(pat)` (cf. AstBuilder.java:780-792 — Cst_Anti) and is consumed
// before the recursive call, so `!pat` and `!pat@name` are both supported
// (the latter producing `AntiTerm(annotated_pat)`). The `p.peek(1) != '='`
// guard avoids confusing `!=` (eventual numerical constraint operator) with
// the anti prefix.
func (p *parser) parsePattern() (tomast.TomTerm, error) {
	if !p.atEnd() && p.peek(0) == '!' && p.peek(1) != '=' {
		p.advance() // '!'
		p.skipBlankInline()
		inner, err := p.parsePattern()
		if err != nil {
			return nil, fmt.Errorf("after '!': %w", err)
		}
		return tomast.MakeAntiTerm(inner), nil
	}
	base, err := p.parseBasePattern()
	if err != nil {
		return nil, err
	}
	// Optional `@ ID` annotation. Lookahead skips inline whitespace; on
	// mismatch we rewind so the caller observes the cursor right after the
	// base pattern.
	save := p.idx
	saveCur := p.cur
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) != '@' {
		p.idx = save
		p.cur = saveCur
		return base, nil
	}
	p.advance() // '@'
	p.skipBlankInline()
	name, err := p.readIdent()
	if err != nil {
		return nil, fmt.Errorf("after '@': %w", err)
	}
	// AliasTo(Variable(concOption(OT(Name(name),0,"unknown file")),
	//                  Name(name), unknownType, concConstraint()))
	// Line=0 and file="unknown file" are the placeholders the Java parser
	// emits (ASTFactory.java:285); they get filled in later by the typer.
	aliasVar := tomast.MakeVariable(
		tomast.MakeConcOption(tomast.MakeOriginTracking(
			tomast.MakeName(name), 0, "unknown file",
		)),
		tomast.MakeName(name),
		unknownType(),
		tomast.MakeConcConstraint(),
	)
	alias := tomast.MakeAliasTo(aliasVar)
	annotated, err := addPatternConstraint(base, alias)
	if err != nil {
		return nil, fmt.Errorf("'@' annotation: %w", err)
	}
	return annotated, nil
}

// parseBasePattern is the (currently tiny) pattern parser. Five shapes
// supported (any of which can carry an `@` annotation in parsePattern):
//   - `_`             → `Variable(concOption(),     EmptyName(),                       unknownType,          concConstraint())`
//   - `_*`            → `VariableStar(concOption(), EmptyName(),                       unknownType,          concConstraint())`
//   - `x`             → `Variable(concOption(),     Name("x"),                         unknownType,          concConstraint())`
//   - `x*`            → `VariableStar(concOption(), Name("x"),                         unknownType,          concConstraint())`
//   - `Foo(p1, ..., pN)` → `TermAppl(concOption(), concTomName(Name("Foo")), concTomTerm(p1...),             concConstraint())`
//
// Variable / VariableStar follow AstBuilder.java:752-766; applications
// (nullary or with sub-patterns) follow the Cst_Appl branch
// (CstBuilder.java:452-453 + AstBuilder.java:793-804). Sub-patterns are
// parsed recursively, so `Foo(x, Bar())` nests a Variable and a nullary
// TermAppl in the arg list. Applications cannot carry a `*` suffix in
// the source grammar.
func (p *parser) parseBasePattern() (tomast.TomTerm, error) {
	if p.atEnd() {
		return nil, fmt.Errorf("expected pattern at %s", p.cur)
	}
	// OR-pattern head: '(' ID ('|' ID)* ')' followed by an explicit arg list.
	// CstBuilder.java:445-471 routes such a pattern through Cst_Appl with a
	// multi-element CstSymbolList; AstBuilder.java:793-804 then emits a
	// TermAppl whose nameList contains the N candidates. A single-element
	// `(Foo)(args)` is also accepted (degenerate OR, same encoding).
	if p.peek(0) == '(' {
		p.advance() // '('
		p.skipBlankInline()
		var names []tomast.TomName
		for {
			id, err := p.readIdent()
			if err != nil {
				return nil, fmt.Errorf("in OR-pattern head: %w", err)
			}
			names = append(names, tomast.MakeName(id))
			p.skipBlankInline()
			if p.atEnd() {
				return nil, fmt.Errorf("unterminated OR-pattern head at %s", p.cur)
			}
			if p.peek(0) == '|' {
				p.advance() // '|'
				p.skipBlankInline()
				continue
			}
			break
		}
		if p.atEnd() || p.peek(0) != ')' {
			return nil, fmt.Errorf("expected ')' to close OR-pattern head at %s", p.cur)
		}
		p.advance() // ')'
		p.skipBlankInline()
		if p.atEnd() || p.peek(0) != '(' {
			return nil, fmt.Errorf("OR-pattern must be followed by '(' arg list at %s", p.cur)
		}
		p.advance() // '('
		args, err := p.parsePatternArgList()
		if err != nil {
			return nil, err
		}
		if p.atEnd() || p.peek(0) != ')' {
			return nil, fmt.Errorf("expected ')' to close OR-pattern arg list at %s", p.cur)
		}
		p.advance() // ')'
		return tomast.MakeTermAppl(
			tomast.MakeConcOption(),
			tomast.MakeConcTomName(names...),
			tomast.MakeConcTomTerm(args...),
			tomast.MakeConcConstraint(),
		), nil
	}
	// Anonymous wildcard or wildcard-star: '_' not followed by another
	// ident char, optionally followed by '*'.
	if p.peek(0) == '_' && !isIdentChar(p.peek(1)) {
		p.advance() // '_'
		if !p.atEnd() && p.peek(0) == '*' {
			p.advance() // '*'
			return tomast.MakeVariableStar(
				tomast.MakeConcOption(),
				tomast.MakeEmptyName(),
				unknownType(),
				tomast.MakeConcConstraint(),
			), nil
		}
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
	// After the ID we look for one of: '(' (application), '*'
	// (VariableStar), or anything else (plain Variable). The check tolerates
	// optional whitespace between the ident and '(' or '*' — same convention
	// as ANTLR's hidden-channel whitespace. On a mismatch we rewind so the
	// caller sees the original cursor.
	save := p.idx
	saveCur := p.cur
	p.skipBlankInline()
	if !p.atEnd() && p.peek(0) == '(' {
		p.advance() // '('
		args, err := p.parsePatternArgList()
		if err != nil {
			return nil, err
		}
		if p.atEnd() || p.peek(0) != ')' {
			return nil, fmt.Errorf("expected ')' to close pattern application at %s", p.cur)
		}
		p.advance() // ')'
		return tomast.MakeTermAppl(
			tomast.MakeConcOption(),
			tomast.MakeConcTomName(tomast.MakeName(name)),
			tomast.MakeConcTomTerm(args...),
			tomast.MakeConcConstraint(),
		), nil
	}
	if !p.atEnd() && p.peek(0) == '*' {
		p.advance() // '*'
		return tomast.MakeVariableStar(
			tomast.MakeConcOption(),
			tomast.MakeName(name),
			unknownType(),
			tomast.MakeConcConstraint(),
		), nil
	}
	p.idx = save
	p.cur = saveCur
	return tomast.MakeVariable(
		tomast.MakeConcOption(),
		tomast.MakeName(name),
		unknownType(),
		tomast.MakeConcConstraint(),
	), nil
}

// addPatternConstraint returns a copy of `pat` with `c` added as its
// (only) constraint. Used by parsePattern to lower the `pat @ name` form:
// the Java reference (AstBuilder.java:823-825) prepends the AliasTo to
// the pattern's existing constraint list and re-emits the pattern via
// `pattern.setConstraints(...)`. Since our parser always starts with an
// empty `concConstraint()`, "prepend to empty list" reduces to
// "single-element list with this constraint".
func addPatternConstraint(pat tomast.TomTerm, c tomast.Constraint) (tomast.TomTerm, error) {
	cl := tomast.MakeConcConstraint(c)
	switch v := pat.(type) {
	case *tomast.VariableTomTerm:
		return tomast.MakeVariable(v.Options, v.AstName, v.AstType, cl), nil
	case *tomast.VariableStarTomTerm:
		return tomast.MakeVariableStar(v.Options, v.AstName, v.AstType, cl), nil
	case *tomast.TermApplTomTerm:
		return tomast.MakeTermAppl(v.Options, v.NameList, v.Args, cl), nil
	default:
		return nil, fmt.Errorf("cannot attach '@' annotation to %T", pat)
	}
}

// parsePatternArgList consumes `(pattern (',' pattern)*)?` after the
// opening `(` but before the closing `)`. Recursively delegates to
// parsePattern for each sub-pattern.
func (p *parser) parsePatternArgList() ([]tomast.TomTerm, error) {
	var args []tomast.TomTerm
	p.skipBlankInline()
	if !p.atEnd() && p.peek(0) == ')' {
		return args, nil
	}
	for {
		sub, err := p.parsePattern()
		if err != nil {
			return nil, err
		}
		args = append(args, sub)
		p.skipBlankInline()
		if p.atEnd() {
			return nil, fmt.Errorf("unterminated pattern arg list at %s", p.cur)
		}
		if p.peek(0) == ')' {
			return args, nil
		}
		if p.peek(0) != ',' {
			return nil, fmt.Errorf("expected ',' or ')' in pattern arg list at %s", p.cur)
		}
		p.advance() // ','
		p.skipBlankInline()
	}
}

// lowerActionBody turns the captured content of an action rule's body
// (everything between '{' and '}', positions starting just after the '{')
// into the list of Instructions that AstBuilder.java places inside
// `AbstractBlock(concInstruction(…))`.
//
// The body is a mixture of *water* (host-code chunks) and *backquote
// islands* (`` `bqterm ``). The reference Java parser (CstBuilder.exitBlock
// at lines 236-256, AstBuilder.convert(CstBlock) at lines 101-544) walks
// the children of the block context, emitting either a HOSTBLOCK or a
// Cst_BQTermToBlock per chunk. The latter lowers to a
// `BQTermToInstruction(BQTerm)` (AstBuilder line 119). Each HOSTBLOCK
// becomes one `CodeToInstruction(TargetLanguageToCode(TL(content, start,
// end)))` after the merge.
//
// We replay that two-pass behaviour with a sub-parser:
//   1. Walk `content` byte by byte from `start`.
//   2. On a `` ` ``, flush any accumulated water through the water pipeline
//      (water-only chunks emit no hostblock → match4b/c/d/e/f/g body
//      semantics), then delegate to `parseBQTerm` for the island. The
//      returned BQTerm is wrapped in `BQTermToInstruction`.
//   3. At EOF, flush remaining water.
//
// Other host-language islands (nested `%match`, `%strategy`, …) inside
// the body are deliberately treated as opaque water for now — the byte
// of `%` is not a switch trigger. To be lifted when those islands are
// supported in body context.
func lowerActionBody(content string, start position, filename string) ([]tomast.Instruction, error) {
	sub := newSubParser(content, filename, start)
	var insts []tomast.Instruction
	waterStartIdx := 0
	waterStartPos := start
	flushWater := func(endIdx int, endPos position) {
		_ = endPos // only used to keep the position bookkeeping documented
		if endIdx <= waterStartIdx {
			return
		}
		chunk := content[waterStartIdx:endIdx]
		tokens := tokenizeWater(chunk, waterStartPos)
		blocks := buildHostblocks(tokens)
		if len(blocks) == 0 {
			return
		}
		merged := mergeHostblocks(blocks)
		tl := tomast.MakeTL(
			merged.content,
			tomast.MakeTextPosition(int64(merged.startLine), int64(merged.startCol)),
			tomast.MakeTextPosition(int64(merged.endLine), int64(merged.endCol)),
		)
		insts = append(insts, tomast.MakeCodeToInstruction(tomast.MakeTargetLanguageToCode(tl)))
	}
	for !sub.atEnd() {
		if sub.peek(0) == '`' {
			flushWater(sub.idx, sub.cur)
			bq, err := sub.parseBQTerm()
			if err != nil {
				return nil, fmt.Errorf("at %s: %w", sub.cur, err)
			}
			insts = append(insts, tomast.MakeBQTermToInstruction(bq))
			waterStartIdx = sub.idx
			waterStartPos = sub.cur
			continue
		}
		sub.advance()
	}
	flushWater(sub.idx, sub.cur)
	return insts, nil
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

// captureBalancedBlock is like consumeBalancedBlock but returns the content
// between the braces (exclusive) and the position of the first byte AFTER the
// opening '{'. Used by parseActionRule to lower a non-empty body into
// `TL`/`ITL` instructions via the water pipeline.
func (p *parser) captureBalancedBlock() (content string, start position, err error) {
	if p.atEnd() || p.peek(0) != '{' {
		return "", position{}, fmt.Errorf("expected '{' at %s", p.cur)
	}
	p.advance() // '{'
	start = p.cur
	startIdx := p.idx
	depth := 1
	for !p.atEnd() {
		c := p.advance()
		if c == '{' {
			depth++
		} else if c == '}' {
			depth--
			if depth == 0 {
				// p.idx now points one past the closing '}'.
				return p.src[startIdx : p.idx-1], start, nil
			}
		}
	}
	return "", position{}, fmt.Errorf("unterminated block from %s", p.cur)
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
