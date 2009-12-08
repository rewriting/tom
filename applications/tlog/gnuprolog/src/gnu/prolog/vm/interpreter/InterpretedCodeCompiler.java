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


/** compiler from predicate to interpreted code
  * This version assume following
  * <ul>
  * <li> All variables are stored in environment
  * <li> Environemt is not reused for different branches
  * <li>
  * </ul>
  */ 

public class InterpretedCodeCompiler
{
  // constant used in analisys and comilation
  /** unify tag */
  public static final CompoundTermTag unifyTag = CompoundTermTag.get("=",2);
  /** callable atom */
  public static final AtomTerm callableAtom = AtomTerm.get("callable");
  /** cut "!" atom */
  public static final AtomTerm cutAtom = AtomTerm.get("!");
  /** "true" atom */ 
  public static final AtomTerm trueAtom = AtomTerm.get("true");
  /** "false" atom " */
  public static final AtomTerm failAtom = AtomTerm.get("fail");
  /** if tag */
  public static final CompoundTermTag ifTag          = CompoundTermTag.get("->",2);
  /** disjunction and if then else ';'('->'(...,...)...) tag */
  public static final CompoundTermTag disjunctionTag  = CompoundTermTag.get(";",2);
  /** conjunction tag */
  public static final CompoundTermTag conjunctionTag = CompoundTermTag.get(",",2);
  /** throw tag */
  public static final CompoundTermTag throwTag       = CompoundTermTag.get("throw",1);
  /** catch tag */
  public static final CompoundTermTag catchTag       = CompoundTermTag.get("catch",3);
  /** call tag */
  public static final CompoundTermTag callTag        = CompoundTermTag.get("call",1);
  /** cag for caluse */
  public static final CompoundTermTag clauseTag      = CompoundTermTag.get(":-",2);
  /** array type constant for Instruction.class */
  public final static Instruction instructionArrayConstant[]  = new Instruction[0];
  /** array type constant ExceptionHandlerInfo.class */
  public final static ExceptionHandlerInfo exceptionHandlerArrayConstant[] = new ExceptionHandlerInfo[0];

  // compilation variables
  /** code so far compiled */
  ArrayList code = new ArrayList();
  /** exception handlers */
  ArrayList exceptionHandlers = new ArrayList();
  /** current code position */
  int  currentCodePosition = 0;
  /** number of already allocated reserved variables */
  int  allocatedReserved = 0;
  /** mapping from variables to environment indexes */
  HashMap variableToEnvironmentIndex = new HashMap();
  /** this predicate tag */
  CompoundTermTag codeTag;

  // analisys variables
  /** number of reserved fields, one position for saving cut */
  int numberOfReserved = 1;
  
  /** cut position stack */
  ArrayList cutPositionStack = new ArrayList();
  /** clauses to compile */
  List passedClauses;


  /** a constructor */
  public InterpretedCodeCompiler(List clauses)
  {
    passedClauses = clauses;
  }

  private int allocReserved()
  {
    return allocatedReserved++;
  }
  

