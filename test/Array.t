import aterm.*;
import aterm.pure.*;
import java.util.*;


public class Array {

  private static ATermFactory factory = SingletonFactory.getInstance();
  
  private AFun fzero, fsuc, fplus,ffib;
  public ATermAppl tzero;

  %typeterm L {
    implement { ArrayList }
    equals(l1,l2)    { l1.equals(l2) }
  }

  %oparray L conc( E* ) {
    is_fsym(t)       { t instanceof ArrayList }
    make_empty(n)    { new ArrayList(n) }
    make_append(e,l) { myAdd(e,(ArrayList)l) }
    get_element(l,n) { (ATerm)((ArrayList)l).get(n) }
    get_size(l)      { ((ArrayList)l).size() }
  }

  private ArrayList myAdd(Object e, ArrayList l) {
    l.add(e);
    return l;
  }
  
  %typeterm E {
    implement           { ATerm }
    equals(t1, t2)      { (t1.equals(t2)) }
  }

  %op E a() {
    is_fsym(t) { ((ATermAppl)t).getName() == "a" }
    make() { factory.makeAppl(factory.makeAFun("a", 0, false)) }
  }
  
  %op E b() {
    is_fsym(t) { ((ATermAppl)t).getName() == "b" }
    make() { factory.makeAppl(factory.makeAFun("b", 0, false)) }
  }

  %op E c() {
    is_fsym(t) { ((ATermAppl)t).getName() == "c" }
    make() { factory.makeAppl(factory.makeAFun("c", 0, false)) }
  }

  %op L double3(s1:L) {
    is_fsym(t) { ((ATermAppl)t).getName() == "double3" }
    //get_slot(s1,t) { return null; }
    make(l) { double3(l) }
  }

  %rule {
    double3(conc(X1*,x,X2*,x,X3*)) -> double3(conc(X1*,X2*,x,X3*))
    double3(conc(X*)) -> conc(X*)
  } 

  public final static void main(String[] args) {
    Array test = new Array();
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
    ArrayList l   = `conc(a(),b(),c(),a(),b(),c(),a());
    ArrayList res = `conc(a(),b(),c());
    
    assertTrue(double1(sort1(l)).equals(res));
    assertTrue(double2(sort2(l)).equals(res));
    assertTrue(double3(sort2(l)).equals(res));
    assertTrue(double4(sort2(l)).equals(res));
    assertTrue(double5(sort2(l)).equals(res));
  }

  public ArrayList sort1(ArrayList l) {
    %match(L l) {
      conc(X1*,x,X2*,y,X3*) -> {
        String xname = ((ATermAppl)`x).getName();
        String yname = ((ATermAppl)`y).getName();
        if(xname.compareTo(yname) > 0) {
          ArrayList result = `X1;
          result.add(`y);
          result.addAll(`X2);
          result.add(`x);
          result.addAll(`X3);
          return sort1(result);
        }
      }
    }
		return l; 
  }

  public ArrayList double1(ArrayList l) {
    %match(L l) {
      conc(X1*,x,X2*,x,X3*) -> {
        ArrayList result = `X1;
        result.addAll(`X2);
        result.add(`x);
        result.addAll(`X3);
        return double1(result);
      }
    }
		return l; 
  }

  public ArrayList sort2(ArrayList l) {
    %match(L l) {
      conc(X1*,x,X2*,y,X3*) -> {
        String xname = ((ATermAppl)`x).getName();
        String yname = ((ATermAppl)`y).getName();
        if(xname.compareTo(yname) > 0) {
          return `sort2(conc(X1*,y,X2*,x,X3*));
        }
      }
    }
		return l; 
  }

  public ArrayList double2(ArrayList l) {
    %match(L l) {
      conc(X1*,x,X2*,x,X3*) -> {
        return `double2(conc(X1*,X2*,x,X3*));
      }
    }
		return l; 
  }


  public ArrayList double4(ArrayList l) {
    %match(L l) {
      conc(X1*,x@_,X2@_*,x,X3@_*) -> { return `double4(conc(X1*,X2*,x,X3*)); }
    }
		return l; 
  }

  public ArrayList double5(ArrayList l) {
    %match(L l) {
      conc(X1*,x@a(),X2*,x@a(),X3*) -> { return `double5(conc(X1*,X2*,x,X3*)); }
      conc(X1*,x@_,X2*,x@_,X3*) -> { return `double5(conc(X1*,X2*,x,X3*)); }
      conc(X1*,x@y,X2*,y@x,X3*) -> { return `double5(conc(X1*,X2*,x,X3*)); }
    }
		return l; 
  }

  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }

}

