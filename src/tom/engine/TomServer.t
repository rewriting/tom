package jtom;

import java.util.*;
import java.io.*;

import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;

import jtom.exception.*;

import jtom.runtime.xml.*;

import aterm.*;
import aterm.pure.*;

/**
 * The TomServer manages plugins. It parses Tom.xml in order to find which
 * plugins are used and how they are ordered. Then it instantiates them.
 *
 * @author Gr&eacute;gory ANDRIEN
 */
public class TomServer {

  %include{ adt/TNode.tom }

  /**
   * The current version of the TOM compiler. 
   */
  public final static String VERSION = "3.0alpha";
    
  /**
   * The List containing a reference to the plugins.
   * It also has a reference to the TomServer (for option management purposes).
   */
  private List instances;
    
  /**
   * 
   */
  private TNodeFactory tNodeFactory;
	
  /**
   * 
   */
  private TomEnvironment environment;

  /**
   *
   */
  private OptionManager optionManager;
	
  /**
   * An accessor method.
   * 
   * @return a TNodeFactory
   */  
  public TNodeFactory getTNodeFactory() { return tNodeFactory; }
	
  /**
   * An accessor method.
   * 
   * @return a TomEnvironment
   */
  public TomEnvironment getEnvironment() { return environment; }

  /**
   * An accessor method.
   * 
   * @return an OptionManager
   */
  public OptionManager getOptionManager() { return optionManager; }

  /**
   * Part of the Singleton pattern. The unique instance of the TomServer.
   */
  private static TomServer instance = null;
    
  /**
   * Part of the Singleton pattern. A protected constructor method, that exists to defeat instantiation.
   */
  protected TomServer(){}
    
  /**
   * Part of the Singleton pattern. Returns the instance of the TomServer if it has been initialized before,
   * otherwise it throws a TomRuntimeException.
   * 
   * @return the instance of the TomServer
   * @throws TomRuntimeException if the TomServer hasn't been initialized before the call
   */
  public static TomServer getInstance()
  {
    if(instance == null)
	    {
        throw new TomRuntimeException(TomMessage.getString("GetInitializedTomServerInstance"));
	    }
    return instance;
  }

  /**
   * Part of the Singleton pattern. Initializes the TomServer in case it hasn't been done before,
   * otherwise it reinitializes it.
   * 
   * @return the instance of the TomServer
   */
  public static TomServer create() {
    if(instance == null) {
      instance = new TomServer();
        
      instance.instances = new ArrayList();
      instance.tNodeFactory = TNodeFactory.getInstance(SingletonFactory.getInstance());
      instance.environment = TomEnvironment.create();
      instance.optionManager = new TomOptionManager();
	
      return instance;
    } else {
      TomServer.clear();
      return instance;
    }
  }

  /**
   * Reinitializes the TomServer instance.
   */
  public static void clear() {
    instance.instances = new ArrayList();
    instance.environment = TomEnvironment.create();
    instance.optionManager = new TomOptionManager();
  }

  /**
   * 
   * @param args
   * @return
   */
  public static int exec(String args[]) {
    TomServer server = TomServer.create();
    return server.run(args);
  }

