import org.antlr.runtime.*;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.LinkedList;

public class HParser {

  private InputStream inputStream;
  private boolean finished;
  private LinkedList<String> tokens = new LinkedList<String>();

  public HParser(InputStream stream) {
    finished = false;
    inputStream = stream;
  }

  public boolean isDone() {
    return finished;
  }

  public ANTLRStringStream getNextTom() {
    int state = 0;
    String pattern = "%match";
    StringBuffer guest = new StringBuffer();
    StringBuffer host = new StringBuffer();
    char c = '\000';
    while(state != -1) {
      try {
      c = (char) inputStream.read();
      } catch (Exception e){
        System.out.println("Cannot read file");
        e.printStackTrace();
        System.exit(0);
      }
      if(c == (char) -1) {
        System.out.println("EOF reached");
        finished = true;
        state = -1;
      }
      else if(c == pattern.charAt(state)) {
        state++;
	guest.append(c);
      }
      else {
	if(state > 0) {
	  host.append(guest);
          guest = new StringBuffer();
        }
        host.append(c);
        state = 0;
      }
      if(state == pattern.length())
      {
        state = -1;
      }
    }
    tokens.add(host.toString());
    if (finished) {
/* no need to retrieve a Tom block, send an empty StringStream (that is, with an EOF) */
      return new ANTLRStringStream("\000");
    }
    else {
      return new ANTLRStringStream(getInsideContent(inputStream));
    }
  }

  private String getInsideContent(InputStream is) {
    StringBuffer buffer = new StringBuffer();
    char c = '\000';
    int level = 0;
    boolean inside = true;
    while(inside) {
      try {
        c = (char) is.read();
        buffer.append(c);
        if(c == '{') {
          level++;
        }
        else if(c == '}') {
          level--;
          if(level < 1) {
            inside = false;
          }
        }
        else if(c == (char) -1) {
          System.out.println("EOF reached while flying over Tom content");
          finished = true;
          inside = false;
        }
      }
      catch (Exception e) {
        inside = false;
      }
    }
    return buffer.toString();
  }

  public List<String> getHostTokens () {
    return tokens;
  }

}
