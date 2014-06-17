package examples.factory;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

public class CarFactory {

	public static final Enumeration<Car> getEnumeration() {

		Enumeration<Car> enumRes = null;

		final Enumeration<Car> emptyEnum = Enumeration.singleton(null);

		final F<Integer, F<Color, Car>> _fork = new F<Integer, F<Color, Car>>() {
			public F<Color, Car> apply(final Integer n) {
				return new F<Color, Car>() {
					public Car apply(final Color c) {
						return new Car(n, c);
					}
				};
			}
		};

		// F<Enumeration<Car>, Enumeration<Car>> forkEnum = new
		// F<Enumeration<Car>, Enumeration<Car>>() {
		// public Enumeration<Car> apply(final Enumeration<Car> t) {
		// return emptyEnum.plus(
		// Enumeration.apply(Enumeration.apply(Enumeration.apply(
		// Enumeration.singleton(_fork), enumeration), t), t))
		// .pay();
		// }
		// };

		enumRes = Enumeration.apply(Enumeration.apply(Enumeration.singleton(_fork),
						Combinators.makeInteger()),ColorFactory.getEnumeration());

		return enumRes;
	}

}
