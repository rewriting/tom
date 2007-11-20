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
 * Emilie Balland  e-mail: Emilie.Balland@loria.fr
 *
 **/

package strategy.sl;

public class VisitableBuiltin<T> implements Visitable {

  private T builtin;

  public VisitableBuiltin(T builtin) {
    this.builtin = builtin;
  }

  public T getBuiltin() {
    return builtin;
  }

  public int getChildCount() {
    return 0 ;
  }

  public strategy.sl.Visitable getChildAt(int index) {
    throw new IndexOutOfBoundsException();
  }

  public strategy.sl.Visitable setChildAt(int index, strategy.sl.Visitable v) {
    throw new IndexOutOfBoundsException();
  }

  public strategy.sl.Visitable setChildren(strategy.sl.Visitable[] childs) {
    if (childs.length==0) {
      return this;
    }
    else {
      throw new IndexOutOfBoundsException();
    }
  }

  public strategy.sl.Visitable[] getChildren() {
    return new strategy.sl.Visitable[0];
  }


  public String toString() {
    return builtin.toString();
  }

}

