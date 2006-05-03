import sequents.*;
import sequents.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import java.util.Collection;
import java.util.HashSet;


import java.io.*;
import antlr.*;
import antlr.collections.*;



public class RuleCalc {

  private static Utils ut = new Utils();


  %include { sequents/sequents.tom }
  %include { mutraveler.tom }


  static int counter = 0;

  %strategy NewRulesVisitor() extends `Identity() {
    
    visit RuleList {

// 	    (X*,ruledesc(1,_,l,_),Y*) -> { 
// 		PrettyPrinter pprinter = new PrettyPrinter();
// 		System.out.println("etape = " + pprinter.prettyRule(l));
// 	    }

      // and R
      (X*,ruledesc(hs,c,(x*,sequent(ctxt,and(a,b)),y*)),Y*) -> {
        return `rlist(X*,ruledesc(hs,c,concSeq(x*, sequent(ctxt,a), sequent(ctxt,b), y*)),Y*);
      }

      // => R
      (X*,ruledesc(hs,c,(x*, sequent(ctxt,implies(a,b)), y*)), Y*) -> {
        return `rlist(X*,ruledesc(hs,c,concSeq(x*, sequent(context(ctxt*,a),b), y*)), Y*);
      }

	    // forall R
      (X*,ruledesc(hs,c,(x*, sequent(ctxt,forAll(n,a)), y*)), Y*) -> {
        //Term fresh_v = freshVar(n,l);
        Term fresh_v = `FreshVar(counter++,n);
        Prop new_a = (Prop) Utils.replaceFreeVars(`a,`Var(n), fresh_v);
        if (new_a != `a) { // si on a remplace qqc
          return `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(ctxt, new_a), y*)), Y*);
        } else
          return `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(ctxt, a), y*)), Y*);
      }

	    // and L
	    (X*,ruledesc(hs,c,(x*,sequent((t*,and(a,b),q*),p),y*)),Y*) -> {
        return `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(t*,a,b,q*),p), y*)), Y*);
      }

	    // => L
      (X*,ruledesc(hs,c,(x*,sequent((t*,implies(a,b),q*),p),y*)),Y*) -> {
        return `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(t*,q*),a), sequent(context(t*,q*,b),p), y*)), Y*);
      }

	    // ou L
      (X*,ruledesc(hs,c,(x*,sequent((t*,or(a,b),q*),p),y*)),Y*) -> {
        return `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(t*,a,q*),p), sequent(context(t*,b,q*),p), y*)), Y*);
      }
    }
  }

  public RuleList transform(Prop atom, Prop p) {
    RuleList init = `rlist(ruledesc(1,atom,concSeq(sequent(context(),p))),
        ruledesc(0,atom,concSeq(sequent(context(p),nullProp()))));
    NewRulesVisitor v = new NewRulesVisitor();
    try {
      RuleList l = (RuleList) MuTraveler.init(`RepeatId(v)).visit(init);
      return l;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

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

    PrettyPrinter pprinter = new PrettyPrinter();
    System.out.println(pprinter.prettyRule(rl));
  }

  public final static void main(String[] args) throws Exception {
    RuleCalc test = new RuleCalc();
    test.run();
  }
}

