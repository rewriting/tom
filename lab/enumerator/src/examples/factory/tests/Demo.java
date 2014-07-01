package examples.factory.tests;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import examples.factory.Student;

public class Demo {
	public static void main(String[] args) {

		// Integeres
		Enumeration<Integer> enumInt = Combinators.makeInteger();
		LazyList<Finite<Integer>> parts = enumInt.parts();
		for (int i = 0; i < 8 && !parts.isEmpty(); i++) {
			System.out.println(i + " --> " + parts.head());
			parts = parts.tail();
		}

		// Students
//		Enumeration<Student> enumStudent = StudentFactory.getEnumeration();
//		LazyList<Finite<Student>> partsStudent = enumStudent.parts();
//		for (int i = 0; i < 8 && !partsStudent.isEmpty(); i++) {
//			System.out.println(i + " --> " + partsStudent.head());
//			partsStudent = partsStudent.tail();
//		}


	}
}
