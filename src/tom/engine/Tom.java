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

package jtom;

import java.util.logging.*;

/**
 *
 */
public class Tom {

  /**
   * The current version of the TOM compiler. 
   */
  public final static String VERSION = "3.0alpha";

  protected static Logger rootLogger;

  public static int exec(String[] args) {
    Handler ch = new ConsoleHandler();
    ch.setLevel(Level.WARNING); // by default, will be changed by options like verbose or noWarning
    ch.setFormatter( new TomBasicFormatter() );

    rootLogger = Logger.getLogger("jtom", "jtom.TomMessageResources");
    rootLogger.setLevel(Level.ALL);
    rootLogger.setUseParentHandlers(false);
    rootLogger.addHandler(ch);

//     try{
//       Handler fh = new FileHandler("log");
//       rootLogger.addHandler(fh);
//     } catch(Exception e) { 
//       System.out.println("No log output"); 
//     }

    OptionManager om = new TomOptionManager();

    return TomServer.exec(args, om, "jtom");
  }

  public static void main(String[] args) {
    exec(args);
  }

}
