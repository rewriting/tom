package tom.library.theory.shrink.suppliers;

import java.util.ArrayList;
import java.util.List;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.PotentialAssignment;

import tom.library.theory.shrink.ShrinkParameterSupplier;

/**
 * Generates list of {@code PotentialAssignment}s that are used in the shrinking process.
 * There are three steps to generate the {@code PotentialAssignment}s:
 * <ol>
 * <li>Generate subterms of the initial term that have the same sort as the initial term</li>
 * <li>Explode the term: generate new terms based on the initial term that has smaller size. It 
 * is done by replacing its child with a terminal of its child's sort, recursively</li>
 * <li>Reduce the value inside a term, e.g, cs(3) to [cs(0), cs(1), cs(2)]
 * </ol>
 * @author nauval
 *
 */
public class ShrunkenTermsParameterSupplier implements ShrinkParameterSupplier{

	@Override
	public List<PotentialAssignment> getValueSources(
			Object counterExample) {
		return null;//buildValueSources(signature, counterExample);
	}
	
	protected List<PotentialAssignment> buildValueSources(ParameterSignature signature, Object counterExample) {
		List<PotentialAssignment> inputs = new ArrayList<PotentialAssignment>();
		ShrinkParameterSupplier reducedTermsSupplier = new ReducedTermsParameterSupplier();
		ShrinkParameterSupplier explodedTermsSupplier = new ExplodedTermParameterSupplier();
		ShrinkParameterSupplier reducedValueSupplier = new ReducedValueParameterSupplier();
		
		inputs.addAll(reducedTermsSupplier.getValueSources(counterExample));
		inputs.addAll(explodedTermsSupplier.getValueSources(counterExample));
		inputs.addAll(reducedValueSupplier.getValueSources(counterExample));
		
		return inputs;
	}
}
