package xrho;

import java.io.*;

import aterm.pure.SingletonFactory;

import xrho.rhoterm.*;
import xrho.rhoterm.types.*;

public class Main {
  public static void main(String[] args) throws FileNotFoundException {
    File file = new File(args[0]);

    DataInputStream input = new DataInputStream(new FileInputStream(file));

    // attach lexer to the input stream
    RhoLexer lexer = new RhoLexer(input); // Create parser attached to lexer
    RhoParser parser = new RhoParser(lexer);
    // start up the parser by calling the rule
    // at which you want to begin parsing. parser.rhoterm(); 
    try {
      RTerm term = parser.program();
      System.out.println(term);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
