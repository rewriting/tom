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

package tom.platform.tools;

import java.util.*;
import java.util.logging.*;

import aterm.*;
import aterm.pure.*;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

import tom.platform.*;
import tom.platform.adt.platformoption.*;
import tom.platform.adt.platformoption.types.*;


/**
 * This Factory, when given a proper XML file, can manage a set of plugins.
 * A plugin managed by this Factory MUST respect two very important conditions :
 * <ul>
 * <li>It MUST have an activation flag, which MUST be the first element in the option list it declares ;</li>
 * <li>In case it has other options, it MUST precise in its requiredOptions() method that each of these options
 * needs the activation flag to be set to true to be set to another value than the default value.</li>
 * </ul>
 * Of course, the plugin must also implement the Plugin interface, and the XML file must respect this DTD :
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
 * Please note that if you put two or more plugins in the set that have their activation flag set to true by default,
 * it will lead to an error if the user doesn't activate manually (i.e. with the command line) one of the plugins.
 * So all the benefit of having an activation flag set to true by default (that is avoiding to set one manually)
 * will be gone.
 *
 * @author Gr&eacute;gory ANDRIEN
 */
public class PluginFactory implements Plugin {

  %include{ adt/TNode.tom }
  %include{ adt/PlatformOption.tom }

  private PlatformOptionList allDeclaredOptions;
  private PlatformOptionList allRequiredOptions;
  private Map flagOwners;
  private ATerm termToRelay;

  private String pluginName;
  private Logger logger;

  protected Logger getLogger() { return logger; }

  private TNodeFactory getTNodeFactory() {
    return (new XmlTools()).getTNodeFactory();
  }

  private PlatformOptionFactory getPlatformOptionFactory() {
    return PlatformOptionFactory.getInstance(SingletonFactory.getInstance());
  }

  private OptionManager getOM() { return PluginPlatform.getInstance().getOptionManager(); }

  public PluginFactory(String name, String xmlFile) {
    allDeclaredOptions = `emptyPlatformOptionList();
    allRequiredOptions = `emptyPlatformOptionList();
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
          logger.log(Level.SEVERE,
		     "ClassNotAPlugin",
		     new Object[]{pluginName, path});
        }
      } catch(ClassNotFoundException cnfe) { 
        logger.log(Level.WARNING,
		   "ClassNotFound",
		   new Object[]{pluginName, path}); 
      } catch(Exception e) { 
        logger.log(Level.SEVERE,
		   "InstantiationError",
		   new Object[]{pluginName, path});
      }
    }

    it = plugins.iterator();
    while( it.hasNext() ) {
      Plugin plugin = (Plugin)it.next();

      PlatformOptionList declaredList = plugin.declaredOptions();
      allDeclaredOptions = `concPlatformOption(allDeclaredOptions*, declaredList*);
      String flagName = declaredList.getHead().getName();
      flagOwners.put(flagName, plugin);

      PlatformOptionList requiredList = plugin.requiredOptions();
      allRequiredOptions = `concPlatformOption(allRequiredOptions*, requiredList*);
    }
  }

  public void setTerm(ATerm term) {
    termToRelay = term;
  }

  public ATerm getTerm() {
    return termToRelay;
  }

  public void run() {
    Plugin activatedPlugin = null;

    Iterator it = flagOwners.keySet().iterator();
	
    while( it.hasNext() ) {
      String flagName = (String)it.next();
	
      if( getOM().getOptionBooleanValue(flagName) ) {
	activatedPlugin = (Plugin)flagOwners.get(flagName);
      }
    }
    try{
      activatedPlugin.setTerm(termToRelay);
      activatedPlugin.run();
      termToRelay = activatedPlugin.getTerm();
    } catch(NullPointerException npe) {
      System.out.println("Error : No plugin was activated."); // TODO: when error management has changed, change this
      return;
    }
  }

  public PlatformOptionList declaredOptions() {
    return allDeclaredOptions;
  }

  public PlatformOptionList requiredOptions() {
    Iterator it = flagOwners.keySet().iterator();
	
    while( it.hasNext() ) { // for all plugins
      String flagName = (String)it.next();
  
      if( getOM().getOptionBooleanValue(flagName) ) { // if this plugin is activated
	it = flagOwners.keySet().iterator();
	      
	while( it.hasNext() ) {
	  String name = (String)it.next();
	  if( !name.equals(flagName) ) // require that the other aren't
	    allRequiredOptions = `concPlatformOption(OptionBoolean(name, "", "", False()), allRequiredOptions*);
	}
      }
    }

    return allRequiredOptions; 
  }

  public void setOption(String optionName, Object optionValue) {
    getOM().putOptionValue(optionName, optionValue);

    if(optionValue.equals(Boolean.TRUE)) { // no more than 1 plugin can be activated at a time
      if( flagOwners.keySet().contains(optionName) ) { // if the flag just set is an activation flag...
	Iterator it = flagOwners.keySet().iterator();
	
	while( it.hasNext() ) {
	  String flagName = (String)it.next();
	  
	  if( !flagName.equals(optionName) ) {
	      getOM().putOptionValue(flagName, Boolean.FALSE); // ...desactivate the other flags
	    //System.out.println(flagName + " desactivated");
	  }
	}
	//System.out.println(optionName + " activated");
      }
    }
  }

  private void fillClassPathsList(List classPaths, String xmlFile) {
    XmlTools xtools = new XmlTools();
    TNode docNode = ( (TNode)xtools.convertXMLToATerm(xmlFile) ).getDocElem();

    %match(TNode docNode) {
      fact@<factory></factory> -> {

        %match(TNode fact) {
          ElementNode[childList = cl] -> { 
            while(!(cl.isEmpty())) {
              TNode pluginNode = cl.getHead();

              %match(TNode pluginNode) {
                <plugin [classpath = cp] /> -> { classPaths.add(cp);/*System.out.println(cp);*/ }
	      }
              cl = cl.getTail();
	    }
	  }
	}
      }
    }
  }
}
