/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2005, INRIA
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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package jtom.optimizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import jtom.adt.tomsignature.types.Expression;
import jtom.adt.tomsignature.types.Instruction;
import jtom.adt.tomsignature.types.Option;
import jtom.adt.tomsignature.types.TomName;
import jtom.adt.tomsignature.types.TomTerm;
import jtom.adt.tomsignature.*;
import jtom.tools.TomGenericPlugin;
import jtom.tools.PILFactory;
import jtom.tools.Tools;
import tom.library.traversal.Collect1;
import tom.library.traversal.Replace1;
import tom.library.traversal.Replace2;
import tom.library.traversal.Replace3;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import aterm.*;

import jtom.adt.tomsignature.types.instruction.*;
import jtom.adt.tomsignature.*;


import aterm.pure.PureFactory;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * The TomOptimizer plugin.
 */
public class TomOptimizer extends TomGenericPlugin {

  %include{ adt/tomsignature/TomSignature.tom }
  %include{ mutraveler.tom }

  /** some output suffixes */
  private static final String OPTIMIZED_SUFFIX = ".tfix.optimized";

  /** the declared options string*/
  private static final String DECLARED_OPTIONS = "<options>" + 
    "<boolean name='optimize' altName='O' description='Optimized generated code' value='false'/>" +
    "<boolean name='optimize2' altName='O2' description='Optimized generated code' value='false'/>" +
    "<boolean name='prettyPIL' altName='pil' description='PrettyPrint IL' value='false'/>" +
    "</options>";


  private VisitableVisitor optRule1;
  private VisitableVisitor optRule2;
  private VisitableVisitor optStrategy1;
  private VisitableVisitor optStrategy2;
  private VisitableVisitor normRule;
  private VisitableVisitor normStrategy;

  /** Constructor */
  public TomOptimizer() {
    super("TomOptimizer");
    optRule1 = new RewriteSystem1();
    optRule2 = new RewriteSystem2();
    
    normRule = new NormExpr();
        
  }
  
