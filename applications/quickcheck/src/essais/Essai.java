/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package essais;

import aterm.AFun;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.pure.PureFactory;

/**
 *
 * @author hubert
 */
public class Essai {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    PureFactory factory = new PureFactory();
    AFun fun = factory.makeAFun("fun", 1, false);
    System.out.println(fun);
    ATerm term = factory.make("aterm");
    System.out.println(term);
    ATermAppl appl = factory.makeAppl(fun, term);
    System.out.println(appl);
    
  }
}
