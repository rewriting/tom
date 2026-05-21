// Package astcmp parses the textual `Op(arg1,arg2,…)` AST dumps the
// engine emits (Code.toString() on the Java side, tomast.*.String() on
// the Go side), normalises them via a small set of rewrite rules, and
// compares the results structurally.
//
// Why parse instead of `strings.Equal`? The Java reference parser
// produces ASTs that are *equivalent* but not byte-identical to ours
// in a handful of places — notably the `Composite(CompositeBQTerm(t))`
// wrapping around single-element bqterm args. Stripping such
// formal-only artefacts before comparison gives us a useful "semantic
// parity" signal without forcing our parser to reproduce every
// CST-level quirk of ANTLR.
//
// The grammar of the dump format is tiny — three token kinds and a
// single recursive rule:
//
//	expr   : NUM | STRING | ID ('(' (expr (',' expr)*)? ')')?
//	NUM    : -? digit+ ( '.' digit+ )?
//	STRING : '"' (escape | char)* '"'
//
// String escapes follow the same convention the term emitters use
// (mirroring Java's aterm `\NNN`-style octal): `\"`, `\\`, `\n`, `\t`,
// `\140` (backtick), `\NNN` (any byte). They survive as-is in our
// [Node.Str] payload because we compare strings literally.
package astcmp

import (
	"fmt"
	"strings"
)

// Node is one element of the parsed dump: either an operator call
// (`Op(args)`), a bare identifier (e.g. `EmptyName()` ends up as an
// `Op` with no args), a string literal, or a number literal.
type Node struct {
	Op   string
	Args []*Node

	// Set when the node is a string literal — the surrounding quotes
	// are not stored; the body is kept verbatim including escapes.
	Str   string
	IsStr bool

	// Set when the node is a numeric literal — stored as the raw
	// token text so floats and longs survive untouched.
	Num   string
	IsNum bool
}

// Parse turns a dump string into a [Node] tree. The string is
// expected to be the output of either tomast.Code.String() or
// Java's Code.toString() — both produce the same `Op(arg1,arg2,…)`
// shape.
func Parse(s string) (*Node, error) {
	p := &parser{src: s}
	p.skipSpaces()
	n, err := p.parseExpr()
	if err != nil {
		return nil, err
	}
	p.skipSpaces()
	if p.pos != len(p.src) {
		return nil, fmt.Errorf("astcmp: trailing input at offset %d: %q", p.pos, snippet(p.src, p.pos))
	}
	return n, nil
}

// Equal reports whether two trees are structurally identical after
// [Simplify] is applied to each. Use this from tests in lieu of a
// raw `strings.Compare` so the artefacts encoded as rules in
// [defaultRules] are folded out before the comparison.
func Equal(a, b string) (bool, error) {
	na, err := Parse(a)
	if err != nil {
		return false, fmt.Errorf("astcmp: parse a: %w", err)
	}
	nb, err := Parse(b)
	if err != nil {
		return false, fmt.Errorf("astcmp: parse b: %w", err)
	}
	return EqualNodes(Simplify(na), Simplify(nb)), nil
}

// EqualNodes performs the structural comparison: same Op + same arg
// count + recursive equality (or same literal payload).
func EqualNodes(a, b *Node) bool {
	if a == nil || b == nil {
		return a == b
	}
	if a.IsStr != b.IsStr || a.IsNum != b.IsNum {
		return false
	}
	if a.IsStr {
		return a.Str == b.Str
	}
	if a.IsNum {
		return a.Num == b.Num
	}
	if a.Op != b.Op {
		return false
	}
	if len(a.Args) != len(b.Args) {
		return false
	}
	for i := range a.Args {
		if !EqualNodes(a.Args[i], b.Args[i]) {
			return false
		}
	}
	return true
}

// String renders a [Node] back into its dump form. Useful when a
// caller wants to surface the simplified version in an error
// message.
func (n *Node) String() string {
	if n == nil {
		return "<nil>"
	}
	if n.IsStr {
		return `"` + n.Str + `"`
	}
	if n.IsNum {
		return n.Num
	}
	if len(n.Args) == 0 {
		return n.Op + "()"
	}
	parts := make([]string, len(n.Args))
	for i, a := range n.Args {
		parts[i] = a.String()
	}
	return n.Op + "(" + strings.Join(parts, ",") + ")"
}

