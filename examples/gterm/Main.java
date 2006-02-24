public class Main {

	public static void main(String[] args) {
		Factory factory = Factory.getInstance();

		Empty e1 = factory.makeEmpty();
    System.out.println("e1.isEmpty() = "+e1.isEmpty());

		System.out.println("e1 = " + e1);
		System.out.println("e1.name = " + e1.getName());

		Empty e2 = factory.makeEmpty();
		System.out.println("e1==e2: " + (e1==e2));

		Element p1 = factory.makePlop();
		Element p2 = factory.makePlop();
		List l1 = factory.makeCons(p1,e1);
		List l2 = factory.makeCons(p2,e2);
		System.out.println("l1 = " + l1.toATerm().toString());
		System.out.println("l1==l2: " + (l1==l2));

		Int i1 = factory.makeInt(1);
		Int i2 = factory.makeInt(1);
		System.out.println("i1==i2: " + (i1==i2));


		List l3 = factory.makeCons(i1,l1);
		List l4 = factory.makeCons(i2,l2);
		System.out.println("l3 = " + l3.toATerm().toString());
		System.out.println("l3==l4: " + (l3==l4));
	}
}
