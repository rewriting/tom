package tom.engine.newparser.parser;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.*;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

import tom.engine.TomStreamManager;
import tom.engine.TomMessage;

import tom.engine.newparser.parser.miniTomParser.*;
import tom.engine.newparser.parser.BQTermParser.*;
import tom.engine.newparser.parser.miniTomLexer;
import tom.engine.newparser.parser.BQTermLexer;
import static tom.engine.newparser.util.TreeFactory.*;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.ANTLRFileStream;

import tom.platform.OptionManager;

import tom.engine.newparser.debug.HostParserDebugger;

import tom.engine.newparser.streamanalysis.DelimitedSequenceDetector;
import tom.engine.newparser.streamanalysis.StreamAnalyst;

/**
 * 
 * @see HostParser
 */
public abstract class ParserAction {

  // static fields with cool ParserActions
  public static final ParserAction
   SKIP_DELIMITED_SEQUENCE              = new SkipDelimitedSequence(false),
   SKIP_DELIMITED_SEQUENCE_EOF_TOLERANT = new SkipDelimitedSequence(true),
   PACK_HOST_CONTENT              = PackHostContent.getInstance(),
   PARSE_MATCH_CONSTRUCT          = ParseMatchConstruct.getInstance(),
   PARSE_OPERATOR_CONSTRUCT       = ParseOperatorConstruct.getInstance(),
   PARSE_OPERATOR_LIST_CONSTRUCT  = ParseOperatorListConstruct.getInstance(),
   PARSE_OPERATOR_ARRAY_CONSTRUCT = ParseOperatorArrayConstruct.getInstance(),
   PARSE_TYPETERM_CONSTRUCT       = ParseTypetermConstruct.getInstance(),
   PARSE_INCLUDE_CONSTRUCT        = ParseIncludeConstruct.getInstance(),
   PARSE_METAQUOTE_CONSTRUCT      = ParseMetaQuoteConstruct.getInstance(),
   PARSE_GOM_CONSTRUCT            = ParseGomConstruct.getInstance(), 
   PARSE_BQTERM_CONSTRUCT         = ParseBQConstruct.getInstance();

  /**
   * Implementations of ParserAction.doAction should check
   * runtime type of analyst.
   * 
   * doAction should be called right after analyst found something.
   * char that made it match should be accessible using input.LA(1)
   * 
   * if matched keyword is more that 1 char long, previous chars
   * are at the end of hostCharBuffer.
   * 
   * doAction will terminate in such way that there is no need to
   * call input.consume(). 
   * 
   * @param input the inputStream
   * @param hostCharBuffer host code which has yet been stored in the tree
   * @param tree the tree under construction
   * @param analyst the analyst that matched (i.e. that fired the action)
   * @param optionManager
   */
  public abstract void doAction(CharStream input,
      HostBlockBuilder hostBlockBuilder,
      Tree tree,
      StreamAnalyst analyst,
      TomStreamManager streamManager,
      OptionManager optionManager);

  private static class SkipDelimitedSequence extends ParserAction {

    private boolean EOFTolerant;

    private SkipDelimitedSequence(boolean EOFTolerant) {
      this.EOFTolerant = EOFTolerant;
    }

    @Override
    public void doAction(CharStream input, HostBlockBuilder hostBlockBuilder,
          Tree tree, StreamAnalyst analyst, TomStreamManager streamManager,
          OptionManager optionManager) {

      int startLine = input.getLine();
      int startColumn = input.getCharPositionInLine();

      if(!(analyst instanceof DelimitedSequenceDetector)) {
        throw new RuntimeException("Bad StreamAnalyst implementation");
      }

      // skip one char, this is last char of opening sequence
      // forget to keep it would make analyst state wrong if
      // delimiter sequence is only one char long
      hostBlockBuilder.readOneChar(input);
      input.consume();

      while(analyst.readChar(input)) { // readChar update and return "foundness" value
        if(input.LA(1)==CharStream.EOF) {
          if(EOFTolerant) {
            return;
          } else {
            //System.err.println("Unexpected EndOfFile");
            throw new RuntimeException( // XXX handle nicely
                "File :"+input.getSourceName()
                + " :: unexpected EOF, expecting '"
                + ((DelimitedSequenceDetector)analyst).getClosingKeywordString()
                + "' ('"
                + ((DelimitedSequenceDetector)analyst).getOpeningKeywordString()
                + "' is at "+startLine+":"+startColumn
                );
          }
        }

        hostBlockBuilder.readOneChar(input); // save host code char for later use
        input.consume();
      }
    }
  }

