import aterm.*;
import aterm.pure.*;
import java.util.*;

public class Peano1 {

  ATermFactory factory;
  AFun fzero, fsuc;
  ATerm tzero;

  public Peano1(ATermFactory factory) {
    this.factory = factory;
    fzero = factory.makeAFun("zero", 0, false);
    fsuc  = factory.makeAFun("suc" , 1, false);
    tzero = factory.makeAppl(fzero);
  }


  %typeterm term {
    implement           { ATerm }
    get_fun_sym(t)      { (((ATermAppl)t).getAFun()) }
    cmp_fun_sym(t1,t2)  { t1 == t2 }
    get_subterm(t, n)   { (((ATermAppl)t).getArgument(n)) }
  }

  %op term zero {
    fsym { fzero }
  }
  
  %op term suc(term) {
    fsym { fsuc }
  }

  public ATerm suc(ATerm t) {
    return factory.makeAppl(fsuc,t);
  }
  
  public ATerm plus(ATerm t1, ATerm t2) {
    %match(term t1, term t2) {
      x,zero   -> { return x; }
      x,suc(y) -> { return suc(plus(x,y)); }
    }
    return null;
  }

  public void run(int n) {
    ATerm N = tzero;
    for(int i=0 ; i<n ; i++) {
      N = suc(N);
    }
    ATerm res = plus(N,N);
    System.out.println("plus(" + n + "," + n + ") = " + res);
  }

  public final static void main(String[] args) {
    Peano1 test = new Peano1(new PureFactory());
    test.run(10);
  }
 

}

