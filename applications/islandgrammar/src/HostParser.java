import org.antlr.runtime.tree.*;
import org.antlr.runtime.*;

public class HostParser {

/*
 * Definition of the keyword classes used in the rest
 */

  public class Watcher extends Keyword {
    public Watcher(String pattern) {
      this.pattern = pattern;
    }

    public void action() {}
  }

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

/* Next one is a tool to use two Keywords at the same time with a 'level' to count the number of { and } for instance (but it can be used with any pattern you like) */
  public class DoubleKeyword extends Keyword {
    private Keyword openKeyword;
    private Keyword closeKeyword;
    private int level = 0;
    protected String pattern = null;

    public DoubleKeyword(Keyword openKeyword, Keyword closeKeyword) {
      this.openKeyword = openKeyword;
      this.closeKeyword = closeKeyword;
    }

    public boolean take(char c) {
      boolean oAnswer = openKeyword.take(c);
      boolean cAnswer = closeKeyword.take(c);
      matched = openKeyword.isReady() || closeKeyword.isReady();
      return oAnswer || cAnswer;
    }

    protected void action() throws org.antlr.runtime.RecognitionException {
      if(openKeyword.isReady()) {
        level++;
        openKeyword.reset();
      } else if (level > 0) {
        level--;
        closeKeyword.reset();
      } else {
        closeKeyword.action();
        closeKeyword.reset();
      }
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
  private Keyword[] keywords;/* This array will contain the keywords that trigger various events */

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
    keywords = new Keyword[] {
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
    keywords[0] = new ExitKeyword(StopToken);
  }

  public HostParser(CharStream input, String OpenToken, String CloseToken) {
    this(input);
    keywords[0] = new DoubleKeyword(new Watcher(OpenToken), new ExitKeyword(CloseToken));
  }

/*
** Main routine : read the input and produce corresponding Tree
*/
  public Tree getTree() {
    while (alive) {
      char read = (char) input.LA(1);
      boolean found = false;
//      System.out.println(read);
      for(Keyword k : keywords) {
/* Warning ! Logic is lazy in Java ! had we written 'found || keywords…', keywords left wouldn't be fed 'read' as soon as one of them was ok */
        found =  k.take(read) || found;
      }
      savedContent.append(read);
      input.consume();
      if(!found) {
        hostContent.append(savedContent);
        savedContent.setLength(0);
      }
      for(Keyword k : keywords) {
        if(k.isReady()) {
          try {
            k.action();
            k.reset();
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

