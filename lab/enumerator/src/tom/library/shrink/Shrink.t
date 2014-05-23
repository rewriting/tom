package tom.library.shrink;

/*
 * Should be compiled with -gi option
 */
import aterm.*;
import aterm.pure.PureFactory;

import examples.adt.tree.types.*;

import tom.library.sl.Visitable;
import tom.library.sl.VisitableBuiltin;
import tom.library.shrink.metaterm.types.*;
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
      at@ATermAppl[] -> { 
        if(`at != `t) {

System.out.println("t = " + t);
									System.out.println("p = " + getEnvironment().getPosition());
									System.out.println("p.omega = " + getEnvironment().getPosition().getSubterm().visit(t,
											new LocalIntrospector()));
									//for(Object o:constants) {
									ATerm cst = (ATerm)constants.toArray()[0]; //(ATerm)o;
									System.out.println("cst = " + cst);
									

									ATerm res = getEnvironment()
											.getPosition()
											.getReplace(cst)
											.visit(t, new LocalIntrospector());
									
									System.out.println(t.getChildCount());
									for(int i = 0 ; i < t.getChildCount() ; i ++) {
										System.out.println(t.getChildAt(i));
									}

									System.out.println(res);

										c.add(res);
										
										try {
											Tree tr = Tree.fromTerm(res);
											System.out.println("*** res = " + tr);
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

    Collection bag = new HashSet();
    Collection constants = new HashSet();
    try {
      `TopDown(CollectConstant(at,constants)).visit(at, new LocalIntrospector());
      `TopDown(CollectSubterm(at,bag)).visit(at, new LocalIntrospector());
      `TopDown(ReplaceSubtermByConstant(at,constants,bag)).visit(at, new LocalIntrospector());
    } catch (VisitFailure e) {
      e.printStackTrace();
    }
    
    System.out.println("constants = " + constants);
		System.out.println("bag.size = " + bag.size());
		for(ATerm tt: bag) {
			try {
				Tree tr = Tree.fromTerm(tt);
				System.out.println("tree = " + tr);
			} catch(Exception e) {
				
			}
		}


  }

}
