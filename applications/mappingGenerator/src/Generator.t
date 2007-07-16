import java.io.BufferedWriter;
import java.io.*;


public class Generator {
  
  //TODO - the includes
  private StringBuilder mappingBuilder = new StringBuilder();

  public static void main(String[] args) throws IOException{
    if (args.length == 0) {
      // TODO
      System.out.println("Usage");
      // TODO
      System.out.println("Ex:");
      System.exit(0);
    }

    Generator gen = new Generator();    
    gen.generate(args[0], args[1], args[2]);
  }

  private void generate(String startPoint, String destination, String mappingsFileName) throws IOException{

    File currentClassFile = new File(getPath());
    String parentPath = currentClassFile.getParent();
    String startPointFullPath = null;
    if (parentPath != null){ 
      startPointFullPath = (new File(parentPath)).getCanonicalPath() + File.pathSeparator + startPoint;
    }else{
      startPointFullPath = startPoint;
    }    
    
    File startPointFile = new File(startPointFullPath);    
    if (!startPointFile.exists()){
      throw new FileNotFoundException("Unable to find start path '" + startPointFullPath + "'.");
    }    
    
    if(destination != null){
      File destinationFile = new File(destination);    
      if (!destinationFile.exists()){
        throw new FileNotFoundException("Unable to find destination path '" + destination + "'.");
      }
      generate(startPointFile,destinationFile,mappingsFileName);
    }else{
      generate(startPointFile,new File(parentPath == null ? "" : parentPath),mappingsFileName);
    }
  }  

  private void generate(File startPointFile, File destination, String mappingsFileName) {
    File[] files = startPointFile.listFiles();
    Writer writer = null;
    try{
      writer = new BufferedWriter(new FileWriter(mappingsFileName));
      for(File file:files){
        extractMapping(file, writer);
      }
      // TODO - call recursivelly
    }catch(Exception e){      
      e.printStackTrace();
    }finally{
      writer.close();
    }
  }

  private void extractMapping(File file, Writer writer) throws ClassNotFoundException, IOException {
    Class classFName = Class.forName(file.getCanonicalPath());
    // generate %typeterm
    classFName.getc
    // take it's super class that
  }
  
  private void generateTypeTerm(Class classFName){
    mappingBuilder.append(%[
      %typeterm @classFName.getName()@ {
        implement     { @classFName.getCanonicalName()@ }
        is_sort(t)    { t instanceof @classFName.getCanonicalName()@ }
        equals(t1,t2) { t1.equals(t2) }      
      }]%
    );
  }

  public String getPath() {
    String className = getClass().getName();
    return ClassLoader.getSystemResource(className + ".class").getPath();
  }
}