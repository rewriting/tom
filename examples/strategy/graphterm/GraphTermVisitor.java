package strategy.graphterm;

public interface GraphTermVisitor {

	public strategy.graphterm.types.GraphTerm visit_GraphTerm(strategy.graphterm.types.GraphTerm arg) throws jjtraveler.VisitFailure;
	public strategy.graphterm.types.GraphTermList visit_GraphTermList(strategy.graphterm.types.GraphTermList arg) throws jjtraveler.VisitFailure;

}
