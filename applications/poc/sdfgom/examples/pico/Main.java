package pico;

import java.io.*;
import java.lang.*;
import java.util.*;
import aterm.*;
import aterm.pure.PureFactory;
import aterm.pure.SingletonFactory;
import sdfgom.converter.MeptConverter;
import sdfgom.ImplodeMept;
import sdfgom.Options;
import org.kohsuke.args4j.*;

public class Main {
  private static PureFactory factory = SingletonFactory.getInstance();
  protected static Options options = new Options();

  public static void main(String[] args) {
    CmdLineParser optionParser = new CmdLineParser(options);
    optionParser.setUsageWidth(80);
    try {
      // parse the arguments.
      optionParser.parseArgument(args);
      if( options.help || options.h ) {
        throw new CmdLineException("Help");
      }
    } catch( CmdLineException e ) {
      // if there's a problem in the command line,
      // you'll get this exception. this will report
      // an error message.
      System.err.println(e.getMessage());
      System.err.println("java Main [options...] arguments ...");
      // print the list of available options
      optionParser.printUsage(System.err);
      System.err.println();
      return;
    }

    try {
      InputStream fileinput = System.in;
      if(options.in != null) {
        fileinput = new FileInputStream(options.in);
      }

      ATerm at = factory.readFromTextFile(fileinput);
      MeptConverter meptConverter = new MeptConverter();
      ImplodeMept imploder = new ImplodeMept();

      PicoTool picoTool = new PicoTool();

      ATerm ast = imploder.implode(at);
      System.out.println("ast = " + ast);
      String result = picoTool.convertFromProgram(ast);
      System.out.println("result = " + result);

    } catch (Exception e) {
      System.err.println("exception: " + e);
      e.printStackTrace();
      return;
    }

  }

} 
