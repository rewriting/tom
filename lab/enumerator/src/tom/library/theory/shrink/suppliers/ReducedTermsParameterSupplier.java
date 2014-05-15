package tom.library.theory.shrink.suppliers;

import java.util.Arrays;
import java.util.List;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.PotentialAssignment;

import tom.library.enumerator.Enumeration;
import tom.library.theory.TomCheck;
import tom.library.theory.shrink.ShrinkException;
import tom.library.theory.shrink.ShrinkParameterSupplier;
import tom.library.theory.shrink.suppliers.reducers.TermReducer;

public class ReducedTermsParameterSupplier implements ShrinkParameterSupplier{

	@Override
	public List<PotentialAssignment> getValueSources(
			ParameterSignature signature, Object counterExample) {
		return getReducedTermsAsSources(signature, counterExample);
	}
	
	protected List<PotentialAssignment> getReducedTermsAsSources(ParameterSignature parameter, Object term) {
		TermReducer reducer;
		try {
			reducer = TermReducer.build(term, getInitializedEnumeration(parameter));
			return SupplierHelper.buildPotentialAssignments(reducer.getInputValues());
		} catch (ShrinkException e) {
			return SupplierHelper.buildPotentialAssignments(Arrays.asList(term));
		}
	}

	protected Enumeration<?> getInitializedEnumeration(ParameterSignature parameter) {
		return TomCheck.get(parameter.getType());
	}
}
