import org.antlr.runtime.*;

public class HostParser {

  private CharStream input;

  public HostParser(CharStream input) {
    this.input = input;
  }

  public void parse() {
    char truc = (char) input.LT(1);// reads the first character of the given CharStream (illustrates that we'll need to use .LT())
    System.out.println("I've found "+truc);
  }

}
