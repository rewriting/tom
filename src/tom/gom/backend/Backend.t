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

public class Backend {
  TemplateFactory templatefactory;
  private File tomHomePath;
  private List importList = null;

  %include { ../adt/objects/Objects.tom }
  %include { mustrategy.tom }

  Backend(TemplateFactory templatefactory, File tomHomePath, List importList) {
    this.templatefactory = templatefactory;
    this.tomHomePath = tomHomePath;
    this.importList = importList;
  }

  private GomEnvironment environment() {
    return GomEnvironment.getInstance();
  }

  private Map mappingForMappingName = new HashMap();

  public int generate(GomClassList classList) {
    int errno = 0;
    // populate the mappingForMappingName Map
    %match(GomClassList classList) {
      concGomClass(_*,mapping@TomMapping[ClassName=mappingName],_*) -> {
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
      TomMapping[ClassName=className@ClassName(pkg,name),BasicStrategy=basicStrategy,SortClasses=sortClasses,OperatorClasses=ops] -> {
        TemplateClass mapping = templatefactory.makeTomMappingTemplate(`className,`basicStrategy,`sortClasses,`ops);
        mapping.generateFile();

        TemplateClass stratMapping = 
          new tom.gom.backend.strategy.StratMappingTemplate(`ClassName(pkg,"_"+name),`ops);
        stratMapping.generateFile();
        return 1;
      }
      FwdClass[ClassName=className,
               Visitor=visitorClass,
               ImportedVisitors=importedVisitors,
               AbstractType=abstractType,
               ImportedAbstractTypes=imported,
               SortClasses=sortClasses,
               OperatorClasses=ops] -> {
        TemplateClass fwd = templatefactory.makeForwardTemplate(`className,`visitorClass,`importedVisitors,`abstractType,`imported,`sortClasses,`ops);
        fwd.generateFile();
        return 1;
      }
      VisitableFwdClass[ClassName=className,Fwd=FwdClass[ClassName=fwdClass]] -> {
        TemplateClass visitablefwd = templatefactory.makeVisitableForwardTemplate(`className,`fwdClass);
        visitablefwd.generateFile();
        return 1;
      }
      VisitorClass[ClassName=className,SortClasses=sortClasses,OperatorClasses=ops] -> {
        TemplateClass visitor = templatefactory.makeVisitorTemplate(`className,`sortClasses,`ops);
        visitor.generateFile();
        return 1;
      }
      AbstractTypeClass[ClassName=className,
                        Visitor=visitorName,
                        Mapping=mapping,
                        SortList=sortList,
                        Hooks=hooks] -> {
        GomClass mappingClass = (GomClass)mappingForMappingName.get(`mapping);
        TemplateClass abstracttype = templatefactory.makeAbstractTypeTemplate(tomHomePath,importList,`className,`visitorName,`sortList,`hooks,getMappingTemplate(mappingClass));
        abstracttype.generateFile();
        return 1;
      }
      SortClass[ClassName=className,
                AbstractType=abstracttype,
                Visitor=visitorName,
                Operators=ops,
                Mapping=mapping,
                VariadicOperators=varyops,
                Slots=slots,
                Hooks=hooks] -> {
        GomClass mappingClass = (GomClass)mappingForMappingName.get(`mapping);
        TemplateClass sort = templatefactory.makeSortTemplate(tomHomePath,importList,`className,`abstracttype,`visitorName,`ops,`varyops,`slots,`hooks,getMappingTemplate(mappingClass));
            
        sort.generateFile();
        return 1;
      }
      OperatorClass[ClassName=className,
                    AbstractType=abstracttype,
                    ExtendsType=extendstype,
                    Mapping=mapping,
                    SortName=sort,
                    Visitor=visitorName,
                    Slots=slots,
                    Hooks=hooks] -> {
        GomClass mappingClass = (GomClass)mappingForMappingName.get(`mapping);
        TemplateClass operator = templatefactory.makeOperatorTemplate(
            tomHomePath,
            importList,
            `className,
            `abstracttype,
            `extendstype,
            `sort,
            `visitorName,
            `slots,
            `hooks,
            getMappingTemplate(mappingClass));
        operator.generateFile();

        //TemplateClass isOpStrat = new tom.gom.backend.strategy.IsOpTemplate(`className);
        //isOpStrat.generateFile();

        TemplateClass sOpStrat = new tom.gom.backend.strategy.SOpTemplate(`className,`slots);
        sOpStrat.generateFile();

        TemplateClass makeOpStrat = new tom.gom.backend.strategy.MakeOpTemplate(`className,`slots);
        makeOpStrat.generateFile();
         TemplateClass whenOpStrat = new tom.gom.backend.strategy.WhenOpTemplate(`className);
        whenOpStrat.generateFile();

       return 1;
      }
      VariadicOperatorClass[ClassName=className,
                            AbstractType=abstracttype,
                            SortName=sort,
                            Mapping=mapping,
                            Empty=empty,
                            Cons=cons,
                            Hooks=hooks] -> {
        GomClass mappingClass = (GomClass)mappingForMappingName.get(`mapping);
        TemplateClass operator = templatefactory.makeVariadicOperatorTemplate(tomHomePath,importList,`className,`abstracttype,`sort,`empty,`cons,`hooks,getMappingTemplate(mappingClass));
        operator.generateFile();
        return 1;
      }
    }
    throw new GomRuntimeException("Trying to generate code for a strange class: "+gomclass);
  }

  public TemplateClass getMappingTemplate(GomClass mapping) {
    TemplateClass mappingTemplate = null;
    %match(GomClass mapping) {
      TomMapping[ClassName=mappingName,
                 BasicStrategy=basicStrategy,
                 SortClasses=sortClasses,
                 OperatorClasses=ops] -> {
        mappingTemplate = templatefactory.makeTomMappingTemplate(`mappingName,`basicStrategy,`sortClasses,`ops);
      }
    }
    return mappingTemplate;
  }
}
