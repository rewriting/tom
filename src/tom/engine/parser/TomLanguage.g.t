header{
/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2007, INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.parser;

}

{
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;
import tom.engine.xml.Constants;
import tom.platform.OptionManager;
import tom.platform.PlatformLogRecord;
import aterm.*;
import antlr.TokenStreamSelector;
}
class TomParser extends Parser;

options{
    k=1; // the lookahead value during parsing
    defaultErrorHandler = false;
}

    {
    //--------------------------
    %include{ adt/tomsignature/TomSignature.tom }
    //--------------------------
        
    public String currentFile(){
        return targetparser.getCurrentFile();
    }

    // the default-mode parser
    private HostParser targetparser;
    protected BackQuoteParser bqparser;
    private TomLexer tomlexer;

    //store information for the OriginalText contained in the OptionList
    private StringBuffer text = new StringBuffer();
    
    private int lastLine; 

    private SymbolTable symbolTable;

    public TomParser(ParserSharedInputState state, HostParser target,
                     OptionManager optionManager){
        this(state);
        this.targetparser = target;
        this.bqparser = new BackQuoteParser(state,this);
        this.tomlexer = (TomLexer) selector().getStream("tomlexer");
        this.symbolTable = target.getSymbolTable();
    }
    
    private void putTypeDefinition(String name, TomType type, TomForwardType forward) {
        symbolTable.putTypeDefinition(name,type,forward);
    }

    private void putSymbol(String name, TomSymbol symbol) {
        symbolTable.putSymbol(name,symbol);
    }
    
    private int getLine(){
        return tomlexer.getLine();
    }

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
    
    private Logger getLogger() {
      return Logger.getLogger(getClass().getName());
    }

}

constant returns [Token result]
{
    result = null;
}
  : (
          t1:NUM_INT {result = t1;}
        | t2:CHARACTER {result = t2;}
        | t3:STRING {result = t3;}
        | t4:NUM_FLOAT {result = t4;}
        | t5:NUM_LONG {result = t5;}
        | t6:NUM_DOUBLE {result = t6;}
        )
    ;

// the %match construct :
matchConstruct [Option ot] returns [Instruction result] throws TomException
{ 
    result = null;
    OptionList optionList = `concOption(ot,ModuleName(TomBase.DEFAULT_MODULE_NAME));
    LinkedList argumentList = new LinkedList();
    LinkedList patternInstructionList = new LinkedList();
    TomList subjectList = null;
}
  : (
            LPAREN matchArguments[argumentList] RPAREN 
            LBRACE { subjectList = ASTFactory.makeList(argumentList); }
            ( 
             patternInstruction[subjectList,patternInstructionList] 
            )* 
            t:RBRACE 
            { 
                result = `Match(
                    SubjectList(subjectList),
                    ASTFactory.makePatternInstructionList(patternInstructionList),
                    optionList
                );
                
                // update for new target block...
                updatePosition(t.getLine(),t.getColumn());
                
                // Match is finished : pop the tomlexer and return in
                // the target parser.  
                selector().pop(); 
            }
        )
  ;

matchArguments [LinkedList list] throws TomException
    :   
        ( matchArgument[list] ( COMMA matchArgument[list] )*)
    ;

matchArgument [LinkedList list] throws TomException
{
  TomTerm subject1 = null;
  TomTerm subject2 = null;
  TomType tomType = null;
}
    :   

    //(type:ALL_ID { tomType = `TomTypeAlone(type.getText()); })?
    //(BACKQUOTE)?
    subject1 = plainTerm[null,null,0] 
    (BACKQUOTE)?
    (subject2 = plainTerm[null,null,0])?
    {
      if(subject2==null) {
	//System.out.println("matchArgument = " + subject1);
	list.add(subject1); 
      } else {
	if(subject1.isVariable() && subject2.isVariable()) {
	  String type = subject1.getAstName().getString();
	  TomName name = subject2.getAstName();
	  //LinkedList optionList = new LinkedList();
	  //optionList.add(`OriginTracking(name,type.getLine(),currentFile()));
	  //list.add(`Variable(ASTFactory.makeOptionList(optionList),name,tomType,concConstraint()));
	  Option ot = `OriginTracking(name, lastLine, currentFile());
	  list.add(`Variable(concOption(ot),name,TomTypeAlone(type),concConstraint()));
	} else { 
	  throw new TomException(TomMessage.invalidMatchSubject, new Object[]{subject1, subject2});
	}
      }
    }
        ;

patternInstruction [TomList subjectList, LinkedList list] throws TomException
{
    LinkedList matchPatternList = new LinkedList();
    LinkedList listOfMatchPatternList = new LinkedList();
    LinkedList listTextPattern = new LinkedList();
    LinkedList listOrgTrackPattern = new LinkedList();
    LinkedList blockList = new LinkedList();

    LinkedList matchGuardsList = new LinkedList();

    Option option = null;

    clearText();
}
    :   (
            ( (ALL_ID COLON) => label:ALL_ID COLON )?
             option = matchPattern[matchPatternList] 
            {
                listOfMatchPatternList.add(ASTFactory.makeList(matchPatternList));
                matchPatternList.clear();
                listTextPattern.add(text.toString());
                clearText();
                listOrgTrackPattern.add(option);
            }
            ( 
                ALTERNATIVE option = matchPattern[matchPatternList] 
                {
                    listOfMatchPatternList.add(ASTFactory.makeList(matchPatternList));
                    matchPatternList.clear();
                    listTextPattern.add(text.toString());
                    clearText();
                    listOrgTrackPattern.add(option);

                getLogger().log(new PlatformLogRecord(Level.WARNING, TomMessage.deprecatedDisjunction,
                                new Object[]{currentFile(), new Integer(getLine())},
                                   currentFile(), getLine()));

                }
            )* 
            {
                matchGuardsList.clear();
            }
            ( WHEN matchGuards[matchGuardsList] )?
            ARROW t:LBRACE
            {
                // update for new target block
                updatePosition(t.getLine(),t.getColumn());
                
                // actions in target language : call the target lexer and
                // call the target parser
                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.targetLanguage(blockList);

                // target parser finished : pop the target lexer
                selector().pop();

                blockList.add(tlCode);
                OptionList optionList = `concOption();
                
                if(label != null){
                    optionList = `concOption(Label(Name(label.getText())));
                }

                TomList patterns = null;
                String patternText = null;
                /*
                 * The following loop splits disjuntions of patterns
                 * into several PatternInstructions
                 */
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

                    //System.out.println("pattern = " + `Pattern(subjectList,patterns,ASTFactory.makeList(matchGuardsList)));

                    list.add(`PatternInstruction(
                            Pattern(subjectList,patterns,ASTFactory.makeList(matchGuardsList)),
                            RawAction(AbstractBlock(ASTFactory.makeInstructionList(blockList))),
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
                result = `OriginTracking(Name("Pattern"),lastLine,currentFile());
            } 
            ( 
                COMMA {text.append('\n');}  
                term = annotedTerm {list.add(term);}
            )*
        )
    ;

matchGuards [LinkedList list] throws TomException
{
    TomTerm term = null;
}
    :   (
             term = annotedTerm 
            {
                list.add(term);
            }
            ( 
                COMMA {text.append('\n');}  
                term = annotedTerm {list.add(term);}
            )*
        )
    ;

extendsBqTerm returns [TomTerm bqTerm] throws TomException
{
  bqTerm = null;
}
  :
    EXTENDS //(BACKQUOTE)?
    {
    selector().push("bqlexer");
    bqTerm = bqparser.beginBackquote();
    //selector().pop();
    }
