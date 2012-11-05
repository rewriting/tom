import lib.*;
import lib.sl.*;
import tom.library.sl.Visitable;

import mof.expr.types.*;

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
            System.out.println("reducing " + u.toString());

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


    public static void run() throws MOFException {
        System.out.println("MOF POC\n");

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
    }


}