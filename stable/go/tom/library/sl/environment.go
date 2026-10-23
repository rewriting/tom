package sl

import (
	"fmt"
	"strconv"
	"strings"
)

// Status codes returned by [Strategy.VisitEnv] — Java's
// Environment.SUCCESS / FAILURE / IDENTITY.
const (
	StatusSuccess  = 0
	StatusFailure  = 1
	StatusIdentity = 2
)

const defaultEnvironmentLength = 8

// Environment is the zipper an environment-based visit
// ([Strategy.Visit], [Strategy.VisitEnv]) walks the subject with: the
// stack of the subterms from the root down to the current subject,
// and the child numbers (omega) taken at each step. A strategy reads
// and writes the current subject through it, and can ask for its
// [Position] in the root.
//
// Equivalent to Java's tom.library.sl.Environment. subterm[0] is the
// root; omega[0] is unused; omega[i] (i >= 1) is the 1-based number of
// the child subterm[i] is in subterm[i-1].
type Environment struct {
	current      int
	omega        []int
	subterm      []any
	introspector Introspector
	status       int
}

// NewEnvironment returns an empty environment using
// [VisitableIntrospector] (Java new Environment()).
func NewEnvironment() *Environment {
	return newEnvironment(defaultEnvironmentLength, VisitableIntrospector{})
}

// NewEnvironmentWith returns an empty environment using intro (Java
// new Environment(introspector)).
func NewEnvironmentWith(intro Introspector) *Environment {
	return newEnvironment(defaultEnvironmentLength, intro)
}

func newEnvironment(length int, intro Introspector) *Environment {
	return &Environment{
		omega:        make([]int, length+1),
		subterm:      make([]any, length+1),
		introspector: intro,
		status:       StatusSuccess,
	}
}

func (e *Environment) ensureLength(minLength int) {
	if minLength > len(e.omega) {
		max := max(len(e.omega)*2, minLength)
		newOmega := make([]int, max)
		newSubterm := make([]any, max)
		copy(newOmega, e.omega)
		copy(newSubterm, e.subterm)
		e.omega, e.subterm = newOmega, newSubterm
	}
}

// Clone returns an independent copy of e (Java clone()).
func (e *Environment) Clone() *Environment {
	c := *e
	c.omega = append([]int(nil), e.omega...)
	c.subterm = append([]any(nil), e.subterm...)
	return &c
}

// Equals reports whether o is an *Environment at the same depth with
// the same omegas and the same (identical) subterms on the stack —
// Java's Environment.equals(Object).
func (e *Environment) Equals(o any) bool {
	p, ok := o.(*Environment)
	if !ok || p == nil || e.current != p.current {
		return false
	}
	for i := 0; i <= e.current; i++ {
		if e.omega[i] != p.omega[i] || e.subterm[i] != p.subterm[i] {
			return false
		}
	}
	return true
}

func (e *Environment) GetStatus() int  { return e.status }
func (e *Environment) SetStatus(s int) { e.status = s }

// GetRoot returns the root of the subject (subterm[0]).
func (e *Environment) GetRoot() any { return e.subterm[0] }

// SetRoot replaces the root of the subject.
func (e *Environment) SetRoot(root any) { e.subterm[0] = root }

// GetCurrentStack returns the subterms from the root to the parent of
// the current subject.
func (e *Environment) GetCurrentStack() []any {
	v := make([]any, 0, e.Depth())
	for i := 0; i < e.Depth(); i++ {
		v = append(v, e.subterm[i])
	}
	return v
}

// GetAncestor returns the parent of the current subject.
func (e *Environment) GetAncestor() any { return e.subterm[e.current-1] }

// GetSubject returns the current subject.
func (e *Environment) GetSubject() any { return e.subterm[e.current] }

// SetSubject replaces the current subject.
func (e *Environment) SetSubject(s any) { e.subterm[e.current] = s }

func (e *Environment) GetIntrospector() Introspector      { return e.introspector }
func (e *Environment) SetIntrospector(intro Introspector) { e.introspector = intro }

// GetSubOmega returns the child number of the current subject in its
// parent (0 at the root).
func (e *Environment) GetSubOmega() int { return e.omega[e.current] }

// GetPosition returns the position of the current subject in the root.
func (e *Environment) GetPosition() *Position {
	return MakePositionFromSubarray(e.omega, 1, e.Depth())
}

// Depth returns the depth of the current subject (0 at the root).
func (e *Environment) Depth() int { return e.current }

// Up goes back to the parent, rebuilding it with the (possibly new)
// current subject as its child.
func (e *Environment) Up() {
	childIndex := e.omega[e.current] - 1
	child := e.subterm[e.current]
	e.current--
	e.subterm[e.current] = e.introspector.SetChildAt(e.subterm[e.current], childIndex, child)
}

// UpLocal goes back to the parent without rebuilding it.
func (e *Environment) UpLocal() { e.current-- }

// Down goes to the n-th child (1-based) of the current subject; n <=
// 0 does nothing, as in Java.
func (e *Environment) Down(n int) {
	if n > 0 {
		child := e.subterm[e.current]
		e.current++
		if e.current == len(e.omega) {
			e.ensureLength(e.current + 1)
		}
		e.omega[e.current] = n
		e.subterm[e.current] = e.introspector.GetChildAt(child, n-1)
	}
}

// GoToPosition moves to the absolute position p.
func (e *Environment) GoToPosition(p *Position) {
	e.FollowPath(p.Sub(e.GetPosition()))
}

// FollowPath follows the relative path, rebuilding the parents on the
// way up.
func (e *Environment) FollowPath(path Path) { e.genericFollowPath(path, false) }

// FollowPathLocal follows the relative path without rebuilding the
// parents on the way up.
func (e *Environment) FollowPathLocal(path Path) { e.genericFollowPath(path, true) }

func (e *Environment) genericFollowPath(path Path, local bool) {
	normalized := path.GetCanonicalPath().ToIntArray()
	length := len(normalized)
	for i := 0; i < length; i++ {
		if normalized[i] > 0 {
			e.Down(normalized[i])
			if ref, ok := e.subterm[e.current].(Path); ok && i+1 < length {
				// we do not want to follow the last reference
				e.genericFollowPath(ref, local)
			}
		} else if local {
			e.UpLocal()
		} else {
			e.Up()
		}
	}
}

// String renders the omega stack then the subterm stack, as Java does.
func (e *Environment) String() string {
	var r strings.Builder
	r.WriteString("[")
	for i := 0; i <= e.current; i++ {
		r.WriteString(strconv.Itoa(e.omega[i]))
		if i < e.current {
			r.WriteString(", ")
		}
	}
	r.WriteString("]\n[")
	for i := 0; i <= e.current; i++ {
		fmt.Fprint(&r, e.subterm[i])
		if i < e.current {
			r.WriteString(", ")
		}
	}
	r.WriteString("]")
	return r.String()
}
