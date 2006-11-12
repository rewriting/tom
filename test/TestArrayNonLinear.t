import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.ArrayList;

public class TestArrayNonLinear extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestArrayNonLinear.class));
  }

%include { util/ArrayList.tom }
  
%op Object a() {
  is_fsym(t) { t.equals("a") }
  make() { "a" }
}
%op Object b() {
  is_fsym(t) { t.equals("b") }
  make() { "b" }
}
%op Object c() {
  is_fsym(t) { t.equals("c") }
  make() { "c" }
}
%op Object F(l:ArrayList) {
  is_fsym(t) { (t instanceof F) }
  get_slot(l,t) { ((F)t).l }
  make(t) { new F(t) }
}

  public void test1() {
    ArrayList l1 = `concArrayList(a(),b(),c());
    ArrayList l2 = `concArrayList(a(),b(),c());
    ArrayList subject = `concArrayList(F(l1),F(l2));
    %match(subject) {
      concArrayList(F(concArrayList(x*,y*)),F(concArrayList(_,y*))) -> {
        return;
      }
    }
    fail("failure");
  }
}

class F {
 public ArrayList l;
 public F(ArrayList l) {
  this.l=l;
 }
}
