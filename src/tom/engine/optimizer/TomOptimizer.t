/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2006, INRIA
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

package tom.engine.optimizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomsignature.*;

import tom.engine.TomMessage;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.PILFactory;
import tom.engine.tools.Tools;
import tom.library.traversal.Collect1;
import tom.library.traversal.Replace1;
import tom.library.traversal.Replace2;
import tom.library.traversal.Replace3;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import aterm.*;

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

  public void optionChanged(String optionName, Object optionValue) {
    if(optionName.equals("optimize2") && ((Boolean)optionValue).booleanValue() ) { 
      setOptionValue("pretty", Boolean.TRUE);        
    }
  }

  private VisitableVisitor normStrategy;

  /** Constructor */
  public TomOptimizer() {
    super("TomOptimizer");
  }
  
  public void run() {
    if(getOptionBooleanValue("optimize") || getOptionBooleanValue("optimize2")) {
      // Initialize strategies
      
      VisitableVisitor optRule1 = new RewriteSystem1();
      VisitableVisitor optStrategy1 = `InnermostId(optRule1);

      VisitableVisitor nopElimAndFlatten = new NopElimAndFlatten();
      //nopElimAndFlatten = `RepeatId(nopElimAndFlatten);

      VisitableVisitor ifSwapping = new IfSwapping();
      VisitableVisitor blockFusion = new BlockFusion();
      VisitableVisitor ifFusion = new IfFusion();
      VisitableVisitor interBlock = new InterBlock();
      VisitableVisitor normExpr = new NormExpr();

      /*
      VisitableVisitor optStrategy2 = `Sequence(InnermostId(nopElimAndFlatten),
                               InnermostId(ChoiceId(SequenceId(RepeatId(ifSwapping),nopElimAndFlatten), 
                                                    ChoiceId(RepeatId(SequenceId(ChoiceId(blockFusion,ifFusion),nopElimAndFlatten)),
                                                             SequenceId(interBlock,nopElimAndFlatten)))));
      */

      VisitableVisitor optStrategy2 = `Sequence(
                                                InnermostId(ChoiceId(RepeatId((nopElimAndFlatten)),normExpr)),
                                                InnermostId(
                                                            ChoiceId(
                                                                       Sequence(RepeatId(ifSwapping), RepeatId(SequenceId(ChoiceId(blockFusion,ifFusion),OnceTopDownId(nopElimAndFlatten)))),
                                                                       SequenceId(interBlock,OnceTopDownId(RepeatId(nopElimAndFlatten))))

)
                                                );
      normStrategy = `InnermostId(normExpr);

      long startChrono = System.currentTimeMillis();
      boolean intermediate = getOptionBooleanValue("intermediate");
      try {
        TomTerm renamedTerm   = renameIntoTomVariable( (TomTerm)getWorkingTerm(), new HashSet() );
      
        if(getOptionBooleanValue("optimize2")) {
          //System.out.println(renamedTerm);
          renamedTerm = (TomTerm) optStrategy2.visit(renamedTerm);
        }

        if(getOptionBooleanValue("optimize")) {
          renamedTerm = (TomTerm) optStrategy1.visit(renamedTerm);
        }
        setWorkingTerm(renamedTerm);

        // verbose
        getLogger().log(Level.INFO, TomMessage.tomOptimizationPhase.getMessage(),
            new Integer((int)(System.currentTimeMillis()-startChrono)) );
      } catch (Exception e) {
        getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
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
    if(getOptionBooleanValue("prettyPIL")) {
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
				%match(TomTerm subject) {
					(Variable|VariableStar|BuildVariable)[astName=name] -> {
						if(variableName == `name) {
							return `ExpressionToTomTerm(expression);
						}
					}
				} // end match

          /*
           * Defaul case: traversal
           */
        return traversal().genericTraversal(subject,this,arg1,arg2);
      } // end apply
    };

  public Instruction inlineInstruction(TomTerm variable, Expression expression, Instruction subject) {
    return (Instruction) replace_inline.apply(subject,variable,expression); 
  }

  private List computeOccurences(final TomName variableName, ATerm subject) {
    final List list = new ArrayList();
    Collect1 collect = new Collect1() { 
        public boolean apply(ATerm t) {
					%match(Instruction t) { 
						TypedAction[astInstruction=ast] -> {
							traversal().genericCollect(`ast, this);
							return false;
						}
					}
					%match(TomTerm t) { 
						(Variable|VariableStar|BuildVariable)[astName=name] -> {
							if(variableName == `name) {
								list.add(t);
								return false;
							}
						}
					}
					return true;
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
					}
					return true;
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
          // System.out.println("t = " + t);
					%match(TomTerm t) {
						Ref((Variable|VariableStar)[astName=name])  -> {
							collection.add(`name);
							return false;
						}
          }
					return true;
        } // end apply
      }; // end new
    
    traversal().genericCollect(subject, collect);
  }

  /*
   * add a prefix (tom_) to back-quoted variables which comes from the lhs
   */
  Replace2 replace_renameIntoTomVariable = new Replace2() {
      public ATerm apply(ATerm subject, Object arg1) {
        Set context = (Set) arg1;
				%match(TomTerm subject) {
					var@(Variable|VariableStar|BuildVariable)[astName=astName@Name(name)] -> {
						if(context.contains(`astName)) {
							return `var.setAstName(`Name(getAstFactory().makeTomVariableName(name)));
						}
					}
				}
				/*
				 * collect the set of variables that correspond
				 * to the lhs of this instruction
				 */

				%match(Instruction subject) {
					CompiledPattern(patternList,instruction) -> {
						Map map = collectMultiplicity(`patternList);
						Set newContext = new HashSet(map.keySet());
						newContext.addAll(context);
						return this.apply(`instruction,newContext);
					}
				}

          /*
           * Defaul case: traversal
           */
        return traversal().genericTraversal(subject,this,context);
      } // end apply
    };


  private TomTerm renameIntoTomVariable(TomTerm subject, Set context) {
    return (TomTerm) replace_renameIntoTomVariable.apply(subject,context); 
  }


  /* 
   * rename variable1 into variable2
   */
  public Instruction renameVariable(TomTerm variable1, TomTerm variable2, Instruction subject) {
    return (Instruction) rename_variable.apply(subject,variable1,variable2); 
  }

  Replace3 rename_variable = new Replace3() {
      public ATerm apply(ATerm subject, Object arg1, Object arg2) {
        TomName variableName = ((TomTerm) arg1).getAstName();
        TomName newVariableName = ((TomTerm) arg2).getAstName();
				%match(TomTerm subject) {
					var@(Variable|VariableStar|BuildVariable)[astName=astName] -> {
						if(variableName == `astName) {
							return `var.setAstName(newVariableName);
						}
					}
				} // end match

          /*
           * Defaul case: traversal
           */
        return traversal().genericTraversal(subject,this,arg1,arg2);
      } // end apply
    };

  public boolean compare (ATerm term1, ATerm term2){
    PILFactory factory = new PILFactory();
    return factory.remove(term1)==factory.remove(term2);
  }
  
  public class RewriteSystem1 extends TomSignatureVisitableFwd {

    public RewriteSystem1() {
      super(`Identity());
    }

    public jjtraveler.Visitable visit(jjtraveler.Visitable subject) throws jjtraveler.VisitFailure{

			%match(TomTerm subject) {
				ExpressionToTomTerm(TomTermToExpression(t)) -> {
					return `t;
				}
			}
			%match(Expression subject) {
				TomTermToExpression(ExpressionToTomTerm(t)) -> {
					return `t;
				}
			}
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
								TomMessage.unusedVariable.getMessage(),
								new Object[]{orgTrack.getFileName().getString(), new Integer(orgTrack.getLine()),
								`extractRealName(tomName)} );
						getLogger().log( Level.INFO,
								TomMessage.remove.getMessage(),
								new Object[]{ new Integer(mult), `extractRealName(tomName) });

						return `body;

					} else if(mult == 1) {
						if(expConstantInBody(`exp,`body)) {

							getLogger().log( Level.INFO,
									TomMessage.inline.getMessage(),
									new Object[]{ new Integer(mult), `extractRealName(tomName) });

							return inlineInstruction(`var,`exp,`body);
						} else {
							getLogger().log( Level.INFO,
									TomMessage.noInline.getMessage(),
									new Object[]{ new Integer(mult), `extractRealName(tomName) });
						}

					} else {
						/* do nothing: traversal */
						getLogger().log( Level.INFO,
								TomMessage.doNothing.getMessage(),
								new Object[]{ new Integer(mult), `extractRealName(tomName) });
					}
				}


				Let((UnamedVariable|UnamedVariableStar)[],_,body) -> {
					return `body; 
				} 

				Let(var@(Variable|VariableStar)[astName=name@Name(tomName)],exp,body) -> {
					List list  = computeOccurences(`name,`body);
					int mult = list.size();

					if(mult == 0) {
						Option orgTrack = findOriginTracking(`var.getOption());

						getLogger().log( Level.WARNING,
								TomMessage.unusedVariable.getMessage(),
								new Object[]{orgTrack.getFileName().getString(), new Integer(orgTrack.getLine()),
								`extractRealName(tomName)} );
						getLogger().log( Level.INFO,
								TomMessage.remove.getMessage(),
								new Object[]{ new Integer(mult), `extractRealName(tomName) });

						return `body; 
					} else if(mult == 1) {
						if(expConstantInBody(`exp,`body)) {
							getLogger().log( Level.INFO,
									TomMessage.inline.getMessage(),
									new Object[]{ new Integer(mult), `extractRealName(tomName) });
							return inlineInstruction(`var,`exp,`body);
						} else {
							getLogger().log( Level.INFO,
									TomMessage.noInline.getMessage(),
									new Object[]{ new Integer(mult), `extractRealName(tomName) });
						}
					} else {
						/* do nothing: traversal */
						getLogger().log( Level.INFO,
								TomMessage.doNothing.getMessage(),
								new Object[]{ new Integer(mult), `extractRealName(tomName) });
					}
				}

			} // end match
      /*
       * Defaul case: traversal
       */
      return subject;
    }      
      
  }

  public class BaseId extends TomSignatureVisitableFwd {
    public BaseId() {
      super(new tom.library.strategy.mutraveler.Identity());
    }
  }

  public class NopElimAndFlatten extends BaseId {
    public tom.engine.adt.tomsignature.types.Instruction visit_Instruction(tom.engine.adt.tomsignature.types.Instruction subject)
      throws jjtraveler.VisitFailure{
      %match(Instruction subject) {
        
        AbstractBlock(concInstruction(C1*,AbstractBlock(L1),C2*)) -> {
          getLogger().log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                        "flatten");     
          return `AbstractBlock(concInstruction(C1*,L1*,C2*));
        }

        AbstractBlock(concInstruction(C1*,Nop(),C2*)) -> {
          getLogger().log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                        "nop-elim");     
          return `AbstractBlock(concInstruction(C1*,C2*));
        }  

        AbstractBlock(concInstruction()) -> {
           getLogger().log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                        "abstractblock-elim1");     
          return `Nop();
        } 

        AbstractBlock(concInstruction(i)) -> {
           getLogger().log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                        "abstractblock-elim2");     
          return `i;
        }

        
        /*
        AbstractBlock(l@concInstruction(_*)) -> {
          System.out.println("flatten/nop");
          InstructionList res = flatten(`l);
          if(res.isEmpty()) {
            return `Nop();
          } else {
            return `AbstractBlock(res);
          }
        } 
        */
      }
      // Defaul case: traversal
      return subject;
    }      

    private InstructionList flatten(InstructionList list) {
      %match(InstructionList list) {
        emptyInstructionList() -> { 
          return list;
        }
        
        manyInstructionList(head, tail) -> {
          %match(Instruction head) {
            Nop() -> { 
              return flatten(`tail);
            }
            AbstractBlock(l) -> {
              return (InstructionList) flatten(`l).concat(`tail);
            }
            _ -> {return `manyInstructionList(head,flatten(tail));}
          }

        }
      }
      return list;
    }

  }

  public class IfSwapping extends BaseId {
    public tom.engine.adt.tomsignature.types.Instruction visit_Instruction(tom.engine.adt.tomsignature.types.Instruction subject)
      throws jjtraveler.VisitFailure{

      PILFactory factory = new PILFactory(); 
      //System.out.println(factory.prettyPrint(factory.remove(subject)));

      /*
      %match(Instruction subject) {
        AbstractBlock(concInstruction(L*)) -> {
          System.out.println(factory.prettyPrint(factory.remove(subject)));
          System.out.println(`L);
        }
      }
      */

      %match(Instruction subject) {
        AbstractBlock(concInstruction(X1*,I1@If(cond1,_,Nop()),I2@If(cond2,_,Nop()),X2*)) -> {
          String s1 = factory.prettyPrint(factory.remove(`cond1));
          String s2 = factory.prettyPrint(factory.remove(`cond2));
          //System.out.println("s1 = " + s1);
          //System.out.println("s2 = " + s2);
          //System.out.println("cmp = " + s1.compareTo(s2));
          
          if(s1.compareTo(s2) < 0) {
            Expression compatible = (Expression) normStrategy.visit(`And(cond1,cond2));
            if(compatible==`FalseTL()) {
                        getLogger().log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                        new Object[]{"if-swapping"});     
              return `AbstractBlock(concInstruction(X1*,I2,I1,X2*));
            }
          }
        }
      }
      // Defaul case: traversal
      return subject;
    }      
  }

  public class BlockFusion extends BaseId {
    public tom.engine.adt.tomsignature.types.Instruction visit_Instruction(tom.engine.adt.tomsignature.types.Instruction subject)
      throws jjtraveler.VisitFailure{
      %match(Instruction subject) {
        AbstractBlock(concInstruction(X1*,Let(var1@(Variable|VariableStar|BuildVariable)[astName=name],term1,body1),Let(var2,term2,body2),X2*)) -> {
          /* Fusion de 2 blocs Let contigus instanciant deux variables egales */
          if(`compare(term1,term2)) {
            if(`compare(var1,var2)) {
                        getLogger().log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                        new Object[]{"block-fusion1"});     
              return `AbstractBlock(concInstruction(X1*,Let(var1,term1,AbstractBlock(concInstruction(body1,body2))),X2*));
            } else {
              List list  = computeOccurences(`name,`body2);
              int mult = list.size();
              if(mult==0){
                         getLogger().log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                         new Object[]{"block-fusion2"});     
                /*
                 * TODO: check that var1 does not appear in body2
                 */
                return `AbstractBlock(concInstruction(X1*,Let(var1,term1,AbstractBlock(concInstruction(body1,renameVariable(var2,var1,body2)))),X2*));
              }
            }
          }
        }
      }
      // Defaul case: traversal
      return subject;
    }      
  }

  public class IfFusion extends BaseId {
    public tom.engine.adt.tomsignature.types.Instruction visit_Instruction(tom.engine.adt.tomsignature.types.Instruction subject)
      throws jjtraveler.VisitFailure{
      %match(Instruction subject) {
        AbstractBlock(concInstruction(X1*,If(cond1,success1,failure1),If(cond2,success2,failure2),X2*)) -> {
          /* Fusion de 2 blocs If gardes par la meme condition */
          if(`compare(cond1,cond2)) {
            if(`failure1.isNop() && `failure2.isNop()) {
        getLogger().log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                        new Object[]{"if-fusion1"});
              Instruction res = `AbstractBlock(concInstruction(X1*,If(cond1,AbstractBlock(concInstruction(success1,success2)),Nop()),X2*));
              //System.out.println(res);

              return res;
            } else {
         getLogger().log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                       new Object[]{ "if-fusion2"});
              return `AbstractBlock(concInstruction(X1*,If(cond1,AbstractBlock(concInstruction(success1,success2)),AbstractBlock(concInstruction(failure1,failure2))),X2*));
            }
          }
        }
      }
      // Defaul case: traversal
      return subject;
    }      
  }

  public class InterBlock extends BaseId {
    public tom.engine.adt.tomsignature.types.Instruction visit_Instruction(tom.engine.adt.tomsignature.types.Instruction subject)
      throws jjtraveler.VisitFailure{

      %match(Instruction subject) {
        /* on entrelace deux blocs incompatibles */
        AbstractBlock(concInstruction(X1*,If(cond1,suc1,fail1),If(cond2,suc2,Nop()),X2*)) -> {
          Expression compatible = (Expression) normStrategy.visit(`And(cond1,cond2));
          if(compatible==`FalseTL()) {
          getLogger().log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                        new Object[]{"inter-block"});
            return `AbstractBlock(concInstruction(X1*,If(cond1,suc1,AbstractBlock(concInstruction(fail1,If(cond2,suc2,Nop())))),X2*));
          }
        }  
      }
       // Defaul case: traversal
      return subject;
    }      
  }

  public class NormExpr extends BaseId {
    public tom.engine.adt.tomsignature.types.Expression visit_Expression(tom.engine.adt.tomsignature.types.Expression subject) 
      throws jjtraveler.VisitFailure {
      %match(Expression subject) {
        Or(_,TrueTL()) -> {
          return `TrueTL();
        }
        Or(TrueTL(),_) -> {
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
        And(FalseTL(),_) -> {
          return `FalseTL();
        }
        And(TrueTL(),_) -> {
          return `FalseTL();
        }
        ref@EqualTerm(_,kid1,kid2) -> {
          if(`compare(kid1,kid2)){
            return `TrueTL();
          } else {
            return `ref;
          }
        }
        ref@And(EqualFunctionSymbol(astType,exp,exp1),EqualFunctionSymbol(astType,exp,exp2)) -> {
          NameList l1 = `exp1.getNameList();
          NameList l2 = `exp2.getNameList();
          if (`exp1.getNameList()==`exp2.getNameList()){
            return `EqualFunctionSymbol(astType,exp,exp1);
          } else if(l1.getLength()==1 && l2.getLength()==1) {
              return `FalseTL();
          } else {
            return `ref;
          }
        }
      } 
      
      // Defaul case: traversal
      return subject;
    }      
      
  }

} // class TomOptimizer
