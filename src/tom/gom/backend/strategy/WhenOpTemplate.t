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

public class WhenOpTemplate extends TemplateClass {
  ClassName operator;

  %include { ../../adt/objects/Objects.tom}

  /*
   * The argument is an operator class, and this template generates the
   * assotiated isOp strategy
   */
  public WhenOpTemplate(ClassName className) {
    super(className);
    %match(ClassName className) {
      ClassName(pkg,name) -> {
        String newpkg = `pkg.replaceFirst(".types.",".strategy.");
        String newname = "When_"+`name;
        this.className = `ClassName(newpkg,newname);
      }
    }
    this.operator = className;
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
writer.write(%[
package @getPackage()@;

public class @className()@  implements tom.library.strategy.mutraveler.MuStrategy, tom.library.sl.Strategy {
  private static final String msg = "Not an @className(operator)@";
  private static jjtraveler.reflective.VisitableVisitor sub;

  public @className()@(jjtraveler.reflective.VisitableVisitor v) {
    sub = v;
  }

  public int getChildCount() {
    return 1;
  }
  public jjtraveler.Visitable getChildAt(int index) {
    switch(index) {
    case 1: return sub;
      default: throw new IndexOutOfBoundsException();
    }
  }
  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable child) {
    switch(index) {
      case 1: sub = (jjtraveler.reflective.VisitableVisitor)child;
      default: throw new IndexOutOfBoundsException();
    }
  }

  public jjtraveler.Visitable[] getChildren() {
    return new jjtraveler.Visitable[]{sub};
  }

  public jjtraveler.Visitable setChildren(jjtraveler.Visitable[] children) {
    sub = (jjtraveler.reflective.VisitableVisitor)children[0];
    return this;
  }

  protected tom.library.sl.Environment environment;
  public void setEnvironment(tom.library.sl.Environment env) {
    this.environment = env;
  }

  public tom.library.sl.Environment getEnvironment() {
    if(environment!=null) {
      return environment;
    } else {
      throw new RuntimeException("environment not initialized");
    }
  }

  private tom.library.strategy.mutraveler.Position position;
  public void setPosition(tom.library.strategy.mutraveler.Position pos) {
    position = pos;  
  }

  public tom.library.strategy.mutraveler.Position getPosition() {
    if(position!=null) {
      return position;
    } else {
      throw new RuntimeException("position not initialized");
    }
  }

  public boolean hasPosition() { return false; }

  /*
   * Apply the strategy, and returns the subject in case of VisitFailure
   */
  public jjtraveler.Visitable apply(jjtraveler.Visitable any) {
    try {
      return tom.library.strategy.mutraveler.MuTraveler.init(this).visit(any);
    } catch (jjtraveler.VisitFailure f) {
      return any;
    }
  }

  public tom.library.sl.Visitable apply(tom.library.sl.Visitable any) { /*throws Failure*/
    try {
      tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
      getEnvironment().setRoot(any);
      visit();
      return getEnvironment().getRoot();
    } catch (jjtraveler.VisitFailure f) {
      return any;
    }
  }

  public tom.library.strategy.mutraveler.MuStrategy accept(tom.library.strategy.mutraveler.reflective.StrategyVisitorFwd v) throws jjtraveler.VisitFailure {
    return v.visit_Strategy(this);
  }

  public tom.library.sl.Strategy accept(tom.library.sl.reflective.StrategyFwd v) throws jjtraveler.VisitFailure {
    return v.visit_Strategy(this);
  }

  public jjtraveler.Visitable visit(jjtraveler.Visitable any) throws jjtraveler.VisitFailure {
    if(any instanceof @fullClassName(operator)@) {
      return sub.visit(any);
    } else {
      throw new jjtraveler.VisitFailure(msg);
    }
  }

  public void visit() throws jjtraveler.VisitFailure {
    if(getEnvironment().getSubject() instanceof @fullClassName(operator)@) {
      ((tom.library.sl.Strategy)sub).visit();
      return;
    } else {
      throw new jjtraveler.VisitFailure(msg);
    }
  }
}
]%);
  }

  public String generateMapping() {
    return %[
%op Strategy @className()@(s:Strategy) {
  is_fsym(t) { (t!=null) && t instanceof (@fullClassName()@)}
  make(s) { new @fullClassName()@(s) }
}
]%;
  }

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
