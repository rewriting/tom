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

    public static Expr e = `M(I(3),P(I(1),I(1)));

    /*
     Instead of defining a visitor by defining visitZK, we build one by mapping a function into a Visitor
     getChildAt can give either a Expr or a int, so we define the function on their common super type.
      */

    public static Visitor<Visitable,Visitable> v = Visitor.map( new Fun<Visitable,Visitable>() {
        public Visitable apply(Visitable u) throws MOFException {

            // usual printf debuging
            System.out.println("[Expr] reducing " + u.toString());

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
                P(I(x),I(y)) -> { return `I(x + y); }
                M(I(x),I(y)) -> { return `I(x * y); }
                _         -> { throw new MOFException(); }
            };
            return null;
        }});



    %gom {
        module Tree
        imports String int
        abstract syntax
        Tree = L(x:int)
             | N(n1:Tree, n2:Tree)
    }


    public static Tree t = `N(N(N(L(1),L(2)),N(L(3),L(4))),N(N(L(5),L(6)),N(L(7),L(8))));


    public static Visitor<P<Visitable,Visitable>, P<Visitable,Visitable>> vtree = Visitor.map(
        new Fun<P<Visitable,Visitable>, P<Visitable,Visitable>>() {
        public P<Visitable,Visitable> apply(P<Visitable,Visitable> u) throws MOFException {

            // usual printf debuging
            System.out.println("[Tree] reducing " + u.toString());

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
                _    << l && _    << r -> { throw new MOFException(); }
            };
            return null;
        }});



    public static void run() throws MOFException {
        System.out.println("MOF POC\n\n");


        System.out.println("Expr\n");
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
        Visitor<Visitable,Visitable> w = All.innerMost(v);

        // The show must go on!
        System.out.println(w.visit(e));



        System.out.println("Tree\n");
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
        System.out.println(wtree.visitUZ(t).focus);


    }


}