  private static class ParseGomConstruct extends ParserAction {

    private static final ParseGomConstruct instance = new ParseGomConstruct();
    private ParseGomConstruct() {}

    public static ParserAction getInstance() {
      return instance;
    }

    @Override
    public void doAction(CharStream input, HostBlockBuilder hostBlockBuilder,
        Tree tree, StreamAnalyst analyst, TomStreamManager streamManager,
        OptionManager optionManager) {

      ArrayList<String> parameters = new ArrayList<String>();
      Logger logger = Logger.getLogger("tom.engine.parser.HostParser");

      String currentFile = input.getSourceName();


      // remove keyword from stream and hostBlockBuilder and insert previously
      // consumed hostChars in tree
      hostBlockBuilder.removeLastChars(analyst.getOffsetAtMatch());
      PACK_HOST_CONTENT.doAction(input, hostBlockBuilder, tree, analyst,
          streamManager, optionManager);
      input.consume();

      String gomCode;

      // XXX there is a copy of this in ParseIncludeConstruct (need refactoring) ===
      // consume chars until '{'
      List<Character> legitChars = Arrays.asList('\n', '\r', '\t', ' ');
      while(legitChars.contains((char)input.LA(1))) {
        input.consume();
      }

      char tmp;
      if((tmp=(char)input.LA(1))!='{') {
        throw new RuntimeException("Unexpected '"+tmp+"', expecting '{'");//XXX
      }

      // get Gom code
      DelimitedSequenceDetector delimitedSequenceDetector =
        new DelimitedSequenceDetector("{", "}");

      delimitedSequenceDetector.readChar(input); // init detector
      input.consume(); // avoid '{' to go in hostBlockBuilder

      int initialGomLine = input.getLine();

      // read every char and add them to hostBlockBuilder
      SKIP_DELIMITED_SEQUENCE.doAction(input, hostBlockBuilder, tree,
          delimitedSequenceDetector, streamManager, optionManager);

      // remove '}' from hostBlockBuilder
      hostBlockBuilder.removeLastChars(1);  

      gomCode = hostBlockBuilder.getText();
      //XXX end copy ===============================================================

      // call Gom Parser

      // prepare parameters (from parser.HostLanguage.g.t)
      File config_xml = null;

      try {
        String tom_home = System.getProperty("tom.home");
        if(tom_home != null) {
          config_xml = new File(tom_home,"Gom.xml");
        } else {
          // for the eclipse plugin for example
          String tom_xml_filename =
            ((String)optionManager.getOptionValue("X"));
          config_xml =
            new File(new File(tom_xml_filename).getParentFile(),"Gom.xml");
          // pass all the received parameters to gom in the case that it will call tom
          java.util.List<File> imp = streamManager.getUserImportList();
          for(File f:imp) {
            parameters.add("--import");
            parameters.add(f.getCanonicalPath());
          }
        }
        config_xml = config_xml.getCanonicalFile();
      } catch (IOException e) {
        TomMessage.finer(logger, null, 0, TomMessage.failGetCanonicalPath,config_xml.getPath());
      }

      String destDir = streamManager.getDestDir().getPath();
      String packageName = streamManager.getPackagePath().replace(File.separatorChar, '.');
      String inputFileNameWithoutExtension = streamManager.getRawFileName().toLowerCase();
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
      if(optionManager.getOptionValue("wall")==Boolean.TRUE) {
        parameters.add("--wall");
      }
      if(optionManager.getOptionValue("intermediate")==Boolean.TRUE) {
        parameters.add("--intermediate");
      }
      if(Boolean.TRUE == optionManager.getOptionValue("optimize")) {
        parameters.add("--optimize");
      }
      if(Boolean.TRUE == optionManager.getOptionValue("optimize2")) {
        parameters.add("--optimize2");
      }
      if(Boolean.TRUE == optionManager.getOptionValue("newtyper")) {
        parameters.add("--newtyper");
      }
      if(Boolean.TRUE == optionManager.getOptionValue("newparser")) {
        parameters.add("--newparser");
      }
      parameters.add("--intermediateName");
      parameters.add(streamManager.getRawFileName()+".t.gom");
      if(optionManager.getOptionValue("verbose")==Boolean.TRUE) {
        parameters.add("--verbose");
      }

      /* treat user supplied options */
      /*if(gomCode.length() > 6) {
        String[] userOpts = gomCode.substring(5,gomCode.length()-1).split("\\s+");
        for(int i=0; i < userOpts.length; i++) {
        parameters.add(userOpts[i]);
        }
        }*/

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
      informationTracker.put("inputFileName", streamManager.getInputFileName());

      try {
        // Call tom.gom.Gom.exec(params,informationTracker) using reflexivity, to
        // avoid a build time dempendency between tom and gom
        res = ((Integer) Class.forName("tom.gom.Gom")
            .getMethod("exec", new Class[] {params.getClass(), Map.class})
            .invoke(null, new Object[] {params, informationTracker}))
          .intValue();
        /*
           } catch (ClassNotFoundException cnfe) {
           TomMessage.error(logger, currentFile, initialGomLine,
           TomMessage.gomInitFailure,currentFile,
           Integer.valueOf(initialGomLine), cnfe);
           } catch (NoSuchMethodException nsme) {
           TomMessage.error(logger, currentFile, initialGomLine,
           TomMessage.gomInitFailure,currentFile,
           Integer.valueOf(initialGomLine), nsme);
           } catch (InvocationTargetException ite) {
           TomMessage.error(logger, currentFile, initialGomLine,
           TomMessage.gomInitFailure,currentFile,
           Integer.valueOf(initialGomLine), ite);
           } catch (IllegalAccessException iae) {
           TomMessage.error(logger, currentFile, initialGomLine,
           TomMessage.gomInitFailure,currentFile,
           Integer.valueOf(initialGomLine), iae);
           }*/
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    tmpFile.deleteOnExit();
    if(res != 0) {
      TomMessage.error(logger, currentFile, initialGomLine,
          TomMessage.gomFailure,currentFile,
          Integer.valueOf(initialGomLine));
      return;
    }

    String generatedMapping = (String)informationTracker.get(
        tom.engine.tools.TomGenericPlugin.KEY_LAST_GEN_MAPPING);

    // parse resulting tom code
    CharStream tomInput;
    try {
      tomInput = new ANTLRFileStream(generatedMapping);
    } catch (Exception e) {
      throw new RuntimeException(e); //XXX
    }

    // XXX streamManager and optionManager should be modified
    HostParser parser = new HostParser(streamManager, optionManager);
    List<CommonTree> listOfBlock = parser.parseListOfBlock(tomInput);

    // insert it in AST
    for(Tree t : listOfBlock) {
      tree.addChild(t);
    }

  }
}

