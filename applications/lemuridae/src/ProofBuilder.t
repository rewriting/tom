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

import java.io.*;
import antlr.*;
import antlr.collections.*;

public class ProofBuilder {

  %include { sequents/sequents.tom }
  %include { mutraveler.tom }

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
      String name = fvar.getname();
      String new_name = ((Term) tds.get(name)).getname();
      Term new_var = Utils.freshVar(new_name, res);
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
    throw new Exception("can't apply rule");
  }

  public static SeqList applyAndL(Sequent seq, Prop active) throws Exception {
    %match(Sequent seq, Prop active) {
      sequent((X*,act@and(p1,p2),Y*),c), act -> {
        return `concSeq(sequent(context(X*,p1,p2,Y*),c));
      }
    }
    throw new Exception("can't apply rule");
  }

  // Or
  
  public static SeqList applyOrR1(Sequent seq) throws Exception {
    %match(Sequent seq) {
      sequent(ctxt,or(p1,_)) -> {
        return `concSeq(sequent(ctxt,p1));
      }
    }
    throw new Exception("can't apply rule");
  }

  public static SeqList applyOrR2(Sequent seq) throws Exception {
    %match(Sequent seq) {
      sequent(ctxt,or(_,p2)) -> {
        return `concSeq(sequent(ctxt,p2));
      }
    }
    throw new Exception("can't apply rule");
  }

  public static SeqList applyOrL(Sequent seq, Prop active) throws Exception {
    %match(Sequent seq, Prop active) {
      sequent((X*,act@or(p1,p2),Y*),c), act -> {
        return `concSeq(sequent(context(X*,p1,Y*),c), sequent(context(X*,p2,Y*),c));
      }
    }
    throw new Exception("can't apply rule");
  }

  // Implies

  public static SeqList applyImpliesR(Sequent seq) throws Exception {
    %match(Sequent seq) {
      sequent(ctxt,implies(p1,p2)) -> {
        return `concSeq(sequent(context(ctxt*,p1),p2));
      }
    }
    throw new Exception("can't apply rule");
  }
  
  public static SeqList applyImpliesL(Sequent seq, Prop active) throws Exception {
    %match(Sequent seq, Prop active) {
      sequent((X*,act@implies(p1,p2),Y*),c), act -> {
        return `concSeq(sequent(context(X*,Y*),p1), sequent(context(X*,p2,Y*),c));
      }
    }
    throw new Exception("can't apply rule");
  }

  // Axiom

  public static SeqList applyAxiom(Sequent seq, Prop active) throws Exception {
    %match(Sequent seq, Prop active) {
      sequent((_*,act,_*),act), act -> { return `concSeq(); }
    }
    throw new Exception("can't apply rule");
  }
  
  // Bottom

  public static SeqList applyBottomL(Sequent seq) throws Exception {
    %match(Sequent seq) {
      sequent((_*,bottom(),_*),_) -> {
        return `concSeq();
      }
    }
    throw new Exception("can't apply rule");
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
    throw new Exception("can't apply rule");
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
      throw new Exception("can't apply rule");
    }

  // exists

  public static SeqList applyExistsR(Sequent seq, Term new_var) throws Exception {
    %match(Sequent seq) {
      sequent(ctxt,exists(n,p)) -> {
        Prop res = (Prop) Utils.replaceFreeVars(`p, `Var(n), new_var); 
        return `concSeq(sequent(ctxt,res));
      }
    }
    throw new Exception("can't apply rule");
  }

  // TODO add FV test
  public static SeqList applyForAllL(Sequent seq, Prop active) throws Exception 
    {
      %match(Sequent seq, Prop active) {
        sequent((X*,act@exists(_,p),Y*),c), act -> {
          return `concSeq(sequent(context(X*,p,Y*),c));
        }
      }
      throw new Exception("can't apply rule");
    }


  // User interaction 
 
  SeqLexer lexer = new SeqLexer(new DataInputStream(System.in));
  SeqParser parser = new SeqParser(lexer);
  SeqTreeParser walker = new SeqTreeParser();

  LinkedList new_rules = new LinkedList();

  private void proofLoop(Sequent seq) {
    LinkedList openGoals = new LinkedList();
    Tree tree;
    String command = null;
    Sequent goal;
    
    openGoals.add(seq);
    
    while(true) {
      goal = (Sequent) openGoals.getFirst();
      System.out.println("Current goal : " + PrettyPrinter.prettyPrint(goal));
     break; 
    }
  }
  
  public void mainLoop() throws Exception {
    String command = null;
    
    while(true) {
      System.out.print("> ");
      command = Utils.getInput();
      
      if (command.equals("rule")) {
        System.out.print("lhs. (atom) > ");
        Prop p1 = Utils.getProp();
        System.out.print("rhs. > ");
        Prop p2 = Utils.getProp();
        RuleList rl = RuleCalc.transform(p1,p2);
        System.out.println("The new deduction rules are : \n");
        System.out.println(PrettyPrinter.prettyRule(rl));
      }

      if (command.equals("proof")) {
        System.out.print("sequent. > ");
        parser.seq();
        AST t = parser.getAST();
        Sequent seq = (Sequent) walker.seq(t);
        proofLoop(seq);
      }
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
