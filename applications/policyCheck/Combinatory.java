import java.util.ArrayList;
import java.util.Iterator;


public class Combinatory extends Thread{

	int n;
	int p;
	ArrayList<ArrayList<Integer>> combination=new ArrayList<ArrayList<Integer>>();
	ArrayList<Combinatory> siblings=new ArrayList<Combinatory>();
	
	Combinatory( int n, int p) {
		super();
		this.n = n;
		this.p = p;
	}


	public void run(){
		if (p==0){
			ArrayList<Integer> cbn=new ArrayList<Integer>();
			for (int j = 0; j < n; j++) {
				cbn.add(p);
			}
			combination.add(cbn);
			return;
		}
		if (n==1){
			for (int i = 0; i <= p; i++) {
				ArrayList<Integer> cbn=new ArrayList<Integer>();
				cbn.add(i);
				combination.add(cbn);	
			} 
			return;
		}else{
			
			ArrayList<Integer> cbn=new ArrayList<Integer>();
			for (int j = 0; j < n; j++) {
				cbn.add(p);
			}
			combination.add(cbn);
			for (int j = 0; j < n; j++) {
			Combinatory sibling=new Combinatory(j+1,p-1);	
			siblings.add(sibling);
			sibling.start();
			}
			for (int i = 0; i < siblings.size(); i++) {
				Combinatory element = (Combinatory) siblings.get(i);
				try {
					element.join();
				} catch (InterruptedException e) {
					System.out.println("A join on Thread couldn't be done");
					e.printStackTrace();
				}
				ArrayList<ArrayList<Integer>> subCombinations=element.combination;
				
				for (Iterator iterator = subCombinations.iterator(); iterator.hasNext();) {
					ArrayList<Integer> cbnAux=new ArrayList<Integer>();
					for (int j = 0; j < n-(i+1); j++) {
						cbnAux.add(p);
					}
					ArrayList<Integer> name = (ArrayList<Integer>) iterator.next();
					cbnAux.addAll(name);
					combination.add(cbnAux);
				}
			}
		}
		return;
	}
	
	

	
	
	public static void main(String[] args) {
		Combinatory toto=new Combinatory(2,1);
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
