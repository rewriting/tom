package tom.library.theory.shrink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.PotentialAssignment;

import tom.library.enumerator.Enumeration;
import tom.library.theory.TomCheck;

public class ShrinkValueSupplier {
	// TODO make TermReducer.getValueSource() returns only list of reduced terms
	// and build the PotentialAssigment here.
	
	public List<PotentialAssignment> getNextPotentialSources(ParameterSignature parameter, Object cex) {
			return getReducedTermsAsSources(parameter, cex);
	}
	
	protected List<PotentialAssignment> getReducedTermsAsSources(ParameterSignature parameter, Object term) {
		TermReducer reducer;
		try {
			reducer = TermReducer.build(term, getInitializedEnumeration(parameter));
		} catch (ShrinkException e) {
			return buildValueSources(Arrays.asList(term));
		} 
		return buildValueSources(reducer.getInputValues());
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
	
}
