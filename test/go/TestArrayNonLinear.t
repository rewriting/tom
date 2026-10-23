// Port of test/TestArrayNonLinear.t.
// port: java.util.ArrayList is []any (Go mapping util/ArrayList.tom);
// Object.equals("a") is == on the Go any value; the class F is the Go
// struct *F, `new F(l)` its literal &F{l}, `instanceof F` a type
// assertion.
// port: org.junit.Assert.fail is t.Fatal.
// port: no main — `go test` runs the tests (the original main only launched JUnit).
package testarraynonlinear

import "testing"

%include { util/ArrayList.tom }
  
%op Object a() {
  is_fsym(t) { $t == "a" }
  make() { "a" }
}
%op Object b() {
  is_fsym(t) { $t == "b" }
  make() { "b" }
}
%op Object c() {
  is_fsym(t) { $t == "c" }
  make() { "c" }
}
%op Object F(l:ArrayList) {
  is_fsym(t) { isF($t) }
  get_slot(l,t) { $t.(*F).l }
  make(t) { &F{$t} }
}

func Test1(t *testing.T) {
    var l1 []any = `concArrayList(a(),b(),c())
    var l2 []any = `concArrayList(a(),b(),c())
    var subject []any = `concArrayList(F(l1),F(l2))
    %match(subject) {
      concArrayList(F(concArrayList(_x*,y*)),F(concArrayList(_,y*))) -> {
        return
      }
    }
    t.Fatal("failure")
  }

type F struct {
 l []any
}

// isF is `t instanceof F`.
func isF(t any) bool { _, ok := t.(*F); return ok }
