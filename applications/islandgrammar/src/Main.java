import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;


public class Main {

  public static void main(String[] args) {

    try {
      ANTLRFileStream file = new ANTLRFileStream(args[0]);
      HostParser hostParser = new HostParser(file);
      Tree parsed = hostParser.parse();
      System.out.println(parsed.toStringTree());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}

