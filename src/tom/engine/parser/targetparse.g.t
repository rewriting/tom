header{
    package jtom.parser;

    import jtom.*;
    import jtom.exception.*;
    import jtom.tools.*;
    import jtom.adt.tomsignature.*;
    import jtom.adt.tomsignature.types.*;

    import java.lang.reflect.*;
    import java.io.*;
    import java.text.*;
    import java.util.*;

    import aterm.*;
    import aterm.pure.*;    

    import antlr.*;
}




class NewTargetParser extends Parser;

options{
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
    NewTomParser tomparser; 

    // the lexer for target language
    NewTargetLexer targetlexer = null;
    
    // locations of target language blocks
    Stack lines = new Stack();
    Stack columns = new Stack();


    public NewTargetParser(TokenStreamSelector selector,String currentFile,HashSet includedFiles,HashSet alreadyParsedFiles){
        this(selector);
        this.selector = selector;
        this.currentFile = currentFile;
        this.targetlexer = (NewTargetLexer) selector.getStream("targetlexer");
        targetlexer.setParser(this);
        this.includedFileSet = includedFiles;
        this.alreadyParsedFileSet = alreadyParsedFiles;
        
        // then create the Tom mode parser
        tomparser = new NewTomParser(getInputState(),this);
    } 

    public TokenStreamSelector getSelector(){
        return selector;
    }

    public String getCurrentFile(){
        return currentFile;
    }
    
    public TomServer getServer(){
        return TomServer.getInstance();
    }

    private final TomSignatureFactory getTomSignatureFactory(){
        return tsf();
    }
    
    private TomSignatureFactory tsf(){
        return environment().getTomSignatureFactory();
    }
    
    private TomEnvironment environment() {
        return getServer().getEnvironment();
    }
    
    private jtom.tools.ASTFactory ast() {
        return environment().getASTFactory();
    }
    
    private SymbolTable symbolTable() {
        return environment().getSymbolTable();
    }

    public TomStructureTable getStructTable() {
        return tomparser.getStructTable();
    }



    // pop the line number of the current block
    public int popLine(){
        return ((Integer) lines.pop()).intValue();
    }

    // pop the column number of the current block
    public int popColumn(){
        return ((Integer) columns.pop()).intValue();
    }

    // push the line number of the next block
    public void pushLine(int line){
        lines.push(new Integer(line));
    }

    // push the column number of the next block
    public void pushColumn(int column){
        columns.push(new Integer(column));
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

    // returns the current line (for error management)
    public int getLine(){
        return targetlexer.getLine();
    }

    private void includeFile(String fileName, LinkedList list) 
    throws TomException, TomIncludeException {
        TomTerm astTom;
        InputStream input;
        byte inputBuffer[];
        //  TomParser tomParser;
        NewTargetParser parser = null;
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
                    
                    for(int i=0 ; !found && i<environment().getImportList().size() ; i++) {
                        file = new File((File)environment().getImportList().get(i),fileName).getCanonicalFile();

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
            if(testIncludedFile(fileAbsoluteName, alreadyParsedFileSet)) {    
                if(!environment().isSilentDiscardImport(fileName)) {
					environment().messageWarning(TomMessage.getString("IncludedFileAlreadyParsed"), new Object[]{fileName, new Integer(getLine()), currentFile}, fileName,getLine());
				}
				return;
                // 				String msg = MessageFormat.format(TomMessage.getString("IncludedFileAlreadyParsed"), new Object[]{fileName, new Integer(getLine()), currentFile});
                //         throw new TomIncludeException(msg);
            }
            
            parser = TomMainParser.newParser(fileAbsoluteName);
            astTom = parser.input();
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

    private boolean testIncludedFile(String fileName, HashSet fileSet) {
        // !(true) if the set did not already contain the specified element.
        return !fileSet.add(fileName);
    }
    
    void p(String s){
        System.out.println(s);
    }
} 


// The grammar starts here

input returns [TomTerm result] throws TomException
{
    result = null;
    LinkedList list = new LinkedList();

    // the current position in text file
    pushLine(1);
    pushColumn(1);
}   
	:
        blockList[list] t:EOF
        {
            list.add(`TargetLanguageToTomTerm(
                    TL(
                        getCode(),
                        TextPosition(popLine(),popColumn()),
                        TextPosition(t.getLine(),t.getColumn())
                    )
                )
            );
            String comment = 
            "Generated by TOM (version " + TomServer.VERSION + "): Do not edit this file";
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
        |   localVariable[list]
        |   operator[list] 
        |   operatorList[list] 
        |   operatorArray[list] 
        |   includeConstruct[list]
        |   typeTerm[list] 
        |   typeList[list] 
        |   typeArray[list] 
        |   LBRACE blockList[list] RBRACE 
        )*
    ;

// the %rule construct
ruleConstruct [LinkedList list]
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
                    TextPosition(popLine(),popColumn()),
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
                    TextPosition(popLine(),popColumn()),
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
                code = `TL(
                    textCode,
                    TextPosition(popLine(),popColumn()),
                    TextPosition(t.getLine(),t.getColumn())
                );
                list.add(code);
            } 
        }
        vasTL = goalLanguage[blockList]
        {
            vasCode = vasTL.getCode().trim();
			String destDir = environment().getDestDir().getPath();

            // Generated Tom, ADT and API from VAS Code
            Object generatedADTName = null;

            try{
                Class vasClass = Class.forName("vas.Vas");
				
				Method execMethod = vasClass.getMethod(
					"externalExec",
					new Class[]{(new String[]{}).getClass(), 
                        Class.forName("java.io.InputStream"),
                        Class.forName( "java.lang.String")
					}
                );
                
                ArrayList vasParams = new ArrayList();
				vasParams.add("--destdir");
				vasParams.add(destDir);
				
				packageName = environment().getPackagePath().replace(File.separatorChar, '.');
                String inputFileNameWithoutExtension = environment().getRawFileName().toLowerCase();

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
                    currentFile
                };
				generatedADTName = execMethod.invoke(vasClass, realParams);
            }
            
            catch (ClassNotFoundException e) {
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
                            environment().messageError(
                                alert.getMessage(),
                                currentFile,alert.getLine()+initialVasLine
                            );
                        } else {
                            environment().messageWarning(alert.getMessage(),
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
            file = new File(adtFileName.substring(0,adtFileName.length()-".adt".length())+".tom");

            try {
                fileName = file.getCanonicalPath();
            } catch (IOException e) {
                throw new TomIncludeException(MessageFormat.format(TomMessage.getString("IOExceptionWithGeneratedTomFile"), new Object[]{fileName, currentFile, e.getMessage()}));
            } 
            includeFile(fileName, list);
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
                    TextPosition(popLine(),popColumn()),
                    TextPosition(t.getLine(),t.getColumn())
                );
                list.add(code);
            } 
            
            Option ot = `OriginTracking(Name("Backquote"),t.getLine(), Name(currentFile));
            TomTerm bqTerm = tomparser.bqTerm();
            list.add(bqTerm);
        }
    ;

localVariable [LinkedList list]
    :
        t:VARIABLE
        {
            String textCode = pureCode(getCode());
            if(isCorrect(textCode)) {
                TargetLanguage code = `TL(
                    textCode,
                    TextPosition(popLine(),popColumn()),
                    TextPosition(t.getLine(),t.getColumn()));
                list.add(`TargetLanguageToTomTerm(code));
            }
            
            list.add(`LocalVariable());
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
                    TextPosition(popLine(),popColumn()),
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
                    TextPosition(popLine(),popColumn()),
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
                    TextPosition(popLine(),popColumn()),
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
                    TextPosition(popLine(),popColumn()),
                    TextPosition(t.getLine(),t.getColumn()));
                list.add(`TargetLanguageToTomTerm(code));
            }
        }
        tlCode = goalLanguage[blockList]
        {
            includeFile(tlCode.getCode(),list);
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
                    TextPosition(popLine(),popColumn()),
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
                    TextPosition(popLine(),popColumn()),
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
                    TextPosition(popLine(),popColumn()),
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
            pushLine(t1.getLine());
            pushColumn(t1.getColumn());
        }
        blockList[list]
        t2:RBRACE 
        {
            result = `TL(cleanCode(getCode()),
                TextPosition(popLine(),popColumn()),
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
                TextPosition(popLine(),popColumn()),
                TextPosition(t.getLine(),t.getColumn())
            );
            
            targetlexer.clearTarget();
        }
    ;



// here begins the lexer


class NewTargetLexer extends Lexer;
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
    private NewTargetParser parser = null;

    public void setParser(NewTargetParser parser){
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
    :   "`" {selector().push("tomlexer");}
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
    :   "%oplist"   {selector().push("tomlexer");}
    ;
OPERATORARRAY
    :   "%oparray"  {selector().push("tomlexer");}
    ;
    

// following tokens are keyword for tom constructs
// do not need to switch lexers
INCLUDE
    :   "%include" 
    ;
VARIABLE
    :   "%variable" 
	;
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



protected
HEX_DIGIT
	:	('0'..'9'|'A'..'F'|'a'..'f')
	;

// comments
COMMENT 
    :
        ( SL_COMMENT | t:ML_COMMENT {$setType(t.getType());} )
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
