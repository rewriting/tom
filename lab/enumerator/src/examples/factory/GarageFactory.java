package examples.factory;

import java.util.List;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

public class GarageFactory {

	public static final Enumeration<Garage> getEnumeration() {
		boolean canBeNull = false;
		// if(@MainFactoryGenerator(canBeNull))
		// canBeNull = true;
		return getEnumeration(canBeNull);
	}

	public static final Enumeration<Garage> getEnumeration(boolean canBeNull) {

		Enumeration<Garage> enumRes = null;

		final F<List<Car>, Garage> _garage = new F<List<Car>, Garage>() {
			public Garage apply(final List<Car> l) {
				return new Garage(l);
			}
		};

		// @Enumerate(memberCanBeNull=false)
		// List<Car> cars
		Enumeration<List<Car>> enumListCar = ListFactory
				.getEnumeration(
						// memberCanBeNull=false
						CarFactory.getEnumeration(false));

		enumRes = Enumeration
				.apply(Enumeration.singleton(_garage), enumListCar);

		if (canBeNull) {
			final Enumeration<Garage> emptyEnum = Enumeration.singleton(null);
			enumRes = emptyEnum.plus(enumRes);
		}

		return enumRes;
	}

}
