header{/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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

package tom.engine.parser.antlr2;
}

{
import java.util.*;
import java.util.logging.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;

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
import tom.engine.adt.code.types.*;

import tom.engine.tools.ASTFactory;
import antlr.TokenStreamSelector;
import tom.platform.OptionManager;
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

  private static Logger logger = Logger.getLogger("tom.engine.parser.HostParser");
  // the lexer selector
  private TokenStreamSelector selector = null;

  // the file to be parsed
  private String currentFile = null;

  private HashSet<String> includedFileSet = null;
  private HashSet<String> alreadyParsedFileSet = null;
  //private static final Object lock = new Object();// verrou pour l'exec de Gom

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
                    HashSet<String> includedFiles, HashSet<String> alreadyParsedFiles,
                    OptionManager optionManager, TomStreamManager streamManager){
    this(selector);
    this.selector = selector;
    this.currentFile = currentFile;
    this.optionManager = optionManager;
    this.streamManager = streamManager;
    this.targetlexer = (HostLexer) selector.getStream("targetlexer");
    targetlexer.setParser(this);
    this.includedFileSet = new HashSet<String>(includedFiles);
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

  private synchronized OptionManager getOptionManager() {
    return optionManager;
  }

  private synchronized TomStreamManager getStreamManager() {
    return streamManager;
  }

  public synchronized TokenStreamSelector getSelector(){
    return selector;
  }

  public synchronized String getCurrentFile(){
    return currentFile;
  }

  public synchronized void updatePosition(){
    updatePosition(getLine(),getColumn());
  }

  public synchronized SymbolTable getSymbolTable() {
    return getStreamManager().getSymbolTable();
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
  private String getCode() {
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

  private synchronized void includeFile(String fileName, List<Code> list)
    throws TomException, TomIncludeException {
    Code astTom;
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
      throw new TomIncludeException(TomMessage.missingIncludedFile,new Object[]{currentFile, Integer.valueOf(getLine())});
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
      throw new TomIncludeException(TomMessage.includedFileNotFound,new Object[]{fileName, currentFile, Integer.valueOf(getLine()), currentFile});
    }
    try {
      fileCanonicalName = file.getCanonicalPath();
      //if(testIncludedFile(fileCanonicalName, includedFileSet)) {
        //throw new TomIncludeException(TomMessage.includedFileCycle,new Object[]{fileName, Integer.valueOf(getLine()), currentFile});
      //}

      // if trying to include a file twice, or if in a cycle: discard
      if(testIncludedFile(fileCanonicalName, alreadyParsedFileSet) ||
	  testIncludedFile(fileCanonicalName, includedFileSet)) {
        if(!getStreamManager().isSilentDiscardImport(fileName)) {
          TomMessage.info(logger, currentFile, getLine(), TomMessage.includedFileAlreadyParsed,fileName);
        }
        return;
      }
      Reader fileReader = new BufferedReader(new FileReader(fileCanonicalName));

      //if ((Boolean)optionManager.getOptionValue("newparser")) {
      //  parser = NewParserPlugin.newParser(fileReader,fileCanonicalName,
      //      includedFileSet,alreadyParsedFileSet,
      //      getOptionManager(), getStreamManager());
      //} else {
        parser = tom.engine.parser.TomParserPlugin.newParser(fileReader,fileCanonicalName,
            includedFileSet,alreadyParsedFileSet,
            getOptionManager(), getStreamManager());
      //}
      parser.setSkipComment();
      astTom = parser.input();
      astTom = `TomInclude(astTom.getCodeList());
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
            Integer.valueOf(getLine()),
            sw.toString()
          });
    }
  }

  private boolean testIncludedFile(String fileName, HashSet<String> fileSet) {
    // !(true) if the set did not already contain the specified element.
    return !fileSet.add(fileName);
  }

  /*
   * this function receives a string that comes from %[ ... ]%
   * @@ corresponds to the char '@', so they are encoded into ]% (which cannot
   * appear in the string)
   * then, the string is split around the delimiter @
   * alternatively, each string correspond either to a metaString, or a string
   * to parse the @@ encoded by ]% is put back as a single '@' in the metaString
   */
  public String tomSplitter(String subject, List<Code> list) {

    String metaChar = "]%";
    String escapeChar = "@";

    //System.out.println("initial subject: '" + subject + "'");
    subject = subject.replace(escapeChar+escapeChar,metaChar);
    //System.out.println("subject: '" + subject + "'");
    String split[] = subject.split(escapeChar);
    boolean last = subject.endsWith(escapeChar);
    int numSeparator = split.length + 1 + (last ? 1 : 0);
    if (numSeparator%2==1) {
      TomMessage.error(logger, currentFile, getLine(), TomMessage.badNumberOfAt);
    }
    //System.out.println("split.length: " + split.length);
    boolean metaMode = true;
    String res = "";
    for(int i=0 ; i<split.length ; i++) {
      if(metaMode) {
        // put back escapeChar instead of metaChar
        String code = metaEncodeCode(split[i].replace(metaChar,escapeChar));
        metaMode = false;
        //System.out.println("metaString: '" + code + "'");
        list.add(`TargetLanguageToCode(ITL(code)));
      } else {
        String code = "+"+split[i]+"+";
        metaMode = true;
        //System.out.println("prg to parse: '" + code + "'");
        try {
          Reader codeReader = new BufferedReader(new StringReader(code));
          HostParser parser;
          //if (((Boolean)optionManager.getOptionValue("newparser"))) {
          //  parser = NewParserPlugin.newParser(codeReader,getCurrentFile(),
          //      getOptionManager(), getStreamManager());
          //} else {
            parser = tom.engine.parser.TomParserPlugin.newParser(codeReader,getCurrentFile(),
                getOptionManager(), getStreamManager());
          //}
          Code astTom = parser.input();
          %match(astTom) {
            Tom(concCode(_*,c,_*)) -> {
              list.add(`c);
            }
          }
        } catch (IOException e) {
          throw new TomRuntimeException("IOException catched in tomSplitter");
        } catch (Exception e) {
          throw new TomRuntimeException("Exception catched in tomSplitter");
        }
      }
    }
    if(subject.endsWith(escapeChar)) {
      // add an empty string when %[...@...@]%
      list.add(`TargetLanguageToCode(ITL("\"\"")));
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
		StringBuilder sb = new StringBuilder((int)1.5*code.length());
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
  }

}

// The grammar starts here

input returns [Code result] throws TomException
{
    result = null;
    List<Code> list = new LinkedList<Code>();
}
  :
  blockList[list] t:EOF
        {
          // This TL is last block: do no need to specify line and column
          list.add(`TargetLanguageToCode(TL(getCode(),
                                            TextPosition(currentLine(),currentColumn()),
                                            TextPosition(t.getLine(), t.getColumn()))));
            //String comment = "Generated by TOM (version " + Tom.VERSION + "): Do not edit this file";
            //list.add(0,`TargetLanguageToCode(Comment(comment)));
            result = `Tom(ASTFactory.makeCodeList(list));
        }
    ;

blockList [List<Code> list] throws TomException
    :
        (
            // either a tom construct or everything else
            matchConstruct[list]
        |   strategyConstruct[list]
        |   transformationConstruct[list] 
        |   tracelinkConstruct[list]
        |   resolveConstruct[list]
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
strategyConstruct [List<Code> list] throws TomException
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
                list.add(`TargetLanguageToCode(code));
            }

            Option ot = `OriginTracking( Name("Strategy"), t.getLine(), currentFile);

            // call the tomparser for the construct
            Declaration strategy = tomparser.strategyConstruct(ot);
            list.add(`DeclarationToCode(strategy));
        }
    ;

// the %transformation construct
transformationConstruct [List<Code> list] throws TomException
{
    TargetLanguage code = null;
}
    :
        t:TRANSFORMATION // we switch the lexers here : we are in Tom mode
        {
            // add the target code preceeding the construct
            String textCode = getCode();

            if(isCorrect(textCode)) {
                code = `TL(
                    textCode,
                    TextPosition(currentLine,currentColumn),
                    TextPosition(t.getLine(),t.getColumn())
                );
                list.add(`TargetLanguageToCode(code));
            }

            Option ot = `OriginTracking( Name("Transformation"), t.getLine(), currentFile);

            // call the tomparser for the construct
            Declaration transformation = tomparser.transformationConstruct(ot);
            list.add(`DeclarationToCode(transformation));
        }
    ;
