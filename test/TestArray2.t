import java.util.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestArray2 extends TestCase {

  %typearray jtList {
	  implement { List }
 	  get_fun_sym(t) { List.class }
	  cmp_fun_sym(s1,s2) { s1 == s2 }
	  equals(l1,l2) { l1.equals(l2) }
    get_element(l,n) { ((ArrayList)l).get(n) }
    get_size(l)      { ((ArrayList)l).size() }
  }
	  
	%oparray jtList conc(jtElement*){
	  fsym { List.class }
    make_empty(n)    { new ArrayList(n) }
    make_append(e,l) { myAdd(e,(ArrayList)l) }
	}

  private static ArrayList myAdd(Object e, ArrayList l) {
    l.add(e);
    return l;
  }

	%typeterm jtElement {
    implement           { Element }
    get_fun_sym(t)      { Element.class }
    cmp_fun_sym(s1,s2)  { (s1==s2) }
    get_subterm(t, n)   { null }
    equals(t1,t2)       { t1.equals(t2) }
  }
	  
	%op jtElement ListElement(v:jtList) {
	  fsym { (t instanceof Element) && (t.getObject() instanceof List) }
	  is_fsym(t) { true }
	  get_slot(v,e)  { (List)e.getObject() }
	  make(v) { new Element(v) }
	}

	%op jtElement a {
	  fsym { }
	  is_fsym(t) { (t instanceof Element) && t.getObject().equals("a") }
	  make() { new Element("a") }
	}

	%op jtElement b {
	  fsym { }
	  is_fsym(t) { (t instanceof Element) && t.getObject().equals("b") }
	  make() { new Element("b") }
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestArray2.class));
	}
	
	protected void setUp() {
	}

  public void testVariableStar1() {
    int nbSol = 0;
    List l =  `conc(ListElement(conc(a(),b())),a(),b());
		%match(jtList l) {
      conc(ListElement(conc(R*,T*)),X1*,u,X2*) -> {
        nbSol++;
				System.out.println("R = " + `R* + " T = " + `T*+" X1 = " + `X1* + " u = " + `u + " X2 = " + `X2*);
      }
    }

    assertTrue("TestVariableStar1",nbSol==6);
	}
  
  private class Element {
    private Object object;
    
    public Element(Object o) {
      this.object = o;
    }
    
    public Object getObject() {
      return object;
    }

    public String toString() {
      if(getObject() instanceof List) {
        return "ListElement(" + getObject() + ")";
      } else {
        return getObject().toString();
      }
    }
  }

}

