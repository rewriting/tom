
/*
 * this file contains the lexer and parser for
 * tom constructs
 */

header{
    import java.util.*;

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

    private StringBuffer text = new StringBuffer("");
    
    public NewTomParser(ParserSharedInputState state, NewTargetParser target, String filename){
        this(state);
        this.filename = filename;
        this.targetparser = target;
        symbolTable().init();
    }

    private void pushLine(int line){
        targetparser.pushLine(line);
    }

    private void pushColumn(int column){
        targetparser.pushColumn(column);
    }

    // creation of a tom variable : doesn't need a rule
    public void variable(){
        // creer la structure ici
    }
    
    private TomList makeTomList(LinkedList list){
        return targetparser.makeTomList(list);
    }
   
    private void clearText(){
        text.delete(0,text.length());
    }

}

constant returns [Token result]
{
    result = null;
}
	:	(
            t1:NUM_INT {result = t1;}
        |	t2:CHARACTER {result = t2;}
        |	t3:STRING {result = t3;}
        |	t4:NUM_FLOAT {result = t4;}
        |	t5:NUM_LONG {result = t5;}
        |	t6:NUM_DOUBLE {result = t6;}
        )
    ;

/*
 * the %match construct : 
 */
matchConstruct 
{ 
    String arg = null, mp = null;
    String result = null;
}
	:	(
            LPAREN arg = matchArguments() RPAREN {result = "(" + arg + ")";}
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
    LinkedList bList = new LinkedList();
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
                //TargetLanguage action = 
                targetparser.goalLanguage(new LinkedList());
//                result += action;
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
             annotedTerm {result = at;}
            ( 
                COMMA  annotedTerm {result += "," + at2;}
            )*
        )
    ;

/*
 * Vas signature : no parsing. Here, we will just call
 * vas-to-adt.
 */
signature
{

    LinkedList bList = new LinkedList();
}
    :   
        { 
            /*
             * We didn't switch the lexers ! just have to get target code.
             */
//            TargetLanguage vasCode = 
            targetparser.goalLanguage(new LinkedList()); 
            }
        // faire les action adequates ici
    ;

/*
 * The %rule construct
 */
