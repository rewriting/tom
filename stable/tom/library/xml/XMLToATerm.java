/* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2004, Pierre-Etienne Moreau
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.  
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * INRIA, Nancy, France 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.library.xml;

import java.util.*;
import aterm.*;
import aterm.pure.*;

import java.io.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XMLToATerm {
  
  /* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/  /* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/  public  char  tom_get_fun_sym_char( char  t) { return  t ; }public boolean tom_cmp_fun_sym_char( char  s1,  char  s2) { return  (s1==s2) ; }public Object tom_get_subterm_char( char  t,  int  n) { return  null ; }public boolean tom_terms_equal_char( char  t1,  char  t2) { return  (t1==t2) ; }public Object tom_get_fun_sym_Character( Character  t) { return  t ; }public boolean tom_cmp_fun_sym_Character(Object s1, Object s2) { return  (s1.equals(s2)) ; }public Object tom_get_subterm_Character( Character  t,  int  n) { return  null ; }public boolean tom_terms_equal_Character(Object t1, Object t2) { return  (t1.equals(t2)) ; }public boolean tom_is_fun_sym_Char( Character  t) { return  (t!= null) && (t instanceof Character) ; }public  Character  tom_make_Char( char  c) { return  new Character(c) ; }public  char  tom_get_slot_Char_c( Character  t) { return  t.charValue() ; } public  String  tom_get_fun_sym_String( String  t) { return  t ; }public boolean tom_cmp_fun_sym_String( String  s1,  String  s2) { return  (s1.equals(s2)) ; }public boolean tom_terms_equal_String( String  t1,  String  t2) { return  (t1.equals(t2)) ; }public  char  tom_get_head_String( String  s) { return  s.charAt(0) ; }public  String  tom_get_tail_String( String  s) { return  s.substring(1) ; }public boolean tom_is_empty_String( String  s) { return  (s.length()==0) ; }public boolean tom_is_fun_sym_concString( String  t) { return  (t!= null) && (t instanceof String) ; }public  String  tom_empty_list_concString() { return  "" ; }public  String  tom_cons_list_concString( char  c,  String  s) { return  (c+s) ; }public  String  tom_append_list_concString( String  l1,  String  l2) {    if(tom_is_empty_String(l1)) {     return l2;    } else if(tom_is_empty_String(l2)) {     return l1;    } else if(tom_is_empty_String(( String )tom_get_tail_String(l1))) {     return ( String )tom_cons_list_concString(( char )tom_get_head_String(l1),l2);    } else {      return ( String )tom_cons_list_concString(( char )tom_get_head_String(l1),tom_append_list_concString(( String )tom_get_tail_String(l1),l2));    }   }  public  String  tom_get_slice_concString( String  begin,  String  end) {    if(tom_terms_equal_String(begin,end)) {      return ( String )tom_empty_list_concString();    } else {      return ( String )tom_cons_list_concString(( char )tom_get_head_String(begin),( String )tom_get_slice_concString(( String )tom_get_tail_String(begin),end));    }   }    /*  * old definition of String %typeterm String {   implement           { String }   get_fun_sym(t)      { t }   cmp_fun_sym(s1,s2)  { s1.equals(s2) }   get_subterm(t, n)   { null }   equals(t1,t2)       { t1.equals(t2) } } */ /* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//*  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  */  public  int  tom_get_fun_sym_int( int  t) { return  t ; }public boolean tom_cmp_fun_sym_int( int  s1,  int  s2) { return  (s1==s2) ; }public Object tom_get_subterm_int( int  t,  int  n) { return  null ; }public boolean tom_terms_equal_int( int  t1,  int  t2) { return  (t1==t2) ; } /* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/  public  double  tom_get_fun_sym_double( double  t) { return  t ; }public boolean tom_cmp_fun_sym_double( double  s1,  double  s2) { return  (s1==s2) ; }public Object tom_get_subterm_double( double  t,  int  n) { return  null ; }public boolean tom_terms_equal_double( double  t1,  double  t2) { return  (t1==t2) ; } /* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/  public Object tom_get_fun_sym_ATerm( ATerm  t) { return  (((ATermAppl)t).getAFun()) ; }public boolean tom_cmp_fun_sym_ATerm(Object t1, Object t2) { return  t1 == t2 ; }public Object tom_get_subterm_ATerm( ATerm  t,  int  n) { return  (((ATermAppl)t).getArgument(n)) ; }public boolean tom_terms_equal_ATerm(Object t1, Object t2) { return  t1 == t2; } /* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/  public Object tom_get_fun_sym_ATermList( ATermList  t) { return  null /*((t instanceof ATermList)?factory.makeAFun("conc", 1, false):null)*/ ; }public boolean tom_cmp_fun_sym_ATermList(Object t1, Object t2) { return  false ; }public boolean tom_terms_equal_ATermList(Object l1, Object l2) { return  l1==l2 ; }public Object tom_get_head_ATermList( ATermList  l) { return  ((ATermList)l).getFirst() ; }public  ATermList  tom_get_tail_ATermList( ATermList  l) { return  ((ATermList)l).getNext() ; }public boolean tom_is_empty_ATermList( ATermList  l) { return  ((ATermList)l).isEmpty() ; } public Object tom_get_fun_sym_TNode( tom.library.adt.tnode.types.TNode t) { return null; }public boolean tom_cmp_fun_sym_TNode(Object s1, Object s2) { return  false; }public Object tom_get_subterm_TNode( tom.library.adt.tnode.types.TNode t,  int  n) { return null; }public boolean tom_terms_equal_TNode(Object t1, Object t2) { return t1.equals(t2); }public void tom_check_stamp_TNode( tom.library.adt.tnode.types.TNode t) { if(t.getAnnotation(getTNodeFactory().getPureFactory().makeList()) == getTNodeFactory().getPureFactory().makeList())  return; else throw new RuntimeException("bad stamp"); }public  tom.library.adt.tnode.types.TNode tom_set_stamp_TNode( tom.library.adt.tnode.types.TNode t) { return (tom.library.adt.tnode.types.TNode)t.setAnnotation(getTNodeFactory().getPureFactory().makeList(),getTNodeFactory().getPureFactory().makeList()); }public  tom.library.adt.tnode.types.TNode tom_get_implementation_TNode( tom.library.adt.tnode.types.TNode t) { return t; }public boolean tom_is_fun_sym_CommentNode( tom.library.adt.tnode.types.TNode t) { return  (t!= null) && t.isCommentNode(); }public  tom.library.adt.tnode.types.TNode tom_make_CommentNode( String  t0) { return  getTNodeFactory().makeTNode_CommentNode(t0); }public  String  tom_get_slot_CommentNode_data( tom.library.adt.tnode.types.TNode t) { return  t.getData(); }public boolean tom_is_fun_sym_ProcessingInstructionNode( tom.library.adt.tnode.types.TNode t) { return  (t!= null) && t.isProcessingInstructionNode(); }public  tom.library.adt.tnode.types.TNode tom_make_ProcessingInstructionNode( String  t0,  String  t1) { return  getTNodeFactory().makeTNode_ProcessingInstructionNode(t0, t1); }public  String  tom_get_slot_ProcessingInstructionNode_target( tom.library.adt.tnode.types.TNode t) { return  t.getTarget(); }public  String  tom_get_slot_ProcessingInstructionNode_data( tom.library.adt.tnode.types.TNode t) { return  t.getData(); }public boolean tom_is_fun_sym_TextNode( tom.library.adt.tnode.types.TNode t) { return  (t!= null) && t.isTextNode(); }public  tom.library.adt.tnode.types.TNode tom_make_TextNode( String  t0) { return  getTNodeFactory().makeTNode_TextNode(t0); }public  String  tom_get_slot_TextNode_data( tom.library.adt.tnode.types.TNode t) { return  t.getData(); }public boolean tom_is_fun_sym_CDATASectionNode( tom.library.adt.tnode.types.TNode t) { return  (t!= null) && t.isCDATASectionNode(); }public  tom.library.adt.tnode.types.TNode tom_make_CDATASectionNode( String  t0) { return  getTNodeFactory().makeTNode_CDATASectionNode(t0); }public  String  tom_get_slot_CDATASectionNode_data( tom.library.adt.tnode.types.TNode t) { return  t.getData(); }public boolean tom_is_fun_sym_DocumentNode( tom.library.adt.tnode.types.TNode t) { return  (t!= null) && t.isDocumentNode(); }public  tom.library.adt.tnode.types.TNode tom_make_DocumentNode( tom.library.adt.tnode.types.TNode t0,  tom.library.adt.tnode.types.TNode t1) { return  getTNodeFactory().makeTNode_DocumentNode(t0, t1); }public  tom.library.adt.tnode.types.TNode tom_get_slot_DocumentNode_docType( tom.library.adt.tnode.types.TNode t) { return  t.getDocType(); }public  tom.library.adt.tnode.types.TNode tom_get_slot_DocumentNode_docElem( tom.library.adt.tnode.types.TNode t) { return  t.getDocElem(); }public boolean tom_is_fun_sym_ElementNode( tom.library.adt.tnode.types.TNode t) { return  (t!= null) && t.isElementNode(); }public  tom.library.adt.tnode.types.TNode tom_make_ElementNode( String  t0,  tom.library.adt.tnode.types.TNodeList t1,  tom.library.adt.tnode.types.TNodeList t2) { return  getTNodeFactory().makeTNode_ElementNode(t0, t1, t2); }public  String  tom_get_slot_ElementNode_name( tom.library.adt.tnode.types.TNode t) { return  t.getName(); }public  tom.library.adt.tnode.types.TNodeList tom_get_slot_ElementNode_attrList( tom.library.adt.tnode.types.TNode t) { return  t.getAttrList(); }public  tom.library.adt.tnode.types.TNodeList tom_get_slot_ElementNode_childList( tom.library.adt.tnode.types.TNode t) { return  t.getChildList(); }public boolean tom_is_fun_sym_AttributeNode( tom.library.adt.tnode.types.TNode t) { return  (t!= null) && t.isAttributeNode(); }public  tom.library.adt.tnode.types.TNode tom_make_AttributeNode( String  t0,  String  t1,  String  t2) { return  getTNodeFactory().makeTNode_AttributeNode(t0, t1, t2); }public  String  tom_get_slot_AttributeNode_name( tom.library.adt.tnode.types.TNode t) { return  t.getName(); }public  String  tom_get_slot_AttributeNode_specified( tom.library.adt.tnode.types.TNode t) { return  t.getSpecified(); }public  String  tom_get_slot_AttributeNode_value( tom.library.adt.tnode.types.TNode t) { return  t.getValue(); }public boolean tom_is_fun_sym_DocumentTypeNode( tom.library.adt.tnode.types.TNode t) { return  (t!= null) && t.isDocumentTypeNode(); }public  tom.library.adt.tnode.types.TNode tom_make_DocumentTypeNode( String  t0,  String  t1,  String  t2,  String  t3,  tom.library.adt.tnode.types.TNodeList t4,  tom.library.adt.tnode.types.TNodeList t5) { return  getTNodeFactory().makeTNode_DocumentTypeNode(t0, t1, t2, t3, t4, t5); }public  String  tom_get_slot_DocumentTypeNode_name( tom.library.adt.tnode.types.TNode t) { return  t.getName(); }public  String  tom_get_slot_DocumentTypeNode_publicId( tom.library.adt.tnode.types.TNode t) { return  t.getPublicId(); }public  String  tom_get_slot_DocumentTypeNode_systemId( tom.library.adt.tnode.types.TNode t) { return  t.getSystemId(); }public  String  tom_get_slot_DocumentTypeNode_internalSubset( tom.library.adt.tnode.types.TNode t) { return  t.getInternalSubset(); }public  tom.library.adt.tnode.types.TNodeList tom_get_slot_DocumentTypeNode_entities( tom.library.adt.tnode.types.TNode t) { return  t.getEntities(); }public  tom.library.adt.tnode.types.TNodeList tom_get_slot_DocumentTypeNode_notations( tom.library.adt.tnode.types.TNode t) { return  t.getNotations(); }public boolean tom_is_fun_sym_EntityReferenceNode( tom.library.adt.tnode.types.TNode t) { return  (t!= null) && t.isEntityReferenceNode(); }public  tom.library.adt.tnode.types.TNode tom_make_EntityReferenceNode( String  t0,  tom.library.adt.tnode.types.TNodeList t1) { return  getTNodeFactory().makeTNode_EntityReferenceNode(t0, t1); }public  String  tom_get_slot_EntityReferenceNode_name( tom.library.adt.tnode.types.TNode t) { return  t.getName(); }public  tom.library.adt.tnode.types.TNodeList tom_get_slot_EntityReferenceNode_childList( tom.library.adt.tnode.types.TNode t) { return  t.getChildList(); }public boolean tom_is_fun_sym_EntityNode( tom.library.adt.tnode.types.TNode t) { return  (t!= null) && t.isEntityNode(); }public  tom.library.adt.tnode.types.TNode tom_make_EntityNode( String  t0,  String  t1,  String  t2) { return  getTNodeFactory().makeTNode_EntityNode(t0, t1, t2); }public  String  tom_get_slot_EntityNode_notationName( tom.library.adt.tnode.types.TNode t) { return  t.getNotationName(); }public  String  tom_get_slot_EntityNode_publicId( tom.library.adt.tnode.types.TNode t) { return  t.getPublicId(); }public  String  tom_get_slot_EntityNode_systemId( tom.library.adt.tnode.types.TNode t) { return  t.getSystemId(); }public boolean tom_is_fun_sym_NotationNode( tom.library.adt.tnode.types.TNode t) { return  (t!= null) && t.isNotationNode(); }public  tom.library.adt.tnode.types.TNode tom_make_NotationNode( String  t0,  String  t1) { return  getTNodeFactory().makeTNode_NotationNode(t0, t1); }public  String  tom_get_slot_NotationNode_publicId( tom.library.adt.tnode.types.TNode t) { return  t.getPublicId(); }public  String  tom_get_slot_NotationNode_systemId( tom.library.adt.tnode.types.TNode t) { return  t.getSystemId(); }public Object tom_get_fun_sym_TNodeList( tom.library.adt.tnode.types.TNodeList t) { return null; }public boolean tom_cmp_fun_sym_TNodeList(Object s1, Object s2) { return  false; }public boolean tom_terms_equal_TNodeList(Object t1, Object t2) { return t1.equals(t2); }public  tom.library.adt.tnode.types.TNode tom_get_head_TNodeList( tom.library.adt.tnode.types.TNodeList l) { return l.getHead(); }public  tom.library.adt.tnode.types.TNodeList tom_get_tail_TNodeList( tom.library.adt.tnode.types.TNodeList l) { return l.getTail(); }public boolean tom_is_empty_TNodeList( tom.library.adt.tnode.types.TNodeList l) { return l.isEmpty(); }public void tom_check_stamp_TNodeList( tom.library.adt.tnode.types.TNodeList t) { if(t.getAnnotation(getTNodeFactory().getPureFactory().makeList()) == getTNodeFactory().getPureFactory().makeList())  return; else throw new RuntimeException("bad stamp"); }public  tom.library.adt.tnode.types.TNodeList tom_set_stamp_TNodeList( tom.library.adt.tnode.types.TNodeList t) { return (tom.library.adt.tnode.types.TNodeList)t.setAnnotation(getTNodeFactory().getPureFactory().makeList(),getTNodeFactory().getPureFactory().makeList()); }public  tom.library.adt.tnode.types.TNodeList tom_get_implementation_TNodeList( tom.library.adt.tnode.types.TNodeList t) { return t; }public boolean tom_is_fun_sym_concTNode( tom.library.adt.tnode.types.TNodeList t) { return (t!= null) && t.isSortTNodeList(); }public  tom.library.adt.tnode.types.TNodeList tom_empty_list_concTNode() { return getTNodeFactory().makeTNodeList(); }public  tom.library.adt.tnode.types.TNodeList tom_cons_list_concTNode( tom.library.adt.tnode.types.TNode e,  tom.library.adt.tnode.types.TNodeList l) { return getTNodeFactory().makeTNodeList(e,l); }public  tom.library.adt.tnode.types.TNodeList tom_append_list_concTNode( tom.library.adt.tnode.types.TNodeList l1,  tom.library.adt.tnode.types.TNodeList l2) {    if(tom_is_empty_TNodeList(l1)) {     return l2;    } else if(tom_is_empty_TNodeList(l2)) {     return l1;    } else if(tom_is_empty_TNodeList(( tom.library.adt.tnode.types.TNodeList)tom_get_tail_TNodeList(l1))) {     return ( tom.library.adt.tnode.types.TNodeList)tom_cons_list_concTNode(( tom.library.adt.tnode.types.TNode)tom_get_head_TNodeList(l1),l2);    } else {      return ( tom.library.adt.tnode.types.TNodeList)tom_cons_list_concTNode(( tom.library.adt.tnode.types.TNode)tom_get_head_TNodeList(l1),tom_append_list_concTNode(( tom.library.adt.tnode.types.TNodeList)tom_get_tail_TNodeList(l1),l2));    }   }  public  tom.library.adt.tnode.types.TNodeList tom_get_slice_concTNode( tom.library.adt.tnode.types.TNodeList begin,  tom.library.adt.tnode.types.TNodeList end) {    if(tom_terms_equal_TNodeList(begin,end)) {      return ( tom.library.adt.tnode.types.TNodeList)tom_empty_list_concTNode();    } else {      return ( tom.library.adt.tnode.types.TNodeList)tom_cons_list_concTNode(( tom.library.adt.tnode.types.TNode)tom_get_head_TNodeList(begin),( tom.library.adt.tnode.types.TNodeList)tom_get_slice_concTNode(( tom.library.adt.tnode.types.TNodeList)tom_get_tail_TNodeList(begin),end));    }   }  public boolean tom_is_fun_sym_emptyTNodeList( tom.library.adt.tnode.types.TNodeList t) { return  (t!= null) && t.isEmpty(); }public  tom.library.adt.tnode.types.TNodeList tom_make_emptyTNodeList() { return getTNodeFactory().makeTNodeList(); }public boolean tom_is_fun_sym_manyTNodeList( tom.library.adt.tnode.types.TNodeList t) { return  (t!= null) && t.isMany(); }public  tom.library.adt.tnode.types.TNodeList tom_make_manyTNodeList( tom.library.adt.tnode.types.TNode e,  tom.library.adt.tnode.types.TNodeList l) { return getTNodeFactory().makeTNodeList(e,l); }public  tom.library.adt.tnode.types.TNode tom_get_slot_manyTNodeList_head( tom.library.adt.tnode.types.TNodeList t) { return  t.getHead(); }public  tom.library.adt.tnode.types.TNodeList tom_get_slot_manyTNodeList_tail( tom.library.adt.tnode.types.TNodeList t) { return  t.getTail(); } 
  
  private TNodeFactory factory = null;
  private TNode nodeTerm = null;
  private boolean deleteWhiteSpaceNodes = false;
  private Hashtable ht_Nodes = new Hashtable();
  
  protected Collection getNodes(TNode key) {
    return (Collection)ht_Nodes.get(key);
  }

  public void setDeletingWhiteSpaceNodes(boolean b_d) {
    deleteWhiteSpaceNodes=b_d;
  }

  private TNodeFactory getTNodeFactory() {
    return factory;
  }

  public XMLToATerm () {
    factory = TNodeFactory.getInstance(SingletonFactory.getInstance());
  }

  public XMLToATerm(TNodeFactory factory) {
    this.factory = factory;
  }

  public XMLToATerm(String filename) {
    this();
    convert(filename);
  }

  public XMLToATerm(InputStream is) {
    this();
    convert(is);
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

  public void convert(InputStream is) {
    nodeTerm = xmlToATerm(convertToNode(is));
  } 
 
  public void convert(Node node) {
    nodeTerm = xmlToATerm(node);
  }
  
  public Node convertToNode(String filename) {
    try {
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      documentFactory.setValidating(true);
      DocumentBuilder db = documentFactory.newDocumentBuilder();
      return db.parse(filename);
    } catch (SAXException e) {
      System.err.println("XMLToATerm: "+ e.getMessage());
      //e.printStackTrace();
    } catch (IOException e) {
      System.err.println("XMLToATerm: "+ e.getMessage());
      //e.printStackTrace();
    } catch (ParserConfigurationException e) {
      System.err.println("XMLToATerm: "+ e.getMessage());
      //e.printStackTrace();
    } catch (FactoryConfigurationError e) {
      System.err.println("XMLToATerm: "+ e.getMessage());
      //e.printStackTrace();
    }
    return null;
  }

    public Node convertToNode(InputStream is) {
    try {
      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      return db.parse(is);
    } catch (SAXException e) {
      System.err.println("XMLToATerm: "+ e.getMessage());
      //e.printStackTrace();
    } catch (IOException e) {
      System.err.println("XMLToATerm: "+ e.getMessage());
      //e.printStackTrace();
    } catch (ParserConfigurationException e) {
      System.err.println("XMLToATerm: "+ e.getMessage());
      //e.printStackTrace();
    } catch (FactoryConfigurationError e) {
      System.err.println("XMLToATerm: "+ e.getMessage());
      //e.printStackTrace();
    }
    return null;
  }

  public TNodeList nodeListToAterm(NodeList list) {
    TNodeList res = tom_make_emptyTNodeList();
    for(int i=list.getLength()-1 ; i>=0 ; i--) {
      TNode elt = xmlToATerm(list.item(i));
      if(elt != null) {
        res = tom_make_manyTNodeList(elt,res);
      }
    }
    return res;
  }
  
  public TNodeList namedNodeMapToAterm(NamedNodeMap list) {
    TNodeList res = tom_make_emptyTNodeList();
    for(int i=list.getLength()-1 ; i>=0 ; i--) {
      TNode elt = xmlToATerm(list.item(i));
      if(elt != null) {
        res = tom_make_manyTNodeList(elt,res);
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
      System.err.println("We shouldn't find DocumentFragment in a freshly-parsed document");
      throw new RuntimeException("We shouldn't find DocumentFragment in a freshly-parsed document");
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
      throw new RuntimeException("The type of Node is unknown");
    } // switch
  }

  private TNode makeDocumentNode (Document doc){
    TNode doctype = xmlToATerm(doc.getDoctype());
    if (doctype == null) {
      doctype = tom_make_TextNode("");
    }
    TNode elem = xmlToATerm(doc.getDocumentElement());
    return tom_make_DocumentNode(doctype,elem);
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
    return tom_make_DocumentTypeNode(name,publicId,systemId,internalSubset,entitiesList,notationsList)
;
  }
    
  private TNode makeElementNode(Element elem) {
    TNodeList attrList  = namedNodeMapToAterm(elem.getAttributes());
      //System.out.println("attrList = " + attrList);
    attrList = sortAttributeList(attrList);
      //System.out.println("sorted attrList = " + attrList);
    TNodeList childList = nodeListToAterm(elem.getChildNodes());
    TNode result = tom_make_ElementNode(elem.getNodeName(),attrList,childList);
    Collection curCol = (Collection)ht_Nodes.get(elem);
    if (curCol==null) {
      curCol = new ArrayList();
    }
    curCol.add(elem);
    ht_Nodes.put(result,curCol);
    return result;
  }

  public TNodeList sortAttributeList(TNodeList attrList) {
    TNodeList res = tom_make_emptyTNodeList();
    while(!attrList.isEmpty()) {
      res = insertSortedAttribute(attrList.getHead(),res);
      attrList = attrList.getTail();
    }
    return res;
  }

  private TNodeList insertSortedAttribute(TNode elt, TNodeList list) {
     { tom.library.adt.tnode.types.TNode tom_match1_1=(( tom.library.adt.tnode.types.TNode)elt);{ { tom.library.adt.tnode.types.TNodeList tom_match1_2=(( tom.library.adt.tnode.types.TNodeList)list);{ if(tom_is_fun_sym_AttributeNode(tom_match1_1) ||  false ) { { String  tom_match1_1_1=tom_get_slot_AttributeNode_name(tom_match1_1); { String  name1=tom_match1_1_1; if(tom_is_fun_sym_emptyTNodeList(tom_match1_2) ||  false ) {

        return tom_make_manyTNodeList(elt,list);
       }}} } if(tom_is_fun_sym_AttributeNode(tom_match1_1) ||  false ) { { String  tom_match1_1_1=tom_get_slot_AttributeNode_name(tom_match1_1); { String  name1=tom_match1_1_1; if(tom_is_fun_sym_manyTNodeList(tom_match1_2) ||  false ) { { tom.library.adt.tnode.types.TNode tom_match1_2_1=tom_get_slot_manyTNodeList_head(tom_match1_2); { tom.library.adt.tnode.types.TNodeList tom_match1_2_2=tom_get_slot_manyTNodeList_tail(tom_match1_2); if(tom_is_fun_sym_AttributeNode(tom_match1_2_1) ||  false ) { { tom.library.adt.tnode.types.TNode head=tom_match1_2_1; { String  tom_match1_2_1_1=tom_get_slot_AttributeNode_name(tom_match1_2_1); { String  name2=tom_match1_2_1_1; { tom.library.adt.tnode.types.TNodeList tail=tom_match1_2_2;


        if(name1.compareTo(name2) >= 0) {
          return tom_make_manyTNodeList(head,insertSortedAttribute(elt,tail));
        } else {
          return tom_make_manyTNodeList(elt,list);
        }
      }}}} }}} }}} }}}}}

    System.err.println("insertSortedAttribute: Strange case");
    return list;
  }
    
  private TNode makeAttributeNode(Attr attr) {
    String specif = (attr.getSpecified() ? "true" : "false");
    return tom_make_AttributeNode(attr.getNodeName(),specif,attr.getNodeValue());
  }

  private TNode makeTextNode(Text text) {
    if (!deleteWhiteSpaceNodes) {
      return tom_make_TextNode(text.getData());
    }
    if (!text.getData().trim().equals("")) {
      return tom_make_TextNode(text.getData());
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
    return tom_make_EntityNode(nn,publicId,systemId);
  }

  private TNode makeEntityReferenceNode(EntityReference er) {
    TNodeList list = nodeListToAterm(er.getChildNodes());
    return tom_make_EntityReferenceNode(er.getNodeName(),list);
  }

  private TNode makeNotationNode(Notation notation) {
    String publicId = notation.getPublicId();
    publicId = (publicId == null ? "UNDEF" : publicId);
    String systemId = notation.getSystemId();
    systemId = (systemId == null ? "UNDEF" : systemId);
    return tom_make_NotationNode(publicId,systemId);
  }

  private TNode makeCommentNode(Comment comment) {
    return tom_make_CommentNode(comment.getData());
  }
  
  private TNode makeCDATASectionNode(CDATASection cdata) {
    return tom_make_CDATASectionNode(cdata.getData());
  }

  private TNode makeProcessingInstructionNode(ProcessingInstruction instr) {
    return tom_make_ProcessingInstructionNode(instr.getTarget(),instr.getData());
  }

}
