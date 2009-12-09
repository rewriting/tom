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
package gnu.prolog.vm;
import gnu.prolog.term.*;
/** a prolog exception */
public class PrologException extends Exception
{
  /** term of the exception */
  Term term;
  /** message of exception */
  String msg;


  /** a constructor */
  public PrologException(Term term)
  {
    this.term = term;
  }
  
  public String getMessage()
  {
    if (msg == null)
    {
      msg = gnu.prolog.io.TermWriter.toString(term);
    }
    return msg;
  }

  /** get term of this excepion */
  public Term getTerm()
  {
    return term;
  }

  //public final static AtomTerm Atom = AtomTerm.get("");
  public final static AtomTerm instantiationErrorAtom = AtomTerm.get("instantiation_error");
  public final static AtomTerm systemErrorAtom = AtomTerm.get("system_error");
  public final static AtomTerm errorAtom = AtomTerm.get("error");
  //public final static CompoundTermTag Tag = CompoundTermTag.get("",);
  public final static CompoundTermTag errorTag = CompoundTermTag.get("error",2);
  public final static CompoundTermTag typeErrorTag = CompoundTermTag.get("type_error",2);
  public final static CompoundTermTag existenceErrorTag = CompoundTermTag.get("existence_error",2);
  public final static CompoundTermTag domainErrorTag = CompoundTermTag.get("domain_error",2);
  public final static CompoundTermTag representationErrorTag = CompoundTermTag.get("representation_error",1);
  public final static CompoundTermTag syntaxErrorTag = CompoundTermTag.get("syntax_error",1);
  public final static CompoundTermTag permissionErrorTag = CompoundTermTag.get("permission_error",3);
  
  static PrologException getError(Term term)
  {
    return new PrologException(new CompoundTerm(errorTag,term,errorAtom));
  }

  public static void systemError() throws PrologException
  {
    throw getError(systemErrorAtom);
  }
  
  public static void instantiationError() throws PrologException
  {
    throw getError(instantiationErrorAtom);
  }

  public static void typeError(AtomTerm errorType, Term errorTerm) throws PrologException
  {
    throw getError(new CompoundTerm(typeErrorTag,errorType,errorTerm));
  }

  public static void existenceError(AtomTerm errorType, Term errorTerm) throws PrologException
  {
    throw getError(new CompoundTerm(existenceErrorTag,errorType,errorTerm));
  }

  public static void domainError(AtomTerm errorType, Term errorTerm) throws PrologException
  {
    throw getError(new CompoundTerm(domainErrorTag,errorType,errorTerm));
  }

  public static void representationError(Term errorTerm) throws PrologException
  {
    throw getError(new CompoundTerm(representationErrorTag,errorTerm));
  }

  public static void syntaxError(AtomTerm term) throws PrologException
  {
    throw getError(new CompoundTerm(syntaxErrorTag,term));
  }
  
  public static void permissionError(AtomTerm operation,AtomTerm permissionType,Term culprit) throws PrologException
  {
    throw getError(new CompoundTerm(permissionErrorTag, operation, permissionType, culprit));
  }


  public static void syntaxError(gnu.prolog.io.ParseException ex) throws PrologException
  {
    syntaxError(AtomTerm.get("l"+ex.getLine()+"c"+ex.getColumn()));
  }

}

