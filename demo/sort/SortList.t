import sortlist.term.types.*;

public class SortList {
  %gom {
    module Term
    imports int String
    abstract syntax
    Element =
            | a()
            | b() 
            | c() 

    List = conc( Element* )
  }

public final static void main(String[] args) {
    List l1 = `conc(a(),b(),c(),a());
    System.out.println("l1 = " + l1);
    System.out.println("swap l1   = " + swapSort(l1));
    System.out.println("bubble l1 = " + bubbleSort(l1));
}

 public static List swapSort(List l) {
    %match(l) {
      conc(X1*,x,X2*,y,X3*) -> {
        if(`x.compareToLPO(`y) > 0) {
          return `swapSort(conc(X1*,y,X2*,x,X3*));
        }
      }
    }
    return l; 
}
 
public static List bubbleSort(List l) {
    %match(l) {
      conc(X1*,x,y,X3*) -> {
        if(`x.compareToLPO(`y) > 0) {
          return `bubbleSort(conc(X1*,y,x,X3*));
        }
      }
    }
    return l; 
  }
 
}
