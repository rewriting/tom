package test;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

import org.junit.Ignore;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.theory.ForSome;
import tom.library.theory.PropCheck;
import examples.adt.table.SimpleTable;
import examples.adt.table.TableDemo;
import examples.adt.table.table.types.Elem;
import examples.adt.table.table.types.Key;
import examples.adt.table.table.types.Table;
import examples.adt.table.table.types.Val;

@RunWith(PropCheck.class)
public class TableDemoTest {
	@Ignore
	@Theory
	public void testAddValue(
			@ForSome(minSampleSize=20, maxSampleSize = 30) Table table, 
			@ForSome(maxSampleSize = 30) Key key, 
			@ForSome(maxSampleSize = 30) Val val) throws Exception {
		Table result = TableDemo.evaluate(table);
		Table add = TableDemo.add(result, key, val);
		Val v = TableDemo.value(add, key);
		assertThat(v, equalTo(val));
	}	
	
	@Ignore
	@Theory
	public void testAddValueWith2keys(
			@ForSome(minSampleSize=0, maxSampleSize = 10) Table table, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Key key, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Key key2, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Val val) throws Exception {
		Table result = TableDemo.evaluate(table);
		
		assumeThat(result.isempty(), equalTo(false));
		assumeThat(TableDemo.has(result, key2), equalTo(true));
		assumeThat(key, not(equalTo(key2)));
		
		Table add = TableDemo.add(table, key, val);
		Val v = TableDemo.value(add, key2);
		Val v2 = TableDemo.value(table, key2);
		assertThat(v, equalTo(v2));
	}
	
	@Ignore
	@Theory
	public void testDeleteAdd(
			@ForSome(minSampleSize=0, maxSampleSize = 10) Table table, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Key key,  
			@ForSome(minSampleSize=0, maxSampleSize = 10) Val val) throws Exception {
		Table result = TableDemo.evaluate(table);
		assumeThat(TableDemo.has(result, key), equalTo(false));
		Table t = TableDemo.remove(TableDemo.add(result, key, val), key);
		assertThat(t, equalTo(result));
	}
	
	@Ignore
	@Theory
	public void testDeleteAddWith2Keys(
			@ForSome(minSampleSize=0, maxSampleSize = 10) Table table, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Key key, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Key key2, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Val val) throws Exception {
		
		Table result = TableDemo.evaluate(table);
		assumeThat(result.isempty(), equalTo(false));
		assumeThat(TableDemo.has(result, key2), equalTo(true));
		assumeThat(key, not(equalTo(key2)));
		
		Table t = TableDemo.remove(TableDemo.add(result, key, val), key2);
		Table t2 = TableDemo.add(TableDemo.remove(result, key2), key, val);
		assertThat(t, equalTo(t2));
	}
	
	@Ignore
	@Theory
	public void testHas(
			@ForSome(minSampleSize=0, maxSampleSize = 10) Table table, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Key key, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Val val) throws Exception {
		
		Table result = TableDemo.evaluate(table);
		Table t = TableDemo.add(result, key, val);
		assertThat(TableDemo.has(t, key), equalTo(true));
	}
	
	@Ignore
	@Theory
	public void testHasWith2Keys(
			@ForSome(minSampleSize=0, maxSampleSize = 10) Table table, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Key key, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Key key2, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Val val) throws Exception {
		Table result = TableDemo.evaluate(table);
		assumeThat(result.isempty(), equalTo(false));
		assumeThat(key, not(equalTo(key2)));
		
		assertThat(TableDemo.has(TableDemo.add(result, key, val), key2), equalTo(TableDemo.has(result, key2)));
	}
	
	@Ignore
	@Theory
	public void testEvaluate(
			@ForSome(minSampleSize=980, maxSampleSize = 1000) Table table) throws Exception {
		System.out.println("table: " + table);
		Table result = TableDemo.evaluate(table);
		System.out.println("result: " + result);
	}
	
	// comparing two implementations
	@Ignore
	@Theory
	public void testComparisonAddValue(
			@ForSome(minSampleSize=20, maxSampleSize = 30) Table table, 
			@ForSome(maxSampleSize = 30) Key key, 
			@ForSome(maxSampleSize = 30) Val val) throws Exception {
		Table result = TableDemo.evaluate(table);
		Table add = TableDemo.add(result, key, val);
		Val v = TableDemo.value(add, key);
		assertThat("1",v, equalTo(val));
		
		SimpleTable st = new SimpleTable();
		st.fromTable(table);
		st.add(key, val);
		Val v2 = st.getValue(key);
		assertThat("2",v2, equalTo(val));
		
		assertThat(TableDemo.size(add), equalTo((Integer) st.size()));
	}
	
	@Ignore
	@Theory
	public void testComparisonAddValueWith2keys(
			@ForSome(minSampleSize=0, maxSampleSize = 10) Table table, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Key key, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Key key2, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Val val) throws Exception {
		Table result = TableDemo.evaluate(table);
		
		assumeThat(result.isempty(), equalTo(false));
		assumeThat(TableDemo.has(result, key2), equalTo(true));
		assumeThat(key, not(equalTo(key2)));
		
		Table add = TableDemo.add(result, key, val);
		Val v = TableDemo.value(add, key2);
		Val v2 = TableDemo.value(result, key2);
		assertThat("Table 1", v, equalTo(v2));
		
		SimpleTable st = new SimpleTable();
		st.fromTable(table);
		st.add(key, val);
		Val vSt1 = st.getValue(key2);
		
		SimpleTable st2 = new SimpleTable();
		st2.fromTable(table);
		Val vSt2 = st2.getValue(key2);
		
		assertThat("Table 2", vSt1, equalTo(vSt2));
	}
	
	@Ignore
	@Theory
	public void testComparisonDeleteAddWith2Keys(
			@ForSome(minSampleSize=0, maxSampleSize = 10) Table table, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Key key, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Key key2, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Val val) throws Exception {
		
		Table result = TableDemo.evaluate(table);
		assumeThat(result.isempty(), equalTo(false));
		assumeThat(TableDemo.has(result, key2), equalTo(true));
		assumeThat(key, not(equalTo(key2)));
		
		Table t = TableDemo.remove(TableDemo.add(result, key, val), key2);
		Table t2 = TableDemo.add(TableDemo.remove(result, key2), key, val);
		assertThat("1",t, equalTo(t2));
		
		SimpleTable st = new SimpleTable();
		st.fromTable(result);
		st.add(key, val);
		st.remove(key2);
		
		SimpleTable st2 = new SimpleTable();
		st2.fromTable(result);
		st2.remove(key2);
		st2.add(key, val);
		
		assertThat(st2.size(), is(equalTo(st.size())));
		for (Elem e : st.getData()) {
			assertThat("2",st2.has(e.getKey()), is(equalTo(true)));
		}
	}
	
	
	@Theory
	public void testComparisonDeleteAdd(
			@ForSome(minSampleSize=990, maxSampleSize = 1000) Table table, 
			@ForSome(minSampleSize=0, maxSampleSize = 10) Key key,  
			@ForSome(minSampleSize=0, maxSampleSize = 10) Val val) throws Exception {
		Table result = TableDemo.evaluate(table);
		
		assumeThat(TableDemo.has(result, key), equalTo(false));
		
		Table t = TableDemo.remove(TableDemo.add(result, key, val), key);
		assertThat("1",t, equalTo(result));
		
		SimpleTable st = new SimpleTable();
		st.fromTable(result);
		st.add(key, val);
		st.remove(key);
		assertThat("2", st.toTable(), equalTo(result));
		
		assertThat("3",st.toTable(), equalTo(t));
	}
}
