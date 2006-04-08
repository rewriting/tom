
package parsingtests;

public class TokenTable {
  private static java.util.HashMap tokenMap = null;

  private static java.util.HashMap initTokenMap() {
    tokenMap = new java.util.HashMap();

    tokenMap.put(new Integer(13),"ID");
    tokenMap.put(new Integer(4),"SEQ");
    tokenMap.put(new Integer(9),"OR");
    tokenMap.put(new Integer(8),"AND");
    tokenMap.put(new Integer(11),"RPAREN");
    tokenMap.put(new Integer(6),"LIST");
    tokenMap.put(new Integer(14),"WS");
    tokenMap.put(new Integer(10),"LPAREN");
    tokenMap.put(new Integer(7),"IMPL");
    tokenMap.put(new Integer(12),"NOT");
    tokenMap.put(new Integer(5),"END");
    return tokenMap;
  }
  public static java.util.Map getTokenMap() {
    if (tokenMap == null) {
      tokenMap = initTokenMap();
    }
    return (java.util.Map)tokenMap.clone();
  }

}

