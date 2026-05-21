package sl

// MuVar is the named-variable side of the [Mu] fixed-point combinator.
// Until [Mu] expansion, MuVar is an "unbound" leaf that fails on any
// VisitLight. Expansion sets Instance to the recursive body, after
// which VisitLight delegates to it.
//
// MuVar instances are matched by name: two MuVars are considered the
// same variable if their names match, regardless of identity. This
// lets users write a strategy graph with multiple MuVar("x") leaves
// and have all of them bind to the same Mu body.
//
// Equivalent to Java's tom.library.sl.MuVar.
type MuVar struct {
	abstractCombinator
	Name     string
	Instance Strategy // nil until parent Mu is expanded
}

func NewMuVar(name string) *MuVar {
	m := &MuVar{Name: name}
	m.initSubterm()
	return m
}

func (m *MuVar) VisitLight(subject any, intro Introspector) (any, error) {
	if m.Instance == nil {
		return nil, ErrVisitFailure
	}
	return m.Instance.VisitLight(subject, intro)
}

// IsExpanded reports whether this MuVar has been bound to a Mu body
// by [Mu.expand].
func (m *MuVar) IsExpanded() bool { return m.Instance != nil }
