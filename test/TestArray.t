import aterm.*;
import aterm.pure.*;
import java.util.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestArray extends TestCase {

  private ATermFactory factory;
  
  private AFun fzero, fsuc, fplus,ffib;
  public ATermAppl tzero;
	private ArrayList unsortedlist;
	private ArrayList sortedlist;
	private ArrayList listwithdoubles;
	private ArrayList listwithoutdoubles;

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

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestArray.class));
	}
	
	protected void setUp() {
		this.factory = new PureFactory(16);

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
		this.unsortedlist = l;
					
		ArrayList res = new ArrayList();
		res.add(ta);
		res.add(ta);
		res.add(tb);
		res.add(tb);
		res.add(tc);
		res.add(tc);
		this.sortedlist = res;
					
		this.listwithdoubles = `conc(a,b,c,a,b,c,a);
		this.listwithoutdoubles = `conc(a,b,c);
					
	}

  private ArrayList myAdd(Object e, ArrayList l) {
    l.add(e);
    return l;
  }
  
	public void testSort1() {
		assertEquals(
			"sort1 should return a sorted list",
			sort1(unsortedlist), sortedlist);
	}

	public void testSort2() {
		assertEquals(
			"sort2 should return a sorted list",
			sort2(unsortedlist), sortedlist);
	}

	public void testDouble1() {
		assertEquals(
			"double1 should remove all double element in a list",
			double1(sort1(listwithdoubles)),listwithoutdoubles);
	}

	public void testDouble2() {
		assertEquals(
			"double2 should remove all double element in a list",
			double2(sort2(listwithdoubles)),listwithoutdoubles);
	}

	public void testDouble3() {
		assertEquals(
			"double3 should remove all double element in a list",
			double3(sort2(listwithdoubles)),listwithoutdoubles);
	}

	public void testDouble4() {
		assertEquals(
			"double4 should remove all double element in a list",
			double4(sort2(listwithdoubles)),listwithoutdoubles);
	}

	public void testDouble5() {
		assertEquals(
			"double5 should remove all double element in a list",
			double5(sort2(listwithdoubles)),listwithoutdoubles);
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
        
      _ -> { return l; }
    }
    
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
      
      _ -> { return l; }
    }
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

  public void testVariableStar1() {
    int nbSol = 0;
    ArrayList l = `conc(a,b);
		%match(L l) {
      conc(X1*,X2*,X3*) -> {
        nbSol++;
				//System.out.println("X1 = " + `X1* + " X2 = " + `X2*+ " X3 = " + `X3*);
      }
    }

    assertTrue("TestVariableStar1",nbSol==6);
	}
  

}

