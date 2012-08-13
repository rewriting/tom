/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package essais;

import aterm.ATerm;
import aterm.ATermIterator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hubert
 */
public class Test_Java {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    Map<String, Integer> map = new HashMap<String, Integer>();
    Integer n = map.get("truc");
    System.out.println(n);

    ATermIterator truc = new ATermIterator() {
      public boolean a = true;

      @Override
      public boolean hasNext() {
        return a;
      }

      @Override
      public ATerm next() {
        a = !a;
        return null;
      }
    };
    
    ATermIterator truc2 = truc.clone();
    System.out.println(truc.hasNext());
    System.out.println(truc2.hasNext());
    System.out.println(truc.hasNext());
    System.out.println(truc2.hasNext());
    System.out.println("");
    truc.next();
    System.out.println(truc.hasNext());
    System.out.println(truc2.hasNext());
    System.out.println(truc.hasNext());
    System.out.println(truc2.hasNext());
  }
}
