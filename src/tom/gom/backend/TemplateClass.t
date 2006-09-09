/*
 * Gom
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
  protected ClassName className;

  public TemplateClass(ClassName className) {
    this.className = className;
  }

  %include { ../adt/objects/Objects.tom}

  public abstract void generate(Writer writer) throws java.io.IOException;

  public String className() {
    return className(this.className);
  }
  public String className(ClassName clsName) {
    %match(ClassName clsName) {
      ClassName[Name=name] -> {
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
      ClassName[Pkg=pkgPrefix,Name=name] -> {
        if(`pkgPrefix.length()==0) {
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
      ClassName[Pkg=pkg] -> {
        return `pkg;
      }
    }
    throw new GomRuntimeException("TemplateClass:getPackage got a strange ClassName");
  }

  public String hasMethod(SlotField slot) {
    %match(SlotField slot) {
      SlotField[Name=name] -> {
        return "has"+`name;
      }
    }
    throw new GomRuntimeException("TemplateClass:hasMethod got a strange SlotField");
  }
  public String getMethod(SlotField slot) {
    %match(SlotField slot) {
      SlotField[Name=name] -> {
        return "get"+`name;
      }
    }
    throw new GomRuntimeException("TemplateClass:getMethod got a strange SlotField");
  }
  public String setMethod(SlotField slot) {
    %match(SlotField slot) {
      SlotField[Name=name] -> {
        return "set"+`name;
      }
    }
    throw new GomRuntimeException("TemplateClass:getMethod got a strange SlotField");
  }
  public String index(SlotField slot) {
    %match(SlotField slot) {
      SlotField[Name=name] -> {
        return "index_"+`name;
      }
    }
    throw new GomRuntimeException("TemplateClass:index got a strange SlotField");
  }
  public String slotDomain(SlotField slot) {
    %match(SlotField slot) {
      SlotField[Domain=domain] -> {
        return fullClassName(`domain);
      }
    }
    throw new GomRuntimeException("TemplateClass:slotDomain got a strange SlotField");
  }
  public String fieldName(ClassName clsName) {
    %match(ClassName clsName) {
      ClassName[Name=name] -> {
        return `name.toLowerCase();
      }
    }
    throw new GomRuntimeException("TemplateClass:className got a strange ClassName");
  }

  protected String fileName() {
    return fullClassName().replace('.',File.separatorChar)+".java";
  }

  protected File fileToGenerate() {
    GomStreamManager stream = GomEnvironment.getInstance().getStreamManager();
    File output = new File(stream.getDestDir(),fileName());
    return output;
  }

  public int generateFile() {
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
    return 0;
  }

  public String visitMethod(ClassName sortName) {
    return "visit_"+className(sortName);
  }
  public String isOperatorMethod(ClassName opName) {
    return "is"+className(opName);
  }
}
