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
 * Gregory Andrien
 *
 **/

package jtom;

import java.util.*;
import java.util.logging.*;
import java.io.*;

import jtom.*;

import tom.platform.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

import tom.platform.adt.platformoption.*;
import tom.platform.adt.platformoption.types.*;

import jtom.exception.*;

import tom.library.xml.*;

import jtom.tools.*;

import aterm.*;
import aterm.pure.*;

public class TomOptionManager implements OptionManager, OptionOwner {
  
  /** Used to analyse xml configuration file*/
  %include{ adt/TNode.tom }
  
  /**
   * Accessor method necessary when including adt/TNode.tom 
   * @return a TNodeFactory
   */
  private TNodeFactory getTNodeFactory() {
    return TNodeFactory.getInstance(SingletonFactory.getInstance());
  }

  %include{ adt/PlatformOption.tom }
  /**
   * Accessor method necessary to include adt/PlatformOption.tom
   * @return a PlatformOptionFactory
   */
  private PlatformOptionFactory getPlatformOptionFactory() {
    return PlatformOptionFactory.getInstance(SingletonFactory.getInstance());
  }

  private final static String[] NULL_STRING_ARRAY = new String[0];
  
  /** The global options */    
  private PlatformOptionList globalOptions;
  
  /**  map the name of an option to the plugin which defines this option */
  private Map mapNameToOptionOwner;
  
  /** map the name of an option to the option itself */
  private Map mapNameToOption;

  /** map a shortname of an option to its full name */
  private Map mapShortNameToName;
  
  /** the list of input files extract from the commandLine */
  private List inputFileList;

  /** the TomOptionManager logger */
  private  Logger logger = Logger.getLogger(getClass().getName());
  
  private static TomOptionManager instance;

  private TomOptionManager() {
    mapNameToOptionOwner = new HashMap();
    mapNameToOption = new HashMap();
    mapShortNameToName = new HashMap();
    inputFileList = new ArrayList();
  }
  
  public static TomOptionManager getInstance() {
    if(instance == null) {
      throw new RuntimeException();
    }
    return instance;
  }

  /**
   * This method does the following :
   * <ul>
   * <li>a first call to getDeclaredOptions() on the PluginPlatform and each
   * plugin, in order to determine which options exist and their default values
   * </li>
   * <li>a call to processArguments() in order to read the command line and set
   * the options to their actual values</li>
   * <li>a second call to getDeclaredOptions() in order to collect the options'
   * real value</li>
   * <li>eventually, prerequisites are checked.</li>
   * </ul>
   * 
   * @param argumentList the command line
   * @return an array of String containing the names of the files to compile
   */
  public static int create(ConfigurationManager confManager, String[] commandLine) {
    instance = new TomOptionManager();
    return instance.initialize(confManager, commandLine);
  }

  private int initialize(ConfigurationManager confManager, String[] commandLine) {
    List optionOwnerList = new ArrayList(confManager.getPluginsReferenceList());
    optionOwnerList.add(this);
    
    this.globalOptions = confManager.getGlobalOtionList();    
    collectOptions(optionOwnerList, confManager.getPluginsReferenceList());
    this.inputFileList = processArguments(commandLine);
    if(this.inputFileList == null) {
      return 1;
    }
    return checkAllOptionsDepedencies(optionOwnerList);
  }

  /**
   * collects the options/services provided by each plugin
   */
  public void collectOptions(List optionOwnerList, List plugins) {
    Iterator owners = optionOwnerList.iterator();
    while(owners.hasNext()) {
      OptionOwner plugin = (OptionOwner)owners.next();
      PlatformOptionList list = plugin.getDeclaredOptionList();
      while(!list.isEmpty()) {
        PlatformOption option = list.getHead();
        %match(PlatformOption option) {
          PluginOption[name=name, altName=altName] -> {
            setOptionOwnerFromName(`name, plugin);
            setOptionFromName(`name, option);
            if(altName.length() > 0) {
              mapShortNameToName.put(`altName,`name);
            }
          }
        }
        list = list.getTail();
      }
    }
  }

