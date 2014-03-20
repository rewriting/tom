package examples.theory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.ParameterSupplier;
import org.junit.contrib.theories.PotentialAssignment;
import org.junit.contrib.theories.PotentialAssignment.CouldNotGenerateValueException;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import tom.library.enumerator.F2;

public class ExhaustiveValueSupplier extends ParameterSupplier {

	HashMap<String, Enumeration> enumerations;
	
	public ExhaustiveValueSupplier() {
		enumerations = new HashMap<>();
		enumerations.put("TreeNat", TreeEnumerations.makeTreeNatEnumeration());
		enumerations.put("int", Combinators.makeint());
		enumerations.put("String", Combinators.makeString());
	}
	@Override
	public List<PotentialAssignment> getValueSources(ParameterSignature signature) {
		int samplesize = signature.getAnnotation(ExhaustiveForAll.class).sampleSize();
		String enumerationName = signature.getAnnotation(ExhaustiveForAll.class).enumerationName();
		final Enumeration enumeration = enumerations.get(enumerationName);
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
