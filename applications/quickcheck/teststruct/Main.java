package teststruct;

import definitions.Field;
import definitions.Type;

public class Main {

  public static void main(String[] args) {
    Type nat = Type.declare();
    Field[] tab = {new Field(nat, true)};
    nat.addConstructor(tab);
    Field[] vide = new Field[0];
    nat.addConstructor(vide);
    
    Type tree = Type.declare();
    Field[] tab2 = {new Field(tree, true), new Field(tree, true)};
    tree.addConstructor(tab2);
    tree.addConstructor(vide);
    
    Type forest = Type.declare();
    Type tree2 = Type.declare();
    Field[] cons_tree1 = {new Field(nat, false)};
    Field[] cons_tree2 = {new Field(forest, true)};
    Field[] cons_forest1 = {new Field(tree2, true)};
    forest.addConstructor(cons_forest1);
    tree2.addConstructor(cons_tree1);
    tree2.addConstructor(cons_tree2);
    
    
    System.out.println("nat : " + nat.getDimention());
    System.out.println("tree : " + tree.getDimention());
    System.out.println("tree2 : " + tree2.getDimention());
    System.out.println("forest : " + forest.getDimention());
  }
}
