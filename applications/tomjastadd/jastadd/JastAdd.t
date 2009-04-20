package jastadd;

import ast.AST.*;

import jrag.AST.*;

import java.util.*;
import java.io.*;

import jrag.*;


public class JastAdd {
  
    public static final String VERSION = "JastAdd II (http://jastadd.cs.lth.se) version R20080520";
    public static final String VERSIONINFO = "\n// Generated with " + VERSION + "\n\n";

    protected java.util.List files;

    protected Grammar root; // root of the ast for the ast-grammar file
    protected String pack;
    protected File outputDir;
    protected String grammar;
    protected boolean publicModifier;
	
    public static void main(String[] args) {
      new JastAdd().compile(args);
      Runtime.getRuntime().gc();
    }

    public void compile(String[] args) {
      try {
           files = new ArrayList();
 	         if (readArgs(args)) System.exit(1);;

           long time = System.currentTimeMillis();
           
           root = new Grammar();
           root.abstractAncestors();
           
 	         // Parse ast-grammar
           Collection errors = new ArrayList();
           for(Iterator iter = files.iterator(); iter.hasNext(); ) {
             String fileName = (String)iter.next();
             if(fileName.endsWith(".ast")) {
               try {
                 Ast parser = new Ast(new FileInputStream(fileName));
                 parser.fileName = new File(fileName).getName();
                 Grammar g = parser.Grammar();
                 for(int i = 0; i < g.getNumTypeDecl(); i++) {
                   root.addTypeDecl(g.getTypeDecl(i));
                 }
                 for(Iterator errorIter = parser.getErrors(); errorIter.hasNext(); ) {
                   String[] s = ((String)errorIter.next()).split(";");
                   errors.add("Syntax error in " + fileName + " at line " + s[0] + ", column " + s[1]);
                 }
               
               } catch (ast.AST.TokenMgrError e) {
                 System.out.println("Lexical error in " + fileName + ": " + e.getMessage());
                 System.exit(1);
               } catch (ast.AST.ParseException e) {
                 // Exceptions actually caught by error recovery in parser
               } catch (FileNotFoundException e) {
                 System.out.println("File error: Abstract syntax grammar file " + fileName + " not found");
                 System.exit(1);
               }
             }
           }

           if(!errors.isEmpty()) {
             for(Iterator iter = errors.iterator(); iter.hasNext(); )
               System.out.println(iter.next());
             System.exit(1);
           }
            

           StringBuilder builder = new StringBuilder();
           // include for primitive types
           builder.append(%[
%include{ int.tom }
%include{ boolean.tom }
%include{ string.tom }
               ]%);
           generateASTType(builder);
           generateListDestructor(builder);
           for(int i = 0; i < root.getNumTypeDecl(); i++) {
             if(root.getTypeDecl(i) instanceof ASTDecl) {
               ASTDecl decl = (ASTDecl)root.getTypeDecl(i);
               //generate only an operator mapping for non abstract ASTDecl
               if ( !decl.hasAbstract() && !decl.name().equals("ASTNode") && !decl.name().equals("Opt") && !decl.name().equals("List") ) {
                 generateOperator(decl,builder);
               }
             }
           }
           System.out.println(builder);
           /** 
             Writer writer = new BufferedWriter(new FileWriter(fileName+".tom"));
             writer.append(builder.toString());
             writer.close();
            */
      }
      catch(NullPointerException e) {
        e.printStackTrace();
        throw e;
      }
      catch(ArrayIndexOutOfBoundsException e) {
        e.printStackTrace();
        throw e;
      }
      catch(Exception e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    private void generateASTType(StringBuilder strBuilder) {
      strBuilder.append(%[
%typeterm ASTNode {
  implement { ASTNode }
  is_sort(t) { $t instanceof ASTNode }
  equals(t1, t2) { $t1.equals($t2) }
}
]%);
    }

  private void generateListDestructor(StringBuilder strBuilder) {
    strBuilder.append(%[
%oplist ASTNode List(ASTNode*) {
  is_fsym(l)       { ($l!= null) && ($l instanceof List) }
  make_empty()     { new List() }
  make_insert(o,l) { ((List)$l).add($o) }
  get_head(l)      { $l.getChild(0) }
  get_tail(l)      { getTail(((List)$l)) }
  is_empty(l)      { ($l.getNumChild()==0) }
}

private static List getTail(List l) {
  List res = new List();
  for(int i = 1; i < l.getNumChild(); i++) {
  ASTNode node = l.getChildNoTransform(i);
  if(node != null) node = node.fullCopy();
  res.setChild(node, i-1);
  }
  return res;
}
]%);
  }


    private void generateOperator(ASTDecl decl, StringBuilder strBuilder) {
      String className = decl.name();
      strBuilder.append(%[
%op ASTNode @className@(@getTypedParameters(decl.getComponents())@) {
  make(@getParameters(decl.getComponents())@) { new @className@(@getParametersWithDollar(decl.getComponents())@) }
  is_fsym(t) { $t instanceof @className@ } ]%);
      strBuilder.append(getSlotDeclarations(className,decl.getComponents()));     
      strBuilder.append(%[
}
]%);
}

private String getParameters(Iterator components) {
  StringBuilder result = new StringBuilder();
  while (components.hasNext()) {
    Components c = (Components)components.next();
    if(! c.isNTA()) {
      if(c instanceof TokenComponent) {
        result.append(c.name()+", ");
      } else {
        if(c instanceof ListComponents) {
          result.append(c.name()+"List, ");
        } else {
          if(c instanceof AggregateComponents) {
            result.append(c.name()+", ");
          } else {
            if(c instanceof OptionalComponent) {
              result.append(c.name()+"Opt, ");
            } 
          }
        }
      }
    }
  }
  // remove the ","
  String finalstring = result.toString();
  return finalstring==null || "".equals(finalstring) ? "" : finalstring.substring(0, finalstring.length() - 2);
}

private String getParametersWithDollar(Iterator components) {
  StringBuilder result = new StringBuilder();
  while (components.hasNext()) {
    Components c = (Components)components.next();
    if(! c.isNTA()) {
      if(c instanceof TokenComponent) {
        result.append("("+c.type()+") $"+c.name()+", ");
      } else {
        if(c instanceof ListComponents) {
          result.append("(List) $"+c.name()+"List, ");
        } else {
          if(c instanceof AggregateComponents) {
            result.append("("+c.type()+") $"+c.name()+", ");
          } else {
            if(c instanceof OptionalComponent) {
              result.append("(Opt) $"+c.name()+"Opt, ");
            } 
          }
        }
      }
    }
  }
  // remove the ","
  String finalstring = result.toString();
  return finalstring==null || "".equals(finalstring) ? "" : finalstring.substring(0, finalstring.length() - 2);
}

private String getTypedParameters(Iterator components) {
  StringBuilder result = new StringBuilder();
  while (components.hasNext()) {
    Components c = (Components)components.next();
    if(! c.isNTA()) {
      if(c instanceof TokenComponent) {
        TokenComponent m = (TokenComponent) c;
        //remove the package name
        if (m.type().equals("java.lang.String")) {
          result.append(m.name()+":String, ");
        } else {
          result.append(m.name()+":"+m.type()+", ");
        }
      } else {
        if(c instanceof ListComponents) {
          ListComponents m = (ListComponents) c;
          result.append(m.name()+"List:ASTNode, ");
        } else {
          if(c instanceof AggregateComponents) {
            AggregateComponents m = (AggregateComponents) c;
            result.append(m.name()+":ASTNode, ");
          } else {
            if(c instanceof OptionalComponent) {
              OptionalComponent m = (OptionalComponent) c;
              //Opt is a subclass of ASTNode
              result.append(m.name()+"Opt:ASTNode, ");
            } else {
              throw new RuntimeException("Unexpected class "+c.getClass());
            }
          }
        }
      }
    }
  }
  // remove the ","
  String finalstring = result.toString();
  return finalstring==null || "".equals(finalstring) ? "" : finalstring.substring(0, finalstring.length() - 2);
}

private String getSlotDeclarations(String className, Iterator components) {
  StringBuilder result = new StringBuilder();
  while (components.hasNext()) {
    Components c = (Components)components.next();
    if(! c.isNTA()) {
      if(c instanceof TokenComponent) {
        TokenComponent m = (TokenComponent) c;
        result.append(%[
  get_slot(@m.name()@, t)  { ((@className@)$t).get@m.name()@() }]%);    
      } else {
        if(c instanceof ListComponents) {
          ListComponents m = (ListComponents) c;
          result.append(%[
  get_slot(@m.name()@List, t)  { ((@className@)$t).get@m.name()@List() }]%);    
        } else {
          if(c instanceof AggregateComponents) {
            AggregateComponents m = (AggregateComponents) c;
            result.append(%[
  get_slot(@m.name()@, t)  { ((@className@)$t).get@m.name()@() }]%);    
          } else {
            if(c instanceof OptionalComponent) {
              OptionalComponent m = (OptionalComponent) c;
              result.append(%[
  get_slot(@m.name()@Opt, t)  { ((@className@)$t).get@m.name()@Opt() }]%);    
            }
          }
        }
      }
    }
  }
  String finalstring = result.toString();
  return finalstring==null || "".equals(finalstring) ? "" : finalstring;
}

  /* Read and process commandline */
  public boolean readArgs(String[] args) {
    CommandLineArguments cla = new CommandLineArguments(args);
    if(cla.hasLongOption("jjtree") && !cla.hasLongOption("grammar")) {
      System.out.println("Missing grammar option that is required in jjtree-mode");
      return true;
    }
    ASTNode.jjtree = cla.hasLongOption("jjtree");
    grammar = cla.getLongOptionValue("grammar", "Unknown");

    ASTNode.createDefaultMap = cla.getLongOptionValue("defaultMap", "new java.util.HashMap(4)");
    ASTNode.createDefaultSet = cla.getLongOptionValue("defaultSet", "new java.util.HashSet(4)");

    ASTNode.lazyMaps = cla.hasLongOption("lazyMaps");

    publicModifier = !cla.hasLongOption("private");

    ASTNode.rewriteEnabled = cla.hasLongOption("rewrite");
    ASTNode.beaver = cla.hasLongOption("beaver");
    ASTNode.visitCheckEnabled = !cla.hasLongOption("novisitcheck");
    ASTNode.cacheCycle = !cla.hasLongOption("noCacheCycle");
    ASTNode.componentCheck = !cla.hasLongOption("noComponentCheck");

    ASTNode.noInhEqCheck = cla.hasLongOption("noInhEqCheck");

    ASTNode.suppressWarnings = cla.hasLongOption("suppressWarnings");
    ASTNode.parentInterface = cla.hasLongOption("parentInterface");

    ASTNode.doc = cla.hasLongOption("doc");

    ASTNode.license = "";
    if(cla.hasLongOption("license")) {
      String fileName = cla.getLongOptionValue("license", null);
      try {
        if(fileName != null) {
          ASTNode.license = readFile(fileName);
        }
      } catch (java.io.IOException e) {
        System.err.println("Error loading license file " + fileName);
        System.exit(1);
      }
    }

    ASTNode.java5 = !cla.hasLongOption("java1.4");

    if(cla.hasLongOption("debug")) {
      ASTNode.debugMode = true;
      ASTNode.cycleLimit = 100;
      ASTNode.rewriteLimit = 100;
      ASTNode.visitCheckEnabled = true;
    }

    ASTNode.block = cla.hasLongOption("synch");

    String outputDirName = cla.getLongOptionValue("o", System.getProperty("user.dir"));
    outputDir = new File(outputDirName);

    if(!outputDir.exists()) {
      System.out.println("Output directory does not exist");
      System.exit(1);
    }
    if(!outputDir.isDirectory()) {
      System.out.println("Output directory is not a directory");
      System.exit(1);
    }
    if(!outputDir.canWrite()) {
      System.out.println("Output directory is write protected");
      System.exit(1);
    }

    pack = cla.getLongOptionValue("package", "").replace('/', '.');
    int n = cla.getNumOperands();
    for (int k=0; k<n; k++) {
      String fileName = cla.getOperand(k);
      if(fileName.endsWith(".ast") || fileName.endsWith(".jrag") || fileName.endsWith(".jadd")) {
        files.add(fileName);
      }
      else {
        System.out.println("FileError: " + fileName + " is of unknown file type");
        return true;
      }
    }

    if(cla.hasLongOption("version")) {
      System.out.println(VERSION);
      return true;
    }

    if (cla.hasLongOption("help") || files.isEmpty()) {
      System.out.println(VERSION + "\n");
      printHelp();
      return true;
    }
    return false;
  }


private String readFile(String name) throws java.io.IOException {
  StringBuffer buf = new StringBuffer();
  java.io.Reader reader = new java.io.BufferedReader(new java.io.FileReader(name));
  char[] cbuf = new char[1024];
  int i = 0;
  while((i = reader.read(cbuf)) != -1)
    buf.append(String.valueOf(cbuf, 0, i));	
  reader.close();
  return buf.toString();
}

private void checkMem() {
  Runtime runtime = Runtime.getRuntime();
  long total = runtime.totalMemory();
  long free = runtime.freeMemory();
  long use = total-free;
  System.out.println("Before GC: Total " + total + ", use " + use);
  runtime.gc();
  total = runtime.totalMemory();
  free = runtime.freeMemory();
  use = total-free;
  System.out.println("After GC: Total " + total + ", use " + use);
}

/**
  Print help
 */
public void printHelp() {
  System.out.println("This program reads a number of .jrag, .jadd, and .ast files");
  System.out.println("and creates the nodes in the abstract syntax tree");
  System.out.println();
  System.out.println("The .jrag source files may contain declarations of synthesized ");
  System.out.println("and inherited attributes and their corresponding equations.");
  System.out.println("It may also contain ordinary Java methods and fields.");
  System.out.println();
  System.out.println("Source file syntax can be found at http://jastadd.cs.lth.se");
  System.out.println();
  System.out.println("Options:");
  System.out.println("  --help (prints this text and stops)");
  System.out.println("  --version (prints version information and stops)");
  System.out.println("  --package=PPP (optional package for generated files, default is none)");
  System.out.println("  --o=DDD (optional base output directory, default is current directory");
  System.out.println("  --beaver (use beaver base node)");
  System.out.println("  --jjtree (use jjtree base node, this requires --grammar to be set)");
  System.out.println("  --grammar=GGG (the parser for the grammar is called GGG, required when using jjtree)");
  System.out.println("  --rewrite (enable ReRAGs support)");
  System.out.println("  --novisitcheck (disable circularity check for attributes)");
  System.out.println("  --noCacheCycle (disable cache cyle optimization for circular attributes)");
  System.out.println("  --license=LICENSE (include the file LICENSE in each generated file)");
  System.out.println();
  System.out.println("Arguments:");
  System.out.println("Names of .ast, .jrag and .jadd source files");
  System.out.println();
  System.out.println("Example: The following command reads and translates files NameAnalysis.jrag");
  System.out.println("and TypeAnalysis.jrag, weaves PrettyPrint.jadd into the abstract syntax tree");
  System.out.println("defined in the grammar Toy.ast.");
  System.out.println("The result is the generated classes for the nodes in the AST that are placed");
  System.out.println("in the package ast.");
  System.out.println();
  System.out.println("JastAdd --package=ast Toy.ast NameAnalysis.jrag TypeAnalysis.jrag PrettyPrinter.jadd");
  System.out.println();
  System.out.println("Stopping program");
}
}
