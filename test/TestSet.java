import aterm.*;
import aterm.pure.*;

import shared.*;

import jtom.runtime.set.SharedSet;
import jtom.runtime.set.jgtreeset.JGTreeSet;

public class TestSet {
  
  public static void main(String args[]) {
      // Basic test interface 0 (static like)
    PureFactory factory = new PureFactory();
    ATerm ok   = factory.parse("ok");
    ATerm fail   = factory.parse("fail");
    System.out.println("ok Id:"+ok.getUniqueIdentifier()+"\nfail Id:"+fail.getUniqueIdentifier());
      //((SharedObjectWithID)ok).setUniqueIdentifier(fail.getUniqueIdentifier());
    
    SharedSet set = new SharedSet(factory);
    JGTreeSet set0 = set.makeEmptySet();
    System.out.println("EmptySet is: "+set0);
    JGTreeSet set1 = set.makeSingleton(ok);
    System.out.println("Singleton is: "+set1);
    set0 = set.add(ok, set0);
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
  }
  
}
