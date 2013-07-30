package examples.trees;

import examples.trees.binarytree.types.*;

public class DemoBinaryTree {

  %include{binarytree/BinaryTree.tom}

  // ---------------- noLeafNodes -----------------------------------------------------------------
	// Reminder: a leaf node has no children
	public static boolean noLeafNodes(Node tree){
		%match (tree){
			nil() -> { return true; }
			add(_,node1,node2) && (node1 == nil() && node2 == nil()) -> { return false; }
			add(_,node1,node2) -> { return ( noLeafNodes(`node1) && noLeafNodes(`node2) ); }
		}
		return true;
	}

  // ---------------- prettyPrinter ---------------------------------------------------------------
	public static String prettyNode(Node tree){
		%match (tree){
			nil() -> {  return "nil"; }
      add(cst,node1,node2) -> {
				return prettyValue(`cst)+":"+prettyNode(`node1)+":"+prettyNode(`node2); }
			}
		return "";
	}

	public static String prettyValue(Value cst){  
		%match (cst){
			a() -> {  return "a"; }
			b() -> {  return "b"; }
			nat(i) -> {  return Integer.toString(`i); } 
		}
		return "";
	}

  // ---------------- main ------------------------------------------------------------------------
  public static void main(String args[]) {    
		
		Node tree1 = `add(a(), add(nat(5), nil(), nil()), add(b(), nil(), nil()));
		Node tree2 = `add(a(), nil(), nil());
		Node tree3 = `nil();

		System.out.print("\n");

		System.out.println("------------------- Trees ----------------------------------------------");
		System.out.println("tree1 : " + tree1);
		System.out.println("tree2 : " + tree2);
		System.out.println("tree3 : " + tree3);

		System.out.println("------------------- Pretty Printer -------------------------------------");
		System.out.println("prettyNode(tree1) : " + prettyNode(tree1));
		System.out.println("prettyNode(tree2) : " + prettyNode(tree2));
		System.out.println("prettyNode(tree3) : " + prettyNode(tree3));

		System.out.println("------------------- No Leaf Nodes --------------------------------------");
		System.out.println( "noLeafNodes(tree1) = " + noLeafNodes(tree1) );
		System.out.println( "noLeafNodes(tree2) = " + noLeafNodes(tree2) );
		System.out.println( "noLeafNodes(tree3) = " + noLeafNodes(tree3) );

		System.out.print("\n");
  }

}
