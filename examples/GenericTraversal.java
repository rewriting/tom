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

  protected void genericCollectReplace(Collection collection, ATerm subject, Replace replace) {
    try {
      if(subject instanceof ATermAppl) {
        ATerm newSubterm;
        ATermAppl subjectAppl = (ATermAppl) subject; 
        for(int i=0 ; i<subjectAppl.getArity() ; i++) {
          newSubterm = replace.apply(subject.getArgument(i));
          if(newSubterm != subject.getArgument(i)) {
            subject = subject.setArgument(newSubterm,i);
          }
          
          
          ATerm term = subjectAppl.getArgument(i);
          genericCollect(term,collect); 
        } 
      } 
    } catch(Exception e) {
      System.out.println("Please, extend genericCollectReplace");
      System.exit(0);
    }
  } 

  
}
