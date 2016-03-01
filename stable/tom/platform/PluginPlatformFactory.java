/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.*;

/**
 * The PluginPlatform factory is a singleton class that is responsable for the
 * creation of PluginPlatform based on a commandLine seen as a array of string
 */
public class PluginPlatformFactory {

  /** Log radical string*/
  public final static String LOG_RADICAL = "tom.platform";

  /** The root logger */
  private static Logger logger = Logger.getLogger(LOG_RADICAL);

  /** the singleton instance*/
  private static PluginPlatformFactory instance = new PluginPlatformFactory();

  /** the PluginPlatformTracker */
  private static Map<Long,PluginPlatform> pluginPlatformTracker;

  public static Map<Long,PluginPlatform> getPluginPlatformTracker() {
    return pluginPlatformTracker;
  }

  public static PluginPlatform getPluginPlatform(Long pluginPlatformID) {
    return pluginPlatformTracker.get(pluginPlatformID);
  }

  public static PluginPlatform putPluginPlatform(Long pluginPlatformID, PluginPlatform pluginPlatform) {
    return pluginPlatformTracker.put(pluginPlatformID,pluginPlatform);
  }

/*
 private static int threadsCounter = 0;

 public synchronized static void increaseThreadsCounter() {
    threadsCounter++;
  }

  public synchronized static void decreaseThreadsCounter() {
    threadsCounter--;
  }

  public synchronized static int getThreadsCounter() {
    return threadsCounter;
  }
*/
///////////////////


  /** protection again instanciation */
  private PluginPlatformFactory() {
    pluginPlatformTracker = new HashMap<Long,PluginPlatform>();
    logger.setUseParentHandlers(false);
    Handler consoleHandler = new ConsoleHandler();
    consoleHandler.setLevel(Level.WARNING);
    // by default, print everything that the logger sends
    Handler[] handlers = PluginPlatformFactory.logger.getHandlers();
    for(int i = 0; i < handlers.length; i++) {
      PluginPlatformFactory.logger.removeHandler(handlers[i]);
    }
    PluginPlatformFactory.logger.addHandler(consoleHandler);
 }

  /** the singleton accessor*/
  public static PluginPlatformFactory getInstance() {
    return instance;
  }

  /**
   * Based on an array of string, the create method return a
   * PluginPlatform or null if something wrong occurs.  The first
   * argument shall contain a sequence of string -X and configFileName
   * to be able to create the PluginPlatform.
   */
  public PluginPlatform create(String[] commandLine, String logRadical,List<String> inputToCompileList) {
    String confFileName = extractConfigFileName(commandLine);
    if (null == confFileName) {
      return null;
    }
    ConfigurationManager confManager = new ConfigurationManager(confFileName);
    if (confManager.initialize(commandLine) == 1) {
      return null;
    }
    return new PluginPlatform(confManager.getPluginsList(),logRadical,inputToCompileList);
  }

  //second PluginPlatform create() method : used if it is launched by Gom
  public PluginPlatform create(String[] commandLine,
                               String logRadical,
                               List<String> inputToCompileList,
                               Map<String,String> informationTracker) {
    String confFileName = extractConfigFileName(commandLine);
    if (confFileName == null) {
      return null;
    }
    ConfigurationManager confManager = new ConfigurationManager(confFileName);
    if (confManager.initialize(commandLine) == 1) {
      return null;
    }
    return new PluginPlatform(confManager.getPluginsList(),logRadical,inputToCompileList,informationTracker);
  }

  /**
   * This method analyzes the command line and determines which configuration
   * file should be used. As the tom scripts already specify a default
   * configuration file which can be overridden by the user, only the last one
   * is taken into account
   *
   * @param commandLine the command line
   * @return a String containing the path to the configuration file to be used
   */
  public static String extractConfigFileName(String[] commandLine) {
    String xmlConfigurationFile = null;
    int i=0;
    List<String> commandList = new ArrayList<String>();
    try {
      for (; i < commandLine.length; i++) {
        if (commandLine[i].equals("-X")) {
          xmlConfigurationFile = commandLine[++i];
        } else {
          commandList.add(commandLine[i]);
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
    PluginPlatformMessage.error(logger, null, 0, 
        PluginPlatformMessage.incompleteXOption, commandLine[--i]);
      return null;
    }

    try {
      File file = new File(xmlConfigurationFile).getCanonicalFile();
      if (file.exists()) {
        // side effect on the commandLine since config information is no more needed
        commandLine = commandList.toArray(new String[0]);
        return xmlConfigurationFile;
      }
    } catch(Exception e) {}
    PluginPlatformMessage.error(logger, null, 0, PluginPlatformMessage.configFileNotSpecified);
    return null;
  }

  public static void refreshTopLoggerHandlers()
    throws InstantiationException,ClassNotFoundException,
           IllegalAccessException {
    Handler[] handlers = Logger.getLogger("").getHandlers();
    for (int i=0; i < handlers.length; i++) {
      /*
       * OK, the following code is ugly, I could have made it prettier but
       * it is more robust that way, since it handles all the basic
       * handlers as well as the ones that might extend them.
       * I wrote that because the LogManager won't refresh the formatters,
       * although its properties are set at the appropriate values.
       */
      if(handlers[i] instanceof ConsoleHandler) {
        handlers[i].setFormatter((Formatter)Class.forName(
              LogManager.getLogManager().getProperty(
                "java.util.logging.ConsoleHandler.formatter")
              ).newInstance());
      } else if (handlers[i] instanceof FileHandler) {
        handlers[i].setFormatter((Formatter)Class.forName(
              LogManager.getLogManager().getProperty(
                "java.util.logging.FileHandler.formatter")
              ).newInstance());
      } else if(handlers[i] instanceof SocketHandler) {
        handlers[i].setFormatter((Formatter)Class.forName(
              LogManager.getLogManager().getProperty(
                "java.util.logging.SocketHandler.formatter")
              ).newInstance());
      } else if(handlers[i] instanceof MemoryHandler) {
        handlers[i].setFormatter((Formatter)Class.forName(
              LogManager.getLogManager().getProperty(
                "java.util.logging.MemoryHandler.formatter")
              ).newInstance());
      } else if(handlers[i] instanceof StreamHandler) {
        handlers[i].setFormatter((Formatter)Class.forName(
              LogManager.getLogManager().getProperty(
                "java.util.logging.StreamHandler.formatter")
              ).newInstance());
      }
    }
  }
}
