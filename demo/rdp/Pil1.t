import pil1.term.types.*;

class Pil1 {
 
  %gom {
    module Term
    imports String
    abstract syntax   
    Expr = 
         | Var(name:String) 
         | Let(var:Expr, e:Expr, body:Expr) 
         | a()
         | b()
  }

  public final static void main(String[] args) {
    Expr prg = `Let(Var("x"),a(), Var("y"));
    System.out.println("prg = " + prg);
    System.out.println(pretty(prg));  
  }
  
  ///* 
  public static String pretty(Object e) {
    %match(e) {
      Var(name) -> { return `name; }
      Let(var,expr,body) -> { return "let " + pretty(`var) +
                                     " <- " + pretty(`expr) + 
                                     " in " + pretty(`body); }
    }     
    return e.toString();
  }
  //*/
}

/*
 * show error
 * String: "x"+"y"
 * pretty
 */