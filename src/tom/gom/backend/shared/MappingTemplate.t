/*
 * Gom
 *
 * Copyright (c) 2006-2015, Universite de Lorraine, Inria
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
  GomClassList sortClasses;
  GomClassList operatorClasses;
  TemplateClass strategyMapping;

  %include { ../../adt/objects/Objects.tom}

  public MappingTemplate(GomClass gomClass, TemplateClass strategyMapping, GomEnvironment gomEnvironment) {
    super(gomClass,gomEnvironment);
    %match(gomClass) {
      TomMapping[SortClasses=sortClasses,
                 OperatorClasses=ops] -> {
        this.sortClasses = `sortClasses;
        this.operatorClasses = `ops;
        this.strategyMapping = strategyMapping;

        assert this.templates!=null ;
        return;
      }
    }
    throw new GomRuntimeException(
        "Wrong argument for MappingTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
    if(getGomEnvironment().isBuiltinSort("boolean")) {
      writer.write(%[
%include { boolean.tom }
]%);
    }
    if(getGomEnvironment().isBuiltinSort("String")) {
      writer.write(%[
%include { string.tom }
]%);
    }
    if(getGomEnvironment().isBuiltinSort("int")) {
      writer.write(%[
%include { int.tom }
]%);
    }
    if(getGomEnvironment().isBuiltinSort("char")) {
      writer.write(%[
%include { char.tom }
]%);
    }
    if (getGomEnvironment().isBuiltinSort("double")) {
      writer.write(%[
%include { double.tom }
]%);
    }
    if (getGomEnvironment().isBuiltinSort("long")) {
      writer.write(%[
%include { long.tom }
]%);
    }
    if (getGomEnvironment().isBuiltinSort("float")) {
      writer.write(%[
%include { float.tom }
]%);
    }
    if (getGomEnvironment().isBuiltinSort("ATerm")) {
      writer.write(%[
%include { aterm.tom }
]%);
    }
    if (getGomEnvironment().isBuiltinSort("ATermList")) {
      writer.write(%[
%include { aterm.tom }
]%);
    }

    // generate a %typeterm for each class
    %match(sortClasses) {
      ConcGomClass(_*,
          SortClass[ClassName=sortName],
          _*) -> {
        (templates.get(`sortName))
          .generateTomMapping(writer);
      }
    }

    // generate a %op for each operator
    %match(operatorClasses) {
      ConcGomClass(_*,
          OperatorClass[ClassName=opName],
          _*) -> {
        //System.out.println("templates = " + templates);
        //System.out.println("opname    = " + `opName);
        //System.out.println("result    = " + templates.get(`opName));
        (templates.get(`opName))
          .generateTomMapping(writer);
      }
    }

    // generate a %oplist for each variadic operator
    %match(operatorClasses) {
      ConcGomClass(_*,
          VariadicOperatorClass[ClassName=opName],
          _*) -> {
        (templates.get(`opName))
          .generateTomMapping(writer);
      }
    }
    /* Include the strategy mapping (_file.tom) if needed */
    if(strategyMapping != null) {
      strategyMapping.generateTomMapping(writer);
    }
  }

  protected String fileName() {
    return fullClassName().replace('.',File.separatorChar)+".tom";
  }

  protected File fileToGenerate() {
    GomStreamManager stream = getGomEnvironment().getStreamManager();
    File output = new File(stream.getDestDir(),fileName());
    // log the generated mapping file name
    try {
      getGomEnvironment()
        .setLastGeneratedMapping(output.getCanonicalPath());
    } catch(Exception e) {
      e.printStackTrace();
    }
    return output;
  }
}
