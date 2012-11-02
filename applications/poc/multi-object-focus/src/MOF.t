import lib.*;
import lib.sl.*;
import tom.library.sl.Visitable;

import mof.term.types.*;

public class MOF {
    %include { string.tom }

    %gom {
        module Term
        imports String
        abstract syntax
        Term = L(name:String)
             | F(fsym:String, left:Term, right:Term)
    }

    public static Term t = `F("f",L("totp"),L("toto"));

    /*
     Instead of defining a visitor by defining visitZK, we build one by mapping a function into a Visitor
     getChildAt can give either a Term or a String, so we define the function on their common super type.
      */

    public static Visitor<Visitable,Visitable> v = Visitor.map( new Fun<Visitable,Visitable>() {
        public Visitable apply(Visitable u) throws MOFException {

            // usual printf debuging
            System.out.println("toto->titi: recieved " + u.toString());

            /*
             The match is only defined on Term and NOT String, so we fail on String!
             Forgetting this case will result in a uncastable exception between VisitableBuiltin (String)
             and Term.
              */

            if (!(u instanceof Term)) throw new MOFException();

            /*
             Now that we are on terms, we can match.
             */
            %match(u) {
                L("toto") -> { return `L("titi");        }
                _         -> { throw new MOFException(); }
            };
            return null;
        }});


    public static void main(String[] args) throws MOFException {
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
        Visitor<Visitable,Visitable> w = new SelectChild<Visitable,Visitable>().seq(v).reset().up();

        // The show must go on!
        System.out.println(w.visit(t));
    }


}