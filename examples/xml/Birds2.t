import xtom.adt.*;
import xtom.tools.XTomTools;
import aterm.*;
import jtom.runtime.*;

public class Birds2 {
  
  %include{ NodeTerm.tom }
    
  private XTomTools xtools;
  private GenericTraversal traversal = new GenericTraversal();
  private NodeTermFactory getNodeTermFactory() {
      return xtools.getNodeTermFactory();
  }

  public static void main (String args[]) {
    Birds2 birds = new Birds2();
    birds.run("birds.xml");
  }

  private void run(String filename){
    xtools = new XTomTools();
    ATerm term = xtools.convertXMLToATerm(filename);
    term = replace(term);
      //xtools.printXMLFromATerm(term);
  }
    
  private ATerm replace(ATerm subject) {
    Replace1 replace = new Replace1 () {
	public ATerm apply(ATerm t) {
	    if (t instanceof NodeTerm) {
		%match(NodeTerm t) {
		    <Species [Scientific_Name=scname]>[#TEXT(data)]</Species> -> {
                      System.out.println("catched birds '" + data + "'");
                    }

		    <Species [Scientific_Name=scname]>[#TEXT("Yellow-billed Loon.")]</Species> -> {
                      System.out.println("Bingo catched bird");
                    }

		    <Species [Scientific_Name="Gavia adamsii"]>["Yellow-billed Loon."]</Species> -> {
                      System.out.println("Double bingo catched bird");
                    }


                    _ -> {
			return traversal.genericTraversal(t,this);
		    }
		} // match 
	    } else 
		return traversal.genericTraversal(t,this);
	} //apply
	};
    return replace.apply(subject);
    }
}
