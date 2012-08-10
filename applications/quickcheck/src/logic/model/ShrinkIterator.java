/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermFactory;
import aterm.ATermList;
import aterm.pure.PureFactory;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
      if(term instanceof ATermAppl) {
        return ((ATermAppl) term).getArguments();
      }
      throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    public boolean hasNext() {
      if(current != null) {
        return true;
      }
      if(stack.empty()) {
        return false;
      }
      ATermList args = stack.pop();
      if(args.isEmpty()) {
        return hasNext();
      }
      ATerm head = args.getFirst();
      ATermList tail = args.getNext();
      stack.push(tail);
      if(domain.includes(head)) {
        current = head;
        return true;
      }
      stack.push(getArgs(head));
      return hasNext();
    }

    @Override
    public ATerm next() {
      if(current != null) {
        ATerm res = current;
        current = null;
        return res;
      } else if(hasNext()) {
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

  public static Iterator<ATerm> s1Strict(ATerm term, final DomainInterpretation domain) {
    return new s1Iterator(term, domain);
  }

  @Deprecated
  private static Iterator<ATerm> s1Large(ATerm term, final DomainInterpretation domain) {
    Iterator<ATerm> res = s1Strict(term, domain);
    Iterator<ATerm> tmp = s1Strict(term, domain);
    if(!tmp.hasNext()) {
      List<ATerm> list = new LinkedList<ATerm>();
      list.add(term);
      return list.iterator();
    } else {
      return res;
    }
  }

  @Deprecated
  public static Iterator<ATerm> s1Large(final Iterator<ATerm> termIterator, final DomainInterpretation domain) {
    return new Iterator<ATerm>() {
      //<editor-fold defaultstate="collapsed" desc="s1Large">
      private Iterator<ATerm> globalIterator = termIterator;
      private DomainInterpretation dom = domain;
      private Iterator<ATerm> localIterator;

      @Override
      public boolean hasNext() {
        if(localIterator == null) {
          if(globalIterator.hasNext()) {
            localIterator = s1Large(globalIterator.next(), dom);
          } else {
            return false;
          }
        }
        if(localIterator.hasNext()) {
          return true;
        } else if(globalIterator.hasNext()) {
          localIterator = s1Large(globalIterator.next(), dom);
          return hasNext();
        } else {
          return false;
        }
      }

      @Override
      public ATerm next() {
        if(hasNext()) {
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

  @Deprecated
  public static ATermList s1Large(ATermList list, DomainInterpretation domain) {
    return toATermList(s1Large(toIterator(list), domain), list.getFactory());
  }

  /*
   * -------------------------- S2 -------------------------
   */
  public static Iterator<ATerm> s2Strict(ATerm term, final DomainInterpretation domain) {
    return domain.lighten(term);
  }

  @Deprecated
  private static Iterator<ATerm> s2Large(ATerm term, final DomainInterpretation domain) {
    Iterator<ATerm> res = s2Strict(term, domain);
    Iterator<ATerm> tmp = s2Strict(term, domain);
    if(!tmp.hasNext()) {
      List<ATerm> list = new LinkedList<ATerm>();
      list.add(term);
      return list.iterator();
    } else {
      return res;
    }
  }

  @Deprecated
  public static Iterator<ATerm> s2Large(final Iterator<ATerm> termIterator, final DomainInterpretation domain) {
    return new Iterator<ATerm>() {
      //<editor-fold defaultstate="collapsed" desc="s2Large">
      private Iterator<ATerm> globalIterator = termIterator;
      private DomainInterpretation dom = domain;
      private Iterator<ATerm> localIterator;

      @Override
      public boolean hasNext() {
        if(localIterator == null) {
          if(globalIterator.hasNext()) {
            localIterator = s2Large(globalIterator.next(), dom);
          } else {
            return false;
          }
        }
        if(localIterator.hasNext()) {
          return true;
        } else if(globalIterator.hasNext()) {
          localIterator = s2Large(globalIterator.next(), dom);
          return hasNext();
        } else {
          return false;
        }
      }

      @Override
      public ATerm next() {
        if(hasNext()) {
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

  @Deprecated
  public static ATermList s2Large(ATermList list, DomainInterpretation domain) {
    return toATermList(s2Large(toIterator(list), domain), list.getFactory());
  }

  /*
   * -------------------------- UTILS -----------------------
   */
  public static Iterator<ATerm> toIterator(final ATermList list) {
    return new Iterator<ATerm>() {
      private ATermList state = list;

      @Override
      public boolean hasNext() {
        return !state.isEmpty();
      }

      @Override
      public ATerm next() {
        if(hasNext()) {
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

  public static ATermList toATermList2(Iterator<ATerm> iterator) {
    ATermFactory factory = null;
    ATermList list = null;

    while(iterator.hasNext()) {
      ATerm aTerm = iterator.next();
      if(factory == null) {
        factory = aTerm.getFactory();
        list = factory.makeList();
      }
      list = list.insert(aTerm);
    }
    return list;
  }

  public static ATermList toATermList(Iterator<ATerm> iterator, ATermFactory factory) {
    if(!iterator.hasNext()) {
      return factory.makeList();
    }
    ATerm term = iterator.next();
    return toATermList(iterator, factory).insert(term);
  }
}
