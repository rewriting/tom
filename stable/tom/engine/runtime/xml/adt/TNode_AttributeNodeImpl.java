package jtom.runtime.xml.adt;

abstract public class TNode_AttributeNodeImpl
extends TNode
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TNode_AttributeNodeImpl(TNodeFactory factory) {
    super(factory);
  }
  private static int index_name = 0;
  private static int index_specified = 1;
  private static int index_child = 2;
  public shared.SharedObject duplicate() {
    TNode_AttributeNode clone = new TNode_AttributeNode(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TNode_AttributeNode) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTNodeFactory().makeTNode_AttributeNode(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTNodeFactory().toTerm(this);
    }
    return term;
  }

  public boolean isAttributeNode()
  {
    return true;
  }

  public boolean hasName()
  {
    return true;
  }

  public boolean hasSpecified()
  {
    return true;
  }

  public boolean hasChild()
  {
    return true;
  }

  public String getName()
  {
   return ((aterm.ATermAppl) this.getArgument(index_name)).getAFun().getName();
  }

  public TNode setName(String _name)
  {
    return (TNode) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_name, 0, true)), index_name);
  }

  public String getSpecified()
  {
   return ((aterm.ATermAppl) this.getArgument(index_specified)).getAFun().getName();
  }

  public TNode setSpecified(String _specified)
  {
    return (TNode) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_specified, 0, true)), index_specified);
  }

  public TNode getChild()
  {
    return (TNode) this.getArgument(index_child) ;
  }

  public TNode setChild(TNode _child)
  {
    return (TNode) super.setArgument(_child, index_child);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TNode_AttributeNode should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 1 of a TNode_AttributeNode should have type str");
        }
        break;
      case 2:
        if (! (arg instanceof TNode)) { 
          throw new RuntimeException("Argument 2 of a TNode_AttributeNode should have type TNode");
        }
        break;
      default: throw new RuntimeException("TNode_AttributeNode does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
