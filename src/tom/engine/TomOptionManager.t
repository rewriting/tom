/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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

package tom.engine;

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
import tom.platform.Plugin;
import tom.platform.adt.platformoption.types.PlatformBoolean;
import tom.platform.adt.platformoption.types.PlatformOption;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import tom.platform.adt.platformoption.types.PlatformValue;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;

/**
 * The TomOptionManager manages options of each plugin in the platform.
 */
public class TomOptionManager implements OptionManager, OptionOwner {

  %include { ../platform/adt/platformoption/PlatformOption.tom }

  /** The global options */
  private PlatformOptionList globalOptions;

  /**  map the name of an option to the plugin which defines this option */
  private Map<String,OptionOwner> mapNameToOwner;

  /** map the name of an option to the option itself */
  private Map<String,PlatformOption> mapNameToOption;

  /** map a shortname of an option to its full name */
  private Map<String,String> mapShortNameToName;

  /** the list of input files extract from the commandLine */
  private List<String> inputFileList;

  private static Logger logger = Logger.getLogger("tom.engine.TomOptionManager");
  /**
   * Basic Constructor
   * constructing a configurationManager that needs to be initialized
   */
  public TomOptionManager() {
    mapNameToOwner = new HashMap<String,OptionOwner>();
    mapNameToOption = new HashMap<String,PlatformOption>();
    mapShortNameToName = new HashMap<String,String>();
    inputFileList = new ArrayList<String>();
    globalOptions = `concPlatformOption();
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
    List<Plugin> pluginList = confManager.getPluginsList();
    List<OptionOwner> optionOwnerList = new ArrayList<OptionOwner>(pluginList);
    optionOwnerList.add(this);
    collectOptions(optionOwnerList, pluginList);
    this.inputFileList = processArguments(commandLine);
    if(this.inputFileList == null) {
      return 1;
    }
    // only if it's not a call from GOM
    if(((Boolean)getOptionValue("optimize2")).booleanValue()
        && !(inputFileList.size() == 1 && "-".equals((String)inputFileList.get(0)) ) ) {
      TomMessage.warning(logger, null, 0, TomMessage.optimizerModifiesLineNumbers);
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
  public List<String> getInputToCompileList() {
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
    %match(option) {
      PluginOption[Value=BooleanValue(True())]  -> {
        return Boolean.valueOf(true);
      }
      PluginOption[Value=BooleanValue(False())] -> {
        return Boolean.valueOf(false);
      }
      PluginOption[Value=IntegerValue(value)]   -> {
        return Integer.valueOf(`value);
      }
      PluginOption[Value=StringValue(value)]    -> {
        return `value;
      }
    }
    TomMessage.error(logger, null, 0, TomMessage.notReturnedPluginOption);
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
    PlatformOptionList prerequisites = `concPlatformOption();
/*
    // options destdir and output are incompatible
    if(!((String)getOptionValue("destdir")).equals(".")) {
      prerequisites = `concPlatformOption(PluginOption("output", "o", "", StringValue(""), "file"), prerequisites*);
      // destdir is not set at its default value -> it has been changed
      // -> we want output at its default value
    }
    if(!((String)getOptionValue("output")).equals("")) {
      prerequisites = `concPlatformOption(PluginOption("destdir", "d", "", StringValue("."), "dir"), prerequisites*);
      // output is not set at its default value -> it has been changed
      // -> we want destdir at its default value
    }
    */
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
  private void collectOptions(List<OptionOwner> optionOwnerList, List plugins) {
    for (OptionOwner owner : optionOwnerList) {
      PlatformOptionList list = owner.getDeclaredOptionList();
      owner.setOptionManager((OptionManager)this);
      while(!list.isEmptyconcPlatformOption()) {
        PlatformOption option = list.getHeadconcPlatformOption();
        %match(option) {
          PluginOption[Name=name, AltName=altName] -> {
            setOptionOwnerFromName(`name, owner);
            setOptionFromName(`name, option);
            if(`altName.length() > 0) {
              mapShortNameToName.put(`altName,`name);
            }
          }
        }
        list = list.getTailconcPlatformOption();
      }
    }
  }

  /**
   * Checks if every plugin's needs are fulfilled.
   * @param optionownerlist the list of option owners to check
   * @return 0 if there is no unfulfilled need, 1 otherwise
   */
  private int checkAllOptionsDepedencies(List<OptionOwner> optionOwnerList) {
    for(OptionOwner plugin : optionOwnerList) {
      if(!checkOptionDependency(plugin.getRequiredOptionList())) {
        TomMessage.error(logger, null, 0, TomMessage.prerequisitesIssue, plugin.getClass().getName());
        return 1;
      }
    }
    return 0;
  }

  private String getCanonicalName(String name) {
    if(mapShortNameToName.containsKey(name)) {
      return mapShortNameToName.get(name);
    }
    return name;
  }

  private PlatformOption getOptionFromName(String name) {
    //System.out.println("getOptionFromName: " + name);
    PlatformOption option = mapNameToOption.get(getCanonicalName(name));
    //System.out.println("option: " + option);
    if(option == null) {
      TomMessage.error(logger, null, 0, TomMessage.optionNotFound, getCanonicalName(name));
      //throw new RuntimeException();
    }
    return option;
  }

  private PlatformOption setOptionFromName(String name, PlatformOption option) {
    //System.out.println("setOptionFromName: " + name);
    return mapNameToOption.put(getCanonicalName(name),option);
  }

  private OptionOwner getOptionOwnerFromName(String name) {
    OptionOwner plugin = mapNameToOwner.get(getCanonicalName(name));
    if(plugin == null) {
      TomMessage.error(logger, null, 0, TomMessage.optionNotFound, getCanonicalName(name));
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
      PlatformOption replaced = setOptionFromName(name, newOption);
      TomMessage.finer(logger, null, 0, TomMessage.setValue, 
          name,value,replaced);
    } else {
      TomMessage.error(logger, null, 0, TomMessage.optionNotFound, getCanonicalName(name));
      throw new RuntimeException();
    }
  }

  /**
   * Displays an help message indicating how to use the compiler.
   */
  private void displayHelp() {
    String beginning = "usage: tom [options] input[.t] [... input[.t]]"
      + "\noptions:\n";
    StringBuilder buffer = new StringBuilder(beginning);
    TreeMap<String,PlatformOption> treeMap = new TreeMap<String,PlatformOption>(mapNameToOption);
    for (PlatformOption h : treeMap.values()) {
      %match(h) {
        PluginOption[Name=name, AltName=altName, Description=description,
          AttrName=attrName] -> {
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
        "Copyright (c) 2000-2016, Universite de Lorraine, Inria, Nancy, France.\n");
  }

  /**
   * Checks if all the options a plugin needs are here.
   *
   * @param requiredOptions a list of options that must be found with
   * the right value
   * @return true if every option was found with the right value
   */
  private boolean checkOptionDependency(PlatformOptionList requiredOptions) {

    //System.out.println("requiredOptions: " + requiredOptions);
    %match(requiredOptions) {
      concPlatformOption() -> {
        return true;
      }

      concPlatformOption(PluginOption[Name=name,Value=value],tail*) -> {
        PlatformOption option = getOptionFromName(`name);
        if(option !=null) {
          PlatformValue localValue = option.getValue();
          if(`value != localValue) {
            TomMessage.error(logger, null, 0, TomMessage.incorrectOptionValue, `name,`value,getOptionValue(`name));
            return false;
          } else {
            return checkOptionDependency(`tail*);
          }
        } else {
          TomMessage.error(logger, null, 0, TomMessage.incorrectOptionValue, `name,`value,getOptionValue(`name));
          return false;
        }
      }
    }

    System.out.println("strange term: " + requiredOptions);
    return false;
  }

  /**
   * This method takes the arguments given by the user and deduces the options
   * to set, then sets them.
   *
   * @param argumentList
   * @return an array containing the name of the input files
   */
  private List<String> processArguments(String[] argumentList) {
    List<String> fileList = new ArrayList<String>();
    StringBuilder imports = new StringBuilder();
    boolean outputEncountered = false;
    boolean destdirEncountered = false;
    int i = 0;
    String argument = "";
    try {
      for(; i < argumentList.length; i++) {
        argument = argumentList[i];

        if(!argument.startsWith("-") || (argument.equals("-"))) {
          // input file name, should never start with '-' (except for System.in)
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
            return null;
          }
          if(argument.equals("X")) {
            // just skip it, along with its argument
            i++;
            continue;
          }
          if(argument.equals("import") || argument.equals("I")) {
            imports.append(argumentList[++i] + File.pathSeparator);
          }
          if(argument.equals("output") || argument.equals("o")) {
            if(outputEncountered) {
              TomMessage.error(logger, null, 0, TomMessage.outputTwice);
              return null;
            } else {
              outputEncountered = true;
            }
          }
          if(argument.equals("destdir") || argument.equals("d")) {
            if(destdirEncountered) {
              TomMessage.error(logger, null, 0, TomMessage.destdirTwice);
              return null;
            } else {
              destdirEncountered = true;
            }
          }

          PlatformOption option = getOptionFromName(argument);
          OptionOwner owner = getOptionOwnerFromName(argument);

          if(option == null || owner == null) {// option not found
            TomMessage.error(logger, null, 0, TomMessage.invalidOption, argument);
            displayHelp();
            return null;
          } else {
            %match(option) {
              PluginOption[Value=BooleanValue[]] -> {
                // this is a boolean flag: we set to TRUE
                // and no the opposite since -O2 implies -p=true
                setOptionValue(argument, Boolean.TRUE);
              }

              PluginOption[Value=IntegerValue[]] -> {
                String t = argumentList[++i];
                setOptionValue(argument, Integer.valueOf(t));
              }

              PluginOption[Value=StringValue[]] -> {
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
      TomMessage.error(logger, null, 0, TomMessage.incompleteOption, argument);
      return null;
    }

    setOptionValue("import",imports.toString());

    if(fileList.isEmpty()) {
      TomMessage.error(logger, null, 0, TomMessage.noFileToCompile);
      displayHelp();
      return null;
    } else if(fileList.size() > 1 && outputEncountered) {
      TomMessage.error(logger, null, 0, TomMessage.outputWithMultipleCompilation);
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
