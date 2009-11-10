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

package tom.gom.expander;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.platform.PlatformLogRecord;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import tom.gom.parser.GomLanguageLexer;
import tom.gom.parser.GomLanguageParser;
import tom.gom.adt.gom.GomLanguageGomAdaptor;

public class Expander {
  %include { ../adt/gom/Gom.tom}

  private GomEnvironment gomEnvironment;

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
    GomModuleList result = `ConcGomModule(module);
    Set<GomModuleName> alreadyParsedModule = new HashSet<GomModuleName>();
    alreadyParsedModule.add(module.getModuleName());
    Set<GomModuleName> moduleToAnalyse = generateModuleToAnalyseSet(module, alreadyParsedModule);
    getLogger().log(Level.FINER, "GomExpander:moduleToAnalyse {0}",
        new Object[]{moduleToAnalyse});

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
            result = `ConcGomModule(result*, importedModule);
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
      GomModuleName name = importedModules.getHeadConcImportedModule().getModuleName();
      if(!alreadyParsedModule.contains(name)) {
        moduleToAnalyse.add(name);
      }
      importedModules = importedModules.getTailConcImportedModule();
    }
    return moduleToAnalyse;
  }

  private GomModule parse(String moduleName) {
    getLogger().log(Level.FINE, "Seeking for file {0}",
        new Object[]{moduleName});
    GomModule result = null;
    File importedModuleFile = findModuleFile(moduleName);
    if(importedModuleFile == null) {
      getLogger().log(Level.SEVERE,
          GomMessage.moduleNotFound.getMessage(),
          new Object[]{moduleName});
      return null;
    }
    CharStream inputStream = null;
    try {
      inputStream = new ANTLRReaderStream(new FileReader(importedModuleFile));
    } catch (FileNotFoundException e) {
      getLogger().log(Level.SEVERE,
          GomMessage.fileNotFound.getMessage(),
          new Object[]{moduleName+".gom"});
      return null;
    } catch (java.io.IOException e) {
      getLogger().log(Level.SEVERE,
          GomMessage.fileNotFound.getMessage(),
          new Object[]{moduleName+".gom"});
      return null;
    }
		GomLanguageLexer lexer = new GomLanguageLexer(inputStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		GomLanguageParser parser = new GomLanguageParser(tokens,getStreamManager());
    try {
      Tree tree = (Tree) parser.module().getTree();
      result = (GomModule) GomLanguageGomAdaptor.getTerm(tree);
    } catch (RecognitionException re) {
      getLogger().log(new PlatformLogRecord(Level.SEVERE,
            GomMessage.detailedParseException,
            re.getMessage(),moduleName+".gom", lexer.getLine()));
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
    ImportList imports = `ConcImportedModule();
    %match(module) {
      GomModule(_,ConcSection(_*,Imports(importList),_*)) -> {
        imports = `ConcImportedModule(importList*,imports*);
      }
    }
    return imports;
  }
}
