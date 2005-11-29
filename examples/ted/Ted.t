package ted;

import java.io.*;
import aterm.*;
import aterm.pure.PureFactory;
import java.util.*;
import java.lang.reflect.*;


// pour le main
//import tom.library.strategy.mutraveler.*;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.*;
import tom.library.traversal.*;


public class Ted {

  %include { atermmapping.tom }
  %include { mutraveler.tom }

  private static ATermFactory atermFactory = new PureFactory();
  //strategy      
  private Constructor ctor = null;

  /* Ted symbol table */
  private HashMap tds = new HashMap();
  //private HashMap globalTds = new HashMap();

  public boolean match(ATerm a1, ATerm a2) {
    return match(a1,a2,true);
  }

  public boolean match(ATerm a1, ATerm a2, boolean reset) {

    if (reset)
      tds.clear();

    /* exact match case */
    if (a1 == a2) return true;
    boolean ok = true;

    %match ( ATerm a1, ATerm a2 ) {

      ATermAppl(AFun(name,arity,_),args1), ATermAppl(AFun(name,arity,_), args2) -> {
        return match(`args1,`args2,false);
      }

      ATermList(_*), ATermList(_*) -> {
        ATermList l1 = (ATermList) a1;
        ATermList l2 = (ATermList) a2;
        if(l1.getLength() != l2.getLength()) {
          return false;
        }

        for(int i = 0; i < l1.getLength(); ++i) {
          ok &= this.match(l1.elementAt(i), l2.elementAt(i),false);
        }
        return ok;
      }

      t@ATermPlaceholder( ATermAppl(AFun(name,arity,_),_) ), _ -> {
        
    	if( `arity != 0 ) {
          System.err.println("Bad placeholder format");
          System.exit(1);
        }   
    	
        if(`name.equals("any")) {
          return true;
        } else {
          if(tds.containsKey(`t)) {
            return match(((ATerm) tds.get(`t)), a2, false);
          } else {        	  
            tds.put(`t, a2);            
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

  public void run(String[] argv) throws java.io.IOException {
    if (argv.length != 1) {
      System.out.println("Usage java Ted file");
      System.exit(1);
    }

    BufferedReader in = new BufferedReader(new FileReader(argv[0]));

    // aterm to modify
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    ATerm res = atermFactory.parse(stdin.readLine());

    while(in.ready()) {
    	
      //strategy
      String strategy = in.readLine().trim();
      //action
      ATerm action = atermFactory.parse(in.readLine());
      
      res = run(res, strategy,action);
      
      // reads an empty line
      in.readLine();    

    }
    System.out.println(res);
  }
    
  public ATerm run(ATerm res, String strategy, ATerm action) throws java.io.IOException {

    try {
	  Class strategy_class = Class.forName(strategy);
	  ctor = strategy_class.getConstructor(new Class[] {jjtraveler.Visitor.class});
  	}
  	catch( Exception e ) { System.err.println("This strategy doesn't exist or has a bad signature : " + e.getMessage()); System.exit(1); }

  	jjtraveler.Visitor vtor = null;

  	%match(ATerm action) {

	    ATermAppl_2(AFun[name="replace"], tomatch, replacement) -> {
	      try { vtor = (jjtraveler.Visitor) ctor.newInstance (new Object[] {new ReplaceVisitor(`tomatch, `replacement, this)} ); }
	      catch ( Exception e ) { e.printStackTrace(); }
	    }
	
	    ATermAppl_1(AFun[name="remove"], tomatch) -> {
	      try { vtor = (jjtraveler.Visitor) ctor.newInstance (new Object[] {new MatchAndRemoveVisitor(`tomatch)}); }
	      catch ( Exception e ) { e.printStackTrace(); }
	    }
  	}

  	// application
  	try { res = (ATerm) vtor.visit(res);}
  	catch (Exception e) {e.printStackTrace();}        
    
    return res;
  }
    
  //same functionality as above, except for the input and output
  public String run(String aTerm, String strategy, String actionStr) throws java.io.IOException {
	
	ATerm at = run(atermFactory.parse(aTerm),strategy,atermFactory.parse(actionStr));
	
	return at.toString();
  }
  
  // will replace in the term the placeholders with 
  // their initialization from tds
  public ATerm modifyReplacement(ATerm term){
	  
    String termStr = term.toString();
	  
  	Iterator it = tds.keySet().iterator();
  	
	while (it.hasNext()){
		
		ATerm key = (ATerm)it.next();
		String keyStr = key.toString();
		
		if (termStr.equals(keyStr)){
			termStr = ((ATerm)tds.get(key)).toString();
		}else{
			termStr.replaceAll(keyStr,((ATerm)tds.get(key)).toString());
		}
	}	
	
	return atermFactory.parse(termStr);
  }

  public static void main (String[] argv) throws IOException {
    Ted ted = new Ted();
    ted.run(argv);
  }

}
