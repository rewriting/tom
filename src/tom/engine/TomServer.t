/*
 *
 * The TomServer manages plugins. It parses Tom.xml in order to find which
 * plugins are used and how they are ordered. Then it instantiates them.
 *
 */

package jtom;

import java.util.*;
import java.io.*;

import jtom.starter.*;

import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import jtom.adt.tomsignature.*;
import jtom.adt.tomsignature.types.*;

import jtom.runtime.*;
import jtom.runtime.xml.*;

import jtom.tools.*;

import aterm.*;
import aterm.pure.*;

public class TomServer
{
    %include{ adt/TomSignature.tom }
    %include{ adt/TNode.tom }

    private String[] argumentList;
    private String xmlConfigurationFile;
    
    private Vector instances;
    private Vector services;
    
    private jtom.adt.tnode.Factory tnf;
    private jtom.adt.tomsignature.Factory tsf; 
  
    public jtom.adt.tnode.Factory getTNodeFactory()
    {
	return tnf;
    }

    public jtom.adt.tomsignature.Factory getTomSignatureFactory()
    {
	return tsf;
    }

    /*
     * Singleton pattern
     */
    private static TomServer instance = null;

    protected TomServer(){} // exists to defeat instantiation

    public static TomServer getInstance() throws Exception
    {
	if(instance == null)
	    {
		throw new Exception("The TomServer is not initialized.");
	    }
	return instance;
    }

    public static TomServer create(String[] argumentList) throws Exception
    {
	if(instance == null)
	    {
		instance = new TomServer();
		instance.argumentList = argumentList;
		instance.instances = new Vector();
		instance.services = new Vector();

		instance.tnf = new jtom.adt.tnode.Factory(new PureFactory());
		instance.tsf = new jtom.adt.tomsignature.Factory(new PureFactory());
		
		ASTFactory astFactory = new ASTFactory(instance.tsf);
		SymbolTable symbolTable = new SymbolTable(astFactory);
		TomEnvironment.create(instance.tsf, astFactory, symbolTable);
		
		return instance;
	    }
	else
	    {
		throw new Exception("The TomServer already exists.");
	    }
    }

