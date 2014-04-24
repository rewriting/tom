package tom.library.theory.shrink;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.contrib.theories.PotentialAssignment;

import tom.library.enumerator.Enumeration;
import tom.library.sl.Visitable;

public class TermReducer {
	private Visitable root;
	private Enumeration<?> enumeration;
	private List<Visitable> inputs;
	private List<Visitable> terminals;
	
	private TermReducer(Object root, Enumeration<?> enumeration) throws Throwable {
		initialize(root, enumeration);
	}
	
	public static TermReducer build(Object term, Enumeration<?> enumeration) throws Throwable {
		return new TermReducer(term, enumeration);
	}

	private void initialize(Object root, Enumeration<?> enumeration) throws Throwable {
		initializeLists();
		this.enumeration = enumeration;
		if (!isInstanceOfVisitable(root)) {
			throw new Throwable(root + " is not algabreic term");
		}
		this.root = (Visitable) root;
	}
	
	private void initializeLists() {
		if (inputs == null) {
			inputs = new ArrayList<Visitable>();
		}
		if (terminals == null) {
			terminals = new ArrayList<Visitable>();
		}
	}

	private boolean isInstanceOfVisitable(Object object) {
		return object instanceof Visitable;
	}
	
	public List<PotentialAssignment> getValueSources() {
		buildInputValues();
		return buildValueSources();
	}
	
	private void buildInputValues() {
		terminals.addAll(getTerminalConstructorFromEnumeration());
		getSubtermsWithSameType();
		inputs.addAll(0, terminals);
	}

	private List<PotentialAssignment> buildValueSources() {
		List<PotentialAssignment> assignments = new ArrayList<PotentialAssignment>();
		
		for (final Visitable visitable : inputs) {
			PotentialAssignment assignment = new PotentialAssignment() {
				
				@Override
				public Object getValue() throws CouldNotGenerateValueException {
					return visitable;
				}
				
				@Override
				public String getDescription() throws CouldNotGenerateValueException {
					return null;
				}
			}; 
			assignments.add(assignment);
		}
		return assignments;
	}
	
	private void getSubtermsWithSameType() {
		getSubtermsWithSameType(root);
		if (inputs.size() == 0) {
			inputs.add(root);
		}
	}

	private void getSubtermsWithSameType(Visitable term) {
		for (Visitable child : term.getChildren()) {
			if (isChildHasSameTypeAsRoot(child)) {
				inputs.add(child);
			} 
			getSubtermsWithSameType(child);
		}
	}
	
	private boolean isChildHasSameTypeAsRoot(Visitable term) {
		return isTermHaveTheSameSortAsRoot(term) && !isTerminalConstructor(term);
	}
	
	private boolean isTermHaveTheSameSortAsRoot(Visitable term) {
		return term.getClass().getSuperclass().equals(this.root.getClass().getSuperclass());
	}
	
	private boolean isTerminalConstructor(Visitable term) {
		return terminals.contains(term);
	}
	
	public List<Visitable> getTerminalConstructorFromEnumeration() {
		List<Visitable> constants = new ArrayList<Visitable>();
		BigInteger card = enumeration.parts().head().getCard();
		BigInteger index = BigInteger.ZERO;
		while (card.compareTo(index) > 0) {
			constants.add((Visitable) enumeration.get(index));
			index = index.add(BigInteger.ONE);
		}
		return constants;
	}
}
