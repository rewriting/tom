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
  
import java.util.*;
import java.io.*;

import aterm.*;
import aterm.pure.*;

import jtom.*;
import jtom.runtime.*;
import jtom.tools.*;
import jtom.adt.*;

public class TomOptimizer extends TomBase {

  private int numberCompiledMatchFound;
  private int numberCompiledPatternFound;
  private int numberVarFound;
  private int numberVarRemoved;

  public TomOptimizer(jtom.TomEnvironment environment) {
    super(environment);
    numberCompiledMatchFound = 0;
    numberCompiledPatternFound = 0;
    numberVarFound = 0;
    numberVarRemoved = 0;
  }
  
  // ------------------------------------------------------------

  %include { Tom.signature }

  // ------------------------------------------------------------
    
  private final boolean debug = true;

  private void optimDebug(String s) {
    if (debug)
      System.out.println(s);
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

  private TomTerm replaceCompiledMatch(TomTerm subject) {
    Replace1 replace = new Replace1 () {
	public ATerm apply(ATerm t) {
	  if (t instanceof TomTerm) {
	    %match(TomTerm t) {
	      CompiledMatch(decls,automata) -> {
		numberCompiledMatchFound++;
		declVarList = traversalCollectDecls(decls);
		TomList newAutomata = collectNReplace(automata);
		//optimDebug("declVarList: \n"+declVarList.toString());
		TomList newDecls = replaceDeclList(decls);
		return `CompiledMatch(newDecls,newAutomata);
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
      InstructionToTomTerm(Assign(Variable[astName=PositionName(l1)],source)) -> {
	String name = "tom"+numberListToIdentifier(l1);
	Integer zero = new Integer(0);
	//optimDebug("Var "+name+" found in decls");
	numberVarFound++;
	return cons(`AssignedVariable(name,source,zero),
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
    exitWithMesg("We should be there");
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
		collectCompiledPattern(instList); // fill varList with all the variable in the CP.
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

  private void collectCompiledPattern(TomList subject) {
     Collect1 collect = new Collect1() {
	public boolean apply(ATerm t) {
	  if (t instanceof TomTerm) {
	    %match(TomTerm t) {
	      Variable[astName=PositionName(l1)] -> {
		avList = addOneUse("tom"+numberListToIdentifier(l1));
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
		  Integer minusOne = new Integer(-1);
		  //optimDebug("Var "+name+" found in CP");
		  avList = append(`AssignedVariable(name,source,minusOne),
				  avList);
		  numberVarFound++;
		} else {
		  avList = addOneUse(name);
		}	
		return true;// we go deeper to search in the 'source'	    
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
		//optimDebug("Declaration of "+name1+" found");
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

  private TomList addOneUse (final String name) {
    Replace1 replace = new Replace1 () {
	public ATerm apply(ATerm t) {
	  if (t instanceof TomTerm) {
	    %match(TomTerm t) {
	      AssignedVariable(varName,source,nbUse) -> {
		if (varName.equals(name)) {
		  Integer newNbUse = new Integer(nbUse.intValue() + 1);
		  return `AssignedVariable(varName, source, newNbUse);
		} else
		  return t;
	      }
	      _ -> {
		optimDebug("in rest of match: "+t.toString());
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
	AssignedVariable[varName=name,nbUse=n] -> {
	  if (name.equals(varName))
	    return n.intValue();
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
