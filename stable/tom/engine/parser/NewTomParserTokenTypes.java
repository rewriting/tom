// $ANTLR 2.7.2: "tomparse.g" -> "NewTomLexer.java"$

    package jtom.parser;

    import java.util.*;
    import java.util.logging.*;
    import java.text.*;

    import aterm.*;
    import aterm.pure.*;
    
    import jtom.*;
    import jtom.adt.tomsignature.*;
    import jtom.adt.tomsignature.types.*;
    import jtom.exception.*;
    import jtom.tools.*;

    import jtom.xml.Constants;

    import tom.platform.*;

    import antlr.*;

public interface NewTomParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int NUM_INT = 4;
	int CHARACTER = 5;
	int STRING = 6;
	int NUM_FLOAT = 7;
	int NUM_LONG = 8;
	int NUM_DOUBLE = 9;
	int LPAREN = 10;
	int RPAREN = 11;
	int LBRACE = 12;
	int RBRACE = 13;
	int COMMA = 14;
	int ALL_ID = 15;
	int BACKQUOTE = 16;
	int COLON = 17;
	int ALTERNATIVE = 18;
	int ARROW = 19;
	int WHERE = 20;
	int AFFECT = 21;
	int IF = 22;
	int DOUBLEEQ = 23;
	int AT = 24;
	int XML_START = 25;
	int XML_CLOSE_SINGLETON = 26;
	int XML_CLOSE = 27;
	int XML_START_ENDING = 28;
	int XML_TEXT = 29;
	int XML_COMMENT = 30;
	int XML_PROC = 31;
	int LBRACKET = 32;
	int RBRACKET = 33;
	int EQUAL = 34;
	int UNDERSCORE = 35;
	int TOM_COMMA = 36;
	int STAR = 37;
	int IMPLEMENT = 38;
	int GET_FUN_SYM = 39;
	int GET_SUBTERM = 40;
	int CMP_FUN_SYM = 41;
	int EQUALS = 42;
	int GET_HEAD = 43;
	int GET_TAIL = 44;
	int IS_EMPTY = 45;
	int GET_ELEMENT = 46;
	int GET_SIZE = 47;
	int FSYM = 48;
	int IS_FSYM = 49;
	int GET_SLOT = 50;
	int MAKE = 51;
	int MAKE_EMPTY = 52;
	int MAKE_INSERT = 53;
	int MAKE_APPEND = 54;
	int DOULEARROW = 55;
	int DOUBLE_QUOTE = 56;
	int WS = 57;
	int SLCOMMENT = 58;
	int ML_COMMENT = 59;
	int ESC = 60;
	int HEX_DIGIT = 61;
	int LETTER = 62;
	int DIGIT = 63;
	int ID = 64;
	int ID_MINUS = 65;
	int MINUS = 66;
	int PLUS = 67;
	int QUOTE = 68;
	int EXPONENT = 69;
	int DOT = 70;
	int FLOAT_SUFFIX = 71;
}
