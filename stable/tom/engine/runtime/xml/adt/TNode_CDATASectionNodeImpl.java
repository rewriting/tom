package jtom.runtime.xml.adt;

abstract public class TNode_CDATASectionNodeImpl
extends TNode
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TNode_CDATASectionNodeImpl(TNodeFactory factory) {
    super(factory);
  }
  private static int index_data = 0;
  public shared.SharedObject duplicate() {
    TNode_CDATASectionNode clone = new TNode_CDATASectionNode(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TNode_CDATASectionNode) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTNodeFactory().makeTNode_CDATASectionNode(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTNodeFactory().toTerm(this);
    }
    return term;
  }

  public boolean isCDATASectionNode()
  {
    return true;
  }

  public boolean hasData()
  {
    return true;
  }

  public String getData()
  {
   return ((aterm.ATermAppl) this.getArgument(index_data)).getAFun().getName();
  }

  public TNode setData(String _data)
  {
    return (TNode) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_data, 0, true)), index_data);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TNode_CDATASectionNode should have type str");
        }
        break;
      default: throw new RuntimeException("TNode_CDATASectionNode does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
