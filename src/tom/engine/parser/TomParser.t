options {
   JAVA_UNICODE_ESCAPE = true;
   STATIC = false;
   //DEBUG_PARSER = true;
   //DEBUG_TOKEN_MANAGER = true;
 //DEBUG_LOOKAHEAD = true;
 }
 
 PARSER_BEGIN(TomParser) 
 /*
  *   
  * TOM - To One Matching Compiler
  * 
  * Copyright (C) 2000-2004 INRIA
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
  * Pierre-Etienne Moreau e-mail: Pierre-Etienne.Moreau@loria.fr
  *
  **/

package jtom.parser;

import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jtom.TomEnvironment;
import aterm.*;
import jtom.adt.tomsignature.Factory;
import jtom.adt.tomsignature.types.*;
import jtom.Tom;
import jtom.exception.*;
import jtom.tools.*;
import jtom.TomMessage;
import jtom.xml.Constants;

public class TomParser {
  private int oldPos=0, oldLine=0;
  private Position orgTrack;
  private TomBuffer tomBuffer;
  private TomBackQuoteParser tomBackQuoteParser;
  private TomFactory tomFactory;
  private HashSet includedFileSet;
  private HashSet alreadyParsedFileSet;
  private String currentFile = "";
  private LinkedList debuggedStructureList;
  private String text="";

  public static TomParser createParser(String fileName) throws FileNotFoundException, IOException {
    return createParser(fileName,new HashSet(), new HashSet());
  }
  
  public static TomParser createParser(String fileName, HashSet includedFileSet, HashSet alreadyParsedFiles) 
    throws FileNotFoundException,IOException {
      File file = new File(fileName);
      InputStream input = new FileInputStream(file);
      byte inputBuffer[] = new byte[(int)file.length()+1];
      (new FileInputStream(file)).read(inputBuffer);
      return new TomParser(input, inputBuffer, fileName, includedFileSet, alreadyParsedFiles);
  }
  
  private TomParser(InputStream input,
                    byte[] inputBuffer,
                    String fileName,
                    HashSet includedFileSet,
                    HashSet alreadyParsedFiles) {
    this(input);
    this.tomBuffer = new TomBuffer(inputBuffer);
    this.currentFile = fileName;
    this.orgTrack = makePosition(1,1);
    this.debuggedStructureList = new LinkedList();
    this.tomBackQuoteParser = new TomBackQuoteParser();
    this.tomFactory = new TomFactory();
    this.includedFileSet = new HashSet(includedFileSet);
    testIncludedFile(fileName, includedFileSet);
    alreadyParsedFileSet = alreadyParsedFiles;
  }

  //------------------------------------------------------------
  %include { TomSignature.tom }
  //------------------------------------------------------------
  
  protected Factory tsf() {
    return environment().getTomSignatureFactory();
  }
  protected final Factory getTomSignatureFactory() {
    return tsf();
  }
  
  private ASTFactory ast() {
    return environment().getASTFactory();
  }

  private TomTaskInput getInput() {
    return TomTaskInput.getInstance();
  }

  private TomEnvironment environment() {
    return TomEnvironment.getInstance();
  }

  private SymbolTable symbolTable() {
    return environment().getSymbolTable();
  }

  public TomStructureTable getStructTable() {
    TomList list = ast().makeList(debuggedStructureList);
    return `StructTable(list);
  }


  public int getLine() {
    return  token.beginLine;
  }

  private int getPos() {
    return jj_input_stream.getBeginOffset();
  }

  private String savePosAndExtract() {
    return savePosAndExtract(0);
  }
  
  private String savePosAndExtract(int offset) {
    //System.out.println("oldPos = " + oldPos);
    //System.out.println("getPos = " + getPos());
    //System.out.println("offset = " + offset);
    String string = tomBuffer.extractBuffer(oldPos,getPos() + offset);
    oldPos = getPos() + offset;
    return string;
  }

  private void putSymbol(String name, TomSymbol symbol) {
    symbolTable().putSymbol(name,symbol);
  }

  private void putType(String name, TomType type) {
    symbolTable().putType(name,type);
  }

  private void switchToTomMode() {
    token_source.SwitchTo(TOM);
  }

  private void switchToDefaultMode() {
    token_source.SwitchTo(DEFAULT);
    oldPos = getPos() + token.image.length();
    oldLine = getLine();
    orgTrack = makePosition(token.beginLine,token.beginColumn);
  }
  
  public Position makePosition(int line, int column) {
    return  `TextPosition(line, column);
  }
  
  private TargetLanguage makeTL(String code) {
    Position newOriginTracking = makePosition(token.beginLine,token.beginColumn);
    return `TL(code, orgTrack, newOriginTracking); 
  }
  
  private void addPreviousCode(LinkedList list) {
    String code = savePosAndExtract();
    String pureCode = code.replace('\t', ' ');
    if(pureCode.matches("\\s*")) {
      return;
    }
    if (code.equals("")) {return ;}
    list.add(makeTL(code));
  }
  
  private static boolean testIncludedFile(String fileName, HashSet fileSet) {
    // !(true) if the set did not already contain the specified element.
    return !fileSet.add(fileName);
  }
  
  private void includeFile(String fileName, LinkedList list) throws TomException, TomIncludeException {
    TomTerm astTom;
    InputStream input;
    byte inputBuffer[];
    TomParser tomParser;
    File file;
    String fileAbsoluteName = "";
    fileName = fileName.trim();
    fileName = fileName.replace('/',File.separatorChar);
    fileName = fileName.replace('\\',File.separatorChar);
		if(fileName.equals("")) {
			String msg = MessageFormat.format(TomMessage.getString("EmptyIncludedFile"), new Object[]{new Integer(getLine()), currentFile});
      throw new TomIncludeException(msg);
		}
    try {
      file = new File(fileName);
      if(file.isAbsolute()) {
        if (!file.exists()) {
          String msg = MessageFormat.format(TomMessage.getString("IncludedFileNotFound"), new Object[]{fileName, new Integer(getLine())});
          throw new TomIncludeException(msg);
        }
      } else {
        boolean found = false;
          // try first relative to inputfilename
        File parent = new File(currentFile).getParentFile();
        file = new File(parent, fileName).getCanonicalFile();
        found = file.exists();
        if(!found) {
            // Look for importList
          for(int i=0 ; !found && i<getInput().getImportList().size() ; i++) {
            file = new File((File)getInput().getImportList().get(i),fileName).getCanonicalFile();
            //System.out.println("look for: " + file.getPath());
            found = file.exists();
          }
          if(!found) {
            String msg = MessageFormat.format(TomMessage.getString("IncludedFileNotFound"), new Object[]{fileName, new Integer(getLine()), currentFile});
            throw new TomIncludeException(msg);
          }
        }
      }
      fileAbsoluteName = file.getAbsolutePath();
      if(testIncludedFile(fileAbsoluteName, includedFileSet)) {
        String msg = MessageFormat.format(TomMessage.getString("IncludedFileCycle"), new Object[]{fileName, new Integer(getLine()), currentFile});
          throw new TomIncludeException(msg);
      }

			// if trying to include a file twice, but not in a cycle : discard
      if(TomParser.testIncludedFile(fileAbsoluteName, alreadyParsedFileSet)) {    
				TomEnvironment.getInstance().messageWarning(TomMessage.getString("IncludedFileAlreadyParsed"), new Object[]{fileName, new Integer(getLine()), currentFile}, fileName,getLine());
				return;
// 				String msg = MessageFormat.format(TomMessage.getString("IncludedFileAlreadyParsed"), new Object[]{fileName, new Integer(getLine()), currentFile});
//         throw new TomIncludeException(msg);
      }
       
      tomParser = TomParser.createParser(fileAbsoluteName, includedFileSet, alreadyParsedFileSet);
      astTom = tomParser.startParsing();     
      astTom = `TomInclude(astTom.getTomList());
      list.add(astTom);
    } catch (Exception e) {
      if(e instanceof TomIncludeException) {
        throw (TomIncludeException)e;
      }
      String msg = MessageFormat.format(TomMessage.getString("ErrorWhileIncludindFile"), new Object[]{e.getClass(), fileAbsoluteName, currentFile, new Integer(getLine()), e.getMessage()});
      throw new TomException(msg);
    }
  }
}

PARSER_END(TomParser)


/*******************************************
 * THE TOM TOKEN SPECIFICATION STARTS HERE *
 *******************************************/

/*******************************************
 * DEFAULT MODE                            *
 *******************************************/
  
TOKEN :
{
    < MATCH:               "%match" > : TOM
  | < RULE:                "%rule" > : TOM
  | < MAKE_TERM:           "%make" > : TOM
  | < BACKQUOTE_TERM:      "`" > : BQ
  | < VARIABLE:            "%variable" > : TOM
  | < TYPE:                "%type" > : TOM
  | < TYPETERM:            "%typeterm" > : TOM
  | < TYPELIST:            "%typelist" > : TOM
  | < TYPEARRAY:           "%typearray" > : TOM
  | < INCLUDE:             "%include" > : TOM
  | < OPERATOR:            "%op" > : TOM
  | < OPERATOR_LIST:       "%oplist" > : TOM
  | < OPERATOR_ARRAY:      "%oparray" > : TOM
  | < VAS_SIGNATURE:       "%vas" > : TOM
}

/* WHITE SPACE */
SKIP :
{
  " "
| "\t"
| "\n"
| "\r"
| "\f"
}

/* COMMENTS */

MORE :
{
  "//" : IN_SINGLE_LINE_COMMENT
| <"/**" ~["/"]> { input_stream.backup(1); } : IN_FORMAL_COMMENT
| "/*" : IN_MULTI_LINE_COMMENT
}

<IN_SINGLE_LINE_COMMENT>
SPECIAL_TOKEN :
{
  <SINGLE_LINE_COMMENT: "\n" | "\r" | "\r\n" > : DEFAULT
}

<IN_FORMAL_COMMENT>
SPECIAL_TOKEN :
{
  <FORMAL_COMMENT: "*/" > : DEFAULT
}

<IN_MULTI_LINE_COMMENT>
SPECIAL_TOKEN :
{
  <MULTI_LINE_COMMENT: "*/" > : DEFAULT
}

<IN_SINGLE_LINE_COMMENT,IN_FORMAL_COMMENT,IN_MULTI_LINE_COMMENT>
MORE :
{
  < ~[] >
}

SPECIAL_TOKEN :
{
 < STRING_LITERAL:
      "\""
      (   (~["\"","\\","\n","\r"])
        | ("\\"
            ( ["n","t","b","r","f","\\","'","\""]
            | ["0"-"7"] ( ["0"-"7"] )?
            | ["0"-"3"] ["0"-"7"] ["0"-"7"]
            )
          )
      )*
      "\""
  >
}