  /**
   * Checks if every plugin's needs are fulfilled
   */
  public int checkAllOptionsDepedencies(List optionOwnerList) {
    Iterator owners = optionOwnerList.iterator();
    while(owners.hasNext()) {
      OptionOwner plugin = (OptionOwner)owners.next();
      if(!checkOptionDependency(plugin.getRequiredOptionList())) {
        logger.log(Level.SEVERE, "PrerequisitesIssue", plugin.getClass().getName());
        return 1;
      }
    }
    return 0;
  }

  public List getInputFileList() {
    return inputFileList;
  }

  private String getCanonicalName(String name) {
    if(mapShortNameToName.containsKey(name)) {
      return (String)mapShortNameToName.get(name);
    }
    return name;
  }

  private PlatformOption getOptionFromName(String name) {
    PlatformOption option = (PlatformOption)mapNameToOption.get(getCanonicalName(name));
    if(option == null) {
      logger.log(Level.SEVERE,"OptionNotFound",getCanonicalName(name));
      throw new RuntimeException();
    }
    return option;
  }

  private PlatformOption setOptionFromName(String name, PlatformOption option) {
    return (PlatformOption)mapNameToOption.put(getCanonicalName(name),option);
  }

  private OptionOwner getOptionOwnerFromName(String name) {
    OptionOwner plugin = (OptionOwner)mapNameToOptionOwner.get(getCanonicalName(name));
    if(plugin == null) {
      logger.log(Level.SEVERE,"OptionNotFound",getCanonicalName(name));
      throw new RuntimeException();
    }
    return plugin;
  }

  private void setOptionOwnerFromName(String name, OptionOwner plugin) {
    mapNameToOptionOwner.put(getCanonicalName(name),plugin);
  }
  
  private void setOptionPlatformValue(String name, PlatformValue value) {
    PlatformOption option = getOptionFromName(name);
    if(option != null) {
      PlatformOption newOption = option.setValue(value);
      Object replaced = setOptionFromName(name, newOption);
      logger.log(Level.FINER,"SetValue",new Object[]{name,value,replaced});
    } else {
      throw new RuntimeException();
    }
  }
  
  /**
   * Sets an option to the desired value.
   * 
   * @param optionName the option's name
   * @param optionValue the option's desired value
   */
  public void setOption(String optionName, Object optionValue) {
    // trampoline to implement OptionOwner
    setOptionValue(optionName,optionValue);
  }
  
