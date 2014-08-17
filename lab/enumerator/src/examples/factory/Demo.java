package examples.factory;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;

import java.util.ArrayList;
import java.util.List;

public class Demo {
	public static void main(String[] args) {

		// Integeres
		Enumeration<Integer> enumInt = Combinators.makeInteger();
		LazyList<Finite<Integer>> parts = enumInt.parts();
		for (int i = 0; i < 8 && !parts.isEmpty(); i++) {
			System.out.println(i + " --> " + parts.head());
			parts = parts.tail();
		}

		// Tree of Integeres
		Enumeration<Tree<Integer>> enumTree = TreeFactory.getEnumeration(enumInt);
		LazyList<Finite<Tree<Integer>>> partsTree = enumTree.parts();
		for (int i = 0; i < 8 && !partsTree.isEmpty(); i++) {
			System.out.println(i + " --> " + partsTree.head());
			partsTree = partsTree.tail();
		}

		// Colors
		Enumeration<Color> enumColor = ColorFactory.getEnumeration();
		LazyList<Finite<Color>> partsColor = enumColor.parts();
		for (int i = 0; i < 8 && !partsColor.isEmpty(); i++) {
			System.out.println(i + " --> " + partsColor.head());
			partsColor = partsColor.tail();
		}

		// Tree of Colors
		Enumeration<Tree<Color>> enumTreeC = TreeFactory.getEnumeration(enumColor);
		LazyList<Finite<Tree<Color>>> partsTreeC = enumTreeC.parts();
		for (int i = 0; i < 8 && !partsTreeC.isEmpty(); i++) {
			System.out.println(i + " --> " + partsTreeC.head());
			partsTreeC = partsTreeC.tail();
		}

		// Cars
		Enumeration<Car> enumCar = CarFactory.getEnumeration();
		LazyList<Finite<Car>> partsCar = enumCar.parts();
		for (int i = 0; i < 8 && !partsCar.isEmpty(); i++) {
			System.out.println(i + " --> " + partsCar.head());
			partsCar = partsCar.tail();
		}

		// (Array)List of Integeres
		Enumeration<List<Integer>> enumArrayList = ListFactory.getEnumeration(enumInt);
		LazyList<Finite<List<Integer>>> partsArrayList = enumArrayList.parts();
		for (int i = 0; i < 8 && !partsArrayList.isEmpty(); i++) {
			System.out.println(i + " --> " + partsArrayList.head());
			partsArrayList = partsArrayList.tail();
		}

		// Cars
		Enumeration<Garage> enumGarage = GarageFactory.getEnumeration();
		LazyList<Finite<Garage>> partsGarage = enumGarage.parts();
		for (int i = 0; i < 8 && !partsGarage.isEmpty(); i++) {
			System.out.println(i + " --> " + partsGarage.head());
			partsGarage = partsGarage.tail();
		}

		// Student
		Enumeration<Student> enumStudent = StudentFactory.getEnumeration();
		LazyList<Finite<Student>> partsStudent = enumStudent.parts();
		for (int i = 0; i < 8 && !partsStudent.isEmpty(); i++) {
			System.out.println(i + " --> " + partsStudent.head());
			partsStudent = partsStudent.tail();
		}
		


	}
}
