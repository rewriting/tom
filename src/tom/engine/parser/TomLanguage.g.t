header{
/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2011, INPL, INRIA
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
import java.io.StringReader;
import java.util.*;
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
import tom.engine.adt.code.types.*;

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
    %include{ ../adt/tomsignature/TomSignature.tom }
    //--------------------------

    public String currentFile() {
        return targetparser.getCurrentFile();
    }

    // the default-mode parser
    private HostParser targetparser;
    protected BackQuoteParser bqparser;
    private TomLexer tomlexer;

    //store information for the OriginalText contained in the OptionList
    private StringBuilder text = new StringBuilder();

    private int lastLine;

    private SymbolTable symbolTable;

    public TomParser(ParserSharedInputState state, HostParser target,
                     OptionManager optionManager) {
        this(state);
        this.targetparser = target;
        this.tomlexer = (TomLexer) selector().getStream("tomlexer");
        this.symbolTable = target.getSymbolTable();
        this.bqparser = new BackQuoteParser(state,this);
    }

    private void putType(String name, TomType type) {
        symbolTable.putType(name,type);
    }

    private TomType getType(String name) {
        return symbolTable.getType(name);
    }

    private void putSymbol(String name, TomSymbol symbol) {
        symbolTable.putSymbol(name,symbol);
    }

    private int getLine() {
        return tomlexer.getLine();
    }

    public void updatePosition(int i, int j) {
        targetparser.updatePosition(i,j);
    }

    public void addTargetCode(Token t) {
        targetparser.addTargetCode(t);
    }

    private void setLastLine(int line) {
        lastLine = line;
    }

    private void clearText() {
        text.delete(0,text.length());
    }

    protected TokenStreamSelector selector() {
        return targetparser.getSelector();
    }

    private Logger getLogger() {
      return Logger.getLogger(getClass().getName());
    }

}

// the %match construct :
matchConstruct [Option ot] returns [Instruction result] throws TomException
{
    result = null;
    OptionList optionList = `concOption(ot,ModuleName(TomBase.DEFAULT_MODULE_NAME));
    List<BQTerm> argumentList = new LinkedList<BQTerm>();
    List<TomType> subjectTypeList = new LinkedList<TomType>();
    List<ConstraintInstruction> constraintInstructionList = new LinkedList<ConstraintInstruction>();
    BQTermList subjectList = null;
    TomType patternType = SymbolTable.TYPE_UNKNOWN;
}
  : (
            LPAREN matchArguments[argumentList,subjectTypeList] RPAREN
            LBRACE 
            { 
              subjectList = ASTFactory.makeBQTermList(argumentList); 
            }
            (
             patternInstruction[subjectList,subjectTypeList,constraintInstructionList,patternType]
            )*
            t1:RBRACE
            {
               result = `Match(ASTFactory.makeConstraintInstructionList(constraintInstructionList),optionList);
               // update for new target block...
               updatePosition(t1.getLine(),t1.getColumn());
               // Match is finished : pop the tomlexer and return in the target parser.
               selector().pop();
            }
            |
            LBRACE { subjectList = ASTFactory.makeBQTermList(argumentList); }
            (
             constraintInstruction[constraintInstructionList,patternType]
            )*
            t2:RBRACE
            {
               result = `Match(ASTFactory.makeConstraintInstructionList(constraintInstructionList),optionList);
               // update for new target block...
               updatePosition(t2.getLine(),t2.getColumn());
               // Match is finished : pop the tomlexer and return in the target parser.
               selector().pop();
            }

        )
  ;

matchArguments [List<BQTerm> list, List<TomType> typeList] throws TomException
    :
        ( matchArgument[list,typeList] ( COMMA matchArgument[list,typeList] )*)
    ;

matchArgument [List<BQTerm> list, List<TomType> typeList] throws TomException
{
  BQTerm subject1 = null;
  BQTerm subject2 = null;

  String s1 = null;
  String s2 = null;
}
    :

    subject1 = plainBQTerm {
      s1 = text.toString();
      text.delete(0, text.length());
    }
    (BACKQUOTE { text.delete(0, text.length()); } )?
    (subject2 = plainBQTerm { s2 = text.toString(); })?
    {
      if(subject2==null) {
        list.add(subject1);
        typeList.add(SymbolTable.TYPE_UNKNOWN);
      } else {
        if(subject1.isBQVariable()) {
          String typeName = subject1.getAstName().getString();
          %match(subject2) {
            (BQVariable|BQAppl|BuildConstant)[] -> {
              list.add(subject2);
              typeList.add(`Type(concTypeOption(),typeName,EmptyTargetLanguageType()));
              return;
            }
          }
        }
        throw new TomException(TomMessage.invalidMatchSubject, new Object[]{subject1, subject2});
      }
    }
    ;

patternInstruction [BQTermList subjectList, List<TomType> subjectTypeList, List<ConstraintInstruction> list, TomType rhsType] throws TomException
{
    List<Option> optionListLinked = new LinkedList<Option>();
    List<TomTerm> matchPatternList = new LinkedList<TomTerm>();

    Constraint constraint = `TrueConstraint();
    Constraint constr = null;
    OptionList optionList = null;
    Option option = null;

    boolean isAnd = false;

    clearText();
}
    :   (
            ( (ALL_ID COLON) => label:ALL_ID COLON )?
             option = matchPattern[matchPatternList,true]
            {
              if(matchPatternList.size() != subjectList.length()) {
                TomMessage.error(getLogger(),currentFile(), getLine(),
                    TomMessage.badMatchNumberArgument, 
                    Integer.valueOf(subjectList.length()), Integer.valueOf(matchPatternList.size()));
                return;
              }

              int counter = 0;
              %match(subjectList) {
                concBQTerm(_*,subjectAtIndex,_*) -> {
                constraint =
                `AndConstraint(constraint,MatchConstraint(matchPatternList.get(counter),subjectAtIndex,subjectTypeList.get(counter)));
                  counter++;
                }
              }

              optionList = `concOption(option, OriginalText(Name(text.toString())));

              matchPatternList.clear();
              clearText();
            }
            (
              (
               AND_CONNECTOR { isAnd = true;}
               | OR_CONNECTOR  { isAnd = false;}
              )
//              constr = matchConstraintComposition[optionListLinked]
              constr = matchOrConstraint[optionListLinked]
              {
                constraint = isAnd ? `AndConstraint(constraint,constr) : `OrConstraint(constraint,constr);
              }
            )?
            arrowAndAction[list,optionList,optionListLinked,label,rhsType,constraint]
        )
    ;

visitInstruction [List<ConstraintInstruction> list, TomType rhsType] throws TomException
{
    List<Option> optionListLinked = new LinkedList<Option>();
    List<TomTerm> matchPatternList = new LinkedList<TomTerm>();

    Constraint constraint = `TrueConstraint();
    Constraint constr = null;
    OptionList optionList = null;
    Option option = null;

    boolean isAnd = false;

    List<Code> blockList = new LinkedList<Code>();
    BQTerm rhsTerm = null;

    clearText();
}
    :   (
            ( (ALL_ID COLON) => label:ALL_ID COLON )?
             option = matchPattern[matchPatternList,true]
            {
            int subjectListLength = 1;
              if(matchPatternList.size() != subjectListLength) {
                TomMessage.error(getLogger(),currentFile(), getLine(),
                    TomMessage.badMatchNumberArgument,
                    subjectListLength, Integer.valueOf(matchPatternList.size()));
                return;
              }

              BQTerm subject =
              `BQVariable(concOption(),Name("tom__arg"),SymbolTable.TYPE_UNKNOWN);
              constraint =
              `AndConstraint(constraint,MatchConstraint(matchPatternList.get(0),subject,rhsType));
              //optionList = `concOption(option, OriginalText(Name(text.toString())));

              matchPatternList.clear();
              clearText();
            }
            (
              (
               AND_CONNECTOR { isAnd = true;}
               | OR_CONNECTOR  { isAnd = false;}
              )
              constr = matchOrConstraint[optionListLinked]
              {
                constraint = isAnd ? `AndConstraint(constraint,constr) : `OrConstraint(constraint,constr);
              }
            )?
   ARROW
   {
       optionList = `concOption();
       for(Option op:optionListLinked) {
         optionList = `concOption(optionList*,op);
       }
       optionList = `concOption(optionList*,OriginalText(Name(text.toString())));
       if (label != null) {
         optionList = `concOption(Label(Name(label.getText())),optionList*);
       }

   }
