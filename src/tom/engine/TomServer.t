/**
 *
 * The TomServer manages plugins. It parses Tom.xml in order to find which
 * plugins are used and how they are ordered. Then it instantiates them.
 *
 */

// test command line :
// java jtom.TomServer --import ~/jtom/src/dist/share/jtom ~/jtom/examples/tutorial/HelloWorld.t --verbose --optimize --intermediate

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

public class TomServer implements TomPluginOptions
{
    %include{ adt/TomSignature.tom }
    %include{ adt/TNode.tom }
    %include{ adt/Options.tom }

    public final static String VERSION = "3.0alpha";
    
    private Vector instances;
    private Vector services;

    private ASTFactory astFactory;
    private TNodeFactory tNodeFactory;
    private TomSignatureFactory tomSignatureFactory;
    private OptionsFactory optionsFactory;

    private TomEnvironment environment;

    public ASTFactory getASTFactory() { return astFactory; }  
    public TNodeFactory getTNodeFactory() { return tNodeFactory; }
    public TomSignatureFactory getTomSignatureFactory() { return tomSignatureFactory; }
    public OptionsFactory getOptionsFactory() { return optionsFactory; }

    public TomEnvironment getEnvironment() { return environment; }

    /**
     * Singleton pattern
     */
    private static TomServer instance = null;
    /**
     * Singleton pattern
     */
    protected TomServer(){} // exists to defeat instantiation
    /**
     * Singleton pattern
     */
    public static TomServer getInstance()
    {
	if(instance == null)
	    {
		throw new TomRuntimeException(TomMessage.getString("GetInitializedTomServerInstance"));
	    }
	return instance;
    }

    public static TomServer create()
    {
	if(instance == null)
	    {
		instance = new TomServer();
		instance.instances = new Vector();
		instance.services = new Vector();

		instance.tNodeFactory = new TNodeFactory(new PureFactory());
		instance.optionsFactory = new OptionsFactory(new PureFactory());
		instance.tomSignatureFactory = new TomSignatureFactory(new PureFactory());
		instance.astFactory = new ASTFactory(instance.tomSignatureFactory);
		
		SymbolTable symbolTable = new SymbolTable(instance.astFactory);
		instance.environment = new TomEnvironment(symbolTable);

		return instance;
	    }
	else
	    {
		instance.clear();
		return instance;
		//throw new TomRuntimeException(TomMessage.getString("TwoTomServerInstance"));
	    }
    }

    public static void clear() {
	instance.instances = new Vector();
	instance.services = new Vector();
	SymbolTable symbolTable = new SymbolTable(instance.astFactory);
	instance.environment = new TomEnvironment(symbolTable);
    }

    public static int exec(String args[])
    {
	TomServer server = TomServer.create();
	return server.run(args);
    }

    private String whichConfigFile(String[] argumentList)
    {
	String xmlConfigurationFile = "./jtom/Tom.xml"; // default configuration file
	int i = 0;

	try
	    {
		for(; i < argumentList.length; i++)
		    {
			if(argumentList[i].equals("-X")) // tests if argumentList redefines the configuration file
			    xmlConfigurationFile = argumentList[++i];
		    }
	    }
	catch (ArrayIndexOutOfBoundsException e) 
	    {
		environment.messageError(TomMessage.getString("IncompleteOption"), 
					 new Object[]{argumentList[--i]}, 
					 "TomServer", 
					 TomMessage.DEFAULT_ERROR_LINE_NUMBER);
	    }

 	if( environment.hasError() )
 	    {
		environment.printErrorMessage();
		displayHelp();
 		return null;
 	    }

	if(! (new File(xmlConfigurationFile)).exists() )
	    {
		environment.messageError(TomMessage.getString("ConfigFileNotFound"), 
					 new Object[]{xmlConfigurationFile}, 
					 "TomServer", 
					 TomMessage.DEFAULT_ERROR_LINE_NUMBER);
	    }

 	if( environment.hasError() )
 	    {
		environment.printErrorMessage();
 		return null;
 	    }

	return xmlConfigurationFile;
    }

