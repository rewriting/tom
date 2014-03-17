/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
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
 */
package xquery.lib; 

import org.w3c.dom.*;
//import xquery.util.DomTree02;


public class NodeTraversal {

    /*
     * Traverse a subject and collect
     */
  public void genericCollect(Object subject, Collect1 collect) {
    genericCollectArray(subject, collect, new Object[] {});
  }

  public void genericCollect(Object subject, Collect2 collect, Object arg1) {
    genericCollectArray(subject, collect, new Object[] {arg1});
  }

  public void genericCollect(Object subject, Collect3 collect, Object arg1, Object arg2) {
    genericCollectArray(subject, collect, new Object[] {arg1,arg2});
  }

  public void genericCollect(Object subject, Collect4 collect, Object arg1, Object arg2, Object arg3) {
    genericCollectArray(subject, collect, new Object[] {arg1,arg2,arg3});
  }
 
    /*
     * Traverse a subject and collect
     * %all(subject, collect(vTable,subject,f)); 
     */
  protected void genericCollectArray(Object subject, Collect collect, Object[] args) 
  {
	//	DomTree02 dt = new DomTree02(); 

    try {
      if(collect.apply(subject,args)) { 
        if(subject instanceof Node) { 
		  Node node = (Node)subject; 
		  // 		  System.out.println("=========================================================================================");
// 		  if (node.getNodeName().compareTo("#text")!=0) {
// 			System.out.println(node.getNodeName() + ":" + level + ":" + childNumber);
// 			System.out.println(node.getChildNodes().getLength());
// 		  }
// 		  if (node.getNodeName().compareTo("book") ==0)
// 			System.out.println("Book found");
		  
// 		  if (node.getNodeName().compareTo("#document") ==0) {
// 			System.out.println(node.getChildNodes().getLength());
// 		  }
		  
		  
		  // travers all childs
		  if (node.hasChildNodes()) {
			NodeList childList = node.getChildNodes();
			for (int i=0; i < childList.getLength(); i++) {
			  genericCollectArray(childList.item(i), collect, args); 
			}
		  }

		  if (node.hasAttributes()) {
			NamedNodeMap attrList=node.getAttributes();
			for (int i=0; i < attrList.getLength(); i++) {
			  genericCollectArray(attrList.item(i), collect, args); 
			}
		  }
		} 

		else if(subject instanceof NodeList) {
		  NodeList nodelist = (NodeList)subject; 
		  for (int i=0; i < nodelist.getLength(); i++) {
			genericCollectArray(nodelist.item(i), collect, args); 
		  }
		}

	  }
	}
  	catch(Exception e) {
      e.printStackTrace();
      System.out.println("Please, extend genericCollectArray");
      throw new RuntimeException("Please, extend genericCollectArray");
    }
  }

//   protected Node genericTraversalArray(Node subject, Replace replace, Object[] args) {
//     Node res = subject;
    
//     if(subject instanceof NodeAppl) { 
//       res = genericMapterm((NodeAppl) subject, replace, args);
//     } else if(subject instanceof NodeList) {
//       res = genericMap((NodeList) subject, replace, args);
//     } else if(subject instanceof NodeInt) {
//       res = subject;
//     } else {
//       System.out.println("Please, extend genericTraversalArray.."+subject);
//       throw new RuntimeException("Please, extend genericTraversalArray.."+subject);
//     }
//     return res;
//   } 

 
//     /*
//      * Apply a function to each element of a list
//      */
//   private NodeList genericMap(NodeList subject, Replace replace, Object[] args) {
//     NodeList res = subject;

//      res = subject.getEmpty();
//     while(!subject.isEmpty()) {
//       Node term = replace.apply(subject.getFirst(),args);
//       res = res.insert(term);
//       subject = subject.getNext();
//     }
//     res = res.reverse();
//     return res;
//   }

//     /*
//      * Apply a function to each subterm of a term
//      */
//   private NodeAppl genericMapterm(NodeAppl subject, Replace replace, Object[] args) {
//     Node newSubterm;
//     for(int i=0 ; i<subject.getArity() ; i++) {
//       newSubterm = replace.apply(subject.getArgument(i),args);
//       if(newSubterm != subject.getArgument(i)) {
//         subject = subject.setArgument(newSubterm,i);
//       }
//     }
//     return subject;
//   }

 
//   public void genericCollectReach(Node subject, CollectReach collect,
//                                  Collection collection) {
//     try {
//       if(subject instanceof NodeAppl) {
//         Node newSubterm;
//         NodeAppl subjectAppl = (NodeAppl) subject;
//         collect.apply(subject,collection);
//         for(int i=0 ; i<subjectAppl.getArity() ; i++) {
//           Collection tmpCollection = new ArrayList();
//           genericCollectReach(subjectAppl.getArgument(i),collect,tmpCollection);
//           Iterator it = tmpCollection.iterator();
//           while(it.hasNext()) {
//             collection.add(subjectAppl.setArgument((Node)it.next(),i));
//           }
//         }
//       } 
//     } catch(Exception e) {
//       System.out.println("exception: " + e);
//       System.out.println("Please, extend genericCollectReplace "+e.getStackTrace());
//       throw new RuntimeException("Please, extend genericTraversalArray.."+subject);
//     }
//   } 

  
}
