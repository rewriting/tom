package jtom.runtime.xml.adt;


abstract public class TNodeImpl extends TNodeConstructor
{
  protected TNodeImpl(TNodeFactory factory) {
     super(factory);
  }
  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  public boolean isEqual(TNode peer)
  {
    return super.isEqual(peer);
  }
  public boolean isSortTNode()  {
    return true;
  }

  public boolean isCommentNode()
  {
    return false;
  }

  public boolean isProcessingInstructionNode()
  {
    return false;
  }

  public boolean isTextNode()
  {
    return false;
  }

  public boolean isCDATASectionNode()
  {
    return false;
  }

  public boolean isDocumentNode()
  {
    return false;
  }

  public boolean isElementNode()
  {
    return false;
  }

  public boolean isAttributeNode()
  {
    return false;
  }

  public boolean isDocumentTypeNode()
  {
    return false;
  }

  public boolean isEntityReferenceNode()
  {
    return false;
  }

  public boolean isEntityNode()
  {
    return false;
  }

  public boolean isNotationNode()
  {
    return false;
  }

  public boolean hasData()
  {
    return false;
  }

  public boolean hasTarget()
  {
    return false;
  }

  public boolean hasDocType()
  {
    return false;
  }

  public boolean hasDocElem()
  {
    return false;
  }

  public boolean hasName()
  {
    return false;
  }

  public boolean hasAttrList()
  {
    return false;
  }

  public boolean hasChildList()
  {
    return false;
  }

  public boolean hasSpecified()
  {
    return false;
  }

  public boolean hasChild()
  {
    return false;
  }

  public boolean hasPublicId()
  {
    return false;
  }

  public boolean hasSystemId()
  {
    return false;
  }

  public boolean hasInternalSubset()
  {
    return false;
  }

  public boolean hasEntities()
  {
    return false;
  }

  public boolean hasNotations()
  {
    return false;
  }

  public boolean hasNotationName()
  {
    return false;
  }

  public String getData()
  {
     throw new UnsupportedOperationException("This TNode has no Data");
  }

  public TNode setData(String _data)
  {
     throw new IllegalArgumentException("Illegal argument: " + _data);
  }

  public String getTarget()
  {
     throw new UnsupportedOperationException("This TNode has no Target");
  }

  public TNode setTarget(String _target)
  {
     throw new IllegalArgumentException("Illegal argument: " + _target);
  }

  public TNode getDocType()
  {
     throw new UnsupportedOperationException("This TNode has no DocType");
  }

  public TNode setDocType(TNode _docType)
  {
     throw new IllegalArgumentException("Illegal argument: " + _docType);
  }

  public TNode getDocElem()
  {
     throw new UnsupportedOperationException("This TNode has no DocElem");
  }

  public TNode setDocElem(TNode _docElem)
  {
     throw new IllegalArgumentException("Illegal argument: " + _docElem);
  }

  public String getName()
  {
     throw new UnsupportedOperationException("This TNode has no Name");
  }

  public TNode setName(String _name)
  {
     throw new IllegalArgumentException("Illegal argument: " + _name);
  }

  public TNodeList getAttrList()
  {
     throw new UnsupportedOperationException("This TNode has no AttrList");
  }

  public TNode setAttrList(TNodeList _attrList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _attrList);
  }

  public TNodeList getChildList()
  {
     throw new UnsupportedOperationException("This TNode has no ChildList");
  }

  public TNode setChildList(TNodeList _childList)
  {
     throw new IllegalArgumentException("Illegal argument: " + _childList);
  }

  public String getSpecified()
  {
     throw new UnsupportedOperationException("This TNode has no Specified");
  }

  public TNode setSpecified(String _specified)
  {
     throw new IllegalArgumentException("Illegal argument: " + _specified);
  }

  public TNode getChild()
  {
     throw new UnsupportedOperationException("This TNode has no Child");
  }

  public TNode setChild(TNode _child)
  {
     throw new IllegalArgumentException("Illegal argument: " + _child);
  }

  public String getPublicId()
  {
     throw new UnsupportedOperationException("This TNode has no PublicId");
  }

  public TNode setPublicId(String _publicId)
  {
     throw new IllegalArgumentException("Illegal argument: " + _publicId);
  }

  public String getSystemId()
  {
     throw new UnsupportedOperationException("This TNode has no SystemId");
  }

  public TNode setSystemId(String _systemId)
  {
     throw new IllegalArgumentException("Illegal argument: " + _systemId);
  }

  public String getInternalSubset()
  {
     throw new UnsupportedOperationException("This TNode has no InternalSubset");
  }

  public TNode setInternalSubset(String _internalSubset)
  {
     throw new IllegalArgumentException("Illegal argument: " + _internalSubset);
  }

  public TNodeList getEntities()
  {
     throw new UnsupportedOperationException("This TNode has no Entities");
  }

  public TNode setEntities(TNodeList _entities)
  {
     throw new IllegalArgumentException("Illegal argument: " + _entities);
  }

  public TNodeList getNotations()
  {
     throw new UnsupportedOperationException("This TNode has no Notations");
  }

  public TNode setNotations(TNodeList _notations)
  {
     throw new IllegalArgumentException("Illegal argument: " + _notations);
  }

  public String getNotationName()
  {
     throw new UnsupportedOperationException("This TNode has no NotationName");
  }

  public TNode setNotationName(String _notationName)
  {
     throw new IllegalArgumentException("Illegal argument: " + _notationName);
  }

}

