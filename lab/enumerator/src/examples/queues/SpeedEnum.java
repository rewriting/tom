package examples.queues;

import java.math.BigInteger;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import examples.queues.queue.types.*;

public class SpeedEnum {

	public static void main(String[] args) {
		int nb = 3000;
		BigInteger TWO = BigInteger.ONE.add(BigInteger.ONE);
		
		Enumeration<Queue> e = Queue.getEnumeration();
		long start = System.currentTimeMillis();
		BigInteger index = BigInteger.ONE;
		for(int i = 0 ; i<nb ; i++) {
			Queue q = e.get(index);
			//System.out.println(q);
			
			//index = index.add(BigInteger.ONE);
			index = index.multiply(TWO);
		}
		long stop = System.currentTimeMillis();

		System.out.println(nb + " elems in " + (stop-start));
	}

}
