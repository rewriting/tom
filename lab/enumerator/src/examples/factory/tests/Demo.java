package examples.factory.tests;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import examples.factory.Car;
import examples.factory.Student;

public class Demo {
	public static void main(String[] args) {

		// Integers
		Enumeration<Integer> enumInt = Combinators.makeInteger();
		LazyList<Finite<Integer>> parts = enumInt.parts();
		for (int i = 0; i < 8 && !parts.isEmpty(); i++) {
			System.out.println(i + " --> " + parts.head());
			parts = parts.tail();
		}

		// Students
		Enumeration<Student> enumStudent = StudentFactory.getEnumeration();
		LazyList<Finite<Student>> partsStudent = enumStudent.parts();
		for (int i = 0; i < 4 && !partsStudent.isEmpty(); i++) {
			System.out.println(i + " --> " + partsStudent.head());
			partsStudent = partsStudent.tail();
		}
		
//		Enumeration<Car> enumCar = CarFactory.getEnumeration();
//		LazyList<Finite<Car>> partsCar = enumCar.parts();
//		for (int i = 0; i < 4 && !partsCar.isEmpty(); i++) {
//			System.out.println(i + " --> " + partsCar.head());
//			partsCar = partsCar.tail();
//		}
//		
//		Enumeration<Garage> enumGarage = GarageFactory.getEnumeration();
//		LazyList<Finite<Garage>> partsGarage = enumGarage.parts();
//		for (int i = 0; i < 8 && !partsGarage.isEmpty(); i++) {
//			System.out.println(i + " --> " + partsGarage.head());
//			partsGarage = partsGarage.tail();
//		}
//
//		Enumeration<Garage2> enumGarage2 = Garage2Factory.getEnumeration();
//		LazyList<Finite<Garage2>> partsGarage2 = enumGarage2.parts();
//		for (int i = 0; i < 8 && !partsGarage2.isEmpty(); i++) {
//			System.out.println(i + " --> " + partsGarage2.head());
//			partsGarage2 = partsGarage2.tail();
//		}

	}
}
