package examples.parser;

import static org.junit.Assert.assertEquals;

import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.theory.ExhaustiveForAll;
import tom.library.theory.RandomForAll;
import tom.library.theory.TomCheck;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;

import examples.parser.rec.types.Exp;

@RunWith(TomCheck.class)
public class ParserTest {

	@Theory
	public void testExp(@ForAll(sampleSize=100) @From({ExpGenerator.class}) Exp n) {
		System.out.println("Quick: "+n);
	}




}