/* SEPARATORS */

TOKEN :
{
  < LBRACE: "{" >
| < RBRACE: "}" >
| < OTHER: ~[] >
}

/************************************************************
 * TOM MODE
 ************************************************************/

/* TOM WHITE SPACE */
<TOM>
SKIP :
{
  " "
| "\t"
| "\n"
| "\r"
| "\f"
| < ( " " | "\t" | "\n" | "\r" )+     >
| < "<!--" ( ~["-"] | ( "-" ~["-"] ) )* "-->" >
| < "<?"   (~[">"])* ">"                      >
}

/* TOM COMMENTS */
<TOM>
MORE :
{
  "//" : TOM_IN_SINGLE_LINE_COMMENT
|
  <"/**" ~["/"]> { input_stream.backup(1); } : TOM_IN_FORMAL_COMMENT
|
  "/*" : TOM_IN_MULTI_LINE_COMMENT
}

<TOM_IN_SINGLE_LINE_COMMENT>
SPECIAL_TOKEN :
{
  <TOM_SINGLE_LINE_COMMENT: "\n" | "\r" | "\r\n" > : TOM
}

<TOM_IN_FORMAL_COMMENT>
SPECIAL_TOKEN :
{
  <TOM_FORMAL_COMMENT: "*/" > : TOM
}

<TOM_IN_MULTI_LINE_COMMENT>
SPECIAL_TOKEN :
{
  <TOM_MULTI_LINE_COMMENT: "*/" > : TOM
}

<TOM_IN_SINGLE_LINE_COMMENT,TOM_IN_FORMAL_COMMENT,TOM_IN_MULTI_LINE_COMMENT>
MORE :
{
  < ~[] >
}

/* TOM IDENTIFIERS */
<TOM>
TOKEN :
{
  < TOM_ARROW:       "->" >
| < TOM_DOUBLE_ARROW:"=>" >
| < TOM_ALTERNATIVE: "|" >
| < TOM_COMMA:       "," >
| < TOM_COLON:       ":" >
| < TOM_EQUAL:       "=" >
| < TOM_AT:          "@" >
| < TOM_DOT:         "." >
| < TOM_LPAREN:      "(" >
| < TOM_RPAREN:      ")" >
| < TOM_LBRACKET:    "[" >
| < TOM_RBRACKET:    "]" >
| < TOM_LBRACE:      "{" >
| < TOM_RBRACE:      "}" >
| < TOM_UNDERSCORE:  "_" >
| < TOM_MINUS:       "-" >
| < TOM_PLUS:        "+" >
| < TOM_STAR:        "*" >
| < TOM_SHARP:       "#" >
| < TOM_AND:         "&" >
| < TOM_WHERE:       "where" >
| < TOM_IF:          "if" >
| < TOM_BACKQUOTE:   "`" >
| < TOM_QUOTE:       "'" >
| < TOM_MAKE:        "make" >
| < TOM_MAKE_EMPTY:  "make_empty" >
| < TOM_MAKE_INSERT: "make_insert" >
| < TOM_MAKE_APPEND: "make_append" >
| < TOM_IMPLEMENT:   "implement" >
| < TOM_FSYM:        "fsym" >
| < TOM_GET_SLOT:    "get_slot" >
| < TOM_IS_FSYM:     "is_fsym" >
| < TOM_GET_SUBTERM: "get_subterm" >
| < TOM_GET_FUN_SYM: "get_fun_sym" >
| < TOM_CMP_FUN_SYM: "cmp_fun_sym" >
| < TOM_EQUALS:      "equals" >
| < TOM_GET_HEAD:    "get_head" >
| < TOM_GET_TAIL:    "get_tail" >
| < TOM_IS_EMPTY:    "is_empty" >
| < TOM_GET_ELEMENT: "get_element" >
| < TOM_GET_SIZE:    "get_size" >
| < TOM_INTEGER: (<MINUS_SIGN>)? <TOM_DIGIT> (<TOM_DIGIT>)* >
| < TOM_CHAR:  <TOM_QUOTE> (<TOM_LETTER>)+ <TOM_QUOTE>>
| < TOM_IDENTIFIER: <TOM_LETTER> (<TOM_LETTER>|<TOM_DIGIT>|<TOM_UNDERSCORE>| (<TOM_MINUS><TOM_LETTER>))* >
| < TOM_DOUBLE:
        (["0"-"9"])+ "." (["0"-"9"])* (<EXPONENT>)? (["f","F","d","D"])?
      | "." (["0"-"9"])+ (<EXPONENT>)? (["f","F","d","D"])?
      | (["0"-"9"])+ <EXPONENT> (["f","F","d","D"])?
      | (["0"-"9"])+ (<EXPONENT>)? ["f","F","d","D"]
  >
| < #EXPONENT: ["e","E"] (["+","-"])? (["0"-"9"])+ >
| < #MINUS_SIGN: ["-"] >
| < #TOM_LETTER:
    [ "a"-"z", "A"-"Z" ]
  >
| < #TOM_DIGIT:
    [ "0"-"9" ]
  >
| < TOM_STRING:
      "\""
      (   (~["\"","\\","\n","\r"])
        | ("\\"
            ( ["n","t","b","r","f","\\","'","\""]
            | ["0"-"7"] ( ["0"-"7"] )?
            | ["0"-"3"] ["0"-"7"] ["0"-"7"]
            )
          )
      )*
      "\""
  >
        
| < XML_NAME: (<TOM_LETTER>|<TOM_DIGIT>)+ (<TOM_LETTER>|<TOM_DIGIT> | "_" )* >
| < XML_START: "<" >
| < XML_START_ENDING: "</" >
| < XML_CLOSE: ">" >
| < XML_CLOSE_SINGLETON: "/>" >
| < DOUBLE_QUOTE: "\"">
| < XML_TEXT: "#TEXT">
| < XML_COMMENT: "#COMMENT">
| < XML_PROC: "#PROCESSING-INSTRUCTION">
| < TOM_OTHER: ~[] >
}


/*********************************************
 * THE TOM GRAMMAR SPECIFICATION STARTS HERE *
 *********************************************/

TomTerm startParsing() throws TomException : /* in DEFAULT mode */
{
  String upToEOF; 
  TomTerm parseTree = null;
  LinkedList blockList = new LinkedList();
}
{
  BlockList(blockList)
    {
      upToEOF = tomBuffer.extractBuffer(oldPos,tomBuffer.getCount()-1);
      blockList.add(makeTL(upToEOF));
      String comment = "Generated by TOM (version " + Tom.VERSION + "): Do not edit this file";
      blockList.addFirst(`Comment(comment));
      parseTree = `Tom(ast().makeList(blockList));
    }
  <EOF>
    {
      return parseTree;
    } 
}

void BlockList(LinkedList blockList) throws TomException : /* in DEFAULT mode */
{}
{
  (
      MatchConstruct(blockList)
    | RuleConstruct(blockList)
    | BackQuoteTerm(blockList)
    | Signature(blockList)
    | IncludeConstruct(blockList)
    | LocalVariableConstruct(blockList)
    | Operator(blockList)
    | OperatorList(blockList)
    | OperatorArray(blockList)
    | TypeTerm(blockList)     
    | TypeList(blockList)     
    | TypeArray(blockList)
    | <LBRACE> BlockList(blockList) <RBRACE>
    | <OTHER>
  )*
}

void MatchConstruct(LinkedList list) throws TomException: /* in DEFAULT mode */
{
  LinkedList matchArgumentsList = new LinkedList();
  LinkedList patternActionList = new LinkedList();
  LinkedList optionList = new LinkedList();
}
{
  <MATCH> /* switch to TOM mode */
    {
      addPreviousCode(list);
      Option ot = `OriginTracking(Name("Match"),getLine(), Name(currentFile));
      optionList.add(ot);
      String debugKey = ot.getFileName().getString() + ot.getLine();
    }

  <TOM_LPAREN> MatchArguments(matchArgumentsList) <TOM_RPAREN>

  <TOM_LBRACE> ( PatternAction(patternActionList, debugKey) )* <TOM_RBRACE> /* we are in TOM mode */
    {
      switchToDefaultMode(); /* switch to DEFAULT mode */
      OptionList option = ast().makeOptionList(optionList);
      Instruction match = `Match(SubjectList(ast().makeList(matchArgumentsList)),
                           PatternList(ast().makeList(patternActionList)),
                           option);
      list.add(match);
      if (getInput().isDebugMode())
        debuggedStructureList.add(match);
    }
}

void PatternAction(LinkedList list, String debugKey) throws TomException: /* in TOM mode */
{
  LinkedList matchPatternsList = new LinkedList();
  LinkedList listTextPattern = new LinkedList();
  LinkedList listOrgTrackPattern = new LinkedList();
  LinkedList listOfMatchPatternsList = new LinkedList();
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option option;
  Token label = null;
  text = "";
}
{

  [ LOOKAHEAD(2) label=<TOM_IDENTIFIER> <TOM_COLON> ]
  option = MatchPatterns(matchPatternsList)
    { listOfMatchPatternsList.add(ast().makeList(matchPatternsList));
      matchPatternsList.clear();
      listTextPattern.add(text);text = "";
      listOrgTrackPattern.add(option);
    }
  [ ( <TOM_ALTERNATIVE>  MatchPatterns(matchPatternsList) 
    { listOfMatchPatternsList.add(ast().makeList(matchPatternsList));
      matchPatternsList.clear();
      listTextPattern.add(text);text = "";
      listOrgTrackPattern.add(option);
    }
    )+ ]
  <TOM_ARROW>
    {
      if(getInput().isDebugMode()) {
        blockList.add(`ITL("jtom.debug.TomDebugger.debugger.patternSuccess(\""+debugKey+"\");\n"));
        if(getInput().isDebugMemory()) {
          blockList.add(`ITL("jtom.debug.TomDebugger.debugger.emptyStack();\n"));
        }
      }
    }
  tlCode = GoalLanguageBlock(blockList)
    {
      blockList.add(tlCode);
      TomList patterns;
      String patternText = "";
      LinkedList optionList = new LinkedList();
      if(label != null) {
        optionList.add(`Label(Name(label.image)));
      }
      for(int i=0 ;  i<listOfMatchPatternsList.size() ; i++) {
        patterns = (TomList)listOfMatchPatternsList.get(i);
        patternText = (String)listTextPattern.get(i);
          //TODO solve with xmlterm
          //if (patternText == null) patternText = "";
        optionList.add(listOrgTrackPattern.get(i));
        optionList.add(`OriginalText(Name(patternText)));
        list.add(`PatternAction(
                   TermList(patterns),
                   AbstractBlock(ast().makeInstructionList(blockList)),
                   ast().makeOptionList(optionList)));
      }
    }
}

void MatchArguments(LinkedList list) throws TomException: /* in TOM mode */
{}
{
  MatchArgument(list) ( <TOM_COMMA> MatchArgument(list) )*
}

void MatchArgument(LinkedList list) throws TomException: /* in TOM mode */
{
  Token type,name;
  Token backquote = null;
  String variableName;
}
{
  type=<TOM_IDENTIFIER> [backquote=<TOM_BACKQUOTE>] name=<TOM_IDENTIFIER>
  {
    if(backquote!=null) {
        //variableName = ast().makeTomVariableName(name.image);
      variableName = name.image;
    } else {
      variableName = name.image;
    }
    list.add(`TLVar(
                   variableName,
                   TomTypeAlone(type.image)));
  }
}

Option MatchPatterns(LinkedList list) throws TomException: /* in TOM mode */
{
  TomTerm term;
  Option ot;
} 
{
  term=AnnotedTerm()
    {
      list.add(term);
      ot = `OriginTracking(Name("Pattern"),getLine(), Name(currentFile));
    }
    ( <TOM_COMMA> {text += "\n";} term=AnnotedTerm() { list.add(term); } )*
    { return ot;}
}


TomName HeadSymbol(LinkedList optionList)  throws TomException:
{
  Token name;
}
{
    // identifier | integer | double | string
  ( name = <TOM_IDENTIFIER> |
    name = <TOM_INTEGER> |
    name = <TOM_CHAR> |
    name = <TOM_DOUBLE> |
    name = <TOM_STRING>)
    {
      text += name.image;
      optionList.add(`OriginTracking(Name(name.image),getLine(), Name(currentFile)));
      switch(name.kind) {
          case TOM_INTEGER:
            ast().makeIntegerSymbol(symbolTable(),name.image,optionList);
            break;
          case TOM_CHAR:
            ast().makeCharSymbol(symbolTable(),name.image,optionList);
            break;
          case TOM_DOUBLE:
            ast().makeDoubleSymbol(symbolTable(),name.image,optionList);
            break;
          case TOM_STRING:
            ast().makeStringSymbol(symbolTable(),name.image,optionList);
            break;
          default:
      }
      return `Name(name.image);
    }
}

NameList HeadSymbolList(LinkedList optionList)  throws TomException:
{
  TomName name;
  LinkedList nameList = new LinkedList();
}
{
  (
  name = HeadSymbol(optionList) { nameList.add(name); }
  |
    <TOM_LPAREN> { text += "("; }
    name = HeadSymbol(optionList)  { nameList.add(name); }
    <TOM_ALTERNATIVE> { text += "|"; }
    name = HeadSymbol(optionList)  { nameList.add(name); }
    ( <TOM_ALTERNATIVE> { text += "|"; }
      name = HeadSymbol(optionList)  { nameList.add(name); }
    )*
    <TOM_RPAREN> { text += ")"; }
  )
  {
    return ast().makeNameList(nameList);
  }
}

TomTerm UnamedVariableStarOrVariableStar(LinkedList optionList, LinkedList constraintList)  throws TomException:
{
  Token name;
  OptionList option;
  ConstraintList constraint;
}
{ // X* | _*
  (name = <TOM_IDENTIFIER> | name = <TOM_UNDERSCORE>) <TOM_STAR>
    {
      text += name.image + "*";
      optionList.add(`OriginTracking(Name(name.image),getLine(), Name(currentFile)));
      option = ast().makeOptionList(optionList);
      constraint = ast().makeConstraintList(constraintList);
      if(name.kind == TOM_UNDERSCORE) {
        return ast().makeUnamedVariableStar(option,"unknown type",constraint);
      } else {
        return ast().makeVariableStar(option,name.image,"unknown type",constraint);
      }
    }
}

