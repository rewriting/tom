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
    import jtom.xml.Constants;

    import tom.platform.*;

    import antlr.*;
}

class TomParser extends Parser;

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
    private HostParser targetparser;
    protected BackQuoteParser bqparser;
    private TomLexer tomlexer;

    private Logger logger;

    private StringBuffer text = new StringBuffer("");
    
    private int lastLine; 

    private TomList debuggedStructureList = null;

    private TomFactory tomFactory;

    private boolean debugMode;
    private boolean debugMemoryMode;    

    public TomParser(ParserSharedInputState state, HostParser target, OptionManager optionManager){
        this(state);
        this.targetparser = target;
        this.debuggedStructureList = `emptyTomList();
        this.bqparser = new BackQuoteParser(state,this);
        this.tomFactory = new TomFactory();
        this.tomlexer = (TomLexer) selector().getStream("tomlexer");
        this.debugMode = ((Boolean)optionManager.getOptionValue("debug")).booleanValue();
        this.debugMemoryMode = ((Boolean)optionManager.getOptionValue("memory")).booleanValue();
        logger = Logger.getLogger(getClass().getName());
    }
    
    private final TomSignatureFactory getTomSignatureFactory(){
        return tsf();
    }
    
    private TomEnvironment environment() {
        return TomEnvironment.getInstance();
    }

    private TomSignatureFactory tsf(){
        return TomBase.getTomSignatureFactory();
    }
    
    private jtom.tools.ASTFactory ast() {
        return TomBase.getAstFactory();
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

    private int getLine(){
        return tomlexer.getLine();
    }
    /*
    public void pushLine(int line){
        targetparser.pushLine(line);
    }

    public void pushColumn(int column){
        targetparser.pushColumn(column);
    }
*/
    public void updatePosition(int i, int j){
        targetparser.updatePosition(i,j);
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
                
                if(debugMode){
                    debuggedStructureList = (TomList) debuggedStructureList.append(result);
                }
                
                // update for new target block...
                //pushLine(t.getLine());
                //pushColumn(t.getColumn());
                updatePosition(t.getLine(),t.getColumn());
                
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
//                pushLine(t.getLine());
  //              pushColumn(t.getColumn());
                updatePosition(t.getLine(),t.getColumn());
                
                if(debugMode){
                    blockList.add(`ITL(
                            "jtom.debug.TomDebugger.debugger.patternSuccess(\""
                            +debugKey
                            +"\");\n")
                    );
                    if(debugMemoryMode){
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

matchPattern [LinkedList list] returns [Option result] throws TomException
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
ruleConstruct [Option orgTrack] returns [Instruction result] throws TomException
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
//            pushLine(t.getLine());
  //          pushColumn(t.getColumn());
            updatePosition(t.getLine(),t.getColumn());

            result = `RuleSet(ruleList,orgTrack);

            if(debugMode){
                debuggedStructureList = (TomList) debuggedStructureList.append(result);
            }
            
            // %rule finished: go back in target parser.
            selector().pop();
        }
    ;


// terms for %match and %rule
annotedTerm returns [TomTerm result] throws TomException
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

plainTerm [TomName astAnnotedName, int line] returns [TomTerm result] throws TomException
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
        (   // xml term
            result = xmlTerm[optionList, constraintList]

        |   // var* or _*
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



xmlTerm [LinkedList optionList, LinkedList constraintList] returns [TomTerm result] throws TomException
{
  result = null;
  TomTerm arg1, arg2;
  LinkedList pairSlotList = new LinkedList();
  LinkedList attributeList = new LinkedList();
  LinkedList childs = new LinkedList();
  String keyword = "";
  boolean implicit;
  NameList nameList, closingNameList;
  OptionList option = null;
  ConstraintList constraint;
}
    :
        (
            // < NODE attributes [ /> | > childs </NODE> ]
            XML_START {text.append("<");}
            nameList = xmlNameList[optionList, true]
            implicit = xmlAttributeList[attributeList]
            {
                if(implicit) { optionList.add(`ImplicitXMLAttribut()); }
            }
            (   // case: /> 
                XML_CLOSE_SINGLETON
                {
                    text.append("\\>");
                    option =  ast().makeOptionList(optionList);
                }
            |   // case: > childs  </NODE>
                XML_CLOSE  {text.append(">");}
                implicit = xmlChilds[childs]

                XML_START_ENDING {text.append("</"); }
                closingNameList = xmlNameList[optionList, false]
                t:XML_CLOSE  {text.append(">");}
                {
                    if(!nameList.equals(closingNameList)) {
                        String found="", expected ="";
                        while(!nameList.isEmpty()) {
                            expected += "|"+nameList.getHead().getString();
                            nameList = nameList.getTail();
                        }
                        while(!closingNameList.isEmpty()) {
                            found += "|"+closingNameList.getHead().getString();
                            closingNameList = closingNameList.getTail();
                        }
                        //throw new TomException("Error on closing XML pattern: expecting '"+ expected.substring(1) +"' but got '"+found.substring(1)+ "' at line "+getLine());
                        // return null;
                        // TODO find the orgTrack of the match
                        logger.log( Level.SEVERE,
                            "MalformedXMLTerm",
                            new Object[]{currentFile(), new Integer(getLine()), 
                                "match", new Integer(getLine()),
                                expected.substring(1), found.substring(1)} );
                    }
                    if(implicit) {
                        // Special case when XMLChilds() is reduced to a singleton
                        // when XMLChilds() is reduced to a singleton
                        // Appl(...,Name(""),args)
                        if(tomFactory.isExplicitTermList(childs)) {
                            childs = tomFactory.metaEncodeExplicitTermList(symbolTable(), (TomTerm)childs.getFirst());
                        } else {
                            optionList.add(`ImplicitXMLChild());
                        }
                    }
                    option = ast().makeOptionList(optionList);    
                }
            ) // end choice
            {
                result = `XMLAppl(
                    option,
                    nameList,
                    ast().makeList(attributeList),
                    ast().makeList(childs),
                    ast().makeConstraintList(constraintList));
            }

        | // #TEXT(...)
            XML_TEXT LPAREN arg1 = annotedTerm RPAREN
            {
                keyword = Constants.TEXT_NODE;
                pairSlotList.add(`PairSlotAppl(Name(Constants.SLOT_DATA),arg1));

                optionList.add(`OriginTracking(Name(keyword),getLine(),Name( currentFile())));
                option = ast().makeOptionList(optionList);
                constraint = ast().makeConstraintList(constraintList);
                nameList = `concTomName(Name(keyword));
                result = `RecordAppl(option,
                    nameList,
                    ast().makeList(pairSlotList),
                    constraint);
            }
        | // #COMMENT(...)
            XML_COMMENT LPAREN arg1 = termStringIdentifier[null] RPAREN
            {
                keyword = Constants.COMMENT_NODE;
                pairSlotList.add(`PairSlotAppl(Name(Constants.SLOT_DATA),arg1));

                optionList.add(`OriginTracking(Name(keyword),getLine(),Name( currentFile())));
                option = ast().makeOptionList(optionList);
                constraint = ast().makeConstraintList(constraintList);
                nameList = `concTomName(Name(keyword));
                result = `RecordAppl(option,
                    nameList,
                    ast().makeList(pairSlotList),
                    constraint);
            }
        | // #PROCESSING-INSTRUCTION(... , ...)
            XML_PROC LPAREN
            arg1 = termStringIdentifier[null] COMMA arg2 = termStringIdentifier[null]
            RPAREN
            {
                keyword = Constants.PROCESSING_INSTRUCTION_NODE;
                pairSlotList.add(`PairSlotAppl(Name(Constants.SLOT_TARGET),arg1));
                pairSlotList.add(`PairSlotAppl(Name(Constants.SLOT_DATA),arg2));

                optionList.add(`OriginTracking(Name(keyword),getLine(),Name( currentFile())));
                option = ast().makeOptionList(optionList);
                constraint = ast().makeConstraintList(constraintList);
                nameList = `concTomName(Name(keyword));
                result = `RecordAppl(option,
                    nameList,
                    ast().makeList(pairSlotList),
                    constraint);
            }
        )
    ;


xmlAttributeList [LinkedList list] returns [boolean result] throws TomException
{
    result = false;
    TomTerm term;
}
    :
        (
            LBRACKET {text.append("[");}
            (
                term = xmlAttribute {list.add(term);}
                (
                    COMMA {text.append("(");}
                    term = xmlAttribute {list.add(term);}
                )*  
            )?
            RBRACKET
            {
                text.append("]");
                result = true;
            }
        |
            LPAREN {text.append("(");}
            (
                term = xmlAttribute {list.add(term);}
                (
                    COMMA {text.append(",");}
                    term = xmlAttribute {list.add(term);}
                )*  
            )?
            RPAREN
            {
                text.append(")");
                result = false;
            }
        |
            (
               {LA(1) != XML_CLOSE}? term = xmlAttribute {list.add(term);}
            )*
            {result = true;}
        )
    ;

xmlAttribute returns [TomTerm result] throws TomException
{
    result = null;
    LinkedList list = new LinkedList();
    TomTerm term = null;
    TomTerm termName = null;
    String name;
    OptionList option = null;
    ConstraintList constraint;
    LinkedList optionList = new LinkedList();
    LinkedList constraintList = new LinkedList();
    LinkedList optionListAnno2 = new LinkedList();
    NameList nameList;
    boolean varStar = false;
}
    :
        (
            // _* | X*
            {LA(2) == STAR}?
            result = variableStar[optionList,constraintList]
            {varStar = true;}
            //(variableStar[null,null]) => result = variableStar[optionList,constraintList]
        |   // name = [anno2@](_|String|Identifier)
            {LA(2) == EQUAL}?
            id:ALL_ID EQUAL {text.append(id.getText()+"=");}
            (

                {LA(2) == AT}?
                anno2:ALL_ID AT
                {
                    text.append(anno2.getText()+"@");
                    optionListAnno2.add(`Name(anno2.getText()));
                }
            )?
            term = unamedVariableOrTermStringIdentifier[optionListAnno2]
            {
                name = tomFactory.encodeXMLString(symbolTable(),id.getText());
                nameList = `concTomName(Name(name));
                termName = `Appl(ast().makeOption(),nameList,concTomTerm(),concConstraint());
            }
        | // [anno1@]_ = [anno2@](_|String|Identifier)
            (
                anno1:ALL_ID AT
                {
                    text.append(anno1.getText()+"@");
                    optionList.add(`Name(anno1.getText()));
                }
            )?
            termName = placeHolder[optionList,constraintList]
            e:EQUAL {text.append("=");}
            (
                {LA(2) == AT}?
                anno3:ALL_ID AT
                {
                    text.append(anno3.getText()+"@");
                    optionListAnno2.add(`Name(anno3.getText()));
                }
            )?
            term = unamedVariableOrTermStringIdentifier[optionListAnno2]
        )
        {
            if (!varStar)
            {
                list.add(`PairSlotAppl(Name(Constants.SLOT_NAME),termName));
                // we add the specif value : _
                list.add(`PairSlotAppl(Name(Constants.SLOT_SPECIFIED),Placeholder(ast().makeOption(),ast().makeConstraint())));
                //list.add(tomFactory.metaEncodeXMLAppl(symbolTable(),term));
                // no longer necessary ot metaEncode Strings in attributes
                list.add(`PairSlotAppl(Name(Constants.SLOT_VALUE),term));
                optionList.add(`OriginTracking(Name(Constants.ATTRIBUTE_NODE),getLine(),Name( currentFile())));
                option = ast().makeOptionList(optionList);            
                constraint = ast().makeConstraintList(constraintList);
                
                nameList = `concTomName(Name(Constants.ATTRIBUTE_NODE));
                result = `RecordAppl(option,
                    nameList,
                    ast().makeList(list),
                    constraint);
            }   
        }
    ;

// This corresponds to the implicit notation
xmlTermList [LinkedList list] returns [boolean result] throws TomException
{
    result = false;
    TomTerm term;
}
    :
        (
            term = annotedTerm {list.add(term);}
        )*
        {result = true;}
    ;

xmlNameList [LinkedList optionList, boolean needOrgTrack] returns [NameList result] throws TomException
{
    result = `emptyNameList();
    StringBuffer XMLName = new StringBuffer("");
    int decLine = 0;
}
    :
        (
            name:ALL_ID
            {
                text.append(name.getText());
                XMLName.append(name.getText());
                decLine = name.getLine();
                result = `concTomName(Name(name.getText()));
            }
        |   name2:UNDERSCORE
            {
                text.append(name2.getText());
                XMLName.append(name2.getText());
                decLine = name2.getLine();
                result = `concTomName(Name(name2.getText()));
            }
        |   LPAREN name3:ALL_ID
            {
                text.append(name3.getText());
                XMLName.append(name3.getText());
                decLine = name3.getLine();
                result = `concTomName(Name(name3.getText()));
            }
            (
                ALTERNATIVE name4:ALL_ID
                {
                    text.append("|"+name4.getText());
                    XMLName.append("|"+name4.getText());
                    result = (NameList)result.append(`Name(name4.getText()));
                }
            )*
            RPAREN
        )
        {
            if(needOrgTrack) {
                optionList.add(`OriginTracking(Name(XMLName.toString()),
                        decLine,
                        Name(currentFile())));
            }
        }
    ;

termStringIdentifier [LinkedList options] returns [TomTerm result] throws TomException
{
  result = null;
  LinkedList optionList = (options==null)?new LinkedList():options;
  OptionList option = null;
  NameList nameList = null;
}
    :
        
        (
            nameID:ALL_ID
            {
                text.append(nameID.getText());
                optionList.add(`OriginTracking(Name(nameID.getText()),nameID.getLine(),Name(currentFile())));
                option = ast().makeOptionList(optionList);
                nameList = `concTomName(Name(nameID.getText()));
            }
        |
            nameString:STRING
            {
                text.append(nameString.getText());
                optionList.add(`OriginTracking(Name(nameString.getText()),nameString.getLine(),Name(currentFile())));
                option = ast().makeOptionList(optionList);
                ast().makeStringSymbol(symbolTable(),nameString.getText(),optionList);
                nameList = `concTomName(Name(nameString.getText()));
            }
        )
        {
            result = `Appl(option,nameList,concTomTerm(),concConstraint());
        }
    ;


unamedVariableOrTermStringIdentifier [LinkedList options] returns [TomTerm result] throws TomException
{
  result = null;
  LinkedList optionList = (options==null)?new LinkedList():options;
  OptionList option = null;
  NameList nameList = null;
}
    :
        (
            nameUnderscore:UNDERSCORE
            {
                text.append(nameUnderscore.getText());
                optionList.add(`OriginTracking(Name(nameUnderscore.getText()),nameUnderscore.getLine(),Name(currentFile())));
                option = ast().makeOptionList(optionList);
                result = `UnamedVariable(option,TomTypeAlone("unknown type"),concConstraint());
            }
        |
            nameID:ALL_ID
            {
                text.append(nameID.getText());
                optionList.add(`OriginTracking(Name(nameID.getText()),nameID.getLine(),Name(currentFile())));
                option = ast().makeOptionList(optionList);
                nameList = `concTomName(Name(nameID.getText()));
                result = `Appl(option,nameList,concTomTerm(),concConstraint());
            }
        |
            nameString:STRING 
            {
                text.append(nameString.getText());
                optionList.add(`OriginTracking(Name(nameString.getText()),nameString.getLine(),Name(currentFile())));
                option = ast().makeOptionList(optionList);
                ast().makeStringSymbol(symbolTable(),nameString.getText(),optionList);
                nameList = `concTomName(Name(nameString.getText()));
                result = `Appl(option,nameList,concTomTerm(),concConstraint());
            }
        )
    ;

// return true for implicit mode
implicitTermList [LinkedList list] returns [boolean result] throws TomException
{
    result = false;
    TomTerm term;
}
    :
        (
            LBRACKET
            { text.append("["); }
            (
                term = annotedTerm { list.add(term); }
                (
                    COMMA { text.append(","); }
                    term = annotedTerm { list.add(term); }
                )*
            )?
            RBRACKET
            {
                text.append("]");
                result=true;
            }
        )
    ;


xmlChilds [LinkedList list] returns [boolean result] throws TomException
{
  result = false;
  LinkedList childs = new LinkedList();
  Iterator it;
}
    :
        (
            //(implicitTermList[null]) => 
            {LA(1) == LBRACKET}? result = implicitTermList[childs]
        |   result = xmlTermList[childs]
        )
        {
            it = childs.iterator();
            while(it.hasNext()) {
                list.add(tomFactory.metaEncodeXMLAppl(symbolTable(),(TomTerm)it.next()));
            }
        }
    ;


args [LinkedList list, LinkedList optionList] returns [boolean result] throws TomException
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

termList [LinkedList list] throws TomException
{
    TomTerm term = null;
}
    :   (
            term = annotedTerm {list.add(term);}
            ( COMMA {text.append(',');} term = annotedTerm {list.add(term);})*
        )
    ;

pairList [LinkedList list] throws TomException
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
                            String detailedMsg = TomMessage.getMessage("RepeatedSlotName", new Object[]{stringSlotName});
                            String msg = TomMessage.getMessage("MainErrorMessage", 
                                                               new Object[]{new Integer(ot.getLine()), "%op "+type.getText(), new Integer(ot.getLine()), currentFile(), detailedMsg});
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
                  logger.log( Level.WARNING, "WarningTwoSameSlotDecl",
                              new Object[]{currentFile(), new Integer(attribute.getOrgTrack().getLine()),
                                           "%op "+type.getText(), new Integer(ot.getLine()), sName.getString()} );
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
                }
            }
            
            TomSymbol astSymbol = ast().makeSymbol(name.getText(), type.getText(), types, slotList, options, tlFsym);
            putSymbol(name.getText(),astSymbol);

            result = `SymbolDecl(astName);

//            pushLine(t.getLine());
  //          pushColumn(t.getColumn());
            updatePosition(t.getLine(),t.getColumn());

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

//            pushLine(t.getLine());
  //          pushColumn(t.getColumn());
            updatePosition(t.getLine(),t.getColumn());

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

//            pushLine(t.getLine());
  //          pushColumn(t.getColumn());
            updatePosition(t.getLine(),t.getColumn());

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

//            pushLine(t.getLine());
  //          pushColumn(t.getColumn());
            updatePosition(t.getLine(),t.getColumn());

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

//            pushLine(t.getLine());
  //          pushColumn(t.getColumn());

            updatePosition(t.getLine(),t.getColumn());

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
            
//            pushLine(t.getLine());
  //          pushColumn(t.getColumn());
            updatePosition(t.getLine(),t.getColumn());

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
//                pushLine(l.getLine());
  //              pushColumn(l.getColumn());
                updatePosition(t.getLine(),t.getColumn());

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



class TomLexer extends Lexer;
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

//XML Tokens

XML_START   :   '<';
XML_CLOSE   :   '>' ;
DOUBLE_QUOTE:   '\"';
XML_TEXT    :   "#TEXT";
XML_COMMENT :   "#COMMENT";
XML_PROC    :   "#PROCESSING-INSTRUCTION";
XML_START_ENDING    : "</" ;
XML_CLOSE_SINGLETON : "/>" ;

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

protected LETTER    :   ('a'..'z' | 'A'..'Z')   ;
protected DIGIT     :   ('0'..'9')  ;

ALL_ID
options{testLiterals = true;}
    :
        (
            (ID_MINUS) => ID_MINUS
        |   ID
        )
    ;
        

protected ID
options{testLiterals = true;}
    :
        LETTER
        ( 
            options{greedy = true;}:
            ( LETTER | DIGIT | UNDERSCORE )
        )* 
    ;   

protected ID_MINUS
    :
        ID MINUS ('a'..'z' | 'A'..'Z') 
        ( 
            MINUS ('a'..'z' | 'A'..'Z') 
        |   ID
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
