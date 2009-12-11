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

/** prolog code 
  */
public class Predicate_char_code implements PrologCode
{
  static final AtomTerm atomAtom = AtomTerm.get("atom");
  static final AtomTerm integerAtom = AtomTerm.get("integer");
  static final AtomTerm notLessThenZeroAtom = AtomTerm.get("not_less_then_zero");
  static final AtomTerm characterCodeAtom = AtomTerm.get("character_code");
  static final AtomTerm characterAtom = AtomTerm.get("character");
  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public int execute(Interpreter interpreter, boolean backtrackMode, gnu.prolog.term.Term args[]) 
         throws PrologException
  {
    Term tchar = args[0];
    Term tcode = args[1];
    if (tchar instanceof VariableTerm)
    {
      VariableTerm vchar = (VariableTerm)tchar;
      if (tcode instanceof VariableTerm)
      {
        PrologException.instantiationError();
      }
      else if (!(tcode instanceof IntegerTerm))
      {
        PrologException.typeError(integerAtom, tcode);
      }
      IntegerTerm icode = (IntegerTerm)tcode;
      if (icode.value < 0 || 0xffff < icode.value )
      {
        PrologException.representationError(characterCodeAtom);
      }
      StringBuffer bu = new StringBuffer(1);
      bu.append((char)icode.value);
      interpreter.addVariableUndo(vchar);
      vchar.value = AtomTerm.get(bu.toString());
      return SUCCESS_LAST;
    }
    else if (tchar instanceof AtomTerm)
    {
      AtomTerm achar = (AtomTerm)tchar;
      if (achar.value.length() != 1)
      {
        PrologException.typeError(characterAtom,achar);
      }
      IntegerTerm code = IntegerTerm.get((int)achar.value.charAt(0));
      return interpreter.unify(code,tcode);
    }
    else
    {
      PrologException.typeError(characterAtom,tchar);
    }
    return FAIL; // fake return
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


