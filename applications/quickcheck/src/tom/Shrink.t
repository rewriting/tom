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

    private ATermSameTypeIterator(ATerm term, DomainInterpretation domain) {
      this.domain = domain;
      ATermList args = getArgs(term);
      stack.push(args);
    }

    private ATermList getArgs(ATerm term){
      %match(term){
        ATermAppl(fun, list) -> {return `list;}
        _ -> {throw new UnsupportedOperationException("Operation not supported");}
      }
      return null; //unreachable
    }

    @Override
    public boolean hasNext() {
      ATermList args = stack.pop();

      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ATerm next() {
      if (current != null) {
        ATerm res = current;
        current = null;
        return res;
      } else if (hasNext()) {
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

  public static Iterator<ATerm> getSameTypeFields(ATerm term, DomainInterpretation domain) {
    return new ATermSameTypeIterator(term, domain);
  }
}
