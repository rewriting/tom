package sl

import "testing"

func pos(moves ...int) *Position { return MakePositionFromArray(moves) }

func TestPosition_Root(t *testing.T) {
	r := MakePosition()
	if r.Length() != 0 || r.String() != "[]" {
		t.Fatalf("root = %v (length %d)", r, r.Length())
	}
	if MakePosition() != r {
		t.Fatal("MakePosition must return the ROOT singleton")
	}
}

func TestPosition_StringAndArray(t *testing.T) {
	p := pos(1, 2, 3)
	if got := p.String(); got != "[1, 2, 3]" {
		t.Fatalf("String = %q", got)
	}
	a := p.ToIntArray()
	a[0] = 9
	if p.GetHead() != 1 {
		t.Fatal("ToIntArray must copy")
	}
	src := []int{4, 5}
	q := MakePositionFromArray(src)
	src[0] = 0
	if q.GetHead() != 4 {
		t.Fatal("MakePositionFromArray must copy")
	}
	if got := MakePositionFromSubarray([]int{0, 7, 8, 9}, 1, 2).String(); got != "[7, 8]" {
		t.Fatalf("MakePositionFromSubarray = %s", got)
	}
}

func TestPosition_EqualsUsesCanonicalPath(t *testing.T) {
	if !pos(1, 2).Equals(pos(1, 2)) {
		t.Fatal("[1,2] != [1,2]")
	}
	if pos(1, 2).Equals(pos(1, 3)) || pos(1).Equals(pos(1, 2)) {
		t.Fatal("unexpected equality")
	}
	if !pos(1, 2, -2, 3).Equals(pos(1, 3)) {
		t.Fatal("[1,2,-2,3] should equal [1,3]")
	}
	if pos(1).Equals("x") || pos(1).Equals(nil) {
		t.Fatal("a Position never equals a non-Position")
	}
	if pos(1, 2).HashCode() != 1*31+2 {
		t.Fatalf("HashCode = %d", pos(1, 2).HashCode())
	}
}

func TestPosition_Compare(t *testing.T) {
	cases := []struct {
		a, b *Position
		want int
	}{
		{pos(1, 2), pos(1, 2), 0},
		{pos(1, 2), pos(1, 3), -1},
		{pos(2), pos(1, 3), 1},
		{pos(1), pos(1, 3), -1},
		{pos(1, 3), pos(1), 1},
		{MakePosition(), MakePosition(), 0},
	}
	for _, c := range cases {
		if got := c.a.Compare(c.b); got != c.want {
			t.Errorf("%v.Compare(%v) = %d, want %d", c.a, c.b, got, c.want)
		}
	}
}

func TestPosition_PathAlgebra(t *testing.T) {
	p := pos(1, 2)
	if got := p.Add(pos(3)).(*Position).String(); got != "[1, 2, 3]" {
		t.Errorf("Add = %s", got)
	}
	if got := p.Add(MakePosition()).(*Position); got == p || !got.Equals(p) {
		t.Errorf("Add(root) must be an equal copy")
	}
	if got := p.Inverse().(*Position).String(); got != "[-2, -1]" {
		t.Errorf("Inverse = %s", got)
	}
	// Sub: from [1,3] to [1,2] is up 3, up 1, down 1, down 2.
	sub := pos(1, 2).Sub(pos(1, 3)).(*Position)
	if sub.String() != "[-3, -1, 1, 2]" {
		t.Errorf("Sub = %s", sub)
	}
	if got := sub.GetCanonicalPath().(*Position).String(); got != "[-3, 2]" {
		t.Errorf("canonical Sub = %s", got)
	}
	if got := pos(1, 2, -2, -1).GetCanonicalPath(); got.Length() != 0 || !MakePosition().Equals(got) {
		t.Errorf("canonical of a closed walk = %v, want the root", got)
	}
	if got := p.Conc(5).(*Position).String(); got != "[1, 2, 5]" {
		t.Errorf("Conc = %s", got)
	}
	if got := p.GetTail().(*Position).String(); got != "[2]" {
		t.Errorf("GetTail = %s", got)
	}
	if got := p.Up().String(); got != "[1]" {
		t.Errorf("Up = %s", got)
	}
	if got := p.Down(4).String(); got != "[1, 2, 4]" {
		t.Errorf("Down = %s", got)
	}
	defer func() {
		if recover() == nil {
			t.Error("GetTail of the root must panic")
		}
	}()
	MakePosition().GetTail()
}

func TestPosition_Prefix(t *testing.T) {
	p := pos(1, 2, 3)
	if !p.HasPrefix(pos(1, 2)) || !p.HasPrefix(MakePosition()) || p.HasPrefix(pos(2)) || pos(1).HasPrefix(p) {
		t.Fatal("HasPrefix")
	}
	if got := p.ChangePrefix(pos(1, 2), pos(4)); got.String() != "[4, 3]" {
		t.Fatalf("ChangePrefix = %v", got)
	}
	if p.ChangePrefix(pos(2), pos(4)) != nil {
		t.Fatal("ChangePrefix without the prefix must be nil")
	}
}

func TestPosition_GetOmegaReplaceSubterm(t *testing.T) {
	intro := VisitableIntrospector{}
	tree := parent("f", leaf("a"), parent("g", leaf("b"), leaf("c")))
	p := pos(2, 1)

	// GetSubterm, light and environment-based.
	for _, visit := range []func(Strategy) (any, error){
		func(s Strategy) (any, error) { return s.VisitLight(tree, intro) },
		func(s Strategy) (any, error) { return s.Visit(tree, intro) },
	} {
		got, err := visit(p.GetSubterm())
		if err != nil || printNode(got.(*node)) != "b" {
			t.Fatalf("GetSubterm = %v, %v", got, err)
		}
	}

	// GetReplace, light and environment-based.
	for _, visit := range []func(Strategy) (any, error){
		func(s Strategy) (any, error) { return s.VisitLight(tree, intro) },
		func(s Strategy) (any, error) { return s.Visit(tree, intro) },
	} {
		got, err := visit(p.GetReplace(leaf("z")))
		if err != nil || printNode(got.(*node)) != "(f a (g z c))" {
			t.Fatalf("GetReplace = %v, %v", got, err)
		}
	}

	// A position outside the tree fails.
	if _, err := pos(3).GetOmega(NewIdentity()).Visit(tree, intro); err == nil {
		t.Fatal("GetOmega at [3] should fail on a binary node")
	}

	// GetOmegaPath applies v on the way down (position excluded).
	var seen []string
	rec := strategyFn(func(s any, _ Introspector) (any, error) {
		seen = append(seen, printNode(s.(*node)))
		return s, nil
	})
	if _, err := pos(2, 1).GetOmegaPath(rec).Visit(tree, intro); err != nil {
		t.Fatal(err)
	}
	if len(seen) != 2 || seen[0] != "(g b c)" || seen[1] != "(f a (g b c))" {
		t.Fatalf("GetOmegaPath visited %v", seen)
	}
}
