import sequents.*;
import sequents.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import java.util.*;

import java.io.*;
import antlr.*;
import antlr.collections.*;

class Unification {

  %include { sequents/sequents.tom }
  %include { mutraveler.tom }

  /**
  /* returns a Symbol Table if it matches, else returns false 
   **/
  public static HashMap match(Prop atom, Prop p) {
    return match(atom, p, new HashMap());
  }

  private static HashMap match(sequentsAbstractType p1, sequentsAbstractType p2, HashMap tds) {

    %match (Prop p1, Prop p2) {
      relationAppl(x,args1), relationAppl(x,args2) -> {
        return match(`args1,`args2, tds);
      }
    }

    %match (TermList p1, TermList p2) {
      (), () -> { return tds; }

      // 2 listes de longueur differentes, clash
      (_*), () -> { return null; }

      (x1,t1*), (x2,t2*) -> { 
        tds = match(`x1,`x2,tds);
        if (tds == null)
          return null;
        else
          return match(`t1,`t2,tds);
      }
    }

    %match (Term p1, Term p2) {

      funAppl(f,args1), funAppl(f,args2) -> { 
        return match(`args1, `args2, tds);
      }

      Var(x), t@Term -> { 
        if(tds.containsKey(`x)) {
          if (tds.get(`x) == `t) return tds;
          else return null;
        } else {
          tds.put(`x, `t);
          return tds;
        }
      }
    }

    return null; // clash par defaut
  }

  public static void main(String[] args) throws Exception {
    SeqLexer lexer = new SeqLexer(new DataInputStream(System.in));
    SeqParser parser = new SeqParser(lexer);
    parser.start1();
    AST t = parser.getAST();
    SeqTreeParser walker = new SeqTreeParser();
    Prop p1  = walker.pred(t);
    parser.start1();
    t = parser.getAST();
    Prop p2  = walker.pred(t);

    Unification uni = new Unification();
    HashMap res = uni.match(p1,p2);

    if (res == null) {
      System.out.println("clash !");
    } else {
      Set entries = res.entrySet();
      Iterator it = entries.iterator();
      while( it.hasNext() ) {
        Map.Entry ent = (Map.Entry) it.next();
        System.out.println(ent.getKey() 
            + " -> " 
            + PrettyPrinter.prettyPrint((sequentsAbstractType)ent.getValue()));
      }
    }
  }
}
