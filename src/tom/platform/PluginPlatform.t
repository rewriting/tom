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
 * The TomServer manages plugins. It parses Tom.xml in order to find which
 * plugins are used and how they are ordered. Then it instantiates them,
 * and runs them.
 *
 * @author Gr&eacute;gory ANDRIEN
 */
public class PluginPlatform {

  %include{ adt/TNode.tom }
    
  /** The List containing a reference to the plugins. */
  private List instances;
    
  /**  */
  private TNodeFactory tNodeFactory;

  /**  */
  private OptionManager optionManager;
	
  /**
   * An accessor method.
   * 
   * @return a TNodeFactory
   */  
  public TNodeFactory getTNodeFactory() { return tNodeFactory; }
	
  /**
   * An accessor method.
   * 
   * @return an OptionManager
   */
  public OptionManager getOptionManager() { return optionManager; }

  /**  */
  private Logger logger;

  /**  */
  private StatusHandler statusHandler;

  /** Accessor method for the status handler. */
  public StatusHandler getStatusHandler() { return statusHandler; }

  /**
   * Part of the Singleton pattern. The unique instance of the TomServer.
   */
  private static PluginPlatform instance = null;
    
  /**
   * Part of the Singleton pattern. A private constructor method, that exists to defeat instantiation.
   */
  private PluginPlatform(){}
    
  /**
   * Part of the Singleton pattern. Returns the instance of the TomServer if it has been initialized before,
   * otherwise it throws a RuntimeException.
   * 
   * @return the instance of the TomServer
   * @throws RuntimeException if the TomServer hasn't been initialized before the call
   */
  public static PluginPlatform getInstance() {
    if(instance == null) {
      throw new RuntimeException("Cannot get the instance of an uninitialized TomServer");
    }
    return instance;
  }

  /**
   * Part of the Singleton pattern. Initializes the PluginPlatform in case it hasn't been done before,
   * otherwise it reinitializes it.
   * 
   * @return the instance of the PluginPlatform
   */
  public static PluginPlatform create(OptionManager optionManager, String loggerRadical) {
    if(instance == null) {
      instance = new PluginPlatform();
        
      instance.instances = new ArrayList();
      instance.tNodeFactory = TNodeFactory.getInstance(SingletonFactory.getInstance());
      instance.optionManager = optionManager;

      instance.statusHandler = new StatusHandler();
      Logger.getLogger(loggerRadical).addHandler(instance.statusHandler);
      instance.logger = Logger.getLogger(loggerRadical+".PluginPlatform","tom.platform.PluginPlatformResources");
	
      return instance;
    } else {
      PluginPlatform.reset(optionManager, loggerRadical);
      return instance;
    }
  }

  /**
   * Reinitializes the PluginPlatform instance.
   */
  public static void reset(OptionManager optionManager, String loggerRadical) {
    instance.instances = new ArrayList();
    instance.optionManager = optionManager;
    instance.statusHandler = new StatusHandler();
    Logger.getLogger(loggerRadical).addHandler(instance.statusHandler);
    instance.logger = Logger.getLogger(loggerRadical+".PluginPlatform","tom.platform.PluginPlatformResources");
  }

  /**
   * 
   * @param args
   * @return
   */
  public static int exec(String args[], OptionManager optionManager, String loggerRadical) {
    PluginPlatform server = PluginPlatform.create(optionManager, loggerRadical);
    return server.run(args);
  }