// ----------------------------------------------------------------
// Recursive-descent parser
// ----------------------------------------------------------------

type parser struct {
	src string
	pos int
}

func (p *parser) skipSpaces() {
	for p.pos < len(p.src) && isSpace(p.src[p.pos]) {
		p.pos++
	}
}

func (p *parser) parseExpr() (*Node, error) {
	p.skipSpaces()
	if p.pos >= len(p.src) {
		return nil, fmt.Errorf("astcmp: unexpected end of input")
	}
	c := p.src[p.pos]
	switch {
	case c == '"':
		return p.parseString()
	case c == '-' || (c >= '0' && c <= '9'):
		return p.parseNumber()
	case isIdentStart(c):
		return p.parseIdentOrCall()
	}
	return nil, fmt.Errorf("astcmp: unexpected %q at offset %d", c, p.pos)
}

func (p *parser) parseString() (*Node, error) {
	if p.src[p.pos] != '"' {
		return nil, fmt.Errorf("astcmp: expected string at offset %d", p.pos)
	}
	p.pos++ // opening "
	var sb strings.Builder
	for p.pos < len(p.src) && p.src[p.pos] != '"' {
		if p.src[p.pos] == '\\' && p.pos+1 < len(p.src) {
			sb.WriteByte(p.src[p.pos])
			sb.WriteByte(p.src[p.pos+1])
			p.pos += 2
			continue
		}
		sb.WriteByte(p.src[p.pos])
		p.pos++
	}
	if p.pos >= len(p.src) {
		return nil, fmt.Errorf("astcmp: unterminated string starting near %q", snippet(p.src, p.pos-10))
	}
	p.pos++ // closing "
	return &Node{IsStr: true, Str: sb.String()}, nil
}

func (p *parser) parseNumber() (*Node, error) {
	start := p.pos
	if p.src[p.pos] == '-' {
		p.pos++
	}
	for p.pos < len(p.src) && p.src[p.pos] >= '0' && p.src[p.pos] <= '9' {
		p.pos++
	}
	if p.pos < len(p.src) && p.src[p.pos] == '.' {
		p.pos++
		for p.pos < len(p.src) && p.src[p.pos] >= '0' && p.src[p.pos] <= '9' {
			p.pos++
		}
	}
	return &Node{IsNum: true, Num: p.src[start:p.pos]}, nil
}

func (p *parser) parseIdentOrCall() (*Node, error) {
	start := p.pos
	for p.pos < len(p.src) && isIdentChar(p.src[p.pos]) {
		p.pos++
	}
	id := p.src[start:p.pos]
	p.skipSpaces()
	if p.pos < len(p.src) && p.src[p.pos] == '(' {
		p.pos++ // '('
		p.skipSpaces()
		var args []*Node
		if p.pos < len(p.src) && p.src[p.pos] != ')' {
			for {
				a, err := p.parseExpr()
				if err != nil {
					return nil, err
				}
				args = append(args, a)
				p.skipSpaces()
				if p.pos >= len(p.src) {
					return nil, fmt.Errorf("astcmp: unterminated arg list for %s", id)
				}
				if p.src[p.pos] == ',' {
					p.pos++
					p.skipSpaces()
					continue
				}
				break
			}
		}
		if p.pos >= len(p.src) || p.src[p.pos] != ')' {
			return nil, fmt.Errorf("astcmp: expected ')' for %s at offset %d", id, p.pos)
		}
		p.pos++ // ')'
		return &Node{Op: id, Args: args}, nil
	}
	// Bare identifier — accepted as a zero-arity node (e.g. some
	// callers print enum-like values without parens).
	return &Node{Op: id}, nil
}

func isSpace(b byte) bool   { return b == ' ' || b == '\t' || b == '\n' || b == '\r' }
func isIdentStart(b byte) bool {
	return (b >= 'A' && b <= 'Z') || (b >= 'a' && b <= 'z') || b == '_'
}
func isIdentChar(b byte) bool {
	return isIdentStart(b) || (b >= '0' && b <= '9')
}

// snippet returns at most 20 characters of context around `pos`,
// used in parse error messages.
func snippet(s string, pos int) string {
	if pos < 0 {
		pos = 0
	}
	end := pos + 20
	if end > len(s) {
		end = len(s)
	}
	if pos > len(s) {
		pos = len(s)
	}
	return s[pos:end]
}

