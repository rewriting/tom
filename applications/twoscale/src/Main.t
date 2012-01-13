import formula.*;
import formula.types.*;
import java.util.*;
import tom.library.sl.*;

public class Main {

  %include { formula/formula.tom }
  %include { sl.tom }

	public static void main(String[] args) {
    Pretty pp = new Pretty();

    // Sum_i=1..n Integral_Omega( d(U.T*(v_i))/dx_i dx )
    Exp U = `Function(Symbol("U"),concExp());
    Exp v = `Function(Symbol("v"),concExp());
    Exp Ts = `Function(Symbol("T*"),concExp(Index(v,"i")));

    Exp integral = `Integral(
                      Domain("Omega"),
                      Partial(Mult(U,Ts),Index(Var("x"),"i")),
                      Var("x"));

    Exp psi = `Sum( Interval(CstInt(1),Var("n")), integral, Var("i"));
    //Exp psi = `Plus(Mult(O(Epsilon(1)),CstInt(3)),
    //                Plus(O(Epsilon(1)),CstInt(5)));

    System.out.println("psi = " + psi);
    System.out.println("psi = " + pp.pretty(psi));

    try {

      Exp e = `RepeatId(BottomUp(ApproxTs())).visit(psi);
      System.out.println("e1 = " + pp.pretty(e));
      e = `RepeatId(BottomUp(Expand())).visit(e);
      System.out.println("e2 = " + pp.pretty(e));
      e = `RepeatId(BottomUp(ExpandPartial())).visit(e);
      System.out.println("e3 = " + pp.pretty(e));
      //e = `RepeatId(BottomUp(ConvergenceEpsilon())).visit(e);
      //System.out.println("e4 = " + pp.pretty(e));
      e = `RepeatId(BottomUp(LinearityIntegral())).visit(e);
      System.out.println("e4 = " + pp.pretty(e));
      e = `RepeatId(BottomUp(LinearitySum())).visit(e);
      System.out.println("e5 = " + pp.pretty(e));

    } catch(VisitFailure e) {
      System.out.println("failure on: " + psi);
    }


  }

  // T*(v) -> B(v) + O(epsilon)
  %strategy ApproxTs() extends Identity() {
    visit Exp {
      Function(Symbol("T*"),concExp(x)) -> {
        return `Plus(Function(Symbol("B"),concExp(x)),O(Epsilon(1)));
      }
    }
  }

  // A*(B+C) -> A*B + A*C
  %strategy Expand() extends Identity() {
    visit Exp {
      Mult(x,Plus(y,z)) -> Plus(Mult(x,y),Mult(x,z))
    }
  }

  // d(A+B)/dx -> dA/dx + dB/dx 
  %strategy ExpandPartial() extends Identity() {
    visit Exp {
      Partial(Plus(x,y),z) -> Plus(Partial(x,z),Partial(y,z))
    }
  }

  // Integral(A+B) -> Integral(A)+Integral(B)
  %strategy LinearityIntegral() extends Identity() {
    visit Exp {
      Integral(domain,Plus(x,y),z) -> Plus(Integral(domain,x,z),Integral(domain,y,z))
    }
  }

  // Sum(A+B) -> Sum(A)+Sum(B)
  %strategy LinearitySum() extends Identity() {
    visit Exp {
      Sum(domain,Plus(x,y),z) -> Plus(Sum(domain,x,z),Sum(domain,y,z))
    }
  }

  // O(e) + O(e) -> O(e)
  // O(e) - O(e) -> O(e)
  // c.O(e) -> O(e)
  %strategy ConvergenceEpsilon() extends Identity() {
    visit Exp {
      Plus(o@O(Epsilon[]),o) -> o
      Minus(o@O(Epsilon[]),o) -> o
      Mult(_,o@O(Epsilon[])) -> o
    }
  }

}
