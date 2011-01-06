import org.antlr.runtime.*;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.LinkedList;

public class HParser {

  private File file;
  private InputStream inputFile;
  private LinkedList<String> tokens = new LinkedList<String>();

  public HParser(String name) {
    try {
      file = new File(name);
      inputFile = new FileInputStream(file);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public CharStream getNextTom() {
    int state = 0;
    String pattern = "%match";
    StringBuffer guest = new StringBuffer();
    StringBuffer host = new StringBuffer();
    char c = '\000';
    while(state != -1) {
      try {
      c = (char) inputFile.read();
      } catch (Exception e){
        System.out.println("Impossible de lire à partir du fichier source");
        e.printStackTrace();
        System.exit(0);
      }
      if(c == -1) {
        System.out.println("Fin du fichier atteinte.");
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
    return new ANTLRStringStream(getInsideContent(inputFile));
//    return inputFile;
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
