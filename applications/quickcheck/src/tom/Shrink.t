package gen;

import aterm.ATerm;
import aterm.ATermList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import logic.model.DomainInterpretation;

public class Shrink{
  %include{int.tom}
  %include{string.tom}
  %include{aterm.tom}


  private static class ATermSameTypeIterator implements Iterator<ATerm> {

    private ATerm current;
    private Stack<ATermList> stack;
    private DomainInterpretation domain;

    private ATermSameTypeIterator(final ATerm term, DomainInterpretation domain) {
      this.domain = domain;
      ATermList args = getArgs(term);
      stack.push(args);
      current = null;
    }

    private ATermList getArgs(ATerm term) {
      %match(term){ 
        ATermAppl(fun, list) -> {return `list;}
        _ -> {throw new UnsupportedOperationException("Operation not supported");}
      }
      return null; //unreachable
    }

    @Override
    public boolean hasNext() {
      if (stack.empty()) {
        return false;
      }
      ATermList args = stack.pop();
      if (args.isEmpty()) {
        return hasNext();
      }
      ATerm head = args.getFirst();
      ATermList tail = args.getNext();
      stack.push(tail);
      if (domain.includes(head)) {
        current = head;
        return true;
      }
      stack.push(getArgs(head));
      return hasNext();
    }

    @Override
    public ATerm next() {
      if (current != null) {
        ATerm res = current;
        current = null;
        return res;
      } else if (hasNext()) {
        System.out.println("WARNING : the use of the methode next() is not preceded by hasNext().");
        ATerm res = current;
        current = null;
        return res;
      } else {
        throw new NoSuchElementException();
      }
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("Not supported yet.");
    }
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