  public void run() {
    if(getOptionBooleanValue("optimize") || getOptionBooleanValue("optimize2")) {
      // Initialize strategies
      optStrategy1 = `InnermostId(optRule1);
      optStrategy2 = `InnermostId(optRule2);
      normStrategy = `InnermostId(normRule);

      long startChrono = System.currentTimeMillis();
      boolean intermediate = getOptionBooleanValue("intermediate");
      try {
        TomTerm renamedTerm   = renameVariable( (TomTerm)getWorkingTerm(), new HashSet() );
      
        if(getOptionBooleanValue("optimize")) {
          renamedTerm = (TomTerm) optStrategy1.visit(renamedTerm);
        }
        if(getOptionBooleanValue("optimize2")) {
          renamedTerm = (TomTerm) optStrategy2.visit(renamedTerm);
        }

        setWorkingTerm(renamedTerm);

        // verbose
        getLogger().log(Level.INFO, "TomOptimizationPhase",
                        new Integer((int)(System.currentTimeMillis()-startChrono)) );
      } catch (Exception e) {
        getLogger().log( Level.SEVERE, "ExceptionMessage",
                         new Object[]{"TomOptimizer", getStreamManager().getInputFile().getName(), e.getMessage()} );
        
        e.printStackTrace();
        return;
      }
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileNameWithoutSuffix() + OPTIMIZED_SUFFIX, 
                             (ATerm)getWorkingTerm() );
      }
    } else {
      // not active plugin
      getLogger().log(Level.INFO, "The optimizer is not activated and thus WILL NOT RUN.");
    }
    if (getOptionBooleanValue("prettyPIL")) {
      PILFactory fact = new PILFactory();
      System.out.println(fact.prettyPrintCompiledMatch(fact.reduce((TomTerm)getWorkingTerm())));
    }

  }

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomOptimizer.DECLARED_OPTIONS);
  }

  private String extractRealName(String name) {
    if(name.startsWith("tom_")) {
      return name.substring(4);
    }
    return name;
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

  /* renommer une variable dans un bloc en une autre 
   */

  Replace3 rename_variable = new Replace3() {
      public ATerm apply(ATerm subject, Object arg1, Object arg2) {
        TomTerm variable = (TomTerm) arg1;
        TomTerm newVariable = (TomTerm) arg2;
        TomName variableName = variable.getAstName();
        TomName newVariableName = newVariable.getAstName();

        if(subject instanceof TomTerm) {
          %match(TomTerm subject) { 
            Variable(option,astName,astType,constraints) -> {
              if(variableName == `astName) {
                return `Variable(option,newVariableName,astType,constraints);
              }
            }

            VariableStar(option,astName,astType,constraints) -> {
              if(variableName == `astName) {
                return `VariableStar(option,newVariableName,astType,constraints);
              }
            }

            BuildVariable(astName,args) -> {
              if(variableName == `astName) {
                return `BuildVariable(newVariableName,args);
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




  public Instruction renameVariable(TomTerm variable, TomTerm variable2,
                                    Instruction subject) {
    return (Instruction) rename_variable.apply(subject,variable,variable2); 
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
                return `var.setAstName(`Name(getAstFactory().makeTomVariableName(name)));
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


  public boolean compare (ATerm term1, ATerm term2){
    PILFactory factory = new PILFactory();
    return factory.remove(term1)==factory.remove(term2);
  }
  
  
  public class RewriteSystem1 extends TomSignatureVisitableFwd {

    public RewriteSystem1() {
      super(`Identity());
    }

  public jjtraveler.Visitable visit(jjtraveler.Visitable subject) throws jjtraveler.VisitFailure{

      if(subject instanceof TomTerm) {
        %match(TomTerm subject) {
          ExpressionToTomTerm(TomTermToExpression(t)) -> {
            return `t;
          }
        }
      } else if(subject instanceof Expression) {
        %match(Expression subject) {
          TomTermToExpression(ExpressionToTomTerm(t)) -> {
            return `t;
          }
        }
      } else if(subject instanceof Instruction) {
        %match(Instruction subject) {

          /*
           * 
           * LetRef x where x is used 0 or 1 ==> eliminate
           */
          (LetRef|LetAssign)(var@(Variable|VariableStar)[astName=name@Name(tomName)],exp,body) -> {
            List list  = computeOccurences(`name,`body);
            int mult = list.size();
            if(mult == 0) {
              Option orgTrack = findOriginTracking(`var.getOption());
                
              getLogger().log( Level.WARNING,
                               "UnusedVariable",
                               new Object[]{orgTrack.getFileName().getString(), new Integer(orgTrack.getLine()),
                                            `extractRealName(tomName)} );
              getLogger().log( Level.INFO,
                               "Remove",
                               new Object[]{ new Integer(mult), `extractRealName(tomName) });

              return `body;

            } else if(mult == 1) {
              if(expConstantInBody(`exp,`body)) {

                getLogger().log( Level.INFO,
                                 "Inline",
                                 new Object[]{ new Integer(mult), `extractRealName(tomName) });

                return inlineInstruction(`var,`exp,`body);
              } else {
                getLogger().log( Level.INFO,
                                 "NoInline",
                                 new Object[]{ new Integer(mult), `extractRealName(tomName) });
              }

            } else {
              /* do nothing: traversal */
              getLogger().log( Level.INFO,
                               "DoNothing",
                               new Object[]{ new Integer(mult), `extractRealName(tomName) });
            }
          }
            

          Let((UnamedVariable|UnamedVariableStar)[],exp,body) -> {
            return `body; 
          } 

          Let(var@(Variable|VariableStar)[astName=name@Name(tomName)],exp,body) -> {
            List list  = computeOccurences(`name,`body);
            int mult = list.size();

            if(mult == 0) {
              Option orgTrack = findOriginTracking(`var.getOption());

              getLogger().log( Level.WARNING,
                               "UnusedVariable",
                               new Object[]{orgTrack.getFileName().getString(), new Integer(orgTrack.getLine()),
                                            `extractRealName(tomName)} );
              getLogger().log( Level.INFO,
                               "Remove",
                               new Object[]{ new Integer(mult), `extractRealName(tomName) });
                
              return `body; 
            } else if(mult == 1) {
              if(expConstantInBody(`exp,`body)) {
                getLogger().log( Level.INFO,
                                 "Inline",
                                 new Object[]{ new Integer(mult), `extractRealName(tomName) });
                return inlineInstruction(`var,`exp,`body);
              } else {
                getLogger().log( Level.INFO,
                                 "NoInline",
                                 new Object[]{ new Integer(mult), `extractRealName(tomName) });
              }
            } else {
              /* do nothing: traversal */
              getLogger().log( Level.INFO,
                               "DoNothing",
                               new Object[]{ new Integer(mult), `extractRealName(tomName) });
            }
         }

        } // end match
      } // end instanceof Instruction
      /*
       * Defaul case: traversal
       */
      return subject;
    }      
      
  }

  public class RewriteSystem2 extends TomSignatureVisitableFwd {

    public RewriteSystem2() {
      super(new tom.library.strategy.mutraveler.Identity());
    }
    
  public jjtraveler.Visitable visit(jjtraveler.Visitable subject) throws jjtraveler.VisitFailure{

    PILFactory fact = new PILFactory();

    if(subject instanceof Instruction) {
      

        %match(Instruction subject) {

          AbstractBlock(concInstruction(C1*,AbstractBlock(L1),C2*)) -> {
            return `AbstractBlock(concInstruction(C1*,L1*,C2*));
            
          }

          AbstractBlock(concInstruction(C1*,Nop(),C2*)) -> {
            return `AbstractBlock(concInstruction(C1*,C2*));
            
          }  

          /* if-swapping */
          
           AbstractBlock(concInstruction(X1*,if1@If(cond1,_,Nop()),if2@If(cond2,_,Nop()),X2*)) -> 
             {
               if(cond1.toString().compareTo(cond2.toString()) > 0){
                 Expression compatible = (Expression) normStrategy.visit(`And(cond1,cond2));
                 if(compatible==`FalseTL()){
                   return  `AbstractBlock(concInstruction(X1*,if2,if1,X2*));
                 }
               }
             }
           

          /* Fusion de 2 blocs Let contigus instanciant deux variables �gales */
            

          AbstractBlock(concInstruction(X1*,Let(var1,term1,body1),Let(var2,term2,body2),X2*)) -> 
            {
              if(compare(term1,term2)) {
                if(compare(var1,var2)) {
                  return   `AbstractBlock(concInstruction(X1*,Let(var1,term1,AbstractBlock(concInstruction(body1,body2))),X2*));
                  
                }
                else{
                  return `AbstractBlock(concInstruction(X1*,Let(var1,term1,AbstractBlock(concInstruction(body1,renameVariable(var1,var2,body2)))),X2*));
                  
                }
              }
            }

          /* Fusion de 2 blocs If gard�s par la m�me condition */
          
          AbstractBlock(concInstruction(X1*,If(cond1,success1,failure1),If(cond2,success2,failure2),X2*)) -> 
            {
               if(compare(cond1,cond2)){
                 return `AbstractBlock(concInstruction(X1*,If(cond1,AbstractBlock(concInstruction(success1,success2)),AbstractBlock(concInstruction(failure1,failure2))),X2*));}
            }

          
          /* on entrelace deux blocs incompatibles */
          
          AbstractBlock(concInstruction(X1*,If(cond1,suc1,fail1),If(cond2,suc2,Nop()),X2*)) -> 
            {
                Expression compatible = (Expression) normStrategy.visit(`And(cond1,cond2));
                if(compatible==`FalseTL()){
                  return  `AbstractBlock(concInstruction(X1*,If(cond1,suc1,AbstractBlock(concInstruction(fail1,If(cond2,suc2,Nop())))),X2*));
                  
                }
            }


      
        } // end match
      } // end instanceof Instruction

          
      /*
       * Defaul case: traversal
       */

