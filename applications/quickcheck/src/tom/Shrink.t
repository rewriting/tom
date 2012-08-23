package logic.model;

import aterm.AFun;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermFactory;
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

  private static DomainInterpretation getCorrespondingDomain(DomainInterpretation[] subDoms, ATerm term){
    for(int i = 0; i<subDoms.length; i++){
      if(subDoms[i].includes(term)){
        return subDoms[i];
      }
    }
    throw new UnsupportedOperationException("The term " + term + " is not included in " + Arrays.toString(subDoms));
  }

  @Deprecated
  private static ATermList s1Strict_aux(ATermList list, DomainInterpretation domain){
    %match(list){
      concATerm() -> {return `concATerm();}
      concATerm(hd, tl*) -> {
        if(domain.includes(`hd)){
          ATermList tail = `s1Strict_aux(tl*, domain);
          return `concATerm(hd, tail*);
        } else {
          %match(`hd){
            ATermAppl(_, listFields) -> {
              ATermList a = s1Strict_aux(`listFields, domain);
              ATermList b = s1Strict_aux(`tl*, domain);
              return `concATerm(a*, b*);
            }
            _ -> {throw new UnsupportedOperationException();}
          }
        }
      }
    }
    return null; // unreachable
  }

  @Deprecated
  public static ATermList s1Strict(ATerm term, DomainInterpretation domain){
    ATermList list = null;
    breakmatch : {
      %match(term){
        ATermAppl(_, listFields) -> {list = s1Strict_aux(`listFields, domain); break breakmatch;}
        ATermInt(_) -> {
          System.out.println("WARNING: ATermInt are not shrinkable.");
          list = term.getFactory().makeList(term);
          return list;
        }
        _ -> {throw new UnsupportedOperationException();}
      }
    }
    return list;
  }

  @Deprecated
  private static ATermList s1Large_aux(ATerm term, DomainInterpretation domain){
    ATermList list = null;
    breakmatch : {
      %match(term){
        ATermAppl(_, listFields) -> {list = s1Strict_aux(`listFields, domain); break breakmatch;}
        _ -> {throw new UnsupportedOperationException();}
      }
    }
    if(list.isEmpty()){
      return `concATerm(term);
    } else {
      return list;
    }
  }

  @Deprecated
  public static ATermList s1Large(ATermList list, DomainInterpretation domain){
    if (list.isEmpty()){
      return list;
    }
    ATerm head = list.getFirst();
    return s1Large_aux(head, domain).concat(s1Large(list.getNext(), domain));
  }

  @Deprecated
  public static ATermList s1WithDepthStrict(ATerm term, DomainInterpretation domain, int depth) {
    if (depth == 0) {
      return s1Strict(term, domain);
    }

    DomainInterpretation[] subDoms = domain.getDepsDomains();

    ATermFactory factory = term.getFactory();
    AFun fun = ((ATermAppl) term).getAFun();
    ATerm[] args = ((ATermAppl) term).getArgumentArray();
    int n = term.getChildCount();

    ATermList list = factory.makeList();
    for (int childIndex = 0; childIndex < n; childIndex++) {
      ATerm child = args[childIndex];
      DomainInterpretation dom = getCorrespondingDomain(subDoms, child);
      ATermList resS1 = s1WithDepthStrict(child, dom, depth - 1);

      while (!resS1.isEmpty()) {
        ATerm[] newArgs = Arrays.copyOf(args, n); // here is necessary
        ATerm head = resS1.getFirst();
        resS1 = resS1.getNext();
        newArgs[childIndex] = head;
        ATerm newTerm = factory.makeAppl(fun, newArgs);
        list = list.insert(newTerm);
      }
    }
    return list;
  }

  @Deprecated
  public static ATermList s2WithDepthStrict(ATerm term, DomainInterpretation domain, int depth) {
    if (depth == 0) {
      return ShrinkIterator.s2Strict(term, domain).toATermList(term.getFactory());
    }

    DomainInterpretation[] subDoms = domain.getDepsDomains();

    ATermFactory factory = term.getFactory();
    AFun fun = ((ATermAppl) term).getAFun();
    ATerm[] args = ((ATermAppl) term).getArgumentArray();
    int n = term.getChildCount();

    ATermList list = factory.makeList();
    for (int childIndex = 0; childIndex < n; childIndex++) {
      ATerm child = args[childIndex];
      DomainInterpretation dom = getCorrespondingDomain(subDoms, child);
      ATermList resS2 = s2WithDepthStrict(child, dom, depth - 1);

      while (!resS2.isEmpty()) {
        ATerm[] newArgs = Arrays.copyOf(args, n); // here is necessary
        ATerm head = resS2.getFirst();
        resS2 = resS2.getNext();
        newArgs[childIndex] = head;
        ATerm newTerm = factory.makeAppl(fun, newArgs);
        list = list.insert(newTerm);
      }
    }
    return list;
  }

//  @Deprecated
//  private static ATermList s1WithDepthLarge_aux(ATerm term, DomainInterpretation domain, int depth){
//    ATermList res = s1WithDepthStrict(term, domain, depth);
//    if (res.isEmpty()) {
//      return term.getFactory().makeList(term);
//    } else {
//      return res;
//    }
//  }
//  
//  @Deprecated
//  private static ATermList s2WithDepthLarge_aux(ATerm term, DomainInterpretation domain, int depth){
//    ATermList res = s2WithDepthStrict(term, domain, depth);
//    if (res.isEmpty()) {
//      return term.getFactory().makeList(term);
//    } else {
//      return res;
//    }
//  }
//  @Deprecated
//  private static ATermList s1WithDepthLarge(ATermList list, DomainInterpretation domain, int depth){
//    if (list.isEmpty()) {
//      return list;
//    } else {
//      return s1WithDepthLarge_aux(list.getFirst(), domain, depth).concat(s1WithDepthLarge(list.getNext(), domain, depth));
//    }
//  }
//
//  @Deprecated
//  private static ATermList s2WithDepthLarge(ATermList list, DomainInterpretation domain, int depth){
//    if (list.isEmpty()) {
//      return list;
//    } else {
//      return s2WithDepthLarge_aux(list.getFirst(), domain, depth).concat(s2WithDepthLarge(list.getNext(), domain, depth));
//    }
//  }
  

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