resolveConstruct [List<Code> list] throws TomException
{
    TargetLanguage code = null;
}
    :
        t:RESOLVE
        {
            String textCode = getCode();
            if(isCorrect(textCode)) {
                code = `TL(
                    textCode,
                    TextPosition(currentLine,currentColumn),
                    TextPosition(t.getLine(),t.getColumn())
                );
                list.add(`TargetLanguageToCode(code));
            }
            Option ot = `OriginTracking(Name("Resolve"), t.getLine(), currentFile);
            Instruction resolve = tomparser.resolveConstruct(ot);
            list.add(`InstructionToCode(resolve));
        }
    ;

tracelinkConstruct [List<Code> list] throws TomException
{
    TargetLanguage code = null;
}
    :
        t:TRACELINK
        {
            String textCode = getCode();
            if(isCorrect(textCode)) {
                code = `TL(
                    textCode,
                    TextPosition(currentLine,currentColumn),
                    TextPosition(t.getLine(),t.getColumn())
                );
                list.add(`TargetLanguageToCode(code));
            }
            Option ot = `OriginTracking(Name("Tracelink"), t.getLine(), currentFile);
            Instruction tracelink = tomparser.tracelinkConstruct(ot);
            list.add(`InstructionToCode(tracelink));
        }
    ;


