package examples.theory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.ParameterSupplier;
import org.junit.contrib.theories.PotentialAssignment;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;

public class ExhaustiveValueSupplier extends ParameterSupplier {

	HashMap<String, Enumeration> enumerations;
	
	public ExhaustiveValueSupplier() {
		enumerations = new HashMap<String, Enumeration>();
		// TODO use types instead of the type names
		enumerations.put("examples.theory.Tree<examples.theory.Nat>", TreeEnumerations.makeTreeNatEnumeration());
		enumerations.put("class java.lang.String", Combinators.makeString());
	}
	@Override
	public List<PotentialAssignment> getValueSources(ParameterSignature signature) {
		String parameterClassName = signature.getType().toString();
		System.out.println(parameterClassName);
		int samplesize = signature.getAnnotation(ExhaustiveForAll.class).sampleSize();
		final Enumeration enumeration = enumerations.get(parameterClassName);
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
