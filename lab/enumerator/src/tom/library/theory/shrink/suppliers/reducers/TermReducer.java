package tom.library.theory.shrink.suppliers.reducers;

import java.util.ArrayList;
import java.util.List;

import tom.library.enumerator.Enumeration;
import tom.library.sl.Visitable;
import tom.library.theory.shrink.ShrinkException;
import tom.library.theory.shrink.tools.TermClass;

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

	public List<Object> getInputValues() {
		buildInputValues();
		return inputs;
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
	
	
	private void buildInputValues() {
		terminals.addAll(getTerminalConstructorFromEnumeration());
		getSubtermsWithSameType();
		inputs.addAll(0, terminals);
	}

	
	private void getSubtermsWithSameType() {
		getSubtermsWithSameType(root);
		if (inputs.isEmpty()) {
			inputs.add(root);
		}
	}

	private void getSubtermsWithSameType(Visitable term) {
		for (Visitable child : term.getChildren()) {
			if (isChildHasSameTypeAsRootAndNotConstructor(child)) {
				inputs.add(child);
			} else {
				getSubtermsWithSameType(child);
			}
		}
	}
	
	private boolean isChildHasSameTypeAsRootAndNotConstructor(Visitable term) {
		return isTermHaveTheSameSortAsRoot(term) && !isTerminalConstructor(term);
	}
	
	private boolean isTermHaveTheSameSortAsRoot(Visitable term) {
		return term.getClass().getSuperclass().equals(this.root.getClass().getSuperclass());
	}
	
	private boolean isTerminalConstructor(Visitable term) {
		return terminals.contains(term);
	}
	
	public List<Visitable> getTerminalConstructorFromEnumeration() {
		return TermClass.getTerminalConstructorFromEnumeration(enumeration);
	}
	
	public void setRoot(Object root) throws ShrinkException {
		initialize(root, enumeration);
	}
}