  private static class ParseIncludeConstruct extends ParserAction {
    
    private static final ParseIncludeConstruct instance = new ParseIncludeConstruct();
    public static ParserAction getInstance() {return instance;}
    private ParseIncludeConstruct() {;}
    
    @Override
    public void doAction(CharStream input,
		  				HostBlockBuilder hostBlockBuilder,
		  				Tree tree,
		  				StreamAnalyst analyst,
              TomStreamManager streamManager,
              OptionManager optionManager) {

    String includeName;

    // treat keyword chars
    hostBlockBuilder.removeLastChars(analyst.getOffsetAtMatch());
    PACK_HOST_CONTENT.doAction(input, hostBlockBuilder, tree, analyst,
          streamManager, optionManager);
    input.consume();

    // get file to include name
// XXX there is a copy of this in ParseGomConstruct (need refactoring) =======
    
    List<Character> legitChars = Arrays.asList('\n', '\r', '\t', ' ');
    while(legitChars.contains((char)input.LA(1))) {
      input.consume();
    }

    char tmp;
    if((tmp=(char)input.LA(1))!='{') {
      throw new RuntimeException("Unexpected '"+tmp+"', expecting '{'");//XXX
    }
    
    
    DelimitedSequenceDetector delimitedSequenceDetector =
      new DelimitedSequenceDetector("{", "}");
      
      delimitedSequenceDetector.readChar(input); // init detector
      input.consume(); // avoid '{' to go in hostBlockBuilder

      int initialGomLine = input.getLine();
     
      // read every char and add them to hostBlockBuilder
    SKIP_DELIMITED_SEQUENCE.doAction(input, hostBlockBuilder, tree,
        delimitedSequenceDetector, streamManager, optionManager);
      
      // remove '}' from hostBlockBuilder
      hostBlockBuilder.removeLastChars(1);  

      includeName = hostBlockBuilder.getText().trim();
//XXX end copy ===============================================================

    // include it
    // XXX treat every failure case like in HostLanguage.g.t
    HostParser parser = new HostParser(streamManager, optionManager);
    File file = streamManager.findFile(
      new File(input.getSourceName()).getParentFile(), includeName);

    CharStream tomInput;
    try{
      tomInput = new ANTLRFileStream(file.getCanonicalPath());
    } catch (Exception e) {
      throw new RuntimeException(e); //XXX
    }

    List<CommonTree> listOfBlock = parser.parseListOfBlock(tomInput);
    for(Tree t : listOfBlock) {
      tree.addChild(t);
    }

    }
  }
    private static abstract class GenericParseConstruct extends ParserAction {
    
