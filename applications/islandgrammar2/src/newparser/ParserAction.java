package newparser;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.ClassicToken;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.Tree;

import antlr.CommonToken;
import antlr.Token;

import streamanalysis.DelimitedSequenceDetector;
import streamanalysis.StreamAnalyst;

public abstract class ParserAction{

  // static fields with cool ParserActions
  public static final SkipDelimitedSequence SKIP_DELIMITED_SEQUENCE = new SkipDelimitedSequence();
  public static final ParseMatchConstruct PARSE_MATCH_CONSTRUCT = new ParseMatchConstruct();
  
  
  
  /**
   * Implementations of ParserAction.doAction should check
   * runtime type of analyst.
   * 
   * doAction should be called right after analyst found something.
   * char that made it match should accessible using input.LA(1)
   * 
   * if matched keyword is more that 1 char long, previous chars
   * are at the end of hostCharBuffer.
   * 
   * doAction will terminate in such way that you don't need to
   * call input.consume(). 
   * 
   * @param input
   * @param hostCharBuffer
   * @param tree
   * @param analyst
   */
  public abstract void doAction(CharStream input,
		  				StringBuffer hostCharsBuffer,
		  				Tree tree,
		  				StreamAnalyst analyst);

  
  public static class SkipDelimitedSequence extends ParserAction{
	
	// not instanciable
	private SkipDelimitedSequence(){;}
	
	public SkipDelimitedSequence getInstance(){
		return ParserAction.SKIP_DELIMITED_SEQUENCE;
	}
	
	@Override
	public void doAction(CharStream input, StringBuffer hostCharsBuffer, Tree tree,
			StreamAnalyst analyst) {

		if(!(analyst instanceof DelimitedSequenceDetector)){
			throw new RuntimeException("Bad StreamAnalyst implementation");
		}
		
		// skip one char, this is last char of opening sequence
		// forget to kip it would make analyst state wrong if
		// delimiter sequence is only one char long
		hostCharsBuffer.append((char)input.LA(1));
		input.consume();
		
		while(analyst.readChar(input)){ // readChar update and return "foundness" value
	        hostCharsBuffer.append((char)input.LA(1)); // save host code char for later use
	        input.consume();
	    }
	}
  }
  
  public static class ParseMatchConstruct extends ParserAction{

    // not instanciable
    private ParseMatchConstruct(){;}
    
    public ParseMatchConstruct getInstance(){
      return ParserAction.PARSE_MATCH_CONSTRUCT;
    }
    
    @Override
    public void doAction(CharStream input, StringBuffer hostCharsBuffer,
        Tree tree, StreamAnalyst analyst) {
    
      packHostContent(hostCharsBuffer, tree);
      
      // consume 'h' of %match
      input.consume();
      
      try{
      miniTomLexer lexer = new miniTomLexer(input);
      CommonTokenStream tokenStream = new CommonTokenStream(lexer);
      miniTomParser parser = new miniTomParser(tokenStream);
      tree.addChild((Tree)parser.matchconstruct().getTree());
      }catch(Exception e){
        // XXX poorly handled exception
        e.printStackTrace();
      }
    }
    
  }
  
  private static void packHostContent(StringBuffer hostCharsBuffer,
        Tree tree) {
    
    CommonTreeAdaptor adaptor = new CommonTreeAdaptor();
    
    // XXX is it REALLY the clearest way to do that ?
    Tree child = (Tree) adaptor.nil();
    child = (Tree)adaptor.becomeRoot((Tree)adaptor.create(miniTomParser.HOSTBLOCK, hostCharsBuffer.toString()), child);
    
    tree.addChild(child);

  }
  
}
