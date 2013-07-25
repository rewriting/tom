package examples.trees;

import examples.trees.binarytree.types.*;


public class DemoBinaryTree {

  %include{binarytree/BinaryTree.tom}

	public static boolean noLeafNodes(Node tree){
		%match (tree){
			add(_, nothing(), nothing()) -> { return false; }
		}
		return true;
	}

  public static void main(String args[]) {    
		
		Node tree = `add(c1(), add(nat(5), nothing(), nothing()), add(c2(), nothing(), nothing()));

		System.out.println("BinaryTree : " + tree);

		System.out.println( "noLeafNodes(tree) = " + noLeafNodes(tree) );

  }

}
