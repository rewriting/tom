/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2006, Pierre-Etienne Moreau
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

import java.io.*;
import java.util.Collection;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import aterm.*;
import aterm.pure.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

public class XmlTools {

  private ATermToXML a2x;
  private XMLToATerm x2a;
  private TNodeFactory ntf = null;

  public XmlTools () {
    ntf = TNodeFactory.getInstance(SingletonFactory.getInstance());
    a2x = new ATermToXML(ntf);
    x2a = new XMLToATerm(ntf);
  }

  public void setDeletingWhiteSpaceNodes(boolean b_d) {
    x2a.setDeletingWhiteSpaceNodes(b_d);
  }

  public TNodeFactory getTNodeFactory() {
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

  public Node convertToNode(InputStream is) {
    return x2a.convertToNode(is);
  }

  /**
   * Returns all TNodes contained in TNode n
   * @param n root to collect all TNodes in the tree
   */
  public Collection getNodes(TNode n) {
    return x2a.getNodes(n);
  }

  /**
   * converts XML to ATerm
   * @param filename input representing ATerm
   */
  public ATerm convertXMLToATerm(String filename) {
    x2a.convert(filename);
    return x2a.getATerm();
  }

  /**
   * converts XML to ATerm
   * @param is input representing ATerm
   */
  public ATerm convertXMLToATerm(InputStream is) {
    x2a.convert(is);
    return x2a.getATerm();
  }

  /**
   * print ATerm t in XML format, without any extra character (ie:\n)
   * @param an ATerm to print
   */
  public void printXMLFromATerm(ATerm t) {
    a2x.setWriter(null);
    a2x.setOutputStream(System.out);
    a2x.convert(t);
  }

  /**
   * given a filename, print ATerm in XML format, without any extra character (ie:\n)
   * @param filename the filename, containing Aterm
   */
  public void printXMLFromATermFile(String filename) {
    a2x.setWriter(null);
    a2x.setOutputStream(System.out);
    a2x.convert(filename);
  }

  /**
   * write ATerm in XML format, without any extra character (ie:\n)
   * @see ATerm
   */
  public void writeXMLFileFromATerm(Writer writer, ATerm t) {
    a2x.setWriter(writer);
    a2x.setOutputStream(null);
    a2x.convert(t);
  }

  /**
   * Constructs TNode in XML
   * @param t this parameter can be either a TNode, a TNode list, or #TEXT("...").
   */
  public String xml(TNode t) {
    return a2x.xml(t);
  }

}
