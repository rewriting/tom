/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 *
 * @author hubert
 */
public class Random {

  public static int[] pile(int n, int k) {
    int[] res = new int[k];
    for (int i = 0; i < res.length; i++) {
      res[i] = 0;
    }
    if (k == 0) {
      return res;
    }
    while (n != 0) {
      int index = (int) (Math.random() * k);
      res[index]++;
      n--;
    }
    return res;
  }
}
