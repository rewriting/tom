import sequents.*;
import sequents.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.MuStrategy;
import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import java.util.Collection;
import java.util.HashSet;


import java.io.*;
import antlr.*;
import antlr.collections.*;

import tom.library.strategy.mutraveler.Position;


public class RuleCalc {

  private static Utils ut = new Utils();

  %include { sequents/sequents.tom }
  %include { mutraveler.tom }

  /**
   * usage : RulesComputer rc = new RulesComputer(rulelist);
   *         Prop permutability_problem = rc.getProblem();
   *         while((permutability_problem = rc.getProblem()) != null) {
   *            // ask user for a name
   *            rc.run(name);
   *         }
   *         RuleList res = rc.getResult();
   **/
  private static class RulesComputer {

    private RuleList ruleList = null;
    private Position lastPos = null;
    private Prop lastProp = null;
    private Rule lastRule = null;
    
    public RulesComputer(Prop atom, Prop p) {
      ruleList = `rlist(ruledesc(1,atom,concSeq(sequent( context() , context(p) ))),
	                ruledesc(0,atom,concSeq(sequent( context(p), context()  )))
	          );
      run();
    }

    %strategy ReplaceProp(old_prop: Prop, new_prop: Prop) extends `Identity() {
      visit Prop {
	x -> {
	  if (`x == old_prop) return new_prop;
	}
      }
    }
    
    public void run(String name) {
      HashSet<Term> freevars = Utils.collectFreeVars(lastProp);
      TermList tl = `concTerm();
      for(Term var: freevars) {	tl = `concTerm(tl*,var); }
      Prop newProp = `relationAppl(relation(name),tl);
      MuStrategy rep = (MuStrategy) `TopDown(ReplaceProp(lastProp,newProp));
      lastRule = (Rule) rep.apply(lastRule);
      ruleList = `rlist(ruleList*,lastRule,
	                ruledesc(1,newProp,concSeq(sequent( context() , context(lastProp) ))),
	                ruledesc(0,newProp,concSeq(sequent( context(lastProp), context()  )))
	          );
     run();
    }

    public Prop getProblem() {
      return lastProp;
    }

    public RuleList getResult() {
      return ruleList;
    }

    private void run() {
      boolean pause = false;
      while(!pause) {
	RuleList tmp = null; // to check fixpoint
	
	//System.out.println("etape");
	%match(RuleList ruleList) {
	  /*
	  (X*,ruledesc(hs,_,l),Y*) -> {
	    if (`hs == 1)
	      System.out.println("droite " + PrettyPrinter.prettyRule(`l));
	    else
	      System.out.println("gauche " + PrettyPrinter.prettyRule(`l));
	  }
	  */

	  // axiom
	  (X*,ruledesc(hs,c,(x*,sequent((_*,p,_*),(_*,p,_*)),y*)),Y*) -> {
	    tmp = `rlist(X*,ruledesc(hs,c,concSeq(x*,y*)),Y*);
	  }

	  // bottom
	  (X*,ruledesc(hs,c,(x*,sequent((_*,bottom(),_*),_),y*)),Y*) -> {
	    tmp = `rlist(X*,ruledesc(hs,c,concSeq(x*,y*)),Y*);
	  }

	  // top
	  (X*,ruledesc(hs,c,(x*,sequent(_,(_*,top(),_*)),y*)),Y*) -> {
	    tmp = `rlist(X*,ruledesc(hs,c,concSeq(x*,y*)),Y*);
	  }

	  // and R
	  (X*,ruledesc(hs,c,(x*,sequent(ctxt,(u*,and(a,b),v*)),y*)),Y*) -> {
	    tmp = `rlist(X*,ruledesc(hs,c,concSeq(x*, sequent(ctxt,context(u*,a,v*)),sequent(ctxt,context(u*,b,v*)), y*)),Y*);
	  }

	  // or R
	  (X*,ruledesc(hs,c,(x*,sequent(ctxt,(u*,or(a,b),v*)),y*)),Y*) -> {
	    tmp = `rlist(X*,ruledesc(hs,c,concSeq(x*, sequent(ctxt,context(u*,a,b,v*)), y*)),Y*);
	  }

	  // => R
	  (X*,ruledesc(hs,c,(x*, sequent(ctxt,(u*,implies(a,b),v*)), y*)), Y*) -> {
	    tmp = `rlist(X*,ruledesc(hs,c,concSeq(x*, sequent(context(ctxt*,a),context(u*,b,v*)), y*)), Y*);
	  }

	  // forall R
	  l@(X*,ruledesc(hs,c,(x*, sequent(ctxt,(u*,forAll(n,a),v*)), y*)), Y*) -> {
	    String new_n = Utils.freshVar(`n,`l).getname();
	    Term fresh_v = `FreshVar(new_n,n);
	    Prop new_a = (Prop) Utils.replaceFreeVars(`a,`Var(n), fresh_v);
	    tmp = `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(ctxt, context(u*,new_a,v*)), y*)), Y*);
	  }

	  // exists R
	  l@(X*,rul@ruledesc(hs,c,(x*, sequent(ctxt,(u*,exists(n,a),v*)), y*)), Y*) -> {
	    lastPos = forallInPositivePosition(`a);  
	    if (lastPos == null) {
	      String new_n = Utils.freshVar(`n,`l).getname();
	      Term new_v = `NewVar(new_n,n);
	      Prop new_a = (Prop) Utils.replaceFreeVars(`a,`Var(n), new_v);
	      tmp = `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(ctxt, context(u*,new_a,v*)), y*)), Y*);
	    } else {
	      lastRule = `rul;
	      try {lastProp = (Prop) MuTraveler.init(lastPos.getSubterm()).visit(`a); }
	      catch (VisitFailure e) { e.printStackTrace(); }
	      tmp = `rlist(X*,Y*);
	      pause = true; // get out of fixpoint computation
	    }
	  }

	  // and L
	  (X*,ruledesc(hs,c,(x*,sequent((u*,and(a,b),v*),p),y*)),Y*) -> {
	    tmp = `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(u*,a,b,v*),p), y*)), Y*);
	  }

	  // => L
	  (X*,ruledesc(hs,c,(x*,sequent((u*,implies(a,b),v*),p),y*)),Y*) -> {
	    tmp = `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(u*,v*),context(a,p*)), sequent(context(u*,v*,b),p), y*)), Y*);
	  }

	  // or L
	  (X*,ruledesc(hs,c,(x*,sequent((u*,or(a,b),v*),p),y*)),Y*) -> {
	    tmp = `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(u*,a,v*),p), sequent(context(u*,b,v*),p), y*)), Y*);
	  }

	  // and L
	  (X*,ruledesc(hs,c,(x*,sequent((u*,and(a,b),v*),p),y*)),Y*) -> {
	    tmp = `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(u*,a,b,v*),p), y*)), Y*);
	  }

