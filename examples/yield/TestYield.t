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
    private boolean done = false;

    public Getter(Strategy s, Visitable t) {
      e = new Environment(this);
      final Visitable ft = t;
      final Strategy  fs = s;
      th = new Thread(new Runnable() {
          public void run() {
          e.setSubject(ft);
          synchronized(e) {
          e.notify();
          try { e.wait(); }
          catch(InterruptedException ex) {}
          }
          try { fs.visit(e); }
          catch(VisitFailure e) {}

          done = true;
          synchronized(e) {
          e.notify();
          }
          }
          });
    }

    public synchronized void set(Visitable v) {
      n = v;
    }

    public Visitable next() {
      if (th.getState() == Thread.State.NEW) {
        synchronized(e) {
          th.start();
          try{ e.wait(); }
          catch(InterruptedException ex) {}
        }
      }
      synchronized(e) {
        e.notify(); 
        try{ e.wait(); }
        catch(InterruptedException ex) {}
      }
      if(done) return null;
      else return n;
    }
  }

  %strategy YStrat() extends Identity() {
    visit T {
      g(x) -> { getEnvironment().yield(`x); }
    }
  }

  public void go() {
    T t = `f(g(a()),f(b(),g(b())));
    Getter g = new Getter(`TopDown(YStrat()),t);

    Visitable next = null;
    do {
      next = g.next();
      System.out.println("next x in g(x) topdown: " + next);
    } while(next != null);

  }

  public static void main(String[] args) {
    TestYield test = new TestYield();
    test.go();
  }
}
