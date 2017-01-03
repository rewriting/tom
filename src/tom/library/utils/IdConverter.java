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

package tom.library.utils;

import aterm.ATerm;
import tom.library.utils.ATermConverter;

/**
 * This class does nothing : no processing is done in its methods.
 * It is used by default import method in Gom, when no conversion is needed.
 */

public class IdConverter implements ATermConverter {

  /**
   * Method triggered when using the default import method in Gom.
   * It is the Identity, this method does not modify the ATerm
   *
   * @param at the ATerm which is currently processed
   * @return the processed ATerm without any modification
   */
  public ATerm convert(ATerm at) {
    return at;
  }

}
