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
 * Emilie Balland  e-mail: Emilie.Balland@inria.fr
 *
 **/

package tom.platform;

import java.util.logging.*;

public class RegressFormatter extends Formatter {
  
  public String format(LogRecord record) {
    Level level = record.getLevel();
    if(record instanceof PlatformLogRecord) {
      PlatformLogRecord plr = (PlatformLogRecord) record;
      if(level.equals(Level.SEVERE)) { 
        // we are only interested into SEVERE logs
        // TODO: select the information necessary for regression tests
        // for the moment, we only test the name of the message
        return "REGRESS: "+plr.getPlatformMessage().getMessageName()+"\n";
      }
    }
    return "";
  }

}