  /**
   * This method analyzes the command line and determines which configuration file should be used.
   * 
   * @param argumentList the command line
   * @return a String containing the path to the configuration file to be used
   */
  private String whichConfigFile(String[] argumentList) {
    // there is no default configuration file
    // because the tom script already specifies a configuration file
    // (which can be overridden by the user since only the last one that is specified is taken into account)
    // so there MUST be at least one "-X" option
    // (should be remembered if the TomServer is used outside of the tom script)
    String xmlConfigurationFile = null; 
	
    int i = 0;

    try {
      for(; i < argumentList.length; i++) {
        if(argumentList[i].equals("-X")) {
          // tests if argumentList redefines the configuration file
          xmlConfigurationFile = argumentList[++i];
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      environment.messageError(TomMessage.getString("IncompleteOption"), 
                               new Object[]{argumentList[--i]}, 
                               "TomServer", 
                               TomMessage.DEFAULT_ERROR_LINE_NUMBER);
    }

    if( environment.hasError() ) {
      environment.printErrorMessage();
      return null;
    }

    try {
      File file = new File(xmlConfigurationFile);
      
      if(! file.exists() ) {
        // the case where the specified file doesn't exist is handled here
        environment.messageError(TomMessage.getString("ConfigFileNotFound"), 
                                 new Object[]{xmlConfigurationFile}, 
                                 "TomServer", 
                                 TomMessage.DEFAULT_ERROR_LINE_NUMBER);
      }
    } catch(NullPointerException npe) {
      // the lack of a configuration file is handled here
      environment.messageError(TomMessage.getString("ConfigFileNotSpecified"), 
                               "TomServer", 
                               TomMessage.DEFAULT_ERROR_LINE_NUMBER);
    }

    if( environment.hasError() ) {
      environment.printErrorMessage();
      return null;
    }

    return xmlConfigurationFile;
  }

  /**
   * This method parses the configuration and extracts the global options as well as
   * the class paths of the plugins that are going to be used.
   * 
   * @param xmlConfigurationFile the name of the XML configuration file to be parsed
   * @return a List containing the class paths of the plugins
   */
  private List parseConfigFile(String xmlConfigurationFile) {
    // parses configuration file...
    XmlTools xtools = new XmlTools();
    List classPaths = new ArrayList();
    TNode node = (TNode)xtools.convertXMLToATerm(xmlConfigurationFile);

    if( node == null ) {
      // parsing failed
      environment.messageError(TomMessage.getString("ConfigFileNotXML"), 
                               new Object[]{xmlConfigurationFile}, 
                               "TomServer", 
                               TomMessage.DEFAULT_ERROR_LINE_NUMBER);
      return null;
    }

    // ... to extract global options
    optionManager.extractOptionList(node.getDocElem());

    // ... to extract plugin classpaths
    extractClassPaths(node.getDocElem(), classPaths);
    return classPaths;
  }

  /**
   * The main method, which runs the TomServer.
   * 
   * @param argumentList the command line
   * @return an error code :
   * <ul>
   * <li>0 if no error was encountered</li>
   * <li>1 if something went wrong</li>
   * </ul>
   */
  public int run(String[] argumentList) {
    String xmlConfigurationFile = whichConfigFile(argumentList);

    if( xmlConfigurationFile == null ) {
	    // method whichConfigFile encountered an error
	    return 1;
    }

    List classPaths = parseConfigFile(xmlConfigurationFile);
  
    if( classPaths == null ) {
      // method parseConfigFile encountered an error
      environment.printErrorMessage();
      return 1;
    }
  
    // creates an instance of each plugin
    Iterator it = classPaths.iterator();
    while(it.hasNext()) {
      Object instance;
      String path = (String)it.next();
      try { 
        instance = Class.forName(path).newInstance();
        if(instance instanceof TomPlugin) {
          instances.add(instance);
        } else {
	  //System.out.println("pas un plugin");
          environment.messageError(TomMessage.getString("ClassNotAPlugin"), new Object[]{path},
                                   "TomServer", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
        }
      } catch(ClassNotFoundException cnfe) { 
        environment.messageWarning(TomMessage.getString("ClassNotFound"),new Object[]{path},
                                   "TomServer", TomMessage.DEFAULT_ERROR_LINE_NUMBER); 
      } catch(Exception e) { 
        environment.messageError(TomMessage.getString("InstantiationError"),
                                 "TomServer", TomMessage.DEFAULT_ERROR_LINE_NUMBER); 
      }
    }
    
    optionManager.setPlugins(instances);

    String[] inputFiles = optionManager.optionManagement(argumentList);
  
    if( environment.hasError() ) {
      environment.printAlertMessage("TomServer");
      //displayHelp();
      // it is kind of hard to display the help message outside of the option
      // manager, cause the method isn't part of its interface
      // I guess we'd better only print the help when the user requests it
      return 1;
    }

    for(int i = 0; i < inputFiles.length; i++) {
      environment.updateEnvironment(inputFiles[i]);
      //System.out.println(inputFiles[i]);
      ATerm term = (SingletonFactory.getInstance()).makeAFun(inputFiles[i],0,false);
      
      // runs the modules
      it = instances.iterator();
      while(it.hasNext()) {
        TomPlugin plugin = (TomPlugin)it.next();
        plugin.setInput(term);
        plugin.run();
        term = plugin.getOutput();
        
        if( environment.hasError() ) {
          environment.printAlertMessage(plugin.getClass().toString());
          environment.messageError(TomMessage.getString("ProcessingError"), new Object[]{inputFiles[i]},
                                   "TomServer", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
          break;
        }
      }
    }
    if( environment.hasError() ) {
      environment.printAlertMessage("TomServer");
      return 1;
    } else {
	    return 0;
    }
  }

    
  /**
   * Extracts the plugins' class paths from the XML configuration file.
   * 
   * @param node the node containing the XML document
   * @param v the List into which class paths will be put
   */
  private void extractClassPaths(TNode node, List v)
  {
    %match(TNode node)
    {
      <server>plug@<plugins></plugins></server> -> {
		    %match(TNode plug)
         {
           ElementNode[childList = cl] -> // gets the <plugin> nodes
           { 
             while(!(cl.isEmpty())) // for each node...
               {
                 TNode pluginNode = cl.getHead();
                 %match(TNode pluginNode)
                 {
                   <plugin [classpath = cp] /> -> { v.add(cp);/*System.out.println(cp);*/ }
                 }
                 cl = cl.getTail();
               }
           }
         }
      }
    }
  }

  /**
   * Returns the value of an option. Returns an Object which is a Boolean, a String or an Integer
   * depending on what the option type is.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return an Object containing the option's value
   */
  public Object getOptionValue(String optionName) {
    return optionManager.getOptionValue(optionName);
  }

  /**
   * Returns the value of a boolean option.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return a boolean that is the option's value
   */
  public boolean getOptionBooleanValue(String optionName) {
    return optionManager.getOptionBooleanValue(optionName);
  }
    
  /**
   * Returns the value of an integer option.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return an int that is the option's value
   */
  public int getOptionIntegerValue(String optionName) {
    return optionManager.getOptionIntegerValue(optionName);
  }
    
  /**
   * Returns the value of a string option.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return a String that is the option's value
   */
  public String getOptionStringValue(String optionName) {
    return optionManager.getOptionStringValue(optionName);
  }

  public void putOptionValue(Object key, Object value) {
    optionManager.putOptionValue(key, value);
  }

  /**
   * Entry point of the class
   * 
   * @param args the command line
   */
  public static void main(String args[])
  {
    exec(args);
  }

}