    @Override
    public void doAction(CharStream input, HostBlockBuilder hostBlockBuilder,
        Tree tree, StreamAnalyst analyst, TomStreamManager streamManager,
        OptionManager optionManager) {

      // remove beginning of the keyword from hostBlockBuilder
      // ("%matc" if keyword is "%match")
      hostBlockBuilder.removeLastChars(analyst.getOffsetAtMatch());
      
      PACK_HOST_CONTENT.doAction(input, hostBlockBuilder, tree, analyst,
          streamManager, optionManager);
      
      // consume last chat of the keyword
      // ("h" if keyword is "%match")
      input.consume();
      
      try {
        miniTomLexer lexer = new miniTomLexer(input);
        
  // XXX DEBUG ===
  if(HostParserDebugger.isOn()) {
  HostParserDebugger.getInstance()
   .debugNewCall(lexer.getClassDesc(), input, getConstructName());
  }
  // == /DEBUG ===
        
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        miniTomParser parser = new miniTomParser(tokenStream);
        
        GenericConstructReturn retval = parseSpecificConstruct(parser);
        
        tree.addChild((Tree)retval.getTree());
        
        // allow action to return with a "clean" input state
        // (input.LA(1) is char after '}')
        input.rewind(retval.getMarker());
        
  // XXX DEBUG ===
  if(HostParserDebugger.isOn()) {
  HostParserDebugger.getInstance()
   .debugReturnedCall(lexer.getClassDesc(), input, getConstructName());
  }
  // == /DEBUG ===
        
        } catch(Exception e) {
          // XXX poorly handled exception
          e.printStackTrace();
        }
      
    }

