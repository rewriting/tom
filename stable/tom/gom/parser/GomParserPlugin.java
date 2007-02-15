/*
 * Gom
 * 
 * Copyright (c) 2000-2007, INRIA
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

import tom.platform.PlatformLogRecord;
import tom.engine.tools.Tools;
import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.GomGenericPlugin;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.TokenStreamSelector;

/**
 * The responsability of the GomParser plugin is to parse the input Gom file
 * Get the input file from GomStreamManager and parse
 */
public class GomParserPlugin extends GomGenericPlugin {

  public static final String PARSED_SUFFIX = ".tfix.gom.parsed";
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
    if (arg[0] instanceof GomStreamManager) {
      setStreamManager((GomStreamManager)arg[0]);
      inputReader = getStreamManager().getInputReader();
      inputFileName = getStreamManager().getInputFileName();
    } else {
      getLogger().log(Level.SEVERE,
          GomMessage.invalidPluginArgument.getMessage(),
          new Object[]{"GomParser", "[GomStreamManager]",
            getArgumentArrayString(arg)});
    }
  }

  /**
   * inherited from plugin interface
   * Create the initial GomModule parsed from the input file
   */
  public void run() {
    boolean intermediate = ((Boolean)getOptionManager().getOptionValue("intermediate")).booleanValue();

    if (inputReader == null)
      return;
    GomLexer gomlexer = new GomLexer(inputReader);
    //BlockLexer blocklexer = new BlockLexer(gomlexer.getInputState());
    GomParser parser = new GomParser(gomlexer,"GomParser");
    getLogger().log(Level.INFO, "Start parsing");
    try {
      module = parser.module();
    } catch (RecognitionException re) {
      //StringWriter sw = new StringWriter();
      //PrintWriter pw = new PrintWriter(sw);
      //re.printStackTrace(pw);
      getLogger().log(new PlatformLogRecord(Level.SEVERE,
            GomMessage.detailedParseException,
            re.toString(), inputFileName, gomlexer.getLine()));
      return;
    } catch(TokenStreamException streamException) {
      //StringWriter stringwriter = new StringWriter();
      //PrintWriter printwriter = new PrintWriter(stringwriter);
      //streamException.printStackTrace(printwriter);
      getLogger().log(new PlatformLogRecord(Level.SEVERE,
            GomMessage.detailedParseException,
            streamException.toString(),inputFileName, gomlexer.getLine()));
      return;
    } catch (Exception e) {
      StringWriter stringwriter = new StringWriter();
      PrintWriter printwriter = new PrintWriter(stringwriter);
      e.printStackTrace(printwriter);
      getLogger().log(Level.SEVERE, GomMessage.exceptionMessage.getMessage(),
          new Object[]{getClass().getName(), inputFileName, stringwriter.toString()});
      return;
    }

    getLogger().log(Level.INFO, "Parsing succeeds");
    if(intermediate) {
      Tools.generateOutput(getStreamManager().getInputFileNameWithoutSuffix()
                           + PARSED_SUFFIX, (aterm.ATerm)module.toATerm());
    }
  }

  /**
   * inherited from plugin interface
   * returns an array containing the parsed module and the streamManager
   * got from setArgs phase
   */
  public Object[] getArgs() {
    return new Object[]{module, getStreamManager()};
  }

}
