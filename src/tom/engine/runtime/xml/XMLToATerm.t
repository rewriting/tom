/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRST, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom.runtime.xml;

import java.io.*;
import jtom.runtime.xml.adt.*;
import aterm.*;
import aterm.pure.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import jtom.exception.TomRuntimeException;

public class XMLToATerm {
  
  %include{ adt/TNode.tom }
	
  private TNodeFactory nodesFactory = null;
  private TNode nodeTerm = null;
  private boolean deleteWhiteSpaceNodes = false;
  
  public void setDeletingWhiteSpaceNodes(boolean b_d) {
    deleteWhiteSpaceNodes=b_d;
  }

  private TNodeFactory getTNodeFactory() {
    return nodesFactory;
  }

  public XMLToATerm () {
    nodesFactory = new TNodeFactory(new PureFactory());
  }

  public XMLToATerm(TNodeFactory factory) {
    nodesFactory = factory;
  }

  public XMLToATerm(String filename) {
    this();
    convert(filename);
  }

  public XMLToATerm(TNodeFactory factory,String filename) {
    this(factory);
    convert(filename);
  }

  public ATerm getATerm() {
    return nodeTerm;
  }

  public void convert(String filename) {
    nodeTerm = xmlToATerm(convertToNode(filename));
  } 
 
  public void convert(Node node) {
    nodeTerm = xmlToATerm(node);
  }
  