  public void setOptionValue(String optionName, Object optionValue) {
    // to implement OptionManager
    PlatformBoolean bool = null;
    if(optionValue instanceof Boolean) {
      bool = ((Boolean)optionValue).booleanValue()?`True():`False();
      setOptionPlatformValue(optionName, `BooleanValue(bool));
    } else if(optionValue instanceof Integer) {
      Integer v = (Integer) optionValue;
      setOptionPlatformValue(optionName, `IntegerValue(v.intValue()));
    } else if(optionValue instanceof String) {
      String v = (String) optionValue;
      setOptionPlatformValue(optionName, `StringValue(v));
    } else {
      throw new RuntimeException("unknown optionValue type: " + optionValue);
    }
    
    if(optionName.equals("verbose") && bool == `True()) {
      Tom.changeLogLevel(Level.INFO);
    } else if(optionName.equals("noWarning") && bool == `True()) {
      Tom.changeLogLevel(Level.SEVERE);
    }
  }
  
  /**
   * Returns the value of an option. Returns an Object which is a Boolean,
   * a String or an Integer depending on what the option type is.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return an Object containing the option's value
   */
  public Object getOptionValue(String name) {
    PlatformOption option = getOptionFromName(name);
    %match(PlatformOption option) {
      PluginOption[value=BooleanValue(True())]  -> { return new Boolean(true); }
      PluginOption[value=BooleanValue(False())] -> { return new Boolean(false); }
      PluginOption[value=IntegerValue(value)]   -> { return new Integer(`value); }
      PluginOption[value=StringValue(value)]    -> { return `value; }
    }
    
    logger.log(Level.SEVERE,"OptionNotFound",name);
    throw new RuntimeException();
  }
  
  /**
   * Displays an help message indicating how to use the compiler.
   */
  private void displayHelp() {
    String beginning = "usage :"
	    + "\n\ttom [options] input[.t] [... input[.t]]"
	    + "\noptions :";
    StringBuffer buffer = new StringBuffer(beginning);
    buffer.append("\n\t-X <file>:\tDefines an alternate XML configuration file\n");
    
    Iterator it = mapNameToOption.values().iterator();
    while(it.hasNext()) {
      PlatformOption h = (PlatformOption)it.next();
      %match(PlatformOption h) {
        PluginOption[name=name, altName=altName, description=description, attrName=attrName] -> {
          buffer.append("\t--" + `name);
          if(attrName.length() > 0) {
            buffer.append(" <" + `attrName + ">");
          }
          if(altName.length() > 0) {
            buffer.append(" | -" + `altName);
          }
          buffer.append(":\t" + `description);
          buffer.append("\n");
        }
      }			
    }
    System.out.println(buffer.toString());
  }
  
  /**
   * Displays the current version of the TOM compiler.
   */
  public void displayVersion() {
    System.out.println("jtom " + Tom.VERSION + "\n\n"
                       + "Copyright (C) 2000-2004 INRIA, Nancy, France.\n");
  }
  
  /**
   * Checks if all the options a plugin needs are here.
   * 
   * @param list a list of options that must be found with the right value
   * @return true if every option was found with the right value
   */
  private boolean checkOptionDependency(PlatformOptionList requiredOptions) {
    %match(PlatformOptionList requiredOptions) {
      concPlatformOption() -> {
        return true;
      }

      concPlatformOption(PluginOption[name=name,value=value],tail*) -> {
        PlatformOption option = getOptionFromName(`name);
        PlatformValue localValue = option.getValue();
        if(`value != localValue) {
          logger.log(Level.SEVERE, "IncorrectOptionValue", new Object[]{`name,`value,getOptionValue(`name)});
          return false;
        } else {
          return checkOptionDependency(`tail*);
        }
      }
    }
    return false;
  }
  
  /**
   * Extracts the global options from the XML configuration file.
   * 
   * @param node the node containing the XML file
   */
  public void setGlobalOptionList(TNode node) {
    %match(TNode node) {
      <server>opt@<options></options></server> -> {
        globalOptions = xmlNodeToOptionList(`opt);
       }
    }
  }

  /**
   * From OptionOwner Interface
   * @return the global options
   */
  public PlatformOptionList getDeclaredOptionList() {
    return globalOptions;
  }

  /**
   * From OptionOwner Interface
   * @return the prerequisites
   */
  public PlatformOptionList getRequiredOptionList() {
    PlatformOptionList prerequisites = `emptyPlatformOptionList();

    if(((Boolean)getOptionValue("debug")).booleanValue()) {
      prerequisites = `concPlatformOption(PluginOption("jCode", "", "", BooleanValue(True()),""), prerequisites*);
      // for the moment debug is only available for Java as target language
    }

    // options destdir and output are incompatible
    if(!((String)getOptionValue("destdir")).equals(".")) {
      prerequisites = `concPlatformOption(PluginOption("output", "", "", StringValue(""), ""), prerequisites*);
      // destdir is not set at its default value -> it has been changed
      // -> we want output at its default value
    }
    if(!((String)getOptionValue("output")).equals("")) {
      prerequisites = `concPlatformOption(PluginOption("destdir", "", "", StringValue("."), ""), prerequisites*);
      // output is not set at its default value -> it has been changed
      // -> we want destdir at its default value
    }
    return prerequisites;
  }
  
  /**
   * This method takes the arguments given by the user and deduces the options
   * to set, then sets them.
   * 
   * @param argumentList
   * @return an array containing the name of the input files
   */
  private List processArguments(String[] argumentList) {
    List fileList = new ArrayList();
    StringBuffer imports = new StringBuffer();
    boolean outputEncountered = false;
    boolean destdirEncountered = false;
    int i = 0;

    try {
      for(; i < argumentList.length; i++) {
        String argument = argumentList[i];

        if(!argument.startsWith("-")) {
          // input file name, should never start with '-'
          fileList.add(argument);
        } else { // s does start with '-', thus is -or at least should be- an option
          argument = argument.substring(1); // crops the '-'
          if(argument.startsWith("-")) {
            // if there's another one
            argument = argument.substring(1); // crops the second '-'
          }
          if( argument.equals("help") || argument.equals("h") ) {
            displayHelp();
            return null;//System.exit(0);
          }
          if( argument.equals("version") || argument.equals("V") ) {
            displayVersion();
            return null;//System.exit(0);
          }
          if( argument.equals("X") ) {
            // if we're here, the PluginPlatform has already handled the "-X" option
            // and all errors that might occur
            // just skip it,along with its argument
            i++;
            continue;
          }
          if( argument.equals("import") || argument.equals("I") ) {
            imports.append(argumentList[++i] + ":");
          }
          if( argument.equals("output") || argument.equals("o") ) {
            if(outputEncountered) {
              logger.log(Level.SEVERE, "OutputTwice");
              return null;
            } else {
              outputEncountered = true;
            }
          }
          if( argument.equals("destdir") || argument.equals("d") ) {
            if(destdirEncountered) {
              logger.log(Level.SEVERE, "DestdirTwice");
              return null;
            } else {
              destdirEncountered = true;
            }
          }

          OptionOwner plugin = getOptionOwnerFromName(argument);
          PlatformOption option = getOptionFromName(argument);

          if(option == null || plugin == null) {// option not found
            logger.log(Level.SEVERE, "InvalidOption", argumentList[i]);
            return null;
          } else {
            %match(PlatformOption option) {
              PluginOption[value=BooleanValue[]] -> {
                plugin.setOption(argument, Boolean.TRUE);
              }

              PluginOption[value=IntegerValue[]] -> {
                String t = argumentList[++i];
                plugin.setOption(argument, new Integer(t));
              }

              PluginOption[value=StringValue[]] -> {
                if ( !( argument.equals("import") || argument.equals("I") ) ) {
                  // "import" is handled in the end
                  String t = argumentList[++i];
                  plugin.setOption(argument, t);
                }
              }
              
            }
            
          }     				
        }	
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      logger.log(Level.SEVERE, "IncompleteOption", argumentList[--i]);
      return null;
    }
    
    setOptionValue("import",imports.toString());
    
    if(fileList.isEmpty()) {
      displayVersion();
      displayHelp();
      logger.log(Level.SEVERE, "NoFileToCompile");
      return null;
    } else if(fileList.size() > 1 && outputEncountered) {
      logger.log(Level.SEVERE, "OutputWithMultipleCompilation");
      return null;
    }
    
    return fileList;
  }
  
  public static PlatformOptionList xmlToOptionList(String xmlString) {
    XmlTools xtools = new XmlTools();
    InputStream stream = new ByteArrayInputStream(xmlString.getBytes());
    TNode node = (TNode)xtools.convertXMLToATerm(stream);
    return (new TomOptionManager()).xmlNodeToOptionList(node.getDocElem());
  }
  
  private PlatformOptionList xmlNodeToOptionList(TNode optionsNode) {
    PlatformOptionList list = `emptyPlatformOptionList();
    %match(TNode optionsNode) {
      <options>(_*,option,_*)</options> -> {
        %match(TNode option) {
          <OptionBoolean [name = n, altName = an, description = d, value = v] /> -> {	
            PlatformBoolean bool = Boolean.valueOf(`v).booleanValue()?`True():`False();
            list = `concPlatformOption(list*, PluginOption(n, an, d, BooleanValue(bool), "")); 
          }
          <OptionInteger [name = n, altName = an, description = d, value = v, attrName = at] /> -> {
            list = `concPlatformOption(list*, PluginOption(n, an, d, IntegerValue(Integer.parseInt(v)), at));
          }
          <OptionString [name = n, altName = an, description = d, value = v, attrName = at] /> -> {
            list = `concPlatformOption(list*, PluginOption(n, an, d, StringValue(v), at));
          }
        }
      }
    }
    return list;
  }
  
}
