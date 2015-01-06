/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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

package tom.platform;

import java.util.*;
import java.util.logging.*;
import aterm.*;
import aterm.pure.*;
import tom.library.adt.tnode.*;

/**
 * The PluginPlatform manages plugins defined in an xml configuration file.
 * (which plugins are used and how they are ordered) with the intermediate
 * of a ConfigurationManager objet
 * Its main role is to run the plugins in the specified order and make some
 * error management.
 *
 */
public class PluginPlatform extends PluginPlatformBase implements Runnable {

  public final static String FORMATTER =
    "tom.platform.error.formatter";
  public final static String LOG_FILE =
    "tom.platform.error.logfile";
 
  /** Used to analyse xml configuration file */
  %include{ adt/tnode/TNode.tom }

  /** The List of reference to plugins. */
  private List<Plugin> pluginsList;

  /** The status handler */
  private StatusHandler statusHandler;

  /** The test handler */
  private TestHandler testHandler;

  /** List of input arg */
  private List<String> inputToCompileList;

  /** List of generated object cleared before each run */
  private List<Object> lastGeneratedObjects;

  /** Radical of the logger */
  private String loggerRadical;

  /** informationTracker */
  private Map<String,String> informationTracker;

  /** Result of the run() method which is collected with all the run() results */
  private int runResult;

  /**
   * The current file name to process - this is used in the status handler 
   * in order to have the file name when it was not passed at logging 
   * 
   * (generally, this information mostly serves for the eclipse plugin) 
   */
  private static String currentFileName = null; 

  /** Class Pluginplatform constructor */
  //public PluginPlatform(ConfigurationManager confManager, String loggerRadical) {
  //public PluginPlatform(List<Plugin> pluginsList, String loggerRadical, List<String> inputToCompileList, Map<,String> informationTracker) {
  public PluginPlatform(List<Plugin> pluginsList, String loggerRadical, List<String> inputToCompileList) {
    super(loggerRadical);  
    statusHandler = new StatusHandler();
 
    this.loggerRadical = loggerRadical;
    Logger logger = Logger.getLogger(loggerRadical);
    logger.addHandler(this.statusHandler);
    //pluginsList = confManager.getPluginsList();
    this.pluginsList = pluginsList;
    this.inputToCompileList = inputToCompileList;
    //inputToCompileList = confManager.getOptionManager().getInputToCompileList();
    this.informationTracker = new HashMap<String,String>();
    runResult = 0; //init
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

//modifier les commentaires

  /**
   * The main method which runs the PluginPlatform.
   */
  public void run() {
    try {
      boolean globalSuccess = true;
      int globalNbOfErrors = 0;
      int globalNbOfWarnings = 0;
      //increase a global counter at the beginning of each run()
      ///PluginPlatformFactory.getInstance().increaseThreadsCounter();
      // intialize run instances
      lastGeneratedObjects = new ArrayList<Object>();
      // for each input we call the sequence of plug-ins
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
        // runs the plugins
        Iterator<Plugin> it = pluginsList.iterator();
        while(it.hasNext()) {
          /*
           * very strangely, the object pointed by statusHandler changes, and therefore 
           * it is no longer associated to the logger; therefore, we add it again as a handler  
           */
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
            success = false;`
            globalSuccess = false;
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
          // save the first element of last plugin getArg response
          // this shall correspond to a generated file name
          lastGeneratedObjects.add(pluginArg[0]);
          globalNbOfWarnings += statusHandler.nbOfWarnings();
        }
      }
      if(!globalSuccess) {
        PluginPlatformMessage.info(getLogger(), null, 0, 
            PluginPlatformMessage.runErrorMessage,
            Integer.valueOf(globalNbOfErrors));
        //return 1;
        this.runResult = 1;
        ///PluginPlatformFactory.getInstance().decreaseThreadsCounter();
      } /*else if(globalNbOfWarnings>0) {
        PluginPlatformMessage.info(getLogger(), null, 0, 
            PluginPlatformMessage.runWarningMessage,
            Integer.valueOf(globalNbOfWarnings));
        //return 0;
        this.runResult = 0;
        PluginPlatformFactory.getInstance().decreaseThreadsCounter();
        System.out.println("end run PluginPlatform succeeded");
      } */
      else {
        if (globalNbOfWarnings>0) {
          PluginPlatformMessage.info(getLogger(), null, 0, 
              PluginPlatformMessage.runWarningMessage,
              Integer.valueOf(globalNbOfWarnings));
        }
        this.runResult = 0;
        //PluginPlatformFactory.getInstance().decreaseThreadsCounter();
      }
    } catch(PlatformException e) {
      PluginPlatformMessage.error(getLogger(), null, 0, 
          PluginPlatformMessage.platformStopped);
      //return 1;
      this.runResult = 1;
    }
    //return 0;
  }

  /**
   * An accessor method
   * @return the status handler.
   */
  public StatusHandler getStatusHandler() {
    return statusHandler;
  }

  /**
   * An accessor method
   * @return the test handler.
   */
  public TestHandler getTestHandler() {
    return testHandler;
  }

  /** return the list of last generated objects */
  public List<Object> getLastGeneratedObjects() {
    return lastGeneratedObjects;
  }

  public RuntimeAlert getAlertForInput(String filePath) {
    return statusHandler.getAlertForInput(filePath);
  }

  /** logger accessor in case of logging needs*/
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
