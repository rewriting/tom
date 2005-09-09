/****
 * This file shows how to use the tom aterm mapping and the generic traversals functions.
 * - test1 function matches some aterms
 * - test2 demonstrates how to use implicit lists with atermlist
 * - test3 uses gerenicReplace to replace any atermappl whose name matches argv[1] in the aterm contained in the argv[0] file by a mark
 * - test4 uses gerenicRemove to remove any atermappl whose name matches argv[1] in the aterm contained in the argv[0] file
 */

import aterm.*;
import aterm.pure.PureFactory;

// for substitutions
import tom.library.traversal.*;

public class ATermMappingExemple {

    %include { atermmapping.tom }

    private ATermFactory atermFactory = new PureFactory();

    public static void main (String[] argv) {

	if (argv.length != 2) {
	    System.out.println("Usage : java ATermMappingExemple file string\n\n\tfile: contains an aterm\n\tstring: name of the atermappl to remove/replace.\n\ntry java ATermMappingExemple example.aterm a");
	    System.exit(1);
	}

	ATermMappingExemple test = new ATermMappingExemple();
	test.test1();
	test.test2();
	test.test3(argv[0], argv[1]);
	test.test4(argv[0], argv[1]);
    }

    public void test1() {
	ATerm at = atermFactory.parse("f(<1>,2,3)");

	%match ( ATerm at ) {

	    ATermAppl_0(_) -> { System.out.println("ATermAppl matched"); }
	    ATermAppl_1(_,_) -> { System.out.println("ATermAppl_1 matched"); }
	    ATermAppl_2(_,_,_) -> { System.out.println("ATermAppl_2 matched"); }
	    ATermAppl_3(_,_,_,_) -> { System.out.println("ATermAppl_3 matched"); }
	    ATermAppl_3[fun=AFun("g",_,_)] -> { System.out.println("ATermAppl_3 g matched"); }
	    ATermAppl_3[fun=AFun("f",_,_)] -> { System.out.println("ATermAppl_3 f matched"); }
	    ATermAppl(AFun[name="f"], concATerm(_*,ATermInt(3))) -> { System.out.println("ATermAppl f,3 matched"); }
	    ATermAppl(AFun[name="f"], (_*,ATermInt(3))) -> { System.out.println("ATermAppl f,3  implicit matched"); }
	    ATermAppl(_,(ATermPlaceholder(ATermInt(1)),_,_)) -> { System.out.println("ATermPlaceholder <1> matched"); }
      	    ATermAppl(_,(ATermPlaceholder(ATermInt(2)),_,_)) -> { System.out.println("ATermPlaceholder <2> matched"); }
	}
    }

    public void test2() {
	ATerm at = atermFactory.parse("[1,2,3]");
	%match ( ATerm at ) {
	    ATermList(ATermInt(1),_*) -> {  System.out.println("list matched"); }
	    (_,ATermInt(1),_) -> { System.out.println("implicit list _,1,_ matched"); }
	    (_,ATermInt(2),_) -> { System.out.println("implicit list _,2,_ matched"); }
	}
    }


    public void test3(String file, final String str) {
        final GenericTraversal traversal = new GenericTraversal();


	Replace1 replace = new Replace1 () {
            public ATerm apply(ATerm t) {
		    %match( ATerm t, String str ) {
			ATermAppl[fun=AFun(var,_,_)], var ->  { return atermFactory.makeAppl(atermFactory.makeAFun("replaced",0,false)); }
		    }
		    return traversal.genericTraversal(t,this);
            }
	};


	try {
	    ATerm at = atermFactory.readFromFile(file);
	    at = replace.apply(at);
	    System.out.println("replace: " + at);
	} catch ( Exception e ) { e.printStackTrace(); }
    }

    public void test4(String file, final String str){
	final GenericTraversal traversal = new GenericTraversal();

	Remove1 remove = new Remove1(){
	   public boolean apply(ATerm t) {
		%match( ATerm t, String str) {
			ATermAppl[fun=AFun(var,_,_)], var -> { return true ;} 
		}
		return false;
	   }
	};

	try {
	   ATerm at = atermFactory.readFromFile(file);
	   at = traversal.genericRemove(at,remove);
	   System.out.println("remove: "+ at);
	} catch(Exception e){ e.printStackTrace();}
    }
}
