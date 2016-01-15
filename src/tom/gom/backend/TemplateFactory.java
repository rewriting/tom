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
package tom.gom.backend;

import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;
import tom.platform.OptionManager;
import tom.gom.tools.GomEnvironment;

public abstract class TemplateFactory {

  protected GomEnvironment gomEnvironment;

  public GomEnvironment getGomEnvironment() {
    return gomEnvironment;
  }

  public TemplateFactory getFactory(OptionManager manager) {
    String mode = (String) manager.getOptionValue("generator");
    if (mode.equals("shared")) {
      return new SharedTemplateFactory(manager,getGomEnvironment());
    } else {
      throw new GomRuntimeException("Output mode "+mode+" not supported");
    }
  }

  public abstract MappingTemplateClass makeTomMappingTemplate(GomClass gomClass,TemplateClass strategyMapping, GomEnvironment gomEnvironment);
  public abstract TemplateClass makeAbstractTypeTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, boolean maximalsharing, GomEnvironment gomEnvironment);
  public abstract TemplateClass makeSortTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, boolean maximalsharingi, GomEnvironment gomEnvironment);
  public abstract TemplateClass makeOperatorTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, boolean multithread, boolean maximalsharing, boolean jmicompatible, GomEnvironment gomEnvironment);
  public abstract TemplateClass makeVariadicOperatorTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, GomEnvironment gomEnvironment);
}

class SharedTemplateFactory extends TemplateFactory {

  private OptionManager manager;
  SharedTemplateFactory(OptionManager manager) {
    this.manager = manager;
  }
  SharedTemplateFactory(OptionManager manager, GomEnvironment gomEnvironment) {
    this.manager = manager;
    this.gomEnvironment = gomEnvironment;
  }

  public MappingTemplateClass makeTomMappingTemplate(
      GomClass gomClass,
      TemplateClass strategyMapping,
      GomEnvironment gomEnvironment) {
    return new tom.gom.backend.shared.MappingTemplate(gomClass,strategyMapping,gomEnvironment);
  }

  public TemplateClass makeAbstractTypeTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, boolean maximalsharing, GomEnvironment gomEnvironment) {
    return new tom.gom.backend.shared.AbstractTypeTemplate(tomHomePath, manager, importList, gomClass, mapping, maximalsharing, gomEnvironment);
  }
  public TemplateClass makeSortTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, boolean maximalsharing, GomEnvironment gomEnvironment) {
    return new tom.gom.backend.shared.SortTemplate(tomHomePath, manager, maximalsharing, importList, gomClass, mapping, gomEnvironment);
  }
  public TemplateClass makeOperatorTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, boolean multithread, boolean maximalsharing, boolean jmicompatible, GomEnvironment gomEnvironment) {
    return new tom.gom.backend.shared.OperatorTemplate(tomHomePath, manager, importList, gomClass,mapping,multithread,maximalsharing,jmicompatible,gomEnvironment);
  }
  public TemplateClass makeVariadicOperatorTemplate(java.io.File tomHomePath, java.util.List importList, GomClass gomClass, TemplateClass mapping, GomEnvironment gomEnvironment) {
    return new tom.gom.backend.shared.VariadicOperatorTemplate(tomHomePath, manager, importList, gomClass, mapping, gomEnvironment);
  }
}
