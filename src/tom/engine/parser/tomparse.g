
class NewTomParser extends Parser;


options{
    k=2;
    exportVocab = Tom;
}

tokens{
    WHERE="where";IF="if";
}

{
    private NewTargetParser targetparser;
    
    public StringBuffer match = new StringBuffer("");

    public NewTomParser(ParserSharedInputState state, NewTargetParser target){
        this(state);
        targetparser = target;
    }

    public void variable(){
        // creer la structure ici
    }
   
}

constant returns [String result]
{
    result = null;
}
	:	(
            t1:NUM_INT {result = t1.getText();}
        |	t2:CHAR_LITERAL {result = t2.getText();}
        |	t3:STRING_LITERAL {result = t3.getText();}
        |	t4:NUM_FLOAT {result = t4.getText();}
        |	t5:NUM_LONG {result = t5.getText();}
        |	t6:NUM_DOUBLE {result = t6.getText();}
        )
        { System.out.println("constante : "+result); }
	;

matchConstruct 
{ 
    String args = null, mp = null;
    String result = null;
}
	:	(
            LPAREN args = matchArguments() RPAREN {result = "(" + args + ")";}
            LBRACE {result += "{\n";}
            ( 
                mp = patternAction() {result += mp + "\n";}
            )* 
            RBRACE {result += "}";}
            { 
                System.out.println("match : \n" +
                                   "--------------\n" +
                                    result +
                                   "\n--------------");
                Main.selector.pop(); 
            }
        )
	;

matchArguments returns [String result]
{ 
    String arg = null;
    result = null;
}
    :   (
            result = matchArgument() ( COMMA  arg = matchArgument() {result += "," + arg;} )*
        )
    ;

matchArgument returns [String result]
{
    result = null;
}
    :   (
            i1:ID i2:ID  {result = i1.getText() + " " + i2.getText();}
        )
    ;

patternAction returns [String result]
{
    result = null;
    String mp = null, mp2 = null;
}
    :   (
            mp = matchPattern() {result = mp;}
            ( 
                ALTERNATIVE mp2 = matchPattern() {result += "|" + mp2;}
            )* 
            ARROW {result += "->";}
            {
                Main.selector.push("targetlexer");
                String action = targetparser.goalLanguage();
/*                System.out.println("action : \n -------------------\n"
                                    + action+
                                              "\n--------------------");*/
                result += action;
                Main.selector.pop();
            }
        )
    ;

matchPattern returns [String result]
{
    result = null;
    String at = null, at2 = null;
}
    :   (
            at = annotedTerm() {result = at;}
            ( 
                COMMA at2 = annotedTerm() {result += "," + at2;}
            )*
        )
    ;

annotedTerm returns [String result]
{
    result = "";
    String pt = null;
}
    :   (
            ( 
                i:ID AT {result += i.getText() + "@";}
            )? 
            pt = plainTerm() {result += pt;}
        )
    ;

plainTerm returns [String result]
{
    result = null;
    String v = null, p = null, s = null, el = null, il = null,
    el1 = null, sl = null, el2 = null, il2 = null;
}
    :  
        (   // xml is missing
            v = variableStar() {result = v;}
        |   p = placeHolder() {result = p;}
        |    
            (
                s = headSymbol() {result = s;}
                ( 
                    {LA(3) != WHERE && LA(3) != IF}? el = explicitTermList() {result += el;}
                |   il = implicitPairList() {result += il;}
                )?
            |   {LA(3) != ALTERNATIVE}? el1 = explicitTermList() {result = el1;}
            |   ( 
                    sl = headSymbolList()  {result = sl;}
                    ( 
                        {LA(3) != WHERE && LA(3) != IF}? el2 = explicitTermList() {result += el2;}
                    |   il2 = implicitPairList() {result += il2;} )?  
                )
            )
        )
    ;

variableStar returns [String result]
{ result = null; }
    :   (
            ( 
                i:ID {result = i.getText();}
            |   u:UNDERSCORE {result = u.getText();}
            ) 
            STAR {result += "*";}
        )
    ;

placeHolder returns [String result]
{ result = null; }
    :   (
            UNDERSCORE {result = "_";} 
        )
    ;

headSymbolList returns [String result]
{ 
    result = null;
    String s1 = null, s2 = null, s3 = null;
}
    :  
        (
            LPAREN s1 = headSymbol() ALTERNATIVE s2 = headSymbol() {result = "(" + s1 + "|" + s2;}
            ( ALTERNATIVE s3 = headSymbol() {result += "|" + s3;})* 
            RPAREN {result += ")";}
        )
    ;

