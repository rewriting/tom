import org.antlr.runtime.*;
import java.lang.StringBuffer;

public class HostParser {

  private CharStream input;
  private StringBuffer hostContent;

  public HostParser(CharStream input) {
    this.input = input;
    hostContent = new StringBuffer();
  }

  public void parse() {
    
    while(true) {
      char truc = (char) input.LA(1);
      if(truc == (char) -1) {
        break;
      }
      hostContent.append(truc);
      input.consume();
    }
  }

  public String getCode() {
    return hostContent.toString();
  }
}
