package tom.library.shrink.metaterm;

/*
 * This is a prototype --- not used by the PropCheck framework
 * Should be compiled with -gi option
 */
import aterm.*;
import aterm.pure.PureFactory;

import examples.adt.tree.types.*;

import tom.library.sl.Visitable;
import tom.library.sl.VisitableBuiltin;
//import tom.library.shrink.metaterm.types.*;
import tom.library.sl.*;

import java.util.*;

public class Shrink {
  %include { int.tom }
  %include { string.tom }
  %include { aterm.tom }
  %include { sl.tom }
  %include { java/util/types/Collection.tom }

  %strategy CollectSubterm(t:ATerm, c:Collection) extends `Identity() {
    visit ATerm {
      at@ATermAppl[] -> { 
        if(`at != `t) {
          c.add(`at); 
        }
      }
    }
  }

  %strategy CollectConstant(t:ATerm, c:Collection) extends `Identity() {
    visit ATerm {
      at@ATermAppl[args=concATerm()] -> { c.add(`at); }
      at@ATermInt[] -> { c.add(`at); }
    }
  }

  %strategy ReplaceSubtermByConstant(t:ATerm, constants:Collection, c:Collection) extends `Identity() {
    visit ATerm {
      at@(ATermAppl|ATermInt)[] -> { 
        if(`at != `t) {
          Position pos = getEnvironment().getPosition();
          System.out.println("pos = " + pos);
          for(Object o:constants) {
            ATerm cst = (ATerm)o;
            ATerm res = pos.getReplace(cst).visit(t, new LocalIntrospector());
            try {
              Tree tr = Tree.fromTerm(res);
              c.add(tr);
              //System.out.println("add tree = " + tr);
            } catch(Exception e) {

            }

          }
        }
      }
    }
  }

  public static void main(String[] Args) {

    Tree t = Tree.fromString("tree(val(3),empty(),tree(val(5),empty(),empty()))");

    ATerm at = t.toATerm();
    System.out.println("at = " + at);

    Collection<ATerm> bagPhase1 = new HashSet<ATerm>();
    Collection<ATerm> bagPhase2 = new HashSet<ATerm>();
    Collection<ATerm> constants = new HashSet<ATerm>();
    Introspector introspector = new LocalIntrospector();
    try {
      `TopDown(CollectConstant(at,constants)).visit(at, introspector);
      `TopDown(CollectSubterm(at,bagPhase1)).visit(at, introspector);
      bagPhase1.add(at);
      for(ATerm tt: bagPhase1) {
        `TopDown(ReplaceSubtermByConstant(tt,constants,bagPhase2)).visit(tt, introspector);
      }
      bagPhase2.addAll(bagPhase1);
    } catch (VisitFailure e) {
      e.printStackTrace();
    }

    System.out.println("constants = " + constants);
    System.out.println("bag1       = " + bagPhase1);
    System.out.println("bag2       = " + bagPhase2);


  }

  }
