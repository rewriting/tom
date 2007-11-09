import java.util.*;

public class MiscTests {
	
	public static void main(String[] args) {

    //		Combinations 
		System.out.println("COMBINATIONS ---------------------");
		Combinations toto=new Combinations(3,2);
		System.out.println(toto.getCombination());

    // PermutationGenerator
		System.out.println("PermutationGenerator ---------------------");
    PermutationGenerator pg = new  PermutationGenerator(2);
    int nbt = Integer.parseInt(pg.getTotal().toString());
		System.out.println(nbt+" permutations");
    for(int i=0; i< nbt; i++){
      int[] perm = pg.getNext();
      for(int j=0; j<perm.length; j++){
        System.out.print(perm[j]+" ");
      }
      System.out.println("");
    }

    //		Permutations 
    // 		System.out.println("PERMUTATIONS ---------------------");
    //     Permutations titi=new Permutations(10);
    // 		System.out.println(titi.getPermutation().size() + " : " + titi.getPermutation());
    //     while(titi.hasMore()){
    //       System.out.println(titi.getNextPermutation());
    //     }

    //		DIRECTPRODUCT
    //     System.out.println("DIRECTPRODUCT ---------------------");
    //     LinkedList<Integer> to=new LinkedList<Integer>();
    //     to.add(2);
    //     to.add(2);
    //     to.add(2);
    //     System.out.println(to);
    //     DirectProduct titi=new DirectProduct(to);
    //     titi.compute();
    //     System.out.println(titi.ProductsList);
    //     System.out.println(titi.ProductsList.size());
    

    //		DIRECTPRODUCT
    System.out.println("DIRECTPRODUCT ---------------------");
    ArrayList<Integer> a1 = new ArrayList<Integer>();
    a1.add(0);
    a1.add(1);
    ArrayList<Integer> a2 = new ArrayList<Integer>();
    a2.add(4);
    a2.add(5);
    ArrayList<Integer> a3 = new ArrayList<Integer>();
    a3.add(2);
    a3.add(3);
    ArrayList<ArrayList<Integer>> al = new ArrayList<ArrayList<Integer>>();
    al.add(a1);
    al.add(a2);
    al.add(a3);
    System.out.println("AL= "+al);
    Product pp=new Product(al);
    //     pp.compute();
    System.out.println(pp.getProduct().size() + " : " + pp.getProduct());


	}
	
	
	

}
