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

  private class Replace {
    protected ATerm apply(ATerm t, Object[] args) {
      int length = args.length;
      switch(length) {
          case 0: return ((Replace1)this).apply(t);
          case 1: return ((Replace2)this).apply(t, args[0]);
          case 2: return ((Replace3)this).apply(t, args[0], args[1]);
          default:
            System.out.println("Extend Replace.apply to " + length + " arguments");
            System.exit(1);
      }
      return t;
    }
  }

  protected abstract class Replace1 extends Replace {
    abstract public ATerm apply(ATerm t);
  }

  protected abstract class Replace2 extends Replace {
    abstract public ATerm apply(ATerm t, Object arg1);
  }

  protected abstract class Replace3 extends Replace {
    abstract public ATerm apply(ATerm t, Object arg1, Object arg2);
  }
    /*
     * Apply a function to each element of a list
     */
  protected ATermList genericMap(ATermList subject, Replace replace, Object[] args) {
    ATermList res = subject;
    try {
      if(!subject.isEmpty()) {
        ATerm term = replace.apply(subject.getFirst(),args);
        ATermList list = genericMap(subject.getNext(),replace, args);
        res = list.insert(term);
      }
    } catch(Exception e) {
      System.out.println("Please, extend genericMap");
      System.exit(0);
    }
    return res;
  }

    /*
     * Apply a function to each subterm of a term
     */
  protected ATermAppl genericMapterm(ATermAppl subject, Replace replace, Object[] args) {
    try {
      ATerm newSubterm;
      for(int i=0 ; i<subject.getArity() ; i++) {
        newSubterm = replace.apply(subject.getArgument(i),args);
        if(newSubterm != subject.getArgument(i)) {
          subject = subject.setArgument(newSubterm,i);
        }
      }
    } catch(Exception e) {
      e.printStackTrace();

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
    return genericTraversal(subject,replace, new ATerm[] {});
  }

  protected ATerm genericTraversal(ATerm subject, Replace replace, Object arg1) {
    return genericTraversal(subject, replace, new Object[] {arg1});
  }

  protected ATerm genericTraversal(ATerm subject, Replace replace, Object arg1, Object arg2) {
    return genericTraversal(subject, replace, new Object[] {arg1,arg2});
  }
  
  protected ATerm genericTraversal(ATerm subject, Replace replace, Object[] args) {
    ATerm res = subject;
    try {
      if(subject instanceof ATermAppl) { 
        res = genericMapterm((ATermAppl) subject, replace, args);
      } else if(subject instanceof ATermList) {
        res = genericMap((ATermList) subject, replace, args);
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
