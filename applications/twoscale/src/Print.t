import formule.*;
import formule.types.*;
import java.util.*;
import java.io.FileWriter;

public class Print {
	
	%include {../gen/formule/formule.tom}
	
	
	public String printer(Exp e){
		%match(e){
			CstInt(v) -> { return ""+`v; }
			Var(name) -> { return `name + ""; }
			Plus(e1,e2) -> { return "(" + printer(`e1)  + "+" + printer(`e2) + ")"; }
			Minus(e1,e2) -> { return printer(`e1) + "-" + printer(`e2); }
			Div(e1,e2) -> {return printer(`e1) + "/" + printer(`e2);}
			Uminus(e1) -> { return "-"+printer(`e1); }
			Mult(e1,e2) -> { return printer(`e1) + "*" + printer(`e2); }
			Partial(body,var) -> { return "\\frac{\\partial}{" + "\\partial " + printer(`var)+"}("+printer(`body)+")"; }
			Sum(domaine,mainExp,variable) -> { return "\\sum\\limits_{ " + printer(`variable) + "\\in " + printer(`domaine) + "} " + printer(`mainExp); }
			Integral(domaine, mainExp) -> { return "\\int_{" + printer(`domaine) + "}" + printer(`mainExp); }
			FunctionVar(e1,var) -> { return printer(`e1) + "d" + printer(`var);}
			FunctionVar2(e1,var1,var2) -> { return printer(`e1)  + "d" + printer(`var1) + "d" + printer(`var2);}
			FunctionList(symbol, args) -> { return printer(`symbol) + "(" + printer(`args) + ")" ;}
			Function(symbol, arg) -> { return printer(`symbol) + "(" + printer(`arg) + ")" ;}
			FunctionIndice(symbol, indice, arg) -> { return printer(`symbol) + "_" + printer(`indice) + "(" + printer(`arg) + ")" ;}
			FunctionExposant(symbol, exposant ) ->{ return printer(`symbol)+ "^"+printer(`exposant);}
			VarIndice(var,indice) -> {return printer(`var) + "_" + printer(`indice);}
			IndiceGreenRule(symbol,variable,indice) ->{return "("+printer (`symbol)+"_"+printer(`variable)+")"+"_"+printer (`indice);}
 			O(oname) -> {return "\\O{" + "(" + printer(`oname) + ")" +"}"; }
			Epsilon(number) -> { return "\\epsilon " + "(" + `number + ")"; }
		}
		return "Exp undefined case: " + e;
	}
	
	public String printer(ExpList e){
		%match(e){
			concExp() -> { return ""; }
			concExp(head,tail*) -> { return printer(`head)+","+printer(`tail*); }
		}
		return "ExpList undefined case: " + e;
	}
	
	public String printer(Region e){
		%match(e) {
			Interval(e1,e2) -> { return   printer(`e1) + "," + printer(`e2); }
			Domain(name) -> { return `name; }
			Intersectiondomain(name1, name2) -> { return `name1 +" "+ " X " +" "+ `name2 ;}
			PartialDomain(name) -> {return "\\partial " + `name; }
			}
		return "Region undefined case: " + e;
	}

	
	public String toLatex(String text, String expr){
		return text+"\\\\ $$"+expr+"$$"; 
	}
	
	public void finalLatex(String text){
		String txt = "\\documentclass{report}\\usepackage[top=1cm, bottom=1cm,right=1cm,left=1cm]{geometry}\\begin{document}"+text+"\\end{document}";
		try{
			FileWriter out = new FileWriter("result.tex");
			out.write(txt);
			out.close();
		}
		catch(Exception e){
			System.out.println("Writing error");
		}
	}
	

	
}
