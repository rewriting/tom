package gom;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import gom.testcollection.m.types.*;
import gom.testcollection.m.types.t.*;
import gom.testcollection.m.types.l.*;
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
    f l = (f) `f(a(),b());
    assertTrue(l.contains(`a()));
    assertTrue(l.contains(`b()));
    Iterator<T> it = l.iterator();
    boolean a=false;
    boolean b=false;
    while(it.hasNext()) {
      T o = it.next();
      if(o==`a()) { a = true; }
      if(o==`b()) { b = true; }
    }
    assertTrue(a);
    assertTrue(b);
    for(T x:l) {
      assertTrue(l.contains(x));
    }

    Collection<T> col = l.getCollection();
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
    cons l = (cons)`cons(a(),b());
    assertTrue(l.contains(`a()));
    assertTrue(l.contains(`b()));
    Iterator<T> it = l.iterator();
    boolean a=false;
    boolean b=false;
    while(it.hasNext()) {
      T o = it.next();
      if(o==`a()) { a = true; }
      if(o==`b()) { b = true; }
    }
    assertTrue(a);
    assertTrue(b);

    for(T x:l) {
      assertTrue(l.contains(x));
    }

    Collection<T> col = l.getCollection();
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
    consint l = (consint) `consint(1,2);
    assertTrue(l.contains(1));
    assertTrue(l.contains(2));
    Iterator<Integer> it = l.iterator();
    boolean a=false;
    boolean b=false;
    while(it.hasNext()) {
      Integer o = it.next();
      if(o==1) { a = true; }
      if(o==2) { b = true; }
    }
    assertTrue(a);
    assertTrue(b);

    for(Integer x:l) {
      assertTrue(l.contains(x));

    }
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