;

// The %strategy construct
strategyConstruct [Option orgTrack] returns [Declaration result] throws TomException
{
    result = null;
    TomTerm extendsTerm = null;
    TomType codomainType = null;
    LinkedList visitList = new LinkedList();
    TomVisitList astVisitList = `concTomVisit();
    TomName orgText = null;
    TomTypeList types = `concTomType();
    LinkedList options = new LinkedList();
    LinkedList slotNameList = new LinkedList();
    LinkedList pairNameDeclList = new LinkedList();
    String stringSlotName = null;

    clearText();
}
    :(
        name:ALL_ID
        {
	Option ot = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
        options.add(ot);
        if (symbolTable.getSymbolFromName(name.getText()) != null) {
	throw new TomException(TomMessage.invalidStrategyName, new Object[]{name.getText()});
        }
        }
        (
            LPAREN (slotName:ALL_ID COLON typeArg:ALL_ID 
            {
                stringSlotName = slotName.getText(); 
                TomName astName = `Name(stringSlotName);
                slotNameList.add(astName); 

                // Define get<slot> method.
                Option slotOption = `OriginTracking(Name(stringSlotName),slotName.getLine(),currentFile());
                TomTerm slotVar = `Variable(concOption(slotOption),Name("t"),TomTypeAlone("Strategy"),concConstraint());
                Instruction slotInst = `Return((TargetLanguageToTomTerm(ITL("((" + name.getText() + ")t).get" + stringSlotName + "()"))));
                Declaration slotDecl = `GetSlotDecl(Name(name.getText()),Name(stringSlotName),slotVar,slotInst,slotOption);

                pairNameDeclList.add(`PairNameDecl(astName,slotDecl)); 
                types = `concTomType(types*,TomTypeAlone(typeArg.getText()));
            }
            (
                COMMA
                slotName2:ALL_ID COLON typeArg2:ALL_ID
                {
                    stringSlotName = slotName2.getText(); 
                    TomName astName = ASTFactory.makeName(stringSlotName);
                    if(slotNameList.indexOf(astName) != -1) {
                      getLogger().log(new PlatformLogRecord(Level.SEVERE, TomMessage.repeatedSlotName,
                        new Object[]{stringSlotName},
                        currentFile(), getLine()));
                    }
                    slotNameList.add(astName); 

                    // Define get<slot> method.
                    Option slotOption = `OriginTracking(Name(stringSlotName),slotName.getLine(),currentFile());
                    TomTerm slotVar = `Variable(concOption(slotOption),Name("t"),TomTypeAlone("Strategy"),concConstraint());
                    Instruction slotInst = `Return((TargetLanguageToTomTerm(ITL("((" + name.getText() + ")t).get" + stringSlotName + "()"))));
                    Declaration slotDecl = `GetSlotDecl(Name(name.getText()),Name(stringSlotName),slotVar,slotInst,slotOption);

                    pairNameDeclList.add(`PairNameDecl(Name(stringSlotName),slotDecl)); 
                    types = `concTomType(types*,TomTypeAlone(typeArg2.getText()));
                }
            )*
            )? RPAREN
        )
        extendsTerm = extendsBqTerm
        {
        matchBlock: {
					%match(TomTerm extendsTerm) {
						Composite(concTomTerm(BackQuoteAppl[AstName=astName],_*)) -> {
							codomainType = `Codomain(astName);
							break matchBlock;
						}
						_ -> {
							throw new TomException(TomMessage.malformedStrategy,
									new Object[]{currentFile(), new Integer(getLine()),
									"strat","a composite",extendsTerm.toString()});
						}
					}
                    }
        }
        LBRACE
        strategyVisitList[visitList]{astVisitList = ASTFactory.makeTomVisitList(visitList);}
        t:RBRACE
       {
         //initialize arrayList with argument names
				 TomList makeArgs = `concTomTerm();
         int index = 0;
         TomTypeList makeTypes = types;//keep a copy of types
				 String makeTlCode = "new " + name.getText() + "(";
         while(!makeTypes.isEmptyconcTomType()) {
					 String argName = "t"+index;
           if (index>0) {//if many parameters
             makeTlCode = makeTlCode.concat(",");
           }
					 makeTlCode += argName;

           TomTerm arg = `Variable(concOption(),Name(argName),makeTypes.getHeadconcTomType(),concConstraint());
           makeArgs = `concTomTerm(makeArgs*,arg);
           
					 makeTypes = makeTypes.getTailconcTomType();
           index++;
         }
				 makeTlCode += ")";

				 Option makeOption = `OriginTracking(Name(name.getText()),t.getLine(),currentFile());
				 Declaration makeDecl = `MakeDecl(Name(name.getText()), codomainType,makeArgs, TargetLanguageToInstruction(ITL(makeTlCode)), makeOption);
          options.add(makeDecl);

          // Define the is_fsym method.
          Option fsymOption = `OriginTracking(Name(name.getText()),t.getLine(),currentFile());
          TomTerm fsymVar = `Variable(concOption(fsymOption),Name("t"),TomTypeAlone("Strategy"),concConstraint());
          Declaration fsymDecl = `IsFsymDecl(Name(name.getText()),fsymVar,Return(TargetLanguageToTomTerm(ITL("(t instanceof " + name.getText() + ")"))),fsymOption);
          options.add(fsymDecl);

          TomSymbol astSymbol = ASTFactory.makeSymbol(name.getText(), codomainType, types, ASTFactory.makePairNameDeclList(pairNameDeclList), options);
          putSymbol(name.getText(),astSymbol);
          // update for new target block...
          updatePosition(t.getLine(),t.getColumn());

          result = `AbstractDecl(concDeclaration(Strategy(Name(name.getText()),extendsTerm,astVisitList,orgTrack),SymbolDecl(Name(name.getText()))));

          // %strat finished: go back in target parser.
            selector().pop();
        }
     )
    ;

strategyVisitList [LinkedList list] throws TomException
    :   ( strategyVisit[list] )* ;

strategyVisit [LinkedList list] throws TomException
{
  LinkedList patternInstructionList = new LinkedList();
  TomType vType = null;
  TomList subjectList = `concTomTerm();

  clearText();
}
    :   
  (
    visit:ALL_ID { 
      if (!"visit".equals(visit.getText())) {
        throw new TomException(TomMessage.malformedStrategy,
          new Object[]{currentFile(), new Integer(getLine()),
          "strat","visit",visit.getText()});
      }
    }
    type:ALL_ID  LBRACE
    {
    vType = `TomTypeAlone(type.getText());
    subjectList = `concTomTerm(TomTypeToTomTerm(vType));
    }
    (  
      patternInstruction[subjectList,patternInstructionList] 
    )* 
    RBRACE
  )
  {
    LinkedList optionList = new LinkedList();
    optionList.add(`OriginTracking(Name(type.getText()),type.getLine(),currentFile()));
    OptionList options = ASTFactory.makeOptionList(optionList);
    list.add(`VisitTerm(vType,ASTFactory.makePatternInstructionList(patternInstructionList),options));
  }
;


