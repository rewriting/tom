package tom.library.theory.shrink.suppliers;

import java.util.Arrays;
import java.util.List;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.PotentialAssignment;

import tom.library.theory.shrink.ShrinkException;
import tom.library.theory.shrink.ShrinkParameterSupplier;
import tom.library.theory.shrink.suppliers.reducers.IntegerReducer;
import tom.library.theory.shrink.suppliers.reducers.StringReducer;
import tom.library.theory.shrink.suppliers.reducers.TermValueReducer;

public class ReducedValueParameterSupplier implements ShrinkParameterSupplier {

	@Override
	public List<PotentialAssignment> getValueSources(
			ParameterSignature signature, Object counterExample) {
		try {
			TermValueReducer reducer = TermValueReducer.build(counterExample);
			return SupplierHelper.buildPotentialAssignments(reducer.buildTermsWithReducedValue());
		} catch (ShrinkException e) {
			return getValueSourcePrimitives(counterExample);
		}
	}	
	
	private List<PotentialAssignment> getValueSourcePrimitives(Object counterExample) {
		if (isInstanceOfInteger(counterExample)) {
			List<? extends Object> inputs = IntegerReducer.build().getReducedIntegerValue((int) counterExample);
			return SupplierHelper.buildPotentialAssignments(inputs);
		} else if (isInstanceOfString(counterExample)) {
			List<? extends Object> inputs = StringReducer.build().getReducedStringValues((String) counterExample);
			return SupplierHelper.buildPotentialAssignments(inputs);
		} else {
			return SupplierHelper.buildPotentialAssignments(Arrays.asList(counterExample));
		}
	}
	
	private boolean isInstanceOfInteger(Object counterExample) {
		return counterExample instanceof Integer;
	}
	
	private boolean isInstanceOfString(Object counterExample) {
		return counterExample instanceof String;
	}
	
	
}
