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


  public static boolean forallInNegativePosition(Prop p) {
    return forallNeg(p, true);
  }

  public static boolean forallInPositivePosition(Prop p) {
    return forallNeg(p, false);
  }


  private static boolean forallNeg(Prop p, boolean current) {
    %match(Prop p) {
      (relationAppl | top | bottom) [] -> { return false; }
      (and | or) (p1,p2) -> { return forallNeg(`p1,current) || forallNeg(`p2,current); }
      implies(p1,p2) -> { return forallNeg(`p1, !current) || forallNeg(`p2,current); }
      forAll(_,p1) -> { return current ? forallNeg(`p1,current) : true; }
      exists(_,p1) -> { return current ? true : forallNeg(`p1,current); }
    }
    return false;
  }
  
  %strategy NewRulesVisitor() extends `Identity() {
    
    visit RuleList {

// 	    (X*,ruledesc(1,_,l,_),Y*) -> { 
// 		PrettyPrinter pprinter = new PrettyPrinter();
// 		System.out.println("etape = " + pprinter.prettyRule(l));
// 	    }

      // axiom
      (X*,ruledesc(hs,c,(x*,sequent((_*,p,_*),(_*,p,_*)),y*)),Y*) -> {
        return `rlist(X*,ruledesc(hs,c,concSeq(x*,y*)),Y*);
      }
      
      // bottom
      (X*,ruledesc(hs,c,(x*,sequent((_*,bottom(),_*),_),y*)),Y*) -> {
        return `rlist(X*,ruledesc(hs,c,concSeq(x*,y*)),Y*);
      }

      // top
      (X*,ruledesc(hs,c,(x*,sequent(_,(_*,top(),_*)),y*)),Y*) -> {
        return `rlist(X*,ruledesc(hs,c,concSeq(x*,y*)),Y*);
      }

      // and R
      (X*,ruledesc(hs,c,(x*,sequent(ctxt,(u*,and(a,b),v*)),y*)),Y*) -> {
        return `rlist(X*,ruledesc(hs,c,concSeq(x*, sequent(ctxt,context(u*,a,v*)),sequent(ctxt,context(u*,b,v*)), y*)),Y*);
      }

      // or R
      (X*,ruledesc(hs,c,(x*,sequent(ctxt,(u*,or(a,b),v*)),y*)),Y*) -> {
        return `rlist(X*,ruledesc(hs,c,concSeq(x*, sequent(ctxt,context(u*,a,b,v*)), y*)),Y*);
      }

      // => R
      (X*,ruledesc(hs,c,(x*, sequent(ctxt,(u*,implies(a,b),v*)), y*)), Y*) -> {
        return `rlist(X*,ruledesc(hs,c,concSeq(x*, sequent(context(ctxt*,a),context(u*,b,v*)), y*)), Y*);
      }

	    // forall R
      l@(X*,ruledesc(hs,c,(x*, sequent(ctxt,(u*,forAll(n,a),v*)), y*)), Y*) -> {
        String new_n = Utils.freshVar(`n,`l).getname();
        Term fresh_v = `FreshVar(new_n,n);
        Prop new_a = (Prop) Utils.replaceFreeVars(`a,`Var(n), fresh_v);
        return `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(ctxt, context(u*,new_a,v*)), y*)), Y*);
      }

      // exists R
      l@(X*,ruledesc(hs,c,(x*, sequent(ctxt,(u*,exists(n,a),v*)), y*)), Y*) -> {
        if (! forallInPositivePosition(`a)) {
          String new_n = Utils.freshVar(`n,`l).getname();
          Term new_v = `NewVar(new_n,n);
          Prop new_a = (Prop) Utils.replaceFreeVars(`a,`Var(n), new_v);
          return `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(ctxt, context(u*,new_a,v*)), y*)), Y*);
        }
      }
 
	    // and L
	    (X*,ruledesc(hs,c,(x*,sequent((u*,and(a,b),v*),p),y*)),Y*) -> {
        return `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(u*,a,b,v*),p), y*)), Y*);
      }

	    // => L
      (X*,ruledesc(hs,c,(x*,sequent((u*,implies(a,b),v*),p),y*)),Y*) -> {
        return `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(u*,v*),context(a,p*)), sequent(context(u*,v*,b),p), y*)), Y*);
      }
      
      // or L
      (X*,ruledesc(hs,c,(x*,sequent((u*,or(a,b),v*),p),y*)),Y*) -> {
        return `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(u*,a,v*),p), sequent(context(u*,b,v*),p), y*)), Y*);
      }

      // and L
      (X*,ruledesc(hs,c,(x*,sequent((u*,and(a,b),v*),p),y*)),Y*) -> {
        return `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(u*,a,b,v*),p), y*)), Y*);
      }

	    // exists L
      l@(X*,ruledesc(hs,c,(x*, sequent((u*,exists(n,a),v*),p), y*)), Y*) -> {
        String new_n = Utils.freshVar(`n,`l).getname();
        Term fresh_v = `FreshVar(new_n,n);
        Prop new_a = (Prop) Utils.replaceFreeVars(`a,`Var(n), fresh_v);
        return `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(u*,new_a,v*),p), y*)), Y*);
      }
      
      // forall L
      l@(X*,ruledesc(hs,c,(x*, sequent((u*,forAll(n,a),v*),p), y*)), Y*) -> {
        if (! forallInNegativePosition(`a)) {
          String new_n = Utils.freshVar(`n,`l).getname();
          Term new_v = `NewVar(new_n,n);
          Prop new_a = (Prop) Utils.replaceFreeVars(`a,`Var(n), new_v);
          return `rlist(X*, ruledesc(hs,c,concSeq(x*, sequent(context(u*,new_a,v*),p), y*)), Y*);
        }
      }
    }
  }

  public static RuleList transform(Prop atom, Prop p) {
    RuleList init = `rlist(ruledesc(1,atom,concSeq(sequent( context() , context(p) ))),
                           ruledesc(0,atom,concSeq(sequent( context(p), context()  )))
                          );
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

    System.out.println(PrettyPrinter.prettyRule(rl));
  }

  public final static void main(String[] args) throws Exception {
    RuleCalc test = new RuleCalc();
    test.run();
  }
}

