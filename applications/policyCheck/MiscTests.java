import java.util.*;

public class MiscTests {
	
	public static void main(String[] args) {

    //		Combinations 
		System.out.println("COMBINATIONS ---------------------");
    int n=3, p=2;
		Combinations toto=new Combinations(n,p);
		System.out.println("Dynamic: "+toto.getCombination());
    System.out.println("Static: "+Combination.getCombination(n,p));

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
    ArrayList<Integer> a = new ArrayList<Integer>();
    a.add(2);
    a.add(2);
    a.add(2);
    System.out.println("AL= "+a);
    ArrayList<ArrayList<Integer>>  pp=Product.getProduct(a);
    System.out.println(pp.size() + " : " + pp);
    a.add(2);
    a.add(2);
    pp=Product.getProduct(a);
    System.out.println(pp.size() + " : " + pp);
    a.add(2);
    pp=Product.getProduct(a);
    System.out.println(pp.size() + " : " + pp);


	}
	
	
	

}