matchConstruct [List<Code> list] throws TomException
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
                list.add(`TargetLanguageToCode(code));
            }

            Option ot = `OriginTracking(Name("Match"),t.getLine(), currentFile);

            Instruction match = tomparser.matchConstruct(ot);
            list.add(`InstructionToCode(match));
        }
    ;

gomsignature [List<Code> list] throws TomException
{
  int initialGomLine;
  TargetLanguage code = null;
  List<Code> blockList = new LinkedList<Code>();
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
      list.add(`TargetLanguageToCode(code));
    }
  }
  {
    synchronized(Tom.getLock()) {
    BlockParser blockparser =
      BlockParser.makeBlockParser(targetlexer.getInputState());
    gomCode = cleanCode(blockparser.block().trim());

    File config_xml = null;
    ArrayList<String> parameters = new ArrayList<String>();
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
      TomMessage.finer(logger, null, 0, TomMessage.failGetCanonicalPath,config_xml.getPath());
    }

    String destDir = getStreamManager().getDestDir().getPath();
    String packageName = getStreamManager().getPackagePath().replace(File.separatorChar, '.');
    String inputFileNameWithoutExtension = getStreamManager().getRawFileName().toLowerCase();
    String subPackageName = "";
    if (packageName.equals("")) {
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
    if(Boolean.TRUE == getOptionManager().getOptionValue("optimize")) {
      parameters.add("--optimize");
    }
    if(Boolean.TRUE == getOptionManager().getOptionValue("optimize2")) {
      parameters.add("--optimize2");
    }
    if(Boolean.TRUE == getOptionManager().getOptionValue("newtyper")) {
      parameters.add("--newtyper");
    }
    if(Boolean.TRUE == getOptionManager().getOptionValue("newparser")) {
      parameters.add("--newparser");
    }
    parameters.add("--intermediateName");
    parameters.add(getStreamManager().getRawFileName()+".t.gom");
    if(getOptionManager().getOptionValue("verbose")==Boolean.TRUE) {
      parameters.add("--verbose");
    }
    /* treat user supplied options */
    textCode = t.getText();
    if(textCode.length() > 6) {
      String[] userOpts = textCode.substring(5,textCode.length()-1).split("\\s+");
      for(int i=0; i < userOpts.length; i++) {
        parameters.add(userOpts[i]);
      }
    }

    final File tmpFile;
    try {
      tmpFile = File.createTempFile("tmp", ".gom", null).getCanonicalFile();
      parameters.add(tmpFile.getPath());
    } catch (IOException e) {
      TomMessage.error(logger, null, 0, TomMessage.ioExceptionTempGom,e.getMessage());
      e.printStackTrace();
      return;
    }

    TomMessage.fine(logger, null, 0, TomMessage.writingExceptionTempGom,tmpFile.getPath());

    try {
      Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpFile)));
      writer.write(new String(gomCode.getBytes("UTF-8")));
      writer.flush();
      writer.close();
    } catch (IOException e) {
      TomMessage.error(logger, null, 0, TomMessage.writingFailureTempGom,e.getMessage());
      return;
    }

    /* Prepare arguments */
    Object[] preparams = parameters.toArray();
    String[] params = new String[preparams.length];
    for (int i = 0; i < preparams.length; i++) {
      params[i] = (String)preparams[i];
    }

    int res = 1;
    Map<String,String> informationTracker = new HashMap<String,String>();
    informationTracker.put(tom.engine.tools.TomGenericPlugin.KEY_LAST_GEN_MAPPING,null);

    informationTracker.put("gomBegin",""+initialGomLine);
    informationTracker.put("inputFileName",getStreamManager().getInputFileName());

    //5 tom.platform.PluginPlatformFactory.getInstance().getInformationTracker().put(java.lang.Thread.currentThread().getId(),null);
    try {
    // Call tom.gom.Gom.exec(params,informationTracker) using reflexivity, to
    // avoid a build time dempendency between tom and gom
    res = ((Integer) Class.forName("tom.gom.Gom")
        .getMethod("exec", new Class[] {params.getClass(), Map.class})
        .invoke(null, new Object[] {params, informationTracker}))
        .intValue();
    } catch (ClassNotFoundException cnfe) {
      TomMessage.error(logger, currentFile, initialGomLine, TomMessage.gomInitFailure,currentFile,Integer.valueOf(initialGomLine), cnfe);
    } catch (NoSuchMethodException nsme) {
      TomMessage.error(logger, currentFile, initialGomLine, TomMessage.gomInitFailure,currentFile,Integer.valueOf(initialGomLine), nsme);
    } catch (InvocationTargetException ite) {
      TomMessage.error(logger, currentFile, initialGomLine, TomMessage.gomInitFailure,currentFile,Integer.valueOf(initialGomLine), ite);
    } catch (IllegalAccessException iae) {
      TomMessage.error(logger, currentFile, initialGomLine, TomMessage.gomInitFailure,currentFile,Integer.valueOf(initialGomLine), iae);
    }
    tmpFile.deleteOnExit();
    if(res != 0) {
      TomMessage.error(logger, currentFile, initialGomLine, TomMessage.gomFailure,currentFile,Integer.valueOf(initialGomLine));
      return;
    }
    String generatedMapping = (String)informationTracker.get(tom.engine.tools.TomGenericPlugin.KEY_LAST_GEN_MAPPING);

    // Simulate the inclusion of generated Tom file
    /*
     * We shall not need to test the validity of the generatedMapping file name
     * as gom returned <> 0 if it is not valid
     *
     * Anyway, for an empty gom signature, no files are generated
     */
    if (generatedMapping != null) {
    	includeFile(generatedMapping, list);
    }
    updatePosition();
    } //synchronized
  }

