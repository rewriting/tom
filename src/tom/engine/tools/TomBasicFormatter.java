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

package jtom.tools;

import java.util.logging.*;

public class TomBasicFormatter extends Formatter {

  public String format(LogRecord record) {
    Level level = record.getLevel();
    String levelString;

    if( level.equals(Level.SEVERE) ) { 
      // SEVERE logs are labelled as errors
      levelString = "ERROR:";
    } else if( level.equals(Level.INFO) ) { 
      // INFO logs are verbose messages : no label
      levelString = "";
    } else if( level.equals(Level.OFF) ) { 
      // OFF logs are used to print messages no matter what : no label
      levelString = "";
    } else {
      // for WARNING, CONFIG, FINE, FINER, FINEST or custom levels, use the Level's name
      levelString = level.toString() + ":";
    }

    return levelString + formatMessage(record) + "\n";
  }

}
