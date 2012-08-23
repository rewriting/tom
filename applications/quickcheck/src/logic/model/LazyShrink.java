/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.AFun;
import aterm.ATerm;
import aterm.ATermAppl;
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

public class LazyShrink {
  
  private LazyShrink(){}

  private static DomainInterpretation getCorrespondingDomain(DomainInterpretation[] subDoms, ATerm term) {
    for(int i = 0; i < subDoms.length; i++) {
      if(subDoms[i].includes(term)) {
        return subDoms[i];
      }
    }
    throw new UnsupportedOperationException("The term " + term + " is not included in " + Arrays.toString(subDoms));
  }

  /*
   * -------------------------- S1 -------------------------
   */
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

  /**
   * Gives nearer subterms of same type of term.
   *
   * @param term Term to shrink by retriving nearer nearer subterms of the same
   * type.
   * @param domain domain containing term.
   * @return list of strictly smaller subterms
   */
  /*private*/ static ATermIterator s1StrictLazy(ATerm term, final DomainInterpretation domain) {
    return new s1Iterator(term, domain);
  }

  //<editor-fold defaultstate="collapsed" desc="deprecated">
  @Deprecated
  private static ATermIterator s1LargeLazy_aux(ATerm term, final DomainInterpretation domain) {
    ATermIterator res = s1StrictLazy(term, domain);
    ATermIterator tmp = s1StrictLazy(term, domain);
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
  static ATermIterator s1LargeLazy(final ATermIterator termIterator, final DomainInterpretation domain) {
    return new ATermIterator() {
      //<editor-fold defaultstate="collapsed" desc="s1Large">
      private ATermIterator globalIterator = termIterator;
      private DomainInterpretation dom = domain;
      private ATermIterator localIterator;

      @Override
      public boolean hasNext() {
        if(localIterator == null) {
          if(globalIterator.hasNext()) {
            localIterator = s1LargeLazy_aux(globalIterator.next(), dom);
          } else {
            return false;
          }
        }
        if(localIterator.hasNext()) {
          return true;
        } else if(globalIterator.hasNext()) {
          localIterator = s1LargeLazy_aux(globalIterator.next(), dom);
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
  static ATermList s1Large(ATermList list, DomainInterpretation domain) {
    return s1LargeLazy(new ATermIteratorFromATermList(list), domain).toATermList(list.getFactory());
  }
  //</editor-fold>

  /*
   * -------------------------- S2 -------------------------
   */
  /**
   * Gives new terms built from term by replacing its constructor by all
   * constructors whose the set of arguments is include in the set of arguments
   * of the constructor of term.
   *
   * @param term Term to shrink in changing its constructor.
   * @param domain domain containing term.
   * @return list of strictly smaller subterms.
   */
  /*private*/ static ATermIterator s2StrictLazy(ATerm term, final DomainInterpretation domain) {
    return domain.lighten(term);
  }

  //<editor-fold defaultstate="collapsed" desc="deprecated">
  @Deprecated
  private static ATermIterator s2LargeLazy_aux(ATerm term, final DomainInterpretation domain) {
    ATermIterator res = s2StrictLazy(term, domain);
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
  static ATermIterator s2LargeLazy(final ATermIterator termIterator, final DomainInterpretation domain) {
    return new ATermIterator() {
      //<editor-fold defaultstate="collapsed" desc="s2Large">
      private ATermIterator globalIterator = termIterator;
      private DomainInterpretation dom = domain;
      private ATermIterator localIterator;

      @Override
      public boolean hasNext() {
        if(localIterator == null) {
          if(globalIterator.hasNext()) {
            localIterator = s2LargeLazy_aux(globalIterator.next(), dom);
          } else {
            return false;
          }
        }
        if(localIterator.hasNext()) {
          return true;
        } else if(globalIterator.hasNext()) {
          localIterator = s2LargeLazy_aux(globalIterator.next(), dom);
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
  static ATermList s2Large(ATermList list, DomainInterpretation domain) {
    return s2LargeLazy(new ATermIteratorFromATermList(list), domain).toATermList(list.getFactory());
  }
  //</editor-fold>

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
        localIte = s1WithDepthStrictLazy(args[childIndex], dom, d - 1);
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
        localIte = s2WithDepthStrictLazy(args[childIndex], dom, d - 1);
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

  public static ATermIterator s1WithDepthStrictLazy(final ATerm term, final DomainInterpretation domain, final int depth) {
    if(depth == 0) {
      return s1StrictLazy(term, domain);
    }
    return new S1depth((ATermAppl) term, domain, depth);
  }

  public static ATermIterator s2WithDepthStrictLazy(final ATerm term, final DomainInterpretation domain, final int depth) {
    if(depth == 0) {
      return s2StrictLazy(term, domain);
    }
    return new S2depth((ATermAppl) term, domain, depth);
  }
}
