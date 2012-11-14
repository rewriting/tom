import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
<<<<<<< HEAD
<<<<<<< HEAD
import jjtraveler.VisitableList;
import jjtraveler.graph.VisitedTest;
=======
>>>>>>> parent of 7f27d9b... MOF: Multi-focus example OK
=======
>>>>>>> parent of 7f27d9b... MOF: Multi-focus example OK
import lib.*;
import lib.sl.*;
import tom.library.sl.Visitable;

import mof.expr.types.*;
import mof.tree.types.*;


public class MOF {
    %include { string.tom }

    /*
      **************************
      *                        *
      *  SINGLE FOCUS EXAMPLE  *
      *                        *
      * ************************
     */


    %gom {
        module Expr
        imports String int
        abstract syntax
        Expr = I(x:int)
             | P(e1:Expr, e2:Expr)
             | M(e1:Expr, e2:Expr)
    }

    public static void showresult(Object x) { System.out.println("<result>" + x.toString() + "</result>"); }

    public static Expr e = `M(I(3),P(I(1),I(1)));

    /*
     Instead of defining a visitor by defining visitZK, we build one by mapping a function into a Visitor
     getChildAt can give either a Expr or a int, so we define the function on their common super type.
      */

    public static Visitor<Visitable,Visitable> v = Visitor.map( new Fun<Visitable,Visitable>() {
        public Visitable apply(Visitable u) throws MOFException {

            /*
             The match is only defined on Term and NOT String, so we fail on String!
             Forgetting this case will result in a uncastable exception between VisitableBuiltin (String)
             and Term.
              */

            if (!(u instanceof Expr)) throw new MOFException();

            /*
             Now that we are on terms, we can match.
             */
            %match(u) {
                P(I(x),I(y)) -> { return `I(x + y);                               }
                M(I(x),I(y)) -> { return `I(x * y);                               }
                _            -> { throw new MOFException();                       }

            };
            return null;
        }});

<<<<<<< HEAD


=======


>>>>>>> parent of 7f27d9b... MOF: Multi-focus example OK
    %gom {
        module Tree
        imports String int
        abstract syntax
        Tree = L(x:int)
             | N(n1:Tree, n2:Tree)
    }


    public static Tree t0  = `L(0);
    public static Tree t1  = `L(1);
    public static Tree t2  = `L(2);
    public static Tree t3  = `L(3);
    public static Tree t4  = `L(4);
    public static Tree t5  = `L(5);
    public static Tree t6  = `L(6);
    public static Tree t7  = `L(7);
    public static Tree t8  = `L(8);
    public static Tree t9  = `L(9);

    public static Tree t01  = `N(t0,t1);
    public static Tree t23  = `N(t2,t3);
    public static Tree t45  = `N(t4,t5);
    public static Tree t67  = `N(t6,t7);
    public static Tree t89  = `N(t8,t9);

    public static Tree t0123 = `N(t01,t23);
    public static Tree t4567 = `N(t45,t67);

    public static Tree tAll  = `N(N(t0123,t4567),t89);



    public static Visitor<P<Visitable,Visitable>, P<Visitable,Visitable>> vtree = Visitor.map(
        new Fun<P<Visitable,Visitable>, P<Visitable,Visitable>>() {
        public P<Visitable,Visitable> apply(P<Visitable,Visitable> u) throws MOFException {

            /*
             The match is only defined on Term and NOT String, so we fail on String!
             Forgetting this case will result in a uncastable exception between VisitableBuiltin (String)
             and Term.
              */


            if (!(u instanceof P)) throw new MOFException();

            /*
             Now that we are on terms, we can match.
             */
            Visitable l = u.left;
            Visitable r = u.right;

            %match {
                L(2) << l && L(8) << r -> { return P.mkP((Visitable)(`L(8)),(Visitable)(`L(2))); }
                _    << l && _    << r -> { throw new MOFException();                            }
            };
            return null;
        }});


    public static void runExpr() throws MOFException {
        System.out.println("<Expr>\n");
        /*
         The visitor is built like this: it selects a child. In this case it will try in order
          1 - "f"
          2 - L("totp")
          3 - L("titi")

          it applies s to the child and backtrack on error.
          "reset" is there to stop backtracking from going further.

          at the end of reset, the focus is still placed on a (rewritten) child. "up" rebuild the
          parent.
         */
        Visitor<Visitable,Visitable> w = All.innerMost(v.trace("v"));

        // The show must go on!
        showresult(w.visit(e));

        System.out.println("</Expr>\n");
    }

        System.out.println("</Expr>\n");
    }

    public static void runTree() throws MOFException {
        System.out.println("<Tree>\n");
        /*
         The visitor is built like this: it selects a child. In this case it will try in order
          1 - "f"
          2 - L("totp")
          3 - L("titi")

          it applies s to the child and backtrack on error.
          "reset" is there to stop backtracking from going further.

          at the end of reset, the focus is still placed on a (rewritten) child. "up" rebuild the
          parent.
         */
        Visitor<Visitable,P<Visitable,Visitable>> wtree = new SelectChild2<Visitable, Visitable>();

        // The show must go on!
        showresult(wtree.trace("wtree").visitUZ(tAll).focus);
        System.out.println("</Tree>\n");
    }


    /**
     * This example is essentially the same as above but this time we not only want to replace one pair of leaves but
     * all pairs.
     */

    /**
     *  A visitor that exchange two leaves if the first one is lesser than the second one. It takes the two focus as a
     *  pair of visitable terms: P<Visitable,Visitable> Then it match the two terms/members of the pair.
     */
    public static Visitor<P<Visitable,Visitable>, P<Visitable,Visitable>> vorder = Visitor.map(
            new Fun<P<Visitable,Visitable>, P<Visitable,Visitable>>() {
                public P<Visitable,Visitable> apply(P<Visitable,Visitable> u) throws MOFException {
                    /*
                    The match on the two focuses
                    */

                    %match {
                        L(x) << u.left && L(y) << u.right && x < y -> { return P.mkP((Visitable)(`L(y))
                                                                                    ,(Visitable)(`L(x))
                                                                                    );
                        }
                    };
                    throw new MOFException();
                }});


    public static void runOrder() throws MOFException {
        System.out.println("<Order>\n");
        System.out.println("<exampleDepth>" + exampleDepth + "</exampleDepth>\n");
        System.out.println("<exampleTree>"  + exampleTree  + "</exampleTree>\n");


        Visitor<Visitable,Visitable>                           select1     = new SelectChild<Visitable,Visitable>();
        Visitor<Visitable,P<Visitable,Visitable>>              select2     = new SelectChild2<Visitable, Visitable>();

        Var<P<Visitable,Visitable>,P<Visitable,Visitable>>     x           = new Var<P<Visitable, Visitable>, P<Visitable, Visitable>>();
        Visitor<P<Visitable,Visitable>,P<Visitable,Visitable>> tracedOrder = vorder.trace("vorder");

        Visitor<Visitable,P<Visitable,Visitable>>              visitor     =
                select2.seq(x.set(select1.times(select1).seq(x).trace("fix")));



        /*
         The show must go on! Note that we use visitUZ instead of visit. We want to show where the two focuses are
         at the end of the computation and not only the resulting whole term (which you can still see on the "whole"
         xml node of the zipper toString.
          */
        try                    { showresult(visitor.visitUZ(exampleTree)); }
        catch (MOFException e) { System.out.print("<stategy-failed/>") ; }

        System.out.println("</Order>\n");
    }


    public static void run() throws MOFException {
        System.out.println("<MOF-POC>\n\n");
        //runExpr();
        //runTree();
        runOrder();
        System.out.println("</MOF-POC>\n\n");
    }


}