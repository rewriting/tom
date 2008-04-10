package mathsEnumeration;

import java.util.*;

public class Product {

	private static HashMap<String, ArrayList<ArrayList<Integer>>> products = new HashMap<String, ArrayList<ArrayList<Integer>>>();

	public static ArrayList<ArrayList<Integer>> getProduct(List<Integer> sizeSet) {
		// key
		String key = sizeSet.toString();
		// the resulted product
		ArrayList<ArrayList<Integer>> product = new ArrayList<ArrayList<Integer>>();

		ArrayList<ArrayList<Integer>> exist = products.get(key);
		if (exist != null) {
			product = exist;
		} else {
			// fix-point
			if (sizeSet.isEmpty()) {
				product.add(new ArrayList<Integer>());
			} else {
				int currentSize = sizeSet.remove(0);
				ArrayList<ArrayList<Integer>> subProductsList = Product.getProduct(sizeSet);
				for (int i = 0; i < currentSize; i++) {
					for (int j = 0; j < subProductsList.size(); j++) {
						ArrayList<Integer> entry = new ArrayList<Integer>();
						entry.add(i);
						entry.addAll(subProductsList.get(j));
						product.add(entry);
					}
				}
			}
			products.put(key, product);
		}

		return product;
	}

}
