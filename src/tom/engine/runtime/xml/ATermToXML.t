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
import jtom.exception.TomRuntimeException;

public class ATermToXML {
  
  %include{ adt/TNode.tom }

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
    %match(TNode n) {
      DocumentNode(docType,docElem) -> {
				write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				atermToXML(docType);
				atermToXML(docElem);
				write("\n");
				return;
      }
      DocumentTypeNode(name,publicId,systemId,internalSubset,_,_) -> {
				write("<!DOCTYPE "+name);
				if (!publicId.equals("UNDEF") && !systemId.equals("UNDEF"))
	  			write(" PUBLIC \""+publicId+"\" \"");
       	else if (!systemId.equals("UNDEF") && publicId.equals("UNDEF"))
	  			write(" SYSTEM \"");
				else {
	  			System.out.println("Problem in DocumentTypeNode");
	  			throw new TomRuntimeException(new Throwable("Problem in DocumentTypeNode"));
				}
				write(systemId+"\"");
				if (!internalSubset.equals("UNDEF")) 
	  			write(" ["+internalSubset+"]");
					write(">\n");
					return;
      }
      ElementNode(name,attrList,concTNode()) -> {
				write("<"+name);
				atermToXMLList(attrList);
				write("/>");
				return;
      }
      ElementNode(name,attrList,childList) -> {
				write("<"+name);
				atermToXMLList(attrList);
				write(">");
				atermToXMLList(childList);
				write("</"+name+">");
				return;
      }
      AttributeNode(name,specified,child) -> {
				if (specified.equals("true")) {
	  			write(" "+name+"=\"");
	  			atermToXML(child);
	  			write("\"");
				}
				return;
      }
      TextNode(data) -> {
				write(data);
				return;
      }
      CommentNode(data) -> {
				write("<!-- "+data+" -->");
				return;
      }
      CDATASectionNode(data) -> {
				write("<![CDATA["+data+"]]>");
				return;
      }
      ProcessingInstructionNode(target,data) -> {
				write("<?"+target+" "+data+"?>");
				return;
      }
      EntityReferenceNode(name,childList) -> {
				write("&"+name+";");
				return;
      }
      _ -> {
				System.out.println("Unknown type of TNode : "+n);
      }
    }
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
      if (out != null) {
		out.write(s.getBytes());
		}
      if (writer != null) {
		writer.write(s);
	  }
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      throw new TomRuntimeException(new Throwable(e.getMessage()));
    }
  }

}
