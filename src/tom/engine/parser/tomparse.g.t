/*
 * this file contains the lexer and parser for
 * tom constructs
 */

header{
    package jtom.parser;

    import java.util.*;
    import java.util.logging.*;
    import java.text.*;

    import aterm.*;
    import aterm.pure.*;
    
    import jtom.*;
    import jtom.adt.tomsignature.*;
    import jtom.adt.tomsignature.types.*;
    import jtom.exception.*;
    import jtom.tools.*;

    import antlr.*;
}

class NewTomParser extends Parser;

options{
    k=1; // the lookahead value during parsing
    defaultErrorHandler = false;
}

    {
    //--------------------------
    %include{TomSignature.tom}
    //--------------------------
        
    public String currentFile(){
        return targetparser.getCurrentFile();
    }

    // the default-mode parser
    private NewTargetParser targetparser;
    protected NewBQParser bqparser;

    private Logger logger;

    private StringBuffer text = new StringBuffer("");
    
    private int lastLine; 

    private TomList debuggedStructureList = null;

    public NewTomParser(ParserSharedInputState state, NewTargetParser target){
        this(state);
        this.targetparser = target;
        this.debuggedStructureList = `emptyTomList();
        this.bqparser = new NewBQParser(state,this);

	logger = Logger.getLogger(getClass().getName());
    }



    private final TomSignatureFactory getTomSignatureFactory(){
        return tsf();
    }
    
    public TomServer getServer(){
        return TomServer.getInstance();
    }
    
    private TomEnvironment environment() {
       // return getServer().getEnvironment();
        return TomEnvironment.getInstance();
    }

    private TomSignatureFactory tsf(){
        return environment().getTomSignatureFactory();
    }
    
    private jtom.tools.ASTFactory ast() {
        return environment().getASTFactory();
    }

    private void putType(String name, TomType type) {
        symbolTable().putType(name,type);
    }

    private void putSymbol(String name, TomSymbol symbol) {
        symbolTable().putSymbol(name,symbol);
    }

    private SymbolTable symbolTable() {
        return environment().getSymbolTable();
    }
    
    public TomStructureTable getStructTable() {
        return `StructTable(debuggedStructureList);
    }
/*
    public NewTomBackQuoteParser tomBQ(){
        return NewTomBackQuoteParser.getInstance();
    }
*/


    public void pushLine(int line){
        targetparser.pushLine(line);
    }

    public void pushColumn(int column){
        targetparser.pushColumn(column);
    }

    public void addTargetCode(Token t){
        targetparser.addTargetCode(t);
    }

    private void setLastLine(int line){
        lastLine = line;
    }

    private void clearText(){
        text.delete(0,text.length());
    }

    protected TokenStreamSelector selector(){
        return targetparser.getSelector();
    }
    
    void p(String s){
        System.out.println(s);
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


// the %match construct : 
 
matchConstruct [Option ot] returns [Instruction result] throws TomException
{ 
    result = null;
    OptionList optionList = `concOption(ot);
    StringBuffer debugKey = new StringBuffer(currentFile() + ot.getLine());

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
                
                if(((Boolean)getServer().getOptionValue("debug")).booleanValue()){
                    debuggedStructureList = (TomList) debuggedStructureList.append(result);
                }
                
                // update for new target block...
                pushLine(t.getLine());
                pushColumn(t.getColumn());
                
                // Match is finished : pop the tomlexer and return in
                // the target parser.  
                selector().pop(); 
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
            type:ALL_ID ( BACKQUOTE )? name:ALL_ID 
        )
        {
            list.add(`TLVar(name.getText(),TomTypeAlone(type.getText())));
        }        
    ;

patternAction [LinkedList list, StringBuffer debugKey] throws TomException
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
            ( (ALL_ID COLON) => label:ALL_ID COLON )?
             option = matchPattern[matchPatternList] 
            {
                listOfMatchPatternList.add(ast().makeList(matchPatternList));
                matchPatternList.clear();
                listTextPattern.add(text.toString());
                clearText();
                listOrgTrackPattern.add(option);
            }
            ( 
                ALTERNATIVE option = matchPattern[matchPatternList] 
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
                // update for new target block
                pushLine(t.getLine());
                pushColumn(t.getColumn());
                
                if(((Boolean)getServer().getOptionValue("debug")).booleanValue()){
                    blockList.add(`ITL(
                            "jtom.debug.TomDebugger.debugger.patternSuccess(\""
                            +debugKey
                            +"\");\n")
                    );
                    if(((Boolean)getServer().getOptionValue("memory")).booleanValue()){
                            blockList.add(
                                `ITL("jtom.debug.TomDebugger.debugger.emptyStack();\n")
                            );
                    }
                }

                // actions in target language : call the target lexer and
                // call the target parser
                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.targetLanguage(blockList);

                // target parser finished : pop the target lexer
                selector().pop();

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
                result = `OriginTracking(Name("Pattern"),lastLine,Name(currentFile()));
            }
            ( 
                COMMA {text.append('\n');}  
                term = annotedTerm {list.add(term);}
            )*
        )
    ;



// The %rule construct
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
            ( ALTERNATIVE {text.append('|');} lhs = annotedTerm
                {listOfLhs = (TomList) listOfLhs.append(lhs);} 
            )*
 
            ARROW {orgText = `Name(text.toString());} rhs = plainTerm[null,0]
            (
                WHERE pattern = annotedTerm AFFECT subject = annotedTerm 
                {conditionList = (InstructionList) conditionList.append(`MatchingCondition(pattern,subject));}
            |   IF pattern = annotedTerm DOUBLEEQ subject = annotedTerm
                {conditionList = (InstructionList) conditionList.append(`EqualityCondition(pattern,subject));}
            )*
            
            {
                // get the last token's line
                int line = lastLine;
                Option ot = `OriginTracking(
                    Name("Pattern"),
                    line,
                    Name(currentFile())
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

            if(((Boolean)getServer().getOptionValue("debug")).booleanValue()){
                debuggedStructureList = (TomList) debuggedStructureList.append(result);
            }
            
            // %rule finished: go back in target parser.
            selector().pop();
        }
    ;


// terms for %match and %rule
annotedTerm returns [TomTerm result]
{
    result = null;
    TomName annotedName = null;
    int line = 0;
}
    :   (
            ( 
                (ALL_ID AT) => name:ALL_ID AT 
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
    (astAnnotedName == null)?null:ast().makeAssignTo(astAnnotedName, line, currentFile());
    if(annotedName != null)
        constraintList.add(annotedName);
}
    :  
    (   // xml is missing
            // var* or _*
            (variableStar[null,null]) => result = variableStar[optionList,constraintList] 

        |   // _
            result = placeHolder[optionList,constraintList] 

        |   // for a single constant. 
            // ambiguous with the next rule so :
            {LA(2) != LPAREN && LA(2) != LBRACKET}? 
            name = headSymbol[optionList] 
            {
                nameList = (NameList) nameList.append(name);
                result = `Appl(
                        ast().makeOptionList(optionList),
                        nameList,
                        ast().makeList(list),
                        ast().makeConstraintList(constraintList)
                    );
            }

        |   // f(...) or f[...]
            name = headSymbol[optionList] 
            {nameList = (NameList) nameList.append(name);}
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
            // ambiguity with the last rule so use a lookahead
            // if ALTERNATIVE then parse headSymbolList
	    	{LA(3) == ALTERNATIVE}? nameList = headSymbolList[optionList] 
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
            // (term , term , ...)
            t1:LPAREN {text.append('(');} 
            ( termList[list] )? 
            t2:RPAREN 
            {
                // setting line number for origin tracking
                // in %rule construct
                setLastLine(t2.getLine());

                text.append(t2.getText());
            
                result = false;
                optionList.add(`OriginTracking(Name(""),t1.getLine(),Name(currentFile())));
            }
            
        |   // [term = term , term = term , ...]
            t3:LBRACKET {text.append('[');} 
            ( pairList[list] )? 
            t4:RBRACKET 
            {
                // setting line number for origin tracking
                // in %rule construct
                setLastLine(t4.getLine());
                text.append(t4.getText());
                
                result = true;
                optionList.add(`OriginTracking(Name(""),t3.getLine(),Name(currentFile())));
            }
        )
    ;

