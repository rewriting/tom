header{/*
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
import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.*;
import java.io.*;

import tom.engine.Tom;
import tom.engine.TomStreamManager;
import tom.engine.TomMessage;
import tom.engine.exception.*;
import tom.engine.tools.SymbolTable;

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

import tom.engine.tools.ASTFactory;
import aterm.*;
import antlr.TokenStreamSelector;
import tom.platform.OptionManager;
import tom.platform.PluginPlatform;
import tom.platform.PlatformLogRecord;
}
class HostParser extends Parser;

options{
  // antlr does not catch exceptions automaticaly
  defaultErrorHandler = false;
}

{
  //--------------------------
    %include{ ../adt/tomsignature/TomSignature.tom }
  //--------------------------

  // the lexer selector
  private TokenStreamSelector selector = null;

  // the file to be parsed
  private String currentFile = null;

  private HashSet includedFileSet = null;
  private HashSet alreadyParsedFileSet = null;

  // the parser for tom constructs
  TomParser tomparser;

  // the lexer for target language
  HostLexer targetlexer = null;

  BackQuoteParser bqparser;

  OptionManager optionManager;

  TomStreamManager streamManager;

  // locations of target language blocks
  private int currentLine = 1;
  private int currentColumn = 1;

  private boolean skipComment = false;

  public HostParser(TokenStreamSelector selector, String currentFile,
                    HashSet includedFiles, HashSet alreadyParsedFiles,
                    OptionManager optionManager, TomStreamManager streamManager){
    this(selector);
    this.selector = selector;
    this.currentFile = currentFile;
    this.optionManager = optionManager;
    this.streamManager = streamManager;
    this.targetlexer = (HostLexer) selector.getStream("targetlexer");
    targetlexer.setParser(this);
    this.includedFileSet = new HashSet(includedFiles);
    this.alreadyParsedFileSet = alreadyParsedFiles;

    testIncludedFile(currentFile, includedFileSet);
    // then create the Tom mode parser
    tomparser = new TomParser(getInputState(),this, optionManager);
    bqparser = tomparser.bqparser;
  }

  private void setSkipComment() {
    skipComment = true;
	}
  public boolean isSkipComment() {
    return skipComment;
	}

  private OptionManager getOptionManager() {
    return optionManager;
  }

  private TomStreamManager getStreamManager() {
    return streamManager;
  }

  public TokenStreamSelector getSelector(){
    return selector;
  }

  public String getCurrentFile(){
    return currentFile;
  }

  public SymbolTable getSymbolTable() {
    return getStreamManager().getSymbolTable();
  }

  public void updatePosition(){
    updatePosition(getLine(),getColumn());
  }

  public void updatePosition(int i, int j){
    currentLine = i;
    currentColumn = j;
  }

  public int currentLine(){
    return currentLine;
  }

  public int currentColumn(){
    return currentColumn;
  }

  // remove braces of a code block
  private String cleanCode(String code){
    return code.substring(code.indexOf('{')+1,code.lastIndexOf('}'));
  }

  // remove the last right-brace of a code block
  private String removeLastBrace(String code){
    return code.substring(0,code.lastIndexOf("}"));
  }

  // returns the current goal language code
  private String getCode(){
    String result = targetlexer.target.toString();
    targetlexer.clearTarget();
    return result;
  }

  // add a token in the target code buffer
  public void addTargetCode(Token t){
    targetlexer.target.append(t.getText());
  }

  private String pureCode(String code){
    return code.replace('\t',' ');
  }

  private boolean isCorrect(String code){
    return (! code.equals("") && ! code.matches("\\s*"));
  }

  // returns the current line number
  public int getLine(){
    return targetlexer.getLine();
  }

  // returns the current column number
  public int getColumn(){
    return targetlexer.getColumn();
  }

  private void includeFile(String fileName, LinkedList list)
    throws TomException, TomIncludeException {
    TomTerm astTom;
    InputStream input;
    byte inputBuffer[];
    //  TomParser tomParser;
    HostParser parser = null;
    File file;
    String fileCanonicalName = "";
    fileName = fileName.trim();
    fileName = fileName.replace('/',File.separatorChar);
    fileName = fileName.replace('\\',File.separatorChar);
    if(fileName.equals("")) {
      throw new TomIncludeException(TomMessage.missingIncludedFile,new Object[]{currentFile, new Integer(getLine())});
    }

    file = new File(fileName);
    if(file.isAbsolute()) {
      try {
        file = file.getCanonicalFile();
      } catch (IOException e) {
        System.out.println("IO Exception when computing included file");
        e.printStackTrace();
      }

      if(!file.exists()) {
        file = null;
      }
    } else {
      // StreamManager shall find it
      file = getStreamManager().findFile(new File(currentFile).getParentFile(),fileName);
    }

    if(file == null) {
      throw new TomIncludeException(TomMessage.includedFileNotFound,new Object[]{fileName, currentFile, new Integer(getLine()), currentFile});
    }
    try {
      fileCanonicalName = file.getCanonicalPath();
      //if(testIncludedFile(fileCanonicalName, includedFileSet)) {
        //throw new TomIncludeException(TomMessage.includedFileCycle,new Object[]{fileName, new Integer(getLine()), currentFile});
      //}

      // if trying to include a file twice, or if in a cycle: discard
      if(testIncludedFile(fileCanonicalName, alreadyParsedFileSet) ||
	  testIncludedFile(fileCanonicalName, includedFileSet)) {
        if(!getStreamManager().isSilentDiscardImport(fileName)) {
          getLogger().log(new PlatformLogRecord(Level.INFO,
                TomMessage.includedFileAlreadyParsed,
                currentFile, fileName, getLine()));
        }
        return;
      }
      Reader fileReader = new BufferedReader(new FileReader(fileCanonicalName));
      parser = TomParserPlugin.newParser(fileReader,fileCanonicalName,
                                         includedFileSet,alreadyParsedFileSet,
                                         getOptionManager(), getStreamManager());
      parser.setSkipComment();
      astTom = parser.input();
      astTom = `TomInclude(astTom.getTomList());
      list.add(astTom);
    } catch (Exception e) {
      if(e instanceof TomIncludeException) {
        throw (TomIncludeException)e;
      }
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      throw new TomException(TomMessage.errorWhileIncludingFile,
          new Object[]{e.getClass(),
            fileName,
            currentFile,
            new Integer(getLine()),
            sw.toString()
          });
    }
  }

  private boolean testIncludedFile(String fileName, HashSet fileSet) {
    // !(true) if the set did not already contain the specified element.
    return !fileSet.add(fileName);
  }

  /*
   * this function receives a string that comes from %[ ... ]%
   * @@ corresponds to the char '@', so they a encoded into ]% (which cannot
   * appear in the string)
   * then, the string is split around the delimiter @
   * alternatively, each string correspond either to a metaString, or a string
   * to parse the @@ encoded by ]% is put back as a single '@' in the metaString
   */
  public String tomSplitter(String subject, LinkedList list) {

    String metaChar = "]%";
    String escapeChar = "@";

    //System.out.println("initial subject: '" + subject + "'");
    subject = subject.replaceAll(escapeChar+escapeChar,metaChar);
    //System.out.println("subject: '" + subject + "'");
    String split[] = subject.split(escapeChar);
    boolean metaMode = true;
    String res = "";
    for(int i=0 ; i<split.length ; i++) {
      if(metaMode) {
        // put back escapeChar instead of metaChar
        String code = metaEncodeCode(split[i].replaceAll(metaChar,escapeChar));
        metaMode = false;
        //System.out.println("metaString: '" + code + "'");
        list.add(`ITL(code));
      } else {
        String code = "+"+split[i]+"+";
        metaMode = true;
        //System.out.println("prg to parse: '" + code + "'");
        try {
          Reader codeReader = new BufferedReader(new StringReader(code));
          HostParser parser = TomParserPlugin.newParser(codeReader,getCurrentFile(),
              getOptionManager(), getStreamManager());
          TomTerm astTom = parser.input();
          list.add(astTom); 
        } catch (IOException e) {
          throw new TomRuntimeException("IOException catched in tomSplitter");
        } catch (Exception e) {
          throw new TomRuntimeException("Exception catched in tomSplitter");
        }
      }
    }
    if(subject.endsWith(escapeChar)) {
      // add an empty string when %[...@...@]%
      list.add(`ITL("\"\""));
    }
    return res;
  }

  private static String metaEncodeCode(String code) {
		/*
			 System.out.println("before: '" + code + "'");
			 for(int i=0 ; i<code.length() ; i++) {
			 System.out.print((int)code.charAt(i));
			 System.out.print(" ");
			 }
			 System.out.println();
		 */
		char bs = '\\';
		StringBuffer sb = new StringBuffer((int)1.5*code.length());
		for(int i=0 ; i<code.length() ; i++) {
			char c = code.charAt(i);
			switch(c) {
				case '\n': 
					sb.append(bs);
					sb.append('n');
					break;
				case '\r': 
					sb.append(bs);
					sb.append('r');
					break;
				case '\t': 
					sb.append(bs);
					sb.append('t');
					break;
				case '\"': 
				case '\\': 
					sb.append(bs);
					sb.append(c);
					break;
				default:
					sb.append(c);
			}
		}
    //System.out.println("sb = '" + sb + "'");
		sb.insert(0,'\"');
		sb.append('\"');
		return sb.toString();

/*
    code = code.replaceAll("\\\"","\\\\\"");
    code = code.replaceAll("\\\\n","\\\\\\\\n");
    code = code.replaceAll("\\\\t","\\\\\\\\t");
    code = code.replaceAll("\\\\r","\\\\\\\\r");

    code = code.replaceAll("\n","\\\\n");
    code = code.replaceAll("\r","\\\\r");
    code = code.replaceAll("\t","\\\\t");
    code = code.replaceAll("\"","\\\"");
    
    System.out.println("after: '" + code + "'");

    return "\"" + code + "\"";
*/


  }

  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}

// The grammar starts here

input returns [TomTerm result] throws TomException
{
    result = null;
    LinkedList list = new LinkedList();
}
  :
  blockList[list] t:EOF
        {
          // This TL is last block: do no need to specify line and column
          list.add(`TargetLanguageToTomTerm(
                                            TL(
                                               getCode(),
                                               TextPosition(currentLine(),currentColumn()),
                                               TextPosition(t.getLine(), t.getColumn()))));
            String comment =
            "Generated by TOM (version " + Tom.VERSION + "): Do not edit this file";
            list.addFirst(`TargetLanguageToTomTerm(Comment(comment)));
            result = `Tom(ASTFactory.makeList(list));
        }
    ;

blockList [LinkedList list] throws TomException
    :
        (
            // either a tom construct or everything else
            matchConstruct[list]
        |   strategyConstruct[list]
        |   gomsignature[list]
        |   backquoteTerm[list]
        |   operator[list]
        |   operatorList[list]
        |   operatorArray[list]
        |   includeConstruct[list]
        |   typeTerm[list]
        |   code[list]
        |   STRING
        |   LBRACE blockList[list] RBRACE
        )*
    ;

// the %strategy construct
strategyConstruct [LinkedList list] throws TomException
{
    TargetLanguage code = null;
}
    :
        t:STRATEGY // we switch the lexers here : we are in Tom mode
        {
            // add the target code preceeding the construct
            String textCode = getCode();

            if(isCorrect(textCode)) {
                code = `TL(
                    textCode,
                    TextPosition(currentLine,currentColumn),
                    TextPosition(t.getLine(),t.getColumn())
                );
                list.add(code);
            }

            Option ot = `OriginTracking( Name("Strategy"), t.getLine(), currentFile);

            // call the tomparser for the construct
            Declaration strategy = tomparser.strategyConstruct(ot);
            list.add(strategy);
        }
    ;

matchConstruct [LinkedList list] throws TomException
{
    TargetLanguage code = null;
}
  :
        t:MATCH
        {
            String textCode = getCode();
            if(isCorrect(textCode)) {
                code = `TL(
                    textCode,
                    TextPosition(currentLine,currentColumn),
                    TextPosition(t.getLine(),t.getColumn())
                );
                list.add(code);
            }

            Option ot = `OriginTracking(Name("Match"),t.getLine(), currentFile);

            Instruction match = tomparser.matchConstruct(ot);
            list.add(match);
        }
    ;

gomsignature [LinkedList list] throws TomException
{
  int initialGomLine;
  TargetLanguage gomTL = null, code = null;
  LinkedList blockList = new LinkedList();
  String gomCode = null;
}
:
  t:GOM
  {
    initialGomLine = t.getLine();

    String textCode = getCode();
    if(isCorrect(textCode)) {
      code = `TL(textCode,
                 TextPosition(currentLine,currentColumn),
                 TextPosition(t.getLine(),t.getColumn()));
      list.add(code);
    }
  }
  {
    tom.gom.parser.BlockParser blockparser = 
      tom.gom.parser.BlockParser.makeBlockParser(targetlexer.getInputState());
    gomCode = cleanCode(blockparser.block().trim());
    String destDir = getStreamManager().getDestDir().getPath();

    File config_xml = null;
    ArrayList parameters = new ArrayList();
    try {
      String tom_home = System.getProperty("tom.home");
      if(tom_home != null) {
        config_xml = new File(tom_home,"Gom.xml");
      } else {
        // for the eclipse plugin for example
        String tom_xml_filename =
          ((String)getOptionManager().getOptionValue("X"));
        config_xml =
          new File(new File(tom_xml_filename).getParentFile(),"Gom.xml");
        // pass all the received parameters to gom in the case that it will call tom
        java.util.List<File> imp = getStreamManager().getUserImportList();
        for(File f:imp){
          parameters.add("--import");
          parameters.add(f.getCanonicalPath());
        }
      }
      config_xml = config_xml.getCanonicalFile();
    } catch (IOException e) {
      getLogger().log(
          Level.FINER,
          "Failed to get canonical path for "+config_xml.getPath());
    }

    String packageName =
      getStreamManager().getPackagePath().replace(File.separatorChar, '.');
    String inputFileNameWithoutExtension =
      getStreamManager().getRawFileName().toLowerCase();
    String subPackageName = "";
    if(packageName.equals("")) {
      subPackageName = inputFileNameWithoutExtension;
    } else {
      subPackageName = packageName + "." + inputFileNameWithoutExtension;
    }    
    parameters.add("-X");
    parameters.add(config_xml.getPath());
    parameters.add("--destdir");
    parameters.add(destDir);
    parameters.add("--package");
    parameters.add(subPackageName);
    if(getOptionManager().getOptionValue("wall")==Boolean.TRUE) {
      parameters.add("--wall");
    }
    if(getOptionManager().getOptionValue("intermediate")==Boolean.TRUE) {
      parameters.add("--intermediate");
    }
    parameters.add("--intermediateName");
    parameters.add(getStreamManager().getRawFileName()+".t.gom");
    if(getOptionManager().getOptionValue("verbose")==Boolean.TRUE) {
      parameters.add("--verbose");
    }
    /* treat user supplied options */
    textCode = t.getText();
    if (textCode.length() > 6) {
      String[] userOpts =
        textCode.substring(5,textCode.length()-1).split("\\s+");
      for (int i=0; i < userOpts.length; i++) {
        parameters.add(userOpts[i]);
      }
    }
    parameters.add("-");
    getLogger().log(Level.FINE,"Calling gom with: "+parameters);
    InputStream backupIn = System.in;
    System.setIn(new DataInputStream(new StringBufferInputStream(gomCode)));

    /* Prepare arguments */
    Object[] preparams = parameters.toArray();
    String[] params = new String[preparams.length];
    for (int i = 0; i < preparams.length; i++) {
      params[i] = (String)preparams[i];
    }
    int res = 1;
    res = tom.gom.Gom.exec(params);
    System.setIn(backupIn);
    if (res != 0 ) {
       getLogger().log(
           new PlatformLogRecord(Level.SEVERE,
             TomMessage.gomFailure,
             new Object[]{currentFile,new Integer(initialGomLine)},
             currentFile, initialGomLine));
      return;
    }
    String generatedMapping = tom.gom.tools.GomEnvironment.getInstance().getLastGeneratedMapping();

    // Simulate the inclusion of generated Tom file
    /*
     * We shall not need to test the validity of the generatedMapping file name
     * as gom returned <> 0 if it is not valid
     * 
     * Anyway, for an empty gom signature, no files are generated 
     */
    if (generatedMapping != null){
    	includeFile(generatedMapping, list);
    }

    updatePosition();
  }

