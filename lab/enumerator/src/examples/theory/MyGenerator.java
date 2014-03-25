package examples.theory;

import java.math.BigInteger;
import java.util.HashMap;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class MyGenerator extends Generator<Integer> {
    private int next = 0;

	Enumeration<Integer> enumInt =  Combinators.makeInteger();
	
    public MyGenerator() {
        super(int.class);
    }

    @Override public Integer generate(SourceOfRandomness random, GenerationStatus status) {
        return enumInt.get(BigInteger.valueOf(next++));
    }
}