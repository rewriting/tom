import sequents.*;
import sequents.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.MuStrategy;
import tom.library.strategy.mutraveler.Position;
import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Stack;

import java.io.*;
import antlr.*;
import antlr.collections.*;

public class ProofBuilder {

  %include { sequents/sequents.tom }
  %include { mustrategy.tom }
  %include { string.tom }
  %include { util/LinkedList.tom }

  %strategy AddInContexts(ctxt:Context) extends `Identity() {
    visit Sequent {
      sequent(hyp,concl) -> { return `sequent(context(hyp*,ctxt*),concl);} 
    }
  }

  %strategy PutInConclusion(ctxt:Context) extends `Identity() {
    visit Sequent {
      sequent(hyp,concl) -> { return `sequent(hyp,context(concl*,ctxt*));} 
    }
  }

  public static SeqList applyRule(Rule rule, Sequent seq, Prop active,
      Map<Term,Term> args) throws Exception {

    SeqList res = rule.getprem();
    // si la regle instancie des variables, il faut dupliquer la proposition
    Prop conclusion = rule.getconcl();

    // renommage temporaire pour eviter les collisions
    // FIXME : renommer dans les regles de reecriture plutot
    Prop active_prepared = (Prop) Unification.substPreTreatment(active);

    // recuperage de la table des symboles
    HashMap<String,Term> tds = Unification.match(conclusion, active_prepared);
    if (tds == null) throw new Exception("active formula and rule conclusion don't match");

    // renommage des variables
    Set<Map.Entry<String,Term>> entries= tds.entrySet();
    for (Map.Entry<String,Term> ent: entries) {
      Term old_term = `Var(ent.getKey());
      Term new_term = ent.getValue();
      res = (SeqList) Utils.replaceFreeVars(res, old_term, new_term); 
    }

    // post traitement 
    res = (SeqList) Unification.substPostTreatment(res);

    // creation des variables fraiches (forall right et exists left)
    Set<Term> fresh = Utils.getSideConstraints(rule.getprem());
    for (Term fvar : fresh) {
      String bname = fvar.getbase_name();
      Term new_var = Utils.freshVar(bname, seq);
      res = (SeqList) Utils.replaceTerm(res,fvar,new_var);
    }

    // remplacement des nouvelles variables (forall left et exists right)
    Set<Term> new_vars = Utils.getNewVars(rule.getprem());
    if (new_vars.size() != args.size())
      throw new Exception("Wrong variables number");
    Set<Map.Entry<Term,Term>> entries2 = args.entrySet();
    for (Map.Entry<Term,Term> ent: entries2) {
      Term old_term = ent.getKey();
      if (! new_vars.contains(old_term))
        throw new Exception("Variable " + old_term.getname() +" not present in the rule");
      Term new_term = ent.getValue();
      res = (SeqList) Utils.replaceFreeVars(res, old_term, new_term);
    }

    // ajout des contextes dans les premisses
b: {
     %match (Rule rule, Sequent seq, Prop active) {

       // si c'est une regle gauche
       ruledesc(0,_,_,_), sequent(ctxt@(u*,act,v*),c), act -> {
         Context gamma = args.size() <= 0 ? `context(u*,v*) : `ctxt;
         res = (SeqList) `TopDown(AddInContexts(gamma)).apply(res); 
         res = (SeqList) `TopDown(PutInConclusion(c)).apply(res); 
         break b;
       }

       // si c'est une regle droite
       ruledesc(1,_,_,_), sequent(ctxt,c@(u*,act,v*)), act -> {
         res = (SeqList) `TopDown(AddInContexts(ctxt)).apply(res);
         Context delta = args.size() <= 0 ? `context(u*,v*) : `c;
         res = (SeqList) `TopDown(PutInConclusion(delta)).apply(res); 
         break b;
       }

       // probleme
       _,_,_ -> { throw new Exception("wrong hand side rule application"); }
     }
   }

   return res;
  }

  %typeterm TermMap {
    implement { Map<Term,Term> }
  }

  %strategy ApplyRule(rule: Rule, active: Prop, args: TermMap) extends Fail() {
    visit Tree {
      rule[c=seq] -> {

        SeqList res = rule.getprem();
        Prop conclusion = rule.getconcl();

        // renommage temporaire pour eviter les collisions
        // FIXME : renommer dans les regles de reecriture plutot
        Prop active_prepared = (Prop) Unification.substPreTreatment(active);

        // recuperage de la table des symboles
        HashMap<String,Term> tds = Unification.match(conclusion, active_prepared);
        if (tds == null) throw new VisitFailure("active formula and rule conclusion don't match");

        // renommage des variables
        Set<Map.Entry<String,Term>> entries= tds.entrySet();
        for (Map.Entry<String,Term> ent: entries) {
          Term old_term = `Var(ent.getKey());
          Term new_term = ent.getValue();
          res = (SeqList) Utils.replaceFreeVars(res, old_term, new_term); 
        }

        // post traitement 
        res = (SeqList) Unification.substPostTreatment(res);

        // creation des variables fraiches (forall right et exists left)
        Set<Term> fresh = Utils.getSideConstraints(rule.getprem());
        for (Term fvar : fresh) {
          String bname = fvar.getbase_name();
          Term new_var = Utils.freshVar(bname, `seq);
          res = (SeqList) Utils.replaceTerm(res,fvar,new_var);
        }

        // remplacement des nouvelles variables (forall left et exists right)
        Set<Term> new_vars = Utils.getNewVars(rule.getprem());
        if (new_vars.size() != args.size())
          throw new VisitFailure("Wrong variables number");
        Set<Map.Entry<Term,Term>> entries2 = args.entrySet();
        for (Map.Entry<Term,Term> ent: entries2) {
          Term old_term = ent.getKey();
          if (! new_vars.contains(old_term))
            throw new VisitFailure("Variable " + old_term.getname() +" not present in the rule");
          Term new_term = ent.getValue();
          res = (SeqList) Utils.replaceFreeVars(res, old_term, new_term);
        }

        // ajout des contextes dans les premisses
b: {
        %match (rule, seq, Prop active) {

          // si c'est une regle gauche
          ruledesc(0,_,_,_), sequent(ctxt@(u*,act,v*),c), act -> {
            Context gamma = args.size() <= 0 ? `context(u*,v*) : `ctxt;
            res = (SeqList) `TopDown(AddInContexts(gamma)).apply(res); 
            res = (SeqList) `TopDown(PutInConclusion(c)).apply(res); 
            break b;
          }

          // si c'est une regle droite
          ruledesc(1,_,_,_), sequent(ctxt,c@(u*,act,v*)), act -> {
            res = (SeqList) `TopDown(AddInContexts(ctxt)).apply(res);
            Context delta = args.size() <= 0 ? `context(u*,v*) : `c;
            res = (SeqList) `TopDown(PutInConclusion(delta)).apply(res); 
            break b;
          }

          // probleme
          _,_,_ -> { throw new VisitFailure("wrong hand side rule application"); }
        }
   }

        // creating open leaves
        Premisses newprems = `premisses();
        %match(SeqList res) {
          (_*,x,_*) -> {
            newprems = `premisses(newprems*,createOpenLeaf(x));    
          }
        }

        return `rule(customRuleInfo(""),newprems,seq,active);
      }
    }
  }




  /**
   * classical rules
   **/

  private static Tree createOpenLeaf(Sequent concl) {
    return `rule(openInfo(),premisses(),concl,nullProp());
  }

  %strategy ApplyAxiom() extends Fail() {
    visit Tree {
      rule[c=seq@sequent((_*,act,_*),(_*,act,_*))] -> {
        return `rule(axiomInfo(),premisses(),seq,act);
      }
    }
  }

  %strategy ApplyImpliesR(active:Prop) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent(d,(X*,act@implies(p1,p2),Y*)), act -> {
            Tree t1 = createOpenLeaf(`sequent(context(d*,p1),context(X*,p2,Y*)));
            return `rule(impliesRightInfo(),premisses(t1),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyImpliesL(active:Prop) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent((X*,act@implies(p1,p2),Y*),g), act -> {
            Tree t1 = createOpenLeaf(`sequent(context(X*,Y*),context(p1,g*)));
            Tree t2 = createOpenLeaf(`sequent(context(X*,p2,Y*),g));
            return `rule(impliesLeftInfo(),premisses(t1,t2),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyAndR(active:Prop) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent(d,(X*,act@and(p1,p2),Y*)), act -> {
            Tree t1 = createOpenLeaf(`sequent(d,context(X*,p1,Y*)));
            Tree t2 = createOpenLeaf(`sequent(d,context(X*,p2,Y*)));
            return `rule(andRightInfo(),premisses(t1,t2),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyAndL(active:Prop) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent((X*,act@and(p1,p2),Y*),g), act -> {
            Tree t1 = createOpenLeaf(`sequent(context(X*,p1,p2,Y*),g));
            return `rule(andLeftInfo(),premisses(t1),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyOrR(active:Prop) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent(d,(X*,act@or(p1,p2),Y*)), act -> {
            Tree t1 = createOpenLeaf(`sequent(d,context(X*,p1,p2,Y*)));
            return `rule(orRightInfo(),premisses(t1),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyOrL(active:Prop) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent((X*,act@or(p1,p2),Y*),g), act -> {
            Tree t1 = createOpenLeaf(`sequent(context(X*,p1,Y*),g));
            Tree t2 = createOpenLeaf(`sequent(context(X*,p2,Y*),g));
            return `rule(andLeftInfo(),premisses(t1,t2),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyBottom() extends Fail() {
    visit Tree {
      rule[c=seq@sequent((_*,act@bottom(),_*),_)] -> {
        return `rule(bottomInfo(),premisses(),seq,act);
      }
    }
  }

  %strategy ApplyTop() extends Fail() {
    visit Tree {
      rule[c=seq@sequent(_,(_*,act@top(),_*))] -> {
        return `rule(topInfo(),premisses(),seq,act);
      }
    }
  }

 %strategy ApplyCut(prop:Prop) extends Fail() {
    visit Tree {
      rule[c=seq@sequent(l,r)] -> {
        Tree t1 = createOpenLeaf(`sequent(l*,context(r*,prop)));
        Tree t2 = createOpenLeaf(`sequent(context(l*,prop),r));
        return `rule(cutInfo(prop),premisses(t1,t2),seq,nullProp());
      }
    }
  }

  %strategy ApplyForAllR(active:Prop) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent(d,(X*,act@forAll(n,p),Y*)), act -> {
            Term nvar = Utils.freshVar(`n,`seq);
            Prop res = (Prop) Utils.replaceFreeVars(`p, `Var(n), nvar); 
            Tree t1 = createOpenLeaf(`sequent(d,context(X*,res,Y*)));
            return `rule(forAllRightInfo(nvar),premisses(t1),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyForAllL(active:Prop) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent(d,(X*,act@forAll(n,p),Y*)), act -> {
            Term nvar = Utils.freshVar(`n,`seq);
            Prop res = (Prop) Utils.replaceFreeVars(`p, `Var(n), nvar); 
            Tree t1 = createOpenLeaf(`sequent(d,context(X*,res,Y*)));
            return `rule(forAllRightInfo(nvar),premisses(t1),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyExistsR(active:Prop,term:Term) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent((X*,act@exists(n,p),Y*),g), act -> {
            Prop res = (Prop) Utils.replaceFreeVars(`p, `Var(n), term); 
            Tree t1 = createOpenLeaf(`sequent(context(X*,res,Y*),g));
            return `rule(existsRightInfo(term),premisses(t1),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyExistsL(active:Prop,term:Term) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent((X*,act@exists(n,p),Y*),g), act -> {
            Prop res = (Prop) Utils.replaceFreeVars(`p, `Var(n), term); 
            Tree t1 = createOpenLeaf(`sequent(context(X*,res,Y*),g));
            return `rule(existsRightInfo(term),premisses(t1),seq,act);
          }
        }
      }
    }
  }

  // And

  public static SeqList applyAndR(Sequent seq, Prop active) throws Exception {
    %match(Sequent seq, Prop active) {
      sequent(ctxt,(X*,act@and(p1,p2),Y*)), act -> {
        return `concSeq(sequent(ctxt,context(X*,p1,Y*)),sequent(ctxt,context(X*,p2,Y*)));
      }
    }
    throw new Exception("can't apply rule and R");
  }

  public static SeqList applyAndL(Sequent seq, Prop active) throws Exception {
    %match(Sequent seq, Prop active) {
      sequent((X*,act@and(p1,p2),Y*),c), act -> {
        return `concSeq(sequent(context(X*,p1,p2,Y*),c));
      }
    }
    throw new Exception("can't apply rule and L");
  }

  // Or

  public static SeqList applyOrR(Sequent seq, Prop active) throws Exception {
    %match(Sequent seq, Prop active) {
      sequent(ctxt,(X*,act@or(p1,p2),Y*)), act -> {
        return `concSeq(sequent(ctxt,context(X*,p1,p2,Y*)));
      }
    }
    throw new Exception("can't apply rule Or R 1");
  }

  public static SeqList applyOrL(Sequent seq, Prop active) throws Exception {
    %match(Sequent seq, Prop active) {
      sequent((X*,act@or(p1,p2),Y*),c), act -> {
        return `concSeq(sequent(context(X*,p1,Y*),c), sequent(context(X*,p2,Y*),c));
      }
    }
    throw new Exception("can't apply rule Or L");
  }

  // Implies

  public static SeqList applyImpliesR(Sequent seq, Prop active) throws Exception {
    %match(Sequent seq, Prop active) {
      sequent(ctxt,(X*,act@implies(p1,p2),Y*)), act -> {
        return `concSeq(sequent(context(ctxt*,p1),context(X*,p2,Y*)));
      }
    }
    throw new Exception("can't apply rule => R");
  }

  public static SeqList applyImpliesL(Sequent seq, Prop active) throws Exception {
    %match(Sequent seq, Prop active) {
      sequent((X*,act@implies(p1,p2),Y*),c), act -> {
        return `concSeq(sequent(context(X*,Y*),context(p1,c*)), sequent(context(X*,p2,Y*),c));
      }
    }
    throw new Exception("can't apply rule => L");
  }

  // Axiom

  public static SeqList applyAxiom(Sequent seq, Prop active) throws Exception {
    %match(Sequent seq, Prop active) {
      sequent((_*,act,_*),(_*,act,_*)), act -> { return `concSeq(); }
    }
    throw new Exception("this is not an axiom");
  }

  // Bottom

  public static SeqList applyBottom(Sequent seq) throws Exception {
    %match(Sequent seq) {
      sequent((_*,bottom(),_*),_) -> {
        return `concSeq();
      }
    }
    throw new Exception("can't apply rule bottom");
  }

  // Top

  public static SeqList applyTop(Sequent seq) throws Exception {
    %match(Sequent seq) {
      sequent(_,(_*,top(),_*)) -> {
        return `concSeq();
      }
    }
    throw new Exception("can't apply rule top");
  }

  // Cut
  public static SeqList applyCut(Sequent seq, Prop prop) {
    %match(Sequent seq) {
      sequent(l,r) -> {
        return `concSeq(sequent(l*,context(r*,prop)),sequent(context(l*,prop),r));
      }
    }
    return null;
  }

  // forall
  public static SeqList applyForAllR(Sequent seq, Prop active, Term new_var) throws Exception {
    %match(Sequent seq, Prop active) {
      sequent(ctxt,(X*,act@forAll(n,p),Y*)), act -> {
        Prop res = (Prop) Utils.replaceFreeVars(`p, `Var(n), new_var); 
        return `concSeq(sequent(ctxt,context(X*,res,Y*)));
      }
    }
    throw new Exception("can't apply rule forall R");
  }

  public static SeqList applyForAllL
    (Sequent seq, Prop active, Term new_var) throws Exception 
    {
      %match(Sequent seq, Prop active) {
        sequent((X*,act@forAll(n,p),Y*),c), act -> {
          Prop res = (Prop) Utils.replaceFreeVars(`p, `Var(n), new_var); 
          return `concSeq(sequent(context(X*,res,Y*),c));
        }
      }
      throw new Exception("can't apply rule forall L");
    }

  // exists

  public static SeqList applyExistsR(Sequent seq, Prop active, Term new_var) throws Exception {
    %match(Sequent seq, Prop active) {
      sequent(ctxt,(X*,act@exists(n,p),Y*)), act -> {
        Prop res = (Prop) Utils.replaceFreeVars(`p, `Var(n), new_var); 
        return `concSeq(sequent(ctxt,context(X*,res,Y*)));
      }
    }
    throw new Exception("can't apply rule exists R");
  }

  public static SeqList applyExistsL(Sequent seq, Prop active, Term new_var) throws Exception 
  {
    %match(Sequent seq, Prop active) {
      sequent((X*,act@exists(n,p),Y*),c), act -> {
        Prop res = (Prop) Utils.replaceFreeVars(`p, `Var(n), new_var); 
        return `concSeq(sequent(context(X*,res,Y*),c));
      }
    }
    throw new Exception("can't apply rule exists L");
  }

 // contraction

  public static SeqList applyContractionL(Sequent seq, Prop active) throws Exception 
  {
    %match(Sequent seq, Prop active) {
      sequent((X*,act,Y*),c), act -> {
        return `concSeq(sequent(context(X*,act,act,Y*),c));
      }
    }
    throw new Exception("can't apply rule contraction L");
  }

  public static SeqList applyContractionR(Sequent seq, Prop active) throws Exception 
  {
    %match(Sequent seq, Prop active) {
      sequent(ctxt,(X*,act,Y*)), act -> {
        return `concSeq(sequent(ctxt,context(X*,act,act,Y*)));
      }
    }
    throw new Exception("can't apply rule contraction R");
  }

  // weakening

  public static SeqList applyWeakL(Sequent seq, Prop active) throws Exception 
  {
    %match(Sequent seq, Prop active) {
      sequent((X*,act,Y*),c), act -> {
        return `concSeq(sequent(context(X*,Y*),c));
      }
    }
    throw new Exception("can't apply rule weakening L");
  }

  public static SeqList applyWeakR(Sequent seq, Prop active) throws Exception 
  {
    %match(Sequent seq, Prop active) {
      sequent(ctxt,(X*,act,Y*)), act -> {
        return `concSeq(sequent(ctxt,context(X*,Y*)));
      }
    }
    throw new Exception("can't apply rule weakening R");
  }


  /* --- User interaction  --- */

  private String prettyGoal(ArrayList<Prop> hyp, ArrayList<Prop> concl, boolean left, int focus) {
    String res = "";
    for (int i=0; i < hyp.size(); i++) {
      res +=  (left && focus == i+1) ? "*" : " ";
      res += "h"+ (i+1) + ":  ";
      res += PrettyPrinter.prettyPrint(hyp.get(i)) + "\n";
    }
    res += "-----------\n";
    for (int i=0; i < concl.size(); i++) {
      res +=  (!left && focus == i+1) ? "*" : " ";
      res += "c"+ (i+1) + ":  ";
      res += PrettyPrinter.prettyPrint(concl.get(i)) + "\n";
    }
    return res;
  }

  private ArrayList<Prop> getHypothesis(Sequent seq) {
    ArrayList<Prop> res = new ArrayList<Prop>();
    Context ctxt = seq.geth();
    %match (Context ctxt) {
      (_*, p, _*) -> {
        res.add(`p);
      }
    }
    return res;
  }

  private ArrayList<Prop> getConclusions(Sequent seq) {
    ArrayList<Prop> res = new ArrayList<Prop>();
    Context ctxt = seq.getc();
    %match (Context ctxt) {
      (_*, p, _*) -> {
        res.add(`p);
      }
    }
    return res;
  }

  %strategy getOpenPositions(list:LinkedList) extends `Identity() {
    visit Tree {
      rule[type=openInfo[]] -> {
        list.add(getPosition());
      }
    }
  }

  private Sequent getSequentByPosition(Tree tree, Position pos) {
    Tree res = null;
    try {res = (Tree) MuTraveler.init(pos.getSubterm()).visit(tree); }
    catch (VisitFailure e) { e.printStackTrace(); }
    return res.getc();
  }

  /* ----------- auxiliary functions for buildProofTree ---------------*/

  private Tree ruleCommand(Tree tree, Position pos,
      Prop active, boolean focus_left, int n) throws Exception {
    HashMap<Term,Term> args = new HashMap<Term,Term>();

    if (n == -1) { // trying to apply one unique rule

      for(int i=0; i<newRules.size(); i++) {
        Rule rule = newRules.get(i); 
        Prop conclusion = rule.getconcl();
        HashMap<String,Term> tds = Unification.match(conclusion, active);
        if (tds != null && ((focus_left && rule.geths() == 0) || (!focus_left && rule.geths() == 1))) {
          if (n != -1)  throw new Exception("more than one matching rule, give a number"); // more than one rule
          else n = i; 
        }
      }
    }

    if (n < newRules.size()) {
      // TODO verify hand side
      Rule rule = newRules.get(n);

      // asking for new vars
      Set<Term> new_vars = Utils.getNewVars(rule.getprem());
      for (Term t : new_vars) {
        String varname = t.getname();
        System.out.print("new term for variable " + varname + " in rule " + n + " > ");
        Term new_var = Utils.getTerm();
        args.put(t, new_var);
      }

      Sequent goal = getSequentByPosition(tree, pos);
      SeqList slist = applyRule(rule, goal, active, args);

      // create open leaves
      Premisses prems = `premisses();
      %match(SeqList slist) {
        (_*,s,_*) -> {
          prems = `premisses(prems*,
              rule(openInfo(), premisses(), s, s.getc().getHeadcontext())) ;
        }
      }

      // get new tree
      Tree newrule = `rule(customRuleInfo("rule\\ " + n), prems, goal, active);
      return (Tree) MuTraveler.init(pos.getReplace(newrule)).visit(tree); 
    }

    else throw new Exception("rule " + n + " doesn't exist.");
  }


  private Tree elimCommand(Tree tree, Position pos,
      Prop active, boolean focus_left) throws Exception {

    Sequent goal = getSequentByPosition(tree, pos);

    // list of the new premisses after applying a rule
    SeqList slist = null;  
    RuleType ruleType = null;
    boolean applied = false;

    // breaking right rule
    if(!focus_left) {
      %match(Prop active) {

        // And R
        and[] -> {
          ruleType = `andRightInfo();
          slist = applyAndR(goal,active);
          applied = true;
        }

        // Or R
        or[] -> {
          ruleType = `orRightInfo();
          slist = applyOrR(goal,active);
          applied = true;
        }

        // Implies R
        implies[] -> {
          ruleType = `impliesRightInfo();
          slist = applyImpliesR(goal, active);
          applied = true;
        }

        // forAll R
        forAll[var=n] -> {
          // TODO interactif ?
          Term nvar = Utils.freshVar(`n, goal);
          ruleType = `forAllRightInfo(nvar);
          slist = applyForAllR(goal, active, nvar);
          applied = true;
        }

        // exists R
        exists(x,_) -> {
          System.out.print("instance of " + `x + " > ");
          Term new_term = Utils.getTerm();
          ruleType = `existsRightInfo(new_term);
          slist = applyExistsR(goal, active, new_term);
          applied = true;
        }

      } // match(active)

    } else /* focus_left */ {
      %match(Prop active) {

        // Or L
        or[] -> {
          ruleType = `orLeftInfo();
          slist = applyOrL(goal, active);
          applied = true;
        }

        // And L
        and[] -> {
          ruleType = `andLeftInfo();
          slist = applyAndL(goal, active);
          applied = true;
        }

        // Implies L
        implies[] -> {
          ruleType = `impliesLeftInfo();
          slist = applyImpliesL(goal, active);
          applied = true;
        }

        // forAll L
        forAll(x,_) -> {
          System.out.print("instance of " + `x + " > ");
          Term new_term = Utils.getTerm();
          ruleType = `forAllLeftInfo(new_term);
          slist = applyForAllL(goal, active, new_term);
          applied = true;
        }

        // exists L
        exists[var=n] -> {
          Term nvar = Utils.freshVar(`n, goal);
          ruleType = `existsLeftInfo(nvar);
          slist = applyExistsL(goal, active, nvar);
          applied = true;
        }
      } // match(active)
    } // focus != 0

    // if one of the rules has been applied
    if (applied) {
      Premisses prems = `premisses();
      %match(SeqList slist) {
        (_*,s,_*) -> {
          prems = `premisses(prems*,
              rule(openInfo(), premisses(), s, s.getc().getHeadcontext())) ;
        }
      }
      // get new tree
      Tree newrule = `rule(ruleType, prems, goal, active);
      return (Tree) MuTraveler.init(pos.getReplace(newrule)).visit(tree); 
    } else throw new Exception ("there is no head connector");
  }

  private Tree axiomCommand(Tree tree, Position pos,
      Prop active, boolean focus_left) throws Exception {
    Sequent goal = getSequentByPosition(tree, pos);
    %match(Sequent goal) {
      sequent((_*,x,_*),(_*,x,_*)) -> { active = `x; }
    }
    applyAxiom(goal,active); 
    Tree newrule = `rule(axiomInfo(), premisses(), goal, active);
    return (Tree) MuTraveler.init(pos.getReplace(newrule)).visit(tree);
  }

  private Tree topCommand(Tree tree, Position pos,
      Prop active, boolean focus_left) throws Exception {
    Sequent goal = getSequentByPosition(tree, pos);
    applyTop(goal); 
    Tree newrule = `rule(topInfo(), premisses(), goal, active);
    return (Tree) MuTraveler.init(pos.getReplace(newrule)).visit(tree);
  }

  private Tree bottomCommand(Tree tree, Position pos,
      Prop active, boolean focus_left) throws Exception {
    Sequent goal = getSequentByPosition(tree, pos);
    applyBottom(goal); 
    Tree newrule = `rule(bottomInfo(), premisses(), goal, active);
    return (Tree) MuTraveler.init(pos.getReplace(newrule)).visit(tree);
  }

  private Tree duplicateCommand(Tree tree, Position pos, 
      Prop active, boolean focus_left) throws Exception {
    Sequent goal = getSequentByPosition(tree, pos);
    SeqList slist = null;
    if(focus_left) slist = applyContractionL(goal, active);
    else slist = applyContractionR(goal, active);
    Premisses prems = `premisses();
    %match(SeqList slist) {
      (_*,s,_*) -> { 
        prems = 
          `premisses(prems*, rule(openInfo(), premisses(), s, s.getc().getHeadcontext())) ;
      }
    }
    // get new tree
    Tree newrule = null;
    if(focus_left) newrule = `rule(contractionLeftInfo(), prems, goal, active);
    else newrule = `rule(contractionRightInfo(), prems, goal, active);
    return (Tree) MuTraveler.init(pos.getReplace(newrule)).visit(tree); 
  }

  private Tree removeCommand(Tree tree, Position pos, 
      Prop active, boolean focus_left) throws Exception {
    Sequent goal = getSequentByPosition(tree, pos);
    SeqList slist = null;
    if(focus_left) slist = applyWeakL(goal, active);
    else slist = applyWeakR(goal, active);
    Premisses prems = `premisses();
    %match(SeqList slist) {
      (_*,s,_*) -> { 
        prems = 
          `premisses(prems*, rule(openInfo(), premisses(), s, s.getc().getHeadcontext())) ;
      }
    }
    // get new tree
    Tree newrule = null;
    if(focus_left) newrule = `rule(weakLeftInfo(), prems, goal, active);
    else newrule = `rule(weakRightInfo(), prems, goal, active);
    return (Tree) MuTraveler.init(pos.getReplace(newrule)).visit(tree); 
  }

  private Tree cutCommand(Tree tree, Position pos, Prop prop) throws Exception {
    Sequent goal = getSequentByPosition(tree, pos);
    SeqList slist = applyCut(goal, prop);
    Premisses prems = `premisses();
    %match(SeqList slist) {
      (_*,s,_*) -> { 
        prems = 
          `premisses(prems*, rule(openInfo(), premisses(), s, s.getc().getHeadcontext())) ;
      }
    }
    // get new tree
    Tree newrule = `rule(cutInfo(prop), prems, goal, goal.getc().getHeadcontext());
    return (Tree) MuTraveler.init(pos.getReplace(newrule)).visit(tree); 
  }

  // FIXME uses positions ... may be easily broken
  private Tree theoremCommand(Tree tree, Position pos, String name) throws Exception {

    Tree thtree = theorems.get(`name);
    Prop conclusion = null;
    Position poscopy = (Position) pos.clone();

    %match(Tree thtree) {
      rule(_,_,sequent((),(prop)),_) -> {
        tree = cutCommand(tree,poscopy,`prop);
        poscopy.down(2);
        poscopy.down(1);
        conclusion = `prop;
      }
    }

    while(true) {
b :{
      Sequent goal = getSequentByPosition(tree, poscopy);
      %match(goal,Prop conclusion) {
        sequent((),(concl)), concl -> {
          return (Tree) ((MuStrategy) poscopy.getReplace(thtree)).apply(tree);
        }
        sequent((p,_*),_), _ -> {
          tree = removeCommand(tree,poscopy,`p,true);
          poscopy.down(2);
          poscopy.down(1);
          break b;
        }
        sequent((),(_*,p,_*)), tokeep -> {
          //FIXME replace condition by p@!tokeep in pattern when bug disappears
          if (`p != conclusion) {
            tree = removeCommand(tree,poscopy,`p,false);
            poscopy.down(2);
            poscopy.down(1);
            break b;
          }
        }
      }
      throw new Exception("can't apply theorem");
   }
    }
  }

  private Tree reduceCommand(Tree tree, Position pos, 
      Prop active, boolean focus_left) throws Exception {
    Sequent goal = getSequentByPosition(tree, pos);
    Sequent s = (Sequent) Unification.reduce(goal,newTermRules);
    Premisses prems = `premisses(rule(openInfo(), premisses(), s, s.getc().getHeadcontext()));

    // get new tree
    Tree newrule = `rule(reductionInfo(), prems, goal, active);
    return (Tree) MuTraveler.init(pos.getReplace(newrule)).visit(tree); 
  }


  private Rule applicableInAuto(Prop prop, boolean left) {
    Rule res = null; 

    for(int i=0; i<newRules.size(); i++) {
      Rule rule = newRules.get(i); 
      if( Utils.getNewVars(rule).size() == 0 ) {
        Prop conclusion = rule.getconcl();
        HashMap<String,Term> tds = Unification.match(conclusion, prop);
        if (tds != null && ((left && rule.geths() == 0) || (!left && rule.geths() == 1))) {
          if (res != null)  return null; // more than one rule
          else res = rule; 
        }
      }
    }
    return res;
  }


  private Tree autoCommandRecursive(Sequent goal) {

    SeqList slist = null;
    RuleType ruleType = null;

    %match(Sequent goal) {

      // axiom
      sequent((_*,p,_*),(_*,p,_*)) -> {
        try {
          // FIXME useful ?
          applyAxiom(goal,`p);
          return `rule(axiomInfo(), premisses(), goal, p);
        } catch (Exception e) {}
      }

      // top
      sequent(_,(_*,p@top(),_*)) -> {
        try {
          // FIXME useful ?
          applyTop(goal);
          return `rule(topInfo(), premisses(), goal, p);
        } catch (Exception e) {}
      }

      // bottom
      sequent((_*,p@bottom(),_*),_) -> {
        try {
          // FIXME useful ?
          applyBottom(goal);
          return `rule(bottomInfo(), premisses(), goal, p);
        } catch (Exception e) {}
      }

      // right hand side
      sequent(_,(_*,p,_*)) -> {
        try {
          %match(Prop `p) {
            // And R
            and[] -> {
              ruleType = `andRightInfo();
              slist = applyAndR(goal,`p);
            }

            // Or R
            or[] -> {
              ruleType = `orRightInfo();
              slist = applyOrR(goal,`p);
            }

            // Implies R
            implies[] -> {
              ruleType = `impliesRightInfo();
              slist = applyImpliesR(goal, `p);
            }

            // forAll R
            forAll[var=n] -> {
              // TODO interactif ?
              Term nvar = Utils.freshVar(`n, `p);
              ruleType = `forAllRightInfo(nvar);
              slist = applyForAllR(goal, `p, nvar);
            }

            // trying rules
            atom@relationAppl[] -> {
              Rule r = applicableInAuto(`atom, false);
              if (r != null) {
                slist = applyRule(r, goal,`atom, new HashMap<Term,Term>());
                ruleType = `customRuleInfo("custom rule");
              }
            }
          }
        } catch (Exception e) { System.out.println(e.getMessage());}
        if (slist != null) {
          Premisses prems = `premisses();
          %match(SeqList slist) {
            (_*,s,_*) -> {
              prems = `premisses(prems*,autoCommandRecursive(s));
            }
          }
          return `rule(ruleType, prems, goal, p);
        }
      } // match right hand side

      // left hand side
      sequent((_*,p,_*),_) -> {
        try {
          %match(Prop `p) {
            // Or L
            or[] -> {
              ruleType = `orLeftInfo();
              slist = applyOrL(goal, `p);
            }

            // And L
            and[] -> {
              ruleType = `andLeftInfo();
              slist = applyAndL(goal, `p);
            }

            // Implies L
            implies[] -> {
              ruleType = `impliesLeftInfo();
              slist = applyImpliesL(goal, `p);
            }

            // exists L
            exists[var=n] -> {
              Term nvar = Utils.freshVar(`n, goal);
              ruleType = `existsLeftInfo(nvar);
              slist = applyExistsL(goal, `p, nvar);
            }

            // trying rules
            atom@relationAppl[] -> {
              Rule r = applicableInAuto(`atom, true);
              if(r != null) {
                slist = applyRule(r, goal,`atom, new HashMap<Term,Term>());
                ruleType = `customRuleInfo("custom rule");
              }
            }

          }
        } catch (Exception e) {} 
        if (slist != null) {
          Premisses prems = `premisses();
          %match(SeqList slist) {
            (_*,s,_*) -> {
              prems = `premisses(prems*,autoCommandRecursive(s));
            }
          }
          return `rule(ruleType, prems, goal, p);
        }
      }
    }
    return `rule(openInfo(),premisses(),goal,top());
  }


  private Tree autoCommand(Tree tree, Position pos, 
      Prop active, boolean focus_left) throws Exception {

    Sequent goal = getSequentByPosition(tree, pos);
    Tree newtree = autoCommandRecursive(goal);
    return (Tree) MuTraveler.init(pos.getReplace(newtree)).visit(tree); 
  }

  /* ------------------------------------------------------------------ */

  private class ProofEnv implements Cloneable {
    public Tree tree = null;
    LinkedList<Position> openGoals = null;
    public boolean focus_left = false;  // true if focus on one hypothesis, false if on a conclusion
    public int focus = 1;
    public int currentGoal = 0;

    public Object clone() {
      ProofEnv res = new ProofEnv();
      res.tree = tree;
      res.openGoals = (LinkedList<Position>) openGoals.clone();
      res.focus_left = focus_left;
      res.focus = focus;
      res.currentGoal = currentGoal;
      return res;
    }
  }

  // builds the proof by manipulating a tree
  private Tree buildProofTree(Sequent goal) {

    // environnement stack to allow the "undo" command and incomplete proof save
    Stack<ProofEnv> envStack = new Stack<ProofEnv>();
    ProofEnv env = new ProofEnv();

    // initialisations
    env.focus_left = false;
    env.focus = 1;
    env.currentGoal = 0;
    env.openGoals = new LinkedList<Position>();
    env.tree = `rule(openInfo(), premisses(), goal, goal.getc().getHeadcontext());
    try { MuTraveler.init(`TopDown(getOpenPositions(env.openGoals))).visit(env.tree); }
    catch (VisitFailure e) { e.printStackTrace(); }

    // main loop
    while(env.openGoals.size() > 0) {
      Tree tree = null;

      // printing open goals
      System.out.println("Open goals : ");
      LinkedList<Position> og = env.openGoals;
      %match(LinkedList og) {
        (_*,p,_*) -> {
          Position pos = (Position) `p;
          System.out.println("\t"+PrettyPrinter.prettyPrint(getSequentByPosition(env.tree, pos)));
        }
      }

      // " + openGoals.size() + ", currentGoal = " + currentGoalgets current goal
      Position currentPos = env.openGoals.get(env.currentGoal);
      goal = getSequentByPosition(env.tree, currentPos);

      // pritty prints the current goal 
      ArrayList<Prop> hyp = getHypothesis(goal);
      ArrayList<Prop> concl = getConclusions(goal);
      Prop active = env.focus_left ?  hyp.get(env.focus-1) : concl.get(env.focus-1);  // for conveniance
      System.out.println("\n" + prettyGoal(hyp, concl, env.focus_left, env.focus) + "\n");

      System.out.print("proof> ");
      ProofCommand pcommand;
      try {pcommand = Utils.getProofCommand(); }
      catch (Exception e) { System.out.println("Unknown command : " + e); continue; }

      /* begin of the big switch */
      %match(ProofCommand pcommand) {

        proofCommand("undo") -> {
          if (envStack.size() > 0) {
            env = envStack.pop();
          }
        }

        proofCommand("next") -> {
          env.currentGoal = (env.currentGoal+1 ) % env.openGoals.size();
        }

        proofCommand("display") -> {
          try { PrettyPrinter.display(env.tree, newTermRules); }
          catch (Exception e) { System.out.println("display failed : " + e); }
        }


        focusCommand(arg) -> {
          int n;
          char hs = `arg.charAt(0);
          try { n = Integer.parseInt(`arg.substring(1)); }
          catch (NumberFormatException e) { continue; }

          if (hs == 'h' && n <= hyp.size()) {
            env.focus_left = true;
            env.focus = n;
          } 
          else if (hs == 'c' && n <= concl.size()) {
            env.focus_left = false;
            env.focus = n;
          }
        }

        /* ask for possible rules */
        askrulesCommand()-> {
          for(int i=0; i<newRules.size(); i++) {
            Rule rule = newRules.get(i);
            Prop conclusion = rule.getconcl();
            HashMap<String,Term> tds = Unification.match(conclusion, active);
            int rule_hs = rule.geths();

            // same side condition
            if (tds != null && ((rule_hs==0 && env.focus_left) || (rule_hs==1 && !env.focus_left))) 
            {
              System.out.println("\n- rule " + i + " :\n");
              System.out.println(PrettyPrinter.prettyRule(rule));
            }
          } // for
          System.out.println();
        }


        /* applying one of the custom rules */
        ruleCommand(n) -> {
          try {
            tree = ruleCommand(env.tree, currentPos, active, env.focus_left, `n);
          } catch (Exception e) {
            System.out.println("Can't apply custom rule "+ `n +": " + e.getMessage());
          }
        }

        /* elim case */
        proofCommand("elim") -> {
          try {
            tree = elimCommand(env.tree, currentPos, active, env.focus_left); 
          } catch (Exception e) {
            System.out.println("Can't apply elim: " + e.getMessage());
          }
        }

        /* duplicate case */
        proofCommand("duplicate") -> {
          try {
            tree = duplicateCommand(env.tree, currentPos, active, env.focus_left); 
          } catch (Exception e) {
            System.out.println("Can't apply duplicate: " + e.getMessage());
          }
        }

        /* remove case */
        proofCommand("remove") -> {
          try {
            tree = removeCommand(env.tree, currentPos, active, env.focus_left); 
          } catch (Exception e) {
            System.out.println("Can't apply duplicate: " + e.getMessage());
          }
        }

        /* auto case */
        proofCommand("auto") -> {
          try {
            tree = autoCommand(env.tree, currentPos, active, env.focus_left); 
          } catch (Exception e) {
            System.out.println("Can't apply auto: " + e.getMessage());
            e.printStackTrace();
          }
        }

        /* Axiom case */
        proofCommand("axiom") -> {
          try {
            tree = axiomCommand(env.tree, currentPos, active, env.focus_left);
          } catch (Exception e) {
            System.out.println("can't apply rule axiom : " + e.getMessage());
          }
        }

        /* Bottom case */
        proofCommand("bottom") -> {
          try {
            tree = bottomCommand(env.tree, currentPos, active, env.focus_left);
          } catch (Exception e) {
            System.out.println("can't apply bottom rule : " + e.getMessage());
          }
        }

        /* top case */
        proofCommand("top") -> {
          try {
            tree = topCommand(env.tree, currentPos, active, env.focus_left);
          } catch (Exception e) {
            System.out.println("can't apply top rule : " + e.getMessage());
          }
        }

        /* cut case */
        cutCommand(prop) -> {
          try {
            tree = cutCommand(env.tree, currentPos, `prop);
          } catch (Exception e) {
            System.out.println("can't apply cut rule : " + e.getMessage());
          }
        }

        /* experimental theorem case */
        theoremCommand(name) -> {
          try {
            tree = theoremCommand(env.tree, currentPos, `name);
            //System.out.println(tree);
          } catch(Exception e) {
            System.out.println("can't apply theorem " + `name + " : " + e.getMessage());
          }
        }

        /* experimental reduce case */
        proofCommand("reduce") -> {
          try {
            tree = reduceCommand(env.tree, currentPos, active, env.focus_left);
          } catch (Exception e) {
            System.out.println("can't apply cut rule : " + e.getMessage());
          }
        }


      } /* end of the big command switch */

      if(tree != null && tree != env.tree) {
        // get open positions
        envStack.push(env);
        env = (ProofEnv) env.clone();

        env.tree = tree;       
        env.openGoals.remove(currentPos);
        ((MuStrategy)currentPos.getOmega(`TopDown(getOpenPositions(env.openGoals)))).apply(env.tree);
        env.currentGoal = env.openGoals.size()-1;

        // reset focus
        env.focus_left = false;
        env.focus = 1;
      }


    } // while still open goals

    return env.tree;
  }


  private ArrayList<Rule> newRules = new ArrayList<Rule>();
  private TermRuleList newTermRules = `termrulelist();
  private HashMap<String,Tree> theorems = new HashMap<String,Tree>();

  public void mainLoop() throws Exception {
    Command command = null;

    while(true) {

      System.out.print("> ");
      try { command = Utils.getCommand(); }
      catch (Exception e) {
        System.out.println("Unknow command : " + e);
        continue;
      }

      %match(Command command) {
        rewritep(p1,p2) -> {
          RuleList rl = RuleCalc.transform(`p1,`p2);
          %match(RuleList rl) {
            (_*,r,_*) -> { newRules.add(`r);}
          }
          System.out.println("The new deduction rules are : \n");
          System.out.println(PrettyPrinter.prettyRule(rl));
        }

        rewritet(lhs,rhs) -> {
          newTermRules = `termrulelist(newTermRules*,termrule(lhs,rhs));
        }

        proof(name,p) -> {
          Tree tree = buildProofTree(`sequent(context(),context(p)));
          theorems.put(`name,tree);
          System.out.println(`name + " proved.");
        }

        proofcheck(name) -> {
          Tree tree = theorems.get(`name);
          if(ProofChecker.proofcheck(tree)) System.out.println("Proof check passed !");
          else System.out.println("Proof check failed :S");
        }

        display(name) -> {
          Tree tree = theorems.get(`name);
          PrettyPrinter.display(tree,newTermRules);
        }

        quit() -> {
          System.exit(0);
        }

        print(name) -> {
          Tree tree = theorems.get(`name);
          %match(Tree tree) {
            rule[c=concl] -> {
              String str = `name + " : " + PrettyPrinter.prettyPrint(`concl);
              System.out.println(str);
            }
          }
        }

      }  // %match(Command)
    } // while true
  }

  public final static void main(String[] args) throws Exception {
    ProofBuilder test = new ProofBuilder();
    //test.run();
    test.mainLoop();
  }
}
