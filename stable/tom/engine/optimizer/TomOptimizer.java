/* Generated by TOM: Do not edit this file */ /*
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
import jtom.TomMessage;
import jtom.runtime.*;

public class TomOptimizer extends TomTask {
	
  // ------------------------------------------------------------
  /* Generated by TOM: Do not edit this file *//* Generated by TOM: Do not edit this file */ /*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/  /* Generated by TOM: Do not edit this file */ /*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/       /*  * old definition of String %typeterm String {   implement           { String }   get_fun_sym(t)      { t }   cmp_fun_sym(s1,s2)  { s1.equals(s2) }   get_subterm(t, n)   { null }   equals(t1,t2)       { t1.equals(t2) } } */ /* Generated by TOM: Do not edit this file */ /*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/    /* Generated by TOM: Do not edit this file */ /*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/       
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
      addError("Exception occurs in TomOptimizer: "+e.getMessage(), getInput().getInputFile().getName(), TomMessage.DEFAULT_ERROR_LINE_NUMBER, TomMessage.TOM_ERROR);
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
           { TomTerm tom_match1_1=(( TomTerm)subject);{ if(tom_is_fun_sym_ExpressionToTomTerm(tom_match1_1) ||  false ) { { Expression tom_match1_1_1=tom_get_slot_ExpressionToTomTerm_astExpression(tom_match1_1); if(tom_is_fun_sym_TomTermToExpression(tom_match1_1_1) ||  false ) { { TomTerm tom_match1_1_1_1=tom_get_slot_TomTermToExpression_astTerm(tom_match1_1_1); { TomTerm t=tom_match1_1_1_1; 

              return optimize(t );
            }} }} }}} 

        } else if(subject instanceof Expression) {
           { Expression tom_match2_1=(( Expression)subject);{ if(tom_is_fun_sym_TomTermToExpression(tom_match2_1) ||  false ) { { TomTerm tom_match2_1_1=tom_get_slot_TomTermToExpression_astTerm(tom_match2_1); if(tom_is_fun_sym_ExpressionToTomTerm(tom_match2_1_1) ||  false ) { { Expression tom_match2_1_1_1=tom_get_slot_ExpressionToTomTerm_astExpression(tom_match2_1_1); { Expression t=tom_match2_1_1_1; 

              return optimizeExpression(t );
            }} }} }}} 

        } else if(subject instanceof Instruction) {
          boolean verbose = getInput().isVerbose();
           { Instruction tom_match3_1=(( Instruction)subject);{ if(tom_is_fun_sym_LetAssign(tom_match3_1) || tom_is_fun_sym_LetRef(tom_match3_1) ||  false ) { { TomTerm tom_match3_1_1=tom_get_slot_LetRef_variable(tom_match3_1); { Expression tom_match3_1_2=tom_get_slot_LetRef_source(tom_match3_1); { Instruction tom_match3_1_3=tom_get_slot_LetRef_astInstruction(tom_match3_1); if(tom_is_fun_sym_VariableStar(tom_match3_1_1) || tom_is_fun_sym_Variable(tom_match3_1_1) ||  false ) { { TomTerm var=tom_match3_1_1; { TomName tom_match3_1_1_2=tom_get_slot_Variable_astName(tom_match3_1_1); { TomName name=tom_match3_1_1_2; { Expression exp=tom_match3_1_2; { Instruction body=tom_match3_1_3; 





              List list  = computeOccurences(name ,body );
              int mult = list.size();

              if(mult == 0) {
                Option orgTrack = findOriginTracking(var .getOption());
                messageError(orgTrack.getLine(),
                             orgTrack.getFileName().getString(),
                             orgTrack.getAstName().getString(),
                             orgTrack.getLine(),
                             "Variable `{0}` is never used",
                             new Object[]{name },
                             TomMessage.TOM_WARNING);
                if(verbose) {
                  System.out.println(mult + " -> remove:     " + name );
                }
                return optimizeInstruction(body );

              } else if(mult == 1) {
                if(expConstantInBody(exp ,body )) {
                  if(verbose) {
                    System.out.println(mult + " -> inline:     " + name );
                  }
                  return optimizeInstruction(inlineInstruction(var ,exp ,body ));
                } else {
                  if(verbose) {
                    System.out.println(mult + " -> no inline:  " + name );
                      //System.out.println("exp  = " + exp);
                      //System.out.println("body = " + body);
                  }
                }

              } else {
                  /* do nothing: traversal */
                if(verbose) {
                  System.out.println(mult + " -> do nothing: " + name );
                }
              }
            }}}}} }}}} } if(tom_is_fun_sym_Let(tom_match3_1) ||  false ) { { TomTerm tom_match3_1_1=tom_get_slot_Let_variable(tom_match3_1); { Expression tom_match3_1_2=tom_get_slot_Let_source(tom_match3_1); { Instruction tom_match3_1_3=tom_get_slot_Let_astInstruction(tom_match3_1); if(tom_is_fun_sym_VariableStar(tom_match3_1_1) || tom_is_fun_sym_Variable(tom_match3_1_1) ||  false ) { { TomTerm var=tom_match3_1_1; { TomName tom_match3_1_1_2=tom_get_slot_Variable_astName(tom_match3_1_1); { TomName name=tom_match3_1_1_2; { Expression exp=tom_match3_1_2; { Instruction body=tom_match3_1_3; 


              List list  = computeOccurences(name ,body );
              int mult = list.size();

              if(mult == 0) {
                Option orgTrack = findOriginTracking(var .getOption());
                messageError(orgTrack.getLine(),
                             orgTrack.getFileName().getString(),
                             orgTrack.getAstName().getString(),
                             orgTrack.getLine(),
                             "Variable `{0}` is never used",
                             new Object[]{name },
                             TomMessage.TOM_WARNING);
                if(verbose) {
                  System.out.println(mult + " -> remove:     " + name );
                }
                return optimizeInstruction(body ); 
              } else if(mult == 1) {
                if(expConstantInBody(exp ,body )) {
                  if(verbose) {
                    System.out.println(mult + " -> inline:     " + name );
                  }
                  return optimizeInstruction(inlineInstruction(var ,exp ,body ));
                } else {
                  if(verbose) {
                    System.out.println(mult + " -> no inline:  " + name );
                  }
                }
              } else {
                  /* do nothing: traversal */
                if(verbose) {
                  System.out.println(mult + " -> do nothing: " + name );
                }
              }
            }}}}} }}}} }}} 

 // end match
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
           { TomTerm tom_match4_1=(( TomTerm)subject);{ if(tom_is_fun_sym_VariableStar(tom_match4_1) || tom_is_fun_sym_Variable(tom_match4_1) ||  false ) { { TomName tom_match4_1_2=tom_get_slot_Variable_astName(tom_match4_1); { TomName name=tom_match4_1_2; 


              if(variableName == name ) {
                return tom_make_ExpressionToTomTerm(expression) ;
              }
            }} } if(tom_is_fun_sym_BuildVariable(tom_match4_1) ||  false ) { { TomName tom_match4_1_1=tom_get_slot_BuildVariable_astName(tom_match4_1); { TomName name=tom_match4_1_1;                 if(variableName == name ) {                  return tom_make_ExpressionToTomTerm(expression) ;                }              }} }}} 
 // end match
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
             { TomTerm tom_match5_1=(( TomTerm)t);{ if(tom_is_fun_sym_VariableStar(tom_match5_1) || tom_is_fun_sym_Variable(tom_match5_1) ||  false ) { { TomName tom_match5_1_2=tom_get_slot_Variable_astName(tom_match5_1); { TomName name=tom_match5_1_2; 


                if(variableName == name ) {
                  list.add(t);
                  return false;
                }
              }} } if(tom_is_fun_sym_BuildVariable(tom_match5_1) ||  false ) { { TomName tom_match5_1_1=tom_get_slot_BuildVariable_astName(tom_match5_1); { TomName name=tom_match5_1_1;                   if(variableName == name ) {                    list.add(t);                    return false;                  }                }} } 

 return true; }} 

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
             { Instruction tom_match6_1=(( Instruction)t);{ if(tom_is_fun_sym_Assign(tom_match6_1) ||  false ) { { TomTerm tom_match6_1_1=tom_get_slot_Assign_variable(tom_match6_1); if(tom_is_fun_sym_VariableStar(tom_match6_1_1) || tom_is_fun_sym_Variable(tom_match6_1_1) ||  false ) { { TomName tom_match6_1_1_2=tom_get_slot_Variable_astName(tom_match6_1_1); { TomName name=tom_match6_1_1_2; 

                if(variableName == name ) {
                  list.add(t);
                  return false;
                }
              }} }} } if(tom_is_fun_sym_LetAssign(tom_match6_1) ||  false ) { { TomTerm tom_match6_1_1=tom_get_slot_LetAssign_variable(tom_match6_1); if(tom_is_fun_sym_VariableStar(tom_match6_1_1) || tom_is_fun_sym_Variable(tom_match6_1_1) ||  false ) { { TomName tom_match6_1_1_2=tom_get_slot_Variable_astName(tom_match6_1_1); { TomName name=tom_match6_1_1_2; 


                if(variableName == name ) {
                  list.add(t);
                  return false;
                }
              }} }} } 

 return true; }} 

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
             { TomTerm tom_match7_1=(( TomTerm)t);{ if(tom_is_fun_sym_Ref(tom_match7_1) ||  false ) { { TomTerm tom_match7_1_1=tom_get_slot_Ref_tomTerm(tom_match7_1); if(tom_is_fun_sym_VariableStar(tom_match7_1_1) || tom_is_fun_sym_Variable(tom_match7_1_1) ||  false ) { { TomName tom_match7_1_1_2=tom_get_slot_Variable_astName(tom_match7_1_1); { TomName name=tom_match7_1_1_2; 

                collection.add(name );
                return false;
              }} }} } 

 return true; }} 

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
           { TomTerm tom_match8_1=(( TomTerm)subject);{ if(tom_is_fun_sym_VariableStar(tom_match8_1) || tom_is_fun_sym_Variable(tom_match8_1) ||  false ) { { TomTerm var=tom_match8_1; { OptionList tom_match8_1_1=tom_get_slot_Variable_option(tom_match8_1); { TomName tom_match8_1_2=tom_get_slot_Variable_astName(tom_match8_1); { OptionList option=tom_match8_1_1; if(tom_is_fun_sym_Name(tom_match8_1_2) ||  false ) { { TomName astName=tom_match8_1_2; { String  tom_match8_1_2_1=tom_get_slot_Name_string(tom_match8_1_2); { String  name=tom_match8_1_2_1; 


              if(context.contains(astName )) {
                return var .setAstName(tom_make_Name(ast().makeTomVariableName(name)) );
              }
            }}} }}}}} } if(tom_is_fun_sym_BuildVariable(tom_match8_1) ||  false ) { { TomTerm var=tom_match8_1; { TomName tom_match8_1_1=tom_get_slot_BuildVariable_astName(tom_match8_1); if(tom_is_fun_sym_Name(tom_match8_1_1) ||  false ) { { TomName astName=tom_match8_1_1; { String  tom_match8_1_1_1=tom_get_slot_Name_string(tom_match8_1_1); { String  name=tom_match8_1_1_1;                 if(context.contains(astName )) {                  return var .setAstName(tom_make_Name(ast().makeTomVariableName(name)) );                }              }}} }}} }}} 

        } else if(subject instanceof Expression) {
        } else if(subject instanceof Instruction) {
           { Instruction tom_match9_1=(( Instruction)subject);{ if(tom_is_fun_sym_CompiledPattern(tom_match9_1) ||  false ) { { TomList tom_match9_1_1=tom_get_slot_CompiledPattern_patternList(tom_match9_1); { Instruction tom_match9_1_2=tom_get_slot_CompiledPattern_automataInst(tom_match9_1); { TomList patternList=tom_match9_1_1; { Instruction instruction=tom_match9_1_2; 

              Map map = collectMultiplicity(patternList );
              Set newContext = new HashSet(map.keySet());
              newContext.addAll(context);
                //Set newContext = map.keySet();
                //for(Iterator it=context.iterator() ; it.hasNext() ;) {
                //newContext.add(it.next());
                //}
              return renameVariableInstruction(instruction ,newContext);
            }}}} }}} 

          
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
