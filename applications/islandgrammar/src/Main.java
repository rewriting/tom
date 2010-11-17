//TO COMPLETE
package ....;
import ....;

public class MainNewTom {

  public static void main(String[] args) {
    try {
      InputStream input;

      if(args.length!=0) {
        File inputFile = new File(args[0]);
        input=new FileInputStream(inputFile);
      } else {
        input=System.in;
      }

//TO COMPLETE
//will look like that : 
//lexer = new ....
//tokens = new ....(lexer)
//parser = new ....(tokens)

    } catch (Exception e) {
      //System.err.println("exception: " + e);
      e.printStackTrace();
    }

  }
}
