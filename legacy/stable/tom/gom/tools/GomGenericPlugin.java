






















package tom.gom.tools;



import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.Map;



import tom.platform.OptionManager;
import tom.platform.Plugin;
import tom.platform.StatusHandler;
import tom.platform.adt.platformoption.types.PlatformOptionList;



import tom.gom.Gom;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;



public abstract class GomGenericPlugin implements Plugin {

  public GomGenericPlugin(String name) {
    pluginName = name;
    
    gomEnvironment = new GomEnvironment();
  }

     private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }   

  
  private String pluginName;

  
  private StatusHandler statusHandler;

  
  private OptionManager optionManager;

  public final static String KEY_LAST_GEN_MAPPING = "lastGeneratedMapping";
  public final static String KEY_LAST_READ_LINE = "lastReadLine";

  
  protected GomEnvironment gomEnvironment;

  
  protected Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  
  protected StatusHandler getStatusHandler() {
    if(statusHandler == null) {
      findStatusHandler();
    }
    return statusHandler;
  }

  public GomEnvironment getGomEnvironment() {
    return gomEnvironment;
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  public GomStreamManager getStreamManager() {
    return gomEnvironment.getStreamManager();
  }

  public void setStreamManager(GomStreamManager m) {
    gomEnvironment.setStreamManager(m);
  }

  
  public abstract void setArgs(Object[] arg);

  
  public abstract void run(Map<String,String> informationTracker);

  
  public abstract Object[] getArgs();

  
  public void setOptionManager(OptionManager optionManager) {
    this.optionManager = optionManager;
  }

  
  public PlatformOptionList getDeclaredOptionList() {
    return  tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ;
  }

  
  public PlatformOptionList getRequiredOptionList() {
    return  tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ;
  }

  
  public void optionChanged(String optionName, Object optionValue) {
  }

  private void findStatusHandler() {
    Handler[] handlers = Logger.getLogger(Gom.LOGRADICAL).getHandlers();
    for(int i=0;i<handlers.length;i++) {
      if(handlers[i] instanceof StatusHandler) {
        statusHandler = (StatusHandler)handlers[i];
        break;
      }
    }
  }

  public OptionManager getOptionManager() {
    return optionManager;
  }

  public void setOptionValue(String name, Object value) {
    optionManager.setOptionValue(name, value);
  }

  protected Object getOptionValue(String name) {
    return optionManager.getOptionValue(name);
  }

  
  public boolean getOptionBooleanValue(String optionName) {
    return ((Boolean)getOptionValue(optionName)).booleanValue();
  }

  
  public int getOptionIntegerValue(String optionName) {
    return ((Integer)getOptionValue(optionName)).intValue();
  }

  
  public String getOptionStringValue(String optionName) {
    return (String) getOptionValue(optionName);
  }

  public String getArgumentArrayString(Object[] arg) {
    String argString = "[";
    for(int i=0;i<arg.length;i++) {
      if (arg[i] == null) { continue; }
      argString += arg[i].getClass().getName();
      if(i < arg.length-1) {
        argString += ",";
      }
    }
    return argString+"]";
  }
}
