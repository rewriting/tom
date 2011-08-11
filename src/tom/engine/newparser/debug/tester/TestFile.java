package tom.engine.newparser.debug.tester;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class TestFile {

  private File testedFile;
  
  private File imgFile;
  private File outputFile;
  
  private String description = "";
  private TestParser.Type parserType = TestParser.Type.JAVATOM;
  private String actualContent = "";
  
  public TestFile(File file) throws FormatException{
    if(!file.exists()) {
      throw new IllegalArgumentException("File should exists");
    }
    if(!file.canRead()) {
      throw new IllegalArgumentException("File should be readable");
    }
    if(file.isDirectory()) {
      throw new IllegalArgumentException("File should not be a directory");
    }
    
    try {
      testedFile = file.getCanonicalFile();
    } catch (Exception e) {
      throw new RuntimeException("This API s**** !", e);
    }
    
    // create imgFile Object (may create actual folder file)
    {
      File imgFolder = new File(testedFile.getParent()+"/_imgs");
      if(imgFolder.mkdir()){
        System.out.println("new folder created : \n    "+imgFolder.getPath());
      }
      
      imgFile = new File(imgFolder.getPath()+"/"+testedFile.getName()+".png");
      try {
        if(!imgFile.exists() && imgFile.createNewFile()){
          System.out.println("new (empty) file created : \n    "+imgFile.getPath());
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      
    }
    
    // create outputFile Object (may create actual folder and file)
    {
      File outputFolder = new File(testedFile.getParent()+"/_outputs");
      if(outputFolder.mkdir()){
        System.out.println("new folder created : \n    "+outputFolder.getPath());
      }
      
      outputFile = new File(outputFolder.getPath()+"/"+testedFile.getName()+".output");
      try {
        if(!outputFile.exists() && outputFile.createNewFile()){
          System.out.println("new (empty) file created : \n    "+imgFile.getPath());
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      
    }
    
//=== now preParse this file====================================================
{
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(testedFile));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(e);
    }
    
    String line ="";
    StringBuilder preHeaderContentBuilder = new StringBuilder();
    StringBuilder postHeaderContentBuilder = new StringBuilder();
    boolean hasEnteredHeader = false;
    boolean hasOuteredHeader = false;
    
    // default
    try {
      while((line=reader.readLine())!=null){
        if(!hasEnteredHeader){
          if(line.startsWith("#>")){
            hasEnteredHeader=true;
          }else{
            preHeaderContentBuilder.append(line+"\n");
          }
        }else
        if(hasEnteredHeader && !hasOuteredHeader){
          if(line.startsWith("#<")){
            hasOuteredHeader=true;
          }else{
            // description
            if(line.startsWith("#description")){
              line = line.replaceFirst("#description", "");
              line = line.trim();
              
              description = line;
            
            } else
            // inputType
            if(line.startsWith("#type")){
              line = line.replaceFirst("#type", "");
              line = line.trim().toLowerCase();
              
              if(line.equals("javatom")){
                
                parserType = TestParser.Type.JAVATOM;
                
              }else
              if(line.equals("tompattern")){
            	  
                parserType = TestParser.Type.TOMPATTERN;
                
              }else
              if(line.equals("tomconstraint")){ 
              
                parserType = TestParser.Type.TOMCONSTRAINT;
                
              }else{
                throw new FormatException("Unknown parser type :'"+line+"' ("+testedFile.getPath()+")");
              }
            }
          }
        }else
        // if hasEnteredHeader && hasOuteredHeader
        {
          postHeaderContentBuilder.append(line+"\n"); 
        }
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
    
    // remove last '\n'
    if(preHeaderContentBuilder.length()>0){
      preHeaderContentBuilder.deleteCharAt(preHeaderContentBuilder.length()-1);
    }
    if(postHeaderContentBuilder.length()>0){
      postHeaderContentBuilder.deleteCharAt(postHeaderContentBuilder.length()-1);
    }

    if(hasEnteredHeader){
      if(hasOuteredHeader){
        actualContent = postHeaderContentBuilder.toString();
      }else{
        throw new FormatException("Header is never closed ("+testedFile.getPath()+")");
      }
    }else{
       actualContent = preHeaderContentBuilder.toString();
    }
}
//=== preParsing Done ! ========================================================
    
  }// end constructor

  public String getDescription(){
    return description;
  }

  public String getPath(){
    return testedFile.getPath();
  }
  
  public TestParser.Type getParserType(){
    return parserType;
  }
  
  public String getActualContent(){
    return actualContent;
  }
  
  public String getActualContentOneLined(){
    String res = actualContent;
    res = res.replaceAll("\n", "\\\\n");
    res = res.replaceAll("\t", "\\\\t");
    res = res.replaceAll("\r", "\\\\r");
    return res;
  }
  
  public File getImgFile(){
    return imgFile;
  }
  
  public File getOutputFile(){
    return outputFile;
  }
}
