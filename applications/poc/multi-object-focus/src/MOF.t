import lib.MOFException;
import lib.sl.*;
import term.*;
import lib.fun.*;
import lib.zip.*;

public class MOF {
 %include{MOF.tom}

  public static void main(String[] args) throws MOFException {
    System.out.println("MOF POC\n");

    Term t = `F("f",L("toto"),L("tata"));


    Visitor<Term,Term> v = VisitorLib.map( new Fun<Term,Term>() {
       public Term apply(Term u) throws MOFException {
          System.out.println("toto->titi: recieved " + u.toString());
          %match(u) {
             L("toto") -> { return `L("titi");        }
             _         -> { throw new MOFException(); }
          };
          return null;
       }});

    Visitor<Term,Term> w = (new One()).seq(v);
    System.out.println(w.run(t).run());
  }


}