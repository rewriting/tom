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

package jtom.runtime;

import aterm.*;
import aterm.pure.*;
import java.util.*;

public class GenericTraversal {

    /*
     * collects something in table
     * returns false if no more traversal is needed
     * returns true  if traversal has to be continued
     */
  protected interface Collect {
    boolean apply(ATerm t);
  }

  protected interface Replace {
    ATerm apply(ATerm t);
  }

    /*
     * Apply a function to each element of a list
     */
  protected ATermList genericMap(ATermList subject, Replace replace) {
    ATermList res = subject;
    try {
      if(!subject.isEmpty()) {
        ATerm term = replace.apply(subject.getFirst());
        ATermList list = genericMap(subject.getNext(),replace);
        res = list.insert(term);
      }
    } catch(Exception e) {
      System.out.println("Please, extend genericMapterm");
      System.exit(0);
    }
    return res;
  }

    /*
     * Apply a function to each subterm of a term
     */
  protected ATermAppl genericMapterm(ATermAppl subject, Replace replace) {
    try {
      ATerm newSubterm;
      for(int i=0 ; i<subject.getArity() ; i++) {
        newSubterm = replace.apply(subject.getArgument(i));
        if(newSubterm != subject.getArgument(i)) {
          subject = subject.setArgument(newSubterm,i);
        }
      }
    } catch(Exception e) {
      System.out.println("Please, extend genericMapterm");
      System.exit(0);
    }
    return subject;
  }
  
    /*
     * Traverse a subject and collect
     * %all(subject, collect(vTable,subject,f)); 
     */
  protected void genericCollect(ATerm subject, Collect collect) {
    try {
      if(collect.apply(subject)) { 
        if(subject instanceof ATermAppl) { 
          ATermAppl subjectAppl = (ATermAppl) subject; 
          for(int i=0 ; i<subjectAppl.getArity() ; i++) {
            ATerm term = subjectAppl.getArgument(i);
            genericCollect(term,collect); 
          } 
        } else if(subject instanceof ATermList) { 
          ATermList subjectList = (ATermList) subject; 
          while(!subjectList.isEmpty()) { 
            genericCollect(subjectList.getFirst(),collect); 
            subjectList = subjectList.getNext(); 
          } 
        } 
      }
    } catch(Exception e) {
      System.out.println("Please, extend genericCollect");
      System.exit(0);
    }
  } 

    /*
     * Traverse a subject and replace
     */
  protected ATerm genericTraversal(ATerm subject, Replace replace) {
    ATerm res = subject;
    try {
      if(subject instanceof ATermAppl) { 
        res = genericMapterm((ATermAppl) subject,replace);
      } else if(subject instanceof ATermList) {
        res = genericMap((ATermList) subject,replace);
      }
    } catch(Exception e) {
      System.out.println("Please, extend genericTraversal");
      System.exit(0);
    }
    return res;
  } 

  protected interface Collect2 {
    boolean apply(ATerm t, Collection c);
  }

  
  protected void genericCollect2(ATerm subject, Collect2 collect,
                                 Collection collection) {
    try {
      if(subject instanceof ATermAppl) {
        ATerm newSubterm;
        ATermAppl subjectAppl = (ATermAppl) subject;
        collect.apply(subject,collection);
        for(int i=0 ; i<subjectAppl.getArity() ; i++) {
          Collection tmpCollection = new ArrayList();
          genericCollect2(subjectAppl.getArgument(i),collect,tmpCollection);
          Iterator it = tmpCollection.iterator();
          while(it.hasNext()) {
            collection.add(subjectAppl.setArgument((ATerm)it.next(),i));
          }
        }
      } 
    } catch(Exception e) {
      System.out.println("Please, extend genericCollectReplace");
      System.exit(0);
    }
  } 

  
}