// The %rule construct
ruleConstruct [Option ot] returns [Declaration result] throws TomException
{
    result = null;
    TomRuleList ruleList = `concTomRule();
    TomTerm lhs = null, rhs = null, pattern = null, subject = null;
    TomList listOfLhs = `concTomTerm();
    InstructionList conditionList = `concInstruction();
    TomName orgText = null;
    OptionList optionList = `concOption(ot,ModuleName(TomBase.DEFAULT_MODULE_NAME));
    clearText();
}
    :
        LBRACE
        (
            lhs = annotedTerm 
            {listOfLhs = `concTomTerm(lhs);}
            ( ALTERNATIVE {text.append('|');} lhs = annotedTerm
                { listOfLhs = `concTomTerm(listOfLhs*,lhs); }
            )*
 
            ARROW {orgText = `Name(text.toString());} rhs = plainTerm[null,null,0]
            (
                WHERE pattern = annotedTerm AFFECT subject = annotedTerm 
                { conditionList = `concInstruction(conditionList*,MatchingCondition(pattern,subject)); }
            |   IF pattern = annotedTerm DOUBLEEQ subject = annotedTerm
                { conditionList = `concInstruction(conditionList*,EqualityCondition(pattern,subject)); }
            )*
            
            {
                // get the last token's line
                int line = lastLine;
                Option ot2 = `OriginTracking( Name("Pattern"), line, currentFile()
                );
                OptionList optionList2 = `concOption(ot2,OriginalText(orgText),ModuleName(TomBase.DEFAULT_MODULE_NAME));
                while(! listOfLhs.isEmptyconcTomTerm()){
                    ruleList = `concTomRule(ruleList*,
                        RewriteRule(
                            Term(listOfLhs.getHeadconcTomTerm()),
                            Term(rhs),
                            conditionList,
                            optionList2
                        )
                    );
                    listOfLhs = listOfLhs.getTailconcTomTerm();
                }
                
                conditionList = `concInstruction();
                clearText();
            }
        )*
        t:RBRACE
        {
            
            // update for new target block...
            updatePosition(t.getLine(),t.getColumn());

            result = `RuleSet(ruleList,optionList);

            // %rule finished: go back in target parser.
            selector().pop();
        }
    ;


// terms for %match and %rule
annotedTerm returns [TomTerm result] throws TomException
{
    result = null;
    TomName labeledName = null;
    TomName annotedName = null;
    int line = 0;
}
    :   (
            ( 
                (ALL_ID COLON) => lname:ALL_ID COLON 
                {
                    text.append(lname.getText());
                    text.append(':');
                    labeledName = `Name(lname.getText());
                    line = lname.getLine();
                }
            )? 
            ( 
                (ALL_ID AT) => name:ALL_ID AT 
                {
                    text.append(name.getText());
                    text.append('@');
                    annotedName = `Name(name.getText());
                    line = name.getLine();
                }
            )? 
            
            result = plainTerm[labeledName,annotedName,line] 
        )
    ;

