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

import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;

public abstract class TemplateFactory {

  public static TemplateFactory getFactory(String mode) {
    if (mode.equals("shared")) {
      return new SharedTemplateFactory();
    } else {
      throw new GomRuntimeException("Output mode "+mode+" not supported");
    }
  }

  public abstract TemplateClass makeTomMappingTemplate(ClassName className, ClassName basicStrategy, GomClassList sortClasses, GomClassList opClasses);
  public abstract TemplateClass makeForwardTemplate(ClassName className, ClassName visitor, ClassNameList importedVisitors, ClassName abstractType, ClassNameList importedAbstract, GomClassList sortClasses, GomClassList opClasses);
  public abstract TemplateClass makeVisitableForwardTemplate(ClassName className, ClassName forward);
  public abstract TemplateClass makeVisitorTemplate(ClassName className, GomClassList sortClasses, GomClassList opClasses);
  public abstract TemplateClass makeAbstractTypeTemplate(ClassName className, ClassName visitor, ClassNameList sortList, HookList hooks);
  public abstract TemplateClass makeSortTemplate(ClassName className, ClassName abstractType, ClassName visitor, ClassNameList operators, ClassNameList variadicOperators, SlotFieldList slots, HookList hooks);
  public abstract TemplateClass makeOperatorTemplate(java.io.File tomHomePath, java.util.List importList, ClassName className, ClassName abstractType, ClassName extendsType, ClassName sort, ClassName visitor, SlotFieldList slots, HookList hooks,TemplateClass mapping);
  public abstract TemplateClass makeVariadicOperatorTemplate(ClassName className, ClassName abstractType, ClassName sort, GomClass empty, GomClass conc, HookList hooks);
}

class SharedTemplateFactory extends TemplateFactory {

  public TemplateClass makeTomMappingTemplate(ClassName className, ClassName basicStrategy, GomClassList sortClasses, GomClassList opClasses) {
    return new tom.gom.backend.shared.MappingTemplate(className,basicStrategy,sortClasses,opClasses);
  }
  public TemplateClass makeForwardTemplate(ClassName className, ClassName visitor,ClassNameList importedVisitors, ClassName abstractType, ClassNameList importedAbstract, GomClassList sortClasses, GomClassList opClasses) {
    return new tom.gom.backend.shared.ForwardTemplate(className, visitor, importedVisitors, abstractType, importedAbstract, sortClasses, opClasses);
  }
  public TemplateClass makeVisitableForwardTemplate(ClassName className, ClassName forward) {
    return new tom.gom.backend.shared.BasicStrategyTemplate(className,forward);
  }
  public TemplateClass makeVisitorTemplate(ClassName className, GomClassList sortClasses, GomClassList opClasses) {
    return new tom.gom.backend.shared.VisitorTemplate(className,sortClasses,opClasses);
  }
  public TemplateClass makeAbstractTypeTemplate(ClassName className, ClassName visitor, ClassNameList sortList, HookList hooks) {
    return new tom.gom.backend.shared.AbstractTypeTemplate(className, visitor, sortList,hooks);
  }
  public TemplateClass makeSortTemplate(ClassName className, ClassName
      abstractType, ClassName visitor, ClassNameList operators,
      ClassNameList variadicOperators, SlotFieldList slots,HookList hooks) {
    return new tom.gom.backend.shared.SortTemplate(className,abstractType,visitor,operators,variadicOperators,slots,hooks);
  }
  public TemplateClass makeOperatorTemplate(java.io.File tomHomePath, java.util.List importList, ClassName className, ClassName abstractType, ClassName extendsType, ClassName sort, ClassName visitor, SlotFieldList slots, HookList hooks,TemplateClass mapping) {
    return new tom.gom.backend.shared.OperatorTemplate(tomHomePath, importList, className,abstractType,extendsType,sort,visitor,slots,hooks,mapping);
  }
  public TemplateClass makeVariadicOperatorTemplate(ClassName
      className, ClassName abstractType, ClassName sort, GomClass
      empty, GomClass conc,HookList hooks) {
    return new tom.gom.backend.shared.VariadicOperatorTemplate(className,abstractType,sort,empty,conc,hooks);
  }
}
