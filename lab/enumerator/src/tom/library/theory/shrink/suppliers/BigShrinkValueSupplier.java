package tom.library.theory.shrink.suppliers;

import java.util.List;

import org.junit.contrib.theories.PotentialAssignment;

import tom.library.shrink.BigShrink;
import tom.library.shrink.Shrink;
import tom.library.shrink.TermComparator;

/**
 * An implementation of {@code ShrinkParameterSupplier} that uses
 * {@link BigShrink} to generate smaller counter examples.
 * 
 * @author nauval
 *
 */
public class BigShrinkValueSupplier implements ShrinkParameterSupplier {
	
	@Override
	public List<PotentialAssignment> getValueSources(
			Object counterExample) {
		Shrink shrink = new BigShrink();
		Object[] terms = shrink.shrink(counterExample, new TermComparator()).toArray();
		return SupplierHelper.buildPotentialAssignments(terms);
	}
}
