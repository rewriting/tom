import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import jjtraveler.graph.VisitedTest;
import lib.*;
import lib.sl.*;
import tom.library.sl.Visitable;

import mof.expr.types.*;
import mof.tree.types.*;


public class MOF {
    %include { string.tom }

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

            };
            throw new MOFException();
        }});

    public static void runExpr() throws MOFException {
        System.out.println("<Expr>\n");

        /*
         .
         */
        Visitor<Visitable,Visitable> w = All.innerMost(v.trace("v"));

        // The show must go on!
        showresult(w.visit(e));

        System.out.println("</Expr>\n");
    }


    /*
      **************************
      *                        *
      *  MULTI FOCUS EXAMPLE   *
      *                        *
      * ************************
     */


    /*
      Binary trees with integer at leaves.
     */
    %gom {
        module Tree
        imports String int
        abstract syntax
        Tree = L(x:int)
             | N(n1:Tree, n2:Tree)
    }


    /** The depth of the example tree */
    final static int exampleDepth = 3;


    /*
     The strategy will search for two leaves with specific value and exchange them.
     To get the worse case senario we take the following values for the two leaves.
      */
    static final int leftSide  = (int) Math.pow(2 , exampleDepth - 1) - 1;
    static final int rightSide = (int) Math.pow(2 , exampleDepth    ) - 1;


    /**
     * A simple method to build full binary trees of size 2 power n.
     */
    public static Tree makeTree(int depth) { return makeTree(0, depth); }

    public static Tree makeTree(int path, int depth) {
        if (depth <= 0) return `L(path);
        else { path  = 2 * path;
               depth = depth - 1;
               return `N(makeTree(path    , depth)
                        ,makeTree(path + 1, depth)
                        );
             }
    }


    // The example Tree
    static final Tree exampleTree = makeTree(exampleDepth);


    /**
     *  A visitor that exchange two leaves. It takes the two focus as a pair of visitable terms: P<Visitable,Visitable>
     *  Then it match the two terms/members of the pair.
     */
    public static Visitor<P<Visitable,Visitable>, P<Visitable,Visitable>> vtree = Visitor.map(
        new Fun<P<Visitable,Visitable>, P<Visitable,Visitable>>() {
        public P<Visitable,Visitable> apply(P<Visitable,Visitable> u) throws MOFException {
            /*
             The match on the two focuses
             */

            %match {
                L(x) << u.left && x == leftSide && L(y) << u.right && y == rightSide -> { return P.mkP((Visitable)(`L(y))
                                                                                                      ,(Visitable)(`L(x))
                                                                                                      );
                                                                                        }
            };
            throw new MOFException();
        }});




    public static void runTree() throws MOFException {
        System.out.println("<Tree>\n");
        System.out.println("<exampleDepth>" + exampleDepth + "</exampleDepth>\n");
        System.out.println("<leftSide>"     + leftSide     + "</leftSide>\n");
        System.out.println("<rightSide>"    + rightSide    + "</rightSide>\n");
        System.out.println("<exampleTree>"  + exampleTree  + "</exampleTree>\n");




        /*
         We repeat the child selection action (SelectChild) until reaching a leaf. Note that this strategy
         is very close to onceBottumUp but does not require neither a strategy as argument nor a specific
         Mu but only repeat.
        */
        Visitor<Visitable,Visitable> selectBottomUp = Visitor.repeat(new SelectChild<Visitable, Visitable>());

        /*
          The previous visitor will be used twice and we want to be able to distinguish each instances so we use
          two different trace names.
         */
        Visitor<Visitable,Visitable> left  = selectBottomUp.trace("LEFT" );
        Visitor<Visitable,Visitable> right = selectBottomUp.trace("RIGHT");


        //  We also trace the strategy vtree.
        Visitor<P<Visitable,Visitable>,P<Visitable,Visitable>> vtreetraced = vtree.trace("vtree");

        /*
          A simple instance of SelectChild2 on visitable objects. It selects 2 children of an operator and, if the
          selected pair makes the rest the computation to fail, it backtrack until finding a good one.
         */
        Visitor<Visitable,P<Visitable,Visitable>> select2 = new SelectChild2<Visitable, Visitable>();

        /*
          The core of the example: select2 gives 2 children, each instance of selectBottomUp is applied on a child
          (left on the left child and right on the right one). Both of them get a leaf of every side and then apply
          vstree. Note that vtree is also traced.
         */
        Visitor<Visitable,P<Visitable,Visitable>> wtree = select2.seq(left.times(right)).seq(vtreetraced);

        /*
         The show must go on! Note that we use visitUZ instead of visit. We want to show where the two focuses are
         at the end of the computation and not only the resulting whole term (which you can still see on the "whole"
         xml node of the zipper toString.
          */
        try                    { showresult(wtree.visitUZ(exampleTree)); }
        catch (MOFException e) { System.out.print("<stategy-failed/>") ; }

        System.out.println("</Tree>\n");
    }

    public static void run() throws MOFException {
        System.out.println("<MOF-POC>\n\n");
        //runExpr();
        runTree();
        System.out.println("</MOF-POC>\n\n");
    }


}