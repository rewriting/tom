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
import jtom.adt.tomsignature.types.*;
import jtom.adt.options.*;
import jtom.adt.options.types.*;

import jtom.exception.*;

import jtom.runtime.*;
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

	for(int i = 0; i < argumentList.length; i++)
	    {
		if(argumentList[i].equals("-X")) // tests if argumentList redefines the configuration file
		    {
			if (i+1 >= argumentList.length) // argument expected but no more input
			    {
				System.out.println("Option -X requires a string attribute.");
			    }   
			else
			    {
				String fileName = argumentList[i+1];
				String suffix = fileName.substring(fileName.length() - 4);
				//System.out.println(suffix);
				if( suffix.equalsIgnoreCase(".xml") ) // is the suffix valid ?
				    xmlConfigurationFile = fileName;
				else throw new TomRuntimeException("The alternate configuration file must be a XML file.");
			    }
		    }
	    }

	if(! (new File(xmlConfigurationFile)).exists() )
	    throw new TomRuntimeException("The configuration file " +xmlConfigurationFile+ " was not found.");

	return xmlConfigurationFile;
    }

    private Vector parseConfigFile(String xmlConfigurationFile)
    {
	// parses configuration file...
	XmlTools xtools = new XmlTools();
	TNode node = (TNode)xtools.convertXMLToATerm(xmlConfigurationFile);
	Vector classPaths = new Vector();

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
			System.out.println(plugin.getClass().getName() + " can't run : prerequisites not met.");
			System.exit(1);
		    }
	    }

	return inputFiles;
    }

    public int run(String[] argumentList)
    {
	//environment.init();

	String xmlConfigurationFile = whichConfigFile(argumentList);

	Vector classPaths = parseConfigFile(xmlConfigurationFile);

	// creates an instance of each plugin
	instances.add(this); // the server is added to allow option declaration and mapping
	Iterator it = classPaths.iterator();
	while(it.hasNext())
	    {
		Object instance;
		try
		    { 
			instance = Class.forName((String)it.next()).newInstance();
			if(instance instanceof TomPlugin)
			    instances.add(instance);
		    
		    }
		catch(Exception e){ System.out.println(e.getMessage()); }
	    }

	String[] inputFiles = optionManagement(argumentList);

	if(environment.hasError()) // copied from Tom.java
	    {
		environment.printAlertMessage("Tom");
		displayHelp();
		return 1;
	    }

	for(int i = 0; i < inputFiles.length; i++)
	    {
		try
		    {
			environment.updateEnvironment(inputFiles[i]);
			
			ATerm term = `FileName(inputFiles[i]);
			
			// runs the modules
			it = instances.iterator();
			it.next(); // skips the server
			while(it.hasNext())
			    {
				TomPlugin plugin = (TomPlugin)it.next();
				plugin.setInput(term);
				plugin.run();
				term = plugin.getOutput();
			    }
			
			if(environment.hasError()) // copied from Tom.java
			    return 1;

		    }
		catch(Exception e){ System.out.println(e.getMessage()); }
	    }

	if(environment.hasError()) // copied from Tom.java
	    return 1;
	else
	    return 0;
    }

    private void extractClassPaths(TNode node, Vector v)
    {
	%match(TNode node)
	    {
		<TOM>plug@<PLUGINS></PLUGINS></TOM> -> {
		    %match(TNode plug)
			{
			    ElementNode[childList = cl] -> // gets the <plugin> nodes
				{ 
				    while(!(cl.isEmpty())) // for each node...
					{
					    TNode h1 = cl.getHead();
					    %match(TNode h1)
						{
						    ElementNode[attrList = al] -> // gets the attribute list 
							{
							    while(!(al.isEmpty())) // for each attribute...
								{
								    TNode h2 = al.getHead();
								    %match(TNode h2)
									{
									    AttributeNode[name=n, value=val] -> 
										{ 
										    if(n.equals("classpath"))
											{
											    //System.out.println(val);
											    v.add(val);
											    // adds the value of the "classpath"
											    // attribute to the Vector of class paths
											}
										}
									}
								    al = al.getTail();
								}
							}	
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
	StringBuffer imports = new StringBuffer(); // temporary
	boolean importEncountered = false;
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
			else // s does start with '-'
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
					imports.append(argumentList[++i] + ":"); // temporary
					// 					if(importEncountered)
					// 					    {
					// 						environment.messageError(TomMessage.getString("ImportTwice"),
					// 									   "Tom", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
					// 					    }
					// 					else importEncountered = true;
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
						if(t.startsWith("-")) // argument expected but option found instead
						    {
							System.out.println("Option " +s+ " requires an integer attribute");
							// may change later :
							// _ some arguments could start with '-' (i.e. negative integers)
							// _ if '-' isn't considered a valid start, then this will still
							//   be considered an error, but will be handled differently
							//   ( with environment.messageError(...) )
						    }
						else
						    plugin.setOption(s, t);
					    }
					else if (type.equals("string")) 
					    {
						if ( !( s.equals("import") || s.equals("I") ) ) // temporary
						    {
							String t = argumentList[++i];
							if(t.startsWith("-")) // argument expected but option found instead
							    {
								System.out.println("Option " +s+ " requires a string attribute");
							    }
							else
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

	setOption("import",imports.toString()); // temporary

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
		<TOM>opt@<OPTIONS></OPTIONS></TOM> -> {
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
	String name = "";
	String altName = "";
	String description = "";
	String valueB = "";

	%match(TNode optionBooleanNode)
	    {
		ElementNode[attrList = al] -> // gets the attribute list 
		    {
			while(!(al.isEmpty())) // for each attribute...
			    {
				TNode h = al.getHead();
				%match(TNode h)
				    {
					AttributeNode[name="name", value=val] -> { name = val; } 
					AttributeNode[name="altName", value=val] -> { altName = val; } 
					AttributeNode[name="description", value=val] -> { description = val; } 
					AttributeNode[name="valueB", value=val] -> { valueB = val; } 	
				    }
				al = al.getTail();
			    }
		    }
	    }
	
	%match(String valueB)
	    {
		('true') ->
		    { globalOptions = `concTomOption(globalOptions*, OptionBoolean(name, altName, description, True())); }
		('false') ->
		    { globalOptions = `concTomOption(globalOptions*, OptionBoolean(name, altName, description, False())); }
	    }
	//System.out.println(name + "|" + altName + "|" + description + "|" + valueB);
    }

    private void extractOptionInteger(TNode optionIntegerNode)
    {
	String name = "";
	String altName = "";
	String description = "";
	String valueI = "";
	String attrName = "";

	%match(TNode optionIntegerNode)
	    {
		ElementNode[attrList = al] -> // gets the attribute list 
		    {
			while(!(al.isEmpty())) // for each attribute...
			    {
				TNode h = al.getHead();
				%match(TNode h)
				    {
					AttributeNode[name="name", value=val] -> { name = val; } 
					AttributeNode[name="altName", value=val] -> { altName = val; } 
					AttributeNode[name="description", value=val] -> { description = val; } 
					AttributeNode[name="valueI", value=val] -> { valueI = val; } 	
					AttributeNode[name="attrName", value=val] -> { attrName = val; } 
				    }
				al = al.getTail();
			    }
		    }
	    }

	globalOptions = `concTomOption(OptionInteger(name, altName, description, Integer.parseInt(valueI), attrName), globalOptions*);
	//System.out.println(name + "|" + altName + "|" + description + "|" + valueI + "|" + attrName);
    }

    private void extractOptionString(TNode optionStringNode)
    {
	String name = "";
	String altName = "";
	String description = "";
	String valueS = "";
	String attrName = "";

	%match(TNode optionStringNode)
	    {
		ElementNode[attrList = al] -> // gets the attribute list 
		    {
			while(!(al.isEmpty())) // for each attribute...
			    {
				TNode h = al.getHead();
				%match(TNode h)
				    {
					AttributeNode[name="name", value=val] -> { name = val; } 
					AttributeNode[name="altName", value=val] -> { altName = val; } 
					AttributeNode[name="description", value=val] -> { description = val; } 
					AttributeNode[name="valueS", value=val] -> { valueS = val; } 	
					AttributeNode[name="attrName", value=val] -> { attrName = val; } 
				    }
				al = al.getTail();
			    }
		    }
	    }

	globalOptions = `concTomOption(OptionString(name, altName, description, valueS, attrName), globalOptions*);
	//System.out.println(name + "|" + altName + "|" + description + "|" + valueS + "|" + attrName);
    }

}
