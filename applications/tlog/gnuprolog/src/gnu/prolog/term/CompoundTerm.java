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
import java.util.List;
/** compound term.
  * @author Constantine Plotnilkov
  * @version 0.0.1
  */
public class CompoundTerm extends Term
{
  /** tag for list */
  public static final CompoundTermTag listTag = CompoundTermTag.get(".",2);
  /** tag for conjunction */
  public static final CompoundTermTag conjunctionTag = CompoundTermTag.get(",",2);
  /** tag for disjunction */
  public static final CompoundTermTag disjunctionTag = CompoundTermTag.get(";",2);
  /** tag for if */
  public static final CompoundTermTag ifTag = CompoundTermTag.get("->",2);

  public static boolean isListPair(Term term)
  {
    if (term instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)term;
      return ct.tag == listTag;
    }
    else
    {
      return false;
    }
  }
  
  /** get prolog list by java list */
  public static Term getList(List list)
  {
    Term tlist = AtomTerm.emptyList;
    for (int i=list.size()-1;i>=0;i--)
    {
      tlist = getList((Term)list.get(i),tlist);
    }
    return tlist;
  }
  

  /** get conjunction term
    * @param head head term
    * @param tail tail term
    * @return ','(head, tail) term
    */
  public static CompoundTerm getConjunction(Term head, Term tail)
  {
    CompoundTerm rc = new CompoundTerm(conjunctionTag, head, tail);
    return rc;
  }
  

  /** get disjunction term
    * @param head head term
    * @param tail tail term
    * @return ','(head, tail) term
    */
  public static CompoundTerm getDisjunction(Term head, Term tail)
  {
    CompoundTerm rc = new CompoundTerm(disjunctionTag, head, tail);
    return rc;
  }
  
  /** get list pair
    * @param head head term
    * @param tail tail term
    * @return '.'(head, tail) term
    */
  public static CompoundTerm getList(Term head, Term tail)
  {
    CompoundTerm rc = new CompoundTerm(listTag, head, tail);
    return rc;
  }
  
  /** get term with specified term tag and arguments.
    * @param tg tag of new term
    * @param arg1 1st argument of term 
    */
  public CompoundTerm(CompoundTermTag tg,Term arg1)
  {
    this(tg, new Term[]{arg1});
  }

  /** get term with specified term tag and arguments.
    * @param tg tag of new term
    * @param arg1 1st argument of term 
    * @param arg2 2nd argument of term 
    */
  public CompoundTerm(CompoundTermTag tg,Term arg1,Term arg2)
  {
    this(tg, new Term[]{arg1,arg2});
  }

  /** get term with specified term tag and arguments.
    * @param tg tag of new term
    * @param arg1 1st argument of term 
    * @param arg2 2nd argument of term 
    * @param arg3 3rd argument of term 
    */
  public CompoundTerm(CompoundTermTag tg,Term arg1,Term arg2,Term arg3)
  {
    this(tg, new Term[]{arg1,arg2,arg3});
  }


  /** get term with specified functor and arity
    * @param functor a functor of new term
    * @param arity rity of new term
    * @return new term
    */
  public CompoundTerm(String functor, int arity)
  {
    this(AtomTerm.get(functor), arity);
  }

  /** get term with specified functor and arity
    * @param functor a functor of new term
    * @param arity rity of new term
    * @return new term
    */
  public CompoundTerm(AtomTerm functor, int arity)
  {
    this(CompoundTermTag.get(functor,arity));
  }

  
  /** get term with specified term functor and arguments.
    * @param functor a functor of new term
    * @param args argumetes of term, this array is directly assigned to
    *             term and any changes that are done to array change term.
    * @return new term
    */
  public CompoundTerm(AtomTerm functor,Term args[])
  {
    this(CompoundTermTag.get(functor,args.length),args);
  }

  /** get term with specified term functor and arguments.
    * @param functor a functor of new term
    * @param args argumetes of term, this array is directly assigned to
    *             term and any changes that are done to array change term.
    * @return new term
    */
  public CompoundTerm(String functor,Term args[])
  {
    this(CompoundTermTag.get(functor,args.length),args);
  }

  /** term tag */
  public final CompoundTermTag tag;
  /** term argumets */
  public final Term   args[];

  /** a contructor
    * @param tag tag of term
    */
  public CompoundTerm(CompoundTermTag tag)
  {
    this.tag = tag;
    args = new Term[tag.arity];
  }

  /** a constructor
    * @param tag tag of term
    * @param args arguments of term
    */
  public CompoundTerm(CompoundTermTag tag, Term args[])
  {
    this.tag = tag;
    this.args = args;
  }

  /** clone the object using clone context
    * @param context clone context
    * @return cloned term
    */
  public Term clone(TermCloneContext context)
  {
    CompoundTerm term = (CompoundTerm)context.getTerm(this);
    if (term == null)
    {
      term = new CompoundTerm(tag);
      context.putTerm(this, term);
      for (int i=0; i<args.length; i++ )
      {
        if (args[i] != null)
        {
          term.args[i] = args[i].clone(context);
        }
      }
    }
    return term;
  }


  /** get type of term 
    * @return type of term
    */
  public int getTermType()
  {
    return COMPOUND;
  }

}


