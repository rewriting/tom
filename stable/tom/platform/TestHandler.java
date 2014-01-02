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
 * Emilie Balland  e-mail: Emilie.Balland@loria.fr
 *
 **/

package tom.platform;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.logging.*;

public class TestHandler extends Handler {

  List<LogRecord> records;
  Vector<Object> mess_attempted;
  int index;

  /** Constructor */
  public TestHandler(String fileName) {
    records = new ArrayList<LogRecord>();
    mess_attempted = new Vector<Object>();
    index = 0;
    try {
      BufferedReader reader = new BufferedReader(new FileReader(new File(fileName+".nrt")));
      String line = reader.readLine();
      while (line != null) {
        // On specifie sur chaque ligne les erreurs attendues
        // par le nom de la classe de messages (TomMessage,PluginPlatformMessage,...) suivi de " : ",
        // puis par le nom du champ correspondant au message attendu.
        String className = null;
        String fieldName = null;
        try {
          className = line.substring(0,line.indexOf(" : "));
          fieldName = line.substring(line.indexOf(" : ")+3);
          mess_attempted.add( (Class.forName(className)).getField(fieldName).get(null));
        } catch (java.lang.StringIndexOutOfBoundsException e) {
          publish(new LogRecord(Level.SEVERE, "Illformed .nrt line: "+line));
        }
        line  = reader.readLine();
      }
    } catch (java.io.IOException e) {
      publish(new LogRecord(Level.SEVERE, "No Non Regression Test file : <file-name>+\".nrt\""));
    } catch (java.lang.NoSuchFieldException e) {
      publish(new LogRecord(Level.SEVERE, e.getMessage()));
    } catch (java.lang.ClassNotFoundException e) {
      publish(new LogRecord(Level.SEVERE, e.getMessage()));
    } catch (java.lang.IllegalAccessException e) {
      publish(new LogRecord(Level.SEVERE, e.getMessage()));
    }
  }

  public boolean hasLog(Level level) {
    for (LogRecord record : records) {
      if (record.getLevel().equals(level)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasError() {
    return hasLog(Level.SEVERE);
  }

  public boolean hasWarning() {
    return hasLog(Level.WARNING);
  }

  public int nbOfLogs(Level level) {
    int nb_rec =0;
    for (LogRecord record : records) {
      if(record.getLevel().equals(level)) {
        nb_rec ++;
      }
    }
    return nb_rec;
  }

  public int nbOfErrors() {
    return nbOfLogs(Level.SEVERE);
  }

  public int nbOfWarnings() {
    return nbOfLogs(Level.WARNING);
  }

  public void publish(LogRecord record) {
    records.add(record);
    if (record instanceof PlatformLogRecord) {
      nonRegressionTest((PlatformLogRecord)record);
    }
  }


  // Part of Handler abstract interface
  public void close() {/*No resources to free */}
  public void flush() {/*No needs to flush any buffered output */}

  public void clear() {
    index=0;
    mess_attempted = new Vector<Object>();
    records = new ArrayList<LogRecord>();
  }

  public String toString() {
    return records.toString();
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

  /**
   * This method tests if this current record was attempted by the test file
   * by comparing the content of the message, the line, the filepath and paramaters.
   * Currently, only the type of message is tested. It is insufficient because
   * most of the messages are encapsulated in a message of type DetailedMessage
   *
   * @param record a given LogRecord
   */
  public void nonRegressionTest(PlatformLogRecord record) {
    /* The nonRegressionTest method verify that all structured log messages (of
     * type PlatformLogRecord) were attempted in the same order and with the
     * same parameters. The attempted messages are contained in a file with
     * suffix .nrt. Currently, only the type of messages are compared.
     */
    PlatformMessage message = record.getPlatformMessage();
    //TODO : comparing this elements
    int line = record.getLine();
    String filePath = record.getFilePath();
    Object[] parameters = record.getParameters();
    System.out.println("Message attempted : "+mess_attempted.get(index));
    if( ! message.equals(mess_attempted.get(index))){
      System.out.println("Message obtained : "+record.getMessage()+"\n->Non regression test failed");
      publish(new LogRecord(Level.INFO, "The Non Regression Test has failed"));
    }
    else{
      System.out.println("Message obtained : "+record.getMessage()+"\n->Non regression test succeed");
      publish(new LogRecord(Level.INFO,record.getMessage()+" ->The Non Regression Test has succeeded"));
    }
    index++;

    //TODO : at the end of the file analysis, verify that all attempted messages have been checked. In cases of conflicts, log the differences between the two traces.
  }

}
