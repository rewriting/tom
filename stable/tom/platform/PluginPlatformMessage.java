/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2006, INRIA
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

/**
 * The PluginPlatformMessage class is a container for error messages, using the
 * typesafe enum pattern
 */

public class PluginPlatformMessage implements PlatformMessage {
  private final String message;

  private PluginPlatformMessage(String message) {
    this.message = message;
  }

  // Factory messages
  public static final PluginPlatformMessage incompleteXOption =
    new PluginPlatformMessage("Expecting configuration file name after -X option");
  public static final PluginPlatformMessage configFileNotSpecified =
    new PluginPlatformMessage("A configuration file must be specified with the -X option");
  public static final PluginPlatformMessage configFileNotFound =
    new PluginPlatformMessage("The configuration file {0} was not found");

  // Platform messages
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

} // class PluginPlatformMessage
