package sl

import (
	"strconv"
	"strings"
)

// Path is a sequence of moves in a tree: a positive integer n goes
// down to the n-th child (1-based), a negative one -n goes back up
// from the n-th child to its parent. [Position] is the canonical
// implementation (a path from the root that only goes down).
//
// Equivalent to Java's tom.library.sl.Path.
type Path interface {
	// Add returns the concatenation of this path and p.
	Add(p Path) Path
	// Sub returns the path that leads from p to this path:
	// p.Add(this.Sub(p)) and this designate the same node.
	Sub(p Path) Path
	// Inverse returns the path that undoes this one.
	Inverse() Path
	// GetCanonicalPath removes the down/up pairs that cancel out.
	GetCanonicalPath() Path
	Length() int
	GetHead() int
	GetTail() Path
	// Conc returns this path extended by the move i.
	Conc(i int) Path
	ToIntArray() []int
}

// Position is a path from the root of a subject to one of its nodes:
// the sequence of child numbers (1-based) followed downward. The root
// position is the empty sequence.
//
// Equivalent to Java's tom.library.sl.Position. A Position is
// immutable: every operation returns a fresh value.
type Position struct {
	omega []int
}

var rootPosition *Position

// MakePosition returns the root position (Java Position.make()).
func MakePosition() *Position {
	if rootPosition == nil {
		rootPosition = makePositionWithoutCopy([]int{})
	}
	return rootPosition
}

func makePositionWithoutCopy(array []int) *Position {
	return &Position{omega: array}
}

// MakePositionFromSubarray builds the position src[srcIndex :
// srcIndex+length] (Java Position.makeFromSubarray).
func MakePositionFromSubarray(src []int, srcIndex, length int) *Position {
	array := make([]int, length)
	copy(array, src[srcIndex:srcIndex+length])
	return makePositionWithoutCopy(array)
}

// MakePositionFromArray builds the position whose moves are array
// (copied) — Java Position.makeFromArray.
func MakePositionFromArray(array []int) *Position {
	return MakePositionFromSubarray(array, 0, len(array))
}

// MakePositionFromPath builds the position whose moves are the ones
// of p (Java Position.makeFromPath).
func MakePositionFromPath(p Path) *Position {
	return makePositionWithoutCopy(p.ToIntArray())
}

// IsPosition reports whether t is a *Position (the is_sort hook of
// the Go Position.tom mapping).
func IsPosition(t any) bool { _, ok := t.(*Position); return ok }

// Clone returns a copy of p.
func (p *Position) Clone() *Position {
	return MakePositionFromArray(p.omega)
}

// HashCode mirrors Java's Position.hashCode.
func (p *Position) HashCode() int {
	h := int32(0)
	for _, o := range p.omega {
		h = h*31 + int32(o)
	}
	return int(h)
}

// Equals reports whether o is a *Position designating the same node
// (the canonical paths are compared) — Java's Position.equals(Object).
func (p *Position) Equals(o any) bool {
	q, ok := o.(*Position)
	if !ok || q == nil {
		return false
	}
	p1 := p.GetCanonicalPath().(*Position)
	p2 := q.GetCanonicalPath().(*Position)
	if p1.Length() != p2.Length() {
		return false
	}
	for i := range p1.omega {
		if p1.omega[i] != p2.omega[i] {
			return false
		}
	}
	return true
}

// Compare orders positions lexicographically: -1, 0 or 1.
func (p *Position) Compare(path Path) int {
	q := MakePositionFromPath(path)
	for i := 0; i < p.Length(); i++ {
		if i == q.Length() || p.omega[i] > q.omega[i] {
			return 1
		}
		if p.omega[i] < q.omega[i] {
			return -1
		}
	}
	if p.Length() == q.Length() {
		return 0
	}
	return -1
}

// String renders the position as Java does: "[1, 2]".
func (p *Position) String() string {
	var r strings.Builder
	r.WriteString("[")
	for i, o := range p.omega {
		r.WriteString(strconv.Itoa(o))
		if i < len(p.omega)-1 {
			r.WriteString(", ")
		}
	}
	r.WriteString("]")
	return r.String()
}

func (p *Position) Add(q Path) Path {
	if q.Length() == 0 {
		return p.Clone()
	}
	ap := q.ToIntArray()
	merge := make([]int, 0, p.Length()+len(ap))
	merge = append(merge, p.omega...)
	merge = append(merge, ap...)
	return makePositionWithoutCopy(merge)
}

func (p *Position) Sub(q Path) Path {
	return MakePositionFromPath(q).Inverse().Add(p)
}

func (p *Position) Inverse() Path {
	n := p.Length()
	inverse := make([]int, n)
	for i := 0; i < n; i++ {
		inverse[n-(i+1)] = -p.omega[i]
	}
	return makePositionWithoutCopy(inverse)
}

func (p *Position) Length() int { return len(p.omega) }

func (p *Position) GetHead() int { return p.omega[0] }

func (p *Position) GetTail() Path {
	if p.Length() == 0 {
		panic("Empty list has no tail")
	}
	return MakePositionFromSubarray(p.omega, 1, p.Length()-1)
}

func (p *Position) Conc(i int) Path {
	result := make([]int, 0, p.Length()+1)
	result = append(result, p.omega...)
	return makePositionWithoutCopy(append(result, i))
}

