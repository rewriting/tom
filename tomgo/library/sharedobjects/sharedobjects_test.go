package sharedobjects

import (
	"sync"
	"testing"
)

// peano is a minimal Term implementation used to exercise the Factory:
// either Zero() or Suc(arg). Two distinct goroutines must agree on the
// canonical instance for any structurally equal value.
type peano struct {
	symbol string  // "zero" or "suc"
	arg    *peano  // nil when symbol == "zero"
	hash   uint32  // memoized
}

func newZero() *peano {
	return &peano{symbol: "zero", hash: StringHash("zero")}
}

func newSuc(arg *peano) *peano {
	return &peano{
		symbol: "suc",
		arg:    arg,
		hash:   MixSymbol(StringHash("suc"), []uint32{arg.hash}),
	}
}

func (p *peano) Hash() uint32 { return p.hash }

func (p *peano) Equivalent(other Term) bool {
	o, ok := other.(*peano)
	if !ok {
		return false
	}
	if p.symbol != o.symbol {
		return false
	}
	if p.arg == nil && o.arg == nil {
		return true
	}
	if p.arg == nil || o.arg == nil {
		return false
	}
	// Equivalent sub-terms must already be the same canonical instance.
	return p.arg == o.arg
}

func (p *peano) Duplicate() Term {
	clone := *p
	return &clone
}

func makeNat(f *Factory, n int) *peano {
	t := f.Build(newZero()).(*peano)
	for i := 0; i < n; i++ {
		t = f.Build(newSuc(t)).(*peano)
	}
	return t
}

func TestFactory_Sharing(t *testing.T) {
	f := NewFactory()
	a := makeNat(f, 5)
	b := makeNat(f, 5)
	if a != b {
		t.Fatalf("structurally equal naturals should share: %p != %p", a, b)
	}
	c := makeNat(f, 6)
	if a == c {
		t.Fatalf("distinct naturals must not share")
	}
	// Build with an equivalent prototype should still return the existing
	// instance — not a clone of the prototype.
	again := f.Build(newSuc(makeNat(f, 4)))
	if again != a {
		t.Fatalf("re-Build of equivalent prototype should hit the cache")
	}
}

func TestFactory_DistinctSymbolsDoNotShare(t *testing.T) {
	f := NewFactory()
	zero := f.Build(newZero())
	one := f.Build(newSuc(zero.(*peano)))
	if zero == one {
		t.Fatal("zero() and suc(zero()) must not be equal pointers")
	}
}

func TestFactory_Contains(t *testing.T) {
	f := NewFactory()
	z := f.Build(newZero())
	if !f.Contains(z) {
		t.Fatal("Contains must report true for an instance returned by Build")
	}
	loose := newZero()
	if f.Contains(loose) {
		t.Fatal("Contains must compare by identity, not equivalence")
	}
}

func TestFactory_Stats(t *testing.T) {
	f := NewFactory()
	_ = makeNat(f, 10) // creates 11 unique terms: zero + suc^1..suc^10
	s := f.Stats()
	if s.NumTerms != 11 {
		t.Errorf("expected 11 stored terms, got %d", s.NumTerms)
	}
	if s.NumBuckets == 0 || s.NumBuckets > s.NumTerms {
		t.Errorf("unexpected NumBuckets=%d for %d terms", s.NumBuckets, s.NumTerms)
	}
	_ = s.String() // smoke test
}

func TestFactory_Concurrent(t *testing.T) {
	f := NewFactory()
	const goroutines = 16
	const each = 50
	var wg sync.WaitGroup
	results := make([]*peano, goroutines)
	for i := 0; i < goroutines; i++ {
		i := i
		wg.Add(1)
		go func() {
			defer wg.Done()
			results[i] = makeNat(f, each)
		}()
	}
	wg.Wait()
	for i := 1; i < goroutines; i++ {
		if results[i] != results[0] {
			t.Fatalf("goroutine %d got a distinct canonical instance for the same value", i)
		}
	}
	if got := f.Stats().NumTerms; got != each+1 {
		t.Fatalf("expected %d unique terms after concurrent insertion, got %d", each+1, got)
	}
}

func TestOneAtATime_Stable(t *testing.T) {
	// Sanity: same input → same output, and order matters.
	a := OneAtATime([]uint32{1, 2, 3})
	b := OneAtATime([]uint32{1, 2, 3})
	c := OneAtATime([]uint32{3, 2, 1})
	if a != b {
		t.Errorf("non-deterministic: %x vs %x", a, b)
	}
	if a == c {
		t.Errorf("order-insensitive hash, that's wrong: %x", a)
	}
}