plainTerm [TomName astLabeledName, TomName astAnnotedName, int line] returns [TomTerm result] throws TomException
{
    result = null;
    LinkedList constraintList = new LinkedList();
    LinkedList optionList = new LinkedList();
    LinkedList secondOptionList = new LinkedList();
    TomNameList nameList = `concTomName();
    TomName name = null;
    LinkedList list = new LinkedList();
    boolean implicit = false;
    boolean anti = false;

    if(astLabeledName != null) {
      constraintList.add(ASTFactory.makeStorePosition(astLabeledName, line, currentFile()));
    }
    if(astAnnotedName != null) {
      constraintList.add(ASTFactory.makeAssignTo(astAnnotedName, line, currentFile()));
    }

}
    :  	(a:ANTI_SYM {anti = !anti;} )*    	
        (   // xml term
            result = xmlTerm[optionList, constraintList]
            {
            	if (anti){
    		    	result = `AntiTerm(result);
    		    }
            }

        |   // var* or _*
        	{!anti}? // do not allow anti symbols on var* or _* 
            (variableStar[null,null]) => result = variableStar[optionList,constraintList] 

        |   // _
            {!anti}? // do not allow anti symbols on _
        	result = unamedVariable[optionList,constraintList] 

        |   // for a single constant. 
            // ambiguous with the next rule so :
        	{LA(2) != LPAREN && LA(2) != LBRACKET}? 
            name = headSymbol[optionList] 
            {
	      result = `Variable(ASTFactory.makeOptionList(optionList),name,
		  TomTypeAlone("unknown type"),ASTFactory.makeConstraintList(constraintList));
	      if (anti) {
		result = `AntiTerm(result);
	      }
            }            

        |   // for a single constant. 
            // ambiguous with the next rule so :
       	{LA(2) != LPAREN && LA(2) != LBRACKET}? 
            nameList = headConstantList[optionList] 
            {
	      //nameList = `concTomName(nameList*,name);
	      optionList.add(`Constant());
	      result = `TermAppl(
		  ASTFactory.makeOptionList(optionList),
		  nameList,
		  ASTFactory.makeList(list),
		  ASTFactory.makeConstraintList(constraintList));
	      if (anti) {
		result = `AntiTerm(result);
	      }
            }

        |   // f(...) or f[...] or !f(...) or !f[...]
            name = headSymbol[optionList] 
            { nameList = `concTomName(nameList*,name); }
            implicit = args[list,secondOptionList]
            {
              if(implicit) {
                    result = `RecordAppl(
                        ASTFactory.makeOptionList(optionList),
                        nameList,
                        ASTFactory.makeSlotList(list),
                        ASTFactory.makeConstraintList(constraintList)
                    );
              } else {
                    result = `TermAppl(
                        ASTFactory.makeOptionList(optionList),
                        nameList,
                        ASTFactory.makeList(list),
                        ASTFactory.makeConstraintList(constraintList)
                    );
              }
              if(anti) {
                result = `AntiTerm(result);
              }
            }
            
        |   // (f|g...) 
            // ambiguity with the last rule so use a lookahead
            // if ALTERNATIVE then parse headSymbolList
       	{LA(3) == ALTERNATIVE || LA(4) == ALTERNATIVE}? nameList = headSymbolList[optionList] 
            implicit = args[list, secondOptionList] 
            {
              if(implicit) {
                    result = `RecordAppl(
                        ASTFactory.makeOptionList(optionList),
                        nameList,
                        ASTFactory.makeSlotList(list),
                        ASTFactory.makeConstraintList(constraintList)
                    );
              } else {
                    result = `TermAppl(
                        ASTFactory.makeOptionList(optionList),
                        nameList,
                        ASTFactory.makeList(list),
                        ASTFactory.makeConstraintList(constraintList)
                    );
              }
              if(anti) {
                  result = `AntiTerm(result);
              }
            }
        |   // (...)
            implicit = args[list,secondOptionList]
            {
                nameList = `concTomName(Name(""));
                optionList.addAll(secondOptionList);
                result = `TermAppl(
                    ASTFactory.makeOptionList(optionList),
                    nameList,
                    ASTFactory.makeList(list),
                    ASTFactory.makeConstraintList(constraintList)
                );
                if(anti) {
                    result = `AntiTerm(result);
                }
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
  TomNameList nameList, closingNameList;
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
                    option =  ASTFactory.makeOptionList(optionList);
                }
            |   // case: > childs  </NODE>
                XML_CLOSE  {text.append(">");}
                implicit = xmlChilds[childs]

                XML_START_ENDING {text.append("</"); }
                closingNameList = xmlNameList[optionList, false]
                t:XML_CLOSE  {text.append(">");}
                {
                    if(!nameList.equals(closingNameList)) {
                        StringBuffer found = new StringBuffer();
                        StringBuffer expected = new StringBuffer();
                        while(!nameList.isEmptyconcTomName()) {
                            expected.append("|"+nameList.getHeadconcTomName().getString());
                            nameList = nameList.getTailconcTomName();
                        }
                        while(!closingNameList.isEmptyconcTomName()) {
                            found.append("|"+closingNameList.getHeadconcTomName().getString());
                            closingNameList = closingNameList.getTailconcTomName();
                        }
                        // TODO find the orgTrack of the match
                        throw new TomException(TomMessage.malformedXMLTerm,
                            new Object[]{currentFile(), new Integer(getLine()), 
                            "match", expected.substring(1), found.substring(1)});
                    }
                    if(implicit) {
                        // Special case when XMLChilds() is reduced to a singleton
                        // when XMLChilds() is reduced to a singleton
                        // Appl(...,Name(""),args)
                        if(ASTFactory.isExplicitTermList(childs)) {
                            childs = ASTFactory.metaEncodeExplicitTermList(symbolTable, (TomTerm)childs.getFirst());
                        } else {
                            optionList.add(`ImplicitXMLChild());
                        }
                    }
                    option = ASTFactory.makeOptionList(optionList);    
                }
            ) // end choice
            {
                result = `XMLAppl(
                    option,
                    nameList,
                    ASTFactory.makeList(attributeList),
                    ASTFactory.makeList(childs),
                    ASTFactory.makeConstraintList(constraintList));
            }

        | // #TEXT(...)
            XML_TEXT LPAREN arg1 = annotedTerm RPAREN
            {
                keyword = Constants.TEXT_NODE;
                pairSlotList.add(`PairSlotAppl(Name(Constants.SLOT_DATA),arg1));

                optionList.add(`OriginTracking(Name(keyword),getLine(),currentFile()));
                option = ASTFactory.makeOptionList(optionList);
                constraint = ASTFactory.makeConstraintList(constraintList);
                nameList = `concTomName(Name(keyword));
                result = `RecordAppl(option,
                    nameList,
                    ASTFactory.makeSlotList(pairSlotList),
                    constraint);
            }
        | // #COMMENT(...)
            XML_COMMENT LPAREN arg1 = termStringIdentifier[null] RPAREN
            {
                keyword = Constants.COMMENT_NODE;
                pairSlotList.add(`PairSlotAppl(Name(Constants.SLOT_DATA),arg1));

                optionList.add(`OriginTracking(Name(keyword),getLine(),currentFile()));
                option = ASTFactory.makeOptionList(optionList);
                constraint = ASTFactory.makeConstraintList(constraintList);
                nameList = `concTomName(Name(keyword));
                result = `RecordAppl(option,
                    nameList,
                    ASTFactory.makeSlotList(pairSlotList),
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

                optionList.add(`OriginTracking(Name(keyword),getLine(),currentFile()));
                option = ASTFactory.makeOptionList(optionList);
                constraint = ASTFactory.makeConstraintList(constraintList);
                nameList = `concTomName(Name(keyword));
                result = `RecordAppl(option,
                    nameList,
                    ASTFactory.makeSlotList(pairSlotList),
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
    LinkedList slotList = new LinkedList();
    TomTerm term = null;
    TomTerm termName = null;
    String name;
    OptionList option = null;
    ConstraintList constraint;
    LinkedList optionList = new LinkedList();
    LinkedList constraintList = new LinkedList();
    LinkedList optionListAnno2 = new LinkedList();
    TomNameList nameList;
    boolean varStar = false;
    boolean anti = false;
}
    :
        (
            // _* | X*
            {LA(2) == STAR}?
            result = variableStar[optionList,constraintList] {varStar = true;}
        |   // name = [anno2@](_|String|Identifier)
            {LA(2) == EQUAL}?  id:ALL_ID EQUAL {text.append(id.getText()+"=");}
            (
                {LA(2) == AT}? anno2:ALL_ID AT
                {
                    text.append(anno2.getText()+"@");
                    optionListAnno2.add(`Name(anno2.getText()));
                }
            )?
            (a:ANTI_SYM {anti = !anti;} )*
            term = unamedVariableOrTermStringIdentifier[optionListAnno2]
            {
                name = ASTFactory.encodeXMLString(symbolTable,id.getText());
                nameList = `concTomName(Name(name));
                termName = `TermAppl(concOption(),nameList,concTomTerm(),concConstraint());                
            }
        | // [anno1@]_ = [anno2@](_|String|Identifier)
            (
                anno1:ALL_ID AT
                {
                    text.append(anno1.getText()+"@");
                    optionList.add(`Name(anno1.getText()));
                }
            )?
            termName = unamedVariable[optionList,constraintList]
            e:EQUAL {text.append("=");}
            (
                {LA(2) == AT}? anno3:ALL_ID AT
                {
                    text.append(anno3.getText()+"@");
                    optionListAnno2.add(`Name(anno3.getText()));
                }
            )?
            (b:ANTI_SYM {anti = !anti;} )*		
            term = unamedVariableOrTermStringIdentifier[optionListAnno2]
        )
        {
            if (!varStar) {
            	
            	if (anti){
            		term = `AntiTerm(term);
            	}
            	
                slotList.add(`PairSlotAppl(Name(Constants.SLOT_NAME),termName));
                // we add the specif value : _
                slotList.add(`PairSlotAppl(Name(Constants.SLOT_SPECIFIED),UnamedVariable(concOption(),TomTypeAlone("unknown type"),concConstraint())));
                // no longer necessary ot metaEncode Strings in attributes
                slotList.add(`PairSlotAppl(Name(Constants.SLOT_VALUE),term));
                optionList.add(`OriginTracking(Name(Constants.ATTRIBUTE_NODE),getLine(),currentFile()));
                option = ASTFactory.makeOptionList(optionList);            
                constraint = ASTFactory.makeConstraintList(constraintList);
                
                nameList = `concTomName(Name(Constants.ATTRIBUTE_NODE));
                result = `RecordAppl(option,
                    nameList,
                    ASTFactory.makeSlotList(slotList),
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

xmlNameList [LinkedList optionList, boolean needOrgTrack] returns [TomNameList result] throws TomException
{
    result = `concTomName();
    StringBuffer XMLName = new StringBuffer("");
    int decLine = 0;
    boolean anti = false;
}
    :    	
        (
        	(a:ANTI_SYM {anti = !anti;} )*	
            name:ALL_ID
            {
                text.append(name.getText());
                XMLName.append(name.getText());
                decLine = name.getLine();                
                if (anti) { 
                	result =  `concTomName(AntiName(Name(name.getText())));
                }else{
	               	result = `concTomName(Name(name.getText()));
                }
            }
        |   name2:UNDERSCORE
            {
                text.append(name2.getText());
                XMLName.append(name2.getText());
                decLine = name2.getLine();
                result = `concTomName(Name(name2.getText()));
            }
        |   LPAREN (b:ANTI_SYM {anti = !anti;} )* name3:ALL_ID
            {
                text.append(name3.getText());
                XMLName.append(name3.getText());
                decLine = name3.getLine();
                if (anti) { 
                	result =  `concTomName(AntiName(Name(name3.getText())));
                }else{
                	result = `concTomName(Name(name3.getText()));
                }

            }
            (
                ALTERNATIVE (c:ANTI_SYM {anti = !anti;} )* name4:ALL_ID
                {
                    text.append("|"+name4.getText());
                    XMLName.append("|"+name4.getText());
                    if (anti) { 
                    	result = `concTomName(result*,AntiName(Name(name4.getText())));
                    }else{
                    	result = `concTomName(result*,Name(name4.getText()));
                    }
                }
            )*
            RPAREN
        )
        {
            if(needOrgTrack) {
                optionList.add(`OriginTracking(Name(XMLName.toString()), decLine, currentFile()));
            }
        }
    ;

termStringIdentifier [LinkedList options] returns [TomTerm result] throws TomException
{
  result = null;
  LinkedList optionList = (options==null)?new LinkedList():options;
  OptionList option = null;
  TomNameList nameList = null;
}
    :
        (
            nameID:ALL_ID
            {
                text.append(nameID.getText());
                optionList.add(`OriginTracking(Name(nameID.getText()),nameID.getLine(),currentFile()));
                option = ASTFactory.makeOptionList(optionList);
                result = `Variable(option,Name(nameID.getText()),TomTypeAlone("unknown type"),concConstraint());
            }
        |
            nameString:STRING
            {
                text.append(nameString.getText());
                optionList.add(`OriginTracking(Name(nameString.getText()),nameString.getLine(),currentFile()));
                option = ASTFactory.makeOptionList(optionList);
                ASTFactory.makeStringSymbol(symbolTable,nameString.getText(),optionList);
                nameList = `concTomName(Name(nameString.getText()));
								result = `TermAppl(option,nameList,concTomTerm(),concConstraint());
            }
        )
    ;


unamedVariableOrTermStringIdentifier [LinkedList options] returns [TomTerm result] throws TomException
{
  result = null;
  LinkedList optionList = (options==null)?new LinkedList():options;
  OptionList option = null;
  TomNameList nameList = null;
}
    :
        (
            result = unamedVariable[optionList, new LinkedList()]
        |
            nameID:ALL_ID
            {
                text.append(nameID.getText());
                optionList.add(`OriginTracking(Name(nameID.getText()),nameID.getLine(),currentFile()));
                option = ASTFactory.makeOptionList(optionList);
                result = `Variable(option,Name(nameID.getText()),TomTypeAlone("unknown type"),concConstraint());
            }
        |
            nameString:STRING 
            {
                text.append(nameString.getText());
                optionList.add(`OriginTracking(Name(nameString.getText()),nameString.getLine(),currentFile()));
                option = ASTFactory.makeOptionList(optionList);
                ASTFactory.makeStringSymbol(symbolTable,nameString.getText(),optionList);
                nameList = `concTomName(Name(nameString.getText()));
                result = `TermAppl(option,nameList,concTomTerm(),concConstraint());
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
                list.add(ASTFactory.metaEncodeXMLAppl(symbolTable,(TomTerm)it.next()));
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
                optionList.add(`OriginTracking(Name(""),t1.getLine(),currentFile()));
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
                optionList.add(`OriginTracking(Name(""),t3.getLine(),currentFile()));
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
                
                optionList.add(`OriginTracking(Name(name),line,currentFile()));
                options = ASTFactory.makeOptionList(optionList);
                constraints = ASTFactory.makeConstraintList(constraintList);
                if(name1 == null) {
                    result = `UnamedVariableStar(
                        options,
                        TomTypeAlone("unknown type"),
                        constraints
                    );
                } else {
                    result = `VariableStar(
                        options,
                        Name(name),
                        TomTypeAlone("unknown type"),
                        constraints
                    );
                }
            }
        )
    ;

// _
unamedVariable [LinkedList optionList, LinkedList constraintList] returns [TomTerm result]
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

                optionList.add(`OriginTracking(Name(t.getText()),t.getLine(),currentFile()));
                options = ASTFactory.makeOptionList(optionList);
                constraints = ASTFactory.makeConstraintList(constraintList);
                result = `UnamedVariable(options,TomTypeAlone("unknown type"),constraints);
            } 
        )
    ;

// ( id | id | ...)
headSymbolList [LinkedList optionList] returns [TomNameList result]
{ 
    result = `concTomName();
    TomName name = null;
}
    :
        (
            LPAREN {text.append('(');}
            name = headSymbolOrConstant[optionList] 
            {
            	result = `concTomName(result*,name);            	
            }

            ALTERNATIVE {text.append('|');}
            name = headSymbolOrConstant[optionList] 
            {
            	result = `concTomName(result*,name);                
            }

            ( 
                ALTERNATIVE {text.append('|');}
                name = headSymbolOrConstant[optionList] 
                {
                	result = `concTomName(result*,name);                    
                }
            )* 
            t:RPAREN 
            {
                text.append(t.getText());
                setLastLine(t.getLine());
            }
        )
    ;

