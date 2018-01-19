/*
 * Gom
 *
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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
 * Antoine Reilles    e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.parser;

import java.io.Reader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import tom.engine.tools.Tools;
import tom.gom.GomMessage;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.GomGenericPlugin;
import tom.gom.tools.GomEnvironment;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import org.antlr.v4.runtime.ANTLRInputStream;

import tom.gom.adt.gom.GomAdaptor;

import tom.engine.parser.tomjava.*;

/**
 * The responsability of the GomParser plugin is to parse the input Gom file
 * Get the input file from GomStreamManager and parse
 */
public class GomParserPlugin extends GomGenericPlugin {

  public static final String PARSED_SUFFIX = ".tfix.gom.parsed";
  public static final String PARSEDTERM_SUFFIX = ".tfix.aterm.parsed";
  /** input stream */
  private Reader inputReader;
  private String inputFileName;

  /** the parsed module */
  private GomModule module;

  /** The constructor*/
  public GomParserPlugin() {
    super("GomParser");
  }

  /**
   * inherited from plugin interface
   * arg[0] should contain the GomStreamManager to get the input file name
   */
  public void setArgs(Object arg[]) {
    if (arg[0] instanceof GomEnvironment) {
      setGomEnvironment((GomEnvironment)arg[0]);
      inputReader = getStreamManager().getInputReader();
      inputFileName = getStreamManager().getInputFileName();
    } else {
      GomMessage.error(getLogger(),null,0,
          GomMessage.invalidPluginArgument,
          new Object[]{"GomParser", "[GomEnvironment]",
            getArgumentArrayString(arg)});
    }
  }

  /**
   * inherited from plugin interface
   * Create the initial GomModule parsed from the input file
   */
  public synchronized void run(Map<String,String> informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    if (null == inputReader) {
      return;
    }
    
    Lexer lexer = null;
    
    try {
      if(getOptionBooleanValue("tomjava")) {
        ANTLRInputStream input = null;
        try {
          input = new ANTLRInputStream(inputReader);
        } catch (java.io.IOException e) {
          GomMessage.error(getLogger(),null,0,
              GomMessage.unableToUseReaderMessage);
          // Invalid input stream
          return;
        }
        
        tom.engine.parser.tomjava.TomParser parser = new tom.engine.parser.tomjava.TomParser(inputFileName);
        module = parser.parseGom(input, getStreamManager());        
      } else {
        CharStream input = null;
        try {
          input = new ANTLRReaderStream(inputReader);
        } catch (java.io.IOException e) {
          GomMessage.error(getLogger(),null,0,
              GomMessage.unableToUseReaderMessage);
          // Invalid input stream
          return;
        }
        
        lexer = new GomLanguageLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GomLanguageParser parser = new GomLanguageParser(tokens,getStreamManager());
        
        // Parse the input expression
        Tree tree = (Tree) parser.module().getTree();
        module = (GomModule) GomAdaptor.getTerm(tree);
      }
      
      java.io.StringWriter swriter = new java.io.StringWriter();
      tom.library.utils.Viewer.toTree(module,swriter);
      GomMessage.fine(getLogger(), inputFileName, 0, GomMessage.parsedModules, swriter);
      if (null == module) {
        GomMessage.error(getLogger(),inputFileName, lexer.getLine(),GomMessage.detailedParseException);
        return;
      }
    } catch (RecognitionException re) {
        GomMessage.error(getLogger(),inputFileName, lexer.getLine(),GomMessage.detailedParseException,re.toString());
      return;
    } catch (Exception e) {
      StringWriter stringwriter = new StringWriter();
      PrintWriter printwriter = new PrintWriter(stringwriter);
      e.printStackTrace(printwriter);
      GomMessage.error(getLogger(),inputFileName,0,
          GomMessage.exceptionMessage,
          new Object[]{getClass().getName(),stringwriter.toString()});
      return;
    } finally {
      if (null != inputReader) {
        try {
          inputReader.close();
        } catch(java.io.IOException ioExcep) {
          // nothing to do
          GomMessage.error(getLogger(),inputFileName,0,
              GomMessage.unableToCloseReaderMessage);
        }
      }
    }
    GomMessage.info(getLogger(), inputFileName, 0, GomMessage.parsingPhase, 
        (System.currentTimeMillis()-startChrono));
    if (intermediate) {
      Tools.generateOutput(getStreamManager().getOutputFileName()
                           + PARSED_SUFFIX, (aterm.ATerm)module.toATerm());
    }
    informationTracker.put(KEY_LAST_GEN_MAPPING,getGomEnvironment().getLastGeneratedMapping());
  }

  /**
   * inherited from plugin interface
   * returns an array containing the parsed module and the streamManager
   * got from setArgs phase
   */
  public Object[] getArgs() {
    return new Object[]{module, getGomEnvironment()};
  }

}