termList [LinkedList list]
{
    TomTerm term = null;
}
    :   (
            term = annotedTerm {list.add(term);}
            ( COMMA {text.append(',');} term = annotedTerm {list.add(term);})*
        )
    ;

pairList [LinkedList list]
{
    TomTerm term = null;
}
    :   (
            name:ALL_ID EQUAL 
            {
                text.append(name.getText());
                text.append('=');
            } 
            term = annotedTerm 
            {list.add(`PairSlotAppl(Name(name.getText()),term));}
            ( COMMA {text.append(',');} 
                name2:ALL_ID EQUAL 
                {
                    text.append(name2.getText());
                    text.append('=');
                } 
                term = annotedTerm 
                {list.add(`PairSlotAppl(Name(name2.getText()),term));}
            )*
        )
;
   
// _* or var*       
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
                name1:ALL_ID 
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

                // setting line number for origin tracking
                // in %rule construct
                setLastLine(t.getLine());
                
                optionList.add(`OriginTracking(Name(name),line,Name(currentFile())));
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

// _
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
                    `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile()))
                );
                options = ast().makeOptionList(optionList);
                constraints = ast().makeConstraintList(constraintList);
                result = `Placeholder(options, constraints);
            } 
        )
    ;

// ( id | id | ...)
headSymbolList [LinkedList optionList] returns [NameList result]
{ 
    result = `emptyNameList();
    TomName name = null;
}
    :  
        (
            LPAREN {text.append('(');}
            name = headSymbol[optionList] 
            {result = (NameList) result.append(name);}

            ALTERNATIVE {text.append('|');}
            name = headSymbol[optionList] 
            {result = (NameList) result.append(name);}

            ( 
                ALTERNATIVE {text.append('|');} 
                name = headSymbol[optionList] 
                {result = (NameList) result.append(name);}
            )* 
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
            i:ALL_ID 
            {
                name = i.getText();
                line = i.getLine();
                text.append(name);
            }
        |   t = constant // add to symbol table
            {
                name = t.getText();
                line = t.getLine();
                text.append(name);
            }
        )
        {
            setLastLine(line);
            result = `Name(name);
            optionList.add(`OriginTracking(result,line, Name(currentFile())));

            if (t != null){
                switch(t.getType()){
                case NUM_INT:
                    ast().makeIntegerSymbol(symbolTable(),t.getText(),optionList);
                    break;
                case CHARACTER:
                    ast().makeCharSymbol(symbolTable(),t.getText(),optionList);
                    break;
                case NUM_DOUBLE:
                    ast().makeDoubleSymbol(symbolTable(),t.getText(),optionList);
                    break;
                case STRING:
                    ast().makeStringSymbol(symbolTable(),t.getText(),optionList);
                    break;
                default:
                }
            }
        }
    ;

