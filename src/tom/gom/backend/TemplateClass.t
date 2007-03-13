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

import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;
import java.io.*;

public abstract class TemplateClass {
  protected GomClass gomClass;
  protected ClassName className;

  public TemplateClass(GomClass gomClass) {
    this.gomClass = gomClass;
    this.className = gomClass.getClassName();
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
    throw new GomRuntimeException(
        "TemplateClass:className got a strange ClassName "+clsName);
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
    throw new GomRuntimeException(
        "TemplateClass:fullClassName got a strange ClassName "+clsName);
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
    throw new GomRuntimeException(
        "TemplateClass:getPackage got a strange ClassName "+clsName);
  }

  public String hasMethod(SlotField slot) {
    %match(SlotField slot) {
      SlotField[Name=name] -> {
        return "has"+`name;
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:hasMethod got a strange SlotField "+slot);
  }

  public String getMethod(SlotField slot) {
    %match(SlotField slot) {
      SlotField[Name=name] -> {
        return "get"+`name;
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:getMethod got a strange SlotField "+slot);
  }

  public String setMethod(SlotField slot) {
    %match(SlotField slot) {
      SlotField[Name=name] -> {
        return "set"+`name;
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:setMethod got a strange SlotField "+slot);
  }

  public String index(SlotField slot) {
    %match(SlotField slot) {
      SlotField[Name=name] -> {
        return "index_"+`name;
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:index got a strange SlotField "+slot);
  }

  public String slotDomain(SlotField slot) {
    %match(SlotField slot) {
      SlotField[Domain=domain] -> {
        return fullClassName(`domain);
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:slotDomain got a strange SlotField "+slot);
  }

  private String fieldName(String fieldName) {
    return "_"+fieldName;
  }

  public String classFieldName(ClassName clsName) {
    %match(ClassName clsName) {
      ClassName[Name=name] -> {
        return `name.toLowerCase();
      }
    }
    throw new GomRuntimeException(
        "TemplateClass:classFieldName got a strange ClassName "+clsName);
  }

  public void toStringSlotField(StringBuffer res, SlotField slot,
                                String element, String buffer) {
    %match(SlotField slot) {
      SlotField[Name=slotName,Domain=domain] -> {
        if(!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res.append(%[@element@.toStringBuffer(@buffer@);
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
            res.append(%[@buffer@.append("\"");
            @buffer@.append(@element@);
            @buffer@.append("\"");
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

  public void toATermSlotField(StringBuffer res, SlotField slot) {
    %match(SlotField slot) {
      SlotField[Domain=domain] -> {
        if(!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          res.append(getMethod(slot));
          res.append("().toATerm()");
        } else {
          if (`domain.equals(`ClassName("","int"))) {
            res.append("(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeInt(");
            res.append(getMethod(slot));
            res.append("())");
          } else if (`domain.equals(`ClassName("","boolean"))) {
            res.append("(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeInt(");
            res.append(getMethod(slot));
            res.append("()?1:0)");
          } else if (`domain.equals(`ClassName("","long"))) {
            res.append("(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeLong(");
            res.append(getMethod(slot));
            res.append("())");
          } else if (`domain.equals(`ClassName("","double"))) {
            res.append("(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeReal(");
            res.append(getMethod(slot));
            res.append("())");
          } else if (`domain.equals(`ClassName("","float"))) {
            res.append("(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeReal(");
            res.append(getMethod(slot));
            res.append("())");
          } else if (`domain.equals(`ClassName("","char"))) {
            res.append("(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeInt(((int)");
            res.append(getMethod(slot));
            res.append("()-(int)'0'))");
          } else if (`domain.equals(`ClassName("","String"))) {
            res.append("(aterm.ATerm) aterm.pure.SingletonFactory.getInstance().makeAppl(");
            res.append("aterm.pure.SingletonFactory.getInstance().makeAFun(");
            res.append(getMethod(slot));
            res.append("() ,0 , true))");
          } else if (`domain.equals(`ClassName("aterm","ATerm")) ||`domain.equals(`ClassName("aterm","ATermList"))){
            res.append(getMethod(slot));
            res.append("()");
          } else {
            throw new GomRuntimeException("Builtin " + `domain + " not supported");
          }
        }
      }
    }
  }

  public void fromATermSlotField(StringBuffer buffer, SlotField slot, String appl) {
    %match(SlotField slot) {
      SlotField[Domain=domain] -> {
        if(!GomEnvironment.getInstance().isBuiltinClass(`domain)) {
          buffer.append(fullClassName(`domain));
          buffer.append(".fromTerm(");
          buffer.append(appl);
          buffer.append(")");
        } else {
          if (`domain.equals(`ClassName("","int"))) {
            buffer.append("((aterm.ATermInt)").append(appl).append(").getInt()");
          } else  if (`domain.equals(`ClassName("","float"))) {
            buffer.append("(float) ((aterm.ATermReal)").append(appl).append(").getReal()");
          } else  if (`domain.equals(`ClassName("","boolean"))) {
            buffer.append("(((aterm.ATermInt)").append(appl).append(").getInt()==0?false:true)");
          } else  if (`domain.equals(`ClassName("","long"))) {
            buffer.append("((aterm.ATermLong)").append(appl).append(").getLong()");
          } else  if (`domain.equals(`ClassName("","double"))) {
            buffer.append("((aterm.ATermReal)").append(appl).append(").getReal()");
          } else  if (`domain.equals(`ClassName("","char"))) {
            buffer.append("(char) (((aterm.ATermInt)").append(appl).append(").getInt()+(int)'0')");
          } else if (`domain.equals(`ClassName("","String"))) {
            buffer.append("(String) ((aterm.ATermAppl)").append(appl).append(").getAFun().getName()");
          } else if (`domain.equals(`ClassName("aterm","ATerm")) || `domain.equals(`ClassName("aterm","ATermList")) ){
            buffer.append(appl);
          } else {
            throw new GomRuntimeException("Builtin " + `domain + " not supported");
          }
        }
      }
    }
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
