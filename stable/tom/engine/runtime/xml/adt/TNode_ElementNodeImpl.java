package jtom.runtime.xml.adt;

abstract public class TNode_ElementNodeImpl
extends TNode
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TNode_ElementNodeImpl(TNodeFactory factory) {
    super(factory);
  }
  private static int index_name = 0;
  private static int index_attrList = 1;
  private static int index_childList = 2;
  public shared.SharedObject duplicate() {
    TNode_ElementNode clone = new TNode_ElementNode(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TNode_ElementNode) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTNodeFactory().makeTNode_ElementNode(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTNodeFactory().toTerm(this);
    }
    return term;
  }

  public boolean isElementNode()
  {
    return true;
  }

  public boolean hasName()
  {
    return true;
  }

  public boolean hasAttrList()
  {
    return true;
  }

  public boolean hasChildList()
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

  public TNodeList getAttrList()
  {
    return (TNodeList) this.getArgument(index_attrList) ;
  }

  public TNode setAttrList(TNodeList _attrList)
  {
    return (TNode) super.setArgument(_attrList, index_attrList);
  }

  public TNodeList getChildList()
  {
    return (TNodeList) this.getArgument(index_childList) ;
  }

  public TNode setChildList(TNodeList _childList)
  {
    return (TNode) super.setArgument(_childList, index_childList);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TNode_ElementNode should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof TNodeList)) { 
          throw new RuntimeException("Argument 1 of a TNode_ElementNode should have type TNodeList");
        }
        break;
      case 2:
        if (! (arg instanceof TNodeList)) { 
          throw new RuntimeException("Argument 2 of a TNode_ElementNode should have type TNodeList");
        }
        break;
      default: throw new RuntimeException("TNode_ElementNode does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
