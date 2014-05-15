package tom.library.theory.shrink.suppliers;

import java.util.Arrays;
import java.util.List;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.PotentialAssignment;

import tom.library.theory.shrink.ShrinkException;
import tom.library.theory.shrink.ShrinkParameterSupplier;
import tom.library.theory.shrink.suppliers.reducers.TermExploder;

public class ExplodedTermParameterSupplier implements ShrinkParameterSupplier {

	@Override
	public List<PotentialAssignment> getValueSources(
			ParameterSignature signature, Object counterExample) {
		return getExplodedTerms(counterExample);
	}

	protected List<PotentialAssignment> getExplodedTerms(Object term) {
		TermExploder exploder;
		try {
			exploder = TermExploder.build(term);
			return SupplierHelper.buildPotentialAssignments(exploder.explodeTermToSmallerTerms());
		} catch (ShrinkException e) {
			return SupplierHelper.buildPotentialAssignments(Arrays.asList(term));
		} 
	}
}
