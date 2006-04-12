/*
 * Gom
 * 
 * Copyright (c) 2005-2006, INRIA
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

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.MemoryHandler;
import java.util.logging.SocketHandler;
import java.util.logging.StreamHandler;

import java.text.MessageFormat;

import tom.platform.BasicFormatter;
import tom.platform.PluginPlatform;
import tom.platform.PluginPlatformFactory;

import tom.gom.GomMessage;

public class Gom {

  /** The current version of the Gom compiler. */
  public final static String VERSION = "0.01";

  /** Log radical string*/
  public final static String LOGRADICAL = "tom.gom";

  /** "java.util.logging.config.file" */
  private final static String LOGGINGPROPERTYFILE =
    "java.util.logging.config.file";

  /** The root logger */
  private final static Logger logger = Logger.getLogger(Gom.LOGRADICAL);

  /** the console handler that level can be changed dynamically */
  private static Handler consoleHandler;

  public static void main(String[] args) {
    exec(args);
  }

  public static int exec(String[] commandLine) {
    try {
      initializeLogging();
    } catch(Exception e) {
      System.err.println(
          MessageFormat.format(GomMessage.loggingInitializationFailure.getMessage(),
            new Object[]{e.getMessage()})
      );
      return 1;
    }
    PluginPlatform platform = PluginPlatformFactory.getInstance().create(commandLine, Gom.LOGRADICAL);
    if(platform == null) {
      return 1;
    }
    return platform.run();
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
    if(logger != null && newLevel.intValue() <= Level.WARNING.intValue()) {
      logger.setLevel(newLevel);
    }
    if(consoleHandler != null && newLevel.intValue() <= Level.WARNING.intValue()) {
      // if we've found a global console handler
      consoleHandler.setLevel(newLevel);
    }
  }

  private static void initializeLogging()
    throws InstantiationException,ClassNotFoundException,
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
      refreshTopLoggerHandlers();
    }
  }

  /**
   * initGomRootLogger set thee useParentHandlers flad and
   * remove all pre-existing handlers that might exist from prior uses
   * especially for multiple invication in the same VM
   */
  private static void initGomRootLogger(boolean useParentHandler) {
    logger.setUseParentHandlers(useParentHandler);
    Handler[] handlers = logger.getHandlers();
    for(int i = 0; i < handlers.length; i++) {
      logger.removeHandler(handlers[i]);
    }
  }

  private static void refreshTopLoggerHandlers()
    throws InstantiationException,ClassNotFoundException,
           IllegalAccessException {
    Handler[] handlers = Logger.getLogger("").getHandlers();
    for(int i=0; i < handlers.length; i++) {
      /*
       * OK, the following code is ugly, I could have made it prettier but
       * it is more robust that way, since it handles all the basic
       * handlers as well as the ones that might extend them.
       * I wrote that because the LogManager won't refresh the formatters,
       * although its properties are set at the appropriate values.
       */
      if(handlers[i] instanceof ConsoleHandler) {
        // search for the global console handler
        consoleHandler = handlers[i];
        handlers[i].setFormatter((Formatter)Class.forName(
              LogManager.getLogManager().getProperty(
                "java.util.logging.ConsoleHandler.formatter")
              ).newInstance());
      } else if(handlers[i] instanceof FileHandler) {
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
