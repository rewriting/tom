package gom;

import static org.junit.Assert.*;
import org.junit.Test;
import gom.testrulesortlist.sorted.types.*;

public class TestRuleSortList {
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.runClasses(TestRuleSortList.class);
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
        Mix(x,X1*,y,X2*) -> Mix(y,X1*,x,X2*) if greaterThan(x,y)==true
      }

      Mix:import() {
        import gom.testrulesortlist.sorted.types.*;
      }

      Mix:block() {
        public static boolean greaterThan(Element o1, Element o2) {
          if (o1.compareToLPO(o2)>0)
            return true;
          return false;
        }
      }
  }

  @Test
  public void testSorta() {
    Sorted l = `MinMax(4,6,2,0,-2,3);
    assertEquals(-2,l.getHeadMinMax());
  }

  @Test
  public void testSortMaxMin() {
    Sorted l = `MaxMin(4,6,2,0,-2,3);
    assertEquals(6,l.getHeadMaxMin());
  }

  @Test
  public void testSortNoDup() {
    Sorted l = `NoDup(4,6,2,0,-2,3);
    Sorted dup = `NoDup(l*,l*);
    assertEquals(l,dup);
  }

  @Test
  public void testSortMix() {
    Sorted l1 = `Mix(Int(3),Str("a"),Int(6),Str("foo"),Str("bar"));
    Sorted l2 = `Mix(Str("foo"),Str("bar"),Int(6),Str("a"),Int(3));
    assertEquals(l1,l2);
  }
}
