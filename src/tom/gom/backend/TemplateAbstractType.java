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

public class TemplateAbstractType extends TemplateClass {
  ClassName factoryName;
  ClassName visitor;
  ClassNameList sortList;

  TemplateAbstractType(ClassName className, ClassName factoryName, ClassName visitor, ClassNameList sortList) {
    super(className);
    this.factoryName = factoryName;
    this.visitor = visitor;
    this.sortList = sortList;
  }

  /* We may want to return the stringbuffer itself in the future, or directly write to a Stream */
  public String generate() {
    StringBuffer out = new StringBuffer();

    out.append("package "+getPackage()+";\n");
    out.append("\n");
    out.append("public abstract class "+className()+" extends aterm.pure.ATermApplImpl {\n");
    out.append("\tprotected aterm.ATerm term;\n");
    out.append("\n");
    out.append("\tprivate "+fullClassName(factoryName)+" abstractTypeFactory;\n");
    out.append("\n");
    out.append("\tpublic "+className()+"("+fullClassName(factoryName)+" abstractTypeFactory) {\n");
    out.append("\t\tsuper(abstractTypeFactory.getPureFactory());\n");
    out.append("\t\tthis.abstractTypeFactory = abstractTypeFactory;\n");
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
    out.append("\tabstract public aterm.ATerm toTerm();\n");
    out.append("\n");
    out.append("\tpublic String toString() {\n");
    out.append("\t\treturn toTerm().toString();\n");
    out.append("\t}\n");
    out.append("\n");
    out.append("\tprotected void setTerm(aterm.ATerm term) {\n");
    out.append("\t\tthis.term = term;\n");
    out.append("\t}\n");
    out.append("\n");
    out.append("\tpublic "+fullClassName(factoryName)+" get"+className(factoryName)+"() {\n");
    out.append("\t\treturn abstractTypeFactory;\n");
    out.append("\t}\n");
    out.append("\n");
    out.append("\tabstract public "+className()+" accept("+fullClassName(visitor)+" v) throws jjtraveler.VisitFailure;\n");
    out.append("\n");
    
    // generate a visit for each sort
    while (!sortList.isEmpty()) {
      ClassName sortName = sortList.getHead();
      sortList = sortList.getTail();

      out.append("\tpublic boolean isSort"+className(sortName)+"() {\n");
      out.append("\t\treturn false;\n");
      out.append("\t}\n");
      out.append("\n");
    }

    out.append("}");
    
    return out.toString();
  }
}
