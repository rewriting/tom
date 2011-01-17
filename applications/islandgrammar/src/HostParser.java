import org.antlr.runtime.Token;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.tree.*;
import org.antlr.runtime.*;

public class HostParser {

    private CharStream input;

    private boolean ready;/* whether one of the tokenNames has been found */
    private int matchedConstruct;/* which one */
    private StringBuffer read;/* a memory to store characters read before asserting whether they're host content or not */
    private StringBuffer hostContent;/* the characters that haven't been parsed*/
    private boolean found;/* this one remembers if something was found during 'take' operation */
    private Tree arbre;
/* to look for a keyword, add it to this array, then configure the parser it should trigger in the function parserMap */
    public static final String[] tokenNames = new String[] {
      "%match", "%op"
    };
    private int[] states;/* the index of current character in each keyword */

    public HostParser(CharStream input) {
        Token name=new CommonToken(1,"Papyrus");
        arbre=new CommonTree(name);
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
      CommonToken hostcontenttokenized = new CommonToken(1,hostContent.toString());
      CommonTree hostcontenttree= new CommonTree(hostcontenttokenized);
     arbre.addChild(hostcontenttree);
    }

    public void parse() {
        while (true) {
          char read = (char) input.LA(1);
          if (read == (char) -1) {
            System.out.println("Fin du fichier");
      System.out.print(arbre.toStringTree());
            break;
          }
          take(read);
 hostContent.append(read);
          input.consume();
/* the acts as an intermediate : it takes the character read, performs the needed operations on the various patterns watched */
          if(ready) {
            ready = false;   
     parserMap(matchedConstruct);
    hostContent.setLength(0);;
          }
/* and then gives hostContent what it should get : usually a single char (read), but sometimes several if the begining of one of the watched keyword exists in the host code */          
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
