package aterm

// Type constants mirror the Java ATerm interface — every term reports
// its variant via Type().
const (
	APPL        = 1
	INT         = 2
	REAL        = 3
	LIST        = 4
	PLACEHOLDER = 5
	BLOB        = 6
	AFUN        = 7
	LONG        = 8
)

// ATerm is the common surface for every term flavour. Hash-consing
// makes two equal terms share their Go pointer, so `t1 == t2` is the
// idiomatic equality check; IsEqual is provided for parity with the
// Java reference.
type ATerm interface {
	Type() int
	IsEqual(other ATerm) bool
	Hash() uint32
	String() string

	// Match parses `pattern` and tries to match it against the
	// receiver. Returns the list of captured sub-values on success
	// (string for <fun>, int64 for <int>, float64 for <real>,
	// ATerm for <term>/<list>/<placeholder>, etc.), or nil on
	// failure.
	Match(pattern string) []any

	// MatchTerm is the same as Match but takes a pre-parsed pattern
	// term. Useful when the same pattern is applied many times —
	// callers can parse it once and reuse the cached pointer.
	MatchTerm(pattern ATerm) []any

	// Make builds a term from `pattern` (parsed once) using the
	// receiver's factory and the supplied args list as fill-ins.
	// It's primarily here so callers can hop from factory.Make("X",
	// args) to term.Make("X", args) when they already have a term
	// handy.
	Make(args []any) ATerm

	// Factory returns the factory that created this term.
	Factory() *Factory

	// Annotations stubs — see package doc. The pure-equality tests
	// in Test1 only exercise empty annotations.
	GetAnnotations() *ATermList
	SetAnnotations(annos *ATermList) ATerm
	HasAnnotations() bool
}
