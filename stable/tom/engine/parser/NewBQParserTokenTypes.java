// $ANTLR 2.7.2: "bqparse.g" -> "NewBQLexer.java"$

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

public interface NewBQParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int BQ_COMMA = 4;
	int BQ_WS = 5;
	int BQ_ID = 6;
	int BQ_STAR = 7;
	int BQ_LPAREN = 8;
	int BQ_RPAREN = 9;
	int BQ_DOT = 10;
	int BQ_INTEGER = 11;
	int BQ_STRING = 12;
	int BQ_MINUS = 13;
	int ANY = 14;
	int XML_START_ENDING = 15;
	int XML_CLOSE_SINGLETON = 16;
	int XML_START = 17;
	int XML_CLOSE = 18;
	int DOUBLE_QUOTE = 19;
	int XML_TEXT = 20;
	int XML_COMMENT = 21;
	int XML_PROC = 22;
	int XML_EQUAL = 23;
	int XML_SKIP = 24;
	int BQ_SIMPLE_ID = 25;
	int BQ_MINUS_ID = 26;
	int BQ_DIGIT = 27;
	int BQ_UNDERSCORE = 28;
	int BQ_ESC = 29;
	int BQ_HEX_DIGIT = 30;
}
