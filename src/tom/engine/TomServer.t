package jtom;

import java.util.*;
import java.io.*;

import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import jtom.adt.tomsignature.*;
import jtom.adt.options.*;
import jtom.adt.options.types.*;

import jtom.exception.*;

import jtom.runtime.xml.*;

import jtom.tools.*;

import aterm.*;
import aterm.pure.*;

/**
 * The TomServer manages plugins. It parses Tom.xml in order to find which
 * plugins are used and how they are ordered. Then it instantiates them.
 *
 * @author Gr&eacute;gory ANDRIEN
 */
public class TomServer implements TomPluginOptions {
  %include{ adt/TomSignature.tom }
  %include{ adt/TNode.tom }
  %include{ adt/Options.tom }

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
   * A List of TomOptionList objects indicating what services can be provided by each plugin.
   * The mapping between plugins and services is done so : a plugin in the instances List shares the
   * same index as the services it provides in the services List.
   */
  private List services;

  /**
   * A Map allowing to match option names and plugins
   */
  private Map optionOwners;

  /**
   * A Map allowing to match option names and their types
   */
  private Map optionTypes;

  /**
   * A Map allowing to match option names and their values
   */
  private Map optionValues;
	
  /**
   * 
   */
  private ASTFactory astFactory;
    
  /**
   * 
   */
  private TNodeFactory tNodeFactory;
	
  /**
   * 
   */
  private TomSignatureFactory tomSignatureFactory;
	
  /**
   * 
   */
  private OptionsFactory optionsFactory;
	
  /**
   * 
   */
  private TomEnvironment environment;

  /**
   * An accessor method.
   * 
   * @return an ASTFactory
   */
  public ASTFactory getASTFactory() { return astFactory; }
	
  /**
   * An accessor method.
   * 
   * @return a TNodeFactory
   */  
  public TNodeFactory getTNodeFactory() { return tNodeFactory; }
	
  /**
   * An accessor method.
   * 
   * @return a TomSignatureFactory
   */
  public TomSignatureFactory getTomSignatureFactory() { return tomSignatureFactory; }
	
  /**
   * An accessor method.
   * 
   * @return an OptionsFactory
   */
  public OptionsFactory getOptionsFactory() { return optionsFactory; }
	
  /**
   * An accessor method.
   * 
   * @return a TomEnvironment
   */
  public TomEnvironment getEnvironment() { return environment; }

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
        
	instance.instances = new Vector();
        instance.services = new Vector();
	instance.optionOwners = new HashMap();
	instance.optionTypes = new HashMap();
	instance.optionValues = new HashMap();

        instance.tNodeFactory = new TNodeFactory(new PureFactory());
        instance.optionsFactory = new OptionsFactory(new PureFactory());
        instance.tomSignatureFactory = new TomSignatureFactory(new PureFactory());
        instance.astFactory = new ASTFactory(instance.tomSignatureFactory);
		
        SymbolTable symbolTable = new SymbolTable(instance.astFactory);
        instance.environment = new TomEnvironment(symbolTable);

