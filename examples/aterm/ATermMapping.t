import aterm.*;
import aterm.pure.PureFactory;


/**********

 - inverser les listes (que fait vraiment insert dans la lib aterm) ?
 - utiliser des %typearray à la place des %typelist pour les ATermList ?
 - ne pas matcher les afun mais directement des strings ?

 cf code : 
  - une seule ligne dans les make ?
  - comment gérer les exception ?
  
************/



public class ATermMapping {

    %include { string.tom }
    %include { int.tom }
    %include { boolean.tom }

    ATermFactory atermFactory = new PureFactory();



  %typelist ATerm {
    implement { ATerm }
    get_fun_sym(t) { null }
    cmp_fun_sym(s1,s2)  { false }
    //get_subterm(t,n) {null}
    equals(t1, t2) { t1 == t2}
    get_head(t) { ((ATermList)t).getFirst() }
    get_tail(t) { ((ATermList)t).getNext() }
    is_empty(t) { ((ATermList)t).isEmpty() } 
  }
    
  %typelist ATermList {
    implement { ATermList }
    get_fun_sym(t) { null }
    cmp_fun_sym(s1,s2)  { false }
    equals(t1, t2) { t1 == t2}
    get_head(t) { t.getFirst() }
    get_tail(t) { t.getNext() }
    is_empty(t) { t.isEmpty() } 
  }
  
  %typeterm AFun {
    implement { AFun }
    get_fun_sym(t) { null }
    cmp_fun_sym(s1,s2)  { false }
    get_subterm(t,n) { null }
    equals(t1, t2) { t1 == t2 }
  }

    /* ------ operators ----------- */

    /** ATermList **/

  
  %oplist ATerm ATermList ( ATerm* ){
    fsym {}
    is_fsym(t) { t instanceof ATermList } 
    make_empty() { atermFactory.makeList() }
    make_insert(e,l) { ((ATermList)l).insert(e) }
  }
    
  %oplist ATermList concATerm (ATerm*){
    fsym {}
    is_fsym(t) { t instanceof ATermList } 
    make_empty() { atermFactory.makeList() }
    make_insert(e,l) { l.insert(e) }
  }
  
    /** ATermInt  **/

    %op ATerm ATermInt(val: int) {
	fsym {}
	is_fsym(t) { t instanceof ATermInt }
	make(i) { atermFactory.makeInt(i) }
	get_slot(val, t) { ((ATermInt)t).getInt() }
    }
    
    /** ATermFun **/

  %op AFun AFun(name: String, arity: int, quoted: boolean)  {
    fsym {}
    is_fsym(t) { t instanceof AFun } // && t.getArity() == arity 
    make(f,a,q) { atermFactory.makeAFun(f,a,q) }
    get_slot(name, t) { ((AFun)t).getName() }
    get_slot(arity, t) { ((AFun)t).getArity() }
    get_slot(quoted, t) { ((AFun)t).isQuoted() }
  }

    /** AtermAppl **/

  %op ATerm ATermAppl(fun: AFun, args: ATermList  ) {
    fsym {}
    is_fsym(t) { t instanceof ATermAppl }
    make(f,args) { atermFactory.makeAppl(f,args) }
    get_slot(fun, t) { ((ATermAppl)t).getAFun() }
    get_slot(args, t) { ((ATermAppl)t).getArguments() }
  }

	    
    %op ATerm ATermAppl_0(fun: AFun ) {
	fsym {}
	is_fsym(t) { t instanceof ATermAppl && ((ATermAppl)t).getArity() == 0 }
	make(f) { atermFactory.makeAppl(f) }
	get_slot(fun, t) { ((ATermAppl)t).getAFun() }
    }

    %op ATerm ATermAppl_1(fun: AFun, arg1: ATerm) {
	fsym {}
	is_fsym(t) { t instanceof ATermAppl && ((ATermAppl)t).getArity() == 1 }
	make(f,a1) { atermFactory.makeAppl(f,a1) }
	get_slot(fun, t) { ((ATermAppl)t).getAFun() }
	get_slot(arg1, t) { ((ATermAppl)t).getArgument(1) }
    }

    %op ATerm ATermAppl_2(fun: AFun, arg1: ATerm, arg2: ATerm) {
	fsym {}
	is_fsym(t) { t instanceof ATermAppl && ((ATermAppl)t).getArity() == 2 }
	make(f,a1,a2) { atermFactory.makeAppl(f,a1,a2) }
	get_slot(fun, t) { ((ATermAppl)t).getAFun() }
	get_slot(arg1, t) { ((ATermAppl)t).getArgument(1) }
	get_slot(arg2, t) { ((ATermAppl)t).getArgument(2) }
    }

    %op ATerm ATermAppl_3(fun: AFun, arg1: ATerm, arg2: ATerm, arg3: ATerm) {
	fsym {}
	is_fsym(t) { t instanceof ATermAppl && ((ATermAppl)t).getArity() == 3 }
	make(f,a1,a2,a3) { atermFactory.makeAppl(f,a1,a2,a3) }
	get_slot(fun, t) { ((ATermAppl)t).getAFun() }
	get_slot(arg1, t) { ((ATermAppl)t).getArgument(1) }
	get_slot(arg2, t) { ((ATermAppl)t).getArgument(2) }
	get_slot(arg3, t) { ((ATermAppl)t).getArgument(3) }
    }


    /** ATerm **/
    
    // les constructeurs suivants sont-ils bien utiles ?

    %op ATerm ATermFromString(String) {
	fsym {}
	is_fsym(t) { t instanceof ATerm }
	make(s) { atermFactory.parse(s) }
    }

    // une seule ligne dans les make ?
    private ATerm readFromFile(String s) {
	ATerm res = null;
	try { res = atermFactory.readFromFile(s); } catch (Exception e) {}
	return res;
    }
    
    %op ATerm ATermFromFile(String) {
	fsym {}
	is_fsym(t) { t instanceof ATerm }
	// comment gérer les exceptions ??
	make(s) { readFromFile(s) }
    }
    

    /*---------------- tests --------------------*/


    public static void main (String[] argv) {
	ATermMapping test = new ATermMapping();
	test.test1();
	test.test2();
    }

    public void test1() {
	ATerm at = atermFactory.parse("f(1,2,3)");

	%match ( ATerm at ) {
    /*
      AFun("f",_,_) -> { System.out.println("f matched"); }
      AFun("g",_,_) -> { System.out.println("g matched"); }
    */
	    ATermAppl_0(_) -> { System.out.println("ATermAppl matched"); }
	    ATermAppl_1(_,_) -> { System.out.println("ATermAppl_1 matched"); }
	    ATermAppl_2(_,_,_) -> { System.out.println("ATermAppl_2 matched"); }
	    ATermAppl_3(_,_,_,_) -> { System.out.println("ATermAppl_3 matched"); }
	    ATermAppl_3[fun=AFun("g",_,_)] -> { System.out.println("ATermAppl_3 g matched"); }
	    ATermAppl_3[fun=AFun("f",_,_)] -> { System.out.println("ATermAppl_3 f matched"); }
	    ATermAppl(AFun[name="f"], concATerm(_*,ATermInt(3))) -> { System.out.println("ATermAppl f,3 matched"); }
      
	}
    }

    public void test2() {
	/*
	ATerm at = atermFactory.parse("[1,2,3]");
	%match ( ATerm at ) {
	    ATermList(ATermInt(1)) -> {  System.out.println("list matched"); }
	}
	*/
    }
}