(t:LBRACE
 {
 // update for new target block
 updatePosition(t.getLine(),t.getColumn());
 // actions in target language : call the target lexer and
 // call the target parser
 selector().push("targetlexer");
 TargetLanguage tlCode = targetparser.targetLanguage(blockList);
 // target parser finished : pop the target lexer
 selector().pop();
 blockList.add(`TargetLanguageToCode(tlCode));
 list.add(`ConstraintInstruction(
     constraint,
     RawAction(If(TrueTL(),AbstractBlock(ASTFactory.makeInstructionList(blockList)),Nop())),
     optionList)
   );
 }
 | rhsTerm = plainBQTerm
 {
 // case where the rhs of a rule is an algebraic term
 list.add(`ConstraintInstruction(
     constraint,
     Return(rhsTerm),
     optionList)
   );

     }
)
)
;

arrowAndAction[List<ConstraintInstruction> list, OptionList optionList, List<Option> optionListLinked, Token label, TomType rhsType, Constraint constraint] throws TomException
{
  List<Code> blockList = new LinkedList<Code>();
  BQTerm rhsTerm = null;
} :
   ARROW
   {
       optionList = `concOption();
       for(Option op:optionListLinked) {
         optionList = `concOption(optionList*,op);
       }
       optionList = `concOption(optionList*,OriginalText(Name(text.toString())));
       if(label != null) {
         optionList = `concOption(Label(Name(label.getText())),optionList*);
       }
   }
   t:LBRACE
   {
       // update for new target block
       updatePosition(t.getLine(),t.getColumn());
       // actions in target language : call the target lexer and
       // call the target parser
       selector().push("targetlexer");
       TargetLanguage tlCode = targetparser.targetLanguage(blockList);
       // target parser finished : pop the target lexer
       selector().pop();
       blockList.add(`TargetLanguageToCode(tlCode));
       list.add(`ConstraintInstruction(
           constraint,
           RawAction(If(TrueTL(),AbstractBlock(ASTFactory.makeInstructionList(blockList)),Nop())),
           optionList)
       );
   }
  ;


constraintInstruction [List<ConstraintInstruction> list, TomType rhsType] throws TomException
{
    List<Option> optionListLinked = new LinkedList<Option>();
    Constraint constraint = `TrueConstraint();
    clearText();
}
    :   (
            //constraint = matchConstraintComposition[optionListLinked]
            constraint = matchOrConstraint[optionListLinked]
            arrowAndAction[list,null,optionListLinked,null,rhsType,constraint]
        )
    ;

matchOrConstraint [List<Option> optionListLinked] returns [Constraint result] throws TomException
  {
    result = null;
    Constraint constr = null;
  } :
    result = matchAndConstraint[optionListLinked]
    (
        OR_CONNECTOR
        constr = matchAndConstraint[optionListLinked]
        {
          result = `OrConstraint(result,constr);
        }
    )*
  ;

matchAndConstraint [List<Option> optionListLinked] returns [Constraint result] throws TomException
  {
    result = null;
    Constraint constr = null;
  } :
    result = matchParanthesedConstraint[optionListLinked]
    (
        AND_CONNECTOR
        constr = matchParanthesedConstraint[optionListLinked]
        {
          result = `AndConstraint(result,constr);
        }
    )*
  ;

matchParanthesedConstraint [List<Option> optionListLinked] returns [Constraint result] throws TomException
    {
      result = null;
      List<TomTerm> matchPatternList = new LinkedList<TomTerm>();
    } :
      (matchPattern[matchPatternList,true]) => result = constraint[optionListLinked]
      | LPAREN result = matchOrConstraint[optionListLinked] RPAREN
    ;

constraint [List<Option> optionListLinked] returns [Constraint result] throws TomException:
(matchPattern[null,true] MATCH_CONSTRAINT) => result = matchConstraint[optionListLinked]
| result = numericConstraint
;

matchConstraint [List<Option> optionListLinked] returns [Constraint result] throws TomException
{
  List<TomTerm> matchPatternList = new LinkedList<TomTerm>();
  List<BQTerm> matchSubjectList = new LinkedList<BQTerm>();
  List<TomType> matchSubjectTypeList = new LinkedList<TomType>();
  Option option = null;
  result = null;
  int consType = -1;
}
:
    option = matchPattern[matchPatternList,true]
    MATCH_CONSTRAINT
    matchArgument[matchSubjectList,matchSubjectTypeList]
    {
      optionListLinked.add(option);
      TomTerm left  = matchPatternList.get(0);
      BQTerm right = matchSubjectList.get(0);
      TomType type = matchSubjectTypeList.get(0);
      return `MatchConstraint(left,right,type);
    }
;

numericConstraint returns [Constraint result] throws TomException
{
  //TODO could be simplified without using matchArgument rule
  // we are sure to have only one argument in each case
  List<BQTerm> matchLhsList = new LinkedList<BQTerm>();
  List<BQTerm> matchRhsList = new LinkedList<BQTerm>();
  result = null;
  int consType = -1;
  BQTerm subject1 = null;
  BQTerm subject2 = null;
}
:
    (BACKQUOTE { text.delete(0, text.length()); } )?
    subject1 = plainBQTerm {
      text.delete(0, text.length());
      matchLhsList.add(subject1);
    }

    consType=numconstraintType

    (BACKQUOTE { text.delete(0, text.length()); } )?
    subject2 = plainBQTerm {
      text.delete(0, text.length());
      matchRhsList.add(subject2);
    }
    {
      BQTerm left  = matchLhsList.get(0);
      BQTerm right = matchRhsList.get(0);

    switch(consType) {
        case XML_START : {
          return `NumericConstraint(left,right, NumLessThan());
        }
        case LESSOREQUAL_CONSTRAINT : {
          return `NumericConstraint(left,right, NumLessOrEqualThan());
        }
        case XML_CLOSE : {
          return `NumericConstraint(left,right, NumGreaterThan());
        }
        case GREATEROREQUAL_CONSTRAINT : {
          return `NumericConstraint(left,right, NumGreaterOrEqualThan());
        }
        case DIFFERENT_CONSTRAINT : {
          return `NumericConstraint(left,right, NumDifferent());
        }
        case DOUBLEEQ : {
          return `NumericConstraint(left,right, NumEqual());
        }
      }
      // should never reach this statement because of the parsing error that should occur before
      throw new TomException(TomMessage.invalidConstraintType);
  }
;

numconstraintType returns [int result]
{
  result = -1;
}
:   (
      XML_START             { result = XML_START; }
      | LESSOREQUAL_CONSTRAINT      { result = LESSOREQUAL_CONSTRAINT; }
      | XML_CLOSE          { result = XML_CLOSE; }
      | GREATEROREQUAL_CONSTRAINT   { result = GREATEROREQUAL_CONSTRAINT; }
      | DOUBLEEQ                    { result = DOUBLEEQ; }
      | DIFFERENT_CONSTRAINT        { result = DIFFERENT_CONSTRAINT; }
    )
;

matchPattern [List<TomTerm> list, boolean allowImplicit] returns [Option result] throws TomException
{
    result = null;
    TomTerm term = null;
}
    :   (
             term = annotatedTerm[allowImplicit]
            {
                list.add(term);
                result = `OriginTracking(Name("Pattern"),lastLine,currentFile());
            }
            (
                COMMA {text.append('\n');}
                term = annotatedTerm[allowImplicit] {list.add(term);}
            )*
        )
    ;

// The %strategy construct
strategyConstruct [Option orgTrack] returns [Declaration result] throws TomException
{
    result = null;
    BQTerm extendsBQTerm = null;
    List<TomVisit> visitList = new LinkedList<TomVisit>();
    TomVisitList astVisitList = `concTomVisit();
    TomName orgText = null;
    TomTypeList types = `concTomType();
    List<Option> options = new LinkedList<Option>();
    List<TomName> slotNameList = new LinkedList<TomName>();
    List<PairNameDecl> pairNameDeclList = new LinkedList<PairNameDecl>();
    String stringSlotName = null;
    String stringTypeArg = null;

    clearText();
}
    :(
        name:ALL_ID
        {
        Option ot = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
        options.add(ot);
        if(symbolTable.getSymbolFromName(name.getText()) != null) {
          throw new TomException(TomMessage.invalidStrategyName, new Object[]{name.getText()});
        }
        }
        (
            LPAREN (firstSlot1:ALL_ID (colon:COLON)? secondSlot1:ALL_ID
            {
              if(colon != null) {
                stringSlotName = firstSlot1.getText();
                stringTypeArg = secondSlot1.getText();
                } else {
                stringSlotName = secondSlot1.getText();
                stringTypeArg = firstSlot1.getText();
                }
                TomName astName = `Name(stringSlotName);
                slotNameList.add(astName);

                TomType strategyType = `Type(concTypeOption(),"Strategy",EmptyTargetLanguageType());

                // Define get<slot> method.
                Option slotOption = `OriginTracking(Name(stringSlotName),firstSlot1.getLine(),currentFile());
                String varname = "t";
                BQTerm slotVar = `BQVariable(concOption(slotOption),Name(varname),strategyType);
                String code = ASTFactory.abstractCode("((" + name.getText() + ")$"+varname+").get" + stringSlotName + "()",varname);
                Declaration slotDecl = `GetSlotDecl(Name(name.getText()),Name(stringSlotName),slotVar, Code(code), slotOption);

                pairNameDeclList.add(`PairNameDecl(astName,slotDecl));
                types = `concTomType(types*,Type(concTypeOption(),stringTypeArg,EmptyTargetLanguageType()));
            }
            (
                COMMA
                firstSlot2:ALL_ID (colon2:COLON)? secondSlot2:ALL_ID
                {
              if(colon != null) {
              stringSlotName = firstSlot2.getText();
              stringTypeArg = secondSlot2.getText();
              } else {
              stringSlotName = secondSlot2.getText();
              stringTypeArg = firstSlot2.getText();
              }
              TomName astName = ASTFactory.makeName(stringSlotName);
              if(slotNameList.indexOf(astName) != -1) {
                TomMessage.error(getLogger(),currentFile(), getLine(),
                    TomMessage.repeatedSlotName,
                    stringSlotName);
              }
              slotNameList.add(astName);

                TomType strategyType = `Type(concTypeOption(),"Strategy",EmptyTargetLanguageType());
                    // Define get<slot> method.
                    Option slotOption = `OriginTracking(Name(stringSlotName),firstSlot2.getLine(),currentFile());
                    String varname = "t";
                    BQTerm slotVar = `BQVariable(concOption(slotOption),Name(varname),strategyType);
                    String code = ASTFactory.abstractCode("((" + name.getText() + ")$"+varname+").get" + stringSlotName + "()",varname);
                    Declaration slotDecl = `GetSlotDecl(Name(name.getText()),Name(stringSlotName),slotVar, Code(code), slotOption);

                    pairNameDeclList.add(`PairNameDecl(Name(stringSlotName),slotDecl));
                    types = `concTomType(types*,Type(concTypeOption(),stringTypeArg,EmptyTargetLanguageType()));
                }
            )*
            )? RPAREN
        )
        EXTENDS (BACKQUOTE)? extendsBQTerm = plainBQTerm
        LBRACE
        strategyVisitList[visitList] { astVisitList = ASTFactory.makeTomVisitList(visitList); }
        t:RBRACE
       {
         //initialize arrayList with argument names
				 BQTermList makeArgs = `concBQTerm();
         int index = 0;
         TomTypeList makeTypes = types;//keep a copy of types
				 String makeTlCode = "new " + name.getText() + "(";
         while(!makeTypes.isEmptyconcTomType()) {
					 String argName = "t"+index;
           if (index>0) {//if many parameters
             makeTlCode = makeTlCode.concat(",");
           }
					 makeTlCode += argName;

           BQTerm arg = `BQVariable(concOption(),Name(argName),makeTypes.getHeadconcTomType());
           makeArgs = `concBQTerm(makeArgs*,arg);

					 makeTypes = makeTypes.getTailconcTomType();
           index++;
         }
				 makeTlCode += ")";

         TomType strategyType = `Type(concTypeOption(),"Strategy",EmptyTargetLanguageType());
				 Option makeOption = `OriginTracking(Name(name.getText()),t.getLine(),currentFile());
				 Declaration makeDecl = `MakeDecl(Name(name.getText()), strategyType, makeArgs, CodeToInstruction(TargetLanguageToCode(ITL(makeTlCode))), makeOption);
          options.add(`DeclarationToOption(makeDecl));

          // Define the is_fsym method.
          Option fsymOption = `OriginTracking(Name(name.getText()),t.getLine(),currentFile());
          String varname = "t";
          BQTerm fsymVar = `BQVariable(concOption(fsymOption),Name(varname),strategyType);
          String code = ASTFactory.abstractCode("($"+varname+" instanceof " + name.getText() + ")",varname);
          Declaration fsymDecl = `IsFsymDecl(Name(name.getText()),fsymVar,Code(code),fsymOption);
          options.add(`DeclarationToOption(fsymDecl));

          TomSymbol astSymbol = ASTFactory.makeSymbol(name.getText(), strategyType, types, ASTFactory.makePairNameDeclList(pairNameDeclList), options);
          putSymbol(name.getText(),astSymbol);
          // update for new target block...
          updatePosition(t.getLine(),t.getColumn());

          result = `AbstractDecl(concDeclaration(Strategy(Name(name.getText()), extendsBQTerm, astVisitList,orgTrack),SymbolDecl(Name(name.getText()))));

          // %strat finished: go back in target parser.
            selector().pop();
        }
     )
    ;

strategyVisitList [List<TomVisit> list] throws TomException
    :   ( strategyVisit[list] )* ;

strategyVisit [List<TomVisit> list] throws TomException
{
  List<ConstraintInstruction> constraintInstructionList = new LinkedList<ConstraintInstruction>();
  TomType vType = null;
  clearText();
}
    :
  (
    "visit" type:ALL_ID LBRACE
    { vType = `Type(concTypeOption(),type.getText(),EmptyTargetLanguageType()); }
    ( visitInstruction[constraintInstructionList,vType] )*
    RBRACE
  )
  {
    List<Option> optionList = new LinkedList<Option>();
    optionList.add(`OriginTracking(Name(type.getText()),type.getLine(),currentFile()));
    OptionList options = ASTFactory.makeOptionList(optionList);
    list.add(`VisitTerm(vType,ASTFactory.makeConstraintInstructionList(constraintInstructionList),options));
  }
;


//////////////////////////////////////////////////////////////////

// The %transformation construct : looks like the %strategy
transformationConstruct [Option orgTrack] returns [Declaration result] throws TomException
{
  result = null;
  Option ot = `noOption(); // ?
//  List<TomWithTo> withList = new LinkedList<TomWithTo>();
//  TomWithToList astWithToList = `concTomWithTo();
  List<Declaration> declList = new LinkedList<Declaration>();
  DeclarationList astDeclarationList = `concDeclaration();
  List<Declaration> subDeclList = new LinkedList<Declaration>();
  TomTypeList types = `concTomType();
  List<Option> optionList = new LinkedList<Option>();
  OptionList options = `concOption();
  List<Option> symbolOptions = new LinkedList<Option>();
  List<TomName> slotNameList = new LinkedList<TomName>();
  List<PairNameDecl> pairNameDeclList = new LinkedList<PairNameDecl>();
  String stringSlotName = null;
  String stringTypeArg = null;
  TargetLanguage implement;
  //tmp, will probably changed into List<BQTerm>
  List<String> withElementList = new LinkedList<String>();
  List<String> toElementList = new LinkedList<String>();

  Option resolveOrgTrack = `noOption(); // ?

  TomName rsname; 
  List<TomVisit> resolveVisitList = new LinkedList<TomVisit>();
  TomVisitList astResolveVisitList = `concTomVisit();
  BQTerm resolveExtendsTerm;
  List<ConstraintInstruction> resolveConstraintInstructionList = new LinkedList<ConstraintInstruction>();

  List<ResolveStratElement> resolveStratElementList = new LinkedList<ResolveStratElement>();
  ResolveStratElementList astResolveStratElementList = `concResolveStratElement();

  List<ResolveStratBlock> resolveStratBlockList = new LinkedList<ResolveStratBlock>();
  ResolveStratBlockList astResolveStratBlockList = `concResolveStratBlock();

  Constraint constraint;
  BQTerm subject;
  TomTerm pattern;
  clearText();
}
    :(
        name:ALL_ID
        {
        ot = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
        optionList.add(ot);
        options = ASTFactory.makeOptionList(optionList); // usefull for later
        if(symbolTable.getSymbolFromName(name.getText()) != null) {
          throw new TomException(TomMessage.invalidTransformationName, new Object[]{name.getText()});
        }
        }
        (
         LPAREN (firstSlot1:ALL_ID (colon:COLON)? secondSlot1:ALL_ID
           {
           if(colon != null) {
             stringSlotName = firstSlot1.getText();
             stringTypeArg = secondSlot1.getText();
           } else {
             stringSlotName = secondSlot1.getText();
             stringTypeArg = firstSlot1.getText();
           }
           TomName astName = `Name(stringSlotName);
           slotNameList.add(astName);

           TomType transformationType = `Type(concTypeOption(),"Transformation",EmptyTargetLanguageType());
           //
           // todo
           //

           }
           (
            COMMA
            firstSlot2:ALL_ID (colon2:COLON)? secondSlot2:ALL_ID
            {
            if(colon != null) {
            stringSlotName = firstSlot2.getText();
            stringTypeArg = secondSlot2.getText();
            } else {
            stringSlotName = secondSlot2.getText();
            stringTypeArg = firstSlot2.getText();
            }
            TomName astName = ASTFactory.makeName(stringSlotName);
            if(slotNameList.indexOf(astName) != -1) {
            TomMessage.error(getLogger(),currentFile(), getLine(),
              TomMessage.repeatedSlotName,
              stringSlotName);
            }
            slotNameList.add(astName);

            TomType transformationType = `Type(concTypeOption(),"Transformation",EmptyTargetLanguageType());
            //todo

            }
           )*
         )? RPAREN

         // shouldn't we find another syntax ?
         with:WITH LPAREN
         withtoElementList[withElementList]
         RPAREN 
         TO LPAREN 
         withtoElementList[toElementList]
         RPAREN
         {
           int line = with.getLine();
           TomType sType = `Type(concTypeOption(),"String",EmptyTargetLanguageType()); //  /!\ Java dependant
           subject = `BQVariable(concOption(), Name("tom__arg"), SymbolTable.TYPE_UNKNOWN);
           Iterator<String> toIt = toElementList.iterator();
           while(toIt.hasNext()) {
             Iterator<String> withIt = withElementList.iterator();
             String tName = toIt.next();
             TomType tType = `Type(concTypeOption(),tName,EmptyTargetLanguageType());

             while(withIt.hasNext()) {
               String wName = withIt.next();
               String resolveStringName = "Resolve"+ wName + tName;
               String canonicalSrcName = "";
               try {
                 //has to be changed
                 canonicalSrcName = wName; //java.lang.Class.forName(wName).getCanonicalName();
               } catch (Exception e) {
                 e.printStackTrace();
               }

               // temporary innerClassCode
               // in the future, we will need to build a generic construct, and
               // this code will be generated in the backend
//               String innerClassCode = "\n\nprivate static class " + resolveStringName + " extends ###?FQN.ClassImpl?### {\n  public String name;\n  public " + canonicalSrcName + " o;\n  public " + resolveStringName + "(" + canonicalSrcName + " o, String name) {\n    this.name = name;\n    this.o = o;\n  }\n}\n\n";

//               TargetLanguage innerClass = `ITL(innerClassCode);
               
               //same remark : in the future, we'll have to modify the Code()
               //block since it is java dependant
               //this Code() block should be generated in the backend
               
               DeclarationList resolveTTDecl= `concDeclaration(
                   ResolveIsSortDecl(
                     BQVariable(
                       concOption(OriginTracking(Name("t"),line,currentFile())),
                       Name("t"),
                       Type(concTypeOption(),resolveStringName,EmptyTargetLanguageType())),
                     resolveStringName,
                     OriginTracking(Name("is_sort"),line,currentFile())
                     )
                   );

/*
   Here, we have to add the %typeterm implement in the table. How to build it ?
   TargetLanguage implement = null;
   TomType astType =
   `Type(typeoptionList,currentTypeName,TLType(implement.getCode()));
   putType(currentTypeName, astType);
*/
               TomName resolveName = `Name(resolveStringName);
               resolveOrgTrack = `OriginTracking(resolveName,line,currentFile());

               // create the %op that corresponds to the inner class
               // has to be changed: it is Java dependant
               TomType wType = `Type(concTypeOption(),wName,EmptyTargetLanguageType());
               types = `concTomType(wType,sType);

               PairNameDecl objectPNDecl = `PairNameDecl(
                                              Name("o"),
                                              ResolveGetSlotDecl(
                                                Name(resolveStringName),
                                                Name("o"), 
                                                BQVariable(
                                                  concOption(OriginTracking(Name("t"),line,currentFile())),
                                                  Name("t"),
                                                  tType 
                                                  ),
                                                OriginTracking(Name("get_slot"),line,currentFile())
                                                )
                                              );
               PairNameDecl namePNDecl = `PairNameDecl(
                                              Name("name"),
                                              ResolveGetSlotDecl(
                                                Name(resolveStringName),
                                                Name("name"), 
                                                BQVariable(
                                                  concOption(OriginTracking(Name("t"),line,currentFile())),
                                                  Name("t"),
                                                  tType 
                                                  ),
                                                OriginTracking(Name("get_slot"),line,currentFile())
                                                )
                                              );

               pairNameDeclList.add(objectPNDecl);
               pairNameDeclList.add(namePNDecl);

               Declaration isfsymDecl = `ResolveIsFsymDecl(
                   resolveName,
                   BQVariable(concOption(OriginTracking(Name("t"),line,currentFile())),Name("t"),tType),
                   OriginTracking(Name("is_fsym"),line,currentFile())
                   );
             
               BQTermList makeArgs = `concBQTerm(
                   BQVariable(concOption(OriginTracking(Name("o"),line,currentFile())),Name("o"),wType),
                   BQVariable(concOption(OriginTracking(Name("name"),line,currentFile())),Name("name"),sType)
                   );

               Declaration makeDecl = `ResolveMakeDecl(
                   resolveName,
                   tType,
                   makeArgs,
                   OriginTracking(Name("make"),line,currentFile())
                   );

               symbolOptions.add(orgTrack);
               symbolOptions.add(`DeclarationToOption(makeDecl));
               symbolOptions.add(`DeclarationToOption(isfsymDecl));
               
               TomSymbol astSymbol = ASTFactory.makeSymbol(
                   resolveStringName,
                   tType,
                   types,
                   ASTFactory.makePairNameDeclList(pairNameDeclList),
                   symbolOptions);

               String extendsName = "###?FQN.ClassImpl?###"; // find the name
///               declList.add(`ResolveClassDeclInit(CodeToInstruction(TargetLanguageToCode(innerClass)))); //inner
               declList.add(`ResolveClassDecl(wName, tName, extendsName)); //inner
               declList.add(`ResolveTypeTermDecl(resolveName,resolveTTDecl,resolveOrgTrack));//%typeterm

               // tom__resolve@ResolveSrcDst
/*               pattern = `Variable(concOption(resolveOrgTrack),
                   resolveName,
                   Type(concTypeOption(e,"unknown type",EmptyTargetLanguageType()),
                   concConstraint(
                     AliasTo(
                       Variable(
                         concOption(OriginTracking(Name("tom__resolve"),line,currentFile())),
                         Name("tom__resolve"),
                         Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),
                         concConstraint()
                         )
                       )
                     )
                   );*/
               //constraint = `AndConstraint(TrueConstraint(),MatchConstraint(pattern,subject,tType));
//               constraint = `MatchConstraint(pattern,subject,tType);
               //resolveConstraintInstructionList.add(`ResolveConstraintInstruction(constraint,options));
//               resolveConstraintInstructionList.add(`ConstraintInstruction(constraint,ResolveStratInstruction(tType),options));
             resolveStratElementList.add(`ResolveStratElement(wName, resolveOrgTrack));
             }
//             resolveVisitList.add(`VisitTerm(tType,
//                   ASTFactory.makeConstraintInstructionList(resolveConstraintInstructionList),
//                   concOption(resolveOrgTrack)));
//             resolveConstraintInstructionList.clear(); //reset ConstraintInstructionList
             astResolveStratElementList = ASTFactory.makeResolveStratElementList(resolveStratElementList);
             resolveStratBlockList.add(`ResolveStratBlock(tName,astResolveStratElementList));
             resolveStratElementList.clear(); //reset ConstraintInstructionList
           }
           // ResolveStrategy
//           rsname = `Name("tom__StratResolve"); //problem when several transfo
//           resolveExtendsTerm = `BQAppl(concOption(ot),Name("Identity"),concBQTerm());
//           astResolveVisitList = ASTFactory.makeTomVisitList(resolveVisitList);
//           orgTrack = `OriginTracking(Name("Strategy"), name.getLine(), currentFile());
//           declList.add(`Strategy(rsname,resolveExtendsTerm,astResolveVisitList,orgTrack));

           astResolveStratBlockList =
             ASTFactory.makeResolveStratBlockList(resolveStratBlockList);
           declList.add(`ResolveStratDecl(astResolveStratBlockList,ot));
           // ot ou  orgTrack ?
         }
 
        )
        LBRACE
        transformationWithToList[declList, name.getText()] { astDeclarationList = ASTFactory.makeDeclarationList(declList); }
        t:RBRACE
       {
         //
         //TODO
         //
         updatePosition(t.getLine(),t.getColumn());
         /*
         we would like to have somthing like this:
         result = `TransformationDecl(
                     Name(name.getText()),
                     concDeclaration(
                       ResolveTypeTermDecl(  ),
                       ResolveClassDecl(  ),
                       ResolveTypeTermDecl(  ),
                       ResolveClassDecl(  ),
                       Strategy(  ),
                       Strategy(  ),
                       Strategy(  Resolve  )
                     ),
                     orgTrack
                   )
          */
         //work in progress
         //result = `TransformationDecl(Name(name.getText()),astDeclarationList,orgTrack);
         //,SymbolDecl(Name(name.getText()));

         //temporary method
         //result = `AbstractDecl(astDeclarationList); //en attendant
//         result = `AbstractDecl(concDeclaration(
             result = `TransformationDecl(
                 Name(name.getText()),astDeclarationList,orgTrack);//)); //,
//               SymbolDecl(Name(name.getText()))));
         selector().pop();
       }
     )
    ;

transformationWithToList [List<Declaration> declList, String toname] throws TomException
    :   ( transformationWithTo[declList, toname] )*
    ;

transformationWithTo [List<Declaration> declList, String toname] throws TomException
{
  //to clean

//  TomWithTerm wTerm = null;
  List argsList = new LinkedList(); //args
  List<Code> toInstructionList = new LinkedList<Code>();
  List<Option> argsOptionList = new LinkedList<Option>();
  List<Code> blockList = new LinkedList<Code>(); //for the withToInstruction ('To' part)
  BQTerm rhsTerm; //id
  clearText();
  TomTerm lhs;

  TomName sname;
  List<TomVisit> visitList = new LinkedList<TomVisit>();
  TomVisitList astVisitList = `concTomVisit();
  BQTerm extendsTerm;
  List<ConstraintInstruction> constraintInstructionList = new LinkedList<ConstraintInstruction>();
  TomType vType = null;
  clearText();
  
  TomName tomTermName = `EmptyName();
  int lhsLine = 0;
  Constraint constraint;
  BQTerm subject;
}
    :
    (
           lhs = annotatedTerm[true] ARROW
     (
      (
       t:LBRACE
       {
       // update for new target block
       updatePosition(t.getLine(),t.getColumn());
       // actions in target language : call the target lexer and
       // call the target parser
       selector().push("targetlexer");
       TargetLanguage tlCode = targetparser.targetLanguage(blockList);
       // target parser finished : pop the target lexer
       selector().pop();
       blockList.add(`TargetLanguageToCode(tlCode));
       toInstructionList.add(`InstructionToCode(AbstractBlock(ASTFactory.makeInstructionList(blockList))));
       }
      )
      | rhsTerm = plainBQTerm
      {
      // case where the rhs of a rule is an algebraic term
      toInstructionList.add(`InstructionToCode(Return(rhsTerm)));
      }
     )
    )
    {
    //TODO
    %match(lhs) {
      Variable(concOption(_*,OriginTracking[Line=line],_*),n,_,_) -> {
        tomTermName = `n;
        lhsLine = `line;
      }
    }
    List<Option> optionList = new LinkedList<Option>();
    //optionList.add(`OriginTracking(Name(toname),term.getLine(),currentFile()));
    optionList.add(`OriginTracking(tomTermName,lhsLine,currentFile()));
    OptionList options = ASTFactory.makeOptionList(optionList);
    
    /*vType = `Type(concTypeOption(),
                  tomTermName.getString(),
                  EmptyTargetLanguageType());*/

    /*subject = `BQVariable(concOption(),
                                 Name("tom__arg"),
                                 SymbolTable.TYPE_UNKNOWN);*/
    // why And(TrueConstraint, MatchConstraint) ?
    // why not only MatchConstraint ?
    /*constraint = `AndConstraint(TrueConstraint(),
                                MatchConstraint(lhs,subject,vType));*/

    /*constraintInstructionList.add(`ConstraintInstruction(
                                     constraint,
                                     RawAction(If(
                                         TrueTL(),
                                         AbstractBlock(
                                           ASTFactory.makeInstructionList(blockList)
                                           ),
                                         Nop())),
                                     options));*/
    //options will change when conjunction will be added

//    visitList.add(`VisitTerm(vType,ASTFactory.makeConstraintInstructionList(constraintInstructionList),options));
    
    // the same for all
//    extendsTerm = `BQAppl(concOption(OriginTracking(tomTermName,lhsLine,currentFile())),Name("Identity"),concBQTerm());
//    astVisitList = ASTFactory.makeTomVisitList(visitList);
    Option orgTrack = `OriginTracking(Name("Strategy"), lhsLine, currentFile());

//    sname = `Name(tomTermName.getString()+"To"+toname);
//    declList.add(`Strategy(sname,extendsTerm,astVisitList,orgTrack));
    declList.add(`TransfoStratDecl(tomTermName,lhs,ASTFactory.makeInstructionList(blockList),options,orgTrack));
    //declList.add(`AbstractDecl(concDeclaration(Strategy(sname,extendsTerm,astVisitList,orgTrack), SymbolDecl(sname))));
    }
    ;

withtoElementList [List<String> list] throws TomException
    : (withtoElement[list] ( COMMA withtoElement[list] )*)
    ;

withtoElement [List<String> list] throws TomException
    : t:ALL_ID { list.add(t.getText()); }
    ;

////////////////////////////////////////////////////////////////

// terms for %match
annotatedTerm [boolean allowImplicit] returns [TomTerm result] throws TomException
{
    result = null;
    TomName labeledName = null;
    TomName annotatedName = null;
    int line = 0;
    boolean anti = false;
}
    :   (
            (// l: label
                (ALL_ID COLON) => lname:ALL_ID COLON
                {
                    text.append(lname.getText());
                    text.append(':');
                    labeledName = `Name(lname.getText());
                    line = lname.getLine();
                }
            )?
            (// @ annotation
                (ALL_ID AT) => name:ALL_ID AT
                {
                    text.append(name.getText());
                    text.append('@');
                    annotatedName = `Name(name.getText());
                    line = name.getLine();
                }
            )?
            result = plainTerm[labeledName,annotatedName,line]
       )
    ;

plainTerm [TomName astLabeledName, TomName astAnnotedName, int line] returns [TomTerm result] throws TomException
{
    List list = new LinkedList();
    List<Option> secondOptionList = new LinkedList<Option>();
    List<Option> optionList = new LinkedList<Option>();
    List<Constraint> constraintList = new LinkedList<Constraint>();
    result = null;
    TomNameList nameList = `concTomName();
    TomName name = null;
    boolean implicit = false;
    boolean anti = false;

    if(astLabeledName != null) {
      constraintList.add(ASTFactory.makeStorePosition(astLabeledName, line, currentFile()));
    }
    if(astAnnotedName != null) {
      constraintList.add(ASTFactory.makeAliasTo(astAnnotedName, line, currentFile()));
    }
}
    :
      (a:ANTI_SYM {anti = !anti;} )*
        ( // xml term
          result = xmlTerm[optionList, constraintList]
          {
            if(anti) { result = `AntiTerm(result); }
          }

        | // var* or _*
          {!anti}? // do not allow anti symbols on var* or _*
          (variableStar[null,null]) => result = variableStar[optionList,constraintList]

        | // _
          {!anti}? // do not allow anti symbols on _
          result = unamedVariable[optionList,constraintList]

        | // for a single constant: i.e. a variable
          // ambiguous with the next rule so:
          {LA(2) != LPAREN && LA(2) != LBRACKET && LA(2) != QMARK && LA(2) != QQMARK}?
          name = headSymbol[optionList]
          {
            result = `Variable(ASTFactory.makeOptionList(optionList),name,
              SymbolTable.TYPE_UNKNOWN,ASTFactory.makeConstraintList(constraintList));
            if(anti) { result = `AntiTerm(result); }
          }

        | // for a disjuntion of constants: 1, 3.14, "foo", or (1|2|3) for instance
          // ambiguous with the next rule so:
          {LA(2) != LPAREN && LA(2) != LBRACKET && LA(2) != QMARK && LA(2) != QQMARK}?
          nameList = headConstantList[optionList]
          {
            optionList.add(`Constant());
            result = `TermAppl(
                ASTFactory.makeOptionList(optionList),
                nameList,
                ASTFactory.makeTomList((List<TomTerm>)list),
                ASTFactory.makeConstraintList(constraintList));
            if(anti) { result = `AntiTerm(result); }
          }

        | // f(...) or f[...] or !f(...) or !f[...]
          name = headSymbol[optionList]
          (
           st:QQMARK {
             if(st!=null) {
               optionList.add(`MatchingTheory(concElementaryTheory(AC())));
             }
           }
           |
            qm:QMARK {
              if(qm!=null) {
                optionList.add(`MatchingTheory(concElementaryTheory(AU())));
              }
            }
           )?

          implicit = args[list,secondOptionList]
          {
            nameList = `concTomName(nameList*,name);
            if(implicit) {
              result = `RecordAppl(
                  ASTFactory.makeOptionList(optionList),
                  nameList,
                  ASTFactory.makeSlotList((List<Slot>)list),
                  ASTFactory.makeConstraintList(constraintList)
                  );
            } else {
              result = `TermAppl(
                  ASTFactory.makeOptionList(optionList),
                  nameList,
                  ASTFactory.makeTomList((List<TomTerm>)list),
                  ASTFactory.makeConstraintList(constraintList)
                  );
            }
            if(anti) { result = `AntiTerm(result); }
          }

        | // (f|g...)
          // ambiguity with the last rule so use a lookahead
          // if ALTERNATIVE then parse headSymbolList
          {LA(3) == ALTERNATIVE || LA(4) == ALTERNATIVE}? 
          nameList = headSymbolList[optionList]
          implicit = args[list, secondOptionList]
          {
            if(implicit) {
              result = `RecordAppl(
                  ASTFactory.makeOptionList(optionList),
                  nameList,
                  ASTFactory.makeSlotList((List<Slot>)list),
                  ASTFactory.makeConstraintList(constraintList)
                  );
            } else {
              result = `TermAppl(
                  ASTFactory.makeOptionList(optionList),
                  nameList,
                  ASTFactory.makeTomList((List<TomTerm>)list),
                  ASTFactory.makeConstraintList(constraintList)
                  );
            }
            if(anti) { result = `AntiTerm(result); }
          }
        )
    ;

plainBQTerm  returns [BQTerm result]
{
    TomName name = null;
    result = null;
    List<Option> optionList = new LinkedList<Option>();
    BQTerm tmp;
    BQTermList l = `concBQTerm();
}
    :  name = headSymbol[optionList]
      (star:STAR | (args:LPAREN
         (tmp=plainBQTerm { l = `concBQTerm(l*,tmp); })?
         (COMMA tmp=plainBQTerm { l = `concBQTerm(l*,tmp); })*
       RPAREN))?
       {
         if (args==null) {
           if (star == null) {
             result = `BQVariable(ASTFactory.makeOptionList(optionList),name,SymbolTable.TYPE_UNKNOWN);
           } else {
             result = `BQVariableStar(ASTFactory.makeOptionList(optionList),name,SymbolTable.TYPE_UNKNOWN);
           }
         } else {
           result = `BQAppl(ASTFactory.makeOptionList(optionList),name,l);
         }
       }
    | number:NUM_INT {
      String val = number.getText();
      ASTFactory.makeIntegerSymbol(symbolTable,val,optionList);
      result = `BuildConstant(Name(val));
    }

    | string:STRING {
      String val = string.getText();
      ASTFactory.makeStringSymbol(symbolTable,val,optionList);
      result = `BuildConstant(Name(val));
    }

    //| name = headConstant[optionList] { result = `BuildConstant(name); }
