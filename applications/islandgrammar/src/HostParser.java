import org.antlr.runtime.tree.*;
import org.antlr.runtime.*;

public class HostParser {

/*
 * Definition of the keyword classes used in the rest
 */

  public class ExitKeyword extends Keyword {
    public ExitKeyword() {
      this.pattern = String.valueOf((char) CharStream.EOF);
    }
    public ExitKeyword(String pattern) {
      this.pattern = pattern;
    }
    protected void action() throws org.antlr.runtime.RecognitionException {
      packHostContent();
      alive = false;
      hostContent.setLength(0);
    }
  }

  public class MatchConstruct extends Keyword {
    public MatchConstruct() {
      this.pattern = "%match";
    }
    protected void action() throws org.antlr.runtime.RecognitionException {
      packHostContent();
      miniTomLexer lexer = new miniTomLexer(input);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      miniTomParser parser = new miniTomParser(tokens);
      arbre.addChild((Tree) parser.matchconstruct().getTree());
    }
  }

  public class TypeTermConstruct extends Keyword {
    public TypeTermConstruct() {
      this.pattern = "%typeterm";
    }
    protected void action() throws org.antlr.runtime.RecognitionException {
      packHostContent();
      miniTomLexer lexer = new miniTomLexer(input);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      miniTomParser parser = new miniTomParser(tokens);
      arbre.addChild((Tree) parser.typetermconstruct().getTree());
    }
  }

  public class Comment extends Keyword {
    public Comment() {
      this.pattern = "/*";
    }
    protected void action() throws org.antlr.runtime.RecognitionException {
      packHostContent();
      CommentLexer lexer = new CommentLexer(input);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      CommentParser parser = new CommentParser(tokens);
      arbre.addChild((Tree) parser.regular().getTree());
    }
  }

  public class OLComment extends Keyword {
    public OLComment() {
      this.pattern = "//";
    }
    protected void action() throws org.antlr.runtime.RecognitionException {
      packHostContent();
      CommentLexer lexer = new CommentLexer(input, true);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      CommentParser parser = new CommentParser(tokens);
      arbre.addChild((Tree) parser.oneline().getTree());
    }
  }

/*
** Fields definition
*/

  private CharStream input;

  private boolean alive;/* whether the getTree() routine should go on or not */

  private StringBuffer savedContent;/* a memory to store characters read before asserting whether they're host content or not */
  private StringBuffer hostContent;/* the characters that remain uninterpreted */
  
  private Tree arbre;
  private Keyword[] Keywords;/* This array will contain the keywords that trigger various events */

/*
** Constructors
*/

  /* default one : HostParser stops at the end of file */
  public HostParser(CharStream input) {
    this.input = input;

    alive = true;

    savedContent = new StringBuffer();
    hostContent = new StringBuffer();

    arbre = new CommonTree(new CommonToken(1,"HostBlock"));
    Keywords = new Keyword[] {
      (new ExitKeyword()),
      (new MatchConstruct()),
      (new Comment()),
      (new OLComment()),
      (new TypeTermConstruct())
    };
  }

  /* another one : HostParser stops at StopToken */
  public HostParser(CharStream input, String StopToken) {
    this(input);
    Keywords[0] = new ExitKeyword(StopToken);
  }

/*
** Main routine : read the input and produce corresponding Tree
*/
  public Tree getTree() {
    while (alive) {
      char read = (char) input.LA(1);
      boolean found = false;
      System.out.println(read);
      for(int i = 0; i < Keywords.length; i++) {
/* Warning ! Logic is lazy in Java ! had we written 'found || Keywords…', Keywords wouldn't be fed 'read' when one was ok */
        found =  Keywords[i].take(read) || found;
      }
      savedContent.append(read);
      input.consume();
      if(!found) {
        hostContent.append(savedContent);
        savedContent.setLength(0);
      }
      for(int i = 0; i < Keywords.length; i++) {
        if(Keywords[i].isReady()) {
          try {
            Keywords[i].action();
            Keywords[i].reset();
          } catch (Exception e) {e.printStackTrace();}
        }
      }
    }
    return arbre;
  }

/*
** Useful routines called from Keyword classes
*/

  private void packHostContent() {
    savedContent.setLength(0);
    if(hostContent.length() > 0) {
      CommonToken tokenizedHostContent = new CommonToken(1,("<"+hostContent.toString()+">"));
      CommonTree treedHostContent = new CommonTree(tokenizedHostContent);
      arbre.addChild(treedHostContent);
      hostContent.setLength(0);
    }
  }

}

