package gom;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import gom.testcollection.m.types.*;
import java.util.*;

public class TestCollection extends TestCase {

  %gom {
    module M
    imports int
    abstract syntax
      T = a() | b() | c()
        | f(T*)
      L = cons(T*)
        | consint(int*)
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestCollection.class));
  }

  public void test1() {
    T l = `f(a(),b());
    assertTrue(l.contains(`a()));
    assertTrue(l.contains(`b()));
    Iterator it = l.iterator();
    boolean a=false;
    boolean b=false;
    while(it.hasNext()) {
      Object o = it.next();
      if(o==`a()) { a = true; }
      if(o==`b()) { b = true; }
    }
    assertTrue(a);
    assertTrue(b);

    Collection<T> col = l.getCollectionf();
    col.add(`c());
    Iterator<T> itc = col.iterator();
    boolean c = false;
    while(itc.hasNext()) {
      T o = itc.next();
      if(o==`c()) { c = true; }
    }
    assertTrue(c);

    for(T x:col) {
      assertTrue(col.contains(x));
    }
  }

  public void test2() {
    L l = `cons(a(),b());
    assertTrue(l.contains(`a()));
    assertTrue(l.contains(`b()));
    Iterator it = l.iterator();
    boolean a=false;
    boolean b=false;
    while(it.hasNext()) {
      Object o = it.next();
      if(o==`a()) { a = true; }
      if(o==`b()) { b = true; }
    }
    assertTrue(a);
    assertTrue(b);

    Collection<T> col = l.getCollectioncons();
    col.add(`c());
    Iterator<T> itc = col.iterator();
    boolean c = false;
    while(itc.hasNext()) {
      T o = itc.next();
      if(o==`c()) { c = true; }
    }
    assertTrue(c);

    for(T x:col) {
      assertTrue(col.contains(x));
    }
  } 
  
  public void test3() {
    L l = `consint(1,2);
    assertTrue(l.contains(1));
    assertTrue(l.contains(2));
    Iterator it = l.iterator();
    boolean a=false;
    boolean b=false;
    while(it.hasNext()) {
      Object o = it.next();
      if(o.equals(new Integer(1))) { a = true; }
      if(o.equals(new Integer(2))) { b = true; }
    }
    assertTrue(a);
    assertTrue(b);

    Collection<Integer> col = l.getCollectionconsint();
    col.add(3);
    Iterator<Integer> itc = col.iterator();
    boolean c = false;
    while(itc.hasNext()) {
      Integer o = itc.next();
      if(o==3) { c = true; }
    }
    assertTrue(c);

    for(Integer x:col) {
      assertTrue(col.contains(x));
    }
  }

}
