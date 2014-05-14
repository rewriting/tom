package tom.library.theory.shrink.suppliers;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.PotentialAssignment;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import tom.library.theory.shrink.ShrinkParameterSupplier;

public class CharsSupplier implements ShrinkParameterSupplier {
	%include{ string.tom }
	private List<Object> inputs;

	@Override
	public List<PotentialAssignment> getValueSources(ParameterSignature signature, Object counterExample) {
		inputs = new ArrayList<Object>();
		inputs.add("");
		buildPotentialAssigments((String) counterExample);
		List<PotentialAssignment> result;
		try {
			result = SupplierHelper.buildPotentialAssignments(inputs);
		} catch(Exception e) {
			result = SupplierHelper.buildPotentialAssignments(Arrays.asList(counterExample));
		}
		return result;
	}

	protected void buildPotentialAssigments(String term){
		%match(term) {
			concString(e*,_) -> {
				if(`e*.length() > 0) {
					inputs.add(`e*);
					`buildPotentialAssigments(e*);
				}
			}
		}
	}

}
