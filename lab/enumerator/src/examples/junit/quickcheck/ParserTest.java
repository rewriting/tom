package examples.junit.quickcheck;

import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;

import examples.parser.rec.types.Exp;

@RunWith(Theories.class)
public class ParserTest {


//	@Theory
//	public void testInteger(@ForAll(sampleSize = 100) @From({ MyGenerator.class }) int n) {
//		System.out.println("Int: " + n);
//	}

	@Theory
	public void testExp(@ForAll(sampleSize = 10) @From({ ExpGenerator.class }) Exp n) {
		System.out.println("Exp: " + n);
	}
	
}