;

xmlTerm [List<Option> optionList, List<Constraint> constraintList] returns [TomTerm result] throws TomException
{
  result = null;
  TomTerm arg1, arg2;
  List<Slot> pairSlotList = new LinkedList<Slot>();
  List attributeList = new LinkedList();
  List childs = new LinkedList();
  String keyword = null;
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
                        StringBuilder found = new StringBuilder();
                        StringBuilder expected = new StringBuilder();
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
                            new Object[]{currentFile(), Integer.valueOf(getLine()),
                            "match", expected.substring(1), found.substring(1)});
                    }
                    if(implicit) {
                      //System.out.println("implicit");
                      //System.out.println("childs = " + childs);
                      optionList.add(`ImplicitXMLChild());
                    } else {
                      //System.out.println("explicit");
                      //System.out.println("childs1 = " + childs);
                      childs = ASTFactory.metaEncodeExplicitTermList(symbolTable, childs);
                      //System.out.println("childs2 = " + childs);
                    }
                    option = ASTFactory.makeOptionList(optionList);
                }
            ) // end choice
            {
                result = `XMLAppl(
                    option,
                    nameList,
                    ASTFactory.makeTomList((List<TomTerm>)attributeList),
                    ASTFactory.makeTomList((List<TomTerm>)childs),
                    ASTFactory.makeConstraintList(constraintList));
            }

        | // #TEXT(...)
            XML_TEXT LPAREN arg1 = annotatedTerm[true] RPAREN
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


xmlAttributeList [List<TomTerm> list] returns [boolean result] throws TomException
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
    List<Slot> slotList = new LinkedList<Slot>();
    TomTerm term = null;
    TomTerm termName = null;
    String name;
    OptionList option = null;
    ConstraintList constraint;
    List<Option> optionList = new LinkedList<Option>();
    List<Constraint> constraintList = new LinkedList<Constraint>();
    List anno1ConstraintList = new LinkedList();
    List anno2ConstraintList = new LinkedList();
    List<Option> optionListAnno2 = new LinkedList<Option>();
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
                    anno2ConstraintList.add(ASTFactory.makeAliasTo(`Name(anno2.getText()), getLine(), currentFile()));
                }
            )?
            (a:ANTI_SYM {anti = !anti;} )*
            term = unamedVariableOrTermStringIdentifier[optionListAnno2,anno2ConstraintList]
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
                    anno1ConstraintList.add(ASTFactory.makeAliasTo(`Name(anno1.getText()), getLine(), currentFile()));
                }
            )?
            termName = unamedVariable[optionList,anno1ConstraintList]
            e:EQUAL {text.append("=");}
            (
                {LA(2) == AT}? anno3:ALL_ID AT
                {
                    text.append(anno3.getText()+"@");
                    anno2ConstraintList.add(ASTFactory.makeAliasTo(`Name(anno3.getText()), getLine(), currentFile()));
                }
            )?
            (b:ANTI_SYM {anti = !anti;} )*
            term = unamedVariableOrTermStringIdentifier[optionListAnno2,anno2ConstraintList]
        )
        {
            if (!varStar) {
              if (anti) {
                term = `AntiTerm(term);
              }

                slotList.add(`PairSlotAppl(Name(Constants.SLOT_NAME),termName));
                // we add the specif value : _
                optionList.add(`OriginTracking(Name("_"),getLine(),currentFile()));
                option = ASTFactory.makeOptionList(optionList);
                constraint = ASTFactory.makeConstraintList(constraintList);
                slotList.add(`PairSlotAppl(Name(Constants.SLOT_SPECIFIED),Variable(option,EmptyName(),SymbolTable.TYPE_UNKNOWN,constraint)));
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

xmlNameList [List<Option> optionList, boolean needOrgTrack] returns [TomNameList result] throws TomException
{
    result = `concTomName();
    StringBuilder xmlName = new StringBuilder();
    int decLine = 0;
    boolean anti = false;
}
    :
        (
         (a:ANTI_SYM {anti = !anti;} )*
            name:ALL_ID
            {
                text.append(name.getText());
                xmlName.append(name.getText());
                decLine = name.getLine();
                if (anti) {
                  result =  `concTomName(AntiName(Name(name.getText())));
                } else {
                  result = `concTomName(Name(name.getText()));
                }
            }
        |   name2:UNDERSCORE
            {
                text.append(name2.getText());
                xmlName.append(name2.getText());
                decLine = name2.getLine();
                result = `concTomName(Name(name2.getText()));
            }
        |   LPAREN (b:ANTI_SYM {anti = !anti;} )* name3:ALL_ID
            {
                text.append(name3.getText());
                xmlName.append(name3.getText());
                decLine = name3.getLine();
                if (anti) {
                  result =  `concTomName(AntiName(Name(name3.getText())));
                } else {
                  result = `concTomName(Name(name3.getText()));
                }

            }
            (
                ALTERNATIVE (c:ANTI_SYM {anti = !anti;} )* name4:ALL_ID
                {
                    text.append("|"+name4.getText());
                    xmlName.append("|"+name4.getText());
                    if (anti) {
                      result = `concTomName(result*,AntiName(Name(name4.getText())));
                    } else {
                      result = `concTomName(result*,Name(name4.getText()));
                    }
                }
            )*
            RPAREN
        )
        {
            if(needOrgTrack) {
                optionList.add(`OriginTracking(Name(xmlName.toString()), decLine, currentFile()));
            }
        }
    ;

termStringIdentifier [List<Option> options] returns [TomTerm result] throws TomException
{
  result = null;
  List<Option> optionList = (options==null)?new LinkedList<Option>():options;
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
                result = `Variable(option,Name(nameID.getText()),SymbolTable.TYPE_UNKNOWN,concConstraint());
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


unamedVariableOrTermStringIdentifier [List<Option> options, List<Constraint> constraintList] returns [TomTerm result] throws TomException
{
  result = null;
  List<Option> optionList = (options==null)?new LinkedList<Option>():options;
  OptionList option = null;
  TomNameList nameList = null;
  ConstraintList constraints = null;
}
    :
        (
            result = unamedVariable[optionList, constraintList]
        |
            nameID:ALL_ID
            {
                text.append(nameID.getText());
                optionList.add(`OriginTracking(Name(nameID.getText()),nameID.getLine(),currentFile()));
                option = ASTFactory.makeOptionList(optionList);
                constraints = ASTFactory.makeConstraintList(constraintList);
                result = `Variable(option,Name(nameID.getText()),SymbolTable.TYPE_UNKNOWN,constraints);
            }
        |
            nameString:STRING
            {
                text.append(nameString.getText());
                optionList.add(`OriginTracking(Name(nameString.getText()),nameString.getLine(),currentFile()));
                option = ASTFactory.makeOptionList(optionList);
                ASTFactory.makeStringSymbol(symbolTable,nameString.getText(),optionList);
                nameList = `concTomName(Name(nameString.getText()));
                constraints = ASTFactory.makeConstraintList(constraintList);
                result = `TermAppl(option,nameList,concTomTerm(),constraints);
            }
        )
    ;

