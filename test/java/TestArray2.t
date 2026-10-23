import java.util.*;
import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import java.util.logging.Logger;
import java.util.logging.Level;

public class TestArray2 {
  private static Logger logger;
  private static Level level = Level.FINE;

  %typeterm jtList {
    implement { List }
    is_sort(t) { $t instanceof List }
    equals(l1,l2) { $l1.equals($l2) }
  }

  %oparray jtList conc(jtElement*) {
    is_fsym(t) { $t instanceof List }
    make_empty(n)    { new ArrayList($n) }
    make_append(e,l) { myAdd($e,(ArrayList)$l) }
    get_element(l,n) { ((Element)((ArrayList)$l).get($n)) }
    get_size(l)      { ((ArrayList)$l).size() }
  }

  private static ArrayList myAdd(Object e, ArrayList l) {
    l.add(e);
    return l;
  }

  %typeterm jtElement {
    implement { Element }
    is_sort(t) { ($t instanceof Element) }
    equals(t1,t2) { $t1.equals($t2) }
  }

  %op jtElement ListElement(v:jtList) {
    is_fsym(t) { ($t instanceof Element) && (((Element)$t).getObject() instanceof List) }
    get_slot(v,e)  { (List)($e.getObject()) }
    make(v) { new Element($v) }
  }

  %op jtElement a() {
    is_fsym(t) { ($t instanceof Element) && $t.getObject().equals("a") }
    make() { new Element("a") }
  }

  %op jtElement b() {
    is_fsym(t) { ($t instanceof Element) && $t.getObject().equals("b") }
    make() { new Element("b") }
  }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestArray2.class.getName());
  }

  @BeforeClass
  public static void setUp() {
    logger = Logger.getLogger(TestArray2.class.getName());
  }

  @AfterClass
  public static void tearDown() {
    logger = null;
  }

  @Test
  public void testVariableStar1() {
    int nbSol = 0;
    List l =  `conc(ListElement(conc(a(),b())),a(),b());
    %match(jtList l) {
      conc(ListElement(conc(R*,T*)),X1*,u,X2*) -> {
        nbSol++;
        logger.log(level,
            "R = " + `R* + " T = " + `T*+" X1 = " + `X1* + " u = " + `u + " X2 = " + `X2*);
      }
    }
    Assert.assertTrue("TestVariableStar1",nbSol==6);
  }

  private static class Element {
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