//This rule is called from the target parser
//Then the rule calls the backquote parser with 2 cases :
// 1) (...) -> bqTarget
// 2) ALL_ID | ALL_ID* | ALL_ID(...) -> bqTargetAppl
bqTerm returns [TomTerm result]
{
    String bqCode = null;
    LinkedList blockList = new LinkedList();
    result = null;
}
    :
        (
            l:LPAREN 
            {
                blockList.add(l);
                selector().push("bqlexer");
                result = bqparser.beginBqComposite();
                selector().pop();
            }
        |
            i:ALL_ID 
            {
		blockList.add(i);
                selector().push("bqlexer");
                result = bqparser.beginBqAppl(i);
                selector().pop();
            }
        )
    ;



// Operator Declaration
operator returns [Declaration result] throws TomException
{
    result=null;
    Option ot = null;
    TomTypeList types = `emptyTomTypeList();
    LinkedList options = new LinkedList();
    LinkedList slotNameList = new LinkedList();
    SlotList slotList = `emptySlotList();
    TomName astName = null;
    String stringSlotName = null;
    Declaration attribute;
    TargetLanguage tlFsym;

    Map mapNameDecl = new HashMap();
}
    :
        type:ALL_ID name:ALL_ID 
        {
            ot = `OriginTracking(Name(name.getText()),name.getLine(),Name(currentFile()));
            options.add(ot);
        }
        (
            LPAREN
            { stringSlotName = ""; }
            (
               (ALL_ID COLON) => slotName:ALL_ID COLON
                { stringSlotName = slotName.getText(); }
            )? 
            typeArg:ALL_ID 
            {
                slotNameList.add(ast().makeName(stringSlotName)); 
                types = (TomTypeList) types.append(`TomTypeAlone(typeArg.getText()));
            }
            (
                COMMA
                { stringSlotName = ""; }
                (
                    (ALL_ID COLON) => slotName2:ALL_ID COLON
                    { stringSlotName = slotName2.getText(); }
                )?
                typeArg2:ALL_ID
                {
                    astName = ast().makeName(stringSlotName);

                    if (!stringSlotName.equals("")) {
                        if(slotNameList.indexOf(astName) != -1) {
                            String detailedMsg = MessageFormat.format(TomMessage.getString("RepeatedSlotName"), new Object[]{stringSlotName});
                            String msg = MessageFormat.format(
                                TomMessage.getString("MainErrorMessage"), 
                                new Object[]{new Integer(ot.getLine()), "%op "+type.getText(), new Integer(ot.getLine()), currentFile(), detailedMsg}
                            );
                            throw new TomException(msg);
                        }
                    }

                    slotNameList.add(astName); 
                    types = (TomTypeList) types.append(`TomTypeAlone(typeArg2.getText()));
                }
            )*
            RPAREN
        )?
        LBRACE
        tlFsym = keywordFsym
        {
            astName = `Name(name.getText());
        }
        (
            attribute = keywordMake[name.getText(),`TomTypeAlone(type.getText()),types]
            { options.add(attribute); }

        |   attribute = keywordGetSlot[astName,type.getText()]
            {
                TomName sName = attribute.getSlotName();
                if (mapNameDecl.get(sName)==null) {
                    mapNameDecl.put(sName,attribute);
                }
                else {
		    logger.log( Level.WARNING,
				"WarningTwoSameSlotDecl",
				new Object[]{currentFile(), new Integer(attribute.getOrgTrack().getLine()),
					     "%op "+type.getText(), new Integer(ot.getLine()), sName.getString()} );

//                     environment().messageWarning(attribute.getOrgTrack().getLine(),
//                         currentFile(),
//                         "%op "+type.getText(),
//                         ot.getLine(),
//                         TomMessage.getString("WarningTwoSameSlotDecl"), 
//                         new Object[]{sName.getString()});
                }
            }

        |   attribute = keywordIsFsym[astName,type.getText()]
            { options.add(attribute); }
        )*
        t:RBRACE
        {
            for(int i=slotNameList.size()-1; i>=0 ; i--) {
                TomName name1 = (TomName)slotNameList.get(i);
                PairNameDecl pair = null;
                Declaration emptyDeclaration = `EmptyDeclaration();
                if(name1.isEmptyName()) {
                    pair = `Slot(name1,emptyDeclaration);
                } else {
                    Declaration decl = (Declaration)mapNameDecl.get(name1);
                    if(decl == null) {

			logger.log( Level.WARNING,
				    "WarningMissingSlotDecl",
				    new Object[]{currentFile(), new Integer(ot.getLine()),
						 "%op "+type.getText(), new Integer(ot.getLine()), name1.getString()} );

//                         environment().messageWarning(ot.getLine(), 
//                             currentFile(),
//                             "%op "+type.getText(), 
//                             ot.getLine(), 
//                             TomMessage.getString("WarningMissingSlotDecl"),
//                             new Object[]{name1.getString()});
                        decl = emptyDeclaration;
                    }
                    else {
                        mapNameDecl.remove(name1);
                    }
                    pair = `Slot(name1,decl);
                }
                slotList = `manySlotList(pair,slotList);
            }
            // Test if there are still declaration in mapNameDecl
            if ( !mapNameDecl.isEmpty()) {
                Iterator it = mapNameDecl.keySet().iterator();
                while(it.hasNext()) {
                    TomName remainingSlot = (TomName) it.next();

		    logger.log( Level.WARNING,
				"WarningIncompatibleSlotDecl",
				new Object[]{currentFile(), 
					     new Integer(((Declaration)mapNameDecl.get(remainingSlot)).getOrgTrack().getLine()),
					     "%op "+type.getText(), new Integer(ot.getLine()), remainingSlot.getString()} );

//                     environment().messageWarning(((Declaration)mapNameDecl.get(remainingSlot)).getOrgTrack().getLine(),
//                         currentFile(),
//                         "%op "+type.getText(),
//                         ot.getLine(),
//                         TomMessage.getString("WarningIncompatibleSlotDecl"), new Object[]{remainingSlot.getString()});
                }
            }
            
            TomSymbol astSymbol = ast().makeSymbol(name.getText(), type.getText(), types, slotList, options, tlFsym);
            putSymbol(name.getText(),astSymbol);

            result = `SymbolDecl(astName);

            pushLine(t.getLine());
            pushColumn(t.getColumn());
            selector().pop(); 
        }
    ;

operatorList returns [Declaration result] throws TomException
{
    result = null;
    TomTypeList types = `emptyTomTypeList();
    LinkedList options = new LinkedList();
    Declaration attribute = null;
    TargetLanguage tlFsym;
}
    :
        type:ALL_ID name:ALL_ID
        {
            Option ot = `OriginTracking(Name(name.getText()),name.getLine(),Name(currentFile()));
            options.add(ot);
        }
        LPAREN typeArg:ALL_ID STAR RPAREN
        {
            types = (TomTypeList) types.append(`TomTypeAlone(typeArg.getText()));
        }
        LBRACE
        tlFsym=keywordFsym
        (
            attribute = keywordMakeEmptyList[name.getText()]
            { options.add(attribute); }

        |   attribute = keywordMakeAddList[name.getText(),type.getText(),typeArg.getText()]
            { options.add(attribute); }

        |   attribute = keywordIsFsym[`Name(name.getText()), type.getText()]
            { options.add(attribute); }
        )*
        t:RBRACE
        { 
            SlotList slotList = `concPairNameDecl(Slot(EmptyName(), EmptyDeclaration()));
            TomSymbol astSymbol = ast().makeSymbol(
                name.getText(), 
                type.getText(), 
                types, 
                slotList, 
                options, 
                tlFsym
            );
            
            putSymbol(name.getText(),astSymbol);

            result = `ListSymbolDecl(Name(name.getText()));

            pushLine(t.getLine());
            pushColumn(t.getColumn());
            selector().pop(); 
        }
    ;

operatorArray returns [Declaration result] throws TomException
{
    result = null;
    TomTypeList types = `emptyTomTypeList();
    LinkedList options = new LinkedList();
    Declaration attribute = null;
    TargetLanguage tlFsym;
}
    :
        type:ALL_ID name:ALL_ID
        {
            Option ot = `OriginTracking(Name(name.getText()),name.getLine(),Name(currentFile()));
            options.add(ot);
        }
        LPAREN typeArg:ALL_ID STAR RPAREN
        {
            types = (TomTypeList) types.append(`TomTypeAlone(typeArg.getText()));
        }
        LBRACE
        tlFsym = keywordFsym
        (
            attribute = keywordMakeEmptyArray[name.getText(),type.getText()]
            { options.add(attribute); }

        |   attribute = keywordMakeAddArray[name.getText(),type.getText(),typeArg.getText()]
            { options.add(attribute); }

        |   attribute = keywordIsFsym[`Name(name.getText()),type.getText()]
            { options.add(attribute); }
        )*
        t:RBRACE
        { 
            SlotList slotList = `concPairNameDecl(Slot(EmptyName(), EmptyDeclaration()));
            TomSymbol astSymbol = ast().makeSymbol(name.getText(), type.getText(), types, slotList, options, tlFsym);
            putSymbol(name.getText(),astSymbol);

            result = `ArraySymbolDecl(Name(name.getText()));

            pushLine(t.getLine());
            pushColumn(t.getColumn());
            selector().pop(); 
        }
    ;

typeTerm returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
    TomList blockList = `emptyTomList();
    Declaration attribute = null;
    TargetLanguage implement = null;
}
    :   (
            type:ALL_ID
            { 
                ot = `OriginTracking(Name(type.getText()), type.getLine(),Name(currentFile()));
            }
            LBRACE

            implement = keywordImplement
            (
                attribute = keywordGetFunSym[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            |   attribute = keywordGetSubterm[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            |   attribute = keywordCmpFunSym[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            |   attribute = keywordEquals[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            )*
            t:RBRACE
        )
        {
            TomType astType = `Type(ASTTomType(type.getText()),TLType(implement));
            putType(type.getText(), astType);

            result = `TypeTermDecl(Name(type.getText()),blockList,ot);

            pushLine(t.getLine());
            pushColumn(t.getColumn());
            selector().pop();
        }
    ;

typeList returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
    Declaration attribute = null;
    TomList blockList = `emptyTomList();
    TargetLanguage implement = null;
}
    :   (
            type:ALL_ID
            {ot = `OriginTracking(Name(type.getText()),type.getLine(),Name(currentFile()));}
            LBRACE
            implement = keywordImplement
            (
                attribute = keywordGetFunSym[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            |   attribute = keywordGetSubterm[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            |   attribute = keywordCmpFunSym[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            |   attribute = keywordEquals[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            |   attribute = keywordGetHead[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            |   attribute = keywordGetTail[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            |   attribute = keywordIsEmpty[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            )*
            t:RBRACE

        )
        {
            TomType astType = `Type(ASTTomType(type.getText()),TLType(implement));
            putType(type.getText(), astType);

            result = `TypeListDecl(Name(type.getText()),blockList,ot);

            pushLine(t.getLine());
            pushColumn(t.getColumn());
            selector().pop();
        }
    ;


typeArray returns [Declaration result] throws TomException
{
    result=null;
    Option ot = null;
    Declaration attribute = null;
    TomList blockList = `emptyTomList();
    TargetLanguage implement = null;
}
    :   (
            type:ALL_ID
            {ot = `OriginTracking(Name(type.getText()),type.getLine(),Name(currentFile()));}
            LBRACE
            implement = keywordImplement
            (                
                attribute = keywordGetFunSym[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            |   attribute = keywordGetSubterm[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            |   attribute = keywordCmpFunSym[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            |   attribute = keywordEquals[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            |   attribute = keywordGetElement[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}

            |   attribute = keywordGetSize[type.getText()]
                {blockList = (TomList) blockList.append(`DeclarationToTomTerm(attribute));}
            
            )*
            t:RBRACE
        )
        {
            TomType astType = `Type(ASTTomType(type.getText()),TLType(implement));
            putType(type.getText(), astType);

            result = `TypeArrayDecl(Name(type.getText()),blockList,ot);
            
            pushLine(t.getLine());
            pushColumn(t.getColumn());
            selector().pop();
        }
    ;

keywordImplement returns [TargetLanguage tlCode] throws TomException
{
    tlCode = null;
}
    :
        (
            IMPLEMENT
            {
                selector().push("targetlexer");
                tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop();
            }
        )
    ;

keywordGetFunSym [String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:GET_FUN_SYM 
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile())); }
            LPAREN name:ALL_ID RPAREN
            {
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),Name(currentFile()));
                OptionList option = `concOption(info);

                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop();

                result = `GetFunctionSymbolDecl(
                    Variable(option,Name(name.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode,ot);
            }
        )
    ;

keywordGetSubterm[String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:GET_SUBTERM 
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile())); }
            LPAREN name1:ALL_ID COMMA name2:ALL_ID RPAREN
            {
                Option info1 = `OriginTracking(Name(name1.getText()),name1.getLine(),Name(currentFile()));
                Option info2 = `OriginTracking(Name(name2.getText()),name2.getLine(),Name(currentFile()));
                OptionList option1 = `concOption(info1);
                OptionList option2 = `concOption(info2);
                
                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop(); 

                result = `GetSubtermDecl(
                    Variable(option1,Name(name1.getText()),TomTypeAlone(type),emptyConstraintList()),
                    Variable(option2,Name(name2.getText()),TomTypeAlone("int"),emptyConstraintList()),
                    tlCode, ot);
            }
        )
    ;

keywordCmpFunSym [String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:CMP_FUN_SYM 
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile()));}
            LPAREN name1:ALL_ID COMMA name2:ALL_ID RPAREN
            {
                Option info1 = `OriginTracking(Name(name1.getText()),name1.getLine(),Name(currentFile()));
                Option info2 = `OriginTracking(Name(name2.getText()),name2.getLine(),Name(currentFile()));
                OptionList option1 = `concOption(info1);
                OptionList option2 = `concOption(info2);
                
                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop(); 

                result = `CompareFunctionSymbolDecl(
                    Variable(option1,Name(name1.getText()),TomTypeAlone(type),emptyConstraintList()),
                    Variable(option2,Name(name2.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode, ot);
            }
        )
    ;

keywordEquals[String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:EQUALS 
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile())); }
            LPAREN name1:ALL_ID COMMA name2:ALL_ID RPAREN
            {
                Option info1 = `OriginTracking(Name(name1.getText()),name1.getLine(),Name(currentFile()));
                Option info2 = `OriginTracking(Name(name2.getText()),name2.getLine(),Name(currentFile()));
                OptionList option1 = `concOption(info1);
                OptionList option2 = `concOption(info2);
                
                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop();  
                
                result = `TermsEqualDecl(
                    Variable(option1,Name(name1.getText()),TomTypeAlone(type),emptyConstraintList()),
                    Variable(option2,Name(name2.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode, ot);
            }
        )
    ;
keywordGetHead[String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:GET_HEAD 
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile())); }
            LPAREN name:ALL_ID RPAREN
            {
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),Name(currentFile()));
                OptionList option = `concOption(info);

                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop();  

                result = `GetHeadDecl(
                    symbolTable().getUniversalType(),
                    Variable(option,Name(name.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode,
                    ot);
            }
        )
    ;

keywordGetTail[String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:GET_TAIL 
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile())); }
            LPAREN name:ALL_ID RPAREN
            {
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),Name(currentFile()));
                OptionList option = `concOption(info);

                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop();  

                result = `GetTailDecl(
                    Variable(option,Name(name.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode,
                    ot);
            }
        )
    ;

keywordIsEmpty[String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:IS_EMPTY 
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile())); }
            LPAREN name:ALL_ID RPAREN
            {
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),Name(currentFile()));
                OptionList option = `concOption(info);

                selector().push("targetlexer");
                TargetLanguage  tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop(); 

                result = `IsEmptyDecl(
                    Variable(option,Name(name.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode,
                    ot); 
            }
        )
    ;

keywordGetElement[String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:GET_ELEMENT 
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile())); }
            LPAREN name1:ALL_ID COMMA name2:ALL_ID RPAREN
            {
                Option info1 = `OriginTracking(Name(name1.getText()),name1.getLine(),Name(currentFile()));
                Option info2 = `OriginTracking(Name(name2.getText()),name2.getLine(),Name(currentFile()));
                OptionList option1 = `concOption(info1);
                OptionList option2 = `concOption(info2);
                
                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop();  
                
                result = `GetElementDecl(
                    Variable(option1,Name(name1.getText()),TomTypeAlone(type),emptyConstraintList()),
                    Variable(option2,Name(name2.getText()),TomTypeAlone("int"),emptyConstraintList()),
                    tlCode, ot);
            }
        )
    ;

keywordGetSize[String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:GET_SIZE
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile())); }
            LPAREN name:ALL_ID RPAREN
            {
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),Name(currentFile()));
                OptionList option = `concOption(info);

                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop();  

                result = `GetSizeDecl(
                    Variable(option,Name(name.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode,ot);
            }
        )
    ;

keywordFsym returns [TargetLanguage tlCode] throws TomException
{
    tlCode = null;
}
    :
        FSYM 
        {
            selector().push("targetlexer");
            tlCode = targetparser.goalLanguage(new LinkedList());
            selector().pop();
        }
    ;

keywordIsFsym [TomName astName, String typeString] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        t:IS_FSYM
        { ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile())); }
        LPAREN name:ALL_ID RPAREN
        {
            Option info = `OriginTracking(Name(name.getText()),name.getLine(),Name(currentFile()));
            OptionList option = `concOption(info);

            selector().push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            selector().pop();

            result = `IsFsymDecl(astName,
                Variable(option,Name(name.getText()),TomTypeAlone(typeString),emptyConstraintList()),
                tlCode,ot);
        }
    ;

keywordGetSlot [TomName astName, String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:GET_SLOT
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile())); }
            LPAREN slotName:ALL_ID COMMA name:ALL_ID RPAREN
            {                
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),Name(currentFile()));
                OptionList option = `concOption(info);
                
                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop(); 

                result = `GetSlotDecl(astName,
                    Name(slotName.getText()),
                    Variable(option,Name(name.getText()),TomTypeAlone(type),emptyConstraintList()),
                    tlCode, ot);
            }
        )
    ;

