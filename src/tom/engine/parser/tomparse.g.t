
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
        
    private String filename;

    // the default-mode parser
    private NewTargetParser targetparser;

    private StringBuffer text = new StringBuffer("");
    
    private int lastLine; 

    private TomList debuggedStructureList = null;

    public NewTomParser(ParserSharedInputState state, NewTargetParser target, String filename){
        this(state);
        this.filename = filename;
        this.targetparser = target;

        this.debuggedStructureList = `emptyTomList();
    }



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
    
    private TomTaskInput getInput() {
        return TomTaskInput.getInstance();
    }

    public TomStructureTable getStructTable() {
        return `StructTable(debuggedStructureList);
    }



    private void pushLine(int line){
        targetparser.pushLine(line);
    }

    private void pushColumn(int column){
        targetparser.pushColumn(column);
    }

    private void setLastLine(int line){
        lastLine = line;
    }

    // creation of a tom variable : doesn't need a rule
    public void variable(){
        // creer la structure ici
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
matchConstruct [Option ot] returns [Instruction result]
{ 
    result = null;
    OptionList optionList = `concOption(ot);
    StringBuffer debugKey = new StringBuffer(filename + ot.getLine());

    LinkedList argumentList = new LinkedList();
    LinkedList patternActionList = new LinkedList();
}
	:	(
            LPAREN matchArguments[argumentList] RPAREN 
            LBRACE 
            ( 
                patternAction[patternActionList,debugKey] 
            )* 
            t:RBRACE 
            { 
                result = `Match(
                    SubjectList(ast().makeList(argumentList)),
                    PatternList(ast().makeList(patternActionList)),
                    optionList
                );
                
                if (getInput().isDebugMode()){
                    debuggedStructureList = (TomList) debuggedStructureList.append(result);
                }
                
                // update for new target block...
                pushLine(t.getLine());
                pushColumn(t.getColumn());
                
                // Match finished : pop the tomlexer and return in
                // the target parser.  
                Main.selector.pop(); 
            }
        )
	;

matchArguments [LinkedList list]
    :   
        (
            matchArgument[list] ( COMMA matchArgument[list] )*
        )
    ;

