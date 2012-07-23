package gen;

import definitions.*;
import sort.types.*;
import sort.types.intlist.*;

public class TestGen {

  %include {sort/Sort.tom}

  public static IntList reverse(IntList list){
    return append_reverse(list, `nill());
  }

  private static IntList append_reverse(IntList a, IntList b){
    %match(a) {
      nill() -> {return b;}
      consList(hd, tl) -> {return append_reverse(`tl, `consList(hd, b));}
    }
    return null;
  }

  public static void main(String[] args){
    IntList a = `consList(45,consList(34,consList(2, nill())));
    System.out.println(a);
    System.out.println(reverse(a));
  }
}