keywordMake [String opname, TomType returnType, TomTypeList types] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
    TomList args = `emptyTomList();
    int index = 0;
    TomType type;
    int nbTypes = types.getLength();
}
    :
        (
            t:MAKE
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile())); }

            ( 
                LPAREN 
                ( 
                    typeArg:ALL_ID
                    {
                        if( !(nbTypes > 0) ) {
                            type = `EmptyType();
                        } else {
                            type = (TomType)types.elementAt(index++);
                        }
                        Option info1 = `OriginTracking(Name(typeArg.getText()),typeArg.getLine(),Name(currentFile()));  
                        OptionList option1 = `concOption(info1);
                        
                        args = (TomList) args.append(`Variable(
                                option1,
                                Name(typeArg.getText()),
                                type,emptyConstraintList()
                            ));
                    }
                    ( 
                        COMMA nameArg:ALL_ID
                        {
                            if( index >= nbTypes ) {
                                type = `EmptyType();
                            } else {
                                type = (TomType)types.elementAt(index++);
                            }
                            Option info2 = `OriginTracking(Name(nameArg.getText()),nameArg.getLine(),Name(currentFile()));
                            OptionList option2 = `concOption(info2);
                            
                            args = (TomList) args.append(`Variable(
                                    option2,
                                    Name(nameArg.getText()),
                                    type,emptyConstraintList()
                                ));
                        }
                    )*
                )? 
                RPAREN )?
            l:LBRACE
            {
                pushLine(l.getLine());
                pushColumn(l.getColumn());

                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.targetLanguage(new LinkedList());
                selector().pop();

                result = `MakeDecl(Name(opname),returnType,args,tlCode,ot);
            }
        )
    ;

