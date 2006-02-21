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

public class TemplateOperator extends TemplateClass {
  ClassName factoryName;
  ClassName abstractType;
  ClassName sortName;
  ClassName visitor;
  SlotFieldList slotList;

  TemplateOperator(ClassName className, ClassName factoryName, ClassName abstractType, ClassName sortName, ClassName visitor, SlotFieldList slots) {
    super(className);
    this.factoryName = factoryName;
    this.abstractType = abstractType;
    this.sortName = sortName;
    this.visitor = visitor;
    this.slotList = slots;
  }

  /* We may want to return the stringbuffer itself in the future, or directly write to a Stream */
  public String generate() {
    StringBuffer out = new StringBuffer();

    out.append("package "+getPackage()+";\n");
    out.append("\n");
    out.append("public class "+className()+" extends "+fullClassName(sortName)+" implements jjtraveler.Visitable {\n");
    out.append("\tpublic "+className()+"("+fullClassName(factoryName)+" factory) {\n");
    out.append("\t\tsuper(factory);\n");
    out.append("\t}\n");
    out.append("\n");
    out.append("\tpublic void init(int hashCode, aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] args) {\n");
    out.append("\t\tsuper.init(hashCode, annos, fun, args);\n");
    out.append("\t}\n");
    out.append("\n");
    out.append("\tpublic void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] args) {\n");
    out.append("\t\tsuper.initHashCode(annos, fun, args);\n");
    out.append("\t}\n");
    out.append("\n");

    out.append("\tpublic shared.SharedObject duplicate() {\n");
    out.append("\t\t"+className()+" clone = new "+className()+"(get"+className(factoryName)+"());\n");
    out.append("\t\tclone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());\n");
    out.append("\t\treturn clone();\n");
    out.append("\t}\n");
    out.append("\n");

    out.append("\tpublic boolean equivalent(shared.SharedObject peer) {\n");
    out.append("\t\tif (peer instanceof "+className()+") {\n");
    out.append("\t\t\treturn super.isEqual(peer);\n");
    out.append("\t\t}\n");
    out.append("\t\treturn false;\n");
    out.append("\t}\n");
    out.append("\n");

    out.append("\tprotected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {\n");
    out.append("\t\treturn get"+className(factoryName)+"().make"+className()+"_"+className()+"(fun, args, annos);\n");
    out.append("\t}\n");
    out.append("\n");

    out.append("\tpublic aterm.ATerm toTerm() {\n");
    out.append("\t\tif (term == null) {\n");
    out.append("\t\t\tterm = get"+className(factoryName)+"().toTerm(this);\n");
    out.append("\t\t}\n");
    out.append("\t\treturn term;\n");
    out.append("\t}\n");
    out.append("\n");

    out.append("\tpublic boolean is"+className()+"() {\n");
    out.append("\t\treturn true;\n");
    out.append("\t}\n");
    out.append("\n");

    out.append("\tpublic "+fullClassName(abstractType)+" accept("+fullClassName(visitor)+" v) throws jjtraveler.VisitFailure {\n");
    out.append("\t\treturn v.visit_"+className(sortName)+"_"+className()+"(this);\n");
    out.append("\t}\n");
    out.append("\n");

    // methods for each slot
    int count = 0;
    SlotFieldList consum = slotList;
    while (!consum.isEmpty()) {
      SlotField slot = consum.getHead();
      consum = consum.getTail();

      out.append("\tprivate static final int "+index(slot)+" = "+count+";\n");

      out.append("\tpublic boolean "+hasMethod(slot)+"() {\n");
      out.append("\t\treturn true;\n");
      out.append("\t}\n");
      out.append("\n");
      
      // XXX: special treatment for builtin String and int
      out.append("\tpublic "+slotDomain(slot)+" "+getMethod(slot)+"() {\n");
      out.append("\t\treturn ((aterm.ATermAppl) getArgument("+index(slot)+"));\n");
      out.append("\t}\n");
      out.append("\n");

      out.append("\tpublic "+className()+" "+setMethod(slot)+"("+slotDomain(slot)+" _arg) {\n");
      out.append("\t\treturn ("+fullClassName()+") super.setArgument(_arg, "+index(slot)+");\n");
      out.append("\t}\n");
      out.append("\n");

      count++;
    }

    // create the setArgument method
    // XXX: special treatment for builtins
    out.append("\tpublic aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {\n");
    out.append("\t\tswitch(i) {\n");
    out.append("\t\n");
    consum = slotList;
    while (!consum.isEmpty()) {
      SlotField slot = consum.getHead();
      consum = consum.getTail();

      out.append("\t\t\tcase "+index(slot)+":\n");
      out.append("\t\t\t\tif(! (arg instanceof "+slotDomain(slot)+")) {\n");
      out.append("\t\t\t\t\tthrow new RuntimeException(\"Argument "+slot.getName()+" of a "+className()+" should have type "+slotDomain(slot)+"\");\n");
      out.append("\t\t\t\t}\n");
      out.append("\t\t\t\tbreak;\n");
    }
    out.append("\t\t\tdefault: throw new RuntimeException(\""+className()+" does not have an argument at \" + i);\n");
    out.append("\t\t}\n");
    out.append("\t\treturn super.setArgument(arg, i);\n");
    out.append("\t}\n");

    out.append("}");
    
    return out.toString();
  }
}
