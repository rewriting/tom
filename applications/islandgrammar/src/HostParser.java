
import org.antlr.runtime.*;

public class HostParser {

    private CharStream input;

    private boolean ready;/* whether one of the tokenNames has been found */
    private int matchedConstruct;/* which one */
    private StringBuffer read;/* a memory to store characters read before asserting whether they're host content or not */
    private StringBuffer hostContent;/* the characters that haven't been parsed*/
    private boolean found;/* this one remembers if something was found during 'take' operation */

/* to look for a keyword, add it to this array, then configure the parser it should trigger in the function parserMap */
    public static final String[] tokenNames = new String[] {
      "%match", "%op"
    };
    private int[] states;/* the index of current character in each keyword */

    public HostParser(CharStream input) {
        this.input = input;
        hostContent = new StringBuffer();
        read = new StringBuffer();
        states = new int[tokenNames.length];
        for(int i = 0; i < tokenNames.length ; i++) {
          states[i] = 0;
        }
        found = false;
        matchedConstruct = -1;
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
          take(read);
/* the acts as an intermediate : it takes the character read, performs the needed operations on the various patterns watched */
          if(ready) {
            ready = false;
            parserMap(matchedConstruct);
          }
/* and then gives hostContent what it should get : usually a single char (read), but sometimes several if the begining of one of the watched keyword exists in the host code */
          else {
            hostContent.append(getRead());
          }
          input.consume();
        }
    }


    public void take(char c) {
      found = true;
      for(int i = 0; i < tokenNames.length; i++) {
        if(tokenNames[i].charAt(states[i]) == c) {
          states[i]++;
          found = true;
          if(states[i] == tokenNames[i].length()) {
            ready = true;
            matchedConstruct = i;
            states[i] = 0;
          }
        }
        else {
          states[i] = 0;
        }
      }
      if(!found) {/* no matching character found among the tokenNames */
        read.append(c);
      }
    }

    public String getRead() {
      String result = "";
      if(!found) {
        result = read.toString();
        read = new StringBuffer();
      }
      return result;
    }


    public String getCode() {
        return hostContent.toString();
    }
}
