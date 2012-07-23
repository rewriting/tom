package gen;

import definitions.*;
import sort.types.*;
import sort.types.intlist.*;
import sort.types.leaf.*;
import aterm.*;

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
    IntList a = `consList(a(),consList(b(),consList(c(), nill())));
    System.out.println(a);
    System.out.println(reverse(a));

    Scope scope = new Scope();

    SortGom leaf = new SortGom(scope, Leaf.class);
    SortGom list = new SortGom(scope, IntList.class);
    list.addConstructor(nill.class);
    list.addConstructor(consList.class);
    leaf.addConstructor("a");
    leaf.addConstructor("b");
    leaf.addConstructor("c");

    scope.setDependances();
    for(int i = 0; i<10; i++){
      ATerm term = list.generate(10);
      IntList rand = IntList.fromTerm(term);
      System.out.println(rand.equals(reverse(reverse(rand))));
    }
    
  }
}
