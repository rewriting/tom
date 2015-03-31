/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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
 **/
package tom.library.sl;

/**                                                                                                                
 * An abstract implementation of Strategy, used 
 * as a super class by the class generated from %strategy    
 *
 */
/** 
 * <p>
 * Partial implementation of the strategy interface that can be used to
 * implement new basic transformations (like a rewrite rule system). This class
 * has an attribute of type Strategy which corresponds to the default strategy
 * if the transformation system cannot be applied. Note that this abstract
 * class is used to compile the <code>%strategy</code> Tom instruction.
 *
 */

public abstract class AbstractStrategyBasic extends AbstractStrategy {

  /**
   * Default strategy 
   */ 
  protected Strategy any;
  
  public AbstractStrategyBasic(Strategy v) {
    this.any = v;
  }
 
  public int visit(Introspector introspector) {
    try {
      environment.setSubject(this.visitLight(environment.getSubject(),introspector));
      return tom.library.sl.Environment.SUCCESS;
    } catch(VisitFailure f) {
      return tom.library.sl.Environment.FAILURE;
    }
  }

  public int getChildCount() {
    return 1;
  }
    
  public Visitable getChildAt(int i) {
    switch (i) {
      case 0: return any;
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
    return new Visitable[]{any};
  }

  public Visitable setChildren(Visitable[] children) {
    any = (Strategy)children[0];
    return this;
  }

}
