import sequents.*;
import sequents.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.MuStrategy;
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
   * returns a Symbol Table if it matches, else null 
   **/
  public static HashMap<String, Term> match(Prop p1, Prop p2) {
    return match(p1, p2, new HashMap<String, Term>(), 0);
  }

  // if we want to check consistancy with another table symbol, warning : side effect on ts
  public static HashMap<String, Term> match(Prop p1, Prop p2, HashMap<String,Term> ts) {
    return match(p1, p2, ts, 0);
  }
  
  public static HashMap<String, Term> match(Term pattern, Term subject) {
    return match(pattern, subject, new HashMap<String, Term>(), 0);
  }

  private static HashMap<String, Term> match(sequentsAbstractType p1,
      sequentsAbstractType p2, HashMap<String, Term> tds, int varcount) {

    %match (Prop p1, Prop p2) {
      relationAppl(x,args1), relationAppl(x,args2) -> {
        return match(`args1,`args2, tds, varcount);
      }
      and(l1,r1), and(l2,r2) -> {
        match(`l1,`l2, tds, varcount);
        if(tds != null) return match(`r1,`r2, tds, varcount);
      }
      or(l1,r1), or(l2,r2) -> {
        match(`l1,`l2, tds, varcount);
        if(tds != null) return match(`r1,`r2, tds, varcount);
      }
      implies(l1,r1), implies(l2,r2) -> {
        match(`l1,`l2, tds, varcount);
        if(tds != null) return match(`r1,`r2, tds, varcount);
      }
      bottom(),bottom() -> {
        return tds;
      }
      top(),top() -> {
        return tds;
      }
      forAll(var1,r1), forAll(var2,r2) -> {
        /* FIXME : not safe if we add _ in ids*/
        Term new_var = `funAppl(fun("lemu_var_" + varcount),concTerm());
        Prop new_r1 = (Prop) Utils.replaceFreeVars(`r1,`Var(var1),new_var);
        Prop new_r2 = (Prop) Utils.replaceFreeVars(`r2,`Var(var2),new_var);
        return match(new_r1, new_r2, tds, varcount+1);
      }
      exists(var1,r1), exists(var2,r2) -> {
        /* FIXME : not safe if we add _ in ids*/
        Term new_var = `funAppl(fun("lemu_var_" + varcount),concTerm());
        Prop new_r1 = (Prop) Utils.replaceFreeVars(`r1,`Var(var1),new_var);
        Prop new_r2 = (Prop) Utils.replaceFreeVars(`r2,`Var(var2),new_var);
        return match(new_r1, new_r2, tds, varcount+1);
      }
    }

    %match (TermList p1, TermList p2) {
      (), () -> { return tds; }

      // 2 listes de longueur differentes, clash
      (_,_*), () -> { return null; }
      (), (_,_*) -> { return null; }

      (x1,t1*), (x2,t2*) -> { 
        tds = match(`x1,`x2,tds,varcount);
        if (tds == null)
          return null;
        else
          return match(`t1,`t2,tds,varcount);
      }
    }

    %match (Term p1, Term p2) {

      funAppl(f,args1), funAppl(f,args2) -> { 
        return match(`args1, `args2, tds,varcount);
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

  public static sequentsAbstractType reduce(sequentsAbstractType t, TermRuleList tl) {
    sequentsAbstractType res = null;
    while(res != t) {
      res = t;
      %match(TermRuleList tl) {
        (_*,r,_*) -> {
          MuStrategy rr =  (MuStrategy) `InnermostId(ApplyTermRule(r));
          t = (sequentsAbstractType) rr.apply(t);
        }
      }
    }
    return res;
  }

  %strategy ApplyTermRule(rule:TermRule) extends `Identity() {
    visit Term {
      x -> {
        return `reduce(x,rule);
      }
    }
  }


 /* ---------- for reduce ----------*/

  %strategy RenameIntoTemp() extends `Identity() {
    visit Prop {
      forAll(n,p) -> { return `forAll("temp_"+n,p); }
      exists(n,p) -> { return `exists("temp_"+n,p); }
    }
    visit Term {
       Var(name) -> { return `Var("temp_" + name); }
    }
  }

  %strategy RenameFromTemp() extends `Identity() {
    visit Prop {
      forAll(n,p) -> { if (`n.startsWith("temp_")) return `forAll(n.substring(5),p); }
      exists(n,p) -> { if (`n.startsWith("temp_")) return `exists(n.substring(5),p); }
    }
    visit Term {
       Var(name) -> { if (`name.startsWith("temp_")) return `Var(name.substring(5)); }
    }
  }

  public static sequentsAbstractType 
    substPreTreatment(sequentsAbstractType term) {
      return (sequentsAbstractType)
        ((MuStrategy) `TopDown(RenameIntoTemp())).apply(term);
    }

  public static sequentsAbstractType 
    substPostTreatment(sequentsAbstractType term) {
      return (sequentsAbstractType)
        ((MuStrategy) `TopDown(RenameFromTemp())).apply(term);
  }

 /* ----------------------------------*/

  public static Term reduce(Term t, TermRule rule) {
    %match(TermRule rule) {
      termrule(lhs,rhs) -> {
        t = (Term) substPreTreatment(t);

        // recuperage de la table des symboles
        HashMap<String,Term> tds = match(`lhs, t);
        if (tds == null) return (Term) substPostTreatment(t); 

        Term res = `rhs;

        // renommage des variables
        Set<Map.Entry<String,Term>> entries = tds.entrySet();
        for (Map.Entry<String,Term> ent: entries) {
          Term old_term = `Var(ent.getKey());
          Term new_term = ent.getValue();
          res = (Term) Utils.replaceTerm(res, old_term, new_term);
        }

        return (Term) substPostTreatment(res);
      }
    }
    return t;
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
    HashMap<String, Term> res = uni.match(p1,p2);

    if (res == null) {
      System.out.println("clash !");
    } else {
      Set<Map.Entry<String,Term>> entries = res.entrySet();
      for(Map.Entry<String,Term> ent: entries) {
        System.out.println(ent.getKey() 
            + " -> " 
            + PrettyPrinter.prettyPrint((sequentsAbstractType)ent.getValue()));
      }
    }
  }
}