headSymbolOrConstant [LinkedList optionList] returns [TomName result]
{
  result = null;
} : ( result = headSymbol[optionList]
    | result = headConstant[optionList]
    );

headSymbol [LinkedList optionList] returns [TomName result]
{ 
    result = null; 
}
: 
  (i:ALL_ID 
  {
		String name = i.getText();
		int line = i.getLine();
		text.append(name);
		setLastLine(line);
		result = `Name(name);
		optionList.add(`OriginTracking(result,line, currentFile()));
	}
	)
;

headConstantList [LinkedList optionList] returns [TomNameList result]
{
    result = `concTomName();
    TomName name = null;
} :
  name=headConstant[optionList] { result = `concTomName(result*,name); }
  (
    ALTERNATIVE { text.append('|'); }
    name=headConstant[optionList] { result = `concTomName(result*,name); }
  )*
;

headConstant [LinkedList optionList] returns [TomName result]
{ 
    result = null;
    Token t;
} : 
        t=constant // add to symbol table
{
	String name = t.getText();
	int line = t.getLine();
	text.append(name);
	setLastLine(line);
	result = `Name(name);
	optionList.add(`OriginTracking(result,line, currentFile()));

	switch(t.getType()) {
		case NUM_INT:
			ASTFactory.makeIntegerSymbol(symbolTable,name,optionList);
			break;
		case NUM_LONG:
			ASTFactory.makeLongSymbol(symbolTable,name,optionList);
			break;
		case CHARACTER:
			ASTFactory.makeCharSymbol(symbolTable,name,optionList);
			break;
		case NUM_DOUBLE:
			ASTFactory.makeDoubleSymbol(symbolTable,name,optionList);
			break;
		case STRING:
			ASTFactory.makeStringSymbol(symbolTable,name,optionList);
			break;
		default:
	}
}
;

// Operator Declaration
operator returns [Declaration result] throws TomException
{
    result=null;
    Option ot = null;
    TomTypeList types = `concTomType();
    LinkedList options = new LinkedList();
    LinkedList slotNameList = new LinkedList();
    LinkedList pairNameDeclList = new LinkedList();
    TomName astName = null;
    String stringSlotName = null;
    Declaration attribute;
}
    :
      type:ALL_ID name:ALL_ID 
        {
            ot = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
            options.add(ot);
        }
        (
            LPAREN (slotName:ALL_ID COLON typeArg:ALL_ID 
            {
                stringSlotName = slotName.getText(); 
                astName = `Name(stringSlotName);
                slotNameList.add(astName); 
                pairNameDeclList.add(`PairNameDecl(astName,EmptyDeclaration())); 
                types = `concTomType(types*,TomTypeAlone(typeArg.getText()));
            }
            (
                COMMA
                slotName2:ALL_ID COLON typeArg2:ALL_ID
                {
                    stringSlotName = slotName2.getText(); 
                    astName = ASTFactory.makeName(stringSlotName);
                    if(slotNameList.indexOf(astName) != -1) {
                      getLogger().log(new PlatformLogRecord(Level.SEVERE, TomMessage.repeatedSlotName,
                        new Object[]{stringSlotName},
                        currentFile(), getLine()));
                    }
                    slotNameList.add(astName); 
                    pairNameDeclList.add(`PairNameDecl(Name(stringSlotName),EmptyDeclaration())); 
                    types = `concTomType(types*,TomTypeAlone(typeArg2.getText()));
                }
            )*
            )? RPAREN
        )
        (
        LBRACE
        {
            astName = `Name(name.getText());
        }
        (
            attribute = keywordMake[name.getText(),`TomTypeAlone(type.getText()),types]
            { options.add(attribute); }

        |   attribute = keywordGetSlot[astName,type.getText()]
            {
              TomName sName = attribute.getSlotName();
              /*
               * ensure that sName appears in slotNameList, only once
               * ensure that sName has not already been generated
               */
              //System.out.println("slotNameList = " + slotNameList);
              //System.out.println("sName      = " + sName);

              TomMessage msg = null;
              int index = slotNameList.indexOf(sName);
              if(index == -1) {
                msg = TomMessage.errorIncompatibleSlotDecl;
              } else {
                PairNameDecl pair = (PairNameDecl) pairNameDeclList.get(index);
                %match(PairNameDecl pair) {
                  PairNameDecl[SlotDecl=decl] -> {
                    if(`decl != `EmptyDeclaration()) {
                      msg = TomMessage.errorTwoSameSlotDecl;
                    }
                  }
                }
              }
              if(msg != null) {
                getLogger().log(new PlatformLogRecord(Level.SEVERE, msg,
                      new Object[]{currentFile(), new Integer(attribute.getOrgTrack().getLine()),
                      "%op "+type.getText(), new Integer(ot.getLine()), sName.getString()} ,
                    currentFile(), getLine()));
              } else {
                pairNameDeclList.set(index,`PairNameDecl(sName,attribute));
              }
            }   
        |   attribute = keywordIsFsym[astName,type.getText()]
            { options.add(attribute); }
        )*
        t:RBRACE
	)
        {

          //System.out.println("pairNameDeclList = " + pairNameDeclList);

          TomSymbol astSymbol = ASTFactory.makeSymbol(name.getText(), `TomTypeAlone(type.getText()), types, ASTFactory.makePairNameDeclList(pairNameDeclList), options);
          putSymbol(name.getText(),astSymbol);
          result = `SymbolDecl(astName);
          updatePosition(t.getLine(),t.getColumn());
          selector().pop(); 
        }
    ;

operatorList returns [Declaration result] throws TomException
{
    result = null;
    TomTypeList types = `concTomType();
    LinkedList options = new LinkedList();
    Declaration attribute = null;
}
    :
        type:ALL_ID name:ALL_ID
        {
            Option ot = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
            options.add(ot);
        }
        LPAREN typeArg:ALL_ID STAR RPAREN
        {
            types = `concTomType(types*,TomTypeAlone(typeArg.getText()));
        }
        LBRACE
        (
            attribute = keywordMakeEmptyList[name.getText()]
            { options.add(attribute); }

        |   attribute = keywordMakeAddList[name.getText(),type.getText(),typeArg.getText()]
            { options.add(attribute); }

        |   attribute = keywordIsFsym[`Name(name.getText()), type.getText()]
            { options.add(attribute); }

        |   attribute = keywordGetHead[`Name(name.getText()), type.getText()]
            { options.add(attribute); }
        |   attribute = keywordGetTail[`Name(name.getText()), type.getText()]
            { options.add(attribute); }
        |   attribute = keywordIsEmpty[`Name(name.getText()), type.getText()]
            { options.add(attribute); }

        )*
        t:RBRACE
        { 
            PairNameDeclList pairNameDeclList = `concPairNameDecl(PairNameDecl(EmptyName(), EmptyDeclaration()));
            TomSymbol astSymbol = ASTFactory.makeSymbol(name.getText(), `TomTypeAlone(type.getText()), types, pairNameDeclList, options);
            putSymbol(name.getText(),astSymbol);
            result = `ListSymbolDecl(Name(name.getText()));
            updatePosition(t.getLine(),t.getColumn());
            selector().pop(); 
        }
    ;

operatorArray returns [Declaration result] throws TomException
{
    result = null;
    TomTypeList types = `concTomType();
    LinkedList options = new LinkedList();
    Declaration attribute = null;
}
    :
        type:ALL_ID name:ALL_ID
        {
            Option ot = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
            options.add(ot);
        }
        LPAREN typeArg:ALL_ID STAR RPAREN
        {
            types = `concTomType(types*,TomTypeAlone(typeArg.getText()));
        }
        LBRACE
        (
            attribute = keywordMakeEmptyArray[name.getText(),type.getText()]
            { options.add(attribute); }

        |   attribute = keywordMakeAddArray[name.getText(),type.getText(),typeArg.getText()]
            { options.add(attribute); }

        |   attribute = keywordIsFsym[`Name(name.getText()),type.getText()]
            { options.add(attribute); }

        |   attribute = keywordGetElement[`Name(name.getText()), type.getText()]
            { options.add(attribute); }
        |   attribute = keywordGetSize[`Name(name.getText()), type.getText()]
            { options.add(attribute); }
        )*
        t:RBRACE
        { 
            PairNameDeclList pairNameDeclList = `concPairNameDecl(PairNameDecl(EmptyName(), EmptyDeclaration()));
            TomSymbol astSymbol = ASTFactory.makeSymbol(name.getText(), `TomTypeAlone(type.getText()), types, pairNameDeclList, options);
            putSymbol(name.getText(),astSymbol);

            result = `ArraySymbolDecl(Name(name.getText()));

            updatePosition(t.getLine(),t.getColumn());

            selector().pop(); 
        }
    ;

typeTerm returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
    Declaration attribute = null;
    TargetLanguage implement = null;
    TomForwardType tomFwdType = `EmptyForward();
    DeclarationList declarationList = `concDeclaration();
		String s;
}
    :   (
            type:ALL_ID
            { 
                ot = `OriginTracking(Name(type.getText()), type.getLine(),currentFile());
            }
            LBRACE

            implement = keywordImplement
            (    s = keywordVisitorFwd
								{ tomFwdType = `TLForward(s); }
            |   attribute = keywordEquals[type.getText()]
                { declarationList = `concDeclaration(attribute,declarationList*); }
            |   attribute = keywordIsSort[type.getText()]
                { /* nothing */ }
            |   attribute = keywordCheckStamp[type.getText()]
                { declarationList = `concDeclaration(attribute,declarationList*); }
            |   attribute = keywordSetStamp[type.getText()]
                { declarationList = `concDeclaration(attribute,declarationList*); }
            |   attribute = keywordGetImplementation[type.getText()]
                { declarationList = `concDeclaration(attribute,declarationList*); }
            )*
            t:RBRACE
        )
{
  TomType astType = `Type(ASTTomType(type.getText()),TLType(implement));
          putTypeDefinition(type.getText(), astType, tomFwdType); 
          result = `TypeTermDecl(Name(type.getText()),declarationList,ot);
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

keywordVisitorFwd returns [String code] throws TomException
{
  code = "";
}
    :
        (
           VISITOR_FWD 
            {
                selector().push("targetlexer");
                code = targetparser.goalLanguage(new LinkedList()).getCode();
                selector().pop();
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
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
            LPAREN name1:ALL_ID COMMA name2:ALL_ID RPAREN
            {
                Option info1 = `OriginTracking(Name(name1.getText()),name1.getLine(),currentFile());
                Option info2 = `OriginTracking(Name(name2.getText()),name2.getLine(),currentFile());
                OptionList option1 = `concOption(info1);
                OptionList option2 = `concOption(info2);
                
                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop();  
                
                result = `TermsEqualDecl(
                    Variable(option1,Name(name1.getText()),TomTypeAlone(type),concConstraint()),
                    Variable(option2,Name(name2.getText()),TomTypeAlone(type),concConstraint()),
                    Return(TargetLanguageToTomTerm(tlCode)), ot);
            }
        )
    ;

