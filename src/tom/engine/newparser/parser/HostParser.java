package tom.engine.newparser.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.BaseTree;

import tom.platform.OptionManager;
import tom.engine.TomStreamManager;

import tom.engine.newparser.streamanalysis.DelimitedSequenceDetector;
import tom.engine.newparser.streamanalysis.EOLdetector;
import tom.engine.newparser.streamanalysis.KeywordDetector;
import tom.engine.newparser.streamanalysis.StreamAnalyst;

import static tom.engine.newparser.parser.miniTomLexer.*;
import static tom.engine.newparser.util.TreeFactory.*;
/**
 * 
 * @see ParserAction
 */
public class HostParser {
	
	private StreamAnalyst stopCondition;
	private Map<StreamAnalyst, ParserAction> actionsMapping;

  private OptionManager optionManager;
  private TomStreamManager streamManager;

	/**
	 * Create a HostParser untill stopCondition find what it's looking for.
	 * Defautl stopCondition is EOF.
	 * @param stopCondition
	 */
  // streamManager/optionManager... used only for PARSER_GOM_... need cleaning
	public HostParser(TomStreamManager streamManager, OptionManager optionManager,
      StreamAnalyst stopCondition) {
	
    this.optionManager = optionManager;
    this.streamManager = streamManager;

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
		  // %match
		actionsMapping.put(
		  new KeywordDetector("%match"),
		  ParserAction.PARSE_MATCH_CONSTRUCT);
		  // %op
		actionsMapping.put(
		  new KeywordDetector("%op "),
		  ParserAction.PARSE_OPERATOR_CONSTRUCT);
		  // %oparray
		actionsMapping.put(
		  new KeywordDetector("%oparray "),
		  ParserAction.PARSE_OPERATOR_ARRAY_CONSTRUCT);
		  // %oplist
		actionsMapping.put(
		  new KeywordDetector("%oplist "),
		  ParserAction.PARSE_OPERATOR_LIST_CONSTRUCT);
      // %typeterm
    actionsMapping.put(
      new KeywordDetector("%typeterm "),
      ParserAction.PARSE_TYPETERM_CONSTRUCT);
      // %[ ]%
    actionsMapping.put(
      new DelimitedSequenceDetector("%[", "]%"),
      ParserAction.PARSE_METAQUOTE_CONSTRUCT);
      // %include
    actionsMapping.put(
      new KeywordDetector("%include"),
      ParserAction.PARSE_INCLUDE_CONSTRUCT);
      // %gom
    actionsMapping.put(
      new KeywordDetector("%gom"),
      ParserAction.PARSE_GOM_CONSTRUCT);
	}
	
	public HostParser(TomStreamManager streamManager, OptionManager optionManager) {
		this(streamManager, optionManager, new KeywordDetector(""+(char)CharStream.EOF));
	}
	
	public Tree parseProgram(CharStream input){
    return makeRootTree(CsProgram, "CsProgram", parseBlockList(input));
	}
	
	public Tree parseBlockList(CharStream input){
	  return parse(input);
	}

  public List<Tree> parseListOfBlock(CharStream input){
    return ((BaseTree)parse(input)).getChildren();
  }

	private Tree parse(CharStream input) {

	  HostBlockBuilder hostBlockBuilder = new HostBlockBuilder();
    Tree tree = makeTree(CsBlockList, "CsBlockList");

		while(! stopCondition.readChar(input)) { 
                                    // readChar() updates internal state
												            // and return match()
			
			// update state of all analysts
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
			  hostBlockBuilder.readOneChar(input);	
				input.consume();
			} else {
				ParserAction action = actionsMapping.get(recognized);
				action.doAction(input, hostBlockBuilder, tree, recognized,
            getStreamManager(), getOptionManager());
				// doAction is allowed to modify its parameters
				// especially, doAction can consume chars from input
				// so every StreamAnalyst needs to start fresh.
				resetAllAnalysts();
			}
			
		} // end while
		
		ParserAction.PACK_HOST_CONTENT.doAction(input, hostBlockBuilder, tree,
        null, getStreamManager(), getOptionManager()) ;
		
		return tree;
	}

	private void resetAllAnalysts() {
		stopCondition.reset();
		for(StreamAnalyst analyst : actionsMapping.keySet()) {
			analyst.reset();
		}
	}

  //XXX this is synchronized in "old" parser, maybe it should be here to
  private OptionManager getOptionManager() {
    return optionManager;
  }

  //XXX this is synchronized in "old" parser, maybe it should be here to
  private TomStreamManager getStreamManager() {
    return streamManager;
  }

  // === DEBUG =========================== //
  public String getClassDesc() {
    return "HostParser";
  }

}
