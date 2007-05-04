 /* Gom
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

import tom.gom.backend.TemplateClass;
import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class BasicStrategyTemplate extends TemplateClass {
  ClassName fwd;

  %include { ../../adt/objects/Objects.tom }

  public BasicStrategyTemplate(GomClass basic) {
    super(basic);
    %match(basic) {
      VisitableFwdClass[ClassName=className,
                        Fwd=FwdClass[ClassName=fwdClass]] -> {
        this.fwd = `fwdClass;
        return;
      }
    }
    throw new GomRuntimeException(
        "Wrong argument for BasicStrategyTemplate: " + basic);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
    writer.write(%[
package @getPackage()@;
import tom.library.sl.*;
   
  public class @className()@ extends @className(fwd)@ implements tom.library.sl.Strategy {
  private tom.library.sl.Environment environment;
      
  public int getChildCount() {
    return 1;
  }
    
  public Visitable getChildAt(int i) {
    switch (i) {
      case 0: return (Visitable) any;
      default: throw new IndexOutOfBoundsException();
    }
  }
    
  public Visitable setChildAt(int i, Visitable child) {
    switch (i) {
      case 0: any = (Strategy) child; return this;
      default: throw new IndexOutOfBoundsException();
    }
  }

  public Visitable[] getChildren() {
    return new Visitable[]{(Visitable)any};
  }

  public Visitable setChildren(Visitable[] children) {
    any = (Strategy)children[0];
    return this;
  }

  public Visitable visit(Environment envt) throws VisitFailure {
    setEnvironment(envt);
    int status = visit();
    if(status == Environment.SUCCESS) {
      return environment.getRoot();
    } else {
      throw new VisitFailure();
    }
  }


  public tom.library.sl.Visitable visit(tom.library.sl.Visitable any) throws VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
    environment.setRoot(any);
    int status = visit();
    if(status == tom.library.sl.Environment.SUCCESS) {
      return environment.getRoot();
    } else {
      throw new VisitFailure();
    }
  }

  public int visit() {
    try {
      environment.setSubject((tom.library.sl.Visitable)this.visitLight(environment.getSubject()));
      return tom.library.sl.Environment.SUCCESS;
    } catch(VisitFailure f) {
      return tom.library.sl.Environment.FAILURE;
    }
  }

  public tom.library.sl.Strategy accept(tom.library.sl.reflective.StrategyFwd v) throws tom.library.sl.VisitFailure {
    return v.visit_Strategy(this);
  } 

  public tom.library.sl.Environment getEnvironment() {
    if(environment!=null) {
      return environment;
    } else {
      throw new java.lang.RuntimeException("environment not initialized");
    }
  }

  public void setEnvironment(tom.library.sl.Environment env) {
    this.environment = env;
  }

   
    
  public @className()@(Strategy any) {
    super(any);
  }
}
]%);
  }
}
