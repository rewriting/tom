/*
 * Gom
 *
 * Copyright (C) 2006 INRIA
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

import java.io.*;
import java.util.logging.*;
import tom.gom.backend.TemplateClass;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;

public class VariadicOperatorTemplate extends TemplateClass {
  ClassName abstractType;
  ClassName sortName;
  ClassName empty;
  GomClass conc;

  %include { ../../adt/objects/Objects.tom}

  public VariadicOperatorTemplate(ClassName className,
                                  ClassName abstractType,
                                  ClassName sortName,
                                  ClassName empty,
                                  GomClass conc) {
    super(className);
    this.abstractType = abstractType;
    this.sortName = sortName;
    this.empty = empty;
    this.conc = conc;
  }

  public String generate() {

    String classBody = %[
package @getPackage()@;

public abstract class @className()@ extends @fullClassName(sortName)@ {

@generateBody()@

}
]%;

    return classBody;
  }

  private String generateBody() {
    StringBuffer out = new StringBuffer();

    out.append(%[
  public int length() {
    int count = 0;
    if(this instanceof @fullClassName(conc.getclassName())@) {
      @fullClassName(sortName)@ tl = ((@fullClassName(conc.getclassName())@)this).getTail@className()@();
      if (tl instanceof @className()@) {
        return 1+((@className()@)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }
  // TODO reverse !!
]%);

    return out.toString();
  }

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
