package examples.data.treenat;

public class Suc extends Nat {
	Nat p;

	public Suc(Nat t) {
		this.p=t;
	}

	public String toString() {
		return "Suc(" + p + ")";
	}

	public int toInt() { return 1+p.toInt(); }
}