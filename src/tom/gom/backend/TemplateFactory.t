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
  GomClassList sorts;
  GomClassList operators;

	%include { ../adt/objects/Objects.tom}

  TemplateFactory(ClassName className, ClassNameList factories, GomClassList sorts, GomClassList operators) {
    super(className);
    this.factories = factories;
    this.sorts = sorts;
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
        out.append("\t\t"+proto(`op)+" = new "+fullClassName(`opName)+"(this);\n");
        out.append("\n");
      }
    }
    out.append("\t}\n");

    // methods to make operators
    %match(GomClassList operators) {
      concGomClass(_*,op@OperatorClass[className=opName,sortName=sortName],_*) -> {
        out.append("\tpublic "+fullClassName(`sortName)+" "+make(`op)+"(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {\n");
        out.append("\t\tsynchronized ("+proto(`op)+") {\n");
        out.append("\t\t\t"+proto(`op)+".initHashCode(annos, fun, args);\n");
        out.append("\t\t\treturn ("+fullClassName(`sortName)+") factory.build("+proto(`op)+");\n");
        out.append("\t\t}\n");
        out.append("\t}\n");
        out.append("\n");

        out.append("\tpublic "+fullClassName(`sortName)+" "+make(`op)+"("+childArgsListWithType(`op)+") {\n");
        out.append("\t\taterm.ATerm[] args = new aterm.ATerm[] {"+childArgsList(`op)+"};\n");
        out.append("\t\treturn "+make(`op)+"("+fun(`op)+", args, factory.getEmpty());\n");
        out.append("\t}\n");
        out.append("\n");

        out.append("\tprotected "+fullClassName(`sortName)+" "+makefromterm(`op)+"(aterm.ATerm trm) {\n");
        out.append("\t\tjava.util.List children = trm.match("+pattern(`op)+");\n");
        out.append("\n");
        out.append("\t\tif(children != null) {\n");
        out.append("\t\t\treturn "+make(`op)+"(\n");
        out.append("\t\t\t"+fromTermChilds(`op,"children")+"\n");
        out.append("\t\t\t);\n");
        out.append("\t\t}\n");
        out.append("\t\telse {\n");
        out.append("\t\treturn null;\n");
        out.append("\t\t}\n");
        out.append("\t}\n");
        out.append("\n");

        out.append("\tpublic aterm.ATerm toTerm("+fullClassName(`opName)+" arg) {\n");
        out.append("\t\tjava.util.List args = new java.util.LinkedList();\n");
        out.append("\t\t"+toTermChilds(`op,"args", "arg"));
        out.append("\t\treturn factory.make("+pattern(`op)+", args);\n");
        out.append("\t}\n");
        out.append("\n");
      }
    }

    // methods for the sorts
    %match(GomClassList sorts) {
      concGomClass(_*,sort@SortClass[className=sortName,operators=sortOperators],_*) -> {
        out.append("\tpublic "+fullClassName(`sortName)+" "+makesortfromterm(`sortName)+"(aterm.ATerm trm) {\n");
        out.append("\t\t"+fullClassName(`sortName)+" tmp;\n");
        
        while(!`sortOperators.isEmpty()) {
          ClassName operatorName = `sortOperators.getHead();
          `sortOperators = `sortOperators.getTail();
          out.append("\t\ttmp = "+makefromterm(`sortName,operatorName)+"(trm);\n");
          out.append("\t\tif (tmp != null) {\n");
          out.append("\t\t\treturn tmp;\n");
          out.append("\t\t}\n");
        }
        out.append("\t\tthrow new IllegalArgumentException(\"This is not a "+className(`sortName)+"\");\n");
        out.append("\t}\n");
        out.append("\n");
      }
    }

    out.append("/* FromFile, FromString,...*/\n");
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
		throw new GomRuntimeException("TemplateFactory:fun got a strange operatorclass: "+op); 
  }
  private String proto(GomClass op) {
    %match(GomClass op) {
      OperatorClass[className=opName,sortName=sortName] -> {
        return "proto_"+className(`sortName)+"_"+className(`opName);
      }
    }
		throw new GomRuntimeException("TemplateFactory:proto got a strange operatorclass: "+op); 
  }
  private String pattern(GomClass op) {
    %match(GomClass op) {
      OperatorClass[className=opName,sortName=sortName] -> {
        return "pattern_"+className(`sortName)+"_"+className(`opName);
      }
    }
		throw new GomRuntimeException("TemplateFactory:pattern got a strange operatorclass: "+op); 
  }
  private String atermPattern(GomClass op) {
    %match(GomClass op) {
      OperatorClass[className=name,slots=slots] -> {
        String res = className(`name) + "(";
        String args = "";
        SlotFieldList slotList = `slots;
        while(!slotList.isEmpty()) {
          SlotField head = slotList.getHead();
          slotList = slotList.getTail();
          %match(SlotField head) {
            SlotField[domain=domain] -> {
              if (!args.equals("")) {
                args+= ", ";
              }
              // TODO: handle builtins
              args+= "<term>";
            }
          }
        }
        return "\"" + res + args + ")\"";
      }
    }
		throw new GomRuntimeException("TemplateFactory:childArgsList got a strange operatorclass: "+op); 
  }
  private String childArgsList(GomClass op) {
    %match(GomClass op) {
      OperatorClass[slots=slots] -> {
        String res = "";
        SlotFieldList slotList = `slots;
        while(!slotList.isEmpty()) {
          SlotField head = slotList.getHead();
          slotList = slotList.getTail();
          %match(SlotField head) {
            SlotField[name=name] -> {
              if (!res.equals("")) {
                res+= ", ";
              }
              res+= "_"+`name;
            }
          }
        }
        return res;
      }
    }
		throw new GomRuntimeException("TemplateFactory:childArgsList got a strange operatorclass: "+op); 
  }
  private String childArgsListWithType(GomClass op) {
    %match(GomClass op) {
      OperatorClass[slots=slots] -> {
        String res = "";
        SlotFieldList slotList = `slots;
        while(!slotList.isEmpty()) {
          SlotField head = slotList.getHead();
          slotList = slotList.getTail();
          %match(SlotField head) {
            SlotField[name=name, domain=domain] -> {
              if (!res.equals("")) {
                res+= ", ";
              }
              res+= fullClassName(`domain) + " _"+`name;
            }
          }
        }
        return res;
      }
    }
		throw new GomRuntimeException("TemplateFactory:childArgsListWithType got a strange operatorclass: "+op); 
  }
  private String atermFunArgs(GomClass op) {
    %match(GomClass op) {
      OperatorClass[className=opName,sortName=sortName] -> {
        return "\"_"+className(`sortName)+"_"+className(`opName)+"\", 0, false";
      }
    }
		throw new GomRuntimeException("TemplateFactory:atermFunArgs got a strange operatorclass: "+op); 
  }
  private String make(GomClass op) {
    %match(GomClass op) {
      OperatorClass[className=opName,sortName=sortName] -> {
        return "make"+className(`opName)+"_"+className(`sortName);
      }
    }
		throw new GomRuntimeException("TemplateFactory:make got a strange operatorclass: "+op); 
  }
  private String makefromterm(GomClass op) {
    %match(GomClass op) {
      OperatorClass[className=opName,sortName=sortName] -> {
        return makefromterm(`sortName,`opName);
      }
    }
		throw new GomRuntimeException("TemplateFactory:makefromterm got a strange operatorclass: "+op); 
  }
  private String makefromterm(ClassName sortName, ClassName opName) {
    return className(`sortName)+"_"+className(`opName)+"FromTerm";
  }

  private String fromTermChilds(GomClass op, String list) {
    %match(GomClass op) {
      OperatorClass[slots=slots] -> {
        String res = "";
        int index = 0;
        SlotFieldList slotList = `slots;
        while(!slotList.isEmpty()) {
          SlotField head = slotList.getHead();
          slotList = slotList.getTail();
          %match(SlotField head) {
            SlotField[domain=domain] -> {
              if (!res.equals("")) {
                res+= ", ";
              }
              res+= makesortfromterm(`domain) + "((aterm.ATerm) "+list+".get("+index+"))";
              index++;
            }
          }
        }
        return res;
      }
    }
		throw new GomRuntimeException("TemplateFactory:childArgsListWithType got a strange operatorclass: "+op); 
  }
  private String makesortfromterm(ClassName clsName) {
    %match(ClassName clsName) {
      ClassName[name=name] -> {
        return `name+"FromTerm";
      }
    }
    throw new GomRuntimeException("TemplateFactory:makesortfromterm got a strange ClassName");
  }
  private String toTermChilds(GomClass op, String list, String arg) {
    %match(GomClass op) {
      OperatorClass[slots=slots] -> {
        String res = "";
        SlotFieldList slotList = `slots;
        while(!slotList.isEmpty()) {
          SlotField head = slotList.getHead();
          slotList = slotList.getTail();
          res+= list+".add("+arg+"."+getMethod(head)+"().toTerm());\n";
        }
        return res;
      }
    }
		throw new GomRuntimeException("TemplateFactory:childArgsListWithType got a strange operatorclass: "+op); 
  }
}
