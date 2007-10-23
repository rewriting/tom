import java.util.ArrayList;
import java.util.LinkedList;


public class DirectProduct {
LinkedList<Integer> setsSize;
ArrayList<ArrayList<Integer>> ProductsList=new ArrayList<ArrayList<Integer>>();

DirectProduct(LinkedList<Integer> setsSize) {
	this.setsSize = (LinkedList<Integer>) setsSize.clone();
}

void compute(){
if (setsSize.isEmpty())return;
if (setsSize.size()==1){
	int size1=setsSize.remove();
	for (int i = 0; i < size1; i++) {
		ArrayList<Integer> entry=new ArrayList<Integer>();
		entry.add(i);
		ProductsList.add(entry);
	} 
return;
}
/*if (setsSize.size()==2){
	int size1=setsSize.remove();
	int size2=setsSize.remove();
	for (int i = 0; i < size1; i++) {
		for (int j = 0; j < size2; j++) {
			ArrayList<Integer> entry=new ArrayList<Integer>();
			entry.add(i);
			entry.add(j);
			ProductsList.add(entry);
		}
	}
return;
}
*/
if (setsSize.size()>1){
	int size1=setsSize.remove();
	DirectProduct subDirectProduct=new DirectProduct(setsSize);
	subDirectProduct.compute();
	ArrayList<ArrayList<Integer>> subProductsList=subDirectProduct.ProductsList;
	for (int i = 0; i < size1; i++) {
		for (int j = 0; j < subProductsList.size(); j++) {
			ArrayList<Integer> entry=new ArrayList<Integer>();
			entry.add(i);
			entry.addAll(subProductsList.get(j));
			ProductsList.add(entry);
		}
	}
return;
}


}
public static void main(String[] args) {
	LinkedList<Integer> toto=new LinkedList<Integer>();
	toto.add(2);
	toto.add(2);
	toto.add(2);
	System.out.println(toto);
	DirectProduct titi=new DirectProduct(toto);
	titi.compute();
	System.out.println(titi.ProductsList);
	System.out.println(titi.ProductsList.size());
}


}
