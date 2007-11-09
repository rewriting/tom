import java.util.*;

public class MiscTests {
	
	public static void main(String[] args) {

    //		Combinations 
		Combinations toto=new Combinations(3,2);
    // 		toto.run();
		System.out.println(toto.getCombination());

    // PermutationGenerator
    PermutationGenerator pg = new  PermutationGenerator(4);
    int nbt = Integer.parseInt(pg.getTotal().toString());
		System.out.println(nbt+" permutations");
    for(int i=0; i< nbt; i++){
      int[] perm = pg.getNext();
      for(int j=0; j<perm.length; j++){
        System.out.print(perm[j]+" ");
      }
      System.out.println("");
    }

	}
	
	
	

}
