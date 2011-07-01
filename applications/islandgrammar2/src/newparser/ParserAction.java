package newparser;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.Tree;

import debug.HostParserDebugger;

import streamanalysis.DelimitedSequenceDetector;
import streamanalysis.StreamAnalyst;

/**
 * 
 * @see HostParser
 */
public abstract class ParserAction {

  // static fields with cool ParserActions
  public static final SkipDelimitedSequence SKIP_DELIMITED_SEQUENCE= SkipDelimitedSequence.getInstance();
  public static final ParseMatchConstruct PARSE_MATCH_CONSTRUCT= ParseMatchConstruct.getInstance();
  public static final PackHostContent PACK_HOST_CONTENT= PackHostContent.getInstance();
  
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
		  				StringBuffer hostCharsBuffer,
		  				Tree tree,
		  				StreamAnalyst analyst);

  
  public static class SkipDelimitedSequence extends ParserAction {
    
    private static final SkipDelimitedSequence instance = new SkipDelimitedSequence();
    
    private SkipDelimitedSequence() {}

    public static SkipDelimitedSequence getInstance() {
      return instance;
    }

    @Override
    public void doAction(CharStream input, StringBuffer hostCharsBuffer, Tree tree,
        StreamAnalyst analyst) {

      if(!(analyst instanceof DelimitedSequenceDetector)){
        throw new RuntimeException("Bad StreamAnalyst implementation");
      }

      // skip one char, this is last char of opening sequence
      // forget to keep it would make analyst state wrong if
      // delimiter sequence is only one char long
      hostCharsBuffer.append((char)input.LA(1));
      input.consume();

      while(analyst.readChar(input)) { // readChar update and return "foundness" value
        if(input.LA(1)==CharStream.EOF) {
          System.err.println("Unexpected EndOfFile"); //TODO handle nicely
          return;
        }

        hostCharsBuffer.append((char)input.LA(1)); // save host code char for later use
        input.consume();
      }
    }
  }
  
  public static class ParseMatchConstruct extends ParserAction {

    private static final ParseMatchConstruct instance = new ParseMatchConstruct();

    private ParseMatchConstruct(){;}
    
    public static ParseMatchConstruct getInstance() {
      return instance;
    }
    
    @Override
    public void doAction(CharStream input, StringBuffer hostCharsBuffer,
        Tree tree, StreamAnalyst analyst) {
    
      // remove "%matc" (without 'h') from hostCharsBuffer
      hostCharsBuffer.setLength(hostCharsBuffer.length()-analyst.getOffsetAtMatch());	
    	
      PACK_HOST_CONTENT.doAction(input, hostCharsBuffer, tree, analyst);
      
      // consume 'h' of %match
      input.consume();
                   
      try {
      miniTomLexer lexer = new miniTomLexer(input);
      
// XXX DEBUG ===
if(HostParserDebugger.isOn()){
HostParserDebugger.getInstance()
 .debugNewCall(lexer.getClassDesc(), input, "matchconstruct");
}
// == /DEBUG ===
      
      CommonTokenStream tokenStream = new CommonTokenStream(lexer);
      miniTomParser parser = new miniTomParser(tokenStream);
      
      miniTomParser.matchConstruct_return
        matchconstructReturnedValue = parser.matchConstruct();
      
      tree.addChild((Tree)matchconstructReturnedValue.getTree());
      
      // allow action to return with a "clean" input state
      // (input.LA(1) is char after '}')
      input.rewind(matchconstructReturnedValue.marker);
      
// XXX DEBUG ===
if(HostParserDebugger.isOn()){
HostParserDebugger.getInstance()
 .debugReturnedCall(lexer.getClassDesc(), input, "matchconstruct");
}
// == /DEBUG ===
      
      } catch(Exception e) {
        // XXX poorly handled exception
        e.printStackTrace();
      }
    }
    
  }
  
  public static class PackHostContent extends ParserAction {
    
    private static final PackHostContent instance = new PackHostContent();
    
    private PackHostContent(){;}
    
    public static PackHostContent getInstance(){
    	return instance;
    }

	@Override
	public void doAction(CharStream input, StringBuffer hostCharsBuffer,
			Tree tree, StreamAnalyst analyst) {
		
	  if(hostCharsBuffer.length()>0){
	    CommonTreeAdaptor adaptor = new CommonTreeAdaptor();
		    
		// XXX is it REALLY the clearest way to do that ?
		Tree child = (Tree) adaptor.nil();
		child = (Tree)adaptor.becomeRoot((Tree)adaptor.create(miniTomParser.HOSTBLOCK, "HOSTBLOCK"), child);
		
		// XXX maybe define a type for gom's Strings ?
		Tree hContent = (Tree) adaptor.nil();
		hContent = (Tree)adaptor.becomeRoot((Tree)adaptor.create(miniTomParser.HostBlockContent, hostCharsBuffer.toString()), hContent);
		
		child.addChild(hContent);
		
	    tree.addChild(child);
	  }
	  
	  hostCharsBuffer.setLength(0);
	}
  }
}
