package tom.engine.newparser.parser;

import tom.engine.newparser.parser.miniTomParser.*;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.Tree;

import tom.engine.newparser.debug.HostParserDebugger;

import tom.engine.newparser.streamanalysis.DelimitedSequenceDetector;
import tom.engine.newparser.streamanalysis.StreamAnalyst;

/**
 * 
 * @see HostParser
 */
public abstract class ParserAction {

  // static fields with cool ParserActions
  public static final ParserAction  SKIP_DELIMITED_SEQUENCE = SkipDelimitedSequence.getInstance();
  public static final ParserAction  PACK_HOST_CONTENT       = PackHostContent.getInstance();
  
  // actions using miniTomParser
  public static final ParserAction  PARSE_MATCH_CONSTRUCT           = ParseMatchConstruct.getInstance();
  public static final ParserAction  PARSE_OPERATOR_CONSTRUCT        = ParseOperatorConstruct.getInstance();
  public static final ParserAction  PARSE_OPERATOR_LIST_CONSTRUCT   = ParseOperatorListConstruct.getInstance();
  public static final ParserAction  PARSE_OPERATOR_ARRAY_CONSTRUCT  = ParseOperatorArrayConstruct.getInstance();
  public static final ParserAction  PARSE_TYPETERM_CONSTRUCT        = ParseTypetermConstruct.getInstance();
  public static final ParserAction  PARSE_INCLUDE_CONSTRUCT         = ParseIncludeConstruct.getInstance();
  
  // independant action 
  public static final ParserAction  PARSE_METAQUOTE_CONSTRUCT       = ParseMetaQuoteConstruct.getInstance();
  
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
   */
  public abstract void doAction(CharStream input,
		  				HostBlockBuilder hostBlockBuilder,
		  				Tree tree,
		  				StreamAnalyst analyst);
  
  private static class SkipDelimitedSequence extends ParserAction {
    
    private static final SkipDelimitedSequence instance = new SkipDelimitedSequence();
    
    private SkipDelimitedSequence() {}

    public static SkipDelimitedSequence getInstance() {
      return instance;
    }

    @Override
    public void doAction(CharStream input, HostBlockBuilder hostBlockBuilder,
        Tree tree, StreamAnalyst analyst) {

      if(!(analyst instanceof DelimitedSequenceDetector)){
        throw new RuntimeException("Bad StreamAnalyst implementation");
      }

      // skip one char, this is last char of opening sequence
      // forget to keep it would make analyst state wrong if
      // delimiter sequence is only one char long
      hostBlockBuilder.readOneChar(input);
      input.consume();

      while(analyst.readChar(input)) { // readChar update and return "foundness" value
        if(input.LA(1)==CharStream.EOF) {
          System.err.println("Unexpected EndOfFile"); //TODO handle nicely
          return;
        }

        hostBlockBuilder.readOneChar(input); // save host code char for later use
        input.consume();
      }
    }
  }
  
  private static abstract class GenericParseConstruct extends ParserAction {
    
