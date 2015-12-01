
import maintrs.m.*;
import maintrs.m.types.*;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class TestTrs {
  %include { maintrs/m/m.tom }
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(Mainfg.class.getName());
	}
 
  @Before
  public void setUp() {
  }

  @Test
  public void test1() {
    assertEquals("b() = f(a())",
                 `b(), maintrs.m.types.t.f.make(`a()));
  }
  
  @Test
  public void test2() {
    assertEquals("a() = f(g(a()))",
                 `a(), maintrs.m.types.t.f.make(`g(a())));
  }

  @Test
  public void test3() {
    assertEquals("g(b()) = f(g(b()))",
                 `g(b()), maintrs.m.types.t.f.make(`g(b())));
  }
}
