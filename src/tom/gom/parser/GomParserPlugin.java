/*
 * Gom
 * 
 * Copyright (c) 2000-2009, INRIA
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
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.Map;

import tom.platform.PlatformLogRecord;
import tom.engine.tools.Tools;
import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.GomGenericPlugin;
import tom.gom.tools.GomEnvironment;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import tom.gom.adt.gom.GomLanguageGomAdaptor;

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

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
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
      getLogger().log(Level.SEVERE,
          GomMessage.invalidPluginArgument.getMessage(),
          new Object[]{"GomParser", "[GomEnvironment]",
            getArgumentArrayString(arg)});
    }
  }

  /**
   * inherited from plugin interface
   * Create the initial GomModule parsed from the input file
   */
  public synchronized void run(Map<String,String> informationTracker) {
    boolean intermediate = getOptionBooleanValue("intermediate");
    if (inputReader == null)
      return;
    CharStream input = null;
    try {
      input = new ANTLRReaderStream(inputReader);
    } catch (java.io.IOException e) {
      getLogger().log(Level.INFO,
          GomMessage.unableToUseReaderMessage.getMessage(),
          new Object[]{});
      // Invalid input stream
      return;
    }
		GomLanguageLexer lex = new GomLanguageLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lex);
		GomLanguageParser parser = new GomLanguageParser(tokens,getStreamManager());
    getLogger().log(Level.INFO, "Start parsing");
    try {
      // Parse the input expression
      Tree tree = (Tree)parser.module().getTree();
      module = (GomModule) GomLanguageGomAdaptor.getTerm(tree);
      java.io.StringWriter swriter = new java.io.StringWriter();
      tom.library.utils.Viewer.toTree(module,swriter);
      getLogger().log(Level.FINE, "Parsed Module:\n{0}", swriter);
      if (module == null) {
        getLogger().log(new PlatformLogRecord(Level.SEVERE,
              GomMessage.detailedParseException,
              "", inputFileName, lex.getLine()));
        return;
      }
    } catch (RecognitionException re) {
      getLogger().log(new PlatformLogRecord(Level.SEVERE,
            GomMessage.detailedParseException,
            re.toString(), inputFileName, lex.getLine()));
      return;
    } catch (Exception e) {
      StringWriter stringwriter = new StringWriter();
      PrintWriter printwriter = new PrintWriter(stringwriter);
      e.printStackTrace(printwriter);
      getLogger().log(Level.SEVERE, GomMessage.exceptionMessage.getMessage(),
          new Object[]{getClass().getName(), inputFileName, stringwriter.toString()});
      return;
    } finally {
      if (inputReader != null){
        try {
          inputReader.close();
        } catch(java.io.IOException ioExcep){
          // nothing to do
          getLogger().log(Level.INFO, GomMessage.unableToCloseReaderMessage.getMessage(),
              new Object[]{});
        }
      }
    }
    getLogger().log(Level.INFO, "Parsing succeeds");
    if(intermediate) {
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

