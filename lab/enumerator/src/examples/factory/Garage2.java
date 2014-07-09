package examples.factory;

import java.util.ArrayList;
import java.util.List;

import examples.factory.generation.Enumerate;
import examples.factory.generation.EnumerateGenerator;

public class Garage2 {
	// a car can appear twice!
	// use Set if otherwise and change the Factory accordingly
	private List<List<Car>> cars;
	private List<Car> cars2;

	public Garage2() {
		cars = new ArrayList<List<Car>>();
	}

	@EnumerateGenerator(canBeNull = false)
	public Garage2(@Enumerate(memberCanBeNull = false) List<List<Car>> cars,@Enumerate(memberCanBeNull = false) List<Car> cars2) {
		this.cars = cars;
		this.cars2=cars2;
	}

	@Override
	public String toString() {
		return "Garage [cars=" + cars + ", cars2=" + cars2 + "]";
	}


}
