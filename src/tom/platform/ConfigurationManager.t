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
 * Gregory Andrien
 *
 **/

package tom.platform;

import java.util.*;
import java.util.logging.*;
import java.io.*;


import aterm.*;
import aterm.pure.*;
import jtom.*;

import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import tom.library.xml.*;
import tom.platform.adt.platformoption.*;
import tom.platform.adt.platformoption.types.*;

public class ConfigurationManager {
  
  /** Used to analyse xml configuration file*/
  %include{ adt/TNode.tom }
  
  /**
   * Accessor method necessary when including adt/TNode.tom 
   * @return a TNodeFactory
   */
  private TNodeFactory getTNodeFactory() {
    return TNodeFactory.getInstance(SingletonFactory.getInstance());
  }

  %include{ adt/PlatformOption.tom }
  /**
   * Accessor method necessary to include adt/PlatformOption.tom
   * @return a PlatformOptionFactory
   */
  private PlatformOptionFactory getPlatformOptionFactory() {
    return PlatformOptionFactory.getInstance(SingletonFactory.getInstance());
  }
  
  /** */
  private String xmlConfigurationFileName;

  /** The plugins instance list*/
  private List pluginsReferenceList;

  /** The global options */    
  private PlatformOptionList globalOptions;
  
  public ConfigurationManager(String xmlConfigurationFileName) {
    this.xmlConfigurationFileName = xmlConfigurationFileName;
    this.pluginsReferenceList = new ArrayList();
    globalOptions = `emptyPlatformOptionList;
  }
  
  public int initialize() {
    XmlTools xtools = new XmlTools();
    TNode configurationNode = (TNode)xtools.convertXMLToATerm(xmlConfigurationFileName);
    if(configurationNode == null) {
      // parsing failed
      getLogger().log(Level.SEVERE, "ConfigFileNotXML", xmlConfigurationFileName);
      return 1;
    }
    if(initPluginReferenceList(configurationNode.getDocElem())==1) {
      return 1;
    }
    return initGlobalOptionList(configurationNode.getDocElem());
  }

  public List getPluginsReferenceList() {
    return pluginsReferenceList;
  }
  
  public PlatformOptionList getGlobalOtionList() {
    return globalOptions;
  }
  
  private int initPluginReferenceList(TNode configurationNode) {
    List pluginsClassPaths = extractClassPaths(configurationNode);
    // creates an instance of each plugin
    Iterator classPathIt = pluginsClassPaths.iterator();
    while(classPathIt.hasNext()) {
      String path = (String)classPathIt.next();
      try { 
        Object pluginInstance = Class.forName(path).newInstance();
        if(pluginInstance instanceof Plugin) {
          pluginsReferenceList.add(pluginInstance);
        } else {
          getLogger().log(Level.SEVERE, "ClassNotAPlugin", path);
          pluginsReferenceList = null;
          return 1;
        }
      } catch(ClassNotFoundException cnfe) { 
        getLogger().log(Level.WARNING, "ClassNotFound", path);
      } catch(Exception e) {
            System.out.println("PATH: "+path);
            e.printStackTrace();

        getLogger().log(Level.SEVERE, "InstantiationError", path);
        pluginsReferenceList = null;
        return 1;
      }
    }
    return 0;
  }

    /**
   * Extracts the plugins' class paths from the XML configuration file.
   * 
   * @param node the node containing the XML document
   * @return the List of plugins class path
   */
  private List extractClassPaths(TNode node) {
    List res = new ArrayList();
    %match(TNode node) {
      <server><plugins><plugin [classpath=cp]/></plugins></server> -> {
         res.add(cp);
         getLogger().log(Level.FINER, "ClassPathRead", cp);
       }
    }
    return res;
  }

  /**
   * Extracts the global options from the XML configuration file.
   * 
   * @param node the node containing the XML file
   */
  public int initGlobalOptionList(TNode node) {
    %match(TNode node) {
      <server>opt@<options></options></server> -> {
        globalOptions = xmlNodeToOptionList(`opt);
        return 0;
      }
    }
    return 1;
  }
  
  private PlatformOptionList xmlNodeToOptionList(TNode optionsNode) {
    PlatformOptionList list = `emptyPlatformOptionList();
    %match(TNode optionsNode) {
      <options>(_*,option,_*)</options> -> {
        %match(TNode option) {
          <OptionBoolean [name = n, altName = an, description = d, value = v] /> -> {	
            PlatformBoolean bool = Boolean.valueOf(`v).booleanValue()?`True():`False();
            list = `concPlatformOption(list*, PluginOption(n, an, d, BooleanValue(bool), "")); 
          }
          <OptionInteger [name = n, altName = an, description = d, value = v, attrName = at] /> -> {
            list = `concPlatformOption(list*, PluginOption(n, an, d, IntegerValue(Integer.parseInt(v)), at));
          }
          <OptionString [name = n, altName = an, description = d, value = v, attrName = at] /> -> {
            list = `concPlatformOption(list*, PluginOption(n, an, d, StringValue(v), at));
          }
        }
      }
    }
    return list;
  }

  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

}
