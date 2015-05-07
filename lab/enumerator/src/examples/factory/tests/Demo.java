package examples.factory.tests;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import tom.library.factory.ListFactory;
import examples.adt.stack.stack.types.Stack;
import examples.factory.*;
import examples.factory.handwritten.recursive1.*;
import examples.factory.handwritten.recursive2.*;
import examples.factory.handwritten.recursive3.*;

public class Demo {
	public static void main(String[] args) {

		// Integers
//		Enumeration<Integer> enumInt = Combinators.makeInteger();
//		LazyList<Finite<Integer>> parts = enumInt.parts();
//		for (int i = 0; i < 8 && !parts.isEmpty(); i++) {
//			System.out.println(i + " --> " + parts.head());
//			parts = parts.tail();
//		}
		
//		Enumeration<Character> enumChar = Combinators.makeCharacter();
//        LazyList<Finite<Character>> partsChar = enumChar.parts();
//        for (int i = 0; i < 8 && !partsChar.isEmpty(); i++) {
//            System.out.println(i + " --> " + partsChar.head());
//            partsChar = partsChar.tail();
//        }
	    // User
	    Enumeration<User> enumUser = UserFactory.getEnumeration();
	    LazyList<Finite<User>> partsUser = enumUser.parts();
	    for (int i = 0; i < 7 && !partsUser.isEmpty(); i++) {
	        System.out.println(i + " --> " + partsUser.head());
	        partsUser = partsUser.tail();
	    }
	    // Account
	    Enumeration<Account> enumAccount = AccountFactory.getEnumeration();
        LazyList<Finite<Account>> partsAccount = enumAccount.parts();
        for (int i = 0; i < 7 && !partsAccount.isEmpty(); i++) {
            System.out.println(i + " --> " + partsAccount.head());
            partsAccount = partsAccount.tail();
        }
        // Address
        Enumeration<Address> enumAddress = AddressFactory.getEnumeration();
        LazyList<Finite<Address>> partsAddress = enumAddress.parts();
        for (int i = 0; i < 7 && !partsAddress.isEmpty(); i++) {
            System.out.println(i + " --> " + partsAddress.head());
            partsAddress = partsAddress.tail();
        }
	    
//
//		// Students
//		Enumeration<Student> enumStudent = StudentFactory.getEnumeration();
//		LazyList<Finite<Student>> partsStudent = enumStudent.parts();
//		for (int i = 0; i < 4 && !partsStudent.isEmpty(); i++) {
//			System.out.println(i + " --> " + partsStudent.head());
//			partsStudent = partsStudent.tail();
//		}

//		// Rooms
//		Enumeration<Room> enumRoom = RoomFactory.getEnumeration();
//		LazyList<Finite<Room>> partsRoom = enumRoom.parts();
//		for (int i = 0; i < 4 && !partsRoom.isEmpty(); i++) {
//			System.out.println(i + " --> " + partsRoom.head());
//			partsRoom = partsRoom.tail();
//		}
		
//		// StudentWithCar
//		Enumeration<StudentWithCar> enumStudentWC = examples.factory.handwritten.recursive2.StudentWithCarFactory.getEnumeration();
//        LazyList<Finite<StudentWithCar>> partsStudentWC = enumStudentWC.parts();
//        for (int i = 0; i < 8 && !partsStudentWC.isEmpty(); i++) {
//            System.out.println(i + " --> " + partsStudentWC.head());
//            partsStudentWC = partsStudentWC.tail();
//        }
        
      // StudentCar
//      Enumeration<StudentCar> enumStudentCar = examples.factory.handwritten.recursive2.StudentCarFactory.getEnumeration();
//        LazyList<Finite<StudentCar>> partsStudentCar = enumStudentCar.parts();
//        for (int i = 0; i < 8 && !partsStudentCar.isEmpty(); i++) {
//            System.out.println(i + " --> " + partsStudentCar.head());
//            partsStudentCar = partsStudentCar.tail();
//        }
		
      // StudentFriend
//      Enumeration<StudentFriend> enumStudentFriend = StudentFriendFactory.getEnumeration();
//        LazyList<Finite<StudentFriend>> partsStudentFriend = enumStudentFriend.parts();
//        for (int i = 0; i < 10 && !partsStudentFriend.isEmpty(); i++) {
//            System.out.println(i + " --> " + partsStudentFriend.head());
//            partsStudentFriend = partsStudentFriend.tail();
//        }
		
//		// Lists of Students
//		Enumeration<List<Student>> enumListStudent = ListFactory.getEnumeration(StudentFactory.getEnumeration());
//		LazyList<Finite<List<Student>>> partsListStudent = enumListStudent.parts();
//		for (int i = 0; i < 7 && !partsListStudent.isEmpty(); i++) {
//			System.out.println(i + " --> " + partsListStudent.head());
//			partsListStudent = partsListStudent.tail();
//		}
		
//		System.out.println(Combinators.makeInteger().get(BigInteger.valueOf(0)));
		// Stacks
//		Enumeration<ListStack> enumListStack = ListStackFactory.getEnumeration();
//		LazyList<Finite<ListStack>> partsListStack = enumListStack.parts();
//		for (int i = 0; i < 7 && !partsListStack.isEmpty(); i++) {
//			System.out.println(i + " --> " + partsListStack.head());
//			partsListStack = partsListStack.tail();
//		}
//		
//
//		// Stacks
//		Enumeration<Stack> en = Stack.getEnumeration();
//		LazyList<Finite<Stack>> partsEn = en.parts();
//		for (int i = 0; i < 7 && !partsEn.isEmpty(); i++) {
//			System.out.println(i + " --> " + partsEn.head());
//			partsEn = partsEn.tail();
//		}
		


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
//		for (int i = 0; i < 6 && !partsGarage.isEmpty(); i++) {
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
