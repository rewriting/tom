/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import material.ExamplesFormula;
import gen.Interpretation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import system.types.Formula;

/**
 *
 * @author hubert
 */
public class LogicTest {

  private Interpretation interp;
  private Formula formula = ExamplesFormula.f1;

  public LogicTest() {
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
  // TODO add test methods here.
  // The methods must be annotated with annotation @Test. For example:
  //
  // @Test
  // public void hello() {}
}
