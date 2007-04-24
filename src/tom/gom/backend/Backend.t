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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.gom.GomMessage;
import tom.gom.tools.error.GomRuntimeException;

import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

public class Backend {
  TemplateFactory templatefactory;
  private File tomHomePath;
  private List importList = null;
  private boolean strategySupport = true;

  %include { ../adt/objects/Objects.tom }
  %include { mustrategy.tom }

  Backend(TemplateFactory templatefactory,
          File tomHomePath,
          boolean strategySupport,
          List importList) {
    this.templatefactory = templatefactory;
    this.tomHomePath = tomHomePath;
    this.strategySupport = strategySupport;
    this.importList = importList;
  }

  public int generate(GomClassList classList) {
    int errno = 0;
    Set mappingSet = new HashSet();
    Map generators =
      new HashMap();
    // prepare stuff for the mappings
    %match(GomClassList classList) {
      concGomClass(_*,
          gomclass@TomMapping[ClassName=className@ClassName(pkg,name)],
          _*) -> {
        ClassName smappingclass = `ClassName(pkg,"_"+name);
        GomClass nGomClass =
          `gomclass.setClassName(smappingclass);
        TemplateClass stratMapping =
          new tom.gom.backend.strategy.StratMappingTemplate(nGomClass);
        generators.put(smappingclass,stratMapping);

        TemplateClass mapping = null;
        if(strategySupport) {
          mapping =
            templatefactory.makeTomMappingTemplate(`gomclass,stratMapping);
        } else {
          mapping =
            templatefactory.makeTomMappingTemplate(`gomclass,null);
        }
        mappingSet.add(mapping);
        generators.put(`className,mapping);
      }
    }
    // generate a class for each element of the list
    while (!classList.isEmptyconcGomClass()) {
      GomClass gomclass = classList.getHeadconcGomClass();
      classList = classList.getTailconcGomClass();
      errno += generateClass(gomclass,generators);
    }
    /* The mappings may need to access generators */
    Iterator it = mappingSet.iterator();
    while (it.hasNext()) {
      ((MappingTemplateClass)it.next()).addTemplates(generators);
    }
    it = generators.keySet().iterator();
    while (it.hasNext()) {
      ((TemplateClass)generators.get(it.next())).generateFile();
    }

    return 1;
  }

  /*
   * Create template classes for the different classes to generate
   */
  public int generateClass(
      GomClass gomclass,
      Map generators) {
    %match(GomClass gomclass) {
      TomMapping[ClassName=className] -> {
        /* It was processed by the caller: check it is already in generators */
        if (!generators.containsKey(`className)) {
          throw new GomRuntimeException(
              "Mapping should be processed before generateClass is called");
        }
        return 1;
      }
      FwdClass[ClassName=className] -> {
        TemplateClass fwd = templatefactory.makeForwardTemplate(gomclass);
        generators.put(`className,fwd);
        return 1;
      }
      VisitableFwdClass[ClassName=className] -> {
        TemplateClass visitablefwd =
          templatefactory.makeVisitableForwardTemplate(gomclass);
        generators.put(`className,visitablefwd);
        return 1;
      }
      VisitorClass[ClassName=className] -> {
        TemplateClass visitor = templatefactory.makeVisitorTemplate(gomclass);
        generators.put(`className,visitor);
        return 1;
      }
      AbstractTypeClass[ClassName=className,Mapping=mapping] -> {
        TemplateClass abstracttype =
          templatefactory.makeAbstractTypeTemplate(
              tomHomePath,
              importList,
              gomclass,
              (TemplateClass)generators.get(`mapping));
        generators.put(`className,abstracttype);
        return 1;
      }
      SortClass[ClassName=className,Mapping=mapping] -> {
        TemplateClass sort =
          templatefactory.makeSortTemplate(
              tomHomePath,
              importList,
              gomclass,
              (TemplateClass)generators.get(`mapping));
        generators.put(`className,sort);
        return 1;
      }
      OperatorClass[ClassName=className,
                    Mapping=mapping,
                    Slots=slots] -> {
        TemplateClass operator = templatefactory.makeOperatorTemplate(
            tomHomePath,
            importList,
            gomclass,
            (TemplateClass)generators.get(`mapping));
        generators.put(`className,operator);

        TemplateClass sOpStrat =
          new tom.gom.backend.strategy.SOpTemplate(gomclass);
        sOpStrat.generateFile();

        TemplateClass makeOpStrat = new tom.gom.backend.strategy.MakeOpTemplate(gomclass);
        makeOpStrat.generateFile();
       return 1;
      }
      VariadicOperatorClass[ClassName=className,
                            Mapping=mapping,
                            Empty=empty,
                            Cons=cons] -> {
        TemplateClass operator =
          templatefactory.makeVariadicOperatorTemplate(
              tomHomePath,
              importList,
              gomclass,
              (TemplateClass)generators.get(`mapping));
        generators.put(`className,operator);
        /* Generate files for cons and empty */
        int ret = 1;
        ret+=generateClass(`empty,generators);
        ret+=generateClass(`cons,generators);

        return ret;
      }
    }
    throw new GomRuntimeException("Trying to generate code for a strange class: "+gomclass);
  }
}