  /** compile creation of term 
    * @param term term to create 
    * @throw PrologException.systemError() if term cannot be compiled
    */
  void compileTermCreation(Term term) throws PrologException
  {
    if (term instanceof VariableTerm)
    {
      iPushEnvironment(getEnvironmentIndex((VariableTerm)term));
    }
    else if (term instanceof AtomicTerm)
    {
      iPushConstant((AtomicTerm)term);
    }
    else if (term instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)term;
      for (int j=0;j<ct.args.length;j++)
      {
        compileTermCreation(ct.args[j]);
      }
      iCreateCompoundTerm(ct.tag);
    }
    else // unknown type of term
    {
      PrologException.systemError(); 
    }
  }
  
  
  /** compile head of clause 
    * @param term term to compile 
    * @throw PrologException.typeError(callable,head) if term cannot be compiled
    */
  void compileHead(Term headTerm) throws PrologException
  {
    if (headTerm instanceof AtomTerm)
    {                               
      //  do nothing
      if (codeTag == null)
      {
        codeTag = CompoundTermTag.get((AtomTerm)headTerm,0);
      }
    }
    else if (headTerm instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)headTerm;
      for (int i = 0;i<ct.tag.arity;i++)
      {
        iPushArgument(i);
        compileTermCreation(ct.args[i]);
        iUnify();
      }
      if (codeTag == null)
      {
        codeTag = ct.tag;
      }
    }
    else
    {
      PrologException.typeError(callableAtom,headTerm);
    }
  }

  /** compile body of clause 
    * @param term term to compile 
    * @throw PrologException.typeError(callable,head) if term cannot be compiled
    */
  void compileBody(Term body) throws PrologException
  {
    if (body instanceof VariableTerm) // all variable are converted to call(Var)
    {
      compileTermCreation(body);
      iCall(callTag); 
    }
    else if (body instanceof AtomTerm)
    {
      if (body == cutAtom) // cut
      {
        iCut(getCutPosition());
      }
      else if (body == trueAtom) // true
      {
        iTrue();
      }
      else if (body == failAtom) // false
      {
        iFail();
      }
      else // user defined procedure
      {
        iCall(CompoundTermTag.get((AtomTerm)body, 0)); 
      }
    }
    else if (body instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)body;
      CompoundTermTag tag = ct.tag;
      if (tag == conjunctionTag)
      {
        compileBody(ct.args[0]);
        compileBody(ct.args[1]);
      }
      else if (tag == disjunctionTag)
      {
        if (ct.args[0] instanceof CompoundTerm  // if then else
            && ((CompoundTerm)ct.args[0]).tag == ifTag)
        {
          CompoundTerm ct2 = (CompoundTerm)ct.args[0];
          compileIfThenElse(ct2.args[0],ct2.args[1],ct.args[1]);
        }
        else // just disjunction
        {
          ITryMeElse tryMeElse = iTryMeElse(-1);
          compileBody(ct.args[0]);
          IJump jump = iJump(-1);
          ITrustMe trustMe = iTrustMe();
          tryMeElse.retryPosition = trustMe.codePosition;
          compileBody(ct.args[1]);
          jump.jumpPosition = currentCodePosition;
        }
      }
      else if (tag == throwTag)
      {
        compileTermCreation(ct.args[0]);
        iThrow();
      }
      else if (tag == ifTag)
      {
        compileIfThenElse(ct.args[0],ct.args[1],failAtom);
      }
      else if (tag == catchTag)
      {
        ExceptionHandlerInfo eh 
          = new ExceptionHandlerInfo();
        // compile body
        eh.startPosition = currentCodePosition;
        compileTermCreation(ct.args[0]);
        iCall(callTag);
        eh.endPosition = currentCodePosition;
        IJump jumpOut = iJump(-1);
        // compile handler
        eh.handlerPosition = currentCodePosition;
        int coughtTermPos = allocReserved();
        int handlerCutPos = allocReserved();
        iStoreEnvironment(coughtTermPos); // save cought term in environment
        iSaveCut(handlerCutPos);
        ITryMeElse tryMeElse = iTryMeElse(-1);
        // unify head
        iPushEnvironment(coughtTermPos); // get cought from term in environment
        compileTermCreation(ct.args[1]);
        iUnify();
        iCut(handlerCutPos);
        // compile handler body
        compileTermCreation(ct.args[2]);
        iCall(callTag);
        IJump jump = iJump(-1);
        ITrustMe trustMe = iTrustMe();
        tryMeElse.retryPosition = trustMe.codePosition;
        iPushEnvironment(coughtTermPos); // get cought from term in environment
        iThrow();
        jump.jumpPosition = currentCodePosition;
        jumpOut.jumpPosition = currentCodePosition;
        addExceptionHandler(eh);
      }
      else if (tag == unifyTag)
      {
        compileTermCreation(ct.args[0]);
        compileTermCreation(ct.args[1]);
        iUnify(); 
      }
      else // user defined predicate 
      {
        int i, n = tag.arity;
        for (i=0;i<n;i++)
        {
          compileTermCreation(ct.args[i]);
        }
        iCall(tag); 
      }
    }
    else // other type
    {
      PrologException.typeError(callableAtom,body);
    }
  }

  /** compile is then else construct 
    * @param ifTerm term for if
    * @param thenTerm term for then
    * @param elseTerm else term or atom fail if just "->"
    * @throw PrologException if one of terms cannot be compiled
    */
  void compileIfThenElse(Term ifTerm, Term thenTerm, Term elseTerm) throws PrologException
  {
    int envPos = allocReserved();
    iSaveCut(envPos);
    ITryMeElse tryMeElse = iTryMeElse(-1);
    pushCutPosition(envPos);
    // compile if part
    compileBody(ifTerm);
    iCut(envPos);
    popCutPosition();
    // compile then
    compileBody(thenTerm);
    IJump jump = iJump(-1);
    ITrustMe trustMe = iTrustMe();
    tryMeElse.retryPosition = trustMe.codePosition;
    compileBody(elseTerm);
    jump.jumpPosition = currentCodePosition;
  }

  /** get reserved environment size for body term
    * @param term term to analize
    * @return amount of allocated environement
    * @throw PrologException.typeError(callable,head) if term cannot be compiled
    */
  int getReservedEnvironemt(Term body) throws PrologException
  {
    int rc = 0;
    if (body instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)body;
      CompoundTermTag tag = ct.tag;
      if (tag == conjunctionTag)
      {
        rc += getReservedEnvironemt(ct.args[0]);
        rc += getReservedEnvironemt(ct.args[1]);
      }
      else if (tag == disjunctionTag)
      {
        if (ct.args[0] instanceof CompoundTerm  // if then else
            && ((CompoundTerm)ct.args[0]).tag == ifTag)
        {
          CompoundTerm ct2 = (CompoundTerm)ct.args[0];
          rc += 1;
          rc += getReservedEnvironemt(ct2.args[0]);
          rc += getReservedEnvironemt(ct2.args[1]);
          rc += getReservedEnvironemt(ct.args[1]);
        }
        else // just disjunction
        {
          rc += getReservedEnvironemt(ct.args[0]);
          rc += getReservedEnvironemt(ct.args[1]);
        }
      }
      else if (tag == ifTag)
      {
        rc += 1;
        rc += getReservedEnvironemt(ct.args[0]);
        rc += getReservedEnvironemt(ct.args[1]);
      }
      else if (tag == catchTag)
      {
        rc += 2;
      }
    }
    return rc;
  }

  /** get all variables from term and populate variableToEnvironmentIndex
    * map
    * @param term to analize
    * @param currentEnvPositon current position in environment
    * @return current position in environemt after call
    */
  int getAllVariables(Term term, int currentEnvPositon)
  {
    if (term instanceof VariableTerm)
    {
      if (!variableToEnvironmentIndex.containsKey(term))
      {
        variableToEnvironmentIndex.put(term, new Integer(currentEnvPositon++));
      }
    }
    else if (term instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)term;
      int n = ct.tag.arity;
      Term args[] = ct.args;
      for (int i=0;i<n;i++)
      {
        currentEnvPositon = getAllVariables(args[i], currentEnvPositon);
      }
    }
    return currentEnvPositon;
  }

  /** recursively dereference term 
    * @param term to be recursively dereferenced
    * @return recursively dereferenced term
    */
  public static Term rdereferenced(Term term)
  {
    term = term.dereference();
    if (term instanceof CompoundTerm)
    {
      CompoundTerm ct1 = (CompoundTerm)term;
      int  n = ct1.tag.arity;
      Term args1[] = ct1.args;
      Term args2[] = new Term[n];
      for (int i=0; i<n; i++)
      {
        args2[i] = rdereferenced(args1[i]);
      }
      term = new CompoundTerm(ct1.tag, args2);
    }
    return term;
  }
  /** compile one clause of predicate */
  void compileClause(Term clause) throws PrologException
  {
    if (clause instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)clause;
      if (ct.tag == clauseTag)
      {
        compileHead(ct.args[0]);
        compileBody(ct.args[1]);
        return;
      }
    }
    compileHead(clause);
  }

  /** compile set of clauses to interpreted code
    * @param passedClauses clauses passed to compiler
    * @return instance of inerpreted code
    * @throw PrologException
    */
  public static PrologCode compile(List clauses) throws PrologException
  {
    return (new InterpretedCodeCompiler(clauses)).compilePredicate();
  }


  /** compile set of clauses to interpreted code
    * @param passedClauses clauses passed to compiler
    * @return instance of inerpreted code
    * @throw PrologException
    */
  PrologCode compilePredicate() throws PrologException
  {
    List clauses = new ArrayList();
    
    int i,n;
    n = passedClauses.size();
    if (n==0)
    {
      iFail();
    }
    else
    {
      Iterator iclauses;
      // dereference all clauses, it will simplify analisys a bit
      for (iclauses = passedClauses.iterator();iclauses.hasNext();)
      {
        clauses.add(rdereferenced((Term)iclauses.next())); 
      }
      // get number of reserved variables
      numberOfReserved = 1; // init number of reserved variable, 1 is reserved for cut
      for (iclauses = clauses.iterator();iclauses.hasNext();)
      {
        Term term = (Term)iclauses.next();
        if (term instanceof CompoundTerm)
        {
          CompoundTerm ct = (CompoundTerm)term;
          if (ct.tag == clauseTag)
          {
            numberOfReserved += getReservedEnvironemt(ct.args[1]);
          }
        }
      }
      // get number of variables
      int environmentSize = numberOfReserved;
      for (iclauses = clauses.iterator();iclauses.hasNext();)
      {
        environmentSize = getAllVariables((Term)iclauses.next(), environmentSize);
      }
  
      // compile predicate
      // predicate prefix
      iAllocate(environmentSize, numberOfReserved);
      int envPos = allocReserved();
      iSaveCut(envPos);
      pushCutPosition(envPos);
      // compile clauses
      n = clauses.size();
      if (n > 1) // if more then one clause
      {
        List jumps = new ArrayList();
        RetryInstruction prv = iTryMeElse(-1); 
        compileClause((Term)clauses.get(0)); 
        jumps.add(iJump(-1));
        for (i=1;i<n-1;i++)
        {
          prv.retryPosition = currentCodePosition;
          prv = iRetryMeElse(-1); 
          compileClause((Term)clauses.get(i)); 
          jumps.add(iJump(-1));
        }
        prv.retryPosition = currentCodePosition;
        iTrustMe(); 
        compileClause((Term)clauses.get(n-1)); 
        for (Iterator j = jumps.iterator();j.hasNext();)
        {
          IJump jump = (IJump)j.next();
          jump.jumpPosition = currentCodePosition;
        }
      }
      else // there is just one clause
      {
        compileClause((Term)clauses.get(0)); 
      }
      iReturn();
      popCutPosition();
    }
    // predicate compilation finished, construct InterpretedCode
    Instruction instr[] = (Instruction[])code.toArray(instructionArrayConstant);
    ExceptionHandlerInfo ehs[] = 
       (ExceptionHandlerInfo[])  
          exceptionHandlers.toArray(exceptionHandlerArrayConstant);
    return new InterpretedByteCode(codeTag, instr, ehs);
    //return new InterpretedCode(codeTag, instr, ehs);
  }

  /** get index of variable in environment */
  int getEnvironmentIndex(VariableTerm term)
  {
    return ((Integer)variableToEnvironmentIndex.get(term)).intValue();
  }
  /** push cut position */
  void pushCutPosition(int envPos)
  {
    cutPositionStack.add(new Integer(envPos));
  }
  
  /** pop cut position */
  int popCutPosition()
  {
    return ((Integer)cutPositionStack.remove(cutPositionStack.size()-1)).intValue();
  }
  
  /** get current cut position */
  int getCutPosition()
  {
    return ((Integer)cutPositionStack.get(cutPositionStack.size()-1)).intValue();
  }
  
  // instructions 
  /** add instruction */
  void addInstruction(Instruction i)
  {
    i.codePosition = currentCodePosition;
    code.add(i);
    currentCodePosition++;
  }
  
  /** add allocate instruction */
  IAllocate iAllocate(int environmentSize, int numberOfReserved)
  {
    IAllocate rc = new IAllocate(environmentSize, numberOfReserved);
    addInstruction(rc);
    return rc;
  }
  /** add call instruction */
  ICall iCall(CompoundTermTag tag)
  {
    ICall rc = new ICall(tag);
    addInstruction(rc);
    return rc;
  }
  /** add create compound tag instruction */
  ICreateCompoundTerm iCreateCompoundTerm(CompoundTermTag tag)
  {
    ICreateCompoundTerm rc = new ICreateCompoundTerm(tag);
    addInstruction(rc);
    return rc;
  }

  /** add cut instruction */
  ICut iCut(int envPos)
  {
    ICut rc = new ICut(envPos);
    addInstruction(rc);
    return rc;
  }
  
  /** add fail instruction */
  IFail iFail()
  {
    IFail rc =  new IFail();
    addInstruction(rc);
    return rc;
  }
  
  /** add jump instruction */ 
  IJump iJump(int pos)
  {
    IJump rc = new IJump(pos);
    addInstruction(rc);
    return rc;
  }

  /** add push argument instruction */
  IPushArgument iPushArgument(int i)
  {
    IPushArgument rc = new IPushArgument(i);
    addInstruction(rc);
    return rc;
  }
  
  /** push constant */
  IPushConstant iPushConstant(AtomicTerm term)
  {
    IPushConstant rc = new IPushConstant(term); 
    addInstruction(rc);
    return rc;
  }
  
  /** push term from environment */
  IPushEnvironment iPushEnvironment(int envIdx)
  {
    IPushEnvironment rc = new IPushEnvironment(envIdx);
    addInstruction(rc);
    return rc;
  }
  
  /** add retry me else instruction */
  IRetryMeElse iRetryMeElse(int retryPos)
  {
    IRetryMeElse rc = new IRetryMeElse(retryPos);
    addInstruction(rc);
    return rc;
  }
  
  /** add return instruction */
  IReturn iReturn()
  {
    IReturn rc =  new IReturn();
    addInstruction(rc);
    return rc;
  }

  /** add cut instruction */
  ISaveCut iSaveCut(int envPos)
  {
    ISaveCut rc = new ISaveCut(envPos);
    addInstruction(rc);
    return rc;
  }

  IStoreEnvironment iStoreEnvironment(int envPos)
  {
    IStoreEnvironment rc = new IStoreEnvironment(envPos);
    addInstruction(rc);
    return rc;
  }

  /** add try me else instruction */
  ITryMeElse iTryMeElse(int retryPos)
  {
    ITryMeElse rc = new ITryMeElse(retryPos);
    addInstruction(rc);
    return rc;
  }
  
  /** add throw instruction */
  IThrow iThrow()
  {
    IThrow rc =  new IThrow();
    addInstruction(rc);
    return rc;
  }

  /** add true instruction */
  ITrue iTrue()
  {
    ITrue rc =  new ITrue();
    addInstruction(rc);
    return rc;
  }

  /** add true instruction */
  ITrustMe iTrustMe()
  {
    ITrustMe rc =  new ITrustMe();
    addInstruction(rc);
    return rc;
  }


  /** add fail instruction */
  IUnify iUnify()
  {
    IUnify rc =  new IUnify();
    addInstruction(rc);
    return rc;
  }
  
  /** add exeption handler */
  void addExceptionHandler(ExceptionHandlerInfo eh)
  {
    exceptionHandlers.add(eh);
  }
}


