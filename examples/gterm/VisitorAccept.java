public interface VisitorAccept {
	public AbstractType accept(VisitorForward v) throws jjtraveler.VisitFailure;
}
