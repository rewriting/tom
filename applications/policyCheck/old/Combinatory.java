import java.util.*;

public class Combinatory extends Thread{

	int n;
	int p;
	ArrayList<ArrayList<Integer>> combination=new ArrayList<ArrayList<Integer>>();
	ArrayList<Combinatory> siblings=new ArrayList<Combinatory>();
	
  // An n "super-Combinations" (not unique elements) of a p size set 
	Combinatory( int n, int p) {
		super();
		this.n = n;
		this.p = p;
	}


	public void run(){
    //     System.out.println("RUN::  "+n+"-"+p);
		if (p==0){     // one element set
			ArrayList<Integer> cbn=new ArrayList<Integer>();
			for (int j = 0; j < n; j++) {
				cbn.add(p);
			}
			combination.add(cbn);
		} else {     // more than one element set
      if (n==1){     // one element set
        for (int i = 0; i <= p; i++) {
          ArrayList<Integer> cbn=new ArrayList<Integer>();
          cbn.add(i);
          combination.add(cbn);	
        } 
      }else{        
        ArrayList<Integer> cbn=new ArrayList<Integer>();
        for (int j = 0; j < n; j++) {
          cbn.add(p);
        }
        combination.add(cbn);

        // Start recursive call
        for (int j = 0; j < n; j++) {
          Combinatory sibling=new Combinatory(j+1,p-1);	
          siblings.add(sibling);
          sibling.start();
        }

        // Get results from recursive calls  
        for (Combinatory element: siblings) {
          try {
            element.join();
          } catch (InterruptedException e) {
            System.out.println("A join on Thread couldn't be done");
            e.printStackTrace();
          }
          ArrayList<ArrayList<Integer>> subCombinations=element.combination;
          // size of combination
          int size = subCombinations.get(0).size();
				
          for (ArrayList<Integer> name : subCombinations) {
            ArrayList<Integer> cbnAux=new ArrayList<Integer>();
            // fill to get to size "n" from size "size"
            for (int j = 0; j < n-size; j++) {
              cbnAux.add(p);
            }
            cbnAux.addAll(name);
            combination.add(cbnAux);
          }
        }
      }
    }
	}
	
	

	
	
	public static void main(String[] args) {
		Combinatory toto=new Combinatory(3,2);
		toto.start();
		try {
			toto.join();
		} catch (InterruptedException e) {
			System.out.println("A join on Thread couldn't be done");
			e.printStackTrace();
		}
		System.out.println(toto.combination);

	}
	
	
	

}
