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

  %op L double3(L) {
    fsym { factory.makeAFun("double3", 1, false) }
    make(l) { double3(l) }
  }

  %rule {
    double3(conc(X1*,x,X2*,x,X3*)) -> double3(conc(X1*,X2*,x,X3*))
    double3(conc(X*)) -> conc(X*)
  } 

  public Array(ATermFactory factory) {
    this.factory = factory;
  }

  public final static void main(String[] args) {
    Array test = new Array(new PureFactory(16));
    test.testArray1();
  }

  public void testArray1() {
    ATerm ta = factory.makeAppl(factory.makeAFun("a", 0, false));
    ATerm tb = factory.makeAppl(factory.makeAFun("b", 0, false));
    ATerm tc = factory.makeAppl(factory.makeAFun("c", 0, false));
    ArrayList l = new ArrayList();
    l.add(ta);
    l.add(tb);
    l.add(tc);
    l.add(ta);
    l.add(tb);
    l.add(tc);

    ArrayList res = new ArrayList();
    res.add(ta);
    res.add(ta);
    res.add(tb);
    res.add(tb);
    res.add(tc);
    res.add(tc);
    
    assertTrue(sort1(l).equals(res));
    assertTrue(sort2(l).equals(res));
  }

  public void testArray2() {
    ArrayList l   = `conc(a,b,c,a,b,c,a);
    ArrayList res = `conc(a,b,c);
    
    assertTrue(double1(sort1(l)).equals(res));
    assertTrue(double2(sort2(l)).equals(res));
    assertTrue(double3(sort2(l)).equals(res));
    assertTrue(double4(sort2(l)).equals(res));
    assertTrue(double5(sort2(l)).equals(res));
  }


  public ArrayList sort1(ArrayList l) {
    %match(L l) {
      conc(X1*,x,X2*,y,X3*) -> {
        String xname = ((ATermAppl)x).getName();
        String yname = ((ATermAppl)y).getName();
        if(xname.compareTo(yname) > 0) {
          ArrayList result = X1;
          result.add(y);
          result.addAll(X2);
          result.add(x);
          result.addAll(X3);
          return sort1(result);
        }
      }
        
      _ -> { return l; }
    }
    
  }

  public ArrayList double1(ArrayList l) {
    %match(L l) {
      conc(X1*,x,X2*,x,X3*) -> {
        ArrayList result = X1;
        result.addAll(X2);
        result.add(x);
        result.addAll(X3);
        return double1(result);
      }
      
      _ -> { return l; }
    }
  }

  public ArrayList sort2(ArrayList l) {
    %match(L l) {
      conc(X1*,x,X2*,y,X3*) -> {
        String xname = ((ATermAppl)x).getName();
        String yname = ((ATermAppl)y).getName();
        if(xname.compareTo(yname) > 0) {
          return `sort2(conc(X1*,y,X2*,x,X3*));
        }
      }
        
      _ -> { return l; }
    }
    
  }

  public ArrayList double2(ArrayList l) {
    %match(L l) {
      conc(X1*,x,X2*,x,X3*) -> {
        return `double2(conc(X1*,X2*,x,X3*));
      }
      
      _ -> { return l; }
    }
  }


  public ArrayList double4(ArrayList l) {
    %match(L l) {
      conc(X1*,x@_,X2@_*,x,X3@_*) -> { return `double4(conc(X1*,X2*,x,X3*)); }
      _ -> { return l; }
    }
  }

  public ArrayList double5(ArrayList l) {
    %match(L l) {
      conc(X1*,x@a(),X2*,x@a(),X3*) -> { return `double5(conc(X1*,X2*,x,X3*)); }
      conc(X1*,x@_,X2*,x@_,X3*) -> { return `double5(conc(X1*,X2*,x,X3*)); }
      conc(X1*,x@y,X2*,y@x,X3*) -> { return `double5(conc(X1*,X2*,x,X3*)); }
      _ -> { return l; }
    }
  }

  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }

}

