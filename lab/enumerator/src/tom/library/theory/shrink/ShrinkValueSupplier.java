package tom.library.theory.shrink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.PotentialAssignment;

import tom.library.enumerator.Enumeration;
import tom.library.theory.TomCheck;

public class ShrinkValueSupplier {
	
	public List<PotentialAssignment> getNextReducedPotentialSources(ParameterSignature parameter, Object counterExample) {
			return getReducedTermsAsSources(parameter, counterExample);
	}
	
	protected List<PotentialAssignment> getReducedTermsAsSources(ParameterSignature parameter, Object term) {
		TermReducer reducer;
		try {
			reducer = TermReducer.build(term, getInitializedEnumeration(parameter));
			return buildValueSources(reducer.getInputValues());
		} catch (ShrinkException e) {
			return buildValueSources(Arrays.asList(term));
		}
	}

	protected Enumeration<?> getInitializedEnumeration(ParameterSignature parameter) {
		return TomCheck.get(parameter.getType());
	}
	
	private List<PotentialAssignment> buildValueSources(List<Object> inputs) {
		List<PotentialAssignment> assignments = new ArrayList<PotentialAssignment>();
		
		for (final Object input : inputs) {
			PotentialAssignment assignment = new PotentialAssignment() {
				
				@Override
				public Object getValue() throws CouldNotGenerateValueException {
					return input;
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
	
	public List<PotentialAssignment> getNextExplodedPotentialSources(Object counterExample) {
		return getExplodedTerms(counterExample);
	}
	
	protected List<PotentialAssignment> getExplodedTerms(Object term) {
		TermExploder exploder;
		try {
			exploder = TermExploder.build(term);
			return buildValueSources(exploder.explodeTermToSmallerTerms());
		} catch (ShrinkException e) {
			return buildValueSources(Arrays.asList(term));
		} 
	}
	
}
