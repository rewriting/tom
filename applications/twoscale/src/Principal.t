import formule.*;
import formule.types.*;
import java.util.*;
import tom.library.sl.*;

public class Principal {

  %include { ../gen/formule/formule.tom }
  %include { sl.tom }

	public static void main(String[] args) {
		
		String test = new String();
		String test2 = new String();
		String test3 = new String();
		String test4 = new String();
		String test4bis = new String();
		String test5 = new String();
		String test6 = new String();
		String test7 = new String();
		String test8 = new String();
		String test9 = new String();
		String test10 = new String();
		String test11 = new String();
		String test12 = new String();
		String test13 = new String();
		String test14 = new String();
		String test15 = new String();
		String test17 = new String();
		String test19 = new String();
		String test20 = new String();
		String test21 = new String();
		String test22 = new String();
		Print p = new Print();
		
		Exp un = `CstInt(1);
		Exp n = `Var("n");
		Region intervalle = `Interval(un,n);
		Region domaine = `Domain("omega");
		Exp u = `Var("u");
		Exp x = `Var("x");
		Exp i = `Var("i");
		Exp varindice = `VarIndice(x,i);
		Exp function = `Function(u,x);
		Exp partial = `Partial(function,varindice);
		Exp symbol = `Var("T'");
		Exp v = `Var("v");
		Exp foncti = `FunctionIndice(v,i,x);
		Exp function2 = `Function(symbol,foncti);
		Exp mult = `Mult(partial,function2);
		Exp fon = `FunctionVar(mult,x);
		Exp integrale = `Integral(domaine,fon);
		Exp psi = `Sum(intervalle,integrale,i);
	

	
		
		try{
			test+=p.toLatex("psi = ",p.printer(psi));
			
			Exp psi2 = `RepeatId(BottomUp(T())).visit(psi);
			test2+=p.toLatex("psi2 = ",p.printer(psi2));
			
			Exp psi3 = `RepeatId(BottomUp(Dev())).visit(psi2);
			test3+=p.toLatex("psi3 = ",p.printer(psi3));
			
			Exp psi4 = `RepeatId(BottomUp(LinInt())).visit(psi3);
			test4+=p.toLatex("psi4 = ",p.printer(psi4));
			
			Exp psi4bis = `RepeatId(BottomUp(LinSom())).visit(psi4);
			test4bis+=p.toLatex("psi4bis = ",p.printer(psi4bis));
			
			Exp psi5 = `RepeatId(BottomUp(Eps())).visit(psi4bis);
			test5+=p.toLatex("psi5 = ",p.printer(psi5));
			
			Exp psi6 = `RepeatId(BottomUp(EpsInt())).visit(psi5);
			test6+=p.toLatex("psi6 = ",p.printer(psi6));
			
			Exp psi7= `RepeatId(BottomUp(GreenRule())).visit(psi6);
			test7+=p.toLatex("psi7 = ",p.printer(psi7));
			
			Exp psi8 =  `RepeatId(BottomUp(EpsSom())).visit(psi7);
			test8+=p.toLatex("psi8 = ",p.printer(psi8));
			
			Exp psi9 =`RepeatId(BottomUp(EpsSom())).visit(psi8);
			test9+=p.toLatex("psi9 = ",p.printer(psi9));
			
			Exp psi10 = `RepeatId(BottomUp(Rmv())).visit(psi9);
			test10+=p.toLatex("psi10 = ",p.printer(psi10));
			
			Exp psi11 = `RepeatId(BottomUp(ChainRule())).visit(psi10);
			test11+=p.toLatex("psi11 = ",p.printer(psi11));
			
			Exp psi12 = `RepeatId(BottomUp(Int())).visit(psi11);
			test12+=p.toLatex("psi12 = ",p.printer(psi12));
			
			Exp psi13 = `RepeatId(BottomUp(Psi1())).visit(psi12);
			test13+=p.toLatex("psi13 = ",p.printer(psi13));
			
			Exp psi14 = `RepeatId(BottomUp(Psi2())).visit(psi13);
			test14+=p.toLatex("psi14 = ",p.printer(psi14));
			
			Exp psi15 = `RepeatId(BottomUp(Te())).visit(psi14);
			test15+=p.toLatex("psi15 = ",p.printer(psi15));

			Exp psi17= `RepeatId(BottomUp(functionDoubleVar())).visit(psi15);
			
			//Exp psi18=`RepeatId(BottomUp(functionDouble())).visit(psi17);
			
			Exp psi19 = `RepeatId(BottomUp(DomInt())).visit(psi17);
			test19+=p.toLatex("psi19 = ",p.printer(psi19));
			System.out.println(psi19);
			
			Exp psi20 = `RepeatId(BottomUp(TLin())).visit(psi19);
			test20+=p.toLatex("psi20 = ",p.printer(psi20));
			System.out.println(psi20);
			
			Exp psi21 = `RepeatId(BottomUp(LinIntVar2())).visit(psi20);
			test21+=p.toLatex("psi21 = ",p.printer(psi21));
			
			Exp psi22 = `RepeatId(BottomUp(zeroOrder())).visit(psi21);
			test22+=p.toLatex("psi22 = ",p.printer(psi22));


			p.finalLatex(test+test2+test3+test4+test5+test6+test7+test8+test9+test10+test11+test12+test13+test14+test15+test19+test20+test21+test22); 
		}
		catch(VisitFailure error){
			System.out.println("Essaie encore");
		}
		
	}
	
