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

import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.backend.TemplateClass;
import java.io.*;
import tom.gom.adt.objects.types.*;

public class StratMappingTemplate extends TemplateClass {
  GomClassList operatorClasses;

  %include { ../../adt/objects/Objects.tom}

  public StratMappingTemplate(ClassName className, GomClassList operatorClasses) {
    super(className);
    this.operatorClasses = operatorClasses;
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
    writer.write(%[
%include { mustrategy.tom }
]%);
    /* XXX: i could introduce an interface providing generateMapping() */
    %match(GomClassList operatorClasses) {
      concGomClass(_*,
          OperatorClass[className=opName,
                        slots=slotList],
          _*) -> {
      writer.write(
        (new tom.gom.backend.strategy.IsOpTemplate(`opName)).generateMapping());

      writer.write(
        (new tom.gom.backend.strategy.SOpTemplate(`opName,`slotList)).generateMapping());
      writer.write(
        (new tom.gom.backend.strategy.MakeOpTemplate(`opName,`slotList)).generateMapping());
      }
      concGomClass(_*,
          VariadicOperatorClass[className=vopName,
                                empty=OperatorClass[className=empty],
                                cons=OperatorClass[className=cons]],
                            _*)-> {
        writer.write(%[
%op Strategy _@className(`vopName)@(sub:Strategy) {
  is_fsym(t) { false }
  make(sub)  { `mu(MuVar("x"),Choice(_@className(`cons)@(sub,MuVar("x")),_@className(`empty)@())) }
}
]%);
      }
    }
  }

  protected String fileName() {
    return fullClassName().replace('.',File.separatorChar)+".tom";
  }

}
