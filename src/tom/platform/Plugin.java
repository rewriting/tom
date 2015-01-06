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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.platform;

import java.util.Map;

/**
 * This interface must be implemented by all plugins. It provides methods
 * to access options (inherited from the OptionOwner interface) as well as
 * methods to run the compilation. The processing follow the template:
 * "feed" the plugin (set), run it, retrieve the processed term(get)
 *
 */
public interface Plugin extends OptionOwner {
  /**
   * Sets the input of the plugin.
   *
   * @param arg the input argument
   */
  public void setArgs(Object[] arg);

  /**
   * Runs the plugin.
   */
  public void run(Map<String,String> informationTracker);

  /**
   * Retrieves the output of the plugin.
   *
   * @return the output argument
   */
  public Object[] getArgs();

}