matchArgument [LinkedList list]
    :   
        (
            type:ID name:ID 
        )
        {
            list.add(`TLVar(name.getText(),TomTypeAlone(type.getText())));
        }
        
    ;

patternAction [LinkedList list, StringBuffer debugKey]
{
    LinkedList matchPatternList = new LinkedList();
    LinkedList listOfMatchPatternList = new LinkedList();
    LinkedList listTextPattern = new LinkedList();
    LinkedList listOrgTrackPattern = new LinkedList();
    LinkedList blockList = new LinkedList();

    Option option = null;

    clearText();
}
    :   (
            ( label:ID COLON )?
             option = matchPattern[matchPatternList] 
            {
                listOfMatchPatternList.add(ast().makeList(matchPatternList));
                matchPatternList.clear();
                listTextPattern.add(text.toString());
                clearText();
                listOrgTrackPattern.add(option);
            }
            ( 
                ALTERNATIVE matchPattern[matchPatternList] 
                {
                    listOfMatchPatternList.add(ast().makeList(matchPatternList));
                    matchPatternList.clear();
                    listTextPattern.add(text.toString());
                    clearText();
                    listOrgTrackPattern.add(option);
                }
            )* 
            ARROW t:LBRACE
            {
                pushLine(t.getLine());
                pushColumn(t.getColumn());
                
                if(getInput().isDebugMode()) {
                    blockList.add(`ITL(
                            "jtom.debug.TomDebugger.debugger.patternSuccess(\""
                            +debugKey
                            +"\");\n")
                    );
                    if(getInput().isDebugMemory()) {
                        blockList.add(`ITL(
                                "jtom.debug.TomDebugger.debugger.emptyStack();\n")
                        );
                    }
                }

                // actions in target language : call the target lexer and
                // call the target parser
                Main.selector.push("targetlexer");
                //                TargetLanguage tlCode = targetparser.goalLanguage(blockList);
                TargetLanguage tlCode = targetparser.targetLanguage(blockList);
                
                // target parser finished : pop the target lexer
                Main.selector.pop();

                blockList.add(tlCode);
                OptionList optionList = `emptyOptionList();
                
                if(label != null){
                    optionList = `concOption(Label(Name(label.getText())));
                }

                TomList patterns = null;
                String patternText = null;
                for(int i=0 ;  i<listOfMatchPatternList.size() ; i++) {
                    patterns = (TomList) listOfMatchPatternList.get(i);
                    patternText = (String) listTextPattern.get(i);

                    //TODO solve with xmlterm
                    //if (patternText == null) patternText = "";
                    
                    optionList = `concOption(
                        optionList*, 
                        (Option) listOrgTrackPattern.get(i),
                        OriginalText(Name(patternText))
                    );
                    
                    list.add(`PatternAction(
                            TermList(patterns),
                            AbstractBlock(ast().makeInstructionList(blockList)),
                            optionList)
                    );
                }
            }
        )
    ;

matchPattern [LinkedList list] returns [Option result]
{
    result = null;
    TomTerm term = null;
}
    :   (
             term = annotedTerm 
            {
                list.add(term);
                result = `OriginTracking(Name("Pattern"),lastLine,Name(filename));
            }
            ( 
                COMMA {text.append('\n');}  
                term = annotedTerm {list.add(term);}
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
ruleConstruct [Option orgTrack] returns [Instruction result]
{
    result = null;
    TomRuleList ruleList = `emptyTomRuleList();
    TomTerm lhs = null, rhs = null, pattern = null, subject = null;
    TomList listOfLhs = `emptyTomList();
    InstructionList conditionList = `emptyInstructionList();
    TomName orgText = null;

    clearText();
}
    :
        LBRACE
        (
            lhs = annotedTerm 
            {listOfLhs = `concTomTerm(lhs);}
            ( ALTERNATIVE {text.append('|');} lhs = annotedTerm() 
                {listOfLhs = `concTomTerm(listOfLhs*,lhs);} 
            )*
 
            ARROW {orgText = `Name(text.toString());} rhs = plainTerm[null,0]
            (
                WHERE pattern = annotedTerm AFFECT subject = annotedTerm 
                {conditionList = `concInstruction(conditionList*, MatchingCondition(pattern,subject));}
            |   IF pattern = annotedTerm DOUBLEEQ subject = annotedTerm
                {conditionList = `concInstruction(conditionList*, EqualityCondition(pattern,subject));}
            )*
            
            {
                int line = lastLine;
                //int line = ((NewTomLexer) Main.selector.getCurrentStream()).getLine();
                Option ot = `OriginTracking(
                    Name("Pattern"),
                    line,
                    Name(filename)
                );
                OptionList optionList = `concOption(ot,OriginalText(orgText));
                
                while(! listOfLhs.isEmpty()){
                    ruleList = (TomRuleList) ruleList.append(
                        `RewriteRule(
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

            result = `RuleSet(ruleList,orgTrack);

            if(getInput().isDebugMode()) {
                debuggedStructureList = (TomList) debuggedStructureList.append(result);
            }
            
            // %rule finished. go back in target parser.
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
    LinkedList constraintList = new LinkedList();
    LinkedList optionList = new LinkedList();
    LinkedList secondOptionList = new LinkedList();
    TomTerm term = null;
    NameList nameList = `emptyNameList();
    TomName name = null;
    LinkedList list = new LinkedList();
    boolean implicit = false;
    boolean withArgs = false;

    Constraint annotedName = 
    (astAnnotedName == null)?null:ast().makeAssignTo(astAnnotedName, line, filename);
    if(annotedName != null)
        constraintList.add(annotedName);
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
                        ast().makeList(list),
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
                        ast().makeList(list),
                        ast().makeConstraintList(constraintList)
                    );
                else 
                    result = `Appl(
                        ast().makeOptionList(optionList),
                        nameList,
                        ast().makeList(list),
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
                        ast().makeList(list),
                        ast().makeConstraintList(constraintList)
                    );
                else 
                    result = `Appl(
                        ast().makeOptionList(optionList),
                        nameList,
                        ast().makeList(list),
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
                    ast().makeList(list),
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
            t2:RPAREN 
            {
                setLastLine(t2.getLine());
                text.append(t2.getText());
            
                result = false;
                optionList.add(`OriginTracking(Name(""),t1.getLine(),Name(filename)));
            }
            
        |   t3:LBRACKET {text.append('[');} 
            ( pairList[list] )? 
            t4:RBRACKET 
            {
                setLastLine(t4.getLine());
                text.append(t4.getText());
                
                result = true;
                optionList.add(`OriginTracking(Name(""),t3.getLine(),Name(filename)));
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
                text.append('=');
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
            t:STAR 
            {
                text.append(name);
                text.append(t.getText());
                setLastLine(t.getLine());
                
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
                text.append(t.getText());
                setLastLine(t.getLine());

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
    result = `emptyNameList();
    TomName name = null;
}
    :  
        (
            LPAREN {text.append('(');}
            name = headSymbol[optionList] {result = `concTomName(result*,name);}
            ALTERNATIVE {text.append('|');}
            name = headSymbol[optionList] {result = `concTomName(result*,name);}
            ( 
                ALTERNATIVE {text.append('|');} 
                name = headSymbol[optionList] {result = `concTomName(result*,name);})* 
            t:RPAREN 
            {
                text.append(t.getText());
                setLastLine(t.getLine());                
            }
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
            
            setLastLine(line);
            result = `Name(name);
            optionList.add(`OriginTracking(result,line, Name(filename)));
        }
    ;



/*
 * Operator Declaration
 */

operator returns [Declaration result]
{
    result=null;
    Option ot = null;
    TomList blockList = `emptyTomList();
    TomList options = `emptyTomList();
    TomName astName = null;
    String stringSlotName;
    Declaration attribute;
}
    :
        type:ID name:ID 
        {
            astName = `Name(name.getText());
            ot = `OriginTracking(Name(name.getText()),name.getLine(),Name(filename));
        }
        (
            LPAREN 
            (
                slotName:ID COLON
                {
                    stringSlotName = slotName.getText();
                }
            )? 
            typeArg:ID 

            ( COMMA ( slotName2:ID COLON )? ID )*
            RPAREN
        )?
        LBRACE
        keywordFsym()
        {
            astName = `Name(name.getText());
        }
        (
            attribute = keywordMake[name.getText(), type.getText()]
            {options = `concTomTerm(options*,DeclarationToTomTerm(attribute));}

        | attribute = keywordGetSlot[astName,type.getText()]
            {options = `concTomTerm(options*,DeclarationToTomTerm(attribute));}

        | attribute = keywordIsFsym[astName,type.getText()]
            {options = `concTomTerm(options*,DeclarationToTomTerm(attribute));}
        )*
        t:RBRACE
        { 
            result = `SymbolDecl(astName);

            pushLine(t.getLine());
            pushColumn(t.getColumn());

            Main.selector.pop(); 
        }
    ;

operatorList returns [Declaration result]
{
    result = null;
    Declaration attribute = null;  
}
    :
        type:ID name:ID
        LPAREN typeArg:ID STAR RPAREN
        LBRACE
        keywordFsym()
        (
            attribute = keywordMakeEmptyList[name.getText()]

        |   attribute = keywordMakeAddList[name.getText(),type.getText(),typeArg.getText()]

        |   attribute = keywordIsFsym[`Name(name.getText()), type.getText()]
        )*
        t:RBRACE
        { 
            result = `ListSymbolDecl(Name(name.getText()));

            pushLine(t.getLine());
            pushColumn(t.getColumn());

            Main.selector.pop(); 
        }
    ;

operatorArray returns [Declaration result]
{
    result = null;
    Declaration attribute = null;
}
    :
        type:ID name:ID
        LPAREN typeArg:ID STAR RPAREN
        LBRACE
        keywordFsym()
        (
            attribute = keywordMakeEmptyArray[name.getText(),type.getText()]

        |   attribute = keywordMakeAddArray[name.getText(),type.getText(),typeArg.getText()]

        |   attribute = keywordIsFsym[`Name(name.getText()),type.getText()]
        )*
        t:RBRACE
        { 
            result = `ArraySymbolDecl(Name(name.getText()));

            pushLine(t.getLine());
            pushColumn(t.getColumn());

            Main.selector.pop(); 
        }
    ;

/*
 * Type Declaration
 */

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

/*
 * Keywords
 */

keywordMakeEmptyList[String name] returns [Declaration result]
{
    result = null;
    Option ot = null;
}
    :
        t:MAKE_EMPTY
        {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
        (LPAREN RPAREN)?
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            Main.selector.pop();
            result = `MakeEmptyList(Name(name),tlCode,ot);
        }
    ;

keywordMakeAddList[String name, String listType, String elementType] returns [Declaration result]
{
    result = null;
    Option ot = null;
}
    :
        t:MAKE_INSERT
        {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
        LPAREN elementName:ID COMMA listName:ID RPAREN
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            Main.selector.pop();
            
            Option listInfo = `OriginTracking(Name(listName.getText()),listName.getLine(),Name(filename));  
            Option elementInfo = `OriginTracking(Name(elementName.getText()),elementName.getLine(),Name(filename));
            OptionList listOption = `concOption(listInfo);
            OptionList elementOption = `concOption(elementInfo);
            
            result = `MakeAddList(Name(name),
                Variable(elementOption,Name(elementName.getText()),TomTypeAlone(elementType),emptyConstraintList()),
                Variable(listOption,Name(listName.getText()),TomTypeAlone(listType),emptyConstraintList()),
                tlCode,ot);
        }
    ;

keywordMakeEmptyArray[String name, String listType] returns [Declaration result]
{
    result = null;
    Option ot = null;
}
    :
        t:MAKE_EMPTY
        {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
        LPAREN listName:ID RPAREN
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode =  targetparser.goalLanguage(new LinkedList());
            Main.selector.pop();

            Option listInfo = `OriginTracking(Name(listName.getText()),listName.getLine(),Name(filename));  
            OptionList listOption = `concOption(listInfo);

            result = `MakeEmptyArray(Name(name),
                Variable(listOption,Name(listName.getText()),TomTypeAlone(listType),emptyConstraintList()),
                tlCode,ot);
        }
    ;   

keywordMakeAddArray[String name, String listType, String elementType] returns [Declaration result]
{
    result = null;
    Option ot = null;
}
    :
        t:MAKE_APPEND
        {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
        LPAREN elementName:ID COMMA listName:ID RPAREN
        {
            Main.selector.push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            Main.selector.pop();

            Option listInfo = `OriginTracking(Name(listName.getText()),listName.getLine(),Name(filename));  
            Option elementInfo = `OriginTracking(Name(elementName.getText()),elementName.getLine(),Name(filename));
            OptionList listOption = `concOption(listInfo);
            OptionList elementOption = `concOption(elementInfo);
            
            result = `MakeAddArray(Name(name),
                Variable(elementOption,Name(elementName.getText()),TomTypeAlone(elementType),emptyConstraintList()),
                Variable(listOption,Name(listName.getText()),TomTypeAlone(listType),emptyConstraintList()),
                tlCode,ot);
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
keywordMake [String opname, String type] returns [Declaration result]
{
    result = null;
    Option ot = null;
    TomList args = `emptyTomList();
    TargetLanguage tlCode = null;
}
    :
        (
            t:MAKE
            {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}

            ( 
                LPAREN 
                ( 
                    typeArg:ID
                    {
                        Option info1 = `OriginTracking(Name(typeArg.getText()),typeArg.getLine(),Name(filename));  
                        OptionList option1 = `concOption(info1);
                        args = `concTomTerm(
                            Variable(option1,Name(typeArg.getText()),TomTypeAlone(type),emptyConstraintList() )
                        );
                    }
                    ( 
                        COMMA nameArg:ID
                        {                            
                            Option info2 = `OriginTracking(Name(nameArg.getText()),nameArg.getLine(),Name(filename));
                            OptionList option2 = `concOption(info2);
                            args = `concTomTerm(
                                Variable(option2,Name(nameArg.getText()),TomTypeAlone(type),emptyConstraintList() )
                            );
                        }
                    )*
                )? 
                RPAREN )?
            LBRACE
            {
                Main.selector.push("targetlexer");
                tlCode = targetparser.targetLanguage(new LinkedList());
                Main.selector.pop();
                result = `MakeDecl(Name(opname),TomTypeAlone(type),args,tlCode,ot);
            }
           
        )
    ;
 
keywordGetSlot [TomName astName, String type] returns [Declaration result]
{
    result = null;
    Option ot = null;
    TargetLanguage tlCode = null;
}
    :
        (
            t:GET_SLOT
            {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
            LPAREN slotName:ID COMMA name:ID RPAREN
            {                
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),Name(filename));
                OptionList option = `concOption(info);
                
                Main.selector.push("targetlexer");
                tlCode = targetparser.goalLanguage(new LinkedList());
                Main.selector.pop(); 

                result = `GetSlotDecl(astName,
                    Name(slotName.getText()),
                    Variable(option,Name(name.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode, ot);
            }
        )
    ;

keywordIsFsym [TomName astName, String type] returns [Declaration result]
{
    result = null;
    Option ot = null;
    TargetLanguage tlCode = null;
}
    :
        t:IS_FSYM
        {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(filename));}
        LPAREN name:ID RPAREN
        {
            Main.selector.push("targetlexer");
            tlCode = targetparser.goalLanguage(new LinkedList());
            Main.selector.pop();

            Option info = `OriginTracking(Name(name.getText()),name.getLine(),Name(filename));
            OptionList option = `concOption(info);
            result = `IsFsymDecl(astName,
                Variable(option,Name(name.getText()),TomTypeAlone(type),emptyConstraintList()),
                tlCode,ot);
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
            LPAREN name1:ID COMMA name2:ID RPAREN
            {
                Option info1 = `OriginTracking(Name(name1.getText()),name1.getLine(),Name(filename));
                Option info2 = `OriginTracking(Name(name2.getText()),name2.getLine(),Name(filename));
                OptionList option1 = `concOption(info1);
                OptionList option2 = `concOption(info2);
                
                Main.selector.push("targetlexer");
                tlCode = targetparser.goalLanguage(new LinkedList());
                Main.selector.pop(); 

                result = `GetSubtermDecl(
                    Variable(option1,Name(name1.getText()),TomTypeAlone(type),emptyConstraintList()),
                    Variable(option2,Name(name2.getText()),TomTypeAlone("int"),emptyConstraintList()),
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
            LPAREN name1:ID COMMA name2:ID RPAREN
            {
                Option info1 = `OriginTracking(Name(name1.getText()),name1.getLine(),Name(filename));
                Option info2 = `OriginTracking(Name(name2.getText()),name2.getLine(),Name(filename));
                OptionList option1 = `concOption(info1);
                OptionList option2 = `concOption(info2);
                
                Main.selector.push("targetlexer");
                tlCode = targetparser.goalLanguage(new LinkedList());
                Main.selector.pop(); 

                result = `CompareFunctionSymbolDecl(
                    Variable(option1,Name(name1.getText()),TomTypeAlone(type),emptyConstraintList()),
                    Variable(option2,Name(name2.getText()),TomTypeAlone(type),emptyConstraintList()),
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
            LPAREN name1:ID COMMA name2:ID RPAREN
            {
                Option info1 = `OriginTracking(Name(name1.getText()),name1.getLine(),Name(filename));
                Option info2 = `OriginTracking(Name(name2.getText()),name2.getLine(),Name(filename));
                OptionList option1 = `concOption(info1);
                OptionList option2 = `concOption(info2);
                
                Main.selector.push("targetlexer");
                tlCode = targetparser.goalLanguage(new LinkedList());
                Main.selector.pop();  
                
                result = `TermsEqualDecl(
                    Variable(option1,Name(name1.getText()),TomTypeAlone(type),emptyConstraintList()),
                    Variable(option2,Name(name2.getText()),TomTypeAlone(type),emptyConstraintList()),
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

