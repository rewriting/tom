import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

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
    StringBuilder strBuilder = new StringBuilder("%include { Collection.tom }\n");
    try {
      // the types used in operator declaration
      HashSet<Class> usedTypes = new HashSet<Class>();
      // the types declared
      HashSet<Class> declaredTypes = new HashSet<Class>();      
      if (destination != null) {
        File destinationFile = new File(destination);
        if (!destinationFile.exists()) {
          throw new FileNotFoundException("Unable to find destination path '" + destination + "'.");
        }
        mappingsFileName = destinationFile.getCanonicalPath() + File.separator + mappingsFileName;        
      }      
      generate(startPoint, startPointFile, strBuilder, usedTypes, declaredTypes);
      // generate a mapping for each used type that was not declared
      for(Class usedType: usedTypes){
        if (!declaredTypes.contains(usedType) && !Collection.class.equals(usedType)){
          if (usedType.isPrimitive()){
            strBuilder.insert(0, "%include { " + usedType.getName() + ".tom }\n");
          } else {
            // generate %typeterm
            generateTypeTerm(usedType, strBuilder, declaredTypes);
          }
        }
      }  
      // add 'myAdd' method
      strBuilder.append(%[      
private static ArrayList myAdd(Object e,ArrayList l) {
  l.add(e);
  return l;
}
]%);      
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

  private void generate(String currentPackage, File startPointFile, StringBuilder strBuilder,
      HashSet<Class> usedTypes, HashSet<Class> declaredTypes) throws IOException, ClassNotFoundException {
    File[] files = startPointFile.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        generate(currentPackage + "." + file.getName(), file, strBuilder, usedTypes, declaredTypes);
      } else {
        System.out.println("Extracting mapping for:" + file.getName());
        extractMapping(currentPackage + "." + file.getName().substring(0, file.getName().indexOf('.')), strBuilder, 
            usedTypes, declaredTypes);
      }
    }
  }

  private void extractMapping(String className, StringBuilder strBuilder, HashSet<Class> usedTypes, 
      HashSet<Class> declaredTypes) throws ClassNotFoundException, IOException {
    Class classFName = Class.forName(className);
    strBuilder.append("\n/*******************************************************************************/\n");
    // generate %typeterm
    generateTypeTerm(classFName, strBuilder, declaredTypes);
    // generate %op
    generateOperator(classFName, strBuilder, usedTypes);
    // generate %oparray (only for base classes)
    if (Object.class.equals( classFName.getSuperclass())){
      generateOpArray(className, strBuilder);
    }   
  }

  private void generateTypeTerm(Class classFName, StringBuilder strBuilder, HashSet<Class> declaredTypes){
    String className = classFName.getCanonicalName().substring(classFName.getCanonicalName().lastIndexOf('.') + 1);
    declaredTypes.add(classFName);
    strBuilder.append(%[
%typeterm @className@ {
  implement     { @classFName.getCanonicalName()@ }
  is_sort(t)    { t instanceof @classFName.getCanonicalName()@ }
  equals(t1,t2) { t1.equals(t2) }      
}
]%);
  }

  private void generateOperator(Class classFName, StringBuilder strBuilder, HashSet<Class> usedTypes){
    String fullClassName = classFName.getCanonicalName();
    String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
    Method[] methods = classFName.getMethods();    
    // find the class that is the highest in the hierarchy
    Class superClass = null;    
    while(classFName.getSuperclass() != null && !Object.class.equals(classFName.getSuperclass()) ) {      
      superClass = classFName.getSuperclass();
      classFName = superClass;
    }
    String codomain = null;
    // if we have some super class
    if(superClass != null) {
      codomain = superClass.getCanonicalName().substring(superClass.getCanonicalName().lastIndexOf('.') + 1);
      usedTypes.add(superClass);
    }else{
      codomain = className;
      usedTypes.add(classFName);
    }
    strBuilder.append(%[
%op @codomain@ @className@(@getFieldsDeclarations(methods,usedTypes)@) {
  is_fsym(t)                { t instanceof @fullClassName@ } @getSlotDeclarations(methods,className)@     
}
]%);
  }

  private String getFieldsDeclarations(Method[] methods, HashSet<Class> usedTypes) {
    StringBuilder result = new StringBuilder();
    for (Method m : methods) {
      // not a 'get' or an 'is'
      String methodName = m.getName();
      if (!methodName.startsWith("get") && !methodName.startsWith("is")) {
        continue;
      }      
      String fieldName = methodName.startsWith("get") ? methodName.substring(3) : methodName.substring(2);
      fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
      if ("class".equalsIgnoreCase(fieldName)) { continue; }
      result.append(fieldName + ":");
      if (m.getReturnType().isPrimitive()) {
        result.append(m.getReturnType().getName());        
      } else {
        result.append(m.getReturnType().getCanonicalName().substring(
            m.getReturnType().getCanonicalName().lastIndexOf('.') + 1));
      }
      usedTypes.add(m.getReturnType());
      result.append(",");
    }
    // remove the ","
    String finalString = result.toString();
    return (finalString == null || "".equals(finalString)) ? "" : finalString.substring(0, finalString.length() - 1);
  }

  private String getSlotDeclarations(Method[] methods, String className){
    StringBuilder result = new StringBuilder();
    for(Method m: methods){
      // not a 'get' or an 'is'
      String methodName = m.getName();
      if (!methodName.startsWith("get") && !methodName.startsWith("is")) {continue;};
      String fieldName = methodName.startsWith("get") ? methodName.substring(3) : methodName.substring(2);
      fieldName = Character.toLowerCase(fieldName.charAt(0)) +  fieldName.substring(1);
      if ("class".equalsIgnoreCase(fieldName)) { continue; }
      result.append(%[
  get_slot(@fieldName@, t)  { ((@className@)t).@methodName@() }]%);    
    }
    return result.toString();
  }

  private void generateOpArray(String className, StringBuilder strBuilder){
    className = className.substring(className.lastIndexOf('.') + 1);
    String opName = Character.toLowerCase(className.charAt(0)) +  className.substring(1); 
    strBuilder.append(%[ 
%typeterm @className@List {
  implement                 { java.util.List }
  is_sort(t)                { t instanceof java.util.List }
  equals(t1,t2)             { t1.equals(t2) }
}

%oparray @className@List @opName@List(@className@*) {
  is_fsym(t)                { t instanceof java.util.List  }
  make_empty(n)             { new java.util.ArrayList(n) }
  make_append(e,l)          { myAdd(e,(ArrayList)l)  }
  get_element(l,n)          { (@className@)l.get(n)        }
  get_size(l)               { l.size()                }
}
    ]%); 
  }
  
  public String getPath() {
    String className = getClass().getName();
    return ClassLoader.getSystemResource(className + ".class").getPath();
  }
}