keywordMakeEmptyList[String name] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        t:MAKE_EMPTY
        { ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile())); }
        (LPAREN RPAREN)?
        {
            selector().push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            selector().pop();

            result = `MakeEmptyList(Name(name),tlCode,ot);
        }
    ;

keywordMakeAddList[String name, String listType, String elementType] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        t:MAKE_INSERT
        {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile()));}

        LPAREN elementName:ALL_ID COMMA listName:ALL_ID RPAREN
        {
            Option listInfo = `OriginTracking(Name(listName.getText()),listName.getLine(),Name(currentFile()));  
            Option elementInfo = `OriginTracking(Name(elementName.getText()),elementName.getLine(),Name(currentFile()));
            OptionList listOption = `concOption(listInfo);
            OptionList elementOption = `concOption(elementInfo);

            selector().push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            selector().pop();
            
            result = `MakeAddList(Name(name),
                Variable(elementOption,Name(elementName.getText()),TomTypeAlone(elementType),emptyConstraintList()),
                Variable(listOption,Name(listName.getText()),TomTypeAlone(listType),emptyConstraintList()),
                tlCode,ot);
        }
    ;

keywordMakeEmptyArray[String name, String listType] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        t:MAKE_EMPTY
        {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile()));}
        LPAREN listName:ALL_ID RPAREN
        {
            Option listInfo = `OriginTracking(Name(listName.getText()),listName.getLine(),Name(currentFile()));  
            OptionList listOption = `concOption(listInfo);

            selector().push("targetlexer");
            TargetLanguage tlCode =  targetparser.goalLanguage(new LinkedList());
            selector().pop();

            result = `MakeEmptyArray(Name(name),
                Variable(listOption,Name(listName.getText()),TomTypeAlone(listType),emptyConstraintList()),
                tlCode,ot);
        }
    ;   

keywordMakeAddArray[String name, String listType, String elementType] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        t:MAKE_APPEND
        {ot = `OriginTracking(Name(t.getText()),t.getLine(),Name(currentFile()));}
        LPAREN elementName:ALL_ID COMMA listName:ALL_ID RPAREN
        {
            selector().push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            selector().pop();

            Option listInfo = `OriginTracking(Name(listName.getText()),listName.getLine(),Name(currentFile()));  
            Option elementInfo = `OriginTracking(Name(elementName.getText()),elementName.getLine(),Name(currentFile()));
            OptionList listOption = `concOption(listInfo);
            OptionList elementOption = `concOption(elementInfo);
            
            result = `MakeAddArray(Name(name),
                Variable(elementOption,Name(elementName.getText()),TomTypeAlone(elementType),emptyConstraintList()),
                Variable(listOption,Name(listName.getText()),TomTypeAlone(listType),emptyConstraintList()),
                tlCode,ot);
        }
    ;

/*


// here begins the xml part

xmlTerm 
{
    NameList nameList = null, closingNameList = null;
    TomTerm arg1 = null, arg2 = null;

    LinkedList childs = new LinkedList();
    boolean implicit;
}
    :
        START nameList = xmlNameList[optionList,true] xmlAttributeList
        (
            CLOSE_SINGLETON
        |   CLOSE implicit = xmlChilds[childs] START_ENDING closingNameList = xmlNameList[optionList,false] CLOSE
        )
    |   TEXT LPAREN annotedTerm RPAREN
    |   COMMENT LPAREN arg1 = termStringIdentifier[null] RPAREN 
    |   PROC LPAREN arg1 = termStringIdentifier[null] COMMA arg2 = termStringIdentifier[null] RPAREN
    ;

xmlNameList [LinkedList optionList, boolean needOrgTrack] returns [NameList result] 
{
    result = null;
    String xmlName = null;
    int decLine;
}
    :
        (
            i:ALL_ID 
            {
                text.append(i.getText());
                xmlName = i.getText();
                decLine = i.getLine();
                result = `concTomName(Name(i.getText()));
            }
        |   u:UNDERSCORE 
            {
                text.append(u.getText());
                xmlName = u.getText();
                decLine = u.getLine();
                result = `concTomName(Name(u.getText()));
            }
        |   LPAREN i1:ALL_ID 
            {
                text.append(i1.getText());
                xmlName = i1.getText();
                decLine = i1.getLine();
                result = `concTomName(Name(i1.getText()));
            }
            ( 
                ALTERNATIVE i2:ALL_ID  
                {
                    text.append('|');
                    text.append(i1.getText());
                    xmlName += "|" + i1.getText();
                    result = (NameList) result.append(`Name(i1.getText()));
                }
            )+ RPAREN
        )
        {
            if(needOrgTrack) {
                optionList.add(`OriginTracking(Name(XMLName),decLine,Name( currentFile)));
            }
        }
    ;

