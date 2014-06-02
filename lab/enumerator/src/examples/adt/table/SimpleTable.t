package examples.adt.table;

import java.util.ArrayList;
import java.util.List;

import examples.adt.table.table.types.*;
import examples.adt.table.table.types.elem.elem;

public class SimpleTable {
	%include{ table/Table.tom }
	
	private List<Elem> data;
		
	public SimpleTable() {
		data = new ArrayList<Elem>();
					
	}
		
	public int size() {
		return data.size();
					
	}
		
	public boolean isEmpty() {
		return data.isEmpty();
					
	}

	public Val getValue(Key key) {
		for (Elem e : data) {
			if (e.getKey() == key) {
				return e.getValue();
			}
		}
		return null;
	}

	/**
	* Wrong implementation, should check if the key is present first
	*/
	public void add(Key key, Val val) {
		data.add(elem.make(key, val));
					
	}
		
	public void remove(Key key) {
		List<Elem> tmp = new ArrayList<Elem>(data);
		for (Elem e : tmp) {
			if (e.getKey() == key) {
				data.remove(e);
			}
		}
	}
		
	public boolean has(Key key) {
		for (Elem e : data) {
			if (e.getKey() == key) {
				return true;
			}
		}
		return false;
	}

	public void fromTable(Table table) {
		%match(table) {
			table(x, t) -> { 
				if(data.contains(`x)) {
					data.remove(`x);
				}
				data.add(`x);
				`fromTable(t);
			}
		}
	}

	public List<Elem> getData() {
		return data;
	}

	public Table toTable() {
		Table result = `empty();
		for (Elem e : data) {
			result = `table(e, result);
		} 
		return result;
	}
}

