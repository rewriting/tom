/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package libtests;

import java.util.Iterator;

/**
 *
 * @author hubert
 */
public class LibTests {

  public static void testIterator(Iterator<?> ite, int n, boolean print) {
    while(ite.hasNext()) {
      for(int i = 0; i < n; i++) {
        if(print) {
          System.out.println("true " + i);
        }
        assert ite.hasNext();
      }
      ite.next();
    }
    for(int i = 0; i < n; i++) {
      if(print) {
        System.out.println("false " + i);
      }
      assert !ite.hasNext();
    }
  }
}
