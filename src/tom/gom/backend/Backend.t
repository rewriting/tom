/*
 * Gom
 *
 * Copyright (c) 2006-2015, Universite de Lorraine, Inria
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
import java.util.concurrent.*;

import tom.gom.tools.error.GomRuntimeException;
import tom.gom.tools.GomEnvironment;

import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;

public class Backend {
  TemplateFactory templatefactory;
  private File tomHomePath;
  private List importList = null;
  private int generateStratMapping = 0;
  private boolean multithread = false;
  private boolean maximalsharing = true;
  private boolean jmicompatible = false;
  private GomEnvironment gomEnvironment;

  %include { ../adt/objects/Objects.tom }
  %include { ../../library/mapping/java/sl.tom }

  Backend(TemplateFactory templatefactory,
          File tomHomePath,
          int generateStratMapping,
          boolean multithread,
          boolean nosharing,
          boolean jmicompatible,
          List importList,
          GomEnvironment gomEnvironment) {
    this.templatefactory = templatefactory;
    this.tomHomePath = tomHomePath;
    this.generateStratMapping = generateStratMapping;
    this.multithread = multithread;
    this.maximalsharing = !nosharing;
    this.jmicompatible = jmicompatible;
    this.importList = importList;
    this.gomEnvironment = gomEnvironment;
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public int generate(GomClassList classList) {
    int errno = 0;
    Set<MappingTemplateClass> mappingSet = new HashSet<MappingTemplateClass>();
    final Map<ClassName,TemplateClass> generators = new HashMap<ClassName,TemplateClass>();
    // prepare stuff for the mappings
    %match(classList) {
      ConcGomClass(_*,
          gomclass@TomMapping[ClassName=className@ClassName(pkg,name)],
          _*) -> {
        MappingTemplateClass mapping = null;
        if (generateStratMapping > 0) { // generate congruence strategies
          ClassName smappingclass = `ClassName(pkg,"_"+name);
          GomClass nGomClass = `gomclass.setClassName(smappingclass);
          TemplateClass stratMapping = new tom.gom.backend.strategy.StratMappingTemplate(nGomClass,getGomEnvironment(),generateStratMapping);
          if (1 == generateStratMapping) {
            // classical mode: generate extra-mapping in file.tom
            mapping = templatefactory.makeTomMappingTemplate(`gomclass,stratMapping,getGomEnvironment());
          } else {
            // expert mode: generate extra-mapping in _file.tom
            generators.put(smappingclass,stratMapping);
            mapping = templatefactory.makeTomMappingTemplate(`gomclass,null,getGomEnvironment());
          }
        } else {
          // do not generate congruence strategies
          mapping = templatefactory.makeTomMappingTemplate(`gomclass,null,getGomEnvironment());
        }
        mappingSet.add(mapping);
        generators.put(`className,mapping);
      }
    }
    // generate a class for each element of the list
    if (multithread) {
      while (!classList.isEmptyConcGomClass()) {
        final GomClass gomclass = classList.getHeadConcGomClass();
        classList = classList.getTailConcGomClass();
        errno += generateClass(gomclass,generators);
      }
      /* The mappings may need to access generators */
      for(final MappingTemplateClass templateClass : mappingSet) {
        templateClass.addTemplates(generators);
      }

      /* generation of files in parallel */
      try {
        int poolSize = 4;
        int maxPoolSize = 8;
        long keepAliveTime = 10;
        final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(generators.size());
        ThreadPoolExecutor exec = new ThreadPoolExecutor(poolSize, maxPoolSize,
            keepAliveTime, TimeUnit.SECONDS, queue);
        //ExecutorService exec = Executors.newCachedThreadPool();
        for (final ClassName clsName : generators.keySet()) {
          //System.out.println("generateFile: "+clsName.getName());
          //generators.get(clsName).generateFile();
          exec.execute(new Runnable() {
              public void run() {
              generators.get(clsName).generateFile();
              }
              });
          //System.out.println("Task count.." + queue.size());
        }
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        //System.out.println("Largest pool size.." + exec.getLargestPoolSize());
      } catch(InterruptedException e) {
        e.printStackTrace();
      }

    } else {
      while (!classList.isEmptyConcGomClass()) {
        final GomClass gomclass = classList.getHeadConcGomClass();
        classList = classList.getTailConcGomClass();
        errno += generateClass(gomclass,generators);
      }
      /* The mappings may need to access generators */
      for(final MappingTemplateClass templateClass : mappingSet) {
        templateClass.addTemplates(generators);
      }

      for (final ClassName clsName : generators.keySet()) {
        generators.get(clsName).generateFile();
      }
    }
    
    return 1;
  }



  /*
   * Create template classes for the different classes to generate
   */
  public int generateClass(GomClass gomclass, Map<ClassName,TemplateClass> generators) {
    %match(gomclass) {
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
              maximalsharing,
              getGomEnvironment());
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
              maximalsharing,
              getGomEnvironment());
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
            maximalsharing,
            jmicompatible,
            getGomEnvironment());
        generators.put(`className,operator);
        if(generateStratMapping>0) {
          TemplateClass sOpStrat = new tom.gom.backend.strategy.SOpTemplate(gomclass,getGomEnvironment());
          sOpStrat.generateFile();

          TemplateClass isOpStrat = new tom.gom.backend.strategy.IsOpTemplate(gomclass,getGomEnvironment());
          isOpStrat.generateFile();

          TemplateClass makeOpStrat = new tom.gom.backend.strategy.MakeOpTemplate(gomclass,getGomEnvironment());
          makeOpStrat.generateFile();
        }
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
              (TemplateClass)generators.get(`mapping),
              getGomEnvironment());
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
