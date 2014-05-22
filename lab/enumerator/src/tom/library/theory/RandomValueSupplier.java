package tom.library.theory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.ParameterSupplier;
import org.junit.contrib.theories.PotentialAssignment;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;

public class RandomValueSupplier extends ParameterSupplier {

	private BigInteger nextRandomBigInteger(BigInteger n) {
		Random rand = new Random();
		BigInteger result = new BigInteger(n.bitLength(), rand);
		while( result.compareTo(n) >= 0 ) {
			result = new BigInteger(n.bitLength(), rand);
		}
		return result;
	}

	@Override
	public List<PotentialAssignment> getValueSources(ParameterSignature signature) {
		int samplesize = signature.getAnnotation(RandomForAll.class).sampleSize();
		final Enumeration<?> enumeration = TomCheck.get(signature.getType());
		List<PotentialAssignment> l = new ArrayList<PotentialAssignment>();
		LazyList<?> parts = enumeration.parts();
		int i = 0;
		while (i < samplesize) {
			if (parts.isEmpty()) break;
			final Finite<?> part = (Finite<?>) parts.head();
			parts = parts.tail();
			BigInteger card = part.getCard();
			if (! card.equals(BigInteger.ZERO)) {
				i++;
				final BigInteger j = nextRandomBigInteger(card);	
				PotentialAssignment assignment = new PotentialAssignment() {

					@Override
					public Object getValue() throws CouldNotGenerateValueException {
						return part.get(j);
					}

					@Override
					public String getDescription() throws CouldNotGenerateValueException {
						return null;
					}
				};
				l.add(assignment);
			}
		}
		return l;
	}


}
