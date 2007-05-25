import sequents.*;
import sequents.types.*;

import urban.*;
import urban.types.*;

import tom.library.sl.*;

import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.Set;
import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Observable;
import java.util.Collection;

import java.io.*;
import antlr.*;
import antlr.collections.*;

public class ProofBuilder extends Observable {

  //%include { sequents/sequents.tom }
  %include { sequents/_sequents.tom }
  %include { urban/urban.tom }
  %include { sl.tom }
  %include { string.tom }
  %include { util/LinkedList.tom }

  // methode remplacant System.out.println
  private void writeToOutputln (String text) {
    writeToOutput(text+"\n");
  }
  
  // methode remplacant System.out.print
  private void writeToOutput (String text) {
    if (countObservers() != 0) {
      setChanged();
      notifyObservers(text);
    }
    else System.out.print(text);
  }

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

  %typeterm TermMap {
    implement { Map<Term,Term> }
    is_sort(t) { t instanceof Map }
  }

  %typeterm ProofBuilder {
    implement { ProofBuilder }
    is_sort(t) { t instanceof ProofBuilder }
  }


  %strategy ReplaceTree(t: Tree) extends Fail() {
    visit Tree {
      _ -> { return t; }
    }
  }

  %strategy ApplyRule(rule: Rule, active: Prop, args: TermMap) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        SeqList res = rule.getprem();
        Prop conclusion = rule.getconcl();
        Tree expanded = rule.gettree();

        // recuperage de la table des symboles
        HashMap<String,Term> tds = Unification.match(conclusion, active);
        if (tds == null)  throw new VisitFailure("active formula and rule conclusion don't match");

        //  -- building the original axiom with quantifiers --

        // building p => phi /\ phi -> p
        Prop conj = null;
        if (rule.geths() == 1) { // right rule
          %match (expanded) {
            rule[c=sequent((),(phi))] -> {
              conj = `and(implies(conclusion,phi),implies(phi,conclusion));
            }
          }
        } else {
          %match (expanded) {
            rule[c=sequent((phi),())] -> {
              conj = `and(implies(conclusion,phi),implies(phi,conclusion));
            }
          }
        }

        // adding quantifiers
        Prop newconcl = conj;
        Set<String> vars = tds.keySet();
        for (String var: vars) {
          newconcl = `forAll(var,newconcl);
        }

        // -- 

        // renommage des variables
        Set<Map.Entry<String,Term>> entries= tds.entrySet();
        for (Map.Entry<String,Term> ent: entries) {
          Term old_term = `Var(ent.getKey());
          Term new_term = ent.getValue();
          res = (SeqList) Utils.replaceFreeVars(res, old_term, new_term);
          // also replacing in the expanded tree
          expanded = (Tree) Utils.replaceFreeVars(expanded, old_term, new_term); 
        }

