package jtom;

import java.util.*;
import java.util.logging.*;

import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

import tom.platform.*;
import tom.platform.adt.platformoption.*;
import tom.platform.adt.platformoption.types.*;

import tom.library.xml.*;

import aterm.*;
import aterm.pure.*;

/**
 * This Factory, when given a proper XML file, can manage a set of plugins.
 * A plugin managed by this Factory MUST respect two very important conditions :
 * <ul>
 * <li>It MUST have an activation flag, which MUST be the first element in the option list it declares ;</li>
 * <li>In case it has other options, it MUST precise in its requiredOptions() method that each of these options
 * needs the activation flag to be set to true to be set to another value than the default value.</li>
 * </ul>
 * Of course, the plugin must also implement the TomPlugin interface, and the XML file must respect this DTD :
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
public class PluginFactory implements TomPlugin {

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

  private OptionManager getOM() { return TomServer.getInstance().getOptionManager(); }
  private TomEnvironment environment() { return TomEnvironment.getInstance(); }

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
        if(instance instanceof TomPlugin) {
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
      TomPlugin plugin = (TomPlugin)it.next();

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
    TomPlugin activatedPlugin = null;

    Iterator it = flagOwners.keySet().iterator();
	
    while( it.hasNext() ) {
      String flagName = (String)it.next();
	
      if( getOM().getOptionBooleanValue(flagName) ) {
	activatedPlugin = (TomPlugin)flagOwners.get(flagName);
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
