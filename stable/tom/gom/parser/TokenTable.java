
package tom.gom.parser;

public class TokenTable {
  private static java.util.HashMap tokenMap = null;

  private static java.util.HashMap initTokenMap() {
    tokenMap = new java.util.HashMap();

    tokenMap.put(new Integer(15),"LEFT_BRACE");
    tokenMap.put(new Integer(4),"MODULE");
    tokenMap.put(new Integer(19),"STAR");
    tokenMap.put(new Integer(8),"SORTS");
    tokenMap.put(new Integer(23),"WS");
    tokenMap.put(new Integer(11),"ARROW");
    tokenMap.put(new Integer(16),"COMMA");
    tokenMap.put(new Integer(18),"COLON");
    tokenMap.put(new Integer(7),"PUBLIC");
    tokenMap.put(new Integer(12),"EQUALS");
    tokenMap.put(new Integer(22),"RBRACE");
    tokenMap.put(new Integer(17),"RIGHT_BRACE");
    tokenMap.put(new Integer(13),"ALT");
    tokenMap.put(new Integer(9),"ABSTRACT");
    tokenMap.put(new Integer(21),"LBRACE");
    tokenMap.put(new Integer(6),"IMPORTS");
    tokenMap.put(new Integer(20),"PRIVATE");
    tokenMap.put(new Integer(14),"SEMI");
    tokenMap.put(new Integer(25),"ML_COMMENT");
    tokenMap.put(new Integer(24),"SLCOMMENT");
    tokenMap.put(new Integer(10),"SYNTAX");
    tokenMap.put(new Integer(5),"ID");
    return tokenMap;
  }
  public static java.util.Map getTokenMap() {
    if (tokenMap == null) {
      tokenMap = initTokenMap();
    }
    return (java.util.Map)tokenMap.clone();
  }

}

