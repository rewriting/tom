package xrho;

import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;
import java.util.Arrays;

@RunWith(Parameterized.class)
public class TestRho {
  String question;
  String answer;

  public TestRho(String question, String answer) {
    this.question = question;
    this.answer = answer;
  }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestRho.class.getName());
  }

  private static Rho engine = null;
  
  @BeforeClass
  public static void init() {
    engine = new Rho();
  }

  @AfterClass
  public static void stop() {
    engine = null;
  }

  @Test
  public void testEval() {
    Assert.assertEquals(answer,engine.test(question));
  }

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        { "X;",                                         "X" },
        { "a;",                                         "a" },
        { "a -> b . a;",                                "b" },
        { "(s.a) -> b . c ;",                           "stk" },
        { "X -> X . a;",                                "a" },
        { "(f.X.Y) -> X . (f.a.b);",                    "a" },
        { "(f.X) -> X . (f.a);",                        "a" },
        { "(f.X.Y.Z) -> Y . (f.a.b.c);",                "b" },
        { "Z -> ((f.X.Y) -> X. (f.a.b));",              "(Z->((((f.X).Y)->X).((f.a).b)))" },
        { "Z -> ((f.X.Y) -> X. (f.a.b)) . c;",          "a" },
        { "Z -> ((f.X.Y) -> X|Y|Z. (f.a.b)) . c;",      "((a|b)|c)" },
        { "(f.X.Y) -> X|Y . (f.a.b);",                  "(a|b)" },
        { "(f.X.Y) -> Y|Y . (f.a.b);",                  "(b|b)" },
        { "(f.X.Y) -> Y|Y|Y|X|X|X . (f.a.b);",          "(((((b|b)|b)|a)|a)|a)" },
        { "((f.X.Y) -> X) | ((f.X.Y) -> Y ). (f.a.b);", "(a|b)" },
        { "X -> (X -> X) .a .b;",                       "b" },
        { "((red -> go) | (green-> ok) | (orange->go) | (orange -> stop) ).orange;", "(go|stop)" }
    });
  }
}
