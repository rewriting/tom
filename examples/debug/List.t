import aterm.*;
import aterm.pure.*;
import java.util.*;

public class List {
  private ATermFactory factory;

  %typelist L {
    implement { ATermList }
    get_fun_sym(t) { ((t instanceof ATermList)?factory.makeAFun("conc", 1, false):null) }
    cmp_fun_sym(t1,t2) { t1 == t2 }
    equals(l1,l2)  { l1==l2 }
    get_head(l)    { ((ATermList)l).getFirst() }
    get_tail(l)    { ((ATermList)l).getNext() }
    is_empty(l)    { ((ATermList)l).isEmpty() }
  }

  %oplist L conc( E* ) {
    fsym { factory.makeAFun("conc", 1, false) }
    make_empty()  { factory.makeList() }
    make_insert(e,l) { ((ATermList)l).insert((ATerm)e) }
  }
  
  %typeterm E {
    implement { ATerm }
    cmp_fun_sym(t1,t2)  { t1 == t2 }
    get_fun_sym(t)      { (((ATermAppl)t).getAFun()) }
    get_subterm(t, n)   { (((ATermAppl)t).getArgument(n)) }
    equals(t1, t2)      { t1==t2 }
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
  
  %op E f(E) {
    fsym { factory.makeAFun("f", 1, false) }
    make(t) { factory.makeAppl(factory.makeAFun("f", 1, false),t) }
  }

  public List(ATermFactory factory) {
    this.factory = factory;
  }

  public final static void main(String[] args) {
    List test = new List(new PureFactory(16));
    test.run();
  }

  public void run() {
    ATermList res = sort2(`conc(a,b,c,f(a),b,c,a));
    System.out.println("res = " + res);
  }

  public ATermList sort2(ATermList l) {
    %match(L l) {
      /*
      conc(X1*,x,y,X2*) -> {
        String xname = ((ATermAppl)x).getName();
        String yname = ((ATermAppl)y).getName();
        if(xname.compareTo(yname) > 0) {
          return `sort2(conc(X1*,y,x,X2*));
        }
      }
      */
      conc(X1*,f(x),X2*) -> {
        if(x == `a()) {
          System.out.println("bingo");
          return l;
        }
      }


      _ -> { return l; }
    }
  }

 
}

