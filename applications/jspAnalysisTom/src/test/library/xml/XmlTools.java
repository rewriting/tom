/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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

package test.library.xml;

import java.io.*;
import java.util.Collection;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import test.tnode.*;
import test.tnode.types.*;

public class XmlTools {

  private TNodeToXML t2x;
  private XMLToTNode x2t;

  public XmlTools () {
    t2x = new TNodeToXML();
    x2t = new XMLToTNode();
  }

  public void setDeletingWhiteSpaceNodes(boolean b_d) {
    x2t.setDeletingWhiteSpaceNodes(b_d);
  }

  public TNode nodeToTNode(Node node) {
    return x2t.xmlToTNode(node);
  }

  public TNodeList nodeListToAterm(NodeList n) {
    return x2t.nodeListToAterm(n);
  }

  public Node convertToNode(String filename) {
    return x2t.convertToNode(filename);
  }

  public Node convertToNode(InputStream is) {
    return x2t.convertToNode(is);
  }

  /**
   * Returns all TNodes contained in TNode n
   * @param n root to collect all TNodes in the tree
   */
  public Collection getNodes(TNode n) {
    return x2t.getNodes(n);
  }

  /**
   * converts XML to TNode
   * @param filename input representing XML
   */
  public TNode convertXMLToTNode(String filename) {
    x2t.convert(filename);
    return x2t.getTNode();
  }

  /**
   * converts XML to TNode
   * @param is input representing XML
   */
  public TNode convertXMLToTNode(InputStream is) {
    x2t.convert(is);
    return x2t.getTNode();
  }

  /**
   * print TNode t in XML format, without any extra character (ie:\n)
   * @param an TNode to print
   */
  public void printXMLFromTNode(TNode t) {
    t2x.setWriter(null);
    t2x.setOutputStream(System.out);
    t2x.tnodeToXML(t);
  }

  /**
   * write TNode in XML format, without any extra character (ie:\n)
   * @see TNode
   */
  public void writeXMLFileFromTNode(Writer writer, TNode t) {
    t2x.setWriter(writer);
    t2x.setOutputStream(null);
    t2x.tnodeToXML(t);
  }

  /**
   * Constructs TNode in XML
   * @param t this parameter can be either a TNode, a TNode list, or #TEXT("...").
   */
  public String xml(TNode t) {
    return t2x.xml(t);
  }

}