// returns true is implicit notation
xmlChilds [List<TomTerm> list] returns [boolean result] throws TomException
{
  result = false;
  TomTerm term;
}
    :
        (
            LBRACKET { text.append("["); }
            ( termList[list] )?
            RBRACKET
            {
                text.append("]");
                result=true;
            }
        |   LPAREN { text.append("("); }
            ( termList[list] )?
            RPAREN
            {
                text.append(")");
                result=false;
            }
        |   ( term = annotatedTerm[true] {list.add(ASTFactory.metaEncodeXMLAppl(symbolTable,term));} )*
            {
              result = true;
            }
        )
    ;

args [List list, List<Option> optionList] returns [boolean result] throws TomException
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

termList [List<TomTerm> list] throws TomException
{
    TomTerm term = null;
}
    :   (
            term = annotatedTerm[true] {list.add(term);}
            ( COMMA {text.append(',');} term = annotatedTerm[true] {list.add(term);})*
        )
    ;

pairList [List<Slot> list] throws TomException
{
    TomTerm term = null;
}
    :   (
            name:ALL_ID EQUAL
            {
                text.append(name.getText());
                text.append('=');
            }
            term = annotatedTerm[true]
            {list.add(`PairSlotAppl(Name(name.getText()),term));}
            ( COMMA {text.append(',');}
                name2:ALL_ID EQUAL
                {
                    text.append(name2.getText());
                    text.append('=');
                }
                term = annotatedTerm[true]
                {list.add(`PairSlotAppl(Name(name2.getText()),term));}
            )*
        )
