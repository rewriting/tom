package jtom.optimizer;

import jtom.*;
import jtom.adt.tomsignature.types.*;
import tom.platform.adt.platformoption.types.*;
import aterm.*;
import java.util.*;
import java.util.logging.*;
import jtom.tools.*;
import jtom.TomMessage;
import tom.library.traversal.*;

/**
 * The TomOptimizer plugin.
 */
public class TomOptimizer extends TomGenericPlugin {

  %include{ adt/TomSignature.tom }
  %include{ adt/PlatformOption.tom }

  public static final String OPTIMIZED_SUFFIX = ".tfix.optimized";

  public TomOptimizer() {
    super("TomOptimizer");
  }

  public void run() {
    if(isActivated()) {
      try {
	long startChrono = System.currentTimeMillis();
						
	boolean verbose      = getServer().getOptionBooleanValue("verbose");
	boolean intermediate = getServer().getOptionBooleanValue("intermediate");
						
	TomTerm renamedTerm   = renameVariable( (TomTerm)getTerm(), new HashSet() );
	TomTerm optimizedTerm = optimize(renamedTerm);
	setTerm(optimizedTerm);
			
	if(verbose)			
	    System.out.println("TOM optimization phase (" +(System.currentTimeMillis()-startChrono)+ " ms)");
			
	if(intermediate)
	    Tools.generateOutput( environment().getOutputFileNameWithoutSuffix() + OPTIMIZED_SUFFIX, 
				  getTerm() );
		
	environment().printAlertMessage("TomOptimizer");
	if(!environment().isEclipseMode()) 
	    {
		// remove all warning (in command line only)
		environment().clearWarnings();
	    }
      }
      catch (Exception e) 
	  {
	      getLogger().log( Level.SEVERE,
			       "ExceptionMessage",
			       new Object[]{environment().getInputFile().getName(), "TomOptimizer", e.getMessage()} );

	      e.printStackTrace();
	  }
    }
    else
	{
	    boolean verbose = getServer().getOptionBooleanValue("verbose");
	    
	    if(verbose)
		System.out.println("The optimizer is not activated and thus WILL NOT RUN.");
	}
  }

  public PlatformOptionList declaredOptions() {
    return `concPlatformOption(OptionBoolean("optimize", "O", "Optimized generated code", False()) // activation flag
			  );
  }

  private boolean isActivated() {
    return getServer().getOptionBooleanValue("optimize");
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
          boolean verbose = ((Boolean)getServer().getOptionValue("verbose")).booleanValue();
          %match(Instruction subject) {
              /*
               * TODO
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

// 		environment().messageWarning(
//                                              TomMessage.getString("UnusedVariable"),
//                                              new Object[]{`extractRealName(tomName)},
//                                              orgTrack.getFileName().getString(),
//                                              orgTrack.getLine());

                if(verbose) {
                  System.out.println(mult + " -> remove:     " + `extractRealName(tomName));
                }
                return optimizeInstruction(`body);

              } else if(mult == 1) {
                if(expConstantInBody(`exp,`body)) {
                  if(verbose) {
                    System.out.println(mult + " -> inline:     " + `extractRealName(tomName));
                  }
                  return optimizeInstruction(inlineInstruction(`var,`exp,`body));
                } else {
                  if(verbose) {
                    System.out.println(mult + " -> no inline:  " + `extractRealName(tomName));
                      //System.out.println("exp  = " + exp);
                      //System.out.println("body = " + body);
                  }
                }

              } else {
                  /* do nothing: traversal */
                if(verbose) {
                  System.out.println(mult + " -> do nothing: " + `extractRealName(tomName));
                }
              }
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

//                 environment().messageWarning(
//                                              TomMessage.getString("UnusedVariable"),
//                                              new Object[]{`extractRealName(tomName)},
//                                              orgTrack.getFileName().getString(),
//                                              orgTrack.getLine());
                if(verbose) {
                  System.out.println(mult + " -> remove:     " + `extractRealName(tomName));
                }
                return optimizeInstruction(`body); 
              } else if(mult == 1) {
                if(expConstantInBody(`exp,`body)) {
                  if(verbose) {
                    System.out.println(mult + " -> inline:     " + `extractRealName(tomName));
                  }
                  return optimizeInstruction(inlineInstruction(`var,`exp,`body));
                } else {
                  if(verbose) {
                    System.out.println(mult + " -> no inline:  " + `extractRealName(tomName));
                  }
                }
              } else {
                  /* do nothing: traversal */
                if(verbose) {
                  System.out.println(mult + " -> do nothing: " + `extractRealName(tomName));
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


  private String extractRealName(String name) {
    if(name.startsWith("tom_")) {
      return name.substring(4);
    }
    return name;
  }

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

}
