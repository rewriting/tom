import aterm.*;
import aterm.pure.*;

public class Poly1 {
    
  private ATermFactory factory;
  private AFun fzero, fone, fa, fX, fplus, fmult;
  public ATermAppl tzero;
    
  public Poly1(ATermFactory factory) {
    this.factory = factory;
	
    fzero = factory.makeAFun("zero", 0, false);
    fone  = factory.makeAFun("one", 0, false);
    fa    = factory.makeAFun("a", 0, false);
    fX    = factory.makeAFun("X", 0, false);
    fmult = factory.makeAFun("mult" , 2, false);
    fplus = factory.makeAFun("plus", 2, false);
    tzero = factory.makeAppl(fzero);
  }    
    // Everything is an Aterm:
  %typeterm term {
    implement {
      ATerm
        }
    get_fun_sym(t)      { (((ATermAppl)t).getAFun()) }
    cmp_fun_sym(t1,t2)  { t1 == t2 }
    get_subterm(t, n)   { (((ATermAppl)t).getArgument(n)) }
    equals(t1, t2)      { (t1.equals(t2)) }
  }
    
    // My operators
  %op term zero {
    fsym { fzero }
  }
  %op term one {
    fsym { fone }
  }
  %op term a {
    fsym { fa }
  }
  %op term X {
    fsym { fX }
  }
  %op term plus(term,term) {
    fsym { fplus }
  }   
  %op term mult(term, term) {
    fsym { fmult }
  }

    
  public ATerm zero() {
    return tzero;
  }
  
  public ATerm one() {
    return factory.makeAppl(fone);
  } 

  public ATerm a() {
    return factory.makeAppl(fa);
  }
  
  public ATerm X() {
    return factory.makeAppl(fX);
  }
  
  public ATerm plus(ATerm arg1, ATerm arg2) {
    return factory.makeAppl(fplus, arg1, arg2);
  }
  
  public ATerm mult(ATerm arg1, ATerm arg2) {
    return factory.makeAppl(fmult,arg1, arg2 );
  } 
    
  public ATerm differentiate(ATerm poly) {
    %match(term poly) {
      a()             -> { return zero(); }
      X()             -> { return one(); }
      plus(arg1,arg2) -> { return plus(differentiate(arg1),differentiate(arg2)); }
      mult(arg1,arg2) -> { 
        ATerm res1 = mult(arg1, differentiate(arg2));
        ATerm res2 = mult(arg2, differentiate(arg1));
        return plus(res1,res2);
      }
      _ -> { System.out.println("No match for: "+poly); }
	    
    }
    return null;
  }
    
  public void run() {
    ATerm t = mult(X(),plus(X(), a()));
    ATerm res = null;
	
    res = differentiate(t);
    System.out.println("Derivative form of " + t +" =\n\t" + res);
  }
    
  public final static void main(String[] args) {
    Poly1 test = new  Poly1(new PureFactory(16));
    test.run();
  }
}
