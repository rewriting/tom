import sequents.*;
import sequents.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Position;
import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;

import java.io.*;
import antlr.*;
import antlr.collections.*;

public class ProofBuilder {

  %include { sequents/sequents.tom }
  %include { mutraveler.tom }
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


    // recuperage de la table des symboles
    HashMap<String,Term> tds = Unification.match(conclusion, active);
    if (tds == null) throw new Exception("active formula and rule conclusion don't match");


    // renommage des variables
    Set<Map.Entry<String,Term>> entries= tds.entrySet();
    for (Map.Entry<String,Term> ent: entries) {
      Term old_term = `Var(ent.getKey());
      Term new_term = ent.getValue();
      res = (SeqList) Utils.replaceFreeVars(res, old_term, new_term); 
    }


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
       ruledesc(0,_,_), sequent(ctxt@(u*,act,v*),c), act -> {
         Context gamma = args.size() <= 0 ? `context(u*,v*) : `ctxt;
         try { 
           VisitableVisitor v1 = `AddInContexts(gamma);
           res = (SeqList) MuTraveler.init(`TopDown(v1)).visit(res); 
           VisitableVisitor v2 = `PutInConclusion(c);
           res = (SeqList) MuTraveler.init(`TopDown(v2)).visit(res); 
         } catch (VisitFailure e) { e.printStackTrace(); }
         break b;
       }

       // si c'est une regle droite
       ruledesc(1,_,_), sequent(ctxt,c@(u*,act,v*)), act -> {
         try { 
           VisitableVisitor v1 = `AddInContexts(ctxt);
           res = (SeqList) MuTraveler.init(`TopDown(v1)).visit(res);
           Context delta = args.size() <= 0 ? `context(u*,v*) : `c;
           VisitableVisitor v2 = `PutInConclusion(delta);
           res = (SeqList) MuTraveler.init(`TopDown(v2)).visit(res); 
         } catch (VisitFailure e) {  e.printStackTrace();  }
         break b;
       }

       // probleme
       _,_,_ -> { throw new Exception("wrong hand side rule application"); }
     }
   }

   return res;
  }


  /**
   * classical rules
   **/


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

  public static SeqList applyAxiom(Sequent seq) throws Exception {
    %match(Sequent seq) {
      sequent((_*,x,_*),(_*,x,_*)) -> { return `concSeq(); }
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

  // forall
  // TODO add FV test
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
	  // FIXME : remove act and add contraction rule ??
          return `concSeq(sequent(context(X*,act,res,Y*),c));
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

  // TODO add FV test
  public static SeqList applyExistsL(Sequent seq, Prop active) throws Exception 
    {
      %match(Sequent seq, Prop active) {
        sequent((X*,act@exists(_,p),Y*),c), act -> {
          return `concSeq(sequent(context(X*,p,Y*),c));
        }
      }
      throw new Exception("can't apply rule exists L");
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
      rule[name="open"] -> {
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

  // builds the proof by manipulating a tree
  private Tree buildProofTree(Sequent goal) {

    // initialisations
    Tree tree = `rule("open", premisses(), goal, goal.getc().getHeadcontext()); 
    LinkedList<Position> openGoals = new LinkedList<Position>(); 
    try { MuTraveler.init(`TopDown(getOpenPositions(openGoals))).visit(tree); }
    catch (VisitFailure e) { e.printStackTrace(); }

    boolean focus_left = false; // true if focus on one hypothesis, false if on a conclusion
    int focus = 1;
    int currentGoal = 0;

    // main loop
    while(openGoals.size() > 0) {
      // printing open goals
      System.out.println("Open goals : ");
      %match(LinkedList openGoals) {
        (_*,p,_*) -> {
          Position pos = (Position) `p;
          System.out.println("\t"+PrettyPrinter.prettyPrint(getSequentByPosition(tree, pos)));
        }
      }

      // " + openGoals.size() + ", currentGoal = " + currentGoalgets current goal
      Position currentPos = openGoals.get(currentGoal);
      goal = getSequentByPosition(tree, currentPos);

      // pritty prints the current goal 
      ArrayList<Prop> hyp = getHypothesis(goal);
      ArrayList<Prop> concl = getConclusions(goal);
      Prop active = focus_left ?  hyp.get(focus-1) : concl.get(focus-1);  // for conveniance
      System.out.println("\n" + prettyGoal(hyp, concl, focus_left, focus) + "\n");

      System.out.print("proof> ");
      ProofCommand pcommand;
      try {pcommand = Utils.getProofCommand(); }
      catch (Exception e) { System.out.println("Unknown command : " + e); continue; }
      
      
      /* begin of the big switch */
      %match(ProofCommand pcommand) {
      proofCommand("next") -> {
        currentGoal = (currentGoal+1 ) % openGoals.size();
      }

      proofCommand("display") -> {
        try { PrettyPrinter.display(tree); }
        catch (Exception e) { System.out.println("display failed : " + e); }
      }


      focusCommand(arg) -> {
        int n;
        char hs = `arg.charAt(0);
        try { n = Integer.parseInt(`arg.substring(1)); }
        catch (NumberFormatException e) { continue; }
     
        if (hs == 'h' && n <= hyp.size()) {
          focus_left = true;
          focus = n;
        } 
        else if (hs == 'c' && n <= concl.size()) {
          focus_left = false;
          focus = n;
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
          if (tds != null && ((rule_hs==0 && focus_left) || (rule_hs==1 && !focus_left))) 
          {
            System.out.println("\n- rule " + i + " :\n");
            System.out.println(PrettyPrinter.prettyRule(rule));
          }
        } // for
        System.out.println();
      }


      /* applying one of the custom rules */
      ruleCommand(n) -> {
        HashMap<Term,Term> args = new HashMap<Term,Term>();

       if (`n < newRules.size()) {
          // TODO verify hand side
          try {
            Rule rule = newRules.get(`n);

            // asking for new vars
            Set<Term> new_vars = Utils.getNewVars(rule.getprem());
            for (Term t : new_vars) {
              String varname = t.getname();
              System.out.print("new term for variable " + varname + " in rule " + `n + " > ");
              Term new_var = Utils.getTerm();
              args.put(t, new_var);
            }

            SeqList slist = applyRule(rule, goal, active, args);

            // create open leafs
            Premisses prems = `premisses();
            %match(SeqList slist) {
              (_*,s,_*) -> { prems = `premisses(prems*, rule("open", premisses(), s, s.getc().getHeadcontext())) ;}
            }

            // get new tree
            Tree newrule = `rule("rule\\ " + n, prems, goal, active);
            tree = (Tree) MuTraveler.init(currentPos.getReplace(newrule)).visit(tree); 

            // get open positions
            openGoals.remove(currentPos);
            MuTraveler.init(currentPos.getOmega(`TopDown(getOpenPositions(openGoals)))).visit(tree);
            currentGoal = openGoals.size()-1;

          } catch (Exception e) {
            System.out.println("Can't apply custom rule "+ `n +": " + e.getMessage());
          }
       }
      }

      /* All classical rules */
      proofCommand("elim") -> {

        // list of the new premisses after applying a rule
        SeqList slist = null;  
        String ruleName = null;
        boolean applied = false;

        try {
          // breaking right rule
          if(!focus_left) {
            %match(Prop active) {

              // And R
              and[] -> {
                ruleName = "and R";
                slist = applyAndR(goal,active);
                applied = true;
              }

              // Or R
              or[] -> {
                ruleName = "or R";
                slist = applyOrR(goal,active);
                applied = true;
              }

              // Implies R
              implies[] -> {
                ruleName = "implies R";
                slist = applyImpliesR(goal, active);
                applied = true;
              }

              // forAll R
              forAll[var=n] -> {
                ruleName = "forAll R";
                // TODO interactif ?
                Term nvar = Utils.freshVar(`n, goal);
                slist = applyForAllR(goal, active, nvar);
                applied = true;
              }

              // exists R
              exists(x,_) -> {
                ruleName = "exists R";
                System.out.print("instance of " + `x + " > ");
                Term new_var = Utils.getTerm();
                slist = applyExistsR(goal, active, new_var);
                applied = true;
              }

            } // match(active)

          } else /* focus_left */ {
            %match(Prop active) {

              // Or L
              or[] -> {
                ruleName = "or L";
                slist = applyOrL(goal, active);
                applied = true;
              }

              // And L
              and[] -> {
                ruleName = "and L";
                slist = applyAndL(goal, active);
                applied = true;
              }

              // Implies L
              implies[] -> {
                ruleName = "implies L";
                slist = applyImpliesL(goal, active);
                applied = true;
              }

              // forAll L
              forAll(x,_) -> {
                ruleName = "forAll L";
                System.out.print("instance of " + `x + " > ");
                Term new_var = Utils.getTerm();
                slist = applyForAllL(goal, active, new_var);
                applied = true;
              }

              // exists L
              exists[] -> {
                ruleName = "exists L";
                slist = applyExistsL(goal, active);
                applied = true;
              }
            } // match(active)
          } // focus != 0

