// $ANTLR 2.7.2: "BackQuoteLanguage.g" -> "BackQuoteParser.java"$

    package jtom.parser;
    
    import jtom.*;
    import jtom.adt.tomsignature.*;
    import jtom.adt.tomsignature.types.*;
    import jtom.xml.*;

    import tom.platform.*;
    
    import aterm.*;
    import aterm.pure.*;

    import antlr.*;

    import java.util.*;

public interface BackQuoteParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int BQ_WS = 4;
	int BQ_COMMA = 5;
	int BQ_ID = 6;
	int BQ_STRING = 7;
	int XML_EQUAL = 8;
	int BQ_STAR = 9;
	int XML_START = 10;
	int XML_CLOSE_SINGLETON = 11;
	int XML_CLOSE = 12;
	int XML_START_ENDING = 13;
	int XML_TEXT = 14;
	int BQ_LPAREN = 15;
	int BQ_RPAREN = 16;
	int BQ_INTEGER = 17;
	int BQ_MINUS = 18;
	int BQ_DOT = 19;
	int DOUBLE_QUOTE = 20;
	int ANY = 21;
	int XML_COMMENT = 22;
	int XML_PROC = 23;
	int XML = 24;
	int XML_SKIP = 25;
	int BQ_SIMPLE_ID = 26;
	int BQ_MINUS_ID = 27;
	int BQ_DIGIT = 28;
	int BQ_UNDERSCORE = 29;
	int BQ_ESC = 30;
	int BQ_HEX_DIGIT = 31;
}
