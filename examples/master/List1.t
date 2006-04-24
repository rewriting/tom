package master;

import master.list1.list.types.*;

public class List1 {
  %gom {
    // extension of adt syntax
    module List
    abstract syntax
    E = a()
      | b()
      | c()
    L = f( E* )
   }

  public L swapSort(L l) {
    %match(L l) {
      f(X*,e1,Z*,e2,Y*) -> {
        if(`gt(e1,e2)) {
          return `swapSort(f(X*,e2,Z*,e1,Y*));
          //System.out.println("("+`e1+","+`e2+")");
        }
      }
    }
    return l;
  }

  private boolean gt(E e1, E e2) {
    return e1.toString().compareTo(e2.toString()) > 0;
  }

  public L removeDouble(L l) {
    %match(L l) {
      f(X1*,x,x,X2*) -> {
        return removeDouble(`f(X1*,x,X2*));
      }
    }
    return l;
  }

  public void run() {
    L l = `f(a(),b(),c(),a(),b(),c(),a());
    L res1 = swapSort(l);
    L res2 = removeDouble(res1);
    System.out.println(" l       = " + l);
    System.out.println("sorted l = " + res1);
    System.out.println("single l = " + res2);

  }

  public final static void main(String[] args) {
    List1 test = new List1();
    test.run();
  }

}
