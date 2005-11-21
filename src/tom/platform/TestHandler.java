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
import java.io.BufferedReader;
import java.io.FileReader;
import jtom.TomMessage;
import java.util.*;
import java.util.logging.*;

public class TestHandler extends Handler {


  Vector records;
  Vector mess_attempted;
  int index;
 
  /** Constructor */
  public TestHandler() {
    records = new Vector();
    mess_attempted = new Vector();
    index=0;
  }
  
  public void publish(LogRecord record) {
    if (record instanceof PlatformLogRecord){
      records.add(((PlatformLogRecord)record));
      nonRegressionTest((PlatformLogRecord)record);
    }
    else{records.add(record.getMessage());}
  }


  public void changeTomFile(String fileName){
    try{
      BufferedReader reader = new BufferedReader(new FileReader(new File(fileName+".nrt")));
      String line = reader.readLine();
      while(line != null){
        TomMessage message = (TomMessage)((Class.forName("jtom.TomMessage")).getField(line)).get(null);
        mess_attempted.add(message);
        line  = reader.readLine();
      }
    }catch(java.io.IOException e)
      {publish(new LogRecord(Level.INFO, "No Non Regression Test file : <file-name>+\".nrt\""));}
               catch(java.lang.NoSuchFieldException e)
                 {publish(new LogRecord(Level.SEVERE, e.getMessage()));}
               catch(java.lang.ClassNotFoundException e)
                 {publish(new LogRecord(Level.SEVERE, e.getMessage()));}
               catch(java.lang.IllegalAccessException e)
                 {publish(new LogRecord(Level.SEVERE, e.getMessage()));}
               }
          
  // Part of Handler abstract interface
  public void close() {/*No resources to free */}
  public void flush() {/*No needs to flush any buffered output */}

  public void clear(){
    index=0;
    mess_attempted = new Vector();
    records = new Vector();
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

  public void nonRegressionTest(PlatformLogRecord record){
      //il doit y avoir exactemement les memes messages et dans le meme ordre
    System.out.println("index :"+index);
    System.out.println("mess_obtained :"+record.getMessage());
    System.out.println("mess_attempted :"+mess_attempted.get(index));

    if( ! record.equals(mess_attempted.get(index))){
        System.out.println("Non regression test failed");
        publish(new LogRecord(Level.INFO, "The Non Regression Test has failed"));
      }
      else{
        System.out.println("Non regression test succeed");
          publish(new LogRecord(Level.INFO, "The Non Regression Test has succeeded"));
      }
    index++;

    //TODO : logger les differences pour debugger
  }

} // class TestHandler
