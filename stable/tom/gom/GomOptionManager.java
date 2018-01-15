























package tom.gom;



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
import tom.platform.adt.platformoption.types.*;



public class GomOptionManager implements OptionManager, OptionOwner {

     private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }   

  
  private PlatformOptionList globalOptions;

  
  private Map<String,OptionOwner> mapNameToOptionOwner;

  
  private Map<String,PlatformOption> mapNameToOption;

  
  private Map<String,String> mapShortNameToName;

  
  private List<String> inputFileList;

  
  public GomOptionManager() {
    mapNameToOptionOwner = new HashMap<String,OptionOwner>();
    mapNameToOption = new HashMap<String,PlatformOption>();
    mapShortNameToName = new HashMap<String,String>();
    inputFileList = new ArrayList<String>();
    globalOptions =  tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ;
  }

  
  public int initialize(
      ConfigurationManager confManager,
      String[] commandLine) {
    List<tom.platform.Plugin> pluginList = confManager.getPluginsList();
    List<OptionOwner> optionOwnerList = new ArrayList<OptionOwner>(pluginList);
    optionOwnerList.add(this);
    collectOptions(optionOwnerList, pluginList);
    this.inputFileList = processArguments(commandLine);
    if(this.inputFileList == null) {
      return 1;
    }
    return checkAllOptionsDepedencies(optionOwnerList);
  }

  
  public void setGlobalOptionList(PlatformOptionList globalOptions) {
    this.globalOptions = globalOptions;
  }

  
  public List<String> getInputToCompileList() {
    return inputFileList;
  }

  
  public void optionChanged(String optionName, Object optionValue) {
    String canonicalOptionName = getCanonicalName(optionName);
    if(canonicalOptionName.equals("verbose")) {
      if( ((Boolean)optionValue).booleanValue() ) {
        Gom.changeLogLevel(Level.INFO);
      }
    } else if(canonicalOptionName.equals("wall")) {
      if( ((Boolean)optionValue).booleanValue() ) {
        Gom.changeLogLevel(Level.WARNING);
      }
    } else if(canonicalOptionName.equals("debug")) {
      if( ((Boolean)optionValue).booleanValue() ) {
        Gom.changeLogLevel(Level.FINE);
      }
    } else if(canonicalOptionName.equals("verbosedebug")) {
      if( ((Boolean)optionValue).booleanValue() ) {
        Gom.changeLogLevel(Level.FINER);
      }
    }
  }

  
  public void setOptionValue(String optionName, Object optionValue) {
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
    
    OptionOwner owner = getOptionOwnerFromName(optionName);
    owner.optionChanged(optionName, optionValue);
  }

  
  public Object getOptionValue(String name) {
    PlatformOption option = getOptionFromName(name);
    if(option != null) {
      { /* unamed block */{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) { tom.platform.adt.platformoption.types.PlatformValue  tomMatch550_1= (( tom.platform.adt.platformoption.types.PlatformOption )option).getValue() ;if ( ((( tom.platform.adt.platformoption.types.PlatformValue )tomMatch550_1) instanceof tom.platform.adt.platformoption.types.platformvalue.BooleanValue) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformBoolean ) tomMatch550_1.getBooleanValue() ) instanceof tom.platform.adt.platformoption.types.platformboolean.True) ) {
 return Boolean.valueOf(true); }}}}}{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) { tom.platform.adt.platformoption.types.PlatformValue  tomMatch550_10= (( tom.platform.adt.platformoption.types.PlatformOption )option).getValue() ;if ( ((( tom.platform.adt.platformoption.types.PlatformValue )tomMatch550_10) instanceof tom.platform.adt.platformoption.types.platformvalue.BooleanValue) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformBoolean ) tomMatch550_10.getBooleanValue() ) instanceof tom.platform.adt.platformoption.types.platformboolean.False) ) {
 return Boolean.valueOf(false); }}}}}{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) { tom.platform.adt.platformoption.types.PlatformValue  tomMatch550_19= (( tom.platform.adt.platformoption.types.PlatformOption )option).getValue() ;if ( ((( tom.platform.adt.platformoption.types.PlatformValue )tomMatch550_19) instanceof tom.platform.adt.platformoption.types.platformvalue.IntegerValue) ) {
 return Integer.valueOf( tomMatch550_19.getIntegerValue() ); }}}}{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) { tom.platform.adt.platformoption.types.PlatformValue  tomMatch550_26= (( tom.platform.adt.platformoption.types.PlatformOption )option).getValue() ;if ( ((( tom.platform.adt.platformoption.types.PlatformValue )tomMatch550_26) instanceof tom.platform.adt.platformoption.types.platformvalue.StringValue) ) {
 return  tomMatch550_26.getStringValue() ; }}}}}

    } else {
      GomMessage.error(getLogger(),null,0,
          GomMessage.optionNotFound,name);
      throw new RuntimeException();
    }
    return null;
  }

  
  public PlatformOptionList getDeclaredOptionList() {
    return globalOptions;
  }

  
  public PlatformOptionList getRequiredOptionList() {
    PlatformOptionList prerequisites =  tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ;
    
    return prerequisites;
  }

  public void setOptionManager(OptionManager om) {}

  
  private void collectOptions(List<OptionOwner> optionOwnerList, List plugins) {
    for (OptionOwner owner : optionOwnerList) {
      PlatformOptionList list = owner.getDeclaredOptionList();
      owner.setOptionManager(this);
      while(!list.isEmptyconcPlatformOption()) {
        PlatformOption option = list.getHeadconcPlatformOption();
        { /* unamed block */{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) { String  tom___name= (( tom.platform.adt.platformoption.types.PlatformOption )option).getName() ; String  tom___altName= (( tom.platform.adt.platformoption.types.PlatformOption )option).getAltName() ;

            setOptionOwnerFromName(tom___name, owner);
            setOptionFromName(tom___name, option);
            if(tom___altName.length() > 0) { /* unamed block */
              mapShortNameToName.put(tom___altName,tom___name);
            }}}}}


        list = list.getTailconcPlatformOption();
      }
    }
  }

  
  private int checkAllOptionsDepedencies(List optionOwnerList) {
    Iterator owners = optionOwnerList.iterator();
    while(owners.hasNext()) {
      OptionOwner plugin = (OptionOwner)owners.next();
      if(!checkOptionDependency(plugin.getRequiredOptionList())) {
        GomMessage.error(getLogger(),null,0,
            GomMessage.prerequisitesIssue,
            plugin.getClass().getName());
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
    PlatformOption option = mapNameToOption.get(getCanonicalName(name));
    if (null == option) {
      GomMessage.warning(getLogger(),null,0,
          GomMessage.optionNotFound,
          getCanonicalName(name));
    }
    return option;
  }

  private PlatformOption setOptionFromName(String name, PlatformOption option) {
    return mapNameToOption.put(getCanonicalName(name),option);
  }

  private OptionOwner getOptionOwnerFromName(String name) {
    OptionOwner plugin = mapNameToOptionOwner.get(getCanonicalName(name));
    if (null == plugin) {
      GomMessage.warning(getLogger(),null,0,
          GomMessage.optionNotFound,
          getCanonicalName(name));
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
      GomMessage.finer(getLogger(),null,0,
          GomMessage.setValue,
          new Object[]{name,value,replaced});
    } else {
      throw new RuntimeException();
    }
  }

  
  private void displayHelp() {
    String beginning = "usage: gom [options] file [... file_n]"
      + "\noptions:\n";
    StringBuilder buffer = new StringBuilder(beginning);
    TreeMap<String,PlatformOption> treeMap =
      new TreeMap<String,PlatformOption>(mapNameToOption);
    for (Map.Entry<String,PlatformOption> entry : treeMap.entrySet()) {
      PlatformOption h = entry.getValue();
      { /* unamed block */{ /* unamed block */if ( (h instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )h) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) { String  tom___altName= (( tom.platform.adt.platformoption.types.PlatformOption )h).getAltName() ; String  tom___attrName= (( tom.platform.adt.platformoption.types.PlatformOption )h).getAttrName() ;

          buffer.append("\t--" +  (( tom.platform.adt.platformoption.types.PlatformOption )h).getName() );
          if(tom___attrName.length() > 0) { /* unamed block */
            buffer.append(" <" + tom___attrName + ">");
          }
          if(tom___altName.length() > 0) { /* unamed block */
            buffer.append(" | -" + tom___altName);
          }
          buffer.append(":\t" +  (( tom.platform.adt.platformoption.types.PlatformOption )h).getDescription() );
          buffer.append("\n");
        }}}}

    }

    System.out.println(buffer.toString());
  }

  
  public void displayVersion() {
    System.out.println("Gom " + tom.engine.Tom.VERSION + "\n\n"
                       + "Copyright (c) 2000-2017, Universite de Lorraine, Inria, Nancy, France.\n");
  }

  
  private boolean checkOptionDependency(PlatformOptionList requiredOptions) {
    { /* unamed block */{ /* unamed block */if ( (requiredOptions instanceof tom.platform.adt.platformoption.types.PlatformOptionList) ) {if ( (((( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions) instanceof tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption) || ((( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions) instanceof tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption)) ) {if ( (( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions).isEmptyconcPlatformOption() ) {

        return true;
      }}}}{ /* unamed block */if ( (requiredOptions instanceof tom.platform.adt.platformoption.types.PlatformOptionList) ) {if ( (((( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions) instanceof tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption) || ((( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions) instanceof tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption)) ) {if (!( (( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions).isEmptyconcPlatformOption() )) { tom.platform.adt.platformoption.types.PlatformOption  tomMatch553_8= (( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions).getHeadconcPlatformOption() ;if ( ((( tom.platform.adt.platformoption.types.PlatformOption )tomMatch553_8) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) { String  tom___name= tomMatch553_8.getName() ; tom.platform.adt.platformoption.types.PlatformValue  tom___value= tomMatch553_8.getValue() ;


        PlatformOption option = getOptionFromName(tom___name);
        if(option !=null) { /* unamed block */
          PlatformValue localValue = option.getValue();
          if(tom___value != localValue) { /* unamed block */
            GomMessage.error(getLogger(),null,0,
                GomMessage.incorrectOptionValue,
                new Object[]{ /* unamed block */tom___name,tom___value,getOptionValue(tom___name)});
            return false;
          } else { /* unamed block */
            return checkOptionDependency( (( tom.platform.adt.platformoption.types.PlatformOptionList )requiredOptions).getTailconcPlatformOption() );
          }}
 else { /* unamed block */
          GomMessage.error(getLogger(),null,0,
              GomMessage.incorrectOptionValue,
              new Object[]{ /* unamed block */tom___name,tom___value,getOptionValue(tom___name)});
          return false;
        }}}}}}}


    return false;
  }

  
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

    
  private List<String> processArguments(String[] argumentList) {
    List<String> inputFiles = new ArrayList<String>();
    StringBuilder imports = new StringBuilder();
    boolean outputEncountered = false;
    boolean destdirEncountered = false;
    int i = 0;
    String argument="";
    try {
      for(; i < argumentList.length; i++) {
        argument = argumentList[i];
        GomMessage.finer(getLogger(), null, 0, GomMessage.processingArgument,
            i, argument);
        if(!argument.startsWith("-") || (argument.equals("-")) ) {
          
          inputFiles.add(argument);
        } else { 
          argument = argument.substring(1); 
          if(argument.startsWith("-")) { 
            argument = argument.substring(1); 
          }

          if(argument.equals("X")) {
            
            
            
            i++;
            continue;
          }

          if(argument.equals("help") || argument.equals("h")) {
            displayHelp();
            return null;
          }
          if(argument.equals("version") || argument.equals("V")) {
            displayVersion();
            return null;
          }
          if (argument.equals("import") || argument.equals("I")) {
            i++;
            imports.append(argumentList[i] + ":");
          }
          if (argument.equals("destdir") || argument.equals("d")) {
            if (destdirEncountered) {
              GomMessage.error(getLogger(),null,0,
                  GomMessage.destdirTwice);
              return null;
            } else {
              destdirEncountered = true;
            }
          }

          OptionOwner plugin = getOptionOwnerFromName(argument);
          PlatformOption option = getOptionFromName(argument);

          if (option == null || plugin == null) {
            GomMessage.error(getLogger(),null,0,
                GomMessage.invalidOption,
                argument);
            displayHelp();
            return null;
          } else {
            { /* unamed block */{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformValue ) (( tom.platform.adt.platformoption.types.PlatformOption )option).getValue() ) instanceof tom.platform.adt.platformoption.types.platformvalue.BooleanValue) ) {

                setOptionValue(argument, Boolean.TRUE);
              }}}}{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformValue ) (( tom.platform.adt.platformoption.types.PlatformOption )option).getValue() ) instanceof tom.platform.adt.platformoption.types.platformvalue.IntegerValue) ) {


                String t = argumentList[++i];
                setOptionValue(argument, Integer.valueOf(t));
              }}}}{ /* unamed block */if ( (option instanceof tom.platform.adt.platformoption.types.PlatformOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformOption )option) instanceof tom.platform.adt.platformoption.types.platformoption.PluginOption) ) {if ( ((( tom.platform.adt.platformoption.types.PlatformValue ) (( tom.platform.adt.platformoption.types.PlatformOption )option).getValue() ) instanceof tom.platform.adt.platformoption.types.platformvalue.StringValue) ) {


                if ( !( argument.equals("import") || argument.equals("I") ) ) { /* unamed block */
                  
                  String t = argumentList[++i];
                  setOptionValue(argument, t);
                }}}}}}


          }
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.incompleteOption,
          argument);
      displayHelp();
      return null;
    }

    setOptionValue("import",imports.toString());

    if (inputFiles.isEmpty()) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.noFileToCompile);
      displayHelp();
      return null;
    }
    return inputFiles;
  }
}
