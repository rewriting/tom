package sl

// Mu is the fixed-point strategy combinator: it builds a recursive
// strategy by tying a [MuVar] in its body to its own body. The first
// VisitLight expands the Mu in-place (walks the strategy graph, finds
// every MuVar with a matching name, and points its Instance at the
// body); subsequent calls skip expansion.
//
//	Mu(MuVar("x"), body)  with body referring to MuVar("x") for recursion.
//
// Equivalent to Java's tom.library.sl.Mu.
type Mu struct {
	abstractCombinator
	expanded bool
}

const (
	muVarSlot  = 0 // the MuVar holding the binding name
	muBodySlot = 1 // the body strategy that may contain MuVars
)

// NewMu returns a Mu combinator with the given binding variable and
// body. The body may reference v (or any [MuVar] with the same name)
// to express recursion.
func NewMu(v *MuVar, body Strategy) Strategy {
	m := &Mu{}
	m.initSubterm(v, body)
	return m
}

func (m *Mu) VisitLight(subject any, intro Introspector) (any, error) {
	if !m.expanded {
		expandMu(m)
		m.expanded = true
	}
	return m.args[muBodySlot].VisitLight(subject, intro)
}

// expandMu walks the strategy tree rooted at root and binds every
// unbound [MuVar] to its enclosing Mu's body. The Java reference does
// this via a depth-first walk maintaining a stack of enclosing Mu
// combinators; we follow the same shape, since the strategy children
// expose ChildCount/ChildAt/SetChildAt.
//
// Note: this mutates the strategy graph in place. Mu's `expanded` flag
// guards against re-expansion on subsequent visits.
func expandMu(root Strategy) {
	visited := make(map[Strategy]struct{})
	stack := []*Mu{}
	expandWalk(root, nil, 0, visited, &stack)
}

func expandWalk(s Strategy, parent Strategy, childIdx int, visited map[Strategy]struct{}, stack *[]*Mu) {
	if _, seen := visited[s]; seen {
		return
	}
	// Java seeds with `parent` rather than `any`; do the same so we
	// match the de-duplication discipline exactly. In practice the
	// strategy graph is a tree (no shared sub-strategies) for our
	// downstream plugins, so this rarely matters.
	if parent != nil {
		visited[parent] = struct{}{}
	}

	if mu, ok := s.(*Mu); ok {
		body := mu.args[muBodySlot]
		muvar := mu.args[muVarSlot]
		*stack = append([]*Mu{mu}, *stack...) // push front
		expandWalk(body, mu, 0, visited, stack)
		expandWalk(muvar, nil, 0, visited, stack)
		*stack = (*stack)[1:] // pop front
		return
	}

	if mv, ok := s.(*MuVar); ok {
		if !mv.IsExpanded() {
			for _, m := range *stack {
				binding := m.args[muVarSlot].(*MuVar)
				if binding.Name == mv.Name {
					mv.Instance = m
					if parent != nil {
						// Java's shortcut: replace the MuVar slot in
						// `parent` with the Mu body, so further visits
						// see the body directly (no Mu/MuVar hop).
						parent.SetChildAt(childIdx, m.args[muBodySlot])
					}
					return
				}
			}
		}
		return
	}

	for i := range s.ChildCount() {
		expandWalk(s.ChildAt(i), s, i, visited, stack)
	}
}
