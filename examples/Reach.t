import aterm.*;
import aterm.pure.*;
import java.util.*;
import jtom.runtime.*;

public class Reach {

  private ATermFactory factory;
  private GenericTraversal traversal;
  
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
    this.traversal = new GenericTraversal();
    
    fa = factory.makeAFun("a", 0, false);
    fs = factory.makeAFun("s", 1, false);
    ff = factory.makeAFun("f", 1, false);
  }

  public void run(int size) {
    ATerm subject = `f(a);

    ATerm number = int2peano(size);
    ATerm search = `f(number);

    System.out.println("subject = " + subject);

      //ArrayList collection = new ArrayList();
      //collectOneStep(collection,subject);
      //System.out.println("list = " + collection);

    long startChrono = System.currentTimeMillis();
    boolean res = reach(subject,search);
    long stopChrono = System.currentTimeMillis();

    System.out.println("res = " + res + " in " + (stopChrono-startChrono) + " ms");
    
  }

    /*
     * Apply a function to each subterm of a term
     * and collect all possible results in a collection
     */
  public void collectOneStep(final Collection collection, ATerm subject) {
    CollectReach collect = new CollectReach() { 
        public boolean apply(ATerm t, Collection c) {
          %match(term t) { 
            f(x)    -> { c.add(`f(s(x))); }
            f(s(x)) -> { c.add(`f(f(x))); }
            s(s(x)) -> { c.add(`f(x)); }
          }
          return true;
        } // end apply
      }; // end new
    traversal.genericCollectReach(subject, collect, collection); 
  }

  static final int MAXITER = 25;
  public boolean reach(ATerm start, ATerm end) {
    Collection result = new HashSet();
    Collection c1 = new HashSet();
    c1.add(start);

    for(int i=1 ; i<MAXITER ; i++) {
      Collection c2 = new HashSet();
      Iterator it = c1.iterator();
      while(it.hasNext()) {
        collectOneStep(c2,(ATerm)it.next());
      }

      System.out.print("iteration " + i + ":");
      System.out.print("\tc2.size = " + c2.size());
      c2.removeAll(result);
      System.out.print("\tc2'.size = " + c2.size());
      System.out.println();
      c1 = c2;
      result.addAll(c2);
        //System.out.println("result.size = " + result.size());

      if(result.contains(end)) {
        return true;
      }
    }
    return false;
  }
  
  public final static void main(String[] args) {
    Reach test = new Reach(new PureFactory());
    int size;
    try {
      size = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java Reach <size>");
      return;
    }
    
    test.run(size);
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
  
}

