/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package jtom.optimizer;

import aterm.*;

import java.util.*;

import jtom.TomEnvironment;
import jtom.adt.tomsignature.types.*;
import jtom.tools.*;
import jtom.checker.TomCheckerMessage;
import jtom.runtime.*;

public class TomOptimizer extends TomTask {
	
  // ------------------------------------------------------------
  %include { ../adt/TomSignature.tom }
  // ------------------------------------------------------------
		
  public TomOptimizer() {
    super("Tom Optimizer");
  }

  public void initProcess() {
  } 
  
  public void process() {
    try {
      long startChrono = 0;
      boolean verbose = getInput().isVerbose();
      if(verbose) { startChrono = System.currentTimeMillis();}
      
      TomTerm renamedTerm = renameVariable(environment().getTerm(),new HashSet());
      TomTerm optimizedTerm = optimize(renamedTerm);
      
      if(verbose) {
        System.out.println("TOM optimization phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");
      }
      environment().setTerm(optimizedTerm);
      
    } catch (Exception e) {
      addError("Exception occurs in TomOptimizer: "+e.getMessage(), getInput().getInputFile().getName(), TomCheckerMessage.DEFAULT_ERROR_LINE_NUMBER, TomCheckerMessage.TOM_ERROR);
      e.printStackTrace();
      return;
    }
  }

    /* 
     * optimize:
     * remove variables which are only assigned once (but not used)
     * inline variables which are used only once
     *
     * a variable is inlined when it is used only once and
     * when the expression depends on ref-variables which
     * are not modified in the body
     */

