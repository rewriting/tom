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

public class XMLToATerm {
  
  %include{ adt/NodeTerm.tom }
	
  private NodeTermFactory nodesFactory = null;
  private NodeTerm nodeTerm = null;
  private boolean deleteWhiteSpaceNodes = false;
  
  public void setDeletingWhiteSpaceNodes(boolean b_d) {
    deleteWhiteSpaceNodes=b_d;
  }

  private NodeTermFactory getNodeTermFactory() {
    return nodesFactory;
  }

  public XMLToATerm () {
    nodesFactory = new NodeTermFactory(new PureFactory());
  }

  public XMLToATerm(NodeTermFactory factory) {
    nodesFactory = factory;
  }

  public XMLToATerm(String filename) {
    this();
    convert(filename);
  }

  public XMLToATerm(NodeTermFactory factory,String filename) {
    this(factory);
    convert(filename);
  }

  public ATerm getATerm() {
    return nodeTerm;
  }

  public void convert(String filename) {
    try {
      DocumentBuilder db = DocumentBuilderFactory.newInstance(
	  					 ).newDocumentBuilder();
      nodeTerm = xmlToATerm(db.parse(filename));
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

  public NodeTermList nodeListToAterm(NodeList n) {
    NodeTermList nt_result = `emptyNodeTermList();
    for (int it=0;it<n.getLength();it++)
      nt_result = `concNodeTerm(xmlToATerm(n.item(it)),nt_result*);
    return nt_result;
  }
	
  public NodeTerm xmlToATerm(Node node) {
    if ( node == null ) { // Nothing to do
      //System.out.println("\n\nFound a null node\n\n");
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
      System.exit(1);
    } // switch
    System.err.println("We should not be here");
    return null;
  }

  private NodeTerm makeDocumentNode (Document doc){
    NodeTerm doctype = xmlToATerm(doc.getDoctype());
    if (doctype == null) {
      doctype = `TextNode("");
    }
    NodeTerm elem = xmlToATerm(doc.getDocumentElement());
    return `DocumentNode(doctype,elem);
  }
    
  private NodeTerm makeDocumentTypeNode (DocumentType doctype) {
    String name=doctype.getName();
    name = (name == null ? "UNDEF" : name);
    String publicId = doctype.getPublicId();
    publicId = (publicId == null ? "UNDEF" : publicId);
    String systemId = doctype.getSystemId();
    systemId = (systemId == null ? "UNDEF" : systemId);
    String internalSubset = doctype.getInternalSubset();
    internalSubset = (internalSubset == null ? "UNDEF" : internalSubset);

    NodeTermList entitiesList= `concNodeTerm();
    NamedNodeMap entities = doctype.getEntities();
    for(int i=0; i < entities.getLength(); i++)
      entitiesList = `concNodeTerm(entitiesList*,xmlToATerm(entities.item(i)));

    NodeTermList notationsList = `concNodeTerm();
    NamedNodeMap notations = doctype.getNotations();
    for(int i=0; i < notations.getLength(); i++) 
      notationsList = `concNodeTerm(notationsList*,xmlToATerm(notations.item(i)));

    return `DocumentTypeNode(name,publicId,systemId,
			     internalSubset,entitiesList,notationsList);
  }
    
  private NodeTerm makeElementNode(Element elem) {
    NodeTermList attrList=`concNodeTerm();
    NodeTerm n;
    NamedNodeMap attrs = elem.getAttributes();
    for(int i=0; i < attrs.getLength(); i++) {
      n = xmlToATerm(attrs.item(i));
      if (n!=null) attrList = `concNodeTerm(attrList*,n);
    }
    NodeTermList childList=`concNodeTerm();
    NodeList nodes = elem.getChildNodes();
    for(int i = 0; i < nodes.getLength(); i++) {
      n = xmlToATerm(nodes.item(i));
      if (n!=null) childList = `concNodeTerm(childList*,n);
    }
    return `ElementNode(elem.getNodeName(),attrList,childList);
  }

  private NodeTerm makeAttributeNode(Attr attr) {
    String specif = (attr.getSpecified() ? "true" : "false");
    return `AttributeNode(attr.getNodeName()
			  ,specif
			  ,TextNode(attr.getNodeValue()));
  }

  private NodeTerm makeTextNode(Text text) {
    if (!deleteWhiteSpaceNodes)
      return `TextNode(text.getData());
    if (!text.getData().trim().equals(""))
      return `TextNode(text.getData());
    return null;
  }
  
  private NodeTerm makeEntityNode(Entity e) {
    String nn= e.getNotationName();
    nn = (nn == null ? "UNDEF" : nn);
    String publicId = e.getPublicId();
    publicId = (publicId == null ? "UNDEF" : publicId);
    String systemId = e.getSystemId();
    systemId = (systemId == null ? "UNDEF" : systemId);
    return `EntityNode(nn,publicId,systemId);
  }

  private NodeTerm makeEntityReferenceNode(EntityReference er) {
    NodeTermList list=`concNodeTerm();
    NodeTerm n;
    NodeList nodes = er.getChildNodes();
    for(int i = 0; i < nodes.getLength(); i++) {
      n = xmlToATerm(nodes.item(i));
      if (n!=null) list = `concNodeTerm(list*,n);
    }
    return `EntityReferenceNode(er.getNodeName(),list);
  }

  private NodeTerm makeNotationNode(Notation notation) {
    String publicId = notation.getPublicId();
    publicId = (publicId == null ? "UNDEF" : publicId);
    String systemId = notation.getSystemId();
    systemId = (systemId == null ? "UNDEF" : systemId);
    return `NotationNode(publicId,systemId);
  }

  private NodeTerm makeCommentNode(Comment comment) {
    return `CommentNode(comment.getData());
  }
  
  private NodeTerm makeCDATASectionNode(CDATASection cdata) {
    return `CDATASectionNode(cdata.getData());
  }

  private NodeTerm makeProcessingInstructionNode(ProcessingInstruction instr) {
    return `ProcessingInstructionNode(instr.getTarget(),instr.getData());
  }

}
