package tom.gom.backend.ada;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;

public abstract class TemplateClass extends tom.gom.backend.TemplateClass{

  public TemplateClass(GomClass gomClass, GomEnvironment gomEnvironment) {
    this.gomClass = gomClass;
    this.className = gomClass.getClassName();
    this.gomEnvironment = gomEnvironment;
  }

  %include { ../../adt/objects/Objects.tom}

  public String className() {
    return className(this.className);
  }

  public String className(ClassName clsName) {
    %match(clsName) {
      ClassName[Name=name] -> {
	//"Cleaning" file names, Ada specific
        return (`name).replaceAll("AbstractType","");
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:className got a strange ClassName "+clsName);
  }

  public String fullClassName() {
    return fullClassName(this.className);
  }

  public static String fullClassName(ClassName clsName) {
    %match(clsName) {
      ClassName[Pkg=pkgPrefix,Name=name] -> {
        if(`pkgPrefix.length()==0) {
          return `name;
        } else {
	  //"Cleaning" full qualified name, Ada specific
          return (`pkgPrefix+"."+`name).replaceAll("\\.types\\.","\\.").replaceAll("\\.[^\\.]*AbstractType","");
        }
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:fullClassName got a strange ClassName "+clsName);
  }

  public String getPackage() {
    return getPackage(this.className);
  }

  public String getPackage(ClassName clsName) {
    %match(clsName) {
      ClassName[Pkg=pkg] -> {
	//"Cleaning" package name, Ada specific
        return (`pkg).replaceAll(".types","");
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:getPackage got a strange ClassName "+clsName);
  }

  public String hasMethod(SlotField slot) {
    %match(slot) {
      SlotField[Name=name] -> {
        return "has"+`name;
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:hasMethod got a strange SlotField "+slot);
  }

  public String getMethod(SlotField slot) {
    return getMethod(slot,false);
  }

  public String setMethod(SlotField slot) {
    return setMethod(slot,false);
  }

  /**
    * @param slot contains the name of the slot
    * @param jmi true to generate a name with a capital letter
    */
  public String getMethod(SlotField slot,boolean jmi) {
    %match(slot) {
      SlotField[Name=name] -> {
        if(jmi) {
          return "get"+jmize(`name);
        } else {
          return "get"+`name;
        }
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:getMethod got a strange SlotField "+slot);
  }

  /**
    * @param slot contains the name of the slot
    * @param jmi true to generate a name with a capital letter
    */
  public String setMethod(SlotField slot,boolean jmi) {
    %match(slot) {
      SlotField[Name=name] -> {
        if(jmi) {
          return "set"+jmize(`name);
        } else {
          return "set"+`name;
        }
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:setMethod got a strange SlotField "+slot);
  }

  private String jmize(String name) {
    //TODO
    return org.apache.commons.lang.StringUtils.capitalize(name);
  }

  public String index(SlotField slot) {
    %match(slot) {
      SlotField[Name=name] -> {
        return "index_"+`name;
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:index got a strange SlotField "+slot);
  }

  public String slotDomain(SlotField slot) {
    %match(slot) {
      SlotField[Domain=domain] -> {
        return fullClassName(`domain);
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:slotDomain got a strange SlotField "+slot);
  }

  private String fieldName(String fieldName) {
    return ""+fieldName;
  }

  public String classFieldName(ClassName clsName) {
    %match(clsName) {
      ClassName[Name=name] -> {
        return `name.toLowerCase();
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:classFieldName got a strange ClassName "+clsName);
  }

  public void toStringSlotField(StringBuilder res, SlotField slot,
                                String element, String buffer) {
    %match(slot) {
      SlotField[Domain=domain] -> {
        if(!getGomEnvironment().isBuiltinClass(`domain)) {
          res.append(%[@element@.toStringBuilder(@buffer@);
]%);
        } else {
          if (`domain.equals(`ClassName("","int"))
              || `domain.equals(`ClassName("","double"))
              || `domain.equals(`ClassName("","float"))) {
            res.append(%[@buffer@.append(@element@);
]%);
          } else if (`domain.equals(`ClassName("","long"))) {
            res.append(%[@buffer@.append(@element@);
            @buffer@.append("l");
]%);
          } else if (`domain.equals(`ClassName("","char"))) {
            res.append(%[@buffer@.append(((int)@element@-(int)'0'));
]%);
          } else if (`domain.equals(`ClassName("","boolean"))) {
            res.append(%[@buffer@.append(@element@?1:0);
]%);
          } else if (`domain.equals(`ClassName("","String"))) {
            String atchar = "@";
            res.append(%[@buffer@.append('"');
            for (int i = 0; i < @element@.length(); i++) {
              char c = @element@.charAt(i);
              switch (c) {
                case '\n':
                  @buffer@.append('\\');
                  @buffer@.append('n');
                  break;
                case '\t':
                  @buffer@.append('\\');
                  @buffer@.append('t');
                  break;
                case '\b':
                  @buffer@.append('\\');
                  @buffer@.append('b');
                  break;
                case '\r':
                  @buffer@.append('\\');
                  @buffer@.append('r');
                  break;
                case '\f':
                  @buffer@.append('\\');
                  @buffer@.append('f');
                  break;
                case '\\':
                  @buffer@.append('\\');
                  @buffer@.append('\\');
                  break;
                case '\'':
                  @buffer@.append('\\');
                  @buffer@.append('\'');
                  break;
                case '\"':
                  @buffer@.append('\\');
                  @buffer@.append('\"');
                  break;
                case '!':
                case '@atchar@':
                case '#':
                case '$':
                case '%':
                case '^':
                case '&':
                case '*':
                case '(':
                case ')':
                case '-':
                case '_':
                case '+':
                case '=':
                case '|':
                case '~':
                case '{':
                case '}':
                case '[':
                case ']':
                case ';':
                case ':':
                case '<':
                case '>':
                case ',':
                case '.':
                case '?':
                case ' ':
                case '/':
                  @buffer@.append(c);
                  break;

                default:
                  if (java.lang.Character.isLetterOrDigit(c)) {
                    @buffer@.append(c);
                  } else {
                    @buffer@.append('\\');
                    @buffer@.append((char) ('0' + c / 64));
                    c = (char) (c % 64);
                    @buffer@.append((char) ('0' + c / 8));
                    c = (char) (c % 8);
                    @buffer@.append((char) ('0' + c));
                  }
              }
            }
            @buffer@.append('"');
]%);
          } else if (`domain.equals(`ClassName("aterm","ATerm")) ||`domain.equals(`ClassName("aterm","ATermList"))) {
            res.append(%[@buffer@.append(@element@.toString());
]%);
          } else {
            throw new GomRuntimeException("Builtin " + `domain + " not supported");
          }
        }
      }
    }
  }

  protected String primitiveToReferenceType(String classname) {
    %match(classname) {
      "byte" -> { return "java.lang.Byte"; }
      "short" -> { return "java.lang.Short"; }
      "int" -> { return "java.lang.Integer"; }
      "long" -> { return "java.lang.Long"; }
      "float" -> { return "java.lang.Float"; }
      "double" -> { return "java.lang.Double"; }
      "boolean" -> { return "java.lang.Boolean"; }
      "char" -> { return "java.lang.Character"; }
    }
    return classname;
  }

  protected String fileName() {
    return fullClassName().replace('.','-')+".adb";
  }

  protected String fileNameSpec() {
    return fullClassName().replace('.','-')+".ads";
  }

  protected File fileToGenerateSpec() {
    GomStreamManager stream = getGomEnvironment().getStreamManager();
    File output = new File(stream.getDestDir(),fileNameSpec());
    return output;
  }

public void generateSpec(Writer writer) throws java.io.IOException{
return;
}

public int generateSpecFile() {

  try {
        File output2 = fileToGenerateSpec(); 
       
	// make sure the directory exists
       // if creation failed, try again, as this can be a manifestation of a
       // race condition in mkdirs
	if (!output2.getParentFile().mkdirs()) {
         output2.getParentFile().mkdirs();
       }

	Writer writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output2)));
       generateSpec(writer2);
       writer2.flush();
       writer2.close();


    } catch(IOException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.tomCodeGenerationFailure, e.getMessage());
      return 1;
    }
    return 0;


}

  protected void slotDecl(java.io.Writer writer, SlotFieldList slotList)
                        throws java.io.IOException {
    int index = 0;
    while(!slotList.isEmptyConcSlotField()) {
      SlotField slot = slotList.getHeadConcSlotField();
      slotList = slotList.getTailConcSlotField();
      if (index>0) { writer.write(", "); }
      %match(slot) {
        SlotField[Name=slotName,Domain=ClassName[Name=domainName]] -> {
          writer.write(`slotName);
          writer.write(":");
          writer.write(`domainName);
          index++;
        }
      }
    }
  }

  protected void slotArgs(java.io.Writer writer, SlotFieldList slotList)
                        throws java.io.IOException {
    int index = 0;
    while(!slotList.isEmptyConcSlotField()) {
      SlotField slot = slotList.getHeadConcSlotField();
      slotList = slotList.getTailConcSlotField();
      if (index>0) { writer.write(", "); }
      /* Warning: do not write the 'index' alone, this is not a valid variable
         name */
      writer.write("t"+index);
      index++;
    }
  }

  protected void slotArgsWithDollar(java.io.Writer writer, SlotFieldList slotList)
                        throws java.io.IOException {
    int index = 0;
    while(!slotList.isEmptyConcSlotField()) {
      SlotField slot = slotList.getHeadConcSlotField();
      slotList = slotList.getTailConcSlotField();
      if (index>0) { writer.write(", "); }
      /* Warning: do not write the 'index' alone, this is not a valid variable
         name */
      writer.write("$t"+index);
      index++;
    }
  }

  public void generateTomMapping(Writer writer)
      throws java.io.IOException {
    return;
  }

}
