package jtom.runtime.xml.adt;

import aterm.pure.PureFactory;
public class TNodeFactory
{
  private PureFactory factory;
  private aterm.AFun funTNode_CommentNode;
  private TNode protoTNode_CommentNode;
  private aterm.ATerm patternTNode_CommentNode;
  private aterm.AFun funTNode_ProcessingInstructionNode;
  private TNode protoTNode_ProcessingInstructionNode;
  private aterm.ATerm patternTNode_ProcessingInstructionNode;
  private aterm.AFun funTNode_TextNode;
  private TNode protoTNode_TextNode;
  private aterm.ATerm patternTNode_TextNode;
  private aterm.AFun funTNode_CDATASectionNode;
  private TNode protoTNode_CDATASectionNode;
  private aterm.ATerm patternTNode_CDATASectionNode;
  private aterm.AFun funTNode_DocumentNode;
  private TNode protoTNode_DocumentNode;
  private aterm.ATerm patternTNode_DocumentNode;
  private aterm.AFun funTNode_ElementNode;
  private TNode protoTNode_ElementNode;
  private aterm.ATerm patternTNode_ElementNode;
  private aterm.AFun funTNode_AttributeNode;
  private TNode protoTNode_AttributeNode;
  private aterm.ATerm patternTNode_AttributeNode;
  private aterm.AFun funTNode_DocumentTypeNode;
  private TNode protoTNode_DocumentTypeNode;
  private aterm.ATerm patternTNode_DocumentTypeNode;
  private aterm.AFun funTNode_EntityReferenceNode;
  private TNode protoTNode_EntityReferenceNode;
  private aterm.ATerm patternTNode_EntityReferenceNode;
  private aterm.AFun funTNode_EntityNode;
  private TNode protoTNode_EntityNode;
  private aterm.ATerm patternTNode_EntityNode;
  private aterm.AFun funTNode_NotationNode;
  private TNode protoTNode_NotationNode;
  private aterm.ATerm patternTNode_NotationNode;
  private TNodeList protoTNodeList;
  private aterm.ATerm patternTNodeListMany;
  private TNodeList emptyTNodeList;
  public TNodeFactory(PureFactory factory)
  {
     this.factory = factory;
     initialize();
  }
  public PureFactory getPureFactory()
  {
    return factory;
  }
  private void initialize()
  {

    patternTNode_CommentNode = factory.parse("CommentNode(<str>)");
    funTNode_CommentNode = factory.makeAFun("_TNode_CommentNode", 1, false);
    protoTNode_CommentNode = new TNode_CommentNode(this);

    patternTNode_ProcessingInstructionNode = factory.parse("ProcessingInstructionNode(<str>,<str>)");
    funTNode_ProcessingInstructionNode = factory.makeAFun("_TNode_ProcessingInstructionNode", 2, false);
    protoTNode_ProcessingInstructionNode = new TNode_ProcessingInstructionNode(this);

    patternTNode_TextNode = factory.parse("TextNode(<str>)");
    funTNode_TextNode = factory.makeAFun("_TNode_TextNode", 1, false);
    protoTNode_TextNode = new TNode_TextNode(this);

    patternTNode_CDATASectionNode = factory.parse("CDATASectionNode(<str>)");
    funTNode_CDATASectionNode = factory.makeAFun("_TNode_CDATASectionNode", 1, false);
    protoTNode_CDATASectionNode = new TNode_CDATASectionNode(this);

    patternTNode_DocumentNode = factory.parse("DocumentNode(<term>,<term>)");
    funTNode_DocumentNode = factory.makeAFun("_TNode_DocumentNode", 2, false);
    protoTNode_DocumentNode = new TNode_DocumentNode(this);

    patternTNode_ElementNode = factory.parse("ElementNode(<str>,<term>,<term>)");
    funTNode_ElementNode = factory.makeAFun("_TNode_ElementNode", 3, false);
    protoTNode_ElementNode = new TNode_ElementNode(this);

    patternTNode_AttributeNode = factory.parse("AttributeNode(<str>,<str>,<term>)");
    funTNode_AttributeNode = factory.makeAFun("_TNode_AttributeNode", 3, false);
    protoTNode_AttributeNode = new TNode_AttributeNode(this);

    patternTNode_DocumentTypeNode = factory.parse("DocumentTypeNode(<str>,<str>,<str>,<str>,<term>,<term>)");
    funTNode_DocumentTypeNode = factory.makeAFun("_TNode_DocumentTypeNode", 6, false);
    protoTNode_DocumentTypeNode = new TNode_DocumentTypeNode(this);

    patternTNode_EntityReferenceNode = factory.parse("EntityReferenceNode(<str>,<term>)");
    funTNode_EntityReferenceNode = factory.makeAFun("_TNode_EntityReferenceNode", 2, false);
    protoTNode_EntityReferenceNode = new TNode_EntityReferenceNode(this);

    patternTNode_EntityNode = factory.parse("EntityNode(<str>,<str>,<str>)");
    funTNode_EntityNode = factory.makeAFun("_TNode_EntityNode", 3, false);
    protoTNode_EntityNode = new TNode_EntityNode(this);

    patternTNode_NotationNode = factory.parse("NotationNode(<str>,<str>)");
    funTNode_NotationNode = factory.makeAFun("_TNode_NotationNode", 2, false);
    protoTNode_NotationNode = new TNode_NotationNode(this);

    protoTNodeList = new TNodeList(this);
    protoTNodeList.init(84, null, null, null);
    emptyTNodeList = (TNodeList) factory.build(protoTNodeList);
    emptyTNodeList.init(84, emptyTNodeList, null, null);

  }
  protected TNode_CommentNode makeTNode_CommentNode(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTNode_CommentNode) {
      protoTNode_CommentNode.initHashCode(annos,fun,args);
      return (TNode_CommentNode) factory.build(protoTNode_CommentNode);
    }
  }

  public TNode_CommentNode makeTNode_CommentNode(String _data) {
    aterm.ATerm[] args = new aterm.ATerm[] {factory.makeAppl(factory.makeAFun(_data, 0, true))};
    return makeTNode_CommentNode(funTNode_CommentNode, args, factory.getEmpty());
  }

  public TNode TNode_CommentNodeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTNode_CommentNode);

    if (children != null) {
      TNode tmp = makeTNode_CommentNode((String) children.get(0));
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TNode_CommentNodeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(arg.getData());    return factory.make(patternTNode_CommentNode, args);
  }

  protected TNode_ProcessingInstructionNode makeTNode_ProcessingInstructionNode(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTNode_ProcessingInstructionNode) {
      protoTNode_ProcessingInstructionNode.initHashCode(annos,fun,args);
      return (TNode_ProcessingInstructionNode) factory.build(protoTNode_ProcessingInstructionNode);
    }
  }

  public TNode_ProcessingInstructionNode makeTNode_ProcessingInstructionNode(String _target, String _data) {
    aterm.ATerm[] args = new aterm.ATerm[] {factory.makeAppl(factory.makeAFun(_target, 0, true)), factory.makeAppl(factory.makeAFun(_data, 0, true))};
    return makeTNode_ProcessingInstructionNode(funTNode_ProcessingInstructionNode, args, factory.getEmpty());
  }

  public TNode TNode_ProcessingInstructionNodeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTNode_ProcessingInstructionNode);

    if (children != null) {
      TNode tmp = makeTNode_ProcessingInstructionNode((String) children.get(0), (String) children.get(1));
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TNode_ProcessingInstructionNodeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(arg.getTarget());    args.add(arg.getData());    return factory.make(patternTNode_ProcessingInstructionNode, args);
  }

  protected TNode_TextNode makeTNode_TextNode(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTNode_TextNode) {
      protoTNode_TextNode.initHashCode(annos,fun,args);
      return (TNode_TextNode) factory.build(protoTNode_TextNode);
    }
  }

  public TNode_TextNode makeTNode_TextNode(String _data) {
    aterm.ATerm[] args = new aterm.ATerm[] {factory.makeAppl(factory.makeAFun(_data, 0, true))};
    return makeTNode_TextNode(funTNode_TextNode, args, factory.getEmpty());
  }

  public TNode TNode_TextNodeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTNode_TextNode);

    if (children != null) {
      TNode tmp = makeTNode_TextNode((String) children.get(0));
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TNode_TextNodeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(arg.getData());    return factory.make(patternTNode_TextNode, args);
  }

  protected TNode_CDATASectionNode makeTNode_CDATASectionNode(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTNode_CDATASectionNode) {
      protoTNode_CDATASectionNode.initHashCode(annos,fun,args);
      return (TNode_CDATASectionNode) factory.build(protoTNode_CDATASectionNode);
    }
  }

  public TNode_CDATASectionNode makeTNode_CDATASectionNode(String _data) {
    aterm.ATerm[] args = new aterm.ATerm[] {factory.makeAppl(factory.makeAFun(_data, 0, true))};
    return makeTNode_CDATASectionNode(funTNode_CDATASectionNode, args, factory.getEmpty());
  }

  public TNode TNode_CDATASectionNodeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTNode_CDATASectionNode);

    if (children != null) {
      TNode tmp = makeTNode_CDATASectionNode((String) children.get(0));
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TNode_CDATASectionNodeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(arg.getData());    return factory.make(patternTNode_CDATASectionNode, args);
  }

  protected TNode_DocumentNode makeTNode_DocumentNode(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTNode_DocumentNode) {
      protoTNode_DocumentNode.initHashCode(annos,fun,args);
      return (TNode_DocumentNode) factory.build(protoTNode_DocumentNode);
    }
  }

  public TNode_DocumentNode makeTNode_DocumentNode(TNode _docType, TNode _docElem) {
    aterm.ATerm[] args = new aterm.ATerm[] {_docType, _docElem};
    return makeTNode_DocumentNode(funTNode_DocumentNode, args, factory.getEmpty());
  }

  public TNode TNode_DocumentNodeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTNode_DocumentNode);

    if (children != null) {
      TNode tmp = makeTNode_DocumentNode(TNodeFromTerm( (aterm.ATerm) children.get(0)), TNodeFromTerm( (aterm.ATerm) children.get(1)));
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TNode_DocumentNodeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add((arg.getDocType()).toTerm());    args.add((arg.getDocElem()).toTerm());    return factory.make(patternTNode_DocumentNode, args);
  }

  protected TNode_ElementNode makeTNode_ElementNode(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTNode_ElementNode) {
      protoTNode_ElementNode.initHashCode(annos,fun,args);
      return (TNode_ElementNode) factory.build(protoTNode_ElementNode);
    }
  }

  public TNode_ElementNode makeTNode_ElementNode(String _name, TNodeList _attrList, TNodeList _childList) {
    aterm.ATerm[] args = new aterm.ATerm[] {factory.makeAppl(factory.makeAFun(_name, 0, true)), _attrList, _childList};
    return makeTNode_ElementNode(funTNode_ElementNode, args, factory.getEmpty());
  }

  public TNode TNode_ElementNodeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTNode_ElementNode);

    if (children != null) {
      TNode tmp = makeTNode_ElementNode((String) children.get(0), TNodeListFromTerm( (aterm.ATerm) children.get(1)), TNodeListFromTerm( (aterm.ATerm) children.get(2)));
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TNode_ElementNodeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(arg.getName());    args.add((arg.getAttrList()).toTerm());    args.add((arg.getChildList()).toTerm());    return factory.make(patternTNode_ElementNode, args);
  }

  protected TNode_AttributeNode makeTNode_AttributeNode(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTNode_AttributeNode) {
      protoTNode_AttributeNode.initHashCode(annos,fun,args);
      return (TNode_AttributeNode) factory.build(protoTNode_AttributeNode);
    }
  }

  public TNode_AttributeNode makeTNode_AttributeNode(String _name, String _specified, TNode _child) {
    aterm.ATerm[] args = new aterm.ATerm[] {factory.makeAppl(factory.makeAFun(_name, 0, true)), factory.makeAppl(factory.makeAFun(_specified, 0, true)), _child};
    return makeTNode_AttributeNode(funTNode_AttributeNode, args, factory.getEmpty());
  }

  public TNode TNode_AttributeNodeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTNode_AttributeNode);

    if (children != null) {
      TNode tmp = makeTNode_AttributeNode((String) children.get(0), (String) children.get(1), TNodeFromTerm( (aterm.ATerm) children.get(2)));
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TNode_AttributeNodeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(arg.getName());    args.add(arg.getSpecified());    args.add((arg.getChild()).toTerm());    return factory.make(patternTNode_AttributeNode, args);
  }

  protected TNode_DocumentTypeNode makeTNode_DocumentTypeNode(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTNode_DocumentTypeNode) {
      protoTNode_DocumentTypeNode.initHashCode(annos,fun,args);
      return (TNode_DocumentTypeNode) factory.build(protoTNode_DocumentTypeNode);
    }
  }

  public TNode_DocumentTypeNode makeTNode_DocumentTypeNode(String _name, String _publicId, String _systemId, String _internalSubset, TNodeList _entities, TNodeList _notations) {
    aterm.ATerm[] args = new aterm.ATerm[] {factory.makeAppl(factory.makeAFun(_name, 0, true)), factory.makeAppl(factory.makeAFun(_publicId, 0, true)), factory.makeAppl(factory.makeAFun(_systemId, 0, true)), factory.makeAppl(factory.makeAFun(_internalSubset, 0, true)), _entities, _notations};
    return makeTNode_DocumentTypeNode(funTNode_DocumentTypeNode, args, factory.getEmpty());
  }

  public TNode TNode_DocumentTypeNodeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTNode_DocumentTypeNode);

    if (children != null) {
      TNode tmp = makeTNode_DocumentTypeNode((String) children.get(0), (String) children.get(1), (String) children.get(2), (String) children.get(3), TNodeListFromTerm( (aterm.ATerm) children.get(4)), TNodeListFromTerm( (aterm.ATerm) children.get(5)));
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TNode_DocumentTypeNodeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(arg.getName());    args.add(arg.getPublicId());    args.add(arg.getSystemId());    args.add(arg.getInternalSubset());    args.add((arg.getEntities()).toTerm());    args.add((arg.getNotations()).toTerm());    return factory.make(patternTNode_DocumentTypeNode, args);
  }

  protected TNode_EntityReferenceNode makeTNode_EntityReferenceNode(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTNode_EntityReferenceNode) {
      protoTNode_EntityReferenceNode.initHashCode(annos,fun,args);
      return (TNode_EntityReferenceNode) factory.build(protoTNode_EntityReferenceNode);
    }
  }

  public TNode_EntityReferenceNode makeTNode_EntityReferenceNode(String _name, TNodeList _childList) {
    aterm.ATerm[] args = new aterm.ATerm[] {factory.makeAppl(factory.makeAFun(_name, 0, true)), _childList};
    return makeTNode_EntityReferenceNode(funTNode_EntityReferenceNode, args, factory.getEmpty());
  }

  public TNode TNode_EntityReferenceNodeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTNode_EntityReferenceNode);

    if (children != null) {
      TNode tmp = makeTNode_EntityReferenceNode((String) children.get(0), TNodeListFromTerm( (aterm.ATerm) children.get(1)));
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TNode_EntityReferenceNodeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(arg.getName());    args.add((arg.getChildList()).toTerm());    return factory.make(patternTNode_EntityReferenceNode, args);
  }

  protected TNode_EntityNode makeTNode_EntityNode(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTNode_EntityNode) {
      protoTNode_EntityNode.initHashCode(annos,fun,args);
      return (TNode_EntityNode) factory.build(protoTNode_EntityNode);
    }
  }

  public TNode_EntityNode makeTNode_EntityNode(String _notationName, String _publicId, String _systemId) {
    aterm.ATerm[] args = new aterm.ATerm[] {factory.makeAppl(factory.makeAFun(_notationName, 0, true)), factory.makeAppl(factory.makeAFun(_publicId, 0, true)), factory.makeAppl(factory.makeAFun(_systemId, 0, true))};
    return makeTNode_EntityNode(funTNode_EntityNode, args, factory.getEmpty());
  }

  public TNode TNode_EntityNodeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTNode_EntityNode);

    if (children != null) {
      TNode tmp = makeTNode_EntityNode((String) children.get(0), (String) children.get(1), (String) children.get(2));
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TNode_EntityNodeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(arg.getNotationName());    args.add(arg.getPublicId());    args.add(arg.getSystemId());    return factory.make(patternTNode_EntityNode, args);
  }

  protected TNode_NotationNode makeTNode_NotationNode(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoTNode_NotationNode) {
      protoTNode_NotationNode.initHashCode(annos,fun,args);
      return (TNode_NotationNode) factory.build(protoTNode_NotationNode);
    }
  }

  public TNode_NotationNode makeTNode_NotationNode(String _publicId, String _systemId) {
    aterm.ATerm[] args = new aterm.ATerm[] {factory.makeAppl(factory.makeAFun(_publicId, 0, true)), factory.makeAppl(factory.makeAFun(_systemId, 0, true))};
    return makeTNode_NotationNode(funTNode_NotationNode, args, factory.getEmpty());
  }

  public TNode TNode_NotationNodeFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternTNode_NotationNode);

    if (children != null) {
      TNode tmp = makeTNode_NotationNode((String) children.get(0), (String) children.get(1));
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(TNode_NotationNodeImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(arg.getPublicId());    args.add(arg.getSystemId());    return factory.make(patternTNode_NotationNode, args);
  }

  public TNodeList makeTNodeList() {
    return emptyTNodeList;
  }
  public TNodeList makeTNodeList(TNode elem ) {
    return (TNodeList) makeTNodeList(elem, emptyTNodeList);
  }
  public TNodeList makeTNodeList(TNode head, TNodeList tail) {
    return (TNodeList) makeTNodeList((aterm.ATerm) head, (aterm.ATermList) tail, factory.getEmpty());
  }
  protected TNodeList makeTNodeList(aterm.ATerm head, aterm.ATermList tail, aterm.ATermList annos) {
    synchronized (protoTNodeList) {
      protoTNodeList.initHashCode(annos,head,tail);
      return (TNodeList) factory.build(protoTNodeList);
    }
  }
  public TNode TNodeFromTerm(aterm.ATerm trm)
  {
    TNode tmp;
    tmp = TNode_CommentNodeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TNode_ProcessingInstructionNodeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TNode_TextNodeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TNode_CDATASectionNodeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TNode_DocumentNodeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TNode_ElementNodeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TNode_AttributeNodeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TNode_DocumentTypeNodeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TNode_EntityReferenceNodeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TNode_EntityNodeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = TNode_NotationNodeFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a TNode: " + trm);
  }
  public TNodeList TNodeListFromTerm(aterm.ATerm trm)
  {
     if (trm instanceof aterm.ATermList) {
        aterm.ATermList list = ((aterm.ATermList) trm).reverse();
        TNodeList result = makeTNodeList();
        for (; !list.isEmpty(); list = list.getNext()) {
          TNode elem = TNodeFromTerm(list.getFirst());
           if (elem != null) {
             result = makeTNodeList(elem, result);
           }
           else {
             throw new RuntimeException("Invalid element in TNodeList: " + elem);
           }
        }
        return result;
     }
     else {
       throw new RuntimeException("This is not a TNodeList: " + trm);
     }
  }
  public TNode TNodeFromString(String str)
  {
    aterm.ATerm trm = factory.parse(str);
    return TNodeFromTerm(trm);
  }
  public TNode TNodeFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TNodeFromTerm(factory.readFromFile(stream));
  }
  public TNodeList TNodeListFromString(String str)
  {
    aterm.ATerm trm = factory.parse(str);
    return TNodeListFromTerm(trm);
  }
  public TNodeList TNodeListFromFile(java.io.InputStream stream) throws java.io.IOException {
    return TNodeListFromTerm(factory.readFromFile(stream));
  }
}
