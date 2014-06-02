package tom.library.theory.shrink.suppliers;

import java.util.Arrays;
import java.util.List;

import org.junit.contrib.theories.PotentialAssignment;

import tom.library.shrink.Shrink;
import tom.library.shrink.metaterm.TomShrink;

public class TomShrinkValueSupplier implements ShrinkParameterSupplier {

	@Override
	public List<PotentialAssignment> getValueSources(Object counterExample) {
		Shrink shrink = new TomShrink();
		Object[] inputs = shrink.shrink(counterExample).toArray();
		System.out.println("inputs = " + Arrays.toString(inputs));
		List<PotentialAssignment> res = SupplierHelper.buildPotentialAssignments(inputs);
		//System.out.println("res = " + res);
		return res;
	}
}
