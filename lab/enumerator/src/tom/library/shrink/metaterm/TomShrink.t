package tom.library.shrink.metaterm;

/*
 * Should be compiled with -gi option
 */
import aterm.*;
import aterm.pure.PureFactory;

import examples.adt.tree.types.*;

import tom.library.sl.Visitable;
import tom.library.sl.VisitableBuiltin;
//import tom.library.shrink.metaterm.types.*;
import tom.library.sl.*;

import java.lang.reflect.Method;
import java.util.*;

import tom.library.shrink.Shrink;
import java.lang.reflect.InvocationTargetException;

public class TomShrink implements Shrink {
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
          //System.out.println("pos = " + pos);
          for(Object o:constants) {
            ATerm cst = (ATerm)o;
            ATerm res = pos.getReplace(cst).visit(t, new LocalIntrospector());
            c.add(res);
          }
        }
      }
    }
  }

  public static void computeShrink(Collection bag, ATerm at) {
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

    bag.addAll(bagPhase2);
  }


public List getValueSources(Object counterExample) {

	  Method toAterm = null;
	  try {
		  toAterm = counterExample.getClass().getDeclaredMethod("toATerm", null);
	  } catch (NoSuchMethodException | SecurityException e2) {
		  // TODO Auto-generated catch block
		  e2.printStackTrace();
	  }

	  //ATerm at = t.toATerm();
	  ATerm at = null;
    try {
		  at = (ATerm) toAterm.invoke(counterExample, null);
	  } catch (IllegalAccessException | IllegalArgumentException
			  | InvocationTargetException e2) {
		  // TODO Auto-generated catch block
		  e2.printStackTrace();
	  }

	  System.out.println("at = " + at);

	  Collection<ATerm> bag = new HashSet<ATerm>();
	  computeShrink(bag,at);

	  Collection<Object> orderedSet = new HashSet<Object>();

	  Method fromTerm = null;
	  try {
		  Class atermClass = Class.forName("aterm.ATerm");
		  Method[] methods = counterExample.getClass().getMethods();
		  for (Method m : methods) {
			  if ("fromTerm".equals(m.getName())) {
				  fromTerm = m;
				  // for static methods we can use null as instance of class
				  //m.invoke(null, new Object[] {args});
				  //break;
			  }
		  }
	  } catch (SecurityException | ClassNotFoundException e1) {
		  e1.printStackTrace();
	  }

	  for(ATerm elt: bag) {
		  try {
			  //Tree tr = Tree.fromTerm(elt);
			  Object res = fromTerm.invoke(null, elt);
			  orderedSet.add(res);
			  //System.out.println("add tree = " + tr);
		  } catch(Exception e) {

		  }
	  }

	  System.out.println("bag       = " + bag);

	  System.out.println("bag2      = " + orderedSet);

	  return new LinkedList(orderedSet);
  }

  public Collection<Object> shrink(Object term) {
	  return getValueSources(term);
  }

  public Collection<Object> shrink(Object term,
		  Comparator<? super Object> comparator) {
	  return null;
  }

  public static void main(String[] Args) {
	  TomShrink ts = new TomShrink();

	  Tree t = Tree.fromString("tree(val(3),empty(),tree(val(5),empty(),empty()))");
	  Collection res = ts.shrink(t);
	  System.out.println("res       = " + res);
  }



}