;

backquoteTerm [List<Code> list]
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
            list.add(`TargetLanguageToCode(code));
          }

          Option ot = `OriginTracking(Name("Backquote"),t.getLine(), currentFile);
          BQTerm result = bqparser.beginBackquote();
          //System.out.println("parse bqterm \n"+result);

          // update position for new target block
          updatePosition();
          list.add(`BQTermToCode(result));
          //throw new RuntimeException("BackQuote parser not yet implemented");
        }
    ;

operator [List<Code> list] throws TomException
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
                list.add(`TargetLanguageToCode(code));
            }

            Declaration operatorDecl = tomparser.operator();
            list.add(`DeclarationToCode(operatorDecl));
        }
    ;

operatorList [List list] throws TomException
{
    TargetLanguage code = null;
    int line = 0;
    int column = 0;
}
    :
        (
            t1:OPERATORLIST { line=t1.getLine();column=t1.getColumn(); }
        )
        {
            String textCode = pureCode(getCode());
            if(isCorrect(textCode)) {
                code = `TL(
                    textCode,
                    TextPosition(currentLine,currentColumn),
                    TextPosition(line,column));
                list.add(`TargetLanguageToCode(code));
            }

            Declaration operatorListDecl = tomparser.operatorList();
            list.add(`DeclarationToCode(operatorListDecl));
        }
