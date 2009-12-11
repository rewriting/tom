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
import java.util.*;

public class TermUtils
{
  /** chaeck if one term is variant of another */
  public static boolean isVariant(Term term1, Term term2)
  {
    HashMap map = new HashMap();
    return isVariant(term1, term2, map);
  }
  private static boolean isVariant(Term term1, Term term2, HashMap map)
  {
    term1 = term1.dereference();
    term2 = term2.dereference();
    if (term1 instanceof VariableTerm)
    {
      if (!(term2 instanceof VariableTerm))
      {
        return false;
      }
      Term s = (Term)map.get(term1);
      if (s != null)
      {
        return s == term2;
      }
      else
      {
        map.put(term1,term2);
        return true;
      }
    }
    else if (term1 == term2)
    {
      return true;
    }
    else if (term1.getClass() != term2.getClass())
    {
      return false;
    }
    else if (term1 instanceof FloatTerm)
    {
      FloatTerm f1 = (FloatTerm)term1;
      FloatTerm f2 = (FloatTerm)term2;
      return f1.value == f2.value;
    }
    else if (term1 instanceof IntegerTerm)
    {
      IntegerTerm i1 = (IntegerTerm)term1;
      IntegerTerm i2 = (IntegerTerm)term2;
      return i1.value == i2.value;
    }
    else if (term1 instanceof JavaObjectTerm)
    {
      JavaObjectTerm i1 = (JavaObjectTerm)term1;
      JavaObjectTerm i2 = (JavaObjectTerm)term2;
      return i1.value == i2.value;
    }
    else if (term1 instanceof CompoundTerm)
    {
      CompoundTerm c1 = (CompoundTerm)term1;
      CompoundTerm c2 = (CompoundTerm)term2;
      if (c1.tag != c2.tag)
      {
        return false;
      }
      for (int i=c2.tag.arity-1;i>=0;i--)
      {
        if (!isVariant(c1.args[i], c2.args[i], map))
        {
          return false;
        }
      }
      return true;
    }
    else // unknown type
    {
      throw new IllegalArgumentException("unknown term type");
    }

  }

  static final CompoundTermTag existsTag = CompoundTermTag.get("^",2);
  
  /** get variable set */
  public static void getVariableSet(Term term, Set set)
  {
    term = term.dereference();
    if (term instanceof VariableTerm)
    {
      set.add(term);
    }
    else if (term instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)term;
      for (int i = ct.tag.arity-1;i>=0;i--)
      {
        getVariableSet(ct.args[i], set);
      }
    }
  }
  
  /** get existential variable set */
  public static Term getExistentialVariableSet(Term term, Set set)
  {
    term = term.dereference();
    if (term instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)term;
      if (ct.tag == existsTag)
      {
        getVariableSet(ct.args[0],set);
        return getExistentialVariableSet(ct.args[1], set);
      }
      return ct;
    }
    else
    {
      return term;
    }
  }
  
  /** get free variables of term1 with respect to term2 
    * @return term1 w/o existential specifiers
     */
  public static Term getFreeVariableSet(Term term1, Term term2, Set set)
  {
    HashSet bound = new HashSet();
    getVariableSet(term2, bound);
    Term rc = getExistentialVariableSet(term1, bound);
    getVariableSet(term1,set);
    set.removeAll(bound);
    return rc;
  }

  /** get witness of variable set, now just list of variables */
  public static Term getWitness(Set set)
  {
    Term rc = AtomTerm.emptyList;
    Iterator i = set.iterator();
    while (i.hasNext())
    {
      rc = CompoundTerm.getList((Term)i.next(), rc);
    }
    return rc;
  }

}
