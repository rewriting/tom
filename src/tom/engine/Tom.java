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

package jtom;

import java.util.logging.*;
import java.io.*;

import jtom.tools.*;
import tom.platform.*;

/**
 * Main Tom project class
 */
public class Tom {
  
  /** The current version of the TOM compiler. */
  public final static String VERSION = "2.1 - Under development";
  
  /** Log radical string*/
  public final static String LOGGERRADICAL = "jtom";
  
  /** Tom message ressource file name string*/
  private final static String MESSAGERESOURCE = "jtom.TomMessageResources";
    
  /** The root logger */
  private static Logger logger = Logger.getLogger(LOGGERRADICAL, MESSAGERESOURCE);
  
  /** the console handler that level can be changed dynamically */
  private static Handler consoleHandler = null;
  
  public static void main(String[] args) {
    exec(args);
  }
  
  public static int exec(String[] commandLine) {
    try {
      initializeLogging();
    } catch(Exception e) {
      System.err.println("Error during the initialization of Tom: " + e.getMessage());
      e.printStackTrace();
      return 1;
    }
    
    PluginPlatform platform = PluginPlatformFactory.getInstance().create(commandLine, Tom.LOGGERRADICAL); 
    if(platform == null) {
      return 1;
    }
    return platform.run();
  }
   
  /**
   * This method should be used to change the Level of logging, instead of
   * directly accessing to the logger via Logger.getLogger("jtom").
   * Indeed, this method respect the fact that the logger's Level should
   * never ever be set higher than Level.WARNING, because it would cause the 
   * StatusHandler to malfunction.
   *
   * @param newLevel the Level to which we want to set the log output
   */
  public static void changeLogLevel(Level newLevel) {
    /* 
     * IMPORTANT: the rootLogger's Level can be lowered but shouldn't be risen.
     * Indeed, if it is higher than Level.WARNING, the StatusHandler won't
     * see warnings that's why the noWarning option is handled by changing the
     * ConsoleHandler's level while the verbose option lowers the rootLogger's
     * level to Level.INFO.
     */
    if(logger != null && newLevel.intValue() <= Level.WARNING.intValue()) {
      logger.setLevel(newLevel);
    } else if(consoleHandler != null) {
      // if we've found a global console handler
      consoleHandler.setLevel(newLevel);
      // warnings are no more printed, but still seen by the StatusHandler
    }
  }
 
  private static void initializeLogging() throws IOException,
                                                 InstantiationException,
                                                 ClassNotFoundException,
                                                 IllegalAccessException {
    String loggingConfigFile =
      System.getProperty("java.util.logging.config.file");
    if (loggingConfigFile == null) { // default > no custom file is used
      // create a configuration equivalent to normalLog.properties file
      initTomRootLogger(false);
      logger.setLevel(Level.WARNING);
      consoleHandler = new ConsoleHandler();
      consoleHandler.setLevel(Level.ALL);
      // by default, print everything that the logger sends
      consoleHandler.setFormatter(new TomBasicFormatter());
      logger.addHandler(consoleHandler);
    } else { // custom configuration file for LogManager is used
      //LogManager.getLogManager().readConfiguration();
      initTomRootLogger(true);
      refreshTopLoggerHandlers();
    }
  }
  
  /**
   * initTomRootLogger set thee useParentHandlers flad and
   * remove all pre-existing handlers that might exist from prior uses
   * especially for multiple invication in the same VM
   */
  private static void initTomRootLogger(boolean useParentHandler) {
    //logger = Logger.getLogger(Tom.LOGGERRADICAL, Tom.MESSAGERESOURCE);
    logger.setUseParentHandlers(useParentHandler);
    Handler[] handlers = logger.getHandlers();
    for(int i = 0; i < handlers.length; i++) {
      logger.removeHandler(handlers[i]);
    }
    /*Status statusHandler = new StatusHandler();
      Logger.getLogger(loggerRadical).addHandler(instance.statusHandler);
    */
  }
  
  private static void refreshTopLoggerHandlers() throws InstantiationException,
                                                        ClassNotFoundException,
                                                        IllegalAccessException
  {
    Handler[] handlers = Logger.getLogger("").getHandlers();
    for(int i = 0; i < handlers.length; i++) {
      /*
       * OK, the following code is ugly, I could have made it prettier but
       * it is more robust that way, since it handles all the basic
       * handlers as well as the ones that might extend them.
       * I wrote that because the LogManager won't refresh the formatters,
       * although its properties are set at the appropriate values.
       */
      if( handlers[i] instanceof ConsoleHandler ) {
        // search for the global console handler
        consoleHandler = handlers[i];
        handlers[i].setFormatter((Formatter)Class.forName(
                                                          LogManager.getLogManager().getProperty("java.util.logging.ConsoleHandler.formatter")).newInstance());
      }/*else if( handlers[i] instanceof FileHandler ) {
         handlers[i].setFormatter((Formatter)Class.forName(LogManager.getLogManager().getProperty("java.util.logging.FileHandler.formatter")).newInstance());
         } else if( handlers[i] instanceof SocketHandler ) {
         handlers[i].setFormatter((Formatter)Class.forName(LogManager.getLogManager().getProperty("java.util.logging.SocketHandler.formatter")).newInstance());
         } else if( handlers[i] instanceof MemoryHandler ) {
         handlers[i].setFormatter((Formatter)Class.forName(LogManager.getLogManager().getProperty("java.util.logging.MemoryHandler.formatter")).newInstance());
         } else if( handlers[i] instanceof StreamHandler ) {
         handlers[i].setFormatter((Formatter)Class.forName(LogManager.getLogManager().getProperty("java.util.logging.StreamHandler.formatter")).newInstance());
         }*/
      //System.out.println("Handler "+handlers[i]+" has formatter "+handlers[i].getFormatter());    
    }
  }

} // class Tom
