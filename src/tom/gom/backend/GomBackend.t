/*
 *
 * GOM
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

import tom.gom.backend.apigen.*;

public class GomBackend {

	%include { ../adt/objects/Objects.tom}

  %include { mutraveler.tom }

  private GomEnvironment environment() {
    return GomEnvironment.getInstance();
  }

  public int generate(GomClassList classList) {
    int errno = 0;
    // generate a class for each element of the list
    while (!classList.isEmpty()) {
      GomClass gomclass = classList.getHead();
      classList = classList.getTail();
      errno += generateClass(gomclass);
    }
    return 1;
  }

  /*
   * Create template classes for the different classes to generate
   */
  public int generateClass(GomClass gomclass) {
    %match(GomClass gomclass) {
      TomMapping[className=className,sortClasses=sortClasses,operatorClasses=ops] -> {
        TemplateClass mapping = new TemplateMapping(`className,`sortClasses,`ops);
        mapping.generateFile();
        return 1;
      }
      FwdClass[className=className,visitor=visitorClass,abstractType=abstractType,sortClasses=sortClasses,operatorClasses=ops] -> {
        TemplateClass fwd = new TemplateFwd(`className,`visitorClass,`abstractType,`sortClasses,`ops);
        fwd.generateFile();
        return 1;
      }
      VoidFwdClass[className=className,visitor=visitorClass,abstractType=abstractType,sortClasses=sortClasses,operatorClasses=ops] -> {
        TemplateClass voidfwd = new TemplateVoidFwd(`className,`visitorClass,`abstractType,`sortClasses,`ops);
        voidfwd.generateFile();
        return 1;
      }
      VisitableFwdClass[className=className,fwd=FwdClass[className=fwdClass]] -> {
        TemplateClass visitablefwd = new TemplateVisitableFwd(`className,`fwdClass);
        visitablefwd.generateFile();
        return 1;
      }
      VisitorClass[className=className,sortClasses=sortClasses,operatorClasses=ops] -> {
        TemplateClass visitor = new TemplateVisitor(`className,`sortClasses,`ops);
        visitor.generateFile();
        return 1;
      }
      AbstractTypeClass[className=className,factoryName=factory,visitor=visitorName,sortList=sortList] -> {
        TemplateClass abstracttype = new TemplateAbstractType(`className,`factory,`visitorName,`sortList);
        abstracttype.generateFile();
        return 1;
      }
      SortClass[className=className,factoryName=factory,abstractType=abstracttype,operators=ops,slots=slots] -> {
        TemplateClass sort = new TemplateSort(`className,`factory,`abstracttype,`ops,`slots);
        sort.generateFile();
        return 1;
      }
      OperatorClass[className=className,factoryName=factory,abstractType=abstracttype,sortName=sort,visitor=visitorName,slots=slots] -> {
        TemplateClass operator = new TemplateOperator(`className,`factory,`abstracttype,`sort,`visitorName,`slots);
        operator.generateFile();
        return 1;
      }
      FactoryClass[className=className,importedFactories=factories,sortClasses=sorts,operatorClasses=operators] -> {
        TemplateClass factory = new TemplateFactory(`className,`factories,`sorts,`operators);
        factory.generateFile();
        return 1;
      }
    }
		throw new GomRuntimeException("Trying to generate code for a strange class: "+gomclass);
  }

}
