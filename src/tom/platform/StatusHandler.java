/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2005, INRIA
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

import java.io.File;
import java.util.*;
import java.util.logging.*;

public class StatusHandler extends Handler {

  /** Map between a log level and the cardinality of published logrecord */
  private Map levelStats;
  /** Map between a log level and the cardinality of published logrecord */
  private Map inputAlert;
  
  /** Constructor */
  public StatusHandler() {
    levelStats = new HashMap();
    inputAlert = new HashMap();
  }
  
  /** Clear all previous records */
  public void clear() {
    levelStats = new HashMap();
    inputAlert = new HashMap();
  }
  
  public void publish(LogRecord record) {
    Level recordLevel = record.getLevel();
    Integer newStats;
    if(!levelStats.containsKey(recordLevel)) {
      newStats = new Integer(1);
    } else {
      Integer oldStats = (Integer)levelStats.get(recordLevel);
      newStats = new Integer(oldStats.intValue()+1);
    }
    levelStats.put(recordLevel, newStats);
    
    if(record instanceof PlatformLogRecord) {
      PlatformLogRecord plr = (PlatformLogRecord)record;
      String input = plr.getFilePath();
      RuntimeAlert ra;
      if(!inputAlert.containsKey(input)) {
        ra = new RuntimeAlert();
      } else {
        ra = (RuntimeAlert)inputAlert.get(input);
      }
      ra.add(plr);
      inputAlert.put(new File(input).getPath(), ra);
    }
  }

  // Part of Handler abstract interface
  public void close() {/*No resources to free */}
  public void flush() {/*No needs to flush any buffered output */}
  
  public String toString() {
    StringBuffer buffy = new StringBuffer("Status handler :\n");
    Iterator it = levelStats.keySet().iterator();
    while(it.hasNext()) {
      Object level = it.next();
      Object stats = levelStats.get(level);
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
      return ((Integer)levelStats.get(level)).intValue();
    }
  }
  
  public int nbOfErrors() {
    return nbOfLogs(Level.SEVERE);
  }
  
  public int nbOfWarnings() {
    return nbOfLogs(Level.WARNING);
  }
  
  public RuntimeAlert getAlertForInput(String filePath) {
    return (RuntimeAlert)inputAlert.get(new File(filePath).getPath());
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

} // class StatusHandler
