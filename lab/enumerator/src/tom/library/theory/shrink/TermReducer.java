package tom.library.theory.shrink;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import tom.library.enumerator.Enumeration;
import tom.library.sl.Visitable;

public class TermReducer {
	private Visitable root;
	private Enumeration<?> enumeration;
	private List<Object> inputs;
	private List<Object> terminals;
	
	private TermReducer(Object root, Enumeration<?> enumeration) throws ShrinkException {
		initialize(root, enumeration);
	}
	
	public static TermReducer build(Object term, Enumeration<?> enumeration) throws ShrinkException {
		return new TermReducer(term, enumeration);
	}

	private void initialize(Object root, Enumeration<?> enumeration) throws ShrinkException {
		initializeLists();
		this.enumeration = enumeration;
		if (!isInstanceOfVisitable(root)) {
			throw new ShrinkException(root + "is not a term");
		}
		this.root = (Visitable) root;
	}
	
	private void initializeLists() {
		if (inputs == null) {
			inputs = new ArrayList<Object>();
		}
		if (terminals == null) {
			terminals = new ArrayList<Object>();
		}
	}

	private boolean isInstanceOfVisitable(Object object) {
		return object instanceof Visitable;
	}
	
	public List<Object> getInputValues() {
		buildInputValues();
		return inputs;
	}
	
	private void buildInputValues() {
		terminals.addAll(getTerminalConstructorFromEnumeration());
		getSubtermsWithSameType();
		inputs.addAll(0, terminals);
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
