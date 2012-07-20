/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package essais;

import aterm.AFun;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.pure.PureFactory;
import definitions.Buildable;
import definitions.Scope;
import definitions.Sort;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jjtraveler.Visitable;

/**
 *
 * @author hubert
 */
class Essai {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {

    PureFactory factory = new PureFactory();

    AFun fun = factory.makeAFun("truc", 3, false);
    ATerm a = factory.make("a");
    ATerm b = factory.make("b");
    ATerm c = factory.make("c");
    ATermAppl appl = factory.makeAppl(fun, a, b, c);

    System.out.println(appl);
    System.out.println(appl.getChildCount());
    System.out.println(getSize(appl));

    Scope scope = new Scope();

    Sort nat = new Sort(scope, "nat");
    nat.addConstructor("succ", nat);
    nat.addConstructor("zero");

    scope.setDependances();

    int range = 100;
    int nbrTest = 10;
    testSize(nat, range, nbrTest);
  }

  private static int getSize(Visitable term) {
    int n = term.getChildCount();
    int res = 0;
    for (int i = 0; i < n; i++) {
      res += getSize(term.getChildAt(i));
    }
    return res + 1;
  }

  public static void testSize(Buildable type, int range, int nbrTest) {

    File fichier = new File(type.getName() + "_res.txt");
    fichier.delete();
    try {
      fichier.createNewFile();
    } catch (IOException ex) {
      Logger.getLogger(Essai.class.getName()).log(Level.SEVERE, null, ex);
    }

    for (int n = 0; n < range; n++) {
      float moyenne = 0;
      for (int i = 0; i < nbrTest; i++) {
        ATerm term = type.generate(n);
        moyenne += getSize(term);
      }
      moyenne = moyenne / nbrTest;
      String res = n + "\t" + moyenne + "\n";
      try {
        FileOutputStream graveur = new FileOutputStream(fichier, true);
        graveur.write(res.getBytes());
        graveur.close();
      } catch (java.io.IOException err) {
        System.err.println("ecriture fichier impossible");
      }
      System.out.println(n + " : " + moyenne);
    }
  }
}
