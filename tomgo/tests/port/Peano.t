package main

import "fmt"

// Term is the runtime representation of `term` values for Peano. A
// constructor with no children is `&Term{Name:"zero"}`; `suc(p)` is
// `&Term{Name:"suc", Args:[]*Term{p}}`; `plus(a,b)` similarly. Going
// through pointers lets us hash-cons via plain Go map lookups (not
// shown here — we don't need sharing for Peano correctness).
type Term struct {
	Name string
	Args []*Term
}

func (t *Term) Equals(o *Term) bool {
	if t == o {
		return true
	}
	if t == nil || o == nil || t.Name != o.Name || len(t.Args) != len(o.Args) {
		return false
	}
	for i := range t.Args {
		if !t.Args[i].Equals(o.Args[i]) {
			return false
		}
	}
	return true
}

%typeterm term {
	implement     { *Term }
	is_sort(t)    { $t != nil }
	equals(t1,t2) { ($t1).Equals($t2) }
}

%op term zero() {
	is_fsym(t) { ($t).Name == "zero" }
	make()     { &Term{Name: "zero"} }
}

%op term suc(pred:term) {
	is_fsym(t)       { ($t).Name == "suc" }
	get_slot(pred,t) { ($t).Args[0] }
	make(p)          { &Term{Name: "suc", Args: []*Term{$p}} }
}

%op term plus(s1:term, s2:term) {
	is_fsym(t)      { ($t).Name == "plus" }
	get_slot(s1,t)  { ($t).Args[0] }
	get_slot(s2,t)  { ($t).Args[1] }
	make(a,b)       { &Term{Name: "plus", Args: []*Term{$a, $b}} }
}

// plus1 walks t1 — zero acts as identity, suc(x) descends and re-wraps.
func plus1(t1, t2 *Term) *Term {
	%match(term t1, term t2) {
		zero(), x       -> { return `x }
		suc(x), y       -> { return `suc(plus1(x, y)) }
	}
	return nil
}

// int2peano builds n as suc^n(zero).
func int2peano(n int) *Term {
	t := `zero()
	for i := 0; i < n; i++ {
		t = `suc(t)
	}
	return t
}

// peano2int counts suc layers.
func peano2int(t *Term) int {
	n := 0
	for t != nil && t.Name == "suc" {
		n++
		t = t.Args[0]
	}
	return n
}

func main() {
	ok := true
	for i := 0; i < 20; i++ {
		n := int2peano(i)
		got := peano2int(plus1(n, n))
		if got != i+i {
			ok = false
			fmt.Printf("FAIL: plus1(%d,%d) = %d, want %d\n", i, i, got, i+i)
		}
	}
	if ok {
		fmt.Println("First loop has successfully terminated.")
	}
	fmt.Println("Peano test has successfully terminated.")
}
