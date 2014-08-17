package tom.library.theory.shrink.suppliers;

import java.util.List;

import org.junit.contrib.theories.PotentialAssignment;

import tom.library.shrink.Shrink;
import tom.library.shrink.SimpleShrink;
import tom.library.shrink.TermComparator;

/**
 * An implementation of {@code ShrinkParameterSupplier} where it uses
 * {@link SimpleShrink} to generates smaller counter examples.
 * @author nauval
 *
 */
public class ShrinkValueSupplier implements ShrinkParameterSupplier {

	@Override
	public List<PotentialAssignment> getValueSources(
			Object counterExample) {
		Shrink shrink = new SimpleShrink();
		Object[] inputs = shrink.shrink(counterExample, new TermComparator()).toArray();
		return SupplierHelper.buildPotentialAssignments(inputs);
	}
}
