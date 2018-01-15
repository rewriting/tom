
























package tom.platform;



import java.util.*;
import java.util.logging.*;
import aterm.*;
import aterm.pure.*;
import tom.library.adt.tnode.*;











public class PluginPlatform extends PluginPlatformBase implements Runnable {

  public final static String FORMATTER =
    "tom.platform.error.formatter";
  public final static String LOG_FILE =
    "tom.platform.error.logfile";
 
  
  

  
  private List<Plugin> pluginsList;

  
  private StatusHandler statusHandler;

  
  private TestHandler testHandler;

  
  private List<String> inputToCompileList;

  
  private List<Object> lastGeneratedObjects;

  
  private String loggerRadical;

  
  private Map<String,String> informationTracker;

  
  private int runResult;

  
  private static String currentFileName = null; 

  
  
  
  public PluginPlatform(List<Plugin> pluginsList, String loggerRadical, List<String> inputToCompileList) {
    super(loggerRadical);  
    statusHandler = new StatusHandler();
 
    this.loggerRadical = loggerRadical;
    Logger logger = Logger.getLogger(loggerRadical);
    logger.addHandler(this.statusHandler);
    
    this.pluginsList = pluginsList;
    this.inputToCompileList = inputToCompileList;
    
    this.informationTracker = new HashMap<String,String>();
    runResult = 0; 
    String logfile = System.getProperty(LOG_FILE);
    if (logfile != null) {
      try {
        logger.addHandler(new FileHandler(logfile));
      } catch (java.io.IOException e) {
        PluginPlatformMessage.error(getLogger(), null, 0, PluginPlatformMessage.logfileInvalid);
      }
    }
    String formatter = System.getProperty(FORMATTER);
    if (formatter!=null) {
      try {
        for (Handler handler: logger.getHandlers()) {
          handler.setFormatter((java.util.logging.Formatter) Class.forName(formatter).newInstance());
        }
      } catch(ClassNotFoundException e) {
        PluginPlatformMessage.error(getLogger(), null, 0, PluginPlatformMessage.formatterNotFound);
      } catch(java.lang.Exception e) {
        PluginPlatformMessage.error(getLogger(), null, 0, PluginPlatformMessage.formatterInvalid);
      }
    }
  }

  public PluginPlatform(List<Plugin> pluginsList, String loggerRadical, List<String> inputToCompileList, Map<String,String> informationTracker) {
    this(pluginsList, loggerRadical, inputToCompileList);
    this.informationTracker = informationTracker;
  }



  
  public void run() {
    try {
      boolean globalSuccess = true;
      int globalNbOfErrors = 0;
      int globalNbOfWarnings = 0;
      
      
      
      lastGeneratedObjects = new ArrayList<Object>();
      
      for(int i=0; i < inputToCompileList.size(); i++) {
        String input = inputToCompileList.get(i);
        Object[] pluginArg = new Object[]{ input };
        String initArgument = input;
        boolean success = true;
        statusHandler.clear();
        if(this.testHandler!=null) {
          Logger.getLogger(loggerRadical).removeHandler(this.testHandler);
        }
        testHandler = new TestHandler(input);
        if(!testHandler.hasError()) {
          Logger.getLogger(loggerRadical).addHandler(this.testHandler);
        }
        PluginPlatformMessage.finer(getLogger(), null, 0, 
            PluginPlatformMessage.nowCompiling, input);
        
        Iterator<Plugin> it = pluginsList.iterator();
        while(it.hasNext()) {
          
          Handler[] handlers = Logger.getLogger(loggerRadical).getHandlers();
          boolean foundHdl = false;
          for (int k = 0; k < handlers.length ; k++) {
            if (handlers[k].equals(statusHandler)) {
              foundHdl = true;
              break;
            }
          }
          if (!foundHdl) {
            Logger.getLogger(loggerRadical).addHandler(this.statusHandler);  
          }
          Plugin plugin = it.next();
          currentFileName = input;
          plugin.setArgs(pluginArg);
          if(statusHandler.hasError()) {
            PluginPlatformMessage.info(getLogger(), null, 0, PluginPlatformMessage.settingArgError);
            success = false;globalSuccess
 = false;
            globalNbOfErrors += statusHandler.nbOfErrors();
            globalNbOfWarnings += statusHandler.nbOfWarnings();
            break;
          }
          plugin.run(informationTracker);
          
          if(statusHandler.hasError()) {
            PluginPlatformMessage.info(getLogger(), null, 0, 
                PluginPlatformMessage.processingError,
                plugin.getClass().getName(), initArgument);
            success = false;
            globalSuccess = false;
            globalNbOfErrors += statusHandler.nbOfErrors();
            globalNbOfWarnings += statusHandler.nbOfWarnings();
            break;
          }
          pluginArg = plugin.getArgs();
        }
        if(success) {
          
          
          lastGeneratedObjects.add(pluginArg[0]);
          globalNbOfWarnings += statusHandler.nbOfWarnings();
        }
      }
      if(!globalSuccess) {
        PluginPlatformMessage.info(getLogger(), null, 0, 
            PluginPlatformMessage.runErrorMessage,
            Integer.valueOf(globalNbOfErrors));
        
        this.runResult = 1;
        
      } 
      else {
        if (globalNbOfWarnings>0) {
          PluginPlatformMessage.info(getLogger(), null, 0, 
              PluginPlatformMessage.runWarningMessage,
              Integer.valueOf(globalNbOfWarnings));
        }
        this.runResult = 0;
        
      }
    } catch(PlatformException e) {
      PluginPlatformMessage.error(getLogger(), null, 0, 
          PluginPlatformMessage.platformStopped);
      
      this.runResult = 1;
    }
    
  }

  
  public StatusHandler getStatusHandler() {
    return statusHandler;
  }

  
  public TestHandler getTestHandler() {
    return testHandler;
  }

  
  public List<Object> getLastGeneratedObjects() {
    return lastGeneratedObjects;
  }

  public RuntimeAlert getAlertForInput(String filePath) {
    return statusHandler.getAlertForInput(filePath);
  }

  
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  public static String getCurrentFileName() {
    return currentFileName;
  }

  public int getRunResult() {
    return this.runResult;
  }
}
