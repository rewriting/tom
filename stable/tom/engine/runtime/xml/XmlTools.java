/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003 INRIA
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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import aterm.*;
import aterm.pure.*;
import jtom.runtime.xml.adt.tnode.*;
import jtom.runtime.xml.adt.tnode.types.*;

public class XmlTools {

  private ATermToXML a2x;
  private XMLToATerm x2a;
  private Factory ntf = null;

  public XmlTools () {
    ntf = new Factory(new PureFactory());
    a2x = new ATermToXML(ntf);
    x2a = new XMLToATerm(ntf);
  }

  public void setDeletingWhiteSpaceNodes(boolean b_d) {
    x2a.setDeletingWhiteSpaceNodes(b_d);
  }

  public Factory getTNodeFactory() {
    return ntf;
  }

  public TNode nodeToATerm(Node node) {
    return x2a.xmlToATerm(node);
  }

  public TNodeList nodeListToAterm(NodeList n) {
    return x2a.nodeListToAterm(n);
  }

  public Node convertToNode(String filename) {
    return x2a.convertToNode(filename);
  }

  public ATerm convertXMLToATerm(String filename) {
    x2a.convert(filename);
    return x2a.getATerm();
  }

  public void printXMLFromATerm(ATerm t) {
    a2x.setWriter(null);
    a2x.setOutputStream(System.out);
    a2x.convert(t);
  }
    
  public void printXMLFromATermFile(String filename) {
    a2x.setWriter(null);
    a2x.setOutputStream(System.out);
    a2x.convert(filename);
  }

  public void writeXMLFileFromATerm(Writer writer, ATerm t) {
    a2x.setWriter(writer);
    a2x.setOutputStream(null);
    a2x.convert(t);
  }

}
