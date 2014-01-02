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

import java.util.logging.Logger;

/**
 * The PluginPlatformBase manages a global status handler which 
 * is common to all PluginPlatformInstances
 *
 */
public abstract class PluginPlatformBase {
  
  /** The global status handler */
  private static StatusHandler globalStatusHandler = new StatusHandler();
  
  protected PluginPlatformBase(String loggerRadical) {
    Logger.getLogger(loggerRadical).addHandler(globalStatusHandler);
  }
  
  public StatusHandler getGlobalStatusHandler() {
    return globalStatusHandler;
  }
  
  public void clearGlobalStatusHandler() {
    globalStatusHandler.clear();
  }
}