;

backquoteTerm [LinkedList list]
{
    TargetLanguage code = null;
}
    :
        t:BACKQUOTE
        {
          String textCode = getCode();
          if(isCorrect(textCode)) {
            code = `TL(
                       textCode,
                       TextPosition(currentLine,currentColumn),
                       TextPosition(t.getLine(),t.getColumn())
                       );
            list.add(code);
          }

          Option ot = `OriginTracking(Name("Backquote"),t.getLine(), currentFile);
          TomTerm bqTerm = bqparser.beginBackquote();

          // update position for new target block
          updatePosition();
          list.add(bqTerm);
        }
    ;

operator [LinkedList list] throws TomException
{
    TargetLanguage code = null;
}
    :
        t:OPERATOR
        {
            String textCode = pureCode(getCode());
            if(isCorrect(textCode)) {
                code = `TL(
                    textCode,
                    TextPosition(currentLine,currentColumn),
                    TextPosition(t.getLine(),t.getColumn()));
                list.add(`TargetLanguageToTomTerm(code));
            }

            Declaration operatorDecl = tomparser.operator();
            list.add(operatorDecl);
        }
    ;

operatorList [LinkedList list] throws TomException
{
    TargetLanguage code = null;
}
    :
        t:OPERATORLIST
        {
            String textCode = pureCode(getCode());
            if(isCorrect(textCode)) {
                code = `TL(
                    textCode,
                    TextPosition(currentLine,currentColumn),
                    TextPosition(t.getLine(),t.getColumn()));
                list.add(`TargetLanguageToTomTerm(code));
            }

            Declaration operatorListDecl = tomparser.operatorList();
            list.add(operatorListDecl);
        }
    ;

