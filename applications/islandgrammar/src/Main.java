import org.antlr.runtime.*;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;


public class Main {

  public static void main(String[] args) {

    try {
      File file = new File(args[0]);
      InputStream inputFile = new FileInputStream(file);
      HParser hostParser = new HParser(inputFile);
      while(!hostParser.isDone()) {
        System.out.println("Et un tour de plus");
        ANTLRStringStream tomStream = hostParser.getNextTom();
        if (tomStream.size() != 1) {
          GParser guestParser = new GParser(tomStream);
          guestParser.print();
        }
        else {
          System.out.println(tomStream.size());
        }
      }
      System.out.println("Et maintenant les blocs de host");
      for(String s : hostParser.getHostTokens()) {
        System.out.println("Un bloc host !");
        System.out.println(s);
        System.out.println("C'est tout pour aujourd'hui");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}

