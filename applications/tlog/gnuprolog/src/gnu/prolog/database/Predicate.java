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
package gnu.prolog.database;
import java.util.*;
import gnu.prolog.term.*;

/** Predicate in database
  * @author Contantine A Plotnikov
  */
public class Predicate
{
  /** type of predicate is not yet set */
  public static final int UNDEFINED    = -1;
  /** predicate is a control construct. This type of predicate could be
    * created only during initialization. */
  public static final int CONTROL      = 0;
  /** predicate is a build in. This type of predicate could be
    * created only during initialization. */
  public static final int BUILD_IN     = 1;
  /** predicate is a user defined predicate. */
  public static final int USER_DEFINED = 2;
  /** predicate is a user defined predicate defined in Java Class. */
  public static final int EXTERNAL     = 3;

  /** type of predicate. It should be either UNDEFINED, CONTROL ,BUILD_IN,
    * USER_DEFINED or EXTERNAL */
  int type = UNDEFINED;
  /** a tag of predicate head */
  CompoundTermTag tag;
  /** list of clauses for this predicate */
  ArrayList clauses = new ArrayList();
  /** flag which indicate that clauses was added for this predicate */
  boolean propertiesLocked = false;
  /** dynamic property of predicate */
  boolean dynamicFlag = false;
  /** class name for external predicate */
  String  javaClassName;
  /** set files where this predicate is defined */
  HashSet files = new HashSet();
  /** current module */
  Module module;

  /** constructor of predicate */
  public Predicate(Module module, CompoundTermTag tag)
  {
    this.tag = tag;
    this.module = module;
  }

  /** get clauses of predicate */
  public List getClauses()
  {
    return Collections.unmodifiableList(clauses);
  }

  /** get type of predicate
    * @return type of predicate
    */
  public int getType()
  {
    return type;
  }

  /** set type of predicate
    * @param type type of predicate
    * @throws IllegalStateException if predicate type is already set
    */
  public void setType(int type)
  {
    if (this.type != UNDEFINED)
    {
      throw new IllegalStateException("Type of predicate is already set.");
    }
    this.type = type;
  }

  /** Get name of Java class that defines this predicate.
    * @return  true if predicate is external, false otherwise.
    */
  public String getJavaClassName()
  {
    return javaClassName;
  }

  /** set java class name of the predicate.
    */
  public void setJavaClassName(String javaClassName)
  {
    if (this.javaClassName != null)
    {
      throw new IllegalStateException("Java class name could not be changed after it was set.");
    }
    switch (type)
    {
    case CONTROL:
    case BUILD_IN:
    case EXTERNAL:
      break;
    default:
      throw new IllegalStateException("Java class name could be only for control construct, build in  or external predicate.");
    }
    this.javaClassName = javaClassName;
    propertiesLocked = true;
  }

  /** get functor of predicate */
  public AtomTerm getFunctor()
  {
    return tag.functor;
  }
  /** get arity of predicate */
  public int getArity()
  {
    return tag.arity;
  }
  /** get tag of predicate */
  public CompoundTermTag getTag()
  {
    return tag;
  }

  /** Add clause for predicate at the end. This method simply add clause to predicate.
    * No modification to term is done. It even is not copied.
    * @param clause a clause to add
    */
  public void addClauseLast(Term clause)
  {
    if (type != USER_DEFINED)
    {
      throw new IllegalStateException("clauses could be added only to user defined predicate");
    }
    propertiesLocked = true;
    clauses.add(clause);
    module.predicateUpdated(tag);
  }

  /** Add clause for predicate at the heginning. This method simply add clause to predicate.
    * No modification to term is done. It even is not copied.
    * @param clause a clause to add
    */
  public void addClauseFirst(Term clause)
  {
    if (type != USER_DEFINED)
    {
      throw new IllegalStateException("clauses could be added only to user defined predicate");
    }
    propertiesLocked = true;
    if (clauses.size() == 0) // bug workaround
    {
      clauses.add(clause);
    }
    else
    {
      clauses.add(0,clause);
    }
    module.predicateUpdated(tag);
  }

  /** Remove clause for predicate.
    * This method remove first clause which is identical to clause.
    * @param clause a clause to remove
    */
  public void removeClause(Term clause)
  {
    clauses.remove(clause);
    module.predicateUpdated(tag);
  }

  /** Check if properties of predicate could be changed at this moment
    * @return true if properties of predicate could be changed at this moment
    */
  public boolean arePropertiesLocked()
  {
    return propertiesLocked;
  }


  /** Check if predicate is dynamic.
    * @return  true if predicate is dynamic, false otherwise.
    */
  public boolean isDynamic()
  {
    return dynamicFlag;
  }
  /** set "dynamic" property of predicate to true.
    * This method should be called first time before any clause was added.
    * @param new value of "dynamic" property of predicate
    * @throws IllegalStateException if there were clauses added
    *         to predicate and dynamic flag was not set before.
    *         See 7.4.2.1 clause of ISO Prolog.
    */
  public void setDynamic()
  {
    if (type != USER_DEFINED)
    {
      throw new IllegalStateException("only user defined predicate may be declared dynamic");
    }
    if (this.dynamicFlag)
    {
      // ignore if flag already set
      return;
    }
    if (propertiesLocked)
    {
      throw new IllegalStateException("dynamic property of predicate could not be changed");
    }
    this.dynamicFlag = true;
  }

  static final CompoundTermTag clauseTag = CompoundTermTag.get(":-",2);
  static final CompoundTermTag disjunctionTag = CompoundTermTag.get(";",2);
  static final CompoundTermTag conjunctionTag = CompoundTermTag.get(",",2);
  static final CompoundTermTag ifTag = CompoundTermTag.get("->",2);
  static final CompoundTermTag callTag = CompoundTermTag.get("call",1);
  static final AtomTerm trueAtom = AtomTerm.get("true");
  
  public static Term prepareClause(Term clause)
  {
    clause = clause.dereference();
    Term head;
    Term body;
    if (clause instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)clause;
      if (ct.tag == clauseTag)
      {
        head = prepareHead(ct.args[0].dereference());
        body = prepareBody(ct.args[1].dereference());
      }
      else
      {
        head = prepareHead(clause);
        body = trueAtom;
      }
    }
    else if(clause instanceof AtomTerm)
    {
      head = prepareHead(clause);
      body = trueAtom;
    }
    else 
    {
      throw new IllegalArgumentException("not callable");
    }
    return new CompoundTerm(clauseTag, head, body);
  }
  public static Term prepareHead(Term head)
  {
    if (head instanceof VariableTerm)
    {
      throw new IllegalArgumentException("head cannot be a variable");
    }
    else if (head instanceof AtomTerm)
    {
      return head;
    }
    else if (head instanceof CompoundTerm)
    {
      return head;
    }
    else
    {
      throw new IllegalArgumentException("head cannot be converted to predication");
    }
  }
  public static Term prepareBody(Term body)
  {
    if (body instanceof VariableTerm)
    {
      return new CompoundTerm(callTag,body);
    }
    else if (body instanceof AtomTerm)
    {
      return body;
    }
    else if (body instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)body;
      if (ct.tag == conjunctionTag ||
          ct.tag == disjunctionTag ||
          ct.tag == ifTag)
      {
        return new CompoundTerm(ct.tag, 
                                prepareBody(ct.args[0].dereference()),
                                prepareBody(ct.args[1].dereference()));
      }
      return body;
    }
    else
    {
      throw new IllegalArgumentException("body cannot be converted to goal");
    }
  }

}
