import aterm.*;
import aterm.pure.*;

public class PolySimple2 {
    
  private ATermFactory factory;
  private AFun fplus, fmult;

  public PolySimple2(ATermFactory factory) {
    this.factory = factory;
    fmult = factory.makeAFun("mult", 2, false);
    fplus = factory.makeAFun("plus", 2, false);	   
  }

    // Everything is still an ATerm:
  %typeterm term {
    implement { ATerm }
    get_fun_sym(t)      { (((ATermAppl)t).getAFun()) }
    cmp_fun_sym(t1,t2)  { (t1 == t2) }
    get_subterm(t, n)   { (((ATermAppl)t).getArgument(n)) }
    equals(t1, t2)      { (t1.equals(t2)) }
  }
    
    // My operators with constructor allowing to use "`" symbol 
  %op term zero {
    fsym { factory.makeAFun("0", 0, false) }
    make { factory.makeAppl(factory.makeAFun("0", 0, false)) }
  }
    
  %op term one {
    fsym {  factory.makeAFun("1", 0, false) }
    make { factory.makeAppl(factory.makeAFun("1", 0, false)) }
  }
    
  %op term a {
    fsym {  factory.makeAFun("a", 0, false) }
    make { factory.makeAppl(factory.makeAFun("a", 0, false)) }
  }
  %op term b {
    fsym {  factory.makeAFun("b", 0, false) }
    make { factory.makeAppl(factory.makeAFun("b", 0, false)) }
  }
  %op term c {
    fsym {  factory.makeAFun("c", 0, false) }
    make { factory.makeAppl(factory.makeAFun("c", 0, false)) }
  }
  %op term X {
    fsym {  factory.makeAFun("X", 0, false) }
    make { factory.makeAppl(factory.makeAFun("X", 0, false)) }
  }
  %op term Y {
    fsym {  factory.makeAFun("Y", 0, false) }
    make { factory.makeAppl(factory.makeAFun("Y", 0, false)) }
  }
    
  %op term mult(term, term) {
    fsym { fmult }
    make(t1,t2) { factory.makeAppl(fmult,t1,t2) }
  }
  %op term plus(term,term) {
    fsym { fplus }
    make(t1,t2) { factory.makeAppl(fplus,t1,t2) }
  }
    
  public ATerm differentiate(ATerm poly, ATerm variable) {
    %match(term poly, term variable) {
      X(), X() -> { return `one(); }
      Y(), Y() -> { return `one(); }
      plus(arg1,arg2), var  -> { return `plus(differentiate(arg1, var),differentiate(arg2, var)); }
      mult(arg1,arg2), var  -> { 
        ATerm res1, res2;
        res1 = `mult(arg1, differentiate(arg2, var));
        res2 = `mult(arg2, differentiate(arg1, var));
        return `plus(res1,res2);
      }
      X(), var -> { return `zero(); }
      Y(), var -> { return `zero(); }
      a(), var -> { return `zero(); }
      b(), var -> { return `zero(); }
      c(), var -> { return `zero(); }
      _, _ -> { System.out.println("No match for: " + poly); }
	    
    }
    return null;
  }
    
    // Very basic simplification
  public ATerm simplify(ATerm t) {
    %match(term t) {
      plus(zero, x) | plus(x, zero) -> { return simplify(x); }
      mult(one, x)  | mult(x, one)  -> { return simplify(x); }
      mult(zero, x) | mult(x, zero) -> { return `zero(); }
      plus(x,y)     -> { return `plus( simplify(x), simplify(y) ); }
      mult(x,y)     -> { return `mult( simplify(x), simplify(y) ); }
      zero          -> { return `zero(); }
    }
    return t;
  }

  public void run() {
    ATerm t = `mult(X(),plus(X(),a()));
    ATerm var1 = `X();
    ATerm var2 = `Y();
    ATerm res = null;
	
    res = differentiate(t, var1 );
    System.out.println("Derivative form of " + t + " wrt. " + var1 + " is:\n\t" + res);
    res = simplify(res);
    System.out.println("Simplified form is:\n\t" + res);

    res = differentiate(t, var2 );
    System.out.println("Derivative form of " + t + " wrt. " + var2 + " is:\n\t" + res);
    res = simplify(res);
    System.out.println("Simplified form is:\n\t" + res);
  }
    
  public final static void main(String[] args) {
    PolySimple2 test = new  PolySimple2(new PureFactory());
    test.run();
  }
}

