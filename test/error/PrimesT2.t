/*
	Les nombres premiers avec TOM
*/

import jaterm.api.*;
import jaterm.shared.*;
import java.util.*;

public class PrimesT2 {
	
	private ATermFactory factory;

	private AFun fzero, fsuc, fnil, fcons, fapp, fprimes, faddifprime;

	public ATermAppl tzero, tnil;


/*
	definitions des types
*/

	%typeterm term {
		implement { ATerm }
		get_fun_sym(t) { (((ATermAppl)t).getAFun()) }
		cmp_fun_sym(t1,t2) { t1 == t2 }
		get_subterm(t, n) { (((ATermAppl)t).getArgument(n)) }
		equals(t1, t2) { (t1.equals(t2)) }
	}

        %typeterm list {
		implement { ATerm }
		get_fun_sym(l) { (((ATermAppl)l).getAFun()) }
		cmp_fun_sym(l1, l2) { l1 == l2 }
		get_subterm(l, n) { (((ATermAppl)l).getArgument(n)) }
		equals(l1, l2) { (l1.equals(l2)) }
	}




/*
	definitions des op
*/

	%op term zero {
		fsym { fzero }
		make { factory.makeAppl(fzero) }
	}

	%op term suc(term) {
		fsym { fsuc }
		make(t) { factory.makeAppl(fsuc, t) }
	}

	%op list nil {
		fsym { fnil }
		make { factory.makeAppl(fnil) }
	}

	%op list cons( term, list ) {
		fsym { fcons }
    		make( t, l ) { factory.makeAppl(fcons,t,l) }
	}

	%op list app( list, list ) {
		fsym { fapp }
		make( l1, l2 ) { factory.makeAppl( fapp, l1, l2 ) }
	}

	%op list primes( term ) {
		fsym { fprimes }
		make( t ) { factory.makeAppl( fprimes, t ) }
	}

	%op list addifprime( term, list ) {
		fsym { faddifprime }
		make( t, l ) { factory.makeAppl( faddifprime, t, l ) }
	}





/*
	la definition java
*/

	public final static void main( String[] args ) {
		PrimesT2 test = new PrimesT2( new PureFactory( 16 ));
		test.run(15);
	}

	public void run( int n ) {
		ATerm N = tzero;
		for( int i = 0; i < n; i++ ) { N = suc(N); }
		ATerm res = tnil;
		res = primes( N );
		System.out.println("primes("+n+")");
		System.out.println("result = " + res);
		while ( res != tnil ) {
			System.out.println("result = " + toInt(tete(res)));
			res = reste(res);
		}
	}

	public PrimesT2( ATermFactory factory ) {
		this.factory = factory;
		fzero = factory.makeAFun( "zero", 0, false);
		fsuc = factory.makeAFun( "suc", 1, false);
		fnil = factory.makeAFun( "nil", 0, false);
		fcons = factory.makeAFun( "cons", 2, false);
		fapp = factory.makeAFun( "app", 2, false);
		fprimes = factory.makeAFun( "primes", 1, false);
		faddifprime = factory.makeAFun( "addifprime", 2, false);
		tzero = factory.makeAppl( fzero );
		tnil = factory.makeAppl( fnil );
	}

	public ATerm suc(ATerm t) {
		return (ATerm) factory.makeAppl(fsuc, t);
	}

	public ATerm cons( ATerm t, ATerm l ) {
		return (ATerm) factory.makeAppl( fcons, t, l );
	}


	%rule {

// ****** erreur ****** y1 au lieu de y

		app(nil, y) 	-> y1

// ****** erreur ******

		app(cons( x, y ), z)	-> cons( x, app( y, z ))
	}
	

	public ATerm addifprime( ATerm t, ATerm l ) {
		%match(list l ) {
			nil	-> { return cons( t, tnil ); }
			cons( i, y )	-> { if ( ( toInt(i) * toInt(i) ) > toInt(t) )
						{ return cons( i, app( y, cons( t, tnil ))); } }
			cons( i, y ) -> { if ( ( toInt(t) % toInt(i) ) == 0 )
						{ return cons( i, y ); } } 
			cons( i, y ) -> { return cons( i, addifprime( t, y )); } 
		}
		return null;
	}

	public ATerm primes( ATerm t ) {
		%match( term t ) {
			zero	-> { return tnil; }
			suc(zero)	-> { return tnil; }
			suc(suc(zero))	-> { return cons( t, tnil ); }
			suc(x)	-> { return addifprime( suc(x), primes(x)); }
		}
		return null;
	}	

	public int toInt( ATerm t ) {
		%match( term t ) {
			zero -> { return 0; }
			suc(x) -> { return 1 + toInt(x) ; }
		}
		return 0;
	}			

	public ATerm tete( ATerm l ) {
		%match( list l ) {
			nil -> { return tzero; }
			cons(x,y) -> { return x; }
		}
		return null;
	}

	public ATerm reste( ATerm l ) {	
		%match( list l ) {
			cons( x, y ) -> { return y; }
		}
		return null;
	}

}

	 

	