TomTerm Placeholder(LinkedList optionList, LinkedList constraintList) throws TomException:
{
  OptionList option;
  ConstraintList constraint;
}
{ // _
  <TOM_UNDERSCORE>
    {
      text += "_";
      optionList.add(`OriginTracking(Name("_"),getLine(), Name(currentFile)));
      option = ast().makeOptionList(optionList);
      constraint = ast().makeConstraintList(constraintList);
      return `Placeholder(option, constraint);
    }
}

TomTerm PlainTerm(TomName astAnnotedName) throws TomException: /* in TOM mode */
{
  LinkedList list = new LinkedList();
  Token name;
  TomTerm term;
  LinkedList optionList = new LinkedList();
  LinkedList constraintList = new LinkedList();
  NameList nameList = null;
  boolean implicit = false;
  boolean withArgs = false;
  Constraint annotedName = (astAnnotedName==null)?null:ast().makeAssignTo(astAnnotedName,getLine(), currentFile);
  if(annotedName!=null) { constraintList.add(annotedName); }
}
{
    term = XMLTerm(optionList, constraintList) { return term; }
  | // X* | _*
    LOOKAHEAD(2)
    term = UnamedVariableStarOrVariableStar(optionList, constraintList) {return term; }
  | // _
    term = Placeholder(optionList, constraintList) {return term; }
  | // f(a,...) | f[slot=a,...]
    (
      LOOKAHEAD(3)
      nameList = HeadSymbolList(optionList)
      [ LOOKAHEAD( {getToken(1).kind==TOM_LPAREN} )
        implicit = ExplicitTermList(list) { withArgs = true; }
      | implicit = ImplicitPairList(list) { withArgs = true; } 
      ]
      {
        if(withArgs && list.isEmpty()) {
            // check if it is a constant
          optionList.add(`Constructor(nameList));
        }
        if(implicit) {
          return `RecordAppl(ast().makeOptionList(optionList),nameList,ast().makeList(list),ast().makeConstraintList(constraintList));
        } else {
          return `Appl(ast().makeOptionList(optionList),nameList,ast().makeList(list),ast().makeConstraintList(constraintList));
        }
      }
    | implicit = ExplicitTermList(list)
      {
        nameList = `concTomName(Name(""));
        optionList.add(`OriginTracking(Name(""),getLine(),Name( currentFile)));
        return `Appl(ast().makeOptionList(optionList),nameList,ast().makeList(list),ast().makeConstraintList(constraintList));
      }
    )
      
}

// return true for implicit mode
boolean ImplicitTermList(LinkedList list) throws TomException: /* in TOM mode */
{
  TomTerm term;
}
{
  <TOM_LBRACKET> { text+="["; }
    [
      term = AnnotedTerm() { list.add(term); }
      ( <TOM_COMMA> {text += ",";} term = AnnotedTerm() { list.add(term); } )*
    ]
  <TOM_RBRACKET>
  {
    text+="]";
    return true;
  }
}

boolean ExplicitTermList(LinkedList list) throws TomException: /* in TOM mode */
{
  TomTerm term;
}
{
  <TOM_LPAREN> { text+="("; }
    [
      term = AnnotedTerm() { list.add(term); }
      ( <TOM_COMMA> {text += ",";} term = AnnotedTerm() { list.add(term); } )*
    ]
  <TOM_RPAREN>
  {
    text+=")";
    return false;
  }
}

// This corresponds to the implicit notation
boolean XMLTermList(LinkedList list) throws TomException: /* in TOM mode */
{
  TomTerm term;
}
{
  ( term = AnnotedTerm() { list.add(term); } )*
  { return true; }
}

boolean ImplicitPairList(LinkedList list) throws TomException: /* in TOM mode */
{
  TomTerm term;
}
{
  <TOM_LBRACKET> { text += "["; }
    [ term = PairTerm() { list.add(term); }
      ( <TOM_COMMA> {text += ",";} term = PairTerm() { list.add(term); } )*
    ]
  <TOM_RBRACKET>
  {
    text+="]";
    return true;
  }
}

boolean ExplicitPairList(LinkedList list) throws TomException: /* in TOM mode */
{
  TomTerm term;
}
{
  <TOM_LPAREN> { text += "("; }
    [ term = PairTerm() { list.add(term); }
      ( <TOM_COMMA> {text += ",";} term = PairTerm() { list.add(term); } )*
    ]
  <TOM_RPAREN>
  {
    text+=")";
    return false;
  }
}


TomTerm PairTerm() throws TomException: /* in TOM mode */
{
  Token name;
  TomTerm term;
}
{
  name = <TOM_IDENTIFIER> <TOM_EQUAL> term = AnnotedTerm()
      {
        text += name.image + "=";  
        return `PairSlotAppl(Name(name.image),term);
      }
}


TomTerm AnnotedTerm() throws TomException: /* in TOM mode */
{
  Token annotedName = null;
  TomTerm term;
  TomName astAnnotedName = null;
}
{
    // foo@PlainTerm()
  [
    LOOKAHEAD(2)
    annotedName = <TOM_IDENTIFIER> <TOM_AT>
    {
      text += annotedName.image+"@";
      astAnnotedName = `Name(annotedName.image);
    }
  ]
    term = PlainTerm(astAnnotedName)
    {
      return term;
    }
}

NameList XMLNameList(LinkedList optionList, boolean needOrgTrack) throws TomException:
{
  Token name;
  NameList nameList;
  String XMLName = "";
  int decLine;
}
{
  (
    name=<TOM_IDENTIFIER>
    {
      text += name.image;
      XMLName += name.image;
      decLine = getLine();
      nameList = `concTomName(Name(name.image));
    }
   |
    name=<TOM_UNDERSCORE>
    {
      text += name.image;
      XMLName += name.image;
      decLine = getLine();
      nameList = `concTomName(Name(name.image));
    }
   |
    <TOM_LPAREN>
    name=<TOM_IDENTIFIER>
    {
      text += name.image;
      XMLName += name.image;
      decLine = getLine();
      nameList = `concTomName(Name(name.image));
    }
    ( <TOM_ALTERNATIVE> name=<TOM_IDENTIFIER>
      {
        text += "|" + name.image;
        XMLName += "|" + name.image;
        nameList = (NameList)nameList.append(`Name(name.image));
      }
    )+
    <TOM_RPAREN>
   )
    {
      if(needOrgTrack) {optionList.add(`OriginTracking(Name(XMLName), decLine,Name( currentFile)));}
      return nameList;
    }
}

TomTerm XMLTerm(LinkedList optionList,LinkedList constraintList) throws TomException:
{
  TomTerm term;
  TomTerm arg1;
  TomTerm arg2;
  LinkedList pairSlotList = new LinkedList();
  LinkedList attributeList = new LinkedList();
  LinkedList childs = new LinkedList();
  String keyword = "";
  boolean implicit;
  NameList nameList;
  NameList closingNameList;
  OptionList option;
  ConstraintList constraint;
}
{
  (
    // < NODE attributes [ /> | > childs </NODE> ]
    <XML_START> { text += "<"; }
     nameList = XMLNameList(optionList, true)
     implicit = XMLAttributeList(attributeList)
        {
          if(implicit) { optionList.add(`ImplicitXMLAttribut()); }
        }
     (
       // case: /> 
       <XML_CLOSE_SINGLETON>
         {
           text += "\\>";
           option =  ast().makeOptionList(optionList);
         }
     | // case: > childs  </NODE>
       <XML_CLOSE>{text+=">";}
       implicit = XMLChilds(childs)
       <XML_START_ENDING> { text += "</"; }
       closingNameList = XMLNameList(optionList, false)
       <XML_CLOSE> { text += ">";}
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
            /*throw new TomException("Error on closing XML pattern: expecting '"+ expected.substring(1) +"' but got '"+found.substring(1)+ "' at line "+getLine());
            return null;*/
            // TODO find the orgTrack of the match
            environment().messageError(getLine(),
                     currentFile,
                     "match",
                     getLine(),
                     TomMessage.getString("MalformedXMLTerm"), 
                     new Object[]{expected.substring(1), found.substring(1)});
          }
          if(implicit) {
              /*
               * Special case
               * when XMLChilds() is reduced to a singleton
               * Appl(...,Name(""),args)
               */
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
          term =  `XMLAppl(
            option,
            nameList,
            ast().makeList(attributeList),
            ast().makeList(childs),
            ast().makeConstraintList(constraintList));
          return term;
        }
    
  // #TEXT(...)
//  | <XML_TEXT> <TOM_LPAREN> arg1 = TermStringIdentifier(null) <TOM_RPAREN>
//       | <XML_TEXT> <TOM_LPAREN> arg1 = PlainTerm(null) <TOM_RPAREN>
    | <XML_TEXT> <TOM_LPAREN> arg1 = AnnotedTerm() <TOM_RPAREN>
    {
      keyword = Constants.TEXT_NODE;
      pairSlotList.add(`PairSlotAppl(Name(Constants.SLOT_DATA),arg1));
    }
  | // #COMMENT(...)
    <XML_COMMENT> <TOM_LPAREN> arg1 = TermStringIdentifier(null) <TOM_RPAREN>
    {
      keyword = Constants.COMMENT_NODE;
      pairSlotList.add(`PairSlotAppl(Name(Constants.SLOT_DATA),arg1));
    }
  | // #PROCESSING-INSTRUCTION(... , ...)
    <XML_PROC> <TOM_LPAREN>
    arg1=TermStringIdentifier(null) <TOM_COMMA> arg2=TermStringIdentifier(null)
    <TOM_RPAREN>
      {
        keyword = Constants.PROCESSING_INSTRUCTION_NODE;
        pairSlotList.add(`PairSlotAppl(Name(Constants.SLOT_TARGET),arg1));
        pairSlotList.add(`PairSlotAppl(Name(Constants.SLOT_DATA),arg2));
      }
     )
    {
      optionList.add(`OriginTracking(Name(keyword),getLine(),Name( currentFile)));
      option = ast().makeOptionList(optionList);
      constraint = ast().makeConstraintList(constraintList);
      nameList = `concTomName(Name(keyword));
      return `RecordAppl(option,
                                    nameList,
                                    ast().makeList(pairSlotList),
                                    constraint);

    }
}

  
TomTerm TermStringIdentifier(LinkedList options) throws TomException:
{
  LinkedList optionList = (options==null)?new LinkedList():options;
  OptionList option;
  Token name;
  NameList nameList;
}
{
  ( name = <TOM_IDENTIFIER> | name = <TOM_STRING> )
    {
      text += name.image;
      optionList.add(`OriginTracking(Name(name.image),getLine(),Name( currentFile)));
      option = ast().makeOptionList(optionList);
      if(name.kind == TOM_STRING) {
        ast().makeStringSymbol(symbolTable(),name.image,optionList);
      }
      nameList = `concTomName(Name(name.image));
      return `Appl(
        option,
        nameList,
        concTomTerm(),
        concConstraint());
    }
}

TomTerm UnamedVariableOrTermStringIdentifier(LinkedList options) throws TomException:
{
  LinkedList optionList = (options==null)?new LinkedList():options;
  OptionList option;
  Token name;
  NameList nameList;
}
{
  ( name = <TOM_UNDERSCORE> | name = <TOM_IDENTIFIER> | name = <TOM_STRING>)
  {
    text += name.image;
    optionList.add(`OriginTracking(Name(name.image),getLine(),Name( currentFile)));
    option = ast().makeOptionList(optionList);
    if(name.kind == TOM_UNDERSCORE) {
      return `UnamedVariable(option,TomTypeAlone("unknown type"),concConstraint());
    } else {
      if(name.kind == TOM_STRING) {
        ast().makeStringSymbol(symbolTable(),name.image,optionList);
      }
      nameList = `concTomName(Name(name.image));
      return `Appl(
                   option,
                   nameList,
                   concTomTerm(),
                   concConstraint());
    }
  }
}

boolean XMLChilds(LinkedList list) throws TomException:
{
  LinkedList childs = new LinkedList();
  Iterator it;
  boolean implicit;
}
{
  (LOOKAHEAD(2)
   implicit = ImplicitTermList(childs)
     // | implicit = ExplicitTermList(childs)
     | implicit = XMLTermList(childs)
   )
    {
      it = childs.iterator();
      while(it.hasNext()) {
        list.add(tomFactory.metaEncodeXMLAppl(symbolTable(),(TomTerm)it.next()));
      }
      return implicit;
    }
}

boolean XMLAttributeList(LinkedList list) throws TomException: /* in TOM mode */
{
  Token name;
  TomTerm term;
}
{
  <TOM_LBRACKET> { text += "["; }
    [ term = XMLAttribute() { list.add(term); }
      ( <TOM_COMMA> {text += ",";} term = XMLAttribute() { list.add(term); } )*
    ]
  <TOM_RBRACKET>
  {
    text+="]";
    return true;
  }
  |
  <TOM_LPAREN> { text += "("; }
    [ term = XMLAttribute() { list.add(term); }
      ( <TOM_COMMA> {text += ",";} term = XMLAttribute() { list.add(term); } )*
    ]
  <TOM_RPAREN>
  {
    text+=")";
    return false;
  }
    | // XML notation is implicit
  ( term = XMLAttribute() { list.add(term); } )*
  { return true; }
}

TomTerm XMLAttribute() throws TomException:
{
  LinkedList list = new LinkedList();
  TomTerm term;
  TomTerm termName = null;
  Token id,anno1,anno2;
  String name;
  OptionList option;
  ConstraintList constraint;
  LinkedList optionList = new LinkedList();
  LinkedList constraintList = new LinkedList();
  LinkedList optionListAnno2 = new LinkedList();
  NameList nameList;
}
{
    // _* | X*
  LOOKAHEAD(2)
  term = UnamedVariableStarOrVariableStar(optionList,constraintList) { return term; }
  |
  ( // name = [anno2@](_|String|Identifier)
    LOOKAHEAD(2)
    id = <TOM_IDENTIFIER> <TOM_EQUAL> {text+=id.image+"=";}
    [
      LOOKAHEAD(2)
    anno2 = <TOM_IDENTIFIER> <TOM_AT>
    {
      text += anno2.image + "@";
      optionListAnno2.add(`Name(anno2.image));
    }
    ]
    term=UnamedVariableOrTermStringIdentifier(optionListAnno2)
      {
        name = tomFactory.encodeXMLString(symbolTable(),id.image);
        nameList = `concTomName(Name(name));
        termName = `Appl(ast().makeOption(),nameList,concTomTerm(),concConstraint());
      }
  | // [anno1@]_ = [anno2@](_|String|Identifier)
    [ 
    anno1 = <TOM_IDENTIFIER> <TOM_AT>
    {
      text += anno1.image + "@";
      optionList.add(`Name(anno1.image));
    }
    ]
    termName = Placeholder(optionList,constraintList) <TOM_EQUAL> {text+="=";}
    [
      LOOKAHEAD(2)
    anno2 = <TOM_IDENTIFIER> <TOM_AT>
    {
      text += anno2.image + "@";
      optionListAnno2.add(`Name(anno2.image));
    }
    ]
    term=UnamedVariableOrTermStringIdentifier(optionListAnno2)
  ) 
    {
      list.add(`PairSlotAppl(Name(Constants.SLOT_NAME),termName));
        // we add the specif value : _
      list.add(`PairSlotAppl(Name(Constants.SLOT_SPECIFIED),Placeholder(ast().makeOption(),ast().makeConstraint())));
        //list.add(tomFactory.metaEncodeXMLAppl(symbolTable(),term));
        // no longer necessary ot metaEncode Strings in attributes
      list.add(`PairSlotAppl(Name(Constants.SLOT_VALUE),term));
      optionList.add(`OriginTracking(Name(Constants.ATTRIBUTE_NODE),getLine(),Name( currentFile)));
      option = ast().makeOptionList(optionList);
      constraint = ast().makeConstraintList(constraintList);
      
      nameList = `concTomName(Name(Constants.ATTRIBUTE_NODE));
      return `RecordAppl(option,
                                    nameList,
                                    ast().makeList(list),
                                    constraint);
    }
}

  /*
   * The goal of this function is to parse a backquoted term
   * There are 4 cases:
   *   `(...)
   *   `identifier(...)
   *   `identifier
   *   `identifier*
   * 
   */
