package examples.junit.quickcheck;

import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;

@RunWith(Theories.class)
public class QuickCheckExample {


	@Theory
	public void testStringQuickcheck(@ForAll(sampleSize=20) @From({MyGenerator.class}) int n) {
		System.out.println("Quick: "+n);
	}


}