keywordIsSort[String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:IS_SORT LPAREN name:ALL_ID RPAREN
            {
                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop();  
                result = null;
            }
        )
    ;


keywordGetHead[TomName opname, String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:GET_HEAD 
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
            LPAREN name:ALL_ID RPAREN
            {
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
                OptionList option = `concOption(info);

                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop();  

                result = `GetHeadDecl(opname,
                    symbolTable.getUniversalType(),
                    Variable(option,Name(name.getText()),TomTypeAlone(type),concConstraint()),
                    Return(TargetLanguageToTomTerm(tlCode)),
                    ot);
            }
        )
    ;

keywordGetTail[TomName opname, String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:GET_TAIL 
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
            LPAREN name:ALL_ID RPAREN
            {
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
                OptionList option = `concOption(info);

                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop();  

                result = `GetTailDecl(opname,
                    Variable(option,Name(name.getText()),TomTypeAlone(type),concConstraint()),
                    Return(TargetLanguageToTomTerm(tlCode)),
                    ot);
            }
        )
    ;

keywordIsEmpty[TomName opname, String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:IS_EMPTY 
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
            LPAREN name:ALL_ID RPAREN
            {
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
                OptionList option = `concOption(info);

                selector().push("targetlexer");
                TargetLanguage  tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop(); 

                result = `IsEmptyDecl(opname,
                    Variable(option,Name(name.getText()),TomTypeAlone(type),concConstraint()),
                    Return(TargetLanguageToTomTerm(tlCode)),
                    ot); 
            }
        )
    ;

