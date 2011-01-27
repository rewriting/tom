/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
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
 */

/*
 * Created on 19 janv. 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

package xml.dommapping;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.parsers.*;


/**
 * @author bertrand.tavernier
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class domtomTest2 {

  %include{ dom_1_5.tom }

		
  private Document dom;

  public void run() {
    try {		
      System.out.println("Debut des tests ");
        /*Tests sur le document*/
      System.out.println("\nTest de nouveau document :");
      dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      dom.appendChild(dom.createElement("Test"));
      Node root = null;
      %match(TDocument dom) {
        DocumentNode[DocElem=r] -> {
          root = `r;
        }
      }
      System.out.println("ElementNode : "+root);

        /*Creation d'un document XML :*/
      System.out.println("\nTest de creation : ");


      root.appendChild(`xml(dom,<Fils1 At1="V1" Bt1="V1"> 
                            <SousFils><SousousFils></SousousFils></SousFils>
                            <SousFils2></SousFils2>
                            </Fils1>));

      /*
      root.appendChild(`xml(dom,<Fils1 At1="V2"> 
                            <SousFils><SousousFils></SousousFils></SousFils>
                            <SousFils2></SousFils2>
                            </Fils1>));

      root.appendChild(`xml(dom,<Fils1 At1="V1"> 
                            <SousFils><SousousFils></SousousFils></SousFils>
                            <SousFils2></SousFils2>
                            </Fils1>));
      System.out.println(root);
      */
      /*Matching du document
      System.out.println("\nTest de matching : ");
      %match(TNode root) {
        <_><Fils1 At1=val></Fils1></_> -> {
           System.out.println("Attribut At1 trouve, valeur : "+val);
         }
      }

      System.out.println("\nTest de listmatching avec non-linearite");
      %match(TNode root) {
        <_><_ At1=val></_><_ At1=val></_></_> -> {
           System.out.println("Pattern non lineaire matche avec : "+val);
         }
      }
      */
      System.out.println("\nTest de l'ordre des attributs");
      %match(TNode root) {
              <_><Fils1 Bt1=val At1=val  ></Fils1></_> -> {
             /*<_><Fils1 At1=val Bt1=val  ></Fils1></_> -> {*/
           System.out.println("ordre ok : "+`val);
         }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new domtomTest2().run();
		
  }


}
