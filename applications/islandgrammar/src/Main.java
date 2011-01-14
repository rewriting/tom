import org.antlr.runtime.*;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;


public class Main {

  public static void main(String[] args) {

    try {
      ANTLRFileStream file = new ANTLRFileStream(args[0]);
      HostParser hostParser = new HostParser(file);
      hostParser.parse();
      System.out.println(hostParser.getCode());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}