headSymbol returns [String result]
{ 
    result = null; 
    String cst = null;
}
    :   (
            i:ID {result = i.getText();}
        |   cst = constant() {result = cst;}
        |   c:CHARACTER {result = c.getText();}
        |   s:STRING {result = s.getText();}
        )
    ;

explicitTermList returns [String result]
{
    result = null;
    String at = null, at2 = null;
}
    :   (
            LPAREN {result = "(";}
            (   at = annotedTerm() {result += at;} 
                ( 
                    COMMA at2 = annotedTerm() {result += "," + at2;}
                )* 
            )? 
            RPAREN {result += ")";} 
        )
    ;

implicitPairList returns [String result]
{
    result = null;
    String pt = null, pt2 = null;
}
    :   (
            LBRACKET {result = "[";}
            ( 
                pt = pairTerm() {result += pt;} 
                ( 
                    COMMA pt2 = pairTerm() {result += "," + pt2;}
                )* 
            )? 
            RBRACKET {result += "]";} 
        )
    ;

pairTerm returns [String result]
{
    result = null;
    String at = null;
}
    :   (
            i:ID EQUAL at = annotedTerm() {result = i.getText() + "=" + at;}
        )
    ;

signature
    :   
        { String vasCode = targetparser.goalLanguage(); 
            System.out.println("code to be parsed by VAS :\n --------------------------"
                                +vasCode+
                               "\n--------------------------");}
        // faire les action adequates ici
    ;

operator
    :
        {
            System.out.println("begin %op");
        }
        i1:ID i2:ID 
        (
            LPAREN
            ( ID COLON )? ID
            ( COMMA ( ID COLON )? ID )*
            RPAREN
        )?
        LBRACE
        keywordFsym()
        (
           keywordMake()  | keywordGetSlot() | keywordIsFsym()
        )*
        RBRACE 
        { 
            Main.selector.pop(); 
            System.out.println("end %op");
        }
    ;

operatorList
    :
        ID ID
        LPAREN ID STAR RPAREN
        LBRACE
        keywordFsym()
        (
            keywordMakeEmptyList()
        |   keywordMakeAddList()
        |   keywordIsFsym()
        )*
        RBRACE
        { 
            Main.selector.pop(); 
            System.out.println("end %op");
        }
    ;

operatorArray
    :
        ID ID
        LPAREN ID STAR RPAREN
        LBRACE
        keywordFsym()
        (
            keywordMakeEmptyArray()
        |   keywordMakeAddArray()
        |   keywordIsFsym()
        )*
        RBRACE
        { 
            Main.selector.pop(); 
            System.out.println("end %op");
        }
    ;

keywordMakeEmptyList
    :
        "make_empty" (LPAREN RPAREN)? 
        {
            Main.selector.push("targetlexer");
            String tlCode = targetparser.goalLanguage();
            System.out.println("--- makeEmptyList : "+tlCode);
            Main.selector.pop();
        }
    ;

keywordMakeAddList
    :
        "make_insert" LPAREN ID COMMA ID RPAREN
        {
            Main.selector.push("targetlexer");
            String tlCode = targetparser.goalLanguage();
            System.out.println("--- makeInsert : "+tlCode);
            Main.selector.pop();
        }
    ;

keywordMakeEmptyArray
    :
        "make_empty" LPAREN ID RPAREN
        {
            Main.selector.push("targetlexer");
            String tlCode = targetparser.goalLanguage();
            System.out.println("--- makeEmptyarray : "+tlCode);
            Main.selector.pop();
        }
    ;   

keywordMakeAddArray
    :
        "make_append" LPAREN ID COMMA ID RPAREN
        {
            Main.selector.push("targetlexer");
            String tlCode = targetparser.goalLanguage();
            System.out.println("--- makeaddarray : "+tlCode);
            Main.selector.pop();
        }
    ;

keywordFsym
    :
        "fsym" 
        {
            Main.selector.push("targetlexer");
            String tlCode = targetparser.goalLanguage();
            System.out.println("--- fsym : "+tlCode);
            Main.selector.pop();
        }
    ;

keywordMake
    :
        (
            "make" ( LPAREN ( ID ( COMMA ID )* )? RPAREN )?
            LBRACE
            {
                Main.selector.push("targetlexer");
                String tlCode = targetparser.targetLanguage();
                System.out.println("--- make : "+"{"+tlCode);
                Main.selector.pop();
            }
           
        )
    ;
 
