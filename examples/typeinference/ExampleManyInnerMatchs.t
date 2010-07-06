import examplemanyinnermatchs.examplemanyinnermatchs.types.*;

public class ExampleManyInnerMatchs {
  %gom {
    module ExampleManyInnerMatchs 
      imports int
      abstract syntax
      
      B = b1()
        | b2()
        | f(n:B)
        | g(n:B)
  }

  public static void main(String[] args) {
    int e1 = 5;
    B e2 = `f(b1());
    B e3 = `f(b2());
    B e4 = `g(b2());

    %match { //match1
      x << int e1 -> {
        System.out.println("match1 : x = " + `x);
        %match { //match2
          f(y) << e2 -> { System.out.println("match2 : x = " + `x + " and y = " + `y); }
        }
      
        %match { //match3
          f(y) << e3 && (x < 10) -> { System.out.println("match3 : x = " + `x + " and y = " + `y); }
        }

        int x = 10;
        %match { //match4
          // Le "x" ici est le "x" du match1 et pas du java
          10 << x -> { System.out.println("match4 : x = " + `x); }
          //(x >= 10) -> { System.out.println("match4 : x = " + `x); }
        }
      }

      f(y) << e2 && g(y) << e4 -> { System.out.println("match5 : y = " + `y); }
    }
  }

}
