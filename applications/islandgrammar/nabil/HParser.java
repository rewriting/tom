import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.LinkedList;

public class HParser {

  private File file;
  private FileInputStream inputFile;
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

  public FileInputStream getNextTom() {
    int state = 0;
    String pattern = "%match";
    StringBuffer read = new StringBuffer();
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
        continue;
      }
      if(c == pattern.charAt(state)) {
        state++;
      }
      else {
        state = 0;
        read.append(c);
      }
      if(state == pattern.length())
      {
        state = -1;
      }
    }
    tokens.add(read.toString());
    return inputFile;
  }

  public List<String> getHostTokens () {
    return tokens;
  }
}
