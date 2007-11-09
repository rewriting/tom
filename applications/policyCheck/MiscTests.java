import java.util.*;

public class MiscTests {
	
	public static void main(String[] args) {

    //		Combinations 
		System.out.println("COMBINATIONS ---------------------");
		Combinations toto=new Combinations(3,2);
		System.out.println(toto.getCombination());

    // PermutationGenerator
		System.out.println("PermutationGenerator ---------------------");
    PermutationGenerator pg = new  PermutationGenerator(10);
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
		System.out.println("PERMUTATIONS ---------------------");
    Permutations titi=new Permutations(10);
		System.out.println(titi.getPermutation().size() + " : " + titi.getPermutation());
    while(titi.hasMore()){
      System.out.println(titi.getNextPermutation());
    }

	}
	
	
	

}
