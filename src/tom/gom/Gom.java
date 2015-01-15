/*
 * Gom
 *
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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

package tom.gom;

import java.util.*;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import java.text.MessageFormat;

import tom.platform.BasicFormatter;
import tom.platform.PluginPlatform;
import tom.platform.PluginPlatformFactory;
import tom.platform.ConfigurationManager;

import tom.gom.GomMessage;

public class Gom {

  /** Log radical string */
  public final static String LOGRADICAL = "tom.gom";

  /** "java.util.logging.config.file" */
  private final static String LOGGINGPROPERTYFILE =
    "java.util.logging.config.file";

  /** The root logger */
  private final static Logger logger = Logger.getLogger(Gom.LOGRADICAL);

  /** the console handler that level can be changed dynamically */
  private static Handler consoleHandler;

  public static void main(String[] args) {
    Map<String,String> informationTracker = new HashMap<String,String>();
    int errno = exec(args,informationTracker);
    System.exit(errno);
  }

  // different from the Tom.exec() -> need an informationTracker
  public static int exec(String[] commandLine, Map<String,String> informationTracker) {
    try {
      initializeLogging();
    } catch(Exception e) {
      System.err.println(
          MessageFormat.format(
            GomMessage.loggingInitializationFailure.getMessage(),
            new Object[]{ e.getMessage() }));
      return 1;
    }
    // Retrieve the configuration file name
    String configFileName =
      PluginPlatformFactory.getInstance().extractConfigFileName(commandLine);
    if (null == configFileName) {
      return 1;
    }
    // Create a ConfigurationManager in order to retrieve the 'global' inputToCompileList
    ConfigurationManager initConfigurationManager = new ConfigurationManager(configFileName);
    if (initConfigurationManager.initialize(commandLine) == 1) {
      return 1;
    }
    // Retrieve the whole fileList that has to be compiled
    List<String> wholeInputToCompileList =
      initConfigurationManager.getOptionManager().getInputToCompileList();
    int res=0;

    PluginPlatform platform =
      PluginPlatformFactory.getInstance().create(commandLine,Gom.LOGRADICAL,wholeInputToCompileList,informationTracker);
    if (platform == null) {
      return 1;
    }
    platform.run();
    return platform.getRunResult();
  }

  /**
   * This method should be used to change the Level of logging, instead of
   * directly accessing to the logger via Logger.getLogger(tom.gom).
   * Indeed, this method respect the fact that the logger's Level should
   * never ever be set higher than Level.WARNING, because it would cause the
   * StatusHandler to malfunction. The StatusHandler won't
   * see warnings that's why the noWarning option is handled by changing the
   * ConsoleHandler's level while the verbose option lowers the rootLogger's
   * level to Level.INFO.
   *
   * @param newLevel the Level to which we want to set the log output
   */
  public static void changeLogLevel(Level newLevel) {
    if (logger != null && newLevel.intValue() <= Level.WARNING.intValue()) {
      logger.setLevel(newLevel);
    }
    if (consoleHandler != null && newLevel.intValue() <= Level.WARNING.intValue()) {
      // if we've found a global console handler
      consoleHandler.setLevel(newLevel);
    }
  }

  private static void initializeLogging()
    throws InstantiationException, ClassNotFoundException,
           IllegalAccessException, IOException {
    String loggingConfigFile = System.getProperty(LOGGINGPROPERTYFILE);
    if (loggingConfigFile == null) { // default > no custom file is used
      // create a configuration equivalent to defaultlogging.properties file
      initGomRootLogger(false);
      logger.setLevel(Level.WARNING);
      consoleHandler = new ConsoleHandler();
      consoleHandler.setLevel(Level.ALL);
      // by default, print everything that the logger sends
      consoleHandler.setFormatter(new BasicFormatter());
      logger.addHandler(consoleHandler);
    } else { // custom configuration file for LogManager is used
      LogManager.getLogManager().readConfiguration();
      initGomRootLogger(true);
      PluginPlatformFactory.refreshTopLoggerHandlers();
    }
  }

  /**
   * initGomRootLogger set the useParentHandlers flag and
   * remove all pre-existing handlers that might exist from prior uses
   * especially for multiple invication in the same VM
   */
  private static void initGomRootLogger(boolean useParentHandler) {
    logger.setUseParentHandlers(useParentHandler);
    Handler[] handlers = logger.getHandlers();
    for (int i = 0; i < handlers.length; i++) {
      logger.removeHandler(handlers[i]);
    }
  }
}
