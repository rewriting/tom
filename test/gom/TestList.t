package gom;

import static org.junit.Assert.*;
import org.junit.Test;
import gom.testlist.list.types.*;
import gom.testlist.list.types.list.conc;

public class TestList {

  %gom {
    module list
    abstract syntax
    Element = a()
            | b()
    List = conc(Element*)
  }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.runClasses(TestList.class);
  }

  @Test
  public void testException() {
    Element a = `a();
    try {
      a.length();
      fail("length should raise an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void testZeroLength() {
    List l = `conc();
    assertEquals(getListLength(l),l.length());
  }

  @Test
  public void testOneLength() {
    List l = `conc(a());
    assertEquals(getListLength(l),l.length());
  }

  @Test
  public void testTwoLength() {
    List l = `conc(a(),b());
    assertEquals(getListLength(l),l.length());
  }
  @Test
  public void testnLength() {
    int n = 15;
    List l = `conc();
    for(int i = 0; i<n; i++) {
      l = `conc(a(),b(),l*);
    }
    assertEquals(getListLength(l),l.length());
  }

  @Test
  public void testZeroArray() {
    List l = `conc();
    assertEquals(0,((conc)l).toArray().length);
  }

  @Test
  public void testnArray() {
    int n = 15;
    List l = `conc();
    for (int i = 0; i<n; i++) {
      l = `conc(a(),b(),l*);
    }
    Element[] array1 = getListArray(l);
    Object[] array2 = ((conc)l).toArray(); 
    assertEquals(array1.length,array2.length);
    for (int i=0; i<array1.length; i++) {
      assertEquals(array1[i],array2[i]);
    }
  }

  @Test
  public void testFromZeroArray() {
    Element[] array = new Element[]{};
    List l = conc.fromArray(array);
    assertEquals(0,l.length());
  }

  @Test
  public void testFromToZeroArray() {
    Element[] array = new Element[]{};
    List l = conc.fromArray(array);
    assertEquals(0,((conc)l).toArray().length);
  }

  @Test
  public void testFromNArray() {
    int n = 15;
    Element[] array = new Element[15];
    for (int i = 0; i<n; i++) {
      if (0 == i%2) {
        array[i] = `a();
      } else {
        array[i] = `b();
      }
    }
    List list = conc.fromArray(array);
    assertEquals(list.length(),n);
    List l = `conc();
    for (int i = 0; i<n; i++) {
      if (0 == i%2) {
        l = `conc(l*,a());
      } else {
        l = `conc(l*,b());
      }
    }
    assertEquals(l,list);
  }

  @Test
  public void testZeroAdd() {
    List l = `conc();
    assertEquals(`conc(a()),((conc)l).append(`a()));
  }

  @Test
  public void testOneAdd() {
    List l = `conc(a());
    assertEquals(`conc(a(),b()),((conc)l).append(`b()));
  }

  @Test
  public void testZeroReverse() {
    List l = `conc();
    assertEquals(getListReverse(l),l.reverse());
  }

  @Test
  public void testOneReverse() {
    List l = `conc(a());
    assertEquals(getListReverse(l),l.reverse());
  }

  @Test
  public void testTwoReverse() {
    List l = `conc(a(),b());
    assertEquals(getListReverse(l),l.reverse());
  }

  @Test
  public void testnReverse() {
    int n = 15;
    List l = `conc();
    for(int i = 0; i<n; i++) {
      l = `conc(a(),b(),l*);
    }
    assertEquals(getListReverse(l),l.reverse());
  }

  public static List getListReverse(List l) {
    %match(List l) {
      conc(h,t*) -> {
        List nt = getListReverse(`t*);
        return `conc(nt*,h);
      }
    }
    return l;
  }

  public static Element[] getListArray(List l) {
    int length = getListLength(`l);
    Element[] array = new Element[length];
    %match(List l) {
      conc(h,t*) -> {
        array[0]=`h;
        Element[] tailArray = getListArray(`t*);
        for(int i=0;i<length-1;i++){
          array[i+1]=tailArray[i];
        }
      }
    }
    return array;
  }

  public static int getListLength(List l) {
    %match(List l) {
      conc() -> { return 0; }
      conc(_,t*) -> { return 1+getListLength(`t*); }
    }
    return 0;
  }
}
