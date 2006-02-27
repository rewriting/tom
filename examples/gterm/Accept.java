public interface Accept {
	public AbstractType accept(Visitor v) throws jjtraveler.VisitFailure;
}
