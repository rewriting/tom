
























package tom.engine.tools;



import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;



import tom.engine.Tom;
import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.TomStreamManager;
import tom.engine.adt.tomsignature.types.TomSymbol;
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tomtype.types.TomType;
import tom.platform.OptionManager;
import tom.platform.Plugin;
import tom.platform.StatusHandler;
import tom.platform.adt.platformoption.types.PlatformOptionList;













public abstract class TomGenericPlugin implements Plugin {
  
     private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }   

  public final static String KEY_LAST_GEN_MAPPING = "lastGeneratedMapping";
  public final static String KEY_LAST_READ_LINE = "lastReadLine";
  
  
  private String pluginName;

  
  protected Code term;

  
  protected TomStreamManager streamManager;
  
  
  private StatusHandler statusHandler;

  
  private OptionManager optionManager;
  
  
  protected Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  
  protected StatusHandler getStatusHandler() {
    if(statusHandler == null) {
      findStatusHandler();
    }
    return statusHandler;
  }

  
  public TomGenericPlugin(String name) {
    pluginName = name;
  }
  
  
  public void setArgs(Object[] arg) {
    if (arg[0] instanceof Code && arg[1] instanceof TomStreamManager) {
      term = (Code)arg[0];
      streamManager = (TomStreamManager)arg[1];
    } else {
      TomMessage.error(getLogger(),null,0,TomMessage.invalidPluginArgument,
         pluginName, "[Code, TomStreamManager]", getArgumentArrayString(arg));
    }
  }

  public void setWorkingTerm(Object arg) {
    if(arg instanceof Code) {
      term = (Code)arg;
    } else {
      TomMessage.error(getLogger(),null,0,TomMessage.codeExpected, pluginName);
    }
  }

  
  public abstract void run(Map informationTracker);

  
  public Object[] getArgs() {
    return new Object[]{term, streamManager};
  }
  
  
  public Object getWorkingTerm() {
    return term;
  }

  public TomStreamManager getStreamManager() {
    return streamManager; 
  }

  public void setStreamManager(TomStreamManager streamManager) {
    this.streamManager = streamManager; 
  }

  public SymbolTable getSymbolTable() {
    return streamManager.getSymbolTable(); 
  }

  protected TomSymbol getSymbolFromName(String tomName) {
    return TomBase.getSymbolFromName(tomName, streamManager.getSymbolTable());
  }
  
  protected TomType getTermType(BQTerm t) {
    return  TomBase.getTermType(t, streamManager.getSymbolTable());
  }

  protected TomType getTermType(TomTerm t) {
    return  TomBase.getTermType(t, streamManager.getSymbolTable());
  }
 
  protected TomType getTermType(Expression t) {
    return  TomBase.getTermType(t, streamManager.getSymbolTable());
  }

  protected TomType getUniversalType() {
    return streamManager.getSymbolTable().getUniversalType();
  }
  
  
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
    Handler[] handlers = Logger.getLogger(Tom.LOG_RADICAL).getHandlers();
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
    StringBuilder argString = new StringBuilder("[");
    for(int i=0;i<arg.length;i++) {
      if(arg[i]==null) {
        argString.append("null");
      } else {
        argString.append(arg[i].getClass().getName());
      }
      if (i < arg.length -1) {
        argString.append(",");
      }
    }
    argString.append("]");
    return argString.toString();
  }
  

}
