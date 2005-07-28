/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2005, INRIA
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

package jtom;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.platform.ConfigurationManager;
import tom.platform.OptionManager;
import tom.platform.OptionOwner;
import tom.platform.adt.platformoption.PlatformOptionFactory;
import tom.platform.adt.platformoption.types.PlatformBoolean;
import tom.platform.adt.platformoption.types.PlatformOption;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import tom.platform.adt.platformoption.types.PlatformValue;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;
import aterm.pure.SingletonFactory;

/**
 * The TomOptionManager manages options of each plugin in the platform.
 */
public class TomOptionManager implements OptionManager, OptionOwner {

  %include{ adt/platformoption/PlatformOption.tom }
  
  /**
   * Accessor method necessary to include adt/platformoption/PlatformOption.tom
   * @return a PlatformOptionFactory
   */
  private PlatformOptionFactory getPlatformOptionFactory() {
    return PlatformOptionFactory.getInstance(SingletonFactory.getInstance());
  }

  /** The global options */    
  private PlatformOptionList globalOptions;
  
  /**  map the name of an option to the plugin which defines this option */
  private Map mapNameToOwner;
  
  /** map the name of an option to the option itself */
  private Map mapNameToOption;

  /** map a shortname of an option to its full name */
  private Map mapShortNameToName;
  
  /** the list of input files extract from the commandLine */
  private List inputFileList;

