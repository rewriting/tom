/*
 * this file contains the lexer and parser for
 * tom constructs
 */

class NewTomParser extends Parser;


options{
    k=2; // the lookahead value during parsing
}



{
    // the default-mode parser
    private NewTargetParser targetparser;
    
    public NewTomParser(ParserSharedInputState state, NewTargetParser target){
        this(state);
        targetparser = target;
    }

    // creation of a tom variable : doesn't need a rule
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
        |	t2:CHARACTER {result = t2.getText();}
        |	t3:STRING {result = t3.getText();}
        |	t4:NUM_FLOAT {result = t4.getText();}
        |	t5:NUM_LONG {result = t5.getText();}
        |	t6:NUM_DOUBLE {result = t6.getText();}
        )
        { System.out.println("constante : "+result); }
	;


/*
 * the %match construct : 
 */
matchConstruct 
{ 
    String args = null, mp = null;
    String result = null;
}
	:	(
            LPAREN args = matchArguments() RPAREN {result = "(" + args + ")";}
            LBRACE {result += "{\n";}
            ( 
                mp = patternAction() {result += mp + "\n";
                System.out.println("patternaction finished");}
            )* 
            RBRACE {result += "}";}
            { 
                System.out.println("match : \n" +
                                   "--------------\n" +
                                    result +
                                   "\n--------------");
                /* Match finished : pop the tomlexer and return in
                 * the target parser.  
                 */
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
                /*
                 * actions in target language : call the target lexer and
                 * call the target parser
                 */
                Main.selector.push("targetlexer");
                String action = targetparser.goalLanguage();
                result += action;
                /*
                 * target parser finished : pop the target lexer
                 */
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

/*
 * Vas signature : no parsing. Here, we will just call
 * vas-to-adt.
 */
signature
    :   
        { 
            /*
             * We didn't switch the lexers ! just have to get target code.
             */
            String vasCode = targetparser.goalLanguage(); 
            System.out.println("code to be parsed by VAS :\n --------------------------"
                                +vasCode+
                               "\n--------------------------");}
        // faire les action adequates ici
    ;

/*
 * The %rule construct
 */
ruleConstruct
    :
        {System.out.println("rule begin");}
        LBRACE
        (
            annotedTerm() ( ALTERNATIVE annotedTerm() )* ARROW plainTerm()
            (
                WHERE annotedTerm() AFFECT annotedTerm()
            |   IF annotedTerm() DOUBLEEQ annotedTerm()
            )*
        )*
        RBRACE
        {
            /*
             * %rule finished. go back in target parser.
             */
            Main.selector.pop();System.out.println("rule end");
        }
    ;

/*
 * terms for %match and %rule
 */
annotedTerm returns [String result]
{
    result = "";
    String pt = null;
    System.out.println("--- annotedTerm");
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
    System.out.println("--- plainTerm");
}
    :  
        (   // xml is missing
            // var* or _*
            v = variableStar() {result = v;}
        |   // _
            p = placeHolder() {result = p;}
        |   // for a single constant. 
            // ambiguous with the next rule so :
            {LA(2) != LPAREN && LA(2) != LBRACKET}? 
            headSymbol() { } 
        |   // f(...) or f[...]
            headSymbol() args()  {System.out.println("--- args or not");  }
            // (f|g...) 
            // ambiguity with the last rule so use syntactic predicat
            // (headSymbolList()) => headSymbolList()
        |   (headSymbolList()) => headSymbolList() ((args() ) => args() )?
            // (...)
        |   args()
        )
    ;

args 
    {System.out.println("args");}
    :   (
            LPAREN ( termList() )? RPAREN
        |   LBRACKET ( pairList() )? RBRACKET
        )
    ;

termList
    :   (
            annotedTerm() ( COMMA annotedTerm() )*
        )
    ;

pairList
    :   (
            annotedTerm EQUAL annotedTerm ( COMMA annotedTerm EQUAL annotedTerm )*
        )
;
          
variableStar returns [String result]
{ result = null; 
    System.out.println("--- variableStar");}
    :   (
            ( 
                i:ID {result = i.getText();}
            |   u:UNDERSCORE {result = u.getText();}
            ) 
            STAR {result += "*";}
        )
    ;

placeHolder returns [String result]
{ result = null; 
    System.out.println("--- placeHolder");}
    :   (
            UNDERSCORE {result = "_";} 
        )
    ;

headSymbolList returns [String result]
{ 
    result = null;
    String s1 = null, s2 = null, s3 = null;
    System.out.println("--- headSymbolList");
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
    System.out.println("--- headSymbol");
}
    :   (
            i:ID {result = i.getText();}
        |   cst = constant() {result = cst;}
        
        )
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
        MAKE_EMPTY (LPAREN RPAREN)? 
        {
            Main.selector.push("targetlexer");
            String tlCode = targetparser.goalLanguage();
            System.out.println("--- makeEmptyList : "+tlCode);
            Main.selector.pop();
        }
    ;

keywordMakeAddList
    :
        MAKE_INSERT LPAREN ID COMMA ID RPAREN
        {
            Main.selector.push("targetlexer");
            String tlCode = targetparser.goalLanguage();
            System.out.println("--- makeInsert : "+tlCode);
            Main.selector.pop();
        }
    ;

keywordMakeEmptyArray
    :
        MAKE_EMPTY LPAREN ID RPAREN
        {
            Main.selector.push("targetlexer");
            String tlCode = targetparser.goalLanguage();
            System.out.println("--- makeEmptyarray : "+tlCode);
            Main.selector.pop();
        }
    ;   

keywordMakeAddArray
    :
        MAKE_APPEND LPAREN ID COMMA ID RPAREN
        {
            Main.selector.push("targetlexer");
            String tlCode = targetparser.goalLanguage();
            System.out.println("--- makeaddarray : "+tlCode);
            Main.selector.pop();
        }
    ;

keywordFsym
    :
        FSYM 
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
            MAKE ( LPAREN ( ID ( COMMA ID )* )? RPAREN )?
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
        GET_SLOT LPAREN ID COMMA ID RPAREN
        {
            Main.selector.push("targetlexer");
            String tlCode = targetparser.goalLanguage();
            System.out.println("--- get_slot : "+tlCode);
            Main.selector.pop();
        }
    ;

keywordIsFsym
    :
        IS_FSYM LPAREN ID RPAREN
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
            GET_FUN_SYM LPAREN ID RPAREN
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
            GET_SUBTERM LPAREN ID COMMA ID RPAREN
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
            CMP_FUN_SYM LPAREN ID COMMA ID RPAREN
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
            EQUALS LPAREN ID COMMA ID RPAREN
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
            GET_HEAD LPAREN ID RPAREN
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
            GET_TAIL LPAREN ID RPAREN
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
            IS_EMPTY LPAREN ID RPAREN
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
            IMPLEMENT
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
            GET_ELEMENT LPAREN ID COMMA ID RPAREN
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
            GET_SIZE LPAREN ID RPAREN
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
	k=3; // default lookahead
    charVocabulary = '\u0000'..'\uffff'; // each character can be read
    testLiterals = false;
}

tokens{
    WHERE="where";
    IF="if";
    MAKE_EMPTY = "make_empty";
    MAKE_INSERT = "make_insert";
    MAKE_APPEND = "make_append";
    FSYM = "fsym";
    MAKE = "make";
    GET_SLOT = "get_slot";
    IS_FSYM = "is_fsym";
    GET_FUN_SYM = "get_fun_sym";
    GET_SUBTERM = "get_subterm";
    CMP_FUN_SYM = "cmp_fun_sym";
    EQUALS = "equals";
    GET_HEAD = "get_head";
    GET_TAIL = "get_tail";
    IS_EMPTY = "is_empty";
    IMPLEMENT = "implement";
    GET_ELEMENT = "get_element";
    GET_SIZE = "get_size";
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
AFFECT      :   ":="    ;
DOUBLEEQ    :   "=="    ;
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

