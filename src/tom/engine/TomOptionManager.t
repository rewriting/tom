package jtom;

import java.util.*;
import java.util.logging.*;
import java.io.*;

import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;

import jtom.adt.options.*;
import jtom.adt.options.types.*;

import jtom.exception.*;

import jtom.runtime.xml.*;

import jtom.tools.*;

import aterm.*;
import aterm.pure.*;

/**
 *
 *
 * @author Gr&eacute;gory ANDRIEN
 */
public class TomOptionManager implements OptionManager, TomPluginOptions {
  
  %include{ adt/TNode.tom }
  %include{ adt/Options.tom }

  /**
   * A list containing the owners of options (which implement TomPluginOptions).
   */
  private List owners;

  /**
   * The global options.
   */    
  private TomOptionList globalOptions;

  /**
   * This list is used solely to display the help message.
   */
  private TomOptionList helpList;

  /**
   * A Map allowing to match option names and plugins.
   */
  private Map optionOwners;

  /**
   * A Map allowing to match option names and their types.
   */
  private Map optionTypes;

  /**
   * A Map allowing to match option names and their values.
   */
  private Map optionValues;

  /**
   * An option can have up to two different names ; this map allows us to find synonyms.
   */
  private Map synonyms;

  /**
   *
   */
  private OptionsFactory optionsFactory;

  /**
   *
   */
  private static Logger logger;

  /**
   *
   *
   * @return
   */
  private TNodeFactory getTNodeFactory() {
    XmlTools xtools = new XmlTools();
    return xtools.getTNodeFactory();
  }

  private OptionsFactory getOptionsFactory() {
    return optionsFactory;
  }


  public TomOptionManager() {
    optionOwners = new HashMap();
    optionTypes = new HashMap();
    optionValues = new HashMap();
    synonyms = new HashMap();

    optionsFactory = OptionsFactory.getInstance(SingletonFactory.getInstance());

    logger = Logger.getLogger(getClass().getName());
  }

  public void setPlugins(List plugins) {
    owners = new ArrayList();
    owners.add(this);
    owners.addAll(plugins);

//     for(int i=0; i<owners.size(); i++)
// 	System.out.println(owners.get(i));
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
  public String[] optionManagement(String[] argumentList) {
    helpList = `emptyTomOptionList(); // is initialized here and not in create() cause method isn't static

    // collects the options/services provided by each plugin
    Iterator it = owners.iterator();
    while(it.hasNext()) {
      TomPluginOptions plugin = (TomPluginOptions)it.next();

      TomOptionList list = plugin.declaredOptions();
      helpList = `concTomOption(helpList*, list*);
     
      while(!(list.isEmpty())) {
	  TomOption option = list.getHead();
	  
	  %match(TomOption option) {
	      OptionBoolean[name=n, altName=an, valueB=v] 
		  -> {
		  optionOwners.put(n, plugin);
		  optionTypes.put(n,"boolean");
		  optionValues.put(n, new Boolean(v.isTrue()));
		  if( an.length() > 0 ) {
		      optionOwners.put(an, plugin);
		      optionTypes.put(an,"boolean");
		      optionValues.put(an, new Boolean(v.isTrue()));
		      synonyms.put(n,an);
		      synonyms.put(an,n);
		  }
	      }

	      OptionInteger[name=n, altName=an, valueI=v] 
		  -> {
		  optionOwners.put(n, plugin);
		  optionTypes.put(n,"integer");
		  optionValues.put(n, new Integer(v));
		  if( an.length() > 0 ) {
		      optionOwners.put(an, plugin);
		      optionTypes.put(an,"integer");
		      optionValues.put(an, new Integer(v));
		      synonyms.put(n,an);
		      synonyms.put(an,n);
		  }
	      }

	      OptionString[name=n, altName=an, valueS=v] 
		  -> {
		  optionOwners.put(n, plugin);
		  optionTypes.put(n,"string");
		  optionValues.put(n, v);
		  if( an.length() > 0 ) {
		      optionOwners.put(an, plugin);
		      optionTypes.put(an,"string");
		      optionValues.put(an, v);
		      synonyms.put(n,an);
		      synonyms.put(an,n);
		  }
	      }
	  }
		  
	  list = list.getTail();
      }
    }
    
    // set options accordingly to the arguments given in input
    String[] inputFiles = processArguments(argumentList);

    // checks if every plugin's needs are fulfilled
    it = owners.iterator();
    while(it.hasNext()) {
      TomPluginOptions plugin = (TomPluginOptions)it.next();
      boolean canGoOn = arePrerequisitesMet(plugin.requiredOptions());
      if (!canGoOn) {
	logger.log(Level.SEVERE,
		   "PrerequisitesIssue",
		   plugin.getClass().getName());
      }
    }
    
    return inputFiles;
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
    Object obj = optionValues.get(optionName);

    if (obj == null) {
	logger.log(Level.SEVERE,
		   "OptionNotFound",
		   optionName);
	return null;
    } else {
	return obj;
    }
  }

