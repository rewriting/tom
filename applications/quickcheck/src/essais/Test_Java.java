/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package essais;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    
    String s = "machin()";
    String[] tab = s.split("(");
    System.out.println(Arrays.toString(tab));
  }
}
