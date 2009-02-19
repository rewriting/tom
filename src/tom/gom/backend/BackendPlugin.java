/*
 * Gom
 *
 * Copyright (c) 2000-2008, INRIA
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

package tom.gom.backend;

import java.util.Map;
import java.util.logging.Level;
import java.io.File;
import java.io.IOException;

import tom.platform.PlatformLogRecord;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomGenericPlugin;
import tom.gom.tools.GomEnvironment;

import tom.gom.adt.objects.types.*;

/**
 * The BackendPlugin handle the code generation
 */
public class BackendPlugin extends GomGenericPlugin {

  /** the list of compiled classes */
  private GomClassList classList;
  private TemplateFactory templateFactory;

  /** The constructor*/
  public BackendPlugin() {
    super("GomBackend");
    templateFactory = new SharedTemplateFactory(getOptionManager(),getGomEnvironment());
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  /** the declared options string */
  public static final String DECLARED_OPTIONS =
    "<options>" +
    "<string name='generator' altName='g' description='Select Generator. Possible value: \"shared\"' value='shared' attrName='type' />" +
    "<boolean name='optimize' altName='O' description='Optimize generated code' value='false'/>" +
    "<boolean name='optimize2' altName='O2' description='Optimize generated code' value='false'/>" +
    "<boolean name='inlineplus' altName='' description='Make inlining active' value='false'/>" +
    "<boolean name='strategies-mapping' altName='sm' description='Generate tom mapping for strategies' value='false'/>" +
    "<boolean name='multithread' altName='mt' description='Generate code compatible with multi-threading' value='false'/>" +
    "<boolean name='nosharing' altName='ns' description='Generate code without maximal sharing' value='false'/>" +
    "</options>";

  /**
   * inherited from OptionOwner interface (plugin)
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(BackendPlugin.DECLARED_OPTIONS);
  }

  /**
   * inherited from plugin interface
   * arg[0] should contain the GomStreamManager to get the input file name
   */
  public void setArgs(Object arg[]) {
    if (arg[0] instanceof GomClassList) {
      classList = (GomClassList)arg[0];
      setGomEnvironment((GomEnvironment)arg[1]);
    } else {
      getLogger().log(Level.SEVERE,
          GomMessage.invalidPluginArgument.getMessage(),
          new Object[]{"GomBackend", "[GomClassList,GomEnvironment]",
            getArgumentArrayString(arg)});
    }
  }

  /**
   * inherited from plugin interface
   * Create the initial GomModule parsed from the input file
   */
  public void run(Map informationTracker) {
    getLogger().log(Level.INFO, "Start compilation");
    // make sure the environment has the correct streamManager
    getGomEnvironment().setStreamManager(getStreamManager());
    /* Try to guess tom.home */
    File tomHomePath = null;
    String tomHome = System.getProperty("tom.home");
    try {
      if(tomHome == null) {
        String xmlConfigFilename = getOptionStringValue("X");
        tomHome = new File(xmlConfigFilename).getParent();
      }
      tomHomePath = new File(tomHome).getCanonicalFile();
    } catch (IOException e) {
      getLogger().log(Level.FINER,"Failed to get canonical path for " + tomHome);
    }
    boolean strategiesMapping = getOptionBooleanValue("strategies-mapping");
    boolean multithread = getOptionBooleanValue("multithread");
    boolean nosharing = getOptionBooleanValue("nosharing");
    Backend backend =
      new Backend(templateFactory.getFactory(getOptionManager()),
                  tomHomePath, strategiesMapping, multithread, nosharing,
                  getStreamManager().getImportList(),getGomEnvironment());
    backend.generate(classList);
    if(classList == null) {
      getLogger().log(Level.SEVERE,
          GomMessage.generationIssue.getMessage(),
          getStreamManager().getInputFileName());
    } else {
      getLogger().log(Level.INFO, "Code generation succeeds");
    }
    informationTracker.put(KEY_LAST_GEN_MAPPING,getGomEnvironment().getLastGeneratedMapping());
    
  }

  /**
   * inherited from plugin interface
   * returns an array containing the compiled classes and the streamManager
   * got from setArgs phase
   */
  public Object[] getArgs() {
    return new Object[]{getGomEnvironment()};
  }
}
