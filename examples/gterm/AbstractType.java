public abstract class AbstractType implements shared.SharedObject, jjtraveler.Visitable, Accept {
	public abstract aterm.ATerm toATerm();

	public String toString() {
		return toATerm().toString();
	}

}
