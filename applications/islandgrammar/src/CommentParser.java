import org.antlr.runtime.tree.*;
import org.antlr.runtime.*;
import java.lang.StringBuffer;

public class CommentParser
{
  private CharStream input;
  private StringBuffer stringBuffer;

  public CommentParser(CharStream cs)
  {
    input = cs;
    stringBuffer = new StringBuffer();
  }

  public Tree oneLine() {
    boolean insideComment = true;
    while (insideComment) {
      char read = (char) input.LA(1);
      switch(read) {
        case ((char) -1):
          insideComment = false;
          break;
        case '\n':
          insideComment = false;
          break;
        case '\r':
          if ((char) input.LA(2) == '\n') {
            input.consume();
            insideComment = false;
          } else {
            stringBuffer.append('\r');
          }
          break;
        default:stringBuffer.append(read);
      }
      input.consume();
    }
    return new CommonTree(new CommonToken(1,"(comment:"+stringBuffer.toString()+")"));
  }

  public Tree regular() throws Exception {
    boolean insideComment = true;
    while (insideComment) {
      char read = (char) input.LA(1);
      switch(read) {
        case ((char) -1):
          throw new Exception("EOF reached before end of comment");
        case '*':
            if ((char) input.LA(2) == '/') {
              input.consume();
              insideComment = false;
            }
          break;
        default:stringBuffer.append(read);
      }
      input.consume();
    }
    return new CommonTree(new CommonToken(1,"(comment:"+stringBuffer.toString()+")"));
  }
  
}

