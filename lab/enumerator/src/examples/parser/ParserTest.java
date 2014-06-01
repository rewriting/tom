package examples.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.ForSome;
import tom.library.theory.PropCheck;
import examples.parser.rec.types.Exp;
import examples.parser.rec.types.ExpList;
import examples.parser.rec.types.Stm;
import examples.parser.rec.types.Table;

@RunWith(PropCheck.class)
public class ParserTest {
	
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
	/*
	@Theory
	public void testExpList(@ForSome(maxSampleSize=100) ExpList n) {
		System.out.println("Quick: "+n);
	}
	
	@Theory
	public void testExpList(@ForSome(maxSampleSize=100) Stm n) {
		System.out.println("Quick: "+n);
	}
	*/
	
	@Theory
	public void testInsertTable(
			@ForSome(maxSampleSize=5) String name,
			@ForSome(maxSampleSize=5) Integer value,
			@ForSome(maxSampleSize=5) Table table
			) {
        Table newTable = examples.parser.rec.types.table.Table.make(name, value, table);
		//System.out.println("'" + name + "' " + value + " " + table);
        assertEquals("newtable: " + newTable,(int)value, Main.lookup(newTable,name));
	}
	
	@Theory
	public void testOverideTable(
			@ForSome(maxSampleSize=5) String name,
			@ForSome(maxSampleSize=5) Integer value,
			@ForSome(maxSampleSize=50) Table table
			) {
		Integer old = Main.lookup(table, name);
		//System.out.println(old);
		assumeTrue(old != 0);
		
        Table newTable = examples.parser.rec.types.table.Table.make(name, value, table);
		//System.out.println("'" + name + "' " + value + " " + table);
        assertEquals("newtable: " + newTable,(int)value, Main.lookup(newTable,name));
	}
	
	@Theory
	public void testInterpPrint(
			@ForSome(maxSampleSize=10) ExpList explist,
			@ForSome(maxSampleSize=10) Table table
			) {
        Table newTable = Main.interpPrint(explist,table);
		System.out.println(explist + " " + table);
		
		boolean found = (newTable == table);
		/*
		while(!found || !newTable.isEmptyTable()) {
			if(newTable == table) {
				found = true;
			} else {
				newTable = newTable.getTail();
			}
			
		}
		*/
		if(!found) {
			fail();
		}
		
        //assertEquals(newTable,table);
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
