package parsingtests;

import java.io.*;
import java.util.*;

public class Gen {

  %include { string.tom }

  public static void main(String[] args) {
    Gen gen = new Gen();
    gen.gen(args[0]);
  }

  public void gen(String fileName) {
    // use the first argument as input file
    Map tokenMap = new HashMap();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));
      String line = "";
      while(line != null) {
        line = reader.readLine();
        %match(String line) {
          ('//',_*) -> { /* comment */}
          (name*,'=',value*) -> {
            Integer val = Integer.valueOf(`value);
            tokenMap.put(val,`name);
          }
        }
      }
    } catch (Exception e) {
      System.out.println("Exception "+e);
    }
    System.out.println(tokenMap);
    generateTable(tokenMap);
  }

  void generateTable(Map tokenMap) {
    StringBuffer out = new StringBuffer();

    out.append(%[
package parsingtests;

public class TokenTable {
  private static Map tokenMap = null;

  private Map initTokenMap() {
    tokenMap = new HashMap();
@initMap("tokenMap",tokenMap)@
  }
  public static Map getTokenMap() {
    if (tokenMap == null) {
      tokenMap = initTokenMap();
    }
    return tokenMap.clone();
  }

}

]%);
    System.out.println(out.toString());
  }

  private String initMap(String mapName, Map tokMap) {
    StringBuffer out = new StringBuffer();
    Iterator it = tokMap.keySet().iterator();
    while(it.hasNext()) {
      Integer key = (Integer)it.next();
      String value = (String)tokMap.get(key);
      out.append(%[
  @mapName@.put(new Integer(@key.intValue()@),"@value@");]%);
    }
    return out.toString();
  }

}
