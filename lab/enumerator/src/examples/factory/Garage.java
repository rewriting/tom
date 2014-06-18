package examples.factory;

import java.util.ArrayList;
import java.util.List;

public class Garage {
	// a car can appear twice!
	// use Set if otherwise and change the Factory accordingly
	private List<Car> cars;
	
	public Garage(){
		cars = new ArrayList<Car>();
	}

	public Garage(List<Car> cars){
		this.cars = cars;
	}

	@Override
	public String toString() {
		return "Garage [cars=" + cars + "]";
	}
	
	
}
