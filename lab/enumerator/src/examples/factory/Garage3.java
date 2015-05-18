package examples.factory;

import java.util.ArrayList;

import tom.library.factory.Enumerate;
import tom.library.factory.EnumerateGenerator;

public class Garage3 {
	// a car can appear twice!
	// use Set if otherwise and change the Factory accordingly
	private ArrayList<ArrayList<Car>> cars;
	private ArrayList<Car> cars2;

	public Garage3() {
		cars = new ArrayList<ArrayList<Car>>();
	}

	@Enumerate(canBeNull = false)
	public Garage3(@Enumerate(canBeNull = false) ArrayList<ArrayList<Car>> cars,@Enumerate(canBeNull = false) ArrayList<Car> cars2) {
		this.cars = cars;
		this.cars2=cars2;
	}

	@Override
	public String toString() {
		return "Garage [cars=" + cars + ", cars2=" + cars2 + "]";
	}


}
