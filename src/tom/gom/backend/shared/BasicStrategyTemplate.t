 /* Gom
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

package tom.gom.backend.shared;

import tom.gom.backend.TemplateClass;
import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class BasicStrategyTemplate extends TemplateClass {
  ClassName visitor;
  ClassNameList importedVisitors;
  ClassName abstractType;
  ClassNameList importedAbstractTypes;
  GomClassList sortClasses;
  GomClassList operatorClasses;


  %include { ../../adt/objects/Objects.tom }

  public BasicStrategyTemplate(GomClass basic) {
    super(basic);
    %match(basic) {
      VisitableFwdClass[Visitor=visitorClass,
               ImportedVisitors=importedVisitors,
               AbstractType=abstractType,
               ImportedAbstractTypes=imported,
               SortClasses=sortClasses,
               OperatorClasses=ops] -> {
        this.visitor = `visitorClass;
        this.importedVisitors = `importedVisitors;
        this.abstractType = `abstractType;
        this.importedAbstractTypes = `imported;
        this.sortClasses = `sortClasses;
        this.operatorClasses = `ops;
        return;
      }
    }
    throw new GomRuntimeException(
        "Wrong argument for BasicStrategyTemplate: " + basic);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
    writer.write(%[
package @getPackage()@;
import tom.library.sl.*;
   
  public class @className()@ implements tom.library.sl.Strategy,@ className(visitor)+importedVisitorList(importedVisitors) @ {
  private tom.library.sl.Environment environment;
  protected Strategy any;
  
  public @className()@(Strategy v) {
    this.any = v;
  }
    
  public int getChildCount() {
    return 1;
  }
    
  public Visitable getChildAt(int i) {
    switch (i) {
      case 0: return any;
      default: throw new IndexOutOfBoundsException();
    }
  }
    
  public Visitable setChildAt(int i, Visitable child) {
    switch (i) {
      case 0: any = (Strategy) child; return this;
      default: throw new IndexOutOfBoundsException();
    }
  }

  public Visitable[] getChildren() {
    return new Visitable[]{any};
  }

  public Visitable setChildren(Visitable[] children) {
    any = (Strategy) children[0];
    return this;
  }

  public tom.library.sl.Strategy accept(tom.library.sl.reflective.StrategyFwd v) throws tom.library.sl.VisitFailure {
    return v.visit_Strategy(this,tom.library.sl.VisitableIntrospector.getInstance());
  } 

  public tom.library.sl.Environment getEnvironment() {
    if(environment!=null) {
      return environment;
    } else {
      throw new java.lang.RuntimeException("environment not initialized");
    }
  }

  public void setEnvironment(tom.library.sl.Environment env) {
    this.environment = env;
  }
 
  public Visitable visitLight(Visitable any) throws VisitFailure {
    return (Visitable) visitLight(any,VisitableIntrospector.getInstance());
  }

  public Visitable visit(Environment envt) throws VisitFailure {
    return (Visitable) visit(envt,VisitableIntrospector.getInstance());
  }

  public Visitable visit(Visitable any) throws VisitFailure{
    return (Visitable) visit(any,VisitableIntrospector.getInstance());
  }
 
  public Visitable visit(Environment envt, Introspector i) throws VisitFailure {
    setEnvironment(envt);
    int status = visit(i);
    if(status == Environment.SUCCESS) {
      return (Visitable) environment.getSubject();
    } else {
      throw new VisitFailure();
    }
  }


  public Object visit(Object any, Introspector i) throws VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
    environment.setRoot(any);
    int status = visit(i);
    if(status == tom.library.sl.Environment.SUCCESS) {
      return environment.getRoot();
    } else {
      throw new VisitFailure();
    }
  }

  public int visit(Introspector i) {
    try {
      environment.setSubject(this.visitLight(environment.getSubject(),i));
      return tom.library.sl.Environment.SUCCESS;
    } catch(VisitFailure f) {
      return tom.library.sl.Environment.FAILURE;
    }
  }

  public Object visitLight(Object v, Introspector i) throws VisitFailure {
    if (v instanceof @fullClassName(abstractType)@) {
      return ((@fullClassName(abstractType)@) v).accept(this,i);
    }
]%);
generateDispatch(writer,importedAbstractTypes);
writer.write(%[
    else {
      return any.visitLight(v,i);
    }
  }
]%);
generateVisitMethods(writer);
writer.write(%[
}
]%);
}

  private void generateVisitMethods(java.io.Writer writer) throws java.io.IOException {
    // generate a visit for each sort
    %match(GomClassList sortClasses) {
      concGomClass(_*,SortClass[ClassName=sortName],_*) -> {

        writer.write(%[
  public @ fullClassName(`sortName) @ @visitMethod(`sortName)@(@fullClassName(`sortName)@ arg, Introspector i) throws VisitFailure {
   if(environment!=null) {
      //TODO: must be removed
      assert(arg.equals(environment.getSubject()));
   return (@fullClassName(`sortName)@) any.visit(environment,i);
   } else {
    return (@fullClassName(`sortName)@) any.visitLight(arg,i);
   }
 }
]%);
      }
    }
  }

  private void generateDispatch(java.io.Writer writer, ClassNameList types) throws java.io.IOException {
    while(!types.isEmptyconcClassName()) {
      writer.write(%[    else if (v instanceof @fullClassName(types.getHeadconcClassName())@) {
      return ((@fullClassName(types.getHeadconcClassName())@) v).accept(this,i);
    }]%);
      types = types.getTailconcClassName();
    }
  }
  
  private String importedVisitorList(ClassNameList list) {
    StringBuilder out = new StringBuilder();
    while(!list.isEmptyconcClassName()) {
      out.append(", "+fullClassName(list.getHeadconcClassName()));
      list = list.getTailconcClassName();
    }
    return out.toString();
  }

}
