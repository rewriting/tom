
























package tom.platform;



import java.util.logging.Logger;








public abstract class PluginPlatformBase {
  
  
  private static StatusHandler globalStatusHandler = new StatusHandler();
  
  protected PluginPlatformBase(String loggerRadical) {
    Logger.getLogger(loggerRadical).addHandler(globalStatusHandler);
  }
  
  public StatusHandler getGlobalStatusHandler() {
    return globalStatusHandler;
  }
  
  public void clearGlobalStatusHandler() {
    globalStatusHandler.clear();
  }
}
