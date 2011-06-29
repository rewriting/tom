package tester;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.DOTTreeGenerator;
import org.antlr.runtime.tree.Tree;


public class Tester {

  private List<TestFile> TestFileList= new ArrayList<TestFile>();
  private File testedFile;
  
  public Tester(File file){
    if(!file.exists()){
      throw new IllegalArgumentException("File should exists");
    }
    if(!file.canRead()) {
      throw new IllegalArgumentException("File should be readable");
    }
    
    testedFile = file.getAbsoluteFile();
    
    // create TestFile instances for this file if it is not a folder
    // or for every file we can get recursively from here
    if(testedFile.isFile()){
      try {
        TestFileList.add(new TestFile(file));
      } catch (FormatException e) {
        System.err.println("Can't test : "+testedFile.getAbsolutePath()+"\n"+e);
      }
    }else
    if(testedFile.isDirectory()){  
      List<File> flist = listFilesRecursively(testedFile);
      
      for(File f : flist){
        try {
          TestFileList.add(new TestFile(f));
        } catch (FormatException e) {
          System.err.println("Can't test : "+f.getAbsolutePath()+"\n"+e);
        }
      }
      
    }else{
      throw new IllegalArgumentException();
    }
  }
  
  public void testAndPrintResult(){
    testAndOtherThings(false);
  }
  
  public void testPrintResultAndDrawTrees(){
    testAndOtherThings(true);
  }
  
  private void testAndOtherThings(boolean drawTree){
    int testCount = 0;
    int successCount = 0;
    StringBuilder failedTests = new StringBuilder();
    
    for(TestFile tFile : TestFileList){
      TestParser parser = TestParser.getInstance(tFile.getParserType());
      parser.parse(tFile.getActualContent());
      
      
      // test success
        String expectedResult = readFile(tFile.getOutputFile());
        String actualResult = parser.getParsingResultAsString();
        
      
        boolean success = parser.isParsingASuccess() && expectedResult.equals(actualResult);
       
        if(success){
          successCount++;
          
          if(drawTree){
            if(!drawTree(tFile.getImgFile(), parser.getTree())){
              System.out.println("Couldn't draw Tree");
            }
          }
          
        }else{
          failedTests.append(tFile.getPath()).append("\n");
        }
        
        testCount++;
      
      // print
      System.out.println("=====");
      System.out.println("testing : "+tFile.getPath()+"\ndescription : "+tFile.getDescription()+"\nresult : "+((success)?"[SUCCESS]":"[FAILURE]"));
      if(!parser.isParsingASuccess()){
        System.out.println("> Parsing failed");
      }else{
        if(!success){
          System.out.println("> Result different from expected");
          System.out.println("> Expected : \n"+expectedResult);
          System.out.println("> Actual : \n"+actualResult);
        }
      }
      System.out.println("=====");
    }
    
    // print abstract
    System.out.println("=====================================================");
    System.out.println("== failed tests list : ==============================");
    System.out.print(failedTests.toString());
    System.out.println("=====================================================");
    System.out.println("== overall test result : ============================");
    System.out.println(((testCount==successCount)?"[SUCCESS]":"[FAILURE]")+" : "+successCount+"/"+testCount);
    System.out.println("=====================================================");
  }
  
  public void initResultsFiles(){
    
    for(TestFile tFile : TestFileList){
      TestParser parser = TestParser.getInstance(tFile.getParserType());
      parser.parse(tFile.getActualContent());
      
      if(! parser.isParsingASuccess()){
        System.out.println("Parsing failed, can't init output for : "+ tFile.getPath());
      }else{
        if(!writeToFile(tFile.getOutputFile(), parser.getParsingResultAsString())){
          System.out.println("Writing failed, can't init output for : "+tFile.getPath());
        }
      }
    }
    
    System.out.println("Outputs initialisation done");
    
  }
  
  private List<File> listFilesRecursively(File file){
    if(!file.exists() || !file.canRead() || !file.isDirectory()) {
      throw new IllegalArgumentException();
    }
    
    List<File> res = new ArrayList<File>();
    
    File list[] = file.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File arg0, String arg1) {
        return !arg1.startsWith("_") && !arg1.startsWith(".");
      }
    });
    
    for(File f : list){
      if(f.isFile()){
        res.add(f);
      }else
      if(f.isDirectory()){
        res.addAll(listFilesRecursively(f));
      }else{
        throw new RuntimeException("Should not append");
      }
    }
    
    return res;
  }
  
  
  private static String readFile(File f){
    String res = "";
    BufferedReader reader;
  try {
    reader = new BufferedReader(new FileReader(f));
   
    String line;
    while((line = reader.readLine())!=null){
      res+=line+"\n";
    }
    
    // remove last "\n"
    if(res.length()>0){
    res = res.substring(0, res.length()-1);
    }
    
  } catch (FileNotFoundException e) {
    return null;
  } catch (IOException e) {
    return null;
  }
    return res;
  }
  
  private static boolean writeToFile(File f, String content){

   try {
    BufferedWriter writer = new BufferedWriter(new FileWriter(f));
    writer.write(content);
    writer.flush();
    writer.close();
   } catch (IOException e) {
      return false;
   }
   
   return true;
   
  }
  
  private static boolean drawTree(File pngFile, Tree tree){
    try{
    File dotFile = File.createTempFile("islandGrammar2Tester", ".dot");
    dotFile.deleteOnExit();
    
    BufferedWriter dotWriter =
      new BufferedWriter(new FileWriter(dotFile));
    
    DOTTreeGenerator dotTreeGen = new DOTTreeGenerator();
    String dotContent = dotTreeGen.toDOT(tree).toString();
    
    dotWriter.write(dotContent);
    dotWriter.flush();
    dotWriter.close();
    
    Runtime rt = Runtime.getRuntime();
    Process pr = rt.exec("dot -Tpng "+dotFile.getAbsolutePath()+" -o "+pngFile.getAbsolutePath());
    pr.waitFor();
    
    return true;
    
    }catch(IOException e){
      return false;
    } catch (InterruptedException e) {
      return false;
    }
  }
  
  // ===== main and related ===================================================
  
  private static final String usage =
    "java tester.Tester [-d] [-init] fileOrFolderName\n" +
    "\n" +
    "-draw : DrawTrees, will generate png files representing generated trees. Use dot/graphviz.\n" +
    "-init : save currently generated tree and make them new expected result\n";
  
  public static void main(String args[]){
    
    boolean drawing = false;
    boolean initializing = false;
    String fileName = null;
    
    // arguments treatment
    int i;
    for(i=0; i<args.length; i++){
      if(args[i].startsWith("-")){
        if(args[i].startsWith("-draw")){
          drawing=true;
        }else
        if(args[i].startsWith("-init")){
          initializing = true;
        }else{
          System.out.println("Unknown argument : '"+args[i]);
          System.out.println(usage);
          System.exit(1);
        }
      }else{
        fileName = args[i];
        break;
      }
    }
    
    if(i<args.length-1){
      System.out.println("Invalid arguments after '"+args[i]+"'. Filename must be unique and in last position.");
      System.out.println(usage);
      System.exit(1);
    }
    
    if(fileName==null){
      System.out.println("You must specify a file name");
      System.out.println(usage);
      System.exit(1);
    }
    // arguments treated 

    // check filemane
    File subject = new File(fileName);
    Tester tester = new Tester(subject);
    
    if(initializing){
      tester.initResultsFiles();
    }
    
    if(drawing){
      tester.testPrintResultAndDrawTrees();
    }else{
      tester.testAndPrintResult();
    }
    
  }
  
}
