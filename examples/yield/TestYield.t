package yield;
import yield.testyield.sig.types.*;
import yield.testyield.sig.*;
import tom.library.sl.*;


public class TestYield {

  %include{ sl.tom }

  %gom {
    module Sig
      abstract syntax

      T = f(t1:T,t2:T)
        | g(t:T)
        | a()
        | b()
  }

  public class Getter implements YieldGetter {
  
    private final Environment e;
    private Visitable n = null;
    private Thread th;

    public Getter(Strategy s, Visitable t) {
      e = new Environment(this);
      final Visitable ft = t;
      final Strategy  fs = s;
      th = new Thread(new Runnable() {
          public void run() {
          e.setSubject(ft);
          synchronized(e.lock1) {
          try { e.lock1.wait(); }
          catch(InterruptedException ex) {}
          }
          try { fs.visit(e); }
          catch(VisitFailure e) {}
          }
          });
    }

    public synchronized void ready() {
      //System.out.println("ici");
      n = e.getSubject();
    }

    public Visitable next() {
      if (th.getState() == Thread.State.NEW) th.start();
      if (th.getState() == Thread.State.TERMINATED) return null;
      while(th.getState() != Thread.State.WAITING) {}
      synchronized(e.lock1) { 
        e.lock1.notify(); 
      }
      synchronized(e.lock2) {
        try{ e.lock2.wait(); }
        catch(InterruptedException ex) {}
      }
      while(th.getState() != Thread.State.WAITING) {  }
      return n;
    }
  }

  %strategy YStrat() extends Identity() {
    visit T {
      g(x) -> { yield(`x); }
    }
  }

  public void go() {
    T t = `f(g(a()),f(b(),g(b())));
    Getter g = new Getter(`TopDown(YStrat()),t);

    Visitable next = null;

    do {
      next = g.next();
      System.out.println("next g(x) topdown: " + next);
    } while(next != null);

  }

  public static void main(String[] args) {
    TestYield test = new TestYield();
    test.go();
  }
}
