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
/** Term which is embeding JavaObject
  * This term should never participate in text IO operations.
  * It is always created as result of function calls.
  * It could be unifed only with variable or other JavaObjectTerm that 
  * has value identical (==) to value of this object.
  */

public class JavaObjectTerm extends AtomicTerm
{
  /** value of the term */
  public final Object value;
  
  public JavaObjectTerm(Object object)
  {
    value = object;
  }

  /** get type of term 
    * @return type of term
    */
  public int getTermType()
  {
    return JAVA_OBJECT;
  }
}