void BackQuoteTermOld(LinkedList list) throws TomException: /* in DEFAULT mode */
{
  TomTerm term;
  Option ot;
  Token tk, nextTk;
  int parenLevel = 0, lastTokenLine = 0, firstTokenLine = 0;
  boolean backQuoteMode = false;
  LinkedList tokenList = new LinkedList();
  HashMap tokenToDecLineMap = new HashMap();
}
{
  <BACKQUOTE_TERM> /* switch to TOM mode */
  {
    addPreviousCode(list);
    firstTokenLine = getLine();
    ot = `OriginTracking(Name("Backquote"),getLine(),Name( currentFile));
  }
    (
      tk=<TOM_LPAREN> { tokenList.add(tk); 
			parenLevel++; 
			backQuoteMode = true;
                      }
    | tk=<TOM_IDENTIFIER> { tokenList.add(tk);
                            tokenToDecLineMap.put(tk, new Integer(getLine()));
                            switchToDefaultMode(); /* switch to DEFAULT mode */
                          }
    )
    {
      try {
        nextTk = getToken(1);
	//System.out.println("getToken(1).kind = " + nextTk.kind + " --> '" + nextTk.image + "'");
        if(backQuoteMode || nextTk.image.equals("(")) {
            /*
             * cases:
             *   `(...)
             *   `identifier(...)
             *
             * nextTk has been tokenized in either in Tom mode, either in Default mode
             * this is why we test nextTk.image.equals("(")
             * the first tk may be tokenized in Default mode
             * since this token cannot be pushed back and re-tokenized in Tom mode
             * we have to analyse the image and not only the kind
             * (also in TomBackQuoteParser.t)
             */
          switchToTomMode(); /* switch to TOM mode */
          backQuoteMode = true;
          while(backQuoteMode) {
            tk = getNextToken();
	    //System.out.println("tk.kind = " + tk.kind + " --> '" + tk.image + "'");
            tokenList.add(tk);
            lastTokenLine = getLine();
            if(tk.kind == TOM_LPAREN || tk.image.equals("(")) { 
              parenLevel++;
            } else if(tk.kind == TOM_RPAREN) {
              parenLevel--;
              if(parenLevel==0) {
                backQuoteMode = false;
              }
            } else if (tk.kind == TOM_IDENTIFIER) {
              tokenToDecLineMap.put(tk, new Integer(lastTokenLine));
            }
          }
          term = tomBackQuoteParser.buildBackQuoteTerm(tokenList, tokenToDecLineMap, currentFile);
                
          if(term.isComposite() && term.getArgs().isSingle()) {
            TomTerm backQuoteTerm = term.getArgs().getHead(); 
            if(backQuoteTerm.getAstName().getString().equals("xml")) {
              TomList args = backQuoteTerm.getArgs();
              term = `DoubleBackQuote(args);
            } 
          } 
          list.add(term);
          switchToDefaultMode(); /* switch to DEFAULT mode */
        } else {
            /*
             * we are in DEFAULT mode
             * nextTk has been tokenized in Default mode
             */
          if(nextTk.image.equals("*")) {
              /*
               * the backquote-term is a single variable-star
               */
            term = tomBackQuoteParser.buildVariableStar(tk.image,lastTokenLine,currentFile);
            list.add(term);
              /*
               * here we consume the "*" and we go to default mode to
               * remove the "*" from the "previous code"
               */
            tk = getNextToken();
            switchToDefaultMode(); /* switch to DEFAULT mode */
          } else {
            /*
             * the backquote-term is a single variable
             */
          term = tomBackQuoteParser.buildBackQuoteAppl(tk.image,lastTokenLine,currentFile);
          list.add(term);
          }
        }
      } catch (TokenMgrError error) {
        throw new TomException(MessageFormat.format(TomMessage.getString("InvalidBackQuoteTerm"), new Object[]{new Integer(firstTokenLine)}));
      }  
    }
 }

