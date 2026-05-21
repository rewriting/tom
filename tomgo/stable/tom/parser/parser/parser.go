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
	"os/exec"
	"path/filepath"
	"strings"

	"tom/tomgo/stable/library/sharedobjects"
	"tom/tomgo/stable/library/tomast"
)

// Parse turns the contents of a .t file into a tomast.Code term shaped exactly
// like the reference Java parser's output. `filename` is recorded into the
// OriginTracking option of every declaration; callers that want a
// path-independent dump should pass "__INPUT__".
func Parse(source, filename string) (tomast.Code, error) {
	r, err := ParseAll(source, filename)
	if err != nil {
		return nil, err
	}
	return r.Code, nil
}

// ParseAll is the richer entry point: it returns the parsed AST plus
// the signature information gathered from `%typeterm` and `%op`
// declarations. Downstream plugins (Desugarer, Typer) consult the
// signature to look up codomain types, slot names, TLType bodies, and
// other data that the Java reference stores in the SymbolTable.
//
// The Signature follows Java's tom.engine.tools.SymbolTable layout in
// spirit: a Sorts map keyed by sort name and a Symbols map keyed by
// operator name, each pointing at a tomast term.
func ParseAll(source, filename string) (*ParseResult, error) {
	return parseAllWithChain(source, filename, nil)
}

// parseAllWithChain is the cycle-safe entry point: it seeds the new
// parser's includeChain with the list of files currently being
// parsed via `%include`. The top-level call passes nil.
func parseAllWithChain(source, filename string, chain []string) (*ParseResult, error) {
	return parseAllWithChainAndSet(source, filename, chain, nil)
}

func parseAllWithChainAndSet(source, filename string, chain []string, parsed *map[string]bool) (*ParseResult, error) {
	p := newParser(source, filename)
	p.includeChain = chain
	if parsed == nil {
		s := make(map[string]bool)
		parsed = &s
	}
	p.alreadyParsed = parsed
	code, err := p.parseProgram()
	if err != nil {
		return nil, err
	}
	return &ParseResult{
		Code:    code,
		Sorts:   p.sig.Sorts,
		Symbols: p.sig.Symbols,
	}, nil
}

// ParseResult bundles the [tomast.Code] AST with the signature data
// gathered while parsing. Callers that only need the AST can keep
// using [Parse]; callers that need symbol/codomain info (the Desugarer
// for slot-name lookup, the Typer for type propagation) use
// [ParseAll].
type ParseResult struct {
	Code tomast.Code

	// Sorts maps each sort name declared by `%typeterm <name> { … }` to
	// the body of its `implement { … }` hook (used as the TLType when
	// the typer rewrites `Type(_, sort, EmptyTargetLanguageType())`).
	// Sorts without an `implement` hook map to the empty string.
	Sorts map[string]string

	// Symbols maps each operator name declared by `%op` / `%oplist` /
	// `%oparray` to its [tomast.TomSymbol] entry. The TomSymbol carries
	// codomain (TypesToType.Codomain), domain (TypesToType.Domain), and
	// slot names (via PairNameDeclList) — everything needed to resolve
	// `Foo(x, y)` in a pattern or backquote to its proper RecordAppl /
	// FunctionCall / BuildTerm form.
	Symbols map[string]tomast.TomSymbol
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

	// sig accumulates signature data — Sorts (sort → TLType body),
	// Symbols (op name → TomSymbol) — as we encounter `%typeterm`,
	// `%op`, `%oplist`, `%oparray` declarations. Empty by default.
	sig signature

	// includeChain records the resolved paths currently being parsed
	// through nested `%include` directives, used to break cycles
	// (e.g. `%include {self.t}`). The chain is propagated from
	// parser to parser via ParseAllWithChain.
	includeChain []string

	// alreadyParsed is the GLOBAL set of resolved-paths that have
	// already produced a non-empty TomInclude in this compilation
	// unit. Mirrors Java's
	// `TomStreamManager.alreadyParsedIncludedFiles`: a second
	// occurrence of the same `%include {file}` (e.g. RuleBool.tom's
	// duplicate `%include {aterm.tom}`) emits a structurally-empty
	// `TomInclude(concCode())` instead of re-parsing. Shared via
	// the pointer so all sub-parsers see the same set.
	alreadyParsed *map[string]bool

	// subjectCodomains is a side-table keyed by the BQTerm subject
	// pointer that parseSubject returned. The value is the codomain
	// type the user wrote before the constant subject (e.g. `int 5`
	// in `%match(int 5)`). parseActionRule reads it when building
	// MatchConstraint.aType so constant subjects with an explicit
	// codomain produce `Type(_, "int", EmptyTL)` instead of the
	// `"unknown type"` placeholder.
	subjectCodomains map[tomast.BQTerm]string
}

// signature is the parser-internal mutable view of [ParseResult]'s
// Sorts/Symbols. Sub-parsers (used for action-body backquotes etc.)
// don't update it because they only consume bqterm islands, not
// declarations.
type signature struct {
	Sorts   map[string]string
	Symbols map[string]tomast.TomSymbol
}

func newParser(src, filename string) *parser {
	return &parser{
		src:      src,
		filename: filename,
		cur:      position{line: 1, col: 1},
		sig: signature{
			Sorts:   make(map[string]string),
			Symbols: make(map[string]tomast.TomSymbol),
		},
		subjectCodomains: make(map[tomast.BQTerm]string),
	}
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
			if err := p.tryIslandOrWater(p.parseMatch, "match"); err != nil {
				return nil, err
			}
		case p.lookAheadKeyword("%strategy"):
			if err := p.tryIslandOrWater(p.parseStrategy, "strategy"); err != nil {
				return nil, err
			}
		case p.lookAheadKeyword("%gom"):
			if err := p.parseGom(); err != nil {
				return nil, err
			}
		case p.idx+1 < len(p.src) && p.peek(0) == '%' && p.peek(1) == '[':
			if err := p.parseMetaquote(); err != nil {
				return nil, err
			}
		case p.peek(0) == '`' && p.idx+1 < len(p.src) && (isIdentStart(p.peek(1)) || p.peek(1) == '('):
			// `bqcomposite` island (TomIslandParser.g4:31-42): a
			// `\`f(args)` (or `\`(args)`) at top level is parsed as
			// a backquote term and wrapped in
			// `InstructionToCode(BQTermToInstruction(<bqterm>))`.
			if err := p.tryIslandOrWater(p.parseTopLevelBackquote, "bqcomposite"); err != nil {
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
		p.lookAheadKeyword("%match") ||
		p.lookAheadKeyword("%strategy") ||
		p.lookAheadKeyword("%gom") ||
		(p.peek(0) == '%' && p.peek(1) == '[') ||
		(p.peek(0) == '`' && p.idx+1 < len(p.src) && (isIdentStart(p.peek(1)) || p.peek(1) == '('))
}

// parseGom handles `%gom (options)? { body }`. Mirrors Java's
// TomParserTool.parseGomFile behaviour: the body is sent to the
// Gom compiler, which generates a `.tom` file containing
// `%typeterm` and `%op` declarations for every Gom sort and
// operator. That generated `.tom` is then parsed as if it had been
// `%include`'d at this point — its top-level codes are wrapped in
// `TomInclude(concCode(InstructionToCode(AbstractBlock(
// concInstruction(CodeToInstruction(c1), …)))))`.
//
// Tom-side options like `%gom(--xxx)` are accepted but ignored.
//
// If the Gom CLI isn't available (no TOM_HOME / gom binary missing)
// or fails on the body, the block degrades to the empty `TomInclude`
// shape — matching Java's fall-through on empty bodies.
func (p *parser) parseGom() error {
	if !p.matchKeyword("%gom") {
		return fmt.Errorf("expected %%gom at %s", p.cur)
	}
	p.skipBlankInline()
	if !p.atEnd() && p.peek(0) == '(' {
		depth := 0
		for !p.atEnd() {
			c := p.advance()
			if c == '(' {
				depth++
			} else if c == ')' {
				depth--
				if depth == 0 {
					break
				}
			}
		}
		p.skipBlankInline()
	}
	if p.atEnd() || p.peek(0) != '{' {
		return fmt.Errorf("expected '{' after %%gom at %s", p.cur)
	}
	p.advance() // '{'
	bodyStartIdx := p.idx
	depth := 1
	for !p.atEnd() && depth > 0 {
		c := p.advance()
		if c == '{' {
			depth++
		} else if c == '}' {
			depth--
		}
	}
	if depth != 0 {
		return fmt.Errorf("unterminated %%gom body at %s", p.cur)
	}
	// The body text is everything between `{` and the matching `}` —
	// the closing `}` was consumed by p.advance() so we slice off
	// the trailing brace.
	bodyEndIdx := p.idx - 1
	body := p.src[bodyStartIdx:bodyEndIdx]

	javaPkg := detectJavaPackage(p.src)
	subResult, err := expandGomBlock(body, p.filename, javaPkg, p.includeChain, p.alreadyParsed)
	if err != nil || subResult == nil || subResult.Code == nil {
		// Fall back to the empty-wrapper shape so the structural
		// shape still matches Java's output for empty %gom blocks.
		emptyBlock := tomast.MakeAbstractBlock(tomast.MakeConcInstruction())
		inner := tomast.MakeInstructionToCode(emptyBlock)
		p.codes = append(p.codes, tomast.MakeTomInclude(tomast.MakeConcCode(inner)))
		return nil
	}
	// Merge the Gom-generated module's sorts/operators into the
	// outer parser's signature so the Desugarer can fill in slot
	// names and the Typer can resolve symbol types. Java's
	// TomStreamManager-shared SymbolTable does this implicitly.
	for k, v := range subResult.Sorts {
		if _, exists := p.sig.Sorts[k]; !exists {
			p.sig.Sorts[k] = v
		}
	}
	for k, v := range subResult.Symbols {
		if _, exists := p.sig.Symbols[k]; !exists {
			p.sig.Symbols[k] = v
		}
	}
	tom, ok := subResult.Code.(*tomast.TomCode)
	if !ok {
		return fmt.Errorf("gom output produced unexpected AST root: %T", subResult.Code)
	}
	cl, ok := tom.CodeList.(*tomast.ConcCodeCodeList)
	if !ok {
		return fmt.Errorf("gom output codeList not concCode: %T", tom.CodeList)
	}
	innerCodes := cl.Slots
	if len(innerCodes) == 0 {
		emptyBlock := tomast.MakeAbstractBlock(tomast.MakeConcInstruction())
		inner := tomast.MakeInstructionToCode(emptyBlock)
		p.codes = append(p.codes, tomast.MakeTomInclude(tomast.MakeConcCode(inner)))
		return nil
	}
	instructions := make([]tomast.Instruction, len(innerCodes))
	for i, c := range innerCodes {
		instructions[i] = tomast.MakeCodeToInstruction(c)
	}
	instList := tomast.MakeConcInstruction(instructions...)
	ab := tomast.MakeAbstractBlock(instList)
	codeIn := tomast.MakeInstructionToCode(ab)
	p.codes = append(p.codes, tomast.MakeTomInclude(tomast.MakeConcCode(codeIn)))
	return nil
}

// GomDestDir, when non-empty, overrides where parseGom directs the
// generated .tom file. Defaults to `<dir-of-source>/gen` to match
// Java's `tom --intermediate -d test/gen` behaviour for fixtures
// under test/. Tests can override for hermetic harnesses.
var GomDestDir = ""

// expandGomBlock writes `body` to a temp `.gom` file, runs the Gom
// compiler against it, parses the generated `.tom` output, and
// returns the resulting code list. Mirrors Java's
// TomParserTool.parseGomFile: gom is a textual preprocessor at
// parse time.
//
// Java's parseGomFile passes `--package <lowercase-input-basename>`
// to gom; the package controls (a) the qualified Java class names
// gom generates and (b) — empirically — the order of typeterm
// declarations in the emitted `.tom`. We mirror it here so the
// resulting TomInclude tree matches byte-for-byte.
//
// Returns (nil, nil) for an empty/whitespace-only body.
func expandGomBlock(body, filename, javaPkg string, includeChain []string, alreadyParsed *map[string]bool) (*ParseResult, error) {
	trimmed := strings.TrimSpace(body)
	if trimmed == "" {
		return nil, nil
	}
	moduleName := parseGomModuleName(body)
	if moduleName == "" {
		moduleName = "M"
	}
	// Package passed to gom: Java's TomParserTool.parseGomFile uses
	// `packageName + "." + lowerCase(inputBaseName)`, where
	// packageName comes from the source's `package X;` declaration
	// (empty for unpackaged sources). Mirror that.
	inputBase := strings.TrimSuffix(filepath.Base(filename), ".t")
	pkg := strings.ToLower(inputBase)
	if javaPkg != "" {
		pkg = javaPkg + "." + pkg
	}

	tmpDir, err := os.MkdirTemp("", "tomgom-")
	if err != nil {
		return nil, err
	}
	defer os.RemoveAll(tmpDir)
	gomFile := filepath.Join(tmpDir, moduleName+".gom")
	if err := os.WriteFile(gomFile, []byte(body), 0o644); err != nil {
		return nil, err
	}
	gomBin, tomHome := locateGomBin()
	if gomBin == "" {
		return nil, fmt.Errorf("gom CLI not found (set TOM_HOME)")
	}
	// Output the generated .tom alongside the .t (mimicking Java's
	// behaviour where the test build uses destdir=test/gen). For
	// `<dir>/foo.t`, the gom output lands at `<dir>/gen/<pkg>/<mod>/
	// <mod>.tom`, so OriginTracking paths in the included .tom match
	// the Java reference. If GomDestDir is set explicitly, use it
	// instead (preferred for tests that want a hermetic location).
	outDir := GomDestDir
	if outDir == "" {
		outDir = filepath.Join(filepath.Dir(filename), "gen")
	}
	if err := os.MkdirAll(outDir, 0o755); err != nil {
		return nil, err
	}
	args := []string{"-d", outDir, "--package", pkg, gomFile}
	cmd := exec.Command(gomBin, args...)
	cmd.Env = append(os.Environ(), "TOM_HOME="+tomHome)
	if out, err := cmd.CombinedOutput(); err != nil {
		return nil, fmt.Errorf("gom %s failed: %w\n%s", gomFile, err, out)
	}
	// With --package <pkg>, gom writes
	// <outDir>/<pkg-segments>/<module-lower>/<module-lower>.tom.
	// Compute the exact path rather than walking — outDir may be
	// shared with other fixtures' previous output.
	modLower := strings.ToLower(moduleName)
	pkgSegs := strings.Split(pkg, ".")
	parts := append([]string{outDir}, pkgSegs...)
	parts = append(parts, modLower, modLower+".tom")
	tomFile := filepath.Join(parts...)
	if _, err := os.Stat(tomFile); err != nil {
		return nil, fmt.Errorf("gom produced no .tom at %s: %w", tomFile, err)
	}
	src, err := os.ReadFile(tomFile)
	if err != nil {
		return nil, err
	}
	chain := append(append([]string(nil), includeChain...), filename)
	sub, err := parseAllWithChainAndSet(string(src), tomFile, chain, alreadyParsed)
	if err != nil {
		return nil, err
	}
	return sub, nil
}

// detectJavaPackage finds the first `package X;` statement in the
// .t source and returns X (or "" if absent). Used to compute the
// gom `--package` argument so the generated .tom file lands at the
// same `<destdir>/<java-pkg>/<module>/<module>.tom` location that
// `tom --intermediate` produces.
//
// Skips Java-style `//` and `/* … */` comments so a `package` keyword
// inside a comment doesn't confuse us.
func detectJavaPackage(src string) string {
	i := 0
	n := len(src)
	for i < n {
		// Skip comments.
		if i+1 < n && src[i] == '/' && src[i+1] == '/' {
			for i < n && src[i] != '\n' {
				i++
			}
			continue
		}
		if i+1 < n && src[i] == '/' && src[i+1] == '*' {
			i += 2
			for i+1 < n && !(src[i] == '*' && src[i+1] == '/') {
				i++
			}
			if i+1 < n {
				i += 2
			}
			continue
		}
		// `package` must start at a word boundary.
		if i+7 < n && src[i:i+7] == "package" && (src[i+7] == ' ' || src[i+7] == '\t') {
			j := i + 7
			for j < n && (src[j] == ' ' || src[j] == '\t') {
				j++
			}
			start := j
			for j < n && src[j] != ';' && src[j] != '\n' && src[j] != ' ' && src[j] != '\t' {
				j++
			}
			return src[start:j]
		}
		i++
	}
	return ""
}

// parseGomModuleName extracts the value after `module` from a Gom
// body. Empty string if no `module` keyword is found.
func parseGomModuleName(body string) string {
	for _, line := range strings.Split(body, "\n") {
		trim := strings.TrimSpace(line)
		if rest, ok := strings.CutPrefix(trim, "module "); ok {
			return strings.TrimSpace(rest)
		}
	}
	return ""
}

// locateGomBin returns (gomBinPath, tomHome). Tries $TOM_HOME first,
// then walks up from the current binary location looking for a
// src/dist/bin/gom. Returns empty strings if not found.
func locateGomBin() (string, string) {
	if home := os.Getenv("TOM_HOME"); home != "" {
		if p := filepath.Join(home, "bin", "gom"); fileExists(p) {
			return p, home
		}
	}
	// Common dev paths (this codebase lives at /Users/pem/github/tom/).
	for _, home := range []string{
		"/Users/pem/github/tom/src/dist",
	} {
		if p := filepath.Join(home, "bin", "gom"); fileExists(p) {
			return p, home
		}
	}
	return "", ""
}

func fileExists(p string) bool {
	if _, err := os.Stat(p); err == nil {
		return true
	}
	return false
}

// parseMetaquote handles `%[ ... ]%`. Per CstBuilder.java's exitMetaquote
// + CstConverter.simplifyCstBlockList (Cst_Metaquote branch), the body
// content (everything between `%[` and `]%`) is transformed into a
// Java-string-literal — wrapped in double quotes and with special chars
// (newline, tab, `"`, `\`, etc.) escaped per Java/AT format. The literal
// is then emitted as a single `CodeToInstruction(TargetLanguageToCode(
// TL(<literal>, start, end)))`, wrapped in
// `InstructionToCode(AbstractBlock(concInstruction(...)))` to align with
// AstBuilder.java:115 (Cst_Metaquote→AbstractBlock).
//
// Positions: TL.start = position of the `%` of `%[`, TL.end =
// (start.line, start.col + len(<literal>)). Java reports a single-line
// span regardless of how many lines the source metaquote covered — the
// width is the length of the synthesised literal, not the source.
func (p *parser) parseMetaquote() error {
	if p.peek(0) != '%' || p.peek(1) != '[' {
		return fmt.Errorf("expected %%[ at %s", p.cur)
	}
	startPos := p.cur
	p.advance() // '%'
	p.advance() // '['
	var body strings.Builder
	for !p.atEnd() {
		if p.peek(0) == ']' && p.peek(1) == '%' {
			p.advance() // ']'
			p.advance() // '%'
			literal := sharedobjects.JavaEscape(body.String())
			tl := tomast.MakeTL(
				literal,
				tomast.MakeTextPosition(int64(startPos.line), int64(startPos.col)),
				tomast.MakeTextPosition(int64(startPos.line), int64(startPos.col+len(literal))),
			)
			inner := tomast.MakeCodeToInstruction(tomast.MakeTargetLanguageToCode(tl))
			block := tomast.MakeAbstractBlock(tomast.MakeConcInstruction(inner))
			p.codes = append(p.codes, tomast.MakeInstructionToCode(block))
			return nil
		}
		body.WriteByte(p.advance())
	}
	return fmt.Errorf("unterminated %%[ at %s", startPos)
}

// parseVisit handles
//
//	'visit' ID '{' actionRule* '}'
//
// (per the strategy grammar in TomIslandParser.g4). The `ID` names the
// algebraic sort to walk over; each action rule lowers to a
// ConstraintInstruction whose subject is the SYNTHETIC `tom__arg`
// BQVariable typed with the sort. Per AstBuilder.java:847-853
// (Cst_VisitTerm), `tom__arg` carries options
// `concOption(ModuleName("default"))` — no OriginTracking — and the
// type appears both on the subject and on the MatchConstraint.
//
// VisitTerm-level options carry an OriginTracking labelled "VisitTerm"
// at the line where the `visit` keyword sits.
func (p *parser) parseVisit() (tomast.TomVisit, error) {
	visitLine := p.cur.line
	if !p.matchKeyword("visit") {
		return nil, fmt.Errorf("expected 'visit' at %s", p.cur)
	}
	p.skipBlankInline()
	sortName, err := p.readIdent()
	if err != nil {
		return nil, fmt.Errorf("after 'visit': %w", err)
	}
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) != '{' {
		return nil, fmt.Errorf("expected '{' after visit %s at %s", sortName, p.cur)
	}
	p.advance() // '{'

	sortType := tomast.MakeType(
		tomast.MakeConcTypeOption(),
		sortName,
		tomast.MakeEmptyTargetLanguageType(),
	)
	tomArg := tomast.MakeBQVariable(
		tomast.MakeConcOption(tomast.MakeModuleName("default")),
		tomast.MakeName("tom__arg"),
		sortType,
	)

	var rules []tomast.ConstraintInstruction
	for {
		p.skipBlankInline()
		if p.atEnd() {
			return nil, fmt.Errorf("unterminated visit body at %s", p.cur)
		}
		if p.peek(0) == '}' {
			break
		}
		ruleLine := p.cur.line
		pat, err := p.parsePattern()
		if err != nil {
			return nil, err
		}
		p.skipBlankInline()
		if p.atEnd() || p.peek(0) != '-' || p.peek(1) != '>' {
			return nil, fmt.Errorf("expected '->' in visit rule at %s", p.cur)
		}
		p.advance() // '-'
		p.advance() // '>'
		p.skipBlankInline()
		var bodyInsts []tomast.Instruction
		if !p.atEnd() && p.peek(0) == '{' {
			bodyContent, bodyStart, err := p.captureBalancedBlock()
			if err != nil {
				return nil, err
			}
			bodyInsts, err = lowerActionBody(bodyContent, bodyStart, p.filename)
			if err != nil {
				return nil, fmt.Errorf("visit body: %w", err)
			}
		} else {
			// Bare bqterm form: `pat -> bqterm`. Per
			// TomIslandParser.g4:74 (`actionRule : patternlist ARROW
			// bqterm`), Java emits `Return(bqterm)` as the single
			// instruction of the action block (see
			// AstBuilder.java:691-693 wrapping the bqterm in
			// `ReturnInstruction`).
			bq, err := p.parseBQTerm(false)
			if err != nil {
				return nil, fmt.Errorf("visit body bqterm: %w", err)
			}
			bodyInsts = []tomast.Instruction{tomast.MakeReturn(bq)}
		}
		action := tomast.MakeRawAction(tomast.MakeIf(
			tomast.MakeTrueTL(),
			tomast.MakeAbstractBlock(tomast.MakeConcInstruction(bodyInsts...)),
			tomast.MakeNop(),
		))
		mc := tomast.MakeMatchConstraint(pat, tomArg, sortType)
		ruleOpts := tomast.MakeConcOption(
			tomast.MakeOriginTracking(tomast.MakeName("ConstraintAction"), int64(ruleLine), p.filename),
		)
		rules = append(rules, tomast.MakeConstraintInstruction(mc, action, ruleOpts))
	}
	p.advance() // '}'

	visitOpts := tomast.MakeConcOption(
		tomast.MakeOriginTracking(tomast.MakeName("VisitTerm"), int64(visitLine), p.filename),
	)
	return tomast.MakeVisitTerm(sortType, tomast.MakeConcConstraintInstruction(rules...), visitOpts), nil
}