        // creation des variables fraiches (forall right et exists left)
        Set<Term> fresh = Utils.getSideConstraints(rule.getprem());
        for (Term fvar : fresh) {
          String bname = fvar.getbase_name();
          Term new_var = Utils.freshVar(bname, `seq);
          res = (SeqList) Utils.replaceTerm(res,fvar,new_var);
          // also replacing in the expanded tree
          expanded = (Tree) Utils.replaceFreeVars(expanded, fvar, new_var); 
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
          // also replacing in the expanded tree
          expanded = (Tree) Utils.replaceFreeVars(expanded, old_term, new_term); 
        }

/*

        // adding instanciation steps for the expanded form
        // if it is a right rule
        Position next = new Position();
        if (rule.geths() == 1) {
          %match (expanded) {
            rule[c=sequent((),(phi))] -> {
              // p. 22 paul master thesis
              Tree newt = createOpenLeaf(`sequent(context(newconcl),context()));
              // loop on forall left - instanciating the variables
              for(int i=0; i<tds.size(); i++) {
                Tree leaf = (Tree) next.getSubterm().apply(newt);
                %match(leaf) { 
                  rule[c=sequent(context(f@forAll(var,_)),())] -> {
                    Term newterm = tds.get(`var); 
                    newt = (Tree) ((MuStrategy) next.getOmega(`ApplyForAllL(f,newterm))).apply(newt);
                    next.down(2);
                    next.down(1);
                  }
                }
              }
              Prop tmp = `and(implies(active,phi),implies(phi,active));
              newt = (Tree) ((MuStrategy) next.getOmega(`ApplyAndL(tmp))).apply(newt);
              next.down(2); next.down(1);
              newt = (Tree) ((MuStrategy) next.getOmega(`ApplyImpliesL(implies(phi,active)))).apply(newt);
              next.down(2); next.down(1);

              // weakening until we obtain the concusion sequent of expanded
              boolean done = false;
              Position nextcopy = (Position) next.clone();
              while(!done) {
                b :{
                   Sequent currentseq = ((Tree) ((MuStrategy) nextcopy.getSubterm()).apply(newt)).getc();
                   Sequent wantedseq = expanded.getc();
                   %match(currentseq,wantedseq) {
                     x,x -> {
                       done = true ;
                       newt = (Tree) ((MuStrategy) nextcopy.getOmega(`ReplaceTree(expanded))).apply(newt);
                       break b;
                     }
                     sequent((_*,p,_*),_), sequent(!(_*,p,_*),_) -> {
                       newt = (Tree) ((MuStrategy) nextcopy.getOmega(`ApplyWeakL(p))).apply(newt);
                       nextcopy.down(2);
                       nextcopy.down(1);
                       break b;
                     }
                     sequent(_,(_*,p,_*)), sequent(_,!(_*,p,_*)) -> {
                       if (`p != conclusion) {
                         newt = (Tree) ((MuStrategy) nextcopy.getOmega(`ApplyWeakR(p))).apply(newt);
                         nextcopy.down(2);
                         nextcopy.down(1);
                         break b;
                       }
                     }
                   }
                 }
              }
              // going to axiom : cons(_,cons(here,_ ...  = pos(2,1)
              next.up(); next.down(2); next.down(1);
              expanded = newt;
            }
          }
        // if it is a left rule
        } else {
          %match (expanded) {
            rule[c=sequent((phi),())] -> {
              // p. 22 paul master thesis
              Tree newt = createOpenLeaf(`sequent(context(newconcl),context()));
              // loop on forall left - instanciating the variables
              for(int i=0; i<tds.size(); i++) {
                Tree leaf = (Tree) next.getSubterm().apply(newt);
                %match(leaf) { 
                  rule[c=sequent(context(f@forAll(var,_)),())] -> {
                    Term newterm = tds.get(`var); 
                    newt = (Tree) ((MuStrategy) next.getOmega(`ApplyForAllL(f,newterm))).apply(newt);
                    next.down(2);
                    next.down(1);
                  }
                }
              }
              Prop tmp = `and(implies(active,phi),implies(phi,active));
              newt = (Tree) ((MuStrategy) next.getOmega(`ApplyAndL(tmp))).apply(newt);
              next.down(2); next.down(1);
              newt = (Tree) ((MuStrategy) next.getOmega(`ApplyImpliesL(implies(active,phi)))).apply(newt);
              next.down(2);  
              // going to cons(_,cons(here,_ ...  = pos(2,1)
              next.down(2); next.down(1); 


              // weakening until we obtain the concusion sequent of expanded
              boolean done = false;
              Position nextcopy = (Position) next.clone();
              while(!done) {
                b :{
                   Sequent currentseq = ((Tree) ((MuStrategy) nextcopy.getSubterm()).apply(newt)).getc();
                   Sequent wantedseq = expanded.getc();
                   %match(currentseq,wantedseq) {
                     x,x -> {
                       done = true ;
                       newt = (Tree) ((MuStrategy) nextcopy.getOmega(`ReplaceTree(expanded))).apply(newt);
                       break b;
                     }
                     sequent((_*,p,_*),_), sequent(!(_*,p,_*),_) -> {
                       newt = (Tree) ((MuStrategy) nextcopy.getOmega(`ApplyWeakL(p))).apply(newt);
                       nextcopy.down(2);
                       nextcopy.down(1);
                       break b;
                     }
                     sequent(_,(_*,p,_*)), sequent(_,!(_*,p,_*)) -> {
                       if (`p != conclusion) {
                         newt = (Tree) ((MuStrategy) nextcopy.getOmega(`ApplyWeakR(p))).apply(newt);
                         nextcopy.down(2);
                         nextcopy.down(1);
                         break b;
                       }
                     }
                   }
                 }
              }

              // going to axiom 
              next.up(); next.up(); next.down(1);
              expanded = newt;
            }
          }
        }

    */

        // ajout des contextes dans les premisses
b: {
        %match (rule, seq, Prop active) {

          // si c'est une regle gauche
          ruledesc(0,_,_,_), sequent(ctxt@(u*,act,v*),c), act -> {
            // also in expanded tree
            /*
            expanded = (Tree) `TopDown(AddInContexts(ctxt)).fire(expanded); 
            expanded = (Tree) `TopDown(PutInConclusion(c)).fire(expanded); 
            */
            Context gamma = args.size() <= 0 ? `context(u*,v*) : `ctxt;
            try {
              res = (SeqList) `TopDown(AddInContexts(gamma)).visit(res); 
              res = (SeqList) `TopDown(PutInConclusion(c)).visit(res); 
            } catch(VisitFailure e) { e.printStackTrace(); throw new RuntimeException(); }
            break b;
          }

          // si c'est une regle droite
          ruledesc(1,_,_,_), sequent(ctxt,c@(u*,act,v*)), act -> {
            // also in expanded tree
            /*
            expanded = (Tree) `TopDown(AddInContexts(ctxt)).fire(expanded);
            expanded = (Tree) `TopDown(PutInConclusion(c)).fire(expanded); 
            */
            Context delta = args.size() <= 0 ? `context(u*,v*) : `c;
            try {
              res = (SeqList) `TopDown(AddInContexts(ctxt)).visit(res);
              res = (SeqList) `TopDown(PutInConclusion(delta)).visit(res); 
            } catch(VisitFailure e) { e.printStackTrace(); throw new RuntimeException(); }
            break b;
          }

          // probleme
          _,_,_ -> { throw new VisitFailure("wrong hand side rule application");  }
        }
   }

        // closing the expanding tree with the axiom
        //expanded = (Tree) ((MuStrategy) next.getOmega(`ApplyAxiom())).apply(expanded);

        // creating open leaves
        Premisses newprems = `premisses();
        %match(SeqList res) {
          (_*,x,_*) -> {
            newprems = `premisses(newprems*,createOpenLeaf(x));    
          }
        }

        //try { PrettyPrinter.display(expanded); } catch (Exception e) {}

        return `rule(customRuleInfo("customrule",expanded),newprems,seq,active);
      }
    }
  }

  /**
   * classical rules
   **/

  public static Tree createOpenLeaf(Sequent concl) {
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
            return `rule(orLeftInfo(),premisses(t1,t2),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyContractionL(active:Prop) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent((X*,act,Y*),g), act -> {
            Tree t1 = createOpenLeaf(`sequent(context(X*,act,act,Y*),g));
            return `rule(contractionLeftInfo(),premisses(t1),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyContractionR(active:Prop) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent(d,(X*,act,Y*)), act -> {
            Tree t1 = createOpenLeaf(`sequent(d,context(X*,act,act,Y*)));
            return `rule(contractionRightInfo(),premisses(t1),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyWeakL(active:Prop) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent((X*,act,Y*),g), act -> {
            Tree t1 = createOpenLeaf(`sequent(context(X*,Y*),g));
            return `rule(weakLeftInfo(),premisses(t1),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyWeakR(active:Prop) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent(d,(X*,act,Y*)), act -> {
            Tree t1 = createOpenLeaf(`sequent(d,context(X*,Y*)));
            return `rule(weakRightInfo(),premisses(t1),seq,act);
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

  %strategy ApplyForAllL(active:Prop, term:Term) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent((X*,act@forAll(n,p),Y*),g), act -> {
            Prop res = (Prop) Utils.replaceFreeVars(`p, `Var(n), term); 
            Tree t1 = createOpenLeaf(`sequent(context(X*,res,Y*),g));
            return `rule(forAllLeftInfo(term),premisses(t1),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyForAllLInteractive(active:Prop, o:ProofBuilder) extends Fail() {
    visit Tree {
      r@rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent((_*,act@forAll(n,_),_*),_), act -> {
            o.writeToOutput("instance of " + `n + " > ");
            Term term = null;
            try { term = Utils.getTerm(); } catch (Exception e) { throw new VisitFailure(); }
            return (Tree) `ApplyForAllL(active,term).visit(`r);
          }
        }
      }
    }
  }

  %strategy ApplyExistsR(active:Prop, term:Term) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent(d,(X*,act@exists(n,p),Y*)), act -> {
            Prop res = (Prop) Utils.replaceFreeVars(`p, `Var(n), term); 
            Tree t1 = createOpenLeaf(`sequent(d,context(X*,res,Y*)));
            return `rule(existsRightInfo(term),premisses(t1),seq,act);
          }
        }
      }
    }
  }

  %strategy ApplyExistsRInteractive(active:Prop, o:ProofBuilder) extends Fail() {
    visit Tree {
      r@rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent(_,(_*,act@exists(n,_),_*)), act -> {
            o.writeToOutput("instance of " + `n + " > ");
            Term term = null;
            try { term = Utils.getTerm(); } catch (Exception e) { throw new VisitFailure(); }
            return (Tree) `ApplyExistsR(active,term).visit(`r);
          }
        }
      }
    }
  }

  %strategy ApplyExistsL(active:Prop) extends Fail() {
    visit Tree {
      rule[c=seq] -> {
        %match(seq, Prop active) {
          sequent((X*,act@exists(n,p),Y*),g), act -> {
            Term nvar = Utils.freshVar(`n,`seq);
            Prop res = (Prop) Utils.replaceFreeVars(`p, `Var(n), nvar); 
            Tree t1 = createOpenLeaf(`sequent(context(X*,res,Y*),g));
            return `rule(existsLeftInfo(nvar),premisses(t1),seq,act);
          }
        }
      }
    }
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

  %strategy getOpenPosition(list:LinkedList) extends `Fail() {
    visit RuleType {
      // do not look into custom rules expanded trees
      rt -> { return `rt; }
    }
    visit Tree {
      r@rule[type=openInfo[]] -> {
        list.add(getEnvironment().getPosition());
        return `r;
      }
    }
  }

  private static void getOpenPositions(Tree tree, LinkedList pl) {
    Strategy s = `mu(MuVar("x"),Choice(getOpenPosition(pl),All(MuVar("x"))));
    try { s.visit(tree); }
    catch(VisitFailure e) { e.printStackTrace(); throw new RuntimeException(); }
  }

  private static void getOpenPositions(Tree tree, Position pos, LinkedList pl) {
    Strategy s = pos.getOmega(`mu(MuVar("x"),Choice(getOpenPosition(pl),All(MuVar("x")))));
    try { s.visit(tree); }
    catch(VisitFailure e) { e.printStackTrace(); throw new RuntimeException(); }
  }

  private Sequent getSequentByPosition(Tree tree, Position pos) {
    Tree res = null;
    try {res = (Tree) pos.getSubterm().visit(tree); }
    // FIME : why not exit ?
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
      if (n == -1) throw new Exception("No applicable rule.");
    }

    if (n < newRules.size()) {
      // TODO verify hand side
      Rule rule = newRules.get(n);

      // asking for new vars
      Set<Term> new_vars = Utils.getNewVars(rule.getprem());
      for (Term t : new_vars) {
        String varname = t.getname();
        writeToOutput("new term for variable " + varname + " in rule " + n + " > ");
        Term new_var = Utils.getTerm();
        args.put(t, new_var);
      }

      return (Tree) pos.getOmega(`ApplyRule(rule,active,args)).visit(tree);
    }

    else throw new Exception("rule " + n + " doesn't exist.");
  }

  // FIXME uses positions ... may be easily broken
  private Tree theoremCommand(Tree tree, Position pos, String name) throws Exception {

    Tree thtree = theorems.get(`name);
    Prop conclusion = null;

    %match(Tree thtree) {
      rule(_,_,sequent((),(prop)),_) -> {
        tree = (Tree) pos.getOmega(`ApplyCut(prop)).visit(tree);
        pos = pos.down(2);
        pos = pos.down(1);
        conclusion = `prop;
      }
    }

    while(true) {
b :{
      Sequent goal = getSequentByPosition(tree, pos);
      %match(Prop conclusion, goal) {
        // pattern means "only and at least one 'goal' in the sequent rhs"
        concl, sequent((),!(_*,concl,_*,!concl,_*)) -> {
          return (Tree) pos.getReplace(thtree).visit(tree);
        }
        _, sequent((p,_*),_) -> {
          tree = (Tree) pos.getOmega(`ApplyWeakL(p)).visit(tree);
          pos = pos.down(2);
          pos = pos.down(1);
          break b;
        }
        tokeep, sequent((),(_*,p@!tokeep,_*)) -> {
            tree = (Tree) pos.getOmega(`ApplyWeakR(p)).visit(tree);
            pos = pos.down(2);
            pos = pos.down(1);
            break b;
        }
      }
      throw new Exception("theorem not proved");
   }
    }
  }

  private Tree reduceCommand(Tree tree, Position pos, 
      Prop active, boolean focus_left) throws Exception {
    Sequent goal = getSequentByPosition(tree, pos);
    Sequent s = (Sequent) Unification.reduce(goal,newTermRules,newPropRules);
    Premisses prems = `premisses(rule(openInfo(), premisses(), s, s.getc().getHeadcontext()));

    // get new tree
    Tree newrule = `rule(reductionInfo(), prems, goal, active);
    try { return (Tree) pos.getReplace(newrule).visit(tree); }
    catch(VisitFailure e) { e.printStackTrace(); throw new RuntimeException(); }
  }


  static private WeakHashMap<Rule,Boolean> noNewVars = new WeakHashMap(); // to memoize

  private static Rule applicableInAuto(ArrayList<Rule> newRules, Prop prop, boolean left) {
    Rule res = null; 

    for(int i=0; i<newRules.size(); i++) {
      Rule rule = newRules.get(i); 
      boolean ok = false;
      if (noNewVars.containsKey(rule)) {
        ok = noNewVars.get(rule);
      } else { 
        ok = (Utils.getNewVars(rule).size() == 0);
        noNewVars.put(rule,ok);
      }
      if( ok ) {
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

  %typeterm RuleArrayList {
    implement { ArrayList<Rule> }
    is_sort(t) { t instanceof ArrayList }
  }

  %strategy ApplyReduce(newTermRules: TermRuleList, newPropRules: PropRuleList) extends `Identity() {
    visit Tree {
      rule[c=goal,active=a] -> {
        Sequent s = (Sequent) Unification.reduce(`goal,newTermRules,newPropRules);
        if(s.equals(`goal)) throw new VisitFailure();
        Premisses prems = `premisses(rule(openInfo(), premisses(), s, s.getc().getHeadcontext()));
        // get new tree
        return `rule(reductionInfo(), prems, goal, a);
      }
    }
  }

  %strategy ApplyAuto(newRules: RuleArrayList) extends `Fail() {
    visit Tree {
      // right hand side (+ axiom)
      t@rule[c=sequent(_,(_*,p,_*))] -> { 
        Strategy strat;
        Rule r = applicableInAuto(newRules, `p, false);
        HashMap<Term,Term> hm = new HashMap<Term,Term>();
        if(r != null) strat = `ApplyRule(r,p,hm); 
        else strat = `ChoiceV(ApplyAxiom(),ApplyTop(),ApplyAndR(p),ApplyOrR(p),ApplyImpliesR(p),ApplyForAllR(p));
        try {
          Tree res = (Tree) strat.visit(`t); 
          return res;
        } catch (VisitFailure e) {}
      }

      // left hand side
      t@rule[c=sequent((_*,p,_*),_)] -> {
        Strategy strat;
        Rule r = applicableInAuto(newRules, `p, true);
        HashMap<Term,Term> hm = new HashMap<Term,Term>();
        if(r != null) strat = `ApplyRule(r,p,hm); 
        else strat = `ChoiceV(ApplyBottom(),ApplyAndL(p),ApplyOrL(p),ApplyImpliesL(p),ApplyExistsL(p));
        try{
          Tree res = (Tree) strat.visit(`t); 
          return res;
        } catch (VisitFailure e) {}
      }
    }
  }
	
  // TODO: seems ok, check correctness again
  %op Strategy SafeTopDown(s:Strategy) {
    make(s) { 
      `mu(MuVar("y"),Try(Sequence(s,_rule(Identity(),_premisses(MuVar("y")),Identity(),Identity()))))
    }
    //make(s) { `mu(MuVar("x"), Choice(Is_customRuleInfo(),Sequence(s,All(MuVar("x")))))  }
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
  private Stack buildProofTree(Sequent goal) throws ReInitException {

    // environnement stack to allow the "undo" command and incomplete proof save
    Stack<ProofEnv> envStack = new Stack<ProofEnv>();
    ProofEnv env = new ProofEnv();

    // initialisations
    env.focus_left = false;
    env.focus = 1;
    env.currentGoal = 0;
    env.openGoals = new LinkedList<Position>();
    env.tree = `rule(openInfo(), premisses(), goal, goal.getc().getHeadcontext());
    getOpenPositions(env.tree, env.openGoals);
    envStack.push(env);
    return buildProofTreeFromStack(envStack);

  }

  private Stack<ProofEnv> buildProofTreeFromStack(Stack<ProofEnv> envStack)  throws ReInitException {

	  ProofEnv env = envStack.pop(); 
	  Sequent goal = null;
	    
    // main loop
    while(env.openGoals.size() > 0) {
      Tree tree = null;

      // printing open goals
      writeToOutputln("Open goals : ");
      LinkedList<Position> og = env.openGoals;
      %match(LinkedList og) {
        (_*,p,_*) -> {
          Position pos = (Position) `p;
          writeToOutputln("\t"+PrettyPrinter.prettyPrint(getSequentByPosition(env.tree, pos)));
        }
      }

      // " + openGoals.size() + ", currentGoal = " + currentGoalgets current goal
      Position currentPos = env.openGoals.get(env.currentGoal);
      goal = getSequentByPosition(env.tree, currentPos);

      // pritty prints the current goal 
      ArrayList<Prop> hyp = getHypothesis(goal);
      ArrayList<Prop> concl = getConclusions(goal);
      Prop active = env.focus_left ?  hyp.get(env.focus-1) : concl.get(env.focus-1);  // for conveniance
      writeToOutputln("\n" + prettyGoal(hyp, concl, env.focus_left, env.focus) + "\n");

      writeToOutput("proof> ");
      ProofCommand pcommand;
      try {pcommand = Utils.getProofCommand(); }
      catch (Exception e) { writeToOutputln("Unknown command : " + e); continue; }

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
          try { PrettyPrinter.display(env.tree, newTermRules, newPropRules); }
          catch (Exception e) { writeToOutputln("display failed : " + e); }
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
              writeToOutputln("\n- rule " + i + " :\n");
              writeToOutputln(PrettyPrinter.prettyRule(rule));
            }
          } // for
          writeToOutputln("");
        }


        /* applying one of the custom rules */
        ruleCommand(n) -> {
          try {
            tree = ruleCommand(env.tree, currentPos, active, env.focus_left, `n);
          } catch (Exception e) {
            writeToOutputln("Can't apply custom rule "+ `n + ": " + e.getMessage());
            e.printStackTrace();
          }
        }

        /* intro case */
        proofCommand("intro") -> {
          try {
            Strategy strat;

            if (env.focus_left)
              strat = `ChoiceV(ApplyImpliesL(active), ApplyAndL(active), ApplyOrL(active), ApplyForAllLInteractive(active, this), ApplyExistsL(active), ApplyBottom());
            else
              strat = `ChoiceV(ApplyImpliesR(active), ApplyAndR(active), ApplyOrR(active), ApplyForAllR(active), ApplyExistsRInteractive(active, this), ApplyTop());

            tree = (Tree) currentPos.getOmega(strat).visit(env.tree);
          } catch (VisitFailure e) {
            writeToOutputln("Can't apply intro" + e.getMessage());
          }
        }

        /* duplicate case */
        proofCommand("duplicate") -> {
          try {
            Strategy strat;
            if (env.focus_left) strat = `ApplyContractionL(active);
            else strat = `ApplyContractionR(active);
            tree = (Tree) currentPos.getOmega(strat).visit(env.tree);
          } catch (VisitFailure e) {
            writeToOutputln("Can't apply duplicate" + e.getMessage());
          }
        }

        /* remove case */
        proofCommand("remove") -> {
          try {
            Strategy strat;
            if (env.focus_left) strat = `ApplyWeakL(active);
            else strat = `ApplyWeakR(active);
            tree = (Tree) currentPos.getOmega(strat).visit(env.tree);
          } catch (VisitFailure e) {
            writeToOutputln("Can't apply duplicate" + e.getMessage());
          }
        }

        /* intros case */
        proofCommand("intros") -> {
          try {
            ArrayList<Rule> emptylist = new ArrayList<Rule>();
            Strategy strat = `SafeTopDown(Try(ApplyAuto(emptylist)));
            tree = (Tree) currentPos.getOmega(strat).visit(env.tree);
          } catch (VisitFailure e) {
            writeToOutputln("Can't apply intros" + e.getMessage());
            e.printStackTrace();
          }
        }

        /* auto case */
        proofCommand("auto") -> {
          try {
            Strategy strat = `SafeTopDown(Try(ApplyAuto(newRules)));
            tree = (Tree) currentPos.getOmega(strat).visit(env.tree);
          } catch (VisitFailure e) {
            writeToOutputln("Can't apply auto : " + e.getMessage());
            e.printStackTrace();
          }
        }

        /* experimental autoreduce case */
        proofCommand("autoreduce") -> {
          try {
            Strategy strat = 
              `SafeTopDown(Try(Choice(ApplyAuto(newRules),ApplyReduce(newTermRules,newPropRules))));
            tree = (Tree) currentPos.getOmega(strat).visit(env.tree);
          } catch (VisitFailure e) {
            writeToOutputln("Can't apply autoreduce : " + e + ", " + e.getMessage());
            e.printStackTrace();
          }
        }

        /* Axiom case */
        proofCommand("axiom") -> {
          try {
            Strategy strat = `ApplyAxiom(); 
            tree = (Tree) currentPos.getOmega(strat).visit(env.tree);
          } catch (VisitFailure e) {
            writeToOutputln("can't apply rule axiom" + e.getMessage());
          }
        }

        /* cut case */
        cutCommand(prop) -> {
          try {
            Strategy strat = `ApplyCut(prop); 
            tree = (Tree) ((Strategy) currentPos.getOmega(strat)).visit(env.tree);
          } catch (VisitFailure e) {
            writeToOutputln("can't apply cut rule : " + e.getMessage());
          }
        }

        /* experimental theorem case */
        theoremCommand(name) -> {
          try {
            tree = theoremCommand(env.tree, currentPos, `name);
            //System.out.println(tree);
          } catch(Exception e) {
            writeToOutputln("can't apply theorem " + `name + " : " + e.getMessage());
          }
        }

        /* experimental reduce case */
        normalizeSequent() -> {
          try {
            Strategy strat = `Try(ApplyReduce(newTermRules, newPropRules));
            tree = (Tree) currentPos.getOmega(strat).visit(env.tree);
            //old: tree = reduceCommand(env.tree, currentPos, active, env.focus_left);
          } catch (VisitFailure e) {
            writeToOutputln("can't apply cut rule : " + e.getMessage());
          }
        }

        /* quit */
        proofquit() -> {
          System.exit(0);
        }

        // reinit
        proofreinit() -> {
          throw new ReInitException();
        }

        /* abort */
        abort() -> {
          envStack.push(env);
          return envStack; 
        }

        /* proof end of file */
        proofendoffile() -> {
          writeToOutputln("Warning : The file ended while the theorem was not proved !\nProve it manually or abort.");
          Utils.setStream(new DataInputStream(System.in));
          inputStreams.clear();
        }

      } /* end of the big command switch */

      if(tree != null && tree != env.tree) {
        // get open positions
        envStack.push(env);
        env = (ProofEnv) env.clone();

        env.tree = tree;       
        env.openGoals.remove(currentPos);
        getOpenPositions(env.tree, currentPos, env.openGoals);
        env.currentGoal = env.openGoals.size()-1;

        // reset focus
        env.focus_left = false;
        env.focus = 1;
      }


    } // while still open goals

    envStack.push(env);
    return envStack;
  }

  class ReInitException extends Exception {
  }

  public void reInit() {
    newRules = new ArrayList<Rule>();
    newTermRules = `termrulelist();
    newPropRules = `proprulelist();
    theorems = new HashMap<String,Tree>();
    unprovedTheorems = new HashMap<String,Stack<ProofEnv>>();
    inputStreams = new Stack<InputStream>();
    pttheorems = new HashMap<String, ProofTerm>();
    writeToOutput("SESSIONRESTARTED"); // mot cle pour dire qu'on a redemarre la session
  }
 
  private ArrayList<Rule> newRules = new ArrayList<Rule>();
  private TermRuleList newTermRules = `termrulelist();
  private PropRuleList newPropRules = `proprulelist();
  private HashMap<String,Tree> theorems = new HashMap<String,Tree>();
  private HashMap<String,Stack<ProofEnv>> unprovedTheorems = new HashMap<String,Stack<ProofEnv>>();
  private Stack<InputStream> inputStreams = new Stack<InputStream>();
  private HashMap<String,ProofTerm> pttheorems = new HashMap<String, ProofTerm>();

  // called when leaving proof mode
  private void store(Stack<ProofEnv> envStack, String name) {
	  if(envStack.peek().openGoals.size() == 0) {
      Tree tree = envStack.peek().tree;
      theorems.put(name,tree);
      unprovedTheorems.remove(name);
      writeToOutputln(name + " proved.");
      //ted.VisitableViewer.toTreeStdout(tree);
    } else {
      unprovedTheorems.put(name, envStack);
      writeToOutputln(name + " remains unproved !!");
    }
  }

  public RuleList transform(Prop atom, Prop p) {
    RuleCalc rc = new RuleCalc(atom,p);
    Prop permut_problem = rc.getProblem();
    int i = 1;
    while((permut_problem = rc.getProblem()) != null) {
      try {
        writeToOutputln("name the proposition \"" + 
            PrettyPrinter.prettyPrint(permut_problem) + "\" > ");
        String name = Utils.getIdent();
        // ask user for a name
        rc.run(name);
      } catch (Exception e) {
        writeToOutputln("not a valid id : " + e.toString());
      }
    }
    return rc.getResult();
  }
  
  public void mainLoop() throws Exception {
    Command command = null;
      
    while(true) {

      writeToOutput("> ");
      try { command = Utils.getCommand(); }
      catch (Exception e) {
        writeToOutputln("Unknow command : " + e);
        continue;
      }

      %match(Command command) {

        /* declarations */

        rewritesuper(p1,p2) -> {
          RuleList rl = transform(`p1,`p2);
          %match(RuleList rl) {
            (_*,r,_*) -> { 
              `r = (Rule) Unification.substPreTreatment(`r);
              newRules.add(`r);
            }
          }
          writeToOutputln("The new deduction rules are : \n");
          writeToOutputln(PrettyPrinter.prettyRule(rl));
        }

        rewriteterm(lhs,rhs) -> {
          `lhs = (Term) Unification.substPreTreatment(`lhs);
          `rhs = (Term) Unification.substPreTreatment(`rhs);
          newTermRules = `termrulelist(newTermRules*,termrule(lhs,rhs));
        }

        rewriteprop(lhs,rhs) -> {
          `lhs = (Prop) Unification.substPreTreatment(`lhs);
          `rhs = (Prop) Unification.substPreTreatment(`rhs);
          newPropRules = `proprulelist(newPropRules*,proprule(lhs,rhs));
        }

        /* reduce command */

        normalizeProp(p) -> {
          Prop res = (Prop) Unification.reduce(`p,newTermRules,newPropRules);
          writeToOutputln(PrettyPrinter.prettyPrint(res));
        }

        normalizeTerm(t) -> {
           Term res = (Term) Unification.reduce(`t,newTermRules,newPropRules);
           writeToOutputln(PrettyPrinter.prettyPrint(res));
        }

        /* proof handling */

        proof(name,p) -> {
          try {
            Stack<ProofEnv> envStack = buildProofTree(`sequent(context(),context(p)));
            store(envStack, `name);
          }
          catch (ReInitException e) { reInit(); }
        }

        resume(name) -> {
          Stack<ProofEnv> envStack = unprovedTheorems.get(`name);
          if(envStack==null) { writeToOutputln(`name + " not found"); } else {
            try {
              envStack = buildProofTreeFromStack(envStack);
              store(envStack, `name);
            }
            catch (ReInitException e) {reInit(); }
          }
        }

        proofcheck(name) -> {
          Tree tree = theorems.get(`name);
          if(tree==null) writeToOutputln(`name + " not found"); else {
            tree = ProofExpander.expand(tree);
            if(ProofChecker.proofcheck(tree)) writeToOutputln("Proof check passed !");
            else writeToOutputln("Proof check failed :S");
          }
        }

        /* pretty print */

        display(name) -> {
          Tree tree = theorems.get(`name);
          if(tree==null) writeToOutputln(`name + " not found");
          else PrettyPrinter.display(tree,newTermRules,newPropRules);
          //tree = ProofExpander.expand(tree);
          //PrettyPrinter.display(tree,newTermRules,newPropRules);
        }

        proofterm(name) -> {
          /*
          ProofTerm pi = pttheorems.get(`name);
          if (pi==null) {
            Tree tree = theorems.get(`name);
            if(tree==null) writeToOutputln(`name + " not found");
            else {
              pi = Proofterms.getProofterm(tree);
              pttheorems.put(`name,pi);
            }
          }
          if (pi!=null) {
            writeToOutputln(PrettyPrinter.prettyPrint(pi));
            //writeToOutput("IMG"+PrettyPrinter.show(pi));
            PrettyPrinter.display(pi);
          }*/
          Tree tree = theorems.get(`name);
          if(tree==null) writeToOutputln(`name + " not found");
          else {
            //PrettyPrinter.display(Proofterms.getProofterm(tree));
            PrettyPrinter.display(Proofterms.typeProof(tree));
          }
          Collection c = Proofterms.reduce(Proofterms.getProofterm(tree));
          writeToOutputln("Number of one-step reducts found : "+c.size());
          for (Object o:c) {
            writeToOutputln(PrettyPrinter.prettyPrint((urbanAbstractType) o));
          }
        }

        gibber() -> {
          writeToOutputln("        .=\"=.\n      _/.-.-.\\_\n     ( ( 0 0 ) )\n      |/  \"  \\|\n       \\'---'/\n        `\"\"\"`");
          writeToOutputln("Have you gibbered today ?");
        }

        print(name) -> {
          Tree tree = theorems.get(`name);
          if(tree==null) writeToOutputln(`name + " not found"); 
          else
            %match(Tree tree) {
              rule[c=concl] -> {
                String str = `name + " : " + PrettyPrinter.prettyPrint(`concl);
                writeToOutputln(str);
              }
            }
        }

        /* file/stream management */

        importfile(name) -> {
          String newname = `name.substring(1, `name.length()-1);
          try { 
            InputStream stream = new FileInputStream(newname);
            Utils.setStream(stream);
            inputStreams.push(stream);
          }
          catch (Exception FileNotFoundException) {
            writeToOutputln(newname + " : File not found");
          }
        }

        endoffile() -> {
          writeToOutputln("End of file");
          InputStream stream = new DataInputStream(System.in);
          if (! inputStreams.empty()) stream = inputStreams.pop();
          Utils.setStream(stream);
        }
  
        quit() -> {
          System.exit(0);
        }

        reinit() -> {
          reInit();
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
