/* GNU Prolog for Java
 * Copyright (C) 1997-1999  Constantine Plotnikov
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA. The text ol license can be also found 
 * at http://www.gnu.org/copyleft/lgpl.html
 */
package gnu.prolog.term;

/** base class for all terms.
  * @author Constantine Plotniokov
  * @version 0.0.1
  */
public abstract class Term implements java.io.Serializable, Cloneable
{
  public static final int UNKNOWN = -1;
  public static final int VARIABLE    = 1;
  public static final int JAVA_OBJECT = 2;
  public static final int FLOAT       = 3;
  public static final int INTEGER     = 4;
  public static final int ATOM        = 5;
  public static final int COMPOUND    = 6;

  /** clone th term.
    * @return cloned term
    */
  public Object clone()
  {
    TermCloneContext context = new TermCloneContext();
    return clone(context);
  }

  /** clone the object using clone context
    * @param context clone context
    * @return cloned term
    */
  public abstract Term clone(TermCloneContext context);

  /** dereference term.
    * @return dereferenced term
    */
  public Term dereference()
  {
    return this;
  }

  /** get type of term 
    * @return type of term
    */
  public int getTermType()
  {
    return UNKNOWN;
  }

}

