package sharedobjects

import (
	"sync"
	"testing"
)

// peano is a minimal Term implementation used to exercise the Factory:
// either Zero() or Suc(arg). Two distinct goroutines must agree on the
// canonical instance for any structurally equal value.
type peano struct {
	symbol string // "zero" or "suc"
	arg    *peano // nil when symbol == "zero"
	hash   uint32 // memoized
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

// TestFactory_Sequential exercises repeated Build calls on the same
// value from a single goroutine — the factory itself is single-threaded
// (mutex removed for simplicity), so this just pins the sharing
// invariant: equal prototypes return the SAME pointer no matter how
// many times Build is invoked.
func TestFactory_Sequential(t *testing.T) {
	f := NewFactory()
	const each = 50
	const repeats = 16
	results := make([]*peano, repeats)
	for i := 0; i < repeats; i++ {
		results[i] = makeNat(f, each)
	}
	for i := 1; i < repeats; i++ {
		if results[i] != results[0] {
			t.Fatalf("iteration %d got a distinct canonical instance for the same value", i)
		}
	}
	if got := f.Stats().NumTerms; got != each+1 {
		t.Fatalf("expected %d unique terms, got %d", each+1, got)
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

// The factory is safe for concurrent use, and terms built concurrently
// are still maximally shared (Java's SharedObjectFactory is thread-safe;
// test/java/gom/TestGomMultithread.t). Run with -race to check the locking.
func TestFactory_Concurrent(t *testing.T) {
	f := NewFactory()
	const goroutines, depth = 16, 50
	results := make([]*peano, goroutines)
	var wg sync.WaitGroup
	for g := 0; g < goroutines; g++ {
		wg.Add(1)
		go func(g int) {
			defer wg.Done()
			for i := 0; i < 20; i++ {
				results[g] = makeNat(f, depth)
				_ = f.Stats()
				_ = f.Contains(results[g])
			}
		}(g)
	}
	wg.Wait()
	for g := 1; g < goroutines; g++ {
		if results[g] != results[0] {
			t.Fatalf("goroutine %d built a different instance", g)
		}
	}
	if n := f.Stats().NumTerms; n != depth+1 {
		t.Fatalf("NumTerms = %d, want %d", n, depth+1)
	}
}

// TestBuiltinHashes: the hashes of builtin list elements are computed
// directly, and tell apart values that differ only in their high bits.
func TestBuiltinHashes(t *testing.T) {
	if IntHash(1) == IntHash(1<<32+1) || IntHash(1) == IntHash(2) || IntHash(-1) == IntHash(1) {
		t.Error("IntHash collides on simple values")
	}
	if IntHash(7) != IntHash(7) || Float64Hash(1.5) != Float64Hash(1.5) || Float32Hash(2.5) != Float32Hash(2.5) {
		t.Error("builtin hashes are not deterministic")
	}
	if BoolHash(true) == BoolHash(false) || Float64Hash(1.5) == Float64Hash(2.5) {
		t.Error("builtin hashes collide")
	}
	if n := testing.AllocsPerRun(100, func() { IntHash(42); Float64Hash(4.2); StringHash("abc") }); n != 0 {
		t.Errorf("builtin hashes allocate (%v allocs)", n)
	}
}
