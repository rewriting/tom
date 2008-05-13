package lemu;

import java.io.*;
import java.util.*;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import lemu.sequents.types.*;


public class IO {

  %include { sequents/sequents.tom }

  private static char[] append(char[] buf, int index, char c) {
    if (index >= buf.length) {
      char[] newbuf = new char[Math.max(index+1,buf.length*2)]; 
      System.arraycopy(buf, 0, newbuf, 0, buf.length);
      buf = newbuf;
    }
    buf[index] = c;
    return buf;
  }

  private static class InputRes {
    public InputRes(char[] buf, int size) {
      this.buf = buf; this.size = size;
    }
    public char[] buf = null;
    public int size = 0;
  }

  // handling user input
  private static InputRes getInput() throws IOException {
    char[] buf = new char[1024];
    int index = 0;
    char c = 0;
    char last = 0;

    while (c != '.' && c != -1) {
      last = c;
      c = (char) stream.read();
      append(buf,index++,c);
      if (last == '/' && c == '/') {
        while(c != '\n') {
          last = c;
          c = (char) stream.read();
          append(buf,index++,c);
        }
      } else if (last == '/' && c == '*') {
        while( ! (last == '*' && c == '/')) {
          last = c;
          c = (char) stream.read();
          append(buf,index++,c);
        }
      }
    }
    return new InputRes(buf,index);
  }

  // IO functions 

  private static InputStream stream = System.in;

  public static void setStream(InputStream newStream) {
    stream = newStream;
  }

  public static Prop getProp() throws RecognitionException, IOException {
    InputRes input = getInput();
    CharStream cinput = new ANTLRStringStream(input.buf,input.size);
    SeqLexer lex = new SeqLexer(cinput);
    CommonTokenStream tokens = new CommonTokenStream(lex);
    SeqParser parser = new SeqParser(tokens);
    SeqParser.start1_return root = parser.start1();
    CommonTreeNodeStream nodes = new CommonTreeNodeStream((org.antlr.runtime.tree.Tree)root.tree);
    SeqWalker walker = new SeqWalker(nodes);
    return walker.pred();
  }

  public static Term getTerm() throws RecognitionException, IOException {
    InputRes input = getInput();
    CharStream cinput = new ANTLRStringStream(input.buf,input.size);
    SeqLexer lex = new SeqLexer(cinput);
    CommonTokenStream tokens = new CommonTokenStream(lex);
    SeqParser parser = new SeqParser(tokens);
    SeqParser.start2_return root = parser.start2();
    CommonTreeNodeStream nodes = new CommonTreeNodeStream((org.antlr.runtime.tree.Tree)root.tree);
    SeqWalker walker = new SeqWalker(nodes);
    return walker.term();
  }

  public static Command getCommand() throws RecognitionException, IOException {
    InputRes input = getInput();
    CharStream cinput = new ANTLRStringStream(input.buf,input.size);
    SeqLexer lex = new SeqLexer(cinput);
    CommonTokenStream tokens = new CommonTokenStream(lex);
    SeqParser parser = new SeqParser(tokens);
    SeqParser.command_return root = parser.command();
    CommonTreeNodeStream nodes = new CommonTreeNodeStream((org.antlr.runtime.tree.Tree)root.tree);
    SeqWalker walker = new SeqWalker(nodes);
    return walker.command();
  }

  public static ProofCommand getProofCommand() throws RecognitionException, IOException {
    InputRes input = getInput();
    CharStream cinput = new ANTLRStringStream(input.buf,input.size);
    SeqLexer lex = new SeqLexer(cinput);
    CommonTokenStream tokens = new CommonTokenStream(lex);
    SeqParser parser = new SeqParser(tokens);
    SeqParser.proofcommand_return root = parser.proofcommand();
    CommonTreeNodeStream nodes = new CommonTreeNodeStream((org.antlr.runtime.tree.Tree)root.tree);
    SeqWalker walker = new SeqWalker(nodes);
    return walker.proofcommand();
  }

  // FIXME : get rid of "ident" in parser and use lexer directly
  public static String getIdent() throws RecognitionException, IOException {
    InputRes input = getInput();
    CharStream cinput = new ANTLRStringStream(input.buf,input.size);
    SeqLexer lex = new SeqLexer(cinput);
    CommonTokenStream tokens = new CommonTokenStream(lex);
    SeqParser parser = new SeqParser(tokens);
    SeqParser.ident_return root = parser.ident();
    CommonTreeNodeStream nodes = new CommonTreeNodeStream((org.antlr.runtime.tree.Tree)root.tree);
    SeqWalker walker = new SeqWalker(nodes);
    return walker.ident();
  }

  public static Sig getSig() throws RecognitionException, IOException {
    InputRes input = getInput();
    CharStream cinput = new ANTLRStringStream(input.buf,input.size);
    SeqLexer lex = new SeqLexer(cinput);
    CommonTokenStream tokens = new CommonTokenStream(lex);
    SeqParser parser = new SeqParser(tokens);
    SeqParser.rule1_return root = parser.rule1();
    CommonTreeNodeStream nodes = new CommonTreeNodeStream((org.antlr.runtime.tree.Tree)root.tree);
    SeqWalker walker = new SeqWalker(nodes);
    return walker.rule1();
  }

  public static int getInt() throws RecognitionException, IOException {
    InputRes input = getInput();
    try {
      String s = new String(input.buf, 0, input.size-1);
      Scanner i = new Scanner(s);
      return i.nextInt();
    } 
    catch (InputMismatchException e) { throw new RecognitionException(); }
  }

}

