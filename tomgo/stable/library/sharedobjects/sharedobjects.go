package sharedobjects

import (
	"fmt"
)

// Term is the contract a sharable value must satisfy to be hash-consed
// by a Factory. It mirrors the historical `shared.SharedObject` Java
// interface (see reports/phase2a-sharedobjects-survey.md):
//
//   - Hash       — structural, memoized typically.
//   - Equivalent — full structural equality with another Term.
//   - Duplicate  — clone of a prototype before it becomes shared.
//
// After a prototype has been fed through Factory.Build, the returned
// Term must be treated as immutable — comparing two Build'd results for
// equality reduces to comparing their interface identity.
type Term interface {
	Hash() uint32
	Equivalent(Term) bool
	Duplicate() Term
}

// Factory implements maximal sharing (hash-consing) for any value
// implementing Term. Single-threaded — there is no internal locking.
// The Tom/Gom compiler pipeline runs on a single goroutine; aterm
// uses one factory per parse session. If concurrent access is ever
// needed, wrap the calls in your own sync.Mutex.
type Factory struct {
	buckets map[uint32][]Term // hash -> chained terms; chain handles collisions
	count   int
}

// NewFactory returns a freshly initialized Factory.
func NewFactory() *Factory {
	return &Factory{buckets: make(map[uint32][]Term)}
}

// Build returns the unique, canonical instance equivalent to prototype.
// If no equivalent term has been seen yet, prototype is duplicated and
// the duplicate is stored and returned. Otherwise the existing instance
// is returned and the prototype is discarded.
func (f *Factory) Build(prototype Term) Term {
	h := prototype.Hash()
	for _, t := range f.buckets[h] {
		if prototype.Equivalent(t) {
			return t
		}
	}
	clone := prototype.Duplicate()
	f.buckets[h] = append(f.buckets[h], clone)
	f.count++
	return clone
}

// Contains reports whether t (by identity) is one of the canonical terms
// held by this Factory. It is mostly useful for tests and debug checks.
func (f *Factory) Contains(t Term) bool {
	for _, existing := range f.buckets[t.Hash()] {
		if existing == t {
			return true
		}
	}
	return false
}

// Stats captures a snapshot of the factory's state.
type Stats struct {
	NumTerms      int // total canonical terms stored
	NumBuckets    int // number of distinct hash values
	MaxChainLen   int // longest collision chain
	NumCollisions int // sum over buckets of len(chain)-1
}

// Stats returns a snapshot of the factory's internal state.
func (f *Factory) Stats() Stats {
	s := Stats{NumTerms: f.count, NumBuckets: len(f.buckets)}
	for _, chain := range f.buckets {
		if l := len(chain); l > 0 {
			if l > s.MaxChainLen {
				s.MaxChainLen = l
			}
			s.NumCollisions += l - 1
		}
	}
	return s
}

// String returns a short human-readable description of the factory's
// state — useful for `fmt.Println` debugging.
func (s Stats) String() string {
	return fmt.Sprintf("Factory{terms=%d buckets=%d collisions=%d maxChain=%d}",
		s.NumTerms, s.NumBuckets, s.NumCollisions, s.MaxChainLen)
}
