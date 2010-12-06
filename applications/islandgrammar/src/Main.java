import java.io.File;
import java.io.FileInputStream;


public class Main {

  public static void main(String[] args) {

    HParser hostParser = new HParser(args[0]);
    InputStream tomStream = hostParser.getNextTom();

    
  }

}

