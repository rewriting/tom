/*
 * Gom
 *
 * Copyright (c) 2006-2011, INPL, INRIA
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

package tom.gom.backend.ada.shared;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import tom.gom.backend.ada.TemplateHookedClass;
import tom.gom.backend.ada.TemplateClass;
import tom.gom.backend.CodeGen;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;
import tom.platform.OptionManager;

public class VariadicOperatorTemplate extends tom.gom.backend.ada.TemplateHookedClass {
  ClassName abstractType;
  ClassName sortName;
  GomClass empty;
  GomClass cons;
  String comments;

  %include { ../../../adt/objects/Objects.tom}

  public VariadicOperatorTemplate(File tomHomePath,
                                  OptionManager manager,
                                  List importList, 	
                                  GomClass gomClass,
                                  tom.gom.backend.TemplateClass mapping,
                                  GomEnvironment gomEnvironment) {
    super(gomClass,manager,tomHomePath,importList,mapping,gomEnvironment);
    %match(gomClass) {
      VariadicOperatorClass[AbstractType=abstractType,
                            SortName=sortName,
                            Empty=empty,
                            Cons=cons,
                            Comments=comments] -> {
        this.abstractType = `abstractType;
        this.sortName = `sortName;
        this.empty = `empty;
        this.cons = `cons;
        this.comments = `comments;
        return;
      }
    }
    throw new GomRuntimeException(
        "Wrong argument for VariadicOperatorTemplate: " + gomClass);
  }

  public void generateSpec(java.io.Writer writer) throws java.io.IOException {
writer.write(%[ --From generateSpec at VariadicOperatorTemplate.t ]%) ;
}

  public void generate(java.io.Writer writer) throws java.io.IOException {

generateBody(writer);
  }


  private void generateBody(java.io.Writer writer) throws java.io.IOException {
    writer.write(%[
-- Not supported
]%);
    return;
  }

  protected Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
