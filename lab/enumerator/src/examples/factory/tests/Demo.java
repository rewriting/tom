package examples.factory.tests;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import examples.adt.stack.stack.types.Stack;
import examples.factory.*;
import examples.factory.PersonFactory;
import examples.factory.PetFactory;
import examples.factory.handwritten.StudentFactory;
import examples.factory.handwritten.recursive1.*;
import examples.factory.handwritten.recursive2.*;
import examples.factory.handwritten.recursive3.*;
import examples.factory.inheritance.*;

public class Demo {
    
    public static <T, F> void testFactory(Class<T> myClass, Class<F> myFactory) {
        try {
            Enumeration<T> enumT = (Enumeration<T>) myFactory.getMethod("getEnumeration", null).invoke(null, null);
            LazyList<Finite<T>> partsT = enumT.parts();
            for (int i = 0; i < 4 && !partsT.isEmpty(); i++) {
                System.out.println(i + " --> " + partsT.head());
                partsT = partsT.tail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    public static void main(String[] args) {
        // basic
//        testFactory(Student.class, StudentFactory.class);
        // simple recursion
//        testFactory(Friend.class, FriendFactory.class);
        // indirect recursion (circular reference)
//        testFactory(User.class, UserFactory.class);
//        testFactory(Account.class, AccountFactory.class);
//        testFactory(Address.class, AddressFactory.class);
        // collections oneToMany 
//        testFactory(Car.class, CarFactory.class);
//        testFactory(Garage.class, GarageFactory.class);
        // statics
//        testFactory(Color.class, ColorFactory.class);
        // full factory (constructors + methods)
//        testFactory(ListStack.class, ListStackFactory.class);
        // super types
//        testFactory(Person.class, PersonFactory.class);
        testFactory(Pet.class, PetFactory.class);
//        testFactory(Dog.class, DogFactory.class);
        
        
        // Integers
//        Enumeration<Integer> enumInt = Combinators.makeInteger();
//        LazyList<Finite<Integer>> parts = enumInt.parts();
//        for (int i = 0; i < 8 && !parts.isEmpty(); i++) {
//            System.out.println(i + " --> " + parts.head());
//            parts = parts.tail();
//        }
        
//        Enumeration<Character> enumChar = Combinators.makeCharacter();
//        LazyList<Finite<Character>> partsChar = enumChar.parts();
//        for (int i = 0; i < 8 && !partsChar.isEmpty(); i++) {
//            System.out.println(i + " --> " + partsChar.head());
//            partsChar = partsChar.tail();
//        }

//        // Stacks
//        Enumeration<Stack> en = Stack.getEnumeration();
//        LazyList<Finite<Stack>> partsEn = en.parts();
//        for (int i = 0; i < 7 && !partsEn.isEmpty(); i++) {
//            System.out.println(i + " --> " + partsEn.head());
//            partsEn = partsEn.tail();
//        }
        
//        // Tree of Integeres
//        Enumeration<Tree<Integer>> enumTree = TreeFactory.getEnumeration(enumInt);
//        LazyList<Finite<Tree<Integer>>> partsTree = enumTree.parts();
//        for (int i = 0; i < 8 && !partsTree.isEmpty(); i++) {
//            System.out.println(i + " --> " + partsTree.head());
//            partsTree = partsTree.tail();
//        }

//        // Tree of Colors
//        Enumeration<Tree<Color>> enumTreeC = TreeFactory.getEnumeration(enumColor);
//        LazyList<Finite<Tree<Color>>> partsTreeC = enumTreeC.parts();
//        for (int i = 0; i < 8 && !partsTreeC.isEmpty(); i++) {
//            System.out.println(i + " --> " + partsTreeC.head());
//            partsTreeC = partsTreeC.tail();
//        }
//
//        // (Array)List of Integeres
//        Enumeration<List<Integer>> enumArrayList = ListFactory.getEnumeration(enumInt);
//        LazyList<Finite<List<Integer>>> partsArrayList = enumArrayList.parts();
//        for (int i = 0; i < 8 && !partsArrayList.isEmpty(); i++) {
//            System.out.println(i + " --> " + partsArrayList.head());
//            partsArrayList = partsArrayList.tail();
//        }
    }
}