	%strategy T() extends Identity(){
		visit Exp{
			Function(Var("T'"),a) -> Plus(Function(Var("B"),a),O(Epsilon(1)))
		}
	}
	
	%strategy TLin() extends Identity(){
		visit Exp{
			Function(a,Plus(b,c)) -> Plus(Function(a,b),Function(a,c))
		}
	}

	%strategy Dev() extends Identity(){
		visit Exp{
			Mult(a,Plus(b,c)) -> Plus(Mult(a,b),Mult(a,c))
		}
	}
	
		%strategy LinInt() extends Identity() {
		visit Exp{
			Integral(a,FunctionVar(Plus(e,f),d)) -> Plus(Integral(a,FunctionVar(e,d)),Integral(a,FunctionVar(f,d)))
		}
	}
	
		%strategy LinIntVar2() extends Identity(){
			visit Exp{
				Integral(a, FunctionVar2(Plus(b,c),d,e)) -> Plus(Integral(a,FunctionVar2(b,d,e)),Integral(a,FunctionVar2(c,d,e)))
			}
		}
	
		%strategy Int() extends Identity() {
		visit Exp{
			Integral(a,FunctionVar(Mult(x,Plus(y,w)),d)) -> Plus(Integral(a,FunctionVar(Mult(x,y),d)),Integral(a,FunctionVar(Mult(x,w),d)))
		}
	}
	
	
	%strategy LinSom() extends Identity(){
		visit Exp{
			Sum(a,Plus(b,c),d) -> Plus(Sum(a,b,d),Sum(a,c,d))
		}
	}
	
	%strategy Eps() extends Identity(){
		visit Exp{
			Mult(a,O(Epsilon(1))) -> O(Epsilon(1))
		}
	}
	
	%strategy EpsInt() extends Identity(){
		visit Exp{
			Integral(a,FunctionVar(O(Epsilon(1)),b)) -> O(Epsilon(1))
		}
	}
	%strategy GreenRule() extends Identity(){
		visit Exp{
			Sum(I,Integral(Domain(r),FunctionVar(Mult(Partial(a,b),c),d)),i) -> Minus(Sum(I,Integral(PartialDomain(r),FunctionVar(Mult(a,Mult(c,IndiceGreenRule(Var("n"),Var("x"),Var("i")))),Function(Var("s"),Var("x")))),i),	Sum(I,Integral(Domain(r),FunctionVar(Mult(a,Partial(c,b)),d)),i))
		}
	}
	
	%strategy EpsSom() extends Identity(){
		visit Exp{
			Sum(I,O(Epsilon(1)),i)-> O(Epsilon(1))
		}
	}
	
	%strategy Rmv() extends Identity(){
		visit Exp{
			Plus(Minus(Sum(D,Integral(a,FunctionVar(b,c)),i),Sum(D,Integral(R,FunctionVar(w,x)),y)),O(Epsilon(1))) -> Plus(Uminus(Sum(D,Integral(R,FunctionVar(w,x)),y)),O(Epsilon(1)))
		}
	}
	
	%strategy ChainRule() extends Identity(){
		visit Exp{
			Partial(Function(B,FunctionIndice(v,i,x)),VarIndice(x,i)) -> Plus(Function(B,Partial(FunctionIndice(v,i,x),VarIndice(x,i))),Mult(Div(CstInt(1),Epsilon(1)),Function(B,Partial(FunctionIndice(v,i,x),VarIndice(Var("y"),i))))) 
		}
	}
	
	%strategy Psi1() extends Identity(){
		visit Exp{
			Mult(Function(u,z),Function(B,x)) -> Mult(Function(u,z),Function(Var("T'"),x)) 
		}
	}
	
	%strategy Psi2() extends Identity(){
		visit Exp{
			Mult(Function(u,x),Mult(a,Function(B,w))) -> Mult(Function(u,x),Function(Var("T'"),Plus(Mult(a,w),Sum(Interval(CstInt(1),Var("n")),Mult(FunctionIndice(Var("y"),Var("j"),x),Partial(w,VarIndice(Var("x"),Var("j")))),Var("j")))))  
		}
	}
	
	%strategy Te() extends Identity(){
		visit Exp{
			Mult(Function(u,x),Function(t,z)) -> Function(Var("T"),Function(u,z))
		}
	}
	
	%strategy functionDoubleVar() extends Identity(){
		visit Exp{
			FunctionVar(a,b)->FunctionVar2(a,b,Var("y"))
		}
	}
	%strategy functionDouble() extends Identity(){
		visit Exp{
			Function(a,b)->FunctionList(a,concExp(b,Var("y")))
		}
	}
	%strategy DomInt() extends Identity(){
		visit Exp{
			Integral(Domain(a),b) -> Integral(Intersectiondomain("omega Tilde","Y"),b)
			}
	}
	
	%strategy zeroOrder() extends Identity(){
		visit Exp{
			Function(a,Function(c,d))-> Mult(Plus(FunctionExposant(c,CstInt(0)),O(Epsilon(1))),d)
		}
	}

}
