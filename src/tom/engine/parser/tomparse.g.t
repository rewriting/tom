
/*
 * this file contains the lexer and parser for
 * tom constructs
 */

header{
    import aterm.*;
    import aterm.pure.*;
    
    import jtom.*;
    import jtom.tools.*;
    import jtom.adt.tomsignature.*;
    import jtom.adt.tomsignature.types.*;

    import antlr.*;
}

class NewTomParser extends Parser;

options{
    k=2; // the lookahead value during parsing
}



    {
    //--------------------------
    %include{TomSignature.tom}
    //--------------------------

    private final Factory getTomSignatureFactory(){
        return tsf();
    }

    private TomEnvironment environment() {
        return TomEnvironment.getInstance();
    }

    private Factory tsf(){
        return environment().getTomSignatureFactory();
    }
    
    private jtom.tools.ASTFactory ast() {
        return environment().getASTFactory();
    }

    private SymbolTable symbolTable() {
        return environment().getSymbolTable();
    }
    
    private String filename;

    // the default-mode parser
    private NewTargetParser targetparser;
    
    public NewTomParser(ParserSharedInputState state, NewTargetParser target, String filename){
        this(state);
        this.filename = filename;
        this.targetparser = target;
        symbolTable().init();
    }

    // creation of a tom variable : doesn't need a rule
    public void variable(){
        // creer la structure ici
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
                mp = patternAction() {result += mp + "\n";}
            )* 
            RBRACE {result += "}";}
            { 
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
                TargetLanguage action = targetparser.goalLanguage();
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
            TargetLanguage vasCode = targetparser.goalLanguage(); 
            }
        // faire les action adequates ici
    ;

/*
 * The %rule construct
 */
ruleConstruct 
{
    String result = "", a1,a2,a3,a4,a5,a6, pt;
}
    :
        LBRACE
        (
            a1 = annotedTerm() {result += a1;}
            ( ALTERNATIVE a2 = annotedTerm() {result += "|"+a2;} )* 
            ARROW pt = plainTerm() {result += " -> "+pt; } 
            (
                WHERE a3 = annotedTerm() AFFECT a4 = annotedTerm() {result += "\n where"+a3+":="+a4;}
            |   IF a5 = annotedTerm() DOUBLEEQ a6 = annotedTerm() {result += "\n if"+a5+"=="+a6;}
            )*
            {result += "\n";}
        )*
        RBRACE
        {
            /*
             * %rule finished. go back in target parser.
             */
            Main.selector.pop();
        }
    ;

/*
 * terms for %match and %rule
 */
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
    String v = null, p = null, s = null, sl = null, a = null,
    sl2 = null, a2 = null;
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
            s = headSymbol() { result = s;} 

        |   // f(...) or f[...]
            sl = headSymbol() a = args()  
            {
                result = sl + a;               
            }

        |   // (f|g...) 
            // ambiguity with the last rule so use syntactic predicat
            // (headSymbolList()) => headSymbolList()
            (headSymbolList()) => sl2 = headSymbolList() ((args() ) => a2 = args() )?
            {result = sl2 + a2;}
            
        |   // (...)
            result = args()
        )
    ;

args returns [String result]
{
    result = null;
    String tl = null, pl = null;
}
    :   (
            LPAREN ( tl = termList() )? RPAREN 
            {
                if(tl != null)
                    result = "("+tl+")";
                else
                    result = "()";
            }
        |   LBRACKET ( pl = pairList() )? RBRACKET
            {
                if(pl != null)
                    result = "["+pl+"]";
                else
                    result = "[]"; 
            }
        )
    ;

termList returns [String result]
{
    result = null;
    String a = null;
}
    :   (
            result = annotedTerm() ( COMMA a = annotedTerm() {result += ","+a;})*
        )
    ;