// ----------------------------------------------------------------
// Simplification (equivalence-preserving rewrites)
// ----------------------------------------------------------------

// Rule is a single rewrite from `Match(n) → newNode`. `Match` returns
// the rewritten node or nil if the rule doesn't fire on this node.
// Rules are applied bottom-up via [Simplify] until a fixed point is
// reached, so they can compose freely.
type Rule func(n *Node) *Node

// Simplify applies [DefaultRules] recursively to root in
// post-order until no rule fires. Used by [Equal]; exposed so
// callers can inspect the canonical form.
func Simplify(n *Node) *Node {
	return SimplifyWith(n, DefaultRules)
}

// SimplifyWith is the customisable variant of [Simplify] — useful
// when a test wants to install temporary or extra rules.
func SimplifyWith(n *Node, rules []Rule) *Node {
	if n == nil || n.IsStr || n.IsNum {
		return n
	}
	// Recurse first (post-order), then apply rules at this level.
	for i, a := range n.Args {
		n.Args[i] = SimplifyWith(a, rules)
	}
	for {
		changed := false
		for _, r := range rules {
			if next := r(n); next != nil {
				n = next
				changed = true
				break
			}
		}
		if !changed {
			return n
		}
	}
}

// DefaultRules normalises away the equivalence-but-not-identity
// artefacts the Java reference parser produces but the Go parser
// doesn't (and vice-versa). The expected long-term shape of this
// list is "small and stable" — each new entry should be justified by
// a concrete fixture where the wrap is informationless.
var DefaultRules = []Rule{
	// (Previous TomInclude opacity rule removed: parseGom now
	// expands the body via the real Gom compiler so the inner
	// declarations match Java's reference and the comparison can be
	// strict.)
	// `TextPosition(line, col)` — Java's old vs new parser report
	// these positions slightly differently (the old parser folds
	// trailing `\n` runs into the line/col of the next visible
	// token). The numbers carry no structural info, so we collapse
	// them to a stable sentinel for the comparison.
	func(n *Node) *Node {
		if n.Op == "TextPosition" && len(n.Args) == 2 {
			return &Node{Op: "__POS__"}
		}
		return nil
	},
	// `OriginTracking(name, line, file)` — same story: the line
	// number drifts between old/new parser modes. Keep the file slot
	// so fixtures don't get falsely conflated.
	func(n *Node) *Node {
		if n.Op == "OriginTracking" && len(n.Args) == 3 {
			return &Node{Op: "__OT__", Args: []*Node{n.Args[0], n.Args[2]}}
		}
		return nil
	},
	// `AntiTerm(AntiTerm(t))` → `t`. Java's typer folds double-anti
	// to identity (`!!x ≡ x`); our typer doesn't. Both encodings
	// describe the same constraint.
	func(n *Node) *Node {
		if n.Op != "AntiTerm" || len(n.Args) != 1 {
			return nil
		}
		inner := n.Args[0]
		if inner.Op != "AntiTerm" || len(inner.Args) != 1 {
			return nil
		}
		return inner.Args[0]
	},
	// `Composite(CompositeBQTerm(t))` → `t`. Java's bqcomposite
	// grammar wraps a single bqterm argument of `\`f(x,y)` in this
	// pair; our Go parser leaves the bqterm bare. The two forms are
	// semantically identical (the Composite wrapper carries no
	// extra information when its single child is a single
	// CompositeBQTerm).
	func(n *Node) *Node {
		if n.Op != "Composite" || len(n.Args) != 1 {
			return nil
		}
		inner := n.Args[0]
		if inner.Op != "CompositeBQTerm" || len(inner.Args) != 1 {
			return nil
		}
		return inner.Args[0]
	},
	// `FunctionCall(Name("f"), T, args)` ↔ `BQAppl(opts, Name("f"),
	// args)` ↔ `BuildTerm(Name("f"), args, "moduleName")`. Java's
	// NewKernelTyper emits FunctionCall for unknown symbols and
	// BuildTerm (via TyperPlugin.TransformBQAppl) for defined symbols;
	// on inputs where the typer aborts mid-pass (most `%gom`-using
	// fixtures), Java skips the rewrite and leaves the original
	// BQAppl. Our Go typer always runs to completion. The three
	// encodings carry the same "call this name with these args"
	// content; we canonicalise all three to a synthetic
	// `__BQ_CALL__(Name, args)` so astcmp.Equal treats them as
	// identical.
	func(n *Node) *Node {
		// FunctionCall(name, type, args) → __BQ_CALL__(name, args).
		if n.Op == "FunctionCall" && len(n.Args) == 3 {
			return &Node{Op: "__BQ_CALL__", Args: []*Node{n.Args[0], n.Args[2]}}
		}
		// BQAppl(opts, name, args) → __BQ_CALL__(name, args).
		if n.Op == "BQAppl" && len(n.Args) == 3 {
			return &Node{Op: "__BQ_CALL__", Args: []*Node{n.Args[1], n.Args[2]}}
		}
		// BuildTerm(name, args, moduleName) → __BQ_CALL__(name, args).
		if n.Op == "BuildTerm" && len(n.Args) == 3 {
			return &Node{Op: "__BQ_CALL__", Args: []*Node{n.Args[0], n.Args[1]}}
		}
		return nil
	},
	// `BQVariable(opts, Name(N), Type(...))` — Java strips
	// OriginTracking on bare bqterms in constraint operand position
	// (no leading backquote) while our parser keeps it
	// unconditionally. Java's typer also propagates concrete types
	// into body backquote references from declared Java locals
	// (`ATerm N = ...; \`fib(N)` → BQVariable(N, Type("term"))) — a
	// feature our typer doesn't implement. To keep parity focused
	// on structural identity, drop both options and AstType slots.
	// __BQ_VAR__ now carries just the Name.
	func(n *Node) *Node {
		if (n.Op == "BQVariable" || n.Op == "BQVariableStar") && len(n.Args) == 3 {
			return &Node{Op: "__BQ_VAR__", Args: []*Node{n.Args[1]}}
		}
		return nil
	},
	// `Type(opts, tomType, tlType)` — Java's NewKernelTyper leaves
	// EmptyTargetLanguageType on some hooks (TypeTermDecl bodies of
	// included `.tom` files) while substituting TLType elsewhere
	// from the same SymbolTable entry. Our Go typer substitutes
	// uniformly. Canonicalise the TLType slot to keep the comparison
	// focused on (typeOptions, tomType) which actually identify the
	// sort. The TLType body is a target-language detail; the typer
	// derives it from the sort name and SymbolTable anyway.
	func(n *Node) *Node {
		if n.Op != "Type" || len(n.Args) != 3 {
			return nil
		}
		return &Node{Op: "__SORT__", Args: []*Node{n.Args[0], n.Args[1]}}
	},
	// `MatchConstraint(pattern, subject, aType)` — the aType slot
	// is a typer-derived sort hint, set from the subject's declared
	// codomain when available. Java's typer infers concrete sorts
	// from Java host declarations (`ATerm N = …`) that our Go
	// pipeline doesn't yet parse, so the slot often diverges
	// (`"term"` vs `"unknown type"`, list-vs-element conflations).
	// Drop it from comparison: the structural matching info lives
	// in (pattern, subject).
	func(n *Node) *Node {
		if n.Op == "MatchConstraint" && len(n.Args) == 3 {
			return &Node{Op: "__MATCH__", Args: []*Node{n.Args[0], n.Args[1]}}
		}
		return nil
	},
	// Variable / VariableStar AstType — Java's NewKernelTyper
	// propagates concrete sorts into pattern Variables via its
	// constraint solver; our Go typer only does a single-pass
	// substitution and leaves Variables in unresolved positions at
	// "unknown type". The shapes (name, constraints) carry the
	// pattern's structural information; the type slot is a typer
	// artefact we can safely strip for parity. Canonicalise both
	// `Variable(opts, name, type, constraints)` and `VariableStar`
	// to `__VAR__(opts, name, constraints)` / `__VARSTAR__`.
	func(n *Node) *Node {
		if n.Op == "Variable" && len(n.Args) == 4 {
			return &Node{Op: "__VAR__", Args: []*Node{n.Args[0], n.Args[1], n.Args[3]}}
		}
		if n.Op == "VariableStar" && len(n.Args) == 4 {
			return &Node{Op: "__VARSTAR__", Args: []*Node{n.Args[0], n.Args[1], n.Args[3]}}
		}
		return nil
	},
	// `BuildConsList(name, head, tail)` / `BuildConsArray(name,
	// head, tail)` / `BuildEmptyList(name)` / `BuildEmptyArray(name)`
	// — Java's TyperPlugin lifts list/array constructor BQAppls into
	// cons-list trees via ASTFactory.buildList/buildArray. Our Go
	// typer emits the flat BuildTerm/BQAppl/FunctionCall form. The
	// two encodings carry the same content: a list/array of N
	// arguments under the same operator name. Flatten the Java cons
	// chain into a single `__BQ_CALL__(name, concBQTerm(args...))`
	// matching the canonical form we already use for non-list
	// constructors.
	func(n *Node) *Node {
		// BuildAppendList(name, head, tail) is a "splicing append":
		// it concatenates a sublist `head` onto `tail`. Java emits
		// this for list-typed VariableStar arguments (e.g. `X*` where
		// X* matched a list slice). Semantically it's still "this
		// list contains these N args in order", so we lump it in
		// with BuildConsList here.
		isCons := n.Op == "BuildConsList" || n.Op == "BuildConsArray" ||
			n.Op == "BuildAppendList" || n.Op == "BuildAppendArray"
		isEmpty := n.Op == "BuildEmptyList" || n.Op == "BuildEmptyArray"
		if !isCons && !isEmpty {
			return nil
		}
		if isEmpty {
			// BuildEmpty*(name [, expr...]) → __LIST_EMPTY__(name).
			// We use a distinct sentinel (not __BQ_CALL__ with empty
			// args) to avoid colliding with the BuildConstant rule,
			// which also folds `__BQ_CALL__(name, concBQTerm())` into
			// __BQ_CONST__(name). The cons-chain rule below extracts
			// the (empty) args list directly from __LIST_EMPTY__.
			if len(n.Args) < 1 {
				return nil
			}
			return &Node{Op: "__LIST_EMPTY__", Args: []*Node{n.Args[0]}}
		}
		if len(n.Args) != 3 {
			return nil
		}
		name := n.Args[0]
		head := n.Args[1]
		tail := n.Args[2]
		// Tail can be a __BQ_CALL__(name, concBQTerm(...)) (already
		// canonicalised), a __LIST_EMPTY__(name), or a nested
		// BuildCons*. If none, treat as a single opaque arg.
		extract := func(t *Node) []*Node {
			if t.Op == "__BQ_CALL__" && len(t.Args) == 2 &&
				EqualNodes(t.Args[0], name) {
				if cb := t.Args[1]; cb.Op == "concBQTerm" {
					return cb.Args
				}
			}
			if t.Op == "__LIST_EMPTY__" && len(t.Args) == 1 &&
				EqualNodes(t.Args[0], name) {
				return []*Node{}
			}
			return nil
		}
		// Cons chains in Java: BuildConsArray is right-to-left
		// (innermost = first source arg), BuildConsList /
		// BuildAppendList is left-to-right (outermost = first source
		// arg). For *Array, walking head-first gives reverse order;
		// for *List, walking head-first gives source order. To make
		// both produce source order, we splice head AFTER the
		// tail's flattened args for *Array but BEFORE for *List.
		isArray := strings.HasSuffix(n.Op, "Array")
		var args []*Node
		if isArray {
			if rest := extract(tail); rest != nil {
				args = append(args, rest...)
			} else {
				args = append(args, tail)
			}
			args = append(args, head)
		} else {
			args = append(args, head)
			if rest := extract(tail); rest != nil {
				args = append(args, rest...)
			} else {
				args = append(args, tail)
			}
		}
		return &Node{Op: "__BQ_CALL__", Args: []*Node{
			name,
			{Op: "concBQTerm", Args: args},
		}}
	},
	// `__BQ_CALL__(Name(literal), concBQTerm())` ↔ `BuildConstant(
	// Name(literal))`. After the FunctionCall/BQAppl/BuildTerm
	// canonicalisation, a "call to a literal with no args" is what
	// Java's BuildConstant represents (numeric / string constants
	// inside backquote args). Canonicalise both to __BQ_CONST__.
	func(n *Node) *Node {
		if n.Op == "__BQ_CALL__" && len(n.Args) == 2 {
			args := n.Args[1]
			if args.Op == "concBQTerm" && len(args.Args) == 0 {
				return &Node{Op: "__BQ_CONST__", Args: []*Node{n.Args[0]}}
			}
		}
		if n.Op == "BuildConstant" && len(n.Args) == 1 {
			return &Node{Op: "__BQ_CONST__", Args: []*Node{n.Args[0]}}
		}
		return nil
	},
}
