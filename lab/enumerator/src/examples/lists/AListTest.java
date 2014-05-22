package examples.lists;

import static org.junit.Assert.assertSame;
import static org.junit.Assume.assumeTrue;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.TomCheck;
import tom.library.theory.ForSome;
import examples.lists.alist.types.AList;
import examples.lists.alist.types.Elem;

// project should be linked with tom/applications/quickcheck/propcheck/Propcheck/src
@RunWith(TomCheck.class)
public class AListTest {
/*
	@Enum 
	public static Enumeration<AList> enumerationAList = AList.getEnumeration();
	@Enum 
	public static Enumeration<Elem> enumerationElem = Elem.getEnumeration();
*/
	@Theory
	public void testAListPrintRandom(@ForSome(maxSampleSize = 20, numberOfSamples=21) AList l) {
		assumeTrue(!DemoAList.isEmpty(l));

		System.out.println("Random: "+l);
	}


	//@Theory
	public void testAListPrintExhaustive(@ForSome(exhaustive=true, maxSampleSize = 20, numberOfSamples=50) AList l) {
		System.out.println("Exhaustive "+l);
	}


	@Theory
	public void testAListIndexOf(@ForSome(minSampleSize = 10, maxSampleSize = 20) AList l, @ForSome(maxSampleSize = 10) Elem e1, @ForSome(maxSampleSize = 10) Elem e2) {
		assumeTrue(!DemoAList.isEmpty(l) && DemoAList.contains(l, e1));
		
		assertSame("BAD: e1="+e1+" e2="+e2+" l="+l,  DemoAList.getIndexOf(l, e1) + 1, DemoAList.getIndexOf(DemoAList.addFirst(l, e2), e1));
	}

	
	
}
