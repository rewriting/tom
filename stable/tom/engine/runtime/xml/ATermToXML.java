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

public class ATermToXML {
  
  public Object tom_get_fun_sym_String( String t) { return t; }public boolean tom_cmp_fun_sym_String(Object s1, Object s2) { return  s1.equals(s2); }public Object tom_get_subterm_String( String t,  int n) { return null; }public boolean tom_terms_equal_String(Object t1, Object t2) { return t1.equals(t2); }public  int tom_get_fun_sym_int( int t) { return t; }public boolean tom_cmp_fun_sym_int( int s1,  int s2) { return  (s1 == s2); }public Object tom_get_subterm_int( int t,  int n) { return null; }public boolean tom_terms_equal_int( int t1,  int t2) { return (t1 == t2); }public  double tom_get_fun_sym_double( double t) { return t; }public boolean tom_cmp_fun_sym_double( double s1,  double s2) { return  (s1 == s2); }public Object tom_get_subterm_double( double t,  int n) { return null; }public boolean tom_terms_equal_double( double t1,  double t2) { return (t1 == t2); }public Object tom_get_fun_sym_ATerm( aterm.ATerm t) { return ((t instanceof ATermAppl)?((ATermAppl)t).getAFun():null); }public boolean tom_cmp_fun_sym_ATerm(Object s1, Object s2) { return  s1==s2; }public Object tom_get_subterm_ATerm( aterm.ATerm t,  int n) { return (((ATermAppl)t).getArgument(n)); }public boolean tom_terms_equal_ATerm(Object t1, Object t2) { return t1.equals(t2); }public Object tom_get_fun_sym_ATermList( aterm.ATermList t) { return ((t instanceof ATermList)?getTNodeFactory().getPureFactory().makeAFun("conc",1,false):null); }public boolean tom_cmp_fun_sym_ATermList(Object s1, Object s2) { return  s1==s2; }public boolean tom_terms_equal_ATermList(Object t1, Object t2) { return t1.equals(t2); }public Object tom_get_head_ATermList( aterm.ATermList l) { return l.getFirst(); }public  aterm.ATermList tom_get_tail_ATermList( aterm.ATermList l) { return l.getNext(); }public boolean tom_is_empty_ATermList( aterm.ATermList l) { return l.isEmpty(); }public Object tom_get_fun_sym_TNode( TNode t) { return null; }public boolean tom_cmp_fun_sym_TNode(Object s1, Object s2) { return  false; }public Object tom_get_subterm_TNode( TNode t,  int n) { return null; }public boolean tom_terms_equal_TNode(Object t1, Object t2) { return t1.equals(t2); }public boolean tom_is_fun_sym_CommentNode( TNode t) { return  (t!= null) && t.isCommentNode(); }public  TNode tom_make_CommentNode( String t0) { return  getTNodeFactory().makeTNode_CommentNode(t0); }public  String tom_get_slot_CommentNode_data( TNode t) { return  t.getData(); }public boolean tom_is_fun_sym_ProcessingInstructionNode( TNode t) { return  (t!= null) && t.isProcessingInstructionNode(); }public  TNode tom_make_ProcessingInstructionNode( String t0,  String t1) { return  getTNodeFactory().makeTNode_ProcessingInstructionNode(t0, t1); }public  String tom_get_slot_ProcessingInstructionNode_target( TNode t) { return  t.getTarget(); }public  String tom_get_slot_ProcessingInstructionNode_data( TNode t) { return  t.getData(); }public boolean tom_is_fun_sym_TextNode( TNode t) { return  (t!= null) && t.isTextNode(); }public  TNode tom_make_TextNode( String t0) { return  getTNodeFactory().makeTNode_TextNode(t0); }public  String tom_get_slot_TextNode_data( TNode t) { return  t.getData(); }public boolean tom_is_fun_sym_CDATASectionNode( TNode t) { return  (t!= null) && t.isCDATASectionNode(); }public  TNode tom_make_CDATASectionNode( String t0) { return  getTNodeFactory().makeTNode_CDATASectionNode(t0); }public  String tom_get_slot_CDATASectionNode_data( TNode t) { return  t.getData(); }public boolean tom_is_fun_sym_DocumentNode( TNode t) { return  (t!= null) && t.isDocumentNode(); }public  TNode tom_make_DocumentNode( TNode t0,  TNode t1) { return  getTNodeFactory().makeTNode_DocumentNode(t0, t1); }public  TNode tom_get_slot_DocumentNode_docType( TNode t) { return  t.getDocType(); }public  TNode tom_get_slot_DocumentNode_docElem( TNode t) { return  t.getDocElem(); }public boolean tom_is_fun_sym_ElementNode( TNode t) { return  (t!= null) && t.isElementNode(); }public  TNode tom_make_ElementNode( String t0,  TNodeList t1,  TNodeList t2) { return  getTNodeFactory().makeTNode_ElementNode(t0, t1, t2); }public  String tom_get_slot_ElementNode_name( TNode t) { return  t.getName(); }public  TNodeList tom_get_slot_ElementNode_attrList( TNode t) { return  t.getAttrList(); }public  TNodeList tom_get_slot_ElementNode_childList( TNode t) { return  t.getChildList(); }public boolean tom_is_fun_sym_AttributeNode( TNode t) { return  (t!= null) && t.isAttributeNode(); }public  TNode tom_make_AttributeNode( String t0,  String t1,  TNode t2) { return  getTNodeFactory().makeTNode_AttributeNode(t0, t1, t2); }public  String tom_get_slot_AttributeNode_name( TNode t) { return  t.getName(); }public  String tom_get_slot_AttributeNode_specified( TNode t) { return  t.getSpecified(); }public  TNode tom_get_slot_AttributeNode_child( TNode t) { return  t.getChild(); }public boolean tom_is_fun_sym_DocumentTypeNode( TNode t) { return  (t!= null) && t.isDocumentTypeNode(); }public  TNode tom_make_DocumentTypeNode( String t0,  String t1,  String t2,  String t3,  TNodeList t4,  TNodeList t5) { return  getTNodeFactory().makeTNode_DocumentTypeNode(t0, t1, t2, t3, t4, t5); }public  String tom_get_slot_DocumentTypeNode_name( TNode t) { return  t.getName(); }public  String tom_get_slot_DocumentTypeNode_publicId( TNode t) { return  t.getPublicId(); }public  String tom_get_slot_DocumentTypeNode_systemId( TNode t) { return  t.getSystemId(); }public  String tom_get_slot_DocumentTypeNode_internalSubset( TNode t) { return  t.getInternalSubset(); }public  TNodeList tom_get_slot_DocumentTypeNode_entities( TNode t) { return  t.getEntities(); }public  TNodeList tom_get_slot_DocumentTypeNode_notations( TNode t) { return  t.getNotations(); }public boolean tom_is_fun_sym_EntityReferenceNode( TNode t) { return  (t!= null) && t.isEntityReferenceNode(); }public  TNode tom_make_EntityReferenceNode( String t0,  TNodeList t1) { return  getTNodeFactory().makeTNode_EntityReferenceNode(t0, t1); }public  String tom_get_slot_EntityReferenceNode_name( TNode t) { return  t.getName(); }public  TNodeList tom_get_slot_EntityReferenceNode_childList( TNode t) { return  t.getChildList(); }public boolean tom_is_fun_sym_EntityNode( TNode t) { return  (t!= null) && t.isEntityNode(); }public  TNode tom_make_EntityNode( String t0,  String t1,  String t2) { return  getTNodeFactory().makeTNode_EntityNode(t0, t1, t2); }public  String tom_get_slot_EntityNode_notationName( TNode t) { return  t.getNotationName(); }public  String tom_get_slot_EntityNode_publicId( TNode t) { return  t.getPublicId(); }public  String tom_get_slot_EntityNode_systemId( TNode t) { return  t.getSystemId(); }public boolean tom_is_fun_sym_NotationNode( TNode t) { return  (t!= null) && t.isNotationNode(); }public  TNode tom_make_NotationNode( String t0,  String t1) { return  getTNodeFactory().makeTNode_NotationNode(t0, t1); }public  String tom_get_slot_NotationNode_publicId( TNode t) { return  t.getPublicId(); }public  String tom_get_slot_NotationNode_systemId( TNode t) { return  t.getSystemId(); }public Object tom_get_fun_sym_TNodeList( TNodeList t) { return null; }public boolean tom_cmp_fun_sym_TNodeList(Object s1, Object s2) { return  false; }public boolean tom_terms_equal_TNodeList(Object t1, Object t2) { return t1.equals(t2); }public Object tom_get_head_TNodeList( TNodeList l) { return l.getHead(); }public  TNodeList tom_get_tail_TNodeList( TNodeList l) { return l.getTail(); }public boolean tom_is_empty_TNodeList( TNodeList l) { return l.isEmpty(); }public boolean tom_is_fun_sym_concTNode( TNodeList t) { return (t!= null) && t.isSortTNodeList(); }public Object tom_make_empty_concTNode() { return getTNodeFactory().makeTNodeList(); }public  TNodeList tom_make_insert_concTNode( TNode e,  TNodeList l) { return getTNodeFactory().makeTNodeList(e,l); } public  TNodeList tom_reverse_concTNode( TNodeList l) {     TNodeList result = ( TNodeList)tom_make_empty_concTNode();     while(!tom_is_empty_TNodeList(l) ) {       result = ( TNodeList)tom_make_insert_concTNode(( TNode)tom_get_head_TNodeList(l),result);       l = ( TNodeList)tom_get_tail_TNodeList(l);     }     return result;   }  public  TNodeList tom_insert_list_concTNode( TNodeList l1,  TNodeList l2) {    if(tom_is_empty_TNodeList(l1)) {     return l2;    } else if(tom_is_empty_TNodeList(l2)) {     return l1;    } else if(tom_is_empty_TNodeList(( TNodeList)tom_get_tail_TNodeList(l1))) {     return ( TNodeList)tom_make_insert_concTNode(( TNode)tom_get_head_TNodeList(l1),l2);    } else {      return ( TNodeList)tom_make_insert_concTNode(( TNode)tom_get_head_TNodeList(l1),tom_insert_list_concTNode(( TNodeList)tom_get_tail_TNodeList(l1),l2));    }   }  public  TNodeList tom_get_slice_concTNode( TNodeList begin,  TNodeList end) {     TNodeList result = ( TNodeList)tom_make_empty_concTNode();     while(!tom_terms_equal_TNodeList(begin,end)) {       result = ( TNodeList)tom_make_insert_concTNode(( TNode)tom_get_head_TNodeList(begin),result);       begin = ( TNodeList)tom_get_tail_TNodeList(begin);      }     result = ( TNodeList)tom_reverse_concTNode(result);     return result;   } public boolean tom_is_fun_sym_emptyTNodeList( TNodeList t) { return  (t!= null) && t.isEmpty(); }public  TNodeList tom_make_emptyTNodeList() { return getTNodeFactory().makeTNodeList(); }public boolean tom_is_fun_sym_manyTNodeList( TNodeList t) { return  (t!= null) && t.isMany(); }public  TNodeList tom_make_manyTNodeList( TNode e,  TNodeList l) { return getTNodeFactory().makeTNodeList(e,l); }public  TNode tom_get_slot_manyTNodeList_head( TNodeList t) { return  t.getHead(); }public  TNodeList tom_get_slot_manyTNodeList_tail( TNodeList t) { return  t.getTail(); }  

