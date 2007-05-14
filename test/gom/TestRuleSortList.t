package gom;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import gom.testrulesortlist.sorted.types.*;

public class TestRuleSortList extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestRuleSortList.class));
  }

  %gom {
    module Sorted
      imports int String
      abstract syntax
      Element = Int(i:int)
              | Str(s:String)
      Sorted = MinMax(int*)
             | MaxMin(int*)
             | NoDup(int*)
             | Mix(Element*)
      module Sorted:rules() {
        MinMax(x,X1*,y,X2*) -> MinMax(y,X1*,x,X2*) if x > y
        MaxMin(x,X1*,y,X2*) -> MaxMin(y,X1*,x,X2*) if x < y
        NoDup(x,X1*,y,X2*) -> NoDup(X1*,x,X2*) if x == y
        Mix(x,X1*,y,X2*) -> Mix(y,X1*,x,X2*) if x.greaterThan(y)
      }

      sort Element:block() {
        public boolean greaterThan(Element o) {
          if (this.compareToLPO(o)>0)
            return true;
          return false;
        }
      }
  }

  public void testSorta() {
    Sorted l = `MinMax(4,6,2,0,-2,3);
    assertEquals(-2,l.getHeadMinMax());
  }

  public void testSortMaxMin() {
    Sorted l = `MaxMin(4,6,2,0,-2,3);
    assertEquals(6,l.getHeadMaxMin());
  }

  public void testSortNoDup() {
    Sorted l = `NoDup(4,6,2,0,-2,3);
    Sorted dup = `NoDup(l*,l*);
    assertEquals(l,dup);
  }

  public void testSortMix() {
    Sorted l1 = `Mix(Int(3),Str("a"),Int(6),Str("foo"),Str("bar"));
    Sorted l2 = `Mix(Str("foo"),Str("bar"),Int(6),Str("a"),Int(3));
    assertEquals(l1,l2);
  }
}
