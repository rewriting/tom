import jtom.runtime.xml.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;
import jtom.runtime.*;
import java.util.*;

public class FibXml {
  
  %include{ TNode.tom }
    
  private XmlTools xtools;

  private Factory getTNodeFactory() {
    return xtools.getTNodeFactory();
  }

  public static void main (String args[]) {
    FibXml test = new FibXml();
    test.run();
  }

  private TNode xml(TNode t) {
    return t;
  }
  
  public void run() {
    xtools = new XmlTools();


    for(int i=0 ; i<100 ; i++) {
      TNode N = int2peano(i);
      assertTrue( peano2int(plus(N,N)) == (i+i) );
    }

    for(int i=0 ; i<18 ; i++) {
      TNode N = int2peano(i);
      System.out.println("fib( " + i + ") = " + peano2int(fib(N)));
      //System.out.println("fibint = " + fibint(i));
      assertTrue( peano2int(fib(N)) == fibint(i) );
    }
    
  }

  TNode plus(TNode t1, TNode t2) {
    %match(TNode t1, TNode t2) {
      x,<zero/>    -> { return x; }
      x,<s>(y)</s> -> { return `xml(<s>plus(x,y)</s>); }
    }
    return null;
  }

  TNode fib(TNode t) {
    %match(TNode t) {
      <zero/>              -> { return `xml(<s><zero/></s>); }
      <s>(<zero/>)</s>     -> { return `xml(<s><zero/></s>); }
      <s>(<s>(x)</s>)</s>  -> { return plus(fib(x),fib(`xml(<s>x</s>))); }
    }
    return null;
  }

  public TNode int2peano(int n) {
    TNode N = `xml(<zero/>);
    for(int i=0 ; i<n ; i++) {
      N = `xml(<s>N</s>);
    }
    return N;
  }

  public int peano2int(TNode N) {
    %match(TNode N) {
      <zero/>    -> { return 0; }
      <s>(x)</s> -> {return 1+peano2int(x); }
    }
    return 0;
  }

  public int fibint(int n) {
    if(n<=1) {
      return 1;
    } else {
      return fibint(n-1)+fibint(n-2);
    }
  }

  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }


}
