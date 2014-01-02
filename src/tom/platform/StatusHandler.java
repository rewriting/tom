/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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

public class StatusHandler extends Handler {

  /** Map between a log level and the cardinality of published logrecord */
  private Map<Level,Integer> levelStats;
  /** Map between a log level and the cardinality of published logrecord */
  private Map<String,RuntimeAlert> inputAlert;

  /** Constructor */
  //public StatusHandler() {
  public StatusHandler() {
    levelStats = new HashMap<Level,Integer>();
    inputAlert = new HashMap<String,RuntimeAlert>();
  }

  /** Clear all previous records */
  public void clear() {
    levelStats.clear();
    inputAlert.clear();
  }

  public void publish(LogRecord record) {
    final LogRecord recordCopy = record; 
    Level recordLevel = record.getLevel();
    Integer newStats;
    if(!levelStats.containsKey(recordLevel)) {
      newStats = Integer.valueOf(1);
    } else {
      Integer oldStats = levelStats.get(recordLevel);
      newStats = Integer.valueOf(oldStats.intValue()+1);
    }
    levelStats.put(recordLevel, newStats);
    if(!(record instanceof PlatformLogRecord)) {
      if( recordLevel != Level.SEVERE ) { return; }
      record = new PlatformLogRecord(recordLevel, new PlatformMessage(){
          public String getMessage(){
            return recordCopy.getMessage();  
          }

          public String getMessageName() {
            return "empty_name";
          }

          public void setMessageName(String name) {
          }
      }, record.getParameters(), PluginPlatform.getCurrentFileName(), 1);
    }
    PlatformLogRecord plr = (PlatformLogRecord)record;
    String input = plr.getFilePath();
    RuntimeAlert ra;
    if(!inputAlert.containsKey(input)) {
      ra = new RuntimeAlert();
    } else {
      ra = inputAlert.get(input);
    }
    ra.add(plr);
    inputAlert.put(input, ra);    
  }

  // Part of Handler abstract interface
  public void close() {/*No resources to free */}
  public void flush() {/*No needs to flush any buffered output */}

  public String toString() {
    StringBuilder buffy = new StringBuilder("Status handler :\n");
    for (Map.Entry<Level,Integer> entry : levelStats.entrySet()) {
      Object level = entry.getKey();
      Object stats = entry.getValue();
      buffy.append("\t" + stats + " record(s) of level " + level + "\n");
    }
    return buffy.toString();
  }

  public boolean hasLog(Level level) {
    return levelStats.containsKey(level);
  }

  public boolean hasError() {
    return hasLog(Level.SEVERE);
  }

  public boolean hasWarning() {
    return hasLog(Level.WARNING);
  }

  public int nbOfLogs(Level level) {
    if(!hasLog(level)) {
      return 0;
    } else {
      return (levelStats.get(level)).intValue();
    }
  }

  public int nbOfErrors() {
    return nbOfLogs(Level.SEVERE);
  }

  public int nbOfWarnings() {
    return nbOfLogs(Level.WARNING);
  }

  public RuntimeAlert getAlertForInput(String filePath) {
    return inputAlert.get(filePath);
  }
  
  public Map<String,RuntimeAlert> getAlertMap() {
    return inputAlert;
  }

  /**
   * This Handler keeps track of all LogRecords,  therefore this method always
   * returns true. Please note that since we receive the LogRecords from a
   * Logger, we actually only keep track of logs with a Level equal or higher
   * than the Logger's.
   *
   * @param record a given LogRecord
   * @return true
   */
  public boolean isLoggable(LogRecord record) {
    return true;
  }

}
