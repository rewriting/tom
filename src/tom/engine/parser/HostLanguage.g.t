header{/*
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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/
  
package jtom.parser;

}

{
import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.*;
import java.io.*;

import jtom.Tom;
import jtom.TomStreamManager;
import jtom.TomEnvironment;
import jtom.TomMessage;
import jtom.exception.*;
import jtom.adt.tomsignature.TomSignatureFactory;
import jtom.adt.tomsignature.types.*;
import jtom.tools.SymbolTable;
import aterm.*;
import antlr.TokenStreamSelector;
import tom.platform.OptionManager;
import tom.platform.PluginPlatform;
import tom.platform.PlatformLogRecord;
import vas.Vas;
}
class HostParser extends Parser;

options{
  // antlr does not catch exceptions automaticaly
  defaultErrorHandler = false;
}


{
  //--------------------------
    %include{ adt/TomSignature.tom }
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
    
  private final TomSignatureFactory getTomSignatureFactory(){
    return TomEnvironment.getInstance().getTomSignatureFactory();
  }
    
  private TomSignatureFactory tsf(){
    return TomEnvironment.getInstance().getTomSignatureFactory();
  }
  
  private jtom.tools.ASTFactory ast() {
    return TomEnvironment.getInstance().getAstFactory();
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
    String fileAbsoluteName = "";
    fileName = fileName.trim();
    fileName = fileName.replace('/',File.separatorChar);
    fileName = fileName.replace('\\',File.separatorChar);
		if(fileName.equals("")) {
			String msg = TomMessage.getMessage("MissingIncludedFile", new Object[]{currentFile, new Integer(getLine())});
      throw new TomIncludeException(msg);
		}
    
    file = new File(fileName);
    if(file.isAbsolute()) {
      if (!file.exists()) {
        file = null;
      }
    } else {
      // StreamManage rshall find it
      file = getStreamManager().findFile(new File(currentFile).getParentFile(),
                                         fileName);
    }
    
    if(file == null) {
      String msg = TomMessage.getMessage("IncludedFileNotFound", new Object[]{fileName, currentFile, new Integer(getLine()), currentFile});
      throw new TomIncludeException(msg);
    }
    try {
      fileAbsoluteName = file.getAbsolutePath();
      if(testIncludedFile(fileAbsoluteName, includedFileSet)) {
        String msg = TomMessage.getMessage("IncludedFileCycle", new Object[]{fileName, new Integer(getLine()), currentFile});
        throw new TomIncludeException(msg);
      }
      
			// if trying to include a file twice, but not in a cycle : discard
      if(testIncludedFile(fileAbsoluteName, alreadyParsedFileSet)) {    
        if(!getStreamManager().isSilentDiscardImport(fileName)) {
          getLogger().log(new PlatformLogRecord(Level.WARNING, TomMessage.getMessage("IncludedFileAlreadyParsed", 
                          currentFile), fileName, getLine()));
        }
        return;
      }
      
      parser = TomParserPlugin.newParser(fileAbsoluteName,includedFileSet,alreadyParsedFileSet, getOptionManager(), getStreamManager());
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
      String msg = TomMessage.getMessage("ErrorWhileIncludingFile",
                                         new Object[]{e.getClass(),
                                                      fileName,
                                                      currentFile,
                                                      new Integer(getLine()),
																											sw.toString()
                                         });
      throw new TomException(msg);
    }
  }

  private boolean testIncludedFile(String fileName, HashSet fileSet) {
    // !(true) if the set did not already contain the specified element.
    return !fileSet.add(fileName);
  }
    
  void p(String s){
    System.out.println(s);
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
            result = `Tom(ast().makeList(list));
        }
    ;

blockList [LinkedList list] throws TomException
    :
        (
            // either a tom construct or everything else
            matchConstruct[list]
        |   ruleConstruct[list] 
        |   signature[list]
        |   backquoteTerm[list]
        |   operator[list] 
        |   operatorList[list] 
        |   operatorArray[list] 
        |   includeConstruct[list]
        |   typeTerm[list] 
        |   typeList[list] 
        |   typeArray[list]
        |   STRING
        |   LBRACE blockList[list] RBRACE 
        )*
    ;

// the %rule construct
ruleConstruct [LinkedList list] throws TomException
{
    TargetLanguage code = null;
}
    :
        t:RULE // we switch the lexers here : we are in Tom mode
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
            
            Option ot = `OriginTracking(
                Name("Rule"),
                t.getLine(),
                Name(currentFile)
            );    
            
            // call the tomparser for the construct
            Instruction ruleSet = tomparser.ruleConstruct(ot);
            list.add(ruleSet);
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

            Option ot = `OriginTracking(Name("Match"),t.getLine(), Name(currentFile));
            
            Instruction match = tomparser.matchConstruct(ot);
            list.add(match);
        }
    ;

signature [LinkedList list] throws TomException
{
  int initialVasLine;
  TargetLanguage vasTL = null, code = null;
  LinkedList blockList = new LinkedList();
  String vasCode = null, fileName = "", apiName = null, packageName = "";
  File file = null;
}
:
  t:VAS 
  {   
    initialVasLine = t.getLine();
  
    String textCode = getCode();
    if(isCorrect(textCode)) {
      code = `TL(textCode,
                 TextPosition(currentLine,currentColumn),
                 TextPosition(t.getLine(),t.getColumn()));
      list.add(code);
    } 
  }
  vasTL = goalLanguage[blockList]
  {
    vasCode = vasTL.getCode().trim();
    String destDir = getStreamManager().getDestDir().getPath();
    // Generated Tom, ADT and API from VAS Code
    String generatedADTName = null;
    ArrayList vasParams = new ArrayList();
    vasParams.add("--destdir");
    vasParams.add(destDir);
    packageName = getStreamManager().getPackagePath().replace(File.separatorChar, '.');
    String inputFileNameWithoutExtension = getStreamManager().getRawFileName().toLowerCase();
    String subPackageName = "";
    if(packageName.equals("")) {
      subPackageName = inputFileNameWithoutExtension;
    } else {
      subPackageName = packageName + "." + inputFileNameWithoutExtension;
    }
    vasParams.add("--package");
    vasParams.add(subPackageName);
    PluginPlatform vasPlatform = Vas.streamedCall((String[]) vasParams.toArray(new String[vasParams.size()]), new StringReader(vasCode));
		if(vasPlatform == null) {
			throw new TomException(TomMessage.getMessage("VasPlatformFailure", new Object[]{currentFile,new Integer(initialVasLine)}));
		}
		int vasResult = vasPlatform.run();
		if(vasResult != 0) {
			//System.out.println(platform.getAlertForInput().toString());
			throw new TomException(TomMessage.getMessage("VasFailure", new Object[]{currentFile,new Integer(initialVasLine)}));
		}
		
		generatedADTName = (String)vasPlatform.getLastGeneratedObjects().get(0);
    if(generatedADTName == null) {
    	throw new TomException(TomMessage.getMessage("VasFailure", new Object[]{currentFile,new Integer(initialVasLine)}));
    }
    // Simulate the inclusion of generated Tom file
    String adtFileName = new File(generatedADTName).toString();
    try {
      file = new File(adtFileName.substring(0,adtFileName.length()-".adt".length())+".tom");
      fileName = file.getCanonicalPath();
    } catch (IOException e) {
      throw new TomException(TomMessage.getMessage("IOExceptionWithGeneratedTomFile",
                                                          new Object[]{fileName, currentFile, e.getMessage()}));
    } catch (Exception e) {
    	StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      throw new TomException(TomMessage.getMessage("ExceptionWithGeneratedTomFile",
                                                          new Object[]{adtFileName, currentFile, sw.toString()}));
    }
    
    includeFile(fileName, list);
    
    // the vas construct is over : a new target block begins
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
            
          Option ot = `OriginTracking(Name("Backquote"),t.getLine(), Name(currentFile));
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
        |   t:TYPE
            {
                line = t.getLine();
                column = t.getColumn();
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

typeList [LinkedList list] throws TomException
{
    TargetLanguage code = null;
    int line, column;
}
    :
        t:TYPELIST
        {
            String textCode = getCode();
            if(isCorrect(textCode)) {
                code = `TL(
                    textCode,
                    TextPosition(currentLine,currentColumn),
                    TextPosition(t.getLine(),t.getColumn()));
                list.add(code);
            }

            Declaration listdecl = tomparser.typeList();
            list.add(listdecl);
        }
    ;

typeArray [LinkedList list] throws TomException
{
    TargetLanguage code = null;
    int line, column;
}
    :
        t:TYPEARRAY
        {
            String textCode = getCode();
            if(isCorrect(textCode)) {
                code = `TL(
                    textCode,
                    TextPosition(currentLine,currentColumn),
                    TextPosition(t.getLine(),t.getColumn()));
                list.add(code);
            }

            Declaration arraydecl = tomparser.typeArray();
            list.add(arraydecl);
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

            result = `TL(
                code,
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
    :   "`" {selector().push("bqlexer");}
    ;
RULE
    :   "%rule" {selector().push("tomlexer");}
    ;
MATCH
	:	"%match" {selector().push("tomlexer");}
    ;
OPERATOR
    :   "%op"   {selector().push("tomlexer");}
    ;
TYPE
    :   "%type" {selector().push("tomlexer");}
    ;
TYPETERM
    :   "%typeterm" {selector().push("tomlexer");}
    ;
TYPELIST
    :   "%typelist" {selector().push("tomlexer");}
    ;
TYPEARRAY
    :   "%typearray" {selector().push("tomlexer");}
    ;   
OPERATORLIST
    :   "%oplist"   {
            selector().push("tomlexer");}
    ;
OPERATORARRAY
    :   "%oparray"  {selector().push("tomlexer");}
    ;
    

// following tokens are keyword for tom constructs
// do not need to switch lexers
INCLUDE
    :   "%include" 
    ;/*
VARIABLE
    :   "%variable" 
	;*/
VAS
    :   "%vas"  
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
	:	'"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"'
        {
            target.append($getText);
        } 
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
		:	'\r' '\n'
		|	'\r'
		|	'\n'
        )
        {
            target.append($getText);
            newline(); 
        }
	;

protected
ML_COMMENT 
    :
        "/*"        
        (	{ LA(2)!='/' }? '*' 
        |
        )
        (
            options {
                greedy=false;  // make it exit upon "*/"
                generateAmbigWarnings=false; // shut off newline errors
            }
        :	'\r' '\n'	{newline();}
        |	'\r'		{newline();}
        |	'\n'		{newline();}
        |	~('\n'|'\r')
        )*
        "*/"
        {target.append($getText);}
	;


// the rule for the filter : just append the text to the buffer
protected 
TARGET
    :
        ( . )
        {target.append($getText);}
    ;