void Signature(LinkedList list) throws TomException: /* in DEFAULT mode */
{
  TargetLanguage vasTL;
  String vasCode, fileName = "", apiName = null, packageName ="";
  File file;
  LinkedList blockList = new LinkedList();
	int initialVasLine;
}
{
  <VAS_SIGNATURE> /* switch to TOM mode */
    {
      addPreviousCode(list);
			initialVasLine = getLine();
    }
  vasTL = GoalLanguageBlock(list)
    {
      switchToDefaultMode(); /* switch to DEFAULT mode */
      vasCode = vasTL.getCode().trim();
			String destDir = getInput().getDestDir().getPath();
			
        // Generated Tom, ADT and API from VAS Code
      Object generatedADTName = null;
      try{
        Class vasClass = Class.forName("vas.Vas");
				
				Method execMethod = vasClass.getMethod(
					"externalExec",
					new Class[]{(new String[]{}).getClass(), 
											Class.forName("java.io.InputStream"),
											Class.forName( "java.lang.String")
					});

				ArrayList vasParams = new ArrayList();
				// vasParams.add("--verbose");
				vasParams.add("--destdir");
				vasParams.add(destDir);
				packageName = getInput().getPackagePath().replace(File.separatorChar, '.');
        String inputFileNameWithoutExtension = getInput().getRawFileName().toLowerCase();
        String subPackageName = "";
        if(packageName.equals("")) {
          subPackageName = inputFileNameWithoutExtension;
        } else {
          subPackageName = packageName + "." + inputFileNameWithoutExtension;
        }
        vasParams.add("--package");
        vasParams.add(subPackageName);

				Object[] realParams = {
											(String[]) vasParams.toArray(new String[vasParams.size()]),
											new ByteArrayInputStream(vasCode.getBytes()),
											currentFile};
				generatedADTName = execMethod.invoke(vasClass, realParams);
      } catch (ClassNotFoundException e) {
        throw new TomException(TomMessage.getString("VasClassNotFound"));
      } catch (InvocationTargetException e) {
				System.out.println("vas problem " +e);
				System.out.println("vas problen cause " +e.getCause());
				System.out.println("vas problen target exception " +e.getTargetException());
        throw new TomException(MessageFormat.format(TomMessage.getString("VasInvocationIssue"), new Object[]{e.getMessage()}));
      } catch (Exception e) {
        throw new TomException(MessageFormat.format(TomMessage.getString("VasInvocationIssue"), new Object[]{e.getMessage()}));
      }
			  // Check for errors
			try{
        Class vasEnvironmentClass = Class.forName("vas.VasEnvironment");
				Method getErrorMethod = vasEnvironmentClass.getMethod("getErrors", new Class[]{});
				Method getInstanceMethod = vasEnvironmentClass.getMethod("getInstance", new Class[]{});

				Object vasEnvInstance = getInstanceMethod.invoke(vasEnvironmentClass, new Object[]{});
				TomAlertList alerts = (TomAlertList)(getErrorMethod.invoke(vasEnvInstance, new Object[]{}));
				TomAlert alert;
				if(!alerts.isEmpty()) {
					while(!alerts.isEmpty()) {
						alert = alerts.getHead();
            if(alert.isError()) {
              TomEnvironment.getInstance().messageError(alert.getMessage(),
                                                        currentFile, alert.getLine()+initialVasLine);
            } else {
              TomEnvironment.getInstance().messageWarning(alert.getMessage(),
                                                          currentFile, alert.getLine()+initialVasLine);
            }
						alerts = alerts.getTail();
					}
					throw new TomException("See next messages for details...");
				}
			} catch (ClassNotFoundException e) {
        throw new TomException(TomMessage.getString("VasClassNotFound"));
      } catch (Exception e) {
        throw new TomException(MessageFormat.format(TomMessage.getString("VasInvocationIssue"), new Object[]{e.getMessage()}));
      }
        // Simulate the inclusion of generated Tom file
      String adtFileName = new File((String)generatedADTName).toString();
      file = new File(adtFileName.substring(0, adtFileName.length()-".adt".length())+".tom");
      try {
        fileName = file.getCanonicalPath();
      } catch (IOException e) {
        throw new TomIncludeException(MessageFormat.format(TomMessage.getString("IOExceptionWithGeneratedTomFile"), new Object[]{fileName, currentFile, e.getMessage()}));
      } 
      includeFile(fileName, list);
    }
  
}

