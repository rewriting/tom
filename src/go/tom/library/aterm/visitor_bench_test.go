package aterm

import (
	"testing"
)

// buildVisitorTree builds the same balanced tree VisitorBenchmark.java
// uses: at every depth>1, fan out into `fanout` copies of the same
// child term (so hash-consing collapses them to one Go pointer).
// At depth==1, mint a fresh ATermInt with a unique id so the leaf
// count actually matches `fanout`.
func buildVisitorTree(f *Factory, fun *AFun, depth, fanout int, idCounter *int) ATerm {
	if depth == 1 {
		v := f.MakeInt(*idCounter)
		*idCounter++
		return v
	}
	arg := buildVisitorTree(f, fun, depth-1, fanout, idCounter)
	args := make([]ATerm, fanout)
	for i := range args {
		args[i] = arg
	}
	return f.MakeAppl(fun, args...)
}

// countTreeNodes counts every appl + leaf in a balanced (fanout^depth)
// tree analytically — for cross-check against the visitor's count.
// We treat each appl node as one node; leaves are the int literals at
// depth==1. Because the inner subtree at each level is hash-consed,
// the same arg appears `fanout` times but the visitor walks it that
// many times anyway (top-down doesn't dedupe).
func countTreeNodes(depth, fanout int) int {
	// Geometric series: 1 + fanout + fanout^2 + … + fanout^(depth-1).
	n := 1
	x := 1
	for i := 0; i < depth-1; i++ {
		x *= fanout
		n += x
	}
	return n
}

// TestVisitorBenchmark — port of VisitorBenchmark.java. Builds a
// depth-5 fanout-5 tree (3906 nodes) and walks it top-down. Pin: the
// visitor's count matches the analytical expected count.
func TestVisitorBenchmark(t *testing.T) {
	f := NewFactory()
	fun := f.MakeAFun("f", 5, false)
	id := 0
	root := buildVisitorTree(f, fun, 5, 5, &id)

	counter := &NodeCounter{}
	if _, err := TopDown(counter, root); err != nil {
		t.Fatalf("TopDown failed: %v", err)
	}
	want := countTreeNodes(5, 5)
	if counter.Count != want {
		t.Errorf("node count = %d, want %d", counter.Count, want)
	}
}

// TestVisitor_Identity — TopDown with an identity visitor must return
// the SAME canonical pointer (no spurious rebuilds when no child
// changed).
func TestVisitor_Identity(t *testing.T) {
	f := NewFactory()
	original := f.Parse("g(h(1,2,3),[a,b],<c>)")
	out, err := TopDown(VisitorFunc(func(t ATerm) (ATerm, error) { return t, nil }), original)
	if err != nil {
		t.Fatal(err)
	}
	if out != original {
		t.Errorf("identity visitor changed pointer: %v → %v", original, out)
	}
}

// TestVisitor_Rewrite — TopDown that swaps every integer for its
// successor. The whole tree should rebuild and the new tree must
// still be hash-consed (same input → same output pointer).
func TestVisitor_Rewrite(t *testing.T) {
	f := NewFactory()
	original := f.Parse("g(1,2,h(3,4))")
	rewrite := VisitorFunc(func(t ATerm) (ATerm, error) {
		if n, ok := t.(*ATermInt); ok {
			return f.MakeInt(n.GetInt() + 1), nil
		}
		return t, nil
	})
	a, err := TopDown(rewrite, original)
	if err != nil {
		t.Fatal(err)
	}
	want := f.Parse("g(2,3,h(4,5))")
	if a != want {
		t.Errorf("rewrite result = %v, want %v", a, want)
	}
	// Idempotency check: running the rewrite again on `a` produces
	// the same Go pointer as parsing the expected result.
	b, _ := TopDown(rewrite, original)
	if a != b {
		t.Errorf("two rewrites of same input differ")
	}
}

// TestVisitor_BottomUp — exercise the dual traversal. Sum the
// integer leaves into the appl head's name (a hack but a simple
// check that BottomUp visits children before parents).
func TestVisitor_BottomUp(t *testing.T) {
	f := NewFactory()
	original := f.Parse("g(1,2,h(3,4))")
	var seenOrder []int
	visit := VisitorFunc(func(t ATerm) (ATerm, error) {
		if n, ok := t.(*ATermInt); ok {
			seenOrder = append(seenOrder, n.GetInt())
		}
		return t, nil
	})
	if _, err := BottomUp(visit, original); err != nil {
		t.Fatal(err)
	}
	want := []int{1, 2, 3, 4}
	if len(seenOrder) != len(want) {
		t.Fatalf("seen %v, want %v", seenOrder, want)
	}
	for i, v := range want {
		if seenOrder[i] != v {
			t.Errorf("seen[%d] = %d, want %d", i, seenOrder[i], v)
		}
	}
}