xmlAttributeList
    :
        LBRACKET ( xmlAttribute ( COMMA xmlAttribute )* )? RBRACKET
    |   LPAREN ( xmlAttribute ( COMMA xmlAttribute )* )? RPAREN
    |   xmlAttribute
    ;

xmlAttribute
{
    TomTerm term;

    LinkedList optionList = new LinkedList();
    LinkedList constraintList = new LinkedList();
    LinkedList optionListAnno2 = new LinkedList();
}
    :
        {LA(2) == STAR}? result = variableStar[optionList,constraintList] 
    |   ( 
            {LA(2) == EQUAL}? ALL_ID EQUAL ( {LA(2) == AT}? ALL_ID AT )? 
            term = unamedVariableOrTermStringIdentifier[optionListAnno2]
        |   ( ALL_ID AT )? placeHolder[null,null] EQUAL 
            ( {LA(2) == AT}? ALL_ID AT )? term = unamedVariableOrTermStringIdentifier[optionListAnno2]
        )   
    ;

unamedVariableOrTermStringIdentifier [LinkedList options] returns [TomTerm result]
{
    result = null;
    Token name = null;
}
    :
        (
            u:UNDERSCORE {name = u;}
        |   i:ALL_ID {name = i;}
        |   s:STRING {name = s;}
        )
        {
            text.append(name.getText());
            LinkedList optionList = (options==null)?new LinkedList():options;
            optionList.add(
                `OriginTracking(
                    Name(name.getText()),
                    name.getLine(),
                    Name(currentFile())
                )
            );
            
            OptionList option = ast().makeOptionList(optionList);
            
            if(name.getType() == UNDERSCORE) {
                result =  `UnamedVariable(option,TomTypeAlone("unknown type"),concConstraint());
            } 
            else {
                if(name.getType() == STRING) {
                    ast().makeStringSymbol(symbolTable(),name.getText(),optionList);
                }   
                nameList = `concTomName(Name(name.getText()));
                result =  `Appl(
                    option,
                    nameList,
                    concTomTerm(),
                    concConstraint()
                );
            }
        }
    ;

