/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;
import aterm.ATermList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Prototype of tom code.
 *
 * @author hubert
 */
public class Shrink {

  private static class ATermSameTypeIterator implements Iterator<ATerm> {
    //<editor-fold defaultstate="collapsed" desc="ATermSameTypeIterator">

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
      /*
       * %match(term){ ATermAppl(fun, list) -> {return `list;} _ -> {throw new
       * UnsupportedOperationException("Operation not supported");} }
       */
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
            localIterator = new ATermSameTypeIterator(globalIterator.next(), dom);
          } else {
            return false;
          }
        }
        if (localIterator.hasNext()) {
          return true;
        } else if (globalIterator.hasNext()) {
          localIterator = new ATermSameTypeIterator(globalIterator.next(), dom);
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

  public static Iterator<ATerm> s3_aux(final ATerm root, final DomainInterpretation domain) {
    return new Iterator<ATerm>() {
      //<editor-fold defaultstate="collapsed" desc="s3">

      private ATerm term = root;
      private int indexChild = -1;
      private DomainInterpretation[] domainFields = domain.getDepsDomains();
      private Iterator<ATerm> localIterator = null;

      private DomainInterpretation getDomain(ATerm field) {
        for (DomainInterpretation dom : domainFields) {
          if (dom.includes(field)) {
            return dom;
          }
        }
        throw new UnsupportedOperationException("Internal problem");
      }

      @Override
      public boolean hasNext() {
        if (localIterator == null) {
          if (term.getChildCount() != 0) {
            indexChild++; // = 0
            ATerm child = (ATerm) term.getChildAt(indexChild);
            DomainInterpretation newdomain = getDomain(child);
            localIterator = s3_aux(child, newdomain);
            return hasNext();
          }
        } else {
          return false;
        }


      }

      @Override
      public ATerm next() {
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("This method cannot be used");
      }
      //</editor-fold>
    };
  }
}
