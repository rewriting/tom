import java.io.BufferedWriter;
import java.io.*;

public class Generator {

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      // TODO
      System.out.println("Usage");
      // TODO
      System.out.println("Ex:");
      System.exit(0);
    }

    Generator gen = new Generator();
    gen.generate(args[0], args[1], args.length > 2 ? args[2] : null);
  }

  private void generate(String startPoint, String mappingsFileName, String destination) throws IOException {

    File currentClassFile = new File(getPath());
    String parentPath = currentClassFile.getParent();
    String startPointFullPath = null;
    if (parentPath != null) {
      startPointFullPath = (new File(parentPath)).getCanonicalPath() + File.separator + startPoint;
    } else {
      startPointFullPath = startPoint;
    }

    File startPointFile = new File(startPointFullPath);
    if (!startPointFile.exists()) {
      throw new FileNotFoundException("Unable to find start path '" + startPointFullPath + "'.");
    }

    Writer writer = null;
    StringBuilder strBuilder = new StringBuilder();
    try {
      if (destination != null) {
        File destinationFile = new File(destination);
        if (!destinationFile.exists()) {
          throw new FileNotFoundException("Unable to find destination path '" + destination + "'.");
        }
        generate(startPoint, startPointFile, destinationFile, mappingsFileName, strBuilder);
      } else {
        generate(startPoint, startPointFile, new File(parentPath == null ? "" : parentPath), mappingsFileName,strBuilder);
      }
      writer = new BufferedWriter(new FileWriter(mappingsFileName));
      writer.write(strBuilder.toString());      
    } catch (Exception e) {
      System.out.println("An error occured. See the stack trace for more information.\n");
      e.printStackTrace();
    } finally {
      writer.flush();
      writer.close();
    }
  }

  private void generate(String currentPackage, File startPointFile, File destination, String mappingsFileName,
      StringBuilder strBuilder) throws IOException, ClassNotFoundException {
    File[] files = startPointFile.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        generate(currentPackage + "." + file.getName(), file, destination, mappingsFileName, strBuilder);
      } else {
        System.out.println("Extracting mapping for:" + file.getName());
        extractMapping(currentPackage + "." + file.getName().substring(0, file.getName().indexOf('.')), strBuilder);
      }
    }
  }

  private void extractMapping(String className, StringBuilder strBuilder) throws ClassNotFoundException, IOException {
    Class classFName = Class.forName(className);
    // generate %typeterm
    generateTypeTerm(classFName, strBuilder);
    // take it's super class that
  }

  private void generateTypeTerm(Class classFName, StringBuilder strBuilder){
    String className = classFName.getCanonicalName().substring(classFName.getCanonicalName().lastIndexOf('.') + 1);
    strBuilder.append(%[
%typeterm @className@ {
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