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
  public static ATerm nonS1_2_Shinkable;
  public static ATerm[] sortTestShrink2_s1depth_children = new ATerm[6];
  public static ATerm[] sortTestShrink2_s2depth_children = new ATerm[2];
  public static ATerm listABS_T1;

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
    nonS1_2_Shinkable = factory.parse("moyen(b(c(a(nill))),b(c(a(b(c(a(nill)))))),a(b(c(a(nill)))))");
    sortTestShrink2_s1depth_children[0] = factory.parse("gros(b(c(a(nill))),b(c(a(b(c(a(nill)))))),c(a(b(c(a(nill))))),a(b(c(a(nill)))),a(b(c(a(b(c(a(nill))))))),a(b(c(a(b(c(a(nill))))))))");
    sortTestShrink2_s1depth_children[1] = factory.parse("gros(b(c(a(nill))),b(c(a(b(c(a(nill)))))),c(a(b(c(a(nill))))),a(b(c(a(nill)))),a(b(c(a(nill)))),a(b(c(a(b(c(a(b(c(a(nill)))))))))))");
    sortTestShrink2_s1depth_children[2] = factory.parse("gros(b(c(a(nill))),b(c(a(b(c(a(nill)))))),c(a(b(c(a(nill))))),a(nill),a(b(c(a(b(c(a(nill))))))),a(b(c(a(b(c(a(b(c(a(nill)))))))))))");
    sortTestShrink2_s1depth_children[3] = factory.parse("gros(b(c(a(nill))),b(c(a(b(c(a(nill)))))),c(a(nill)),a(b(c(a(nill)))),a(b(c(a(b(c(a(nill))))))),a(b(c(a(b(c(a(b(c(a(nill)))))))))))");
    sortTestShrink2_s1depth_children[4] = factory.parse("gros(b(c(a(nill))),b(c(a(nill))),c(a(b(c(a(nill))))),a(b(c(a(nill)))),a(b(c(a(b(c(a(nill))))))),a(b(c(a(b(c(a(b(c(a(nill)))))))))))");
    sortTestShrink2_s1depth_children[5] = factory.parse("gros(nill,b(c(a(b(c(a(nill)))))),c(a(b(c(a(nill))))),a(b(c(a(nill)))),a(b(c(a(b(c(a(nill))))))),a(b(c(a(b(c(a(b(c(a(nill)))))))))))");
    sortTestShrink2_s2depth_children[0] = factory.parse("gros(b(c(a(nill))),nill,c(a(b(c(a(nill))))),a(b(c(a(nill)))),a(b(c(a(b(c(a(nill))))))),a(b(c(a(b(c(a(b(c(a(nill)))))))))))");
    sortTestShrink2_s2depth_children[1] = factory.parse("gros(nill,b(c(a(b(c(a(nill)))))),c(a(b(c(a(nill))))),a(b(c(a(nill)))),a(b(c(a(b(c(a(nill))))))),a(b(c(a(b(c(a(b(c(a(nill)))))))))))");
    listABS_T1 = factory.parse("consList(b,consList(b,consList(c,nill)))");
  }
}
