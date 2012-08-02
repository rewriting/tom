package gen;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import logic.model.*;

import aterm.ATerm;
import aterm.ATermList;

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
          return `concATerm(hd, s1_aux(tl*, domain));
        } else {
          %match(`hd){
            ATermAppl(_, listFields) -> {return `concATerm(s1_aux(listFields, domain), s1_aux(tl*, domain));}
            _ -> {throw new UnsupportedOperationException();}
          }
        }
      }
    }
    return null; // unreachable
  }

  public static ATermList s1(ATerm term, DomainInterpretation domain){
    ATermList list = s1_aux(`concATerm(term), domain);
    if(list.isEmpty()){
      return `concATerm(term);
    } else {
      return list;
    }
  }
}
