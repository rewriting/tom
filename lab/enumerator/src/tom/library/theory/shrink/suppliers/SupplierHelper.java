package tom.library.theory.shrink.suppliers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.contrib.theories.PotentialAssignment;

/**
 * Helper class to transform a list of inputs to a list of {@code PotentialAssignment}
 * @author nauval
 *
 */
public class SupplierHelper {
	
	public static<T> List<PotentialAssignment> buildPotentialAssignments(List<T> inputs) {
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
	
	public static<T> List<PotentialAssignment> buildPotentialAssignments(T[] inputs) {
		return buildPotentialAssignments(Arrays.asList(inputs));
	}
}
