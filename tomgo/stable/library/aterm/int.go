package aterm

import (
	"strconv"

	"tom/tomgo/stable/library/sharedobjects"
)

// ATermInt wraps a Go int64 (the Java reference uses Java's int but
// we widen to int64 here — see ATermLong for the explicit 64-bit
// variant; ATermInt keeps its narrower range only via its String()
// output which omits the trailing 'L').
type ATermInt struct {
	value   int32
	hash    uint32
	factory *Factory
}

func (t *ATermInt) Type() int           { return INT }
func (t *ATermInt) Hash() uint32        { return t.hash }
func (t *ATermInt) Factory() *Factory   { return t.factory }
func (t *ATermInt) Value() int          { return int(t.value) }
func (t *ATermInt) GetInt() int         { return int(t.value) }
func (t *ATermInt) String() string      { return strconv.FormatInt(int64(t.value), 10) }
func (t *ATermInt) IsEqual(o ATerm) bool { return ATerm(t) == o }
func (t *ATermInt) GetAnnotations() *ATermList { return t.factory.emptyList }
func (t *ATermInt) SetAnnotations(_ *ATermList) ATerm { return t }
func (t *ATermInt) HasAnnotations() bool { return false }

func (t *ATermInt) Match(pattern string) []any { return matchPattern(t, pattern) }
func (t *ATermInt) Make(args []any) ATerm     { return makePattern(t, args) }

func (t *ATermInt) Equivalent(other sharedobjects.Term) bool {
	o, ok := other.(*ATermInt)
	return ok && t.value == o.value
}

func (t *ATermInt) Duplicate() sharedobjects.Term {
	clone := *t
	return &clone
}

// ATermLong is the 64-bit sibling. Its textual form has a trailing 'L'.
type ATermLong struct {
	value   int64
	hash    uint32
	factory *Factory
}

func (t *ATermLong) Type() int            { return LONG }
func (t *ATermLong) Hash() uint32         { return t.hash }
func (t *ATermLong) Factory() *Factory    { return t.factory }
func (t *ATermLong) GetLong() int64       { return t.value }
func (t *ATermLong) Value() int64         { return t.value }
func (t *ATermLong) String() string       { return strconv.FormatInt(t.value, 10) }
func (t *ATermLong) IsEqual(o ATerm) bool { return ATerm(t) == o }
func (t *ATermLong) GetAnnotations() *ATermList       { return t.factory.emptyList }
func (t *ATermLong) SetAnnotations(_ *ATermList) ATerm { return t }
func (t *ATermLong) HasAnnotations() bool             { return false }
func (t *ATermLong) Match(pattern string) []any       { return matchPattern(t, pattern) }
func (t *ATermLong) Make(args []any) ATerm            { return makePattern(t, args) }

func (t *ATermLong) Equivalent(other sharedobjects.Term) bool {
	o, ok := other.(*ATermLong)
	return ok && t.value == o.value
}

func (t *ATermLong) Duplicate() sharedobjects.Term {
	clone := *t
	return &clone
}

// ATermReal — IEEE-754 double, hashed via its bit pattern.
type ATermReal struct {
	value   float64
	hash    uint32
	factory *Factory
}

func (t *ATermReal) Type() int           { return REAL }
func (t *ATermReal) Hash() uint32        { return t.hash }
func (t *ATermReal) Factory() *Factory   { return t.factory }
func (t *ATermReal) GetReal() float64    { return t.value }
func (t *ATermReal) Value() float64      { return t.value }
func (t *ATermReal) String() string      { return strconv.FormatFloat(t.value, 'g', -1, 64) }
func (t *ATermReal) IsEqual(o ATerm) bool { return ATerm(t) == o }
func (t *ATermReal) GetAnnotations() *ATermList       { return t.factory.emptyList }
func (t *ATermReal) SetAnnotations(_ *ATermList) ATerm { return t }
func (t *ATermReal) HasAnnotations() bool             { return false }
func (t *ATermReal) Match(pattern string) []any       { return matchPattern(t, pattern) }
func (t *ATermReal) Make(args []any) ATerm            { return makePattern(t, args) }

func (t *ATermReal) Equivalent(other sharedobjects.Term) bool {
	o, ok := other.(*ATermReal)
	return ok && t.value == o.value
}

func (t *ATermReal) Duplicate() sharedobjects.Term {
	clone := *t
	return &clone
}
