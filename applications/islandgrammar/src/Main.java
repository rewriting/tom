import org.antlr.runtime.*;
import java.io.File;
import java.io.FileInputStream;


public class Main {

  public static void main(String[] args) {
    HParser hostParser = new HParser(args[0]);
    CharStream tomStream = hostParser.getNextTom();
    GParser guestParser = new GParser(tomStream);
    guestParser.print();
    for(String s : hostParser.getHostTokens()) {
      System.out.println("Un bloc host !");
      System.out.println(s);
      System.out.println("C'est tout pour aujourd'hui");
    }
//    guestParser = new GParser(hostParser.getNextTom());
//    guestParser.print();
  }

}

