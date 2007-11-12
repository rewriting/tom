import java.util.*;
import java.math.BigInteger;

public class Permutations {
  private int n;
  private BigInteger numLeft;
  private BigInteger total;
	private ArrayList<ArrayList<Integer>> permutation = new ArrayList<ArrayList<Integer>>();
	
  // An n "super-Permutations" (not unique elements) of a p size set 
	public Permutations(int n) {
    if (n < 1) {
      throw new IllegalArgumentException ("Min 1");
    }
    this.n = n;
    total = getFactorial(n);
    numLeft = total;
    run();
	}

  public ArrayList<ArrayList<Integer>>  getPermutation(){
    return permutation;
  }

	private void run(){
    System.out.println("RUN::  "+n);
    if (n==1){     // one element set
      ArrayList<Integer> cbn=new ArrayList<Integer>();
      cbn.add(1);
      permutation.add(cbn);	
      System.out.println("P 1::  "+permutation);
    }else{        /// recursive call
      Permutations siblings = new Permutations(n-1);	
      ArrayList<ArrayList<Integer>> subPermutations=siblings.permutation;
          
      for (ArrayList<Integer> name : subPermutations) {
        // insert "n"  at each position
        for (int i = 0; i <= name.size(); i++) {
          ArrayList<Integer> cbnAux=new ArrayList<Integer>();
          cbnAux.addAll(name);
          cbnAux.add(i,n);
          permutation.add(cbnAux);
        }
//         System.out.println("P n::  "+permutation);
      }
    }
  }

  public boolean hasMore () {
    return !permutation.isEmpty();
  }


  public ArrayList<Integer> getNextPermutation () {
    return permutation.remove(0);
  }

  public int[] getNext () {
    ArrayList<Integer> current = permutation.remove(0);
    int[] res = new int[current.size()];
    for(int i=0; i<res.length; i++){
      res[i] = current.get(i);
    }
    return res;
  }

  private static BigInteger getFactorial (int n) {
    BigInteger fact = BigInteger.ONE;
    for (int i = n; i > 1; i--) {
      fact = fact.multiply (new BigInteger (Integer.toString (i)));
    }
    return fact;
  }

}


