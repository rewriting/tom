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

public class IsOpTemplate extends TemplateClass {
  ClassName operator;

  %include { ../../adt/objects/Objects.tom}

  /*
   * The argument is an operator class, and this template generates the
   * assotiated isOp strategy
   */
  public IsOpTemplate(ClassName className) {
    super(className);
    %match(ClassName className) {
      ClassName(pkg,name) -> {
        String newpkg = `pkg.replaceFirst(".types.",".strategy.");
        String newname = "Is_"+`name;
        this.className = `ClassName(newpkg,newname);
      }
    }
    this.operator = className;
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
    // generate is normally never called, Is_op is now an alias for When_op(Id)
    /*
writer.write(%[
package @getPackage()@;

import jjtraveler.Visitable;
import jjtraveler.VisitFailure;

public class @className()@ extends tom.library.strategy.mutraveler.Fail {
  private static final String msg = "Not an @className(operator)@";

  public Visitable visit(Visitable any) throws VisitFailure {
    if(any instanceof @fullClassName(operator)@) {
      return any;
    } else {
      throw new VisitFailure(msg);
    }
  }
}
]%);
*/
  }

  public String generateMapping() {
    String whenName = className().replaceFirst("Is","When");
    return %[
%op Strategy @className()@() {
  make() { `@whenName@(Identity()) }
}
]%;
  }

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
