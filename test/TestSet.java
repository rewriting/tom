import aterm.*;
import aterm.pure.*;

import shared.*;

import jtom.runtime.set.SharedSet;

import java.util.Iterator;

public class TestSet {
  
  public static void main(String args[]) {
      // Basic test interface 0 (static like)
    PureFactory factory = new PureFactory();
    ATerm ok   = factory.parse("ok");
    ATerm fail   = factory.parse("fail");
    ATerm ok1   = factory.parse("ok1");
    ATerm ok2   = factory.parse("ok2");
      //System.out.println("ok Id:"+ok.getUniqueIdentifier()+"\nfail Id:"+fail.getUniqueIdentifier());
      //((SharedObjectWithID)ok).setUniqueIdentifier(fail.getUniqueIdentifier());

    SharedSet set = new SharedSet(factory);
    System.out.println(set);
    set.add(ok);
    System.out.println(set);
    
    SharedSet set2 = new SharedSet(factory);
    set2.add(ok);
    
    if(!set.equals(set2))
      System.out.println("No maximal Sharing of internal structures");

    set.add(fail);
    set.add(ok1);
    set.add(ok2);
    System.out.println(set.topRepartition());
    Iterator it = set.iterator();
    while(it.hasNext()) {
      System.out.println("Print: "+it.next());
    }
    
      /*
        if (set1 == set0)
      System.out.println("Maximal Sharing ok: "+set0);
      set0 = set.add(fail, set0);
      System.out.println(set0);
      
        // More complex tests
        boolean b = set.add(ok);
        if (set.getTreeSet() == set1)
        System.out.println("\nStill ok with second interface");
        System.out.println("set.add return:"+b);
        b = set.add(ok);
        System.out.println("set.add return then:"+b);
        
        b = set.remove(ok);
        if (set.getTreeSet() == set.makeEmptySet())
        System.out.println("Still ok with second interface");
        System.out.println("set.remove return:"+b);
        b = set.remove(ok);
        System.out.println("set.remove return then:"+b);
        
        b = set.add(ok);
        b = set.add(fail);
      */
  }
  
}
