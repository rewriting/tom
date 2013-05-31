import formula.*;
import formula.types.*;
import java.util.*;
import java.io.FileWriter;

public class Pretty2 {

  %include { formula/formula.tom }

  /*
   * Pretty printer
   */
  public String pretty(Exp e) {
    %match(e) {
      CstInt(v) -> { return ""+`v; }
      Var(name) -> { return `name + ""; }
      Plus(e1,e2) -> { return pretty(`e1) + "+" + pretty(`e2); }
	  Minus(e1,e2) -> { return pretty(`e1) + "-" + pretty(`e2); }
      Mult(e1,e2*) -> { return pretty(`e1)  + (`e2.isEmptyMult()?"":("\\times " +pretty(`e2*))); }
      O(eps) -> { return "O(" + pretty(`eps) + ")"; }
      Index(body,indexname) -> { return "{"+pretty(`body)+"}" + "_{" + `indexname+"}"; }
      Function(symbol,args) -> { return pretty(`symbol) + "(" + pretty(`args) + ")"; }
      Partial(body,var) -> { return "\\frac{\\partial}{" + "\\partial " + pretty(`var)+"}("+pretty(`body)+")"; }
      Sum(domain,body,var) -> { return "\\sum\\limits_{ " + pretty(`var) + "\\in " + pretty(`domain) + "} " + pretty(`body); }
      Integral(domain,body,var) -> { return "\\int_{" + pretty(`domain) + "}" + pretty(`body) + " d" + pretty(`var); }
    }
    return "Exp undefined case: " + e;
  }

  public String pretty(ExpList e) {
    %match(e) {
      concExp() -> { return ""; }
      concExp(head,tail*) -> { return pretty(`head) + "," + pretty(`tail*); }
    }

    return "ExpList undefined case: " + e;
  }

  public String pretty(Epsilon e) {
    %match(e) {
      Epsilon(v) -> { return "\\epsilon_"+`v; }
    }

    return "Epsilon undefined case: " + e;
  }

  public String pretty(Region e) {
    %match(e) {
      Interval(e1,e2) -> { return "[" + pretty(`e1) + "," + pretty(`e2) + "]"; }
      Domain(name) -> { return `name; }
	  Boundary(name) -> { return "\\partial"+`name;}
    }

    return "Region undefined case: " + e;
  }

  public String pretty(Symbol e) {
    %match(e) {
      Symbol(name) -> { return `name; }
    }

    return "Symbol undefined case: " + e;
  }
  
  public String toLatex(String text, String expr){
    
	 return text+"\\\\ $$"+expr+"$$"; 
  
  }
  
  public void finalLatex(String text){
     
	 String txt = "\\documentclass{report}\\begin{document}"+text+"\\end{document}";
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