void LocalVariableConstruct(LinkedList list) throws TomException: /* in DEFAULT mode */
{}
{
  <VARIABLE> /* switch to TOM mode */
  {
    addPreviousCode(list);
    switchToDefaultMode(); /* switch to DEFAULT mode */
    list.add(`LocalVariable());
  }
}

void IncludeConstruct(LinkedList list) throws TomException, TomIncludeException : /* in DEFAULT mode */
{
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
}
{
  <INCLUDE> /* switch to TOM mode */
  {
    addPreviousCode(list);
  }
  tlCode = GoalLanguageBlock(blockList)
    {
      switchToDefaultMode(); /* switch to DEFAULT mode */
      includeFile(tlCode.getCode(), list);
    }
}

void RuleConstruct(LinkedList list) throws TomException: /* in DEFAULT mode */
{
  TomTerm lhs, rhs;
  TomTerm pattern, subject;
  TomRuleList ruleList = `concTomRule();
  LinkedList listOfLhs = new LinkedList();
  LinkedList condList = new LinkedList();
  LinkedList nameTypeInRule = new LinkedList();
  Option orgTrackRuleSet;
  TomName orgText;
}
{
  <RULE> /* switch to TOM mode */
    {
      addPreviousCode(list);
      orgTrackRuleSet = `OriginTracking(Name("Rule"),getLine(),Name( currentFile));
      text = "";
    }
  <TOM_LBRACE> 
  (
    lhs = AnnotedTerm() { listOfLhs.add(lhs); }
    [ ( <TOM_ALTERNATIVE> {text += " | ";} lhs = AnnotedTerm() { listOfLhs.add(lhs); } )+ ]
    <TOM_ARROW> {orgText = `Name(text);} rhs = PlainTerm(null)
    [ (
      <TOM_WHERE> pattern = AnnotedTerm() <TOM_COLON> <TOM_EQUAL> subject = AnnotedTerm()
      { condList.add(`MatchingCondition(pattern,subject)); }
    | <TOM_IF> pattern = AnnotedTerm() <TOM_EQUAL> <TOM_EQUAL> subject = AnnotedTerm()
      { condList.add(`EqualityCondition(pattern,subject)); }
    )+ ]
    
    {
      Option ot = `OriginTracking(Name("Pattern"),getLine(),Name( currentFile));
      LinkedList optionList = new LinkedList();
      optionList.add(ot);
      optionList.add(`OriginalText(orgText));
      for(int i=0 ; i<listOfLhs.size() ; i++) {
        TomTerm term = (TomTerm) listOfLhs.get(i);
        ruleList = (TomRuleList) ruleList.append(
          `RewriteRule(
            Term(term),
            Term(rhs),
            ast().makeInstructionList(condList),
            ast().makeOptionList(optionList)));
      }
      listOfLhs.clear();
      condList.clear();
      text="";
    }
  )*
    <TOM_RBRACE>
  {
    switchToDefaultMode(); /* switch to DEFAULT mode */
    Instruction rule = `RuleSet(ruleList, orgTrackRuleSet);
    list.add(rule);
    if(getInput().isDebugMode()) {
      debuggedStructureList.add(rule);
    }
  }
}

/*
 * Operator Declaration
 *
 * in DEFAULT mode
 */

void Operator(LinkedList list) throws TomException : /* in DEFAULT mode */
{
  Token type, name, typeArg, slotName;
  LinkedList blockList = new LinkedList();
  TomTypeList types = `concTomType();
  LinkedList options = new LinkedList();
  LinkedList slotNameList = new LinkedList();
  Map mapNameDecl = new HashMap();
  TomName astName;
  TomSymbol astSymbol;
  String stringSlotName;
  TargetLanguage tlFsym;
  Declaration attribute;
  TomType tomType;
  SlotList slotList = `concPairNameDecl();
  Option ot;
}
{
  <OPERATOR> /* switch to TOM mode */
    {
      addPreviousCode(list);
    }
  
  type = <TOM_IDENTIFIER>
  name = <TOM_IDENTIFIER>
    {   ot = `OriginTracking(Name(name.image),getLine(),Name( currentFile));
      options.add(ot);
    }
  [ <TOM_LPAREN>
      { stringSlotName = ""; }
      [ LOOKAHEAD(2)
        slotName = <TOM_IDENTIFIER> <TOM_COLON> { stringSlotName = slotName.image; }
      ]
      typeArg = <TOM_IDENTIFIER>
      {
        slotNameList.add(ast().makeName(stringSlotName)); 
        types = (TomTypeList) types.append(`TomTypeAlone(typeArg.image));
      }
      ( <TOM_COMMA>
        { stringSlotName = ""; }
        [ LOOKAHEAD(2)
          slotName = <TOM_IDENTIFIER> <TOM_COLON> { stringSlotName = slotName.image; }
        ]
        typeArg = <TOM_IDENTIFIER>
        {
          astName = ast().makeName(stringSlotName);
          if (!stringSlotName.equals("")) {
            if(slotNameList.indexOf(astName) != -1) {
              String detailedMsg = MessageFormat.format(TomMessage.getString("RepeatedSlotName"), new Object[]{stringSlotName});
              String msg = MessageFormat.format(TomMessage.getString("MainErrorMessage"), new Object[]{new Integer(ot.getLine()), "%op "+type.image, new Integer(ot.getLine()), currentFile, detailedMsg});
              throw new TomException(msg);
            }
          }
          slotNameList.add(astName);
        types = (TomTypeList) types.append(`TomTypeAlone(typeArg.image));
        }
      )*
      <TOM_RPAREN> ]

  <TOM_LBRACE>
    tlFsym = KeywordFsym()
    {
      astName   = `Name(name.image);
    }
    ( 
      attribute = KeywordMake(name.image,`TomTypeAlone(type.image),types)  { options.add(attribute); }
    | attribute = KeywordGetSlot(astName, type.image)
      {
        TomName sName = attribute.getSlotName();
        if (mapNameDecl.get(sName)==null) {
          mapNameDecl.put(sName,attribute);
        }
        else {
          environment().messageWarning(attribute.getOrgTrack().getLine(),
                                       currentFile,
                                       "%op "+type.image,
                                       ot.getLine(),
                                       TomMessage.getString("WarningTwoSameSlotDecl"), 
                                       new Object[]{sName.getString()});
        }
      }
    | attribute = KeywordIsFsym(astName, type.image)  { options.add(attribute); }
    )*
  <TOM_RBRACE>
    {
      switchToDefaultMode(); /* switch to DEFAULT mode */

      for(int i=slotNameList.size()-1; i>=0 ; i--) {
        TomName name1 = (TomName)slotNameList.get(i);
        PairNameDecl pair = null;
        Declaration emptyDeclaration = `EmptyDeclaration();
        if(name1.isEmptyName()) {
          pair = `Slot(name1,emptyDeclaration);
        } else {
          Declaration decl = (Declaration)mapNameDecl.get(name1);
          if(decl == null) {
             environment().messageWarning(ot.getLine(), 
                         currentFile,
                         "%op "+type.image, 
                         ot.getLine(), 
                         TomMessage.getString("WarningMissingSlotDecl"),
                                          new Object[]{name1.getString()});
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
           environment().messageWarning(((Declaration)mapNameDecl.get(remainingSlot)).getOrgTrack().getLine(),
                     currentFile,
                     "%op "+type.image,
                     ot.getLine(),
                                        TomMessage.getString("WarningIncompatibleSlotDecl"), new Object[]{remainingSlot.getString()});
        }
      }
      
      astSymbol = ast().makeSymbol(name.image, type.image, types, slotList, options, tlFsym);
      list.add(`SymbolDecl(astName));
      putSymbol(name.image,astSymbol);
    }
}

void OperatorList(LinkedList list) throws TomException: /* in DEFAULT mode */
{
  Token type, name, typeArg;
  LinkedList blockList = new LinkedList();
  TomTypeList types = `concTomType();
  SlotList slotList = `concPairNameDecl();
  LinkedList options = new LinkedList();
  TomSymbol astSymbol;
  TargetLanguage tlFsym;
  Declaration attribute;  
}
{
  <OPERATOR_LIST> /* switch to TOM mode */
    {
      addPreviousCode(list);
    }
  type = <TOM_IDENTIFIER> name = <TOM_IDENTIFIER>
    { options.add(`OriginTracking(Name(name.image),getLine(),Name( currentFile)));}
         <TOM_LPAREN> typeArg = <TOM_IDENTIFIER> <TOM_STAR> <TOM_RPAREN>
    {
      types = (TomTypeList) types.append(`TomTypeAlone(typeArg.image));
    }
  <TOM_LBRACE>
    tlFsym = KeywordFsym()
    ( 
      attribute = KeywordMakeEmptyList(name.image)                             { options.add(attribute); }
    | attribute = KeywordMakeAddList(name.image, type.image, typeArg.image)    { options.add(attribute); }
    | attribute = KeywordIsFsym(`Name(name.image), type.image)  { options.add(attribute); }
    )*
  <TOM_RBRACE>
    {
      switchToDefaultMode(); /* switch to DEFAULT mode */
      slotList =`manySlotList(Slot(EmptyName(), EmptyDeclaration()), slotList);
      astSymbol = ast().makeSymbol(name.image, type.image, types, slotList, options, tlFsym);
      list.add(`ListSymbolDecl(Name(name.image)));
      putSymbol(name.image,astSymbol);
    }
}

void OperatorArray(LinkedList list) throws TomException: /* in DEFAULT mode */
{
  Token type, name, typeArg;
  LinkedList blockList = new LinkedList();
  TomTypeList types = `concTomType();
  SlotList slotList = `concPairNameDecl();
  LinkedList options = new LinkedList();
  TomSymbol astSymbol;
  TargetLanguage tlFsym;
  Declaration attribute;
}
{
  <OPERATOR_ARRAY> /* switch to TOM mode */
    {
      addPreviousCode(list);
    }
  type = <TOM_IDENTIFIER> name = <TOM_IDENTIFIER>
    { options.add(`OriginTracking(Name(name.image),getLine(),Name( currentFile)));}
         <TOM_LPAREN> typeArg = <TOM_IDENTIFIER> <TOM_STAR> <TOM_RPAREN>
         { 
     types = (TomTypeList) types.append(`TomTypeAlone(typeArg.image));
         }
  <TOM_LBRACE>
    tlFsym = KeywordFsym()
    ( 
      attribute = KeywordMakeEmptyArray(name.image, type.image)              { options.add(attribute); }
    | attribute = KeywordMakeAddArray(name.image, type.image, typeArg.image) { options.add(attribute); }
    | attribute = KeywordIsFsym(`Name(name.image), type.image)  { options.add(attribute); }
    )*
  <TOM_RBRACE>
    {
      switchToDefaultMode(); /* switch to DEFAULT mode */
      slotList =`manySlotList(Slot(EmptyName(), EmptyDeclaration()), slotList);
      astSymbol = ast().makeSymbol(name.image, type.image, types, slotList, options, tlFsym);
      list.add(`ArraySymbolDecl(Name(name.image)));
      putSymbol(name.image,astSymbol);
    }
}

/*
 * Type Declaration
 *
 * in DEFAULT mode
 */

void TypeTerm(LinkedList list) throws TomException: /* in DEFAULT mode */
{
  Token type;
  LinkedList blockList = new LinkedList();
  TargetLanguage implement;
  Declaration attribute;
  TomType astType;
  Option ot;
}
{
  (
    <TYPETERM> /* switch to TOM mode */
    {
      addPreviousCode(list);
    }
  |
    <TYPE> /* switch to TOM mode */ 
    {
      addPreviousCode(list);//TODO to remove
    }
  )
  type=<TOM_IDENTIFIER>
    { ot = `OriginTracking(Name(type.image),getLine(),Name( currentFile));}
  <TOM_LBRACE>
    implement = KeywordImplement()
    ( 
      attribute = KeywordGetFunSym(type.image)  { blockList.add(attribute); }
    | attribute = KeywordGetSubterm(type.image) { blockList.add(attribute); }
    | attribute = KeywordCmpFunSym(type.image)  { blockList.add(attribute); }
    | attribute = KeywordEquals(type.image)     { blockList.add(attribute); }
    )*
  <TOM_RBRACE>
    {
      switchToDefaultMode(); /* switch to DEFAULT mode */
      astType = `Type(ASTTomType(type.image),TLType(implement));
      putType(type.image,astType);
      list.add(`TypeTermDecl(Name(type.image), ast().makeList(blockList), ot));
    }
}


void TypeList(LinkedList list) throws TomException: /* in DEFAULT mode */
{
  Token type;
  LinkedList blockList = new LinkedList();
  TargetLanguage implement;
  Declaration attribute;
  TomType astType;
  Option ot;
}
{
  <TYPELIST> /* switch to TOM mode */
    {
      addPreviousCode(list);
    }
  
  type=<TOM_IDENTIFIER>
    { ot = `OriginTracking(Name(type.image),getLine(),Name( currentFile)); }
  <TOM_LBRACE>
    implement = KeywordImplement()
    ( 
      attribute = KeywordGetFunSym(type.image)  { blockList.add(attribute); }
    | attribute = KeywordGetSubterm(type.image) { blockList.add(attribute); }
    | attribute = KeywordCmpFunSym(type.image)  { blockList.add(attribute); }
    | attribute = KeywordEquals(type.image)     { blockList.add(attribute); }
    | attribute = KeywordGetHead(type.image)    { blockList.add(attribute); }
    | attribute = KeywordGetTail(type.image)    { blockList.add(attribute); }
    | attribute = KeywordIsEmpty(type.image)    { blockList.add(attribute); }
    )*
  <TOM_RBRACE>
    {
      switchToDefaultMode(); /* switch to DEFAULT mode */
      astType = `Type(ASTTomType(type.image),TLType(implement));
      putType(type.image,astType);
      list.add(`TypeListDecl(Name(type.image), ast().makeList(blockList), ot));
    }
}

void TypeArray(LinkedList list) throws TomException: /* in DEFAULT mode */
{
  Token type;
  LinkedList blockList = new LinkedList();
  TargetLanguage implement;
  Declaration attribute;
  TomType astType;
  Option ot;
}
{
  <TYPEARRAY> /* switch to TOM mode */
    {
      addPreviousCode(list);
    }
  
  type=<TOM_IDENTIFIER>
    { ot = `OriginTracking(Name(type.image),getLine(),Name( currentFile)); }
  <TOM_LBRACE>
    implement = KeywordImplement()
    ( 
      attribute = KeywordGetFunSym(type.image)  { blockList.add(attribute); }
    | attribute = KeywordGetSubterm(type.image) { blockList.add(attribute); }
    | attribute = KeywordCmpFunSym(type.image)  { blockList.add(attribute); }
    | attribute = KeywordEquals(type.image)     { blockList.add(attribute); }
    | attribute = KeywordGetElement(type.image) { blockList.add(attribute); }
    | attribute = KeywordGetSize(type.image)    { blockList.add(attribute); }
    )*
  <TOM_RBRACE>
    {
      switchToDefaultMode(); /* switch to DEFAULT mode */
      astType = `Type(ASTTomType(type.image),TLType(implement));
      putType(type.image,astType);
      list.add(`TypeArrayDecl(Name(type.image), ast().makeList(blockList), ot));
    }
}

/*
 * in TOM mode
 */

TargetLanguage GoalLanguageBlock(LinkedList blockList) throws TomException:
{
}
{
  <TOM_LBRACE> /* we are in TOM mode */
    {
      switchToDefaultMode(); /* switch to DEFAULT mode */
    }
  BlockList(blockList)
    <RBRACE> /* we are in DEFAULT mode */
    {
      switchToTomMode(); /* switch to TOM mode */
      TargetLanguage tlb = makeTL(savePosAndExtract());
      return tlb;
    }
}

//------------------------------------------------------------

TargetLanguage KeywordImplement() throws TomException:
{
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
}
{
  <TOM_IMPLEMENT>
  tlCode = GoalLanguageBlock(blockList)
   {
     return ast().reworkTLCode(tlCode, getInput().isPretty());
   }
}

Declaration KeywordGetFunSym(String typeString) throws TomException:
{
  Token name;
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_GET_FUN_SYM>
    { ot = `OriginTracking(Name("get_fun_sym"), getLine(),Name( currentFile));}
  <TOM_LPAREN> name = <TOM_IDENTIFIER> <TOM_RPAREN>
  tlCode = GoalLanguageBlock(blockList)
   {
     Option info = `OriginTracking(Name(name.image),getLine(),Name( currentFile));
     OptionList option = ast().makeOption(info);
     return `GetFunctionSymbolDecl(
                           ast().makeVariable(option,name.image,typeString),
                           ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

Declaration KeywordGetSubterm(String typeString) throws TomException:
{
  Token name1, name2;
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_GET_SUBTERM>
     { ot = `OriginTracking(Name("get_subterm"), getLine(),Name( currentFile));}
  <TOM_LPAREN> name1 = <TOM_IDENTIFIER> <TOM_COMMA> name2 = <TOM_IDENTIFIER> <TOM_RPAREN>
  tlCode = GoalLanguageBlock(blockList)
   {
     Option info1 = `OriginTracking(Name(name1.image),getLine(),Name( currentFile));
     Option info2 = `OriginTracking(Name(name2.image),getLine(),Name( currentFile));
     OptionList option1 = ast().makeOption(info1);
     OptionList option2 = ast().makeOption(info2);
     return `GetSubtermDecl(
                           ast().makeVariable(option1,name1.image,typeString),
                           ast().makeVariable(option2,name2.image,"int"),
                           ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

Declaration KeywordCmpFunSym(String typeString) throws TomException:
{
  Token name1, name2;
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_CMP_FUN_SYM>
    { ot = `OriginTracking(Name("cmp_fun_sym"), getLine(),Name( currentFile));}
  <TOM_LPAREN> name1 = <TOM_IDENTIFIER> <TOM_COMMA> name2 = <TOM_IDENTIFIER> <TOM_RPAREN>
  tlCode = GoalLanguageBlock(blockList)
   {
     Option info1 = `OriginTracking(Name(name1.image),getLine(),Name( currentFile));
     Option info2 = `OriginTracking(Name(name2.image),getLine(),Name( currentFile));
     OptionList option1 = ast().makeOption(info1);
     OptionList option2 = ast().makeOption(info2);
     return `CompareFunctionSymbolDecl(
                           ast().makeVariable(option1,name1.image,typeString),
                           ast().makeVariable(option2,name2.image,typeString),
                           ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

Declaration KeywordEquals(String typeString) throws TomException:
{
  Token name1, name2;
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_EQUALS>
    { ot = `OriginTracking(Name("equals"), getLine(),Name( currentFile));}
  <TOM_LPAREN> name1 = <TOM_IDENTIFIER> <TOM_COMMA> name2 = <TOM_IDENTIFIER> <TOM_RPAREN>
  tlCode = GoalLanguageBlock(blockList)
   {
     Option info1 = `OriginTracking(Name(name1.image),getLine(),Name( currentFile));
     Option info2 = `OriginTracking(Name(name2.image),getLine(),Name( currentFile));
     OptionList option1 = ast().makeOption(info1);
     OptionList option2 = ast().makeOption(info2);
     return `TermsEqualDecl(
                           ast().makeVariable(option1,name1.image,typeString),
                           ast().makeVariable(option2,name2.image,typeString),
                           ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

Declaration KeywordGetHead(String typeString) throws TomException:
{
  Token name;
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_GET_HEAD>
    { ot = `OriginTracking(Name("get_head"), getLine(),Name( currentFile));}
  <TOM_LPAREN> name = <TOM_IDENTIFIER> <TOM_RPAREN>
  tlCode = GoalLanguageBlock(blockList)
   {
     Option info = `OriginTracking(Name(name.image),getLine(),Name( currentFile));
     OptionList option = ast().makeOption(info);
     return `GetHeadDecl(symbolTable().getUniversalType(),
                         ast().makeVariable(option,name.image,typeString),
                         ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

Declaration KeywordGetTail(String typeString) throws TomException:
{
  Token name;
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_GET_TAIL>
    { ot = `OriginTracking(Name("get_tail"), getLine(),Name( currentFile));}
  <TOM_LPAREN> name = <TOM_IDENTIFIER> <TOM_RPAREN>
  tlCode = GoalLanguageBlock(blockList)
   {
     Option info = `OriginTracking(Name(name.image),getLine(),Name( currentFile));
     OptionList option = ast().makeOption(info);
     return `GetTailDecl(ast().makeVariable(option,name.image,typeString),
                         ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

Declaration KeywordIsEmpty(String typeString) throws TomException:
{
  Token name;
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_IS_EMPTY>
    { ot = `OriginTracking(Name("is_empty"), getLine(),Name( currentFile));}
  <TOM_LPAREN> name = <TOM_IDENTIFIER> <TOM_RPAREN>
  tlCode = GoalLanguageBlock(blockList)
   {
     Option info = `OriginTracking(Name(name.image),getLine(),Name( currentFile));
     OptionList option = ast().makeOption(info);
     return `IsEmptyDecl(ast().makeVariable(option,name.image,typeString),
                         ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

Declaration KeywordGetElement(String typeString) throws TomException:
{
  Token name1, name2;
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_GET_ELEMENT>
    { ot = `OriginTracking(Name("get_element"), getLine(),Name( currentFile));}
  <TOM_LPAREN> name1 = <TOM_IDENTIFIER> <TOM_COMMA> name2 = <TOM_IDENTIFIER> <TOM_RPAREN>
  tlCode = GoalLanguageBlock(blockList)
   {
     Option info1 = `OriginTracking(Name(name1.image),getLine(),Name( currentFile));
     Option info2 = `OriginTracking(Name(name2.image),getLine(),Name( currentFile));
     OptionList option1 = ast().makeOption(info1);
     OptionList option2 = ast().makeOption(info2);
     return `GetElementDecl(ast().makeVariable(option1,name1.image,typeString),
                            ast().makeVariable(option2,name2.image,"int"),
                            ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

Declaration KeywordGetSize(String typeString) throws TomException:
{
  Token name;
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_GET_SIZE>
    { ot = `OriginTracking(Name("get_size"), getLine(),Name( currentFile));}
  <TOM_LPAREN> name = <TOM_IDENTIFIER> <TOM_RPAREN>
  tlCode = GoalLanguageBlock(blockList)
   {
     Option info = `OriginTracking(Name(name.image),getLine(),Name( currentFile));
     OptionList option = ast().makeOption(info);
     return `GetSizeDecl(ast().makeVariable(option,name.image,typeString),
                         ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

TargetLanguage KeywordFsym() throws TomException:
{
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
}
{
  <TOM_FSYM>
  tlCode = GoalLanguageBlock(blockList)
   {
     return ast().reworkTLCode(tlCode, getInput().isPretty());
   }
}

Declaration KeywordIsFsym(TomName astName, String typeString) throws TomException:
{
  Token name;
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_IS_FSYM>
    { ot = `OriginTracking(Name("is_fsym"), getLine(),Name( currentFile));}
  <TOM_LPAREN> name = <TOM_IDENTIFIER> <TOM_RPAREN>
  tlCode = GoalLanguageBlock(blockList)
   {
     Option info = `OriginTracking(Name(name.image),getLine(),Name( currentFile));
     OptionList option = ast().makeOption(info);
     return `IsFsymDecl(astName,
                        ast().makeVariable(option,name.image,typeString),
                        ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

Declaration KeywordGetSlot(TomName astName, String typeString) throws TomException:
{
  Token name, slotName;
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_GET_SLOT>
    { ot = `OriginTracking(Name("get_slot"), getLine(),Name( currentFile));}
  <TOM_LPAREN> slotName = <TOM_IDENTIFIER>
     <TOM_COMMA> name = <TOM_IDENTIFIER> <TOM_RPAREN>
  tlCode = GoalLanguageBlock(blockList)
   {
       Option info = `OriginTracking(Name(name.image),getLine(),Name( currentFile));
       OptionList option = ast().makeOption(info);
       return `GetSlotDecl(astName,
                           Name(slotName.image),
                           ast().makeVariable(option,name.image,typeString),
                           ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

Declaration KeywordMake(String opname, TomType returnType, TomTypeList types) throws TomException:
{
  Token typeArg;
  Token nameArg;
  LinkedList args = new LinkedList();
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  int index = 0;
  TomType type;
  Option ot;
  int nbTypes = types.getLength();
}
{
  <TOM_MAKE>
     { ot = `OriginTracking(Name("make"), getLine(),Name( currentFile));}
     [ ( LOOKAHEAD(2) 
        <TOM_LPAREN> <TOM_RPAREN>
     |
        <TOM_LPAREN>
         nameArg = <TOM_IDENTIFIER>
       {
         if( !(nbTypes > 0) ) {
           type = `EmptyType();
         } else {
           type = (TomType)types.elementAt(index++);
         }
         Option info1 = `OriginTracking(Name(nameArg.image),getLine(),Name( currentFile));
         OptionList option1 = ast().makeOption(info1);
         args.add(ast().makeVariable(option1,`Name(nameArg.image), type));
       }
     ( <TOM_COMMA> nameArg = <TOM_IDENTIFIER>
        {
          if( index >= nbTypes ) {
            type = `EmptyType();
          } else {
            type = (TomType)types.elementAt(index++);
          }
          Option info2 = `OriginTracking(Name(nameArg.image),getLine(),Name( currentFile));
          OptionList option2 = ast().makeOption(info2);
          args.add(ast().makeVariable(option2,`Name(nameArg.image), type));
        }
      )*
      <TOM_RPAREN> ) ]

  tlCode = GoalLanguageBlock(blockList)
   {
     return `MakeDecl(Name(opname),returnType,ast().makeList(args),ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

Declaration KeywordMakeEmptyList(String name) throws TomException:
{
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_MAKE_EMPTY>
    { ot = `OriginTracking(Name("make_empty"), getLine(),Name( currentFile));}
  [<TOM_LPAREN> <TOM_RPAREN>]
  tlCode = GoalLanguageBlock(blockList)
   {
     return `MakeEmptyList(Name(name),
                           ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

Declaration KeywordMakeAddList(String name, String listType, String elementType) throws TomException:
{
  Token listName, elementName;
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_MAKE_INSERT>
    { ot = `OriginTracking(Name("make_add"), getLine(),Name( currentFile));}
  <TOM_LPAREN> elementName=<TOM_IDENTIFIER> <TOM_COMMA>
                                    listName=<TOM_IDENTIFIER> <TOM_RPAREN>
  tlCode = GoalLanguageBlock(blockList)
   {
     Option listInfo = `OriginTracking(Name(listName.image),getLine(),Name( currentFile));
     Option elementInfo = `OriginTracking(Name(elementName.image),getLine(),Name( currentFile));
     OptionList listOption = ast().makeOption(listInfo);
     OptionList elementOption = ast().makeOption(elementInfo);
     return `MakeAddList(Name(name),
                         ast().makeVariable(elementOption,elementName.image,elementType),
                         ast().makeVariable(listOption,listName.image,listType),
                         ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

Declaration KeywordMakeEmptyArray(String name, String listType) throws TomException:
{
  Token listName;
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_MAKE_EMPTY>
    { ot = `OriginTracking(Name("make_empty"), getLine(),Name( currentFile));}
  <TOM_LPAREN> listName=<TOM_IDENTIFIER> <TOM_RPAREN>
  tlCode = GoalLanguageBlock(blockList)
   {
     Option listInfo = `OriginTracking(Name(listName.image),getLine(),Name( currentFile));
     OptionList listOption = ast().makeOption(listInfo);
     return `MakeEmptyArray(Name(name),
                            ast().makeVariable(listOption,listName.image,listType),
                            ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}

Declaration KeywordMakeAddArray(String name, String listType, String elementType) throws TomException:
{
  Token listName, elementName, positionName;
  LinkedList blockList = new LinkedList();
  TargetLanguage tlCode;
  Option ot;
}
{
  <TOM_MAKE_APPEND>
    { ot = `OriginTracking(Name("make_append"), getLine(),Name( currentFile));}
  <TOM_LPAREN> elementName=<TOM_IDENTIFIER>    <TOM_COMMA>
                                    listName=<TOM_IDENTIFIER>
                 <TOM_RPAREN>
  tlCode = GoalLanguageBlock(blockList)
   {
     Option listInfo = `OriginTracking(Name(listName.image),getLine(),Name( currentFile));
     Option elementInfo = `OriginTracking(Name(elementName.image),getLine(),Name( currentFile));
     OptionList listOption = ast().makeOption(listInfo);
     OptionList elementOption = ast().makeOption(elementInfo);
     return `MakeAddArray(Name(name),
                          ast().makeVariable(elementOption,elementName.image,elementType),
                          ast().makeVariable(listOption,listName.image,listType),
                          ast().reworkTLCode(tlCode, getInput().isPretty()), ot);
   }
}


/************************************************************
 * BACKQUOTE MODE
 ************************************************************/

/* BQ SKIP */
<BQ>
SKIP :
{
  < "<!--" ( ~["-"] | ( "-" ~["-"] ) )* "-->" >
| < "<?"   (~[">"])* ">"                      >
}

/* BQ COMMENTS */
<BQ>
MORE :
{
  <"/**" ~["/"]> { input_stream.backup(1); } : BQ_IN_FORMAL_COMMENT
|
  "/*" : BQ_IN_MULTI_LINE_COMMENT
}

<BQ_IN_SINGLE_LINE_COMMENT>
SPECIAL_TOKEN :
{
  <BQ_SINGLE_LINE_COMMENT: "\n" | "\r" | "\r\n" > : BQ
}

<BQ_IN_FORMAL_COMMENT>
SPECIAL_TOKEN :
{
  <BQ_FORMAL_COMMENT: "*/" > : BQ
}

<BQ_IN_MULTI_LINE_COMMENT>
SPECIAL_TOKEN :
{
  <BQ_MULTI_LINE_COMMENT: "*/" > : BQ
}

<BQ_IN_SINGLE_LINE_COMMENT,BQ_IN_FORMAL_COMMENT,BQ_IN_MULTI_LINE_COMMENT>
MORE :
{
  < ~[] >
}

/* BQ TOKENS */
<BQ>
TOKEN :
{
  < BQ_LPAREN:      "(" >
| < BQ_RPAREN:      ")" >
| < BQ_STAR:        "*" >
| < BQ_COMMA:       "," >
| < BQ_INTEGER: (<BQ_MINUS_SIGN>)? <BQ_DIGIT> (<BQ_DIGIT>)* >
| < BQ_IDENTIFIER: <BQ_LETTER> (<BQ_LETTER>|<BQ_DIGIT>|<BQ_UNDERSCORE>| (<BQ_MINUS_SIGN><BQ_LETTER>))* >
| < #BQ_MINUS_SIGN: ["-"] >
| < #BQ_UNDERSCORE: ["_"] >
| < #BQ_LETTER: [ "a"-"z", "A"-"Z" ]>
| < #BQ_DIGIT: [ "0"-"9" ] >
| < BQ_STRING:
      "\""
      (   (~["\"","\\","\n","\r"])
        | ("\\"
            ( ["n","t","b","r","f","\\","'","\""]
            | ["0"-"7"] ( ["0"-"7"] )?
            | ["0"-"3"] ["0"-"7"] ["0"-"7"]
            )
          )
      )*
      "\""
  >
| < BQ_XML_TEXT: "#TEXT">
| < BQ_XML_COMMENT: "#COMMENT">
| < BQ_XML_PROC: "#PROCESSING-INSTRUCTION">
| < BQ_OTHER: ~[] >
}

  /*
   * The goal of this function is to parse a backquoted term
   * There are 4 cases:
   *   `(...)
   *   `identifier(...)
   *   `identifier
   *   `identifier*
   * 
   */
void BackQuoteTerm(LinkedList list) throws TomException: /* in DEFAULT mode */
{
  TomTerm term;
  Option ot;
  Token tk, nextTk;
  int parenLevel = 0, lastTokenLine = 0, firstTokenLine = 0;
  boolean backQuoteMode = false;
  LinkedList tokenList = new LinkedList();
  HashMap tokenToDecLineMap = new HashMap();
}
{
  <BACKQUOTE_TERM> /* switch to BQ mode */
  {
    addPreviousCode(list);
    firstTokenLine = getLine();
    ot = `OriginTracking(Name("Backquote"),getLine(),Name( currentFile));
	}
  (
   tk=<BQ_LPAREN> { 
     tokenList.add(tk); 
     parenLevel++; 
     backQuoteMode = true;
   }
   | tk=<BQ_IDENTIFIER> { 
     tokenList.add(tk);
     tokenToDecLineMap.put(tk, new Integer(getLine()));
     /*
      * We switch to DEFAULT mode to tokenize what follows the identifiers in 
      * a correct mode, in case it is not a "("
      */
     switchToDefaultMode(); /* switch to DEFAULT mode */
   }
   )
  {
    try {
      nextTk = getToken(1);
      //System.out.println("getToken(1).kind = " + nextTk.kind + " --> '" + nextTk.image + "'");
      if(backQuoteMode || nextTk.kind == BQ_LPAREN || nextTk.image.equals("(")) {
        if(nextTk.image.equals("(")) {
          /*
           * it is a "(" so we can switch back to BQ mode
           */
          token_source.SwitchTo(BQ);
        }
        /*
         * cases:
         *   `(...)
         *   `identifier(...)
         *
         * tk and nextTk have been tokenized in BQ mode
         *
         * we have to analyse the image and not only the kind
         * (also in TomBackQuoteParser.t)
         */
        backQuoteMode = true;
        while(backQuoteMode) {
          tk = getNextToken();
          //System.out.println("tk.kind = " + tk.kind + " --> '" + tk.image + "'");
          tokenList.add(tk);
          lastTokenLine = getLine();
          if(tk.kind == BQ_LPAREN || tk.image.equals("(")) { 
            parenLevel++;
          } else if(tk.kind == BQ_RPAREN) {
            parenLevel--;
            if(parenLevel==0) {
              backQuoteMode = false;
            }
          } else if (tk.kind == BQ_IDENTIFIER) {
            tokenToDecLineMap.put(tk, new Integer(lastTokenLine));
          }
        }
        term = tomBackQuoteParser.buildBackQuoteTerm(tokenList, tokenToDecLineMap, currentFile);
        //System.out.println("term = " + term);
        if(term.isComposite() && term.getArgs().isSingle()) {
          TomTerm backQuoteTerm = term.getArgs().getHead(); 
          if(backQuoteTerm.getAstName().getString().equals("xml")) {
            TomList args = backQuoteTerm.getArgs();
            term = `DoubleBackQuote(args);
          } 
        } 
        list.add(term);
        switchToDefaultMode(); /* switch to DEFAULT mode */
      } else {
        /*
         * we are in DEFAULT mode
         * nextTk has been tokenized in Default mode
         */
        if(nextTk.image.equals("*")) {
          /*
           * the backquote-term is a single variable-star
           */
          term = tomBackQuoteParser.buildVariableStar(tk.image,lastTokenLine,currentFile);
          list.add(term);
          /*
           * here we consume the read in advance "*" to remove it from the "previous code"
           */
          tk = getNextToken();
          /*
           * we switch to DEFAULT mode to re-assign the oldPos variable
           */
          switchToDefaultMode(); /* switch to DEFAULT mode */
        } else {
          /*
           * the backquote-term is a single variable
           */
          term = tomBackQuoteParser.buildBackQuoteAppl(tk.image,lastTokenLine,currentFile);
          list.add(term);
					//tk = getNextToken();
					//list.add(`ITL(tk.image));
        }
      }
    } catch (TokenMgrError error) {
      throw new TomException(MessageFormat.format(TomMessage.getString("InvalidBackQuoteTerm"), new Object[]{new Integer(firstTokenLine)}));
    }  
  }
}