;

// _* or var*
bqVariableStar [List<Option> optionList, List<Constraint> constraintList] returns [BQTerm result]
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
                result = `BQVariableStar(
                        options,
                        Name(name),
                        SymbolTable.TYPE_UNKNOWN
                  );

            }
        )
    ;

// _* or var*
variableStar [List<Option> optionList, List<Constraint> constraintList] returns [TomTerm result]
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
                    result = `VariableStar(
                        options,
                        EmptyName(),
                        SymbolTable.TYPE_UNKNOWN,
                        constraints
                    );
                } else {
                    result = `VariableStar(
                        options,
                        Name(name),
                        SymbolTable.TYPE_UNKNOWN,
                        constraints
                    );
                }
            }
        )
    ;

// _
unamedVariable [List<Option> optionList, List<Constraint> constraintList] returns [TomTerm result]
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
                result = `Variable(options,EmptyName(),SymbolTable.TYPE_UNKNOWN,constraints);
            }
        )
    ;

// ( id | id | ...)
headSymbolList [List<Option> optionList] returns [TomNameList result]
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

headSymbolOrConstant [List<Option> optionList] returns [TomName result]
{
  result = null;
} : ( result = headSymbol[optionList]
    | result = headConstant[optionList]
    );

headSymbol [List<Option> optionList] returns [TomName result]
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

