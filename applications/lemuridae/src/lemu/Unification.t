import sequents.*;
import sequents.types.*;

//import tom.library.strategy.mutraveler.MuTraveler;
//import tom.library.strategy.mutraveler.MuStrategy;
//import jjtraveler.VisitFailure;
//import jjtraveler.reflective.VisitableVisitor;

import tom.library.sl.*;
import java.util.*;

import java.io.*;
import antlr.*;
import antlr.collections.*;

class Unification {

  %include { sequents/sequents.tom }
  %include { sl.tom }

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
        Term new_var = `funAppl(("lemu_var_" + varcount),concTerm());
        Prop new_r1 = (Prop) Utils.replaceFreeVars(`r1,`Var(var1),new_var);
        Prop new_r2 = (Prop) Utils.replaceFreeVars(`r2,`Var(var2),new_var);
        return match(new_r1, new_r2, tds, varcount+1);
      }
      exists(var1,r1), exists(var2,r2) -> {
        /* FIXME : not safe if we add _ in ids*/
        Term new_var = `funAppl(("lemu_var_" + varcount),concTerm());
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

      Var(x), t -> { 
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

  public static sequentsAbstractType reduce(sequentsAbstractType t, TermRuleList tl, PropRuleList pl) {
      Strategy ar =  `InnermostId(ApplyRules(tl,pl));
      try { return (sequentsAbstractType) ar.visit(t); }
      catch (VisitFailure e) { e.printStackTrace(); throw new RuntimeException(); }
  }

  %strategy ApplyRules(tl:TermRuleList, pl:PropRuleList) extends `Identity() {
    visit Term {
      x -> { 
        Term res = `x;
        %match(TermRuleList tl) {
          (_*,r,_*) -> { res = `reduceTerm(res,r); }
        }
        return res;
      }
    }
    visit Prop {
      p -> { 
        Prop res = `p;
        %match(PropRuleList pl) {
          (_*,r,_*) -> { res = `reduceProp(res,r); }
        }
        return res;
      }
    }
  }

 /* ---------- to ensure fresh vars in rewrite/super rules ----------*/

  %typeterm StringSet { implement { Set<String> } is_sort(t) { t instanceof Set} }

  %strategy RenameIntoTemp(bounded : StringSet) extends `Fail() {
    visit Prop {
      forAll(n,p) -> { 
        bounded.add(`n); 
        Prop res = (Prop) 
          `mu(MuVar("y"),Choice(RenameIntoTemp(bounded),All(MuVar("y")))).visit(`p);
        bounded.remove(`n);
        return `forAll(n,res);
      }
      exists(n,p) -> { 
        bounded.add(`n); 
        Prop res = (Prop) 
          `mu(MuVar("y"),Choice(RenameIntoTemp(bounded),All(MuVar("y")))).visit(`p);
        bounded.remove(`n);
        return `exists(n,res);
      }
   }
    visit Term {
       v@Var(name) -> { 
         if (!bounded.contains(`name)) return `Var("@" + name); 
         else return `v;
       }
    }
  }

  public static sequentsAbstractType 
    substPreTreatment(sequentsAbstractType term) {
      HashSet<String> bounded = new HashSet();
      try {
        return (sequentsAbstractType)
          `mu(MuVar("y"),Choice(RenameIntoTemp(bounded),All(MuVar("y")))).visit(term);
      } catch (VisitFailure e) { e.printStackTrace(); throw new RuntimeException(); }
    }

 /* ----------------------------------*/

  public static Term reduceTerm(Term t, TermRule rule) {
    %match(TermRule rule) {
      termrule(lhs,rhs) -> {
        // recuperage de la table des symboles
        HashMap<String,Term> tds = match(`lhs, t);
        if (tds == null) return t;

        // substitution
        Term res = (Term) Utils.replaceVars(`rhs, tds);
        return res;
      }
    }
    return t;
  }

  public static Prop reduceProp(Prop p, PropRule rule) {
    %match(PropRule rule) {
      proprule(lhs,rhs) -> {
        // recuperage de la table des symboles
        HashMap<String,Term> tds = match(`lhs, p);
        if (tds == null) return p;

        Prop res = `rhs;
        // substitution
        Set<Map.Entry<String,Term>> entries = tds.entrySet();
        for (Map.Entry<String,Term> ent: entries) {
          Term old_var = `Var(ent.getKey());
          Term new_term = ent.getValue();
          res = Utils.replaceFreeVars(res, old_var, new_term);
        }
        return res;
      }
    }
    return p;
  }
}

