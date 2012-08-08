/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;
import aterm.pure.PureFactory;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Prototype of tom code.
 *
 * @author hubert
 */
public class ShrinkIterator {

  private static class s1Iterator implements Iterator<ATerm> {
    //<editor-fold defaultstate="collapsed" desc="ATermSameTypeIterator">

    private ATerm current;
    private Stack<ATermList> stack;
    private DomainInterpretation domain;

    private s1Iterator(final ATerm term, DomainInterpretation domain) {
      this.domain = domain;
      ATermList args = getArgs(term);
      this.stack = new Stack<ATermList>();
      stack.push(args);
      current = null;
    }

    private ATermList getArgs(ATerm term) {
      /*
       * %match(term){ ATermAppl(fun, list) -> {return `list;} _ -> {throw new
       * UnsupportedOperationException("Operation not supported");} }
       */
      if(term instanceof ATermAppl){
        return ((ATermAppl) term).getArguments();
      }
      throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    public boolean hasNext() {
      if (current != null) {
        return true;
      }
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
    //</editor-fold>
  }

  public static Iterator<ATerm> s1(final Iterator<ATerm> termIterator, final DomainInterpretation domain) {
    return new Iterator<ATerm>() {
      //<editor-fold defaultstate="collapsed" desc="s1">
      private Iterator<ATerm> globalIterator = termIterator;
      private DomainInterpretation dom = domain;
      private Iterator<ATerm> localIterator;

      @Override
      public boolean hasNext() {
        if (localIterator == null) {
          if (globalIterator.hasNext()) {
            localIterator = new s1Iterator(globalIterator.next(), dom);
          } else {
            return false;
          }
        }
        if (localIterator.hasNext()) {
          return true;
        } else if (globalIterator.hasNext()) {
          localIterator = new s1Iterator(globalIterator.next(), dom);
          return hasNext();
        } else {
          return false;
        }
      }

      @Override
      public ATerm next() {
        if (hasNext()) {
          return localIterator.next();
        } else {
          throw new NoSuchElementException();
        }

      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
      }
      //</editor-fold>
    };
  }

  public static ATermList s1(ATermList list, DomainInterpretation domain) {
    return toATermList(s1(toIterator(list), domain));
  }

  public static Iterator<ATerm> s2(final Iterator<ATerm> termIterator, final DomainInterpretation domain) {
    return new Iterator<ATerm>() {
      //<editor-fold defaultstate="collapsed" desc="s2">
      private Iterator<ATerm> globalIterator = termIterator;
      private DomainInterpretation dom = domain;
      private Iterator<ATerm> localIterator;

      @Override
      public boolean hasNext() {
        if (localIterator == null) {
          if (globalIterator.hasNext()) {
            localIterator = dom.lighten(globalIterator.next());
          } else {
            return false;
          }
        }
        if (localIterator.hasNext()) {
          return true;
        } else if (globalIterator.hasNext()) {
          localIterator = dom.lighten(globalIterator.next());
          return hasNext();
        } else {
          return false;
        }
      }

      @Override
      public ATerm next() {
        if (hasNext()) {
          return localIterator.next();
        } else {
          throw new NoSuchElementException();
        }

      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
      }
      //</editor-fold>
    };
  }

  public static ATermList s2(ATermList list, DomainInterpretation domain) {
    return toATermList(s2(toIterator(list), domain));
  }

  // -------------------------- UTILS --------------------------
  public static Iterator<ATerm> toIterator(final ATermList list) {
    return new Iterator<ATerm>() {
      private ATermList state = list;

      @Override
      public boolean hasNext() {
        return !state.isEmpty();
      }

      @Override
      public ATerm next() {
        if (hasNext()) {
          ATerm res = state.getFirst();
          state = state.getNext();
          return res;
        } else {
          throw new NoSuchElementException();
        }
      }

      @Override
      public void remove() {
        state = state.removeElementAt(0);
      }
    };
  }

  public static ATermList toATermList(Iterator<ATerm> iterator) {
    PureFactory factory = new PureFactory();
    ATermList list = factory.makeList();
    while (iterator.hasNext()) {
      ATerm aTerm = iterator.next();
      list = list.insert(aTerm);
    }
    return list;
  }
  
  // --------------------------- TESTS -----------------------------
  
  static Iterator<ATerm> getS1Iterator(ATerm term, DomainInterpretation domain){
    return new s1Iterator(term, domain);
  }
}