    @Override
    public void doAction(CharStream input, HostBlockBuilder hostBlockBuilder,
        Tree tree, StreamAnalyst analyst) {

      // remove beginning of the keyword from hostBlockBuilder
      // ("%matc" if keyword is "%match")
      hostBlockBuilder.removeLastChars(analyst.getOffsetAtMatch());
      
      PACK_HOST_CONTENT.doAction(input, hostBlockBuilder, tree, analyst);
      
      // consume last chat of the keyword
      // ("h" if keyword is "%match")
      input.consume();
      
      try {
        miniTomLexer lexer = new miniTomLexer(input);
        
  // XXX DEBUG ===
  if(HostParserDebugger.isOn()){
  HostParserDebugger.getInstance()
   .debugNewCall(lexer.getClassDesc(), input, getConstructName());
  }
  // == /DEBUG ===
        
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        miniTomParser parser = new miniTomParser(tokenStream);
        
        GenericConstruct_return retval = parseSpecificConstruct(parser);
        
        tree.addChild((Tree)retval.getTree());
        
        // allow action to return with a "clean" input state
        // (input.LA(1) is char after '}')
        input.rewind(retval.getMarker());
        
  // XXX DEBUG ===
  if(HostParserDebugger.isOn()){
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
    
    public abstract GenericConstruct_return
      parseSpecificConstruct(miniTomParser parser) throws RecognitionException;
    
  }

  private static class ParseMatchConstruct extends GenericParseConstruct {

    private static final ParseMatchConstruct instance = new ParseMatchConstruct();
    
    public static ParserAction getInstance(){return instance;}
    
    private ParseMatchConstruct() {;}
    
    @Override
    public String getConstructName() {
      return "MatchConstruct";
    }

    @Override
    public GenericConstruct_return
      parseSpecificConstruct(miniTomParser parser) throws RecognitionException {
      
      matchConstruct_return retval = parser.matchConstruct();
      return new GenericConstruct_return(retval.tree, retval.marker);
    }
    
  }
  
  private static class ParseIncludeConstruct extends GenericParseConstruct{
    
    private static final ParseIncludeConstruct instance = new ParseIncludeConstruct();
    
    public static ParserAction getInstance(){return instance;}
    
    private ParseIncludeConstruct() {;}
    
    @Override
    public String getConstructName(){
      return "IncludeConstruct";
    }
    
    @Override
    public GenericConstruct_return
      parseSpecificConstruct(miniTomParser parser) throws RecognitionException {
      
      csIncludeConstruct_return retval = parser.csIncludeConstruct();
      return new GenericConstruct_return(retval.tree, retval.marker);
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
    public GenericConstruct_return
      parseSpecificConstruct(miniTomParser parser) throws RecognitionException {
      
      csOperatorConstruct_return retval = parser.csOperatorConstruct();
      return new GenericConstruct_return(retval.tree, retval.marker);
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
    public GenericConstruct_return
      parseSpecificConstruct(miniTomParser parser) throws RecognitionException {
      
      csOperatorArrayConstruct_return retval = parser.csOperatorArrayConstruct();
      return new GenericConstruct_return(retval.tree, retval.marker);
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
    public GenericConstruct_return
      parseSpecificConstruct(miniTomParser parser) throws RecognitionException {
    
      csOperatorListConstruct_return retval = parser.csOperatorListConstruct();
      return new GenericConstruct_return(retval.tree, retval.marker);
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
    public GenericConstruct_return
      parseSpecificConstruct(miniTomParser parser) throws RecognitionException {

        csTypetermConstruct_return retval = parser.csTypetermConstruct();
        return new GenericConstruct_return(retval.tree, retval.marker);
      }
  }

  private static class PackHostContent extends ParserAction {
    
    private static final PackHostContent instance = new PackHostContent();
    
    private PackHostContent(){;}
    
    public static PackHostContent getInstance(){
    	return instance;
    }

	@Override
	public void doAction(CharStream input, HostBlockBuilder hostBlockBuilder,
			Tree tree, StreamAnalyst analyst) {
		
	  if(!hostBlockBuilder.isEmpty()){
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
        Tree tree, StreamAnalyst analyst) {
      
      // remove beginning of the keyword from hostBlockBuilder
      hostBlockBuilder.removeLastChars(analyst.getOffsetAtMatch());
      
      PACK_HOST_CONTENT.doAction(input, hostBlockBuilder, tree, analyst);
      
      // consume last char of the keyword
      input.consume();
      
      // consume (and save) all metaquote content
      StringBuilder metaquoteContentBuilder = new StringBuilder();
      while(analyst.readChar(input)){
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
      
      Tree child = (Tree) adaptor.becomeRoot((Tree)adaptor.create(miniTomParser.CsMetaQuoteConstruct, "CsMetaQuoteConsruct"),(Tree) adaptor.nil());
      Tree strTree = (Tree) adaptor.becomeRoot((Tree)adaptor.create(miniTomParser.HOSTBLOCK, metaquoteContent), (Tree) adaptor.nil());
            
      child.addChild(strTree);
      tree.addChild(child);
    }
    
  }
  
  private static class GenericConstruct_return{
    
    private Tree tree;
    private int marker;
    
    public GenericConstruct_return(Tree tree, int marker){
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
