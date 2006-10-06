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
import java.util.logging.Logger;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.TomMessage;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.PILFactory;
import tom.engine.tools.Tools;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;
import tom.library.strategy.mutraveler.*;


/**
 * The TomOptimizer plugin.
 */
public class TomOptimizer extends TomGenericPlugin {

  %include{ adt/tomsignature/TomSignature.tom }
  %include{ mustrategy.tom}
  %include{java/util/ArrayList.tom}
  %include{java/util/HashSet.tom}

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

  // this static field is necessary for %strategy instructions that generate static code
  private static PILFactory factory = new PILFactory();
  private static Logger logger = Logger.getLogger("tom.engine.optimizer.TomOptimizer");

  /** Constructor */
  public TomOptimizer() {
    super("TomOptimizer");
  }

  public void run() {
    if(getOptionBooleanValue("optimize") || getOptionBooleanValue("optimize2")) {
      // Initialize strategies

      VisitableVisitor optStrategy1 = `InnermostId(RewriteSystem1());

      VisitableVisitor optStrategy2 = `Sequence(
          InnermostId(ChoiceId(RepeatId((NopElimAndFlatten())),NormExpr())),
          InnermostId(
            ChoiceId(
              Sequence(RepeatId(IfSwapping()), RepeatId(SequenceId(ChoiceId(BlockFusion(),IfFusion()),OnceTopDownId(NopElimAndFlatten())))),
              SequenceId(InterBlock(),OnceTopDownId(RepeatId(NopElimAndFlatten()))))
            )
          );

      long startChrono = System.currentTimeMillis();
      boolean intermediate = getOptionBooleanValue("intermediate");
      try {
        TomTerm renamedTerm = (TomTerm)getWorkingTerm();

        if(getOptionBooleanValue("optimize2")) {
          renamedTerm = (TomTerm) optStrategy2.visit(renamedTerm);
        }

        if(getOptionBooleanValue("optimize")) {
          renamedTerm = (TomTerm) optStrategy1.visit(renamedTerm);
        }
        setWorkingTerm(renamedTerm);

        // verbose
        logger.log(Level.INFO, TomMessage.tomOptimizationPhase.getMessage(),
            new Integer((int)(System.currentTimeMillis()-startChrono)) );
      } catch (Exception e) {
        logger.log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
            new Object[]{"TomOptimizer", getStreamManager().getInputFileName(), e.getMessage()} );

        e.printStackTrace();
        return;
      }
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileNameWithoutSuffix() + OPTIMIZED_SUFFIX, 
            (TomTerm)getWorkingTerm() );
      }
    } else {
      // not active plugin
      logger.log(Level.INFO, "The optimizer is not activated and thus WILL NOT RUN.");
    }
    if(getOptionBooleanValue("prettyPIL")) {
      System.out.println(factory.prettyPrintCompiledMatch(factory.remove((TomTerm)getWorkingTerm())));
    }

  }

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomOptimizer.DECLARED_OPTIONS);
  }

  private static String extractRealName(String name) {
    if(name.startsWith("tom_")) {
      return name.substring(4);
    }
    return name;
  }

  %op Strategy inlineInstruction(variableName:TomName, expression:Expression){
    make(variableName, expression) {`TopDown(inlineInstrOnce(variableName,expression))}
  }

  %strategy inlineInstrOnce(variableName:TomName, expression:Expression) extends `Identity(){
    visit TomTerm { 
      (Variable|VariableStar)[AstName=name] -> {
        if(variableName == `name) {
          return `ExpressionToTomTerm(expression);
        }
      }
    }
  }

  %op Strategy computeOccurences(variableName:TomName, list:ArrayList){
    make(variableName, list) {`TopDown(findOccurence(variableName,list))}
  }

  %strategy findOccurence(variableName:TomName, list:ArrayList) extends `Identity(){
    visit TomTerm{ 
      t@(Variable|VariableStar)[AstName=name] -> {
        if(variableName == `name) {
          list.add(`t);
        }
      }
    }
  }

  %op Strategy isAssigned(variableName:TomName){
    make(variableName) {`TopDown(findAssignment(variableName))}
  }


  %strategy findAssignment(variableName:TomName) extends `Identity(){
    visit Instruction {
      Assign[Variable=(Variable|VariableStar)[AstName=name]] -> {
        if(variableName == `name) {
          throw new VisitFailure();
        }
      }

      LetAssign[Variable=(Variable|VariableStar)[AstName=name]] -> {
        if(variableName == `name) {
          throw new VisitFailure();
        }
      }
    }
  }

  private static boolean expConstantInBody(Expression exp, Instruction body) {
    HashSet c = new HashSet();
    try {
      MuTraveler.init(`TopDownCollect(findRefVariable(c))).visit(exp);
    } catch(VisitFailure e) {
      logger.log( Level.SEVERE, "Error during collecting variables in "+exp);
    }
    Iterator it = c.iterator();
    while(it.hasNext()) {
      TomName name = (TomName) it.next();
      try {
        MuTraveler.init(`isAssigned(name)).visit(body);
      } catch(VisitFailure e) {
        return false;
      }
    }
    return true; 
  }

    %strategy findRefVariable(set: HashSet) extends `Identity(){
      visit TomTerm {
        Ref((Variable|VariableStar)[AstName=name])  -> {
          set.add(`name);
          //stop to visit this branch (like "return false" with traversal) 
          throw new VisitFailure();
        }
      }
    }

    /* 
     * rename variable1 into variable2
     */

    %op Strategy renameVariable(variable1: TomName, variable2: TomName){
      make(variable1,variable2) {`TopDown(renameVariableOnce(variable1,variable2))}
    }

    %strategy renameVariableOnce(variable1:TomName, variable2:TomName) extends `Identity() {
      visit TomTerm{
        var@(Variable|VariableStar)[AstName=astName] -> {
          if(variable1 == `astName) {
            return `var.setAstName(variable2);
          }
        }
      } // end match
    }


    private static boolean compare (jjtraveler.Visitable term1, jjtraveler.Visitable term2){
      return factory.remove(term1)==factory.remove(term2);
    }

    %strategy RewriteSystem1() extends `Identity() {
      visit TomTerm {
        ExpressionToTomTerm(TomTermToExpression(t)) -> {
          return `t;
        }
      }
      visit Expression {
        TomTermToExpression(ExpressionToTomTerm(t)) -> {
          return `t;
        }
      }
      visit Instruction {

        /*
         * 
         * LetRef x<-exp in body where x is used 0 or 1 ==> eliminate
         * x should not appear in exp
         */
        (LetRef|LetAssign)(var@(Variable|VariableStar)[AstName=name@Name(_)],exp,body) -> {
          /*
           * do not optimize Variable(TomNumber...) because LetRef X*=GeTTail(X*) in ...
           * is not correctly handled 
           * we must check that X notin exp
           */
          String varName = "";
          %match(name) {
            Name(tomName) -> { varName = `tomName; }
          }

          ArrayList list  = new ArrayList();
          MuTraveler.init(`computeOccurences(name,list)).visit(`body);
          int mult = list.size();
          if(mult == 0) {
            if(varName.length() > 0) {
              Option orgTrack = findOriginTracking(`var.getOption());
              logger.log( Level.WARNING,
                  TomMessage.unusedVariable.getMessage(),
                  new Object[]{orgTrack.getFileName(), new Integer(orgTrack.getLine()), `extractRealName(varName)} );
              logger.log( Level.INFO,
                  TomMessage.remove.getMessage(),
                  new Object[]{ new Integer(mult), `extractRealName(varName) });
            }
            return `body;
          } else if(mult == 1) {
            list.clear();
            `computeOccurences(name,list).apply(`exp);
            if(expConstantInBody(`exp,`body) && list.size()==0) {
              if(varName.length() > 0) {
                logger.log( Level.INFO,
                    TomMessage.inline.getMessage(),
                    new Object[]{ new Integer(mult), `extractRealName(varName) });
              }
              //System.out.println("replace1: " + `var + "\nby: " + `exp);
              return (Instruction) (MuTraveler.init(`inlineInstruction(name,exp)).visit(`body));
            } else {
              if(varName.length() > 0) {
                logger.log( Level.INFO,
                    TomMessage.noInline.getMessage(),
                    new Object[]{ new Integer(mult), `extractRealName(varName) });
              }
            }
          } else {
            /* do nothing: traversal() */
            if(varName.length() > 0) {
              logger.log( Level.INFO,
                  TomMessage.doNothing.getMessage(),
                  new Object[]{ new Integer(mult), `extractRealName(varName) });
            }
          }
        }


        Let((UnamedVariable|UnamedVariableStar)[],_,body) -> {
          return `body; 
        } 

        let@Let(var@(Variable|VariableStar)[AstName=name],exp,body) -> {
          String varName = "";
          %match(name) {
            Name(tomName) -> { varName = `tomName; }
          }
          ArrayList list  = new ArrayList();
          MuTraveler.init(`computeOccurences(name,list)).visit(`body);
          int mult = list.size();

          //System.out.println("name: " + `name);
          if(mult == 0) {
            if(varName.length() > 0) {
              Option orgTrack = findOriginTracking(`var.getOption());
              logger.log( Level.WARNING,
                  TomMessage.unusedVariable.getMessage(),
                  new Object[]{orgTrack.getFileName(), new Integer(orgTrack.getLine()),
                  `extractRealName(varName)} );
              logger.log( Level.INFO,
                  TomMessage.remove.getMessage(),
                  new Object[]{ new Integer(mult), `extractRealName(varName) });
            }
            //System.out.println("elim2: " + `var);
            //System.out.println("let: " + `let);
            return `body; 
          } else if(mult == 1) {
            if(expConstantInBody(`exp,`body)) {
              if(varName.length() > 0) {
                logger.log( Level.INFO,
                    TomMessage.inline.getMessage(),
                    new Object[]{ new Integer(mult), `extractRealName(varName) });
              }
              // System.out.println("replace2: " + `var + "\nby: " + `exp);
              return (Instruction) (MuTraveler.init(`inlineInstruction(name,exp)).visit(`body));
            } else {
              if(varName.length() > 0) {
                logger.log( Level.INFO,
                    TomMessage.noInline.getMessage(),
                    new Object[]{ new Integer(mult), `extractRealName(varName) });
              }
            }
          } else {
            /* do nothing: traversal() */
            if(varName.length() > 0) {
              logger.log( Level.INFO,
                  TomMessage.doNothing.getMessage(),
                  new Object[]{ new Integer(mult), `extractRealName(varName) });
            }
          }
        }

      } // end match
    }

    %strategy NopElimAndFlatten() extends `Identity() {
      visit Instruction {

        AbstractBlock(concInstruction(C1*,AbstractBlock(L1),C2*)) -> {
          logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
              "flatten");     
          return `AbstractBlock(concInstruction(C1*,L1*,C2*));
        }

        AbstractBlock(concInstruction(C1*,Nop(),C2*)) -> {
          logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
              "nop-elim");     
          return `AbstractBlock(concInstruction(C1*,C2*));
        }  

        AbstractBlock(concInstruction()) -> {
          logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
              "abstractblock-elim1");     
          return `Nop();
        } 

        AbstractBlock(concInstruction(i)) -> {
          logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
              "abstractblock-elim2");     
          return `i;
        }
      }      

    }

    %strategy IfSwapping() extends `Identity() {

      visit Instruction {
        AbstractBlock(concInstruction(X1*,I1@If(cond1,_,Nop()),I2@If(cond2,_,Nop()),X2*)) -> {
          String s1 = factory.prettyPrint(factory.remove(`cond1));
          String s2 = factory.prettyPrint(factory.remove(`cond2));

          if(s1.compareTo(s2) < 0) {
            Expression compatible = (Expression) `InnermostId(NormExpr()).visit(`And(cond1,cond2));
            if(compatible==`FalseTL()) {
              logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                  new Object[]{"if-swapping"});     
              return `AbstractBlock(concInstruction(X1*,I2,I1,X2*));
            }
          }
        }
      }
    }      

    %strategy BlockFusion() extends `Identity() {
      visit Instruction {
        AbstractBlock(concInstruction(X1*,Let(var1@(Variable|VariableStar)[AstName=name1],term1,body1),Let(var2@(Variable|VariableStar)[AstName=name2],term2,body2),X2*)) -> {
          /* Fusion de 2 blocs Let contigus instanciant deux variables egales */
          if(`compare(term1,term2)) {
            if(`compare(var1,var2)) {
              logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                  new Object[]{"block-fusion1"});     
              return `AbstractBlock(concInstruction(X1*,Let(var1,term1,AbstractBlock(concInstruction(body1,body2))),X2*));
            } else {
              ArrayList list  = new ArrayList();
              `computeOccurences(name1,list).visit(`body2);
              int mult = list.size();
              if(mult==0){
                logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                    new Object[]{"block-fusion2"});    
                Instruction newBody2 =  (Instruction)(MuTraveler.init(`renameVariable(name2,name1)).visit(`body2));
                return `AbstractBlock(concInstruction(X1*,Let(var1,term1,AbstractBlock(concInstruction(body1,newBody2))),X2*));
              }
            }
          }
        }
      }
    }      

    %strategy IfFusion() extends `Identity() {
      visit Instruction {
        AbstractBlock(concInstruction(X1*,If(cond1,success1,failure1),If(cond2,success2,failure2),X2*)) -> {
          /* Fusion de 2 blocs If gardes par la meme condition */
          if(`compare(cond1,cond2)) {
            if(`failure1.isNop() && `failure2.isNop()) {
              logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                  new Object[]{"if-fusion1"});
              Instruction res = `AbstractBlock(concInstruction(X1*,If(cond1,AbstractBlock(concInstruction(success1,success2)),Nop()),X2*));
              //System.out.println(res);

              return res;
            } else {
              logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                  new Object[]{ "if-fusion2"});
              return `AbstractBlock(concInstruction(X1*,If(cond1,AbstractBlock(concInstruction(success1,success2)),AbstractBlock(concInstruction(failure1,failure2))),X2*));
            }
          }
        }
      }
    }

    %strategy InterBlock() extends `Identity(){
      visit Instruction {
        /* on entrelace deux blocs incompatibles */
        AbstractBlock(concInstruction(X1*,If(cond1,suc1,fail1),If(cond2,suc2,Nop()),X2*)) -> {
          Expression compatible = (Expression) `InnermostId(NormExpr()).visit(`And(cond1,cond2));
          if(compatible==`FalseTL()) {
            logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                new Object[]{"inter-block"});
            return `AbstractBlock(concInstruction(X1*,If(cond1,suc1,AbstractBlock(concInstruction(fail1,If(cond2,suc2,Nop())))),X2*));

          }  
        }
      }      
    }

    %strategy NormExpr() extends `Identity() {
      visit Expression {
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
          TomNameList l1 = `exp1.getNameList();
          TomNameList l2 = `exp2.getNameList();
          if (`exp1.getNameList()==`exp2.getNameList()){
            return `EqualFunctionSymbol(astType,exp,exp1);
          } else if(l1.length()==1 && l2.length()==1) {
            return `FalseTL();
          } else {
            return `ref;
          }
        }
      } 
    }

  } // class TomOptimizer
