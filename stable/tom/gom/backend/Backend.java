/*
 * Gom
 *
 * Copyright (c) 2006-2009, INRIA
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

         private static   tom.gom.adt.objects.types.GomClassList  tom_append_list_ConcGomClass( tom.gom.adt.objects.types.GomClassList l1,  tom.gom.adt.objects.types.GomClassList  l2) {     if( l1.isEmptyConcGomClass() ) {       return l2;     } else if( l2.isEmptyConcGomClass() ) {       return l1;     } else if(  l1.getTailConcGomClass() .isEmptyConcGomClass() ) {       return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( l1.getHeadConcGomClass() ,l2) ;     } else {       return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( l1.getHeadConcGomClass() ,tom_append_list_ConcGomClass( l1.getTailConcGomClass() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.GomClassList  tom_get_slice_ConcGomClass( tom.gom.adt.objects.types.GomClassList  begin,  tom.gom.adt.objects.types.GomClassList  end, tom.gom.adt.objects.types.GomClassList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomClass()  ||  (end== tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( begin.getHeadConcGomClass() ,( tom.gom.adt.objects.types.GomClassList )tom_get_slice_ConcGomClass( begin.getTailConcGomClass() ,end,tail)) ;   }             


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
    Map<ClassName,TemplateClass> generators = new HashMap<ClassName,TemplateClass>();
    // prepare stuff for the mappings
    {{if ( (classList instanceof tom.gom.adt.objects.types.GomClassList) ) {if ( (((( tom.gom.adt.objects.types.GomClassList )classList) instanceof tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass) || ((( tom.gom.adt.objects.types.GomClassList )classList) instanceof tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass)) ) { tom.gom.adt.objects.types.GomClassList  tomMatch349NameNumber_end_4=(( tom.gom.adt.objects.types.GomClassList )classList);do {{if (!( tomMatch349NameNumber_end_4.isEmptyConcGomClass() )) { tom.gom.adt.objects.types.GomClass  tomMatch349NameNumber_freshVar_8= tomMatch349NameNumber_end_4.getHeadConcGomClass() ;if ( (tomMatch349NameNumber_freshVar_8 instanceof tom.gom.adt.objects.types.gomclass.TomMapping) ) { tom.gom.adt.objects.types.ClassName  tomMatch349NameNumber_freshVar_7= tomMatch349NameNumber_freshVar_8.getClassName() ;if ( (tomMatch349NameNumber_freshVar_7 instanceof tom.gom.adt.objects.types.classname.ClassName) ) { tom.gom.adt.objects.types.GomClass  tom_gomclass= tomMatch349NameNumber_end_4.getHeadConcGomClass() ;



        MappingTemplateClass mapping = null;
        if(generateStratMapping>0) { // generate congruence strategies
          ClassName smappingclass =  tom.gom.adt.objects.types.classname.ClassName.make( tomMatch349NameNumber_freshVar_7.getPkg() , "_"+ tomMatch349NameNumber_freshVar_7.getName() ) ;
          GomClass nGomClass = tom_gomclass.setClassName(smappingclass);
          TemplateClass stratMapping = new tom.gom.backend.strategy.StratMappingTemplate(nGomClass,getGomEnvironment(),generateStratMapping);
          if(generateStratMapping==1) {
            // classical mode: generate extra-mapping in file.tom
            mapping = templatefactory.makeTomMappingTemplate(tom_gomclass,stratMapping,getGomEnvironment());
          } else {
            // expert mode: generate extra-mapping in _file.tom
            generators.put(smappingclass,stratMapping);
            mapping = templatefactory.makeTomMappingTemplate(tom_gomclass,null,getGomEnvironment());
          }
        } else {
          // do not generate congruence strategies
          mapping = templatefactory.makeTomMappingTemplate(tom_gomclass,null,getGomEnvironment());
        }
        mappingSet.add(mapping);
        generators.put(tomMatch349NameNumber_freshVar_7,mapping);
      }}}if ( tomMatch349NameNumber_end_4.isEmptyConcGomClass() ) {tomMatch349NameNumber_end_4=(( tom.gom.adt.objects.types.GomClassList )classList);} else {tomMatch349NameNumber_end_4= tomMatch349NameNumber_end_4.getTailConcGomClass() ;}}} while(!( (tomMatch349NameNumber_end_4==(( tom.gom.adt.objects.types.GomClassList )classList)) ));}}}}

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
    {{if ( (gomclass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomclass) instanceof tom.gom.adt.objects.types.gomclass.TomMapping) ) {

        /* It was processed by the caller: check it is already in generators */
        if (!generators.containsKey( (( tom.gom.adt.objects.types.GomClass )gomclass).getClassName() )) {
          throw new GomRuntimeException(
              "Mapping should be processed before generateClass is called");
        }
        return 1;
      }}}{if ( (gomclass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomclass) instanceof tom.gom.adt.objects.types.gomclass.AbstractTypeClass) ) {

        TemplateClass abstracttype =
          templatefactory.makeAbstractTypeTemplate(
              tomHomePath,
              importList,
              gomclass,
              (TemplateClass)generators.get( (( tom.gom.adt.objects.types.GomClass )gomclass).getMapping() ),
              maximalsharing,
              getGomEnvironment());
        generators.put( (( tom.gom.adt.objects.types.GomClass )gomclass).getClassName() ,abstracttype);
        return 1;
      }}}{if ( (gomclass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomclass) instanceof tom.gom.adt.objects.types.gomclass.SortClass) ) {

        TemplateClass sort =
          templatefactory.makeSortTemplate(
              tomHomePath,
              importList,
              gomclass,
              (TemplateClass)generators.get( (( tom.gom.adt.objects.types.GomClass )gomclass).getMapping() ),
              maximalsharing,
              getGomEnvironment());
        generators.put( (( tom.gom.adt.objects.types.GomClass )gomclass).getClassName() ,sort);
        return 1;
      }}}{if ( (gomclass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomclass) instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) {

        TemplateClass operator = templatefactory.makeOperatorTemplate(
            tomHomePath,
            importList,
            gomclass,
            (TemplateClass)generators.get( (( tom.gom.adt.objects.types.GomClass )gomclass).getMapping() ),
            multithread,
            maximalsharing,
            jmicompatible,
            getGomEnvironment());
        generators.put( (( tom.gom.adt.objects.types.GomClass )gomclass).getClassName() ,operator);
        if(generateStratMapping>0) {
          TemplateClass sOpStrat = new tom.gom.backend.strategy.SOpTemplate(gomclass,getGomEnvironment());
          sOpStrat.generateFile();

          TemplateClass isOpStrat = new tom.gom.backend.strategy.IsOpTemplate(gomclass,getGomEnvironment());
          isOpStrat.generateFile();

          TemplateClass makeOpStrat = new tom.gom.backend.strategy.MakeOpTemplate(gomclass,getGomEnvironment());
          makeOpStrat.generateFile();
        }
       return 1;
      }}}{if ( (gomclass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomclass) instanceof tom.gom.adt.objects.types.gomclass.VariadicOperatorClass) ) {




        TemplateClass operator =
          templatefactory.makeVariadicOperatorTemplate(
              tomHomePath,
              importList,
              gomclass,
              (TemplateClass)generators.get( (( tom.gom.adt.objects.types.GomClass )gomclass).getMapping() ),
              getGomEnvironment());
        generators.put( (( tom.gom.adt.objects.types.GomClass )gomclass).getClassName() ,operator);
        /* Generate files for cons and empty */
        int ret = 1;
        ret+=generateClass( (( tom.gom.adt.objects.types.GomClass )gomclass).getEmpty() ,generators);
        ret+=generateClass( (( tom.gom.adt.objects.types.GomClass )gomclass).getCons() ,generators);

        return ret;
      }}}}

    throw new GomRuntimeException("Trying to generate code for a strange class: "+gomclass);
  }
}
