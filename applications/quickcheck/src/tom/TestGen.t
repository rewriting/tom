package gen;

import definition.*;
import sort.type.*;
import sort.types.intlist.*;

public class TestGen {
  %include {sort/Sort.tom}
  public static IntList reverse(IntList list){
    %match()
  }
}
