import lib.*;
import lib.sl.*;
import tom.library.sl.*;

import mof.expr.types.*;
import mof.tree.types.*;
import mof.msos.types.*;



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

            };
            throw new MOFException();
        }});

    public static void runExpr() throws MOFException {
        System.out.println("<Expr>\n");

        /*
         .
         */
        Visitor<Visitable,Visitable> w = Visitor.innerMost(v.trace("v"));

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
    final static int exampleDepth = 4;


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
        Visitor<Visitable,Visitable> selectBottomUp = Visitor.repeat(Visitor.forSome);

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
        Visitor<Visitable,P<Visitable,Visitable>> select2 = Visitor.forSome2;

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
        try                    { showresult(wtree.visit(exampleTree)); }
        catch (MOFException e) { System.out.print("<stategy-failed/>") ; }

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


        Visitor<Visitable,Visitable>  left     = Visitor.forAll.trace("left");
        Visitor<Visitable,Visitable>  right    = Visitor.forAll.trace("right");



        Var<P<Visitable,Visitable>,P<Visitable,Visitable>>     x           = new Var<P<Visitable, Visitable>, P<Visitable, Visitable>>();
        Visitor<P<Visitable,Visitable>,P<Visitable,Visitable>> tracedOrder = vorder.trace("vorder");

        Visitor<Visitable,P<Visitable,Visitable>>              visitor     =
                Visitor.forSome2.seq(x.set(left.times(right).seq(x).seq(Visitor.sltry(tracedOrder)).reset()));



        /*
         The show must go on! Note that we use visitUZ instead of visit. We want to show where the two focuses are
         at the end of the computation and not only the resulting whole term (which you can still see on the "whole"
         xml node of the zipper toString.
          */
        try                    { showresult(visitor.visit(exampleTree)); }
        catch (MOFException e) { System.out.print("<stategy-failed/>") ; }

        System.out.println("</Order>\n");
    }


    /*
     **************************
     *                        *
     *      MUDULAR SOS       *
     *                        *
     * ************************
    */

    %gom {
        module MSOS
        imports String int
        abstract syntax

        Arith = Val(val:int)
              | Var(name:String)
              | Plus(a1:Arith, a2:Arith)
              | Mult(a1:Arith, a2:Arith)


        Assoc = Assoc(name:String, val:int)

        Stmt  = Set(name:String, a:Arith)
              | Nop()

        Mem   = Mem(Assoc*)
        Pgrm  = Pgrm(Stmt*)

        module MSOS:rules() {
            Mem(x*, Assoc(n,_), y*, Assoc(n,v), z*) -> Mem(x*, y*, Assoc(n,v), z*)
            Pgrm(x*,Nop(),y*)                       -> Pgrm(x*,y*)
        }
    }

    %include{sl.tom}

    public static Pgrm examplePgrm = `Pgrm( Set("x",Val(1)),
                                            Set("y",Val(2)),
                                            Set("z",Val(3))
                                          );

    public static Visitor<Visitable,Visitable> groundEval = Visitor.map(
            new Fun<Visitable,Visitable>() {
                public Visitable apply(Visitable u) throws MOFException {
                    /*
                    The match on the two focuses
                    */

                    %match(u) {
                      Plus(Val(x),Val(y)) -> { return `Val(x + y); }
                      Mult(Val(x),Val(y)) -> { return `Val(x * y); }

                    };
                    throw new MOFException();
                }}).trace("groundEval");



    public static Visitor<P<Visitable,Visitable>, P<Visitable,Visitable>> varEval = Visitor.map(
            new Fun<P<Visitable,Visitable>, P<Visitable,Visitable>>() {
                public P<Visitable,Visitable> apply(P<Visitable,Visitable> u) throws MOFException {
                    /*
                    The match on the two focuses
                    */

                    %match {
                        Var(x) << u.left && a@Assoc(y,i) << u.right && x == y -> {
                                 return P.mkP( (Visitable)(`Val(i))
                                             , (Visitable)(`a)
                                             );
                        }
                    };
                    throw new MOFException();
                }}).trace("varEval");


    public static Visitor<P<Visitable,Visitable>, P<Visitable,Visitable>> stmtEval = Visitor.map(
            new Fun<P<Visitable,Visitable>, P<Visitable,Visitable>>() {
                public P<Visitable,Visitable> apply(P<Visitable,Visitable> u) throws MOFException {
                    /*
                    The match on the two focuses
                    */

                    %match {
                        Set(x, Val(i)) << u.left && m@Mem(_*) << u.right -> {
                                return P.mkP( (Visitable)`Nop()
                                            , (Visitable)`Mem(m, Assoc(x,i))
                                            );
                        }
                    };
                    throw new MOFException();
                }}).trace("stmtEval");


    /**
     * Pierre-Etienne, regarde ici:
     *
     * Strategie prennant un Pgrm ou Stmt en entree et l'affichant sur la sortie standard.
     *
     * Elle sera appliquee a All pour voir sur quels sous termes All decide de l'appeler
     */
    %strategy Print() extends Identity() {
        visit Stmt {
            x -> { System.out.println("<Print>" + `x.toString() + "</Print>"); }
        }

        visit Pgrm {
            x -> { System.out.println("<Print>" + `x.toString() + "</Print>"); }
        }
    }




    public static void runMSOS() throws MOFException {
        System.out.println("<MSOS>\n");
        System.out.println("<examplePgrm>"  + examplePgrm  + "</examplePgrm>\n");


        /* Pierre-Etienne, regarde ici
           examplePrgm =  Pgrm(Set("x",Val(1)),Set("y",Val(2)),Set("z",Val(3)))

           donc avec 3 enfants. On s'attend a ce que All apelle Print sur chacun d'eux. Mais l'execution donne:

              <Print>Set("x",Val(1))</Print>
              <Print>Pgrm(Set("y",Val(2)),Set("z",Val(3)))</Print>

              Ce qui donne bien que All a appliquer Print sur la tete et la queue de examplePgrm
         */
        Visitable w  = ConsWrapper.mk(examplePgrm);
        System.out.println("<Wrapper>" + w.toString() + "</Wrapper>");
        System.out.println("<Children>" + w.getChildren().toString() + "</Children>");
        try { `All(Print()).visit(w); }
        catch (Exception e) { }

        Visitor<Visitable,Visitable> idv = new Id<Visitable>();


        Visitor<P<Visitable,Visitable>, P<Visitable,Visitable>> realGroundEval = idv.times(Visitor.forSome).seq(groundEval.times(idv)).up();
        Visitor<P<Visitable,Visitable>, P<Visitable,Visitable>> arithEval      = realGroundEval.or(varEval).trace("arithEval");
        Visitor<P<Visitable,Visitable>, P<Visitable,Visitable>> phase1         = Visitor.forAll.trace("forAll-1").times(idv).trace("phase-1");
        Visitor<P<Visitable,Visitable>, P<Visitable,Visitable>> phase2         = Visitor.forAll.trace("forAll-2").times(idv).trace("phase-2");

        Var<P<Visitable,Visitable>,P<Visitable,Visitable>>      x              = new Var<P<Visitable, Visitable>, P<Visitable, Visitable>>();


        Visitor<P<Visitable,Visitable>, P<Visitable,Visitable>> realStmtEval   =
                phase1.seq(x.set(phase2.seq(x).seq(Visitor.sltry(arithEval)).reset()).trace("fix")).trace("phase-1and2").seq(Visitor.sltry(stmtEval).trace("stmtEval")).trace("realStmtEval");






        /*
         The show must go on! Note that we use visitUZ instead of visit. We want to show where the two focuses are
         at the end of the computation and not only the resulting whole term (which you can still see on the "whole"
         xml node of the zipper toString.
          */
        /*try                    { showresult(realStmtEval.visit(P.mkP( (Visitable)examplePgrm
                                                                      , (Visitable)(`Mem())
                                                                      )));
                               }
        catch (MOFException e) { System.out.print("<stategy-failed/>") ; }*/

        System.out.println("</MSOS>\n");
    }


    public static void run() throws MOFException {
        System.out.println("<MOF-POC>\n\n");
        //runExpr();
        //runTree();
        //runOrder();
        runMSOS();
        System.out.println("</MOF-POC>\n\n");
    }

}