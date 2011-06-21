package newparser;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.Tree;

import streamanalysis.DelimitedSequenceDetector;
import streamanalysis.EOLdetector;
import streamanalysis.KeywordDetector;
import streamanalysis.StreamAnalyst;

public class HostParser {
	
	private StreamAnalyst stopCondition;
	private Map<StreamAnalyst, ParserAction> actionsMapping;
	
	/**
	 * Create a HostParser untill stopCondition find what it's looking for.
	 * Defautl stopCondition is EOF.
	 * @param stopCondition
	 */
	public HostParser(StreamAnalyst stopCondition){
		
		this.stopCondition = stopCondition;
		
		actionsMapping = new HashMap<StreamAnalyst, ParserAction>();
		
		// create analysts and associated actions
		  // host strings
		actionsMapping.put(
		  new DelimitedSequenceDetector("\"", "\"", '\\'),
		  ParserAction.SKIP_DELIMITED_SEQUENCE);
		  // host chars
		actionsMapping.put(
		  new DelimitedSequenceDetector("\'", "\'", '\\'),
		  ParserAction.SKIP_DELIMITED_SEQUENCE);
		  // one line comments
		actionsMapping.put(
		  new DelimitedSequenceDetector(new KeywordDetector("//"), new EOLdetector()),
		  ParserAction.SKIP_DELIMITED_SEQUENCE);
		  // multi line comments
		actionsMapping.put(
		  new DelimitedSequenceDetector("/*", "*/"),
		  ParserAction.SKIP_DELIMITED_SEQUENCE);
		  // match construct
		actionsMapping.put(
		  new KeywordDetector("%match"),
		  ParserAction.PARSE_MATCH_CONSTRUCT);
	}
	
	public HostParser(){
		this(new KeywordDetector(""+(char)CharStream.EOF));
	}
	
	public Tree parse(CharStream input){
		
	  StringBuffer hostCharsBuffer = new StringBuffer();
		Tree tree;
		
		CommonTreeAdaptor adaptor = new CommonTreeAdaptor();
		// XXX maybe there is a simpler way...
	  tree = (Tree) adaptor.nil();
    tree = (Tree)adaptor.becomeRoot((Tree)adaptor.create(miniTomParser.HOSTBLOCK, "HostBlock"), tree);
		
		
		
		
		while(! stopCondition.readChar(input)){ // readChar() updates internal state
												// and return found()
			
			// update state of all analysts
			for(StreamAnalyst analyst : actionsMapping.keySet()){
				analyst.readChar(input);
			}

			int matchCount = matchingAnalystsCount();
			
			if(matchCount==0){
				hostCharsBuffer.append((char)input.LA(1));
				input.consume();
				
			} else if(matchCount==1){
				
				Map.Entry<StreamAnalyst, ParserAction> entry;
				ParserAction action;
				StreamAnalyst analyst;
				
				entry = getFirstMathingEntry();
				action = entry.getValue();
				analyst = entry.getKey();
				
				
				action.doAction(input, hostCharsBuffer, tree, analyst);
				
				// doAction is allowed to modify its parameters
				// especially doAction can consume chars from input
				// so every StreamAnalyst needs to start fresh.
				resetAllAnalysts();
				
			} else if(matchCount > 1){
				throw new RuntimeException("Unconsistent HostParser state");
			}
			
		} // while
		
		ParserAction.PACK_HOST_CONTENT.doAction(input, hostCharsBuffer, tree, null);
		
		return tree;
	}
	
	private void resetAllAnalysts(){
		stopCondition.reset();
		for(StreamAnalyst analyst : actionsMapping.keySet()){
			analyst.reset();
		}
	}
	
	private int matchingAnalystsCount(){
		int res = 0;
		
		for(StreamAnalyst analyst : actionsMapping.keySet()){
			if(analyst.match()) {
				res++;
			}
		}
		
		return res;
	}

  /**
   * 
   * @return null if no such entry
   */
  private Map.Entry<StreamAnalyst, ParserAction> getFirstMathingEntry() {
		
    for(Map.Entry<StreamAnalyst, ParserAction> entry : actionsMapping.entrySet()){
      if(entry.getKey().match()){
    	  return entry;
      }
    }
	
    return null;
  }
  
  // === DEBUG =========================== //
  public String getClassDesc(){
    return "HostParser";
  }

}
