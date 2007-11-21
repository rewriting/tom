/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2007, INRIA
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
 * Radu Kopetz e-mail: Radu.Kopetz@loria.fr
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/
package strategy.sl;

/**                                                                                                                
 * An abstract implementation of Strategy, used 
 * as a super class by the class generated from %strategy    
 *
 */

public abstract class BasicStrategy implements Strategy {

  protected strategy.sl.Environment environment;
  protected Strategy any;
  
  public BasicStrategy(Strategy v) {
    this.any = v;
  }
 
  //subpart of Visitable for the initialization
  public int getChildCount() {
    return 1;
  }
    
  public Strategy getChildAt(int i) {
    switch (i) {
      case 0: return any;
      default: throw new IndexOutOfBoundsException();
    }
  }

  public Strategy setChildAt(int i, Strategy child) {
    switch (i) {
      case 0: any = child; return this;
      default: throw new IndexOutOfBoundsException();
    }
  }

  public strategy.sl.Environment getEnvironment() {
    if(environment!=null) {
      return environment;
    } else {
      throw new java.lang.RuntimeException("environment not initialized");
    }
  }

  public void setEnvironment(strategy.sl.Environment env) {
    this.environment = env;
  }

  public Object visit(Environment envt, Introspector m) throws VisitFailure {
    setEnvironment(envt);
    int status = visit(m);
    if(status == Environment.SUCCESS) {
      return environment.getSubject();
    } else {
      throw new VisitFailure();
    }
  }


  public Object visit(Object any, Introspector m) throws VisitFailure {
    strategy.sl.AbstractStrategy.init(this,new strategy.sl.Environment(m));
    environment.setRoot(any);
    int status = visit(m);
    if(status == strategy.sl.Environment.SUCCESS) {
      return environment.getRoot();
    } else {
      throw new VisitFailure();
    }
  }

  public int visit(Introspector m) {
    try {
      environment.setSubject(this.visitLight(environment.getSubject(),m));
      return strategy.sl.Environment.SUCCESS;
    } catch(VisitFailure f) {
      return strategy.sl.Environment.FAILURE;
    }
  }

  public abstract Object visitLight(Object v, Introspector m) throws VisitFailure;

  public Visitable visit(Environment envt) throws VisitFailure {
    return (Visitable) visit(envt,VisitableIntrospector.getInstance());
  }

  public Visitable visit(Visitable any) throws VisitFailure{
    return (Visitable) visit(any,VisitableIntrospector.getInstance());
  }

  public Visitable visitLight(Visitable any) throws VisitFailure {
    return (Visitable) visitLight(any,VisitableIntrospector.getInstance());
  }

}
