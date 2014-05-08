package examples.adt;

import java.util.ArrayList;
import java.util.List;

import examples.adt.table.types.*;
import examples.adt.table.types.elem.elem;

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

	/**
	* Wrong implementation, should check if the key is present first
	*/
	public void add(Key key, Val val) {
		data.add(elem.make(key, val));
					
	}
		
	public void remove(Key key) {
		for (Elem e : data) {
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
				data.add(`x);
				`fromTable(t);
			}
		}
	}
}

