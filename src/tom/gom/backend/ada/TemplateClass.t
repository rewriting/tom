/*
 * Gom
 *
 * Copyright (c) 2006-2012, INPL, INRIA
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
 * 
 *
 **/

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
  super(gomClass, gomEnvironment);    
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