headConstantList [List<Option> optionList] returns [TomNameList result]
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

headConstant [List<Option> optionList] returns [TomName result]
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
    List<Option> options = new LinkedList<Option>();
    List<TomName> slotNameList = new LinkedList<TomName>();
    List<PairNameDecl> pairNameDeclList = new LinkedList<PairNameDecl>();
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
                types = `concTomType(types*,Type(concTypeOption(),typeArg.getText(),EmptyTargetLanguageType()));
            }
            (
                COMMA
                slotName2:ALL_ID COLON typeArg2:ALL_ID
                {
                    stringSlotName = slotName2.getText();
                    astName = ASTFactory.makeName(stringSlotName);
                    if(slotNameList.indexOf(astName) != -1) {
                      TomMessage.error(getLogger(),currentFile(), getLine(),
                        TomMessage.repeatedSlotName,
                        stringSlotName);
                    }
                    slotNameList.add(astName);
                    pairNameDeclList.add(`PairNameDecl(Name(stringSlotName),EmptyDeclaration()));
                    types = `concTomType(types*,Type(concTypeOption(),typeArg2.getText(),EmptyTargetLanguageType()));
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
            attribute = keywordMake[name.getText(),`Type(concTypeOption(),type.getText(),EmptyTargetLanguageType()),types]
            { options.add(`DeclarationToOption(attribute)); }

        |   attribute = keywordGetSlot[astName,type.getText()]
            {
              TomName sName = attribute.getSlotName();
              /*
               * ensure that sName appears in slotNameList, only once
               * ensure that sName has not already been generated
               */
              TomMessage msg = null;
              int index = slotNameList.indexOf(sName);
              if(index == -1) {
                msg = TomMessage.errorIncompatibleSlotDecl;
              } else {
                PairNameDecl pair = pairNameDeclList.get(index);
                %match(pair) {
                  PairNameDecl[SlotDecl=decl] -> {
                    if(`decl != `EmptyDeclaration()) {
                      msg = TomMessage.errorTwoSameSlotDecl;
                    }
                  }
                }
              }
              if(msg != null) {
                TomMessage.error(getLogger(),currentFile(), getLine(),
                  msg,
                  currentFile(), Integer.valueOf(attribute.getOrgTrack().getLine()),
                  "%op "+type.getText(), Integer.valueOf(ot.getLine()), sName.getString());
              } else {
                pairNameDeclList.set(index,`PairNameDecl(sName,attribute));
              }
            }
        |   attribute = keywordIsFsym[astName,type.getText()]
            { options.add(`DeclarationToOption(attribute)); }
        )*
        t:RBRACE
	)
        {
          TomSymbol astSymbol = ASTFactory.makeSymbol(name.getText(), `Type(concTypeOption(),type.getText(),EmptyTargetLanguageType()), types, ASTFactory.makePairNameDeclList(pairNameDeclList), options);
//          System.out.println("(DEBUG) parser - astSymbol = " + astSymbol);
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
    List options = new LinkedList();
    Declaration attribute = null;
    String opName = null;
}
    :
        type:ALL_ID name:ALL_ID
        {
	  opName = name.getText();
	  Option ot = `OriginTracking(Name(opName),name.getLine(),currentFile());
	  options.add(ot);
        }
        LPAREN typeArg:ALL_ID STAR RPAREN
        {
            types = `concTomType(types*,Type(concTypeOption(),typeArg.getText(),EmptyTargetLanguageType()));
        }
        LBRACE
        (
            attribute = keywordMakeEmptyList[opName]
            { options.add(attribute); }
        |   attribute = keywordMakeAddList[opName,type.getText(),typeArg.getText()]
            { options.add(attribute); }
        |   attribute = keywordIsFsym[`Name(opName), type.getText()]
            { options.add(attribute); }
        |   attribute = keywordGetHead[`Name(opName), type.getText()]
            { options.add(attribute); }
        |   attribute = keywordGetTail[`Name(opName), type.getText()]
            { options.add(attribute); }
        |   attribute = keywordIsEmpty[`Name(opName), type.getText()]
            { options.add(attribute); }
        )*
        t:RBRACE
        {
            PairNameDeclList pairNameDeclList = `concPairNameDecl(PairNameDecl(EmptyName(), EmptyDeclaration()));
            TomSymbol astSymbol = ASTFactory.makeSymbol(opName, `Type(concTypeOption(),type.getText(),EmptyTargetLanguageType()), types, pairNameDeclList, options);
            putSymbol(opName,astSymbol);
            result = `ListSymbolDecl(Name(opName));
            updatePosition(t.getLine(),t.getColumn());
            selector().pop();
        }
    ;

operatorArray returns [Declaration result] throws TomException
{
    result = null;
    TomTypeList types = `concTomType();
    List<Option> options = new LinkedList<Option>();
    Declaration attribute = null;
    String opName = null;
}
    :
        type:ALL_ID name:ALL_ID
        {
	  opName = name.getText();
	  Option ot = `OriginTracking(Name(opName),name.getLine(),currentFile());
	  options.add(ot);
        }
        LPAREN typeArg:ALL_ID STAR RPAREN
        {
            types = `concTomType(types*,Type(concTypeOption(),typeArg.getText(),EmptyTargetLanguageType()));
        }
        LBRACE
        (
            attribute = keywordMakeEmptyArray[opName,type.getText()]
            { options.add(`DeclarationToOption(attribute)); }
        |   attribute = keywordMakeAddArray[opName,type.getText(),typeArg.getText()]
            { options.add(`DeclarationToOption(attribute)); }
        |   attribute = keywordIsFsym[`Name(opName),type.getText()]
            { options.add(`DeclarationToOption(attribute)); }
        |   attribute = keywordGetElement[`Name(opName), type.getText()]
            { options.add(`DeclarationToOption(attribute)); }
        |   attribute = keywordGetSize[`Name(opName), type.getText()]
            { options.add(`DeclarationToOption(attribute)); }
        )*
        t:RBRACE
        {
            PairNameDeclList pairNameDeclList = `concPairNameDecl(PairNameDecl(EmptyName(), EmptyDeclaration()));
            TomSymbol astSymbol = ASTFactory.makeSymbol(opName, `Type(concTypeOption(),type.getText(),EmptyTargetLanguageType()), types, pairNameDeclList, options);
            putSymbol(opName,astSymbol);

            result = `ArraySymbolDecl(Name(opName));

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
    DeclarationList declarationList = `concDeclaration();
    String s;

    TypeOptionList typeoptionList = `concTypeOption();
    int currentLine = -1;
    String supertypeName = null;
    String currentTypeName = null;
  }
  :   (
      type:ALL_ID
      {
      currentLine = type.getLine();
      currentTypeName = type.getText();
      ot = `OriginTracking(Name(currentTypeName),currentLine,currentFile());
      }
      (
         EXTENDS
        supertype:ALL_ID
        {
            supertypeName = supertype.getText();
            typeoptionList = `concTypeOption(SubtypeDecl(supertypeName));
        }
      )?
      LBRACE
      implement = keywordImplement
      (  attribute = keywordEquals[currentTypeName]
         { declarationList = `concDeclaration(attribute,declarationList*); }
         |   attribute = keywordIsSort[currentTypeName]
         { declarationList = `concDeclaration(attribute,declarationList*); }
         |   attribute = keywordGetImplementation[currentTypeName]
         { declarationList = `concDeclaration(attribute,declarationList*); }
      )*
      t:RBRACE
      {
          TomType astType = `Type(typeoptionList,currentTypeName,TLType(implement.getCode()));
          putType(currentTypeName, astType);
          result = `TypeTermDecl(Name(currentTypeName),declarationList,ot);
//          System.out.println("(DEBUG) parser - TypeTermDecl =\n" + result);
          updatePosition(t.getLine(),t.getColumn());
          selector().pop();
        }
    )
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
                tlCode = targetparser.goalLanguage(new LinkedList<Code>());
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
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList<Code>());
                selector().pop();
                String code = ASTFactory.abstractCode(tlCode.getCode(),name1.getText(),name2.getText());
                result = `EqualTermDecl(
                    BQVariable(option1,Name(name1.getText()),Type(concTypeOption(),type,EmptyTargetLanguageType())),
                    BQVariable(option2,Name(name2.getText()),Type(concTypeOption(),type,EmptyTargetLanguageType())),
                    Code(code), ot);
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
            t:IS_SORT
            { ot = `OriginTracking(Name(t.getText()),t.getLine(),currentFile()); }
            LPAREN name:ALL_ID RPAREN
            {
                Option info = `OriginTracking(Name(name.getText()),name.getLine(),currentFile());
                OptionList option = `concOption(info);

                selector().push("targetlexer");
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList<Code>());
                selector().pop();

                String code = ASTFactory.abstractCode(tlCode.getCode(),name.getText());
                result = `IsSortDecl(
                    BQVariable(option,Name(name.getText()),Type(concTypeOption(),type,EmptyTargetLanguageType())),
                    Code(code), ot);
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
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList<Code>());
                selector().pop();

                result = `GetHeadDecl(opname,
                    symbolTable.getUniversalType(),
                    BQVariable(option,Name(name.getText()),Type(concTypeOption(),type,EmptyTargetLanguageType())),
                    Code(ASTFactory.abstractCode(tlCode.getCode(),name.getText())),
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
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList<Code>());
                selector().pop();

                result = `GetTailDecl(opname,
                    BQVariable(option,Name(name.getText()),Type(concTypeOption(),type,EmptyTargetLanguageType())),
                    Code(ASTFactory.abstractCode(tlCode.getCode(),name.getText())),
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
                TargetLanguage  tlCode = targetparser.goalLanguage(new LinkedList<Code>());
                selector().pop();

                result = `IsEmptyDecl(opname,
                    BQVariable(option,Name(name.getText()),Type(concTypeOption(),type,EmptyTargetLanguageType())),
                    Code(ASTFactory.abstractCode(tlCode.getCode(),name.getText())),
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
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList<Code>());
                selector().pop();

                result = `GetElementDecl(opname,
                    BQVariable(option1,Name(name1.getText()),Type(concTypeOption(),type,EmptyTargetLanguageType())),
                    BQVariable(option2,Name(name2.getText()),Type(concTypeOption(),"int",EmptyTargetLanguageType())),
                    Code(ASTFactory.abstractCode(tlCode.getCode(),name1.getText(),name2.getText())), ot);
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
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList<Code>());
                selector().pop();

                result = `GetSizeDecl(opname,
                    BQVariable(option,Name(name.getText()),Type(concTypeOption(),type,EmptyTargetLanguageType())),
                    Code(ASTFactory.abstractCode(tlCode.getCode(),name.getText())),ot);
            }
        )
    ;