  /**
   * Basic Constructor
   * constructing a configurationManager that needs to be initialized
   */
  public TomOptionManager() {
    mapNameToOwner = new HashMap();
    mapNameToOption = new HashMap();
    mapShortNameToName = new HashMap();
    inputFileList = new ArrayList();
    globalOptions = `emptyPlatformOptionList();
  }

  /**
   * initialize does everything needed
   *
   * @param confManager a configuration manager
   * @param commandLine the command line, as an array of Strings
   * @return  an error code :
   * <ul>
   * <li>0 if no error was encountered</li>
   * <li>1 if something went wrong</li>
   * </ul>
   */
  public int initialize(ConfigurationManager confManager, String[] commandLine) {
    List pluginList = confManager.getPluginsList();
    List optionOwnerList = new ArrayList(pluginList);
    optionOwnerList.add(this);
    collectOptions(optionOwnerList, pluginList);
    this.inputFileList = processArguments(commandLine);
    if(this.inputFileList == null) {
      return 1;
    }
    return checkAllOptionsDepedencies(optionOwnerList);
  }

  /**
   * Inherited from OptionManager interface
   */
  public void setGlobalOptionList(PlatformOptionList globalOptions) {
    this.globalOptions = globalOptions;
  }

  /**
   * @return the input files list
   */
  public List getInputToCompileList() {
    return inputFileList;
  }
  
  /**
   *  An option has changed
   * 
   * @param optionName the option's name
   * @param optionValue the option's desired value
   */
  public void optionChanged(String optionName, Object optionValue) {
    //optionName = getCanonicalName(optionName);
    if(optionName.equals("verbose")) {
      if( ((Boolean)optionValue).booleanValue() ) { 
        Tom.changeLogLevel(Level.INFO);
      }
    } else if(optionName.equals("wall")) {
      if( ((Boolean)optionValue).booleanValue() ) { 
        Tom.changeLogLevel(Level.WARNING);
      }
    }
  }
  
  /**
   * Sets an option to the desired value.
   * @param optionName the name of the option to set
   * @param optionValue a value for the option to set
   */
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
    // alert the owner of the change
    OptionOwner owner = getOptionOwnerFromName(optionName);
    owner.optionChanged(getCanonicalName(optionName), optionValue);
  }
  
  /**
   * Returns the value of an option. Returns an Object which is a Boolean,
   * a String or an Integer depending on what the option type is.
   * 
   * @param name the name of the option whose value is seeked
   * @return an Object containing the option's value
   */
  public Object getOptionValue(String name) {
    PlatformOption option = getOptionFromName(name);
    %match(PlatformOption option) {
      PluginOption[value=BooleanValue(True())]  -> { 
        return Boolean.valueOf(true); 
      }
      PluginOption[value=BooleanValue(False())] -> { 
        return Boolean.valueOf(false); 
      }
      PluginOption[value=IntegerValue(value)]   -> { 
        return new Integer(`value); 
      }
      PluginOption[value=StringValue(value)]    -> { 
        return `value; 
      }
    }
    getLogger().log(Level.SEVERE,"TomOptionManager: getOptionFromName did not return a PluginOption");
    throw new RuntimeException();
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
   * The TomOptionManager does no need to retain the OptionManager
   * since it is the OptionManager.
   * @param om which is not used 
   */
  public void setOptionManager(OptionManager om) {}
  
  /**
   * collects and initializes the options/services provided by each plugin
   */
  private void collectOptions(List optionOwnerList, List plugins) {
    Iterator owners = optionOwnerList.iterator();
    while(owners.hasNext()) {
      OptionOwner owner = (OptionOwner)owners.next();
      PlatformOptionList list = owner.getDeclaredOptionList();
      owner.setOptionManager((OptionManager)this);
      while(!list.isEmpty()) {
        PlatformOption option = list.getHead();
        %match(PlatformOption option) {
          PluginOption[name=name, altName=altName] -> {
            setOptionOwnerFromName(`name, owner);
            setOptionFromName(`name, option);
            if(`altName.length() > 0) {
              mapShortNameToName.put(`altName,`name);
            }
          }
        }
        list = list.getTail();
      }
    }
  }
  
  /**
   * Checks if every plugin's needs are fulfilled.
   * @param optionownerlist the list of option owners to check
   * @return 0 if there is no unfulfilled need, 1 otherwise
   */
  private int checkAllOptionsDepedencies(List optionOwnerList) {
    Iterator owners = optionOwnerList.iterator();
    while(owners.hasNext()) {
      OptionOwner plugin = (OptionOwner)owners.next();
      if(!checkOptionDependency(plugin.getRequiredOptionList())) {
        getLogger().log(Level.SEVERE, TomMessage.prerequisitesIssue.getMessage(), 
                        plugin.getClass().getName());
        return 1;
      }
    }
    return 0;
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
      getLogger().log(Level.SEVERE,TomMessage.optionNotFound.getMessage(),getCanonicalName(name));
      throw new RuntimeException();
    }
    return option;
  }
  
  private PlatformOption setOptionFromName(String name, PlatformOption option) {
    return (PlatformOption)mapNameToOption.put(getCanonicalName(name),option);
  }
  
  private OptionOwner getOptionOwnerFromName(String name) {
    OptionOwner plugin = (OptionOwner)mapNameToOwner.get(getCanonicalName(name));
    if(plugin == null) {
      getLogger().log(Level.SEVERE,TomMessage.optionNotFound.getMessage(),getCanonicalName(name));
    }
    return plugin;
  }

  private void setOptionOwnerFromName(String name, OptionOwner plugin) {
    mapNameToOwner.put(getCanonicalName(name),plugin);
  }
  
  private void setOptionPlatformValue(String name, PlatformValue value) {
    PlatformOption option = getOptionFromName(name);
    if(option != null) {
      PlatformOption newOption = option.setValue(value);
      Object replaced = setOptionFromName(name, newOption);
      getLogger().log(Level.FINER,TomMessage.setValue.getMessage(),
                      new Object[]{name,value,replaced});
    } else {
      throw new RuntimeException();
    }
  }
  
  /**
   * Displays an help message indicating how to use the compiler.
   */
  private void displayHelp() {
    String beginning = "\nTom usage :"
      + "\n\ttom [options] input[.t] [... input[.t]]"
      + "\noptions :";
    StringBuffer buffer = new StringBuffer(beginning);
    buffer.append("\n\t-X <file>:\tDefines an alternate XML configuration file\n");

    TreeMap treeMap = new TreeMap(mapNameToOption);
    Iterator it = treeMap.values().iterator();
    while(it.hasNext()) {
      PlatformOption h = (PlatformOption)it.next();
      %match(PlatformOption h) {
        PluginOption[name=name, altName=altName, description=description, attrName=attrName] -> {
          buffer.append("\t--" + `name);
          if(`attrName.length() > 0) {
            buffer.append(" <" + `attrName + ">");
          }
          if(`altName.length() > 0) {
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
  public static void displayVersion() {
    System.out.println("\njtom " + Tom.VERSION + "\n" +
                       "Copyright (c) 2000-2005, INRIA, Nancy, France.\n");
  }
  
  /**
   * Checks if all the options a plugin needs are here.
   * 
   * @param requiredOptions a list of options that must be found with
   * the right value
   * @return true if every option was found with the right value
   */
  private boolean checkOptionDependency(PlatformOptionList requiredOptions) {
    %match(PlatformOptionList requiredOptions) {
      concPlatformOption() -> {
        return true;
      }

      concPlatformOption(PluginOption[name=name,value=value],tail*) -> {
        PlatformOption option = getOptionFromName(`name);
        if(option !=null) {
          PlatformValue localValue = option.getValue();
          if(`value != localValue) {
            getLogger().log(Level.SEVERE, TomMessage.incorrectOptionValue.getMessage(), new Object[]{`name,`value,getOptionValue(`name)});
            return false;
          } else {
            return checkOptionDependency(`tail*);
          }
        } else {
          getLogger().log(Level.SEVERE, TomMessage.incorrectOptionValue.getMessage(), 
                          new Object[]{`name,`value,getOptionValue(`name)});
          return false;
        }
      }
    }
    return false;
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
    String argument = "";
    try {
      for(; i < argumentList.length; i++) {
        argument = argumentList[i];

        if(!argument.startsWith("-")) {
          // input file name, should never start with '-'
          fileList.add(argument);
        } else {
          // argument does start with '-', thus is -or at least should be- an option
          argument = argument.substring(1); // crops the '-'
          if(argument.startsWith("-")) {
            // if there's another one
            argument = argument.substring(1); // crops the second '-'
          }
          if(argument.equals("help") || argument.equals("h")) {
            displayHelp();
            return null;
          }
          if(argument.equals("version") || argument.equals("V")) {
            TomOptionManager.displayVersion();
          }
          if(argument.equals("X")) {
            // just skip it,along with its argument
            i++;
            continue;
          }
          if(argument.equals("import") || argument.equals("I")) {
            imports.append(argumentList[++i] + File.pathSeparator);
          }
          if(argument.equals("output") || argument.equals("o")) {
            if(outputEncountered) {
              getLogger().log(Level.SEVERE, TomMessage.outputTwice.getMessage());
              return null;
            } else {
              outputEncountered = true;
            }
          }
          if(argument.equals("destdir") || argument.equals("d")) {
            if(destdirEncountered) {
              getLogger().log(Level.SEVERE, TomMessage.destdirTwice.getMessage());
              return null;
            } else {
              destdirEncountered = true;
            }
          }

          PlatformOption option = getOptionFromName(argument);
          OptionOwner owner = getOptionOwnerFromName(argument);

          if(option == null || owner == null) {// option not found
            getLogger().log(Level.SEVERE, TomMessage.invalidOption.getMessage(), argument);
            displayHelp();
            return null;
          } else {
            %match(PlatformOption option) {
              PluginOption[value=BooleanValue[]] -> {
                // this is a boolean flag if set then we put the opposite of current value
                if(((Boolean)getOptionValue(argument)).booleanValue()) {
                  setOptionValue(argument, Boolean.FALSE);
                } else {
                  setOptionValue(argument, Boolean.TRUE);
                }
              }

              PluginOption[value=IntegerValue[]] -> {
                String t = argumentList[++i];
                setOptionValue(argument, new Integer(t));
              }

              PluginOption[value=StringValue[]] -> {
                if ( !( argument.equals("import") || argument.equals("I") ) ) {
                  // "import" is handled in the end
                  String t = argumentList[++i];
                  setOptionValue(argument, t);
                }
              }
            }
          }             
        } 
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      getLogger().log(Level.SEVERE, TomMessage.incompleteOption.getMessage(), argument);
      return null;
    }
    
    setOptionValue("import",imports.toString());
    
    if(fileList.isEmpty()) {
      getLogger().log(Level.SEVERE, TomMessage.noFileToCompile.getMessage());
      displayHelp();
      return null;
    } else if(fileList.size() > 1 && outputEncountered) {
      getLogger().log(Level.SEVERE, TomMessage.outputWithMultipleCompilation.getMessage());
      displayHelp();
      return null;
    }
    
    return fileList;
  }
  
  /** logger accessor in case of logging needs*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
  
} // class TomOptionManager.t
