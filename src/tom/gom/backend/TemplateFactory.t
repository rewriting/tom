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

import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class TemplateFactory extends TemplateClass {
  ClassNameList factories;
  GomClassList operators;

	%include { ../adt/objects/Objects.tom}

  TemplateFactory(ClassName className, ClassNameList factories, GomClassList operators) {
    super(className);
    this.factories = factories;
    this.operators = operators;
  }

  /* We may want to return the stringbuffer itself in the future, or directly write to a Stream */
  public String generate() {
    StringBuffer out = new StringBuffer();

    out.append("package "+getPackage()+";\n");
    out.append("\n");
    out.append("public class "+className()+" {\n");

    out.append("\tprivate aterm.pure.PureFactory factory;\n");

    // factories
    ClassNameList consum = factories;
    while(!consum.isEmpty()) {
      ClassName factoryName = consum.getHead();
      consum = consum.getTail();
      out.append("\tprivate "+fullClassName(factoryName)+" "+fieldName(factoryName)+";\n");
    }

    // protos
    %match(GomClassList operators) {
      concGomClass(_*,op@OperatorClass[sortName=sortName],_*) -> {
        out.append("\n");
        out.append("\tprivate aterm.AFun "+fun(`op)+";\n");
        out.append("\tprivate "+fullClassName(`sortName)+" "+proto(`op)+";\n");
        out.append("\tprivate aterm.ATerm "+pattern(`op)+";\n");
      }
    }

    // constructor
    out.append("\tprivate "+className()+"() {\n");
    out.append("\t\tthis.factory = aterm.pure.SingletonFactory.getInstance();\n");
    out.append("\t}\n");
    out.append("\tprivate static "+className()+" instance = null;\n");
    out.append("\tpublic static "+className()+" getInstance() {\n");
    out.append("\t\tif (instance == null) {\n");
    out.append("\t\t\tinstance = new "+className()+"();\n");
    out.append("\t\t\tinstance.initialize();\n");
    out.append("\t\t}\n");
    out.append("\t\treturn instance;\n");
    out.append("\t}\n");
    out.append("\n");
    out.append("\tpublic aterm.pure.PureFactory getPureFactory() {\n");
    out.append("\t\treturn factory;\n");
    out.append("\t}\n");
    out.append("\n");

    // initialization
    out.append("\tprivate void initialize() {\n");
    out.append("\n");
    %match(GomClassList operators) {
      concGomClass(_*,op@OperatorClass[className=opName],_*) -> {
        out.append("\t\t"+pattern(`op)+" = factory.parse("+atermPattern(`op)+");\n");
        out.append("\t\t"+fun(`op)+" = factory.makeAFun("+atermFunArgs(`op)+");\n");
        out.append("\t\t"+proto(`op)+" = new "+fullClassName(`opName)+"(this)\n");
        out.append("\n");
      }
    }
    out.append("\t}\n");
    out.append("\nTODO");
    out.append("\n");
    out.append("\n");
    out.append("\n");


    out.append("}");
    
    return out.toString();
  }

  private String fun(GomClass op) {
    %match(GomClass op) {
      OperatorClass[className=opName,sortName=sortName] -> {
        return "fun_"+className(`sortName)+"_"+className(`opName);
      }
    }
		throw new GomRuntimeException("TemplateFactory gor a strange operatorclass: "+op); 
  }
  private String proto(GomClass op) {
    %match(GomClass op) {
      OperatorClass[className=opName,sortName=sortName] -> {
        return "proto_"+className(`sortName)+"_"+className(`opName);
      }
    }
		throw new GomRuntimeException("TemplateFactory got a strange operatorclass: "+op); 
  }
  private String pattern(GomClass op) {
    %match(GomClass op) {
      OperatorClass[className=opName,sortName=sortName] -> {
        return "pattern_"+className(`sortName)+"_"+className(`opName);
      }
    }
		throw new GomRuntimeException("TemplateFactory got a strange operatorclass: "+op); 
  }
  private String atermPattern(GomClass op) {
    return "\"\""; // TODO
  }
  private String atermFunArgs(GomClass op) {
    %match(GomClass op) {
      OperatorClass[className=opName,sortName=sortName] -> {
        return "\"_"+className(`sortName)+"_"+className(`opName)+"\", 0, false";
      }
    }
		throw new GomRuntimeException("TemplateFactory got a strange operatorclass: "+op); 
  }
}
