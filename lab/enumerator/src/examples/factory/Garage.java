package examples.factory;

import java.util.ArrayList;
import java.util.List;

import examples.factory.generation.Enumerate;
import examples.factory.generation.EnumerateGenerator;

public class Garage {
	// a car can appear twice!
	// use Set if otherwise and change the Factory accordingly
	private List<Car> cars;

	public Garage() {
		cars = new ArrayList<Car>();
	}

	@EnumerateGenerator(canBeNull = false)
	public Garage(@Enumerate(memberCanBeNull = false) List<Car> cars) {
		this.cars = cars;
	}

	@Override
	public String toString() {
		return "Garage [cars=" + cars + "]";
	}

}