	  // exists L
	  l@(X*,ruledesc(hs,c,(x*, sequent((u*,exists(n,a),v*),p), y*)), Y*) -> {
	    String new_n = Utils.freshVar(`n,`l).getname();
	    Term fresh_v = `FreshVar(new_n,n);
	    Prop new_a = (Prop) Utils.replaceFreeVars(`a,`Var(n), fresh_v);
	    tmp = `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(u*,new_a,v*),p), y*)), Y*);
	  }

	  // forall L
	  l@(X*,rul@ruledesc(hs,c,(x*, sequent((u*,forAll(n,a),v*),p), y*)), Y*) -> {
	    Position lastPos = forallInNegativePosition(`a); 
	    if (lastPos == null) {
	      String new_n = Utils.freshVar(`n,`l).getname();
	      Term new_v = `NewVar(new_n,n);
	      Prop new_a = (Prop) Utils.replaceFreeVars(`a,`Var(n), new_v);
	      tmp = `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(u*,new_a,v*),p), y*)), Y*);
	    } else {
	      lastRule = `rul;
	      try {lastProp = (Prop) MuTraveler.init(lastPos.getSubterm()).visit(`a); }
	      catch (VisitFailure e) { e.printStackTrace(); }
	      tmp = `rlist(X*,Y*);
	      pause = true; // get out of fixpoint computation
	    }
	  }
	} // big match

	// fixpoint - no rules applied
	if (tmp == null) {
	  // notice it is done
	  lastPos = null;
	  lastProp = null;
	  break;
	}
	else ruleList = tmp;

      } // while loop
    }
  } 

  public static Position forallInNegativePosition(Prop p) {
    return forallNeg(p, true, new Position());
  }

  public static Position forallInPositivePosition(Prop p) {
    return forallNeg(p, false, new Position());
  }


  private static Position forallNeg(Prop p, boolean current, Position pos) {
    %match(Prop p) {
      (relationAppl | top | bottom) [] -> { return null; }

      (and | or) (p1,p2) -> {
	pos.down(1);
	Position pos1 = forallNeg(`p1, current, pos);
	if (pos1 != null) return pos1;
	pos.up();
	pos.down(2);
	pos1 = forallNeg(`p2, current, pos);
	if (pos1 != null) return pos1;
	pos.up();
      }

      implies(p1,p2) -> {
	pos.down(1);
	Position pos1 = forallNeg(`p1, !current, pos);
	if (pos1 != null) return pos1;
	pos.up();
	pos.down(2);
	pos1 = forallNeg(`p2, current, pos);
	if (pos1 != null) return pos1;
	pos.up();
      }

      forAll(_,p1) -> {
	if(current) {
	  pos.down(2);
	  return forallNeg(`p1,current,pos);
	}
	else return pos; 
      }

      exists(_,p1) -> { 
	if(current)
	  return pos;
	else {
	  pos.down(2);
	  return forallNeg(`p1,current,pos); 
	}
      }
    }
    return null;
  }

  public static RuleList transform(Prop atom, Prop p) {
    RulesComputer rc = new RulesComputer(atom,p);
    Prop permut_problem = rc.getProblem();
    int i = 1;
    while((permut_problem = rc.getProblem()) != null) {
      try {
        System.out.print("id for " + 
            PrettyPrinter.prettyPrint(permut_problem) + " > ");
        String name = Utils.getIdent();
      // ask user for a name
      rc.run(name);
      } catch (Exception e) {
        System.out.println("not a valid id : " + e.toString());
      }
    }
    return rc.getResult();
  }


  /*---------------------------- tests -----------------------------*/

  private void run() throws Exception  {
    SeqLexer lexer = new SeqLexer(new DataInputStream(System.in));
    SeqParser parser = new SeqParser(lexer);
    parser.start1();
    AST t = parser.getAST();
    SeqTreeParser walker = new SeqTreeParser();
    Prop p1  = walker.pred(t);
    parser.start1();
    t = parser.getAST();
    Prop p2  = walker.pred(t);
    RuleList rl = transform(p1,p2);

    System.out.println(PrettyPrinter.prettyRule(rl));
  }

  public final static void main(String[] args) throws Exception {
    RuleCalc test = new RuleCalc();
    test.run();
  }
}

