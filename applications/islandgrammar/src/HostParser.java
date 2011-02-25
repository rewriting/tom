import org.antlr.runtime.tree.*;
import org.antlr.runtime.*;

public class HostParser {

    private CharStream input;

    private boolean ready;/* whether one of the tokenNames has been found */
    private int matchedConstruct;/* which one */
    private StringBuffer savedContent;/* a memory to store characters read before asserting whether they're host content or not */
    private StringBuffer hostContent;/* the characters that haven't been parsed*/
    private boolean found;/* this one remembers if something was found during last 'take' operation */
    private Tree arbre;
/* to look for a keyword, add it to this array, then configure the parser it should trigger in the function parserMap */
    public static final String[] tokenNames = new String[] {
      "%match", "%op", "//", "/*"
    };
    private int[] states;/* the index of current character in each keyword */

    public HostParser(CharStream input) {
        Token name=new CommonToken(1,"Papyrus");
        arbre=new CommonTree(name);
        this.input = input;
        hostContent = new StringBuffer();
        savedContent = new StringBuffer();
        states = new int[tokenNames.length];
        for(int i = 0; i < tokenNames.length ; i++) {
          states[i] = 0;
        }
        found = false;
        matchedConstruct = -1;
    }

    private void parserMap(int i) {
      /* prepare a tree for the host content */
      CommonToken tokenizedHostContent = new CommonToken(1,("|\n("+hostContent.toString()+")\n"));
      CommonTree treedHostContent = new CommonTree(tokenizedHostContent);
      arbre.addChild(treedHostContent);
      /* forget the savedContent, which is currently one of the tokens */
      savedContent.setLength(0);
      Tree result = new CommonTree();
      switch(i)
      {
        case 0:
          try {
            miniTomLexer lexer = new miniTomLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            miniTomParser parser0 = new miniTomParser(tokens);
            result = (Tree) parser0.program().getTree();
          } catch (Exception e) {}
          break;
        case 1: break;
        case 2:
          CommentParser parser2 = new CommentParser(input);
          result = parser2.oneLine();
          break;
        case 3:
          try {
            CommentParser parser3 = new CommentParser(input);
            result = parser3.regular();
          } catch (Exception e) {
            e.printStackTrace();
          }
          break;
        default : System.out.println(i + " : this sould not happen");
      }
      arbre.addChild(result);

    }

    public Tree parse() {
        while (true) {
          char read = (char) input.LA(1);
          if (read == (char) -1) {
            break;
          }
          take(read);
          if(!found) {
            hostContent.append(savedContent);
            savedContent.setLength(0);
          }
          input.consume();
          if(ready) {
            ready = false;   
            parserMap(matchedConstruct);
            hostContent.setLength(0);;
          }
        }
        return arbre;
    }


    public void take(char c) {
      found = false;
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
      savedContent.append(c);
    }

}
