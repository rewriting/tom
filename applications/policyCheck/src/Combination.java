import java.util.*;

public class Combination {

  private static HashMap<String,ArrayList<ArrayList<Integer>>> combinations =  new HashMap<String,ArrayList<ArrayList<Integer>>>();

  /*
   * builds n-element sets with elements in {0,...,p}
   * ex C_2,2 = [[2, 2], [2, 1], [2, 0], [1, 1], [1, 0], [0, 0]]
   */
  public static ArrayList<ArrayList<Integer>>  getCombination(int n, int p){
    // key    
    String key = n+"-"+p;;
    // the resulted product
    ArrayList<ArrayList<Integer>> combination = new ArrayList<ArrayList<Integer>>();
    
    ArrayList<ArrayList<Integer>> exist = combinations.get(key);
    if(exist  != null){
      combination = exist;
    } else {
      if (p==0){     // one element set
        ArrayList<Integer> cbn=new ArrayList<Integer>();
        for (int j = 0; j < n; j++) {
          cbn.add(p);
        }
        combination.add(cbn);
      } else {     // more than one element set
        ArrayList<Integer> cbn=new ArrayList<Integer>();
        for (int j = 0; j < n; j++) {
          cbn.add(p);
        }
        combination.add(cbn);
        
        if (n!=0){
          for (int j = 0; j < n; j++) {
            ArrayList<ArrayList<Integer>> subCombinations=Combination.getCombination(j+1,p-1);	
            // size of combination
            int size = subCombinations.get(0).size();
            
            for (ArrayList<Integer> name : subCombinations) {
              ArrayList<Integer> cbnAux=new ArrayList<Integer>();
              // fill to get to size "n" from size "size"
              for (int i = 0; i < n-size; i++) {
                cbnAux.add(p);
              }
              cbnAux.addAll(name);
              combination.add(cbnAux);
            }
          }
        }
      }
      combinations.put(key,combination);
    }
    return combination;
	}
	
}