  /**
   * Returns the value of a boolean option.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return a boolean that is the option's value
   */
  public boolean getOptionBooleanValue(String optionName)
  {
    Object obj = optionValues.get(optionName);

    if (obj == null) {
	logger.log(Level.SEVERE,
		   "OptionNotFound",
		   optionName);
	return false;
    } else {
	try {
	    return ((Boolean)obj).booleanValue();
	} catch (ClassCastException cce) {
	    logger.log(Level.SEVERE,
		       "OptionNotFound",
		       optionName);
	    return false; // we shouldn't be here if the option is indeed a boolean option, that's why we log an error
	}
    }
  }
    
  /**
   * Returns the value of an integer option.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return an int that is the option's value
   */
  public int getOptionIntegerValue(String optionName)
  {
    Object obj = optionValues.get(optionName);

    if (obj == null) {
	logger.log(Level.SEVERE,
		   "OptionNotFound",
		   optionName);
	return 0;
    } else {
	try {
	return ((Integer)obj).intValue();
	} catch (ClassCastException cce) {
	    logger.log(Level.SEVERE,
		       "OptionNotFound",
		       optionName);
	    return 0; // we shouldn't be here if the option is indeed an integer option, that's why we log an error
	}
    }
  }
    
  /**
   * Returns the value of a string option.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return a String that is the option's value
   */
  public String getOptionStringValue(String optionName)
  {
    Object obj = optionValues.get(optionName);

    if (obj == null) {
	logger.log(Level.SEVERE,
		   "OptionNotFound",
		   optionName);
	return null;
    } else {
	try {
	return (String)obj;
	} catch (ClassCastException cce) {
	    logger.log(Level.SEVERE,
		       "OptionNotFound",
		       optionName);
	    return null; // we shouldn't be here if the option is indeed a string option, that's why we log an error
	}
    }
  }

  public void putOptionValue(Object key, Object value) {
    Object replaced = optionValues.put(key, value);
    logger.log(Level.FINER,
	       "SetValue",
	       new Object[]{key,value,replaced});

    Object synonym = synonyms.get(key);
    if( synonym != null ) { // if a synonym exists
      replaced = optionValues.put(synonym, value);
      logger.log(Level.FINER,
		 "SetValue",
		 new Object[]{synonym,value,replaced});
    }
  }

