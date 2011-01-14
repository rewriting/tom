
import org.antlr.runtime.*;

public class HostParser {

    private CharStream input;
    private StringBuffer hostContent;
    private ConstructWatcher watcher;

    public HostParser(CharStream input) {
        this.input = input;
        hostContent = new StringBuffer();
        String[] constructs = {"%match", "%op"};
        watcher = new ConstructWatcher(constructs);
/* to look for another keyword, add it to the previous array, then configure the parser it should trigger in the following function */
    }

    private void parserMap(int i) {
      switch(i) {
        case 0:System.out.println("On a trouvé un match, chef !");break;
        case 1:System.out.println("On a trouvé un op, chef !");break;
        default:break;
      }
    }

    public void parse() {
        while (true) {
          char read = (char) input.LA(1);
          if (read == (char) -1) {
            System.out.println("Fin du fichier");
            break;
          }
          watcher.take(read);
/* the watcher acts as an intermediate : it takes the character read, performs the needed operations on the various patterns watched */
          if(watcher.ready()) {
            parserMap(watcher.getConstruct());
          }
/* and then gives hostContent what it should get : usually a single char (read), but sometimes several if the begining of one of the watched keyword exists in the host code */
          else {
            hostContent.append(watcher.getRead());
          }
          input.consume();
        }
    }

    public String getCode() {
        return hostContent.toString();
    }
}