    public void run() throws Exception
    {
	boolean altConfigFile = false;
	String xmlFileName = null;
	String xmlSuffix = null;

	for(int i = 0; i < argumentList.length; i++)
	    {
		if(argumentList[i].equals("-X"))
		    {
			if (i+1 >= argumentList.length)// argument expected but no more input
			    {
				System.out.println("Option -X requires a string attribute");
			    }   
			else
			    {
				String arg = argumentList[i+1];
				StringTokenizer st = new StringTokenizer(arg, ".");
				if(st.hasMoreTokens()) { xmlFileName = st.nextToken(); }
				if(st.hasMoreTokens()) { xmlSuffix = st.nextToken(); altConfigFile = true; }
			    }
		    }
	    }

	if(altConfigFile == true) // tests if argumentList redefines the configuration file
	    {
		if(xmlSuffix.compareToIgnoreCase("xml") == 0)
		    xmlConfigurationFile = xmlFileName + "." + xmlSuffix;
		else throw new Exception("The alternative configuration file must be a XML file");
	    }
	else // configuration file not redefined, use default
	    {
		xmlConfigurationFile = "./jtom/Tom.xml";
	    }

	System.out.println("--------------------------------------------------");

	if(! (new File(xmlConfigurationFile)).exists() )
	    throw new Exception("The configuration file " +xmlConfigurationFile+ " was not found");

	// parses configuration file
	XmlTools xtools = new XmlTools();
	TNode node = (TNode)xtools.convertXMLToATerm(xmlConfigurationFile);
	Vector classPaths = new Vector();
	extractClassPaths(node.getDocElem(), classPaths);

	// creates an instance of each plugin
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

	System.out.println("--------------------------------------------------");

	// asks the starter to extract global options before options are collected
	TomPlugin firstAsPlugin = (TomPlugin)instances.firstElement();
	if(firstAsPlugin instanceof TomStarterInterface) // first plugin must be a starter
	    {
		TomStarterInterface firstAsStarter = (TomStarterInterface)firstAsPlugin;
		firstAsStarter.extractOptionList();
	    }
	else throw new Exception("First plugin has to implement jtom.starter.TomStarterInterface");

	// collects the options/services provided by each plugin
	it = instances.iterator();
	while(it.hasNext())
	    {
		TomPlugin plugin = (TomPlugin)it.next();
		services.add(plugin.declareOptions());
	    }

	System.out.println("--------------------------------------------------");

	String[] inputFiles = null;

	// asks the starter to set options accordingly to the arguments given in input
	if(firstAsPlugin instanceof TomStarterInterface) // first plugin must be a starter
	    {
		TomStarterInterface firstAsStarter = (TomStarterInterface)firstAsPlugin;
		inputFiles = firstAsStarter.processArguments(argumentList);
	    }
	else throw new Exception("First plugin has to implement jtom.starter.TomStarterInterface");

	System.out.println("--------------------------------------------------");

	// regenerates the options/services now that the proper values have been set
	it = instances.iterator();
	services.clear();
	while(it.hasNext())
	    {
		TomPlugin plugin = (TomPlugin)it.next();
		services.add(plugin.declareOptions());
	    }

	System.out.println("--------------------------------------------------");

	// checks if every plugin's needs are fulfilled
	it = instances.iterator();
	while(it.hasNext())
	    {
		TomPlugin plugin = (TomPlugin)it.next();
		boolean canGoOn = arePrerequisitesMet(plugin.requiredOptions());
		if (!canGoOn)
		    {
			System.out.println(plugin.getClass().getName() + " can't run : prerequisites not met.");
			System.exit(1);
		    }
	    }

	for(int i = 0; i < inputFiles.length; i++)
	    {
		try{
		    // creates a TomTerm from the input file
		    File file = new File(inputFiles[i]);
		    FileInputStream fis = new FileInputStream(file);

		    byte[] source = new byte[(int)file.length()+1];
		    fis.read(source);
	
		    TomTerm term = `TomString(new String(source));

		    // runs the modules
		    it = instances.iterator();
		    while(it.hasNext())
			{
			    TomPlugin plugin = (TomPlugin)it.next();
			    plugin.setInput(term);
			    plugin.run();
			    term = plugin.getOutput();
			}

		    // takes the output TomTerm and writes it down to file
		    %match(TomTerm term)
			{
			    TomString(s) -> 
				{ 
				    FileOutputStream fos = new FileOutputStream("./output");

				    fos.write(s.getBytes());
				}
			}
		}catch(Exception e){ System.out.println(e.getMessage()); }
	    }
	System.out.println("--------------------------------------------------");
    }

    private void extractClassPaths(TNode node, Vector v)
    {
	TNodeList list = null;

	%match(TNode node)
	    {
		ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			extractClassPathsList(c, v); }	
	    }
    }
    private void extractClassPathsList(TNodeList list, Vector v)
    {
	while(!(list.isEmpty()))
	    {
		TNode h = list.getHead();
		extractClassPathsText(h, v);

		list = list.getTail();
	    }
    }	
    private void extractClassPathsText(TNode node, Vector v)
    {
	TNode cPath = null;
	TNodeList list = null;

	%match(TNode node)
	    {
		<plugin>
		     c@<classpath></classpath>
		     </plugin> -> { cPath = c; }
	    }

	%match(TNode cPath)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			list = c; }
	    }

	if(list != null)
	    {
		TNode text = list.getHead();
		String path = null;

		%match(TNode text)
		    {
			TextNode[data = d]
			    -> { path = d; }
		    }

		System.out.println(path);
		v.add(path);
	    }
    }

    /*
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
		OptionList ol = (OptionList)services.elementAt(i);
		OptionList list = `concOption(ol*);

		while(!(list.isEmpty()))
		    {
			Option h = list.getHead();
			%match(Option h)
			    {
				OptionBoolean[name=n, altName=a] ->
				    {
					if(n.equals(optionName)||a.equals(optionName))
					    {
						TomPlugin plugin = (TomPlugin)instances.elementAt(i);
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
						TomPlugin plugin = (TomPlugin)instances.elementAt(i);
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
						TomPlugin plugin = (TomPlugin)instances.elementAt(i);
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

    /*
     * Returns the value of this option. Returns an Object which is a Boolean, a String or an Integer
     * depending on what the option type is.
     */
    public Object getOptionValue(String optionName)
    {
	Vector pair = new Vector();

	for(int i = 0; i < services.size(); i++)
	    {
		OptionList ol = (OptionList)services.elementAt(i);
		OptionList list = `concOption(ol*);

		while(!(list.isEmpty()))
		    {
			Option h = list.getHead();
			%match(Option h)
			    {
				OptionBoolean[name=n, altName=a, valueB=v] ->
				    {
					if(n.equals(optionName)||a.equals(optionName))
					    {
						return new Integer(v); // new Boolean(v);
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
	return null;
    }

    /*
     * Self-explanatory
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
		OptionList ol = (OptionList)services.elementAt(i);
		OptionList list = `concOption(ol*);

		while(!(list.isEmpty()))
		    {
			Option h = list.getHead();
			%match(Option h)
			    {
				OptionBoolean(n, a, d, 0) -> // display only flags that are not activated by default
				    {
					String s;
					if(a.length() > 0)
					    s = "\n\t"+n+" \t| "+a+" : \t"+d;
					else
					    s = "\n\t"+n+" \t \t : \t"+d;
					buffy.append(s);
				    }
				OptionInteger[name=n, altName=a, description=d, attrName=at] ->
				    {
					String s;
					if(a.length() > 0)
					    s = "\n\t"+n+" <"+at+"> \t| "+a+" : \t"+d;
					else
					    s = "\n\t"+n+" <"+at+"> \t \t : \t"+d;
					buffy.append(s);
				    }
				OptionString[name=n, altName=a, description=d, attrName=at] ->
				    {
					String s;
					if(a.length() > 0)
					    s = "\n\t"+n+" <"+at+"> \t| "+a+" : \t"+d;
					else
					    s = "\n\t"+n+" <"+at+"> \t \t : \t"+d;
					buffy.append(s);
				    }
			    }			
			list = list.getTail();
		    }
	    }
	
	System.out.println(buffy.toString());
	System.exit(0);
    }

    /*
     * Checks if all the options a plugin needs are here.
     */
    public boolean arePrerequisitesMet(OptionList list)
    {
	while(!(list.isEmpty()))
	    {
		Option h = list.getHead();
		boolean optionFound = false;

		for(int i = 0; i < services.size(); i++)
		    {
			OptionList ol = (OptionList)services.elementAt(i);
			
			%match(OptionList ol, Option h)
			    {
				concOption(_*,OptionBoolean(name,_,_,val),_*) , OptionBoolean(name,_,_,val) ->
				    { optionFound = true; }
				concOption(_*,OptionInteger(name,_,_,val,_),_*) , OptionInteger(name,_,_,val,_) -> 
				    { optionFound = true; }
				concOption(_*,OptionString(name,_,_,val,_),_*) , OptionString(name,_,_,val,_) -> 
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
	try
	    {
		TomServer server = TomServer.create(args);
		server.run();
	    }
	catch(Exception e){ System.out.println(e.getMessage()); }		


    }
}
