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
import java.util.HashMap;
import java.io.Serializable;
/** Atom term. The object of this class represent prolog atom.
  * @author Constantin Plotnikov
  * @version 0.0.1
  */
public class AtomTerm extends AtomicTerm implements Serializable
{
  /** a map from string to atom */
  private final static HashMap string2atom = new HashMap();
  /** empty list atom */
  public final static AtomTerm emptyList = get("[]");
  /** empty curly atom */
  public final static AtomTerm emptyCurly = get("{}");
  /** cut atom */
  public final static AtomTerm cut = get("!");
  /** get atom term
    * @param s string reprentation of atom.
    */
  public static AtomTerm get(String s)
  {
    synchronized(string2atom)
    {
      AtomTerm atom = (AtomTerm)string2atom.get(s);
      if (atom == null)
      {
        atom = new AtomTerm(s);
        string2atom.put(s,atom);
      }
      return atom;
    }
  }
  
  private static StringBuffer chbu = new StringBuffer(1);
  /** get atom term
    * @param s string reprentation of atom.
    */
  public static AtomTerm getChar(char ch)
  {
    synchronized(chbu)
    {
      chbu.setLength(0);
      chbu.append(ch);
      return get(chbu.toString());
    }
  }
  
  /** Return an object to replace the object extracted from the stream.
    * The object will be used in the graph in place of the original.
    * @return resloved object
    * @see java.io.Resolvable
    */
  public Object readResolve()
  {
    return get(value);
  }
  /** value of atom */
  final public String value;
  /** a constructor.
    * @param value value of atom
    */
  private AtomTerm(String value) // constructor is private to package
  {
    this.value = value;
  }

  /** get type of term 
    * @return type of term
    */
  public int getTermType()
  {
    return ATOM;
  }


}


