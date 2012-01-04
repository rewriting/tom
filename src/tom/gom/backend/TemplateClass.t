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

public interface TemplateClass {

  public abstract void generateTomMapping(java.io.Writer writer) throws java.io.IOException;

  public abstract void generate(Writer writer) throws java.io.IOException;

  public abstract int generateFile() ;

  public int generateSpecFile(); 

  public void generateSpec(java.io.Writer writer) throws java.io.IOException; 

}

