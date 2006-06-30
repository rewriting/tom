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

package tom.gom.backend.shared;

import tom.gom.backend.TemplateClass;
import tom.gom.adt.objects.types.*;

public class AbstractTypeTemplate extends TemplateClass {
  ClassName visitor;
  ClassNameList sortList;

  public AbstractTypeTemplate(ClassName className,
                              ClassName visitor,
                              ClassNameList sortList) {
    super(className);
    this.visitor = visitor;
    this.sortList = sortList;
  }

  public String generate() {
    StringBuffer out = new StringBuffer();

    out.append(
%[
package @getPackage()@;

public abstract class @className()@ implements shared.SharedObjectWithID, jjtraveler.Visitable {
  private int uniqueID;

  public abstract aterm.ATerm toATerm();

  public abstract String symbolName();

  public abstract String toString();

  public int getUniqueIdentifier() {
    return uniqueID;
  }

  public void setUniqueIdentifier(int uniqueID) {
    this.uniqueID = uniqueID;
  }

  abstract public @className()@ accept(@fullClassName(visitor)@ v) throws jjtraveler.VisitFailure;
}
]%);

    return out.toString();
  }

}