xmlChilds [LinkedList list] returns [boolean result]
{
    LinkedList childs = new LinkedList();
    boolean implicit;
}
    :
        ( 
            implicit = implicitTermList[childs]
        |   implicit = xmlTermList[childs]
        )
        {
            it = childs.iterator();
            while(it.hasNext()) {
                list.add(tomFactory.metaEncodeXMLAppl(symbolTable(),(TomTerm)it.next()));
            }
            return implicit;
    }
    ;

implicitTermList
    :
        BRACKET ( annotedTerm ( COMMA annotedTerm )* )? RBRACKET
    ;

xmlTermList [LinkedList list] returns [boolean result]
{
    TomTerm term = null;
}
    :
        ( term = annotedTerm {list.add(term);} )*
        {result = true;}
    ;

termStringIdentifier [LinkedList options] returns [TomTerm result]
{
    result = null;
    Token name = null;
}
    :
        (
            i:ALL_ID {name = i;}
        |   s:STRING {name = s;}
        )
        {
            text.append(name.getText());
            
            LinkedList optionList = (options==null)?new LinkedList():options;
            optionList.add(
                `OriginTracking(
                    Name(name.getText()),
                    name.getLine(),
                    Name(currentFile())
                )
            );
            
            OptionList option = ast().makeOptionList(optionList);
            
            if(name.getType == STRING) {
                ast().makeStringSymbol(symbolTable(),name.getText(),optionList);
            }
            
            nameList = `concTomName(Name(name.getText()));
            result = `Appl(
                option,
                nameList,
                concTomTerm(),
                concConstraint()
            );
        }
    ;

*/


class NewTomLexer extends Lexer;
options {
	k=3; // default lookahead
    charVocabulary = '\u0000'..'\uffff'; // each character can be read
    testLiterals = false;
}

tokens { 
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
DOULEARROW  :   "=>";
ALTERNATIVE :   '|' ;
AFFECT      :   ":=";
DOUBLEEQ    :   "==";
COLON       :   ':' ;
EQUAL       :   '=' ;
AT          :   '@' ;
STAR        :   '*' ;
UNDERSCORE  :   '_' ; 
BACKQUOTE   :   "`" ;

// for xml
START   :   '<' ; 
START_ENDING    :   "</"    ;
CLOSE   :   '>' ;
CLOSE_SINGLETON :   "/>"    ;
DOUBLE_QUOTE    :   '\"'    ;
TEXT    :   "#TEXT" ;
COMMENT :   "#COMMENT"  ;
PROC    :   "#PROCESSING-INSTRUCTION"   ;

// tokens to skip : white spaces
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


// tokens to skip : Single Line Comments
SLCOMMENT
	:	"//"
		(~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
		{
            $setType(Token.SKIP); 
            newline();
        }
	;

// tokens to skip : Multi Lines Comments
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


CHARACTER
	:	'\'' ( ESC | ~('\''|'\n'|'\r'|'\\') )+ '\''
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
/*
BASIC_ID
options{ testLiterals = true; }     
    :   ('a'..'z' | 'A'..'Z')
        ( 
            ('a'..'z' | 'A'..'Z') 
        |   ('0'..'9') 
        |   UNDERSCORE 
        )*
    ;
*/

/*
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
*/

protected LETTER    :   ('a'..'z' | 'A'..'Z')   ;
protected DIGIT     :   ('0'..'9')  ;

ALL_ID
options{testLiterals = true;}
    :
        (
            (ID_MINUS) => ID_MINUS
        |   ID
        )
//        {System.out.println("id "+$getText);}
    ;
        

protected ID
options{testLiterals = true;}
    :
        LETTER
        ( LETTER | DIGIT | UNDERSCORE  /*( (MINUS LETTER) => MINUS LETTER )*/ 
        )* 
    ;   
/*
ID_MINUS
options{testLiterals = true;}   
    :   ('a'..'z' | 'A'..'Z') 
        ( 
            ('a'..'z' | 'A'..'Z') 
        |   ('0'..'9') 
        |   UNDERSCORE 
        |   ( MINUS ('a'..'z' | 'A'..'Z') ) 
        )*  
    ;
*/

protected ID_MINUS
    :
        ID MINUS ('a'..'z' | 'A'..'Z') ( ID_MINUS )*
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
