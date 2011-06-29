package tester;
import java.io.File;

public class Main {

  public static void main(String args[]){
    Tester tester = new Tester(new File("test"));
    //tester.initResultsFiles();
    tester.testPrintResultAndDrawTrees();
  }
  
}
