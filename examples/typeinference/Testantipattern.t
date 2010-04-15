import testantipattern.testantipattern.types.*;
public class Testantipattern {
  %gom {
    module Testantipattern
      imports int
      abstract syntax

      B = conc(int*)
  }

  public static void main(String[] args) {
    B list = `conc(1,2,3);
    B emptyList = `conc();
    %match{
      conc(_*,!3,_*) << B list -> { System.out.println("ligne 1 = " + `list); }
      !conc(_*,3,_*) << B list -> { System.out.println("ligne 2 = " + `list); }
      !conc(_*,3,_*) << B emptyList -> { System.out.println("ligne 3 = " + `list); }
    }
  }
}


