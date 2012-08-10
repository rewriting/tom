/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import definitions.Scope;
import definitions.Sort;
import definitions.SortGom;
import definitions.StrategyParameters;
import examples.sort.types.Expr;
import examples.sort.types.IntList;
import examples.sort.types.Leaf;
import examples.sort.types.Surexpr;
import examples.sort.types.expr.mult;
import examples.sort.types.expr.plus;
import examples.sort.types.expr.un;
import examples.sort.types.expr.zero;
import examples.sort.types.intlist.consList;
import examples.sort.types.intlist.nill;
import examples.sort.types.surexpr.rec;
import examples.sort.types.surexpr.wrapper;

/**
 *
 * @author hubert
 */
public class Examples {

  public static Scope scope;
  public static Sort nat;
  public static Sort tree;
  public static Sort tree2;
  public static Sort forest;
  public static Sort circ;
  public static Sort typetest;
  public static Sort a, b, c;
  public static SortGom expr;
  public static SortGom surexpr;
  public static Sort list;
  public static Sort list2;
  public static Sort mix;
  public static Sort leafABC;
  public static SortGom listABC;
  public static StrategyParameters paramDepth, paramNodes;
  public static Sort sortTestShrink2;

  public static void init() {
    paramDepth = new StrategyParameters(
            StrategyParameters.DistStrategy.DEPTH,
            StrategyParameters.TerminationCriterion.POINT_OF_NO_RETURN);

    paramNodes = new StrategyParameters(
            StrategyParameters.DistStrategy.NODES,
            StrategyParameters.TerminationCriterion.POINT_OF_NO_RETURN);

    scope = new Scope();

    nat = new Sort(scope, "nat");
    nat.addConstructor("succ", nat);
    nat.addConstructor("zero");

    tree = new Sort(scope, "tree");
    tree.addConstructor("branch", tree, tree);
    tree.addConstructor("leaf");

    typetest = new Sort(scope, "typetest");
    typetest.addConstructor("tree", tree);
    typetest.addConstructor("nat", nat);


    forest = new Sort(scope, "forest");
    tree2 = new Sort(scope, "tree2");
    forest.addConstructor("tree", tree2);
    tree2.addConstructor("nat", nat);
    tree2.addConstructor("forest", forest);

    circ = new Sort(scope, "circ");
    circ.addConstructor("circ", circ);

    a = new Sort(scope, "a");
    b = new Sort(scope, "b");
    c = new Sort(scope, "c");
    a.addConstructor("nill");
    a.addConstructor("b", b);
    b.addConstructor("c", c);
    c.addConstructor("a", a);

    expr = new SortGom(scope, Expr.class);
    expr.addConstructor(mult.class);
    expr.addConstructor(plus.class);
    expr.addConstructor(un.class);
    expr.addConstructor(zero.class);

    surexpr = new SortGom(scope, Surexpr.class);
    surexpr.addConstructor(rec.class);
    surexpr.addConstructor(wrapper.class);

    list = new Sort(scope, "list");
    list.addConstructor(expr);
    list.addConstructor("consList", expr, list);

    list2 = new Sort(scope, "list2");
    list2.addConstructor("consList", expr, list2);

    mix = new Sort(scope, "mix");
    mix.addConstructor("consMix", list, surexpr);


    leafABC = new SortGom(scope, Leaf.class);
    listABC = new SortGom(scope, IntList.class);
    listABC.addConstructor(nill.class);
    listABC.addConstructor(consList.class);
    leafABC.addConstructor("a");
    leafABC.addConstructor("b");
    leafABC.addConstructor("c");

    sortTestShrink2 = new Sort(scope, "u");
    sortTestShrink2.addConstructor("gros", a, a, b, c, c, c);
    sortTestShrink2.addConstructor("moyen", a, a, c);
    sortTestShrink2.addConstructor("petit1", a, b);
    sortTestShrink2.addConstructor("petit2", b, c);

    scope.setDependances();
  }
}
