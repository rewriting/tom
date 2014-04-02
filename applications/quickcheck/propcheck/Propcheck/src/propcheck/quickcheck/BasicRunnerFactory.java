package propcheck.quickcheck;

import propcheck.property.Property;
import propcheck.property.Property2;
import propcheck.property.Property3;

public class BasicRunnerFactory implements ShrinkRunnerFactory {

	private static BasicRunnerFactory INSTANCE = null;
	
	private BasicRunnerFactory() {}
	
	public static BasicRunnerFactory make() {
		if (INSTANCE == null) {
			INSTANCE = new BasicRunnerFactory();
		}
		return INSTANCE;
	}
	
	@Override
	public <A> ShrinkRunner get(A input, Property<A> property) {
		return new ShrinkRunner1<A>(input, property);
	}

	@Override
	public <A, B> ShrinkRunner get(A inputA, B inputB, Property2<A, B> property) {
		return new ShrinkRunner2<A, B>(inputA, inputB, property);
	}

	@Override
	public <A, B, C> ShrinkRunner get(A inputA, B inputB, C inputC,
			Property3<A, B, C> property) {
		return new ShrinkRunner3<A, B, C>(inputA, inputB, inputC, property);
	}


}
