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

package tom.gom.backend.strategy;

import java.io.*;
import java.util.logging.*;
import tom.gom.backend.TemplateClass;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;

public class WhenOpTemplate extends TemplateClass {
  ClassName operator;

  %include { ../../adt/objects/Objects.tom}

  /*
   * The argument is an operator class, and this template generates the
   * assotiated isOp strategy
   */
  public WhenOpTemplate(ClassName className) {
    super(className);
    %match(ClassName className) {
      ClassName(pkg,name) -> {
        String newpkg = `pkg.replaceFirst(".types.",".strategy.");
        String newname = "When_"+`name;
        this.className = `ClassName(newpkg,newname);
      }
    }
    this.operator = className;
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
writer.write(%[
package @getPackage()@;

public class @className()@ extends tom.library.sl.Fail {
  private static final String msg = "Not an @className(operator)@";
  public final static int ARG = 0;

  public @className()@(tom.library.sl.Strategy v) {
    initSubterm(v);
  }

  public jjtraveler.Visitable visit(jjtraveler.Visitable any) throws jjtraveler.VisitFailure {
    if(any instanceof @fullClassName(operator)@) {
      return visitors[ARG].visit(any);
    } else {
      throw new jjtraveler.VisitFailure(msg);
    }
  }

  public void visit() throws jjtraveler.VisitFailure {
    if(getSubject() instanceof @fullClassName(operator)@) {
      visitors[ARG].visit();
      return;
    } else {
      throw new jjtraveler.VisitFailure(msg);
    }
  }
}
]%);
  }

  public String generateMapping() {
    return %[
%op Strategy @className()@(s:Strategy) {
  is_fsym(t) { (t!=null) && t instanceof (@fullClassName()@)}
  make(s) { new @fullClassName()@(s) }
}
]%;
  }

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