          // if one of the rules has been applied
          if (applied) {
            Premisses prems = `premisses();
            %match(SeqList slist) {
              (_*,s,_*) -> { prems = `premisses(prems*, rule("open", premisses(), s, s.getc().getHeadcontext())) ;}
            }

            // get new tree
            Tree newrule = `rule(ruleName, prems, goal, active);
            tree = (Tree) MuTraveler.init(currentPos.getReplace(newrule)).visit(tree); 

            // get open positions
            openGoals.remove(currentPos);
            MuTraveler.init(currentPos.getOmega(`TopDown(getOpenPositions(openGoals)))).visit(tree);
            currentGoal = openGoals.size()-1;

            // reset focus
            focus_left = false;
            focus = 1;
          }

        } catch (Exception e) {
          System.out.println("Can't apply elim " + ruleName + ": " + e);
        }
      }

      /* Axiom case */
      proofCommand("axiom") -> {
        try {
          SeqList slist = null;

          applyAxiom(goal); 

          Tree newrule = `rule("axiom", premisses(), goal, active);
          tree = (Tree) MuTraveler.init(currentPos.getReplace(newrule)).visit(tree);
          openGoals.remove(currentPos);
          currentGoal = 0;

          focus_left = false;
          focus = 1;


        } catch (Exception e) {
          System.out.println("can't apply rule axiom : " + e.getMessage());
        }
      }

      /* Bottom case */
      proofCommand("bottom") -> {
        try {
          SeqList slist = null;

          slist = applyBottom(goal); 

          Tree newrule = `rule("axiom", premisses(), goal, active);
          tree = (Tree) MuTraveler.init(currentPos.getReplace(newrule)).visit(tree);
          openGoals.remove(currentPos);
          currentGoal = 0;

          focus_left = false;
          focus = 1;


        } catch (Exception e) {
          System.out.println("can't apply rule bottom : " + e.getMessage());
        }
      }

      /* top case */
      proofCommand("top") -> {
        try {
          SeqList slist = null;

          slist = applyTop(goal); 

          Tree newrule = `rule("axiom", premisses(), goal, active);
          tree = (Tree) MuTraveler.init(currentPos.getReplace(newrule)).visit(tree);
          openGoals.remove(currentPos);
          currentGoal = 0;

          focus_left = false;
          focus = 1;


        } catch (Exception e) {
          System.out.println("can't apply rule bottom : " + e.getMessage());
        }
      }


      }     /* end of the big switch */
    } // while still open goals

    return tree;
  }


  private ArrayList<Rule> newRules = new ArrayList<Rule>();

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
       
        proof(s) -> {
          Tree tree = buildProofTree(`s);
          System.out.println("Proof complete.");
          PrettyPrinter.display(tree);
        }
      }
    }
  }

  public final static void main(String[] args) throws Exception {
    ProofBuilder test = new ProofBuilder();
    //test.run();
    test.mainLoop();
  }
}