operatorArray [LinkedList list] throws TomException
{
    TargetLanguage code = null;
}
    :
        t:OPERATORARRAY
        {
            String textCode = pureCode(getCode());
            if(isCorrect(textCode)) {
              code = `TL(
                         textCode,
                         TextPosition(currentLine,currentColumn),
                         TextPosition(t.getLine(),t.getColumn()));
              list.add(`TargetLanguageToTomTerm(code));
            }

            Declaration operatorArrayDecl = tomparser.operatorArray();
            list.add(operatorArrayDecl);
        }
    ;

includeConstruct [LinkedList list] throws TomException
{
    TargetLanguage tlCode = null;
    LinkedList blockList = new LinkedList();
}
    :
        t:INCLUDE
        {
            TargetLanguage code = null;
            String textCode = getCode();
            if(isCorrect(textCode)) {
              code = `TL(
                         textCode,
                         TextPosition(currentLine,currentColumn),
                         TextPosition(t.getLine(),t.getColumn()));
              list.add(`TargetLanguageToTomTerm(code));
            }
        }
        tlCode = goalLanguage[blockList]
        {
            includeFile(tlCode.getCode(),list);
            updatePosition();
        }
    ;

code [LinkedList list] throws TomException
{
  TargetLanguage code = null;
}
: t:CODE
{
  String textCode = getCode();
  if(isCorrect(textCode)) {
    code = `TL(
        textCode,
        TextPosition(currentLine,currentColumn),
        TextPosition(t.getLine(),t.getColumn())
        );
    list.add(code);
  }
  textCode = t.getText();
  String metacode = textCode.substring(2,textCode.length()-2);
  tomSplitter(metacode, list);
  updatePosition(targetlexer.getInputState().getLine(),targetlexer.getInputState().getColumn());
}
;

typeTerm [LinkedList list] throws TomException
{
    TargetLanguage code = null;
    int line, column;
}
    :
        (
            tt:TYPETERM
            {
                line = tt.getLine();
                column = tt.getColumn();
            }
        )
        {
            // addPreviousCode...
            String textCode = getCode();
            if(isCorrect(textCode)) {
                code = `TL(
                    textCode,
                    TextPosition(currentLine,currentColumn),
                    TextPosition(line,column));
                list.add(code);
            }
            Declaration termdecl = tomparser.typeTerm();

            list.add(termdecl);
        }

    ;

