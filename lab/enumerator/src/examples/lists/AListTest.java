package examples.lists;

import static org.junit.Assert.assertSame;
import static org.junit.Assume.assumeTrue;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.ExhaustiveCheck;
import tom.library.theory.RandomCheck;
import tom.library.theory.TomCheck;
import tom.library.theory.TomForAll;
import examples.lists.alist.types.AList;
import examples.lists.alist.types.Elem;

// project should be linked with tom/applications/quickcheck/propcheck/Propcheck/src
@RunWith(TomCheck.class)
public class AListTest {

	@Enum 
	public static Enumeration<AList> enumerationAList = AList.getEnumeration();
	@Enum 
	public static Enumeration<Elem> enumerationElem = Elem.getEnumeration();

//	@Theory
	public void testAListPrintRandom(@TomForAll @RandomCheck(sampleSize = 10) AList l) {
		System.out.println("Random: "+l);
	}


//	@Theory
	public void testAListPrintExhaustive(@TomForAll @ExhaustiveCheck(maxDepth = 10) AList l) {
		System.out.println("Exhaustive "+l);
	}


	@Theory
	public void testAListIndexOf(@TomForAll @RandomCheck(sampleSize = 10) AList l, @TomForAll @RandomCheck(sampleSize = 10) Elem e1, @TomForAll @RandomCheck(sampleSize = 10) Elem e2) {
		assumeTrue(!DemoAList.isEmpty(l) && DemoAList.contains(l, e1));
		
		assertSame("BAD: e1="+e1+" e2="+e2+" l="+l,  DemoAList.getIndexOf(l, e1) + 1, DemoAList.getIndexOf(DemoAList.addFirst(l, e2), e1));
	}

	
	
}
