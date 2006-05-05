import sequents.*;
import sequents.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import java.util.Iterator;
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
    HashMap tds = Unification.match(conclusion, active);
    if (tds == null) throw new Exception("active formula and rule conclusion don't match");


    // renommage des variables
    Set entries = tds.entrySet();
    Iterator it = entries.iterator();
    while( it.hasNext() ) {
      Map.Entry ent = (Map.Entry) it.next();
      Term old_term = `Var((String) ent.getKey());
      Term new_term = (Term) ent.getValue();
      res = (SeqList) Utils.replaceFreeVars(res, old_term, new_term); 
    }


    // creation des variables fraiches
    Set fresh = Utils.getSideConstraints(rule.getprem());
    it = fresh.iterator();
    while( it.hasNext() ) {
      Term fvar = (Term) it.next();
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
    throw new Exception("can't apply rule axiom");
  }
  
  // Bottom

  public static SeqList applyBottomL(Sequent seq) throws Exception {
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


  // User interaction 
 
  SeqLexer lexer = new SeqLexer(new DataInputStream(System.in));
  SeqParser parser = new SeqParser(lexer);
  SeqTreeParser walker = new SeqTreeParser();
  LinkedList new_rules = new LinkedList();
 
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

  private ArrayList getHypothesis(Sequent seq) {
    ArrayList res = new ArrayList();
    Context ctxt = seq.geth();
    %match (Context ctxt) {
      (_*, p, _*) -> {
        res.add(`p);
      }
    }
    return res;
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
          try { applyBottomL(goal); }
          catch (Exception e) {
            System.out.println("can't apply rule : " + e);
            continue;
          }
          return `rule("bottom", premisses(), goal, nullProp());
      }


      else if (command.equals("rules?")) {
        Prop active = (focus == 0) ? concl : (Prop) hyp.get(focus-1);
        for(int i=0; i<newRules.size(); i++) {
          Rule rule = (Rule) newRules.get(i);
          Prop conclusion = rule.getconcl();
          HashMap tds = Unification.match(conclusion, active);
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
            Rule rule = (Rule) newRules.get(n);
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


  private ArrayList newRules = new ArrayList();

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
        Tree tree = buildProof(seq);
        System.out.println("Proof complete.");
        PrettyPrinter.display(tree);
      }

      else {
        continue;
      }

      System.out.print("> ");
    }
  }

  // ---- tests ----

  private void tryRule(Rule r, Sequent seq, Prop active) {
    SeqList res = null;

    System.out.println("\n ======== Test  ========\n\n- rule:\n");
    System.out.println(PrettyPrinter.prettyRule(r));
    System.out.println("\n- on " + PrettyPrinter.prettyPrint(seq) + "\n- focus : " + PrettyPrinter.prettyPrint(active)+"\n");
    try {
      res = applyRule(r, seq, active);
      System.out.println("SUCCES. new premisses : " + PrettyPrinter.prettyRule(res) +"\n\n");
    } catch (Exception e) {
      System.out.println("FAIL\n\n");
    }
  }


  private void tryAll(RuleList rl, Sequent seq) {

    Context ctxt = seq.geth();
    Prop concl = seq.getc();

    for(RuleList l=rl; !l.isEmptyrlist(); l = l.getTailrlist()) {
      Rule current_rule = l.getHeadrlist();
      for(Context c=ctxt; !c.isEmptycontext(); c = c.getTailcontext()) {
        Prop active = c.getHeadcontext();
        if (current_rule.geths() == 0)
          tryRule(current_rule, seq, active);
      }
      if (current_rule.geths() == 1)
        tryRule(current_rule, seq, concl);
    }
  }


  private void run() throws Exception {

    RuleCalc rulecalc = new RuleCalc();

    SeqLexer lexer = new SeqLexer(new DataInputStream(System.in));
    SeqParser parser = new SeqParser(lexer);
    SeqTreeParser walker = new SeqTreeParser();


    System.out.println("Enter new rewriting rule");
    System.out.print("atom. rhs.  ?  ");
    parser.start1();
    AST t = parser.getAST();
    Prop p1  = walker.pred(t);

    parser.start1();
    t = parser.getAST();
    Prop p2  = walker.pred(t);

    RuleList rl = rulecalc.transform(p1,p2);
    System.out.println("\nThe new deduction rules are : \n");
    System.out.println(PrettyPrinter.prettyRule(rl));

    System.out.println("\nEnter new sequent:");
    parser.seq();
    t = parser.getAST();
    Sequent seq = (Sequent) walker.seq(t);

    System.out.println("\nTests:");
    tryAll(rl, seq );
  }

  public final static void main(String[] args) throws Exception {
    ProofBuilder test = new ProofBuilder();
    //test.run();
    test.mainLoop();
  }
}
