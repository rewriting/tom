/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.platform;

import java.text.*;
import java.util.*;
import java.util.logging.*;
import java.io.*;

import aterm.*;
import aterm.pure.*;

import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import tom.library.xml.*;

/**
 * The PluginPlatform manages plugins defined in an xml configuration file.
 * (which plugins are used and how they are ordered)
 * Then it instantiates them, and runs them in the specified order.
 *
 * @author Gr&eacute;gory ANDRIEN
 */
public class PluginPlatform {

  /** Used to analyse xml configuration file*/
  %include{ adt/TNode.tom }

  /**
   * Accessor method necessary when including adt/TNode.tom
   * @return a TNodeFactory
   */  
  public TNodeFactory getTNodeFactory() {
    return TNodeFactory.getInstance(SingletonFactory.getInstance());
  }
    
  /** The List of reference to plugins. */
  private List pluginsReferenceList;
    
  /** The option manager */
  private OptionManager optionManager;
	
  /** The PluginPlatform logger */
  private Logger logger;

  /** The status handler */
  private StatusHandler statusHandler;

  /** List of input file */
  private List inputFileList;
  /**
   * Class Pluginplatform constructor
   */
  public PluginPlatform(OptionManager optionManager, ConfigurationManager confManager, String loggerRadical) {
    this.pluginsReferenceList = confManager.getPluginsReferenceList();
    this.optionManager = optionManager;
    this.statusHandler = new StatusHandler();
    this.logger = Logger.getLogger(loggerRadical+".PluginPlatform","tom.platform.PluginPlatformResources");
    this.inputFileList = optionManager.getInputFileList();
    Logger.getLogger(loggerRadical).addHandler(this.statusHandler);
  }

  /**
   * The main method which runs the PluginPlatform.
   * 
   * @param argumentList the command line
   * @return an error code :
   * <ul>
   * <li>0 if no error was encountered</li>
   * <li>1 if something went wrong</li>
   * </ul>
   */
  public int run() {
    for(int i = 0; i < inputFileList.size(); i++) { // for each input file
      Object arg = inputFileList.get(i);
      logger.log(Level.FINER, "NowCompiling", arg);
      // runs the modules
      Iterator it = pluginsReferenceList.iterator();
      while(it.hasNext()) {
        Plugin plugin = (Plugin)it.next();
        plugin.setArg(arg);
        plugin.run();
        arg = plugin.getArg();
        if(statusHandler.hasError()) {
          logger.log(Level.SEVERE, "ProcessingError", arg);
          break;
        }
      }
    }
    
    int nbOfErrors   = statusHandler.nbOfErrors();
    int nbOfWarnings = statusHandler.nbOfWarnings();

    if(statusHandler.hasError()) {
      // this is the highest possible level > will be printed no matter what 
      logger.log( Level.OFF,  "TaskErrorMessage", new Object[]{ new Integer(nbOfErrors), new Integer(nbOfWarnings) } );
      return 1;
    } else if( statusHandler.hasWarning() ) {
      logger.log( Level.OFF, "TaskWarningMessage", new Integer(nbOfWarnings) );
      return 0;
    }
    return 0;
  }

  /**
   * An accessor method
   * @return the status handler.
   */
  public StatusHandler getStatusHandler() { return statusHandler; }

	
  /**
   * An accessor method.
   * @return the OptionManager
   */
  public OptionManager getOptionManager() { return optionManager; }
  
  /**
   * This method analyzes the command line and determines which configuration
   * file should be used. As the tom scripts already specify a default
   * configuration file which can be overridden by the user, only the last one
   * is taken into account (should be remembered if the PluginPlatform is used
   * outside of the tom script)
   * 
   * @param commandLine the command line
   * @return a String containing the path to the configuration file to be used
   */
  private String extractConfigFileName(String[] commandLine) {
    String xmlConfigurationFile = null; 
    int i=0;
    try {
      for(;i< commandLine.length;i++) {
        if(commandLine[i].equals("-X")) {
          xmlConfigurationFile = commandLine[++i];
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      logger.log(Level.SEVERE, "IncompleteOption", commandLine[--i]);
      return null;
    }
    
    if(xmlConfigurationFile==null) { // lack of a configuration file
      logger.log(Level.SEVERE, "ConfigFileNotSpecified");
      return null;
    }
    
    File file = new File(xmlConfigurationFile);
    if(!file.exists()) { // the last specified configuration file doesn't exist
      logger.log(Level.SEVERE, "ConfigFileNotFound", xmlConfigurationFile);
      return null;
    }
    
    return xmlConfigurationFile;
  }
  
  /**
   * This method parses the configuration and extracts the global options as
   * well as the class paths of the plugins that are going to be used.
   * 
   * @param xmlConfigurationFile the name of the XML configuration file
   * @return a List containing the class paths of the listed plugins
   */
  private List analyseConfigFile(String xmlConfigurationFile) {
    // parses configuration file...
    XmlTools xtools = new XmlTools();
    TNode node = (TNode)xtools.convertXMLToATerm(xmlConfigurationFile);
    if(node == null) {
      // parsing failed
      logger.log(Level.SEVERE, "ConfigFileNotXML", xmlConfigurationFile);
      return null;
    }
    // ... to extract global options
    optionManager.setGlobalOptionList(node.getDocElem());
    // ... to extract plugin classpaths
    return extractClassPaths(node.getDocElem());
  }
    
  /**
   * Extracts the plugins' class paths from the XML configuration file.
   * 
   * @param node the node containing the XML document
   * @return the List of plugins class path
   */
  private List extractClassPaths(TNode node) {
    List res = new ArrayList();
    %match(TNode node) {
      <server><plugins><plugin [classpath=cp]/></plugins></server> -> {
         res.add(cp);
         logger.log(Level.FINER, "ClassPathRead", cp);
       }
    }
    return res;
  }
  
  /**
   * Returns the value of an option. Returns an Object which is a Boolean, a
   * String or an Integer depending on what the option type is.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return an Object containing the option's value
   */
  public Object getOptionValue(String optionName) {
    return optionManager.getOptionValue(optionName);
  }

  public void setOptionValue(String key, Object value) {
    optionManager.setOptionValue(key, value);
  }

}
