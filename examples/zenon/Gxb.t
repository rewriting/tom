
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
      f(arg:Hop) -> Hop
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
    %match(Hop test) {
      g(a(),b()) -> { System.out.println("un a et un b"); }
      /*g(x,b()) -> { System.out.println(`x); }*/
    }
    Hop essai = `f(f(f(a)));
    %match(Hop essai) {
      f(f(f(y))) -> { System.out.println(`y); }
    }
    Hop essai2 = `g(f(f(a)),b);
    %match(Hop essai2) {
      g(f(f(y)),y) -> { System.out.println(`y); }
    }
  }
}
