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
import java.util.*;

import jtom.tools.*;
import tom.platform.*;

/**
 * 
 */
public class PluginPlatformFactory {

  /** PluginPlatformFactory message ressource file name */
  private final static String MESSAGERESOURCE = "tom.platform.PluginPlatformResources";

  /** The root logger */
  private static Logger logger = Logger.getLogger("tom.platform", MESSAGERESOURCE);

  private static PluginPlatformFactory instance = new PluginPlatformFactory();

  public static PluginPlatformFactory getInstance() {
    return instance;
  }

  public PluginPlatform create(String[] commandLine, String logRadical) {
    String confFileName = extractConfigFileName(commandLine);
    if(confFileName == null) {
      return null;
    }
    ConfigurationManager confManager = new ConfigurationManager(confFileName);
    if(confManager.initialize(commandLine) == 1) {
      System.out.println("confManager init fail");
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
      getLogger().log(Level.SEVERE, "IncompleteOption", commandLine[--i]);
      return null;
    }
    
    if(xmlConfigurationFile==null) { // lack of a configuration file
      getLogger().log(Level.SEVERE, "ConfigFileNotSpecified");
      return null;
    }
    
    File file = new File(xmlConfigurationFile);
    if(!file.exists()) { // the last specified configuration file doesn't exist
      getLogger().log(Level.SEVERE, "ConfigFileNotFound", xmlConfigurationFile);
      return null;
    }
    commandLine = (String[])commandList.toArray(new String[]{});
    return xmlConfigurationFile;
  }

  private static Logger getLogger() {
    return logger;
  }

} // class PluginPlatformFactory
