package xml;

import org.w3c.dom.*;
import tom.library.xml.*;

public class EmptyXml {

  %include{ dom.tom }
  private static Document dom;

  public static void main (String args[]) {
    System.out.println(`xml(dom,<A></A>));
  }

}
