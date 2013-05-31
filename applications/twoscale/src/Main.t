import formula.*;
import formula.types.*;
import java.util.*;
import tom.library.sl.*;

public class Main {

  %include { formula/formula.tom }
  %include { sl.tom }

	public static void main(String[] args) {
	String r = new String();
    Pretty2 pp = new Pretty2();

    // Sum_i=1..n Integral_Omega( d(U.T*(v_i))/dx_i dx )
    Exp U = `Function(Symbol("U"),concExp());
    Exp v = `Function(Symbol("v"),concExp());
    Exp Ts = `Function(Symbol("T^*"),concExp(Index(v,"i")));

    Exp integral = `Integral(
                      Domain("\\Omega"),
                      Mult(Partial(U,Index(Var("x"),"i")),Ts),
                      Var("x"));

    Exp psi = `Sum( Interval(CstInt(1),Var("n")), integral, Var("i"));
    //Exp psi = `Plus(Mult(O(Epsilon(1)),CstInt(3)),
    //                Plus(O(Epsilon(1)),CstInt(5)));
	

	r+=pp.toLatex("Psi pretty : ",pp.pretty(psi));

    try {
	
	  Exp e = `RepeatId(BottomUp(ApproxTs())).visit(psi);
      r+=pp.toLatex("e1 = " , pp.pretty(e));
      e = `RepeatId(BottomUp(Expand())).visit(e);
      r+=pp.toLatex("e2 = " , pp.pretty(e));
      e = `RepeatId(BottomUp(ExpandPartial())).visit(e);
      r+=pp.toLatex("e3 = " , pp.pretty(e));
      //e = `RepeatId(BottomUp(ConvergenceEpsilon())).visit(e);
      //System.out.println("e4 = " + pp.pretty(e));
      e = `RepeatId(BottomUp(LinearityIntegral())).visit(e);
      r+=pp.toLatex("e5 = " , pp.pretty(e));
      e = `RepeatId(BottomUp(LinearitySum())).visit(e);
      r+=pp.toLatex("e6 = " , pp.pretty(e));
	  e = `BottomUp(Sequence(GreenRule(),NoBoundary())).visit(e);
	  r+=pp.toLatex("Step 3 : ",pp.pretty(e));
	  System.out.println(e);

	  
	  
	
	  pp.finalLatex(r);
 

    } catch(VisitFailure error) {
      System.out.println("failure on: " + psi);
    }
	


  }

  // T*(v) -> B(v) + O(epsilon)
  %strategy ApproxTs() extends Identity() {
    visit Exp {
      Function(Symbol("T^*"),concExp(x)) -> {
        return `Plus(Function(Symbol("B"),concExp(x)),O(Epsilon(1)));
      }
    }
  }

  // A*(B+C) -> A*B + A*C
  %strategy Expand() extends Identity() {
    visit Exp {
      Mult(x*,Plus(y,z)) -> Plus(Mult(x*,y),Mult(x*,z))
    }
  }

  // d(A+B)/dx -> dA/dx + dB/dx 
  %strategy ExpandPartial() extends  Identity() {
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
      Mult(_*,o@O(Epsilon[]),_*) -> o
    }
  }
  
  //Green rule
%strategy GreenRule() extends Identity() {
    visit Exp {
	  Integral(Domain(name),Mult(u1*,Partial(v,Index(Var(x),i)),u2*),Var(x)) -> {
	  Exp searched = `Function(Symbol("B"),concExp(Index(Function(Symbol("v"),concExp()),"i")));
	  if(!find(`searched,`v)){return `Minus(Integral(Boundary(name),Mult(Mult(u1*,u2*,v),Index(Index(Var("n"),x),i)),Var("s")),Integral(Domain(name),Mult(v,Partial(Mult(u1*,u2*),Index(Var(x),i))),Var(x)));}}
    }
  }

%strategy NoBoundary() extends Identity() {
	visit Exp {
		Integral(Boundary(_),_,_) -> CstInt(0)
  
	}
}

%strategy Find(p:Exp) extends Fail(){
	visit Exp {
		x -> {if(`x.equals(p)){return `x;}}
	}
}


public static boolean find(Exp p, Exp exp){
	try{
	Exp e = `OnceBottomUp(Find(p)).visit(exp);
	return true;
	}
	catch(VisitFailure error) {
	return false;
	}
}
}