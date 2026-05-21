package aterm

import (
	"strings"

	"tom/tomgo/stable/library/sharedobjects"
)

// ATermAppl represents a function application: a head AFun applied
// to zero-or-more sub-term arguments. The Args slice is immutable
// once the term has been Built (any mutator returns a freshly built
// term instead).
type ATermAppl struct {
	fun     *AFun
	args    []ATerm
	hash    uint32
	factory *Factory
}

func (t *ATermAppl) Type() int           { return APPL }
func (t *ATermAppl) Hash() uint32        { return t.hash }
func (t *ATermAppl) Factory() *Factory   { return t.factory }
func (t *ATermAppl) GetAFun() *AFun      { return t.fun }
func (t *ATermAppl) GetArity() int       { return t.fun.arity }
func (t *ATermAppl) GetArguments() []ATerm { return t.args }
func (t *ATermAppl) GetArgument(i int) ATerm {
	return t.args[i]
}
func (t *ATermAppl) Name() string { return t.fun.name }

func (t *ATermAppl) String() string {
	var b strings.Builder
	b.WriteString(t.fun.String())
	if t.fun.arity == 0 {
		// Java's printer omits `()` on nullary applications (both
		// quoted and unquoted), since the parser's appl-after-name
		// path elects nullary by default when no `(` follows.
		return b.String()
	}
	b.WriteByte('(')
	for i, a := range t.args {
		if i > 0 {
			b.WriteByte(',')
		}
		b.WriteString(a.String())
	}
	b.WriteByte(')')
	return b.String()
}

func (t *ATermAppl) IsEqual(o ATerm) bool { return ATerm(t) == o }
func (t *ATermAppl) GetAnnotations() *ATermList       { return t.factory.emptyList }
func (t *ATermAppl) SetAnnotations(_ *ATermList) ATerm { return t }
func (t *ATermAppl) HasAnnotations() bool             { return false }
func (t *ATermAppl) Match(pattern string) []any       { return matchPattern(t, pattern) }
func (t *ATermAppl) Make(args []any) ATerm            { return makePattern(t, args) }

// SetArgument returns a new ATermAppl with position i replaced by arg.
func (t *ATermAppl) SetArgument(arg ATerm, i int) *ATermAppl {
	newArgs := make([]ATerm, len(t.args))
	copy(newArgs, t.args)
	newArgs[i] = arg
	return t.factory.MakeAppl(t.fun, newArgs...)
}

func (t *ATermAppl) Equivalent(other sharedobjects.Term) bool {
	o, ok := other.(*ATermAppl)
	if !ok {
		return false
	}
	if t.fun != o.fun || len(t.args) != len(o.args) {
		return false
	}
	for i := range t.args {
		if t.args[i] != o.args[i] {
			return false
		}
	}
	return true
}

func (t *ATermAppl) Duplicate() sharedobjects.Term {
	clone := *t
	clone.args = append([]ATerm(nil), t.args...)
	return &clone
}
