import java.util.*;

public class Product {

  public static ArrayList<ArrayList<Integer>>  directProduct(ArrayList<Integer> sizeSet) {
    // the resulted product
    ArrayList<ArrayList<Integer>> product = new ArrayList<ArrayList<Integer>>();

    // fix-point
    if (sizeSet.isEmpty()){
          product.add(new ArrayList<Integer>());
    } else {
      int currentSize = sizeSet.remove(0);
      ArrayList<ArrayList<Integer>> subProductsList = Product.directProduct(sizeSet);
      for (int i = 0; i < currentSize; i++) {
        for (int j = 0; j < subProductsList.size(); j++) {
          ArrayList<Integer> entry=new ArrayList<Integer>();
          entry.add(i);
          entry.addAll(subProductsList.get(j));
          product.add(entry);
        }
      }
    }

    return product;
  }

}
