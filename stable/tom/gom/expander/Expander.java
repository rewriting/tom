/*
 * Gom
 *
 * Copyright (c) 2006-2017, Universite de Lorraine, Inria
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

package tom.gom.expander;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import tom.gom.parser.GomLanguageLexer;
import tom.gom.parser.GomLanguageParser;
import tom.gom.adt.gom.GomAdaptor;

public class Expander {
         private static   tom.gom.adt.gom.types.ImportList  tom_append_list_ConcImportedModule( tom.gom.adt.gom.types.ImportList l1,  tom.gom.adt.gom.types.ImportList  l2) {     if( l1.isEmptyConcImportedModule() ) {       return l2;     } else if( l2.isEmptyConcImportedModule() ) {       return l1;     } else if(  l1.getTailConcImportedModule() .isEmptyConcImportedModule() ) {       return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( l1.getHeadConcImportedModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( l1.getHeadConcImportedModule() ,tom_append_list_ConcImportedModule( l1.getTailConcImportedModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ImportList  tom_get_slice_ConcImportedModule( tom.gom.adt.gom.types.ImportList  begin,  tom.gom.adt.gom.types.ImportList  end, tom.gom.adt.gom.types.ImportList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcImportedModule()  ||  (end== tom.gom.adt.gom.types.importlist.EmptyConcImportedModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.importlist.ConsConcImportedModule.make( begin.getHeadConcImportedModule() ,( tom.gom.adt.gom.types.ImportList )tom_get_slice_ConcImportedModule( begin.getTailConcImportedModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.GomModuleList  tom_append_list_ConcGomModule( tom.gom.adt.gom.types.GomModuleList l1,  tom.gom.adt.gom.types.GomModuleList  l2) {     if( l1.isEmptyConcGomModule() ) {       return l2;     } else if( l2.isEmptyConcGomModule() ) {       return l1;     } else if(  l1.getTailConcGomModule() .isEmptyConcGomModule() ) {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,tom_append_list_ConcGomModule( l1.getTailConcGomModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GomModuleList  tom_get_slice_ConcGomModule( tom.gom.adt.gom.types.GomModuleList  begin,  tom.gom.adt.gom.types.GomModuleList  end, tom.gom.adt.gom.types.GomModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomModule()  ||  (end== tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( begin.getHeadConcGomModule() ,( tom.gom.adt.gom.types.GomModuleList )tom_get_slice_ConcGomModule( begin.getTailConcGomModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SectionList  tom_append_list_ConcSection( tom.gom.adt.gom.types.SectionList l1,  tom.gom.adt.gom.types.SectionList  l2) {     if( l1.isEmptyConcSection() ) {       return l2;     } else if( l2.isEmptyConcSection() ) {       return l1;     } else if(  l1.getTailConcSection() .isEmptyConcSection() ) {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,tom_append_list_ConcSection( l1.getTailConcSection() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SectionList  tom_get_slice_ConcSection( tom.gom.adt.gom.types.SectionList  begin,  tom.gom.adt.gom.types.SectionList  end, tom.gom.adt.gom.types.SectionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSection()  ||  (end== tom.gom.adt.gom.types.sectionlist.EmptyConcSection.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( begin.getHeadConcSection() ,( tom.gom.adt.gom.types.SectionList )tom_get_slice_ConcSection( begin.getTailConcSection() ,end,tail)) ;   }    

  private final GomEnvironment gomEnvironment;

  public Expander(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public GomStreamManager getStreamManager() {
    return this.gomEnvironment.getStreamManager();
  }

  /*
   * Compute the transitive closure of imported modules
   */
  public GomModuleList expand(GomModule module) {
    GomModuleList result =  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make(module, tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule.make() ) ;
    Set<GomModuleName> alreadyParsedModule = new HashSet<GomModuleName>();
    alreadyParsedModule.add(module.getModuleName());
    Set<GomModuleName> moduleToAnalyse = generateModuleToAnalyseSet(module, alreadyParsedModule);
    GomMessage.finer(getLogger(), null, 0, GomMessage.moduleToAnalyse,
        moduleToAnalyse);

    while (!moduleToAnalyse.isEmpty()) {
      Set<GomModuleName> newModuleToAnalyse = new HashSet<GomModuleName>();

      for (GomModuleName moduleNameName : moduleToAnalyse) {
        String moduleName = moduleNameName.getName();

        if (!getGomEnvironment().isBuiltin(moduleName)) {
          if (!alreadyParsedModule.contains(moduleNameName)) {
            GomModule importedModule = parse(moduleName);
            if (null == importedModule) {
              return null;
            }
            result = tom_append_list_ConcGomModule(result, tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make(importedModule, tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule.make() ) );
            alreadyParsedModule.add(moduleNameName);
            newModuleToAnalyse.addAll(generateModuleToAnalyseSet(importedModule,alreadyParsedModule));
          }
        } else {
          getGomEnvironment().markUsedBuiltin(moduleName);
        }
      }
      moduleToAnalyse = newModuleToAnalyse;
    }
   return result;
  }

  /*
   * Compute immediate imported modules where already parsed modules are removed
   */
  private Set<GomModuleName> generateModuleToAnalyseSet(GomModule module, Set<GomModuleName> alreadyParsedModule) {
    Set<GomModuleName> moduleToAnalyse = new HashSet<GomModuleName>();
    ImportList importedModules = getImportList(module);
    while(!importedModules.isEmptyConcImportedModule()) {
      GomModuleName name = importedModules.getHeadConcImportedModule();
      if(!alreadyParsedModule.contains(name)) {
        moduleToAnalyse.add(name);
      }
      importedModules = importedModules.getTailConcImportedModule();
    }
    return moduleToAnalyse;
  }

  private GomModule parse(String moduleName) {
    GomMessage.fine(getLogger(), null, 0, GomMessage.fileSeeking, moduleName);
    File importedModuleFile = findModuleFile(moduleName);
    if (null == importedModuleFile) {
      GomMessage.error(getLogger(),moduleName,0,
          GomMessage.moduleNotFound);
      return null;
    }
    final CharStream inputStream;
    try {
      inputStream = new ANTLRReaderStream(new FileReader(importedModuleFile));
    } catch (FileNotFoundException e) {
      GomMessage.error(getLogger(),moduleName+".gom",0,
          GomMessage.fileNotFound);
      return null;
    } catch (java.io.IOException e) {
      GomMessage.error(getLogger(),moduleName+".gom",0,
          GomMessage.fileNotFound);
      return null;
    }
		GomLanguageLexer lexer = new GomLanguageLexer(inputStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		GomLanguageParser parser = new GomLanguageParser(tokens,getStreamManager());
    final GomModule result;
    try {
      Tree tree = (Tree) parser.module().getTree();
      result = (GomModule) GomAdaptor.getTerm(tree);
    } catch (RecognitionException re) {
      GomMessage.error(getLogger(),moduleName+".gom", lexer.getLine(),
          GomMessage.detailedParseException,
          re.getMessage());
      return null;
    }
    return result;
  }

  /**
   * find a module locally or thanks to the stream manager import list
   */
  private File findModuleFile(String moduleName) {
    String extendedModuleName = moduleName+".gom";
    File f = new File(extendedModuleName);
    if (f.exists()) {
      return f;
    }
    return getStreamManager().findModuleFile(extendedModuleName);
  }

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  public ImportList getImportList(GomModule module) {
    ImportList imports =  tom.gom.adt.gom.types.importlist.EmptyConcImportedModule.make() ;
    { /* unamed block */{ /* unamed block */if ( (module instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )module) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.SectionList  tomMatch653_2= (( tom.gom.adt.gom.types.GomModule )module).getSectionList() ;if ( (((( tom.gom.adt.gom.types.SectionList )tomMatch653_2) instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || ((( tom.gom.adt.gom.types.SectionList )tomMatch653_2) instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch653_end_8=tomMatch653_2;do {{ /* unamed block */if (!( tomMatch653_end_8.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch653_12= tomMatch653_end_8.getHeadConcSection() ;if ( ((( tom.gom.adt.gom.types.Section )tomMatch653_12) instanceof tom.gom.adt.gom.types.section.Imports) ) {

        imports = tom_append_list_ConcImportedModule( tomMatch653_12.getImportList() ,tom_append_list_ConcImportedModule(imports, tom.gom.adt.gom.types.importlist.EmptyConcImportedModule.make() ));
      }}if ( tomMatch653_end_8.isEmptyConcSection() ) {tomMatch653_end_8=tomMatch653_2;} else {tomMatch653_end_8= tomMatch653_end_8.getTailConcSection() ;}}} while(!( (tomMatch653_end_8==tomMatch653_2) ));}}}}}

    return imports;
  }
}