    /**
     * used for debug
     */
    public abstract String getConstructName();
    
    public abstract GenericConstructReturn
      parseSpecificConstruct(miniTomParser parser) throws RecognitionException;
    
  }

  private static class ParseMatchConstruct extends GenericParseConstruct {

    private static final ParseMatchConstruct instance = new ParseMatchConstruct();
    
    public static ParserAction getInstance() {return instance;}
    
    private ParseMatchConstruct() {;}
    
    @Override
    public String getConstructName() {
      return "MatchConstruct";
    }

    @Override
    public GenericConstructReturn
      parseSpecificConstruct(miniTomParser parser) throws RecognitionException {
      
      matchConstruct_return retval = parser.matchConstruct();
      return new GenericConstructReturn(retval.tree, retval.marker);
    }
    
  }
 
  private static class ParseBQConstruct extends ParserAction {

    private static final ParserAction instance = new ParseBQConstruct();
    private ParseBQConstruct() { }
    public static ParserAction getInstance() {
      return instance;
    }

    @Override
    public void doAction(CharStream input, HostBlockBuilder hostBlockBuilder,
        Tree tree, StreamAnalyst analyst, TomStreamManager streamManager,
        OptionManager optionManager) {
     
      hostBlockBuilder.removeLastChars(analyst.getOffsetAtMatch());
      
      PACK_HOST_CONTENT.doAction(input, hostBlockBuilder, tree, analyst,
          streamManager, optionManager);
      
      // for once leave keyword in charStream
      
      try {
        BQTermLexer lexer = new BQTermLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        BQTermParser parser = new BQTermParser(tokenStream);
        
        csBQTerm_return retval =  parser.csBQTerm();
        
        tree.addChild(
            makeTree(BQTermLexer.Cst_BQTermToBlock, "CsBQTermToBlock",
              (CommonTree)retval.getTree()));
        
        // allow action to return with a "clean" input state
        input.rewind(retval.marker);
        
        } catch(Exception e) {
          // XXX poorly handled exception
          e.printStackTrace();
        }
      
    }
  }

  private static class ParseOperatorConstruct extends GenericParseConstruct {
    
    private static final ParseOperatorConstruct instance = new ParseOperatorConstruct();
    
    public static ParserAction getInstance() {
      return instance;
    }
    
    private ParseOperatorConstruct() {;}
    
    @Override
    public String getConstructName() {
      return "OperatorConstruct";
    }
    
    @Override
    public GenericConstructReturn
      parseSpecificConstruct(miniTomParser parser) throws RecognitionException {
      
      csOperatorConstruct_return retval = parser.csOperatorConstruct();
      return new GenericConstructReturn(retval.tree, retval.marker);
    }
  }
  
  private static class ParseOperatorArrayConstruct extends GenericParseConstruct {
    
    private static final ParseOperatorArrayConstruct instance = new ParseOperatorArrayConstruct();
    
    public static ParserAction getInstance() {
      return instance;
    }
    
    private ParseOperatorArrayConstruct() {;}
    
    @Override
    public String getConstructName() {
      return "OperatorArrayConstruct";
    }
    
    @Override
    public GenericConstructReturn
      parseSpecificConstruct(miniTomParser parser) throws RecognitionException {
      
      csOperatorArrayConstruct_return retval = parser.csOperatorArrayConstruct();
      return new GenericConstructReturn(retval.tree, retval.marker);
    }
  }
  
  private static class ParseOperatorListConstruct extends GenericParseConstruct {
    
    private static final ParseOperatorListConstruct instance = new ParseOperatorListConstruct();
    
    public static ParserAction getInstance() {
      return instance;
    }
    
    private ParseOperatorListConstruct() {;}
    
    @Override
    public String getConstructName() {
      return "OperatorListConstruct";
    }
    
