package examples.factory;

import java.util.List;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

public class GarageFactory {

	public static final Enumeration<Garage> getEnumeration() {

		Enumeration<Garage> enumRes = null;

		final Enumeration<Garage> emptyEnum = Enumeration.singleton(null);

		final F<List<Car>, Garage> _garage = new F<List<Car>, Garage>() {
			public Garage apply(final List<Car> l) {
				return new Garage(l);
			}
		};

		enumRes = emptyEnum.plus(Enumeration.apply(
				Enumeration.singleton(_garage),
				ListFactory.getEnumeration(CarFactory.getEnumeration())));
		return enumRes;
	}

}
