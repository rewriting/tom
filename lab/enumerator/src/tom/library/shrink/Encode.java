package tom.library.shrink;

import aterm.AFun;
import aterm.ATerm;
import aterm.ATermFactory;
import examples.adt.tree.types.*;
import tom.library.sl.Visitable;
import tom.library.sl.VisitableBuiltin;
import tom.library.shrink.metaterm.types.*;
import tom.library.shrink.metaterm.types.symbol.Fun;
import tom.library.shrink.metaterm.types.term.Appl;
import tom.library.shrink.metaterm.types.termlist.EmptyTermList;
import tom.library.shrink.metaterm.types.termlist.ConsTermList;

public class Encode {

	
	public static Term encode(tom.library.sl.Visitable t) {
		TermList args = EmptyTermList.make();
		for(int i=t.getChildCount() - 1 ; i >= 0 ; i--) {
			args = ConsTermList.make(encode(t.getChildAt(i)), args);
		}
		String name, type;
		if(t instanceof VisitableBuiltin) {
			name = ((VisitableBuiltin)t).getBuiltin().toString();
			type = ((VisitableBuiltin)t).getBuiltin().getClass().toString();
		} else {
			name = t.getClass().toString();
			type = t.getClass().getGenericSuperclass().toString();
		}
		Symbol symb = Fun.make(name, type);		
		return Appl.make(symb, args);
	}
	
	private static ATermFactory atFactory = aterm.pure.SingletonFactory.getInstance();

	
	public static void main(String[] Args) {
		
		Tree t = Tree.fromString("tree(val(3),empty(),tree(val(5),empty(),empty()))");
		
		System.out.println("t = " + t);
		System.out.println("t.class = " + t.getClass());
		System.out.println("t.class.class = " + t.getClass().getGenericSuperclass());
		
		System.out.println("encoded t = " + encode(t));

		
		ATerm at = t.toATerm();
		System.out.println("at = " + at);

		
		
	}
}
