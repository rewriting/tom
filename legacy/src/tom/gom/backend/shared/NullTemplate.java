/*
 *
 * GOM
 *
 * Copyright (c) 2006-2017, Universite de Lorraine, Inria
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
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.backend.shared;
import tom.gom.tools.GomEnvironment;

import tom.gom.backend.TemplateClass;
import java.io.*;
import tom.gom.adt.objects.types.*;

public class NullTemplate extends TemplateClass {

  /**
   * The NullTemplate class generates nothing.
   */
  public NullTemplate(GomClass gomClass, GomEnvironment gomEnvironment) {
    super(gomClass,gomEnvironment);
  }

  /* We may want to return the stringbuffer itself in the future, or directly write to a Stream */
  public void generate(java.io.Writer writer) { /* does nothing */ }


  public int generateFile() {
    // Do nothing, we don't need to generate empty files
    return 0;
  }
}
