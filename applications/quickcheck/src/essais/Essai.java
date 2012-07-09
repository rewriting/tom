/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package essais;

import definitions.Algebraic;
import java.util.Arrays;

/**
 *
 * @author hubert
 */
public class Essai {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    System.out.println(Arrays.toString(Algebraic.class.getDeclaredMethods()));
    System.out.println(Arrays.toString(Algebraic.class.getMethods()));
    System.out.println(Arrays.toString(Algebraic.class.getDeclaredConstructors()));
    foo(1,2,4);
  }
  
  public static void foo(int... args){
    System.out.println(args.length);
  }
}
