//Source file: C:\\document\\codegen\\xquery\\lib\\data\\Item.java

package xquery.lib.data;

import org.w3c.dom.*;

public class Item extends Sequence 
{
  protected Node node;
  protected Atom atom;


  public Item(Node node) 
  {
	this.node =node; 
  }
   
  public Item(Atom atom) 
  {
	this.atom = atom; 
  }

  /**
   * @roseuid 4110B675033E
   */
  protected Item() 
  {
	node=null;
	atom=null;
  }
   
  /**
   * @return boolean
   * @roseuid 410F87F6018A
   */
  public boolean isNode() 
  {
    if (node!=null) {
	  return true;
	}
	else {
	  return false;
	}
  }
   
  /**
   * @return boolean
   * @roseuid 410F8816008B
   */
  public boolean isAtom() 
  {
	if (atom!=null) {
	  return true;
	}
	else {
	  return false;
	}

  }
   
  /**
   * @return org.w3c.dom.Node
   * @roseuid 410F88410123
   */
  public Node getNode() 
  {
    return node;
  }
   
  /**
   * @return xquery.lib.data.Atom
   * @roseuid 410F8881014D
   */
  public Atom getAtom() 
  {
    return atom;
  }
   
  /**
   * @param o
   * @return boolean
   * @roseuid 41115FF50155
   */
  public boolean add(Object o) 
  {
	if (this.atom !=null) {
	  super.add(atom); 
	  atom=null;
	}

	if (this.node !=null) {
	  super.add(node); 
	  node=null;
	}

	super.add(o);
    return true;
  }
   
  /**
   * @param s
   * @return xquery.lib.data.Sequence
   * @roseuid 4111600902C6
   */
  public Sequence add(Sequence s) 
  {
	if (this.atom!=null) {
	  super.add(atom); 
	  atom=null;

	}

	if (this.node!=null) {
	  super.add(node); 
	  node=null;
	}

	super.add(s);
    return null;
  }

  public static Item convertSequenceToItem(Sequence s)
	throws XQueryGeneralException
  {
	if (s.size()!=1) {
	  throw new XQueryTypeException("Error when convert Sequence to Item. Sequence's element count > 1");
	}
	
	Object o = s.elementAt(0);
	if (o instanceof Item) {
	  return (Item)o; 
	}
	else if (o instanceof Atom) {
	  return new Item((Atom)o);
	}
	else if (o instanceof Node) {
	  return new Item((Node)o);
	}
	else {
	  throw new XQueryTypeException("Error when convert Sequence to Item. Sequence's first element isn't Node, Atom or Item");
	}
  }


}
