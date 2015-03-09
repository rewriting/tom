package examples.factory.tests;

import java.util.List;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import tom.library.factory.ListFactory;
import examples.factory.Car;
import examples.factory.CarFactory;
import examples.factory.Color;
import examples.factory.ColorFactory;
import examples.factory.Garage;
import examples.factory.GarageFactory;
import examples.factory.Student;
import examples.factory.StudentFactory;
import examples.factory.Tree;
import examples.factory.TreeFactory;

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

		// Lists of Students
		Enumeration<List<Student>> enumListStudent = ListFactory.getEnumeration(StudentFactory.getEnumeration());
		LazyList<Finite<List<Student>>> partsListStudent = enumListStudent.parts();
		for (int i = 0; i < 5 && !partsListStudent.isEmpty(); i++) {
			System.out.println(i + " --> " + partsListStudent.head());
			partsListStudent = partsListStudent.tail();
		}
		

//		// Tree of Integeres
//		Enumeration<Tree<Integer>> enumTree = TreeFactory.getEnumeration(enumInt);
//		LazyList<Finite<Tree<Integer>>> partsTree = enumTree.parts();
//		for (int i = 0; i < 8 && !partsTree.isEmpty(); i++) {
//			System.out.println(i + " --> " + partsTree.head());
//			partsTree = partsTree.tail();
//		}
		 
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


//		// Colors
//		Enumeration<Color> enumColor = ColorFactory.getEnumeration();
//		LazyList<Finite<Color>> partsColor = enumColor.parts();
//		for (int i = 0; i < 8 && !partsColor.isEmpty(); i++) {
//			System.out.println(i + " --> " + partsColor.head());
//			partsColor = partsColor.tail();
//		}
//
//		// Tree of Colors
//		Enumeration<Tree<Color>> enumTreeC = TreeFactory.getEnumeration(enumColor);
//		LazyList<Finite<Tree<Color>>> partsTreeC = enumTreeC.parts();
//		for (int i = 0; i < 8 && !partsTreeC.isEmpty(); i++) {
//			System.out.println(i + " --> " + partsTreeC.head());
//			partsTreeC = partsTreeC.tail();
//		}
//
//		// (Array)List of Integeres
//		Enumeration<List<Integer>> enumArrayList = ListFactory.getEnumeration(enumInt);
//		LazyList<Finite<List<Integer>>> partsArrayList = enumArrayList.parts();
//		for (int i = 0; i < 8 && !partsArrayList.isEmpty(); i++) {
//			System.out.println(i + " --> " + partsArrayList.head());
//			partsArrayList = partsArrayList.tail();
//		}


	}
}