    @Override
    public GenericConstructReturn
      parseSpecificConstruct(miniTomParser parser) throws RecognitionException {
    
      csOperatorListConstruct_return retval = parser.csOperatorListConstruct();
      return new GenericConstructReturn(retval.tree, retval.marker);
    }
    
  }

  private static class ParseTypetermConstruct extends GenericParseConstruct {

    private static final ParseTypetermConstruct instance = new ParseTypetermConstruct();
    
    public  static ParserAction getInstance() {
      return instance;
    }

    private ParseTypetermConstruct() {;}

    @Override
    public String getConstructName() {
      return "TypetermConstruct";
    }

    @Override
    public GenericConstructReturn
      parseSpecificConstruct(miniTomParser parser) throws RecognitionException {

        csTypetermConstruct_return retval = parser.csTypetermConstruct();
        return new GenericConstructReturn(retval.tree, retval.marker);
      }
  }

  private static class PackHostContent extends ParserAction {
    
    private static final PackHostContent instance = new PackHostContent();
    
    private PackHostContent() {;}
    
    public static PackHostContent getInstance() {
    	return instance;
    }

    @Override
	  public void doAction(CharStream input, HostBlockBuilder hostBlockBuilder,
        Tree tree, StreamAnalyst analyst, TomStreamManager streamManager,
        OptionManager optionManager) {

      if(!hostBlockBuilder.isEmpty()) {
        tree.addChild(hostBlockBuilder.getHostBlock());
        hostBlockBuilder.reset();
      }
    }
  }

  private static class ParseMetaQuoteConstruct extends ParserAction {

    private static ParseMetaQuoteConstruct instance = new ParseMetaQuoteConstruct();
   
    public static ParserAction getInstance() {
      return instance;
    }
    
    private ParseMetaQuoteConstruct() {;}
    
    @Override
    public void doAction(CharStream input, HostBlockBuilder hostBlockBuilder,
        Tree tree, StreamAnalyst analyst, TomStreamManager streamManager,
        OptionManager optionManager) {
      
      // remove beginning of the keyword from hostBlockBuilder
      hostBlockBuilder.removeLastChars(analyst.getOffsetAtMatch());
      
      PACK_HOST_CONTENT.doAction(input, hostBlockBuilder, tree, analyst,
          streamManager, optionManager);
      
      // consume last char of the keyword
      input.consume();
      
      // consume (and save) all metaquote content
      StringBuilder metaquoteContentBuilder = new StringBuilder();
      while(analyst.readChar(input)) {
        if(input.LA(1)==CharStream.EOF) {
          System.err.println("Unexpected EndOfFile"); //TODO handle nicely
          return;
        }
        metaquoteContentBuilder.append((char)input.LA(1));
        input.consume();
      }
      
      // build nodes to add to tree
      String metaquoteContent = metaquoteContentBuilder.
                    substring(0, metaquoteContentBuilder.length()-1-(analyst.getOffsetAtMatch()+1)); //XXX work only because this delimited sequence is symetric
      
      CommonTreeAdaptor adaptor = new CommonTreeAdaptor();
      
      Tree child = (Tree) adaptor.becomeRoot((Tree)adaptor.create(miniTomParser.Cst_MetaQuoteConstruct, "CsMetaQuoteConsruct"),(Tree) adaptor.nil());
      Tree strTree = (Tree) adaptor.becomeRoot((Tree)adaptor.create(miniTomParser.HOSTBLOCK, metaquoteContent), (Tree) adaptor.nil());
            
      child.addChild(strTree);
      tree.addChild(child);
    }
    
  }
 
  private static class GenericConstructReturn {
    
    private Tree tree;
    private int marker;
    
    public GenericConstructReturn(Tree tree, int marker) {
      this.tree = tree;
      this.marker = marker;
    }
    
    public Tree getTree() {
      return tree;
    }
    
    public int getMarker() {
      return marker;
    }
  }

}
