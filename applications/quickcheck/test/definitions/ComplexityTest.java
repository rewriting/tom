/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import aterm.ATerm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jjtraveler.Visitable;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author hubert
 */
public class ComplexityTest {

  public ComplexityTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  private int getSize(Visitable term) {
    int n = term.getChildCount();
    int res = 0;
    for (int i = 0; i < n; i++) {
      res += getSize(term.getChildAt(i));
    }
    return res + 1;
  }

  private void testSize(Buildable type, int range, int nbrTest) {

    File fichier = new File(type.getName() + "_res.txt");
    fichier.delete();
    try {
      fichier.createNewFile();
    } catch (IOException ex) {
      Logger.getLogger(ComplexityTest.class.getName()).log(Level.SEVERE, null, ex);
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

//  @Test
//  public void testComplexityNat() {
//    Scope scope = new Scope();
//
//    Sort nat = new Sort(scope, "nat");
//    nat.addConstructor("succ", nat);
//    nat.addConstructor("zero");
//
//    scope.setDependances();
//
//    int range = 200;
//    int nbrTest = 50;
//    // 5 minutes 48 secondes
//    testSize(nat, range, nbrTest);
//    assert true;
//  }
  
//  @Test
//  public void testComplexityExpr() {
//    Scope scope = new Scope();
//
//    Sort expr = new Sort(scope, "expr");
//    expr.addConstructor("zero");
//    expr.addConstructor("un");
//    expr.addConstructor("plus", expr, expr);
//    expr.addConstructor("mult", expr, expr);
//
//    scope.setDependances();
//
//    int range = 200;
//    int nbrTest = 50;
//    // 5 minutes 48 secondes
//    testSize(expr, range, nbrTest);
//    assert true;
//  }
}
