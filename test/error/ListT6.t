import jaterm.api.*;
import jaterm.shared.*;
import java.util.*;

public class List1 {

  private ATermFactory factory;
  
  private AFun fzero, fsuc, fplus,ffib;
  public ATermAppl tzero;

  %typelist L {
    implement { ATermList }
    get_fun_sym(t) { ((t instanceof ATermList)?factory.makeAFun("conc", 1, false):null) }
    cmp_fun_sym(t1,t2) { t1 == t2 }
    equals(l1,l2)  { l1.equals(l2) }
    get_head(l)    { ((ATermList)l).getFirst() }
    get_tail(l)    { ((ATermList)l).getNext() }
    is_empty(l)    { ((ATermList)l).isEmpty() }
  }

// ****** erreur ****** type L already exists

  %typelist L {
    implement { ATermList }
    get_fun_sym(t) { ((t instanceof ATermList)?factory.makeAFun("conc", 1, false):null) }
    cmp_fun_sym(t1,t2) { t1 == t2 }
    equals(l1,l2)  { l1.equals(l2) }
    get_head(l)    { ((ATermList)l).getFirst() }
    get_tail(l)    { ((ATermList)l).getNext() }
    is_empty(l)    { ((ATermList)l).isEmpty() }
  }

// ****** erreur ******

  %oplist L conc( E* ) {
    fsym { factory.makeAFun("conc", 1, false) }
    make_empty()  { factory.makeList() }
    make_add(l,e) { ((ATermList)l).insert((ATerm)e) }
  }
  
// ****** erreur ******  operator conc already exists

  %oplist L conc( E* ) {
    fsym { factory.makeAFun("conc", 1, false) }
    make_empty()  { factory.makeList() }
    make_add(l,e) { ((ATermList)l).insert((ATerm)e) }
  }

// ****** erreur ******

  %typeterm E {
    implement { ATerm }
    get_fun_sym(t)      { (((ATermAppl)t).getAFun()) }
    cmp_fun_sym(t1,t2)  { t1 == t2 } 
    get_subterm(t, n)   { (((ATermAppl)t).getArgument(n)) }
    equals(t1, t2)      { (t1.equals(t2)) }
  }

  %op E a {
    fsym { factory.makeAFun("a", 0, false) }
  }
  
  %op E b {
    fsym { factory.makeAFun("b", 0, false) }
  }

  %op E c {
    fsym { factory.makeAFun("c", 0, false) }
  }
  
  public List1(ATermFactory factory) {
    this.factory = factory;
  }

  public final static void main(String[] args) {
    List1 test = new List1(new PureFactory(16));
    test.run1();
  }

  public void run1() {
    ATerm ta = factory.makeAppl(factory.makeAFun("a", 0, false));
    ATerm tb = factory.makeAppl(factory.makeAFun("b", 0, false));
    ATerm tc = factory.makeAppl(factory.makeAFun("c", 0, false));

    ATermList l = factory.makeList();
    l = l
      .append(ta).append(tb).append(tc)
      .append(ta).append(tb).append(tc)
      .append(ta);

    ATerm res = sort1(l);
    System.out.println(" l       = " + l);
    System.out.println("sorted l = " + res);
  }

  public ATermList sort1(ATermList l) {
    
    %match(L l) {
       
      conc(X1*,x,X2*,y,X3*) -> {
          /*
            System.out.print("\tx  = " + x);
            System.out.print("\tX2 = " + X2);
            System.out.print("\ty  = " + y);
            System.out.println("\tX3 = " + X3);
          */
        String xname = ((ATermAppl)x).getName();
        String yname = ((ATermAppl)y).getName();

        if(xname.compareTo(yname) > 0) {
          return sort1(
            X1
            .append(y)
            .concat(X2)
            .append(x)
            .concat(X3)
            );
        }
        
      }

      _ -> { return l; }
    }
  }
  
}

