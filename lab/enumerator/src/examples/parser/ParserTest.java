package examples.parser;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.ExhaustiveForAll;
import tom.library.theory.RandomForAll;
import tom.library.theory.TomCheck;
import examples.parser.rec.types.Exp;
import examples.parser.rec.types.ExpList;
import examples.parser.rec.types.Stm;

@RunWith(TomCheck.class)
public class ParserTest {
	
	@Enum public static Enumeration<Exp> enumeration1 = Exp.getEnumeration();
	@Enum public static Enumeration<ExpList> enumeration2 = ExpList.getEnumeration();
	@Enum public static Enumeration<Stm> enumeration3 = Stm.getEnumeration();

	/*
	@Theory
	public void testExp(@RandomForAll(sampleSize=100) Exp n) {
		System.out.println("Quick: "+n);
	}
*/
	/*
	@Theory
	public void testExp2(@ExhaustiveForAll(maxDepth=3) Exp n) {
		System.out.println("Quick: "+n);
	}
*/
	
	@Theory
	public void testExpList(@RandomForAll(sampleSize=100) ExpList n) {
		System.out.println("Quick: "+n);
	}
	
	@Theory
	public void testExpList(@RandomForAll(sampleSize=100) Stm n) {
		System.out.println("Quick: "+n);
	}
	


}