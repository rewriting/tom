import aterm.*;
import aterm.pure.*;
import java.util.*;

public class PeanoAdvanced1 {
  ATermFactory factory;
  public PeanoAdvanced1(ATermFactory factory) {
    this.factory = factory;
  }

  %typeterm term {
    implement           { ATermAppl }
    get_fun_sym(t)      { null }
    cmp_fun_sym(t1,t2)  { false }
    get_subterm(t, n)   { null }
  }

  %op term zero {
    fsym       { /* empty */ }
    is_fsym(t) { t.getAFun() == factory.makeAFun("zero",0,false) }
    make       { factory.makeAppl(factory.makeAFun("zero",0,false)) }
  }
  
  %op term suc(pred:term) {
    fsym             { /* empty */ }
    is_fsym(t)       { t.getAFun() == factory.makeAFun("suc",1,false) }
    get_slot(pred,t) { (ATermAppl)t.getArgument(0) }
    make(t)          { factory.makeAppl(factory.makeAFun("suc",1,false),t) }
  }

  public ATermAppl plus(ATermAppl t1, ATermAppl t2) {
    %match(term t1, term t2) {
      x,zero   -> { return x; }
      x,suc(y) -> { return `suc(plus(x,y)); }
    }
    return null;
  }

  public void run(int n) {
    ATermAppl N = `zero();
    for(int i=0 ; i<n ; i++) {
      N = `suc(N);
    }
    ATermAppl res = plus(N,N);
    System.out.println("plus(" + n + "," + n + ") = " + res);
  }

  public final static void main(String[] args) {
    PeanoAdvanced1 test = new PeanoAdvanced1(new PureFactory());
    test.run(10);
  }
 

}

