package examples.parser;

import java.math.BigInteger;
import java.util.Random;

import tom.library.enumerator.Enumeration;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import examples.parser.rec.types.Exp;

public class ExpGenerator extends Generator<Exp> {
    private BigInteger next = BigInteger.ONE;

	Enumeration<Exp> enumeration = Exp.getEnumeration();
	
    public ExpGenerator() {
        super(Exp.class);
    }

    @Override public Exp generate(SourceOfRandomness random, GenerationStatus status) {
        Random rnd = new Random();
        BigInteger r = new BigInteger(next.bitLength(), rnd);
        next = next.add(r);
        //next = next.add(BigInteger.ONE);
        //System.out.println(next);
        return enumeration.get(next);
    }
}