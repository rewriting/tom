package tom.engine.newparser.parser;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;

/**
 * Stores chars, remembers first and last char position, allows to create
 */
public class HostBlockBuilder {

  private boolean isEmpty = true;
  private int firstCharLine    = -1;
  private int firstCharColumn = -1;
  private int lastCharLine    = -1;
  private int lastCharColumn  = -1;

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
    }
  }

  // XXX this method should update lastCharLine/Column
  public void removeLastChars(int charsToRemove) {
    stringBuilder.setLength(stringBuilder.length()-charsToRemove);
  }

  public Tree getHostBlock() {
    CommonTreeAdaptor adaptor = new CommonTreeAdaptor();
    
    // XXX is it REALLY the clearest way to do that ?
		Tree hostBlock = (Tree) adaptor.nil();
		hostBlock = 
      (Tree)adaptor.becomeRoot(
          (Tree)adaptor.create(
            miniTomParser.HOSTBLOCK, "HOSTBLOCK"), hostBlock);
		
		Tree hContent = (Tree) adaptor.nil();
		hContent = 
      (Tree)adaptor.becomeRoot(
          (Tree)adaptor.create(0, stringBuilder.toString()), hContent);

    // Start TextPosition
    Tree startTextPosition = (Tree) adaptor.nil();
    startTextPosition = 
      (Tree)adaptor.becomeRoot(
          (Tree)adaptor.create(miniTomParser.CsTextPosition,"TextPosition"),
          startTextPosition);

    Tree startTextPositionLine = (Tree) adaptor.nil();
    startTextPositionLine =
      (Tree)adaptor.becomeRoot(
          (Tree)adaptor.create(0, ""+firstCharLine),
           startTextPositionLine);

    Tree startTextPositionColumn = (Tree) adaptor.nil();
    startTextPositionColumn =
      (Tree)adaptor.becomeRoot(
          (Tree)adaptor.create(0, ""+firstCharColumn),
          startTextPositionColumn);

    startTextPosition.addChild(startTextPositionLine);
    startTextPosition.addChild(startTextPositionColumn);

    // End TextPosition
    Tree endTextPosition = (Tree) adaptor.nil();
    endTextPosition = 
      (Tree)adaptor.becomeRoot(
          (Tree)adaptor.create(miniTomParser.CsTextPosition,"TextPosition"),
          endTextPosition);

    Tree endTextPositionLine = (Tree) adaptor.nil();
    endTextPositionLine =
      (Tree)adaptor.becomeRoot(
          (Tree)adaptor.create(0, ""+lastCharLine),
          endTextPositionLine);

    Tree endTextPositionColumn = (Tree) adaptor.nil();
    endTextPositionColumn =
      (Tree)adaptor.becomeRoot(
          (Tree)adaptor.create(0, ""+lastCharColumn),
          endTextPositionColumn);

    endTextPosition.addChild(endTextPositionLine);
    endTextPosition.addChild(endTextPositionColumn);

		hostBlock.addChild(hContent);
    hostBlock.addChild(startTextPosition);
    hostBlock.addChild(endTextPosition);

    return hostBlock;
  }

  public String getText() {
    return stringBuilder.toString();
  }

  public boolean isEmpty() {
    return isEmpty;
  }
}

