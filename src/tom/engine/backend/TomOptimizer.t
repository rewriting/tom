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
	optimDebug("Starting pass 1 of the optimizer");
	ArrayList varList = new ArrayList();
	collectCompiledMatch(subject, varList);
	System.out.println("Nb match :"+numberMatchFound);
	System.out.println("Nb TOM var found :"+numberVarFound);
	System.out.println("Nb var in list :"+varList.size());
	System.out.println(varList);
	//return replaceCompiledMatch(subject, varList);
	return subject;
    }

    private void collectCompiledMatch(TomTerm subject, final Collection list) {
	//optimDebug("Entering collectCompiledMatch");
	Collect1 collect = new Collect1() {
		public boolean apply(ATerm t) {
		    if (!(t instanceof TomTerm))
			return true; // To avoid ClassCastException when trying to match 
		                     // something not a TomTerm !
		    %match(TomTerm t) {
			CompiledMatch(decl,automata) -> {
			    collectVariable((TomList) decl, list, "");
			    collectVariable((TomList) automata, list, "");
			    numberMatchFound++;
			    return true; //we can find another match inside a match
			}
			_ -> {return true;}
		    } //match
		}
	    };
	traversal().genericCollect(subject, collect);
    }

    private void collectVariable(TomList subject, final Collection list, final String blockName) {
	//optimDebug("Entering collectVariable");
 
	// Actually we can't handle non-TOM variables: they could be used ouside TOM
	
	Collect1 collect = new Collect1() {
		public boolean apply(ATerm t) {
		    if (t instanceof TomTerm) {
			%match(TomTerm t) {
			    Variable[astName=PositionName(l1)] -> {
				Iterator iter = list.iterator();
				String name1 = blockName+":tom"+numberListToIdentifier(l1);
				while (iter.hasNext()) {
				    AssignedVariable av = (AssignedVariable) iter.next();
				    if (av.name().equals(name1))
					av.addOneUse();
				}
				return false; // no need to go deeper in a Variable
			    }
			    CompiledMatch[] -> {return false;} // the next CompiledMatch will be examined
			                                       // from collectCompiledMatch
			    _ -> {return true;}
			}
		    } else if (t instanceof Instruction) {
			%match(Instruction t) {
			    Assign(Variable[astName=PositionName(l1)],source) -> {
				AssignedVariable av = new AssignedVariable(blockName+":tom"+numberListToIdentifier(l1),source);
				if (!list.contains(av)) {
				    list.add(av);
				    numberVarFound++;
				}
				return true;	// we go deeper to search in the 'source'		    
			    }
			    NamedBlock(name,instList) -> {
				collectVariable(instList,list,name);
				return true;
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

    private TomTerm replaceCompiledMatch(TomTerm subject, final Collection varList) {
	optimDebug("Entering replaceCompiledMatch");
	Replace1 replace = new Replace1() {
		public ATerm apply(ATerm t) {
		    if (t instanceof TomTerm) {
			%match(TomTerm t) {
			    CompiledMatch(decl,automata) -> {
				System.out.println("Match found");
				TomList newDecl = replaceUselessVar(decl, varList, "");
				TomList newAutomata = replaceUselessVar(automata, varList, "");
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

    private TomList replaceUselessVar(TomList subject, final Collection varList, final String blockName) {
	optimDebug("Entering replaceUselessVar");
	Replace1 replace = new Replace1() {
		public ATerm apply(ATerm t) {
		    optimDebug("We're in apply");
		    if (t instanceof TomTerm) {
			%match(TomTerm t) {
			    InstructionToTomTerm(Assign(Variable[astName=PositionName(l1)],_)) -> {
			      
			    Iterator iter = varList.iterator();
			    String name1 = blockName+":tom"+numberListToIdentifier(l1);
			    optimDebug("Assignement of "+name1+" found");
			    while (iter.hasNext()) {
				AssignedVariable av = (AssignedVariable) iter.next();
				if (av.name().equals(name1)) {
				    optimDebug("Var "+name1+" found");
				    if (av.numberOfUse()<=1) {
					System.out.println(av.numberOfUse());
					String emptyStr = "";
					optimDebug("Removing var "+name1);
					return `TargetLanguageToTomTerm(ITL(emptyStr)) ;
				    }
				}
			    }	
			    System.err.println("The variable wasn't added to the list when collecting...");
			    System.exit(1);
			    return null;
			}
			Variable[astName=PositionName(l1)] -> {
			    Iterator iter = varList.iterator();
			    String name1 = blockName+":tom"+numberListToIdentifier(l1);
			    while (iter.hasNext()) {
				AssignedVariable av = (AssignedVariable) iter.next();
				if (av.name().equals(name1)) {
				    if (av.numberOfUse()==1) {
					return `ExpressionToTomTerm(av.source());
				    } else if (av.numberOfUse()==0) {
					System.err.println("By construction, the variable should be used at least one time...");
					System.exit(1);
					return null;
				    }
				}
			    }	
			    System.err.println("The variable wasn't added to the list when collecting...");
			    System.exit(1);
			    return null;
			}

			_ -> {return t;}
			} //match
		    } else if (t instanceof Instruction) {
			%match(Instruction t) {
			    
			    NamedBlock(name,instList) -> {
				return replaceUselessVar(instList,varList,name);
			    }
			    _ -> {return t;}
			} //match
		    } else {
			return  t;
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

	public AssignedVariable (String name, Expression source) {
	    this.source = source;
	    this.name = name;
	    numberOfUse = -1; // because collect will find the variable again in the same Assign 
	}

	public Expression source() {
	    return source;
	}

	public String name() {
	    return name;
	}

	public int numberOfUse() {
	    return numberOfUse();
	}

	public void addOneUse() {
	    numberOfUse++;
	}

	public boolean equals(Object o) {
	    if (o==null)
		return false;
	    if (o instanceof AssignedVariable) {
		AssignedVariable av = (AssignedVariable) o;
		return (this.name.equals(av.name()));
	    }
	    return false;
	}

	public String toString() {
	    return "Var '"+name+"' used "+numberOfUse+" times";
	}

    } // class AssignedVariable


} //Class TomOptimizer
