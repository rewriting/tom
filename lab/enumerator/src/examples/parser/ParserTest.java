package examples.parser;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;

import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.RandomForAll;
import tom.library.theory.TomCheck;
import examples.junit.quickcheck.ExpGenerator;
import examples.parser.rec.types.Exp;

@RunWith(TomCheck.class)
public class ParserTest {
	
	@Enum public static Enumeration<Exp> enumeration = Exp.getEnumeration();

	@Theory
	public void testExp(@ForAll(sampleSize=100) @From({ ExpGenerator.class }) Exp n) {
		System.out.println("Quick: "+n);
	}

	@Theory
	public void testExp2(@RandomForAll(sampleSize=100) Exp n) {
		System.out.println("Quick: "+n);
	}
	


}