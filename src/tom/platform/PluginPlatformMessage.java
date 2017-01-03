/*
 * 
 * TOM - To One Matching Compiler
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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 * Antoine Reilles        e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.platform;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.*;

/**
 * The PluginPlatformMessage class is a container for error messages, using the
 * typesafe enum pattern
 */

public class PluginPlatformMessage implements PlatformMessage {
  private final String message;
  private String messageName;

  private static BasicFormatter formatter;

  /*
   * in a first step the class is initialized (each fieldName field is set to null)
   * the initMessageName() method iterates over the static fields
   * and for each of them we set the slot "fieldName" of the corresponding PlatformMessage
   *
   * this method is called from a static block (end of file)
   */
  public static void initMessageName() {
    try {
      Field[] fields = java.lang.Class.forName("tom.platform.PluginPlatformMessage").getDeclaredFields();
      for(Field f:fields) {
        int mod=f.getModifiers();
        if(Modifier.isStatic(mod)) {
          Object o = f.get(null);
          if(o instanceof PluginPlatformMessage) {
            PluginPlatformMessage msg = (PluginPlatformMessage) o;
            msg.setMessageName(f.getName());
            //System.out.println(" --> " + msg.getMessageName());
          }
        }
      }
    } catch(java.lang.Exception e) {
      throw new PlatformException(e.getMessage());
    }
  }


  private PluginPlatformMessage(String message) {
    this.message = message;
    this.formatter = new BasicFormatter();
  }

  // Factory messages
  public static final PluginPlatformMessage incompleteXOption =
    new PluginPlatformMessage("Expecting configuration file name after -X option");
  public static final PluginPlatformMessage configFileNotSpecified =
    new PluginPlatformMessage("A configuration file must be specified with the -X option");
  public static final PluginPlatformMessage configFileNotFound =
    new PluginPlatformMessage("The configuration file {0} was not found");
  public static final PluginPlatformMessage formatterNotFound =
    new PluginPlatformMessage("The formatter class {0} was not found");
  public static final PluginPlatformMessage formatterInvalid =
    new PluginPlatformMessage("The formatter class {0} is invalid");
  public static final PluginPlatformMessage logfileInvalid =
    new PluginPlatformMessage("The log file {0} path is invalid");

  // Platform messages
  public static final PluginPlatformMessage platformStopped =
    new PluginPlatformMessage("Platform stopped");
  public static final PluginPlatformMessage configFileNotXML =
    new PluginPlatformMessage("The configuration file {0} is not a valid XML file");
  public static final PluginPlatformMessage classNotAPlugin =
    new PluginPlatformMessage("Class {0} does not implement the tom.platform.Plugin interface as required");
  public static final PluginPlatformMessage classNotOptionManager=
    new PluginPlatformMessage("Class {0} does not implement the tom.platform.OptionManager interface as required");
  public static final PluginPlatformMessage instantiationError =
    new PluginPlatformMessage("An error occured during the instantiation of class {0}");
  public static final PluginPlatformMessage noPluginFound =
    new PluginPlatformMessage("No plugins of been detected in file {0}, please add at least one.");
  public static final PluginPlatformMessage processingError =
    new PluginPlatformMessage("Plugin {0} reports issues while processing input `{1}`");
  public static final PluginPlatformMessage settingArgError =
    new PluginPlatformMessage("Error while setting arguments to plugin");
  public static final PluginPlatformMessage runErrorMessage =
    new PluginPlatformMessage("=>Execution generated {0,number,integer} fatal error(s): Some files may have not been generated.");
  public static final PluginPlatformMessage runWarningMessage =
    new PluginPlatformMessage("=>Execution generated {0,number,integer} warning(s).");

  //PluginFactory messages
  public static final PluginPlatformMessage noPluginActivated =
    new PluginPlatformMessage("Error : No plugin was activated.");

  // Warning messages (level = Level.WARNING)
  public static final PluginPlatformMessage classNotFound =
    new PluginPlatformMessage("Class {0} not found");

  // Debug messages (level = Level.FINE or FINER or FINEST)
  public static final PluginPlatformMessage nowCompiling =
    new PluginPlatformMessage("Now compiling {0}...");
  public static final PluginPlatformMessage classPathRead =
    new PluginPlatformMessage("Read this class path from the XML file : {0}");

  public String toString() {
    return message;
  }

  public String getMessage() {
    return message;
  }

  public String getMessageName() {
    return messageName;
  }

  public void setMessageName(String name) {
    messageName = name;
  }

  public static final String DEFAULT_ERROR_FILE_NAME = "unknown file";
  public static final int DEFAULT_ERROR_LINE_NUMBER = 1;


  private static void logMessage(Level level,Logger logger, String fileName, int errorLine, PlatformMessage msg, Object[] msgArgs) {
    if(msgArgs==null) {
      msgArgs = new Object[]{};
    }
    if(fileName==null) {
      fileName=DEFAULT_ERROR_FILE_NAME;
      errorLine=DEFAULT_ERROR_LINE_NUMBER;
    }

    if(level==Level.FINER) {
      logger.log(level, msg.getMessage(), msgArgs);
    } else {
      logger.log(level, formatter.format(new PlatformLogRecord(level, msg, msgArgs,fileName, errorLine)));
    }
  }

  public static void error(Logger logger, String fileName, int errorLine, PlatformMessage msg, Object... msgArgs) {
    logMessage(Level.SEVERE, logger, fileName, errorLine, msg, msgArgs);
  }

  public static void warning(Logger logger, String fileName, int errorLine, PlatformMessage msg, Object... msgArgs) {
    logMessage(Level.WARNING, logger, fileName, errorLine, msg, msgArgs);
  }

  public static void info(Logger logger, String fileName, int errorLine, PlatformMessage msg, Object... msgArgs) {
    logMessage(Level.INFO, logger, fileName, errorLine, msg, msgArgs);
  }

  public static void fine(Logger logger, String fileName, int errorLine, PlatformMessage msg, Object... msgArgs) {
    logMessage(Level.FINE, logger, fileName, errorLine, msg, msgArgs);
  }

  public static void finer(Logger logger, String fileName, int errorLine, PlatformMessage msg, Object... msgArgs) {
    logMessage(Level.FINER, logger, fileName, errorLine, msg, msgArgs);
  }

  /*
   * static block: should stay at the end of the file  (after the initialization of static fields)
   */
  static {
    initMessageName();
  }


}
