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

import tom.platform.adt.platformoption.types.*;

/**
 * This interface contains the option-related methods of TomPlugin.
 *
 * @author Gr&eacute;gory ANDRIEN
 */
public interface TomPluginOptions {

  /**
   * Returns a list containing the options the plugin declares.
   *
   * @return the options declared by the plugin
   */
  public abstract PlatformOptionList declaredOptions();

  /**
   * Returns a list containing the options the plugin requires in order
   * to do what is expected of it. A plugin may indeed require that
   * some options are set to specific values depending on which tasks
   * it is told to accomplish.
   *
   * @return the plugin's prerequisites
   */
  public abstract PlatformOptionList requiredOptions();

  /**
   * Sets the option whose name is given to the specified value.
   *
   * @param name the option's name
   * @param value the option's value
   */
  public abstract void setOption(String name, Object value);
}