pairList  returns [String result]
{
    result = null;
    String a = null, a1 = null, a2 = null;
}
    :   (
            result = annotedTerm EQUAL a = annotedTerm {result += "=" + a;}
            ( COMMA a1 = annotedTerm EQUAL a2 = annotedTerm {result += ","+a1+"="+a2;})*
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
{ result = null; 
}
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
        
        )
    ;

operator
{
    Option ot = null;
    Declaration result = null;
}
    :
        type:ID name:ID 
        {ot = `OriginTracking(Name(name.getText()),name.getLine(),Name(filename));} 
        (
            LPAREN 
            ( n:ID COLON )? 
            typeArg:ID 

            ( COMMA ( ID COLON )? ID )*
            RPAREN
        )?
        {
            result = `SymbolDecl(Name(name.getText()));
            printRes(result);
        }
        LBRACE
        keywordFsym()
        (
           keywordMake()  | keywordGetSlot() | keywordIsFsym()
        )*
        RBRACE 
        { 
            Main.selector.pop(); 
        }
    ;

operatorList
{
    Declaration result = null;
}
    :
        ID name:ID
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
            result = `ListSymbolDecl(Name(name.getText()));
            printRes(result);

            Main.selector.pop(); 
        }
    ;

operatorArray
{
    Declaration result = null;
}
    :
        ID name:ID
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
            result = `ArraySymbolDecl(Name(name.getText()));
            printRes(result);

            Main.selector.pop(); 
        }
    ;

keywordMakeEmptyList
    :
        MAKE_EMPTY (LPAREN RPAREN)? 
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage();
            Main.selector.pop();
        }
    ;

keywordMakeAddList
    :
        MAKE_INSERT LPAREN ID COMMA ID RPAREN
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage();
            Main.selector.pop();
        }
    ;

keywordMakeEmptyArray
    :
        MAKE_EMPTY LPAREN ID RPAREN
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage();
            Main.selector.pop();
        }
    ;   

keywordMakeAddArray
    :
        MAKE_APPEND LPAREN ID COMMA ID RPAREN
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage();
            Main.selector.pop();
        }
    ;

keywordFsym
    :
        FSYM 
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage();
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
                Main.selector.pop();
            }
           
        )
    ;
 
keywordGetSlot
    :
        GET_SLOT LPAREN ID COMMA ID RPAREN
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage();
            Main.selector.pop();
        }
    ;

keywordIsFsym
    :
        IS_FSYM LPAREN ID RPAREN
       {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage();
            Main.selector.pop();
        }
    ;

include
    :   {targetparser.goalLanguage();}
    ;