keywordGetSlot
    :
        "get_slot" LPAREN ID COMMA ID RPAREN
        {
            Main.selector.push("targetlexer");
            String tlCode = targetparser.goalLanguage();
            System.out.println("--- get_slot : "+tlCode);
            Main.selector.pop();
        }
    ;

keywordIsFsym
    :
        "is_fsym" LPAREN ID RPAREN
       {
            Main.selector.push("targetlexer");
            String tlCode = targetparser.goalLanguage();
            System.out.println("--- is_fsym : "+tlCode);
            Main.selector.pop();
        }
    ;

include
    :   {targetparser.goalLanguage();}
    ;

keywordGetFunSym
    :
        (
            "get_fun_sym" LPAREN ID RPAREN
            {
                Main.selector.push("targetlexer");
                String tlCode = targetparser.goalLanguage();
                System.out.println("--- get_fun_sym : "+"{"+tlCode);
                Main.selector.pop();
            }
        )
        
    ;

keywordGetSubterm
    :
        (
            "get_subterm" LPAREN ID COMMA ID RPAREN
            {
                Main.selector.push("targetlexer");
                String tlCode = targetparser.goalLanguage();
                System.out.println("--- get_subterm : "+"{"+tlCode);
                Main.selector.pop(); 
            }
        )
        
    ;

keywordCmpFunSym
    :
        (
            "cmp_fun_sym" LPAREN ID COMMA ID RPAREN
            {
                Main.selector.push("targetlexer");
                String tlCode = targetparser.goalLanguage();
                System.out.println("--- cmp_fun_sym : "+"{"+tlCode);
                Main.selector.pop(); 
            }
        )
    ;

keywordEquals
    :
        (
            "equals" LPAREN ID COMMA ID RPAREN
            {
                Main.selector.push("targetlexer");
                String tlCode = targetparser.goalLanguage();
                System.out.println("--- equals : "+"{"+tlCode);
                Main.selector.pop();  
            }
        )
    ;

keywordGetHead
    :
        (
            "get_head" LPAREN ID RPAREN
            {
                Main.selector.push("targetlexer");
                String tlCode = targetparser.goalLanguage();
                System.out.println("--- get_head : "+"{"+tlCode);
                Main.selector.pop();  
            }
        )
    ;

keywordGetTail
    :
        (
            "get_tail" LPAREN ID RPAREN
            {
                Main.selector.push("targetlexer");
                String tlCode = targetparser.goalLanguage();
                System.out.println("--- get_tail : "+"{"+tlCode);
                Main.selector.pop();  
            }
        )
    ;

keywordIsEmpty
    :
        (
            "is_empty" LPAREN ID RPAREN
            {
                Main.selector.push("targetlexer");
                String tlCode = targetparser.goalLanguage();
                System.out.println("--- is_empty : "+"{"+tlCode);
                Main.selector.pop();  
            }
        )
    ;

keywordImplement
    :
        (
            "implement"
            {
                Main.selector.push("targetlexer");
                String tlCode = targetparser.goalLanguage();
                System.out.println("--- implement : "+"{"+tlCode);
                Main.selector.pop();  
            }
        )
    ;

keywordGetElement
    :
        (
            "get_element" LPAREN ID COMMA ID RPAREN
            {
                Main.selector.push("targetlexer");
                String tlCode = targetparser.goalLanguage();
                System.out.println("--- getElement : "+"{"+tlCode);
                Main.selector.pop();  
            }
        )
    ;

keywordGetSize
    :
        "get_size" LPAREN ID RPAREN
            {
                Main.selector.push("targetlexer");
                String tlCode = targetparser.goalLanguage();
                System.out.println("--- getsize : "+"{"+tlCode);
                Main.selector.pop();  
            }
    ;

typeTerm
    :   {System.out.println("***** typeTerm");}
        (
            ID
            LBRACE

            keywordImplement()
            (
                keywordGetFunSym()
            |   keywordGetSubterm()
            |   keywordCmpFunSym()
            |   keywordEquals()
            )*
            RBRACE

        )
        {Main.selector.pop();}
    ;

typeList
    :   {System.out.println("***** typeList");}
        (
            ID
            LBRACE
            keywordImplement()
            (
                keywordGetFunSym()
            |   keywordGetSubterm()
            |   keywordCmpFunSym()
            |   keywordEquals() 
            |   keywordGetHead()
            |   keywordGetTail()
            |   keywordIsEmpty()
            )*
            RBRACE

        )
        {Main.selector.pop();}
    ;

