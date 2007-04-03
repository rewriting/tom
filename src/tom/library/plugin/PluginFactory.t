/*
 *
 * Copyright (c) 2000-2007, Pierre-Etienne Moreau
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 **/

package tom.library.plugin;

import java.util.*;
import java.util.logging.*;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

import tom.platform.*;
import tom.platform.adt.platformoption.*;
import tom.platform.adt.platformoption.types.*;


/**
 * This Factory, when given a proper XML file, can manage a set of plugins.
 * A plugin managed by this Factory MUST respect two very important conditions:
 * <ul>
 * <li>It MUST have an activation flag, which MUST be the first element in the
 * option list it declares;</li>
 * <li>In case it has other options, it MUST precise in its getRequiredOptions
 * () method that each of these options needs the activation flag to be set to
 * true to be set to another value than the default value.</li>
 * </ul>
 * Of course, the plugin must also implement the Plugin interface,
 * and the XML file must respect this DTD :
 * <!DOCTYPE factory [
 *
 * <!ELEMENT factory (plugin*)>
 *
 * <!ELEMENT plugin EMPTY>
 * <!ATTLIST plugin
 *   name CDATA #IMPLIED
 *   version CDATA #IMPLIED
 *   description CDATA #IMPLIED
 *   classpath CDATA #REQUIRED>
 * ]>
 * Please note that if you put two or more plugins in the set that
 * have their activation flag set to true by default, it will lead to
 * an error if the user doesn't activate manually (i.e. with the
 * command line) one of the plugins.  So all the benefit of having an
 * activation flag set to true by default (that is avoiding to set one
 * manually) will be gone.
 *
 * @author Gr&eacute;gory ANDRIEN
 */

public class PluginFactory implements Plugin {

  %include{ adt/tnode/TNode.tom }
  %include{ adt/platformoption/PlatformOption.tom }

  private PlatformOptionList allDeclaredOptions;
  private PlatformOptionList allRequiredOptions;
  private Map flagOwners;
  private Object[] argToRelay;
  private OptionManager optionManager; // it is never written!

  private String pluginName;
  private Logger logger;

  protected Logger getLogger() { return logger; }

  private OptionManager getOM() {
    return optionManager;
  }

  public PluginFactory(String name, String xmlFile) {
    allDeclaredOptions = `concPlatformOption();
    allRequiredOptions = `concPlatformOption();
    flagOwners = new HashMap();

    pluginName = name;
    logger = Logger.getLogger(getClass().getName());

    List classPaths = new ArrayList();
    List plugins = new ArrayList();

    fillClassPathsList(classPaths, xmlFile);

    // creates an instance of each plugin
    Iterator it = classPaths.iterator();
    while( it.hasNext() ) {
      Object instance;
      String path = (String)it.next();
      try {
        instance = Class.forName(path).newInstance();
        if(instance instanceof Plugin) {
          plugins.add(instance);
        } else {
          logger.log(Level.SEVERE, "ClassNotAPlugin",
                     new Object[]{pluginName, path});
        }
      } catch(ClassNotFoundException cnfe) {
        logger.log(Level.WARNING, "ClassNotFound",
                   new Object[]{pluginName, path});
      } catch(Exception e) {
        logger.log(Level.SEVERE, "InstantiationError",
                   new Object[]{pluginName, path});
      }
    }

    it = plugins.iterator();
    while( it.hasNext() ) {
      Plugin plugin = (Plugin)it.next();

      PlatformOptionList declaredList = plugin.getDeclaredOptionList();
      allDeclaredOptions = `concPlatformOption(allDeclaredOptions*, declaredList*);
      String flagName = declaredList.getHeadconcPlatformOption().getName();
      flagOwners.put(flagName, plugin);

      PlatformOptionList requiredList = plugin.getRequiredOptionList();
      allRequiredOptions = `concPlatformOption(allRequiredOptions*, requiredList*);
    }
  }

  /**
   * From Plugin interface
   */
  public void setArgs(Object arg[]) {
    argToRelay = (Object[]) arg.clone();
  }

  /**
   * From Plugin interface
   */
  public Object[] getArgs() {
    return (Object[]) argToRelay.clone();
  }

  /**
   * From Plugin interface
   */
  public void run() {
    Plugin activatedPlugin = null;
    Iterator it = flagOwners.keySet().iterator();
    while(it.hasNext()) {
      String flagName = (String)it.next();
      if( ((Boolean)getOM().getOptionValue(flagName)).booleanValue() ) {
        activatedPlugin = (Plugin)flagOwners.get(flagName);
      }
    }
    try{
      activatedPlugin.setArgs(argToRelay);
      activatedPlugin.run();
      argToRelay = activatedPlugin.getArgs();
    } catch(NullPointerException npe) {
      System.out.println("Error : No plugin was activated.");
      // TODO: when error management has changed, change this
    }
  }

  /**
   * From OptionOwner interface inherited from Plugin interface
   */
  public void setOptionManager(OptionManager optionManager) {}

  /**
   * From OptionOwner interface inherited from Plugin interface
   */
  public PlatformOptionList getDeclaredOptionList() {
    return allDeclaredOptions;
  }

  /**
   * From OptionOwner interface inherited from Plugin interface
   */
  public PlatformOptionList getRequiredOptionList() {
    Iterator it = flagOwners.keySet().iterator();
    while(it.hasNext()) { // for all plugins
      String flagName = (String)it.next();
      if(((Boolean)getOM().getOptionValue(flagName)).booleanValue()) {
        // if this plugin is activated
        it = flagOwners.keySet().iterator();

        while( it.hasNext() ) {
          String name = (String)it.next();
          if( !name.equals(flagName) ) // require that the other aren't
            allRequiredOptions = `concPlatformOption(PluginOption(name, "", "", BooleanValue(False()),""), allRequiredOptions*);
        }
      }
    }

    return allRequiredOptions;
  }

  /**
   * From OptionOwner interface inherited from Plugin interface
   */
  public void optionChanged(String optionName, Object optionValue) {
    getOM().setOptionValue(optionName, optionValue);

    if(optionValue.equals(Boolean.TRUE)) {
      // no more than 1 plugin can be activated at a time
      if( flagOwners.keySet().contains(optionName) ) {
        // if the flag just set is an activation flag...
        Iterator it = flagOwners.keySet().iterator();
        while( it.hasNext() ) {
          String flagName = (String)it.next();
          if( !flagName.equals(optionName) ) {
            getOM().setOptionValue(flagName, Boolean.FALSE);
            // ...desactivate the other flags
            //System.out.println(flagName + " desactivated");
          }
        }
        //System.out.println(optionName + " activated");
      }
    }
  }

  private void fillClassPathsList(List classPaths, String xmlFile) {
    XmlTools xtools = new XmlTools();
    TNode docNode = ( xtools.convertXMLToTNode(xmlFile) ).getDocElem();

    %match(TNode docNode) {
      fact@<factory></factory> -> {

        %match(TNode fact) {
          ElementNode[ChildList=cl] -> {
            while(!(`cl.isEmptyconcTNode())) {
              TNode pluginNode = `cl.getHeadconcTNode();

              %match(TNode pluginNode) {
                <plugin [classpath = cp] /> -> { classPaths.add(`cp);/*System.out.println(cp);*/ }
              }
              `cl = `cl.getTailconcTNode();
            }
          }
        }
      }
    }
  }

}
