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

import tom.platform.adt.platformoption.types.PlatformOptionList;

/**
 * This interface contains the option-related methods of Plugin.
 *
 * @author Gr&eacute;gory ANDRIEN
 */

public interface OptionOwner {

  /**
   * Returns a list containing the options the plugin declares.
   *
   * @return the options declared by the plugin
   */
  public PlatformOptionList getDeclaredOptionList();

  /**
   * Returns a list containing the options the plugin requires in order to do
   * what is expected of it. A plugin may indeed require that some options are
   * set to specific values depending on which tasks it is told to accomplish.
   *
   * @return the plugin's prerequisites
   */
  public PlatformOptionList getRequiredOptionList();

  /**
   * Method triggered each time an option own is changed:
   * The Option owner can react to that change
   *
   * @param name the option's name
   * @param value the option's value
   */
  public void optionChanged(String name, Object value);

  /**
   * Sets the associated OptionManager
   *
   * @param om the option manager
   */
  public void setOptionManager(OptionManager om);

}
