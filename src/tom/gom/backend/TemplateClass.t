package tom.gom.backend ; 

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;

public abstract class TemplateClass {

  protected GomClass gomClass;
  protected ClassName className;
  protected GomEnvironment gomEnvironment;

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  protected File fileToGenerate() {
    GomStreamManager stream = getGomEnvironment().getStreamManager();
    File output = new File(stream.getDestDir(),fileName());
    return output;
  }

    public int generateFile() {
    try {
	File output = fileToGenerate();
       // make sure the directory exists
       // if creation failed, try again, as this can be a manifestation of a
       // race condition in mkdirs
       if (!output.getParentFile().mkdirs()) {
         output.getParentFile().mkdirs();
       }

       Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
       generate(writer);
       writer.flush();
       writer.close();

    } catch(IOException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.tomCodeGenerationFailure, e.getMessage());
      return 1;
    }
    return 0;
  }

  public String visitMethod(ClassName sortName) {
    return "visit_"+className(sortName);
  }

  public String isOperatorMethod(ClassName opName) {
    return "is"+className(opName);
  }

  public String getCollectionMethod(ClassName opName) {
    return "getCollection"+className(opName);
  }

  protected Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  public abstract String className();

  public abstract String className(ClassName clsName);

  public abstract String fullClassName();

  protected abstract String fileName(); 

  public abstract void generateTomMapping(java.io.Writer writer) throws java.io.IOException;

  public abstract void generate(Writer writer) throws java.io.IOException;

  public abstract int generateSpecFile(); 

  public abstract void generateSpec(java.io.Writer writer) throws java.io.IOException; 

}

