/*
 * Gom
 *
 * Copyright (c) 2006-2008, INRIA
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

public class Backend {
  TemplateFactory templatefactory;
  private File tomHomePath;
  private List importList = null;
  private boolean strategySupport = true;
  private boolean multithread = false;
  private boolean maximalsharing = true;

  %include { ../adt/objects/Objects.tom }
  %include { sl.tom }

  Backend(TemplateFactory templatefactory,
          File tomHomePath,
          boolean strategySupport,
          boolean multithread,
          boolean nosharing,
          List importList) {
    this.templatefactory = templatefactory;
    this.tomHomePath = tomHomePath;
    this.strategySupport = strategySupport;
    this.multithread = multithread;
    this.maximalsharing = ! nosharing;
    this.importList = importList;
  }

  public int generate(GomClassList classList) {
    int errno = 0;
    Set<MappingTemplateClass> mappingSet = new HashSet<MappingTemplateClass>();
    Map<ClassName,TemplateClass> generators = new HashMap<ClassName,TemplateClass>();
    // prepare stuff for the mappings
    %match(GomClassList classList) {
      ConcGomClass(_*,
          gomclass@TomMapping[ClassName=className@ClassName(pkg,name)],
          _*) -> {
        ClassName smappingclass = `ClassName(pkg,"_"+name);
        GomClass nGomClass =
          `gomclass.setClassName(smappingclass);
        TemplateClass stratMapping =
          new tom.gom.backend.strategy.StratMappingTemplate(nGomClass);
        generators.put(smappingclass,stratMapping);

        MappingTemplateClass mapping = null;
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
    while (!classList.isEmptyConcGomClass()) {
      GomClass gomclass = classList.getHeadConcGomClass();
      classList = classList.getTailConcGomClass();
      errno += generateClass(gomclass,generators);
    }
    /* The mappings may need to access generators */
    Iterator<MappingTemplateClass> it = mappingSet.iterator();
    while (it.hasNext()) {
      it.next().addTemplates(generators);
    }
    Iterator<ClassName> itc = generators.keySet().iterator();
    while (itc.hasNext()) {
      generators.get(itc.next()).generateFile();
    }

    return 1;
  }

  /*
   * Create template classes for the different classes to generate
   */
  public int generateClass(GomClass gomclass, Map<ClassName,TemplateClass> generators) {
    %match(GomClass gomclass) {
      TomMapping[ClassName=className] -> {
        /* It was processed by the caller: check it is already in generators */
        if (!generators.containsKey(`className)) {
          throw new GomRuntimeException(
              "Mapping should be processed before generateClass is called");
        }
        return 1;
      }
      AbstractTypeClass[ClassName=className,Mapping=mapping] -> {
        TemplateClass abstracttype =
          templatefactory.makeAbstractTypeTemplate(
              tomHomePath,
              importList,
              gomclass,
              (TemplateClass)generators.get(`mapping),
              maximalsharing);
        generators.put(`className,abstracttype);
        return 1;
      }
      SortClass[ClassName=className,Mapping=mapping] -> {
        TemplateClass sort =
          templatefactory.makeSortTemplate(
              tomHomePath,
              importList,
              gomclass,
              (TemplateClass)generators.get(`mapping),
              maximalsharing);
        generators.put(`className,sort);
        return 1;
      }
      OperatorClass[ClassName=className,Mapping=mapping] -> {
        TemplateClass operator = templatefactory.makeOperatorTemplate(
            tomHomePath,
            importList,
            gomclass,
            (TemplateClass)generators.get(`mapping),
            multithread,
            maximalsharing);
        generators.put(`className,operator);

        TemplateClass sOpStrat =
          new tom.gom.backend.strategy.SOpTemplate(gomclass);
        sOpStrat.generateFile();

        TemplateClass isOpStrat =
          new tom.gom.backend.strategy.IsOpTemplate(gomclass);
        isOpStrat.generateFile();

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
