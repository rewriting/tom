package tom.library.theory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.ParameterSupplier;
import org.junit.contrib.theories.PotentialAssignment;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;

public class ExhaustiveValueSupplier extends ParameterSupplier {

	@Override
	public List<PotentialAssignment> getValueSources(ParameterSignature signature) {
		int maxdepth = signature.getAnnotation(ExhaustiveForAll.class).maxDepth();
		final Enumeration<?> enumeration = TomCheck.get(signature.getType());
		List<PotentialAssignment> l = new ArrayList<PotentialAssignment>();
		LazyList<?> parts = enumeration.parts();

		for (int i = 0; i < maxdepth; i++) {
			if (parts.isEmpty()) break;
			final Finite<?> part = (Finite<?>) parts.head();
			parts = parts.tail();
			if (! part.equals(Finite.empty())) {
				BigInteger card = part.getCard();
				for (int j = 0; j < card.intValue(); j++) {

					final BigInteger jj = BigInteger.valueOf(j);

					PotentialAssignment assignment = new PotentialAssignment() {

						@Override
						public Object getValue() throws CouldNotGenerateValueException {
							return part.get(jj);
						}

						@Override
						public String getDescription() throws CouldNotGenerateValueException {
							return null;
						}
					};
					l.add(assignment);
				}
			}
		}
		return l;
	}


}
