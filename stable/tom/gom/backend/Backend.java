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

         private static   tom.gom.adt.objects.types.GomClassList  tom_append_list_ConcGomClass( tom.gom.adt.objects.types.GomClassList l1,  tom.gom.adt.objects.types.GomClassList  l2) {     if( l1.isEmptyConcGomClass() ) {       return l2;     } else if( l2.isEmptyConcGomClass() ) {       return l1;     } else if(  l1.getTailConcGomClass() .isEmptyConcGomClass() ) {       return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( l1.getHeadConcGomClass() ,l2) ;     } else {       return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( l1.getHeadConcGomClass() ,tom_append_list_ConcGomClass( l1.getTailConcGomClass() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.GomClassList  tom_get_slice_ConcGomClass( tom.gom.adt.objects.types.GomClassList  begin,  tom.gom.adt.objects.types.GomClassList  end, tom.gom.adt.objects.types.GomClassList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomClass()  ||  (end== tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( begin.getHeadConcGomClass() ,( tom.gom.adt.objects.types.GomClassList )tom_get_slice_ConcGomClass( begin.getTailConcGomClass() ,end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}   


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
    { /* unamed block */{ /* unamed block */if ( (classList instanceof tom.gom.adt.objects.types.GomClassList) ) {if ( (((( tom.gom.adt.objects.types.GomClassList )classList) instanceof tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass) || ((( tom.gom.adt.objects.types.GomClassList )classList) instanceof tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass)) ) { tom.gom.adt.objects.types.GomClassList  tomMatch547_end_4=(( tom.gom.adt.objects.types.GomClassList )classList);do {{ /* unamed block */if (!( tomMatch547_end_4.isEmptyConcGomClass() )) { tom.gom.adt.objects.types.GomClass  tomMatch547_8= tomMatch547_end_4.getHeadConcGomClass() ;if ( ((( tom.gom.adt.objects.types.GomClass )tomMatch547_8) instanceof tom.gom.adt.objects.types.gomclass.TomMapping) ) { tom.gom.adt.objects.types.ClassName  tomMatch547_7= tomMatch547_8.getClassName() ;if ( ((( tom.gom.adt.objects.types.ClassName )tomMatch547_7) instanceof tom.gom.adt.objects.types.classname.ClassName) ) { tom.gom.adt.objects.types.GomClass  tom___gomclass= tomMatch547_end_4.getHeadConcGomClass() ;



        MappingTemplateClass mapping = null;
        if (generateStratMapping > 0) { // generate congruence strategies
          ClassName smappingclass =  tom.gom.adt.objects.types.classname.ClassName.make( tomMatch547_7.getPkg() , "_"+ tomMatch547_7.getName() ) ;
          GomClass nGomClass = tom___gomclass.setClassName(smappingclass);
          TemplateClass stratMapping = new tom.gom.backend.strategy.StratMappingTemplate(nGomClass,getGomEnvironment(),generateStratMapping);
          if (1 == generateStratMapping) {
            // classical mode: generate extra-mapping in file.tom
            mapping = templatefactory.makeTomMappingTemplate(tom___gomclass,stratMapping,getGomEnvironment());
          } else {
            // expert mode: generate extra-mapping in _file.tom
            generators.put(smappingclass,stratMapping);
            mapping = templatefactory.makeTomMappingTemplate(tom___gomclass,null,getGomEnvironment());
          }
        } else {
          // do not generate congruence strategies
          mapping = templatefactory.makeTomMappingTemplate(tom___gomclass,null,getGomEnvironment());
        }
        mappingSet.add(mapping);
        generators.put(tomMatch547_7,mapping);
      }}}if ( tomMatch547_end_4.isEmptyConcGomClass() ) {tomMatch547_end_4=(( tom.gom.adt.objects.types.GomClassList )classList);} else {tomMatch547_end_4= tomMatch547_end_4.getTailConcGomClass() ;}}} while(!( (tomMatch547_end_4==(( tom.gom.adt.objects.types.GomClassList )classList)) ));}}}}

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
    { /* unamed block */{ /* unamed block */if ( (gomclass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomclass) instanceof tom.gom.adt.objects.types.gomclass.TomMapping) ) {

        /* It was processed by the caller: check it is already in generators */
        if (!generators.containsKey( (( tom.gom.adt.objects.types.GomClass )gomclass).getClassName() )) {
          throw new GomRuntimeException(
              "Mapping should be processed before generateClass is called");
        }
        return 1;
      }}}{ /* unamed block */if ( (gomclass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomclass) instanceof tom.gom.adt.objects.types.gomclass.AbstractTypeClass) ) {

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
      }}}{ /* unamed block */if ( (gomclass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomclass) instanceof tom.gom.adt.objects.types.gomclass.SortClass) ) {

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
      }}}{ /* unamed block */if ( (gomclass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomclass) instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) {

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
      }}}{ /* unamed block */if ( (gomclass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomclass) instanceof tom.gom.adt.objects.types.gomclass.VariadicOperatorClass) ) {




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