          return subject;
    }      
      
  }


  public class NormExpr extends TomSignatureVisitableFwd {

    public NormExpr(){
      super(new tom.library.strategy.mutraveler.Identity());
    }

  public jjtraveler.Visitable visit(jjtraveler.Visitable subject) throws jjtraveler.VisitFailure {

      if(subject instanceof Expression) {
        %match(Expression subject) {
          Or(t1,TrueTL()) -> {
            return `TrueTL();
          }
          Or(TrueTL(),t1) -> {
            return `TrueTL();
          }
          Or(t1,FalseTL()) -> {
            return `t1;
          }
          Or(FalseTL(),t1) -> {
            return `t1;
          }
          And(TrueTL(),t1) -> {
            return `t1;
          }
          And(t1,TrueTL()) -> {
            return `t1;
          }
          And(FalseTL(),t1) -> {
            return `FalseTL();
          }
          And(TrueTL(),t1) -> {
            return `FalseTL();
          }
          EqualTerm(_,kid1,kid2) -> {
            if(compare(kid1,kid2)){
              return `TrueTL();
            }else{
              return `FalseTL();
            }
          }
          And(EqualFunctionSymbol(astType,exp,exp1),EqualFunctionSymbol(astType,exp,exp2)) -> {
            if (compare(`GetSubterm(astType,exp1,Number(1)),`GetSubterm(astType,exp2,Number(1)))){
              return `EqualFunctionSymbol(astType,exp,exp1);
            }else{return `FalseTL();}
          }

        }
      } 
      /*
       * Defaul case: traversal
       */
      return subject;
    }      
      
  }

} // class TomOptimizer