keywordGetFunSym [String type] returns [Declaration result]
{
    result = null;
    Option ot = null;
    TargetLanguage tlCode;
}
    :
        (
            t:GET_FUN_SYM 
            {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
            LPAREN name:ID RPAREN
            {
                Main.selector.push("targetlexer");
                tlCode = targetparser.goalLanguage();
                Main.selector.pop();

                Option info = `OriginTracking(Name(name.getText()),name.getLine(),Name(filename));
                OptionList option = `concOption(info);
                result = `GetFunctionSymbolDecl(
                    Variable(option,Name(name.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode,ot);
            }
        )
        
    ;

keywordGetSubterm[String type] returns [Declaration result]
{
    result = null;
    Option ot = null;
    TargetLanguage tlCode = null;
}
    :
        (
            t:GET_SUBTERM 
            {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
            LPAREN i1:ID COMMA i2:ID RPAREN
            {
                Option info1 = `OriginTracking(Name(i1.getText()),i1.getLine(),Name(filename));
                Option info2 = `OriginTracking(Name(i2.getText()),i2.getLine(),Name(filename));
                OptionList option1 = `concOption(info1);
                OptionList option2 = `concOption(info2);
                
                Main.selector.push("targetlexer");
                tlCode = targetparser.goalLanguage();
                Main.selector.pop(); 

                result = `GetSubtermDecl(
                    Variable(option1,Name(i1.getText()),TomTypeAlone(type),emptyConstraintList()),
                    Variable(option2,Name(i2.getText()),TomTypeAlone("int"),emptyConstraintList()),
                    tlCode, ot);
            }
        )
        
    ;

keywordCmpFunSym [String type] returns [Declaration result]
{
    result = null;
    Option ot = null;
    TargetLanguage tlCode = null;
}
    :
        (
            t:CMP_FUN_SYM 
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
            LPAREN i1:ID COMMA i2:ID RPAREN
            {
                Option info1 = `OriginTracking(Name(i1.getText()),i1.getLine(),Name(filename));
                Option info2 = `OriginTracking(Name(i2.getText()),i2.getLine(),Name(filename));
                OptionList option1 = `concOption(info1);
                OptionList option2 = `concOption(info2);
                
                Main.selector.push("targetlexer");
                tlCode = targetparser.goalLanguage();
                Main.selector.pop(); 

                result = `CompareFunctionSymbolDecl(
                    Variable(option1,Name(i1.getText()),TomTypeAlone(type),emptyConstraintList()),
                    Variable(option2,Name(i2.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode, ot);
            }
        )
    ;

keywordEquals[String type] returns [Declaration result]
{
    result = null;
    Option ot = null;
    TargetLanguage tlCode = null;
}
    :
        (
            t:EQUALS 
            {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
            LPAREN i1:ID COMMA i2:ID RPAREN
            {
                Option info1 = `OriginTracking(Name(i1.getText()),i1.getLine(),Name(filename));
                Option info2 = `OriginTracking(Name(i2.getText()),i2.getLine(),Name(filename));
                OptionList option1 = `concOption(info1);
                OptionList option2 = `concOption(info2);
                
                Main.selector.push("targetlexer");
                tlCode = targetparser.goalLanguage();
                Main.selector.pop();  
                
                result = `TermsEqualDecl(
                    Variable(option1,Name(i1.getText()),TomTypeAlone(type),emptyConstraintList()),
                    Variable(option2,Name(i2.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode, ot);
            }
        )
    ;

keywordGetHead[String type] returns [Declaration result]
{
    result = null;
    Option ot = null;
    TargetLanguage tlCode = null;
}
    :
        (
            t:GET_HEAD 
            {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
            LPAREN name:ID RPAREN
            {
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),Name(filename));
                OptionList option = `concOption(info);

                Main.selector.push("targetlexer");
                tlCode = targetparser.goalLanguage();
                Main.selector.pop();  

                result = `GetHeadDecl(
                    symbolTable().getUniversalType(),
                    Variable(option,Name(name.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode,
                    ot);
            }
        )
    ;

keywordGetTail[String type] returns [Declaration result]
{
    result = null;
    Option ot = null;
    TargetLanguage tlCode = null;
}
    :
        (
            t:GET_TAIL 
            {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
            LPAREN name:ID RPAREN
            {
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),Name(filename));
                OptionList option = `concOption(info);

                Main.selector.push("targetlexer");
                tlCode = targetparser.goalLanguage();
                Main.selector.pop();  

                result = `GetTailDecl(
                    Variable(option,Name(name.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode,
                    ot);
            }
        )
    ;

keywordIsEmpty[String type] returns [Declaration result]
{
    result = null;
    Option ot = null;
    TargetLanguage tlCode = null;
}
    :
        (
            t:IS_EMPTY 
            {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}   
            LPAREN name:ID RPAREN
            {
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),Name(filename));
                OptionList option = `concOption(info);

                Main.selector.push("targetlexer");
                tlCode = targetparser.goalLanguage();
                Main.selector.pop(); 

                result = `IsEmptyDecl(
                    Variable(option,Name(name.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode,
                    ot); 
            }
        )
    ;

keywordImplement
    :
        (
            IMPLEMENT
            {
                Main.selector.push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage();
                Main.selector.pop();  
            }
        )
    ;

keywordGetElement[String type] returns [Declaration result]
{
    result = null;
    Option ot = null;
    TargetLanguage tlCode = null;;
}
    :
        (
            t:GET_ELEMENT 
            {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
            LPAREN name1:ID COMMA name2:ID RPAREN
            {
                Option info1 = `OriginTracking(Name(name1.getText()),name1.getLine(),Name(filename));
                Option info2 = `OriginTracking(Name(name2.getText()),name2.getLine(),Name(filename));
                OptionList option1 = `concOption(info1);
                OptionList option2 = `concOption(info2);
                
                Main.selector.push("targetlexer");
                tlCode = targetparser.goalLanguage();
                Main.selector.pop();  
                
                result = `GetElementDecl(
                    Variable(option1,Name(name1.getText()),TomTypeAlone(type),emptyConstraintList()),
                    Variable(option2,Name(name2.getText()),TomTypeAlone("int"),emptyConstraintList()),
                    tlCode, ot);
            }
        )
    ;

keywordGetSize[String type] returns [Declaration result]
{
    result = null;
    Option ot = null;
    TargetLanguage tlCode;
}
    :
        (
            t:GET_SIZE
            {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
            LPAREN name:ID RPAREN
            {
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),Name(filename));
                OptionList option = `concOption(info);

                Main.selector.push("targetlexer");
                tlCode = targetparser.goalLanguage();
                Main.selector.pop();  

                result = `GetSizeDecl(
                    Variable(option,Name(name.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode,ot);
            }
        )
    ;

typeTerm
{
    Declaration result = null;
    Option ot = null;
    TomList blockList = `emptyTomList();
    Declaration attribute = null;
}
    :   (
            type:ID 
            { 
                ot = `OriginTracking(Name(type.getText()), type.getLine(),Name(filename));
            }
            LBRACE

            keywordImplement()
            (
                attribute = keywordGetFunSym[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            |   attribute = keywordGetSubterm[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            |   attribute = keywordCmpFunSym[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            |   attribute = keywordEquals[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            )*
            RBRACE
            
        )
        {
            result = `TypeTermDecl(Name(type.getText()),blockList,ot);
            printRes(result);

            Main.selector.pop();
        }
    ;

typeList
{
    Option ot = null;
    Declaration attribute = null, result = null;
    TomList blockList = `emptyTomList();
}
    :   (
            type:ID
            {ot = `OriginTracking(Name(type.getText()),type.getLine(),Name(filename));}
            LBRACE
            keywordImplement()
            (
                attribute = keywordGetFunSym[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            |   attribute = keywordGetSubterm[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            |   attribute = keywordCmpFunSym[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            |   attribute = keywordEquals[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            |   attribute = keywordGetHead[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            |   attribute = keywordGetTail[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            |   attribute = keywordIsEmpty[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            )*
            RBRACE

        )
        {
            result = `TypeListDecl(Name(type.getText()),blockList,ot);
            printRes(result);

            Main.selector.pop();
        }
    ;

typeArray
{
    Option ot = null;
    Declaration attribute = null, result = null;
    TomList blockList = `emptyTomList();
}
    :   (
            type:ID
            {ot = `OriginTracking(Name(type.getText()),type.getLine(),Name(filename));}
            LBRACE
            keywordImplement()
            (                
                attribute = keywordGetFunSym[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            |   attribute = keywordGetSubterm[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            |   attribute = keywordCmpFunSym[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            |   attribute = keywordEquals[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            |   attribute = keywordGetElement[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}

            |   attribute = keywordGetSize[type.getText()]
                {blockList = `concTomTerm(blockList*,DeclarationToTomTerm(attribute));}
            
            )*
            RBRACE
        )
        {
            result = `TypeArrayDecl(Name(type.getText()),blockList,ot);
            printRes(result);
            
            Main.selector.pop();
            
        }
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

