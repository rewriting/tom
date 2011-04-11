import org.antlr.runtime.tree.*;
import org.antlr.runtime.*;

public class HostParser {

    private CharStream input;
    /* whether one of the tokenNames has been found */
    private boolean ready;
    private boolean quit;
    private int matchedConstruct;/* which one */
    /* a memory to store characters read before asserting whether they're host
     * content or not */
    private StringBuffer savedContent;
    /* the characters that haven't been parsed */
    private StringBuffer hostContent;
    /* this one remembers if something was found during last 'take' operation */
    private boolean found;
    /* an alias to clarify the interpretation of LA(int i) method */
    private final char EndOfFile = (char) -1;
    private Tree arbre;
    /* This array will contain the keywords that trigger the calling to another
     * parser */
    private String[] Tokens;
    /* A keyword to watch for special instances (which are supposed to stop
     * after a certain keyword is encountered) */
    private String StopToken;
    /* index of current character in each keyword */
    private int[] states;

    public HostParser(CharStream input) {
      Token name = new CommonToken(1,"Papyrus");
      arbre = new CommonTree(name);
      this.input = input;
      hostContent = new StringBuffer();
      savedContent = new StringBuffer();
      Tokens = new String[] { "%match", "%op", "//", "/*" };
      /* the additional box will be used to store the state of the StopToken */
      states = new int[Tokens.length+1];
      /* Put the state of each keyword to 0 (no character read yet) */
      for(int i = 0; i < states.length ; i++) {
        states[i] = 0;
      }
      found = false;
      matchedConstruct = -1;
    }

    public HostParser(CharStream input, String StopToken) {
      this(input);
      this.StopToken = StopToken;
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
          } catch (Exception e) {e.printStackTrace();}
          break;
        case 1: break;
        case 2:
                try {
                  CommentLexer lexer = new CommentLexer(input, true);
                  CommonTokenStream tokens = new CommonTokenStream(lexer);
                  CommentParser parser = new CommentParser(tokens);
                  result = (Tree) parser.oneline().getTree();
                } catch (Exception e) {e.printStackTrace();}
                break;
        case 3:
                try {
                  CommentLexer lexer = new CommentLexer(input);
                  CommonTokenStream tokens = new CommonTokenStream(lexer);
                  CommentParser parser = new CommentParser(tokens);
                  result = (Tree) parser.regular().getTree();
                } catch (Exception e) {e.printStackTrace();}
                break;
        default : System.out.println("Error : pattern #"+i+" was found, but no method was declared in parserMap to handle it");
      }
      arbre.addChild(result);
    }

    public Tree getTree() {
      while (true) {
        char read = (char) input.LA(1);
        if (read == EndOfFile) {
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
        if(quit) {
          break;
        }
      }
      return arbre;
    }

    public void take(char c) {
      found = false;
      for(int i = 0; i < Tokens.length; i++) {
        if(Tokens[i].charAt(states[i]) == c) {
          states[i]++;
          found = true;
          if(states[i] == Tokens[i].length()) {
            ready = true;
            matchedConstruct = i;
            states[i] = 0;
          }
        }
        else {
          states[i] = 0;
        }
      }
      if(StopToken != null) {
        /* The same for the StopToken now */
        if(StopToken.charAt(states[states.length-1]) == c) {
          states[states.length-1]++;
          found = true;
          if(states[states.length-1] == StopToken.length()) {
            quit = true;
            states[states.length-1] = 0;
          }
        }
        else {
          states[states.length-1] = 0;
        }
      }
      savedContent.append(c);
    }

}
