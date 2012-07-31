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

  private static class ATermShrunkFieldsIterator implements Iterator<ATerm> {

    private DomainInterpretation domain;
    private ATerm term;

    private ATermShrunkFieldsIterator(final ATerm term, DomainInterpretation domain) {
      this.domain = domain;
      this.term = term;
    }

    @Override
    public boolean hasNext() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ATerm next() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  public static Iterator<ATerm> getSameTypeFields(final ATerm term, DomainInterpretation domain) {
    return new ATermSameTypeIterator(term, domain);
  }

  public static Iterator<ATerm> getReducedCons(final ATerm term, DomainInterpretation domain) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public static Iterator<ATerm> getShrunkFields(final ATerm term, DomainInterpretation domain) {
    return new ATermShrunkFieldsIterator(term, domain);
  }
}
