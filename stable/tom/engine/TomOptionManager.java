/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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

/**
 * The TomOptionManager manages options of each plugin in the platform.
 */
public class TomOptionManager implements OptionManager, OptionOwner {

        private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }    

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
    globalOptions =  tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ;
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
      bool = ((Boolean)optionValue).booleanValue()? tom.platform.adt.platformoption.types.platformboolean.True.make() : tom.platform.adt.platformoption.types.platformboolean.False.make() ;
      setOptionPlatformValue(optionName,  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make(bool) );
    } else if(optionValue instanceof Integer) {
      Integer v = (Integer) optionValue;
      setOptionPlatformValue(optionName,  tom.platform.adt.platformoption.types.platformvalue.IntegerValue.make(v.intValue()) );
    } else if(optionValue instanceof String) {
      String v = (String) optionValue;
      setOptionPlatformValue(optionName,  tom.platform.adt.platformoption.types.platformvalue.StringValue.make(v) );
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
    { /* unamed block */{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) { tom.platform.adt.platformoption.types.PlatformValue  tomMatch53_1= (( tom.platform.adt.platformoption.types.PlatformOption )option).getValue() ;if ( ((( tom.platform.adt.platformoption.types.PlatformValue )tomMatch53_1) instanceof tom.platform.adt.platformoption.types.platformvalue.BooleanValue) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformBoolean ) tomMatch53_1.getBooleanValue() ) instanceof tom.platform.adt.platformoption.types.platformboolean.True) ) {

        return Boolean.valueOf(true);
      }}}}}{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) { tom.platform.adt.platformoption.types.PlatformValue  tomMatch53_10= (( tom.platform.adt.platformoption.types.PlatformOption )option).getValue() ;if ( ((( tom.platform.adt.platformoption.types.PlatformValue )tomMatch53_10) instanceof tom.platform.adt.platformoption.types.platformvalue.BooleanValue) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformBoolean ) tomMatch53_10.getBooleanValue() ) instanceof tom.platform.adt.platformoption.types.platformboolean.False) ) {

        return Boolean.valueOf(false);
      }}}}}{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) { tom.platform.adt.platformoption.types.PlatformValue  tomMatch53_19= (( tom.platform.adt.platformoption.types.PlatformOption )option).getValue() ;if ( ((( tom.platform.adt.platformoption.types.PlatformValue )tomMatch53_19) instanceof tom.platform.adt.platformoption.types.platformvalue.IntegerValue) ) {

        return Integer.valueOf( tomMatch53_19.getIntegerValue() );
      }}}}{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) { tom.platform.adt.platformoption.types.PlatformValue  tomMatch53_26= (( tom.platform.adt.platformoption.types.PlatformOption )option).getValue() ;if ( ((( tom.platform.adt.platformoption.types.PlatformValue )tomMatch53_26) instanceof tom.platform.adt.platformoption.types.platformvalue.StringValue) ) {

        return  tomMatch53_26.getStringValue() ;
      }}}}}

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
    PlatformOptionList prerequisites =  tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ;

    // options destdir and output are incompatible
    if(!((String)getOptionValue("destdir")).equals(".")) {
      prerequisites =  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("output", "o", "",  tom.platform.adt.platformoption.types.platformvalue.StringValue.make("") , "file") ,tom_append_list_concPlatformOption(prerequisites, tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() )) ;
      // destdir is not set at its default value -> it has been changed
      // -> we want output at its default value
    }
    if(!((String)getOptionValue("output")).equals("")) {
      prerequisites =  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("destdir", "d", "",  tom.platform.adt.platformoption.types.platformvalue.StringValue.make(".") , "dir") ,tom_append_list_concPlatformOption(prerequisites, tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() )) ;
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
  private void collectOptions(List<OptionOwner> optionOwnerList, List plugins) {
    for (OptionOwner owner : optionOwnerList) {
      PlatformOptionList list = owner.getDeclaredOptionList();
      owner.setOptionManager((OptionManager)this);
      while(!list.isEmptyconcPlatformOption()) {
        PlatformOption option = list.getHeadconcPlatformOption();
        { /* unamed block */{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) { String  tom___name= (( tom.platform.adt.platformoption.types.PlatformOption )option).getName() ; String  tom___altName= (( tom.platform.adt.platformoption.types.PlatformOption )option).getAltName() ;

            setOptionOwnerFromName(tom___name, owner);
            setOptionFromName(tom___name, option);
            if(tom___altName.length() > 0) {
              mapShortNameToName.put(tom___altName,tom___name);
            }
          }}}}

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
      { /* unamed block */{ /* unamed block */if ( (h instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )h) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) { String  tom___altName= (( tom.platform.adt.platformoption.types.PlatformOption )h).getAltName() ; String  tom___attrName= (( tom.platform.adt.platformoption.types.PlatformOption )h).getAttrName() ;


          buffer.append("\t--" +  (( tom.platform.adt.platformoption.types.PlatformOption )h).getName() );
          if(tom___attrName.length() > 0) {
            buffer.append(" <" + tom___attrName+ ">");
          }
          if(tom___altName.length() > 0) {
            buffer.append(" | -" + tom___altName);
          }
          buffer.append(":\t" +  (( tom.platform.adt.platformoption.types.PlatformOption )h).getDescription() );
          buffer.append("\n");
        }}}}

    }
    System.out.println(buffer.toString());
  }

  /**
   * Displays the current version of the TOM compiler.
   */
  public static void displayVersion() {
    System.out.println("\njtom " + Tom.VERSION + "\n" +
        "Copyright (c) 2000-2017, Universite de Lorraine, Inria, Nancy, France.\n");
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
    { /* unamed block */{ /* unamed block */if ( (requiredOptions instanceof tom.platform.adt.platformoption.types.PlatformOptionList) ) {if ( (((( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions) instanceof tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption) || ((( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions) instanceof tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption)) ) {if ( (( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions).isEmptyconcPlatformOption() ) {

        return true;
      }}}}{ /* unamed block */if ( (requiredOptions instanceof tom.platform.adt.platformoption.types.PlatformOptionList) ) {if ( (((( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions) instanceof tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption) || ((( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions) instanceof tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption)) ) {if (!( (( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions).isEmptyconcPlatformOption() )) { tom.platform.adt.platformoption.types.PlatformOption  tomMatch56_8= (( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions).getHeadconcPlatformOption() ;if ( ((( tom.platform.adt.platformoption.types.PlatformOption )tomMatch56_8) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) { String  tom___name= tomMatch56_8.getName() ; tom.platform.adt.platformoption.types.PlatformValue  tom___value= tomMatch56_8.getValue() ;


        PlatformOption option = getOptionFromName(tom___name);
        if(option !=null) {
          PlatformValue localValue = option.getValue();
          if(tom___value!= localValue) {
            TomMessage.error(logger, null, 0, TomMessage.incorrectOptionValue, tom___name,tom___value,getOptionValue(tom___name));
            return false;
          } else {
            return checkOptionDependency( (( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions).getTailconcPlatformOption() );
          }
        } else {
          TomMessage.error(logger, null, 0, TomMessage.incorrectOptionValue, tom___name,tom___value,getOptionValue(tom___name));
          return false;
        }
      }}}}}}


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
            { /* unamed block */{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformValue ) (( tom.platform.adt.platformoption.types.PlatformOption )option).getValue() ) instanceof tom.platform.adt.platformoption.types.platformvalue.BooleanValue) ) {

                // this is a boolean flag: we set to TRUE
                // and no the opposite since -O2 implies -p=true
                setOptionValue(argument, Boolean.TRUE);
              }}}}{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformValue ) (( tom.platform.adt.platformoption.types.PlatformOption )option).getValue() ) instanceof tom.platform.adt.platformoption.types.platformvalue.IntegerValue) ) {


                String t = argumentList[++i];
                setOptionValue(argument, Integer.valueOf(t));
              }}}}{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformValue ) (( tom.platform.adt.platformoption.types.PlatformOption )option).getValue() ) instanceof tom.platform.adt.platformoption.types.platformvalue.StringValue) ) {


                if ( !( argument.equals("import") || argument.equals("I") ) ) {
                  // "import" is handled in the end
                  String t = argumentList[++i];
                  setOptionValue(argument, t);
                }
              }}}}}

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
