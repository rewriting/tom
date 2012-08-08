package examples;

import aterm.ATerm;
import aterm.ATermFactory;
import aterm.pure.PureFactory;

public class ExamplesTerms {

  public static ATerm expr_T1;
  public static ATerm expr_T1Left;
  public static ATerm expr_T1Right;
  public static ATerm sortTestShrink2_s2test;
  public static ATerm[] sortTestShrink2_s2test_children = new ATerm[8];

  public static void init() {
    ATermFactory factory = new PureFactory();
    expr_T1 = factory.parse("plus(mult(mult(mult(plus(un(),zero()),mult(zero(),zero())),mult(zero(),zero())),mult(zero(),un())),plus(plus(mult(mult(zero(),zero()),plus(un(),un())),plus(mult(zero(),zero()),mult(zero(),un()))),plus(plus(zero(),un()),mult(zero(),un()))))");
    expr_T1Left = factory.parse("mult(mult(mult(plus(un(),zero()),mult(zero(),zero())),mult(zero(),zero())),mult(zero(),un()))");
    expr_T1Right = factory.parse("plus(plus(mult(mult(zero(),zero()),plus(un(),un())),plus(mult(zero(),zero()),mult(zero(),un()))),plus(plus(zero(),un()),mult(zero(),un())))");
    sortTestShrink2_s2test = factory.parse("gros(b(c(a(nill))),b(c(a(b(c(a(nill)))))),c(a(b(c(a(nill))))),a(b(c(a(nill)))),a(b(c(a(b(c(a(nill))))))),a(b(c(a(b(c(a(b(c(a(nill)))))))))))");
    ATerm sortTestShrink2_s2test1 = factory.parse("moyen(b(c(a(nill))),b(c(a(b(c(a(nill)))))),a(b(c(a(nill)))))");
    ATerm sortTestShrink2_s2test2 = factory.parse("moyen(b(c(a(nill))),b(c(a(b(c(a(nill)))))),a(b(c(a(b(c(a(nill))))))))");
    ATerm sortTestShrink2_s2test3 = factory.parse("moyen(b(c(a(nill))),b(c(a(b(c(a(nill)))))),a(b(c(a(b(c(a(b(c(a(nill)))))))))))");
    ATerm sortTestShrink2_s2test4 = factory.parse("petit1(b(c(a(nill))),c(a(b(c(a(nill))))))");
    ATerm sortTestShrink2_s2test5 = factory.parse("petit1(b(c(a(b(c(a(nill)))))),c(a(b(c(a(nill))))))");
    ATerm sortTestShrink2_s2test6 = factory.parse("petit2(c(a(b(c(a(nill))))),a(b(c(a(nill)))))");
    ATerm sortTestShrink2_s2test7 = factory.parse("petit2(c(a(b(c(a(nill))))),a(b(c(a(b(c(a(nill))))))))");
    ATerm sortTestShrink2_s2test8 = factory.parse("petit2(c(a(b(c(a(nill))))),a(b(c(a(b(c(a(b(c(a(nill)))))))))))");
    sortTestShrink2_s2test_children[0] = sortTestShrink2_s2test1;
    sortTestShrink2_s2test_children[1] = sortTestShrink2_s2test2;
    sortTestShrink2_s2test_children[2] = sortTestShrink2_s2test3;
    sortTestShrink2_s2test_children[3] = sortTestShrink2_s2test4;
    sortTestShrink2_s2test_children[4] = sortTestShrink2_s2test5;
    sortTestShrink2_s2test_children[5] = sortTestShrink2_s2test6;
    sortTestShrink2_s2test_children[6] = sortTestShrink2_s2test7;
    sortTestShrink2_s2test_children[7] = sortTestShrink2_s2test8;
  }
}
