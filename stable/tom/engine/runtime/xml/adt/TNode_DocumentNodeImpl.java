package jtom.runtime.xml.adt;

abstract public class TNode_DocumentNodeImpl
extends TNode
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TNode_DocumentNodeImpl(TNodeFactory factory) {
    super(factory);
  }
  private static int index_docType = 0;
  private static int index_docElem = 1;
  public shared.SharedObject duplicate() {
    TNode_DocumentNode clone = new TNode_DocumentNode(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TNode_DocumentNode) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTNodeFactory().makeTNode_DocumentNode(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTNodeFactory().toTerm(this);
    }
    return term;
  }

  public boolean isDocumentNode()
  {
    return true;
  }

  public boolean hasDocType()
  {
    return true;
  }

  public boolean hasDocElem()
  {
    return true;
  }

  public TNode getDocType()
  {
    return (TNode) this.getArgument(index_docType) ;
  }

  public TNode setDocType(TNode _docType)
  {
    return (TNode) super.setArgument(_docType, index_docType);
  }

  public TNode getDocElem()
  {
    return (TNode) this.getArgument(index_docElem) ;
  }

  public TNode setDocElem(TNode _docElem)
  {
    return (TNode) super.setArgument(_docElem, index_docElem);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof TNode)) { 
          throw new RuntimeException("Argument 0 of a TNode_DocumentNode should have type TNode");
        }
        break;
      case 1:
        if (! (arg instanceof TNode)) { 
          throw new RuntimeException("Argument 1 of a TNode_DocumentNode should have type TNode");
        }
        break;
      default: throw new RuntimeException("TNode_DocumentNode does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
    a += (getArgument(1).hashCode() << 8);
    a += (getArgument(0).hashCode() << 0);

    a -= b; a -= c; a ^= (c >> 13);
    b -= c; b -= a; b ^= (a << 8);
    c -= a; c -= b; c ^= (b >> 13);
    a -= b; a -= c; a ^= (c >> 12);
    b -= c; b -= a; b ^= (a << 16);
    c -= a; c -= b; c ^= (b >> 5);
    a -= b; a -= c; a ^= (c >> 3);
    b -= c; b -= a; b ^= (a << 10);
    c -= a; c -= b; c ^= (b >> 15);

    return c;
  }
}
