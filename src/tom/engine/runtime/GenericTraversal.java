/*
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

package jtom.runtime;

import aterm.*;
import java.util.*;

public class GenericTraversal {

    /*
     * Traverse a subject and collect
     */
  public void genericCollect(ATerm subject, Collect1 collect) {
    genericCollectArray(subject, collect, new ATerm[] {});
  }

  public void genericCollect(ATerm subject, Collect2 collect, Object arg1) {
    genericCollectArray(subject, collect, new Object[] {arg1});
  }

  public void genericCollect(ATerm subject, Collect3 collect, Object arg1, Object arg2) {
    genericCollectArray(subject, collect, new Object[] {arg1,arg2});
  }

  public void genericCollect(ATerm subject, Collect4 collect, Object arg1, Object arg2, Object arg3) {
    genericCollectArray(subject, collect, new Object[] {arg1,arg2,arg3});
  }
 
    /*
     * Traverse a subject and replace
     */
  public ATerm genericTraversal(ATerm subject, Replace1 replace){
    return genericTraversalArray(subject, replace, new ATerm[] {});
  }

  public ATerm genericTraversal(ATerm subject, Replace2 replace, Object arg1){
    return genericTraversalArray(subject, replace, new Object[] {arg1});
  }

  public ATerm genericTraversal(ATerm subject, Replace3 replace, Object arg1, Object arg2) {
    return genericTraversalArray(subject, replace, new Object[] {arg1,arg2});
  }
  
    /*
     * Traverse a subject and collect
     * %all(subject, collect(vTable,subject,f)); 
     */
  protected void genericCollectArray(ATerm subject, Collect collect, Object[] args) {
    try {
      if(collect.apply(subject,args)) { 
        if(subject instanceof ATermAppl) { 
          ATermAppl subjectAppl = (ATermAppl) subject; 
          for(int i=0 ; i<subjectAppl.getArity() ; i++) {
            ATerm term = subjectAppl.getArgument(i);
            genericCollectArray(term,collect,args); 
          } 
        } else if(subject instanceof ATermList) { 
          ATermList subjectList = (ATermList) subject; 
          while(!subjectList.isEmpty()) { 
            genericCollectArray(subjectList.getFirst(),collect,args); 
            subjectList = subjectList.getNext(); 
          } 
        } else if(subject instanceof ATermInt) {
          ATermInt subjectInt = (ATermInt) subject;
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
      System.out.println("Please, extend genericCollectArray");
      throw new RuntimeException(new Throwable("Please, extend genericCollectArray"));
    }
  } 

  protected ATerm genericTraversalArray(ATerm subject, Replace replace, Object[] args) {
    ATerm res = subject;
    
    if(subject instanceof ATermAppl) { 
      res = genericMapterm((ATermAppl) subject, replace, args);
    } else if(subject instanceof ATermList) {
      res = genericMap((ATermList) subject, replace, args);
    } else if(subject instanceof ATermInt) {
      res = subject;
    } else {
      System.out.println("Please, extend genericTraversalArray.."+subject);
      throw new RuntimeException(new Throwable("Please, extend genericTraversalArray.."+subject));
    }
    return res;
  } 

 
    /*
     * Apply a function to each element of a list
     */
  private ATermList genericMap(ATermList subject, Replace replace, Object[] args) {
    ATermList res = subject;

     res = subject.getEmpty();
    while(!subject.isEmpty()) {
      ATerm term = replace.apply(subject.getFirst(),args);
      res = res.insert(term);
      subject = subject.getNext();
    }
    res = res.reverse();
    return res;
  }

    /*
     * Apply a function to each subterm of a term
     */
  private ATermAppl genericMapterm(ATermAppl subject, Replace replace, Object[] args) {
    ATerm newSubterm;
    for(int i=0 ; i<subject.getArity() ; i++) {
      newSubterm = replace.apply(subject.getArgument(i),args);
      if(newSubterm != subject.getArgument(i)) {
        subject = subject.setArgument(newSubterm,i);
      }
    }
    return subject;
  }

 
  public void genericCollectReach(ATerm subject, CollectReach collect,
                                 Collection collection) {
    try {
      if(subject instanceof ATermAppl) {
        ATerm newSubterm;
        ATermAppl subjectAppl = (ATermAppl) subject;
        collect.apply(subject,collection);
        for(int i=0 ; i<subjectAppl.getArity() ; i++) {
          Collection tmpCollection = new ArrayList();
          genericCollectReach(subjectAppl.getArgument(i),collect,tmpCollection);
          Iterator it = tmpCollection.iterator();
          while(it.hasNext()) {
            collection.add(subjectAppl.setArgument((ATerm)it.next(),i));
          }
        }
      } 
    } catch(Exception e) {
      System.out.println("exception: " + e);
      System.out.println("Please, extend genericCollectReplace "+e.getStackTrace());
      throw new RuntimeException(new Throwable("Please, extend genericTraversalArray.."+subject));
    }
  } 

  
}
