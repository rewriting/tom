header{
    import aterm.*;
    import aterm.pure.*;
    
    import jtom.adt.tomsignature.*;
    import jtom.adt.tomsignature.types.*;

    import antlr.*;
}

class NewTargetParser extends Parser;


{
    //--------------------------
    %include{TomSignature.tom}
    //--------------------------

    private Factory tsf;
    private final Factory getTomSignatureFactory(){
        return tsf;
    }

    // the file to be parsed
    private String filename;

    public NewTargetParser(TokenStreamSelector selector, String filename){
        this(selector);
        this.filename = filename;
        tsf = new Factory(new PureFactory());
        tomparser = new NewTomParser(getInputState(),this, tsf,filename);
    }

    // the parser for tom constructs
    NewTomParser tomparser; 

    // the lexer
    NewTargetLexer targetlexer = (NewTargetLexer) Main.selector.getStream("targetlexer");
    
    
    StringBuffer targetLanguage = new StringBuffer("");

    private String cleanCode(String code){
        return code.substring(code.indexOf('{')+1,code.lastIndexOf('}'));
    }

    
}

input
	:
        blockList() 
        {
            System.out.println("------- buffer :\n"+targetlexer.target);
        }
    ;

blockList //returns [String result]
/*{
    result = null;
}
  */  :
        (
            matchConstruct()
        |   ruleConstruct() 
        |   signature()
        |   localVariable()
        |   operator()
        |   operatorList()
        |   operatorArray()
        |   includeConstruct()
        |   typeTerm()
        |   typeList()
        |   typeArray()
        |   LBRACE  
            blockList() 
            RBRACE 
        |   TARGET 
        )*

    ;

ruleConstruct
    :
        {
            System.out.println("target language :"+targetlexer.target);
            targetlexer.clearTarget();
        }
        RULE
        {         
            tomparser.ruleConstruct();
        }
    ;

matchConstruct
	:
        {
            System.out.println("target language :"+targetlexer.target);
            targetlexer.clearTarget();
        }
        MATCH 
        {         
            tomparser.matchConstruct();
        }
    ;

signature
    :
        {
            System.out.println("target language :"+targetlexer.target);
            targetlexer.clearTarget();
        }
        VAS 
        {   
            tomparser.signature();
        } 
    ;

localVariable
    :
        {
            targetlexer.clearTarget();
        }
        VARIABLE
        {
            tomparser.variable();
        }
    ;

operator
    :
        {
            System.out.println("target language :"+targetlexer.target);
            targetlexer.clearTarget();
        }
        OPERATOR 
        {
            tomparser.operator();
        }
    ;

operatorList
    :
        {
            System.out.println("target language :"+targetlexer.target);
            targetlexer.clearTarget();
        }
        OPERATORLIST 
        {
            tomparser.operatorList();
        }
    ;

operatorArray
    :
        {
            System.out.println("target language :"+targetlexer.target);
            targetlexer.clearTarget();
        }
        OPERATORARRAY
        {
            tomparser.operatorArray();
        }
    ;

includeConstruct
    :
        INCLUDE
        {
            tomparser.include();
        }
        
    ;
typeTerm
    :
        (
            TYPETERM
        |   TYPE
        )
        {
            tomparser.typeTerm();
        }

    ;

typeList
    :
        TYPELIST
        {
             tomparser.typeList();
        }
    ;

typeArray
    :
        TYPEARRAY
        {
             tomparser.typeArray();
        }
    ;


goalLanguage returns [TargetLanguage result]
{
    result = null;
    String code = null;
}
    :
        t1:LBRACE 
        blockList 
        t2:RBRACE 
        {
            code = targetlexer.target.toString();
            result = `TL(cleanCode(code),
                         TextPosition(t1.getLine(),t1.getColumn()),
                         TextPosition(t2.getLine(),t2.getColumn())
                        );
            targetlexer.clearTarget();
        }
    ;

targetLanguage returns [String result]
{
    result = null;
}
    :
        blockList() RBRACE
        {
            result = targetlexer.target.toString();
            targetlexer.clearTarget();
     
        }
    ;


// here begins the lexer


class NewTargetLexer extends Lexer;
options {
	k=6;
    filter=TARGET;
    charVocabulary='\u0000'..'\uffff';
}

{
    private boolean appendBraces = false;

    public StringBuffer target = new StringBuffer("");
    
    public void setAppendBraces(boolean b){
        appendBraces = b;
    }

    public void clearTarget(){
        target.delete(0,target.length());
    }
}

RULE
    :   "%rule" {Main.selector.push("tomlexer");}
    ;
INCLUDE
    :   "%include" 
    ;
MATCH
	:	"%match" {Main.selector.push("tomlexer");}
    ;
VARIABLE
    :   "%variable" 
	;
VAS
    :   "%vas"  
    ;
OPERATOR
    :   "%op"   {Main.selector.push("tomlexer");}
    ;
TYPE
    :   "%type" {Main.selector.push("tomlexer");}
    ;
TYPETERM
    :   "%typeterm" {Main.selector.push("tomlexer");}
    ;
TYPELIST
    :   "%typelist" {Main.selector.push("tomlexer");}
    ;
TYPEARRAY
    :   "%typearray" {Main.selector.push("tomlexer");}
    ;   
OPERATORLIST
    :   "%oplist"   {Main.selector.push("tomlexer");}
    ;
OPERATORARRAY
    :   "%oparray"  {Main.selector.push("tomlexer");}
    ;

LBRACE  
    :   '{' 
        {
            target.append($getText);
        }  
    ;
RBRACE  
    :   '}' 
        {
            target.append($getText);
        }  
    ; 

WS	:	(	' '
		|	'\t'
		|	'\f'
		// handle newlines
		|	(	"\r\n"  // Evil DOS
			|	'\r'    // Macintosh
			|	'\n'    // Unix (the right way)
			)
			{ newline(); }
		){  target.append($getText);
            $setType(Token.SKIP);}
		
	;


protected
ESC
	:	'\\'
		(	'n'
		|	'r'
		|	't'
		|	'b'
		|	'f'
		|	'"'
		|	'\''
		|	'\\'
		|	('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
		|	'0'..'3'
			(
				options {
					warnWhenFollowAmbig = false;
				}
			:	'0'..'7'
				(
					options {
						warnWhenFollowAmbig = false;
					}
				:	'0'..'7'
				)?
			)?
		|	'4'..'7'
			(
				options {
					warnWhenFollowAmbig = false;
				}
			:	'0'..'7'
			)?
		)
	;

protected
HEX_DIGIT
	:	('0'..'9'|'A'..'F'|'a'..'f')
	;

COMMENT 
    :
        ( SL_COMMENT | t:ML_COMMENT {$setType(t.getType());} )
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
        { newline(); }
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
	;

protected 
TARGET 
    :
        (   
            . 
        )
        {target.append($getText);}
    ;
