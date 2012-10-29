import lib.*;
import lib.sl.*;
import term.*;

public class MOF {
 %include{term/Term.tom}

  public static Term t = `F("f",L("totp"),L("toto"));

  public static Visitor<Term,Term> v = Visitor.map( new Fun<Term,Term>() {
      public Term apply(Term u) throws MOFException {
          System.out.println("toto->titi: recieved " + u.toString());
          %match(u) {
              L("toto") -> { return `L("titi");        }
              _         -> { throw new MOFException(); }
          };
          return null;
      }});


    public static void main(String[] args) throws MOFException {
    System.out.println("MOF POC\n");
    Visitor<Term,Term> w = TSL.one(v);
    System.out.println(w.run(t));
  }


}