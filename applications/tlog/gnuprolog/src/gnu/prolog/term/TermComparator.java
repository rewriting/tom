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

/** comparator for two term */
public class TermComparator implements java.util.Comparator
{
  int currentIdx;
  java.util.HashMap orderMap = new java.util.HashMap();

  /** Compares its two arguments for order.  Returns a negative integer,
    * zero, or a positive integer as the first argument is less than, equal
    * to, or greater than the second.
    * <p>
    * The implementor must ensure that sgn(compare(x, y)) == -sgn(compare(y,
    * x)) for all x and y.  (This implies that compare(x, y) must throw
    * an exception if and only if compare(y, x) throws an exception.)
    * <p>
    * The implementor must also ensure that the relation is transitive:
    * ((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0)) implies
    * compare(x, z)>0. 
    * <p>
    * The implementer must also ensure that x.equals(y) implies that 
    * compare(x, y) == 0.  Note that the converse is not necessarily true.
    * <p>
    * Finally, the implementer must ensure that compare(x, y) == 0 implies
    * that sgn(compare(x, z)) == sgn(compare(y, z)), for all z.
    * 
    * @return a negative integer, zero, or a positive integer as the
    * 	       first argument is less than, equal to, or greater than the
    *	       second. 
    * @exception ClassCastException the arguments' types prevent them from
    * 		  being compared by this Comparator.
    * @since   JDK1.2
    */
  public int compare(Object o1, Object o2)
  {
    Term t1 = ((Term)o1).dereference();
    Term t2 = ((Term)o2).dereference();
    if (t1 == t2)
    {
      return 0;
    }
    int ty1 = t1.getTermType();
    int ty2 = t2.getTermType();
    if (ty1 != ty2)
    {
      return ty1 - ty2;
    }
    switch (ty1)
    {
    case Term.VARIABLE   :
    case Term.JAVA_OBJECT:
      Integer i1 = (Integer)orderMap.get(t1);
      if (i1 == null)
      {
        i1 = new Integer(currentIdx++);
        orderMap.put(t1,i1);
      }
      Integer i2 = (Integer)orderMap.get(t2);
      if (i2 == null)
      {
        i2 = new Integer(currentIdx++);
        orderMap.put(t2,i2);
      }
      return i1.intValue() - i2.intValue();
    case Term.FLOAT      :
      FloatTerm ft1 = (FloatTerm)t1;
      FloatTerm ft2 = (FloatTerm)t2;
      double fr = ft1.value-ft2.value;
      if (fr < 0)
      {
        return -1;
      }
      else if (fr > 0)
      {
        return 1;
      }
      else 
      {
        return 0;
      }
    case Term.INTEGER    :
      IntegerTerm it1 = (IntegerTerm)t1;
      IntegerTerm it2 = (IntegerTerm)t2;
      return it1.value - it2.value;
    case Term.ATOM       :
      AtomTerm at1 = (AtomTerm)t1;
      AtomTerm at2 = (AtomTerm)t2;
      return at1.value.compareTo(at2.value);
    case Term.COMPOUND   :
      CompoundTerm ct1 = (CompoundTerm)t1;
      CompoundTerm ct2 = (CompoundTerm)t2;
      CompoundTermTag tag1 = ct1.tag;
      CompoundTermTag tag2 = ct2.tag;
      int ar1 = tag1.arity;
      int ar2 = tag2.arity;
      if (ar1 != ar2)
      {
        return ar1 - ar2;
      }
      AtomTerm fu1 = tag1.functor;
      AtomTerm fu2 = tag2.functor;
      if (fu1 != fu2)
      {
        return fu1.value.compareTo(fu2.value);
      }
      Term args1[] = ct1.args;
      Term args2[] = ct2.args;
      for (int i=0; i<ar1; i++)
      {
        int rc = compare(args1[i],args2[i]);
        if (rc != 0)
        {
          return rc;
        }
      }
    }
    return 0;
  }
}
