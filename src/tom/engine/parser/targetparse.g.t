header{
    import java.util.*;

    import aterm.*;
    import aterm.pure.*;
    
    import jtom.*;
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

    private TomEnvironment environment() {
        return TomEnvironment.getInstance();
    }

    // the file to be parsed
    private String filename;
    
    // the parser for tom constructs
    NewTomParser tomparser; 

    // the lexer
    NewTargetLexer targetlexer = (NewTargetLexer) Main.selector.getStream("targetlexer");
    
    StringBuffer targetLanguage = new StringBuffer("");

    Stack lines = new Stack();
    Stack columns = new Stack();

    public NewTargetParser(TokenStreamSelector selector, String filename){
        this(selector);
        this.filename = filename;
        this.tsf = environment().getTomSignatureFactory();
        tomparser = new NewTomParser(getInputState(),this,filename);
    }

    public int popLine(){
        return ((Integer) lines.pop()).intValue();
    }

    public int popColumn(){
        return ((Integer) columns.pop()).intValue();
    }

    public void pushLine(int line){
        lines.push(new Integer(line));
    }

    public void pushColumn(int column){
        columns.push(new Integer(column));
    }
    
    private String cleanCode(String code){
        return code.substring(code.indexOf('{')+1,code.lastIndexOf('}'));
    }

    private String getCode(){
        String result = targetlexer.target.toString();
        targetlexer.clearTarget();
        return result;
    }

    private boolean isCorrect(String code){
        return (! code.equals(""));
    }

    public TomList makeTomList(LinkedList list){
        TomList result = `emptyTomList();
        for(int i = 0; i < list.size(); i++){
            result = `concTomTerm(result*,(TomTerm) list.get(i));
        }
        return result;
    }

    private void printRes(ATerm result){
        try{
            Main.writer.write(result+"\n\n");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
}

input
{
    LinkedList list = new LinkedList();
    pushLine(1);
    pushColumn(1);
}
	:
        blockList[list] t:EOF
        {
            list.add(`TargetLanguageToTomTerm(
                    TL(
                        getCode(),
                        TextPosition(popLine(),popColumn()),
                        TextPosition(t.getLine(),t.getColumn())
                    )
                )
            );
            TomTerm term = `Tom(makeTomList(list));
            printRes(term);
        }
    ;

blockList [LinkedList list]
    :
        (
            matchConstruct()
        |   ruleConstruct[list] 
        |   signature()
        |   localVariable()
        |   operator()
        |   operatorList()
        |   operatorArray()
        |   includeConstruct()
        |   typeTerm[list] 
        |   typeList()
        |   typeArray()
        |   LBRACE  
            blockList[list]
            RBRACE 
        |   TARGET 
        )*

    ;

ruleConstruct [LinkedList list]
{
    TargetLanguage code = null;
}
    :
        t:RULE
        {     
            String textCode = getCode();
            if(isCorrect(textCode)) {
                code = `TL(
                    textCode,
                    TextPosition(popLine(),popColumn()),
                    TextPosition(t.getLine(),t.getColumn())
                );
                list.add(`TargetLanguageToTomTerm(code));
            }
                
            TomRuleList ruleList = tomparser.ruleConstruct();
            Option orgTrackRule = `OriginTracking(
                Name("Rule"),
                t.getLine(),
                Name(filename)
            );
            list.add(`InstructionToTomTerm(RuleSet(ruleList,orgTrackRule)));
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
typeTerm [LinkedList list]
{
    TargetLanguage code = null;
    int line, column;
}
    :
        (
            tt:TYPETERM 
            {
                line = tt.getLine();
                column = tt.getColumn();
            }
        |   t:TYPE
            {
                line = t.getLine();
                column = t.getColumn();
            }
        )
        {
            // addPreviousCode...
            String textCode = getCode();
            if(isCorrect(textCode)) {
                code = `TL(
                    textCode,
                    TextPosition(popLine(),popColumn()),
                    TextPosition(line,column));
                list.add(`TargetLanguageToTomTerm(code));
            }
            Declaration termdecl = tomparser.typeTerm();

//            list.add(`TargetLanguageToTomTerm(code));
            list.add(`DeclarationToTomTerm(termdecl));

            
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


goalLanguage [LinkedList list] returns [TargetLanguage result]
{
    result =  null;
}
    :
        t1:LBRACE 
        {
            pushLine(t1.getLine());
            pushColumn(t1.getColumn());
        }
        blockList[list]
        t2:RBRACE 
        {
            //code = targetlexer.target.toString();
            result = `TL(cleanCode(getCode()),
                TextPosition(popLine(),popColumn()),
                TextPosition(t2.getLine(),t2.getColumn())
            );
            targetlexer.clearTarget();
        }
    ;

targetLanguage [LinkedList list] returns [TargetLanguage result]
{
    result = null;
}
    :
        blockList[list] t:RBRACE
        {
            result = `TL(
                getCode(),
                TextPosition(popLine(),popColumn()),
                TextPosition(t.getLine(),t.getColumn())
            );
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