  /**
   * This method analyzes the command line and determines which configuration file should be used.
   * 
   * @param argumentList the command line
   * @return a String containing the path to the configuration file to be used
   */
  private String whichConfigFile(String[] argumentList) {
    // there is no default configuration file
    // because the tom script already specifies a configuration file
    // (which can be overridden by the user since only the last one that is specified is taken into account)
    // so there MUST be at least one "-X" option
    // (should be remembered if the PluginPlatform is used outside of the tom script)
    String xmlConfigurationFile = null; 
	
    int i = 0;

    try {
      for(; i < argumentList.length; i++) {
        if(argumentList[i].equals("-X")) {
          // tests if argumentList redefines the configuration file
          xmlConfigurationFile = argumentList[++i];
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      logger.log(Level.SEVERE, "IncompleteOption", argumentList[--i]); 
    }

    if( statusHandler.hasError() ) {
      return null;
    }

    try {
      File file = new File(xmlConfigurationFile);
      
      if(! file.exists() ) {
        // the case where the specified file doesn't exist is handled here
        logger.log(Level.SEVERE, "ConfigFileNotFound", xmlConfigurationFile);
      }
    } catch(NullPointerException npe) {
      // the lack of a configuration file is handled here
      logger.log(Level.SEVERE, "ConfigFileNotSpecified");
    }

    if( statusHandler.hasError() ) {
      return null;
    }

    return xmlConfigurationFile;
  }

  /**
   * This method parses the configuration and extracts the global options as well as
   * the class paths of the plugins that are going to be used.
   * 
   * @param xmlConfigurationFile the name of the XML configuration file to be parsed
   * @return a List containing the class paths of the plugins
   */
  private List parseConfigFile(String xmlConfigurationFile) {
    // parses configuration file...
    XmlTools xtools = new XmlTools();
    TNode node = (TNode)xtools.convertXMLToATerm(xmlConfigurationFile);

    if( node == null ) {
      // parsing failed
      logger.log(Level.SEVERE,
		 "ConfigFileNotXML",
		 xmlConfigurationFile);
      return null;
    }

    // ... to extract global options
    optionManager.setGlobalOptionList(node.getDocElem());

    // ... to extract plugin classpaths
    return extractClassPaths(node.getDocElem());
  }

  /**
   * The main method, which runs the PluginPlatform.
   * 
   * @param argumentList the command line
   * @return an error code :
   * <ul>
   * <li>0 if no error was encountered</li>
   * <li>1 if something went wrong</li>
   * </ul>
   */
  public int run(String[] argumentList) {
    String xmlConfigurationFile = whichConfigFile(argumentList);

    if( statusHandler.hasError() ) { // method whichConfigFile() encountered an error
      return 1;
    }

    List classPaths = parseConfigFile(xmlConfigurationFile);
  
    if( statusHandler.hasError() ) { // method parseConfigFile() encountered an error
      return 1;
    }
  
    // creates an instance of each plugin
    for(Iterator it = classPaths.iterator() ; it.hasNext() ;) {
      String path = (String)it.next();
      try { 
        Object pluginInstance = Class.forName(path).newInstance();
        if(pluginInstance instanceof Plugin) {
          instances.add(pluginInstance);
        } else {
          logger.log(Level.SEVERE, "ClassNotAPlugin", path);
        }
      } catch(ClassNotFoundException cnfe) { 
        logger.log(Level.WARNING, "ClassNotFound", path);
      } catch(Exception e) { 
        logger.log(Level.SEVERE, "InstantiationError", path);
      }
    }

    if( statusHandler.hasError() ) {
      return 1;
    }
    
    Object[] inputArgs = optionManager.initOptionManagement(instances,argumentList);
  
    if(statusHandler.hasError()) {
      return 1;
    }

    for(int i = 0; i < inputArgs.length; i++) { // for each file
      Object arg = inputArgs[i];

      logger.log(Level.FINER, "NowCompiling", arg);
      
      // runs the modules
      for(Iterator it = instances.iterator(); it.hasNext(); ) {
        Plugin plugin = (Plugin)it.next();
        plugin.setArg(arg);
        plugin.run();
        arg = plugin.getArg();
        if(statusHandler.hasError()) {
          logger.log(Level.SEVERE, "ProcessingError", inputArgs[i]);
          break;
        }
      }
    }

    int nbOfErrors   = statusHandler.nbOfErrors();
    int nbOfWarnings = statusHandler.nbOfWarnings();

    if( statusHandler.hasError() ) {
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
   * Extracts the plugins' class paths from the XML configuration file.
   * 
   * @param node the node containing the XML document
   * @param v the List into which class paths will be put
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
   * Returns the value of an option. Returns an Object which is a Boolean, a String or an Integer
   * depending on what the option type is.
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
