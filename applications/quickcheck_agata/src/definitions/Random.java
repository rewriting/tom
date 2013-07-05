/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 *
 * @author hubert
 */
class Random {

  static int[] pile(int n, int k) {
    int[] res = new int[k];
    for (int i = 0; i < res.length; i++) {
      res[i] = 0;
    }
    if (k == 0) {
      return res;
    }
    for (int i = 0; i < n; i++) {
      int index = (int) (Math.random() * k);
      res[index]++;
    }
    return res;
  }
}
