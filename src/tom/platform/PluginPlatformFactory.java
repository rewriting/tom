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

import java.util.logging.*;
import java.io.*;
import java.util.*;

import jtom.tools.*;
import tom.platform.*;

/**
 * The PluginPlatform factory is a singleton class that is responsable for the
 * creation of PluginPlatform based on a commandLine seen as a array of string
 */
public class PluginPlatformFactory {

  /** PluginPlatformFactory message ressource file name */
  private final static String MESSAGERESOURCE = "tom.platform.PluginPlatformResources";

    /** Log radical string*/
  public final static String LOGGERRADICAL = "tom.platform";

  /** The root logger */
  private static Logger logger = Logger.getLogger(LOGGERRADICAL, MESSAGERESOURCE);
  
  /** the singleton instance*/
  private static PluginPlatformFactory instance = new PluginPlatformFactory();

  /** protection again instanciation */
  private PluginPlatformFactory() {}
  
  /** the singleton accessor*/
  public static PluginPlatformFactory getInstance() {
    return instance;
  }

  /**
   * Based on an array of string, the create method return a PLuginPlatform or null if
   * something wrong occurs.
   * The first argument shall contain a sequence of string -X and configFileName to be 
   * able to create the PluginPlatform.
   */
  public PluginPlatform create(String[] commandLine, String logRadical) {
    String confFileName = extractConfigFileName(commandLine);
    if(confFileName == null) {
      return null;
    }
    ConfigurationManager confManager = new ConfigurationManager(confFileName);
    if(confManager.initialize(commandLine) == 1) {
      return null;
    }
    return new PluginPlatform(confManager,logRadical);
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
  private static String extractConfigFileName(String[] commandLine) {
    String xmlConfigurationFile = null; 
    int i=0;
    List commandList = new ArrayList();
    try {
      for(;i< commandLine.length;i++) {
        if(commandLine[i].equals("-X")) {
          xmlConfigurationFile = commandLine[++i];
        } else {
          commandList.add(commandLine[i]);
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
    // side effect on the commandLine since config information is no more needed
    commandLine = (String[])commandList.toArray(new String[]{});
    return xmlConfigurationFile;
  }

} // class PluginPlatformFactory