goalLanguage [LinkedList list] returns [TargetLanguage result] throws TomException
{
    result =  null;
}
    :
        t1:LBRACE
        {
            updatePosition(t1.getLine(),t1.getColumn());
        }
        blockList[list]
        t2:RBRACE
        {
          result = `TL(cleanCode(getCode()),
                       TextPosition(currentLine(),currentColumn()),
                       TextPosition(t2.getLine(),t2.getColumn())
                       );
          targetlexer.clearTarget();
        }
    ;

targetLanguage [LinkedList list] returns [TargetLanguage result] throws TomException
{
    result = null;
}
    :
        blockList[list] t:RBRACE
        {
            String code = removeLastBrace(getCode());

            //System.out.println("code = " + code);
            //System.out.println("list = " + list);

            result = `TL(code,
                         TextPosition(currentLine(),currentColumn()),
                         TextPosition(t.getLine(),t.getColumn())
                         );
            targetlexer.clearTarget();
        }
    ;



// here begins the lexer

{
  import antlr.*;
}
class HostLexer extends Lexer;
options {
  k=6; // the default lookahead

    // a filter for the target language
    // permit to read every characters without defining them
    filter=TARGET;

    // fix the vocabulary to all characters
    charVocabulary='\u0000'..'\uffff';
}

