header{
  package jtom.parser;
  
  import jtom.*;
  import jtom.exception.*;
  import jtom.tools.*;
  import jtom.adt.tomsignature.*;
  import jtom.adt.tomsignature.types.*;
  
  import tom.platform.*;

  import vas.Vas;

  import java.lang.reflect.*;
  import java.io.*;
  import java.util.*;
  import java.util.logging.*;

  import aterm.*;
  import aterm.pure.*;    
  
  import antlr.*;
}




class HostParser extends Parser;

options{
  // antlr does not catch exceptions automaticaly
  defaultErrorHandler = false;
}


{
  //--------------------------
  %include{TomSignature.tom}
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
  
  /*
    Stack lines = new Stack();
    Stack columns = new Stack();
  */

  public HostParser(TokenStreamSelector selector,String currentFile,
                    HashSet includedFiles,HashSet alreadyParsedFiles, 
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
  
  public TomStructureTable getStructTable() {
    return tomparser.getStructTable();
  }
  /*
  // pop the line number of the current block
  public int popLine(){
  return ((Integer) lines.pop()).intValue();
        
  }

  // pop the column number of the current block
  public int popColumn(){
  return ((Integer) columns.pop()).intValue();
  }
    
  public void pushLine(){ 
  lines.push(new Integer(getLine()));
  p("--push "+lines);
  }
    
  public void pushColumn(){
  columns.push(new Integer(getColumn()));
  }
    
  // push the line number of the next block
  public void pushLine(int line){
  lines.push(new Integer(line));
  p("--push "+lines);
  }

  // push the column number of the next block
  public void pushColumn(int column){
  columns.push(new Integer(column));
  }*/
  /*
    public void setCurrentLine(int i){
    currentLine = i;
    }

    public void setCurrentColumn(int i){
    currentColumn = i;
    }
  */
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
			String msg = TomMessage.getMessage("EmptyIncludedFile", new Object[]{new Integer(getLine()), currentFile});
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
      String msg = TomMessage.getMessage("IncludedFileNotFound", new Object[]{fileName, new Integer(getLine()), currentFile});
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
          getLogger().log( 
                          Level.WARNING,
                          "IncludedFileAlreadyParsed", 
                          new Object[]{
                            currentFile, 
                            new Integer(getLine()), 
                            fileName
                          } 
                          );
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
      String msg = TomMessage.getMessage("ErrorWhileIncludingFile",
                                         new Object[]{e.getClass(),
                                                      fileName,
                                                      currentFile,
                                                      new Integer(getLine()),
                                                      e.getMessage()
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

    // the current position in text file
//    pushLine(1);
//    pushColumn(1);
}   
	:
        blockList[list] t:EOF
        {
            list.add(`TargetLanguageToTomTerm(
                    TL(
                        getCode(),
                        TextPosition(currentLine(),currentColumn()),
                        TextPosition(t.getLine(),t.getColumn())
                    )
                )
            );
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
            /*int i = popLine();
            int j = popColumn();*/
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
            String textCode = getCode();/*
            int i = popLine();
            int j = popColumn();*/
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
    /*            int i = popLine();
                  int j = popColumn();
    */
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
    generatedADTName = Vas.streamedCall((String[]) vasParams.toArray(new String[vasParams.size()]), new StringReader(vasCode));
    //System.out.println(generatedADTName);
    // Check for errors
    //TODO
    
    // Simulate the inclusion of generated Tom file
    String adtFileName = new File(generatedADTName).toString();
    try {
      file = new File(adtFileName.substring(0,adtFileName.length()-".adt".length())+".tom");
      
      fileName = file.getCanonicalPath();
    } catch (IOException e) {
      throw new TomIncludeException(TomMessage.getMessage("IOExceptionWithGeneratedTomFile",
                                                          new Object[]{fileName, currentFile, e.getMessage()}));
    } catch (Exception e) {
      throw new TomIncludeException(TomMessage.getMessage("ExceptionWithGeneratedTomFile",
                                                          new Object[]{adtFileName, currentFile, e.getMessage()}));
    }
    
    includeFile(fileName, list);
    
    // the vas construct is over : a new target block begins
    //pushLine();
    //pushColumn();
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
/*            int i = popLine();
            int j = popColumn();
  */          if(isCorrect(textCode)) {
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
            String textCode = pureCode(getCode());/*
            int i = popLine();
            int j = popColumn();*/
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
/*            int i = popLine();
            int j = popColumn();
  */          if(isCorrect(textCode)) {
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
/*            int i = popLine();
            int j = popColumn();
  */          if(isCorrect(textCode)) {
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
            //pushLine();
            //pushColumn();
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
/*            int i = popLine();
            int j = popColumn();
  */          if(isCorrect(textCode)) {
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
/*            int i = popLine();
            int j = popColumn();
  */          if(isCorrect(textCode)) {
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
/*            int i = popLine();
            int j = popColumn();
  */          if(isCorrect(textCode)) {
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
            //pushLine(t1.getLine());
            //pushColumn(t1.getColumn());
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
        (   
            .
        )
        {target.append($getText);}
    ;