    private Vector parseConfigFile(String xmlConfigurationFile)
    {
	// parses configuration file...
	XmlTools xtools = new XmlTools();
	Vector classPaths = new Vector();
	TNode node = (TNode)xtools.convertXMLToATerm(xmlConfigurationFile);

	if( node == null ) // parsing failed
	    {
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

    private String[] optionManagement(String[] argumentList)
    {
	// collects the options/services provided by each plugin
	Iterator it = instances.iterator();
	while(it.hasNext())
	    {
		TomPluginOptions plugin = (TomPluginOptions)it.next();
		services.add(plugin.declareOptions());
	    }

	// set options accordingly to the arguments given in input
	String[] inputFiles = processArguments(argumentList);

	// regenerates the options/services now that the proper values have been set
	services.clear();
	it = instances.iterator();
	while(it.hasNext())
	    {
		TomPluginOptions plugin = (TomPluginOptions)it.next();
		services.add(plugin.declareOptions());
	    }

	environment.initInputFromArgs(); // is here because options need to be set to the right value before

	// checks if every plugin's needs are fulfilled
	it = instances.iterator();
	while(it.hasNext())
	    {
		TomPluginOptions plugin = (TomPluginOptions)it.next();
		boolean canGoOn = arePrerequisitesMet(plugin.requiredOptions());
		if (!canGoOn)
		    {
			environment.messageError(TomMessage.getString("PrerequisitesIssue"), 
						 new Object[]{plugin.getClass().getName()},
						 "TomServer",
						 TomMessage.DEFAULT_ERROR_LINE_NUMBER);
		    }
	    }

	return inputFiles;
    }

    public int run(String[] argumentList)
    {
	String xmlConfigurationFile = whichConfigFile(argumentList);

 	if( xmlConfigurationFile == null ) {// method whichConfigFile encountered an error
			return 1;
 	    }
	Vector classPaths = parseConfigFile(xmlConfigurationFile);

	if( classPaths == null ) // method parseConfigFile encountered an error
	    {
		environment.printErrorMessage();
		return 1;
	    }

	// creates an instance of each plugin
	instances.add(this); // the server is added to allow option declaration and mapping
	Iterator it = classPaths.iterator();
	while(it.hasNext())
	    {
		Object instance;
		String path = (String)it.next();
		try
		    { 
			instance = Class.forName(path).newInstance();
			if(instance instanceof TomPlugin)
			    instances.add(instance);
			else
			    {
				System.out.println("pas un plugin");
				environment.messageError(TomMessage.getString("ClassNotAPlugin"), new Object[]{path},
							 "TomServer", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
			    }
		    }
		catch(ClassNotFoundException cnfe) 
		    { 
			environment.messageWarning(TomMessage.getString("ClassNotFound"),new Object[]{path},
						 "TomServer", TomMessage.DEFAULT_ERROR_LINE_NUMBER); 
		    }
		catch(Exception e) 
		    { 
			environment.messageError(TomMessage.getString("InstantiationError"),
						 "TomServer", TomMessage.DEFAULT_ERROR_LINE_NUMBER); 
		    }
	    }

	if( environment.hasError() )
	    {
		environment.printErrorMessage();
		return 1;
	    }

	String[] inputFiles = optionManagement(argumentList);

	if( environment.hasError() )
	    {
		environment.printAlertMessage("TomServer");
		displayHelp();
		return 1;
	    }

	for(int i = 0; i < inputFiles.length; i++)
	    {
		environment.updateEnvironment(inputFiles[i]);
			System.out.println(inputFiles[i]);
		ATerm term = `FileName(inputFiles[i]);
		
		// runs the modules
		it = instances.iterator();
		it.next(); // skips the server
		TomPlugin plugin;
		while(it.hasNext())
		    {
			plugin = (TomPlugin)it.next();
			plugin.setInput(term);
			plugin.run();
			term = plugin.getOutput();
			
			if( environment.hasError() )
					environment.printAlertMessage(plugin.getClass().toString());
			    return 1;
		    }
	    }

	if( environment.hasError() )
	    return 1;
	else
	    return 0;
    }

    private void extractClassPaths(TNode node, Vector v)
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
     * This is done at the same time, since when we find the option (if it exists),
     * we learn both its type and its location. So it's only logical to return them at the
     * same time, but maybe we're not using the most elegant of ways...
     */
    public Vector aboutThisOption(String optionName)
    {
	Vector pair = new Vector();

	for(int i = 0; i < services.size(); i++)
	    {
		TomOptionList ol = (TomOptionList)services.elementAt(i);
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
						TomPluginOptions plugin = (TomPluginOptions)instances.elementAt(i);
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
						TomPluginOptions plugin = (TomPluginOptions)instances.elementAt(i);
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
						TomPluginOptions plugin = (TomPluginOptions)instances.elementAt(i);
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
     * Returns the value of an option. Returns an Object which is a Boolean, a String or an Integer
     * depending on what the option type is.
     * @return An Object containing the option's value.
     */
    public Object getOptionValue(String optionName)
    {
	Vector pair = new Vector();

	for(int i = 0; i < services.size(); i++)
	    {
		TomOptionList ol = (TomOptionList)services.elementAt(i);
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
     * @return A boolean that is the option's value.
     */
    public boolean getOptionBooleanValue(String optionName)
    {
	Vector pair = new Vector();

	for(int i = 0; i < services.size(); i++)
	    {
		TomOptionList ol = (TomOptionList)services.elementAt(i);
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
     * @return An integer that is the option's value.
     */
    public int getOptionIntegerValue(String optionName)
    {
	Vector pair = new Vector();

	for(int i = 0; i < services.size(); i++)
	    {
		TomOptionList ol = (TomOptionList)services.elementAt(i);
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
     * @return A string that is the option's value, or null if the option wasn't found or isn't of string value.
     */
    public String getOptionStringValue(String optionName)
    {
	Vector pair = new Vector();

	for(int i = 0; i < services.size(); i++)
	    {
		TomOptionList ol = (TomOptionList)services.elementAt(i);
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
     * Self-explanatory.
     */
    public void displayVersion()
    {
	System.out.println("\njtom " + VERSION + "\n\n"
			   + "Copyright (C) 2000-2004 INRIA, Nancy, France.\n");
	System.exit(0);
    }

    /**
     * Self-explanatory.
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
		TomOptionList ol = (TomOptionList)services.elementAt(i);
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
     */
    public boolean arePrerequisitesMet(TomOptionList list)
    {
	while(!(list.isEmpty()))
	    {
		TomOption h = list.getHead();
		boolean optionFound = false;

		for(int i = 0; i < services.size(); i++)
		    {
			TomOptionList ol = (TomOptionList)services.elementAt(i);
			
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

    public static void main(String args[])
    {
	exec(args);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private TomOptionList globalOptions;

    public TomOptionList declareOptions()
    {
	return globalOptions;
    }

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
     * @return An array containing the name of the input files.
     */
    public String[] processArguments(String[] argumentList)
    {
	Vector inputFiles = new Vector();
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

				Vector about = aboutThisOption(s);

				if(about == null) // option not found
				    {
					environment.messageError(TomMessage.getString("InvalidOption"), 
								 new Object[]{argumentList[i]},
								 "TomServer", 
								 TomMessage.DEFAULT_ERROR_LINE_NUMBER);
					return (String[])inputFiles.toArray(new String[]{});
				    }
				else
				    {
					String type = (String)about.elementAt(0);
					TomPluginOptions plugin = (TomPluginOptions)about.elementAt(1);
					
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
