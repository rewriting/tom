package polymorphism;

import polymorphism.example.types.*;
public class PolyExample{
  %include{example/Example.tom}

  public static void main(String[] args) {
    B tt = `b1();
    %match{
      b1() << B tt -> { System.out.println(`tt); }
      b2() << B tt -> { System.out.println(`tt); }
    }

    A rr = `a();
    %match{
      a() << A rr -> { System.out.println(`rr); }
    }
/*
    Exp[B] tt = `f(b1());
    %match{
     f(b1()) << Exp[B] tt -> { System.out.println(`tt); }
     f(b2()) << Exp[B] tt -> { System.out.println(`tt); }
    }
    
    Exp[A] rr = `f(a());
    %match{
      f(a()) << Exp[A] rr -> { System.out.println(`rr); }
    }

*/
  }
}
