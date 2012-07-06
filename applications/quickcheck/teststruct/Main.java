package teststruct;

import definitions.Algebraic;
import definitions.Scope;

public class Main {

  public static void main(String[] args) {
    Scope scope = new Scope();

    Algebraic nat = new Algebraic(scope);
    Algebraic[] tab = {nat};
    nat.addConstructor(tab);
    Algebraic[] vide = new Algebraic[0];
    nat.addConstructor(vide);

    Algebraic tree = new Algebraic(scope);
    Algebraic[] tab2 = {tree, tree};
    tree.addConstructor(tab2);
    tree.addConstructor(vide);

    Algebraic typetest = new Algebraic(scope);
    Algebraic[] cons_test1 = {tree};
    Algebraic[] cons_test2 = {nat};
    typetest.addConstructor(cons_test1);
    typetest.addConstructor(cons_test2);

    Algebraic forest = new Algebraic(scope);
    Algebraic tree2 = new Algebraic(scope);
    Algebraic[] cons_tree1 = {nat};
    Algebraic[] cons_tree2 = {forest};
    Algebraic[] cons_forest1 = {tree2};
    forest.addConstructor(cons_forest1);
    tree2.addConstructor(cons_tree1);
    tree2.addConstructor(cons_tree2);
    
    Algebraic circ = new Algebraic(scope);
    Algebraic[] cons_circ = {circ};
    circ.addConstructor(cons_circ);

    scope.setDependances();


    System.out.println("nat : " + nat.getDimention());
    System.out.println("tree : " + tree.getDimention());
    System.out.println("typetest : " + typetest.getDimention());
    System.out.println("tree2 : " + tree2.getDimention());
    System.out.println("forest : " + forest.getDimention());
    System.out.println("circ : " + circ.getDimention());

    System.out.println("nat.isRec() = " + nat.isRec());
    System.out.println("tree.isRec() = " + tree.isRec());
    System.out.println("typetest.isRec() = " + typetest.isRec());
    System.out.println("forest.isRec() = " + forest.isRec());
    System.out.println("tree2.isRec() = " + tree2.isRec());
    System.out.println("circ.isRec() = " + circ.isRec());
  }
}
