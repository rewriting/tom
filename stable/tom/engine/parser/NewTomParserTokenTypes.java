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
	int STAR = 36;
	int IMPLEMENT = 37;
	int GET_FUN_SYM = 38;
	int GET_SUBTERM = 39;
	int CMP_FUN_SYM = 40;
	int EQUALS = 41;
	int GET_HEAD = 42;
	int GET_TAIL = 43;
	int IS_EMPTY = 44;
	int GET_ELEMENT = 45;
	int GET_SIZE = 46;
	int FSYM = 47;
	int IS_FSYM = 48;
	int GET_SLOT = 49;
	int MAKE = 50;
	int MAKE_EMPTY = 51;
	int MAKE_INSERT = 52;
	int MAKE_APPEND = 53;
	int DOULEARROW = 54;
	int DOUBLE_QUOTE = 55;
	int WS = 56;
	int SLCOMMENT = 57;
	int ML_COMMENT = 58;
	int ESC = 59;
	int HEX_DIGIT = 60;
	int LETTER = 61;
	int DIGIT = 62;
	int ID = 63;
	int ID_MINUS = 64;
	int MINUS = 65;
	int PLUS = 66;
	int QUOTE = 67;
	int EXPONENT = 68;
	int DOT = 69;
	int FLOAT_SUFFIX = 70;
}
