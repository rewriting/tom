package tom.library.theory.shrink.suppliers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.PotentialAssignment;

import tom.library.enumerator.Enumeration;
import tom.library.theory.TomCheck;
import tom.library.theory.shrink.ShrinkException;
import tom.library.theory.shrink.ShrinkParameterSupplier;
import tom.library.theory.shrink.suppliers.reducers.TermExploder;
import tom.library.theory.shrink.suppliers.reducers.TermReducer;
import tom.library.theory.shrink.suppliers.reducers.TermValueReducer;

public class ShrinkingValueSupplier_bad implements ShrinkParameterSupplier {
	private HashSet<Object> explored;
	private HashSet<Object> toExplore;
	
	private TermReducer reducer;
	private TermExploder exploder;
	private TermValueReducer valueReducer;
	
	public ShrinkingValueSupplier_bad() {
		explored = new HashSet<Object>();
		toExplore = new HashSet<Object>();
	}
	
	@Override
	public List<PotentialAssignment> getValueSources(
			Object counterExample) {
		try {
			//reducer = TermReducer.build(counterExample, getInitializedEnumeration(signature));
			exploder = TermExploder.build(counterExample);
			valueReducer = TermValueReducer.build(counterExample);
		} catch (ShrinkException e) {
			e.printStackTrace();
		}
		toExplore.add(counterExample);
		Iterator<Object> iterators = toExplore.iterator();
		
		while (iterators.hasNext()) {
			Object object = iterators.next();
			explored.add(object);
			iterators.remove();
			iterators = toExplore.iterator();
		}
		
		return SupplierHelper.buildPotentialAssignments(Arrays.asList(explored.toArray(new Object[0])));
	}
	
	protected Enumeration<?> getInitializedEnumeration(ParameterSignature parameter) {
		return TomCheck.get(parameter.getType());
	}

	private List<Object> shrinkTerm(Object object) {
		// reduce term
		List<Object> reducedTerms = new ArrayList<Object>();
		try {
			reducer.setRoot(object);
		} catch (ShrinkException e) {
			e.printStackTrace();
		}
		reducedTerms = reducer.getInputValues();
		addToExplore(reducedTerms);
		// explode term
		try {
			exploder.setTerm(object);
		} catch (ShrinkException e) {
			e.printStackTrace();
		}
		try {
			reducedTerms = exploder.explodeTermToSmallerTerms();
		} catch (ShrinkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addToExplore(reducedTerms);
		// reduce term value
		
		return reducedTerms;
	}
	
	private void addToExplore(List<Object> inputs) {
		for (Object term : inputs) {
			toExplore.add(term);
		}
	}
}