{
    // this buffer contains the target code
    // we append each read character by lexer
    public StringBuffer target = new StringBuffer("");

    // the target parser
    private HostParser parser = null;

    public void setParser(HostParser parser){
        this.parser = parser;
    }

    // clear the buffer
    public void clearTarget(){
        target.delete(0,target.length());
    }

    private TokenStreamSelector selector(){
        return parser.getSelector();
    }

}

// here begins tokens definition

// the following tokens are keywords for tom constructs
// when read, we switch lexers to tom
BACKQUOTE
    : "`" {selector().push("bqlexer");}
    ;
STRATEGY
    : "%strategy" {selector().push("tomlexer");}
    ;
MATCH
    : "%match" {selector().push("tomlexer");}
    ;
OPERATOR
    : "%op"   {selector().push("tomlexer");}
    ;
TYPETERM
    : "%typeterm" {selector().push("tomlexer");}
    ;
OPERATORLIST
    : "%oplist"   {
            selector().push("tomlexer");}
    ;
OPERATORARRAY
    : "%oparray"  {selector().push("tomlexer");}
    ;

// following tokens are keyword for tom constructs
// do not need to switch lexers
INCLUDE
    : "%include"
    ;
GOM
    : "%gom"
      ( 
      |
      (
       '('
       (
       options {
         greedy=false;
         generateAmbigWarnings=false; // shut off newline errors
       }
       : '\r' '\n' {newline();}
       | '\r'    {newline();}
       | '\n'    {newline();}
       | ~('\n'|'\r')
      )*
      ')')
      )
    ;

