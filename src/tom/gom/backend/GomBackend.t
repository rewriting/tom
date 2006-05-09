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

package tom.gom.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.gom.GomMessage;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;

import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;

import tom.library.strategy.mutraveler.MuTraveler;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

public class GomBackend {
  TemplateFactory templatefactory;

  %include { ../adt/objects/Objects.tom }
  %include { mutraveler.tom }

  GomBackend(TemplateFactory templatefactory) {
    this.templatefactory = templatefactory;
  }

  private GomEnvironment environment() {
    return GomEnvironment.getInstance();
  }

  private Map mappingForMappingName = new HashMap();

  public int generate(GomClassList classList) {
    int errno = 0;
    // populate the mappingForMappingName Map
    %match(GomClassList classList) {
      concGomClass(_*,mapping@TomMapping[className=mappingName],_*) -> {
        mappingForMappingName.put(`mappingName,`mapping);
      }
    }
    // generate a class for each element of the list
    while (!classList.isEmptyconcGomClass()) {
      GomClass gomclass = classList.getHeadconcGomClass();
      classList = classList.getTailconcGomClass();
      errno += generateClass(gomclass);
    }
    return 1;
  }

  /*
   * Create template classes for the different classes to generate
   */
  public int generateClass(GomClass gomclass) {
    %match(GomClass gomclass) {
      TomMapping[className=className,basicStrategy=basicStrategy,sortClasses=sortClasses,operatorClasses=ops] -> {
        TemplateClass mapping = templatefactory.makeTomMappingTemplate(`className,`basicStrategy,`sortClasses,`ops);
        mapping.generateFile();
        return 1;
      }
      FwdClass[className=className,
               visitor=visitorClass,
               importedVisitors=importedVisitors,
               abstractType=abstractType,
               importedAbstractTypes=imported,
               sortClasses=sortClasses,
               operatorClasses=ops] -> {
        TemplateClass fwd = templatefactory.makeForwardTemplate(`className,`visitorClass,`importedVisitors,`abstractType,`imported,`sortClasses,`ops);
        fwd.generateFile();
        return 1;
      }
      VisitableFwdClass[className=className,fwd=FwdClass[className=fwdClass]] -> {
        TemplateClass visitablefwd = templatefactory.makeVisitableForwardTemplate(`className,`fwdClass);
        visitablefwd.generateFile();
        return 1;
      }
      VisitorClass[className=className,sortClasses=sortClasses,operatorClasses=ops] -> {
        TemplateClass visitor = templatefactory.makeVisitorTemplate(`className,`sortClasses,`ops);
        visitor.generateFile();
        return 1;
      }
      AbstractTypeClass[className=className,
                        visitor=visitorName,
                        sortList=sortList] -> {
        TemplateClass abstracttype = templatefactory.makeAbstractTypeTemplate(`className,`visitorName,`sortList);
        abstracttype.generateFile();
        return 1;
      }
      SortClass[className=className,
                abstractType=abstracttype,
                visitor=visitorName,
                operators=ops,
                slots=slots] -> {
        TemplateClass sort = templatefactory.makeSortTemplate(`className,`abstracttype,`visitorName,`ops,`slots);
        sort.generateFile();
        return 1;
      }
      OperatorClass[className=className,
                    abstractType=abstracttype,
                    mapping=mapping,
                    sortName=sort,
                    visitor=visitorName,
                    slots=slots,
                    hooks=hooks] -> {
        GomClass mappingClass = (GomClass)mappingForMappingName.get(`mapping);
        TemplateClass operator = templatefactory.makeOperatorTemplate(`className,`abstracttype,`sort,`visitorName,`slots,`hooks,getMappingTemplate(mappingClass));
        operator.generateFile();
        return 1;
      }
      VariadicOperatorClass[className=className,
                            abstractType=abstracttype,
                            sortName=sort,
                            empty=empty,
                            cons=cons] -> {
        TemplateClass operator = templatefactory.makeVariadicOperatorTemplate(`className,`abstracttype,`sort,`empty,`cons);
        operator.generateFile();
        return 1;
      }
    }
    throw new GomRuntimeException("Trying to generate code for a strange class: "+gomclass);
  }

  public TemplateClass getMappingTemplate(GomClass mapping) {
    TemplateClass mappingTemplate = null;
    %match(GomClass mapping) {
      TomMapping[className=mappingName,
                 basicStrategy=basicStrategy,
                 sortClasses=sortClasses,
                 operatorClasses=ops] -> {
        mappingTemplate = templatefactory.makeTomMappingTemplate(`mappingName,`basicStrategy,`sortClasses,`ops);
      }
    }
    return mappingTemplate;
  }
}
