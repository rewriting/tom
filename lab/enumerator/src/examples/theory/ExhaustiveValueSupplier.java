package examples.theory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.ParameterSupplier;
import org.junit.contrib.theories.PotentialAssignment;

import tom.library.enumerator.Enumeration;

public class ExhaustiveValueSupplier extends ParameterSupplier {
	
	@Override
	public List<PotentialAssignment> getValueSources(ParameterSignature signature) {
		int samplesize = signature.getAnnotation(ExhaustiveForAll.class).sampleSize();
		final Enumeration<?> enumeration = TomCheck.get(signature.getType());
		List<PotentialAssignment> l = new ArrayList<PotentialAssignment>();
		
		for (int i = 0; i < samplesize; i++) {
			
			final BigInteger j = BigInteger.valueOf(i);

			PotentialAssignment assignment = new PotentialAssignment() {

				@Override
				public Object getValue() throws CouldNotGenerateValueException {
					return enumeration.get(j);
				}

				@Override
				public String getDescription() throws CouldNotGenerateValueException {
					return null;
				}
			};
			l.add(assignment);
		}
		return l;
	}
	

}
