import basiclist.term.types.*;

public class BasicList {
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
    List l = `conc(a(),a(),b(),c(),a());
    System.out.println("l = " + l);

    //%match(l) {
    //  conc(_*,element@a(),_*) -> {
    //    System.out.println("found: " + `element); 
    //  }
    //}

    System.out.println(sort(l));
 
  }

  public static int monOrdre(Element e1, Element e2) {
    String s1 = e1.toString();
    String s2 = e2.toString();
    return s1.compareTo(s2);
  }

  public static List sort(List l) {
    %match(l) {
      conc(X*,e1,Y*,e2,Z*) -> {
        if(monOrdre(`e1,`e2) > 0) {
          return sort(`conc(X*,e2,Y*,e1,Z*));
        }
      }
    }
    return l;
  }


}
