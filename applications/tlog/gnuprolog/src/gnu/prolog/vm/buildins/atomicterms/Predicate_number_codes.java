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
package gnu.prolog.vm.buildins.atomicterms;
import gnu.prolog.term.*;
import gnu.prolog.vm.*;
import gnu.prolog.io.*;

/** prolog code 
  */
public class Predicate_number_codes implements PrologCode
{
  static final AtomTerm numberExpectedAtom = AtomTerm.get("number_expected"); 
  static final AtomTerm numberAtom         = AtomTerm.get("number"); 
  static final AtomTerm characterCodeAtom  = AtomTerm.get("character_code"); 
  static final AtomTerm characterCodeListAtom = AtomTerm.get("character_code_list"); 

  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public int execute(Interpreter interpreter, boolean backtrackMode, gnu.prolog.term.Term args[]) 
         throws PrologException
  {
    Term number = args[0];
    Term list = args[1];
    if (!(number instanceof VariableTerm || 
          number instanceof IntegerTerm || 
          number instanceof FloatTerm))
    {
      PrologException.typeError(numberAtom, number);
    }
    
    String numStr = getNumberString(list, (number instanceof VariableTerm));
    if (numStr != null)
    {
      Term res = null;
      try
      {
        res = TermReader.stringToTerm(numStr);
      }
      catch(ParseException ex)
      {
        PrologException.syntaxError(ex);
      }
      if (!(res instanceof IntegerTerm || res instanceof FloatTerm))
      {
        PrologException.syntaxError(numberExpectedAtom);
      }
      return interpreter.unify(res, number);
    }
    else
    {
      numStr = TermWriter.toString(number);
      Term res = AtomTerm.emptyList;
      for(int i=numStr.length()-1;i>=0;i--)
      {
        res = CompoundTerm.getList(IntegerTerm.get(numStr.charAt(i)), res);
      }
      return interpreter.unify(list,res);
    }
  }

  /** returns null if illegal chracter sequenca */
  private static String getNumberString(Term list, boolean numberIsVariable) throws PrologException
  {
    StringBuffer bu = new StringBuffer();
    Term cur = list;
    while (cur != AtomTerm.emptyList )
    {
      if (cur instanceof VariableTerm)
      {
        if (numberIsVariable)
        {
          PrologException.instantiationError();
        }
        else
        {
          return null;
        }
      }
      if (!CompoundTerm.isListPair(cur))
      {
        PrologException.domainError(characterCodeListAtom,list);
      }
      CompoundTerm ct = (CompoundTerm)cur;
      Term head = ct.args[0].dereference();
      cur = ct.args[1].dereference();
      if (head instanceof VariableTerm)
      {
        if (numberIsVariable)
        {
          PrologException.instantiationError();
        }
        else
        {
          return null;
        }
      }
      if (!(head instanceof IntegerTerm))
      {
        PrologException.representationError(characterCodeAtom);
      }
      IntegerTerm ch = (IntegerTerm)head;
      if (ch.value < 0 || 0xffff < ch.value)
      {
        PrologException.representationError(characterCodeAtom);
      }
      bu.append((char)ch.value);
    }
    return bu.toString();
  }

  /** this method is called when code is installed to the environment
    * code can be installed only for one environment.
    * @param environment environemnt to install the predicate
    */
  public void install(Environment env)
  {

  }

  /** this method is called when code is uninstalled from the environment
    * @param environment environemnt to install the predicate
    */
  public void uninstall(Environment env)
  {
  }
    
}

