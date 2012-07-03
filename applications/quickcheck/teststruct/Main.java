package teststruct;

import definitions.Field;
import definitions.Scope;
import definitions.Type;

public class Main {

  public static void main(String[] args) {
    Scope scope = new Scope();
    
    Type nat = new Type(scope);
    Type[] tab = {nat};
    nat.addConstructor(tab);
    Type[] vide = new Type[0];
    nat.addConstructor(vide);
    
    Type tree = new Type(scope);
    Type[] tab2 = {tree, tree};
    tree.addConstructor(tab2);
    tree.addConstructor(vide);
    
    Type typetest = new Type(scope);
    Type[] cons_test1 = {tree};
    Type[] cons_test2 = {nat};
    typetest.addConstructor(cons_test1);
    typetest.addConstructor(cons_test2);
    
    Type forest = new Type(scope);
    Type tree2 = new Type(scope);
    Type[] cons_tree1 = {nat};
    Type[] cons_tree2 = {forest};
    Type[] cons_forest1 = {tree2};
    forest.addConstructor(cons_forest1);
    tree2.addConstructor(cons_tree1);
    tree2.addConstructor(cons_tree2);
    
    scope.setDependances();
    
    
    System.out.println("nat : " + nat.getDimention());
    System.out.println("tree : " + tree.getDimention());
    System.out.println("tree2 : " + tree2.getDimention());
    System.out.println("forest : " + forest.getDimention());
    
    System.out.println("nat.isRec() = " + nat.isRec());
    System.out.println("tree.isRec() = " + tree.isRec());
    System.out.println("typetest.isRec() = " + typetest.isRec());
    System.out.println("forest.isRec() = " + forest.isRec());
    System.out.println("tree2.isRec() = " + tree2.isRec());
  }
}
