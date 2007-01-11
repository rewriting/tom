package tom.gom.parser;
	  
	public class TokenTable {
	private static java.util.HashMap tokenMap = null;

	private static java.util.HashMap initTokenMap() {
	tokenMap = new java.util.HashMap();
	
	  tokenMap.put(new Integer(15),"SEMI");
	  tokenMap.put(new Integer(30),"RBRACE");
	  tokenMap.put(new Integer(8),"PUBLIC");
	  tokenMap.put(new Integer(23),"BLOCK");
	  tokenMap.put(new Integer(31),"WS");
	  tokenMap.put(new Integer(16),"LEFT_BRACE");
	  tokenMap.put(new Integer(7),"IMPORTS");
	  tokenMap.put(new Integer(22),"MAKEINSERT");
	  tokenMap.put(new Integer(32),"SLCOMMENT");
	  tokenMap.put(new Integer(9),"SORTS");
	  tokenMap.put(new Integer(33),"ML_COMMENT");
	  tokenMap.put(new Integer(21),"MAKE");
	  tokenMap.put(new Integer(6),"DOT");
	  tokenMap.put(new Integer(29),"LBRACE");
	  tokenMap.put(new Integer(14),"ALT");
	  tokenMap.put(new Integer(24),"INTERFACE");
	  tokenMap.put(new Integer(4),"MODULE");
	  tokenMap.put(new Integer(19),"OPERATOR");
	  tokenMap.put(new Integer(26),"SORT");
	  tokenMap.put(new Integer(11),"SYNTAX");
	  tokenMap.put(new Integer(18),"RIGHT_BRACE");
	  tokenMap.put(new Integer(12),"ARROW");
	  tokenMap.put(new Integer(27),"STAR");
	  tokenMap.put(new Integer(17),"COMMA");
	  tokenMap.put(new Integer(13),"EQUALS");
	  tokenMap.put(new Integer(28),"PRIVATE");
	  tokenMap.put(new Integer(20),"COLON");
	  tokenMap.put(new Integer(25),"IMPORT");
	  tokenMap.put(new Integer(10),"ABSTRACT");
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

	