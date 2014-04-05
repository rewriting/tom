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

public class TomValueSupplier extends ParameterSupplier {

	private BigInteger nextRandomBigInteger(BigInteger n) {
		Random rand = new Random();
		BigInteger result = new BigInteger(n.bitLength(), rand);
		while (result.compareTo(n) >= 0) {
			result = new BigInteger(n.bitLength(), rand);
		}
		return result;
	}

	@Override
	public List<PotentialAssignment> getValueSources(
			ParameterSignature signature) {
		boolean exhaustive = false;
		int samplesize = 1;
		int maxnumbersamples = 1;
		if (signature.getAnnotation(ExhaustiveCheck.class) != null) {
			exhaustive = true;
			samplesize = signature.getAnnotation(ExhaustiveCheck.class)
					.maxDepth();
		}
		if (signature.getAnnotation(RandomCheck.class) != null) {
			exhaustive = false;
			samplesize = signature.getAnnotation(RandomCheck.class)
					.sampleSize();
			maxnumbersamples = signature.getAnnotation(RandomCheck.class)
					.numberSamples();
		}

//		System.out.println("EXHAUSTIVE = " + exhaustive + " : " + maxnumbersamples + " samples");
		
		final Enumeration<?> enumeration = TomCheck.get(signature.getType());
		List<PotentialAssignment> l = new ArrayList<PotentialAssignment>();
		LazyList<?> parts = enumeration.parts();

		for (int i = 0; i < samplesize; i++) {
			if (parts.isEmpty())
				break;
			final Finite<?> part = (Finite<?>) parts.head();
			parts = parts.tail();

			BigInteger card = part.getCard();
			if (!card.equals(BigInteger.ZERO)) {
				BigInteger j = BigInteger.ZERO;
				boolean done = false;
				int numbersamples = maxnumbersamples;
				int initialnumbersamples = 0;
				if (!exhaustive && card.compareTo(BigInteger.valueOf(maxnumbersamples)) < 0) {
					numbersamples = card.intValue();
//					System.out.println("CHANGED : " + numbersamples + " samples");
				}
//				System.out.println("CARD for  " + i + " = " + card);
				System.out.println("Max nb samples for  " + i + "-th part with card "+card+" = " + maxnumbersamples);

				while (!done) {
					if (!exhaustive) {
						j = nextRandomBigInteger(card);
					}
					final BigInteger jj = j;
					System.out.println("   Select index " + jj);
					PotentialAssignment assignment = new PotentialAssignment() {
						@Override
						public Object getValue()
								throws CouldNotGenerateValueException {
							return part.get(jj);
						}

						@Override
						public String getDescription()
								throws CouldNotGenerateValueException {
							return null;
						}
					};
					l.add(assignment);
					j = j.add(BigInteger.ONE);
					initialnumbersamples++;
					if (exhaustive) {
//						System.out.println("J (" + i +") = " + j);
//						System.out.println("card  = " + card);
						done = j.compareTo(card) == 0;
					} else {
//						System.out.println("INS  = " + initialnumbersamples);
//						System.out.println("NS  = " + numbersamples);
						done = numbersamples == initialnumbersamples;
					}
				}

			}
		}

		return l;
	}
}
