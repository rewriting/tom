package gom;

import java.io.*;
import gom.testtomhook.bool.types.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestTomHook {

  %gom {
    module Bool
    abstract syntax
    Bool = True()
         | False()
         | Not(b:Bool)
         | And(l:Bool,r:Bool)
         | Or(l:Bool,r:Bool)
    Not:make(b) {
      %match(Bool b) {
        And(l,r) -> { return `Or(Not(l),Not(r)); }
        Or(l,r)  -> { return `And(Not(l),Not(r)); }
        True()   -> { return `False(); }
        False()  -> { return `True(); }
      }
    }
  }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestTomHook.class.getName());
  }

  @Test
  public void testTrue() {
    Bool test = `Not(True());
    assertEquals(test,`False());
  }

  @Test
  public void testFalse() {
    Bool test = `Not(False());
    assertEquals(test,`True());
  }

  @Test
  public void testNotAndHook() {
    Bool test = `Not(And(True(),True()));
    assertEquals(test,`Or(False(),False()));
  }

  @Test
  public void testNotOrHook() {
    Bool test = `Not(Or(True(),True()));
    assertEquals(test,`And(False(),False()));
  }
}
