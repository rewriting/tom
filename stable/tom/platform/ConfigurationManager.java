
























package tom.platform;



import java.util.*;
import java.util.logging.*;



import tom.platform.adt.platformoption.*;
import tom.platform.adt.platformoption.types.*;
import tom.platform.adt.platformconfig.*;
import tom.platform.adt.platformconfig.types.*;












public class ConfigurationManager {
     private static   tom.platform.adt.platformconfig.types.PluginList  tom_append_list_concPlugin( tom.platform.adt.platformconfig.types.PluginList l1,  tom.platform.adt.platformconfig.types.PluginList  l2) {     if( l1.isEmptyconcPlugin() ) {       return l2;     } else if( l2.isEmptyconcPlugin() ) {       return l1;     } else if(  l1.getTailconcPlugin() .isEmptyconcPlugin() ) {       return  tom.platform.adt.platformconfig.types.pluginlist.ConsconcPlugin.make( l1.getHeadconcPlugin() ,l2) ;     } else {       return  tom.platform.adt.platformconfig.types.pluginlist.ConsconcPlugin.make( l1.getHeadconcPlugin() ,tom_append_list_concPlugin( l1.getTailconcPlugin() ,l2)) ;     }   }   private static   tom.platform.adt.platformconfig.types.PluginList  tom_get_slice_concPlugin( tom.platform.adt.platformconfig.types.PluginList  begin,  tom.platform.adt.platformconfig.types.PluginList  end, tom.platform.adt.platformconfig.types.PluginList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlugin()  ||  (end== tom.platform.adt.platformconfig.types.pluginlist.EmptyconcPlugin.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformconfig.types.pluginlist.ConsconcPlugin.make( begin.getHeadconcPlugin() ,( tom.platform.adt.platformconfig.types.PluginList )tom_get_slice_concPlugin( begin.getTailconcPlugin() ,end,tail)) ;   }      private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }   
  
  
  private String configurationFileName;

  
  private List<Plugin> pluginsList;

  
  private OptionManager optionManager;
  
  private static Logger logger = Logger.getLogger("tom.platform.ConfigurationManager");
  
  public ConfigurationManager(String configurationFileName) {
    this.configurationFileName = configurationFileName;
    this.pluginsList = new ArrayList<Plugin>();
  }
  
  
  public int initialize(String[] commandLine) {    
    PlatformConfig platformConfig = null;
    try {
      
      java.io.InputStream stream = new java.io.FileInputStream(configurationFileName);
      platformConfig = PlatformConfig.fromStream(stream);
      
    } catch(java.io.IOException e) {
      System.out.println("cannot read: " + configurationFileName);
    }

    if(platformConfig == null) {
      PluginPlatformMessage.error(logger, null, 0, 
          PluginPlatformMessage.configFileNotValid, configurationFileName);
      return 1;
    }

   if(createPlugins(platformConfig)==1) {
     return 1;
   }    

   if(createOptionManager(platformConfig) == 1) {     
     return 1;
   }


    return optionManager.initialize(this, commandLine);
  }

  
  public List<Plugin> getPluginsList() {
    return pluginsList;
  }

  
  public OptionManager getOptionManager() {
    return optionManager;
  }
  
  
  private int createPlugins(PlatformConfig configurationNode) {
    List<String> pluginsClassList = extractClassPaths(configurationNode);
    
    if(pluginsClassList.isEmpty()) {
      PluginPlatformMessage.error(logger, null, 0, 
          PluginPlatformMessage.noPluginFound, configurationFileName);
      pluginsList = null;
      return 1;
    }
    
    for (String pluginClass : pluginsClassList) {
      try { 
        Object pluginInstance = Class.forName(pluginClass).newInstance();
        if(pluginInstance instanceof Plugin) {
          pluginsList.add((Plugin)pluginInstance);
        } else {
          PluginPlatformMessage.error(logger, null, 0, 
              PluginPlatformMessage.classNotAPlugin, pluginClass);
          pluginsList = null;
          return 1;
        }
      } catch(ClassNotFoundException cnfe) {
        PluginPlatformMessage.warning(logger, null, 0, 
            PluginPlatformMessage.classNotFound, pluginClass);
        return 1;
      } catch(Exception e) {
        
        e.printStackTrace();
        PluginPlatformMessage.error(logger, null, 0, 
            PluginPlatformMessage.instantiationError, pluginClass);
        pluginsList = null;
        return 1;
      }
    }
    return 0;
  }
  
