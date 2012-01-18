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

package tom.gom.backend.java.strategy;

import java.util.Map;
import java.util.HashMap;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.backend.MappingTemplateClass;
import java.io.*;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.backend.TemplateClass;

public class StratMappingTemplate extends tom.gom.backend.MappingTemplateClass {
  GomClassList operatorClasses;
  int generateStratMapping = 0;

  %include { ../../../adt/objects/Objects.tom }

  public StratMappingTemplate(GomClass gomClass, GomEnvironment gomEnvironment, int generateStratMapping) {
    super(gomClass,gomEnvironment);
    this.generateStratMapping = generateStratMapping;
    %match(gomClass) {
      TomMapping[OperatorClasses=ops] -> {
        this.operatorClasses = `ops;
        return;
      }
    }
    throw new GomRuntimeException(
        "Wrong argument for MappingTemplate: " + gomClass);
  }

public void addTemplates(Map<ClassName,TemplateClass> map) {
return;
}

  public void generateSpec(java.io.Writer writer) throws java.io.IOException {
return ;
} 


  public void generateTomMapping(java.io.Writer writer) throws java.io.IOException {
    generate(writer);
  }

  /**
    * generate mappings for congruence strategies
    * in a _file.tom
    */
  public void generate(java.io.Writer writer) throws java.io.IOException {
    if(generateStratMapping == 1) {
      writer.write("  %include { sl.tom }");
    }
    %match(operatorClasses) {
      ConcGomClass(_*,op@OperatorClass[],_*) -> {
        writer.write(
            (new tom.gom.backend.java.strategy.SOpTemplate(`op,getGomEnvironment())).generateMapping());
        writer.write(
            (new tom.gom.backend.java.strategy.IsOpTemplate(`op,getGomEnvironment())).generateMapping());
        writer.write(
            (new tom.gom.backend.java.strategy.MakeOpTemplate(`op,getGomEnvironment())).generateMapping());
      }
      ConcGomClass(_*,
          VariadicOperatorClass[ClassName=vopName,
          Empty=OperatorClass[ClassName=empty],
          Cons=OperatorClass[ClassName=cons]],
          _*)-> {
        writer.write(%[
  %op Strategy _@className(`vopName)@(sub:Strategy) {
    is_fsym(t) { false }
    make(sub)  { `mu(MuVar("x_@className(`vopName)@"),Choice(_@className(`cons)@(sub,MuVar("x_@className(`vopName)@")),_@className(`empty)@())) }
  }
  ]%);
      }
    }
      }

  protected String fileName() {
    return fullClassName().replace('.',File.separatorChar)+".tom";
  }

}
