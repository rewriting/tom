package examples.factory;

import java.math.BigInteger;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

public class CarFactory {

	public static final Enumeration<Car> getEnumeration() {
		boolean canBeNull = false;
		// if(@PlayWithGenerators(canBeNull)){
		     canBeNull = true;
	    // }
		return getEnumeration(canBeNull);
	}

	public static final Enumeration<Car> getEnumeration(boolean canBeNull) {

		Enumeration<Car> enumRes = null;

		// F< arg1, F< arg2, ... F <argn, Car>...>
		final F<Integer, F<Color, Car>> _car = new F<Integer, F<Color, Car>>() {
			public F<Color, Car> apply(final Integer n) {
				// F< arg2, ... F <argn, Car>...>
				return new F<Color, Car>() {
					public Car apply(final Color c) {
						return new Car(n, c);
					}
				};
			}
		};

		// //@Enumerate(maxSize=4)
		// int no,
		final Enumeration<Integer> noEnum = new Enumeration<Integer>(
				Combinators.makeInteger().parts().take(BigInteger.valueOf(4)));

		// //@Enumerate
		// Color color
		Enumeration<Color> colorEnum = ColorFactory.getEnumeration();

		// as many apply as number of arguments
		enumRes = Enumeration.apply(
				Enumeration.apply(Enumeration.singleton(_car), noEnum),
				colorEnum);

		if (canBeNull) {
			final Enumeration<Car> emptyEnum = Enumeration.singleton(null);
			enumRes = emptyEnum.plus(enumRes);
		}

		return enumRes;
	}

}
