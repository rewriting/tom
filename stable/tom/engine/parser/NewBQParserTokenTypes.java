// $ANTLR 2.7.2: "bqparse.g" -> "NewBQLexer.java"$

    package jtom.parser;
    
    import jtom.*;
    import jtom.adt.tomsignature.*;
    import jtom.adt.tomsignature.types.*;

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
	int BQ_INTEGER = 10;
	int BQ_STRING = 11;
	int BQ_MINUS = 12;
	int ANY = 13;
	int BQ_DOT = 14;
	int BQ_DIGIT = 15;
	int BQ_UNDERSCORE = 16;
	int BQ_ESC = 17;
	int BQ_HEX_DIGIT = 18;
}
