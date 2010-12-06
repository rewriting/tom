import org.antlr.*;
import java.io.File;
import java.io.FileInputStream;


public class Main {

  public static void main(String[] args) {

    HParser hostParser = new HParser(args[0]);
    FileInputStream tomStream = hostParser.getNextTom();
    GParser guestParser = new GParser(tomStream);
    guestParser.print();
    
  }

}

