/*
  
TOM - To One Matching Compiler

Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
Nancy, France.

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

  package jtom.backend;
  
import jtom.TomBase;
import jtom.adt.Expression;
import jtom.adt.Instruction;
import jtom.adt.Option;
import jtom.adt.TomList;
import jtom.adt.TomName;
import jtom.adt.TomNumberList;
import jtom.adt.TomTerm;
import jtom.adt.TomType;
import jtom.runtime.Collect1;
import jtom.runtime.Replace1;
import jtom.tools.TomTask;
import jtom.tools.TomTaskInput;
import aterm.ATerm;
import jtom.Tom;

public class TomOptimizer extends TomBase implements TomTask {

  private TomTask nextTask;
  private int numberCompiledMatchFound = 0;
  private int numberCompiledPatternFound = 0;
  private int numberVarFound = 0;
  private int numberVarRemoved = 0;

  public TomOptimizer(jtom.TomEnvironment environment) {
    super(environment);
  }
  
  // ------------------------------------------------------------
  %include { Tom.signature }
  // ------------------------------------------------------------
    
 public void addTask(TomTask task) {
  	this.nextTask = task;
  }
  public void process(TomTaskInput input) {
    try {
	  long startChrono = 0;
	  boolean verbose = input.isVerbose(), intermediate = input.isIntermediate();
	  if(verbose) {
		startChrono = System.currentTimeMillis();
	  }
	  TomTerm optimizedTerm = optimize(input.getTerm());
	  if(verbose) {
		System.out.println("TOM optimization phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");
	  } 
      if(intermediate) {
          //Tom.generateOutput(input.inputFileName + input.optimizedSuffix, optimizedTerm);
	  }
	  input.setTerm(optimizedTerm);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if(nextTask != null) {
      nextTask.process(input);
    }
  }
  
  public TomTask getTask() {
  	return nextTask;
  }
  
  private void exitWithMesg(String mesg) {
    System.out.println(mesg);
    System.exit(1);
  }

  public TomTerm optimize(TomTerm subject) {
    TomTerm optimizedTerm;
    optimizedTerm = optimize_pass_1(subject);
    System.out.println("numberCompiledMatchFound :"+numberCompiledMatchFound);
    System.out.println("numberCompiledPatternFound :"+numberCompiledPatternFound);
    System.out.println("numberVarFound :"+numberVarFound);
    System.out.println("numberVarRemoved :"+numberVarRemoved);
    return optimizedTerm;
  }

  /* 
   * optimize_pass_1 : remove useless intermediate variables from a CompiledMatch.
   */

  private TomTerm optimize_pass_1(TomTerm subject) {
    return replaceCompiledMatch(subject);
  }


  private TomList declVarList; // one for each CompiledMatch
  private TomList avList; // avList = AssignedVariableList; one for each CompiledPattern
  private boolean insideDoWhile = false; //true when collecting inside a DoWhile

  private TomTerm replaceCompiledMatch(TomTerm subject) {
    Replace1 replace = new Replace1 () {
	public ATerm apply(ATerm t) {
	  if (t instanceof TomTerm) {
	    %match(TomTerm t) {
	      CompiledMatch(decls,automata,option) -> {
		numberCompiledMatchFound++;
		declVarList = traversalCollectDecls(decls);
		TomList newAutomata = collectNReplace(automata);
		//optimDebug("declVarList: \n"+declVarList.toString());
		TomList newDecls = replaceDeclList(decls);
		return `CompiledMatch(newDecls,newAutomata,option);
	      }
	      _ -> {
		return traversal().genericTraversal(t,this);
	      }
	    } // match
	  } else
	    return traversal().genericTraversal(t,this);
	} //apply
      };
    return (TomTerm) replace.apply(subject);
  }

  private TomList traversalCollectDecls(TomList declsList) {
    if(declsList.isEmpty()) {
      return empty();
    }
    TomTerm t = declsList.getHead();
    TomList l = declsList.getTail();
    %match(TomTerm t) {
      InstructionToTomTerm(AssignMatchSubject(Variable[astName=PositionName(l1)],source)) -> {
	String name = "tom"+numberListToIdentifier(l1);
	numberVarFound++;
	return cons(`AssignedVariable(name,source,0,FalseTL,TrueTL),
	traversalCollectDecls(l));
      }
      _ -> { 
	return traversalCollectDecls(l);
      }		    
    }
  }

  private TomList updateDeclVarList(TomList varList) {
    if(varList.isEmpty()) {
      return empty();
    }
    TomTerm t = varList.getHead();
    TomList l = varList.getTail();
    
    TomTerm av;
    TomList list;
    list = avList;
    while(!list.isEmpty()) {
      av = list.getHead();
      %match(TomTerm t, TomTerm av) {
	AssignedVariable[varName=name] , AssignedVariable[varName=name] -> {
	  return cons(av,updateDeclVarList(l));
	}
      }
      list = list.getTail();
    }
    exitWithMesg("We should not be there");
    return null;
  }

  private TomList collectNReplace(TomList automata) {
    Replace1 replace = new Replace1 () {
	public ATerm apply(ATerm t) {
	  if (t instanceof TomTerm) {
	    %match(TomTerm t) {
	      CompiledPattern(instList) -> {
		//optimDebug("Entering CP");
		numberCompiledPatternFound++;
		avList = declVarList;
		collectVariables(instList); // fill varList with all the variable in the CP.
		//optimDebug("Collecting CP completed :");
		//optimDebug(avList.toString());
		TomList newInstList = replaceCompiledPattern(instList);
		declVarList = updateDeclVarList(declVarList);
		return `CompiledPattern(newInstList);
	      }
	      _ -> {
		return traversal().genericTraversal(t,this);
	      }
	    }
	  } else
	    return traversal().genericTraversal(t,this);
	}
      };
    //optimDebug(automata.toString());
    return  (TomList) traversal().genericTraversal(automata,replace);
  }

  private void collectVariables(TomList subject) {
    Collect1 collect = new Collect1() {
	public boolean apply(ATerm t) {
	  if (t instanceof TomTerm) {
	    %match(TomTerm t) {
	      Variable[astName=PositionName(l1)] -> {
		avList = addOneUse("tom"+numberListToIdentifier(l1),false);
		return false; // no need to go deeper in a Variable
	      }
	      CompiledMatch[] -> {
		// we will inspect this CompiledMatch when replacing the CompiledMatch parent.
		return false;
	      }
	      Declaration[] -> {
		return false;
	      }
	      _ -> {return true;}
	    }
	  } else if (t instanceof Instruction) {
	    %match(Instruction t) {
	      Assign(Variable[astName=PositionName(l1)],source) -> {
		String name = "tom"+numberListToIdentifier(l1);
		if (!isAssigned(name)) {
		  int minusOne = -1;
		  //optimDebug("Var "+name+" found in CP");
		  avList = append(`AssignedVariable(name,source,minusOne,FalseTL,TrueTL),
		  avList);
		  numberVarFound++;
		} else {
		  avList = addOneUse(name,true);
		}	
		return true;// we go deeper to search in the 'source'	    
	      }
	      DoWhile[instList=list] -> {
		boolean savedInsideDoWhile = insideDoWhile;
		insideDoWhile = true;
		//optimDebug("Entering DoWhile");
		collectVariables(list);
		//optimDebug("Exiting DoWhile");
		insideDoWhile = savedInsideDoWhile;
		return false; // no need to go deeper, it has just been inspected
	      }
	      _ -> {return true;}
	    } //match
	  } else {
	    return true;
	  } //if
	} //apply
      };
    traversal().genericCollect(subject, collect);
  }

  private TomList replaceCompiledPattern(TomList subject) {
    //optimDebug("Entering replaceUselessVarAuto");
    Replace1 replace = new Replace1() {
	public ATerm apply(ATerm t) {
	  //optimDebug("Starting apply on a :");
	  //printTerm(t);
	  if (t instanceof TomTerm) {
	    %match(TomTerm t) {
	      Declaration(Variable[astName=PositionName(l1)]) -> {
		String name = "tom"+numberListToIdentifier(l1);
		//optimDebug("Declaration of "+name+" found");
		if (getNbUse(name) <= 1)
		  return `Tom(empty());
		else
		  return t;
	      }
	      Variable[astName=PositionName(l1)] -> {
		String name = "tom"+numberListToIdentifier(l1); 
		if (getNbUse(name) == 1) { // the variable is found, so used at least one time
		  Expression expr = getSource(name);
		  %match(Expression expr) {
		    TomTermToExpression(tomTerm) -> {
		      return tomTerm;
		    }
		    _ -> {
		      return `ExpressionToTomTerm(expr);
		    }
		  } // match
		} else
		  return t;
	      }
	      CompiledMatch[] -> {
		// saving context
		TomList declVarListSav = declVarList;
		TomList avListSav = avList;
		// computing the new CompiledMatch
		TomTerm newCompiledMatch = replaceCompiledMatch((TomTerm) t);
		// restoring the context
		declVarList = declVarListSav;
		avList = avListSav;
		return newCompiledMatch;
	      }
	      _ -> {
		return traversal().genericTraversal(t, this);
	      }
	    } //match
	  } else if (t instanceof Instruction) {
	    %match(Instruction t) {
	      Assign(var@Variable[astName=PositionName(l1)],source) -> {	      
		String name = "tom"+numberListToIdentifier(l1);
		if (getNbUse(name) <= 1) {
		  numberVarRemoved++;
		  return `Action(empty());
		} else {
		  Expression expr = (Expression) traversal().genericTraversal(source, this);
		  return `Assign(var, expr);
		}
	      }
	      Assign(var,TomTermToExpression(Variable[astName=PositionName(l1)])) -> {     
		String name = "tom"+numberListToIdentifier(l1);
		if (getNbUse(name) <= 1) 
		  return `Assign(var,getSource(name));
		else
		  return t;
	      }
	      _ -> {
		return traversal().genericTraversal(t, this);
	      }
	    } //match
	  } 
	  /* ******************************** */
	  /* This part will be remove when the 
	   * EqualFunctionSymbol generation will be 
	   * extended. 
	   */
	  else if (t instanceof Expression) {
	    %match(Expression t) {
	      EqualFunctionSymbol[] -> {
		return t;
	      }
	      _ -> {
		return traversal().genericTraversal(t, this);
	      }
	    }
	  }
	  /* ******************************** */
	  else {
	    return  traversal().genericTraversal(t, this);
	  }
	} //apply
      };
    return (TomList) traversal().genericTraversal(subject, replace);
  }

  private boolean isAssigned(String varName) {
    TomList tmpList = avList;
    TomTerm t;
    while (!tmpList.isEmpty()) {
      t = tmpList.getHead();
      %match(TomTerm t) {
	AssignedVariable[varName=name] -> {
	  if (name.equals(varName))
	    return true;
	}
      }
      tmpList = tmpList.getTail();
    }
    return false;
  }

  private TomList addOneUse (final String name, final boolean inAssign) {
    Replace1 replace = new Replace1 () {
	public ATerm apply(ATerm t) {
	  if (t instanceof TomTerm) {
	    %match(TomTerm t) {
	      AssignedVariable(varName,source,nbUse,TrueTL,FalseTL) -> {
		if (varName.equals(name)) {
		  int newNbUse = nbUse + 1;
		  return `AssignedVariable(varName, source, newNbUse, TrueTL,FalseTL);
		} else
		  return t;
	      }
	      AssignedVariable(varName,source,nbUse,TrueTL,TrueTL) -> {
		if (varName.equals(name)) {
		  int newNbUse = nbUse+ 1;
		  if (inAssign && insideDoWhile) { // the variable is usedInDoWhile and reaffected in DoWhile : it can't be removed
		    //optimDebug(varName+" has been set unremovable");
		    return `AssignedVariable(varName, source, newNbUse, TrueTL,FalseTL);
		  }	else
		    return `AssignedVariable(varName, source, newNbUse, TrueTL,TrueTL);
		} else
		  return t;
	      }
	      AssignedVariable(varName,source,nbUse,FalseTL,TrueTL) -> {
		if (varName.equals(name)) {
		  int newNbUse = nbUse + 1;
		  if (!inAssign && insideDoWhile) {				
		    //optimDebug(varName+" has been set 'usedInDoWhile'");
		    return `AssignedVariable(varName, source, newNbUse, TrueTL,TrueTL);
		  }	else
		    return `AssignedVariable(varName, source, newNbUse, FalseTL,TrueTL);
		} else
		  return t;
	      }
	      // the 'FalseTL, FalseTL' can't exists
	      _ -> {
		exitWithMesg("We should find only AssignedVariable");
		return null;
	      }
	    }
	  } else 
	    return traversal().genericTraversal(t,this);
	} //apply
      };
    //optimDebug("Adding one use to "+name);
    return (TomList) traversal().genericTraversal(avList, replace);
  }

  private int getNbUse(String varName) {
    TomList tmpList = avList;
    TomTerm t;
    while (!tmpList.isEmpty()) {
      t = tmpList.getHead();
      %match(TomTerm t) {
	AssignedVariable[varName=name,nbUse=n,removable=TrueTL] -> {
	  if (name.equals(varName))
	    return n;
	}
	AssignedVariable[removable=FalseTL] -> {
	  return 2; // 2 or any value that prevent a variable from being removed
	}
      }
      tmpList = tmpList.getTail();
    }
    exitWithMesg(varName+" is not in avList");
    return -1;
  }

  private Expression getSource(String varName) {
    TomList tmpList = avList;
    TomTerm t;
    while (!tmpList.isEmpty()) {
      t = tmpList.getHead();
      %match(TomTerm t) {
	AssignedVariable[varName=name,source=s] -> {
	  if (name.equals(varName))
	    return s;
	}
      }
      tmpList = tmpList.getTail();
    }
    exitWithMesg(varName+" is not in avList");
    return null;
  }

  /*
   * The following method will be FULLY usefull
   * when the EqualFunctionSymbol generation will be extended.
   * We will extend it to the one-time-used variables.
   */
  private TomList replaceDeclList(TomList declList) {
    Replace1 replace = new Replace1 () {
	public ATerm apply(ATerm t) {
	  if (t instanceof TomTerm) {
	    %match(TomTerm t) {
	      Declaration(Variable[astName=PositionName(l1)]) -> {
		String name = "tom"+numberListToIdentifier(l1);
		if (getNbUse(name) == 0) //  <= 1 if no problem with code generation
		  return `Tom(empty());
		else
		  return t;
	      }
	      _ -> {
		return traversal().genericTraversal(t, this);
	      }
	    }
	  } else if (t instanceof Instruction) {
	    %match(Instruction t) {
	      Assign(Variable[astName=PositionName(l1)],source) -> {
		String name = "tom"+numberListToIdentifier(l1);
		if (getNbUse(name) == 0) { // <= 1 if no problem with code generation
		  numberVarRemoved++;
		  return `Action(empty());
		}
		else
		  return t;
	      }
	      _ -> {
		return traversal().genericTraversal(t, this);
	      }
	    }
	  } else
	    return traversal().genericTraversal(t, this);
	} // apply
      };//replace      
    return (TomList) traversal().genericTraversal(declList,replace);
  }
      
  
} //Class TomOptimizer
