package aterm

import (
	"strings"

	"tom/tomgo/stable/library/sharedobjects"
)

// ATermAppl represents a function application: a head AFun applied
// to zero-or-more sub-term arguments plus an optional annotation
// list. The Args slice is immutable once the term has been Built —
// every mutator returns a freshly built term instead.
type ATermAppl struct {
	fun     *AFun
	args    []ATerm
	annos   *ATermList // nil ⇒ no annotations
	hash    uint32
	factory *Factory
}

func (t *ATermAppl) Type() int             { return APPL }
func (t *ATermAppl) Hash() uint32          { return t.hash }
func (t *ATermAppl) Factory() *Factory     { return t.factory }
func (t *ATermAppl) GetAFun() *AFun        { return t.fun }
func (t *ATermAppl) GetArity() int         { return t.fun.arity }
func (t *ATermAppl) GetArguments() []ATerm { return t.args }
func (t *ATermAppl) GetArgument(i int) ATerm {
	return t.args[i]
}
func (t *ATermAppl) Name() string { return t.fun.name }

func (t *ATermAppl) String() string {
	var b strings.Builder
	b.WriteString(t.fun.String())
	if t.fun.arity > 0 {
		b.WriteByte('(')
		for i, a := range t.args {
			if i > 0 {
				b.WriteByte(',')
			}
			b.WriteString(a.String())
		}
		b.WriteByte(')')
	}
	if t.annos != nil && !t.annos.IsEmpty() {
		b.WriteByte('{')
		first := true
		for cur := t.annos; !cur.IsEmpty(); cur = cur.tail {
			if !first {
				b.WriteByte(',')
			}
			first = false
			b.WriteString(cur.head.String())
		}
		b.WriteByte('}')
	}
	return b.String()
}

func (t *ATermAppl) IsEqual(o ATerm) bool { return ATerm(t) == o }
func (t *ATermAppl) GetAnnotations() *ATermList {
	if t.annos == nil {
		return t.factory.emptyList
	}
	return t.annos
}
func (t *ATermAppl) SetAnnotations(annos *ATermList) ATerm {
	if annos == nil || annos.IsEmpty() {
		// Drop the annotation list entirely.
		return t.factory.makeApplAnno(t.fun, t.args, nil)
	}
	return t.factory.makeApplAnno(t.fun, t.args, annos)
}
func (t *ATermAppl) HasAnnotations() bool       { return t.annos != nil && !t.annos.IsEmpty() }
func (t *ATermAppl) Match(pattern string) []any { return matchPattern(t, pattern) }
func (t *ATermAppl) Make(args []any) ATerm      { return makePattern(t, args) }

// GetAnnotation looks up the value attached to `key` in the
// annotation list. Returns nil when there is no such annotation.
// Annotations are stored as a list of `[key, value]` pairs, matching
// the Java reference's pure ATermListImpl convention.
func (t *ATermAppl) GetAnnotation(key ATerm) ATerm {
	if t.annos == nil {
		return nil
	}
	return dictGet(t.annos, key)
}

// SetAnnotation returns a copy of `t` whose annotation list carries
// the (key, value) pair. Any prior value for that key is replaced.
func (t *ATermAppl) SetAnnotation(key, value ATerm) ATerm {
	annos := t.annos
	if annos == nil {
		annos = t.factory.emptyList
	}
	annos = dictPut(annos, key, value)
	return t.factory.makeApplAnno(t.fun, t.args, annos)
}

// RemoveAnnotation returns a copy of `t` with the entry for `key`
// dropped from the annotation list.
func (t *ATermAppl) RemoveAnnotation(key ATerm) ATerm {
	if t.annos == nil {
		return t
	}
	annos := dictRemove(t.annos, key)
	if annos.IsEmpty() {
		return t.factory.makeApplAnno(t.fun, t.args, nil)
	}
	return t.factory.makeApplAnno(t.fun, t.args, annos)
}

// SetArgument returns a new ATermAppl with position i replaced by arg.
func (t *ATermAppl) SetArgument(arg ATerm, i int) *ATermAppl {
	newArgs := make([]ATerm, len(t.args))
	copy(newArgs, t.args)
	newArgs[i] = arg
	return t.factory.makeApplAnno(t.fun, newArgs, t.annos)
}

func (t *ATermAppl) Equivalent(other sharedobjects.Term) bool {
	o, ok := other.(*ATermAppl)
	if !ok {
		return false
	}
	if t.fun != o.fun || len(t.args) != len(o.args) || t.annos != o.annos {
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