  private TNodeFactory nodesFactory = null;
  private OutputStream out = System.out;
  private Writer writer = null;

  private TNodeFactory getTNodeFactory() {
    return nodesFactory;
  }

  public ATermToXML () {
    nodesFactory = new TNodeFactory(new PureFactory());
  }

  public ATermToXML (TNodeFactory factory) {
    nodesFactory = factory;
  }

  public void setOutputStream(OutputStream out){
    this.out = out;
  }

  public void setWriter(Writer writer) {
    this.writer = writer;
  }

  public void convert(String filename) {
    try {
      convert(nodesFactory.getPureFactory().readFromFile(filename));
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
  
  public void convert(ATerm term) {
    if (term instanceof TNode) {
      atermToXML((TNode) term);
    } else {
      System.out.println("ATermToXML can only convert TNode to XML");
    }
  }

  public void atermToXML(TNode n) {
     {  TNode tom_match1_1 = null; tom_match1_1 = ( TNode) n;_match1_pattern1: {  TNode docElem = null;  TNode docType = null; if(tom_is_fun_sym_DocumentNode(tom_match1_1)) {  TNode tom_match1_1_1 = null;  TNode tom_match1_1_2 = null; tom_match1_1_1 = ( TNode) tom_get_slot_DocumentNode_docType(tom_match1_1); tom_match1_1_2 = ( TNode) tom_get_slot_DocumentNode_docElem(tom_match1_1); docType = ( TNode) tom_match1_1_1; docElem = ( TNode) tom_match1_1_2;
 
	write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	atermToXML(docType);
	atermToXML(docElem);
	write("\n");
	return;
       }}_match1_pattern2: {  String internalSubset = null;  String systemId = null;  String publicId = null;  String name = null; if(tom_is_fun_sym_DocumentTypeNode(tom_match1_1)) {  String tom_match1_1_1 = null;  String tom_match1_1_2 = null;  String tom_match1_1_3 = null;  String tom_match1_1_4 = null;  TNodeList tom_match1_1_5 = null;  TNodeList tom_match1_1_6 = null; tom_match1_1_1 = ( String) tom_get_slot_DocumentTypeNode_name(tom_match1_1); tom_match1_1_2 = ( String) tom_get_slot_DocumentTypeNode_publicId(tom_match1_1); tom_match1_1_3 = ( String) tom_get_slot_DocumentTypeNode_systemId(tom_match1_1); tom_match1_1_4 = ( String) tom_get_slot_DocumentTypeNode_internalSubset(tom_match1_1); tom_match1_1_5 = ( TNodeList) tom_get_slot_DocumentTypeNode_entities(tom_match1_1); tom_match1_1_6 = ( TNodeList) tom_get_slot_DocumentTypeNode_notations(tom_match1_1); name = ( String) tom_match1_1_1; publicId = ( String) tom_match1_1_2; systemId = ( String) tom_match1_1_3; internalSubset = ( String) tom_match1_1_4;
 
	write("<!DOCTYPE "+name);
	if (!publicId.equals("UNDEF") && !systemId.equals("UNDEF"))
	  write(" PUBLIC \""+publicId+"\" \"");
       	else if (!systemId.equals("UNDEF") && publicId.equals("UNDEF"))
	  write(" SYSTEM \"");
	else {
	  System.out.println("Problem in DocumentTypeNode");
	  System.exit(1);
	}
	write(systemId+"\"");
	if (!internalSubset.equals("UNDEF")) 
	  write(" ["+internalSubset+"]");
	write(">\n");
	return;
       }}_match1_pattern3: {  String name = null;  TNodeList attrList = null; if(tom_is_fun_sym_ElementNode(tom_match1_1)) {  String tom_match1_1_1 = null;  TNodeList tom_match1_1_2 = null;  TNodeList tom_match1_1_3 = null; tom_match1_1_1 = ( String) tom_get_slot_ElementNode_name(tom_match1_1); tom_match1_1_2 = ( TNodeList) tom_get_slot_ElementNode_attrList(tom_match1_1); tom_match1_1_3 = ( TNodeList) tom_get_slot_ElementNode_childList(tom_match1_1); name = ( String) tom_match1_1_1; attrList = ( TNodeList) tom_match1_1_2; if(tom_is_fun_sym_concTNode(tom_match1_1_3)) {  TNodeList tom_match1_1_3_list1 = null; tom_match1_1_3_list1 = ( TNodeList) tom_match1_1_3; if(tom_is_empty_TNodeList(tom_match1_1_3_list1)) {
 
	write("<"+name);
	atermToXMLList(attrList);
	write("/>");
	return;
       } } }}_match1_pattern4: {  TNodeList childList = null;  TNodeList attrList = null;  String name = null; if(tom_is_fun_sym_ElementNode(tom_match1_1)) {  String tom_match1_1_1 = null;  TNodeList tom_match1_1_2 = null;  TNodeList tom_match1_1_3 = null; tom_match1_1_1 = ( String) tom_get_slot_ElementNode_name(tom_match1_1); tom_match1_1_2 = ( TNodeList) tom_get_slot_ElementNode_attrList(tom_match1_1); tom_match1_1_3 = ( TNodeList) tom_get_slot_ElementNode_childList(tom_match1_1); name = ( String) tom_match1_1_1; attrList = ( TNodeList) tom_match1_1_2; childList = ( TNodeList) tom_match1_1_3;
 
	write("<"+name);
	atermToXMLList(attrList);
	write(">");
	atermToXMLList(childList);
	write("</"+name+">");
	return;
       }}_match1_pattern5: {  TNode child = null;  String specified = null;  String name = null; if(tom_is_fun_sym_AttributeNode(tom_match1_1)) {  String tom_match1_1_1 = null;  String tom_match1_1_2 = null;  TNode tom_match1_1_3 = null; tom_match1_1_1 = ( String) tom_get_slot_AttributeNode_name(tom_match1_1); tom_match1_1_2 = ( String) tom_get_slot_AttributeNode_specified(tom_match1_1); tom_match1_1_3 = ( TNode) tom_get_slot_AttributeNode_child(tom_match1_1); name = ( String) tom_match1_1_1; specified = ( String) tom_match1_1_2; child = ( TNode) tom_match1_1_3;
 
	if (specified.equals("true")) {
	  write(" "+name+"=\"");
	  atermToXML(child);
	  write("\"");
	}
	return;
       }}_match1_pattern6: {  String data = null; if(tom_is_fun_sym_TextNode(tom_match1_1)) {  String tom_match1_1_1 = null; tom_match1_1_1 = ( String) tom_get_slot_TextNode_data(tom_match1_1); data = ( String) tom_match1_1_1;
 
	write(data);
	return;
       }}_match1_pattern7: {  String data = null; if(tom_is_fun_sym_CommentNode(tom_match1_1)) {  String tom_match1_1_1 = null; tom_match1_1_1 = ( String) tom_get_slot_CommentNode_data(tom_match1_1); data = ( String) tom_match1_1_1;
 
	write("<!-- "+data+" -->");
	return;
       }}_match1_pattern8: {  String data = null; if(tom_is_fun_sym_CDATASectionNode(tom_match1_1)) {  String tom_match1_1_1 = null; tom_match1_1_1 = ( String) tom_get_slot_CDATASectionNode_data(tom_match1_1); data = ( String) tom_match1_1_1;
 
	write("<![CDATA["+data+"]]>");
	return;
       }}_match1_pattern9: {  String data = null;  String target = null; if(tom_is_fun_sym_ProcessingInstructionNode(tom_match1_1)) {  String tom_match1_1_1 = null;  String tom_match1_1_2 = null; tom_match1_1_1 = ( String) tom_get_slot_ProcessingInstructionNode_target(tom_match1_1); tom_match1_1_2 = ( String) tom_get_slot_ProcessingInstructionNode_data(tom_match1_1); target = ( String) tom_match1_1_1; data = ( String) tom_match1_1_2;
 
	write("<?"+target+" "+data+"?>");
	return;
       }}_match1_pattern10: {  String name = null;  TNodeList childList = null; if(tom_is_fun_sym_EntityReferenceNode(tom_match1_1)) {  String tom_match1_1_1 = null;  TNodeList tom_match1_1_2 = null; tom_match1_1_1 = ( String) tom_get_slot_EntityReferenceNode_name(tom_match1_1); tom_match1_1_2 = ( TNodeList) tom_get_slot_EntityReferenceNode_childList(tom_match1_1); name = ( String) tom_match1_1_1; childList = ( TNodeList) tom_match1_1_2;
 
	write("&"+name+";");
	return;
       }}_match1_pattern11: {
 
	System.out.println("Unknown type of TNode : "+n);
      } }
 
  }

  private void atermToXMLList(TNodeList list) {
    if(list.isEmpty()) {
      return;
    }
    TNode t = (TNode) list.getFirst();
    TNodeList l = list.getTail(); 
    atermToXML(t);
    atermToXMLList(l);
  }
   
  private void write(String s) {
    try {
      if (out != null)
	out.write(s.getBytes());
      if (writer != null)
	writer.write(s);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }

}
