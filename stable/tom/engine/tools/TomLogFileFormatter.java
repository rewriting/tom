/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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

package tom.engine.tools;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class TomLogFileFormatter extends Formatter {
  
  public String format(LogRecord record) {
    String level = record.getLevel().toString();
    String loggerName = record.getLoggerName();
    return loggerName + "(" + level + ")\n\t" + formatMessage(record)+"\n";
  }
  
  public String getHead(Handler h) {
    return "--------------------------------------------------\n"
      + "                   TOM log file\n"
      + "--------------------------------------------------\n";
  }
  
  public String getTail(Handler h) {
    return "\n--------------------------------------------------\n"
      + "                 End of log file\n"
      + "--------------------------------------------------\n";
  }
  
} // class TomLogFileFormatter