keywordIsFsym[TomName astName, String typeString] returns [Declaration result] throws TomException
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
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList<Code>());
            selector().pop();

            String code = ASTFactory.abstractCode(tlCode.getCode(),name.getText());
            result = `IsFsymDecl(astName,
                BQVariable(option,Name(name.getText()),Type(concTypeOption(),typeString,EmptyTargetLanguageType())),
                Code(code),ot);
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
            TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList<Code>());
            selector().pop();

            result = `GetImplementationDecl(BQVariable(option,Name(name.getText()),Type(concTypeOption(),typeString,EmptyTargetLanguageType())),
                Return(Composite(CompositeTL(tlCode))),ot);
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
                TargetLanguage tlCode = targetparser.goalLanguage(new LinkedList<Code>());
                selector().pop();
                String code = ASTFactory.abstractCode(tlCode.getCode(),name.getText());
                result = `GetSlotDecl(astName,
                    Name(slotName.getText()),
                    BQVariable(option,Name(name.getText()),Type(concTypeOption(),type,EmptyTargetLanguageType())),
                     Code(code), ot);
            }
        )
    ;

keywordMake[String opname, TomType returnType, TomTypeList types] returns [Declaration result] throws TomException
{
    result = null;
    Option ot = null;
    BQTermList args = `concBQTerm();
    ArrayList<String> varnameList = new ArrayList<String>();
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
                    nameArg:ALL_ID
                    {
                        if( !(nbTypes > 0) ) {
                            type = `EmptyType();
                        } else {
                            type = TomBase.elementAt(types,index++);
                        }
                        Option info1 = `OriginTracking(Name(nameArg.getText()),nameArg.getLine(),currentFile());
                        OptionList option1 = `concOption(info1);

                        args = `concBQTerm(args*,BQVariable(
                                option1,
                                Name(nameArg.getText()),
                                type
                            ));
                        varnameList.add(nameArg.getText());
                    }
                    (
                        COMMA nameArg2:ALL_ID
                        {
                            if( index >= nbTypes ) {
                              type = `EmptyType();
                            } else {
                              type = TomBase.elementAt(types,index++);
                            }
                            Option info2 = `OriginTracking(Name(nameArg2.getText()),nameArg2.getLine(),currentFile());
                            OptionList option2 = `concOption(info2);

                            args = `concBQTerm(args*,BQVariable(
                                    option2,
                                    Name(nameArg2.getText()),
                                    type
                                ));
                            varnameList.add(nameArg2.getText());
                        }
                    )*
                )?
                RPAREN )?
            l:LBRACE
            {
                updatePosition(t.getLine(),t.getColumn());
                selector().push("targetlexer");
                List<Code> blockList = new LinkedList<Code>();
                TargetLanguage tlCode = targetparser.targetLanguage(blockList);
                selector().pop();
                blockList.add(`TargetLanguageToCode(tlCode));
                if(blockList.size()==1) {
                  String[] vars = new String[varnameList.size()];
                  String code = ASTFactory.abstractCode(tlCode.getCode(),varnameList.toArray(vars));
                  result = `MakeDecl(Name(opname),returnType,args,ExpressionToInstruction(Code(code)),ot);
                } else {
                  result = `MakeDecl(Name(opname),returnType,args,AbstractBlock(ASTFactory.makeInstructionList(blockList)),ot);
                }
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
            List blockList = new LinkedList();
            TargetLanguage tlCode = targetparser.targetLanguage(blockList);
            selector().pop();
            blockList.add(tlCode);
            if(blockList.size()==1) {
              result = `MakeEmptyList(Name(name),ExpressionToInstruction(Code(tlCode.getCode())),ot);
            } else {
              result = `MakeEmptyList(Name(name),AbstractBlock(ASTFactory.makeInstructionList(blockList)),ot);
            }
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
            List blockList = new LinkedList();
            TargetLanguage tlCode = targetparser.targetLanguage(blockList);
            selector().pop();
            blockList.add(tlCode);
            if(blockList.size()==1) {
              String code = ASTFactory.abstractCode(tlCode.getCode(),elementName.getText(),listName.getText());
              result = `MakeAddList(Name(name),
                  BQVariable(elementOption,Name(elementName.getText()),Type(concTypeOption(),elementType,EmptyTargetLanguageType())),
                  BQVariable(listOption,Name(listName.getText()),Type(concTypeOption(),listType,EmptyTargetLanguageType())),
                  ExpressionToInstruction(Code(code)),ot);
            } else {
              result = `MakeAddList(Name(name),
                  BQVariable(elementOption,Name(elementName.getText()),Type(concTypeOption(),elementType,EmptyTargetLanguageType())),
                  BQVariable(listOption,Name(listName.getText()),Type(concTypeOption(),listType,EmptyTargetLanguageType())),
                  AbstractBlock(ASTFactory.makeInstructionList(blockList)),ot);
            }
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
            List blockList = new LinkedList();
            TargetLanguage tlCode = targetparser.targetLanguage(blockList);
            selector().pop();
            blockList.add(tlCode);
            if(blockList.size()==1) {
              String code = ASTFactory.abstractCode(tlCode.getCode(),listName.getText());
              result = `MakeEmptyArray(Name(name),
                  BQVariable(listOption,Name(listName.getText()),Type(concTypeOption(),listType,EmptyTargetLanguageType())), ExpressionToInstruction(Code(code)),ot);
            } else {
              result = `MakeEmptyArray(Name(name),
                  BQVariable(listOption,Name(listName.getText()),Type(concTypeOption(),listType,EmptyTargetLanguageType())),
                  AbstractBlock(ASTFactory.makeInstructionList(blockList)),ot);
            }
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
            List blockList = new LinkedList();
            TargetLanguage tlCode = targetparser.targetLanguage(blockList);
            selector().pop();
            blockList.add(tlCode);

            Option listInfo = `OriginTracking(Name(listName.getText()),listName.getLine(),currentFile());
            Option elementInfo = `OriginTracking(Name(elementName.getText()),elementName.getLine(),currentFile());
            OptionList listOption = `concOption(listInfo);
            OptionList elementOption = `concOption(elementInfo);
            if(blockList.size()==1) {
              String code = ASTFactory.abstractCode(tlCode.getCode(),elementName.getText(),listName.getText());
              result = `MakeAddArray(Name(name),
                  BQVariable(elementOption,Name(elementName.getText()),Type(concTypeOption(),elementType,EmptyTargetLanguageType())),
                  BQVariable(listOption,Name(listName.getText()),Type(concTypeOption(),listType,EmptyTargetLanguageType())),
                  ExpressionToInstruction(Code(code)),ot);
            } else {
              result = `MakeAddArray(Name(name),
                  BQVariable(elementOption,Name(elementName.getText()),Type(concTypeOption(),elementType,EmptyTargetLanguageType())),
                  BQVariable(listOption,Name(listName.getText()),Type(concTypeOption(),listType,EmptyTargetLanguageType())),
                  AbstractBlock(ASTFactory.makeInstructionList(blockList)),ot);
            }
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
    GET_IMPLEMENTATION = "get_implementation";
    EQUALS = "equals";
    IS_SORT = "is_sort";
    GET_HEAD = "get_head";
    GET_TAIL = "get_tail";
    IS_EMPTY = "is_empty";
    IMPLEMENT = "implement";
    GET_ELEMENT = "get_element";
    GET_SIZE = "get_size";
    WHEN = "when";
    WITH = "with";
    TO = "to";
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
QMARK       :   '?' ;
QQMARK      :   "??" ;
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
    | ~('*'|'\n'|'\r') {if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");}
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
MATCH_CONSTRAINT  : "<<";
LESSOREQUAL_CONSTRAINT  : "<=";
//GREATER_CONSTRAINT  : ":>";
GREATEROREQUAL_CONSTRAINT  : ">=";
DIFFERENT_CONSTRAINT  : "!=";
//EQUAL_CONSTRAINT = DOUBLEEQ;

AND_CONNECTOR  : "&&";
OR_CONNECTOR  : "||";

CONSTRAINT_GROUP_START : '{' ;
CONSTRAINT_GROUP_END : '}' ;

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
            ( LETTER | DIGIT | '_' | '.' )
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
        } else {
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
protected EXPONENT      :   ('e'|'E') ( PLUS | MINUS )? ('0'..'9')+  ;
protected DOT           :   '.' ;
protected FLOAT_SUFFIX  : 'f'|'F'|'d'|'D' ;
