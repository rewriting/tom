// $ANTLR 2.7.2: "TomJavaParser.g" -> "TomJavaParser.java"$

  package jtom.parser;
    
  import antlr.*;
    
  import java.io.*;
  import java.util.*;
  import java.text.*;

  import jtom.TomEnvironment;
  import jtom.exception.*;
  import jtom.tools.*;
  import jtom.TomMessage;

public interface TomJavaParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int PACKAGE = 4;
	int SEMICOLON = 5;
	int ID = 6;
	int DOT = 7;
	int LETTER = 8;
	int DIGIT = 9;
	int WS = 10;
	int COMMENT = 11;
	int SL_COMMENT = 12;
	int ML_COMMENT = 13;
	int OTHER = 14;
}
