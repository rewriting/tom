package examples.parser;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.ExhaustiveForAll;
import tom.library.theory.RandomForAll;
import tom.library.theory.TomCheck;
import examples.junit.quickcheck.ExpGenerator;
import examples.parser.rec.types.Exp;
import examples.parser.rec.types.ExpList;
import examples.parser.rec.types.Stm;
import examples.parser.rec.types.Table;

@RunWith(TomCheck.class)
public class ParserTest {
	
	@Enum public static Enumeration<Exp> enumeration1 = Exp.getEnumeration();
	@Enum public static Enumeration<ExpList> enumeration2 = ExpList.getEnumeration();
	@Enum public static Enumeration<Stm> enumeration3 = Stm.getEnumeration();
	@Enum public static Enumeration<Table> enumeration4 = Table.getEnumeration();
	@Enum public static Enumeration<Integer> enumeration5 = Combinators.makeint();
	@Enum public static Enumeration<String> enumeration6 = Combinators.makeString();
	

	/*
	@Theory
	public void testExp(@ForAll(sampleSize=100) @From({ ExpGenerator.class }) Exp n) {
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
	
	@Theory
	public void testInsertTable(
			@RandomForAll(sampleSize=5) String name,
			@RandomForAll(sampleSize=5) Integer value,
			@RandomForAll(sampleSize=5) Table table
			) {
		//System.out.println(name);
		//String name = "a";
		//int value = 42;
        Table newTable = examples.parser.rec.types.table.Table.make(name, value, table);
		System.out.println("'" + name + "' " + value + " " + table);

        //assertTrue(value == Main.lookup(newTable,name));

        assertEquals("newtable: " + newTable,(int)value, Main.lookup(newTable,name));
	}
	
	/*
	@Theory
	public void testExp2(
			@ForAll(sampleSize = 10) @From({ ExpGenerator.class }) Exp e1,
			@ForAll(sampleSize = 10) @From({ ExpGenerator.class }) Exp e2) {
		System.out.println("Exp: " + e1 + " x " + e2);
	}
	*/

}