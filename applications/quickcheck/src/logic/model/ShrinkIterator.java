/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.AFun;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermFactory;
import aterm.ATermInt;
import aterm.ATermIterator;
import aterm.ATermIteratorFromATermList;
import aterm.ATermList;
import java.util.Arrays;
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

  private static DomainInterpretation getCorrespondingDomain(DomainInterpretation[] subDoms, ATerm term) {
    for(int i = 0; i < subDoms.length; i++) {
      if(subDoms[i].includes(term)) {
        return subDoms[i];
      }
    }
    throw new UnsupportedOperationException("The term " + term + " is not included in " + Arrays.toString(subDoms));
  }

  private static class s1Iterator extends ATermIterator {
    //<editor-fold defaultstate="collapsed" desc="ATermSameTypeIterator">

    private ATerm current;
    private Stack<ATermList> stack;
    private DomainInterpretation domain;

    @Override
    public ATermIterator clone() {
      s1Iterator res = (s1Iterator) super.clone();
      res.stack = (Stack<ATermList>) this.stack.clone();
      return res;
    }

    private s1Iterator(final ATerm term, DomainInterpretation domain) {
      this.domain = domain;
      ATermList args = getArgs(term);
      this.stack = new Stack<ATermList>();
      stack.push(args);
      current = null;
    }

    private ATermList getArgs(ATerm term) {
      if(term instanceof ATermAppl) {
        return ((ATermAppl) term).getArguments();
      }
      if(term instanceof ATermInt) {
        System.out.println("WARNING: ATermInt are not shrinkable.");
        ATermList list = term.getFactory().makeList(term);
        return list;
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
    //</editor-fold>
  }

  public static ATermIterator s1Strict(ATerm term, final DomainInterpretation domain) {
    return new s1Iterator(term, domain);
  }

  @Deprecated
  private static ATermIterator s1Large(ATerm term, final DomainInterpretation domain) {
    ATermIterator res = s1Strict(term, domain);
    ATermIterator tmp = s1Strict(term, domain);
    if(!tmp.hasNext()) {
      final List<ATerm> list = new LinkedList<ATerm>();
      list.add(term);
      return new ATermIterator() {
        Iterator<ATerm> l = list.iterator();

        @Override
        public boolean hasNext() {
          return l.hasNext();
        }

        @Override
        public ATerm next() {
          return l.next();
        }
      };
    } else {
      return res;
    }
  }

  @Deprecated
  public static ATermIterator s1Large(final ATermIterator termIterator, final DomainInterpretation domain) {
    return new ATermIterator() {
      //<editor-fold defaultstate="collapsed" desc="s1Large">
      private ATermIterator globalIterator = termIterator;
      private DomainInterpretation dom = domain;
      private ATermIterator localIterator;

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
      //</editor-fold>
    };
  }

  @Deprecated
  public static ATermList s1Large(ATermList list, DomainInterpretation domain) {
    return s1Large(new ATermIteratorFromATermList(list), domain).toATermList(list.getFactory());
  }

  /*
   * -------------------------- S2 -------------------------
   */
  public static ATermIterator s2Strict(ATerm term, final DomainInterpretation domain) {
    return domain.lighten(term);
  }

  @Deprecated
  private static ATermIterator s2Large(ATerm term, final DomainInterpretation domain) {
    ATermIterator res = s2Strict(term, domain);
    if(!res.hasNext()) {
      final List<ATerm> list = new LinkedList<ATerm>();
      list.add(term);
      return new ATermIterator() {
        private Iterator<ATerm> l = list.iterator();

        @Override
        public boolean hasNext() {
          return l.hasNext();
        }

        @Override
        public ATerm next() {
          return l.next();
        }
      };
    } else {
      return res;
    }
  }

  @Deprecated
  public static ATermIterator s2Large(final ATermIterator termIterator, final DomainInterpretation domain) {
    return new ATermIterator() {
      //<editor-fold defaultstate="collapsed" desc="s2Large">
      private ATermIterator globalIterator = termIterator;
      private DomainInterpretation dom = domain;
      private ATermIterator localIterator;

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
      //</editor-fold>
    };
  }

  @Deprecated
  public static ATermList s2Large(ATermList list, DomainInterpretation domain) {
    return s2Large(new ATermIteratorFromATermList(list), domain).toATermList(list.getFactory());
  }

  /*
   * -------------------------- WithDepth -------------------------
   */
  private static class S1depth extends ATermIterator {
    //<editor-fold defaultstate="collapsed" desc="s1WithDepthStrict">

    private ATerm[] args;
    private AFun fun;
    private DomainInterpretation[] subDoms;
    private int d;
    //
    private int childIndex = 0;
    private ATerm current = null;
    private ATermIterator localIte = null;

    @Override
    public ATermIterator clone() {
      S1depth res = (S1depth) super.clone();
      if(this.localIte == null) {
        res.localIte = null;
      } else {
        res.localIte = this.localIte.clone();
      }
      return res;
    }

    public S1depth(ATermAppl term, DomainInterpretation domain, int depth) {
      this.args = ((ATermAppl) term).getArgumentArray();
      this.fun = ((ATermAppl) term).getAFun();
      this.subDoms = domain.getDepsDomains();
      this.d = depth;
    }

    @Override
    public boolean hasNext() {
      if(current != null) {
        return true;
      }
      if(childIndex >= args.length) {
        return false;
      }
      if(localIte == null) {
        ATerm child = args[childIndex];
        DomainInterpretation dom = getCorrespondingDomain(subDoms, child);
        localIte = s1WithDepthStrict(args[childIndex], dom, d - 1);
      }
      if(!localIte.hasNext()) {
        childIndex++;
        localIte = null;
        return hasNext();
      }
      ATerm[] newArgs = Arrays.copyOf(args, args.length);
      ATerm head = localIte.next();
      newArgs[childIndex] = head;
      ATerm newTerm = head.getFactory().makeAppl(fun, newArgs);
      current = newTerm;
      return true;
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
    //</editor-fold>
  }

  private static class S2depth extends ATermIterator {
    //<editor-fold defaultstate="collapsed" desc="s2WithDepthStrict">

    private ATerm[] args;
    private AFun fun;
    private DomainInterpretation[] subDoms;
    private int d;
    //
    private int childIndex = 0;
    private ATerm current = null;
    private ATermIterator localIte = null;

    @Override
    public ATermIterator clone() {
      S2depth res = (S2depth) super.clone();
      if(this.localIte == null) {
        res.localIte = null;
      } else {
        res.localIte = this.localIte.clone();
      }
      return res;
    }

    public S2depth(ATermAppl term, DomainInterpretation domain, int depth) {
      this.args = ((ATermAppl) term).getArgumentArray();
      this.fun = ((ATermAppl) term).getAFun();
      this.subDoms = domain.getDepsDomains();
      this.d = depth;
    }

    @Override
    public boolean hasNext() {
      if(current != null) {
        return true;
      }
      if(childIndex >= args.length) {
        return false;
      }
      if(localIte == null) {
        ATerm child = args[childIndex];
        DomainInterpretation dom = getCorrespondingDomain(subDoms, child);
        localIte = s2WithDepthStrict(args[childIndex], dom, d - 1);
      }
      if(!localIte.hasNext()) {
        childIndex++;
        localIte = null;
        return hasNext();
      }
      ATerm[] newArgs = Arrays.copyOf(args, args.length);
      ATerm head = localIte.next();
      newArgs[childIndex] = head;
      ATerm newTerm = head.getFactory().makeAppl(fun, newArgs);
      current = newTerm;
      return true;
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
    //</editor-fold>
  }

  public static ATermIterator s1WithDepthStrict(final ATerm term, final DomainInterpretation domain, final int depth) {
    if(depth == 0) {
      return s1Strict(term, domain);
    }
    return new S1depth((ATermAppl) term, domain, depth);
  }

  public static ATermIterator s2WithDepthStrict(final ATerm term, final DomainInterpretation domain, final int depth) {
    if(depth == 0) {
      return s2Strict(term, domain);
    }
    return new S2depth((ATermAppl) term, domain, depth);
  }

  /*
   * -------------------------- UTILS -----------------------
   */
//  public static ATermIterator toIterator(final ATermList list) {
//    return new ATermIterator() {
//      private ATermList state = list;
//
//      @Override
//      public boolean hasNext() {
//        return !state.isEmpty();
//      }
//
//      @Override
//      public ATerm next() {
//        if(hasNext()) {
//          ATerm res = state.getFirst();
//          state = state.getNext();
//          return res;
//        } else {
//          throw new NoSuchElementException();
//        }
//      }
//    };
//  }

//  public static ATermList toATermList2(ATermIterator iterator) {
//    ATermFactory factory = null;
//    ATermList list = null;
//
//    while(iterator.hasNext()) {
//      ATerm aTerm = iterator.next();
//      if(factory == null) {
//        factory = aTerm.getFactory();
//        list = factory.makeList();
//      }
//      list = list.insert(aTerm);
//    }
//    return list;
//  }

//  public static ATermList toATermList(ATermIterator iterator, ATermFactory factory) {
//    if(!iterator.hasNext()) {
//      return factory.makeList();
//    }
//    ATerm term = iterator.next();
//    return toATermList(iterator, factory).insert(term);
//  }
}
