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

  public TomOptimizer(jtom.TomEnvironment environment) {
    super(environment);
    numberMatchFound = 0;
    numberVarFound = 0;
  }
  
  // ------------------------------------------------------------
  %include { Tom.signature }
  // ------------------------------------------------------------
    
  private final boolean debug = true;

  private void optimDebug(String s) {
    if (debug)
      System.out.println(s);
  }

  private int numberMatchFound;
  private int numberVarFound;

  public TomTerm optimize(TomTerm subject) {
    TomTerm optimizedTerm;
    optimizedTerm = optimize_pass_1(subject);
    return optimizedTerm;
  }


  private TomTerm optimize_pass_1(TomTerm subject) {
    //optimDebug("Starting pass 1 of the optimizer");
    ArrayList varList = new ArrayList();
    collectCompiledMatch(subject, varList,"");
    /*
    System.out.println("Nb match :"+numberMatchFound);
    System.out.println("Nb TOM var found :"+numberVarFound);
    System.out.println("Nb var in list :"+varList.size());
    System.out.println(varList);*/
    // Some list manipulations
    /*  System.out.println(empty());
       Integer deux = new Integer(2);
       System.out.println(l);
      System.out.println(cons(`Number(deux),empty()));
       // System.out.println(cons(`Number(deux),null)); -> fails
       System.out.println(cons(null,cons(`Number(deux),empty())));
    */
    return replaceCompiledMatch(subject, varList);
    //return subject;
  }

  private void collectCompiledMatch(TomTerm subject, final Collection list, final String blockName) {
    //optimDebug("Entering collectCompiledMatch");
    Collect1 collect = new Collect1() {
	public boolean apply(ATerm t) {
	  if (t instanceof TomTerm) {
	    %match(TomTerm t) {
	      CompiledMatch(decl,automata) -> {
		collectVariableDecl((TomList) decl, list);
		collectVariableAuto((TomList) automata, list, blockName);
		numberMatchFound++;
		return false;
	      }
	      _ -> {return true;}
	    } //match
	  } else 
	    return true;
	}//apply
      };
    traversal().genericCollect(subject, collect);
  }


  private void collectVariableDecl(TomList subject, final Collection list) {
    Collect1 collect = new Collect1() {
	public boolean apply(ATerm t) {
	  if (t instanceof Instruction) {
	    %match(Instruction t) {
	      Assign(Variable[astName=PositionName(l1)],source) -> {
		if (length(l1) != 2) {
		  System.err.println("Such variable should have only 2 number id :"+t);
		  System.exit(1);
		}
		AssignedVariable av = 
		  new AssignedVariable(":tom"+numberListToIdentifier(l1),source,0);
		if (!list.contains(av)) {
		  list.add(av);
		  numberVarFound++;
		} else {
		  System.err.println("By construction a variable cannot be assigned twice : "+t);
		  System.exit(1);
		}
		return false;		    
	      }
	      _ -> { return true; }
	    } // match
	  } else
	    return true;
	} //apply
      };
    traversal().genericCollect(subject, collect);
  }

  private void collectVariableAuto(TomList subject, final Collection list, final String blockName) {
    //optimDebug("Entering collectVariableAuto");
 
    // Actually we can't handle non-TOM variables: they could be used outside TOM
	
    Collect1 collect = new Collect1() {
	public boolean apply(ATerm t) {
	  if (t instanceof TomTerm) {
	    %match(TomTerm t) {
	      Declaration(Variable[astName=PositionName(l1)]) -> { 
		if (l1.getHead().isRenamedVar()) {
		  AssignedVariable av = 
		    new AssignedVariable(blockName+":tom"+numberListToIdentifier(l1),
					 `TomTermToExpression(Tom(empty())),-1);
		  if (!list.contains(av)) {
		    list.add(av);
		    numberVarFound++;
		  }
		}
		return false;
	      }
	      Variable[astName=PositionName(l1)] -> {
		String name1 = ":tom"+numberListToIdentifier(l1);
		if (length(l1) > 2) // the var isn't a 'decl' var : we have to add the blockname
		  name1 = blockName+name1;
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
		  AssignedVariable av = (AssignedVariable) iter.next();
		  if (av.name.equals(name1)) {
		    av.numberOfUse++;
		    return false;
		  } 
		}
		System.err.println("Variable used never assigned : "+ name1);
		System.exit(1);
		return false; // no need to go deeper in a Variable
	      }
	      CompiledMatch[] -> {
		collectCompiledMatch((TomTerm)t,list,blockName);
		return false;
	      }
	      _ -> {return true;}
	    }
	  } else if (t instanceof Instruction) {
	    %match(Instruction t) {
	      Assign(Variable[astName=PositionName(l1)],source) -> {
		if (length(l1) <= 2) {
		  System.err.println("Such variable should be find in decl of a CompiledMatch :"+t);
		  System.exit(1);
		}
		AssignedVariable av = 
		  new AssignedVariable(blockName+":tom"+numberListToIdentifier(l1),
				       source,-1);
		if (!list.contains(av)) {
		  list.add(av);
		  numberVarFound++;
		} else {
		  System.err.println("By construction a variable cannot be assigned twice :"+t);
		  System.exit(1);
		}
		return true;	// we go deeper to search in the 'source'		    
	      }
	      NamedBlock(name,instList) -> {
		//optimDebug("Entering block '"+name+"'");
		collectVariableAuto(instList,list,name);
		//optimDebug("Exiting block '"+name+"'");
		return false;
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

  private void printTerm(ATerm s) {
    String str = s.toString();
    int index = str.indexOf("(");
    if (index>=0)
      System.out.println(str.substring(0,index));
    else
      System.out.println(str);
  }

  private TomTerm replaceCompiledMatch(TomTerm subject, final Collection varList) {
    //optimDebug("Entering replaceCompiledMatch");
    Replace1 replace = new Replace1() {
	public ATerm apply(ATerm t) {
	  if (t instanceof TomTerm) {
	    %match(TomTerm t) {
	      CompiledMatch(decl,automata) -> {
		//System.out.println("Match found");
		TomList newDecl = replaceUselessVarDecl(decl, varList);
		TomList newAutomata = replaceUselessVarAuto(automata, varList, "");
		return `CompiledMatch(newDecl, newAutomata); 
	      }
	      _ -> {return traversal().genericTraversal(t,this);}
	    }
	  } else {
	    return traversal().genericTraversal(t,this);
	  }
	} //apply 
      };
    return (TomTerm) replace.apply(subject);
  }

  private TomList replaceUselessVarDecl(TomList subject, final Collection varList) {
    //optimDebug("Entering replaceUselessVarDecl");
    Replace1 replace = new Replace1() {
	public ATerm apply(ATerm t) {
	  if (t instanceof Instruction) {
	    %match(Instruction t) {
	      Assign(Variable[astName=PositionName(l1)],source) -> {
		if (length(l1) != 2) {
		  System.err.println("Such variable should have only 2 number id :"+t);
		  System.exit(1);
		}	      
		String name1 = ":tom"+numberListToIdentifier(l1);
		//optimDebug("Assignement of "+name1+" found");
		Iterator iter = varList.iterator();
		while (iter.hasNext()) {
		  AssignedVariable av = (AssignedVariable) iter.next();
		  if (av.name.equals(name1)) {
		    //optimDebug("Var "+name1+" found");
		    if (av.numberOfUse==0) { // TO CHANGE TO <= 1 when replacing in 'Equal' work
		      //System.out.println(av.numberOfUse);
		      //optimDebug("Removing assignement of var "+name1);
		      return `Action(empty());
		    } else 
		      return t;
		  }
		}	
		System.err.println("The variable wasn't added to the list when collecting...");
		System.exit(1);
		return null;
	      }
	      _ -> { return traversal().genericTraversal(t, this);}
	    }
	  } else if (t instanceof TomTerm) {
	    %match(TomTerm t) {
	      Declaration(Variable[astName=PositionName(l1)]) -> {
		Iterator iter = varList.iterator();
		String name1 = ":tom"+numberListToIdentifier(l1);
		//optimDebug("Declaration of "+name1+" found");
		while (iter.hasNext()) {
		  AssignedVariable av = (AssignedVariable) iter.next();
		  if (av.name.equals(name1)) {
		    if (av.numberOfUse==0) { // var is unused : no need to be declared
		      //optimDebug("Removing declaration of "+name1);
		      return `Tom(empty());
		    } else {
		      return t;
		    }
		  }
		}	
		System.err.println("The variable wasn't added to the list when collecting...");
		System.exit(1);
		return null;
		}
	      _ -> { return traversal().genericTraversal(t, this);}
	    }
	  } else
	    return traversal().genericTraversal(t,this);
	}
      };
    return (TomList) traversal().genericTraversal(subject, replace);
  }

  private TomList replaceUselessVarAuto(TomList subject, final Collection varList, final String blockName) {
    //optimDebug("Entering replaceUselessVarAuto");
    Replace1 replace = new Replace1() {
	public ATerm apply(ATerm t) {
	  //optimDebug("Starting apply on a :");
	  //printTerm(t);
	  if (t instanceof TomTerm) {
	    %match(TomTerm t) {
	      Declaration(Variable[astName=PositionName(l1)]) -> {
		Iterator iter = varList.iterator();
		String name1 = ":tom"+numberListToIdentifier(l1);
		if (length(l1) > 2) // the var isn't a 'decl' var : we have to add the blockname
		  name1 = blockName+name1;
		//optimDebug("Declaration of "+name1+" found");
		while (iter.hasNext()) {
		  AssignedVariable av = (AssignedVariable) iter.next();
		  if (av.name.equals(name1)) {
		    if (av.numberOfUse==1) { // var is used 1 time : it will be removed
		      //optimDebug("Removing declaration of "+name1);
		      return `InstructionToTomTerm(Action(empty()));
		    } else {
		      return t;
		    }
		  }
		}	
		System.err.println("The variable wasn't added to the list when collecting...");
		System.exit(1);
		return null;
	      }
	      Variable[astName=PositionName(l1)] -> {
		Iterator iter = varList.iterator();
		String name1 = ":tom"+numberListToIdentifier(l1); 
		if (length(l1) > 2) // the var isn't a 'decl' var : we have to add the blockname 
		  name1 = blockName+name1;
		while (iter.hasNext()) { 
		  AssignedVariable av = (AssignedVariable) iter.next(); 
		  if (av.name.equals(name1)) { 
		    if (av.numberOfUse==1) {
		      return `ExpressionToTomTerm(av.source); 
		    } else if (av.numberOfUse==0) {
		      System.err.println("By construction, the variable should be used at least one time..."); 
		      System.exit(1);
		      return null; 
		    } else {
		      return t; 
		    } 
		  } 
		}	
		System.err.println("The variable wasn't added to the list when collecting...");
		System.exit(1);
		return null;
		}
	      CompiledMatch[] -> {
		return replaceCompiledMatch((TomTerm) t, varList);
	      }
	      _ -> {return traversal().genericTraversal(t, this);}
	    } //match
	  } else if (t instanceof Instruction) {
	    %match(Instruction t) {
	      Assign(var@Variable[astName=PositionName(l1)],source) -> {
		if (length(l1) <= 2) {
		  System.err.println("Such variable should be find in decl of a CompiledMatch :"+t);
		  System.exit(1);
		}	      
		String name1 = ":tom"+numberListToIdentifier(l1);
		if (length(l1) > 2) // the var isn't a 'decl' var : we have to add the blockname
		  name1 = blockName+name1;
		//optimDebug("Assignement of "+name1+" found");
		Iterator iter = varList.iterator();
		while (iter.hasNext()) {
		  AssignedVariable av = (AssignedVariable) iter.next();
		  if (av.name.equals(name1)) {
		    //optimDebug("Var "+name1+" found");
		    if (av.numberOfUse<=1) {
		      //System.out.println(av.numberOfUse);
		      //optimDebug("Removing assignement of var "+name1);
		      return `Action(empty());
		    } else {
		      Expression expr = (Expression) traversal().genericTraversal(source, this);
			return `Assign(var, expr);
		    } 
		  }
		}	
		System.err.println("The variable wasn't added to the list when collecting...");
		System.exit(1);
		return null;
	      }
	      Assign(var,TomTermToExpression(Variable[astName=PositionName(l1)])) -> {     
		String name1 = ":tom"+numberListToIdentifier(l1);
		if (length(l1) > 2) // the var isn't a 'decl' var : we have to add the blockname
		  name1 = blockName+name1;
		Iterator iter = varList.iterator();
		while (iter.hasNext()) {
		  AssignedVariable av = (AssignedVariable) iter.next();
		  if (av.name.equals(name1)) {
		    if (av.numberOfUse<=1) {
		      return `Assign(var,av.source);
		    } else {
		      return t;
		    } 
		  }
		}	
		System.err.println("The variable wasn't added to the list when collecting...");
		System.exit(1);
		return null;
	      }
	      NamedBlock(name,instList) -> {
		//optimDebug("Entering block '"+name+"'");
		TomList newInstList = replaceUselessVarAuto(instList,varList,name);
		//optimDebug("Exiting block '"+name+"'");
		return `NamedBlock(name,newInstList);
	      }
	      _ -> {return traversal().genericTraversal(t, this);}
	    } //match
	  } else {
	    return  traversal().genericTraversal(t, this);
	  }
	} //apply
      };
    return (TomList) traversal().genericTraversal(subject, replace);
  }

  private class AssignedVariable {
	
    /* 
     * private variables to give only read 
     * access with the homonymous methods 
     */
    private Expression source;
    private String name;
    private int numberOfUse;

    public AssignedVariable (String name, Expression source, int init) {
      this.source = source;
      this.name = name;
      this.numberOfUse = init;
    }

    public boolean equals(Object o) {
      if (o==null)
	return false;
      if (o instanceof AssignedVariable) {
	AssignedVariable av = (AssignedVariable) o;
	return this.name.equals(av.name);
      }
      return false;
    }

    public String toString() {
      return "Var '"+name+"' used "+numberOfUse+" times";
    }

  } // class AssignedVariable


} //Class TomOptimizer
