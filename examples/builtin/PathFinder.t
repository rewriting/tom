public class PathFinder {

%include {charlist.tom}

  public final static void main(String[] args) {
    PathFinder test = new PathFinder();
    
    test.f("hello");

    String s = "aaaabaaaabaaaabaaaabaaaabaaaabaaaabaaabaa";
    //test.f2(s);
  }

    
  public String f(String s) {
    %match(String s) {
      ("he",X1*,x@"l",y@"o",_*) -> {
        System.out.println("X1   = " + X1);
        System.out.println("char = " + x);
        System.out.println("char = " + y);
        return s;
      }
      _        -> { return "unknown"; }
    }
  }
  

  /*
  public String f2(String s) {
    %match(String s) {
      (X1*,"b",X2*) -> {
        return `concString("b",f2(concString(X1*,X2*)));
      }
      _ -> { return s; }
    }
  }
*/
  
  int cpt=0;
  public String f2(String s) {
    %match(String s) {
      
      (X1*,"b",X2*,"b",X3*,"b",X4*,"b",X5*,"b",X6*,"b",X7*) -> {
      System.out.println(`X1 + " " + `X2 + " " + `X3 + " " + `X4 + " " + `X5 + " " + `X6 + " " + `X7);
      }
      //(X*,"b",_*,"b",_*,"b",_*,"b",_*,"b",_*,"b",_*) -> {
      // System.out.println("bingo " + cpt++);
      //}
      
    }
    return s;
  }
  
}
