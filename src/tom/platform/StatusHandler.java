/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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

  private Map levelStats;

  public StatusHandler() {
    levelStats = new HashMap();
  }

  public void publish(LogRecord record) {
    Level recordLevel = record.getLevel();

    if( !levelStats.containsKey(recordLevel) ) { // first time we meet a LogRecord with this Level
      levelStats.put( recordLevel, new Integer(1) );
    } else {
      Integer oldStats = (Integer)levelStats.get(recordLevel);
      Integer newStats = new Integer( oldStats.intValue() + 1 );
      levelStats.put( recordLevel, newStats );
    }
  }

  public String toString() {
    StringBuffer buffy = new StringBuffer("Status handler :\n");
    Iterator it = levelStats.keySet().iterator();

    while( it.hasNext() ) {
      Object level = it.next();
      Object stats = levelStats.get(level);

      buffy.append("\t" + stats + " records of level " + level + "\n");
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
    if( !hasLog(level) ) {
      return 0;
    } else {
      return ( (Integer)levelStats.get(level) ).intValue();
    }
  }

  public int nbOfErrors() {
    return nbOfLogs(Level.SEVERE);
  }

  public int nbOfWarnings() {
    return nbOfLogs(Level.WARNING);
  }

  public void close() {
  }

  public void flush() {
  }
 
  public Filter getFilter() {
    return null;
  }
  public void setFilter(Filter newFilter) {
  }

  public java.util.logging.Formatter getFormatter() {
    return null;
  }
  public void setFormatter(java.util.logging.Formatter newFormatter) {
  }

  public Level getLevel() {
    return Level.ALL;
  }
  public void setLevel(Level newLevel) {
  }

  /**
   * This Handler keeps track of all LogRecords,
   * therefore this method always returns true.
   * Please note that since we receive the LogRecords
   * from a Logger, we actually only keep track of logs
   * with a Level equal or higher than the Logger's.
   *
   * @param record a given LogRecord
   * @return true
   */
  public boolean isLoggable(LogRecord record) {
    return true;
  }

}
