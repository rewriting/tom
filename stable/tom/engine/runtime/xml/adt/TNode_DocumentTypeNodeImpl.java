package jtom.runtime.xml.adt;

abstract public class TNode_DocumentTypeNodeImpl
extends TNode
{
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  protected TNode_DocumentTypeNodeImpl(TNodeFactory factory) {
    super(factory);
  }
  private static int index_name = 0;
  private static int index_publicId = 1;
  private static int index_systemId = 2;
  private static int index_internalSubset = 3;
  private static int index_entities = 4;
  private static int index_notations = 5;
  public shared.SharedObject duplicate() {
    TNode_DocumentTypeNode clone = new TNode_DocumentTypeNode(factory);
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  public boolean equivalent(shared.SharedObject peer) {
    if (peer instanceof TNode_DocumentTypeNode) {
      return super.equivalent(peer);
    }
    return false;
  }
  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getTNodeFactory().makeTNode_DocumentTypeNode(fun, i_args, annos);
  }
  public aterm.ATerm toTerm() {
    if (term == null) {
      term = getTNodeFactory().toTerm(this);
    }
    return term;
  }

  public boolean isDocumentTypeNode()
  {
    return true;
  }

  public boolean hasName()
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

  public boolean hasInternalSubset()
  {
    return true;
  }

  public boolean hasEntities()
  {
    return true;
  }

  public boolean hasNotations()
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

  public String getInternalSubset()
  {
   return ((aterm.ATermAppl) this.getArgument(index_internalSubset)).getAFun().getName();
  }

  public TNode setInternalSubset(String _internalSubset)
  {
    return (TNode) super.setArgument(getFactory().makeAppl(getFactory().makeAFun(_internalSubset, 0, true)), index_internalSubset);
  }

  public TNodeList getEntities()
  {
    return (TNodeList) this.getArgument(index_entities) ;
  }

  public TNode setEntities(TNodeList _entities)
  {
    return (TNode) super.setArgument(_entities, index_entities);
  }

  public TNodeList getNotations()
  {
    return (TNodeList) this.getArgument(index_notations) ;
  }

  public TNode setNotations(TNodeList _notations)
  {
    return (TNode) super.setArgument(_notations, index_notations);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 0 of a TNode_DocumentTypeNode should have type str");
        }
        break;
      case 1:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 1 of a TNode_DocumentTypeNode should have type str");
        }
        break;
      case 2:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 2 of a TNode_DocumentTypeNode should have type str");
        }
        break;
      case 3:
        if (! (arg instanceof aterm.ATermAppl)) { 
          throw new RuntimeException("Argument 3 of a TNode_DocumentTypeNode should have type str");
        }
        break;
      case 4:
        if (! (arg instanceof TNodeList)) { 
          throw new RuntimeException("Argument 4 of a TNode_DocumentTypeNode should have type TNodeList");
        }
        break;
      case 5:
        if (! (arg instanceof TNodeList)) { 
          throw new RuntimeException("Argument 5 of a TNode_DocumentTypeNode should have type TNodeList");
        }
        break;
      default: throw new RuntimeException("TNode_DocumentTypeNode does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
}