  Replace1 replace_optimize = new Replace1() {
      public ATerm apply(ATerm subject) {
        
        if(subject instanceof TomTerm) {
          %match(TomTerm subject) {
            ExpressionToTomTerm(TomTermToExpression(t)) -> {
              return optimize(`t);
            }
          }
        } else if(subject instanceof Expression) {
          %match(Expression subject) {
            TomTermToExpression(ExpressionToTomTerm(t)) -> {
              return optimizeExpression(`t);
            }
          }
        } else if(subject instanceof Instruction) {
          boolean verbose = getInput().isVerbose();
          %match(Instruction subject) {
              /*
               * TODO
               * LetRef x where x is used 0 or 1 ==> eliminate
               */
            (LetRef|LetAssign)(var@(Variable|VariableStar)[astName=name],exp,body) -> {
              List list  = computeOccurences(`name,`body);
              int mult = list.size();

              if(mult == 0) {
                Option orgTrack = findOriginTracking(`var.getOption());
                messageError(orgTrack.getLine(),
                             orgTrack.getFileName().getString(),
                             orgTrack.getAstName().getString(),
                             orgTrack.getLine(),
                             "Variable `{0}` is never used",
                             new Object[]{`name},
                             TomCheckerMessage.TOM_WARNING);
                if(verbose) {
                  System.out.println(mult + " -> remove:     " + `name);
                }
                return optimizeInstruction(`body);

              } else if(mult == 1) {
                if(expConstantInBody(`exp,`body)) {
                  if(verbose) {
                    System.out.println(mult + " -> inline:     " + `name);
                  }
                  return optimizeInstruction(inlineInstruction(`var,`exp,`body));
                } else {
                  if(verbose) {
                    System.out.println(mult + " -> no inline:  " + `name);
                      //System.out.println("exp  = " + exp);
                      //System.out.println("body = " + body);
                  }
                }

              } else {
                  /* do nothing: traversal */
                if(verbose) {
                  System.out.println(mult + " -> do nothing: " + `name);
                }
              }
            }
            
            Let(var@(Variable|VariableStar)[astName=name],exp,body) -> {
              List list  = computeOccurences(`name,`body);
              int mult = list.size();

              if(mult == 0) {
                Option orgTrack = findOriginTracking(`var.getOption());
                messageError(orgTrack.getLine(),
                             orgTrack.getFileName().getString(),
                             orgTrack.getAstName().getString(),
                             orgTrack.getLine(),
                             "Variable `{0}` is never used",
                             new Object[]{`name},
                             TomCheckerMessage.TOM_WARNING);
                if(verbose) {
                  System.out.println(mult + " -> remove:     " + `name);
                }
                return optimizeInstruction(`body); 
              } else if(mult == 1) {
                if(expConstantInBody(`exp,`body)) {
                  if(verbose) {
                    System.out.println(mult + " -> inline:     " + `name);
                  }
                  return optimizeInstruction(inlineInstruction(`var,`exp,`body));
                } else {
                  if(verbose) {
                    System.out.println(mult + " -> no inline:  " + `name);
                  }
                }
              } else {
                  /* do nothing: traversal */
                if(verbose) {
                  System.out.println(mult + " -> do nothing: " + `name);
                }
              }
            }

          } // end match
        } // end instanceof Instruction

          /*
           * Defaul case: traversal
           */
        return traversal().genericTraversal(subject,this);
      } // end apply
    };


  public TomTerm optimize(TomTerm subject) {
    return (TomTerm) replace_optimize.apply(subject); 
  }
  
  public Instruction optimizeInstruction(Instruction subject) {
    return (Instruction) replace_optimize.apply(subject); 
  }

  public Expression optimizeExpression(Expression subject) {
    return (Expression) replace_optimize.apply(subject); 
  }

    /* 
     * inline:
     * replace a variable instantiation by its content in the body
     */

  Replace3 replace_inline = new Replace3() {
      public ATerm apply(ATerm subject, Object arg1, Object arg2) {
        TomTerm variable = (TomTerm) arg1;
        TomName variableName = variable.getAstName();
        Expression expression = (Expression) arg2;
        if(subject instanceof TomTerm) {
          %match(TomTerm subject) { 
            (Variable|VariableStar)[astName=name] |
            BuildVariable[astName=name] -> {
              if(variableName == `name) {
                return `ExpressionToTomTerm(expression);
              }
            }
          } // end match
        } // end instanceof TomTerm

          /*
           * Defaul case: traversal
           */
        return traversal().genericTraversal(subject,this,arg1,arg2);
      } // end apply
    };


  public Instruction inlineInstruction(TomTerm variable, Expression expression,
                                       Instruction subject) {
    return (Instruction) replace_inline.apply(subject,variable,expression); 
  }

  private List computeOccurences(final TomName variableName, ATerm subject) {
    final List list = new ArrayList();
    Collect1 collect = new Collect1() { 
        public boolean apply(ATerm t) {
          if(t instanceof TomTerm) {
            %match(TomTerm t) { 
              (Variable|VariableStar)[astName=name] |
               BuildVariable[astName=name] -> {
                if(variableName == `name) {
                  list.add(t);
                  return false;
                }
              }
              
              _ -> { return true; }
            }
          } else {
            return true;
          }
        } // end apply
      }; // end new
    
    traversal().genericCollect(subject, collect);
    return list;
  }

  private boolean isAssigned(final TomName variableName, ATerm subject) {
    final List list = new ArrayList();
    Collect1 collect = new Collect1() { 
        public boolean apply(ATerm t) {

            //System.out.println("isAssigned(" + variableName + "): " + t);
          if(t instanceof Instruction) {
            %match(Instruction t) { 
              Assign[variable=(Variable|VariableStar)[astName=name]] -> {
                if(variableName == `name) {
                  list.add(t);
                  return false;
                }
              }
              
              LetAssign[variable=(Variable|VariableStar)[astName=name]] -> {
                if(variableName == `name) {
                  list.add(t);
                  return false;
                }
              }

              _ -> { return true; }
            }
          } else {
            return true;
          }
        } // end apply
      }; // end new
    
    traversal().genericCollect(subject, collect);
    return list.size() > 0;
  }

  private boolean expConstantInBody(Expression exp, Instruction body) {
    boolean res = true;
    HashSet c = new HashSet();
    collectRefVariable(c,exp);
    Iterator it = c.iterator();

      //System.out.println("exp  = " + exp);
      //System.out.println("body = " + body);
    while(res && it.hasNext()) {
      TomName name = (TomName) it.next();
        //List list = computeOccurences(name,body);
        //res = res && (list.size()==0);
        //System.out.println("Ref variable: " + name);
      res = res && !isAssigned(name,body);
        //System.out.println(" assign = " + !res);

    }
    return res; 
  }

  protected void collectRefVariable(final Collection collection, ATerm subject) {
    Collect1 collect = new Collect1() { 
        public boolean apply(ATerm t) {
          //System.out.println("t = " + t);
          if(t instanceof TomTerm) {
            TomTerm annotedVariable = null;
            %match(TomTerm t) { 
              Ref((Variable|VariableStar)[astName=name])  -> {
                collection.add(`name);
                return false;
              }

              _ -> { return true; }
            }
          } else {
            return true;
          }
        } // end apply
      }; // end new
    
    traversal().genericCollect(subject, collect);
  }

    Replace2 replace_renameVariable = new Replace2() {
      public ATerm apply(ATerm subject, Object arg1) {
        Set context = (Set) arg1;
        if(subject instanceof TomTerm) {
          %match(TomTerm subject) {
            var@(Variable|VariableStar)[option=option, astName=astName@Name(name)] |
            var@BuildVariable[astName=astName@Name(name)]-> {
              if(context.contains(`astName)) {
                return `var.setAstName(`Name(ast().makeTomVariableName(name)));
              }
            }
          }
        } else if(subject instanceof Expression) {
        } else if(subject instanceof Instruction) {
          %match(Instruction subject) {
            CompiledPattern(patternList,instruction) -> {
              Map map = collectMultiplicity(`patternList);
              Set newContext = new HashSet(map.keySet());
              newContext.addAll(context);
                //Set newContext = map.keySet();
                //for(Iterator it=context.iterator() ; it.hasNext() ;) {
                //newContext.add(it.next());
                //}
              return renameVariableInstruction(`instruction,newContext);
            }
          }
          
        } // end instanceof Instruction

          /*
           * Defaul case: traversal
           */
        return traversal().genericTraversal(subject,this,context);
      } // end apply
    };


  public TomTerm renameVariable(TomTerm subject, Set context) {
    return (TomTerm) replace_renameVariable.apply(subject,context); 
  }

  public Instruction renameVariableInstruction(Instruction subject, Set context) {
    return (Instruction) replace_renameVariable.apply(subject,context); 
  }
}  //Class TomOptimizer
