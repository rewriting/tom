package jtom.runtime.xml.adt;

abstract public class TNode_NotationNodeImpl
extends TNode
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TNode_NotationNodeImpl(TNodeFactory factory) {
    super(factory);
  }
  private static int index_publicId = 0;
  private static int index_systemId = 1;
  public shared.SharedObject duplicate() {
    TNode_NotationNode clone = new TNode_NotationNode(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TNode_NotationNode) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTNodeFactory().makeTNode_NotationNode(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTNodeFactory().toTerm(this);
    }
    return term;
  }

  public boolean isNotationNode()
  {
    return true;
  }

  public boolean hasPublicId()
  {
    return true;
  }

  public boolean hasSystemId()
  {
    return true;
  }

  public String getPublicId()
  {
   return ((aterm.ATermAppl) this.getArgument(index_publicId)).getAFun().getName();
  }

  public TNode setPublicId(String _publicId)
  {
    return (TNode) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_publicId, 0, true)), index_publicId);
  }

  public String getSystemId()
  {
   return ((aterm.ATermAppl) this.getArgument(index_systemId)).getAFun().getName();
  }

  public TNode setSystemId(String _systemId)
  {
    return (TNode) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_systemId, 0, true)), index_systemId);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TNode_NotationNode should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 1 of a TNode_NotationNode should have type str");
        }
        break;
      default: throw new RuntimeException("TNode_NotationNode does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