  public Node convertToNode(String filename) {
    try {
      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      return db.parse(filename);
    } catch (SAXException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    } catch (FactoryConfigurationError e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
    System.out.println("We shouldn't be there...");
    return null;
  }

  public TNodeList nodeListToAterm(NodeList list) {
    TNodeList res = `emptyTNodeList();
    for(int i=list.getLength()-1 ; i>=0 ; i--) {
      TNode elt = xmlToATerm(list.item(i));
      if(elt != null) {
        res = `manyTNodeList(elt,res);
      }
    }
    return res;
  }
  
  public TNodeList namedNodeMapToAterm(NamedNodeMap list) {
    TNodeList res = `emptyTNodeList();
    for(int i=list.getLength()-1 ; i>=0 ; i--) {
      TNode elt = xmlToATerm(list.item(i));
      if(elt != null) {
        res = `manyTNodeList(elt,res);
      }
    }
    return res;
  }
  
  public TNode xmlToATerm(Node node) {
    if ( node == null ) { // Nothing to do
      return null;
    }
    node.normalize();
    int type = node.getNodeType();
    switch (type) {
    case Node.ATTRIBUTE_NODE:
      //The node is an Attr.
      return makeAttributeNode((Attr) node);
      //break;
    case Node.CDATA_SECTION_NODE:
      //The node is a CDATASection.
      return makeCDATASectionNode((CDATASection) node);
      //break;
    case Node.COMMENT_NODE:
      //The node is a Comment.
      return makeCommentNode((Comment) node);
      //break;
    case Node.DOCUMENT_FRAGMENT_NODE:
      //The node is a DocumentFragment.
      System.out.println("We shouldn't find DocumentFragment in a freshly-parsed document");
      throw new TomRuntimeException(new Throwable("We shouldn't find DocumentFragment in a freshly-parsed document"));
      //break;
    case Node.DOCUMENT_NODE:
      //The node is a Document.
      return makeDocumentNode((Document) node);
      //break;
    case Node.DOCUMENT_TYPE_NODE:
      //The node is a DocumentType.
      return makeDocumentTypeNode((DocumentType) node);
      //break;
    case Node.ELEMENT_NODE:
      //The node is an Element.
      return makeElementNode((Element) node);
      //break;
    case Node.ENTITY_NODE:
      //The node is an Entity.
      return makeEntityNode((Entity) node);
      //break;
    case Node.ENTITY_REFERENCE_NODE:
      return makeEntityReferenceNode((EntityReference) node);
      //The node is an EntityReference.
      //break;
    case Node.NOTATION_NODE:
      //The node is a Notation.
      return makeNotationNode((Notation) node);
      //break;
    case Node.PROCESSING_INSTRUCTION_NODE:
      //The node is a ProcessingInstruction.
      return makeProcessingInstructionNode((ProcessingInstruction) node);
      //break;
    case Node.TEXT_NODE:
      return makeTextNode((Text) node);
      //break;
    default : 
      System.err.println("The type of Node is unknown");
      throw new TomRuntimeException(new Throwable("The type of Node is unknown"));
    } // switch
  }

  private TNode makeDocumentNode (Document doc){
    TNode doctype = xmlToATerm(doc.getDoctype());
    if (doctype == null) {
      doctype = `TextNode("");
    }
    TNode elem = xmlToATerm(doc.getDocumentElement());
    return `DocumentNode(doctype,elem);
  }
    
  private TNode makeDocumentTypeNode (DocumentType doctype) {
    String name=doctype.getName();
    name = (name == null ? "UNDEF" : name);
    String publicId = doctype.getPublicId();
    publicId = (publicId == null ? "UNDEF" : publicId);
    String systemId = doctype.getSystemId();
    systemId = (systemId == null ? "UNDEF" : systemId);
    String internalSubset = doctype.getInternalSubset();
    internalSubset = (internalSubset == null ? "UNDEF" : internalSubset);
    TNodeList entitiesList = namedNodeMapToAterm(doctype.getEntities());
    TNodeList notationsList = namedNodeMapToAterm(doctype.getNotations());
    return `DocumentTypeNode(name,publicId,systemId,
			     internalSubset,entitiesList,notationsList);
  }
    
  private TNode makeElementNode(Element elem) {
    TNodeList attrList  = namedNodeMapToAterm(elem.getAttributes());
      //System.out.println("attrList = " + attrList);
    attrList = sortAttributeList(attrList);
      //System.out.println("sorted attrList = " + attrList);
    TNodeList childList = nodeListToAterm(elem.getChildNodes());
    return `ElementNode(elem.getNodeName(),attrList,childList);
  }

  public TNodeList sortAttributeList(TNodeList attrList) {
    TNodeList res = `emptyTNodeList();
    while(!attrList.isEmpty()) {
      res = insertSortedAttribute(attrList.getHead(),res);
      attrList = attrList.getTail();
    }
    return res;
  }

  private TNodeList insertSortedAttribute(TNode elt, TNodeList list) {
    %match(TNode elt, TNodeList list) {
      AttributeNode[name=name1], emptyTNodeList() -> {
        return `manyTNodeList(elt,list);
      }
      
      AttributeNode[name=name1], manyTNodeList(head@AttributeNode[name=name2],tail) -> {
        if(name1.compareTo(name2) < 0) {
          return `manyTNodeList(elt,list);
        } else {
          return `manyTNodeList(head,insertSortedAttribute(elt,tail));
        }
      }
    }
    System.out.println("insertSortedAttribute: Strange case");
    return list;
  }
    
  private TNode makeAttributeNode(Attr attr) {
    String specif = (attr.getSpecified() ? "true" : "false");
    return `AttributeNode(attr.getNodeName(),specif,TextNode(attr.getNodeValue()));
  }

  private TNode makeTextNode(Text text) {
    if (!deleteWhiteSpaceNodes) {
      return `TextNode(text.getData());
    }
    if (!text.getData().trim().equals("")) {
      return `TextNode(text.getData());
    }
    return null;
  }
  
  private TNode makeEntityNode(Entity e) {
    String nn= e.getNotationName();
    nn = (nn == null ? "UNDEF" : nn);
    String publicId = e.getPublicId();
    publicId = (publicId == null ? "UNDEF" : publicId);
    String systemId = e.getSystemId();
    systemId = (systemId == null ? "UNDEF" : systemId);
    return `EntityNode(nn,publicId,systemId);
  }

  private TNode makeEntityReferenceNode(EntityReference er) {
    TNodeList list = nodeListToAterm(er.getChildNodes());
    return `EntityReferenceNode(er.getNodeName(),list);
  }

  private TNode makeNotationNode(Notation notation) {
    String publicId = notation.getPublicId();
    publicId = (publicId == null ? "UNDEF" : publicId);
    String systemId = notation.getSystemId();
    systemId = (systemId == null ? "UNDEF" : systemId);
    return `NotationNode(publicId,systemId);
  }

  private TNode makeCommentNode(Comment comment) {
    return `CommentNode(comment.getData());
  }
  
  private TNode makeCDATASectionNode(CDATASection cdata) {
    return `CDATASectionNode(cdata.getData());
  }

  private TNode makeProcessingInstructionNode(ProcessingInstruction instr) {
    return `ProcessingInstructionNode(instr.getTarget(),instr.getData());
  }

}
