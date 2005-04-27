import java.io.*;
import aterm.*;
import aterm.pure.PureFactory;
import java.util.*;


// pour le main
//import tom.library.strategy.mutraveler.*;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.*;
import tom.library.traversal.*;


public class Ted {

    %include { ../atermmapping.tom }
    %include { mutraveler.tom }


    private static ATermFactory atermFactory = new PureFactory();

    /* Ted symbol table */
    private HashMap tds = new HashMap();
    
    public boolean match(ATerm a1, ATerm a2) {

	/* exact match case */
	if (a1 == a2) return true;

	boolean ok = true;	    

	%match ( ATerm a1, ATerm a2 ) {
	    
	    ATermAppl(AFun(name,arity,_),args1), ATermAppl(AFun(name,arity,_),args2) -> { 
		for (int i=0; i < arity; ++i)
		    ok &= this.match(args1.elementAt(i), args2.elementAt(i));
		return ok;
	    }


	    ATermList(_), ATermList(_) -> {

		ATermList list1 = (ATermList) a1;
		ATermList list2 = (ATermList) a2;

		int len = list1.getLength();
		if ( len != list2.getLength())
		    return false;
   
		for (int i = 0; i < len; ++i)
		    ok &= this.match(list1.elementAt(i), list2.elementAt(i));
		return ok;
	    }


	    ATermPlaceholder( ATermAppl(AFun(name,arity,_),_) ), _ -> {

		if ( arity != 0 ) {
		    System.err.println("Bad placeholder format");
		    System.exit(1);
		} 

		if (name.equals("any")) {
		    return true;
		} else {
		    if ( tds.containsKey(name) ) {
			return match(((ATerm) tds.get(name)), a2);
		    } else {
			tds.put(name, a2);
			return true;
		    }
		}
	    }


	    /* Placeholder with wrong format */
	    ATermPlaceholder(_), _ -> {

		System.err.println("Bad placeholder format");
		System.exit(1);
	    }
	} 

	return false;
    }

    public static void main (String[] argv) throws IOException {
	
	if (argv.length != 1) {
	    System.out.println("Usage java Ted file");
	    System.exit(1);
	}

	 BufferedReader in = new BufferedReader(new FileReader(argv[0]));
	 ATerm at1 = atermFactory.parse(in.readLine());
	 ATerm at2 = atermFactory.parse(in.readLine());

	 Ted ted = new Ted();

	 jjtraveler.Visitor vtor = new BottomUp(new TedVisitor(at1));
	 try {
	     vtor.visit(at2);
	 } catch ( VisitFailure e) {}
    }
}
