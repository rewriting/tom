// $ANTLR 2.7.2: "targetparse.g" -> "NewTargetParser.java"$

    package jtom.parser;

    import jtom.*;
    import jtom.exception.*;
    import jtom.tools.*;
    import jtom.adt.tomsignature.*;
    import jtom.adt.tomsignature.types.*;

    import tom.platform.*;

    import java.lang.reflect.*;
    import java.io.*;
    import java.text.*;
    import java.util.*;
    import java.util.logging.*;

    import aterm.*;
    import aterm.pure.*;    

    import antlr.*;

public interface NewTargetParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int STRING = 4;
	int LBRACE = 5;
	int RBRACE = 6;
	int RULE = 7;
	int MATCH = 8;
	int VAS = 9;
	int BACKQUOTE = 10;
	int VARIABLE = 11;
	int OPERATOR = 12;
	int OPERATORLIST = 13;
	int OPERATORARRAY = 14;
	int INCLUDE = 15;
	int TYPETERM = 16;
	int TYPE = 17;
	int TYPELIST = 18;
	int TYPEARRAY = 19;
	int ESC = 20;
	int HEX_DIGIT = 21;
	int WS = 22;
	int COMMENT = 23;
	int SL_COMMENT = 24;
	int ML_COMMENT = 25;
	int TARGET = 26;
}