  /**
   * Self-explanatory. Displays an help message indicating how to use the compiler.
   */
  private void displayHelp()
  {
    String beginning = "\nusage :"
	    + "\n\ttom [options] input[.t] [... input[.t]]"
	    + "\noptions :";
    StringBuffer buffy = new StringBuffer(beginning);

    buffy.append("\n\t-X <file> \t \t : \tDefines an alternate XML configuration file");

    while(!(helpList.isEmpty()))
	{
            TomOption h = helpList.getHead();
            %match(TomOption h)
            {
              OptionBoolean(n, a, d, False()) ->
              {
                String s;
                if(a.length() > 0)
                  s = "\n\t--"+n+" \t| -"+a+" : \t"+d;
                else
                  s = "\n\t--"+n+" \t \t : \t"+d;
                buffy.append(s);
              }
              OptionBoolean(n, a, d, True()) ->
              {
                String s;
                if(a.length() > 0)
                  s = "\n\t--"+n+" \t| -"+a+" : \t"+d+" (activated by default)";
                else
                  s = "\n\t--"+n+" \t \t : \t"+d+" (activated by default)";
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
            helpList = helpList.getTail();
          }
	
    System.out.println(buffy.toString());
    System.exit(0);
  }

  /**
   * Self-explanatory. Displays the current version of the TOM compiler.
   */
  public void displayVersion()
  {
    System.out.println("\njtom " + Tom.VERSION + "\n\n"
                       + "Copyright (C) 2000-2004 INRIA, Nancy, France.\n");
    System.exit(0);
  }

  /**
   * Checks if all the options a plugin needs are here.
   * 
   * @param list a list of options that must be found with the right value
   * @return true if every option was found with the right value, false otherwise
   */
  private boolean arePrerequisitesMet(TomOptionList list) {
      while(!(list.isEmpty())) {
	  TomOption option = list.getHead();
	  String optionName = option.getName();
	
	  if( option.isOptionBoolean() ) {
	      boolean expectedValue = option.getValueB().isTrue();
	      boolean actualValue = getOptionBooleanValue(optionName);
	      if ( actualValue != expectedValue ) {
		  logger.log(Level.SEVERE,
			     "IncorrectOptionValue",
			     new Object[]{optionName,Boolean.toString(expectedValue),Boolean.toString(actualValue)});
		  return false;
	      }
	  } else if( option.isOptionInteger() ) {
	      int expectedValue = option.getValueI();
	      int actualValue = getOptionIntegerValue(optionName);
	      if ( actualValue != expectedValue ) {
		  logger.log(Level.SEVERE,
			     "IncorrectOptionValue",
			     new Object[]{optionName,Integer.toString(expectedValue),Integer.toString(actualValue)});
		  return false;
	      }
	  } else if( option.isOptionString() ) {
	      String expectedValue = option.getValueS();
	      String actualValue = getOptionStringValue(optionName);
	      if ( ! actualValue.equals(expectedValue) ) {
		  logger.log(Level.SEVERE,
			     "IncorrectOptionValue",
			     new Object[]{optionName,expectedValue,actualValue});
		  return false;
	      }
	  }
        
	  list = list.getTail();
      }
      return true;
  }



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
    TomOptionList prerequisites = `emptyTomOptionList();

    if( getOptionBooleanValue("debug") ) {
      prerequisites = `concTomOption(OptionBoolean("jCode", "", "", True()), prerequisites*);
      // for the moment debug is only available for Java as target language
    }

    // options destdir and output are incompatible

    if( !getOptionStringValue("destdir").equals(".") ) {
      prerequisites = `concTomOption(OptionString("output", "", "", "", ""), prerequisites*);
      // destdir is not set at its default value -> it has been changed
      // -> we want output at its default value
    }

    if( !getOptionStringValue("output").equals("") ) {
      prerequisites = `concTomOption(OptionString("destdir", "", "", ".", ""), prerequisites*);
      // output is not set at its default value -> it has been changed
      // -> we want destdir at its default value
    }

    return prerequisites;
  }

  /**
   * Sets an option to the desired value.
   * 
   * @param optionName the option's name
   * @param optionValue the option's desired value
   */
  public void setOption(String optionName, Object optionValue) {
    putOptionValue(optionName, optionValue);
  }

