package propcheck.quickcheck;

import propcheck.property.Property;
import propcheck.property.Property2;
import propcheck.property.Property3;

public interface ShrinkRunnerFactory {
	public <A> ShrinkRunner get(A input, Property<A> property);
	public <A, B> ShrinkRunner get(A inputA, B inputB, Property2<A, B> property);
	public <A, B, C> ShrinkRunner get(A inputA, B inputB, C inputC, Property3<A, B, C> property);
}
