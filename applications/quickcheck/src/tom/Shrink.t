package logic.model;

import aterm.ATerm;
import aterm.ATermList;
import aterm.ATermPlaceholder;
import java.util.*;
import tom.library.sl.Strategy;
import tom.library.sl.VisitFailure;

public class Shrink{
  %include{int.tom}
  %include{string.tom}
  %include{aterm.tom}

  %include{sl.tom}
  %include { util/types/Collection.tom }
  %typeterm DomainInterpretation { 
    implement {DomainInterpretation} 
    is_sort(t) { ($t instanceof DomainInterpretation) } 
  }


  private static ATermList s1_aux(ATermList list, DomainInterpretation domain){
    %match(list){
      concATerm() -> {return `concATerm();}
      concATerm(hd, tl*) -> {
        if(domain.includes(`hd)){
          ATermList tail = `s1_aux(tl*, domain);
          return `concATerm(hd, tail*);
        } else {
          %match(`hd){
            ATermAppl(_, listFields) -> {
              ATermList a = s1_aux(`listFields, domain);
              ATermList b = s1_aux(`tl*, domain);
              return `concATerm(a*, b*);
            }
            _ -> {throw new UnsupportedOperationException();}
          }
        }
      }
    }
    return null; // unreachable
  }

  public static ATermList s1(ATerm term, DomainInterpretation domain){
    ATermList list = null;
    breakmatch : {
      %match(term){
        ATermAppl(_, listFields) -> {list = s1_aux(`listFields, domain); break breakmatch;}
        _ -> {throw new UnsupportedOperationException();}
      }
    }
    if(list.isEmpty()){
      return `concATerm(term);
    } else {
      return list;
    }
  }

  public static ATermList s1(ATermList list, DomainInterpretation domain){
    if (list.isEmpty()){
      return list;
    }
    ATerm head = list.getFirst();
    return s1(list.getNext(), domain).insert(head);
  }


  public static Collection s1bis(ATerm term, DomainInterpretation domain) {
    Collection bag = new HashSet();
    try {
      `TopDownStopOnSuccess(SelectSameType(bag,domain,term)).visitLight(term, new LocalIntrospector());
    } catch (VisitFailure e) {
      System.out.println("failure");
    }
    return bag;
  }

  %strategy SelectSameType(bag:Collection,domain:DomainInterpretation,root:ATerm) extends Fail() {
    visit ATerm { 
      subject@ATermAppl[] -> {
        if(`subject != root && domain.includes(`subject)) {
          `bag.add(`subject);
          return `subject;
        }
      }
    }
  }



  public static Iterator<ATerm> toIterator(final ATermList list){
    return new Iterator<ATerm>() {

      private ATermList state = list;

      @Override
      public boolean hasNext(){
        return !state.isEmpty();
      }
    
      @Override
      public ATerm next(){
        if(hasNext()){
          ATerm res = state.getFirst();
          state = state.getNext();
          return res;
        } else {
          throw new NoSuchElementException();
        }
      }

      @Override
      public void remove(){
        state = state.removeElementAt(0);
      }

    };
  }


}


