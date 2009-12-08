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
package gnu.prolog.vm.interpreter;
import gnu.prolog.vm.*;
import gnu.prolog.term.*;
import java.util.*;



/** prolog code 
  */
public class Predicate_call implements PrologCode
{
  Environment environment; 
  
  /** callable atom */
  public static final AtomTerm callableAtom = AtomTerm.get("callable");
  /** if tag */
  public static final CompoundTermTag ifTag          = CompoundTermTag.get("->",2);
  /** disjunction and if then else ';'('->'(_,_)_) tag */
  public static final CompoundTermTag disjunctionTag  = CompoundTermTag.get(";",2);
  /** conjunction tag */
  public static final CompoundTermTag conjunctionTag = CompoundTermTag.get(",",2);
  /** clause tag */
  public static final CompoundTermTag clauseTag       = CompoundTermTag.get(":-",2);
  /** head functor, it is completly unimportant what it is */
  public static final AtomTerm headFunctor = AtomTerm.get("$$$call$$$");
  /** term arry constant */
  public static final Term termArrayType[] = new Term[0];

  /** call term backtrack info */
  public static class CallTermBacktrackInfo extends BacktrackInfo
  {
    public CallTermBacktrackInfo(Interpreter in, PrologCode code, Term args[], Term callTerm)
    {
      super(in.getUndoPosition(), -1);
      this.code = code;
      this.args = args;
      this.callTerm = callTerm;
    }
    
    /** prolog code being tried */
    PrologCode code;
    /** argument of prolog code */
    Term args[];
    /** Term passed as parameter */
    Term callTerm;
    /** environment */
    Environment environment;
    
    protected void finalize() throws Throwable
    {
      super.finalize();
      if (code != null)
      {
        code.uninstall(environment);
      }
    }
  }
  
  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public int execute(Interpreter interpreter, boolean backtrackMode, gnu.prolog.term.Term args[]) 
         throws PrologException
  {
    return staticExecute(interpreter, backtrackMode, args[0]);
  }
  /** this method is used for execution of code
    * @param interpreter interpreter in which context code is executed 
    * @param backtrackMode true if predicate is called on backtracking and false otherwise
    * @param args arguments of code
    * @return either SUCCESS, SUCCESS_LAST, or FAIL.
    */
  public static int staticExecute(Interpreter interpreter, boolean backtrackMode, Term arg) 
         throws PrologException
  {
    CallTermBacktrackInfo cbi = backtrackMode?(CallTermBacktrackInfo)interpreter.popBacktrackInfo():null;
    BacktrackInfo tbi;
    PrologCode code; // code to call
    Term       args[]; // arguments of code
    Term       callTerm; // term being called
    if (cbi == null)
    {
      callTerm = arg;
      if (callTerm instanceof VariableTerm)
      {
        PrologException.instantiationError();
      }
      ArrayList argumentVariables = new ArrayList();
      ArrayList arguments = new ArrayList();
      Term body;
      try
      {
        body = getClause(callTerm, argumentVariables, arguments);
      }
      catch(IllegalArgumentException ex) // term not callable
      {
        PrologException.typeError(callableAtom,callTerm);
        return -1; // fake return
      }
      Term headArgs[] = (Term[])argumentVariables.toArray(termArrayType);
      Term head = new CompoundTerm(headFunctor, headArgs);
      Term clause = new CompoundTerm(clauseTag, head, body);
      args = (Term[])arguments.toArray(termArrayType);
      ArrayList clauses = new ArrayList(1);
      clauses.add(clause);
      code = InterpretedCodeCompiler.compile(clauses);
      code.install(interpreter.environment);
      //System.err.println("converted clause");
      //System.err.println(gnu.prolog.io.TermWriter.toString(clause));
      //System.err.println("converted code");
      //System.err.print(code);
    }
    else
    {
      cbi.undo(interpreter);
      args = cbi.args;
      code = cbi.code;
      callTerm = cbi.callTerm;
    }
    int rc = code.execute(interpreter, backtrackMode, args);
    if (rc == SUCCESS) // redo is possible
    {
      cbi = new CallTermBacktrackInfo(interpreter,code,args,callTerm);
      cbi.environment = interpreter.environment;
      interpreter.pushBacktrackInfo(cbi);
    }
    else
    {
      code.uninstall(interpreter.environment);
      if (cbi != null)
      {
        cbi.code = null;
      }
    }
    return rc;
  }
  
  /** convert callable term to clause */
  public static Term getClause(Term term, ArrayList argumentVariables, ArrayList arguments)
  {
    if (term instanceof AtomTerm)
    {
      return term;
    }
    else if (term instanceof VariableTerm)
    {
      if (!arguments.contains(term))
      {
        VariableTerm var1 = new VariableTerm();
        argumentVariables.add(var1);
        arguments.add(term);
        return var1;
      }
      return (Term)argumentVariables.get(arguments.indexOf(term));
    }
    else if (term instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)term;
      if (ct.tag == ifTag || ct.tag == conjunctionTag || ct.tag == disjunctionTag)
      {
        return new CompoundTerm(ct.tag,  
                                getClause(ct.args[0].dereference(), 
                                          argumentVariables,
                                          arguments),
                                getClause(ct.args[1].dereference(), 
                                          argumentVariables, 
                                          arguments));
      }
      Term newArgs[] = new Term[ct.tag.arity];
      for (int i=0;i<newArgs.length;i++)
      {
        Term arg = ct.args[i].dereference();
        if (!arguments.contains(arg))
        {
          newArgs[i] = new VariableTerm();
          argumentVariables.add(newArgs[i]);
          arguments.add(arg);
        }
        else
        {
          newArgs[i] = (Term)argumentVariables.get(arguments.indexOf(arg));
        }
      }
      return new CompoundTerm(ct.tag,newArgs);
    }
    else
    {
      throw new IllegalArgumentException("the term is not callable");
    }
  }

  /** this method is called when code is installed to the environment
    * code can be installed only for one environment.
    * @param environment environemnt to install the predicate
    */
  public void install(Environment env)
  {
    environment = env;
  }

  /** this method is called when code is uninstalled from the environment
    * @param environment environemnt to install the predicate
    */
  public void uninstall(Environment env)
  {
    environment = null;
  }

    
}



