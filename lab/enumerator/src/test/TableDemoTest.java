package test;


import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import examples.adt.TableDemo;
import examples.adt.table.types.Elem;
import examples.adt.table.types.Key;
import examples.adt.table.types.Table;
import examples.adt.table.types.Val;
import examples.adt.table.types.table.empty;
import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.RandomCheck;
import tom.library.theory.RandomForAll;
import tom.library.theory.TomCheck;
import tom.library.theory.TomForAll;

@RunWith(TomCheck.class)
public class TableDemoTest {
	@Enum public static Enumeration<Table> enumTable = Table.getEnumeration();
	@Enum public static Enumeration<Elem> enumElem = Elem.getEnumeration();
	@Enum public static Enumeration<Key> enumKey = Key.getEnumeration();
	@Enum public static Enumeration<Val> enumVal = Val.getEnumeration();
	
	@Theory
	public void testAddValue(
			@TomForAll @RandomCheck(minSampleSize=20, sampleSize = 30) Table table, 
			@RandomForAll(sampleSize = 30) Key key, 
			@RandomForAll(sampleSize = 30) Val val) throws Exception {
		Table add = TableDemo.add(table, key, val);
		Val v = TableDemo.value(add, key);
		assertThat(v, equalTo(val));
	}
	
	@Theory
	public void testAddValueWith2keys(
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Table table, 
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Key key, 
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Key key2, 
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Val val) throws Exception {
		assumeThat(table.isempty(), equalTo(false));
		assumeThat(TableDemo.has(table, key2), equalTo(true));
		assumeThat(key, not(equalTo(key2)));
		
		Table add = TableDemo.add(table, key, val);
		Val v = TableDemo.value(add, key2);
		Val v2 = TableDemo.value(table, key2);
		assertThat(v, equalTo(v2));
	}
	
	@Theory
	public void testDeleteAdd(
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Table table, 
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Key key,  
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Val val) throws Exception {
		assumeThat(TableDemo.has(table, key), equalTo(false));
		Table t = TableDemo.remove(TableDemo.add(table, key, val), key);
		assertThat(t, equalTo(table));
	}
	
	@Theory
	public void testDeleteAddWith2Keys(
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Table table, 
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Key key, 
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Key key2, 
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Val val) throws Exception {
		
		assumeThat(table.isempty(), equalTo(false));
		assumeThat(TableDemo.has(table, key2), equalTo(true));
		assumeThat(key, not(equalTo(key2)));
		
		Table t = TableDemo.remove(TableDemo.add(table, key, val), key2);
		Table t2 = TableDemo.add(TableDemo.remove(table, key2), key, val);
		assertThat(t, equalTo(t2));
	}
	
	
	@Theory
	public void testHas(
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Table table, 
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Key key, 
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Val val) throws Exception {
		
		
		Table t = TableDemo.add(table, key, val);
		assertThat(TableDemo.has(t, key), equalTo(true));
	}
	
	@Theory
	public void testHasWith2Keys(
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Table table, 
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Key key, 
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Key key2, 
			@TomForAll @RandomCheck(minSampleSize=0, sampleSize = 10) Val val) throws Exception {
		
		assumeThat(table.isempty(), equalTo(false));
		assumeThat(key, not(equalTo(key2)));
		
		assertThat(TableDemo.has(TableDemo.add(table, key, val), key2), equalTo(TableDemo.has(table, key2)));
	}
}
