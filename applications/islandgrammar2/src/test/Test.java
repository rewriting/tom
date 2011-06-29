package test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import newparser.HostParser;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class Test {
  
  public static void main(String args[]) throws IOException{
    
    //private boolean debug = false;
    boolean resetResultFiles = false;
    //private boolean initResultFiles = false;
    boolean drawTrees = true;
    
    int testsCount = 0;
    int failureCount = 0;
    
    // == messages ==
    final String Err_missing_tom = "Can't access environment variable TOM_HOME.";
    //private final String Err_missing_dot = "Can't access dot/graphviz, won't draw trees.";
    final String Err_ls_error = "Can't acces input folder or 'ls' doesn't exists on your system";
    // == /messages =
    
    
    // first ensure that TOM_HOME is accessible
    if(System.getenv("TOM_HOME")==null){
      System.err.println(Err_missing_tom);
      System.exit(1);
   }
   
   final String TOM_HOME = System.getenv("TOM_HOME")+"/"; 
   final String RLT_PATH = "../../applications/islandgrammar2/";
   final String INPUT_DIR = TOM_HOME+RLT_PATH+"test-material/";

   // === make a list of files to test 
   Process ls;
   String testedFiles[] = new String[0];
try {
    ls = Runtime.getRuntime().exec("ls "+INPUT_DIR);
  
  
   BufferedInputStream ls_stdout = new BufferedInputStream(ls.getInputStream());
   
   String ls_res = "";
   int ls_char;
   
    while((ls_char=ls_stdout.read())!=-1){
       ls_res+=(char)ls_char;
     }
   
   testedFiles = ls_res.split("\n");
} catch (IOException e) {
  System.err.println(Err_ls_error);
  System.exit(1);
}
   // ===

  // make a list with .input files
  List<String> inputFilesList = new ArrayList<String>();
  
  for(int i=0; i<testedFiles.length; i++){
    if(testedFiles[i].endsWith(".input")){
      inputFilesList.add(INPUT_DIR+testedFiles[i]);
    }
  }
  
  // test all
  for(String inputFileName : inputFilesList){
    
    String resultFileName = inputFileName.replace(".input", ".result");
    
    CharStream input = new ANTLRFileStream(inputFileName);
    Tree tree = new HostParser().parse(input);
    
    String actualTreeAsString = ((CommonTree)tree).toStringTree();
    String expectedTreeAsString = readFile(resultFileName);
    
    
    testsCount++;
    if(!actualTreeAsString.equals(expectedTreeAsString)){
      failureCount++;
      System.out.println("Test FAILURE : " + inputFileName);
    }
    
    // if reseting resultFiles, do so
    if(resetResultFiles){
      writeToFile(resultFileName, actualTreeAsString);
    }
    
    // if drawing trees, do so
    if(drawTrees){
      futil.ANTLRTreeDrawer.draw(resultFileName+".png", tree);
    }
    
  }
  
  // print overall test result
  System.out.println(
      "======================================== \n"
      + ( (failureCount==0)? "Tests PASSED :" : "Test FAILED :" )
      + " "+(testsCount-failureCount)+"/"+testsCount+"\n"
      + "======================================== \n"
      );
  if(failureCount!=0){
    System.exit(1);
  }
  
  }
  
  private static String readFile(String fileName){
    String res = "";
    BufferedReader reader;
  try {
    reader = new BufferedReader(new FileReader(new File(fileName)));
   
    String line;
    while((line = reader.readLine())!=null){
      res+=line+"\n";
    }
    
    // remove last "\n"
    res = res.substring(0, res.length()-1);
    
  } catch (FileNotFoundException e) {
    return null;
  } catch (IOException e) {
    return null;
  }
    return res;
  }
  
  private static void writeToFile(String fileName, String content) throws IOException{
    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName)));
    writer.write(content);
    writer.flush();
    writer.close();
  }
}
