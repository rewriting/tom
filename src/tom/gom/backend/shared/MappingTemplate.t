/*
 * Gom
 *
 * Copyright (C) 2006-2007, INRIA
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

import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.backend.TemplateClass;
import tom.gom.backend.MappingTemplateClass;
import java.io.*;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class MappingTemplate extends MappingTemplateClass {
  ClassName basicStrategy;
  GomClassList sortClasses;
  GomClassList operatorClasses;

  %include { ../../adt/objects/Objects.tom}

  public MappingTemplate(GomClass gomClass) {
    super(gomClass);
    %match(gomClass) {
      TomMapping[BasicStrategy=basicStrategy,
                 SortClasses=sortClasses,
                 OperatorClasses=ops] -> {
        this.basicStrategy = `basicStrategy;
        this.sortClasses = `sortClasses;
        this.operatorClasses = `ops;
        return;
      }
    }
    throw new GomRuntimeException(
        "Wrong argument for MappingTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
    if(GomEnvironment.getInstance().isBuiltinSort("boolean")) {
      writer.write(%[
%include { boolean.tom }
]%);
    }
    if(GomEnvironment.getInstance().isBuiltinSort("String")) {
      writer.write(%[
%include { string.tom }
]%);
    }
    if(GomEnvironment.getInstance().isBuiltinSort("int")) {
      writer.write(%[
%include { int.tom }
]%);
    }
    if(GomEnvironment.getInstance().isBuiltinSort("char")) {
      writer.write(%[
%include { char.tom }
]%);
    }
    if (GomEnvironment.getInstance().isBuiltinSort("double")) {
      writer.write(%[
%include { double.tom }
]%);
    }
    if (GomEnvironment.getInstance().isBuiltinSort("long")) {
      writer.write(%[
%include { long.tom }
]%);
    }
    if (GomEnvironment.getInstance().isBuiltinSort("float")) {
      writer.write(%[
%include { float.tom }
]%);
    }
    if (GomEnvironment.getInstance().isBuiltinSort("ATerm")) {
      writer.write(%[
%include { aterm.tom }
]%);
    }
    if (GomEnvironment.getInstance().isBuiltinSort("ATermList")) {
      writer.write(%[
%include { atermlist.tom }
]%);
    }

    // generate a %typeterm for each class
    %match(GomClassList sortClasses) {
      concGomClass(_*,
          SortClass[ClassName=sortName],
          _*) -> {
        ((TemplateClass) templates.get(`sortName))
          .generateTomMapping(writer,basicStrategy);
      }
    }

    // generate a %op for each operator
    %match(GomClassList operatorClasses) {
      concGomClass(_*,
          OperatorClass[ClassName=opName],
          _*) -> {
        ((TemplateClass) templates.get(`opName))
          .generateTomMapping(writer,basicStrategy);
      }
    }

    // generate a %oplist for each variadic operator
    %match(GomClassList operatorClasses) {
      concGomClass(_*,
          VariadicOperatorClass[ClassName=opName],
          _*) -> {
        ((TemplateClass) templates.get(`opName))
          .generateTomMapping(writer,basicStrategy);
      }
    }
  }

  protected String fileName() {
    return fullClassName().replace('.',File.separatorChar)+".tom";
  }

  protected File fileToGenerate() {
    GomStreamManager stream = GomEnvironment.getInstance().getStreamManager();
    File output = new File(stream.getDestDir(),fileName());
    // log the generated mapping file name
    try {
      GomEnvironment.getInstance()
        .setLastGeneratedMapping(output.getCanonicalPath());
    } catch(Exception e) {
      e.printStackTrace();
    }
    return output;
  }
}
