package tom.engine.parser.antlr3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.BaseTree;

import tom.platform.OptionManager;
import tom.engine.TomStreamManager;
import tom.engine.exception.TomIncludeException;

import static tom.engine.parser.antlr3.TreeFactory.*;
import static tom.engine.parser.antlr3.miniTomParser.*;
import tom.engine.parser.antlr3.streamanalysis.*;
/**
 * 
 * @see ParserAction
 */
public class HostParser {

  private StreamAnalyst stopCondition;
  private Map<StreamAnalyst, ParserAction> actionsMapping;

  private OptionManager optionManager;
  private TomStreamManager streamManager;

  private HashSet<String> includedFileSet = null;
  private HashSet<String> alreadyParsedFileSet = null;
  public boolean testIncludedFile(String fileName, HashSet<String> fileSet) {
    // !(true) if the set did not already contain the specified element.
    return !fileSet.add(fileName);
  }

  /**
   * Create a HostParser untill stopCondition find what it's looking for.
   * Defautl stopCondition is EOF.
   * @param stopCondition
   */
 // streamManager/optionManager... used only for PARSER_GOM_... need cleaning
  public HostParser(TomStreamManager streamManager, OptionManager optionManager,
                    HashSet<String> includedFiles, HashSet<String> alreadyParsedFiles,
                    StreamAnalyst stopCondition) {

    this.optionManager = optionManager;
    this.streamManager = streamManager;
    
    this.includedFileSet = new HashSet<String>(includedFiles);
    this.alreadyParsedFileSet = alreadyParsedFiles;
    //following test for the case of stopCondition==NegativeImbricationDetector
    if(streamManager!=null) {
      testIncludedFile(streamManager.getInputFileName(), includedFileSet);
    }
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
      ParserAction.SKIP_DELIMITED_SEQUENCE_EOF_TOLERANT);
    // multi line comments
    actionsMapping.put(
      new DelimitedSequenceDetector("/*", "*/"),
      ParserAction.SKIP_DELIMITED_SEQUENCE);
    // %match
    actionsMapping.put(
      new KeywordDetector("%match"),
      ParserAction.PARSE_MATCH_CONSTRUCT);
    // %strategy
    actionsMapping.put(
      new KeywordDetector("%strategy "),
      ParserAction.PARSE_STRATEGY_CONSTRUCT);
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
    // bqTerms
    actionsMapping.put(
      new KeywordDetector("`"),
      ParserAction.PARSE_BQTERM_CONSTRUCT);
  }
  
  public HostParser(TomStreamManager streamManager, OptionManager optionManager) {
     this(streamManager, optionManager, new HashSet<String>(), new 
         HashSet<String>(), new KeywordDetector(""+(char)CharStream.EOF));
  }
 
  public HostParser(TomStreamManager streamManager, OptionManager optionManager,
                    StreamAnalyst stopCondition) {
     this(streamManager, optionManager, new HashSet<String>(),new HashSet<String>(), stopCondition);
   }

  public HostParser(TomStreamManager streamManager, OptionManager optionManager,
                    HashSet<String> includedFiles, 
                    HashSet<String> alreadyParsedFiles) {
    this(streamManager, optionManager, includedFiles, alreadyParsedFiles,
        new KeywordDetector(""+(char)CharStream.EOF));
  }

  public CommonTree parseProgram(CharStream input){
    return makeRootTree(Cst_Program, "Cst_Program", parseBlockList(input));
  }
  
  public CommonTree parseBlockList(CharStream input){
    return parse(input);
  }

  public List<CommonTree> parseListOfBlock(CharStream input){
    return ((CommonTree)parse(input)).getChildren();
  }

  private CommonTree parse(CharStream input) {

    if(input.getSourceName()==null){
      //XXX DEBUG
      System.out.println("DEBUG : CharStream's sourceName not set");
    }

    HostBlockBuilder hostBlockBuilder = new HostBlockBuilder();
    CommonTree tree = makeTree(ConcCstBlock, "ConcCstBlock");

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
        try {
          action.doAction(input, hostBlockBuilder, tree, recognized,
              getStreamManager(), getOptionManager(), includedFileSet,
              alreadyParsedFileSet);
        } catch (Exception e) {
          e.printStackTrace();
        }
        // doAction is allowed to modify its parameters
        // especially, doAction can consume chars from input
        // so every StreamAnalyst needs to start fresh.
        resetAllAnalysts();
      }

    } // end while

    try {
      ParserAction.PACK_HOST_CONTENT.doAction(input, hostBlockBuilder, tree,
          null, getStreamManager(), getOptionManager(), includedFileSet,
          alreadyParsedFileSet);
    } catch (Exception e) {
      e.printStackTrace();
    }

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
