/*
 * Gom
 *
 * Copyright (c) 2006-2016, Universite de Lorraine, Inria
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

package tom.gom.backend.strategy;

import java.io.*;
import java.util.logging.*;
import tom.gom.backend.TemplateClass;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;

public class IsOpTemplate extends TemplateClass {
  ClassName operator;
  SlotFieldList slotList;

       

  /*
   * The argument is an operator class, and this template generates the
   * assotiated _Op strategy
   */
  public IsOpTemplate(GomClass gomClass, GomEnvironment gomEnvironment) {
    super(gomClass,gomEnvironment);
    ClassName clsName = this.className;
    { /* unamed block */{ /* unamed block */if ( (clsName instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )clsName) instanceof tom.gom.adt.objects.types.classname.ClassName) ) {

        String newpkg =  (( tom.gom.adt.objects.types.ClassName )clsName).getPkg() .replaceFirst(".types.",".strategy.");
        String newname = "Is_"+ (( tom.gom.adt.objects.types.ClassName )clsName).getName() ;
        this.className =  tom.gom.adt.objects.types.classname.ClassName.make(newpkg, newname) ;
      }}}}{ /* unamed block */{ /* unamed block */if ( (gomClass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomClass) instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) {



        this.operator =  (( tom.gom.adt.objects.types.GomClass )gomClass).getClassName() ;
        this.slotList =  (( tom.gom.adt.objects.types.GomClass )gomClass).getSlotFields() ;
        return;
      }}}}

    throw new GomRuntimeException(
        "Wrong argument for IsOpTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
writer.write("\npackage "+getPackage()+";\n\npublic class "+className()+" extends tom.library.sl.AbstractStrategyCombinator {\n  private static final String msg = \"Not an "+className(operator)+"\";\n\n  public "+className()+"() {\n    initSubterm();\n  }\n\n  @SuppressWarnings(\"unchecked\")\n  public <T extends tom.library.sl.Visitable> T visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {\n    return (T) visit(envt,tom.library.sl.VisitableIntrospector.getInstance());\n  }\n\n  public <T extends tom.library.sl.Visitable> T visit(T any) throws tom.library.sl.VisitFailure{\n    return visit(any,tom.library.sl.VisitableIntrospector.getInstance());\n  }\n\n  public <T extends tom.library.sl.Visitable> T visitLight(T any) throws tom.library.sl.VisitFailure {\n    return visitLight(any,tom.library.sl.VisitableIntrospector.getInstance());\n  }\n\n  public <T> T visitLight(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {\n    if(any instanceof "+fullClassName(operator)+") {\n     return any;\n    } else {\n      throw new tom.library.sl.VisitFailure(msg);\n    }\n  }\n\n  public int visit(tom.library.sl.Introspector i) {\n    Object any = environment.getSubject();\n    if(any instanceof "+fullClassName(operator)+") {\n     return tom.library.sl.Environment.SUCCESS;\n    } else {\n      return tom.library.sl.Environment.FAILURE;\n    }\n  }\n}\n"







































);
}

public String generateMapping() {

  String whenName = className().replaceFirst("Is_","When_");
  return "\n  %op Strategy "+whenName+"(s:Strategy) {\n    make(s) { `Sequence("+className()+"(),s) }\n  }\n\n  %op Strategy "+className()+"() {\n    make() { new "+fullClassName()+"()}\n  }\n  "







;
}

/** the class logger instance*/
private Logger getLogger() {
  return Logger.getLogger(getClass().getName());
}
}
