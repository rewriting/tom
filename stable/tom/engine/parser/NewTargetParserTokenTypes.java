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
	int LBRACE = 4;
	int RBRACE = 5;
	int RULE = 6;
	int MATCH = 7;
	int VAS = 8;
	int BACKQUOTE = 9;
	int VARIABLE = 10;
	int OPERATOR = 11;
	int OPERATORLIST = 12;
	int OPERATORARRAY = 13;
	int INCLUDE = 14;
	int TYPETERM = 15;
	int TYPE = 16;
	int TYPELIST = 17;
	int TYPEARRAY = 18;
	int WS = 19;
	int HEX_DIGIT = 20;
	int COMMENT = 21;
	int SL_COMMENT = 22;
	int ML_COMMENT = 23;
	int TARGET = 24;
}
