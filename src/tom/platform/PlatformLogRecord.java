/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
 * Emilie Balland         e-mail: Emilie.Balland@loria.fr
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 * Antoine Reilles        e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.platform;

import java.util.logging.*;

public class PlatformLogRecord extends LogRecord {
  private static final long serialVersionUID = 1L;

  private int line;
  private String filePath;
  private PlatformMessage message;

  public PlatformLogRecord(Level level, PlatformMessage message, Object[] detail, String filePath, int line) {
    super(level, message.getMessage());
    if(detail==null) {
      super.setParameters(new Object[]{});
    } else {
      super.setParameters(detail);
    }
    this.filePath =filePath;
    this.line = line;
    this.message = message;
  }

  public int getLine() {
    return line;
  }

  public String getFilePath() {
    return filePath;
  }

  public PlatformMessage getPlatformMessage() {
    return message;
  }

}