ruleConstruct returns [TomRuleList result]
{
    result =  `emptyTomRuleList();
    TomTerm lhs = null, rhs = null, pattern = null, subject = null;
    TomList listOfLhs = `emptyTomList();
    InstructionList conditionList = `emptyInstructionList();
    TomName orgText = null;
    int line = 0;

    clearText();
}
    :
        LBRACE
        (
            {line = ((NewTomLexer) Main.selector.getCurrentStream()).getLine();}
            lhs = annotedTerm {listOfLhs = `concTomTerm(lhs);}
            ( ALTERNATIVE {text.append('|');} lhs = annotedTerm() {listOfLhs = `concTomTerm(listOfLhs*,lhs);} )*
 
            ARROW {orgText = `Name(text.toString());} rhs = plainTerm[null,0]
            (
                WHERE pattern = annotedTerm AFFECT subject = annotedTerm 
                {conditionList = `concInstruction(conditionList*, MatchingCondition(pattern,subject));}
            |   IF pattern = annotedTerm DOUBLEEQ subject = annotedTerm
                {conditionList = `concInstruction(conditionList*, EqualityCondition(pattern,subject));}
            )*
            
            {
                Option ot = `OriginTracking(
                    Name("Pattern"),
                    line,
                    Name(filename)
                );
                OptionList optionList = `concOption(ot,OriginalText(orgText));
                
                while(! listOfLhs.isEmpty()){
                    result = `concTomRule(
                        result*,
                        RewriteRule(
                            Term(listOfLhs.getHead()),
                            Term(rhs),
                            conditionList,
                            optionList
                        )
                    );
                    listOfLhs = listOfLhs.getTail();
                }
                
                conditionList = `emptyInstructionList();
                clearText();
            }
        )*
        t:RBRACE
        {
            
            // update for new target block...
            pushLine(t.getLine());
            pushColumn(t.getColumn());

            /*
             * %rule finished. go back in target parser.
             */
            Main.selector.pop();
        }
    ;

/*
 * terms for %match and %rule
 */
annotedTerm returns [TomTerm result]
{
    result = null;
    TomName annotedName = null;
    int line = 0;
}
    :   (
            ( 
                name:ID AT 
                {
                    text.append(name.getText());
                    text.append('@');
                    annotedName = `Name(name.getText());
                    line = name.getLine();
                }
            )? 
            
            result = plainTerm[annotedName,line] 
        )
    ;

plainTerm [TomName astAnnotedName, int line] returns [TomTerm result]
{
    result = null;
    Constraint annotedName = 
    (astAnnotedName == null)?null:ast().makeAssignTo(astAnnotedName, line, filename);
    LinkedList constraintList = new LinkedList();
    LinkedList optionList = new LinkedList();
    LinkedList secondOptionList = new LinkedList();
    TomTerm term = null;
    NameList nameList = `emptyNameList();
    TomName name = null;
    LinkedList list = new LinkedList();
    boolean implicit = false;
    boolean withArgs = false;
}
    :  
        (   // xml is missing
            // var* or _*
            result = variableStar[optionList,constraintList] 

        |   // _
            result = placeHolder[optionList,constraintList] 

        |   // for a single constant. 
            // ambiguous with the next rule so :
            {LA(2) != LPAREN && LA(2) != LBRACKET}? 
            name = headSymbol[optionList] 
            {
                nameList = `concTomName(nameList*,name);
                result = `Appl(
                        ast().makeOptionList(optionList),
                        nameList,
                        makeTomList(list),
                        ast().makeConstraintList(constraintList)
                    );
            }

        |   // f(...) or f[...]
            name = headSymbol[optionList] {nameList = `concTomName(nameList*,name);}
            implicit = args[list,secondOptionList]
            {
                if(list.isEmpty())
                    optionList.add(`Constructor(nameList));
                if(implicit)
                    result = `RecordAppl(
                        ast().makeOptionList(optionList),
                        nameList,
                        makeTomList(list),
                        ast().makeConstraintList(constraintList)
                    );
                else 
                    result = `Appl(
                        ast().makeOptionList(optionList),
                        nameList,
                        makeTomList(list),
                        ast().makeConstraintList(constraintList)
                    );
            }
            
        |   // (f|g...) 
            // ambiguity with the last rule so use syntactic predicat
            // (headSymbolList()) => headSymbolList()
            (headSymbolList[null]) => nameList = headSymbolList[optionList] 
            ( (args[null,null]) => implicit = args[list,secondOptionList] {withArgs = true;})?
            {
                if(withArgs && list.isEmpty())
                    optionList.add(`Constructor(nameList));
                if(implicit)
                    result = `RecordAppl(
                        ast().makeOptionList(optionList),
                        nameList,
                        makeTomList(list),
                        ast().makeConstraintList(constraintList)
                    );
                else 
                    result = `Appl(
                        ast().makeOptionList(optionList),
                        nameList,
                        makeTomList(list),
                        ast().makeConstraintList(constraintList)
                    );
            }
        |   // (...)
            implicit = args[list,secondOptionList]
            {
                nameList = `concTomName(Name(""));
                optionList.addAll(secondOptionList);
                result = `Appl(
                    ast().makeOptionList(optionList),
                    nameList,
                    makeTomList(list),
                    ast().makeConstraintList(constraintList)
                );
            }
        )
    ;

args [LinkedList list, LinkedList optionList] returns [boolean result]
{
    result = false;
}
    :   (
            t1:LPAREN {text.append('(');} 
            ( termList[list] )? 
            RPAREN {text.append(')');} 
            {
                result = false;
                optionList.add(`OriginTracking(Name(""),t1.getLine(),Name(filename)));
            }
            
        |   t2:LBRACKET {text.append('[');} 
            ( pairList[list] )? 
            RBRACKET {text.append(']');}
            {
                result = true;
                optionList.add(`OriginTracking(Name(""),t2.getLine(),Name(filename)));
            }
        )
    ;

termList [LinkedList list]
{
    TomTerm term = null;
}
    :   (
            term = annotedTerm {list.add(term);}
            ( COMMA {text.append(',');} term = annotedTerm() {list.add(term);})*
        )
    ;

pairList [LinkedList list]
{
    TomTerm term = null;
}
    :   (
            name:ID EQUAL 
            {
                text.append(name.getText());
                {text.append('=');}
            } 
            term = annotedTerm 
            {list.add(`PairSlotAppl(Name(name.getText()),term));}
            ( COMMA {text.append(',');} 
                name2:ID EQUAL 
                {
                    text.append(name2.getText());
                    text.append('=');
                } 
                term = annotedTerm 
                {list.add(`PairSlotAppl(Name(name2.getText()),term));}
            )*
        )
;
          
variableStar [LinkedList optionList, LinkedList constraintList] returns [TomTerm result]
{ 
    result = null; 
    String name = null;
    int line = 0;
    OptionList options = null;
    ConstraintList constraints = null;
}
    :   (
            ( 
                name1:ID 
                {
                    name = name1.getText();
                    line = name1.getLine();
                }
            |   name2:UNDERSCORE 
                {
                    name = name2.getText();
                    line = name2.getLine();
                }
            ) 
            STAR 
            {
                optionList.add(`OriginTracking(Name(name),line,Name(filename)));
                // faire une nouvelle fonction ?
                options = ast().makeOptionList(optionList);
                constraints = ast().makeConstraintList(constraintList);
                if(name1 == null)
                    result = `UnamedVariableStar(
                        options,
                        TomTypeAlone("unknown type"),
                        constraints
                    );
                else
                    result = `VariableStar(
                        options,
                        Name(name),
                        TomTypeAlone("unknown type"),
                        constraints
                    );
            }
        )
    ;

placeHolder [LinkedList optionList, LinkedList constraintList] returns [TomTerm result]
{ 
    result = null;
    OptionList options = null;
    ConstraintList constraints = null;
}
    :   (
            t:UNDERSCORE 
            {
                optionList.add(
                    `OriginTracking(Name(t.getText()),t.getLine(),Name(filename))
                );
                options = ast().makeOptionList(optionList);
                constraints = ast().makeConstraintList(constraintList);
                result = `Placeholder(options, constraints);
            } 
        )
    ;

headSymbolList [LinkedList optionList] returns [NameList result]
{ 
    result = null;
    TomName name = null;
}
    :  
        (
            LPAREN 
            name = headSymbol[optionList] {result = `concTomName(result*,name);}
            ALTERNATIVE name = headSymbol[optionList] {result = `concTomName(result*,name);}
            ( ALTERNATIVE name = headSymbol[optionList] {result = `concTomName(result*,name);})* 
            RPAREN 
        )
    ;

headSymbol [LinkedList optionList] returns [TomName result]
{ 
    result = null; 
    int line = 0;
    String name = null;
    Token t = null;
}
    :   (
            i:ID 
            {
                name = i.getText();
                line = i.getLine();
                text.append(name);
            }
        |   t = constant
            {
                name = t.getText();
                line = t.getLine();
                text.append(name);
            }
        )
        {
            result = `Name(name);
            optionList.add(`OriginTracking(result,line, Name(filename)));
        }
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

            Main.selector.pop(); 
        }
    ;

keywordMakeEmptyList
{
    LinkedList bList = new LinkedList();
}
    :
        MAKE_EMPTY (LPAREN RPAREN)? 
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            Main.selector.pop();
        }
    ;

keywordMakeAddList
{
    LinkedList bList = new LinkedList();
}
    :
        MAKE_INSERT LPAREN ID COMMA ID RPAREN
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            Main.selector.pop();
        }
    ;

keywordMakeEmptyArray
    :
        MAKE_EMPTY LPAREN ID RPAREN
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode =  targetparser.goalLanguage(new LinkedList());
            Main.selector.pop();
        }
    ;   

keywordMakeAddArray
    :
        MAKE_APPEND LPAREN ID COMMA ID RPAREN
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            Main.selector.pop();
        }
    ;

keywordFsym
    :
        FSYM 
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
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
                TargetLanguage tlCode = targetparser.targetLanguage(new LinkedList());
                Main.selector.pop();
            }
           
        )
    ;
 
keywordGetSlot
    :
        GET_SLOT LPAREN ID COMMA ID RPAREN
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            Main.selector.pop();
        }
    ;

keywordIsFsym
    :
        IS_FSYM LPAREN ID RPAREN
       {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            Main.selector.pop();
        }
    ;

include
    :   {targetparser.goalLanguage(new LinkedList());}
    ;

keywordGetFunSym [String type] returns [Declaration result]
{
    result = null;
    Option ot = null;
    TargetLanguage tlCode = null;
}
    :
        (
            t:GET_FUN_SYM 
            {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
            LPAREN name:ID RPAREN
            {
                Main.selector.push("targetlexer");
                tlCode = targetparser.goalLanguage(new LinkedList());
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
                tlCode = targetparser.goalLanguage(new LinkedList());
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
                tlCode = targetparser.goalLanguage(new LinkedList());
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
                tlCode = targetparser.goalLanguage(new LinkedList());
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
                tlCode = targetparser.goalLanguage(new LinkedList());
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
                tlCode = targetparser.goalLanguage(new LinkedList());
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
                tlCode = targetparser.goalLanguage(new LinkedList());
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
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
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
                tlCode = targetparser.goalLanguage(new LinkedList());
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
    TargetLanguage tlCode = null;
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
                tlCode = targetparser.goalLanguage(new LinkedList());
                Main.selector.pop();  

                result = `GetSizeDecl(
                    Variable(option,Name(name.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode,ot);
            }
        )
    ;

typeTerm returns [Declaration result]
{
    result = null;
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
            t:RBRACE
        )
        {
            result = `TypeTermDecl(Name(type.getText()),blockList,ot);

            // update for new target block...
            pushLine(t.getLine());
            pushColumn(t.getColumn());

            // pop the tomlexer and go back to the targetparser
            Main.selector.pop();
        }
    ;

typeList returns [Declaration result]
{
    result = null;
    Option ot = null;
    Declaration attribute = null;
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
            t:RBRACE

        )
        {
            result = `TypeListDecl(Name(type.getText()),blockList,ot);

            pushLine(t.getLine());
            pushColumn(t.getColumn());

            Main.selector.pop();
        }
    ;


typeArray returns [Declaration result]
{
    result=null;
    Option ot = null;
    Declaration attribute = null;
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
            t:RBRACE
        )
        {
            result = `TypeArrayDecl(Name(type.getText()),blockList,ot);
            
            pushLine(t.getLine());
            pushColumn(t.getColumn());

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

