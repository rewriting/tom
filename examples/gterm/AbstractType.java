public abstract class AbstractType implements shared.SharedObject, jjtraveler.Visitable, VisitorAccept {
	protected static aterm.ATermFactory ATFactory = aterm.pure.SingletonFactory.getInstance();

	public abstract aterm.ATerm toATerm();

	public String toString() {
		return toATerm().toString();
	}

}