// parseStrategy handles
//
//	'%strategy' ID '(' slotList? ')' 'extends' bqterm '{' visit* '}'
//
// (TomIslandParser.g4:52-54). Lowers to
//
//	DeclarationToCode(Strategy(Name(id), extends_bqterm, visitList,
//	                           concDeclaration(), concOption(OT(Name(id),line,file))))
//
// per AstBuilder.java:129-137 — the convert() returns CodeToInstruction
// (DeclarationToCode(Strategy(...))) but the top-level emit goes
// through the InstructionToCode/CodeToInstruction inverse-pair hook
// (4.A) which cancels the wrapping, leaving DeclarationToCode at the
// outermost code list.
//
// Current limits: slotList is consumed but discarded (like %op); visit
// blocks are not parsed yet (visitList is always empty).
func (p *parser) parseStrategy() error {
	startLine := p.cur.line
	if !p.matchKeyword("%strategy") {
		return fmt.Errorf("expected %%strategy at %s", p.cur)
	}
	p.skipBlankInline()
	name, err := p.readIdent()
	if err != nil {
		return err
	}
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) != '(' {
		return fmt.Errorf("expected '(' after %%strategy %s at %s", name, p.cur)
	}
	p.advance() // '('
	if err := p.skipSlotList(); err != nil {
		return err
	}
	if p.atEnd() || p.peek(0) != ')' {
		return fmt.Errorf("expected ')' to close %%strategy slot list at %s", p.cur)
	}
	p.advance() // ')'
	p.skipBlankInline()
	if !p.matchKeyword("extends") {
		return fmt.Errorf("expected 'extends' after %%strategy slot list at %s", p.cur)
	}
	p.skipBlankInline()
	extendsBq, err := p.parseBQTerm(false)
	if err != nil {
		return fmt.Errorf("after 'extends': %w", err)
	}
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) != '{' {
		return fmt.Errorf("expected '{' to open %%strategy body at %s", p.cur)
	}
	p.advance() // '{'
	var visits []tomast.TomVisit
	for {
		p.skipWhitespaceAndComments()
		if p.atEnd() {
			return fmt.Errorf("unterminated %%strategy body at %s", p.cur)
		}
		if p.peek(0) == '}' {
			break
		}
		v, err := p.parseVisit()
		if err != nil {
			return err
		}
		visits = append(visits, v)
	}
	p.advance() // '}'
	strat := tomast.MakeStrategy(
		tomast.MakeName(name),
		extendsBq,
		tomast.MakeConcTomVisit(visits...),
		tomast.MakeConcDeclaration(),
		tomast.MakeOriginTracking(tomast.MakeName(name), int64(startLine), p.filename),
	)
	// Java's CstConverter simplification (CstConverter.java, near
	// Cst_StrategyConstruct handling) wraps the strategy in an
	// Cst_AbstractBlock alongside a synthetic `%op Strategy <name>(...)`
	// declaration. The AstBuilder then lowers that AbstractBlock into
	//
	//   InstructionToCode(AbstractBlock(concInstruction(
	//     CodeToInstruction(DeclarationToCode(Strategy(...))),
	//     CodeToInstruction(DeclarationToCode(SymbolDecl(Name(stratName))))
	//   )))
	//
	// We reproduce that shape directly: emit the two CodeToInstruction
	// children and wrap in InstructionToCode(AbstractBlock(...)) for the
	// top-level code list. The synthetic `%op` body (is_fsym/make/get_slot
	// fragments) is dropped — only the SymbolDecl carries through the
	// AST, which is what the engine expects.
	stratCi := tomast.MakeCodeToInstruction(tomast.MakeDeclarationToCode(strat))
	symbolCi := tomast.MakeCodeToInstruction(tomast.MakeDeclarationToCode(
		tomast.MakeSymbolDecl(tomast.MakeName(name)),
	))
	block := tomast.MakeAbstractBlock(tomast.MakeConcInstruction(stratCi, symbolCi))
	p.codes = append(p.codes, tomast.MakeInstructionToCode(block))
	return nil
}

// bqVarTermType returns the AstType slot of a BQVariable /
// BQVariableStar (parser-time helper). nil for any other shape so
// callers can fall back to a placeholder.
func bqVarTermType(bq tomast.BQTerm) tomast.TomType {
	switch v := bq.(type) {
	case *tomast.BQVariableBQTerm:
		return v.AstType
	case *tomast.BQVariableStarBQTerm:
		return v.AstType
	}
	return nil
}

// parseTopLevelBackquote handles `\`f(args)` / `\`(args)` at top
// level (the `bqcomposite` island). The result is wrapped in
// `InstructionToCode(BQTermToInstruction(<bqterm>))` so it composes
// with the rest of the code list. Mirrors Java's bqcomposite
// alternative of the `island` rule.
func (p *parser) parseTopLevelBackquote() error {
	bq, err := p.parseBQTerm(true)
	if err != nil {
		return err
	}
	p.codes = append(p.codes,
		tomast.MakeInstructionToCode(tomast.MakeBQTermToInstruction(bq)),
	)
	return nil
}

