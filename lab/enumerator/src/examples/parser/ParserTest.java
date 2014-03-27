package examples.parser;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Enumeration;
import tom.library.theory.Enum;
import tom.library.theory.RandomForAll;
import tom.library.theory.TomCheck;
import examples.parser.rec.types.Exp;

@RunWith(TomCheck.class)
public class ParserTest {
	
	@Enum public static Enumeration<Exp> enumeration = Exp.getEnumeration();

	@Theory
	public void testExp(@RandomForAll(sampleSize=100) Exp n) {
		System.out.println("Quick: "+n);
	}




}