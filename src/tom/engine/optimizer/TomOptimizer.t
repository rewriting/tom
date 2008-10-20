/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2008, INRIA
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

import tom.engine.TomBase;

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

import tom.library.sl.*;

/**
 * The TomOptimizer plugin.
 */
public class TomOptimizer extends TomGenericPlugin {

  %include{ ../adt/tomsignature/TomSignature.tom }
  %include{ ../adt/tomsignature/_TomSignature.tom }
  %include{ ../../library/mapping/java/sl.tom }
  %include{ ../../library/mapping/java/util/ArrayList.tom }
  %include{ ../../library/mapping/java/util/HashSet.tom }

  %typeterm TomOptimizer { implement { TomOptimizer } }

  /** some output suffixes */
  private static final String OPTIMIZED_SUFFIX = ".tfix.optimized";

  /** the declared options string*/
  private static final String DECLARED_OPTIONS = 
    "<options>" + 
    "<boolean name='optimize' altName='O' description='Optimize generated code: perform inlining' value='true'/>" +
    "<boolean name='optimize2' altName='O2' description='Optimize generated code: discrimination tree' value='false'/>" +
    "<boolean name='prettyPIL' altName='pil' description='PrettyPrint IL' value='false'/>" +
    "</options>";

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomOptimizer.DECLARED_OPTIONS);
  }

  public void optionChanged(String optionName, Object optionValue) {
    if(optionName.equals("optimize2") && ((Boolean)optionValue).booleanValue() ) {
      setOptionValue("pretty", Boolean.TRUE);
    }
  }

  // this static field is necessary for %strategy instructions that generate static code
  private static PILFactory factory = new PILFactory();
  private static Logger logger = Logger.getLogger("tom.engine.optimizer.TomOptimizer");
  private static void info(TomMessage msg, int value, String name) {
    logger.log( Level.INFO, msg.getMessage(), new Object[]{ Integer.valueOf(value), name });
  }

  /** Constructor */
  public TomOptimizer() {
    super("TomOptimizer");
  }

  public void run() {
    if(getOptionBooleanValue("optimize") || getOptionBooleanValue("optimize2")) {
      // Initialize strategies

      long startChrono = System.currentTimeMillis();
      boolean intermediate = getOptionBooleanValue("intermediate");
      try {
        TomTerm renamedTerm = (TomTerm)getWorkingTerm();
        if(getOptionBooleanValue("optimize2")) {          
          Strategy optStrategy2 = `Sequence(
              InnermostId(ChoiceId(RepeatId(NopElimAndFlatten()),NormExpr(this))),
              InnermostId(
                ChoiceId(
                  Sequence(RepeatId(IfSwapping(this)), RepeatId(SequenceId(ChoiceId(BlockFusion(),IfFusion()),OnceTopDownId(NopElimAndFlatten())))),
                  SequenceId(InterBlock(this),OnceTopDownId(RepeatId(NopElimAndFlatten()))))
                )
              );

          renamedTerm = (TomTerm) optStrategy2.visitLight(renamedTerm);
          renamedTerm = (TomTerm) `InnermostId(Inline(TrueConstraint())).visit(renamedTerm);
          renamedTerm = (TomTerm) optStrategy2.visitLight(renamedTerm);
        } else if(getOptionBooleanValue("optimize")) {
          Strategy optStrategy = `Sequence(
                InnermostId(ChoiceId(RepeatId(NopElimAndFlatten()),NormExpr(this))),
                InnermostId(Inline(TrueConstraint())));

          renamedTerm = (TomTerm) optStrategy.visit(renamedTerm);
        }
        setWorkingTerm(renamedTerm);

        // verbose
        logger.log(Level.INFO, TomMessage.tomOptimizationPhase.getMessage(),
            Integer.valueOf((int)(System.currentTimeMillis()-startChrono)) );
      } catch (Exception e) {
        logger.log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
            new Object[]{"TomOptimizer", getStreamManager().getInputFileName(), e.getMessage()} );

        e.printStackTrace();
        return;
      }
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() + OPTIMIZED_SUFFIX, 
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

  private final static String PREFIX = "tom_";
  private static String extractRealName(String name) {
    if(name.startsWith(PREFIX)) {
      return name.substring(PREFIX.length());
    }
    return name;
  }

  %strategy Inline(context:Constraint) extends Identity() {
    visit TomTerm {
      /* optimize the insertion of a slice into a list */
      BuildAppendList(name,ExpressionToTomTerm(GetSliceList(name,begin,end,tailSlice)),tail) -> {
        return `ExpressionToTomTerm(GetSliceList(name,begin,end,BuildAppendList(name,tailSlice,tail)));
      }
    }

    visit Instruction {
      t@TypedAction[PositivePattern=positivePattern] -> {
        context = `positivePattern;
        //System.out.println("found context = " + context);
        return `t;
      }

      /*
       * 
       * Let x<-exp in body where x is used 0 times ==> eliminate
       * Let x<-variable in body ==> inline
       * Let x<-exp in body where x is used 1 times ==> inline
       * If exp does no depend from values which are modified between Let x<-exp and the use of x in the body
       */
      Let((UnamedVariable|UnamedVariableStar)[],_,body) -> {
        return `body; 
      } 

      Let((Variable|VariableStar)[AstName=name],exp,body) &&
        (TomTermToExpression((Variable|VariableStar)[AstName=expname])<<exp ||
         Cast[Source=TomTermToExpression((Variable|VariableStar)[AstName=expname])]<<exp) -> {
          return (Instruction) `TopDown(replaceVariableByExpression(name,exp)).visitLight(`body);
       // return  (Instruction) `renameVariable(name,expname).visitLight(`body);
      }

      Let(var@(Variable|VariableStar)[AstName=name],exp,body) -> {
        String varName = ""; // real name of the variable (i.e. without the tom_ prefix)
        %match(name) {
          Name(tomName) -> { varName = `extractRealName(tomName); }
        }

        // count the occurend of name
        // and inspect the body to 
        InfoVariable info = new InfoVariable(`exp,getPosition());
        getEnvironment().down(3);
        `computeOccurencesLet(name,info).visit(getEnvironment());
        getEnvironment().up();

        int mult = info.readCount;
        Position readPos = info.usePosition;
        if(mult == 0) { // name is not used
          // suppress the Let and all the corresponding Assigns in the body
          if(varName.length() > 0) {
            // why this test?
            // TODO: check variable occurence in TypedAction
            info = new InfoVariable();
            `computeOccurencesLet(name,info).visit(`context);
            if(info.readCount<=1 && !varName.startsWith("_")) {
              // variables introduced by renaming starts with a '_'
              // verify linearity in case of variables from the pattern
              // warning to indicate that this var is unused in the rhs
              Option orgTrack = TomBase.findOriginTracking(`var.getOption());
              TomMessage.warning(logger,orgTrack.getFileName(), orgTrack.getLine(),
                  TomMessage.unusedVariable,varName);
              info(TomMessage.remove,mult,varName);
            }
          }
          return `body;
        } else if(mult == 1) {
          //test if variables contained in the exp to assign have not been
          //modified between the last assignment and the read
          if(!info.modifiedAssignmentVariables) {
            if(varName.length() > 0) {
              info(TomMessage.inline,mult,varName);
            }
            Position current = getPosition();
            getEnvironment().goToPosition(readPos);
            getEnvironment().setSubject(`ExpressionToTomTerm(exp));
            getEnvironment().goToPosition(current);
            Instruction newlet = (Instruction) getEnvironment().getSubject();
            // return only the body
            return (Instruction) newlet.getChildAt(2);
          } else {
            if(varName.length() > 0) {
              info(TomMessage.noInline,mult,varName);
            }
          }
        } else {
          /* do nothing: traversal() */
          if(varName.length() > 0) {
            info(TomMessage.doNothing,mult,varName);
          }
        }
      }

      /*
       * LetRef x<-exp in body where x is used 0 or 1 ==> eliminate
       * x should not appear in exp
       */
      LetRef(var@(Variable|VariableStar)[AstName=name@Name[]],exp,body) -> {
        /*
         * do not optimize Variable(TomNumber...) because LetRef X*=GetTail(X*) in ...
         * is not correctly handled 
         * we must check that X notin exp
         */
        String varName = "";
        %match(name) { 
          Name(tomName) -> { varName = `extractRealName(tomName); }
        }

        InfoVariable info = new InfoVariable(`exp,getEnvironment().getPosition());
        getEnvironment().down(3);
        //computeOccurencesLetRef on the body
        `computeOccurencesLetRef(name,info).visit(getEnvironment());
        getEnvironment().up();
        int mult = info.readCount;
        Position readPos = info.usePosition;
        TomTerm value = `ExpressionToTomTerm(info.assignment);

        //System.out.println(`name + " --> " + mult);
        // 0 -> unused variable
        // suppress the letref and all the corresponding letassigns in the body
        if(mult == 0) {
          // why this test?
          if(varName.length() > 0) {
            // TODO: check variable occurence in TypedAction
            info = new InfoVariable();
            `computeOccurencesLetRef(name,info).visit(`context);
            if(info.readCount<=1 && !varName.startsWith("_")) {
              // verify linearity in case of variables from the pattern
              // warning to indicate that this var is unused in the rhs
              Option orgTrack = TomBase.findOriginTracking(`var.getOption());
              TomMessage.warning(logger,orgTrack.getFileName(), orgTrack.getLine(),
                  TomMessage.unusedVariable,varName);
              info(TomMessage.remove,mult,varName);
            }
          }
          return (Instruction) `CleanAssign(name).visitLight(`body);
        } else if(mult == 1) {
          //test if variables contained in the exp to assign have not been
          //modified between the last assignment and the use
          if(info.assignment!=null) {
            //test if the last assignment is not in a conditional sub-block
            //relatively to the variable use
            Position src = info.usePosition;
            Position dest = info.assignmentPosition;

            // find the positive part of src-dest
            Position positivePart = (Position) dest.sub(src).getCanonicalPath();
            while(positivePart.length()>0 && positivePart.getHead()<0) {
              positivePart = (Position) positivePart.getTail();
            }
            // find the common ancestor of src and dest
            Position commonAncestor = (Position) dest.add(positivePart.inverse()).getCanonicalPath();
            Position current = getPosition();
            getEnvironment().goToPosition(commonAncestor);
            try {
              //this strategy fails if from common ancestor and along the path
              //positivePart there is an instruction If, WhileDo or DoWhile
              positivePart.getOmegaPath(`Not(Choice(Is_If(),Is_DoWhile(),Is_WhileDo()))).visit(`getEnvironment());
              if(varName.length() > 0) {
                info(TomMessage.inline,mult,varName);
              }
              getEnvironment().goToPosition(readPos);
              getEnvironment().setSubject(value);
              getEnvironment().goToPosition(current);
              `CleanAssign(name).visit(getEnvironment());
              Instruction newletref = (Instruction) getEnvironment().getSubject();
              // return only the body
              //System.out.println("inlineletref");
              return (Instruction) newletref.getChildAt(2);
            } catch(VisitFailure e) {
              getEnvironment().goToPosition(current);
              if(varName.length() > 0) {
                info(TomMessage.noInline,mult,varName);
              }
            }
          } else {
            if(varName.length() > 0) {
              info(TomMessage.noInline,mult,varName);
            }
          }
        } else {
          /* do nothing: traversal() */
          if(varName.length() > 0) {
            info(TomMessage.doNothing,mult,varName);
          }
        }
      }

    }
  }

  %typeterm InfoVariable { implement{ InfoVariable } }

  private static class InfoVariable {

    public Expression assignment;
    public Position assignmentPosition;
    public Position usePosition; //only used for letref inlining
    public HashSet<TomName> assignmentVariables= new HashSet();
    public int readCount=0;
    public boolean modifiedAssignmentVariables=false;

    public InfoVariable() {}

    public InfoVariable(Expression assignment, Position assignmentPosition) {
      setAssignment(assignment,assignmentPosition);
    } 

    public void setAssignment(Expression newAssignment,Position newAssignmentPosition) {
      assignment = newAssignment;
      assignmentPosition = newAssignmentPosition;
      assignmentVariables.clear();
      try {
        `TopDownCollect(CollectVariable(assignmentVariables)).visitLight(newAssignment);
      } catch(VisitFailure e) {
        logger.log( Level.SEVERE, "Error during collecting variables in "+newAssignment);
      }
    }

    public void clear() {
      assignment = null;
      assignmentPosition = null;
      assignmentVariables.clear();
    }

  }


  /* strategies for Let inlining (using cps) */
  // comp = AssignCase(TypedActionCase(comp,BaseCase(all(comp),fail())),fail())
  %op Strategy computeOccurencesLet(variableName:TomName, info:InfoVariable) { 
    make(variableName, info) { (
        `Try(
          mu(MuVar("comp"),
            computeOccurenceLet_AssignCase( computeOccurenceLet_TypedActionCase( MuVar("comp"),
                computeOccurenceLet_BaseCase( All(MuVar("comp")),
                  variableName,
                  info
                  ),
                variableName, 
                info
                ),
              info
              )
            )) )
    }
  }

  %strategy computeOccurenceLet_AssignCase(defaultCase:Strategy,info:InfoVariable) extends defaultCase {
    visit Instruction {
      Assign[Variable=Variable[AstName=varname]] -> {
        if(info.assignmentVariables.contains(`varname)) {
          info.modifiedAssignmentVariables=true;
          throw new VisitFailure();
        }
      }
    }
  } 

  %strategy computeOccurenceLet_TypedActionCase(goOnCase:Strategy,cutCase:Strategy,variableName:TomName,info:InfoVariable) extends cutCase {
    visit Instruction {
      TypedAction[] -> {
        /* recursive call of the current strategy on the first child */
        Environment current = getEnvironment();
        current.down(1);
        try {
          goOnCase.visit(current);
          current.up();
          return (Instruction) current.getSubject();
        } catch (VisitFailure e) {
          current.upLocal();
          throw new VisitFailure();
        }
      }

      // should not happen
      Assign[Variable=Variable[AstName=varname]] -> {
        if (variableName.equals(`varname)) {
          logger.log( Level.SEVERE, "TomOptimizer: Assignment cannot be done for the variable "+variableName+" declared in a let", new Object[]{} );
        }
      }
    }
  }

  /*
   * when variableName is encountered, its position is stored
   * if it appears more than once, the computation is stopped because there is no possible inlining
   */
  %strategy computeOccurenceLet_BaseCase(defaultCase:Strategy,variableName:TomName, info:InfoVariable) extends defaultCase {
    visit TomTerm { 
      (Variable|VariableStar)[AstName=name] -> { 
        if(variableName == `name) {
          info.usePosition = getEnvironment().getPosition(); 
          info.readCount++;
          if(info.readCount==2) { throw new VisitFailure(); }
        } 
      } 
    } 
  }

  /* strategies for LetRef inlining (using cps) */
  // comp = special( comp, Basecase(comp,fail()) )
  %op Strategy computeOccurencesLetRef(variableName:TomName, info:InfoVariable) { 
    make(variableName, info) { (
        `Try(
          mu(MuVar("comp"),
            computeOccurencesLetRef_CutCase( MuVar("comp"),
              computeOccurencesLetRef_BaseCase( All(MuVar("comp")), 
                variableName,
                info
                ),
              variableName,
              info
              )
            ))
        ) }
  }


  %strategy CollectVariable(set: HashSet) extends Identity() {
    visit TomTerm {
      (Variable|VariableStar)[AstName=name] -> {
        set.add(`name);
        //stop to visit this branch (like "return false" with traversal) 
        throw new VisitFailure();
      }
    }
  }

  //cases where failure is used to cut branches
  %strategy computeOccurencesLetRef_CutCase(goOnCase:Strategy,defaultCase:Strategy,variableName:TomName,info:InfoVariable) extends defaultCase {
    visit Instruction {
      TypedAction[] -> {
        /* recursive call of the current strategy on the first child */
        Environment current = getEnvironment();
        current.down(1);
        try {
          goOnCase.visit(current);
          current.up();
          return (Instruction) current.getSubject();
        } catch (VisitFailure e) {
          current.upLocal();
          throw new VisitFailure();
        }
      }

      Assign(Variable[AstName=name],src) -> {
        if(variableName == `name) {
          info.setAssignment(`src,getEnvironment().getPosition());
        } else {
          if(info.assignmentVariables.contains(`name)) {
            info.clear();
         }
        }
        /* recursive call of the current strategy on src */
        Environment current = getEnvironment();
        try {
          current.down(2);
          goOnCase.visit(current);
          current.up();
          return (Instruction) current.getSubject();
        } catch (VisitFailure e) {
          current.upLocal();
          throw new VisitFailure();
        }
      }

    }
  }

  %strategy computeOccurencesLetRef_BaseCase(defaultCase:Strategy,variableName:TomName,info:InfoVariable) extends defaultCase {
    visit TomTerm { 
      (Variable|VariableStar)[AstName=name] -> { 
        if(variableName == `name) {
          info.readCount++;
          info.usePosition = getEnvironment().getPosition(); 
          if(info.readCount==2) { 
            throw new VisitFailure(); 
          }
        }  
      } 
    } 
  }

  /* 
   * rename variable1 into variable2
   */
  %op Strategy renameVariable(variable1: TomName, variable2: TomName) {
    make(variable1,variable2) { (`TopDown(renameVariableOnce(variable1,variable2)) ) }
  }

  %strategy renameVariableOnce(variable1:TomName, variable2:TomName) extends Identity() {
    visit TomTerm {
      var@(Variable|VariableStar)[AstName=astName] -> {
        if(variable1 == `astName) {
          return `var.setAstName(variable2);
        }
      }
    }
  }

  %strategy replaceVariableByExpression(variable:TomName, exp:Expression) extends Identity() {
    visit TomTerm {
      (Variable|VariableStar)[AstName=astName] -> {
        if(variable == `astName) {
          return `ExpressionToTomTerm(exp);
        }
      }
    }
  }

  %op Strategy CleanAssign(varname: TomName) {
    make(varname) { (`TopDown(CleanAssignOnce(varname))) }
  }

  %strategy CleanAssignOnce(varname:TomName) extends Identity() {
    visit Instruction {
      Assign((Variable|VariableStar)[AstName=name],_) -> {
        if(`name.equals(varname)) { return `Nop(); }
      }
    }
  }

  private static boolean compare(tom.library.sl.Visitable term1, tom.library.sl.Visitable term2) {
    return factory.remove(term1)==factory.remove(term2);
  }

  %strategy NopElimAndFlatten() extends Identity() {
    visit Instruction {

      AbstractBlock(concInstruction(C1*,AbstractBlock(L1),C2*)) -> {
        logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), "flatten");     
        return `AbstractBlock(concInstruction(C1*,L1*,C2*));
      }

      AbstractBlock(concInstruction(C1*,Nop(),C2*)) -> {
        logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), "nop-elim");     
        return `AbstractBlock(concInstruction(C1*,C2*));
      }  

      AbstractBlock(concInstruction()) -> {
        logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), "abstractblock-elim1");     
        return `Nop();
      } 

      AbstractBlock(concInstruction(i)) -> {
        logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), "abstractblock-elim2");     
        return `i;
      }

      If[SuccesInst=Nop(),FailureInst=Nop()] -> {
        logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), "ifnopnop-elim");     
        return `Nop();
      }


      If[Condition=TrueTL(),SuccesInst=i] -> {
        logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), "iftrue-elim");     
        return `i;
      }

      If[Condition=FalseTL(),FailureInst=i] -> {
        logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), "iffalse-elim");     
        return `i;
      }

    }      

  }

  /*
   * two expressions are incompatible when they cannot be true a the same time
   */ 
  private boolean incompatible(Expression c1, Expression c2) {
    try {
      Expression res = (Expression) `InnermostId(NormExpr(this)).visitLight(`And(c1,c2));
      return res ==`FalseTL();
    } catch(VisitFailure e) {
      return false;
    }
  }

  %strategy IfSwapping(optimizer:TomOptimizer) extends `Identity() {
    visit Instruction {
      AbstractBlock(concInstruction(X1*,I1@If(cond1,_,Nop()),I2@If(cond2,_,Nop()),X2*)) -> {
        String s1 = factory.prettyPrint(factory.remove(`cond1));
        String s2 = factory.prettyPrint(factory.remove(`cond2));
        if(s1.compareTo(s2) < 0) {
          /* swap two incompatible conditions */
          if(optimizer.incompatible(`cond1,`cond2)) {
            logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), "if-swapping");     
            return `AbstractBlock(concInstruction(X1*,I2,I1,X2*));
          }
        }
      }
    }
  }      

  %strategy BlockFusion() extends `Identity() {
    visit Instruction {
      AbstractBlock(concInstruction(X1*,
            Let(var1@(Variable|VariableStar)[AstName=name1],term1,body1),
            Let(var2@(Variable|VariableStar)[AstName=name2],term2,body2),
            X2*)) -> {
        /* Fusion de 2 blocs Let contigus instanciant deux variables egales */
        if(`compare(term1,term2)) {
          if(`compare(var1,var2)) {
            logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), "block-fusion1");
            return `AbstractBlock(concInstruction(X1*,Let(var1,term1,AbstractBlock(concInstruction(body1,body2))),X2*));
          } else {
            InfoVariable info = new InfoVariable();
            `computeOccurencesLet(name1,info).visit(`body2);
            int mult = info.readCount; 
            if(mult==0) {
              logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), "block-fusion2");
              Instruction newBody2 =  (Instruction)(`renameVariable(name2,name1).visitLight(`body2));
              return `AbstractBlock(concInstruction(X1*,Let(var1,term1,AbstractBlock(concInstruction(body1,newBody2))),X2*));
            }
          }
        }
      }
    }
  }      

  %strategy IfFusion() extends `Identity() {
    visit Instruction {
      AbstractBlock(concInstruction(X1*,
            If(cond1,success1,failure1),
            If(cond2,success2,failure2),
            X2*)) -> {
        /* Fusion de 2 blocs If gardes par la meme condition */
        if(`compare(cond1,cond2)) {
          if(`failure1.isNop() && `failure2.isNop()) {
            logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), "if-fusion1");
            Instruction res = `AbstractBlock(concInstruction(X1*,If(cond1,AbstractBlock(concInstruction(success1,success2)),Nop()),X2*));
            //System.out.println(res);

            return res;
          } else {
            logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), "if-fusion2");
            return `AbstractBlock(concInstruction(X1*,If(cond1,AbstractBlock(concInstruction(success1,success2)),AbstractBlock(concInstruction(failure1,failure2))),X2*));
          }
        }
      }
    }
  }

  %strategy InterBlock(optimizer:TomOptimizer) extends Identity() {
    visit Instruction {
      /* interleave two incompatible conditions */
      AbstractBlock(concInstruction(X1*,
            If(cond1,suc1,fail1),
            If(cond2,suc2,Nop()),
            X2*)) -> {
        if(optimizer.incompatible(`cond1,`cond2)) {
          logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), "inter-block");
          return `AbstractBlock(concInstruction(X1*,If(cond1,suc1,AbstractBlock(concInstruction(fail1,If(cond2,suc2,Nop())))),X2*));
        }  
      }
    }      
  }

  %strategy NormExpr(optimizer:TomOptimizer) extends Identity() {
    visit Expression {
      Or(_,TrueTL()) -> TrueTL()
        Or(TrueTL(),_) -> TrueTL()
        Or(t1,FalseTL()) -> t1
        Or(FalseTL(),t1) -> t1
        And(TrueTL(),t1) -> t1
        And(t1,TrueTL()) -> t1
        And(FalseTL(),_) -> FalseTL()
        And(TrueTL(),_) -> FalseTL()

        ref@EqualTerm(_,kid1,kid2) -> {
          //System.out.println("kid1 = " + `kid1);
          //System.out.println("kid2 = " + `kid2);
          if(`compare(kid1,kid2)) {
            return `TrueTL();
          } else {
            return `ref;
          }
        }

      ref@And(IsFsym(name1,term),IsFsym(name2,term)) -> {
        if(`name1==`name2) {
          return `IsFsym(name1,term);
        }
        /*
         * may be true for list operator with domain=codomain
         * two if_sym(f)==is_fsym(g) may be true due to mapping
         */
        TomSymbol tomSymbol = optimizer.symbolTable().getSymbolFromName(`name1.getString());
        if(TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol)) {
          //System.out.println("symbol = " + tomSymbol);
          TomType domain = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
          TomType codomain = TomBase.getSymbolCodomain(tomSymbol);
          if(domain!=codomain) {
            return `FalseTL();
          }
        } else {
          return `FalseTL();
        }
        return `ref;
      }
    } 
  }

} // class TomOptimizer
