import jtom.runtime.xml.*;
import jtom.runtime.xml.adt.tnode.*;
import jtom.runtime.xml.adt.tnode.types.*;
import aterm.*;

public class BackQuote {
  
  %include{ TNode.tom }
    
  private XmlTools xtools;
 
  private Factory getTNodeFactory() {
      return xtools.getTNodeFactory();
  }

  public static void main (String args[]) {
    BackQuote birds = new BackQuote();
    birds.run();
  }

  private TNode xml(TNode t) {
    return t;
  }
  
  private void run() {
    xtools = new XmlTools();
    TNode t;
    String x="goo";
    
      //t = ``xml(<A> <B/> </A>);
      //t = ``xml(<A at1="foo" at2=x at3=dd("text")/>);

    t = `XML(<A at1="x"><B at2="foo"/> </A>);
    
    System.out.println("t = " + t);
    
  }

    public String dd(String x) {
      return x+x;
    }
}
