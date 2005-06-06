
package zenon;

import aterm.*;
import aterm.pure.*;
import zenon.gxb.abg.*;
import zenon.gxb.abg.types.*;

import java.io.*;

class Gxb {
  %vas{
    module Abg  
  
    public sorts Hop

    abstract syntax
      a -> Hop
      b -> Hop
      f(argf:Hop) -> Hop
      h(argg:Hop) -> Hop
      g(left:Hop,right:Hop) -> Hop
  }

  private zenon.gxb.abg.AbgFactory factory;

  Gxb() {
    factory = AbgFactory.getInstance(SingletonFactory.getInstance());
  }

  protected final AbgFactory getAbgFactory() {
    return factory;
  }

  public static void main(String[] args) {
    Gxb essai = new Gxb();
    essai.test();
  }

  void test() {
    Hop test = `g(a(),b());
    Hop test2 = `g(a(),b());
    Hop test3 = `g(a(),b());
    %match(Hop test) {
      g(a(),b()) -> { System.out.println("un a et un b"); }
      g(x,b()) -> { System.out.println(`x); }
      /*g[left=x] -> { System.out.println(`x); }*/
    }
    /*
    %match(Hop test2, Hop test3) {
      g(a(),b()),f(x) -> { System.out.println("un a et un b"); }
      g(x,b()),f(f(x)) -> { 
        
        %match(Hop x) {
          f(f(f(y))) -> { System.out.println(`y); }
        }
        
        System.out.println(`x); }
      g(a(),x),f(f(x)) -> { System.out.println(`x); }
    }
    */
    /*
    %match(Hop test2) {
      f(f(a())) -> { System.out.println("1"); }
      f(h(x))   -> { System.out.println("2"); }
      f(f(b())) -> { System.out.println("3"); }
    }
    */
    /*
    %match(Hop test2) {
      f(a) -> { System.out.println(`a()); }
    }
    %match(Hop test2) {
      f(x) -> { System.out.println(`x); }
    }
    Hop essai = `f(f(f(a())));
    %match(Hop essai) {
      f(f(f(x))) -> { System.out.println(`x); }
    }
    Hop essai2 = `g(f(f(a)),b);
    %match(Hop essai2) {
      g(x,b()) -> { System.out.println(`x); }
    }
    %match(Hop essai2) {
      g(f(f(x)),y) -> { System.out.println(`x + " " + `y); }
    }
    */
  }
}
