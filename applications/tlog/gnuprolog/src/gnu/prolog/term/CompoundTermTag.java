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
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/** A tag of compound term. A tag consists of functor and arity.
  * All tags are unique in one JVM.
  * @author Constatine Plotinikov
  * @version 0.0.1
  */
final public class CompoundTermTag implements java.io.Serializable
{
  /** atom to rag map */
  private static final Map atom2tag = new HashMap();

  // some standard tags
  /** list tag */
  public final static CompoundTermTag list = get(".",2);
  /** comma tag */
  public final static CompoundTermTag comma = get(",",2);
  /** clause tag */
  public final static CompoundTermTag clause = get(":-",2);
  /** derective tag */
  public final static CompoundTermTag directive = get(":-",1);
  /** '{}'/1 tag */
  public final static CompoundTermTag curly1 = get("{}",1);
  /** '-'/2 tag */
  public final static CompoundTermTag minus2 = get("-",2);
  /** '/'/2 tag */
  public final static CompoundTermTag divide2 = get("/",2);

  /** get predicate indicator for this tag */
  public CompoundTerm getPredicateIndicator()
  {
    return new CompoundTerm(divide2, functor, IntegerTerm.get(arity));
  }

  /** get compound term tag
    * @param predicateIndicator a term which represent a predicate indicator of term
    * @return a tag that have specified by a term which represent a predicate indicator of term
    * @throws IllegalArgumentException if term is not a valid predicate indicator
    */
  public static CompoundTermTag get(Term t)
  {
    if (!(t instanceof CompoundTerm))
    {
      throw new IllegalArgumentException();
    }
    CompoundTerm ct = (CompoundTerm)t;
    if (!(ct.args[0] instanceof AtomTerm ) || !(ct.args[1] instanceof IntegerTerm))
    {
      throw new IllegalArgumentException();
    }
    return get((AtomTerm)ct.args[0], ((IntegerTerm)ct.args[1]).value);
  }

  /** check if term is predicate indicator 
    * @param term term to check
    * @return true if term is predicate indicator
    */

  public static boolean isPredicateIndicator(Term term)
  {
    if (!(term instanceof CompoundTerm))
    {
      return false;
    }
    CompoundTerm ct = (CompoundTerm)term;
    if (!(ct.args[0] instanceof AtomTerm ) || !(ct.args[1] instanceof IntegerTerm))
    {
      return false;
    }
    return true;
  }

  /** get compound term tag
    * @param functor functor of tag
    * @param arity arity of tag
    * @return a tag that have specified arity ond functor
    */
  public static CompoundTermTag get(String functor, int arity)
  {
     return get(AtomTerm.get(functor),arity);
  }
  /** get compound term tag
    * @param functor functor of tag
    * @param arity arity of tag
    * @return a tag that have specified arity ond functor
    */
  public static CompoundTermTag get(AtomTerm functor, int arity)
  {
    synchronized (atom2tag)
    {
      List ctgs = (List)atom2tag.get(functor);
      CompoundTermTag tg;
      if (ctgs != null)
      {
        Iterator e = ctgs.iterator();
        while (e.hasNext())
        {
          tg = (CompoundTermTag)e.next();
          if (tg.arity == arity)
          {
            return tg;
          }
        }
        tg = new CompoundTermTag(functor,arity);
        ctgs.add(tg);
        return tg;
      }
      tg = new CompoundTermTag(functor,arity);
      ctgs = new ArrayList();
      ctgs.add(tg);
      atom2tag.put(functor,ctgs);
      return tg;
    }
  }

  /** a functor of term */
  public final AtomTerm functor;

  /** arity of term. Arity of tag could be 0. But in this case this tag
    * could not be used to cunstruct the compound terms.
    */
  public final int      arity;

  /** a constructor
    * @param f functor of term
    * @param a arity of term
    */
  private CompoundTermTag(AtomTerm f, int a)
  {
    functor = f;
    arity   = a;
  }

  /** Return an object to replace the object extracted from the stream.
    * The object will be used in the graph in place of the original.
    * @return resloved object
    * @see java.io.Resolvable
    */
  public Object readResolve()
  {
    return get(functor,arity);
  }

  /** convert tag to string */
  public String toString()
  {
    return functor.value+"/"+arity;
  }
}

