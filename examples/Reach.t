import aterm.*;
import aterm.pure.*;
import java.util.*;

public class Reach extends GenericTraversal {

  private ATermFactory factory;
  private AFun fa, fs, ff;
  public ATermAppl tzero;

  %typeterm term {
    implement { ATerm }
    get_fun_sym(t)      { (((ATermAppl)t).getAFun()) }
    cmp_fun_sym(t1,t2)  { t1 == t2 }
    get_subterm(t, n)   { (((ATermAppl)t).getArgument(n)) }
  }

  %op term a {
    fsym { fa }
    make { factory.makeAppl(fa) }
  }
  
  %op term s(term) {
    fsym { fs }
    make(t) { factory.makeAppl(fs,t) }
  }

  %op term f(term) {
    fsym { ff }
    make(t) { factory.makeAppl(ff,t) }
  }

  public Reach(ATermFactory factory) {
    this.factory = factory;

    fa = factory.makeAFun("a", 0, false);
    fs = factory.makeAFun("s", 1, false);
    ff = factory.makeAFun("f", 1, false);
  }

  public void run() {
    ATerm number  = int2peano(2);
    ATerm subject = `f(number);

    System.out.println("subject = " + subject);

    ArrayList list = new ArrayList();
    collectOneStep(list,subject);
    
    System.out.println("list = " + list);

    
    
  }
  
  public final static void main(String[] args) {
    Reach test = new Reach(new PureFactory(16));
    test.run();
  }

  public ATerm int2peano(int n) {
    ATerm N = `a();
    for(int i=0 ; i<n ; i++) {
      N = `s(N);
    }
    return N;
  }

  public int peano2int(ATerm N) {
    %match(term N) {
      a    -> { return 0; }
      s(x) -> {return 1+peano2int(x); }
    }
    return 0;
  }
  
    /*
     * Apply a function to each subterm of a term
     * and collect all possible results in a collection
     */
  public void collectOneStep(final Collection collection, ATerm subject) {
    Collect collect = new Collect() { 
        public boolean apply(ATerm t) {
          %match(term t) { 
            f(x)    -> { collection.add(`f(s(x))); }
            f(s(x)) -> { collection.add(`f(f(x))); }
            s(s(x)) -> { collection.add(`f(x)); }
          }
          return true;
        } // end apply
      }; // end new
    genericCollect(subject, collect); 
  }
  
}

