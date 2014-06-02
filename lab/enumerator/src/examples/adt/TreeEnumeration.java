package examples.adt;

import java.math.BigInteger;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import examples.adt.tree.types.*;

public class TreeEnumeration {
	
	public static void main(String[] args) {
		Enumeration<Tree> treeEnum = Tree.getEnumeration();
		for(int i=0 ; i<10 ; i++) {
			System.out.println(i + " --> " + treeEnum.get(BigInteger.valueOf(i)));
		}
		int n = 100;
		BigInteger i = java.math.BigInteger.TEN.pow(n);
		System.out.println("10^" + n + "-th tree has size " + treeEnum.get(i));
	}
} 
