/*
 * Gom
 *
 * Copyright (C) 2006-2007, INRIA
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.backend;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import tom.gom.backend.CodeGen;
import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;
import tom.platform.OptionManager;

public abstract class TemplateHookedClass extends TemplateClass {
  protected HookList hooks;
  protected File tomHomePath;
  protected List importList;
  protected TemplateClass mapping;
  protected OptionManager optionManager;
 
  public TemplateHookedClass(GomClass gomClass,
                             OptionManager manager,
                             File tomHomePath,
                             List importList,
                             TemplateClass mapping) {
    super(gomClass);
    this.optionManager = manager;
    this.hooks = gomClass.getHooks();
    this.tomHomePath = tomHomePath;
    this.importList = importList;
    this.mapping = mapping;
  }

  %include { ../adt/objects/Objects.tom}

  protected String generateBlock() {
    StringBuffer res = new StringBuffer();
    HookList h = `concHook(hooks*);   
    %match(HookList h) {
      concHook(L1*,BlockHook(code),L2*) -> {
        res.append(CodeGen.generateCode(`code));
        res.append("\n");
      }
    }
    return res.toString();
  }

  protected String generateImport() {
    StringBuffer res = new StringBuffer();
    HookList h = `concHook(hooks*);   
    %match(HookList h) {
      concHook(L1*,ImportHook(code),L2*) -> {
        res.append(CodeGen.generateCode(`code));
        res.append("\n");
      }
    }
    return res.toString();
  }

  protected String generateInterface() {
    StringBuffer res = new StringBuffer();
    HookList h = `concHook(hooks*);   
    %match(HookList h) {
      concHook(L1*,InterfaceHook(code),L2*) -> {
        res.append(",");
        res.append(CodeGen.generateCode(`code));
        res.append("\n");
      }
    }
    return res.toString();
  }

  /*
   * The function for generating the file is extended, to be able to call Tom if
   * necessary (i.e. if there are user defined hooks)
   */
  public int generateFile() {
    if (hooks.isEmptyconcHook()) {
      try {
        File output = fileToGenerate();
        // make sure the directory exists
        output.getParentFile().mkdirs();
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
        generate(writer);
        writer.flush();
        writer.close();
      } catch(Exception e) {
        e.printStackTrace();
        return 1;
      }
    } else { /* We need to call tom to generate the file */
      File xmlFile = new File(tomHomePath,"Tom.xml");
      if(!xmlFile.exists()) {
        getLogger().log(Level.FINER,"Failed to get canonical path for "+xmlFile.getPath());
      }
      String file_path = null;
      try {
        File output = fileToGenerate();
        file_path = output.getCanonicalPath();
      } catch (IOException e) {
        getLogger().log(Level.FINER,"Failed to get canonical path for "+fileName());
      }

      ArrayList tomParams = new ArrayList();      

      try {
        Iterator it = importList.iterator();
        while(it.hasNext()){
          String importPath = ((File)it.next()).getCanonicalPath();
          tomParams.add("--import");
          tomParams.add(importPath);
        }
      } catch (IOException e) {
        getLogger().log(Level.SEVERE,"Failed compute import list: " + e.getMessage());
      }

      tomParams.add("-X");
      tomParams.add(xmlFile.getPath());
      tomParams.add("--optimize");
      tomParams.add("--optimize2");
      if(optionManager.getOptionValue("wall")==Boolean.TRUE) {
        tomParams.add("--wall");
      }
      if(optionManager.getOptionValue("intermediate")==Boolean.TRUE) {
        tomParams.add("--intermediate");
      }
      if(optionManager.getOptionValue("verbose")==Boolean.TRUE) {
        tomParams.add("--verbose");
      }
      tomParams.add("--output");
      tomParams.add(file_path);
      tomParams.add("-");     

      //String[] params = {"-X",xmlFile.getPath(),"--optimize","--optimize2","--output",file_path,"-"};
      //String[] params = {"-X",config_xml,"--output",file_path,"-"};

      //System.out.println("params: " + tomParams);

      try {
        StringWriter gen = new StringWriter();
        generate(gen);
        InputStream backupIn = System.in;
        System.setIn(new DataInputStream(new StringBufferInputStream(gen.toString())));
        int res = tom.engine.Tom.exec((String[])tomParams.toArray(new String[tomParams.size()]));
        //      int res = tom.engine.Tom.exec(params);
        System.setIn(backupIn);
        if (res != 0 ) {
          getLogger().log(Level.SEVERE, tom.gom.GomMessage.tomFailure.getMessage(),new Object[]{file_path});
          return res;
        }
      } catch (IOException e) {
        getLogger().log(Level.SEVERE,
            "Failed generate Tom code: " + e.getMessage());
      }
    }
    return 0;
  }

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

}
