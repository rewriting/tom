package examples.adt;

import examples.adt.table.types.*;

public class TableDemo {
	%include{ table/Table.tom }

	public static boolean isEmpty(Table table) {
		return table.isempty();
	}

	public static int size(Table table) {
		%match(table) {
			empty() -> { return 0; }
			table(e, tail) -> { return 1 + `size(tail); }
		}
		return -1;
	}

	public static boolean has(Table table, Key key) {
		%match(table) {
			table(elem(k, v), tail) -> {
				if(`k == key) {
					return true;
				} else {
					return has(`tail, key);
				}
			}
		}
		return false;
	}

	public static Table add(Table table, Key k, Val v) throws Exception {
		if(isEmpty(table)) {
			return `table(elem(k, v), table);
		} else {
			Table t = remove(table, k);
			return `table(elem(k, v), t);
		}
	}

	public static Table remove(Table table, Key key) throws Exception {
		%match(table) {
			table(e@elem(k,_), tail) -> {
				if(`k == key) {
					return `tail;
				} else {
					return `table(e, remove(tail, key));
				}
			}
		}
		return table;
	}

	public static Val value(Table table, Key key) throws Exception {
		%match(table) {
			table(elem(k, v), t) -> {
				if(`k == key) {
					return `v;
				} else {
					return `value(t, key);
				}
			}
		}
		throw new Exception("Value not found with key: " + key);
	}
}