// tryIslandOrWater wraps an island-parsing closure with the
// Java-style recovery behaviour: per the ANTLR `start : (island |
// water)*?` rule, when an island fails to parse, the whole
// `<keyword>(args)? { … }` span is consumed as raw water and the
// parser keeps going. Used for `%match` and `%strategy` blocks
// containing non-portable constructs (e.g. `when` clauses) that
// neither Go nor Java actually grok.
//
// `kind` is just a label for diagnostics.
func (p *parser) tryIslandOrWater(island func() error, kind string) error {
	savedIdx := p.idx
	savedCur := p.cur
	savedCodes := len(p.codes)
	if err := island(); err == nil {
		return nil
	}
	// Restore the parser to its pre-island state and consume the
	// whole span up to the matching `}` as water. The recovery span
	// starts at the keyword (saved cursor) and walks through any
	// optional parens, then a brace-balanced body.
	_ = kind
	p.idx = savedIdx
	p.cur = savedCur
	p.codes = p.codes[:savedCodes]
	// Consume the keyword and following content until the next
	// matching `}` at brace-depth 0. We track brace depth from the
	// first `{` we encounter.
	startPos := p.cur
	startIdx := p.idx
	sawOpenBrace := false
	braceDepth := 0
	for !p.atEnd() {
		c := p.peek(0)
		if !sawOpenBrace {
			if c == '{' {
				sawOpenBrace = true
				braceDepth = 1
				p.advance()
				continue
			}
			p.advance()
			continue
		}
		if c == '{' {
			braceDepth++
		} else if c == '}' {
			braceDepth--
			if braceDepth == 0 {
				p.advance() // consume the closing `}`
				break
			}
		}
		p.advance()
	}
	content := p.src[startIdx:p.idx]
	tokens := tokenizeWater(content, startPos)
	blocks := buildHostblocks(tokens)
	if len(blocks) == 0 {
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
	// Whether comments end up in the emitted TL depends on the file
	// kind. Java's lexer routes `//…\n` and `/* … */` comments to
	// a hidden channel for `.tom` files (included via `%include`)
	// but keeps them in the TL stream for `.t` host-language files.
	stripComments := strings.HasSuffix(p.filename, ".tom")
	startPos := p.cur
	var sb strings.Builder
	flush := func(from int, to int) {
		if to > from {
			sb.WriteString(p.src[from:to])
		}
	}
	chunkStart := p.idx
	for !p.atEnd() && !p.isIslandStart() {
		if p.peek(0) == '/' && p.peek(1) == '/' {
			if stripComments {
				flush(chunkStart, p.idx)
			}
			for !p.atEnd() && p.peek(0) != '\n' {
				p.advance()
			}
			if stripComments {
				chunkStart = p.idx
			}
			continue
		}
		if p.peek(0) == '/' && p.peek(1) == '*' {
			if stripComments {
				flush(chunkStart, p.idx)
			}
			p.advance()
			p.advance()
			for !p.atEnd() && !(p.peek(0) == '*' && p.peek(1) == '/') {
				p.advance()
			}
			if !p.atEnd() {
				p.advance()
				p.advance()
			}
			if stripComments {
				chunkStart = p.idx
			}
			continue
		}
		if p.peek(0) == '"' || p.peek(0) == '\'' {
			quote := p.peek(0)
			p.advance()
			for !p.atEnd() && p.peek(0) != quote {
				if p.peek(0) == '\\' && !p.atEnd() {
					p.advance()
					if !p.atEnd() {
						p.advance()
					}
					continue
				}
				if p.peek(0) == '\n' {
					break
				}
				p.advance()
			}
			if !p.atEnd() && p.peek(0) == quote {
				p.advance()
			}
			continue
		}
		p.advance()
	}
	flush(chunkStart, p.idx)
	content := sb.String()
	if content == "" {
		return nil
	}
	// Java's TL position calculation is delegated to a faithful
	// simulation of CstBuilder.buildHostblock + CstConverter.
	// simplifyCstBlockList: tokenize → group visibles into hostblocks
	// with claimed hidden tokens → merge with synthetic padding. The
	// merged hostblock's (start, end) match the Java parser
	// byte-for-byte. Pure-whitespace water yields zero hostblocks and
	// we emit nothing — matching Java's HIDDEN-channel NL/WS rules.
	tokens := tokenizeWater(content, startPos)
	blocks := buildHostblocks(tokens)
	if len(blocks) == 0 {
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

// isAllWhitespace reports whether content is empty or contains only
// space, tab, newline, or carriage-return bytes.
func isAllWhitespace(s string) bool {
	for i := 0; i < len(s); i++ {
		c := s[i]
		if c != ' ' && c != '\t' && c != '\n' && c != '\r' {
			return false
		}
	}
	return true
}

// trimWhitespacePrefix returns the byte offset of the first
// non-whitespace byte in s, or len(s) if all whitespace.
func trimWhitespacePrefix(s string) int {
	for i := 0; i < len(s); i++ {
		c := s[i]
		if c != ' ' && c != '\t' && c != '\n' && c != '\r' {
			return i
		}
	}
	return len(s)
}

// trimWhitespaceSuffix returns one past the byte offset of the last
// non-whitespace byte in s, or 0 if all whitespace.
func trimWhitespaceSuffix(s string) int {
	for i := len(s); i > 0; i-- {
		c := s[i-1]
		if c != ' ' && c != '\t' && c != '\n' && c != '\r' {
			return i
		}
	}
	return 0
}

// trimWhitespaceRange walks `content` from `start` to find the
// (line,col) position of the first non-WS byte and the position just
// past the last non-WS byte. Used to compute the TextPosition pair
// for the TL chunk that wraps a verbatim water blob.
func trimWhitespaceRange(content string, start position) (position, position) {
	first, last := start, start
	curLine, curCol := start.line, start.col
	firstSet := false
	for i := 0; i < len(content); i++ {
		c := content[i]
		if c == '\n' {
			curLine++
			curCol = 1
			continue
		}
		if c == ' ' || c == '\t' || c == '\r' {
			curCol++
			continue
		}
		if !firstSet {
			first = position{line: curLine, col: curCol}
			firstSet = true
		}
		last = position{line: curLine, col: curCol + 1}
		curCol++
	}
	if !firstSet {
		first = start
	}
	return first, last
}

// parseTypeterm handles `%typeterm ID ('extends' ID)? '{' BODY '}'`.
// The body is a sequence of hooks; each one that produces a Declaration
// (is_sort, equals, …) is collected into the TypeTermDecl's
// concDeclaration list, matching the Java reference (AstBuilder.java's
// Cst_TypeTerm branch). Hooks that affect only the SymbolTable side-band
// (implement, get_implementation, …) are recognised but ignored here —
// they'll be handled when the parser starts populating SymbolTable
// entries in a future phase.
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
	if p.atEnd() || p.peek(0) != '{' {
		return fmt.Errorf("expected '{' after %%typeterm at %s", p.cur)
	}
	p.advance() // '{'
	decls, err := p.parseTypetermHooks(name)
	if err != nil {
		return err
	}
	if p.atEnd() || p.peek(0) != '}' {
		return fmt.Errorf("expected '}' to close %%typeterm body at %s", p.cur)
	}
	p.advance() // '}'
	// Java's CstBuilder builds hook lists by cons-front (LIFO), so by
	// the time AstBuilder walks them they come out in reverse source
	// order. Reverse our slice to match.
	reverseDeclarations(decls)
	p.codes = append(p.codes, tomast.MakeDeclarationToCode(tomast.MakeTypeTermDecl(
		tomast.MakeName(name),
		tomast.MakeConcDeclaration(decls...),
		tomast.MakeOriginTracking(
			tomast.MakeName(name),
			int64(startLine),
			p.filename,
		),
	)))
	return nil
}

// reverseDeclarations reverses a Declaration slice in place. Used to
// align our parser's source-order hook collection with Java's
// reverse-order Cst.
func reverseDeclarations(d []tomast.Declaration) {
	for i, j := 0, len(d)-1; i < j; i, j = i+1, j-1 {
		d[i], d[j] = d[j], d[i]
	}
}

// parseTypetermHooks walks the body of a `%typeterm <codomain> { … }`,
// returning the [tomast.Declaration]s produced by each recognised hook.
// `codomain` is the type name that wraps the hook bodies — every BQVariable
// emitted under this body carries `Type(concTypeOption(), codomain, …)`.
//
// Recognised hooks (per AstBuilder.java's Cst_TypeTerm dispatch):
//
//	is_sort(t)       { body } → IsSortDecl
//	equals(t1, t2)   { body } → EqualTermDecl
//	implement        { body } → ignored (codomain TLType, SymbolTable side)
//	get_implementation { body } → ignored (likewise)
//
// Unknown hooks are skipped with their braced body consumed via
// [consumeBalancedBlock] — so unfamiliar fixtures still parse.
func (p *parser) parseTypetermHooks(codomain string) ([]tomast.Declaration, error) {
	var decls []tomast.Declaration
	for {
		p.skipWhitespaceAndComments()
		if p.atEnd() || p.peek(0) == '}' {
			return decls, nil
		}
		hookLine := p.cur.line
		hookName, err := p.readIdent()
		if err != nil {
			return nil, fmt.Errorf("in %%typeterm body: %w", err)
		}
		p.skipBlankInline()
		args, err := p.parseHookArgList()
		if err != nil {
			return nil, err
		}
		p.skipBlankInline()
		if p.atEnd() || p.peek(0) != '{' {
			return nil, fmt.Errorf("expected '{' after hook %q at %s", hookName, p.cur)
		}
		body, err := p.captureHookBody()
		if err != nil {
			return nil, err
		}

		switch hookName {
		case "is_sort":
			if len(args) != 1 {
				return nil, fmt.Errorf("is_sort expects 1 arg, got %d at line %d", len(args), hookLine)
			}
			// Java's AstBuilder runs the body through
			// ASTFactory.abstractCode($vars...) which replaces every
			// $varName with {position} so the Code becomes a template.
			decls = append(decls, tomast.MakeIsSortDecl(
				makeHookBQVariable(args[0], codomain, hookLine, p.filename),
				tomast.MakeCode(abstractCode(body, args)),
				tomast.MakeOriginTracking(tomast.MakeName(codomain), int64(hookLine), p.filename),
			))
		case "equals":
			if len(args) != 2 {
				return nil, fmt.Errorf("equals expects 2 args, got %d at line %d", len(args), hookLine)
			}
			decls = append(decls, tomast.MakeEqualTermDecl(
				makeHookBQVariable(args[0], codomain, hookLine, p.filename),
				makeHookBQVariable(args[1], codomain, hookLine, p.filename),
				tomast.MakeCode(abstractCode(body, args)),
				tomast.MakeOriginTracking(tomast.MakeName(codomain), int64(hookLine), p.filename),
			))
		case "implement":
			// `implement { hostTypeName }` — the body becomes the TLType
			// of every `Type(_, codomain, _)` the typer rewrites. We
			// stash it in the signature side-channel; the typer reads
			// it back via [ParseResult].
			p.sig.Sorts[codomain] = body
		default:
			// get_implementation / unknown — body is host-language code
			// consumed and dropped here. Future phases may grow side
			// effects for specific hook names.
		}
	}
}

// parseHookArgList consumes an optional `(arg1, arg2, …)` parameter
// list of identifiers attached to a hook name. Returns the empty slice
// for hooks with no parens (e.g. `implement {…}`).
func (p *parser) parseHookArgList() ([]string, error) {
	if p.atEnd() || p.peek(0) != '(' {
		return nil, nil
	}
	p.advance() // '('
	var args []string
	for {
		p.skipBlankInline()
		if !p.atEnd() && p.peek(0) == ')' {
			p.advance()
			return args, nil
		}
		id, err := p.readIdent()
		if err != nil {
			return nil, fmt.Errorf("in hook arg list: %w", err)
		}
		args = append(args, id)
		p.skipBlankInline()
		if p.atEnd() {
			return nil, fmt.Errorf("unterminated hook arg list at %s", p.cur)
		}
		switch p.peek(0) {
		case ',':
			p.advance()
		case ':':
			// `slotName:slotType` — skip the type annotation (it's
			// only relevant inside %op slot lists, which still flow
			// through this helper for `get_slot(p,t)` etc.).
			p.advance()
			p.skipBlankInline()
			if _, err := p.readIdent(); err != nil {
				return nil, err
			}
		case ')':
			p.advance()
			return args, nil
		default:
			return nil, fmt.Errorf("expected ',' or ')' in hook arg list at %s", p.cur)
		}
	}
}

// captureHookBody reads `{ <body> }` and returns the body content (the
// text between the outer braces, brace-matched). The braces themselves
// are consumed; their leading/trailing whitespace inside is preserved
// verbatim, since the Java parser routes it through `Code(<body>)`
// where the spaces survive.
func (p *parser) captureHookBody() (string, error) {
	if p.atEnd() || p.peek(0) != '{' {
		return "", fmt.Errorf("expected '{' at %s", p.cur)
	}
	p.advance() // '{'
	startIdx := p.idx
	depth := 1
	for !p.atEnd() && depth > 0 {
		c := p.peek(0)
		switch c {
		case '{':
			depth++
		case '}':
			depth--
			if depth == 0 {
				body := string(p.src[startIdx:p.idx])
				p.advance() // '}'
				return body, nil
			}
		}
		p.advance()
	}
	return "", fmt.Errorf("unterminated hook body at %s", p.cur)
}

// opSlot captures one entry of a `%op` operator's argument list. The
// pair (Name, Type) survives parsing only through the signature
// side-channel; the AST node emitted by `%op` is just SymbolDecl.
type opSlot struct {
	Name string
	Type string
}

// parseOpSlotList consumes `(name1:Type1, name2:Type2, …)?` after the
// opening `(` of a `%op`. The closing `)` is left in the buffer for
// the caller to advance past — matching parseHookArgList's contract
// where the caller owns the bracket symmetry.
func (p *parser) parseOpSlotList() ([]opSlot, error) {
	var slots []opSlot
	p.skipBlankInline()
	if !p.atEnd() && p.peek(0) == ')' {
		return slots, nil
	}
	for {
		p.skipBlankInline()
		name, err := p.readIdent()
		if err != nil {
			return nil, fmt.Errorf("in %%op slot list: %w", err)
		}
		p.skipBlankInline()
		var typeName string
		if !p.atEnd() && p.peek(0) == ':' {
			p.advance() // ':'
			p.skipBlankInline()
			typeName, err = p.readIdent()
			if err != nil {
				return nil, fmt.Errorf("in %%op slot type: %w", err)
			}
			p.skipBlankInline()
		}
		slots = append(slots, opSlot{Name: name, Type: typeName})
		if p.atEnd() {
			return nil, fmt.Errorf("unterminated %%op slot list at %s", p.cur)
		}
		switch p.peek(0) {
		case ',':
			p.advance()
		case ')':
			return slots, nil
		default:
			return nil, fmt.Errorf("expected ',' or ')' in %%op slot list at %s", p.cur)
		}
	}
}

// buildTomSymbol is the no-hook variant of [buildTomSymbolWithHooks];
// preserved for callers that don't (yet) parse the body.
func buildTomSymbol(name, codomain string, slots []opSlot) tomast.TomSymbol {
	return buildTomSymbolWithHooks(name, codomain, slots, nil)
}

// buildTomSymbolWithHooks assembles a [tomast.TomSymbol] for an
// operator declared via `%op codomain ctor(slots) { hooks }`. Mirrors
// `ASTFactory.makeSymbol(opName, codomain, types, pairNameDecls,
// options)` with each hook lifted to a
// `DeclarationToOption(<Decl>)` entry of the symbol's Options.
func buildTomSymbolWithHooks(name, codomain string, slots []opSlot, hooks []tomast.Declaration) tomast.TomSymbol {
	domain := make([]tomast.TomType, 0, len(slots))
	pairs := make([]tomast.PairNameDecl, 0, len(slots))
	for _, s := range slots {
		t := makeTomType(s.Type)
		domain = append(domain, t)
		pairs = append(pairs, tomast.MakePairNameDecl(
			tomast.MakeName(s.Name),
			tomast.MakeEmptyDeclaration(),
		))
	}
	options := make([]tomast.Option, 0, len(hooks))
	for _, h := range hooks {
		options = append(options, tomast.MakeDeclarationToOption(h))
	}
	return tomast.MakeSymbol(
		tomast.MakeName(name),
		tomast.MakeTypesToType(
			tomast.MakeConcTomType(domain...),
			makeTomType(codomain),
		),
		tomast.MakeConcPairNameDecl(pairs...),
		tomast.MakeConcOption(options...),
	)
}

// parseOpHooks consumes the body of `%op` / `%oplist` / `%oparray`
// (between `{` and `}`), returning one [tomast.Declaration] per
// recognised hook. `slots` is the parallel slot list from the
// operator's signature, used to type the BQVariable wrappers around
// hook argument names that correspond to slot positions.
//
// `isVariadic` distinguishes the `%op` body (false) from the
// `%oplist`/`%oparray` body (true); the recognised hook names overlap
// but their AST shapes differ. Unknown hooks are silently consumed.
//
// Per Java's AstBuilder.java Cst_OpConstruct branch, hooks emit (in
// reverse source order):
//   - is_fsym(t) { … }           → IsFsymDecl
//   - get_slot(slot, t) { … }    → GetSlotDecl
//   - get_default(slot) { … }    → GetDefaultDecl
//   - make(a1, a2, …) { … }      → MakeDecl (only for non-variadic)
//   - equals(t1, t2) { … }       → EqualTermDecl
//
// For `%oplist`/`%oparray` (`isVariadic=true`), additionally:
//   - make_empty()       → MakeEmptyList / MakeEmptyArray
//   - make_empty(n)      → MakeEmptyArray (array form)
//   - make_append(e,l)   → MakeAddList / MakeAddArray
//   - make_insert(e,l)   → MakeAddArray (or MakeAddList depending on
//                          subkind; we map to MakeAddArray for now)
//   - get_head(l)        → GetHeadDecl
//   - get_tail(l)        → GetTailDecl
//   - is_empty(l)        → IsEmptyDecl
//   - get_size(l)        → GetSizeDecl
//   - get_element(l, n)  → GetElementDecl
func (p *parser) parseOpHooks(opName, codomain string, slots []opSlot, isVariadic bool) ([]tomast.Declaration, error) {
	var hooks []tomast.Declaration
	for {
		p.skipWhitespaceAndComments()
		if p.atEnd() || p.peek(0) == '}' {
			break
		}
		hookLine := p.cur.line
		hookName, err := p.readIdent()
		if err != nil {
			return nil, fmt.Errorf("in %%op body: %w", err)
		}
		p.skipBlankInline()
		args, err := p.parseHookArgList()
		if err != nil {
			return nil, err
		}
		p.skipBlankInline()
		if p.atEnd() || p.peek(0) != '{' {
			return nil, fmt.Errorf("expected '{' after hook %q at %s", hookName, p.cur)
		}
		body, err := p.captureHookBody()
		if err != nil {
			return nil, err
		}
		opOT := tomast.MakeOriginTracking(tomast.MakeName(opName), int64(hookLine), p.filename)

		switch hookName {
		case "is_fsym":
			if len(args) != 1 {
				return nil, fmt.Errorf("is_fsym expects 1 arg at line %d", hookLine)
			}
			hooks = append(hooks, tomast.MakeIsFsymDecl(
				tomast.MakeName(opName),
				makeHookBQVariable(args[0], codomain, hookLine, p.filename),
				tomast.MakeCode(abstractCode(body, args)),
				opOT,
			))
		case "get_slot":
			if len(args) != 2 {
				return nil, fmt.Errorf("get_slot expects 2 args at line %d", hookLine)
			}
			hooks = append(hooks, tomast.MakeGetSlotDecl(
				tomast.MakeName(opName),
				tomast.MakeName(args[0]),
				makeHookBQVariable(args[1], codomain, hookLine, p.filename),
				tomast.MakeCode(abstractCode(body, args[1:])),
				opOT,
			))
		case "get_default":
			if len(args) != 1 {
				return nil, fmt.Errorf("get_default expects 1 arg at line %d", hookLine)
			}
			hooks = append(hooks, tomast.MakeGetDefaultDecl(
				tomast.MakeName(opName),
				tomast.MakeName(args[0]),
				tomast.MakeCode(body),
				opOT,
			))
		case "make":
			if isVariadic {
				continue
			}
			if len(args) != len(slots) {
				return nil, fmt.Errorf("make(%d) doesn't match slot count %d at line %d", len(args), len(slots), hookLine)
			}
			bqArgs := make([]tomast.BQTerm, len(args))
			for i, a := range args {
				bqArgs[i] = tomast.MakeBQVariable(
					tomast.MakeConcOption(),
					tomast.MakeName(a),
					makeTomType(slots[i].Type),
				)
			}
			hooks = append(hooks, tomast.MakeMakeDecl(
				tomast.MakeName(opName),
				makeTomType(codomain),
				tomast.MakeConcBQTerm(bqArgs...),
				tomast.MakeExpressionToInstruction(tomast.MakeCode(abstractCode(body, args))),
				opOT,
			))
		case "equals":
			if len(args) != 2 {
				return nil, fmt.Errorf("equals expects 2 args at line %d", hookLine)
			}
			hooks = append(hooks, tomast.MakeEqualTermDecl(
				makeHookBQVariable(args[0], codomain, hookLine, p.filename),
				makeHookBQVariable(args[1], codomain, hookLine, p.filename),
				tomast.MakeCode(abstractCode(body, args)),
				opOT,
			))
		case "make_empty":
			if !isVariadic {
				continue
			}
			// list form is `make_empty()`, array form `make_empty(n)`.
			switch len(args) {
			case 0:
				hooks = append(hooks, tomast.MakeMakeEmptyList(
					tomast.MakeName(opName),
					tomast.MakeExpressionToInstruction(tomast.MakeCode(body)),
					opOT,
				))
			case 1:
				hooks = append(hooks, tomast.MakeMakeEmptyArray(
					tomast.MakeName(opName),
					makeHookBQVariable(args[0], "int", hookLine, p.filename),
					tomast.MakeExpressionToInstruction(tomast.MakeCode(abstractCode(body, args))),
					opOT,
				))
			}
		case "make_append", "make_insert":
			if !isVariadic || len(args) != 2 {
				continue
			}
			eltVar := makeHookBQVariable(args[0], codomain, hookLine, p.filename)
			listVar := makeHookBQVariable(args[1], codomain, hookLine, p.filename)
			instr := tomast.MakeExpressionToInstruction(tomast.MakeCode(abstractCode(body, args)))
			if hookName == "make_append" {
				hooks = append(hooks, tomast.MakeMakeAddList(
					tomast.MakeName(opName),
					eltVar,
					listVar,
					instr,
					opOT,
				))
			} else {
				hooks = append(hooks, tomast.MakeMakeAddArray(
					tomast.MakeName(opName),
					eltVar,
					listVar,
					instr,
					opOT,
				))
			}
		case "get_size":
			if !isVariadic || len(args) != 1 {
				continue
			}
			hooks = append(hooks, tomast.MakeGetSizeDecl(
				tomast.MakeName(opName),
				makeHookBQVariable(args[0], codomain, hookLine, p.filename),
				tomast.MakeCode(abstractCode(body, args)),
				opOT,
			))
		case "get_element":
			if !isVariadic || len(args) != 2 {
				continue
			}
			hooks = append(hooks, tomast.MakeGetElementDecl(
				tomast.MakeName(opName),
				makeHookBQVariable(args[0], codomain, hookLine, p.filename),
				makeHookBQVariable(args[1], "int", hookLine, p.filename),
				tomast.MakeCode(abstractCode(body, args)),
				opOT,
			))
		default:
			// Unknown hook — silently drop the body. Future phases
			// may grow recognisers (e.g. get_head, get_tail, is_empty).
		}
	}
	// Java's AstBuilder builds the list head-first (LIFO), so reverse
	// to match the order of the generated DeclarationToOption entries.
	reverseDeclarations(hooks)
	return hooks, nil
}

// makeTomType builds the canonical `Type(concTypeOption(), <name>,
// EmptyTargetLanguageType())` term used for codomain / domain types
// before the typer fills in the TLType from each sort's `implement`
// hook body.
func makeTomType(name string) tomast.TomType {
	if name == "" {
		return tomast.MakeEmptyType()
	}
	return tomast.MakeType(
		tomast.MakeConcTypeOption(),
		name,
		tomast.MakeEmptyTargetLanguageType(),
	)
}

// abstractCode is the Go port of
// `tom.engine.tools.ASTFactory.abstractCode(code, vars...)`: each
// occurrence of `$<var>` in `code` becomes `{<position>}` where
// position is the var's index in `vars`. Used by typeterm/op hooks
// so the host body becomes a positional template (e.g. `($l1).
// equals($l2)` → `({0}).equals({1})`).
func abstractCode(code string, vars []string) string {
	for i, v := range vars {
		code = strings.ReplaceAll(code, "$"+v, "{"+fmt.Sprintf("%d", i)+"}")
	}
	return code
}

// makeHookBQVariable wraps a hook argument name into a BQVariable
// typed at the surrounding codomain — the shape Java's
// makeBQVariableFromName emits in AstBuilder.java for hook bodies.
func makeHookBQVariable(varName, codomain string, line int, filename string) tomast.BQTerm {
	return tomast.MakeBQVariable(
		tomast.MakeConcOption(
			tomast.MakeOriginTracking(tomast.MakeName(varName), int64(line), filename),
		),
		tomast.MakeName(varName),
		tomast.MakeType(
			tomast.MakeConcTypeOption(),
			codomain,
			tomast.MakeEmptyTargetLanguageType(),
		),
	)
}

// skipWhitespaceAndComments advances past any whitespace, // line
// comments, and /* … */ block comments — enough to read hook
// declarations cleanly even when the user separates them with
// host-language comments.
func (p *parser) skipWhitespaceAndComments() {
	for !p.atEnd() {
		c := p.peek(0)
		if c == ' ' || c == '\t' || c == '\r' || c == '\n' {
			p.advance()
			continue
		}
		if c == '/' && p.peek(1) == '/' {
			for !p.atEnd() && p.peek(0) != '\n' {
				p.advance()
			}
			continue
		}
		if c == '/' && p.peek(1) == '*' {
			p.advance()
			p.advance()
			for !p.atEnd() && !(p.peek(0) == '*' && p.peek(1) == '/') {
				p.advance()
			}
			if !p.atEnd() {
				p.advance()
				p.advance()
			}
			continue
		}
		return
	}
}

// parseOp handles `%op SORT CTOR '(' slotList? ')' '{' BODY '}'`. The
// AST output keeps just `SymbolDecl(Name(ctor))`; the codomain and
// slot signature is recorded on the parser's signature side-channel
// (returned via [ParseAll]) so downstream plugins (Desugarer's
// replaceTermAppl, Typer's TransformBQAppl, …) can look the symbol up
// by name and behave the same way the Java reference does after
// updating its SymbolTable.
func (p *parser) parseOp() error {
	if !p.matchKeyword("%op") {
		return fmt.Errorf("expected %%op at %s", p.cur)
	}
	p.skipBlankInline()
	codomain, err := p.readIdent()
	if err != nil {
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
	slots, err := p.parseOpSlotList()
	if err != nil {
		return err
	}
	if p.atEnd() || p.peek(0) != ')' {
		return fmt.Errorf("expected ')' to close operator slot list at %s", p.cur)
	}
	p.advance() // ')'
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) != '{' {
		return fmt.Errorf("expected '{' after %%op signature at %s", p.cur)
	}
	p.advance() // '{'
	hookDecls, err := p.parseOpHooks(ctor, codomain, slots, false)
	if err != nil {
		return err
	}
	if p.atEnd() || p.peek(0) != '}' {
		return fmt.Errorf("expected '}' to close %%op body at %s", p.cur)
	}
	p.advance() // '}'
	p.sig.Symbols[ctor] = buildTomSymbolWithHooks(ctor, codomain, slots, hookDecls)
	p.codes = append(p.codes, tomast.MakeDeclarationToCode(tomast.MakeSymbolDecl(
		tomast.MakeName(ctor),
	)))
	return nil
}

// parseVariadicOp handles both `%oplist` (isList=true) and `%oparray`
// (isList=false). Grammar: `'%oplist'|'%oparray' SORT CTOR '(' DOMAIN '*' ')'
// '{' BODY '}'`.
//
// The AST gets `ListSymbolDecl(Name(ctor))` or `ArraySymbolDecl(...)`,
// matching the Java reference. The signature side-channel records the
// constructor in [signature.Symbols] with a synthetic `MakeEmptyList`
// (or `MakeEmptyArray`) option — that's the marker Java's
// `TomBase.isListOperator` / `isArrayOperator` look for. The codomain
// keeps a `WithSymbol(ctor)` TypeOption when the typer rewrites
// through one of these symbols.
func (p *parser) parseVariadicOp(isList bool) error {
	kw := "%oparray"
	if isList {
		kw = "%oplist"
	}
	if !p.matchKeyword(kw) {
		return fmt.Errorf("expected %s at %s", kw, p.cur)
	}
	p.skipBlankInline()
	codomain, err := p.readIdent()
	if err != nil {
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
	elemType, err := p.readIdent()
	if err != nil {
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
	if p.atEnd() || p.peek(0) != '{' {
		return fmt.Errorf("expected '{' after variadic %%op signature at %s", p.cur)
	}
	p.advance() // '{'
	// Variadic ops share most hook names with regular %op but use
	// list/array-specific shapes (MakeAddList vs MakeAddArray, etc.).
	// We construct a synthetic single-slot list so make_append /
	// get_element can resolve their var names.
	hookSlots := []opSlot{{Name: "_", Type: elemType}}
	hookDecls, err := p.parseOpHooks(ctor, codomain, hookSlots, true)
	if err != nil {
		return err
	}
	if p.atEnd() || p.peek(0) != '}' {
		return fmt.Errorf("expected '}' to close variadic %%op body at %s", p.cur)
	}
	p.advance() // '}'

	p.sig.Symbols[ctor] = buildVariadicTomSymbolWithHooks(ctor, codomain, elemType, isList, hookDecls)

	var decl tomast.Declaration
	if isList {
		decl = tomast.MakeListSymbolDecl(tomast.MakeName(ctor))
	} else {
		decl = tomast.MakeArraySymbolDecl(tomast.MakeName(ctor))
	}
	p.codes = append(p.codes, tomast.MakeDeclarationToCode(decl))
	return nil
}

// buildVariadicTomSymbol assembles a [tomast.TomSymbol] for an
// `%oplist` / `%oparray` declaration. The Options list carries a
// synthetic marker — `DeclarationToOption(MakeEmptyList(...))` or
// `DeclarationToOption(MakeEmptyArray(...))` — so downstream Java-
// compatible [TomBase.isListOperator] / `isArrayOperator` checks pass
// when we lift the SymbolTable into a Java-like form.
func buildVariadicTomSymbol(name, codomain, elemType string, isList bool) tomast.TomSymbol {
	return buildVariadicTomSymbolWithHooks(name, codomain, elemType, isList, nil)
}

// buildVariadicTomSymbolWithHooks is the hook-aware variant. When
// `hooks` is empty, falls back to the synthetic MakeEmpty marker so
// `TomBase.isListOperator`/`isArrayOperator` still resolve correctly.
// When hooks are present, the real MakeEmptyList/MakeEmptyArray
// emitted by [parseOpHooks] takes over as the marker.
func buildVariadicTomSymbolWithHooks(name, codomain, elemType string, isList bool, hooks []tomast.Declaration) tomast.TomSymbol {
	domain := tomast.MakeConcTomType(makeTomType(elemType))
	pairs := tomast.MakeConcPairNameDecl()
	options := make([]tomast.Option, 0, len(hooks)+1)
	hasEmptyMarker := false
	for _, h := range hooks {
		switch h.(type) {
		case *tomast.MakeEmptyListDeclaration, *tomast.MakeEmptyArrayDeclaration:
			hasEmptyMarker = true
		}
		options = append(options, tomast.MakeDeclarationToOption(h))
	}
	if !hasEmptyMarker {
		var marker tomast.Declaration
		if isList {
			marker = tomast.MakeMakeEmptyList(
				tomast.MakeName(name),
				tomast.MakeNop(),
				tomast.MakeOriginTracking(tomast.MakeName(name), 0, "unknown file"),
			)
		} else {
			marker = tomast.MakeMakeEmptyArray(
				tomast.MakeName(name),
				makeHookBQVariable("_", codomain, 0, "unknown file"),
				tomast.MakeNop(),
				tomast.MakeOriginTracking(tomast.MakeName(name), 0, "unknown file"),
			)
		}
		options = append(options, tomast.MakeDeclarationToOption(marker))
	}
	return tomast.MakeSymbol(
		tomast.MakeName(name),
		tomast.MakeTypesToType(domain, makeTomType(codomain)),
		pairs,
		tomast.MakeConcOption(options...),
	)
}

// IncludeSearchPath holds extra directories the parser walks when
// resolving a `%include { foo.tom }` directive whose path is not
// found relative to the including file. Callers append the Tom
// standard-mapping directories here before invoking [Parse] — the
// Go counterpart of Java's TomStreamManager.getImportList(), which
// pulls $TOM_HOME/share/tom and $TOM_HOME/share/tom/<lang> at
// runtime. Order matters: the first hit wins.
//
// As a convenience for tooling that doesn't manage this slice
// directly, the parser also consults the colon-separated
// $TOMGO_TOM_INCLUDE environment variable.
var IncludeSearchPath []string

// resolveIncludePath tries, in order:
//
//  1. `includePath` interpreted as an absolute path or relative to
//     `sourceDir` (the directory of the including file);
//  2. each entry of [IncludeSearchPath];
//  3. each colon-separated entry of $TOMGO_TOM_INCLUDE.
//
// The first existing file wins. Java's getImportList does roughly
// the same walk minus the env-var step, which is the workflow knob
// we add for command-line tools.
func resolveIncludePath(includePath, sourceDir string) (string, error) {
	candidates := []string{}
	if filepath.IsAbs(includePath) {
		candidates = append(candidates, includePath)
	} else {
		candidates = append(candidates, filepath.Join(sourceDir, includePath))
		// Java's TomStreamManager.getImportList prepends the
		// `<destdir>/<source-Java-package>` location so includes
		// produced by Gom land where the surrounding .t expects
		// them. We mirror that heuristically: for each
		// IncludeSearchPath entry, also try appending the source
		// dir's last segment (typically the Java package, e.g.
		// `gom` for `test/gom/TestBool.t`) BEFORE the include path
		// so `%include { bool/Bool.tom }` resolves to
		// `test/gen/gom/bool/Bool.tom`.
		pkgGuess := filepath.Base(sourceDir)
		for _, dir := range IncludeSearchPath {
			candidates = append(candidates,
				filepath.Join(dir, pkgGuess, includePath),
				filepath.Join(dir, includePath),
			)
		}
		if extra := os.Getenv("TOMGO_TOM_INCLUDE"); extra != "" {
			for _, dir := range strings.Split(extra, string(os.PathListSeparator)) {
				if dir != "" {
					candidates = append(candidates, filepath.Join(dir, includePath))
				}
			}
		}
	}
	for _, c := range candidates {
		if abs, err := filepath.Abs(c); err == nil {
			if _, err := os.Stat(abs); err == nil {
				return abs, nil
			}
		}
	}
	return "", fmt.Errorf("could not resolve %%include %q (searched %v)", includePath, candidates)
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

	resolved, err := resolveIncludePath(includePath, filepath.Dir(p.filename))
	if err != nil {
		return err
	}
	// Java's TomStreamManager.alreadyParsedIncludedFiles dedup: a
	// second %include of the same resolved path (anywhere in this
	// compilation unit) collapses to an empty TomInclude. This is
	// what RuleBool.tom's duplicate `%include {aterm.tom}` lines
	// (and the many transitive includes of e.g. char.tom) emit.
	if p.alreadyParsed != nil && (*p.alreadyParsed)[resolved] {
		p.codes = append(p.codes, tomast.MakeTomInclude(tomast.MakeConcCode()))
		return nil
	}
	if p.alreadyParsed != nil {
		(*p.alreadyParsed)[resolved] = true
	}
	src, err := os.ReadFile(resolved)
	if err != nil {
		return fmt.Errorf("read %%include %q: %w", resolved, err)
	}
	chain := append(append([]string(nil), p.includeChain...), p.filename)
	subResult, err := parseAllWithChainAndSet(string(src), resolved, chain, p.alreadyParsed)
	if err != nil {
		return fmt.Errorf("parse %%include %q: %w", resolved, err)
	}
	// Merge the included file's signature data into our own so the
	// outer compilation can resolve sorts/operators declared via the
	// %include. Java's TomStreamManager-shared SymbolTable does this
	// implicitly; we do it explicitly through the parser-internal
	// signature struct.
	for k, v := range subResult.Sorts {
		if _, exists := p.sig.Sorts[k]; !exists {
			p.sig.Sorts[k] = v
		}
	}
	for k, v := range subResult.Symbols {
		if _, exists := p.sig.Symbols[k]; !exists {
			p.sig.Symbols[k] = v
		}
	}
	// subResult.Code is Tom(concCode(c1, c2, …)). Extract the inner
	// codes.
	subTom, ok := subResult.Code.(*tomast.TomCode)
	if !ok {
		return fmt.Errorf("%%include %q produced unexpected AST root: %T", resolved, subResult.Code)
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
	// `%match` accepts an optional parens-delimited subject list. When
	// absent (the constraint-action form, TomIslandParser.g4:49), all
	// rule constraints must be explicit (`pat << bqterm`) — the
	// per-slot count check in parseActionRule is then skipped.
	var subjects []tomast.BQTerm
	if !p.atEnd() && p.peek(0) == '(' {
		p.advance() // '('
		var err error
		subjects, err = p.parseSubjectList()
		if err != nil {
			return err
		}
		if p.atEnd() || p.peek(0) != ')' {
			return fmt.Errorf("expected ')' to close %%match subjects at %s", p.cur)
		}
		p.advance() // ')'
		p.skipBlankInline()
	}
	if p.atEnd() || p.peek(0) != '{' {
		return fmt.Errorf("expected '{' to open %%match body at %s", p.cur)
	}
	p.advance() // '{'

	var rules []tomast.ConstraintInstruction
	for {
		p.skipWhitespaceAndComments()
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

// parseSubject parses one subject of a %match. Two forms supported:
//
//   - `var`        → `BQVariable(opts, Name(var), unknownType())`
//   - `type var`   → `BQVariable(opts, Name(var), Type(type))`
//
// The optional `type` prefix is the codomain annotation from the
// ANTLR grammar (TomIslandParser.g4:123-126 — `codomain=ID? var=ID`).
// AstBuilder.java's makeBQVariableFromName then builds the BQVariable
// with the codomain attached as its AstType. opts carries
// OriginTracking + the default ModuleName, matching the Java reference.
func (p *parser) parseSubject() (tomast.BQTerm, error) {
	firstLine := p.cur.line
	// Constant subjects (`%match(int 5)`, `%match(5)`): if the cursor
	// is on a digit, '-', single quote or `"`, treat as a bare bqterm
	// constant and delegate to parseBQTerm via the constant route.
	// parseBQTerm currently lacks a constant case, so we hand-roll:
	if !p.atEnd() && (isDigit(p.peek(0)) || p.peek(0) == '-' || p.peek(0) == '"' || p.peek(0) == '\'') {
		return p.parseBQTermConstant()
	}
	first, err := p.readIdent()
	if err != nil {
		return nil, err
	}
	// Lookahead: if the next non-blank byte starts an identifier, the
	// first ID was a codomain. Then the second part can be a plain var
	// OR a constant (e.g. `int 5`).
	p.skipBlankInline()
	if !p.atEnd() && (isDigit(p.peek(0)) || p.peek(0) == '-' || p.peek(0) == '"' || p.peek(0) == '\'') {
		bq, err := p.parseBQTermConstant()
		if err != nil {
			return nil, err
		}
		// Java's parser stores the codomain `first` (e.g. `int` in
		// `%match(int 5)`) so the typer later sets the matching
		// constraint's aType. We don't have a slot on BQAppl, so we
		// stash the codomain in a parser-side table keyed by the
		// returned subject. parseActionRule consults it when picking
		// MatchConstraint.aType.
		if p.subjectCodomains != nil {
			p.subjectCodomains[bq] = first
		}
		return bq, nil
	}
	if !p.atEnd() && isIdentStart(p.peek(0)) {
		// Both first and second are identifiers; might be `type var`
		// (BQVariable) or `type fsym(args)` (typed BQAppl). Peek past
		// `second` for `(`.
		save := p.idx
		saveCur := p.cur
		varLine := p.cur.line
		second, err := p.readIdent()
		if err != nil {
			return nil, err
		}
		p.skipBlankInline()
		if !p.atEnd() && p.peek(0) == '(' {
			// `type fsym(args)` — typed application. Rewind to `second`
			// and let parseBQTerm parse the application. We drop the
			// `first` codomain for now; Java's typer would recover it.
			p.idx = save
			p.cur = saveCur
			_ = first
			return p.parseBQTerm(false)
		}
		options := tomast.MakeConcOption(
			tomast.MakeOriginTracking(tomast.MakeName(second), int64(varLine), p.filename),
			tomast.MakeModuleName("default"),
		)
		return tomast.MakeBQVariable(
			options,
			tomast.MakeName(second),
			tomast.MakeType(
				tomast.MakeConcTypeOption(),
				first,
				tomast.MakeEmptyTargetLanguageType(),
			),
		), nil
	}
	if !p.atEnd() && p.peek(0) == '(' {
		// `fsym(args)` subject with no codomain — pure application.
		// Rewind one ID and let parseBQTerm handle it.
		p.idx -= len(first)
		p.cur.col -= len(first)
		return p.parseBQTerm(false)
	}
	options := tomast.MakeConcOption(
		tomast.MakeOriginTracking(tomast.MakeName(first), int64(firstLine), p.filename),
		tomast.MakeModuleName("default"),
	)
	return tomast.MakeBQVariable(options, tomast.MakeName(first), unknownType()), nil
}

// parseBQTermConstant reads a numeric / string / char constant. In
// non-composite contexts (subject of `%match`, RHS of `<<`) it emits a
// bare `BQAppl(opts, Name(literal), concBQTerm())` matching Java's
// AstBuilder for constants in bqterm position. In composite contexts
// (action-body backquotes, top-level bqcomposite) constants are
// wrapped in `Composite(CompositeBQTerm(BuildConstant(Name(literal))))`,
// per Java's CstBuilder.exitComposite. The composite flag is threaded
// through parseBQTermArgList.
func (p *parser) parseBQTermConstant() (tomast.BQTerm, error) {
	return p.parseBQTermConstantWith(false)
}

// parseBQTermConstantWith is the composite-aware variant of
// [parseBQTermConstant]. When `composite` is true, the result is
// `Composite(CompositeBQTerm(BuildConstant(Name(literal))))` —
// Java's CstBuilder.exitComposite shape for constants embedded in
// backquote-composite contexts (action body backquotes, top-level
// bqcomposite islands). Otherwise emit a bare `BQAppl` matching
// AstBuilder's pattern for `%match(int 5)` and similar.
func (p *parser) parseBQTermConstantWith(composite bool) (tomast.BQTerm, error) {
	startLine := p.cur.line
	var literal string
	switch {
	case isDigit(p.peek(0)) || (p.peek(0) == '-' && isDigit(p.peek(1))):
		var sb strings.Builder
		if p.peek(0) == '-' {
			sb.WriteByte(p.advance())
		}
		for !p.atEnd() && isDigit(p.peek(0)) {
			sb.WriteByte(p.advance())
		}
		if !p.atEnd() && p.peek(0) == '.' && isDigit(p.peek(1)) {
			sb.WriteByte(p.advance())
			for !p.atEnd() && isDigit(p.peek(0)) {
				sb.WriteByte(p.advance())
			}
		}
		if !p.atEnd() {
			c := p.peek(0)
			if c == 'l' || c == 'L' || c == 'f' || c == 'F' || c == 'd' || c == 'D' {
				sb.WriteByte(p.advance())
			}
		}
		literal = sb.String()
	case p.peek(0) == '"' || p.peek(0) == '\'':
		quote := p.peek(0)
		var sb strings.Builder
		sb.WriteByte(p.advance()) // opening
		for !p.atEnd() && p.peek(0) != quote {
			c := p.advance()
			sb.WriteByte(c)
			if c == '\\' && !p.atEnd() {
				sb.WriteByte(p.advance())
			}
		}
		if p.atEnd() {
			return nil, fmt.Errorf("unterminated string/char constant at %s", p.cur)
		}
		sb.WriteByte(p.advance()) // closing
		literal = sb.String()
	default:
		return nil, fmt.Errorf("expected constant at %s", p.cur)
	}
	if composite {
		// Composite context: wrap in
		// Composite(CompositeBQTerm(BuildConstant(Name(literal)))).
		return tomast.MakeComposite(
			tomast.MakeCompositeBQTerm(
				tomast.MakeBuildConstant(tomast.MakeName(literal)),
			),
		), nil
	}
	_ = startLine
	options := tomast.MakeConcOption(
		tomast.MakeOriginTracking(tomast.MakeName(literal), int64(startLine), p.filename),
		tomast.MakeModuleName("default"),
	)
	return tomast.MakeBQAppl(options, tomast.MakeName(literal), tomast.MakeConcBQTerm()), nil
}

// parseBQTerm parses one backquote term. The grammar (TomIslandParser.g4:122-
// 128 for the plain `bqterm` form) accepts an optional leading `` ` `` followed
// by either:
//   - `ID '(' (bqterm (',' bqterm)*)? ')'`  → `BQAppl(opts, Name(ID), bqList)`
//   - `ID '*'?`                             → `BQVariable(opts, Name(ID), unknownType)`
//                                             (or `BQVariableStar` if `*` — TODO)
//
// The `composite` flag toggles between the two grammars Java distinguishes
// (`bqterm` for `<<` RHS / `%match(...)` subjects vs `bqcomposite` / `composite`
// for action-body backquotes):
//
//   - composite=false : plain bqterm — args of an inner application stay as
//                       BQVariable/BQAppl regardless of explicit `` ` `` on
//                       them. Used in `<<` RHS (4.F.13).
//   - composite=true  : an arg starting with `` ` `` is wrapped in
//                       `Composite(CompositeTL(ITL("` `")), CompositeBQTerm(<term>))`,
//                       mirroring Java's exitComposite (CstBuilder.java:386-
//                       399) → Cst_BQComposite → AstBuilder.java:583-595.
//                       Used in action bodies (4.F.14+).
//
// Per AstBuilder.java:551-570, the option list carries an `OriginTracking`
// for the symbol/variable name plus the default `ModuleName("default")` —
// same shape as the parens-subject of `%match(...)`.
//
// Limits for this jet: no codomain `ID:Type` prefix, no implicit-args records
// `Foo[a=b]`, no `BQVariableStar`.
func (p *parser) parseBQTerm(composite bool) (tomast.BQTerm, error) {
	startLine := p.cur.line
	// Java's parser emits OriginTracking only when the bqterm was
	// introduced by a leading backquote (CstBuilder paths via
	// `BQUOTE`). Bare bqterms parsed in constraint/arg contexts (no
	// `\``) keep just `concOption(ModuleName("default"))` — see e.g.
	// `g(x) == tt` in regress/SuccessiveErrors.t at the parsed phase.
	hasBackquote := false
	if !p.atEnd() && p.peek(0) == '`' {
		p.advance() // '`'
		p.skipBlankInline()
		hasBackquote = true
	}
	// Numeric / string / char constants — grammar's `codomain=ID?
	// constant` alt (TomIslandParser.g4:126). Emit a bare BQAppl
	// whose Name is the literal text (same shape parsePattern's
	// constant branch uses). A leading `-` followed by a digit is
	// the negative-integer constant.
	if !p.atEnd() && (isDigit(p.peek(0)) || p.peek(0) == '"' || p.peek(0) == '\'' ||
		(p.peek(0) == '-' && isDigit(p.peek(1)))) {
		return p.parseBQTermConstantWith(composite)
	}
	// Parenthesised composite (no fsym), e.g. `` `(x) ``. The Java
	// grammar (TomIslandParser.g4:143) accepts `LPAREN composite*?
	// RPAREN` as one of the `composite` alternatives, reached when
	// the leading BQUOTE is followed by `(`. The emitted AST is a
	// `Composite(CompositeTL("("), CompositeBQTerm(inner)*,
	// CompositeTL(")"))` — Java keeps the parens textually inside the
	// Composite list rather than dropping them.
	if !p.atEnd() && p.peek(0) == '(' {
		p.advance() // '('
		items := []tomast.CompositeMember{
			tomast.MakeCompositeTL(tomast.MakeITL("(")),
		}
		for {
			p.skipBlankInline()
			if p.atEnd() {
				return nil, fmt.Errorf("unterminated `(...) at %s", p.cur)
			}
			if p.peek(0) == ')' {
				break
			}
			inner, err := p.parseBQTerm(composite)
			if err != nil {
				return nil, err
			}
			items = append(items, tomast.MakeCompositeBQTerm(inner))
			p.skipBlankInline()
			if !p.atEnd() && p.peek(0) == ',' {
				p.advance() // ','
			}
		}
		p.advance() // ')'
		items = append(items, tomast.MakeCompositeTL(tomast.MakeITL(")")))
		return tomast.MakeComposite(items...), nil
	}
	if p.atEnd() || !isIdentStart(p.peek(0)) {
		return nil, fmt.Errorf("expected bqterm identifier at %s", p.cur)
	}
	name, err := p.readIdent()
	if err != nil {
		return nil, err
	}
	// Codomain annotation: `codomain=ID? var=ID` (TomIslandParser.g4:123-125).
	// If a second identifier follows the first (with no application/star
	// punctuation between them), the first ID is the codomain.
	codomain := ""
	{
		save := p.idx
		saveCur := p.cur
		p.skipBlankInline()
		if !p.atEnd() && isIdentStart(p.peek(0)) {
			codomain = name
			name, err = p.readIdent()
			if err != nil {
				return nil, err
			}
		} else {
			p.idx = save
			p.cur = saveCur
		}
	}
	options := tomast.MakeConcOption(
		tomast.MakeOriginTracking(tomast.MakeName(name), int64(startLine), p.filename),
		tomast.MakeModuleName("default"),
	)
	_ = hasBackquote
	save := p.idx
	saveCur := p.cur
	p.skipBlankInline()
	if !p.atEnd() && p.peek(0) == '(' {
		p.advance() // '('
		args, err := p.parseBQTermArgList(composite)
		if err != nil {
			return nil, err
		}
		if p.atEnd() || p.peek(0) != ')' {
			return nil, fmt.Errorf("expected ')' to close bqterm application at %s", p.cur)
		}
		p.advance() // ')'
		return tomast.MakeBQAppl(options, tomast.MakeName(name), tomast.MakeConcBQTerm(args...)), nil
	}
	if !p.atEnd() && p.peek(0) == '*' {
		p.advance() // '*'
		return tomast.MakeBQVariableStar(options, tomast.MakeName(name), makeBQVarType(codomain)), nil
	}
	p.idx = save
	p.cur = saveCur
	return tomast.MakeBQVariable(options, tomast.MakeName(name), makeBQVarType(codomain)), nil
}

// makeBQVarType returns the AstType of a BQVariable: a concrete
// `Type(codomain, EmptyTL)` when the bqterm carries a `codomain=ID`
// prefix, otherwise the `unknown type` placeholder.
func makeBQVarType(codomain string) tomast.TomType {
	if codomain == "" {
		return unknownType()
	}
	return tomast.MakeType(
		tomast.MakeConcTypeOption(),
		codomain,
		tomast.MakeEmptyTargetLanguageType(),
	)
}

// parseBQTermArgList consumes `bqterm (',' bqterm)*` between '(' and ')'.
// When composite=true, each arg starting with `` ` `` is wrapped in a
// Composite(CompositeTL(ITL("` `")), CompositeBQTerm(<term>)) — see
// parseBQTerm's docstring.
func (p *parser) parseBQTermArgList(composite bool) ([]tomast.BQTerm, error) {
	var args []tomast.BQTerm
	p.skipBlankInline()
	if !p.atEnd() && p.peek(0) == ')' {
		return args, nil
	}
	for {
		hadBackquote := composite && !p.atEnd() && p.peek(0) == '`'
		// Java's grammar (TomIslandParser.g4) accepts a bare `_` as a
		// dedicated `default` bqterm arg, which AstBuilder lowers to
		// `BQDefault()`. We detect that here so backquote
		// applications like `\`f(_)` produce a BQDefault instead of a
		// BQVariable named "_". A `_` followed by `*` is the
		// variable-star wildcard (handled by parseBQTerm), not the
		// default-value placeholder.
		if !p.atEnd() && p.peek(0) == '_' && !isIdentChar(p.peek(1)) && p.peek(1) != '*' {
			p.advance() // '_'
			args = append(args, tomast.MakeBQDefault())
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
			continue
		}
		bq, err := p.parseBQTerm(composite)
		if err != nil {
			return nil, err
		}
		if hadBackquote {
			bq = tomast.MakeComposite(
				tomast.MakeCompositeTL(tomast.MakeITL("`")),
				tomast.MakeCompositeBQTerm(bq),
			)
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
	// subjects may be empty when %match has no parens (constraint-action
	// form, TomIslandParser.g4:49). In that case every slot must carry
	// an explicit '<<' subject — the count check after the loop enforces.
	ruleLine := p.cur.line

	// Constraint-action form: whenever the lookahead spots a `&&`,
	// `||`, `!=`, `==`, `<`, `>` (or `<<` past the first slot) before
	// the rule's `->`, route the whole LHS through the constraint
	// expression parser (TomIslandParser.g4:75-77 / 73). The simpler
	// patternlist alternative below stays for the common
	// `pat (, pat)* -> body` case.
	if p.looksLikeConstraintAction() {
		c, err := p.parseConstraintExpr()
		if err != nil {
			return nil, err
		}
		return p.finishConstraintRule(c, ruleLine)
	}

	// Each entry of `patterns` may carry an optional explicit subject from a
	// `pattern '<<' bqterm` form. If present, it overrides the i-th implicit
	// subject from the %match parens (cf. AstBuilder.java:683-698 —
	// Cst_MatchTermConstraint vs Cst_MatchArgumentConstraint).
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
			bq, err := p.parseBQTerm(false)
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
		if (p.peek(0) == '&' && p.peek(1) == '&') ||
			(p.peek(0) == '|' && p.peek(1) == '|') {
			// `patternlist (AND|OR) constraint` — Java AstBuilder
			// alternative 1. We've finished the patternlist; the
			// rest is a constraint expression to combine with the
			// implicit MatchConstraints. Switch to constraint mode
			// and capture the extension expression here.
			extra, err := p.parseActionRuleConstraintTail()
			if err != nil {
				return nil, err
			}
			return p.finishHybridRule(slots, subjects, extra, ruleLine)
		}
		break
	}
	// Count check only when the %match has implicit subjects (parens
	// form). For the constraint-action form (no parens, all slots use
	// explicit `<<` constraints), we skip the check — Java's grammar
	// allows any number of constraints there.
	allExplicit := true
	for _, s := range slots {
		if s.explicit == nil {
			allExplicit = false
			break
		}
	}
	if !allExplicit && len(slots) != len(subjects) {
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
		var subj tomast.BQTerm
		switch {
		case slot.explicit != nil:
			subj = slot.explicit
		case i < len(subjects):
			subj = subjects[i]
		default:
			return nil, fmt.Errorf("action-rule slot %d has neither implicit subject nor explicit '<<' at %s", i, p.cur)
		}
		// Java's AstBuilder derives the MatchConstraint's aType from
		// the subject's declared codomain — both for implicit subjects
		// (the `%match(<type> tt)` form) and for explicit ones
		// (`pat << <type> tt`), as seen in
		// regress/UndeclaredExplicityType.t's parsed output. The aType
		// then lets the typer propagate concrete sorts into pattern
		// Variables further down.
		aType := unknownType()
		if t := bqVarTermType(subj); t != nil {
			aType = t
		} else if name, ok := p.subjectCodomains[subj]; ok && name != "" {
			// Constant subjects like `%match(int 5)`: parseSubject
			// stashed the codomain so we can rebuild Type(_, name,
			// EmptyTL) here.
			aType = tomast.MakeType(
				tomast.MakeConcTypeOption(),
				name,
				tomast.MakeEmptyTargetLanguageType(),
			)
		}
		matchConstraints[i] = tomast.MakeMatchConstraint(slot.pat, subj, aType)
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

// parsePattern parses one pattern. Three optional outer wrappings are
// recognised, in this priority order:
//
//   - `ID '@' pattern`  → annotation. The grammar (TomIslandParser.g4:151)
//     puts the annotation name BEFORE `@` and the actual sub-pattern
//     AFTER. The result is the inner pattern with an `AliasTo(Variable(
//     Name(annotationName)))` added to its constraint list (cf.
//     AstBuilder.java:817-825 — Cst_AnnotatedPattern). For `x@a`, `x` is
//     the alias name and `a` is the sub-pattern.
//
//   - `'!' pattern`     → anti pattern, lowered to `AntiTerm(inner)`
//     (cf. AstBuilder.java:780-792 — Cst_Anti). The peek(1) != '='
//     guard avoids capturing `!=` (eventual numerical comparator).
//
//   - basePattern         → see parseBasePattern.
func (p *parser) parsePattern() (tomast.TomTerm, error) {
	// `ID '@' pattern` form — must be detected by lookahead (the leading ID
	// would otherwise be eaten by parseBasePattern as a Variable).
	if isIdentStart(p.peek(0)) {
		save := p.idx
		saveCur := p.cur
		annot, err := p.readIdent()
		if err == nil {
			p.skipBlankInline()
			if !p.atEnd() && p.peek(0) == '@' {
				p.advance() // '@'
				p.skipBlankInline()
				inner, err := p.parsePattern()
				if err != nil {
					return nil, fmt.Errorf("after '@': %w", err)
				}
				// AliasTo(Variable(concOption(OT(Name(annot),0,"unknown file")),
				//                  Name(annot), unknownType, concConstraint()))
				// Line=0 and file="unknown file" are the placeholders the Java
				// parser emits (ASTFactory.java:285); the typer fills them in.
				aliasVar := tomast.MakeVariable(
					tomast.MakeConcOption(tomast.MakeOriginTracking(
						tomast.MakeName(annot), 0, "unknown file",
					)),
					tomast.MakeName(annot),
					unknownType(),
					tomast.MakeConcConstraint(),
				)
				alias := tomast.MakeAliasTo(aliasVar)
				annotated, err := addPatternConstraint(inner, alias)
				if err != nil {
					return nil, fmt.Errorf("'@' annotation: %w", err)
				}
				return annotated, nil
			}
		}
		// Not an annotation — rewind and fall through to the base parser.
		p.idx = save
		p.cur = saveCur
	}

	if !p.atEnd() && p.peek(0) == '!' && p.peek(1) != '=' {
		p.advance() // '!'
		p.skipBlankInline()
		inner, err := p.parsePattern()
		if err != nil {
			return nil, fmt.Errorf("after '!': %w", err)
		}
		return tomast.MakeAntiTerm(inner), nil
	}
	return p.parseBasePattern()
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
	// Parenthesised pattern alternatives:
	//
	//   - OR-pattern head: `(F1|F2|…)` followed by explicit args
	//     `(...)` or implicit args `[...]`. CstBuilder.java:445-471
	//     routes this through Cst_Appl with a multi-element name list.
	//   - Anonymous operator: `(pat1, pat2, …)` — a parenthesised
	//     sequence with commas, used in sublist-style patterns
	//     (TestSublists.t). Emits a `TermAppl(opts, concTomName()
	//     (empty), concTomTerm(args), concConstraint())` — the
	//     missing name list flags this for the typer to fill in with
	//     the surrounding sort's unique operator.
	//
	// We disambiguate by lookahead: scan the body for `|` (OR) vs `,`
	// (anonymous) at depth 1 before either terminator.
	if p.peek(0) == '(' {
		if p.parenOpensAnonymousPattern() {
			return p.parseAnonymousOpPattern()
		}
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
		if p.atEnd() {
			return nil, fmt.Errorf("OR-pattern must be followed by an arg list at %s", p.cur)
		}
		// Explicit args `(...)` → TermAppl; implicit args `[...]` →
		// RecordAppl with a PairSlot list, including the empty `[]`
		// case used in test/Peano.t's `(zero|zero)[]`.
		switch p.peek(0) {
		case '(':
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
		case '[':
			p.advance() // '['
			slots, err := p.parsePatternSlotList()
			if err != nil {
				return nil, err
			}
			if p.atEnd() || p.peek(0) != ']' {
				return nil, fmt.Errorf("expected ']' to close OR-pattern record at %s", p.cur)
			}
			p.advance() // ']'
			return tomast.MakeRecordAppl(
				tomast.MakeConcOption(),
				tomast.MakeConcTomName(names...),
				tomast.MakeConcSlot(slots...),
				tomast.MakeConcConstraint(),
			), nil
		}
		return nil, fmt.Errorf("OR-pattern must be followed by '(' or '[' arg list at %s", p.cur)
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
	// Constant patterns: integer or string literal. Java's AstBuilder
	// emits these as a TermAppl whose Name is the literal text (no
	// args, no constraints) — see Cst_Constant in AstBuilder.java's
	// pattern dispatch.
	// Numeric constant pattern, optionally negative. We greedily consume
	// `-?[0-9]+` plus an optional Java suffix (l, L, f, F, d, D).
	if isDigit(p.peek(0)) || (p.peek(0) == '-' && isDigit(p.peek(1))) {
		var sb strings.Builder
		if p.peek(0) == '-' {
			sb.WriteByte(p.advance())
		}
		for !p.atEnd() && isDigit(p.peek(0)) {
			sb.WriteByte(p.advance())
		}
		// Optional fractional part `.NNN` (floats / doubles).
		if !p.atEnd() && p.peek(0) == '.' && isDigit(p.peek(1)) {
			sb.WriteByte(p.advance()) // '.'
			for !p.atEnd() && isDigit(p.peek(0)) {
				sb.WriteByte(p.advance())
			}
		}
		// Optional Java numeric-literal suffix (l, L, f, F, d, D).
		if !p.atEnd() {
			c := p.peek(0)
			if c == 'l' || c == 'L' || c == 'f' || c == 'F' || c == 'd' || c == 'D' {
				sb.WriteByte(p.advance())
			}
		}
		return tomast.MakeTermAppl(
			tomast.MakeConcOption(),
			tomast.MakeConcTomName(tomast.MakeName(sb.String())),
			tomast.MakeConcTomTerm(),
			tomast.MakeConcConstraint(),
		), nil
	}
	if p.peek(0) == '\'' {
		// Char literal `'x'` (or with backslash escapes inside).
		var sb strings.Builder
		sb.WriteByte(p.advance()) // opening '
		for !p.atEnd() && p.peek(0) != '\'' {
			c := p.advance()
			sb.WriteByte(c)
			if c == '\\' && !p.atEnd() {
				sb.WriteByte(p.advance())
			}
		}
		if p.atEnd() {
			return nil, fmt.Errorf("unterminated char constant at %s", p.cur)
		}
		sb.WriteByte(p.advance()) // closing '
		return tomast.MakeTermAppl(
			tomast.MakeConcOption(),
			tomast.MakeConcTomName(tomast.MakeName(sb.String())),
			tomast.MakeConcTomTerm(),
			tomast.MakeConcConstraint(),
		), nil
	}
	if p.peek(0) == '"' {
		var sb strings.Builder
		sb.WriteByte(p.advance()) // opening "
		for !p.atEnd() && p.peek(0) != '"' {
			c := p.advance()
			sb.WriteByte(c)
			// Naive escape: keep `\x` literally as Java keeps the
			// source representation in the constant's name.
			if c == '\\' && !p.atEnd() {
				sb.WriteByte(p.advance())
			}
		}
		if p.atEnd() {
			return nil, fmt.Errorf("unterminated string constant at %s", p.cur)
		}
		sb.WriteByte(p.advance()) // closing "
		return tomast.MakeTermAppl(
			tomast.MakeConcOption(),
			tomast.MakeConcTomName(tomast.MakeName(sb.String())),
			tomast.MakeConcTomTerm(),
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
	// Matching-theory markers (`?` associative, `??` associative+commutative)
	// attach to the application's option list. Java AstBuilder lifts
	// these into `MatchingTheory(concElementaryTheory(AU|AC))` options
	// on the resulting TermAppl/RecordAppl.
	var theoryOpt tomast.Option
	if !p.atEnd() && p.peek(0) == '?' {
		p.advance() // '?'
		if !p.atEnd() && p.peek(0) == '?' {
			p.advance() // second '?'
			theoryOpt = tomast.MakeMatchingTheory(
				tomast.MakeConcElementaryTheory(tomast.MakeAC()),
			)
		} else {
			theoryOpt = tomast.MakeMatchingTheory(
				tomast.MakeConcElementaryTheory(tomast.MakeAU()),
			)
		}
		p.skipBlankInline()
	}
	options := tomast.MakeConcOption()
	if theoryOpt != nil {
		options = tomast.MakeConcOption(theoryOpt)
	}
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
			options,
			tomast.MakeConcTomName(tomast.MakeName(name)),
			tomast.MakeConcTomTerm(args...),
			tomast.MakeConcConstraint(),
		), nil
	}
	if !p.atEnd() && p.peek(0) == '[' {
		// Record pattern `Foo[slot=value, …]` (TomIslandParser.g4's
		// `fsymbol implicitArgs`). The empty form `Foo[]` is also
		// legal — same desugaring target as `Foo()` (a RecordAppl
		// with an empty slot list).
		p.advance() // '['
		slots, err := p.parsePatternSlotList()
		if err != nil {
			return nil, err
		}
		if p.atEnd() || p.peek(0) != ']' {
			return nil, fmt.Errorf("expected ']' to close record pattern at %s", p.cur)
		}
		p.advance() // ']'
		return tomast.MakeRecordAppl(
			options,
			tomast.MakeConcTomName(tomast.MakeName(name)),
			tomast.MakeConcSlot(slots...),
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
	case *tomast.RecordApplTomTerm:
		return tomast.MakeRecordAppl(v.Options, v.NameList, v.Slots, cl), nil
	case *tomast.AntiTermTomTerm:
		// `name@!pat` — Java attaches AliasTo to the wrapped pattern
		// (AstBuilder.java keeps the constraint inside the AntiTerm's
		// payload). Recurse, then re-wrap in AntiTerm.
		inner, err := addPatternConstraint(v.TomTerm, c)
		if err != nil {
			return nil, err
		}
		return tomast.MakeAntiTerm(inner), nil
	default:
		return nil, fmt.Errorf("cannot attach '@' annotation to %T", pat)
	}
}

// parenOpensAnonymousPattern peeks past `(` and decides between an
// OR-pattern head (`(F1|F2)`) and an anonymous-operator pattern
// (`(pat1, pat2)`). Returns true when a top-level comma is found
// before any `|` — i.e. when the body is a comma-separated pattern
// list rather than a pipe-separated name list.
func (p *parser) parenOpensAnonymousPattern() bool {
	save := p.idx
	saveCur := p.cur
	defer func() { p.idx = save; p.cur = saveCur }()
	if p.peek(0) != '(' {
		return false
	}
	p.advance() // '('
	depth := 1
	for !p.atEnd() && depth > 0 {
		c := p.peek(0)
		switch {
		case c == '(' || c == '[' || c == '{':
			depth++
		case c == ')' || c == ']' || c == '}':
			depth--
			if depth == 0 {
				return false
			}
		case depth == 1 && c == ',':
			return true
		case depth == 1 && c == '|':
			return false
		}
		p.advance()
	}
	return false
}

// parseAnonymousOpPattern consumes `(pat1, pat2, …)` and builds an
// anonymous-operator [tomast.TermAppl]: the NameList is empty, the
// Args carry the sub-patterns. The typer/desugarer is expected to
// resolve the missing operator name from context (e.g. the unique
// operator in the surrounding sort).
func (p *parser) parseAnonymousOpPattern() (tomast.TomTerm, error) {
	if p.peek(0) != '(' {
		return nil, fmt.Errorf("expected '(' for anonymous op pattern at %s", p.cur)
	}
	p.advance() // '('
	args, err := p.parsePatternArgList()
	if err != nil {
		return nil, err
	}
	if p.atEnd() || p.peek(0) != ')' {
		return nil, fmt.Errorf("expected ')' to close anonymous op pattern at %s", p.cur)
	}
	p.advance() // ')'
	return tomast.MakeTermAppl(
		tomast.MakeConcOption(),
		tomast.MakeConcTomName(),
		tomast.MakeConcTomTerm(args...),
		tomast.MakeConcConstraint(),
	), nil
}

// parsePatternSlotList consumes `(slot (',' slot)*)?` after the
// opening `[` but before the closing `]`, where each `slot` is
// `slotName '=' pattern`. Returns one [tomast.MakePairSlotAppl] per
// slot; an empty list (`Foo[]`) returns the zero-length slice.
//
// Java reference: CstBuilder.exitImplicitArgs lowers each `name=val`
// into a CstPairSlot; AstBuilder.java then wraps each into a
// [PairSlotAppl(Name(slotName), patternValue)].
func (p *parser) parsePatternSlotList() ([]tomast.Slot, error) {
	var slots []tomast.Slot
	p.skipBlankInline()
	if !p.atEnd() && p.peek(0) == ']' {
		return slots, nil
	}
	for {
		p.skipBlankInline()
		slotName, err := p.readIdent()
		if err != nil {
			return nil, fmt.Errorf("in record-pattern slot name: %w", err)
		}
		p.skipBlankInline()
		if p.atEnd() || p.peek(0) != '=' {
			return nil, fmt.Errorf("expected '=' after slot name %q at %s", slotName, p.cur)
		}
		p.advance() // '='
		p.skipBlankInline()
		sub, err := p.parsePattern()
		if err != nil {
			return nil, err
		}
		slots = append(slots, tomast.MakePairSlotAppl(tomast.MakeName(slotName), sub))
		p.skipBlankInline()
		if p.atEnd() {
			return nil, fmt.Errorf("unterminated record-pattern slot list at %s", p.cur)
		}
		if p.peek(0) == ']' {
			return slots, nil
		}
		if p.peek(0) != ',' {
			return nil, fmt.Errorf("expected ',' or ']' in record-pattern slot list at %s", p.cur)
		}
		p.advance() // ','
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
	return lowerBlockBody(sub, filename)
}

// lowerBlockBody is the recursive workhorse behind [lowerActionBody]:
// given an already-positioned sub-parser, walk forward until EOF (or
// the next `}` at the current depth) and emit the instruction list
// for the surrounding block. Nested `{ … }` blocks recurse — each one
// becomes `UnamedBlock(concInstruction(<inner instructions>))` per
// the ANTLR `block: LBRACE (island | block | water)*? RBRACE` rule
// and AstBuilder.java's `Cst_UnamedBlock → UnamedBlock(convert(blocks))`
// (lines 535-538). The contained backquotes are still lowered through
// `parseBQTerm`, and host code between them is collected into TL
// chunks via the same water pipeline.
//
// The sub-parser's cursor is left at the closing `}` (which the
// caller should consume) or at EOF.
func lowerBlockBody(sub *parser, filename string) ([]tomast.Instruction, error) {
	var insts []tomast.Instruction
	waterStartIdx := sub.idx
	waterStartPos := sub.cur
	flushWater := func(endIdx int) {
		if endIdx <= waterStartIdx {
			return
		}
		chunk := sub.src[waterStartIdx:endIdx]
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
		c := sub.peek(0)
		if c == '`' {
			flushWater(sub.idx)
			bq, err := sub.parseBQTerm(true)
			if err != nil {
				return nil, fmt.Errorf("at %s: %w", sub.cur, err)
			}
			insts = append(insts, tomast.MakeBQTermToInstruction(bq))
			waterStartIdx = sub.idx
			waterStartPos = sub.cur
			continue
		}
		// Nested `%match` inside an action body. Recognise the
		// keyword, lift the parser's island state, and capture the
		// generated codes (the wrapping `InstructionToCode(Match(…))`)
		// — then unwrap them into raw Instructions for the
		// surrounding block list. Java's CstBuilder treats nested
		// `%match` the same way via the recursive `block: … island …`
		// rule.
		if c == '%' && sub.lookAheadKeyword("%match") {
			flushWater(sub.idx)
			savedCodes := sub.codes
			sub.codes = nil
			if err := sub.parseMatch(); err != nil {
				return nil, fmt.Errorf("nested %%match at %s: %w", sub.cur, err)
			}
			// Unwrap `InstructionToCode(Match(…))` back to its inner
			// instruction so it slots into the block's instruction
			// list.
			for _, code := range sub.codes {
				if itc, ok := code.(*tomast.InstructionToCodeCode); ok {
					insts = append(insts, itc.AstInstruction)
					continue
				}
				// Fallback: keep as a CodeToInstruction so we don't
				// lose information.
				insts = append(insts, tomast.MakeCodeToInstruction(code))
			}
			sub.codes = savedCodes
			waterStartIdx = sub.idx
			waterStartPos = sub.cur
			continue
		}
		if c == '{' {
			// Recursively lower the nested block as `UnamedBlock` to
			// mirror Java's CstBuilder.exitBlock. The opening `{` and
			// closing `}` are consumed here (they don't end up in the
			// surrounding TL chunk). The inner content's instructions
			// are wrapped in `UnamedBlock(concInstruction(…))`.
			flushWater(sub.idx)
			sub.advance() // '{'
			inner, err := lowerBlockBody(sub, filename)
			if err != nil {
				return nil, err
			}
			if sub.atEnd() || sub.peek(0) != '}' {
				return nil, fmt.Errorf("expected '}' to close nested block at %s", sub.cur)
			}
			sub.advance() // '}'
			insts = append(insts, tomast.MakeUnamedBlock(
				tomast.MakeConcInstruction(inner...),
			))
			waterStartIdx = sub.idx
			waterStartPos = sub.cur
			continue
		}
		if c == '}' {
			break
		}
		// String / char literals: consume opaquely so embedded
		// `\`` or `{ }` don't switch the parser into an island /
		// nested-block mode.
		if c == '"' || c == '\'' {
			sub.advance()
			for !sub.atEnd() && sub.peek(0) != c {
				if sub.peek(0) == '\\' && !sub.atEnd() {
					sub.advance()
					if !sub.atEnd() {
						sub.advance()
					}
					continue
				}
				if sub.peek(0) == '\n' {
					break
				}
				sub.advance()
			}
			if !sub.atEnd() && sub.peek(0) == c {
				sub.advance()
			}
			continue
		}
		// `// ... \n` and `/* ... */` comments — consume opaquely so
		// an embedded `\``/`{`/`}` doesn't trip up the parser. Java's
		// lexer routes comment tokens to the skip channel.
		if c == '/' && sub.peek(1) == '/' {
			for !sub.atEnd() && sub.peek(0) != '\n' {
				sub.advance()
			}
			continue
		}
		if c == '/' && sub.peek(1) == '*' {
			sub.advance()
			sub.advance()
			for !sub.atEnd() && !(sub.peek(0) == '*' && sub.peek(1) == '/') {
				sub.advance()
			}
			if !sub.atEnd() {
				sub.advance()
				sub.advance()
			}
			continue
		}
		sub.advance()
	}
	flushWater(sub.idx)
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

// parseActionRuleConstraintTail handles the constraint expression
// that may follow a patternlist via `(AND|OR) constraint`. The
// `&&`/`||` token is already at the cursor; the function consumes it
// then parses the constraint expression to its right and returns it.
// Multiple `&&`/`||` chains compose normally via parseConstraintExpr.
func (p *parser) parseActionRuleConstraintTail() (tomast.Constraint, error) {
	// Eat the first &&/|| and the constraint chain at the same level
	// by simulating parseConstraintAnd / parseConstraintExpr from the
	// op position.
	// First fold the current operator into an AndConstraint or
	// OrConstraintDisjunction wrapping the tail. The patternlist's
	// match-constraints are merged in finishHybridRule.
	if p.peek(0) == '&' && p.peek(1) == '&' {
		p.advance()
		p.advance()
	} else {
		p.advance() // '|'
		p.advance() // '|'
	}
	p.skipBlankInline()
	return p.parseConstraintExpr()
}

// finishHybridRule wraps up the `patternlist (AND|OR) constraint`
// form: build the per-slot MatchConstraints exactly as the regular
// patternlist path would, AND them with the trailing constraint,
// then consume `-> body`.
func (p *parser) finishHybridRule(
	slots []ruleSlot,
	subjects []tomast.BQTerm,
	tail tomast.Constraint,
	ruleLine int,
) (tomast.ConstraintInstruction, error) {
	mcs := make([]tomast.Constraint, 0, len(slots)+1)
	for i, s := range slots {
		var subj tomast.BQTerm
		if s.explicit != nil {
			subj = s.explicit
		} else if i < len(subjects) {
			subj = subjects[i]
		} else {
			subj = nil
		}
		if subj == nil {
			return nil, fmt.Errorf("hybrid rule: pattern %d has no subject at %s", i, p.cur)
		}
		mcs = append(mcs, tomast.MakeMatchConstraint(s.pat, subj, unknownType()))
	}
	mcs = append(mcs, tail)
	combined := tomast.MakeAndConstraint(mcs...)
	return p.finishConstraintRule(combined, ruleLine)
}

// ruleSlot mirrors the local struct used inside parseActionRule, lifted
// to package scope so the hybrid-rule helper can reference it.
type ruleSlot struct {
	pat      tomast.TomTerm
	explicit tomast.BQTerm
}

// looksLikeConstraintAction peeks ahead from `(` to detect whether
// the current rule is in the `c=constraint ARROW block` alternative
// (TomIslandParser.g4:75-77) rather than the `patternlist ARROW
// block` form. The heuristic: scan from the current position to the
// next top-level `->`, watching for `&&`, `||`, `<<`, `!=`, `==`,
// `<`, `>` at depth==0. If any of those operators occur before `->`,
// it's a constraint-action.
//
// We restore p.idx/cur on exit; no side effects.
func (p *parser) looksLikeConstraintAction() bool {
	save := p.idx
	saveCur := p.cur
	defer func() { p.idx = save; p.cur = saveCur }()
	depth := 0
	for !p.atEnd() {
		c := p.peek(0)
		switch {
		case c == '{' || c == '[':
			depth++
		case c == '}' || c == ']':
			depth--
			if depth < 0 {
				return false
			}
		case c == '(':
			depth++
		case c == ')':
			depth--
			if depth < -1 {
				return false
			}
		case depth == 0 && c == '-' && p.peek(1) == '>':
			return false
		// `&&` / `||` at depth 0 mean we've left a patternlist;
		// the remainder is a trailing constraint, handled by the
		// patternlist path. Stop the scan and report "not a pure
		// constraint action" so parseActionRule routes via
		// patternlist + finishHybridRule.
		case depth == 0 && c == '&' && p.peek(1) == '&':
			return false
		case depth == 0 && c == '|' && p.peek(1) == '|':
			return false
		case c == '<' && p.peek(1) == '<':
			return true
		case c == '!' && p.peek(1) == '=':
			return true
		case c == '=' && p.peek(1) == '=':
			return true
		case (c == '<' && p.peek(1) != '<') || (c == '>' && p.peek(1) != '>'):
			return true
		}
		p.advance()
	}
	return false
}

// parseConstraintExpr is the entry point of the constraint-precedence
// parser (lowest precedence). It accumulates `||`-separated AND
// expressions into an [OrConstraintDisjunction]. Java emits
// `OrConstraintDisjunction` for top-level `||` chains in action rules
// (per AstBuilder.java's Cst_OrConstraintDisjunction).
func (p *parser) parseConstraintExpr() (tomast.Constraint, error) {
	left, err := p.parseConstraintAnd()
	if err != nil {
		return nil, err
	}
	terms := []tomast.Constraint{left}
	for {
		p.skipBlankInline()
		if p.atEnd() || !(p.peek(0) == '|' && p.peek(1) == '|') {
			break
		}
		p.advance() // '|'
		p.advance() // '|'
		p.skipBlankInline()
		right, err := p.parseConstraintAnd()
		if err != nil {
			return nil, err
		}
		terms = append(terms, right)
	}
	if len(terms) == 1 {
		return terms[0], nil
	}
	return tomast.MakeOrConstraint(terms...), nil
}

// parseConstraintAnd handles the `&&` level — one rank tighter than
// `||`. Result is wrapped in [AndConstraint] when there is more than
// one conjunct.
func (p *parser) parseConstraintAnd() (tomast.Constraint, error) {
	left, err := p.parseConstraintAtom()
	if err != nil {
		return nil, err
	}
	terms := []tomast.Constraint{left}
	for {
		p.skipBlankInline()
		if p.atEnd() || !(p.peek(0) == '&' && p.peek(1) == '&') {
			break
		}
		p.advance() // '&'
		p.advance() // '&'
		p.skipBlankInline()
		right, err := p.parseConstraintAtom()
		if err != nil {
			return nil, err
		}
		terms = append(terms, right)
	}
	if len(terms) == 1 {
		return terms[0], nil
	}
	return tomast.MakeAndConstraint(terms...), nil
}

// parseConstraintAtom handles a single constraint term:
//
//   - `(constraint)`             → recurse via parseConstraintExpr;
//   - `pat << bqterm`            → MatchConstraint;
//   - `term op term`             → NumericConstraint (where op is
//     `==`, `!=`, `<`, `<=`, `>`, `>=`).
//
// Two LHS-shape decisions matter:
//
//   - For `<<`, the LHS is a [pattern] — full pattern grammar
//     including OR heads, record args, anti, annotations.
//   - For numeric ops, the LHS is a [term] — limited to var, app,
//     constant. We approximate by re-using parseBQTerm there (the
//     two grammars agree on these shapes).
//
// We commit to one or the other by lookahead: scan for `<<` before
// any numeric op, `&&`/`||`/`->`, at the same depth as the cursor.
func (p *parser) parseConstraintAtom() (tomast.Constraint, error) {
	p.skipBlankInline()
	if !p.atEnd() && p.peek(0) == '(' && !p.parenIsConstraintAtomTerm() {
		p.advance() // '('
		c, err := p.parseConstraintExpr()
		if err != nil {
			return nil, err
		}
		p.skipBlankInline()
		if p.atEnd() || p.peek(0) != ')' {
			return nil, fmt.Errorf("expected ')' in constraint at %s", p.cur)
		}
		p.advance() // ')'
		return c, nil
	}
	if p.lookAtomLHSIsPattern() {
		pat, err := p.parsePattern()
		if err != nil {
			return nil, err
		}
		p.skipBlankInline()
		if p.atEnd() || p.peek(0) != '<' || p.peek(1) != '<' {
			return nil, fmt.Errorf("expected '<<' after pattern LHS in constraint at %s", p.cur)
		}
		p.advance() // '<'
		p.advance() // '<'
		p.skipBlankInline()
		rhs, err := p.parseBQTerm(false)
		if err != nil {
			return nil, fmt.Errorf("after '<<' in constraint: %w", err)
		}
		aType := unknownType()
		if t := bqVarTermType(rhs); t != nil {
			aType = t
		}
		return tomast.MakeMatchConstraint(pat, rhs, aType), nil
	}
	lhs, err := p.parseBQTerm(false)
	if err != nil {
		return nil, err
	}
	p.skipBlankInline()
	if p.atEnd() {
		return nil, fmt.Errorf("expected constraint operator after LHS at %s", p.cur)
	}
	if p.peek(0) == '<' && p.peek(1) == '<' {
		p.advance() // '<'
		p.advance() // '<'
		p.skipBlankInline()
		rhs, err := p.parseBQTerm(false)
		if err != nil {
			return nil, fmt.Errorf("after '<<' in constraint: %w", err)
		}
		pat := bqTermToPattern(lhs)
		aType := unknownType()
		if t := bqVarTermType(rhs); t != nil {
			aType = t
		}
		return tomast.MakeMatchConstraint(pat, rhs, aType), nil
	}
	op, advance := p.peekNumericOp()
	if op == nil {
		return nil, fmt.Errorf("expected '<<' or numeric op in constraint at %s", p.cur)
	}
	for range advance {
		p.advance()
	}
	p.skipBlankInline()
	rhs, err := p.parseBQTerm(false)
	if err != nil {
		return nil, fmt.Errorf("after numeric op in constraint: %w", err)
	}
	return tomast.MakeNumericConstraint(lhs, rhs, op), nil
}

// parenIsConstraintAtomTerm peeks past `(` and decides whether the
// content is a pattern-style LHS (OR-head `(F1|F2)` or record args
// `(F1|F2)[...]`) rather than a parenthesised sub-constraint. Pattern
// LHS shapes have a single `|` (OR-head separator) at the same paren
// depth — distinct from the `||` constraint connector.
//
// Returns true if the parenthesised group looks like an atomic pattern
// (so the caller should NOT consume the `(` as a sub-expression).
func (p *parser) parenIsConstraintAtomTerm() bool {
	save := p.idx
	saveCur := p.cur
	defer func() { p.idx = save; p.cur = saveCur }()
	if p.peek(0) != '(' {
		return false
	}
	p.advance() // '('
	depth := 1
	for !p.atEnd() && depth > 0 {
		c := p.peek(0)
		switch {
		case c == '(':
			depth++
		case c == ')':
			depth--
		case c == '|' && p.peek(1) == '|':
			// `||` (constraint disjunction) — consume both bytes
			// so the second `|` isn't mistaken for an OR-head.
			p.advance()
		case depth == 1 && c == '|':
			return true
		}
		p.advance()
	}
	return false
}

// lookAtomLHSIsPattern decides — for a non-parenthesised LHS — whether
// to route via parsePattern vs parseBQTerm. The shapes only a pattern
// can take are `_`, `!`, `(OR-head)`, or an `ID @ …` form. Anything
// else (bare ID, `ID(args)`, constant, `ID*`) parses identically as
// either grammar so we pick bqterm for the AST shape of `NumericConstraint`.
func (p *parser) lookAtomLHSIsPattern() bool {
	if p.atEnd() {
		return false
	}
	c := p.peek(0)
	if c == '_' || c == '!' || c == '(' {
		return true
	}
	if !isIdentStart(c) {
		return false
	}
	// Look ahead for `ID @` (annotation prefix) or `ID [ … ]` (record
	// pattern, only legal in pattern grammar).
	save := p.idx
	saveCur := p.cur
	defer func() { p.idx = save; p.cur = saveCur }()
	for !p.atEnd() && isIdentChar(p.peek(0)) {
		p.advance()
	}
	p.skipBlankInline()
	if p.atEnd() {
		return false
	}
	return p.peek(0) == '@' || p.peek(0) == '['
}

// peekNumericOp returns the [tomast.NumericConstraintType] for the
// upcoming op token plus the number of bytes to consume, or (nil, 0)
// if no numeric op is at the cursor.
func (p *parser) peekNumericOp() (tomast.NumericConstraintType, int) {
	switch {
	case p.peek(0) == '!' && p.peek(1) == '=':
		return tomast.MakeNumDifferent(), 2
	case p.peek(0) == '=' && p.peek(1) == '=':
		return tomast.MakeNumEqual(), 2
	case p.peek(0) == '<' && p.peek(1) == '=':
		return tomast.MakeNumLessOrEqualThan(), 2
	case p.peek(0) == '>' && p.peek(1) == '=':
		return tomast.MakeNumGreaterOrEqualThan(), 2
	case p.peek(0) == '<':
		return tomast.MakeNumLessThan(), 1
	case p.peek(0) == '>':
		return tomast.MakeNumGreaterThan(), 1
	}
	return nil, 0
}

// bqTermToPattern converts a bqterm into the equivalent pattern.
// BQVariable → Variable, BQVariableStar → VariableStar, BQAppl →
// TermAppl. Anything else is returned as-is via a panic — caller
// already validated the shape.
func bqTermToPattern(bq tomast.BQTerm) tomast.TomTerm {
	switch v := bq.(type) {
	case *tomast.BQVariableBQTerm:
		name := v.AstName
		// `_` parsed as a bare ident in bqterm context becomes
		// Name("_"); pattern grammar treats it as the anonymous
		// wildcard with EmptyName so the desugarer can rename it to
		// a fresh variable. Mirror that conversion here.
		if n, ok := name.(*tomast.NameTomName); ok && n.String_ == "_" {
			name = tomast.MakeEmptyName()
		}
		return tomast.MakeVariable(tomast.MakeConcOption(), name, unknownType(), tomast.MakeConcConstraint())
	case *tomast.BQVariableStarBQTerm:
		name := v.AstName
		if n, ok := name.(*tomast.NameTomName); ok && n.String_ == "_" {
			name = tomast.MakeEmptyName()
		}
		return tomast.MakeVariableStar(tomast.MakeConcOption(), name, unknownType(), tomast.MakeConcConstraint())
	case *tomast.BQApplBQTerm:
		// args: convert each bqterm arg recursively as a pattern arg.
		var patArgs []tomast.TomTerm
		if c, ok := v.Args.(*tomast.ConcBQTermBQTermList); ok {
			for _, a := range c.Slots {
				patArgs = append(patArgs, bqTermToPattern(a))
			}
		}
		return tomast.MakeTermAppl(
			tomast.MakeConcOption(),
			tomast.MakeConcTomName(v.AstName),
			tomast.MakeConcTomTerm(patArgs...),
			tomast.MakeConcConstraint(),
		)
	}
	// Fallback: wrap in a synthetic Variable carrying nil — unlikely to
	// be reached given the LHS shapes parseBQTerm produces here.
	return tomast.MakeVariable(tomast.MakeConcOption(), tomast.MakeEmptyName(), unknownType(), tomast.MakeConcConstraint())
}

// finishConstraintRule consumes the `-> body` portion of an action
// rule whose LHS is a fully-parsed [tomast.Constraint], and emits the
// resulting [ConstraintInstruction].
func (p *parser) finishConstraintRule(c tomast.Constraint, ruleLine int) (tomast.ConstraintInstruction, error) {
	p.skipBlankInline()
	if p.atEnd() || p.peek(0) != '-' || p.peek(1) != '>' {
		return nil, fmt.Errorf("expected '->' after constraint expression at %s", p.cur)
	}
	p.advance() // '-'
	p.advance() // '>'
	p.skipBlankInline()
	var bodyInsts []tomast.Instruction
	if !p.atEnd() && p.peek(0) == '{' {
		bodyContent, bodyStart, err := p.captureBalancedBlock()
		if err != nil {
			return tomast.ConstraintInstruction(nil), err
		}
		bodyInsts, err = lowerActionBody(bodyContent, bodyStart, p.filename)
		if err != nil {
			return tomast.ConstraintInstruction(nil), fmt.Errorf("action body: %w", err)
		}
	} else {
		bq, err := p.parseBQTerm(false)
		if err != nil {
			return tomast.ConstraintInstruction(nil), fmt.Errorf("bare bqterm body: %w", err)
		}
		bodyInsts = []tomast.Instruction{tomast.MakeReturn(bq)}
	}
	action := tomast.MakeRawAction(tomast.MakeIf(
		tomast.MakeTrueTL(),
		tomast.MakeAbstractBlock(tomast.MakeConcInstruction(bodyInsts...)),
		tomast.MakeNop(),
	))
	ruleOpts := tomast.MakeConcOption(
		tomast.MakeOriginTracking(tomast.MakeName("ConstraintAction"), int64(ruleLine), p.filename),
	)
	return tomast.MakeConstraintInstruction(c, action, ruleOpts), nil
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
	return isIdentStart(c) || isDigit(c)
}

func isDigit(c byte) bool { return c >= '0' && c <= '9' }
