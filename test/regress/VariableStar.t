import variablestar.bug.types.*;
public class VariableStar {
  %gom {
    module Bug
    imports int String
    abstract syntax
    T = a()
      | f(x:T)

    L = conc(T*)
  }

  public static void main(String[] args) {
    L t = `conc(a());
    %match {
      _* << t -> { System.out.println("x: " + `x); }
      conc(_*) << t -> { System.out.println("x: " + `x); }
    }
  }

}