typeArray
    :   {System.out.println("***** typeArray");}
        (
            ID
            LBRACE
            keywordImplement()
            (
                keywordGetFunSym()
            |   keywordGetSubterm()
            |   keywordCmpFunSym()
            |   keywordEquals() 
            |   keywordGetElement()
            |   keywordGetSize()
            )*
            RBRACE
        )
        {Main.selector.pop();}
    ;




class NewTomLexer extends Lexer;
options {
	k=2;
    exportVocab = Tom;
    charVocabulary = '\u0000'..'\uffff';
	codeGenBitsetTestThreshold=20;
    testLiterals = false;
}




LBRACE      :   '{' ;
RBRACE      :   '}' ;
LPAREN      :   '(' ;
RPAREN      :   ')' ;
LBRACKET    :   '[' ;
RBRACKET    :   ']' ;
COMMA       :   ',' ;
ARROW       :   "->";
DOULEARROW  :   "=>"    ;
ALTERNATIVE :   '|' ;
COLON       :   ':' ;
EQUAL       :   '=' ;
AT          :   '@' ;
STAR        :   '*' ;
BACKQUOTE   :   '`' ;
UNDERSCORE  :   '_' ;   

WS	:	(	' '
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

CHARACTER
	:	'\'' ( ESC | ~('\''|'\n'|'\r'|'\\') ) '\''
	;

STRING
	:	'"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"'
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

SLCOMMENT
	:	"//"
		(~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
		{
            $setType(Token.SKIP); 
            newline();
        }
	;


ML_COMMENT
	:	"/*"
		(	
			options {
				generateAmbigWarnings=false;
			}
		:
			{ LA(2)!='/' }? '*'
		|	'\r' '\n'		{newline();}
		|	'\r'			{newline();}
		|	'\n'			{newline();}
		|	~('*'|'\n'|'\r')
		)*
		"*/"
		{$setType(Token.SKIP);}
	;

ID
    options{ testLiterals = true; }   
    :   ('a'..'z' | 'A'..'Z') 
        ( 
            ('a'..'z' | 'A'..'Z') 
        |   ('0'..'9') 
        |   UNDERSCORE 
        |   ( MINUS ('a'..'z' | 'A'..'Z') ) 
        )*  
    ;

NUM_INT
	{boolean isDecimal=false; Token t=null;}
    :   DOT
            (	('0'..'9')+ (EXPONENT)? (f1:FLOAT_SUFFIX {t=f1;})?
                {
				if (t != null && t.getText().toUpperCase().indexOf('F')>=0) {
                	_ttype = NUM_FLOAT;
				}
				else {
                	_ttype = NUM_DOUBLE; // assume double
				}
				}
            )?

	|	(	'0' {isDecimal = true;} // special case for just '0'
			(	('x'|'X')
				(											// hex
					// the 'e'|'E' and float suffix stuff look
					// like hex digits, hence the (...)+ doesn't
					// know when to stop: ambig.  ANTLR resolves
					// it correctly by matching immediately.  It
					// is therefor ok to hush warning.
					options {
						warnWhenFollowAmbig=false;
					}
				:	HEX_DIGIT
				)+

			|	//float or double with leading zero
				(('0'..'9')+ ('.'|EXPONENT|FLOAT_SUFFIX)) => ('0'..'9')+

			|	('0'..'7')+									// octal
			)?
		|	('1'..'9') ('0'..'9')*  {isDecimal=true;}		// non-zero decimal
		)
		(	('l'|'L') { _ttype = NUM_LONG; }

		// only check to see if it's a float if looks like decimal so far
		|	{isDecimal}?
            (   '.' ('0'..'9')* (EXPONENT)? (f2:FLOAT_SUFFIX {t=f2;})?
            |   EXPONENT (f3:FLOAT_SUFFIX {t=f3;})?
            |   f4:FLOAT_SUFFIX {t=f4;}
            )
            {
			if (t != null && t.getText().toUpperCase() .indexOf('F') >= 0) {
                _ttype = NUM_FLOAT;
			}
            else {
	           	_ttype = NUM_DOUBLE; // assume double
			}
			}
        )?
	;

protected MINUS         :   '-' ;
protected PLUS          :   '+' ;
protected QUOTE         :   '\''    ;
protected EXPONENT      :   ('e'|'E') ( PLUS | MINUS )? ('0'..'9')+  ;
protected DOT           :   '.' ;
protected FLOAT_SUFFIX	:	'f'|'F'|'d'|'D'	;

