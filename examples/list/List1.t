package list;

import aterm.*;
import aterm.pure.*;

public class List1 {
  private ATermFactory factory;

  public List1(ATermFactory factory) {
    this.factory = factory;
  }

  %typelist TomList {
    implement { ATermList }
    get_fun_sym(t) { ((t instanceof ATermList)?factory.makeAFun("conc", 1, false):null) }
    cmp_fun_sym(t1,t2) { t1 == t2 }
    equals(l1,l2) { l1==l2 }
    get_head(l)   { (ATermAppl)l.getFirst() }
    get_tail(l)   { l.getNext() }
    is_empty(l)   { l.isEmpty() }
  }

  %oplist TomList conc( TomTerm* ) {
    fsym { factory.makeAFun("conc", 1, false) }
    make_empty()  { factory.makeList() }
    make_insert(e,l) { l.insert(e) }
  }
  
  %typeterm TomTerm {
    implement { ATermAppl }
    cmp_fun_sym(t1,t2) { t1 == t2 }
    get_fun_sym(t)     { t.getAFun() }
    get_subterm(t, n)  { t.getArgument(n) }
    equals(t1, t2)     { t1==t2 }
  }

  %op TomTerm a {
    fsym { factory.makeAFun("a", 0, false) }
    make() { factory.makeAppl(factory.makeAFun("a", 0, false)) }
  }
  
  %op TomTerm b {
    fsym { factory.makeAFun("b", 0, false) }
    make() { factory.makeAppl(factory.makeAFun("b", 0, false)) }
  }

  %op TomTerm c {
    fsym { factory.makeAFun("c", 0, false) }
    make() { factory.makeAppl(factory.makeAFun("c", 0, false)) }
  }
  
  public ATermList swapSort(ATermList l) {
    %match(TomList l) {
      conc(X1*,x,X2*,y,X3*) -> {
        String xname = x.getName();
        String yname = y.getName();
        if(xname.compareTo(yname) > 0) {
          return `swapSort(conc(X1*,y,X2*,x,X3*));
        }
      }

      _ -> { return l; }
    }
  }

  public ATermList removeDouble(ATermList l) {
    %match(TomList l) {
      conc(X1*,x,x,X2*) -> {
        return `removeDouble(conc(X1*,x,X2*));
      }

      _ -> { return l; }
    }
  }

  public void run() {
    ATermList l = `conc(a,b,c,a,b,c,a);
    ATermList res1 = swapSort(l);
    ATermList res2 = removeDouble(res1);
    System.out.println(" l       = " + l);
    System.out.println("sorted l = " + res1);
    System.out.println("single l = " + res2);
  }

  public final static void main(String[] args) {
    List1 test = new List1(new PureFactory(16));
    test.run();
  }

}