func (p *Position) GetCanonicalPath() Path {
	n := p.Length()
	if n == 0 {
		return MakePosition()
	}
	stack := make([]int, n)
	top := -1
	for i := 0; i < n; i++ {
		if top >= 0 && stack[top] == -p.omega[i] {
			top--
		} else {
			top++
			stack[top] = p.omega[i]
		}
	}
	return MakePositionFromSubarray(stack, 0, top+1)
}

func (p *Position) ToIntArray() []int {
	array := make([]int, p.Length())
	copy(array, p.omega)
	return array
}

// Up returns the position of the parent.
func (p *Position) Up() *Position {
	return MakePositionFromSubarray(p.omega, 0, p.Length()-1)
}

// Down returns the position of the i-th child (1-based).
func (p *Position) Down(i int) *Position {
	return p.Conc(i).(*Position)
}

// HasPrefix reports whether prefix is a prefix of p.
func (p *Position) HasPrefix(prefix *Position) bool {
	if p.Length() < prefix.Length() {
		return false
	}
	for i, o := range prefix.omega {
		if o != p.omega[i] {
			return false
		}
	}
	return true
}

// ChangePrefix replaces the prefix oldprefix of p by prefix; nil when
// oldprefix is not a prefix of p.
func (p *Position) ChangePrefix(oldprefix, prefix *Position) *Position {
	if !p.HasPrefix(oldprefix) {
		return nil
	}
	delta := oldprefix.Length()
	array := make([]int, 0, p.Length()-delta+prefix.Length())
	array = append(array, prefix.omega...)
	array = append(array, p.omega[delta:]...)
	return makePositionWithoutCopy(array)
}

// GetOmega returns the strategy that applies v at this position:
// Omega(o1, Omega(o2, … Omega(on, v))).
func (p *Position) GetOmega(v Strategy) Strategy {
	res := v
	for i := p.Length() - 1; i >= 0; i-- {
		res = NewOmega(p.omega[i], res)
	}
	return res
}

// GetOmegaPath returns the strategy that applies v at each node on
// the way from the root down to this position (the position itself
// excluded, as in Java).
func (p *Position) GetOmegaPath(v Strategy) Strategy {
	return p.getOmegaPathAux(v, 0)
}

func (p *Position) getOmegaPathAux(v Strategy, i int) Strategy {
	if i >= p.Length()-1 {
		return v
	}
	return NewSequence(NewOmega(p.omega[i], p.getOmegaPathAux(v, i+1)), v)
}

// GetReplace returns the strategy that replaces the subterm at this
// position by t.
func (p *Position) GetReplace(t any) Strategy {
	r := &replaceStrategy{term: t}
	r.initSubterm()
	return p.GetOmega(r)
}

// GetSubterm returns the strategy that, applied to a subject, returns
// its subterm at this position.
func (p *Position) GetSubterm() Strategy {
	s := &subtermStrategy{position: p}
	s.initSubterm()
	return s
}

// replaceStrategy is the anonymous Identity of Java's getReplace.
type replaceStrategy struct {
	abstractCombinator
	term any
}

func (r *replaceStrategy) VisitLight(any, Introspector) (any, error) { return r.term, nil }
func (r *replaceStrategy) VisitEnv(Introspector) int {
	r.environment.SetSubject(r.term)
	return StatusSuccess
}
func (r *replaceStrategy) Visit(subject any, intro Introspector) (any, error) {
	return VisitStrategy(r, subject, intro)
}
func (r *replaceStrategy) VisitWith(env *Environment, intro Introspector) (any, error) {
	return VisitStrategyWith(r, env, intro)
}

// captureStrategy is the anonymous Identity of Java's getSubterm: it
// records the subject it is applied to.
type captureStrategy struct {
	abstractCombinator
	ref *any
}

func newCaptureStrategy(ref *any) *captureStrategy {
	c := &captureStrategy{ref: ref}
	c.initSubterm()
	return c
}

func (c *captureStrategy) VisitLight(v any, _ Introspector) (any, error) {
	*c.ref = v
	return v, nil
}
func (c *captureStrategy) VisitEnv(Introspector) int {
	*c.ref = c.environment.GetSubject()
	return StatusSuccess
}
func (c *captureStrategy) Visit(subject any, intro Introspector) (any, error) {
	return VisitStrategy(c, subject, intro)
}
func (c *captureStrategy) VisitWith(env *Environment, intro Introspector) (any, error) {
	return VisitStrategyWith(c, env, intro)
}

// subtermStrategy is the anonymous combinator of Java's getSubterm.
type subtermStrategy struct {
	abstractCombinator
	position *Position
}

func (s *subtermStrategy) VisitLight(subject any, intro Introspector) (any, error) {
	var ref any
	if _, err := s.position.GetOmega(newCaptureStrategy(&ref)).VisitLight(subject, intro); err != nil {
		return nil, err
	}
	return ref, nil
}

func (s *subtermStrategy) VisitEnv(intro Introspector) int {
	var ref any
	str := s.position.GetOmega(newCaptureStrategy(&ref))
	if _, err := str.Visit(s.environment.GetRoot(), intro); err != nil {
		return StatusFailure
	}
	s.environment.SetSubject(ref)
	return StatusSuccess
}

func (s *subtermStrategy) Visit(subject any, intro Introspector) (any, error) {
	return VisitStrategy(s, subject, intro)
}
func (s *subtermStrategy) VisitWith(env *Environment, intro Introspector) (any, error) {
	return VisitStrategyWith(s, env, intro)
}
