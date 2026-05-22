// Package typer — host-code variable-declaration scanner.
//
// `seedVarTypesFromSource` reads `state.Source` (the raw host
// language text — Java or Go) and records every local-variable
// declaration whose type names a known Tom sort. The result feeds
// the typer's MatchConstraint case: when the user writes
//
//	Term t = `f(a()); %match(t) { ... }     // Java
//	var t Term = `f(a()); %match(t) { ... } // Go
//
// the BQVariable `t` reaches the typer with the parser's "unknown
// type" placeholder. The seeder gives us a per-name map of
// (line, sort) declarations; the typer picks the latest
// declaration at or before the %match's line.
//
// This is a pragmatic substitute for Java NewKernelTyper's full
// scope-aware symbol table — enough to unblock fixtures that have
// multiple declarations of the same name across method bodies
// (e.g. `Term res = …` in test1 and `TermList res = …` in
// test5 of /tom/test/TestSublists.t).

package typer

import (
	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

// seedVarTypesFromSource walks `src` once, recognising local
// declarations of the shape `<Sort> <name>` (Java/C/Go param)
// and `var <name> <Sort>` (Go), where `<Sort>` is a key in
// `syms.Sorts`. Each declaration goes into `n.varDecls`; the
// FIRST declaration of each name also goes into `n.varTypes`
// (backward-compatible for callers that don't track line info).
//
// Comments (`//…\n`, `/* … */`) and string/char literals are
// skipped. Tom backquotes (`\``) are NOT treated as string
// delimiters — they open an in-line backquote term that the
// surrounding parser handles, and pairing them as quotes would
// swallow subsequent variable declarations.
func seedVarTypesFromSource(n *newKernelTyper, src []byte, syms *tom.SymbolTable) {
	if syms == nil || len(syms.Sorts) == 0 {
		return
	}
	(&varSeeder{n: n, src: string(src), syms: syms, line: 1}).scan()
}

// varSeeder is a one-shot byte-level scanner. The state
// (cursor, current line, recent tokens) is shared between
// methods so callers don't have to thread it manually.
//
// The token ring is small (4 entries) — it only needs to look
// back one token to spot `<prev> <id>` declaration shapes.
type varSeeder struct {
	n      *newKernelTyper
	src    string
	syms   *tom.SymbolTable
	i      int
	line   int
	tokens []string
}

// scan walks the whole source once.
func (v *varSeeder) scan() {
	for v.i < len(v.src) {
		c := v.src[v.i]
		switch {
		case c == '/' && v.i+1 < len(v.src) && v.src[v.i+1] == '/':
			v.skipLineComment()
		case c == '/' && v.i+1 < len(v.src) && v.src[v.i+1] == '*':
			v.skipBlockComment()
		case c == '"' || c == '\'':
			v.skipQuoted(c)
			v.pushToken("<lit>")
		case c == '`':
			// Tom backquote — not a paired delimiter. Skip the
			// single byte and let the surrounding bytes parse as
			// normal tokens.
			v.i++
			v.pushToken("`")
		case isLetter(c) || c == '_':
			id := v.readIdent()
			v.handleIdent(id)
		case c == ' ' || c == '\t' || c == '\n' || c == '\r':
			if c == '\n' {
				v.line++
			}
			v.i++
		default:
			// Punctuation — record so `<Sort> <name>` doesn't
			// accidentally also match `<Sort> <name> <other>`.
			v.pushToken(string(c))
			v.i++
		}
	}
}

// handleIdent inspects an identifier just read. If the previous
// non-whitespace token was another ident, this is a candidate
// `<Sort> <name>` pair (Java/C-style declaration); if the previous
// token was `var`, this is a Go `var <name> <Sort>` form.
func (v *varSeeder) handleIdent(id string) {
	defer v.pushToken(id)
	if len(v.tokens) == 0 {
		return
	}
	prev := v.tokens[len(v.tokens)-1]
	// `Sort name` (Java/C-style): prev is an ident, not a
	// statement keyword.
	if isIdentLike(prev) && !isKeywordBlockingDecl(prev) {
		v.tryRecord(prev, id, v.peekNext())
	}
	// Go `var name Sort`: read past `name` to find `Sort`.
	if prev == "var" {
		if sortName, nxt, ok := v.peekIdentAfter(); ok {
			v.tryRecord(sortName, id, nxt)
		}
	}
}

// pushToken appends to the ring buffer, capped at 4 entries.
func (v *varSeeder) pushToken(t string) {
	v.tokens = append(v.tokens, t)
	if len(v.tokens) > 4 {
		v.tokens = v.tokens[len(v.tokens)-4:]
	}
}

// readIdent consumes the longest identifier run starting at v.i.
func (v *varSeeder) readIdent() string {
	j := v.i
	for j < len(v.src) && (isLetter(v.src[j]) || isDigitByte(v.src[j]) || v.src[j] == '_') {
		j++
	}
	id := v.src[v.i:j]
	v.i = j
	return id
}

// peekNext returns the next non-space byte after v.i, or ' '
// at EOF. Used by tryRecord to disambiguate `Sort name =`
// (declaration) from `Sort name name2` (likely a call).
func (v *varSeeder) peekNext() byte {
	j := v.i
	for j < len(v.src) && (v.src[j] == ' ' || v.src[j] == '\t') {
		j++
	}
	if j < len(v.src) {
		return v.src[j]
	}
	return ' '
}

// peekIdentAfter is the Go-form lookahead: from v.i, skip
// horizontal whitespace, then read another identifier. Returns
// the identifier and the byte right after it (for the next-byte
// disambiguator). `ok=false` when the lookahead fails.
func (v *varSeeder) peekIdentAfter() (string, byte, bool) {
	save := v.i
	for save < len(v.src) && (v.src[save] == ' ' || v.src[save] == '\t') {
		save++
	}
	if save >= len(v.src) || !(isLetter(v.src[save]) || v.src[save] == '_') {
		return "", 0, false
	}
	k := save
	for k < len(v.src) && (isLetter(v.src[k]) || isDigitByte(v.src[k]) || v.src[k] == '_') {
		k++
	}
	nxt := byte(' ')
	if k < len(v.src) {
		nxt = v.src[k]
	}
	return v.src[save:k], nxt, true
}

// tryRecord registers (varName → sortName) when sortName is a
// known sort AND the next byte is one of the declaration-shape
// punctuators (`=` `,` `;` `)` ` ` `\t` `\n` `{`).
func (v *varSeeder) tryRecord(sortName, varName string, next byte) {
	if _, ok := v.syms.Sorts[sortName]; !ok {
		return
	}
	switch next {
	case '=', ',', ';', ')', '\n', ' ', '\t', '{':
		// declaration-shaped
	default:
		return
	}
	typ := tomast.MakeType(
		tomast.MakeConcTypeOption(),
		sortName,
		tomast.MakeEmptyTargetLanguageType(),
	)
	v.n.varDecls[varName] = append(v.n.varDecls[varName], varDecl{line: v.line, typ: typ})
	if _, exists := v.n.varTypes[varName]; !exists {
		v.n.varTypes[varName] = typ
	}
}

func (v *varSeeder) skipLineComment() {
	for v.i < len(v.src) && v.src[v.i] != '\n' {
		v.i++
	}
}

func (v *varSeeder) skipBlockComment() {
	v.i += 2
	for v.i+1 < len(v.src) && !(v.src[v.i] == '*' && v.src[v.i+1] == '/') {
		if v.src[v.i] == '\n' {
			v.line++
		}
		v.i++
	}
	if v.i+1 < len(v.src) {
		v.i += 2
	}
}

func (v *varSeeder) skipQuoted(q byte) {
	v.i++
	for v.i < len(v.src) && v.src[v.i] != q {
		if v.src[v.i] == '\n' {
			v.line++
		}
		if v.src[v.i] == '\\' && v.i+1 < len(v.src) {
			v.i++
		}
		v.i++
	}
	if v.i < len(v.src) {
		v.i++
	}
}

// isKeywordBlockingDecl returns true for words that AREN'T sort
// names even when they precede an identifier — `var`, `func`,
// `return`, `case` in Go/Java parsing contexts.
func isKeywordBlockingDecl(s string) bool {
	switch s {
	case "var", "func", "return", "case":
		return true
	}
	return false
}

func isLetter(b byte) bool {
	return (b >= 'a' && b <= 'z') || (b >= 'A' && b <= 'Z')
}

func isDigitByte(b byte) bool { return b >= '0' && b <= '9' }

func isIdentLike(s string) bool {
	if s == "" {
		return false
	}
	for i := 0; i < len(s); i++ {
		b := s[i]
		if !(isLetter(b) || isDigitByte(b) || b == '_') {
			return false
		}
	}
	return true
}
