/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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

/**
 * The BasicPlatformMessage class provides common implementation for
 * PlatformMessage typesafe enum pattern
 */

public class BasicPlatformMessage implements PlatformMessage {
  /*
   * name of the error/warning message 
   */
  private String messageName;

  private final String message;

  private static BasicFormatter formatter;

  protected BasicPlatformMessage(String message) {
    this.message = message;
    this.formatter = new BasicFormatter();
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

  public String toString() {
    return message;
  }

  private static void logMessage(Level level,Logger logger, String fileName, int errorLine, PlatformMessage msg, Object[] msgArgs) {
    if (null == msgArgs) {
      msgArgs = new Object[]{};
    }
    if (null == fileName) {
      fileName=DEFAULT_ERROR_FILE_NAME;
      errorLine=DEFAULT_ERROR_LINE_NUMBER;
    }

    if (Level.FINER == level) {
      logger.log(level, msg.getMessage(), msgArgs);
    } else {
      logger.log(new PlatformLogRecord(level, msg, msgArgs,fileName, errorLine));
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

}
