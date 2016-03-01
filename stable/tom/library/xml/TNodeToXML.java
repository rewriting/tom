/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
 *  - Neither the name of the Inria nor the names of its
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
 * Inria, Nancy, France
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.library.xml;

import java.io.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

public class TNodeToXML {

       private static   tom.library.adt.tnode.types.TNodeList  tom_append_list_concTNode( tom.library.adt.tnode.types.TNodeList l1,  tom.library.adt.tnode.types.TNodeList  l2) {     if( l1.isEmptyconcTNode() ) {       return l2;     } else if( l2.isEmptyconcTNode() ) {       return l1;     } else if(  l1.getTailconcTNode() .isEmptyconcTNode() ) {       return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( l1.getHeadconcTNode() ,l2) ;     } else {       return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( l1.getHeadconcTNode() ,tom_append_list_concTNode( l1.getTailconcTNode() ,l2)) ;     }   }   private static   tom.library.adt.tnode.types.TNodeList  tom_get_slice_concTNode( tom.library.adt.tnode.types.TNodeList  begin,  tom.library.adt.tnode.types.TNodeList  end, tom.library.adt.tnode.types.TNodeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTNode()  ||  (end== tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( begin.getHeadconcTNode() ,( tom.library.adt.tnode.types.TNodeList )tom_get_slice_concTNode( begin.getTailconcTNode() ,end,tail)) ;   }    

  public static final String UNDEF = "UNDEF";

  private OutputStream out = System.out;
  private Writer writer = null;

  /**
   * Set up the output stream
   *
   * @param out the output stream to set up
   */
  public void setOutputStream(OutputStream out){
    this.out = out;
  }

  /**
   * Set up the writer
   *
   * @param writer the writer to set up
   */
  public void setWriter(Writer writer) {
    this.writer = writer;
  }

  /**
   * Return a string representation of a TNode
   *
   * @param n the TNode which is converted into xml
   * @return a string representation of the xml node
   */
  public String xml(TNode n) {
		StringWriter str_res = new StringWriter();
		setWriter(str_res);
		tnodeToXML(n);
		return str_res.toString();
  }

  /**
   * Convert a TNode to XML
   *
   * @param n the TNode to convert
   * @throws RuntimeException in case of problem in DocumentTypeNode
   */
  public void tnodeToXML(TNode n) {
    {{if ( (n instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )n) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )(( tom.library.adt.tnode.types.TNode )n)) instanceof tom.library.adt.tnode.types.tnode.DocumentNode) ) {

        write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        tnodeToXML( (( tom.library.adt.tnode.types.TNode )n).getDocType() );
        tnodeToXML( (( tom.library.adt.tnode.types.TNode )n).getDocElem() );
        write("\n");
        return;
      }}}}{if ( (n instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )n) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )(( tom.library.adt.tnode.types.TNode )n)) instanceof tom.library.adt.tnode.types.tnode.DocumentTypeNode) ) { String  tom_publicId= (( tom.library.adt.tnode.types.TNode )n).getPublicId() ; String  tom_systemId= (( tom.library.adt.tnode.types.TNode )n).getSystemId() ; String  tom_internalSubset= (( tom.library.adt.tnode.types.TNode )n).getInternalSubset() ;

        write("<!DOCTYPE "+ (( tom.library.adt.tnode.types.TNode )n).getName() );
        if (!tom_publicId.equals(TNodeToXML.UNDEF) && !tom_systemId.equals(TNodeToXML.UNDEF))
          write(" PUBLIC \""+tom_publicId+"\" \"");
        else if (!tom_systemId.equals(TNodeToXML.UNDEF) && tom_publicId.equals(TNodeToXML.UNDEF))
          write(" SYSTEM \"");
        else {
          System.out.println("Problem in DocumentTypeNode");
          throw new RuntimeException("Problem in DocumentTypeNode");
        }
        write(tom_systemId+"\"");
        if (!tom_internalSubset.equals(TNodeToXML.UNDEF)) {
          write(" ["+tom_internalSubset+"]");
        }
        write(">\n");
        return;
      }}}}{if ( (n instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )n) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )(( tom.library.adt.tnode.types.TNode )n)) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch755_17= (( tom.library.adt.tnode.types.TNode )n).getChildList() ;if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch755_17) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch755_17) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( tomMatch755_17.isEmptyconcTNode() ) {

        write("<"+ (( tom.library.adt.tnode.types.TNode )n).getName() );
        tnodeListToXML( (( tom.library.adt.tnode.types.TNode )n).getAttrList() );
        write("/>");
        return;
      }}}}}}{if ( (n instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )n) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )(( tom.library.adt.tnode.types.TNode )n)) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { String  tom_name= (( tom.library.adt.tnode.types.TNode )n).getName() ;

        write("<"+tom_name);
        tnodeListToXML( (( tom.library.adt.tnode.types.TNode )n).getAttrList() );
        write(">");
        tnodeListToXML( (( tom.library.adt.tnode.types.TNode )n).getChildList() );
        write("</"+tom_name+">");
        return;
      }}}}{if ( (n instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )n) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )(( tom.library.adt.tnode.types.TNode )n)) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {

        if ( (( tom.library.adt.tnode.types.TNode )n).getSpecified() .equals("true")) {
          write(" " +  (( tom.library.adt.tnode.types.TNode )n).getName() + "=\"" +  (( tom.library.adt.tnode.types.TNode )n).getValue() + "\"");
        }
        return;
      }}}}{if ( (n instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )n) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )(( tom.library.adt.tnode.types.TNode )n)) instanceof tom.library.adt.tnode.types.tnode.TextNode) ) {

        write( (( tom.library.adt.tnode.types.TNode )n).getData() );
        return;
      }}}}{if ( (n instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )n) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )(( tom.library.adt.tnode.types.TNode )n)) instanceof tom.library.adt.tnode.types.tnode.CommentNode) ) {

        write("<!-- "+ (( tom.library.adt.tnode.types.TNode )n).getData() +" -->");
        return;
      }}}}{if ( (n instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )n) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )(( tom.library.adt.tnode.types.TNode )n)) instanceof tom.library.adt.tnode.types.tnode.CDATASectionNode) ) {

        write("<![CDATA["+ (( tom.library.adt.tnode.types.TNode )n).getData() +"]]>");
        return;
      }}}}{if ( (n instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )n) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )(( tom.library.adt.tnode.types.TNode )n)) instanceof tom.library.adt.tnode.types.tnode.ProcessingInstructionNode) ) {

        write("<?"+ (( tom.library.adt.tnode.types.TNode )n).getTarget() +" "+ (( tom.library.adt.tnode.types.TNode )n).getData() +"?>");
        return;
      }}}}{if ( (n instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )n) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )(( tom.library.adt.tnode.types.TNode )n)) instanceof tom.library.adt.tnode.types.tnode.EntityReferenceNode) ) {

        write("&"+ (( tom.library.adt.tnode.types.TNode )n).getName() +";");
        return;
      }}}}{if ( (n instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )n) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )(( tom.library.adt.tnode.types.TNode )n)) instanceof tom.library.adt.tnode.types.tnode.EmptyNode) ) {

        return;
      }}}}{if ( (n instanceof tom.library.adt.tnode.types.TNode) ) {

        System.out.println("Unknown type of TNode : "+n);
      }}}

    write("\n");
  }

  private void tnodeListToXML(TNodeList list) {
    if(list.isEmptyconcTNode()) {
      return;
    }
    TNode t = list.getHeadconcTNode();
    TNodeList l = list.getTailconcTNode();
    tnodeToXML(t);
    tnodeListToXML(l);
  }

  private void write(String s) {
    try {
      if (out != null) {
        out.write(s.getBytes("UTF-8"));
    }
      if (writer != null) {
    writer.write(s);
    }
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }

}
