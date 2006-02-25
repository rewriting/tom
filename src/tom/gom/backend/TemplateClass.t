/*
 *
 * GOM
 *
 * Copyright (C) 2006 INRIA
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

import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;
import java.io.*;

public abstract class TemplateClass {

  private ClassName className;

  public TemplateClass(ClassName className) {
    this.className = className;
  }

	%include { ../adt/objects/Objects.tom}
  
  public abstract String generate();

  public String className() {
    return className(this.className);
  }
  public String className(ClassName clsName) {
    %match(ClassName clsName) {
      ClassName[name=name] -> {
        return `name;
      }
    }
    throw new GomRuntimeException("TemplateClass:className got a strange ClassName");
  }
  public String fullClassName() {
    return fullClassName(this.className);
  }
  public String fullClassName(ClassName clsName) {
    %match(ClassName clsName) {
      ClassName[pkg=pkgPrefix,name=name] -> {
        if(`pkgPrefix.equals("")) {
          return `name;
        } else {
          return `pkgPrefix+"."+`name;
        }
      }
    }
    throw new GomRuntimeException("TemplateClass:fullClassName got a strange ClassName "+clsName);
  }

  public String getPackage() {
    return getPackage(this.className);
  }
  public String getPackage(ClassName clsName) {
    %match(ClassName clsName) {
      ClassName[pkg=pkg] -> {
        return `pkg;
      }
    }
    throw new GomRuntimeException("TemplateClass:getPackage got a strange ClassName");
  }

  public String hasMethod(SlotField slot) {
    %match(SlotField slot) {
      SlotField[name=name] -> {
        return "has"+`name;
      }
    }
    throw new GomRuntimeException("TemplateClass:hasMethod got a strange SlotField");
  }
  public String getMethod(SlotField slot) {
    %match(SlotField slot) {
      SlotField[name=name] -> {
        return "get"+`name;
      }
    }
    throw new GomRuntimeException("TemplateClass:getMethod got a strange SlotField");
  }
  public String setMethod(SlotField slot) {
    %match(SlotField slot) {
      SlotField[name=name] -> {
        return "set"+`name;
      }
    }
    throw new GomRuntimeException("TemplateClass:getMethod got a strange SlotField");
  }
  public String index(SlotField slot) {
    %match(SlotField slot) {
      SlotField[name=name] -> {
        return "index_"+`name;
      }
    }
    throw new GomRuntimeException("TemplateClass:index got a strange SlotField");
  }
  public String slotDomain(SlotField slot) {
    %match(SlotField slot) {
      SlotField[domain=domain] -> {
        return fullClassName(`domain);
      }
    }
    throw new GomRuntimeException("TemplateClass:slotDomain got a strange SlotField");
  }
  public String fieldName(ClassName clsName) {
    %match(ClassName clsName) {
      ClassName[name=name] -> {
        return `name.toLowerCase();
      }
    }
    throw new GomRuntimeException("TemplateClass:className got a strange ClassName");
  }

  protected String fileName() {
    return fullClassName().replace('.',File.separatorChar)+".java";
  }

  public int generateFile() {
    GomStreamManager stream = GomEnvironment.getInstance().getStreamManager();
    try {
       File output = new File(stream.getDestDir(),fileName());      
       // make sure the directory exists
       output.getParentFile().mkdirs();
       Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
       writer.write(generate()); 
       writer.flush();
       writer.close();
    } catch(Exception e) {
      e.printStackTrace();
      return 1;
    }
    return 0;
  }
}
