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
    List l = `conc(a(),b(),c());
    System.out.println("l = " + l);

    %match(l) {
      conc(_,b(),_) -> { System.out.println("found: b"); }
    }
 
  }

}
