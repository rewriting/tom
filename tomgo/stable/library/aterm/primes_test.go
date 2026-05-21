package aterm

import (
	"testing"
)

// primesGenerator wraps the sieve from TestPrimes.java, faithful
// down to "1 is in the primes list" (an artefact of the Java
// implementation we keep so the output matches byte-for-byte).
type primesGenerator struct {
	factory *Factory
}

func (g *primesGenerator) generateNumbers(max int) *ATermList {
	out := g.factory.emptyList
	for i := max; i > 0; i-- {
		out = g.factory.MakeList(g.factory.MakeInt(i), out)
	}
	return out
}

func (g *primesGenerator) filterMultiples(n int, numbers *ATermList) *ATermList {
	length := numbers.GetLength()
	for i := 0; i < length; i++ {
		el := numbers.ElementAt(i).(*ATermInt)
		if el.GetInt()%n == 0 {
			length--
			numbers = numbers.RemoveElementAt(i)
			i-- // re-test the new element at position i
		}
	}
	return numbers
}

func (g *primesGenerator) filterNonPrimes(numbers *ATermList) *ATermList {
	primes := g.factory.emptyList
	numbers = numbers.GetNext()
	for !numbers.IsEmpty() {
		prime := numbers.GetFirst().(*ATermInt)
		numbers = g.filterMultiples(prime.GetInt(), numbers)
		primes = primes.Append(prime)
	}
	return g.factory.MakeList(g.factory.MakeInt(1), primes)
}

// getPrimes returns 1 followed by every prime ≤ max. Faithful to
// the Java reference: 1 is included even though it's not prime,
// because the algorithm walks indices and 1 acts as a sentinel.
func (g *primesGenerator) getPrimes(max int) *ATermList {
	primes := g.generateNumbers(max)
	primes = g.filterNonPrimes(primes)
	return primes
}

func TestPrimes_FirstThirty(t *testing.T) {
	f := NewFactory()
	g := &primesGenerator{factory: f}
	got := g.getPrimes(30)
	want := f.Parse("[1,2,3,5,7,11,13,17,19,23,29]")
	if ATerm(got) != want {
		t.Errorf("primes(30) = %s, want %s", got.String(), want.String())
	}
}

func TestPrimes_FiveHundredCount(t *testing.T) {
	f := NewFactory()
	g := &primesGenerator{factory: f}
	got := g.getPrimes(500)
	// 95 primes ≤ 500 + the sentinel `1` = 96.
	if got.GetLength() != 96 {
		t.Errorf("primes(500).length = %d, want 96", got.GetLength())
	}
}

// Sharing pin: running the sieve twice produces the same canonical
// list pointer. This validates that filterMultiples / append /
// makeList all funnel through the factory without leaking.
func TestPrimes_Sharing(t *testing.T) {
	f := NewFactory()
	g := &primesGenerator{factory: f}
	a := g.getPrimes(50)
	b := g.getPrimes(50)
	if a != b {
		t.Fatalf("getPrimes(50) computed twice produced distinct canonical lists")
	}
}