  /**
   * This method takes the arguments given by the user and deduces the options to set, then sets them.
   * 
   * @param argumentList
   * @return an array containing the name of the input files
   */
  private String[] processArguments(String[] argumentList)
  {
    List inputFiles = new ArrayList();
    StringBuffer imports = new StringBuffer();
    boolean outputEncountered = false;
    boolean destdirEncountered = false;
    int i = 0;

    try {
        for(; i < argumentList.length; i++) {
            String s = argumentList[i];
			
            if(!s.startsWith("-")) // input file name, should never start with '-'
              inputFiles.add(s);
            else { // s does start with '-', thus is -or at least should be- an option
                s = s.substring(1); // crops the '-'
                if(s.startsWith("-")) // if there's another one
                  s = s.substring(1); // crops the second '-'
				
                if( s.equals("help") || s.equals("h") )
                  displayHelp();
                if( s.equals("version") || s.equals("V") )
                  displayVersion();
                if( s.equals("X") ) {
                    // if we're here, the TomServer has already handled the "-X" option
                    // and all errors that might occur
                    // just skip it,along with its argument
                    i++;
                    continue;
		}
                if( s.equals("import") || s.equals("I") ) {
                    imports.append(argumentList[++i] + ":");
		}
                if( s.equals("output") || s.equals("o") ) {
                    if(outputEncountered) {
			  logger.log(Level.SEVERE,
				     "OutputTwice");
		    }
                    else outputEncountered = true;
		}
                if( s.equals("destdir") || s.equals("d") ) {
                    if(destdirEncountered) {
			logger.log(Level.SEVERE,
				   "DestdirTwice");
		    }
                    else destdirEncountered = true;
		}

		String type = (String)optionTypes.get(s);
		TomPluginOptions plugin = (TomPluginOptions)optionOwners.get(s);

                if(type == null || plugin == null) {// option not found
		    logger.log(Level.SEVERE,
			       "InvalidOption",
			       argumentList[i]);

                    return (String[])inputFiles.toArray(new String[]{});
		}
                else {                    				
                    if (type.equals("boolean")) {
			plugin.setOption(s, Boolean.TRUE);
		    } else if (type.equals("integer")) {
                        String t = argumentList[++i];
                        plugin.setOption(s, new Integer(t));
		    } else if (type.equals("string")) {
                        if ( !( s.equals("import") || s.equals("I") ) ) {// "import" is handled in the end
                            String t = argumentList[++i];
                            plugin.setOption(s, t);
			}
		    }
		}	
	    }
	}
    } catch (ArrayIndexOutOfBoundsException e) {
	logger.log(Level.SEVERE,
		   "IncompleteOption",
		   argumentList[--i]);

        return (String[])inputFiles.toArray(new String[]{});
    }

    setOption("import",imports.toString());

    if(inputFiles.isEmpty()) {
      logger.log(Level.SEVERE,
		 "NoFileToCompile");
    } else if(inputFiles.size() > 1 && outputEncountered) {
      logger.log(Level.SEVERE,
		 "OutputWithMultipleCompilation");
    }
    
    return (String[])inputFiles.toArray(new String[]{});	
  }

  /**
   * Extracts the global options from the XML configuration file.
   * 
   * @param node the node containing the XML file
   */
  public void extractOptionList(TNode node) {
    globalOptions = `emptyTomOptionList();
    %match(TNode node) {
      <server>opt@<options></options></server> -> {
	%match(TNode opt) {
	  ElementNode[childList = c] -> { 
	    while(!(c.isEmpty())) {
	      TNode h = c.getHead();
					
	      %match(TNode h) {
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
  private void extractOptionBoolean(TNode optionBooleanNode) {
    %match(TNode optionBooleanNode) {
      <OptionBoolean [name = n, altName = an, description = d, valueB = v] /> -> {
	%match(String v) {
          ('true') -> { 
	    globalOptions = `concTomOption(globalOptions*, OptionBoolean(n, an, d, True())); 
	  }
	  ('false') -> { 
	    globalOptions = `concTomOption(globalOptions*, OptionBoolean(n, an, d, False())); 
	  }
        }
      }
    }
  }

  /**
   * Adds an integer option to the global options.
   * 
   * @param optionIntegerNode the node containing the option
   */
  private void extractOptionInteger(TNode optionIntegerNode) {
    %match(TNode optionIntegerNode) {
      <OptionInteger [name = n, altName = an, description = d, valueI = v, attrName = at] /> -> {
        globalOptions = `concTomOption(OptionInteger(n, an, d, Integer.parseInt(v), at), globalOptions*);
      }
    }
  }

  /**
   * Adds a string option to the global options.
   * 
   * @param optionStringNode the node containing the option
   */
  private void extractOptionString(TNode optionStringNode) {
    %match(TNode optionStringNode) {
      <OptionString [name = n, altName = an, description = d, valueS = v, attrName = at] /> -> {
        globalOptions = `concTomOption(OptionString(n, an, d, v, at), globalOptions*);
      }
    }
  }

}
