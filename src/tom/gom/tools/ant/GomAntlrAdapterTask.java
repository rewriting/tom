/*
 * Gom
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
 * Antoine Reilles       e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.tools.ant;

/**
 * Compiles GOM source files into an Antlr adaptor.
 * This task can take the following
 * arguments:
 * <ul>
 * <li>config</li>
 * <li>srcdir</li>
 * <li>destdir</li>
 * <li>package</li>
 * <li>options</li>
 * <li>failonerror</li>
 * <li>fork</li>
 * <li>termgraph</li>
 * <li>fresh</li>
 * <li>pointer</li>
 * <li>gwt</li>
 * <li></li>
 * </ul>
 * Of these arguments, the <b>srcdir</b> and <b>destdir</b> are
 * required.
 * <b>config</b> has to be set if the Gom.xml file can't be found in
 * <b>tom.home</b>.<p>
 *
*/

public class GomAntlrAdapterTask extends GomCommonTask {

  protected String toPattern() {
    return "\\1\\L\\3" + protectedFileSeparator + "\\3Tokens.tokens";
  }

  protected String defaultConfigName() {
    return "GomAntlrAdaptor.xml";
  }
}
