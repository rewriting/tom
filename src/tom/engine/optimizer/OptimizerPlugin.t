/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
import tom.engine.adt.tomoption.types.option.OriginTracking;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;

import tom.engine.exception.TomRuntimeException;
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
public class OptimizerPlugin extends TomGenericPlugin {

  %include{ ../adt/tomsignature/TomSignature.tom }
  %include{ ../adt/tomsignature/_TomSignature.tom }
  %include{ ../../library/mapping/java/sl.tom }
  %include{ ../../library/mapping/java/util/ArrayList.tom }
  
  %typeterm TomNameHashSet {
    implement      { java.util.HashSet<TomName> }
    is_sort(t)      { $t instanceof java.util.HashSet }
    equals(l1,l2)  { $l1.equals($l2) }
  }

  %typeterm OptimizerPlugin { implement { OptimizerPlugin } }

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
    return OptionParser.xmlToOptionList(OptimizerPlugin.DECLARED_OPTIONS);
  }

  public void optionChanged(String optionName, Object optionValue) {
    if(optionName.equals("optimize2") && ((Boolean)optionValue).booleanValue() ) {
      setOptionValue("pretty", Boolean.TRUE);
    }
  }

  // this static field is necessary for %strategy instructions that generate static code
  private static PILFactory factory = new PILFactory();
  private static Logger logger = Logger.getLogger("tom.engine.optimizer.OptimizerPlugin");

  /** Constructor */
  public OptimizerPlugin() {
    super("OptimizerPlugin");
  }

  public void run(Map informationTracker) {
    //System.out.println("(debug) I'm in the Tom optimizer : TSM"+getStreamManager().toString());
    if(getOptionBooleanValue("optimize") || getOptionBooleanValue("optimize2")) {
      /* Initialize strategies */
      long startChrono = System.currentTimeMillis();
      boolean intermediate = getOptionBooleanValue("intermediate");
      try {
       Code renamedTerm = (Code)getWorkingTerm();
        if(getOptionBooleanValue("optimize2")) {
          Strategy optStrategy2 = `Sequence(
              InnermostId(ChoiceId(NormExpr(this),NopElimAndFlatten(this))),
              InnermostId(ChoiceId(
                  Sequence(BuiltinRepeatId(IfSwapping(this)), 
                    BuiltinRepeatId(SequenceId(ChoiceId(BlockFusion(this),IfFusion(this)),OnceTopDownId(NopElimAndFlatten(this))))),
                  SequenceId(InterBlock(this),
                    OnceTopDownId(BuiltinRepeatId(NopElimAndFlatten(this)))))
                )
              );
          renamedTerm = optStrategy2.visitLight(renamedTerm);
          renamedTerm = `BuiltinBottomUp(Inline(TrueConstraint(),this)).visit(renamedTerm);
          renamedTerm = optStrategy2.visitLight(renamedTerm);
          //System.out.println("opt renamedTerm = " + renamedTerm);
        } else if(getOptionBooleanValue("optimize")) {
          Strategy optStrategy = `Sequence(
              InnermostId(ChoiceId(NormExpr(this),NopElimAndFlatten(this))),
              BuiltinBottomUp(Inline(TrueConstraint(),this)));

          renamedTerm = optStrategy.visit(renamedTerm);
        }
        setWorkingTerm(renamedTerm);

        // verbose
        TomMessage.info(logger, getStreamManager().getInputFileName(), 0,
            TomMessage.tomOptimizationPhase,
            Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      } catch (Exception e) {
        TomMessage.error(logger, getStreamManager().getInputFileName(), 0,
            TomMessage.exceptionMessage, e.getMessage());
        e.printStackTrace();
         return;
      }
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() + OPTIMIZED_SUFFIX,
            (Code)getWorkingTerm() );
      }
    } else {
      // not active plugin
      TomMessage.info(logger, getStreamManager().getInputFileName(), 0,
          TomMessage.optimizerNotActive);
    }
    if(getOptionBooleanValue("prettyPIL")) {
      System.out.println(factory.prettyPrintCompiledMatch(factory.remove((Code)getWorkingTerm())));
    }
  }

  private final static String PREFIX = "tom_";
  private static String extractRealName(String name) {
    if(name.startsWith(PREFIX)) {
      return name.substring(PREFIX.length());
    }
    return name;
  }

  %strategy Inline(context:Constraint,optimizer:OptimizerPlugin) extends Identity() {
    visit BQTerm {
      /* optimize the insertion of a slice into a list */
      BuildAppendList(name,ExpressionToBQTerm(GetSliceList(name,begin,end,tailSlice)),tail) -> {
        return `ExpressionToBQTerm(GetSliceList(name,begin,end,BuildAppendList(name,tailSlice,tail)));
      }
    }

    visit Instruction {

      /* only for generated variables */
      /* Let x <- y in body where y is a variable ==> inline */
      /* Let x <- (T) y in body where y is a variable ==> inline */
      Let((BQVariable|BQVariableStar)[AstName=name@!Name(concString('t','o','m','_',_*))],exp,body) &&
        (BQTermToExpression((BQVariable|BQVariableStar)[])<<exp ||
         Cast[Source=BQTermToExpression((BQVariable|BQVariableStar)[])]<<exp) -> {
          return `TopDown(replaceVariableByExpression(name,exp)).visitLight(`body);
      }

      Let(var@(BQVariable|BQVariableStar)[AstName=name],exp,body) -> {
        String varName = ""; // real name of the variable (i.e. without the tom_ prefix)
        %match(name) {
          Name(tomName) -> { varName = `extractRealName(tomName); }
        }

        //System.out.println("varName = " + varName);
        //System.out.println("body = " + `body);

        // count the occurence of name
        InfoVariable infoBody = new InfoVariable();
        infoBody.setAssignment(`exp,getPosition());
        getEnvironment().down(3); // 3 = body
        `computeOccurencesLet(name,infoBody).visit(getEnvironment());
        getEnvironment().up();

        if(infoBody.modifiedAssignmentVariables) {
          /* do nothing */
          if(varName.length() > 0) {
            TomMessage.info(logger,null,0,TomMessage.cannotInline,0,varName);
          }
        } else {
          int mult = infoBody.readCount;
          //System.out.println(`name + " --> " + mult);
          if(mult == 0) { // name is not used
            // suppress the Let
            if(varName.length() > 0) {
              // varName is not empty when the variable is a user defined variable (with a name)
              // and not a variable generated by the compiler itself
              // TODO: check variable occurence in RawAction
              InfoVariable infoContext = new InfoVariable();
              `computeOccurencesLet(name,infoContext).visit(`context);
              if(infoContext.readCount<=1 && !varName.startsWith("_")) {
                // variables introduced by renaming starts with a '_'
                // verify linearity in case of variables from the pattern
                // warning to indicate that this var is unused in the rhs
                Option orgTrack = TomBase.findOriginTracking(`var.getOptions());
                TomMessage.warning(logger,(orgTrack instanceof OriginTracking)?orgTrack.getFileName():"unknown file", 
                  (orgTrack instanceof OriginTracking)?orgTrack.getLine():-1,
                  TomMessage.unusedVariable,varName);
                TomMessage.info(logger,null,0,TomMessage.remove,mult,varName);
              }
            }
            return `body;
          } else if(mult == 1) {
            //test if variables contained in the exp to assign have not been
            //modified between the last assignment and the read
            if(varName.length() > 0) {
              TomMessage.info(logger,
                  optimizer.getStreamManager().getInputFileName(), 0,
                  TomMessage.inline, mult, varName);
            }
            Position current = getPosition();
            getEnvironment().goToPosition(infoBody.usePosition);
            getEnvironment().setSubject(`ExpressionToBQTerm(exp));
            getEnvironment().goToPosition(current);
            Instruction newlet = (Instruction) getEnvironment().getSubject();
            // return only the body
            return (Instruction) newlet.getChildAt(2);
          } else {
            /* do nothing */
            if(varName.length() > 0) {
              TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.doNothing,mult,varName);
            }
          }
        }
      }

      /*
       * LetRef x<-exp in body where x is used 0 or 1 ==> eliminate
       * x should not appear in exp
       */
      LetRef(var@(BQVariable|BQVariableStar)[AstName=name@Name[]],exp,body) -> {
        /*
         * do not optimize Variable(TomNumber...) because LetRef X*=GetTail(X*) in ...
         * is not correctly handled
         * we must check that X notin exp
         */
        String varName = "";
        %match(name) {
          Name(tomName) -> { varName = `extractRealName(tomName); }
        }

        InfoVariable info = new InfoVariable();
        info.setAssignment(`exp,getPosition());
        getEnvironment().down(3);
        //computeOccurencesLetRef on the body
        `computeOccurencesLetRef(name,info).visit(getEnvironment());
        getEnvironment().up();
        int mult = info.readCount;
        Position readPos = info.usePosition;

        if(info.assignment==null) {
          if(varName.length() > 0) {
            TomMessage.info(logger,null,0,TomMessage.cannotInline,0,varName);
          }
        } else {
          //System.out.println(`name + " --> " + mult);
          // 0 -> unused variable
          // suppress the letref and all the corresponding letassigns in the body
          if(mult == 0) {
            // why this test?
            if(varName.length() > 0) {
              // TODO: check variable occurence in RawAction
              info = new InfoVariable();
              `computeOccurencesLetRef(name,info).visit(`context);
              if(info.readCount<=1 && !varName.startsWith("_")) {
                // verify linearity in case of variables from the pattern
                // warning to indicate that this var is unused in the rhs
                Option orgTrack = TomBase.findOriginTracking(`var.getOptions());
                TomMessage.warning(logger,orgTrack.getFileName(), orgTrack.getLine(),
                    TomMessage.unusedVariable,varName);
                TomMessage.info(logger,null,0,TomMessage.remove,mult,varName);
              }
            }
            return `CleanAssign(name).visitLight(`body);
          } else if(mult == 1) {
            //test if variables contained in the exp to assign have not been
            //modified between the last assignment and the use
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
                TomMessage.info(logger,
                    optimizer.getStreamManager().getInputFileName(), 0,
                    TomMessage.inline, mult, varName);
              }
              getEnvironment().goToPosition(readPos);
              BQTerm value = `ExpressionToBQTerm(info.assignment);
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
                TomMessage.info(logger,
                    optimizer.getStreamManager().getInputFileName(), 0,
                    TomMessage.noInline, mult, varName);
              }
            }
          } else {
            /* do nothing: traversal() */
            if(varName.length() > 0) {
              TomMessage.info(logger,
                  optimizer.getStreamManager().getInputFileName(), 0,
                  TomMessage.doNothing, mult, varName);
            }
          }
        }
      }

    }
  }

  %typeterm InfoVariable { implement{ InfoVariable } }

  private static class InfoVariable {

    private HashSet<TomName> assignmentVariables = new HashSet<TomName>();
    public int readCount=0;
    public boolean modifiedAssignmentVariables=false; // true when exp depends on a variable which is modified by Assign
    public Position usePosition; // position where is used the variable
    /*
     * for LetRef
     */
    private Position assignmentPosition;
    private Expression assignment;

    public InfoVariable() {}

    public void setAssignment(Expression newAssignment,Position newAssignmentPosition) {
      assignment = newAssignment;
      assignmentPosition = newAssignmentPosition;
      assignmentVariables.clear();
      try {
        `TopDownCollect(CollectVariable(assignmentVariables)).visitLight(newAssignment);
      } catch(VisitFailure e) {
        throw new TomRuntimeException("Error during collecting variables in "+newAssignment);
      }
    }

    public Set getAssignmentVariables() {
      return assignmentVariables;
    }

    public void clear() {
      assignment = null;
      assignmentPosition = null;
      assignmentVariables.clear();
    }

  }

  %strategy CollectVariable(set:TomNameHashSet) extends Identity() {
    visit BQTerm {
      (BQVariable|BQVariableStar)[AstName=name] -> {
        set.add(`name);
        //stop to visit this branch (like "return false" with traversal)
        throw new VisitFailure();
      }
    }
  }

  /* strategies for Let inlining (using cps) */
  // comp = AssignCase(RawActionCase(comp,BaseCase(all(comp),fail())),fail())
  %op Strategy computeOccurencesLet(variableName:TomName, info:InfoVariable) {
    make(variableName, info) {
        `Try(
          mu(MuVar("comp"),
            computeOccurenceLet_AssignCase(
              computeOccurenceLet_RawActionCase( MuVar("comp"),
                computeOccurenceLet_BaseCase( All(MuVar("comp")), variableName, info),
                variableName,
                info
                ),
              info
              )
            )
          )
    }
  }

  /*
   * check if a variable appearing in an expression (in Let x <- exp ...)
   * is modified (by applying Assign on the variables contained in exp)
   */
  %strategy computeOccurenceLet_AssignCase(defaultCase:Strategy,info:InfoVariable) extends defaultCase {
    visit Instruction {
      (Assign|AssignArray)[Variable=BQVariable[AstName=varname]] -> {
        if(info.getAssignmentVariables().contains(`varname)) {
          info.modifiedAssignmentVariables=true;
          throw new VisitFailure();
        }
      }
    }
  }

  %strategy computeOccurenceLet_RawActionCase(goOnCase:Strategy,cutCase:Strategy,variableName:TomName,info:InfoVariable) extends cutCase {
    visit Instruction {
      RawAction[] -> {

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
      (Assign|AssignArray)[Variable=BQVariable[AstName=varname]] -> {
        if(variableName.equals(`varname)) {
          throw new TomRuntimeException("OptimizerPlugin: Assignment cannot be done for the variable "+variableName+" declared in a let");
        }
      }
    }
  }

  /*
   * when variableName is encountered, its position is stored
   * if it appears more than once, the computation is stopped because there is no possible inlining
   */
  %strategy computeOccurenceLet_BaseCase(defaultCase:Strategy,variableName:TomName, info:InfoVariable) extends defaultCase {

    visit BQTerm {
      (BQVariable|BQVariableStar)[AstName=name] -> {
        if(variableName == `name) {
          info.usePosition = getPosition();
          info.readCount++;
          if(info.readCount==2) { throw new VisitFailure(); }
        }
      }
    }
  }

  %strategy Print(name:String) extends Identity() {
    visit Instruction {
      x -> {
        System.out.println(name + " inst: " + `x);
      }
    }
    visit InstructionList {
      x -> {
        System.out.println(name + " instlist: " + `x);
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

  //cases where failure is used to cut branches
  %strategy computeOccurencesLetRef_CutCase(goOnCase:Strategy,defaultCase:Strategy,variableName:TomName,info:InfoVariable) extends defaultCase {
    visit Instruction {
      RawAction[] -> {
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

      (Assign|AssignArray)[Variable=BQVariable[AstName=name],Source=src] -> {
        if(variableName == `name) {
          info.setAssignment(`src,getPosition());
        } else {
          if(info.getAssignmentVariables().contains(`name)) {
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
    visit BQTerm {
      (BQVariable|BQVariableStar)[AstName=name] -> {
        if(variableName == `name) {
          info.readCount++;
          info.usePosition = getPosition();
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
    visit BQTerm {
      var@(BQVariable|BQVariableStar)[AstName=astName] -> {
        if(variable1 == `astName) {
          return `var.setAstName(variable2);
        }
      }
    }
  }

  %strategy replaceVariableByExpression(variable:TomName, exp:Expression) extends Identity() {
    visit BQTerm {
      (BQVariable|BQVariableStar)[AstName=astName] -> {
        if(variable == `astName) {
          return `ExpressionToBQTerm(exp);
        }
      }
    }
  }

  %op Strategy CleanAssign(varname: TomName) {
    make(varname) { (`TopDown(CleanAssignOnce(varname))) }
  }

  %strategy CleanAssignOnce(varname:TomName) extends Identity() {
    visit Instruction {
      (Assign|AssignArray)[Variable=(BQVariable|BQVariableStar)[AstName=name]] -> {
        if(`name.equals(varname)) { return `Nop(); }
      }
    }
  }

  private static boolean compare(tom.library.sl.Visitable term1, tom.library.sl.Visitable term2) {
    return factory.remove(term1)==factory.remove(term2);
  }

  %strategy NopElimAndFlatten(optimizer:OptimizerPlugin) extends Identity() {
    visit Instruction {

      AbstractBlock(concInstruction(C1*,AbstractBlock(L1),C2*)) -> {
        TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"flatten");
        return `AbstractBlock(concInstruction(C1*,L1*,C2*));
      }

      AbstractBlock(concInstruction(C1*,Nop(),C2*)) -> {
        TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"nop-elim");
        return `AbstractBlock(concInstruction(C1*,C2*));
      }

      AbstractBlock(concInstruction()) -> {
        TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"abstractblock-elim1");
        return `Nop();
      }

      AbstractBlock(concInstruction(i)) -> {
        TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"abstractblock-elim2");
        return `i;
      }

      If[SuccesInst=Nop(),FailureInst=Nop()] -> {
        TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"ifnopnop-elim");
        return `Nop();
      }

      If[Condition=TrueTL(),SuccesInst=i] -> {
        TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"iftrue-elim");
        return `i;
      }

      If[Condition=FalseTL(),FailureInst=i] -> {
        TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"iffalse-elim");
        return `i;
      }

    }
  }

  /*
   * two expressions are incompatible when they cannot be true a the same time
   */
  private boolean incompatible(Expression c1, Expression c2) {
    try {
      Expression res = `InnermostId(NormExpr(this)).visitLight(`And(c2,c2));
      return res ==`FalseTL();
    } catch(VisitFailure e) {
      return false;
    }
  }

  %strategy IfSwapping(optimizer:OptimizerPlugin) extends Identity() {
    visit Instruction {
      AbstractBlock(concInstruction(X1*,I1@If(cond1,_,Nop()),I2@If(cond2,_,Nop()),X2*)) -> {
        String s1 = factory.prettyPrint(factory.remove(`cond1));
        String s2 = factory.prettyPrint(factory.remove(`cond2));
        if(s1.compareTo(s2) < 0) {
          /* swap two incompatible conditions */
          if(optimizer.incompatible(`cond1,`cond2)) {
            TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"if-swapping");
            return `AbstractBlock(concInstruction(X1*,I2,I1,X2*));
          }
        }
      }
    }
  }

  %strategy BlockFusion(optimizer:OptimizerPlugin) extends Identity() {
    visit Instruction {
      block@AbstractBlock(concInstruction(X1*,
            Let(var1@(BQVariable|BQVariableStar)[AstName=name1],term1,body1),
            Let(var2@(BQVariable|BQVariableStar)[AstName=name2],term2,body2),
            X2*)) -> {
        /* Fusion de 2 blocs Let contigus instanciant deux variables egales */
        if(`compare(term1,term2)) {
          if(`compare(var1,var2)) {
            TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"block-fusion1");
            return `(block.setInstList(concInstruction(X1*,Let(var1,term1,AbstractBlock(concInstruction(body1,body2))),X2*)));
          } else {
            InfoVariable info = new InfoVariable();
            `computeOccurencesLet(name1,info).visit(`body2);
            int mult = info.readCount;
            if(mult==0) {
              TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"block-fusion2");
              Instruction newBody2 =  `renameVariable(name2,name1).visitLight(`body2);
              return `(block.setInstList(concInstruction(X1*,Let(var1,term1,AbstractBlock(concInstruction(body1,newBody2))),X2*)));
            }
          }
        }
      }
    }
  }

  %strategy IfFusion(optimizer:OptimizerPlugin) extends Identity() {
    visit Instruction {
      block@(UnamedBlock|AbstractBlock)(concInstruction(X1*,
            If(cond1,success1,failure1),
            If(cond2,success2,failure2),
            X2*)) -> {
        Expression c1 = factory.remove(`cond1);
        Expression c2 = factory.remove(`cond2);
        //System.out.println("c1 = " + c1);
        //System.out.println("c2 = " + c2);
        %match(Expression c1,Expression c2) {
          c,c -> {
            /* Merge 2 blocks whose conditions are equals */
            if(`failure1.isNop() && `failure2.isNop()) {
              TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"if-fusion1");
              Instruction res = `(block.setInstList(concInstruction(X1*,If(cond1,AbstractBlock(concInstruction(success1,success2)),Nop()),X2*)));
              //System.out.println(res);

              return res;
            } else {
              TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"if-fusion2");
              return `(block.setInstList(concInstruction(X1*,If(cond1,AbstractBlock(concInstruction(success1,success2)),AbstractBlock(concInstruction(failure1,failure2))),X2*)));
            }
          }

          Negation(c),c -> {
            /* Merge 2 blocks whose conditions are the negation of the other */
            TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"if-fusion-not");
            return `(block.setInstList(concInstruction(X1*,If(cond1,AbstractBlock(concInstruction(success1,failure2)),AbstractBlock(concInstruction(failure1,success2))),X2*)));
          }
        }
      }
    }
  }

  %strategy InterBlock(optimizer:OptimizerPlugin) extends Identity() {
    visit Instruction {
      /* interleave two incompatible conditions */
      AbstractBlock(concInstruction(X1*,
            If(cond1,suc1,fail1),
            If(cond2,suc2,Nop()),
            X2*)) -> {
        if(optimizer.incompatible(`cond1,`cond2)) {
          TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"inter-block");
          return `AbstractBlock(concInstruction(X1*,If(cond1,suc1,AbstractBlock(concInstruction(fail1,If(cond2,suc2,Nop())))),X2*));
        }
      }
    }
  }

  %strategy NormExpr(optimizer:OptimizerPlugin) extends Identity() {
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
        TomSymbol tomSymbol = optimizer.getSymbolTable().getSymbolFromName(`name1.getString());
        if(TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol)) {
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

}
