package jtom;

import java.util.*;

import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;

import jtom.adt.options.*;
import jtom.adt.options.types.*;

import jtom.runtime.xml.*;
import jtom.tools.PureFactorySingleton;

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
  %include{ adt/Options.tom }

  TomOptionList allDeclaredOptions;
  TomOptionList allRequiredOptions;
  Map flagOwners;
  ATerm termToRelay;

  private TNodeFactory getTNodeFactory() {
    return (new XmlTools()).getTNodeFactory();
  }

  private OptionsFactory getOptionsFactory() {
    return OptionsFactory.getInstance(PureFactorySingleton.getInstance());
  }

  private OptionManager getOM() { return TomServer.getInstance().getOptionManager(); }
  private TomEnvironment environment() { return TomServer.getInstance().getEnvironment(); }

  public PluginFactory(String xmlFile) {
    allDeclaredOptions = `emptyTomOptionList();
    allRequiredOptions = `emptyTomOptionList();
    flagOwners = new HashMap();
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
          environment().messageError(TomMessage.getString("ClassNotAPlugin"), new Object[]{path},
                                   "PluginFactory", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
        }
      } catch(ClassNotFoundException cnfe) { 
        environment().messageWarning(TomMessage.getString("ClassNotFound"),new Object[]{path},
                                   "PluginFactory", TomMessage.DEFAULT_ERROR_LINE_NUMBER); 
      } catch(Exception e) { 
        environment().messageError(TomMessage.getString("InstantiationError"),
                                 "PluginFactory", TomMessage.DEFAULT_ERROR_LINE_NUMBER); 
      }
    }

    it = plugins.iterator();
    while( it.hasNext() ) {
      TomPlugin plugin = (TomPlugin)it.next();

      TomOptionList declaredList = plugin.declaredOptions();
      allDeclaredOptions = `concTomOption(allDeclaredOptions*, declaredList*);
      String flagName = declaredList.getHead().getName();
      flagOwners.put(flagName, plugin);

      TomOptionList requiredList = plugin.requiredOptions();
      allRequiredOptions = `concTomOption(allRequiredOptions*, requiredList*);
    }
  }

  public void setInput(ATerm term) {
    termToRelay = term;
  }

  public ATerm getOutput() {
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
      activatedPlugin.setInput(termToRelay);
      activatedPlugin.run();
      termToRelay = activatedPlugin.getOutput();
    } catch(NullPointerException npe) {
      System.out.println("Error : No plugin was activated."); // TODO: when error management has changed, change this
      return;
    }
  }

  public TomOptionList declaredOptions() {
    return allDeclaredOptions;
  }

  public TomOptionList requiredOptions() {
    Iterator it = flagOwners.keySet().iterator();
	
    while( it.hasNext() ) { // for all plugins
      String flagName = (String)it.next();
  
      if( getOM().getOptionBooleanValue(flagName) ) { // if this plugin is activated
	it = flagOwners.keySet().iterator();
	      
	while( it.hasNext() ) {
	  String name = (String)it.next();
	  if( !name.equals(flagName) ) // require that the other aren't
	    allRequiredOptions = `concTomOption(OptionBoolean(name, "", "", False()), allRequiredOptions*);
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
