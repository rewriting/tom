package tom.library.theory.shrink.suppliers;

import java.util.ArrayList;
import java.util.List;

import org.junit.contrib.theories.PotentialAssignment;

public class SupplierHelper {
	public static List<PotentialAssignment> buildPotentialAssignments(List<Object> inputs) {
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
