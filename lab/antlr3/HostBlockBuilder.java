package tom.engine.parser.antlr3;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;

import static tom.engine.parser.antlr3.TreeFactory.*;
import static tom.engine.parser.antlr3.miniTomParser.*;
/**
 * Stores chars, remembers first and last char position, allows to create
 */
public class HostBlockBuilder {

  private boolean isEmpty = true;
  private int firstCharLine    = -1;
  private int firstCharColumn = -1;
  private int lastCharLine    = -1;
  private int lastCharColumn  = -1;
  private String sourceFileName = null;

  private StringBuilder stringBuilder = new StringBuilder();

  public HostBlockBuilder() {};

  public void  reset(){
    isEmpty = true;
    firstCharLine    = -1;
    firstCharColumn = -1;
    lastCharLine    = -1;
    lastCharColumn  = -1;
    stringBuilder.setLength(0);
  }

  public void readOneChar(CharStream input) {
    lastCharLine  = input.getLine();
    lastCharColumn = input.getCharPositionInLine();
    stringBuilder.append((char)input.LA(1));

    if(isEmpty) {
      isEmpty = false;
      firstCharLine   = lastCharLine;
      firstCharColumn = lastCharColumn;
      sourceFileName = input.getSourceName();
    }
  }

  // XXX this method should update lastCharLine/Column
  public void removeLastChars(int charsToRemove) {
    stringBuilder.setLength(stringBuilder.length()-charsToRemove);
  }

  public Tree getHostBlock() {
    return makeTree(HOSTBLOCK, "HostBlock",
                    makeOptions(sourceFileName, firstCharLine,
                                firstCharColumn, lastCharLine, lastCharColumn),
                    makeTree(NodeString, getText()));
  }

  public String getText() {
    return stringBuilder.toString();
  }

  public boolean isEmpty() {
    return isEmpty;
  }
}

