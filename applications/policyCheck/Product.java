import java.util.*;

public class Product {
  // the sets used to do the product
  private  ArrayList<ArrayList<Integer>> sets;
  // index of current set
  int ind;
  // the resulted product
  private ArrayList<ArrayList<Integer>> product = new ArrayList<ArrayList<Integer>>();

  public Product(ArrayList<ArrayList<Integer>> sets) {
    ind = 0;
    this.sets = new ArrayList<ArrayList<Integer>>();
    for(ArrayList<Integer> s: sets){
      ArrayList<Integer> al = new ArrayList<Integer>(); 
      int size = s.size();
      for(int i=0; i<size; i++){
        al.add(i);
      }
      this.sets.add(al);
    }
    this.compute();
  }

  public ArrayList<ArrayList<Integer>> getProduct(){
    return product;
  }

  private void compute(){
    if (ind < sets.size()){
      ArrayList<Integer> current = sets.get(ind);
      int nbElems = current.size();
      if (ind == sets.size()-1){ // last set
        for (int i = 0; i < nbElems; i++) {
          ArrayList<Integer> entry=new ArrayList<Integer>();
          entry.add(current.get(i));
          product.add(entry);
        } 
      } else {       
        ArrayList<ArrayList<Integer>> rest = new ArrayList<ArrayList<Integer>>();
        for(int i=ind+1; i<sets.size(); i++){
          rest.add(sets.get(i));
        }
        Product subProduct=new Product(rest);
        //         subProduct.compute();
        ArrayList<ArrayList<Integer>> subProductsList=subProduct.product;

        for (int i = 0; i < nbElems; i++) {
            for (int j = 0; j < subProductsList.size(); j++) {
              ArrayList<Integer> entry=new ArrayList<Integer>();
              entry.add(current.get(i));
              entry.addAll(subProductsList.get(j));
              product.add(entry);
            }
        }
      }
    }
  }
  
  
}
