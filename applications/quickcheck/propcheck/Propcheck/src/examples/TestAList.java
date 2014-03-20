package examples;

import static propcheck.assertion.Operator.*;
import examples.lists.DemoAList;
import examples.lists.alist.types.AList;
import examples.lists.alist.types.Elem;
import propcheck.property.Property;
import propcheck.property.Property2;
import propcheck.property.Property3;
import propcheck.quickcheck.Quickcheck;
import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;

public class TestAList {

	/**
	 * DemoAList.get(DemoAList.addFirst(l:AList,el:Elem),DemoAList.zero()) == el:Elem
	 */
	Property2<AList, Elem> addFirstElement = new Property2<AList, Elem>() {

		@Override
		public void apply(AList l, Elem e) {
			equalsTo(DemoAList.get(DemoAList.addFirst(l, e), DemoAList.zero()), e);
		}
	};

	/**
	 * DemoAList.isEmpty(l:AList) == False
	 */
	Property<AList> falseProp = new Property<AList>() {

		@Override
		public void apply(AList l) {
			equalsTo(DemoAList.isEmpty(l), true);
		}
	};

	/**
	 * if(list != empty) and if(list.contains(el1)) then (getIndexOf(list,el1) + 1) == getIndexOf(addFirst(list,el2),el1)
	 * 
	 * This check take so much time because the generated element hardly a member of the list (the chance is slim) 
	 * therefore the filter is not satisfied and the test is continued with another generated element (random) that also 
	 * has high possibility not a member of the list
	 */
	Property3<AList, Elem, Elem> prop3 = new Property3<AList, Elem, Elem>() {

		@Override
		public void apply(AList l, Elem e1, Elem e2) {
			// 
			implies(!DemoAList.isEmpty(l) && DemoAList.contains(l, e1), (DemoAList.getIndexOf(l, e1) + 1) == DemoAList.getIndexOf(DemoAList.addFirst(l, e2), e1));
		}
	};

	/**
	 * Alternatives of if(list != empty) and if(list.contains(el1)) then (getIndexOf(list,el1) + 1) == getIndexOf(addFirst(list,el2),el1)
	 * To make the test shorter, the first element is taken from the list it self while the second is newly generated element
	 * 
	 * Pruning data can be done in the body of property (?)
	 */
	Property2<AList, Elem> prop4 = new Property2<AList, Elem>() {

		@Override
		public void apply(AList l, Elem e1) {


			Elem first = null;
			if (DemoAList.size(l) > 0) {
				first = DemoAList.get(l, 0);
			}

			implies(!DemoAList.isEmpty(l) && DemoAList.contains(l, first), (DemoAList.getIndexOf(l, first) + 1) == DemoAList.getIndexOf(DemoAList.addFirst(l, e1), first));
		}
	};


	/**
	 * The false version of
	 * if(list != empty) and if(list.contains(el1)) then (getIndexOf(list,el1) + 1) == getIndexOf(addFirst(list,el2),el1)
	 * if(list != empty) and if(list.contains(el1)) then (getIndexOf(list,el1) + 1) != getIndexOf(addFirst(list,el2),el1)
	 * 
	 */
	Property2<AList, Elem> prop4False = new Property2<AList, Elem>() {public void apply(AList l, Elem e1) {	
		Elem first = null;
		if (DemoAList.size(l) > 0) {
			first = DemoAList.get(l, 0);
		}

		implies(!DemoAList.isEmpty(l) && DemoAList.contains(l, first), (DemoAList.getIndexOf(l, first) + 1) != DemoAList.getIndexOf(DemoAList.addFirst(l, e1), first));
	}};


	/**
	 * if(list != empty) and if (index < size) then get(index) != null
	 */
	Property2<AList, Integer> prop5 = new Property2<AList, Integer>() {public void apply(AList l, Integer i) {
		//System.out.println("size = " + DemoAList.size(l) + "\ni = " + i);
		implies(!DemoAList.isEmpty(l) && DemoAList.size(l) > i, DemoAList.get(l, i) != null);
	}};

	/**
	 * main method
	 * @param args
	 */
	public static void main(String[] args) {
		TestAList test = new TestAList();
		test.test();
	}



	void test() {
		Enumeration<AList> enumList = AList.getEnumeration();
		Enumeration<Elem> enumElem = Elem.getEnumeration();
		Enumeration<Integer> enumInt = Combinators.makeLinearInt();
		/*Enumeration<Elem> enumElem2 = Elem.getEnumeration();*/

		/*
		System.out.println("run test addFirstElement");
		Quickcheck.make(1000).forAll(enumList, enumElem, addFirstElement).run();
		System.out.println("\n\nrun test falseProp");
		Quickcheck.make().forAll(enumList, falseProp).run();

		System.out.println("test implications prop3");
		Quickcheck.make().forAll(enumList, enumElem, enumElem2, prop3).run();

		System.out.println("test implications prop4");
		Quickcheck.make().forAll(enumList, enumElem, prop4).run();
		 
		System.out.println("test implications prop4False");
		Quickcheck.make().forAll(enumList, enumElem, prop4False).run();
		*/
		
		System.out.println("test implications prop5");
		Quickcheck.make().forAll(enumList, enumInt, prop5).run();
	}
}
