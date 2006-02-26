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

import tom.gom.tools.error.GomRuntimeException;

import tom.gom.adt.objects.types.*;

public abstract class TemplateFactory {

  public static TemplateFactory getFactory(String mode) {
    if (mode.equals("apigen")) {
      return new ApigenTemplateFactory();
    } else if (mode.equals("shared")) {
      return new SharedTemplateFactory();
    } else {
      throw new GomRuntimeException("Output mode "+mode+" not supported");
    }
  }

  public abstract TemplateClass makeTomMappingTemplate(ClassName className, GomClassList sortClasses, GomClassList opClasses);
  public abstract TemplateClass makeForwardTemplate(ClassName className, ClassName visitor, ClassName abstractType, GomClassList sortClasses, GomClassList opClasses);
  public abstract TemplateClass makeForwardVoidTemplate(ClassName className, ClassName visitor, ClassName abstractType, GomClassList sortClasses, GomClassList opClasses);
  public abstract TemplateClass makeVisitableForwardTemplate(ClassName className, ClassName forward);
  public abstract TemplateClass makeVisitorTemplate(ClassName className, GomClassList sortClasses, GomClassList opClasses);
  public abstract TemplateClass makeAbstractTypeTemplate(ClassName className, ClassName factory, ClassName visitor, ClassNameList sortList);
  public abstract TemplateClass makeSortTemplate(ClassName className, ClassName factory, ClassName abstractType, ClassNameList operators, SlotFieldList slots);
  public abstract TemplateClass makeOperatorTemplate(ClassName className, ClassName factory, ClassName abstractType, ClassName sort, ClassName visitor, SlotFieldList slots);
  public abstract TemplateClass makeFactoryTemplate(ClassName className, ClassNameList importedFactories, GomClassList sortClasses, GomClassList operatorClasses);

}

class ApigenTemplateFactory extends TemplateFactory {

  public TemplateClass makeTomMappingTemplate(ClassName className, GomClassList sortClasses, GomClassList opClasses) {
    return new tom.gom.backend.apigen.TemplateMapping(className,sortClasses,opClasses);
  }
  public TemplateClass makeForwardTemplate(ClassName className, ClassName visitor, ClassName abstractType, GomClassList sortClasses, GomClassList opClasses) {
    return new tom.gom.backend.apigen.TemplateFwd(className, visitor, abstractType, sortClasses, opClasses);
  }
  public TemplateClass makeForwardVoidTemplate(ClassName className, ClassName visitor, ClassName abstractType, GomClassList sortClasses, GomClassList opClasses) {
    return new tom.gom.backend.apigen.TemplateVoidFwd(className, visitor, abstractType, sortClasses, opClasses);
  }
  public TemplateClass makeVisitableForwardTemplate(ClassName className, ClassName forward) {
    return new tom.gom.backend.apigen.TemplateVisitableFwd(className,forward);
  }
  public TemplateClass makeVisitorTemplate(ClassName className, GomClassList sortClasses, GomClassList opClasses) {
    return new tom.gom.backend.apigen.TemplateVisitor(className,sortClasses,opClasses);
  }
  public TemplateClass makeAbstractTypeTemplate(ClassName className, ClassName factory, ClassName visitor, ClassNameList sortList) {
    return new tom.gom.backend.apigen.TemplateAbstractType(className,factory,visitor,sortList);
  }
  public TemplateClass makeSortTemplate(ClassName className, ClassName factory, ClassName abstractType, ClassNameList operators, SlotFieldList slots) {
    return new tom.gom.backend.apigen.TemplateSort(className,factory,abstractType,operators,slots);
  }
  public TemplateClass makeOperatorTemplate(ClassName className, ClassName factory, ClassName abstractType, ClassName sort, ClassName visitor, SlotFieldList slots) {
    return new tom.gom.backend.apigen.TemplateOperator(className,factory,abstractType,sort,visitor,slots);
  }
  public TemplateClass makeFactoryTemplate(ClassName className, ClassNameList importedFactories, GomClassList sortClasses, GomClassList operatorClasses) {
    return new tom.gom.backend.apigen.TemplateFactory(className,importedFactories,sortClasses,operatorClasses);
  }
}

class SharedTemplateFactory extends TemplateFactory {

  public TemplateClass makeTomMappingTemplate(ClassName className, GomClassList sortClasses, GomClassList opClasses) {
    return new tom.gom.backend.shared.NullTemplate(className);
  }
  public TemplateClass makeForwardTemplate(ClassName className, ClassName visitor, ClassName abstractType, GomClassList sortClasses, GomClassList opClasses) {
    return new tom.gom.backend.shared.NullTemplate(className);
  }
  public TemplateClass makeForwardVoidTemplate(ClassName className, ClassName visitor, ClassName abstractType, GomClassList sortClasses, GomClassList opClasses) {
    return new tom.gom.backend.shared.NullTemplate(className);
  }
  public TemplateClass makeVisitableForwardTemplate(ClassName className, ClassName forward) {
    return new tom.gom.backend.shared.NullTemplate(className);
  }
  public TemplateClass makeVisitorTemplate(ClassName className, GomClassList sortClasses, GomClassList opClasses) {
    return new tom.gom.backend.shared.NullTemplate(className);
  }
  public TemplateClass makeAbstractTypeTemplate(ClassName className, ClassName factory, ClassName visitor, ClassNameList sortList) {
    return new tom.gom.backend.shared.TemplateAbstractType(className, factory, visitor, sortList);
  }
  public TemplateClass makeSortTemplate(ClassName className, ClassName factory, ClassName abstractType, ClassNameList operators, SlotFieldList slots) {
    return new tom.gom.backend.shared.TemplateSort(className,factory,abstractType,operators,slots);
  }
  public TemplateClass makeOperatorTemplate(ClassName className, ClassName factory, ClassName abstractType, ClassName sort, ClassName visitor, SlotFieldList slots) {
    return new tom.gom.backend.shared.TemplateOperator(className,factory,abstractType,sort,visitor,slots);
  }
  public TemplateClass makeFactoryTemplate(ClassName className, ClassNameList importedFactories, GomClassList sortClasses, GomClassList operatorClasses) {
    return new tom.gom.backend.shared.NullTemplate(className);
  }
}
