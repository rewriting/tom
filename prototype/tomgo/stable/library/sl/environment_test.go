// Frozen prototype copy — the maintained version is src/go/tom/library/sl (module tom).

package sl

import (
	"strings"
	"testing"
)

func TestEnvironment_NewIsAtRoot(t *testing.T) {
	e := NewEnvironment()
	if e.Depth() != 0 || e.GetPosition().Length() != 0 || e.GetSubject() != nil || e.GetRoot() != nil {
		t.Fatalf("fresh environment: %v", e)
	}
	if e.GetStatus() != StatusSuccess {
		t.Fatal("fresh status must be StatusSuccess")
	}
	if _, ok := e.GetIntrospector().(VisitableIntrospector); !ok {
		t.Fatal("default introspector must be VisitableIntrospector")
	}
}

// Same scenario as test/sl/TestEnvironment.t (testEqual, testUpAndDown).
func TestEnvironment_UpAndDown(t *testing.T) {
	a, b := leaf("a"), leaf("b")
	subject := parent("f", a, b)
	e1, e2 := NewEnvironment(), NewEnvironment()
	if !e1.Equals(e2) {
		t.Fatal("two fresh environments must be equal")
	}
	e1.SetSubject(subject)
	e2.SetSubject(subject)
	if !e1.Equals(e2) {
		t.Fatal("same subject: equal")
	}
	e1.Down(2)
	if e1.Equals(e2) {
		t.Fatal("different depths: not equal")
	}
	e2.Down(2)
	if !e1.Equals(e2) {
		t.Fatal("same walk: equal")
	}
	if e1.GetRoot() != subject || e1.GetSubject() != b || !e1.GetPosition().Equals(pos(2)) || e1.GetAncestor() != subject {
		t.Fatalf("after Down(2): %v", e1)
	}
	if e1.GetSubOmega() != 2 {
		t.Fatalf("GetSubOmega = %d", e1.GetSubOmega())
	}
	e1.Up()
	// Up rebuilds f(a,b) through the introspector: the toy nodes are
	// not hash-consed, so compare their prints.
	if printNode(e1.GetSubject().(*node)) != "(f a b)" || !e1.GetPosition().Equals(MakePosition()) {
		t.Fatalf("after Up: %v", e1)
	}
	e1.Down(1)
	if printNode(e1.GetSubject().(*node)) != "a" || !e1.GetPosition().Equals(pos(1)) {
		t.Fatalf("after Down(1): %v", e1)
	}
	if e1.Equals(e2) || e1.Equals("x") {
		t.Fatal("unexpected equality")
	}
	// Down(0) is a no-op, as in Java.
	e1.Down(0)
	if e1.Depth() != 1 {
		t.Fatal("Down(0) must not move")
	}
}

func TestEnvironment_UpRebuildsUpLocalDoesNot(t *testing.T) {
	subject := parent("f", leaf("a"), leaf("b"))
	e := NewEnvironment()
	e.SetRoot(subject)
	e.Down(2)
	e.SetSubject(leaf("z"))
	e.UpLocal()
	if e.GetSubject() != subject {
		t.Fatal("UpLocal must not rebuild the parent")
	}
	e.Down(2)
	e.SetSubject(leaf("z"))
	e.Up()
	if got := printNode(e.GetSubject().(*node)); got != "(f a z)" {
		t.Fatalf("Up must rebuild the parent: %s", got)
	}
	if e.GetRoot() != e.GetSubject() {
		t.Fatal("at depth 0 the subject is the root")
	}
}

func TestEnvironment_GrowsBeyondDefaultLength(t *testing.T) {
	// a chain of depth 20: f(f(...f(x)...))
	var n *node = leaf("x")
	for range 20 {
		n = parent("f", n)
	}
	e := NewEnvironment()
	e.SetRoot(n)
	for range 20 {
		e.Down(1)
	}
	if e.Depth() != 20 || printNode(e.GetSubject().(*node)) != "x" || e.GetPosition().Length() != 20 {
		t.Fatalf("deep walk: depth %d, %v", e.Depth(), e.GetPosition())
	}
	if len(e.GetCurrentStack()) != 20 {
		t.Fatalf("GetCurrentStack has %d elements", len(e.GetCurrentStack()))
	}
	for range 20 {
		e.Up()
	}
	if printNode(e.GetSubject().(*node)) != printNode(n) {
		t.Fatal("unchanged walk must give an equal root back")
	}
}

func TestEnvironment_FollowPathAndGoToPosition(t *testing.T) {
	c := leaf("c")
	subject := parent("f", leaf("a"), parent("g", leaf("b"), c))
	e := NewEnvironment()
	e.SetRoot(subject)
	e.FollowPath(pos(2, 2))
	if e.GetSubject() != c {
		t.Fatalf("FollowPath [2,2]: %v", e)
	}
	e.GoToPosition(pos(1))
	if printNode(e.GetSubject().(*node)) != "a" || !e.GetPosition().Equals(pos(1)) {
		t.Fatalf("GoToPosition [1]: %v", e)
	}
	e.FollowPathLocal(pos(-1, 2, 1))
	if printNode(e.GetSubject().(*node)) != "b" {
		t.Fatalf("FollowPathLocal: %v", e)
	}
}

func TestEnvironment_CloneAndString(t *testing.T) {
	subject := parent("f", leaf("a"))
	e := NewEnvironment()
	e.SetRoot(subject)
	e.Down(1)
	c := e.Clone()
	if !c.Equals(e) {
		t.Fatal("clone must be equal")
	}
	c.Up()
	if e.Depth() != 1 {
		t.Fatal("clone must be independent")
	}
	// Java: omega stack, then the subterm stack.
	if got := e.String(); !strings.HasPrefix(got, "[0, 1]\n[") || strings.Count(got, ", ") != 2 {
		t.Fatalf("String = %q", got)
	}
	e.SetStatus(StatusFailure)
	if e.GetStatus() != StatusFailure {
		t.Fatal("SetStatus")
	}
	e.SetIntrospector(nil)
	if e.GetIntrospector() != nil {
		t.Fatal("SetIntrospector")
	}
}