  private List<String> extractClassPaths(PlatformConfig node) {
    List<String> res = new ArrayList<String>();
    { /* unamed block */{ /* unamed block */if ( (node instanceof tom.platform.adt.platformconfig.types.PlatformConfig) ) {if ( ((( tom.platform.adt.platformconfig.types.PlatformConfig )node) instanceof tom.platform.adt.platformconfig.types.platformconfig.PlatformConfig) ) { tom.platform.adt.platformconfig.types.PluginList  tomMatch836_1= (( tom.platform.adt.platformconfig.types.PlatformConfig )node).getplugins() ;if ( (((( tom.platform.adt.platformconfig.types.PluginList )tomMatch836_1) instanceof tom.platform.adt.platformconfig.types.pluginlist.ConsconcPlugin) || ((( tom.platform.adt.platformconfig.types.PluginList )tomMatch836_1) instanceof tom.platform.adt.platformconfig.types.pluginlist.EmptyconcPlugin)) ) { tom.platform.adt.platformconfig.types.PluginList  tomMatch836_end_8=tomMatch836_1;do {{ /* unamed block */if (!( tomMatch836_end_8.isEmptyconcPlugin() )) { tom.platform.adt.platformconfig.types.Plugin  tomMatch836_13= tomMatch836_end_8.getHeadconcPlugin() ;if ( ((( tom.platform.adt.platformconfig.types.Plugin )tomMatch836_13) instanceof tom.platform.adt.platformconfig.types.plugin.Plugin) ) { String  tom___cp= tomMatch836_13.getclass() ;

         res.add(tom___cp);
         PluginPlatformMessage.finer(logger, null, 0, 
             PluginPlatformMessage.classPathRead, tom___cp);
       }}if ( tomMatch836_end_8.isEmptyconcPlugin() ) {tomMatch836_end_8=tomMatch836_1;} else {tomMatch836_end_8= tomMatch836_end_8.getTailconcPlugin() ;}}} while(!( (tomMatch836_end_8==tomMatch836_1) ));}}}}}

    return res;
  }
 
   

  private int createOptionManager(PlatformConfig node) {
    { /* unamed block */{ /* unamed block */if ( (node instanceof tom.platform.adt.platformconfig.types.PlatformConfig) ) {if ( ((( tom.platform.adt.platformconfig.types.PlatformConfig )node) instanceof tom.platform.adt.platformconfig.types.platformconfig.PlatformConfig) ) { tom.platform.adt.platformconfig.types.OptionManager  tomMatch837_2= (( tom.platform.adt.platformconfig.types.PlatformConfig )node).getoptionmanager() ;if ( ((( tom.platform.adt.platformconfig.types.OptionManager )tomMatch837_2) instanceof tom.platform.adt.platformconfig.types.optionmanager.OptionManager) ) { tom.platform.adt.platformoption.types.PlatformOptionList  tomMatch837_6= tomMatch837_2.getoptions() ; String  tom___omclass= tomMatch837_2.getclass() ;if ( (tomMatch837_6 instanceof tom.platform.adt.platformoption.types.PlatformOptionList) ) {

        try { /* unamed block */
          Object omInstance = Class.forName(tom___omclass).newInstance();
          if(omInstance instanceof OptionManager) { /* unamed block */
            optionManager = (OptionManager)omInstance;
          } else { /* unamed block */
            PluginPlatformMessage.error(logger, null, 0, 
                PluginPlatformMessage.classNotOptionManager, tom___omclass);
            return 1;
          }}
 catch(ClassNotFoundException cnfe) { /* unamed block */
          PluginPlatformMessage.error(logger, null, 0, 
              PluginPlatformMessage.classNotFound, tom___omclass);
          optionManager = null;
          return 1;
        } catch (Exception e) { /* unamed block */
          e.printStackTrace();
          System.out.println(e.getMessage());
          PluginPlatformMessage.error(logger, null, 0, 
              PluginPlatformMessage.instantiationError, tom___omclass);
          optionManager = null;
          return 1;
        }

        PlatformOption optionX =  tom.platform.adt.platformoption.types.platformoption.PluginOption.make("config", "X", "Defines an alternate configuration file",  tom.platform.adt.platformoption.types.platformvalue.StringValue.make(configurationFileName) , "") 
;
        PlatformOptionList globalOptions =  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make(optionX,tom_append_list_concPlatformOption(tomMatch837_6, tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() )) ;
        optionManager.setGlobalOptionList(globalOptions);
        return 0;
      }}}}}}

    return 1;
  }

  
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }



}