        return instance;
	    } else {
        TomServer.clear();
        return instance;
        //throw new TomRuntimeException(TomMessage.getString("TwoTomServerInstance"));
	    }
  }

  /**
   * Reinitializes the TomServer instance.
   */
  public static void clear() {
    instance.instances = new Vector();
    instance.services = new Vector();
    instance.optionOwners = new HashMap();
    instance.optionTypes = new HashMap();
    instance.optionValues = new HashMap();
    SymbolTable symbolTable = new SymbolTable(instance.astFactory);
    instance.environment = new TomEnvironment(symbolTable);
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
      displayHelp();
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
      environment.messageError("A configuration file must be specified with the -X option", 
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
    List classPaths = new Vector();
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
    globalOptions = `emptyTomOptionList();
    extractOptionList(node.getDocElem());

    // ... to extract plugin classpaths
    extractClassPaths(node.getDocElem(), classPaths);
    return classPaths;
  }

  /**
   * This method does the following :
   * <ul>
   * <li>a first call to declaredOptions() on the TomServer and each plugin, in order to determine
   * which options exist and their default values ;</li>
   * <li>a call to processArguments() in order to read the command line and set the options to
   * their actual values ;</li>
   * <li>a second call to declaredOptions() in order to collect the options' real value ;</li>
   * <li>it then tells the environment to set some values right ;</li>
   * <li>eventually, prerequisites are checked.</li>
   * </ul>
   * 
   * @param argumentList the command line
   * @return an array of String containing the names of the files to compile
   */
  private String[] optionManagement(String[] argumentList) {
    // collects the options/services provided by each plugin
    Iterator it = instances.iterator();
    while(it.hasNext()) {
      TomPluginOptions plugin = (TomPluginOptions)it.next();

      TomOptionList list = plugin.declaredOptions();
      services.add(list);

      while(!(list.isEmpty())) {
	  TomOption option = list.getHead();
	  
	  %match(TomOption option) {
	      OptionBoolean[name=n, altName=an] 
		  -> {
		  optionOwners.put(n, plugin);
		  optionTypes.put(n,"boolean");
		  if( an.length() > 0 ) {
		      optionOwners.put(an, plugin);
		      optionTypes.put(an,"boolean");
		  }
	      }

	      OptionInteger[name=n, altName=an] 
		  -> {
		  optionOwners.put(n, plugin);
		  optionTypes.put(n,"integer");
		  if( an.length() > 0 ) {
		      optionOwners.put(an, plugin);
		      optionTypes.put(an,"integer");
		  }
	      }

	      OptionString[name=n, altName=an] 
		  -> {
		  optionOwners.put(n, plugin);
		  optionTypes.put(n,"string");
		  if( an.length() > 0 ) {
		      optionOwners.put(an, plugin);
		      optionTypes.put(an,"string");
		  }
	      }
	  }
		  
	  list = list.getTail();
      }
    }

    // set options accordingly to the arguments given in input
    String[] inputFiles = processArguments(argumentList);

    // regenerates the options/services now that the proper values have been set
    services.clear();
    it = instances.iterator();
    while(it.hasNext()) {
      TomPluginOptions plugin = (TomPluginOptions)it.next();
      
      TomOptionList list = plugin.declaredOptions();
      services.add(list);

      while(!(list.isEmpty())) {
	  TomOption option = list.getHead();
	  
	  %match(TomOption option) {
	      OptionBoolean[name=n, altName=an, valueB=v] 
		  -> {
		  optionValues.put(n, v);
		  if( an.length() > 0 )
		      optionValues.put(an, v);
	      }

	      OptionInteger[name=n, altName=an, valueI=v] 
		  -> {
		  optionValues.put(n, new Integer(v));
		  if( an.length() > 0 )
		      optionValues.put(an, new Integer(v));
	      }

	      OptionString[name=n, altName=an, valueS=v] 
		  -> {
		  optionValues.put(n, v);
		  if( an.length() > 0 )
		      optionValues.put(an, v);
	      }
	  }

	  list = list.getTail();
      }
    }

    environment.initInputFromArgs(); // is here because options need to be set to the right value before

    // checks if every plugin's needs are fulfilled
    it = instances.iterator();
    while(it.hasNext()) {
      TomPluginOptions plugin = (TomPluginOptions)it.next();
      boolean canGoOn = arePrerequisitesMet(plugin.requiredOptions());
      if (!canGoOn) {
        environment.messageError(TomMessage.getString("PrerequisitesIssue"), 
                                 new Object[]{plugin.getClass().getName()},
                                 "TomServer",
                                 TomMessage.DEFAULT_ERROR_LINE_NUMBER);
      }
    }
    
    return inputFiles;
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
    instances.add(this); // the server is added to allow option declaration and mapping
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
    
    // 	if( environment.hasError() )
    // 	    {
    // 		environment.printErrorMessage();
    // 		return 1;
    // 	    }

    String[] inputFiles = optionManagement(argumentList);
  
    if( environment.hasError() ) {
      environment.printAlertMessage("TomServer");
      displayHelp();
      return 1;
    }

    for(int i = 0; i < inputFiles.length; i++) {
      environment.updateEnvironment(inputFiles[i]);
      //System.out.println(inputFiles[i]);
      ATerm term = `FileName(inputFiles[i]);
      
      // runs the modules
      it = instances.iterator();
      it.next(); // skips the server
      while(it.hasNext()) {
        TomPlugin plugin = (TomPlugin)it.next();
        plugin.setInput(term);
        plugin.run();
        term = plugin.getOutput();
        
        if( environment.hasError() ) {
          environment.printAlertMessage(plugin.getClass().toString());
          environment.messageError("Error while processing file " + inputFiles[i],
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
      <tom>plug@<plugins></plugins></tom> -> {
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
   * A mapping method which return both the type of the option whose name is given
   * and the instance of the plugin who declared it.
   * 
   * @param optionName the name of the option we're looking information about
   * @return a List containing a String indicating the type of the option ("boolean",
   * "integer" or "string"), along with a reference to the plugin declaring the option. 
   */
  public List aboutThisOption(String optionName)
  {
    List pair = new Vector();

    for(int i = 0; i < services.size(); i++)
	    {
        TomOptionList ol = (TomOptionList)services.get(i);
        TomOptionList list = `concTomOption(ol*);

        while(!(list.isEmpty()))
          {
            TomOption h = list.getHead();
            %match(TomOption h)
            {
              OptionBoolean[name=n, altName=a] ->
              {
                if(n.equals(optionName)||a.equals(optionName))
                  {
                    TomPluginOptions plugin = (TomPluginOptions)instances.get(i);
                    //System.out.println("Option named " +optionName+
                    //		   " found in " +plugin.getClass().getName());
                    pair.add("boolean");
                    pair.add(plugin);
                    return pair;
                  }
              }
              OptionInteger[name=n, altName=a] ->
              {
                if(n.equals(optionName)||a.equals(optionName))
                  {
                    TomPluginOptions plugin = (TomPluginOptions)instances.get(i);
                    //System.out.println("Option named " +optionName+
                    //		   " found in " +plugin.getClass().getName());
                    pair.add("integer");
                    pair.add(plugin);
                    return pair;
                  }
              }
              OptionString[name=n, altName=a] ->
              {
                if(n.equals(optionName)||a.equals(optionName))
                  {
                    TomPluginOptions plugin = (TomPluginOptions)instances.get(i);
                    //System.out.println("Option named " +optionName+
                    //		   " found in " +plugin.getClass().getName());
                    pair.add("string");
                    pair.add(plugin);
                    return pair;
                  }
              }
            }			
            list = list.getTail();
          }
	    }
    //System.out.println("Option named " +optionName+ " not found");
    return null;
  }

  /**
   *
   *
   * @param optionName
   * @return
   */
  public TomPluginOptions getOptionsOwner(String optionName) {
    return (TomPluginOptions)optionOwners.get(optionName);
  }

  /**
   *
   *
   * @param optionName
   * @return
   */
  public String getOptionsType(String optionName) {
    return (String)optionTypes.get(optionName);
  }

  /**
   * Returns the value of an option. Returns an Object which is a Boolean, a String or an Integer
   * depending on what the option type is.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return an Object containing the option's value
   */
  public Object getOptionValue(String optionName)
  {
    for(int i = 0; i < services.size(); i++)
	    {
        TomOptionList ol = (TomOptionList)services.get(i);
        TomOptionList list = `concTomOption(ol*);

        while(!(list.isEmpty()))
          {
            TomOption h = list.getHead();
            %match(TomOption h)
            {
              OptionBoolean[name=n, altName=a, valueB=v] ->
              {
                if(n.equals(optionName)||a.equals(optionName))
                  {
                    %match(TomBoolean v)
                    {
                      True() -> { return new Boolean(true); }
                      False() -> { return new Boolean(false); }
                    }
                  }
              }
              OptionInteger[name=n, altName=a, valueI=v] ->
              {
                if(n.equals(optionName)||a.equals(optionName))
                  {
                    return new Integer(v);
                  }
              }
              OptionString[name=n, altName=a, valueS=v] ->
              {
                if(n.equals(optionName)||a.equals(optionName))
                  {
                    return v;
                  }
              }
            }			
            list = list.getTail();
          }
	    }
    environment.messageError(TomMessage.getString("OptionNotFound"), new Object[]{optionName}, 
                             "TomServer", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
    return null;
  }

  /**
   * Returns the value of a boolean option.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return a boolean that is the option's value
   */
  public boolean getOptionBooleanValue(String optionName)
  {
    for(int i = 0; i < services.size(); i++)
	    {
        TomOptionList ol = (TomOptionList)services.get(i);
        TomOptionList list = `concTomOption(ol*);

        while(!(list.isEmpty()))
          {
            TomOption h = list.getHead();
            %match(TomOption h)
            {
              OptionBoolean[name=n, altName=a, valueB=v] ->
              {
                if(n.equals(optionName)||a.equals(optionName))
                  {
                    %match(TomBoolean v)
                    {
                      True() -> { return true; }
                      False() -> { return false; }
                    }
                  }
              }
            }			
            list = list.getTail();
          }
	    }
    environment.messageError(TomMessage.getString("OptionNotFound"), new Object[]{optionName}, 
                             "TomServer", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
    return false;
  }
    
  /**
   * Returns the value of an integer option.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return an int that is the option's value
   */
  public int getOptionIntegerValue(String optionName)
  {
    for(int i = 0; i < services.size(); i++)
	    {
        TomOptionList ol = (TomOptionList)services.get(i);
        TomOptionList list = `concTomOption(ol*);

        while(!(list.isEmpty()))
          {
            TomOption h = list.getHead();
            %match(TomOption h)
            {
              OptionInteger[name=n, altName=a, valueI=v] ->
              {
                if(n.equals(optionName)||a.equals(optionName))
                  {
                    return v;
                  }
              }
            }			
            list = list.getTail();
          }
	    }
    environment.messageError(TomMessage.getString("OptionNotFound"), new Object[]{optionName}, 
                             "TomServer", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
    return 0;
  }
    
  /**
   * Returns the value of a string option.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return a String that is the option's value
   */
  public String getOptionStringValue(String optionName)
  {
    for(int i = 0; i < services.size(); i++)
	    {
        TomOptionList ol = (TomOptionList)services.get(i);
        TomOptionList list = `concTomOption(ol*);

        while(!(list.isEmpty()))
          {
            TomOption h = list.getHead();
            %match(TomOption h)
            {
              OptionString[name=n, altName=a, valueS=v] ->
              {
                if(n.equals(optionName)||a.equals(optionName))
                  {
                    return v;
                  }
              }
            }			
            list = list.getTail();
          }
	    }
    environment.messageError(TomMessage.getString("OptionNotFound"), new Object[]{optionName}, 
                             "TomServer", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
    return null;
  }

  /**
   * Self-explanatory. Displays the current version of the TOM compiler.
   */
  public void displayVersion()
  {
    System.out.println("\njtom " + VERSION + "\n\n"
                       + "Copyright (C) 2000-2004 INRIA, Nancy, France.\n");
    System.exit(0);
  }

  /**
   * Self-explanatory. Displays an help message indicating how to use the compiler.
   */
  public void displayHelp()
  {
    String beginning = "\nusage :"
	    + "\n\ttom [options] input[.t] [... input[.t]]"
	    + "\noptions :";
    StringBuffer buffy = new StringBuffer(beginning);

    buffy.append("\n\t-X <file> \t \t : \tDefines an alternate XML configuration file");

    for(int i = 0; i < services.size(); i++)
	    {
        TomOptionList ol = (TomOptionList)services.get(i);
        TomOptionList list = `concTomOption(ol*);

        while(!(list.isEmpty()))
          {
            TomOption h = list.getHead();
            %match(TomOption h)
            {
              OptionBoolean(n, a, d, False()) -> // display only flags that are not activated by default
              {
                String s;
                if(a.length() > 0)
                  s = "\n\t--"+n+" \t| -"+a+" : \t"+d;
                else
                  s = "\n\t--"+n+" \t \t : \t"+d;
                buffy.append(s);
              }
              OptionInteger[name=n, altName=a, description=d, attrName=at] ->
              {
                String s;
                if(a.length() > 0)
                  s = "\n\t--"+n+" <"+at+"> \t| -"+a+" : \t"+d;
                else
                  s = "\n\t--"+n+" <"+at+"> \t \t : \t"+d;
                buffy.append(s);
              }
              OptionString[name=n, altName=a, description=d, attrName=at] ->
              {
                String s;
                if(a.length() > 0)
                  s = "\n\t--"+n+" <"+at+"> \t| -"+a+" : \t"+d;
                else
                  s = "\n\t--"+n+" <"+at+"> \t \t : \t"+d;
                buffy.append(s);
              }
            }			
            list = list.getTail();
          }
	    }
	
    System.out.println(buffy.toString());
    System.exit(0);
  }

  /**
   * Checks if all the options a plugin needs are here.
   * 
   * @param list a list of options that must be found with the right value
   * @return true if every option was found with the right value, false otherwise
   */
  public boolean arePrerequisitesMet(TomOptionList list)
  {
    while(!(list.isEmpty()))
	    {
        TomOption h = list.getHead();
        boolean optionFound = false;

        for(int i = 0; i < services.size(); i++)
          {
            TomOptionList ol = (TomOptionList)services.get(i);
			
            %match(TomOptionList ol, TomOption h)
            {
              concTomOption(_*,OptionBoolean(name,_,_,val),_*) , OptionBoolean(name,_,_,val) ->
              { optionFound = true; }
              concTomOption(_*,OptionInteger(name,_,_,val,_),_*) , OptionInteger(name,_,_,val,_) -> 
              { optionFound = true; }
              concTomOption(_*,OptionString(name,_,_,val,_),_*) , OptionString(name,_,_,val,_) -> 
              { optionFound = true; }
            }
          }
        if(!optionFound)
          return false;
        list = list.getTail();
	    }
    return true;
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

  /**
   * The global options.
   */    
  private TomOptionList globalOptions;

  /**
   * 
   * @return the global options
   */
  public TomOptionList declaredOptions()
  {
    return globalOptions;
  }

  /**
   * 
   * @return the prerequisites
   */
  public TomOptionList requiredOptions()
  {

    TomOptionList list = `concTomOption(globalOptions*);
    TomOptionList prerequisites = `emptyTomOptionList();

    while(!(list.isEmpty()))
	    {
        TomOption h = list.getHead();
        %match(TomOption h)
		    {
          OptionBoolean[name="debug", valueB=True()] -> 
			    { 
            prerequisites = `concTomOption(OptionBoolean("jCode", "", "", True()), prerequisites*);
            // For the moment debug is only available for Java as target language
			    }
          OptionString[name="destdir", valueS=v] -> 
			    { 
            if(!v.equals(".")) // destdir is not set at its default value -> it has been changed
              prerequisites = `concTomOption(OptionString("output", "", "", "", ""), prerequisites*);
            // destdir and output incompatible -> we want output at its default value
			    }
          OptionString[name="output", valueS=v] -> 
			    { 
            if(!v.equals("")) // output is not set at its default value -> it has been changed
              prerequisites = `concTomOption(OptionString("destdir", "", "", ".", ""), prerequisites*);
            // destdir and output incompatible -> we want destdir at its default value
			    }
		    }

        list = list.getTail();
	    }
    return prerequisites;
  }

  /**
   * Sets an option to the desired value.
   * 
   * @param optionName the option's name
   * @param optionValue the option's desired value
   */
  public void setOption(String optionName, String optionValue)
  {
    %match(TomOptionList globalOptions)
    {
      concTomOption(av*, OptionBoolean(n, alt, desc, val), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
          {
            %match(String optionValue)
            {
              ('true') ->
              { globalOptions = `concTomOption(av*, ap*, OptionBoolean(n, alt, desc, True())); }
              ('false') ->
              { globalOptions = `concTomOption(av*, ap*, OptionBoolean(n, alt, desc, False())); }
            }
          }
      }
      concTomOption(av*, OptionInteger(n, alt, desc, val, attr), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
          globalOptions = `concTomOption(av*, ap*, OptionInteger(n, alt, desc, Integer.parseInt(optionValue), attr));
      }
      concTomOption(av*, OptionString(n, alt, desc, val, attr), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
          globalOptions = `concTomOption(av*, ap*, OptionString(n, alt, desc, optionValue, attr));
      }
    }
  }

  /**
   * This method takes the arguments given by the user and deduces the options to set, then sets them.
   * 
   * @param argumentList
   * @return an array containing the name of the input files
   */
  public String[] processArguments(String[] argumentList)
  {
    List inputFiles = new Vector();
    StringBuffer imports = new StringBuffer();
    boolean outputEncountered = false;
    boolean destdirEncountered = false;
    int i = 0;

    try
	    {
        for(; i < argumentList.length; i++)
          {
            String s = argumentList[i];
			
            if(!s.startsWith("-")) // input file name, should never start with '-'
              inputFiles.add(s);
            else // s does start with '-', thus is -or at least should be- an option
              {
                s = s.substring(1); // crops the '-'
                if(s.startsWith("-")) // if there's another one
                  s = s.substring(1); // crops the second '-'
				
                if( s.equals("help") || s.equals("h") )
                  displayHelp();
                if( s.equals("version") || s.equals("V") )
                  displayVersion();
                if( s.equals("X") )
                  {
                    // if we're here, the TomServer has already handled the "-X" option
                    // and all errors that might occur
                    // just skip it,along with its argument
                    i++;
                    continue;
                  }
                if( s.equals("import") || s.equals("I") )
                  {
                    imports.append(argumentList[++i] + ":");
                  }
                if( s.equals("output") || s.equals("o") )
                  {
                    if(outputEncountered)
                      {
                        environment.messageError(TomMessage.getString("OutputTwice"),
                                                 "TomServer", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
                      }
                    else outputEncountered = true;
                  }
                if( s.equals("destdir") || s.equals("d") )
                  {
                    if(destdirEncountered)
                      {
                        environment.messageError(TomMessage.getString("DestdirTwice"),
                                                 "TomServer", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
                      }
                    else destdirEncountered = true;
                  }

		String type = getOptionsType(s);
		TomPluginOptions plugin = getOptionsOwner(s);
                //List about = aboutThisOption(s);

                if(type == null || plugin == null) // option not found
                  {
                    environment.messageError(TomMessage.getString("InvalidOption"), 
                                             new Object[]{argumentList[i]},
                                             "TomServer", 
                                             TomMessage.DEFAULT_ERROR_LINE_NUMBER);
                    return (String[])inputFiles.toArray(new String[]{});
                  }
                else
                  {                    				
                    if (type.equals("boolean"))
                      {
                        plugin.setOption(s, "true");
                      }
                    else if (type.equals("integer"))
                      {
                        String t = argumentList[++i];
                        plugin.setOption(s, t);
                      }
                    else if (type.equals("string")) 
                      {
                        if ( !( s.equals("import") || s.equals("I") ) ) // "import" is handled in the end
                          {
                            String t = argumentList[++i];
                            plugin.setOption(s, t);
                          }
                      }
                  }	
              }
          }
	    }
    catch (ArrayIndexOutOfBoundsException e) 
	    {
        environment.messageError(TomMessage.getString("IncompleteOption"), 
                                 new Object[]{argumentList[--i]}, 
                                 "TomServer", 
                                 TomMessage.DEFAULT_ERROR_LINE_NUMBER);
        return (String[])inputFiles.toArray(new String[]{});
	    }

    setOption("import",imports.toString());

    if(inputFiles.isEmpty())
	    {
        environment.messageError(TomMessage.getString("NoFileToCompile"), 
                                 "TomServer", 
                                 TomMessage.DEFAULT_ERROR_LINE_NUMBER);
	    }

    return (String[])inputFiles.toArray(new String[]{});	
  }

  /**
   * Extracts the global options from the XML configuration file.
   * 
   * @param node the node containing the XML file
   */
  public void extractOptionList(TNode node)
  {
    %match(TNode node)
    {
      <tom>opt@<options></options></tom> -> {
		    %match(TNode opt)
         {
           ElementNode[childList = c]
             -> { while(!(c.isEmpty()))
               {
                 TNode h = c.getHead();
					
                 %match(TNode h){
                   ob@ElementNode[name="OptionBoolean"] -> { extractOptionBoolean(ob); }
					    
                   oi@ElementNode[name="OptionInteger"] -> { extractOptionInteger(oi); }
					    
                   os@ElementNode[name="OptionString"] -> { extractOptionString(os); }
                 }
					
                 c = c.getTail();
               }
           }
         }
      }
    }
  }	

  /**
   * Adds a boolean option to the global options.
   * 
   * @param optionBooleanNode the node containing the option
   */
  private void extractOptionBoolean(TNode optionBooleanNode)
  {
    %match(TNode optionBooleanNode)
    {
      <OptionBoolean
        [name = n,
         altName = an,
         description = d,
         valueB = v] /> ->
      {
        %match(String v)
        {
          ('true') ->
          { globalOptions = `concTomOption(globalOptions*, OptionBoolean(n, an, d, True())); }
          ('false') ->
          { globalOptions = `concTomOption(globalOptions*, OptionBoolean(n, an, d, False())); }
        }
      }
    }
  }

  /**
   * Adds an integer option to the global options.
   * 
   * @param optionIntegerNode the node containing the option
   */
  private void extractOptionInteger(TNode optionIntegerNode)
  {
    %match(TNode optionIntegerNode)
    {
      <OptionInteger
        [name = n,
         altName = an,
         description = d,
         valueI = v,
         attrName = at] /> ->
      {
        globalOptions = `concTomOption(OptionInteger(n, an, d, Integer.parseInt(v), at), globalOptions*);
      }
    }
  }

  /**
   * Adds a string option to the global options.
   * 
   * @param optionStringNode the node containing the option
   */
  private void extractOptionString(TNode optionStringNode)
  {
    %match(TNode optionStringNode)
    {
      <OptionString
        [name = n,
         altName = an,
         description = d,
         valueS = v,
         attrName = at] /> ->
      {
        globalOptions = `concTomOption(OptionString(n, an, d, v, at), globalOptions*);
      }
    }
  }

}
