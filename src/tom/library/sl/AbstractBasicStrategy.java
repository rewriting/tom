/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2009, INRIA
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
package tom.library.sl;

/**                                                                                                                
 * An abstract implementation of Strategy, used 
 * as a super class by the class generated from %strategy    
 *
 */

public abstract class AbstractBasicStrategy extends AbstractStrategy {

  protected Strategy any;
  
  public AbstractBasicStrategy(Strategy v) {
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
