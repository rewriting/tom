import aterm.*;
import aterm.pure.*;
import java.util.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class TestArray {

  private static ATermFactory factory = SingletonFactory.getInstance();

  private AFun fzero, fsuc, fplus,ffib;
  public ATermAppl tzero;
  private ArrayList unsortedlist;
  private ArrayList sortedlist;
  private ArrayList listwithdoubles;
  private ArrayList listwithoutdoubles;
  private static Logger logger;
  private static Level level = Level.FINE;

  %typeterm L {
    implement { ArrayList }
    is_sort(t) { $t instanceof ArrayList }
    equals(l1,l2)    { ($l1).equals($l2) }
  }

  %oparray L conc( E* ) {
    is_fsym(t)       { $t instanceof ArrayList }
    get_element(l,n) { (ATerm)((ArrayList)$l).get($n) }
    get_size(l)      { ((ArrayList)$l).size() }
    make_empty(n)    { new ArrayList($n) }
    make_append(e,l) { myAdd($e,(ArrayList)$l) }
  }

  private static ArrayList myAdd(Object e, ArrayList l) {
    l.add(e);
    return l;
  }

  %typeterm E {
    implement           { ATerm }
    is_sort(t) { $t instanceof ATerm }
    equals(t1, t2)      { ($t1).equals($t2) }
  }

  %op E a() {
    is_fsym(t) { ((ATermAppl)$t).getName() == "a" }
    make() { factory.makeAppl(factory.makeAFun("a", 0, false)) }
  }

  %op E b() {
    is_fsym(t) { ((ATermAppl)$t).getName() == "b" }
    make() { factory.makeAppl(factory.makeAFun("b", 0, false)) }
  }

  %op E c() {
    is_fsym(t) { ((ATermAppl)$t).getName() == "c" }
    make() { factory.makeAppl(factory.makeAFun("c", 0, false)) }
  }

  public static void main(String[] args) {
    level = Level.INFO;
    org.junit.runner.JUnitCore.main(TestArray.class.getName());
  }

  @BeforeClass
  public static void staticSetUp() {
    logger = Logger.getLogger(TestArray.class.getName());
  }

  @Before
  public void setUp() {
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

    this.listwithdoubles = `conc(a(),b(),c(),a(),b(),c(),a());
    this.listwithoutdoubles = `conc(a(),b(),c());
  }

  @AfterClass
  public static void staticTearDown() {
    logger = null;
  }

  @Test
  public void testSort1() {
    assertEquals(
        "sort1 should return a sorted list",
        sort1(unsortedlist), sortedlist);
  }

  @Test
  public void testSort2() {
    assertEquals(
        "sort2 should return a sorted list",
        sort2(unsortedlist), sortedlist);
  }

  @Test
  public void testDouble1() {
    assertEquals(
        "double1 should remove all double element in a list",
        double1(sort1(listwithdoubles)),listwithoutdoubles);
  }

  @Test
  public void testDouble2() {
    assertEquals(
        "double2 should remove all double element in a list",
        double2(sort2(listwithdoubles)),listwithoutdoubles);
  }

  @Test
  public void testDouble4() {
    assertEquals(
        "double4 should remove all double element in a list",
        double4(sort2(listwithdoubles)),listwithoutdoubles);
  }

  @Test
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

  @Test
  public void testVariableStar1() {
    int nbSol = 0;
    ArrayList l = `conc(a(),b());
    %match(L l) {
      conc(X1*,X2*,X3*) -> {
        nbSol++;
        logger.log(level,"X1 = " + `X1* + " X2 = " + `X2*+ " X3 = " + `X3*);
      }
    }
    assertTrue("TestVariableStar1",nbSol==6);
  }
}
