package list

// This file is test data: TestListCells (listcells_test.go) generates
// testdata/listcells/List.gom with gomgo's user-module backend into a
// temporary module, copies this file next to it and runs `go test`
// there (with -bench to print the benchmarks).
//
// The list of a variadic operator is a chain of maximally-shared cons
// cells (Java Gom's Cons<op>/Empty<op>): building a list of n elements
// by repeated cons, or walking it with GetTail, costs O(n) in total.

import (
	"testing"
)

const n = 10000

func consList(n int) L {
	var l L = EmptyConc()
	for i := 0; i < n; i++ {
		e := MakeA()
		if i%2 == 0 {
			e = MakeB()
		}
		l = ConsConc(e, l)
	}
	return l
}

func flList(n int) T {
	var l T = EmptyFl()
	for i := 0; i < n; i++ {
		e := MakeT()
		if i%2 == 0 {
			e = MakeU()
		}
		l = ConsFl(e, l)
	}
	return l
}

// BenchmarkCons10000 builds a 10 000-element list by repeated
// ConsConc (Java's Consconc.make).
func BenchmarkCons10000(b *testing.B) {
	b.ReportAllocs()
	for i := 0; i < b.N; i++ {
		consList(n)
	}
}

// BenchmarkConsFL10000 is BenchmarkCons10000 on an FL operator (the
// default theory of `fl(T*)` in sort T: flattening on each cons).
func BenchmarkConsFL10000(b *testing.B) {
	b.ReportAllocs()
	for i := 0; i < b.N; i++ {
		flList(n)
	}
}

// BenchmarkGetTail10000 walks a 10 000-element list with GetTailconc
// (Tom's get_tail of the %oplist mapping).
func BenchmarkGetTail10000(b *testing.B) {
	l := consList(n)
	b.ReportAllocs()
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		count := 0
		for cur := l; !cur.IsEmptyconc(); cur = cur.GetTailconc() {
			count++
		}
		if count != n {
			b.Fatalf("walked %d elements, want %d", count, n)
		}
	}
}

// TestConsIsLinear checks that building and walking lists costs O(n):
// the bytes allocated per element do not grow with the length of the
// list, and walking allocates nothing.
func TestConsIsLinear(t *testing.T) {
	perElem := func(m int) float64 {
		r := testing.Benchmark(func(b *testing.B) {
			for i := 0; i < b.N; i++ {
				consList(m)
			}
		})
		return float64(r.AllocedBytesPerOp()) / float64(m)
	}
	small, large := perElem(1000), perElem(10000)
	if large > 2*small+64 {
		t.Errorf("cons allocates %.0f bytes per element for 10000 elements, %.0f for 1000: not linear", large, small)
	}
	l := consList(n)
	if allocs := testing.AllocsPerRun(10, func() {
		for cur := l; !cur.IsEmptyconc(); cur = cur.GetTailconc() {
		}
	}); allocs != 0 {
		t.Errorf("walking the list with GetTailconc allocates %v times", allocs)
	}
}

// TestTailsShared checks the maximal sharing of the cells: the tail of
// a list is the very list it was consed on, and equal lists built
// separately are the same pointer.
func TestTailsShared(t *testing.T) {
	tail := MakeConc(MakeB(), MakeA())
	l := ConsConc(MakeA(), tail)
	if l.GetTailconc() != tail {
		t.Fatalf("the tail of cons(a, l) is not l itself")
	}
	if MakeConc(MakeA(), MakeB(), MakeA()) != l {
		t.Fatalf("equal lists are not the same pointer")
	}
	long := consList(n)
	if ConsConc(MakeA(), long).GetTailconc() != long {
		t.Fatalf("the tail of a long list is copied")
	}
	if got := ConsConc(MakeA(), long).Length(); got != n+1 {
		t.Fatalf("Length = %d, want %d", got, n+1)
	}
}
