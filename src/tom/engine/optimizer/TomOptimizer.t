/**
 *
 * The TomOptimizer plugin.
 *
 */

package jtom.optimizer;

import jtom.*;
import jtom.adt.tomsignature.types.*;
import jtom.adt.options.types.*;
import aterm.*;
import java.util.*;
import jtom.tools.*;
import jtom.TomMessage;
import jtom.runtime.*;

public class TomOptimizer extends TomBase implements TomPlugin
{
    %include{ ../adt/TomSignature.tom }
    %include{ ../adt/Options.tom }

    private TomTerm term;
    private TomOptionList myOptions;

    public static final String OPTIMIZED_SUFFIX = ".tfix.optimized"; // was previously in TomTaskInput

    public TomOptimizer()
    {
	myOptions = `concTomOption(OptionBoolean("optimize", "O", "Optimized generated code", False()) // activation flag
				);
    }

    public void setInput(ATerm term)
    {
	if (term instanceof TomTerm)
	    this.term = (TomTerm)term;
	else
	    environment().messageError(TomMessage.getString("TomTermExpected"),
				       "TomParserPlugin", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
    }

    public ATerm getOutput()
    {
	return term;
    }

    public void run()
    {
	if(amIActivated() == true)
	    {
		try
		    {
			long startChrono = System.currentTimeMillis();
						
			boolean verbose = ((Boolean)getServer().getOptionValue("verbose")).booleanValue();
			boolean intermediate = ((Boolean)getServer().getOptionValue("intermediate")).booleanValue();
						
			TomTerm renamedTerm = renameVariable(term, new HashSet());
			TomTerm optimizedTerm = optimize(renamedTerm);
			term = optimizedTerm;
			
			if(verbose)			
			    System.out.println("TOM optimization phase (" +(System.currentTimeMillis()-startChrono)+ " ms)");
			
			if(intermediate)
			    Tools.generateOutput(environment().getOutputFileNameWithoutSuffix() 
						 + OPTIMIZED_SUFFIX, term);
		
			environment().printAlertMessage("TomOptimizer");
			if(!environment().isEclipseMode()) 
			    {
				// remove all warning (in command line only)
				environment().clearWarnings();
			    }
		    }
		catch (Exception e) 
		    {
			environment().messageError("Exception occurs in TomOptimizer: "
						   +e.getMessage(), environment().getInputFile().getName(), 
						   TomMessage.DEFAULT_ERROR_LINE_NUMBER);
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

    public TomOptionList declareOptions()
    {
	return myOptions;
    }

    public TomOptionList requiredOptions()
    {
	return `emptyTomOptionList();
    }

    public void setOption(String optionName, String optionValue)
    {
 	%match(TomOptionList myOptions)
 	    {
		concTomOption(av*, OptionBoolean(n, alt, desc, val), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			{
			    %match(String optionValue)
				{
				    ('true') ->
					{ myOptions = `concTomOption(av*, ap*, OptionBoolean(n, alt, desc, True())); }
				    ('false') ->
					{ myOptions = `concTomOption(av*, ap*, OptionBoolean(n, alt, desc, False())); }
				}
			}
		}
		concTomOption(av*, OptionInteger(n, alt, desc, val, attr), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			myOptions = `concTomOption(av*, ap*, OptionInteger(n, alt, desc, Integer.parseInt(optionValue), attr));
		}
		concTomOption(av*, OptionString(n, alt, desc, val, attr), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			myOptions = `concTomOption(av*, ap*, OptionString(n, alt, desc, optionValue, attr));
		}
	    }
    }

    private boolean amIActivated()
    {
	TomOptionList list = `concTomOption(myOptions*);

	while(!(list.isEmpty()))
	    {
		TomOption h = list.getHead();
		%match(TomOption h)
		    {
			OptionBoolean[name="optimize", valueB=val] -> 
			    { 
				%match(TomBoolean val)
				    {
					True() -> { return true; }
					False() -> { return false; }
				    }
			    }
		    }
		list = list.getTail();
	    }
	return false; // there's a problem if we're here so I guess it's better not to activate the plugin (maybe raise an error ?)
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
                environment().messageWarning(
                                             TomMessage.getString("UnusedVariable"),
                                             new Object[]{`extractRealName(tomName)},
                                             orgTrack.getFileName().getString(),
                                             orgTrack.getLine());

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
                environment().messageWarning(
                                             TomMessage.getString("UnusedVariable"),
                                             new Object[]{`extractRealName(tomName)},
                                             orgTrack.getFileName().getString(),
                                             orgTrack.getLine());
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
