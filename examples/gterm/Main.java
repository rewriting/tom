public class Main {

	public static void main(String[] args) {
		Empty e1 = Empty.make();
    System.out.println("e1.isEmpty() = "+e1.isEmpty());

		System.out.println("e1 = " + e1);
		System.out.println("e1.name = " + e1.getName());

		Empty e2 = Empty.make();
		System.out.println("e1==e2: " + (e1==e2));

		Element p1 = Plop.make();
		Element p2 = Plop.make();
		List l1 = Cons.make(p1,e1);
		List l2 = Cons.make(p2,e2);
		System.out.println("l1 = " + l1.toATerm().toString());
		System.out.println("l1==l2: " + (l1==l2));

		Int i1 = Int.make(1);
		Int i2 = Int.make(1);
		System.out.println("i1==i2: " + (i1==i2));


		List l3 = Cons.make(i1,l1);
		List l4 = Cons.make(i2,l2);
		System.out.println("l3 = " + l3.toATerm().toString());
		System.out.println("l3==l4: " + (l3==l4));

		List l5 = List.fromTerm(l4.toATerm());
		System.out.println("l3==l4: " + (l3==l5));
		System.out.println("l5 = " + l5.toString());
	}
}
