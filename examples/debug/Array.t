import aterm.*;
import aterm.pure.*;
import java.util.*;


public class Array {

  private ATermFactory factory;
  
  private AFun fzero, fsuc, fplus,ffib;
  public ATermAppl tzero;

  %typearray L {
    implement { ArrayList }
    get_fun_sym(t)   { ((t instanceof ArrayList)?factory.makeAFun("conc", 1, false):null) }
    cmp_fun_sym(t1,t2) { t1 == t2 }
    equals(l1,l2)    { l1.equals(l2) }
    get_element(l,n) { ((ArrayList)l).get(n) }
    get_size(l)      { ((ArrayList)l).size() }
  }

  %oparray L conc( E* ) {
    fsym             { factory.makeAFun("conc", 1, false) }
    make_empty(n)    { new ArrayList(n) }
    make_append(e,l) { myAdd(e,(ArrayList)l) }
  }

  private ArrayList myAdd(Object e, ArrayList l) {
    l.add(e);
    return l;
  }
  
  %typeterm E {
    implement           { ATerm }
    get_fun_sym(t)      { (((ATermAppl)t).getAFun()) }
    cmp_fun_sym(t1,t2)  { t1 == t2 } 
    get_subterm(t, n)   { (((ATermAppl)t).getArgument(n)) }
    equals(t1, t2)      { (t1.equals(t2)) }
  }

  %op E a {
    fsym { factory.makeAFun("a", 0, false) }
    make() { factory.makeAppl(factory.makeAFun("a", 0, false)) }
  }
  
  %op E b {
    fsym { factory.makeAFun("b", 0, false) }
    make() { factory.makeAppl(factory.makeAFun("b", 0, false)) }
  }

  %op E c {
    fsym { factory.makeAFun("c", 0, false) }
    make() { factory.makeAppl(factory.makeAFun("c", 0, false)) }
  }

  public Array(ATermFactory factory) {
    this.factory = factory;
  }

  public final static void main(String[] args) {
    Array test = new Array(new PureFactory(16));
    test.run();
  }

  public void run() {
    ArrayList res = sort2(`conc(a,b,c,a,b,c,a));
    System.out.println("res = " + res);
  }

  public ArrayList sort2(ArrayList l) {
    %match(L l) {
      conc(X1*,a,X3*) -> {
        System.out.println("bingo");
        return l;
      }
    }
    return l;
  }


}

