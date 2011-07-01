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

/**
 * 
 * @see ParserAction
 */
public class HostParser {
	
	private StreamAnalyst stopCondition;
	private Map<StreamAnalyst, ParserAction> actionsMapping;
	
	/**
	 * Create a HostParser untill stopCondition find what it's looking for.
	 * Defautl stopCondition is EOF.
	 * @param stopCondition
	 */
	public HostParser(StreamAnalyst stopCondition) {
		
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
	
	public HostParser() {
		this(new KeywordDetector(""+(char)CharStream.EOF));
	}
	
	public Tree parse(CharStream input) {
		
	  // this string buffer will contain chars read from input that are part of
	  // "Host Language" code. They will be added to the tree as late as possible
	  // using ParserAction.packHostContent.
	  StringBuffer hostCharsBuffer = new StringBuffer();
	  Tree tree;
		
		CommonTreeAdaptor adaptor = new CommonTreeAdaptor();
		// XXX maybe there is a simpler way...
	  tree = (Tree) adaptor.nil();
	  tree = (Tree) adaptor.becomeRoot((Tree)adaptor.create(miniTomParser.CsProgram, "CsProgram"), tree);
		
		while(! stopCondition.readChar(input)) { // readChar() updates internal state
												 // and return match()
			
			// update state of all analysts
      //
      StreamAnalyst recognized = null;
			for(StreamAnalyst analyst : actionsMapping.keySet()) {
				boolean matched = analyst.readChar(input);
        if(matched) {
          if(recognized!=null) {
            // can't be two recognized keywords
            throw new RuntimeException("Unconsistent HostParser state");
          }
          recognized = analyst;
        }
			}

			if(recognized==null) {
				hostCharsBuffer.append((char)input.LA(1));
				input.consume();
			} else {
				ParserAction action = actionsMapping.get(recognized);
				action.doAction(input, hostCharsBuffer, tree, recognized);
				// doAction is allowed to modify its parameters
				// especially, doAction can consume chars from input
				// so every StreamAnalyst needs to start fresh.
				resetAllAnalysts();
			}
			
		} // while
		
		ParserAction.PACK_HOST_CONTENT.doAction(input, hostCharsBuffer, tree, null);
		
		return tree;
	}
	
	private void resetAllAnalysts() {
		stopCondition.reset();
		for(StreamAnalyst analyst : actionsMapping.keySet()) {
			analyst.reset();
		}
	}
	
  // === DEBUG =========================== //
  public String getClassDesc() {
    return "HostParser";
  }

}
