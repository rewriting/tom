header{
    package jtom.parser;
    
    import antlr.*;
    
    import java.io.*;
    import java.util.*;
    import java.text.*;

    import jtom.TomEnvironment;
    import jtom.exception.*;
    import jtom.tools.*;
    import jtom.TomMessage;
}

class TomJavaParser extends Parser;

options{
    defaultErrorHandler = false;
}

{
    public static TomJavaParser createParser(String fileName) throws FileNotFoundException,IOException {
        File file = new File(fileName);
        TomJavaLexer lexer = new TomJavaLexer(new BufferedReader(new FileReader(file)));
        return new TomJavaParser(lexer);
    }
}

javaPackageDeclaration returns [String result]
{
    result = "";
}
    :
        PACKAGE result = javaName SEMICOLON
    |   . 
    ;

javaName returns [String result]
{
    result = "";
}
    :
        name:ID { result += name.getText(); }
        (
            DOT sname:ID { result += sname.getText(); }
        )*
    ;



class TomJavaLexer extends Lexer;

options{
    k=2;
    filter = OTHER;
    charVocabulary='\u0000'..'\uffff';
}

tokens{
    PACKAGE="package";
}

SEMICOLON   :   ';' ;
DOT :   '.' ;

ID  :
        LETTER ( LETTER | DIGIT )*
    ;

protected LETTER    :   ( 'a'..'z' | 'A'..'Z' | '_' | '$' ) ;

protected DIGIT :   ( '0'..'9' )    ;


// white spaces
WS	:	
        (	' '
		|	'\t'
		|	'\f'
            // handle newlines
		|	(	"\r\n"  // Evil DOS
			|	'\r'    // Macintosh
			|	'\n'    // Unix (the right way)
			)
			{ newline(); }
		)
        { $setType(Token.SKIP); }
	;

// comments
COMMENT 
    :
        ( SL_COMMENT | ML_COMMENT )
        { $setType(Token.SKIP);}
	;

protected
SL_COMMENT 
    :
        "//"
        ( ~('\n'|'\r') )*
        (
			options {
				generateAmbigWarnings=false;
			}
		:	'\r' '\n'
		|	'\r'
		|	'\n'
        )
        {
     //       target.append($getText);
            newline(); 
        }
	;

protected
ML_COMMENT 
    :
        "/*"        
        (	{ LA(2)!='/' }? '*' 
        |
        )
        (
            options {
                greedy=false;  // make it exit upon "*/"
                generateAmbigWarnings=false; // shut off newline errors
            }
        :	'\r' '\n'	{newline();}
        |	'\r'		{newline();}
        |	'\n'		{newline();}
        |	~('\n'|'\r')
        )*
        "*/"
       // {target.append($getText);}
	;

protected OTHER :   ( . )   ;
