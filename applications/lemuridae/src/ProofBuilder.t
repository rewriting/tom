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

  %strategy AddInContexts(ctx:Context) extends `Identity() {
    visit Sequent {
      sequent((X*),c) -> { return `sequent(context(X*,ctx*),c);} 
    }
  }

  %strategy PutInConclusion(p:Prop) extends `Identity() {
    visit Sequent {
      sequent(ctxt,_) -> { return `sequent(ctxt,p);} 
    }
  }

  public static SeqList applyRule(Rule rule, Sequent seq, Prop active) throws Exception {

    SeqList res = rule.getprem();
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


    // creation des variables fraiches
    Set<Term> fresh = Utils.getSideConstraints(rule.getprem());
    for (Term fvar : fresh) {
      String bname = fvar.getbase_name();
      Term new_var = Utils.freshVar(bname, seq);
      res = (SeqList) Utils.replaceTerm(res,fvar,new_var);
    }
   

    // ajout des contextes dans les premisses
b: {
     %match (Rule rule, Sequent seq, Prop active) {

       // si c'est une regle gauche
       ruledesc(0,_,_), sequent(context(h*,act,q*),c), act -> {
         Context gamma = `context(h*,q*);
         try { 
           AddInContexts v1 = new AddInContexts(gamma);
           res = (SeqList) MuTraveler.init(`TopDown(v1)).visit(res); 
           PutInConclusion v2 = new PutInConclusion(`c);
           res = (SeqList) MuTraveler.init(`TopDown(v2)).visit(res); 
         } catch (VisitFailure e) { e.printStackTrace(); }
         break b;
       }

       // si c'est une regle droite
       ruledesc(1,_,_), sequent(ctxt,act), act -> {
         try { 
           AddInContexts v = new AddInContexts(`ctxt);
           res = (SeqList) MuTraveler.init(`TopDown(v)).visit(res);
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
  
  public static SeqList applyAndR(Sequent seq) throws Exception {
    %match(Sequent seq) {
      sequent(ctxt,and(p1,p2)) -> {
        return `concSeq(sequent(ctxt,p1),sequent(ctxt,p2));
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
  
  public static SeqList applyOrR1(Sequent seq) throws Exception {
    %match(Sequent seq) {
      sequent(ctxt,or(p1,_)) -> {
        return `concSeq(sequent(ctxt,p1));
      }
    }
    throw new Exception("can't apply rule Or R 1");
  }

  public static SeqList applyOrR2(Sequent seq) throws Exception {
    %match(Sequent seq) {
      sequent(ctxt,or(_,p2)) -> {
        return `concSeq(sequent(ctxt,p2));
      }
    }
    throw new Exception("can't apply rule Or R 2");
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

  public static SeqList applyImpliesR(Sequent seq) throws Exception {
    %match(Sequent seq) {
      sequent(ctxt,implies(p1,p2)) -> {
        return `concSeq(sequent(context(ctxt*,p1),p2));
      }
    }
    throw new Exception("can't apply rule => R");
  }
  
  public static SeqList applyImpliesL(Sequent seq, Prop active) throws Exception {
    %match(Sequent seq, Prop active) {
      sequent((X*,act@implies(p1,p2),Y*),c), act -> {
        return `concSeq(sequent(context(X*,Y*),p1), sequent(context(X*,p2,Y*),c));
      }
    }
    throw new Exception("can't apply rule => L");
  }

  // Axiom

  public static SeqList applyAxiom(Sequent seq) throws Exception {
    %match(Sequent seq) {
      sequent((_*,x,_*),x) -> { return `concSeq(); }
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

  // forall
  // TODO add FV test
  public static SeqList applyForAllR(Sequent seq, Term new_var) throws Exception {
    %match(Sequent seq) {
      sequent(ctxt,forAll(n,p)) -> {
        Prop res = (Prop) Utils.replaceFreeVars(`p, `Var(n), new_var); 
        return `concSeq(sequent(ctxt,res));
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

  public static SeqList applyExistsR(Sequent seq, Term new_var) throws Exception {
    %match(Sequent seq) {
      sequent(ctxt,exists(n,p)) -> {
        Prop res = (Prop) Utils.replaceFreeVars(`p, `Var(n), new_var); 
        return `concSeq(sequent(ctxt,res));
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
 
  SeqLexer lexer = new SeqLexer(new DataInputStream(System.in));
  SeqParser parser = new SeqParser(lexer);
  SeqTreeParser walker = new SeqTreeParser();
 
  private String prettyGoal(ArrayList hyp, Prop concl, int focus) {
    String res = "";
    for (int i=0; i < hyp.size(); i++) {
      res +=  (focus == i+1) ? "*" : " ";
      res += i+1 + ":  ";
      res += PrettyPrinter.prettyPrint((Prop) hyp.get(i)) + "\n";
    }
    res += "-----------\n";
    res += (focus == 0) ? "*0:  " : " 0:  ";
    res += PrettyPrinter.prettyPrint(concl);
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
    Tree tree = `rule("open", premisses(), goal, goal.getc()); 
    LinkedList<Position> openGoals = new LinkedList<Position>(); 
    try { MuTraveler.init(`TopDown(getOpenPositions(openGoals))).visit(tree); }
    catch (VisitFailure e) { e.printStackTrace(); }

    int focus = 0;
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
      Prop concl = goal.getc();
      System.out.println("\n" + prettyGoal(hyp, concl, focus) + "\n");

      System.out.print("proof> ");
      String command = Utils.getInput();

      /* begin of the big switch */
      if (command.equals("next")) {
        currentGoal = (currentGoal+1 ) % openGoals.size();
      }

      if (command.equals("display")) {
        try { PrettyPrinter.display(tree); }
        catch (Exception e) { System.out.println("display failed : " + e); }
      }


      else if (command.startsWith("focus")) {
        int n;
        try { n = Integer.parseInt(command.substring(5).trim()); }
        catch (NumberFormatException e) { continue; }
        if (n <= hyp.size()) 
          focus = n;
      }

      /* ask for possible rules */
      else if (command.equals("rules?")) {
        Prop active = (focus == 0) ? concl : (Prop) hyp.get(focus-1);
        for(int i=0; i<newRules.size(); i++) {
          Rule rule = newRules.get(i);
          Prop conclusion = rule.getconcl();
          HashMap<String,Term> tds = Unification.match(conclusion, active);
          int rule_hs = rule.geths();

          // same side condition
          if (tds != null && ((rule_hs==0 && focus >0) || (rule_hs==1 && focus==0))) 
          {
            System.out.println("\n- rule " + i + " :\n");
            System.out.println(PrettyPrinter.prettyRule(rule));
          }
        } // for
        System.out.println();
      }


      /* applying one of the custom rules */
      else if (command.startsWith("rule")) {
        int n;
        try { n = Integer.parseInt(command.substring(4).trim()); }
        catch (NumberFormatException e) { continue; }
        if (n < newRules.size()) {
          // TODO verify hand side
          try {
            Rule rule = newRules.get(n);
            Prop active = (focus == 0) ? concl : (Prop) hyp.get(focus-1);
            SeqList slist = applyRule(rule, goal, active);


            // create open leafs
            Premisses prems = `premisses();
            %match(SeqList slist) {
              (_*,s,_*) -> { prems = `premisses(prems*, rule("open", premisses(), s, s.getc())) ;}
            }

            // get new tree
            Tree newrule = `rule("rule\\ " + n, prems, goal, active);
            tree = (Tree) MuTraveler.init(currentPos.getReplace(newrule)).visit(tree); 

            // get open positions
            openGoals.remove(currentPos);
            MuTraveler.init(currentPos.getOmega(`TopDown(getOpenPositions(openGoals)))).visit(tree);
            currentGoal = openGoals.size()-1;

          } catch (Exception e) {
            System.out.println("Can't apply custom rule "+n+": " + e.getMessage());
          }
        }
      }




      /* All classical rules */
      else if (command.startsWith("elim")) {

        // list of the new premisses after applying a rule
        SeqList slist = null;  

        Prop active = null;
        String ruleName = null;
        boolean applied = false;

        try {
          // breaking right rule
          if(focus == 0) {
            active = concl;
            %match(Prop active) {

              // And R
              and[] -> {
                ruleName = "and R";
                slist = applyAndR(goal);
                applied = true;
              }
              // Or R
              or[] -> {
                ruleName = "or R";
                String hs = command.substring(4).trim();
                %match(String hs) {
                  "right" -> {
                    slist = applyOrR2(goal);
                    applied = true;
                  }

                  "left" -> {
                    slist = applyOrR1(goal);
                    applied = true;
                  }
                }
              }

              // Implies R
              implies[] -> {
                ruleName = "implies R";
                slist = applyImpliesR(goal);
                applied = true;
              }

              // forAll R
              forAll[var=n] -> {
                ruleName = "forAll R";
                // TODO interactif ?
                Term nvar = Utils.freshVar(`n, goal);
                slist = applyForAllR(goal, nvar);
                applied = true;
              }

              // exists R
              exists(x,_) -> {
                ruleName = "exists R";
                System.out.print("instance of " + `x + " > ");
                Term new_var = Utils.getTerm();
                slist = applyExistsR(goal, new_var);
                applied = true;
              }

            } // match(active)

          } else /* focus != 0, left case*/ {
            active = (Prop) hyp.get(focus-1);
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

          // if one of the right rules has been applied
          if (applied) {
            Premisses prems = `premisses();
            %match(SeqList slist) {
              (_*,s,_*) -> { prems = `premisses(prems*, rule("open", premisses(), s, s.getc())) ;}
            }

            // get new tree
            Tree newrule = `rule(ruleName, prems, goal, active);
            tree = (Tree) MuTraveler.init(currentPos.getReplace(newrule)).visit(tree); 

            // get open positions
            openGoals.remove(currentPos);
            MuTraveler.init(currentPos.getOmega(`TopDown(getOpenPositions(openGoals)))).visit(tree);
            currentGoal = openGoals.size()-1;
          }

        } catch (Exception e) {
          System.out.println("Can't apply elim " + ruleName + ": " + e);
        }
      }

      /* Axiom case */
      else if (command.equals("axiom")) {
        try {
          SeqList slist = null;
          Prop active = (focus == 0) ? concl : (Prop) hyp.get(focus-1);

          applyAxiom(goal); 

          Tree newrule = `rule("axiom", premisses(), goal, active);
          tree = (Tree) MuTraveler.init(currentPos.getReplace(newrule)).visit(tree);
          openGoals.remove(currentPos);
          currentGoal = 0;

        } catch (Exception e) {
          System.out.println("can't apply rule axiom : " + e.getMessage());
        }
      }

      /* Bottom case */
      else if (command.equals("bottom")) {
        try {
          SeqList slist = null;
          Prop active = (focus == 0) ? concl : (Prop) hyp.get(focus-1);

          slist = applyBottom(goal); 

          Tree newrule = `rule("axiom", premisses(), goal, active);
          tree = (Tree) MuTraveler.init(currentPos.getReplace(newrule)).visit(tree);
          openGoals.remove(currentPos);
          currentGoal = 0;

        } catch (Exception e) {
          System.out.println("can't apply rule axiom : " + e.getMessage());
        }
      }

      /* end of the big switch */
    } // while still open goals

    return tree;
  }

  // builds recursively the proof
  private Tree buildProof(Sequent goal) {
    String command = null;
    int focus = 0;
    ArrayList hyp = getHypothesis(goal);
    Prop concl = goal.getc();

    System.out.println("\n" + prettyGoal(hyp, concl, focus) + "\n");
    System.out.print("> ");

    while(true) {
      command = Utils.getInput();

      if (command.startsWith("focus")) {
        int n;
        try { n = Integer.parseInt(command.substring(5).trim()); }
        catch (NumberFormatException e) { continue; }
        if (n <= hyp.size()) 
          focus = n;
      }

      // TODO separer
      else if (command.startsWith("elim")) {
        String ruleName = null;
        SeqList prems = null;
        Prop active = null;
        boolean ok = false;

        try {
          // cassage a droite
          if(focus == 0) {
            active = concl;
            %match(Prop active) {

              // And R
              and[] -> {
                ruleName = "and R";
                prems = applyAndR(goal);
                ok = true;
              }

              // Or R
              or[] -> {
                ruleName = "or R";
                String hs = command.substring(4).trim();
                %match(String hs) {
                  "right" -> {
                    prems = applyOrR2(goal);
                    ok = true;
                  }

                  "left" -> {
                    prems = applyOrR1(goal);
                    ok = true;
                  }
                }
              }

              // Implies R
              implies[] -> {
                ruleName = "implies R";
                prems = applyImpliesR(goal);
                ok = true;
              }

              // forAll R
              forAll[var=n] -> {
                ruleName = "forAll R";
                // TODO interactif ?
                Term nvar = Utils.freshVar(`n, goal);
                prems = applyForAllR(goal, nvar);
                ok = true;
              }

              // exists R
              exists(x,_) -> {
                ruleName = "exists R";
                System.out.print("instance of " + `x + " > ");
                Term new_var = Utils.getTerm();
                prems = applyExistsR(goal, new_var);
                ok = true;
              }

              // not a complex formula
              _ -> { if(!ok) continue; }

            } // match

          } else /* focus != 0 */ {
            active = (Prop) hyp.get(focus-1);
            %match(Prop active) {

              // Or L
              or[] -> {
                ruleName = "or L";
                prems = applyOrL(goal, active);
                ok = true;
              }

              // And L
              and[] -> {
                ruleName = "and L";
                prems = applyAndL(goal, active);
                ok = true;
              }

              // Implies L
              implies[] -> {
                ruleName = "implies L";
                prems = applyImpliesL(goal, active);
                ok = true;
              }

              // forAll L
              forAll(x,_) -> {
                ruleName = "forAll L";
                System.out.print("instance of " + `x + " > ");
                Term new_var = Utils.getTerm();
                prems = applyForAllL(goal, active, new_var);
                ok = true;
              }

              // exists L
              exists[] -> {
                ruleName = "exists L";
                prems = applyExistsL(goal, active);
                ok = true;
              }

              // not a complex formula
              _ -> { if(!ok) continue; }

            }
          }
        } catch (Exception e) {
          System.out.println("can't apply elim: " + e);
          continue;
        }

        // new node creation
        System.out.println("New goals : ");
        %match(SeqList prems) {
          (_*,p,_*) -> {  System.out.println("\t"+PrettyPrinter.prettyPrint(`p)); }
        }
        Premisses trees = `premisses();
        %match(SeqList prems) {
          (_*, p, _*) -> { trees = `premisses(trees*, buildProof(p)); }
        }
        return `rule(ruleName, trees, goal, active);
      }

      else if (command.equals("axiom")) {
        try { applyAxiom(goal); }
        catch (Exception e) {
          System.out.println("can't apply rule : " + e);
          continue;
        }
        return Utils.axiom(goal);
      }

      else if (command.equals("bottom")) {
        try { applyBottom(goal); }
        catch (Exception e) {
          System.out.println("can't apply rule : " + e);
          continue;
        }
        return `rule("bottom", premisses(), goal, nullProp());
      }


      else if (command.equals("rules?")) {
        Prop active = (focus == 0) ? concl : (Prop) hyp.get(focus-1);
        for(int i=0; i<newRules.size(); i++) {
          Rule rule = newRules.get(i);
          Prop conclusion = rule.getconcl();
          HashMap<String,Term> tds = Unification.match(conclusion, active);
          int rule_hs = rule.geths();
          // same side condition
          if (tds != null && 
              ((rule_hs==0 && focus >0) || (rule_hs==1 && focus==0))
             ) 
          {
            System.out.println("--rule " + i + "--:\n");
            System.out.println(PrettyPrinter.prettyRule(rule));
            System.out.println();
          }
        }
      }

      else if (command.startsWith("rule")) {
        int n;
        try { n = Integer.parseInt(command.substring(4).trim()); }
        catch (NumberFormatException e) { continue; }
        if (n < newRules.size()) {
          // TODO verify hand side
          try {
            Rule rule = newRules.get(n);
            Prop active = (focus == 0) ? concl : (Prop) hyp.get(focus-1);
            SeqList prems = applyRule(rule, goal, active);

            // new node creation
            System.out.println("New goals : ");
            %match(SeqList prems) {
              (_*,p,_*) -> {  System.out.println("\t"+PrettyPrinter.prettyPrint(`p)); }
            }
            Premisses trees = `premisses();
            %match(SeqList prems) {
              (_*, p, _*) -> { trees = `premisses(trees*, buildProof(p)); }
            }
            return `rule("rule" + n, trees, goal, active);

          } catch (Exception e) {
            System.out.println("Can't apply rule "+n+": " + e);
          }
        }
      }

      // TODO virer 
      else if (command.equals("quit"))
        break;

      else {
        continue;
      }

      System.out.println("\n" + prettyGoal(hyp, concl, focus) + "\n");
      System.out.print("> ");
    }
    return null;
  }


  private ArrayList<Rule> newRules = new ArrayList<Rule>();

  public void mainLoop() throws Exception {
    String command = null;


    System.out.print("> ");
    while(true) {
      command = Utils.getInput();

      if (command.equals("rule")) {
        System.out.print("lhs. (atom) > ");
        Prop p1 = Utils.getProp();
        System.out.print("rhs. > ");
        Prop p2 = Utils.getProp();
        RuleList rl = RuleCalc.transform(p1,p2);
        %match(RuleList rl) {
          (_*,r,_*) -> { newRules.add(`r);}
        }
        System.out.println("The new deduction rules are : \n");
        System.out.println(PrettyPrinter.prettyRule(rl));
      }

      else if (command.equals("proof")) {
        System.out.print("sequent. > ");
        parser.seq();
        AST t = parser.getAST();
        Sequent seq = (Sequent) walker.seq(t);
        Tree tree = buildProofTree(seq);
        System.out.println("Proof complete.");
        PrettyPrinter.display(tree);
      }

      else {
        continue;
      }

      System.out.print("> ");
    }
  }


  public final static void main(String[] args) throws Exception {
    ProofBuilder test = new ProofBuilder();
    //test.run();
    test.mainLoop();
  }
}