// basic tokens
LBRACE
    :   '{'
        {
            target.append($getText);
        }
    ;
RBRACE
    :   '}'
        {
            target.append($getText);
        }
    ;

STRING
  : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"'
        {
            target.append($getText);
        }
  ;

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
        {
            target.append($getText);
            $setType(Token.SKIP);
        }
    ;

// comments
COMMENT
    :
        ( SL_COMMENT | ML_COMMENT )
        { $setType(Token.SKIP);}
  ;

protected
SL_COMMENT
    :
        "//"
        ( ~('\n'|'\r') )*
        (
      options {
        generateAmbigWarnings=false;
      }
    : '\r' '\n'
    | '\r'
    | '\n'
        )
        {
 					if(!parser.isSkipComment()) {
            target.append($getText);
          }
            newline();
        }
  ;

protected
ML_COMMENT
    :
        "/*"
        ( { LA(2)!='/' }? '*'
        |
        )
        (
            options {
                greedy=false;  // make it exit upon "*/"
                generateAmbigWarnings=false; // shut off newline errors
            }
        : '\r' '\n' {newline();if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");}
        | '\r'    {newline();if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");}
        | '\n'    {newline();if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");}
        | ~('\n'|'\r'){if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");}
        )*
        "*/"
        {
					if(!parser.isSkipComment()) {
						target.append($getText);
					}
				}
;

CODE
    :
        '%' '['
        ( { LA(2)!='%' }? ']'
        |
        )
        (
            options {
                greedy=false;
                generateAmbigWarnings=false; // shut off newline errors
            }
        : '\r' '\n' {newline();}
        | '\r'    {newline();}
        | '\n'    {newline();}
        | ~('\n'|'\r')
        )*
        ']' '%'
;

// the rule for the filter: just append the text to the buffer
protected
TARGET
    :
        ( . )
        {target.append($getText);}
    ;
