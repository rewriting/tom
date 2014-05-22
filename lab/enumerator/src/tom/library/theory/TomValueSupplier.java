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
	public List<PotentialAssignment> getValueSources(ParameterSignature signature) {
		TomForAll tomForAllAnnotation = signature.getAnnotation(TomForAll.class);

		/** exhaustive or random mode */
		boolean exhaustive = tomForAllAnnotation.exhaustive();
		
		/** skip parts until minSampleSize */
		int minSampleSize = tomForAllAnnotation.minSampleSize();
		
		/** explore parts up to maxSampleSize */
		int maxSampleSize = tomForAllAnnotation.maxSampleSize();
		
		/** maximal number of selected samples */
		int totalNumberOfSamples = tomForAllAnnotation.numberOfSamples();


		
		if (exhaustive) {		
			if(totalNumberOfSamples == 0) {
				totalNumberOfSamples = Integer.MAX_VALUE;
			} 
		} else {
			if(totalNumberOfSamples == 0) {
				totalNumberOfSamples = maxSampleSize - minSampleSize;
			}
		}

		//System.out.println("EXHAUSTIVE = " + exhaustive + " ; " + totalNumberOfSamples + " samples");
		
		final Enumeration<?> enumeration = TomCheck.get(signature.getType());
		List<PotentialAssignment> l = new ArrayList<PotentialAssignment>();
		LazyList<?> parts = enumeration.parts();

		/** skip parts until minSampleSize */
		for (int i = 0; i < minSampleSize; i++) {
			parts = parts.tail();
		}
		
		int builtSamples = 0;
		for (int i = minSampleSize; i < maxSampleSize && !parts.isEmpty() 
				&& builtSamples < totalNumberOfSamples ; i++) {
			
			final Finite<?> part = (Finite<?>) parts.head();
			parts = parts.tail();

			BigInteger card = part.getCard();
			if (card.equals(BigInteger.ZERO)) {
				/* empty part, do nothing */
			} else {
				/*
				 * case random: numberofSamplesInThisPart = MIN(numberOfSamplesPerPart, card)
				 */
				int numberOfSamplesInThisPart = totalNumberOfSamples - builtSamples;
				
				if(!exhaustive) {
					numberOfSamplesInThisPart = 1 + (totalNumberOfSamples - builtSamples) 
							/ (maxSampleSize - i);
				}
				
				if (card.compareTo(BigInteger.valueOf(numberOfSamplesInThisPart)) < 0) {
					numberOfSamplesInThisPart = card.intValue();
				}
				//System.out.println("card number: " + i);
				//System.out.println("card = " + card + " ; numberOfSamplesInThisPart = " + numberOfSamplesInThisPart);
				
				BigInteger j = BigInteger.ONE.negate();
				int builtSamplesInThisPart = 0;

				while (builtSamplesInThisPart < numberOfSamplesInThisPart
						&& builtSamples < totalNumberOfSamples) {
					if(exhaustive) {
						j = j.add(BigInteger.ONE);
					} else {
						j = nextRandomBigInteger(card);
					}
					final BigInteger jj = j;
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
					builtSamplesInThisPart++;
					builtSamples++;
				}

			}
		}

		return l;
	}
}