;

operatorArray [List<Code> list] throws TomException
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
              list.add(`TargetLanguageToCode(code));
            }

            Declaration operatorArrayDecl = tomparser.operatorArray();
            list.add(`DeclarationToCode(operatorArrayDecl));
        }
    ;

includeConstruct [List<Code> list] throws TomException
{
    TargetLanguage tlCode = null;
    List<Code> blockList = new LinkedList<Code>();
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
              list.add(`TargetLanguageToCode(code));
            }
        }
        tlCode = goalLanguage[blockList]
        {
            includeFile(tlCode.getCode(),list);
            updatePosition();
        }
    ;

code [List<Code> list] throws TomException
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
    list.add(`TargetLanguageToCode(code));
  }
  textCode = t.getText();
  String metacode = textCode.substring(2,textCode.length()-2);
  tomSplitter(metacode, list);
  updatePosition(targetlexer.getInputState().getLine(),targetlexer.getInputState().getColumn());
}
;

typeTerm [List<Code> list] throws TomException
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
                list.add(`TargetLanguageToCode(code));
            }
            Declaration termdecl = tomparser.typeTerm();

            if (termdecl != null) {
              list.add(`DeclarationToCode(termdecl));
            }
        }

    ;

goalLanguage [List<Code> list] returns [TargetLanguage result] throws TomException
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

targetLanguage [List<Code> list] returns [TargetLanguage result] throws TomException
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
    public StringBuilder target = new StringBuilder("");

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
SUBTYPE
    : "%subtype"  {selector().push("tomlexer");}
    ;
TRANSFORMATION
    : "%transformation" {selector().push("tomlexer");}
    ;
TRACELINK
    : "%tracelink" {selector().push("tomlexer");}
    ;
//RESOLVELINK
//    : "%resolvelink" {selector().push("tomlexer");}
//    ;
RESOLVE
    : "%resolve" {selector().push("tomlexer");}
    ;
TYPETERM
    : "%typeterm" {selector().push("tomlexer");}
    ;
OPERATORLIST
    : "%oplist"   {selector().push("tomlexer");}
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

  /*
protected LETTER    :   ('a'..'z' | 'A'..'Z')   ;
protected DIGIT     :   ('0'..'9')  ;
ID
options{testLiterals = true;}
    :
        ('_')? LETTER
        (
            options{greedy = true;}:
            ( LETTER | DIGIT | '_' )
        )*
        {
            target.append($getText);
        }
    ;
*/

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