keywordGetElement[TomName opname, String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:GET_ELEMENT 
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
            LPAREN name1:ALL_ID COMMA name2:ALL_ID RPAREN
            {
                Option info1 = `OriginTracking(Name(name1.getText()),name1.getLine(),currentFile());
                Option info2 = `OriginTracking(Name(name2.getText()),name2.getLine(),currentFile());
                OptionList option1 = `concOption(info1);
                OptionList option2 = `concOption(info2);
                
                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop();  
                
                result = `GetElementDecl(opname,
                    Variable(option1,Name(name1.getText()),TomTypeAlone(type),concConstraint()),
                    Variable(option2,Name(name2.getText()),TomTypeAlone("int"),concConstraint()),
                    Return(TargetLanguageToTomTerm(tlCode)), ot);
            }
        )
    ;

keywordGetSize[TomName opname, String type] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        (
            t:GET_SIZE
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
            LPAREN name:ALL_ID RPAREN
            {
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
                OptionList option = `concOption(info);

                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop();  

                result = `GetSizeDecl(opname,
                    Variable(option,Name(name.getText()),TomTypeAlone(type),concConstraint()),
                    Return(TargetLanguageToTomTerm(tlCode)),ot);
            }
        )
    ;

keywordIsFsym [TomName astName, String typeString] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        t:IS_FSYM
        { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
        LPAREN name:ALL_ID RPAREN
        {
            Option info = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
            OptionList option = `concOption(info);

            selector().push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            selector().pop();

            result = `IsFsymDecl(astName,
                Variable(option,Name(name.getText()),TomTypeAlone(typeString),concConstraint()),
                Return(TargetLanguageToTomTerm(tlCode)),ot);
        }
    ;

keywordCheckStamp [String typeString] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        t:CHECK_STAMP
        { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
        LPAREN name:ALL_ID RPAREN
        {
            Option info = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
            OptionList option = `concOption(info);

            selector().push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            selector().pop();

            result = `CheckStampDecl(Variable(option,Name(name.getText()),TomTypeAlone(typeString),concConstraint()),
                TargetLanguageToInstruction(tlCode),ot);
        }
    ;

keywordSetStamp [String typeString] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        t:SET_STAMP
        { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
        LPAREN name:ALL_ID RPAREN
        {
            Option info = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
            OptionList option = `concOption(info);

            selector().push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            selector().pop();

            result = `SetStampDecl(Variable(option,Name(name.getText()),TomTypeAlone(typeString),concConstraint()),
                Return(TargetLanguageToTomTerm(tlCode)),ot);
        }
    ;

keywordGetImplementation [String typeString] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        t:GET_IMPLEMENTATION
        { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
        LPAREN name:ALL_ID RPAREN
        {
            Option info = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
            OptionList option = `concOption(info);

            selector().push("targetlexer");
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
            selector().pop();

            result = `GetImplementationDecl(Variable(option,Name(name.getText()),TomTypeAlone(typeString),concConstraint()),
                Return(TargetLanguageToTomTerm(tlCode)),ot);
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
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
            LPAREN slotName:ALL_ID COMMA name:ALL_ID RPAREN
            {                
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
                OptionList option = `concOption(info);
                
                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList());
                selector().pop(); 

                result = `GetSlotDecl(astName,
                    Name(slotName.getText()),
                    Variable(option,Name(name.getText()),TomTypeAlone(type),concConstraint()),
                    Return(TargetLanguageToTomTerm(tlCode)), ot);
            }
        )
    ;

keywordMake [String opname, TomType returnType, TomTypeList types] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
    TomList args = `concTomTerm();
    int index = 0;
    TomType type;
    int nbTypes = types.length();
}
    :
        (
            t:MAKE
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
                (LPAREN 
                ( 
                    typeArg:ALL_ID
                    {
                        if( !(nbTypes > 0) ) {
                            type = `EmptyType();
                        } else {
                            type = TomBase.elementAt(types,index++);
                        }
                        Option info1 = `OriginTracking(Name(typeArg.getText()),typeArg.getLine(),currentFile());  
                        OptionList option1 = `concOption(info1);
                        
                        args = `concTomTerm(args*,Variable(
                                option1,
                                Name(typeArg.getText()),
                                type,concConstraint()
                            ));
                    }
                    ( 
                        COMMA nameArg:ALL_ID
                        {
                            if( index >= nbTypes ) {
                                type = `EmptyType();
                            } else {
                              type = TomBase.elementAt(types,index++);
                            }
                            Option info2 = `OriginTracking(Name(nameArg.getText()),nameArg.getLine(),currentFile());
                            OptionList option2 = `concOption(info2);
                            
                            args = `concTomTerm(args*,Variable(
                                    option2,
                                    Name(nameArg.getText()),
                                    type,concConstraint()
                                ));
                        }
                    )*
                )? 
                RPAREN )?
            l:LBRACE
            {
                updatePosition(t.getLine(),t.getColumn());
                selector().push("targetlexer");
                LinkedList blockList = new LinkedList();
                TargetLanguage tlCode = targetparser.targetLanguage(blockList);
                selector().pop();
                blockList.add(tlCode);
                result = `MakeDecl(Name(opname),returnType,args,AbstractBlock(ASTFactory.makeInstructionList(blockList)),ot);
            }
        )
    ;

keywordMakeEmptyList[String name] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        t:MAKE_EMPTY (LPAREN RPAREN)?
        { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
        
        LBRACE
        {   
            selector().push("targetlexer");
            LinkedList blockList = new LinkedList();
            TargetLanguage tlCode = targetparser.targetLanguage(blockList);
            selector().pop();
            blockList.add(tlCode);
            result = `MakeEmptyList(Name(name),AbstractBlock(ASTFactory.makeInstructionList(blockList)),ot);
        }
    ;

keywordMakeAddList[String name, String listType, String elementType] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        t:MAKE_INSERT
        {ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile());}

        LPAREN elementName:ALL_ID COMMA listName:ALL_ID RPAREN
        LBRACE
        {
            Option listInfo = `OriginTracking(Name(listName.getText()),listName.getLine(),currentFile());  
            Option elementInfo = `OriginTracking(Name(elementName.getText()),elementName.getLine(),currentFile());
            OptionList listOption = `concOption(listInfo);
            OptionList elementOption = `concOption(elementInfo);

            selector().push("targetlexer");
            LinkedList blockList = new LinkedList();
            TargetLanguage tlCode = targetparser.targetLanguage(blockList);
            selector().pop();
            blockList.add(tlCode);
            result = `MakeAddList(Name(name),
                Variable(elementOption,Name(elementName.getText()),TomTypeAlone(elementType),concConstraint()),
                Variable(listOption,Name(listName.getText()),TomTypeAlone(listType),concConstraint()),
                AbstractBlock(ASTFactory.makeInstructionList(blockList)),ot);
        }
    ;

keywordMakeEmptyArray[String name, String listType] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        t:MAKE_EMPTY { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
        LPAREN listName:ALL_ID RPAREN
        LBRACE
        {
            Option listInfo = `OriginTracking(Name(listName.getText()),listName.getLine(),currentFile());  
            OptionList listOption = `concOption(listInfo);

            selector().push("targetlexer");
            LinkedList blockList = new LinkedList();
            TargetLanguage tlCode = targetparser.targetLanguage(blockList);
            selector().pop();
            blockList.add(tlCode);
            result = `MakeEmptyArray(Name(name),
                Variable(listOption,Name(listName.getText()),TomTypeAlone(listType),concConstraint()),
                AbstractBlock(ASTFactory.makeInstructionList(blockList)),ot);
        }
    ;   

keywordMakeAddArray[String name, String listType, String elementType] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
}
    :
        t:MAKE_APPEND { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
          LPAREN elementName:ALL_ID COMMA listName:ALL_ID RPAREN
          LBRACE
        {
            selector().push("targetlexer");
            LinkedList blockList = new LinkedList();
            TargetLanguage tlCode = targetparser.targetLanguage(blockList);
            selector().pop();
            blockList.add(tlCode);

            Option listInfo = `OriginTracking(Name(listName.getText()),listName.getLine(),currentFile());  
            Option elementInfo = `OriginTracking(Name(elementName.getText()),elementName.getLine(),currentFile());
            OptionList listOption = `concOption(listInfo);
            OptionList elementOption = `concOption(elementInfo);
            
            result = `MakeAddArray(Name(name),
                Variable(elementOption,Name(elementName.getText()),TomTypeAlone(elementType),concConstraint()),
                Variable(listOption,Name(listName.getText()),TomTypeAlone(listType),concConstraint()),
                AbstractBlock(ASTFactory.makeInstructionList(blockList)),ot);
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
    EXTENDS="extends";
    MAKE_EMPTY = "make_empty";
    MAKE_INSERT = "make_insert";
    MAKE_APPEND = "make_append";
    MAKE = "make";
    GET_SLOT = "get_slot";
    IS_FSYM = "is_fsym";
    CHECK_STAMP = "check_stamp";
    SET_STAMP = "set_stamp";
    GET_IMPLEMENTATION = "get_implementation";
    EQUALS = "equals";
    IS_SORT = "is_sort";
    GET_HEAD = "get_head";
    GET_TAIL = "get_tail";
    IS_EMPTY = "is_empty";
    IMPLEMENT = "implement";
    VISITOR_FWD = "visitor_fwd";
    STAMP = "stamp";
    GET_ELEMENT = "get_element";
    GET_SIZE = "get_size";
    WHEN = "when";
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
UNDERSCORE  :   {!Character.isJavaIdentifierPart(LA(2))}? '_' ; 
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
WS  : ( ' '
    | '\t'
    | '\f'
    // handle newlines
    | ( "\r\n"  // Evil DOS
      | '\r'    // Macintosh
      | '\n'    // Unix (the right way)
      )
      { newline(); }
    )
        { $setType(Token.SKIP); }
  ;


// tokens to skip : Single Line Comments
SLCOMMENT
  : "//"
    (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
    {
            $setType(Token.SKIP); 
            newline();
        }
  ;

// tokens to skip : Multi Lines Comments
ML_COMMENT
  : "/*"
    ( 
      options {
        generateAmbigWarnings=false;
      }
    :
      { LA(2)!='/' }? '*'
    | '\r' '\n'   {newline();if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");}
    | '\r'      {newline();if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");}
    | '\n'      {newline();if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");}
    | ~('*'|'\n'|'\r'){if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");}
    )*
    "*/"
    {$setType(Token.SKIP);}
  ;


CHARACTER
  : '\'' ( ESC | ~('\''|'\n'|'\r'|'\\') )+ '\''
  ;

STRING
  : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"'
  ;

ANTI_SYM  : '!';

protected
ESC
  : '\\'
    ( 'n'
    | 'r'
    | 't'
    | 'b'
    | 'f'
    | '"'
    | '\''
    | '\\'
    | ('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    | '0'..'3'
      (
        options {
          warnWhenFollowAmbig = false;
        }
      : '0'..'7'
        (
          options {
            warnWhenFollowAmbig = false;
          }
        : '0'..'7'
        )?
      )?
    | '4'..'7'
      (
        options {
          warnWhenFollowAmbig = false;
        }
      : '0'..'7'
      )?
    )
  ;

protected
HEX_DIGIT
  : ('0'..'9'|'A'..'F'|'a'..'f')
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
        ('_')? LETTER
        ( 
            options{greedy = true;}:
            ( LETTER | DIGIT | '_' )
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
    :   (MINUS)?
    (
    DOT
            ( ('0'..'9')+ (EXPONENT)? (f1:FLOAT_SUFFIX {t=f1;})?
                {
        if (t != null && t.getText().toUpperCase().indexOf('F')>=0) {
                  _ttype = NUM_FLOAT;
        }
        else {
                  _ttype = NUM_DOUBLE; // assume double
        }
        }
            )?

  | ( '0' {isDecimal = true;} // special case for just '0'
      ( ('x'|'X')
        (                     // hex
          // the 'e'|'E' and float suffix stuff look
          // like hex digits, hence the (...)+ doesn't
          // know when to stop: ambig.  ANTLR resolves
          // it correctly by matching immediately.  It
          // is therefor ok to hush warning.
          options {
            warnWhenFollowAmbig=false;
          }
        : HEX_DIGIT
        )+

      | //float or double with leading zero
        (('0'..'9')+ ('.'|EXPONENT|FLOAT_SUFFIX)) => ('0'..'9')+

      | ('0'..'7')+                 // octal
      )?
    | ('1'..'9') ('0'..'9')*  {isDecimal=true;}   // non-zero decimal
    )
    ( ('l'|'L') { _ttype = NUM_LONG; }

    // only check to see if it's a float if looks like decimal so far
    | {isDecimal}?
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
    )
  ;
protected MINUS         :   '-' ;
protected PLUS          :   '+' ;
protected QUOTE         :   '\''    ;
protected EXPONENT      :   ('e'|'E') ( PLUS | MINUS )? ('0'..'9')+  ;
protected DOT           :   '.' ;
protected FLOAT_SUFFIX  : 'f'|'F'|'d'|'D' ;
