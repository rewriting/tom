// $ANTLR 2.7.2: "HostLanguage.g" -> "HostParser.java"$

  package jtom.parser;
  
  import jtom.*;
  import jtom.exception.*;
  import jtom.tools.*;
  import jtom.adt.tomsignature.*;
  import jtom.adt.tomsignature.types.*;
  
  import tom.platform.*;
  
  import java.lang.reflect.*;
  import java.io.*;
  import java.util.*;
  import java.util.logging.*;

  import aterm.*;
  import aterm.pure.*;    
  
  import antlr.*;

public interface HostParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int STRING = 4;
	int LBRACE = 5;
	int RBRACE = 6;
	int RULE = 7;
	int MATCH = 8;
	int VAS = 9;
	int BACKQUOTE = 10;
	int OPERATOR = 11;
	int OPERATORLIST = 12;
	int OPERATORARRAY = 13;
	int INCLUDE = 14;
	int TYPETERM = 15;
	int TYPE = 16;
	int TYPELIST = 17;
	int TYPEARRAY = 18;
	int ESC = 19;
	int HEX_DIGIT = 20;
	int WS = 21;
	int COMMENT = 22;
	int SL_COMMENT = 23;
	int ML_COMMENT = 24;
	int TARGET = 25;
}
