/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2007, INRIA
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
  //%include{ ../adt/tomsignature/_TomSignature.tom }
  %include{ ../../library/mapping/java/sl.tom }
  %include{ ../../library/mapping/java/util/ArrayList.tom }
  %include{ ../../library/mapping/java/util/HashSet.tom }

  %typeterm TomOptimizer {
    implement { TomOptimizer }
    is_sort(t) { t instanceof TomOptimizer }
  }

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

      Strategy optStrategy2 = `Sequence(
          InnermostId(ChoiceId(RepeatId(NopElimAndFlatten()),NormExpr(this))),
          InnermostId(
            ChoiceId(
              Sequence(RepeatId(IfSwapping(this)), RepeatId(SequenceId(ChoiceId(BlockFusion(),IfFusion()),OnceTopDownId(NopElimAndFlatten())))),
              SequenceId(InterBlock(this),OnceTopDownId(RepeatId(NopElimAndFlatten()))))
            )
          );

      long startChrono = System.currentTimeMillis();
      boolean intermediate = getOptionBooleanValue("intermediate");
      try {
        TomTerm renamedTerm = (TomTerm)getWorkingTerm();

        if(getOptionBooleanValue("optimize2")) {          
          renamedTerm = (TomTerm) optStrategy2.visitLight(renamedTerm);
          renamedTerm = (TomTerm) `InnermostId(Inline(TrueConstraint())).visitLight(renamedTerm);
          renamedTerm = (TomTerm) optStrategy2.visitLight(renamedTerm);
        } else {
          if(getOptionBooleanValue("optimize")) {
            renamedTerm = (TomTerm) `InnermostId(Inline(TrueConstraint())).visitLight(renamedTerm);
          }
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

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomOptimizer.DECLARED_OPTIONS);
  }

  private static String extractRealName(String name) {
    if(name.startsWith("tom_")) {
      return name.substring(4);
    }
    return name;
  }

  %strategy findRefVariable(set: HashSet) extends `Identity(){
    visit TomTerm {
      Ref((Variable|VariableStar)[AstName=name])  -> {
        set.add(`name);
        //stop to visit this branch (like "return false" with traversal) 
        throw new tom.library.sl.VisitFailure();
      }
    }
  }


  /* strategies for LetRef inlining */
  // comp = special( comp, basecase(comp,fail()) )
  %op Strategy computeOccurencesLetRef(variableName:TomName, info:InfoVariableLetRef) {
    make(variableName, info) {
      `Try(
      mu(MuVar("comp"),
          computeOccurencesLetRefSpecialCase( MuVar("comp"),
                                              computeOccurencesLetRefBaseCase( All(MuVar("comp")), 
                                                                               variableName,
                                                                               info
                                                ),
                                              variableName,
                                              info
            )
         ))
    }
  }
  
  %typeterm InfoVariableLetRef { implement{ InfoVariableLetRef } }

  private static class InfoVariableLetRef {

    public tom.library.sl.Position lastRead;
    public Expression lastAssignment;
    public HashSet<TomName> lastAssignmentVariables= new HashSet();
    public int readCount=0;

    public void setlastvalue(Expression newassignment) {
      lastAssignment=newassignment;
      lastAssignmentVariables.clear();
      try {
        `TopDownCollect(findRefVariable(lastAssignmentVariables)).visitLight(newassignment);
      } catch(tom.library.sl.VisitFailure e) {
        logger.log( Level.SEVERE, "Error during collecting variables in "+newassignment);
      }
    }

    public InfoVariableLetRef() {}

    public InfoVariableLetRef(Expression lastAssignment) {
      setlastvalue(lastAssignment);
    } 

  }


  //case where failure is used to cut branches
  // FIXME: tom should accept "extends [variable]", replace "Sequence(defaultCase,Identity())" by "defaultCase" when possible
  %strategy computeOccurencesLetRefSpecialCase(goOnCase:Strategy,defaultCase:Strategy,variableName:TomName,info:InfoVariableLetRef) extends `Sequence(defaultCase,Identity()) {
    visit Instruction {
      TypedAction[AstInstruction=inst] -> {
        /* recursive call of the current strategy on the first child */
        Environment current = getEnvironment();
        current.down(1);
        goOnCase.visit(current);
        current.up();
        return (Instruction) current.getSubject();
      }

      assign@LetAssign(Variable[AstName=varname],src,inst) -> {
        if (`varname.equals(variableName)) {
          info.setlastvalue(`src);
        }
        else {
          if (info.lastAssignmentVariables.contains(`varname)) {
            info.lastAssignment = null;
            info.lastAssignmentVariables.clear();
          }
        }
        /* recursive call of the current strategy on src */
        Environment current = getEnvironment();
        current.down(2);
        goOnCase.visit(current);
        current.up();
        /* recursive call of the current strategy on inst */
        current.down(3);
        goOnCase.visit(current);
        current.up();
        return (Instruction) current.getSubject();
      }

      // same code as for LetAssign with only one recursive call
      Assign(Variable[AstName=varname],src) -> {
        if (`varname.equals(variableName)) {
          info.setlastvalue(`src);
        } else {
          if (info.lastAssignmentVariables.contains(`varname)) {
            info.lastAssignment = null;
            info.lastAssignmentVariables.clear();
          }
        }
        /* recursive call of the current strategy on src */
        Environment current = getEnvironment();
        current.down(2);
        goOnCase.visit(current);
        current.up();
        return (Instruction) current.getSubject();
      }

    }
  }

  // FIXME: tom should accept "extends [variable]", replace "Sequence(defaultCase,Identity())" by "defaultCase" when possible
  %strategy computeOccurencesLetRefBaseCase(defaultCase:Strategy,variableName:TomName,info:InfoVariableLetRef) extends Sequence(defaultCase,Identity()) {
    visit TomTerm { 
      (Variable|VariableStar)[AstName=name] -> { 
        if(variableName == `name) {
          info.readCount++;
          info.lastRead=getEnvironment().getPosition(); 
          if (info.readCount==2) { throw new VisitFailure(); }
        }  
      } 
    } 
  }

  /* strategies for Let inlining */
  // comp = special1(special2(all(comp),base(all(comp),fail())),fail())
  %op Strategy computeOccurencesLet(variableName:TomName, info:InfoVariableLet) {
    make(variableName, info) {
      `Try(
      mu(MuVar("comp"),
          computeOccurencesLetSpecialCase1( computeOccurencesLetSpecialCase2( MuVar("comp"),
                                                                              computeOccurencesLetBaseCase( All(MuVar("comp")),
                                                                                                           variableName,
                                                                                                           info
                                                                                                         ),
                                                                              variableName, 
                                                                              info
                                                                           ),
                                           info
                                         )
          )) 
    }
  }

  %typeterm InfoVariableLet {
    implement{ InfoVariableLet }
  }

  private static class InfoVariableLet {

    public tom.library.sl.Position lastRead;
    public HashSet<TomName> assignmentVariables= new HashSet();
    public int readCount=0;
    public boolean modifiedAssignmentVariables=false;

    public void setlastvalue(Expression newassignment) {
      assignmentVariables.clear();
      try {
        `TopDownCollect(findRefVariable(assignmentVariables)).visitLight(newassignment);
      } catch(tom.library.sl.VisitFailure e) {
        logger.log( Level.SEVERE, "Error during collecting variables in "+newassignment);
      }
    }

    public InfoVariableLet(Expression assignment) { setlastvalue(assignment); } 

    public InfoVariableLet() {}

  }

  // FIXME: tom should accept "extends [variable]", replace "Sequence(defaultCase,Identity())" by "defaultCase" when possible
  %strategy computeOccurencesLetSpecialCase1(defaultCase:Strategy,info:InfoVariableLet) extends Sequence(defaultCase,Identity()) {
    visit Instruction {
      (Assign|LetAssign)[Variable=Variable[AstName=varname]] -> {
        if (info.assignmentVariables.contains(`varname)) {
          info.modifiedAssignmentVariables=true;
          throw new tom.library.sl.VisitFailure();
        }
      }
    }
  } 

  // FIXME: tom should accept "extends [variable]", replace "Sequence(cutCase,Identity())" by "cutCase" when possible
  %strategy computeOccurencesLetSpecialCase2(goOnCase:Strategy,cutCase:Strategy,variableName:TomName,info:InfoVariableLet) extends Sequence(cutCase,Identity()) {
    visit Instruction {
      TypedAction[AstInstruction=inst] -> {
        /* recursive call of the current strategy on the first child */
        Environment current = getEnvironment();
        current.down(1);
        goOnCase.visit(current);
        current.up();
        return (Instruction) current.getSubject();
      }

      // should not happen
      (Assign|LetAssign)[Variable=Variable[AstName=varname]] -> {
        if (variableName.equals(`varname)) {
          logger.log( Level.SEVERE, "TomOptimizer: Assignment cannot be done for the variable "+variableName+" declared in a let", new Object[]{} );
        }
      }
    }
  }

  // FIXME: tom should accept "extends [variable]", replace "Sequence(cutCase,Identity())" by "cutCase" when possible
  %strategy computeOccurencesLetBaseCase(defaultCase:Strategy,variableName:TomName, info:InfoVariableLet) extends Sequence(defaultCase,Identity()) {
    visit TomTerm { 
      (Variable|VariableStar)[AstName=name] -> { 
        if(variableName == `name) {
          info.readCount++;
          info.lastRead = getEnvironment().getPosition(); 
          if (info.readCount==2) { throw new VisitFailure(); }
        } 
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

  %op Strategy CleanAssign(varname: TomName){
    make(varname) {`TopDown(CleanAssignOnce(varname))}
  }

  %strategy CleanAssignOnce(varname:TomName) extends `Identity() {
    visit Instruction {
      LetAssign(var@(Variable|VariableStar)[AstName=name],exp,body) -> {
        if (`name.equals(varname)) { return `body; }
      }
    }
  }

  private static boolean compare(tom.library.sl.Visitable term1, tom.library.sl.Visitable term2) {
    return factory.remove(term1)==factory.remove(term2);
  }

  %strategy Inline(context:Constraint) extends `Identity() {
    visit TomTerm {
      ExpressionToTomTerm(TomTermToExpression(t)) -> { return `t; }

      /* optimize the insertion of a slice into a list */
      BuildAppendList(name,ExpressionToTomTerm(GetSliceList(name,begin,end,tailSlice)),tail) -> {
        return `ExpressionToTomTerm(GetSliceList(name,begin,end,BuildAppendList(name,tailSlice,tail)));
      }
    }

    visit Expression {
      TomTermToExpression(ExpressionToTomTerm(t)) -> { return `t; }
    }


    visit Instruction {
      t@TypedAction[PositivePattern=positivePattern] -> {
        context = `positivePattern;
        //System.out.println("found context = " + context);
        return `t;      
      }

      /*
       * 
       * LetRef x<-exp in body where x is used 0 or 1 ==> eliminate
       * x should not appear in exp
       */
      LetRef(var@(Variable|VariableStar)[AstName=name@Name(_)],exp,body) -> {
        /*
         * do not optimize Variable(TomNumber...) because LetRef X*=GetTail(X*) in ...
         * is not correctly handled 
         * we must check that X notin exp
         */
        String varName = "";
        %match(name) { Name(tomName) -> { varName = `tomName; } }

        //`findOccurencesUpTo(name,list,2).visitLight(`body);
        InfoVariableLetRef info = new InfoVariableLetRef(`exp);
        `computeOccurencesLetRef(name,info).visit(`body);
        int mult = info.readCount;
        tom.library.sl.Position readPos = info.lastRead;
        TomTerm value = `ExpressionToTomTerm(info.lastAssignment);
        // 0 -> unused variable
        // suppress the letref and all the corresponding letassigns in the body
        if(mult == 0) {
          // why this test?
          if(varName.length() > 0) {
            // TODO: check variable occurence in TypedAction
            info = new InfoVariableLetRef();
            `computeOccurencesLetRef(name,info).visit(`context);
            if(info.readCount<=1) {
              // verify linearity in case of variables from the pattern
              // warning to indicate that this var is unused in the rhs
              Option orgTrack = TomBase.findOriginTracking(`var.getOption());
              TomMessage.warning(logger,orgTrack.getFileName(), orgTrack.getLine(),
                  TomMessage.unusedVariable,`extractRealName(varName));
              logger.log( Level.INFO,
                  TomMessage.remove.getMessage(),
                  new Object[]{ new Integer(mult), `extractRealName(varName) });
            }
          }
          //remove all the unused letassign in the letref body
          return (Instruction) `CleanAssign(name).visitLight(`body);
        } else if(mult == 1) {
          //  `findOccurencesUpTo(name,list,2).visitLight(`exp);
          //test if variables contained in the exp to assign have not been
          //modified between the last assignment and the read
          if(info.lastAssignment!=null) {
            if(varName.length() > 0) {
              logger.log( Level.INFO,
                  TomMessage.inline.getMessage(),
                  new Object[]{ new Integer(mult), `extractRealName(varName) });
            }
            //System.out.println("replace1: " + `var + "\nby: " + `exp);
            return (Instruction) `Sequence(readPos.getReplace(value),CleanAssign(name)).visitLight(`body);
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

      /*
       * 
       * Let x<-exp in body where x is used 0 or 1 ==> eliminate
       * x should not appear in exp
       */
      Let(var@(Variable|VariableStar)[AstName=name@Name(_)],exp,body) -> {
        /*
         * do not optimize Variable(TomNumber...) because LetRef X*=GetTail(X*) in ...
         * is not correctly handled 
         * we must check that X notin exp
         */
        String varName = "";
        %match(name) {
          Name(tomName) -> { varName = `tomName; }
        }

        //`findOccurencesUpTo(name,list,2).visitLight(`body);
        InfoVariableLet info = new InfoVariableLet(`exp);
        `computeOccurencesLet(name,info).visit(`body);
        int mult = info.readCount;
        tom.library.sl.Position readPos = info.lastRead;
        // 0 -> unused variable
        // suppress the letref and all the corresponding letassigns in the body
        if(mult == 0) {
          // why this test?
          if(varName.length() > 0) {
            // TODO: check variable occurence in TypedAction
            info = new InfoVariableLet();
            `computeOccurencesLet(name,info).visit(`context);
            if(info.readCount<=1) {
              // verify linearity in case of variables from the pattern
              // warning to indicate that this var is unused in the rhs
              Option orgTrack = TomBase.findOriginTracking(`var.getOption());
              TomMessage.warning(logger,orgTrack.getFileName(), orgTrack.getLine(),
                  TomMessage.unusedVariable,`extractRealName(varName));
              logger.log( Level.INFO,
                  TomMessage.remove.getMessage(),
                  new Object[]{ new Integer(mult), `extractRealName(varName) });
            }
          }
          //remove all the unused letassign in the letref body
          return `body;
        } else if(mult == 1) {
          //  `findOccurencesUpTo(name,list,2).visitLight(`exp);
          //test if variables contained in the exp to assign have not been
          //modified between the last assignment and the read
          if(! info.modifiedAssignmentVariables) {
            if(varName.length() > 0) {
              logger.log( Level.INFO,
                  TomMessage.inline.getMessage(),
                  new Object[]{ new Integer(mult), `extractRealName(varName) });
            }
            return (Instruction) readPos.getReplace(`ExpressionToTomTerm(exp)).visitLight(`body);
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

  /*
   * two expressions are incompatible when they cannot be true a the same time
   */ 
  private boolean incompatible(Expression c1, Expression c2) {
    try {
      Expression res = (Expression) `InnermostId(NormExpr(this)).visitLight(`And(c1,c2));
      return res ==`FalseTL();
    } catch(tom.library.sl.VisitFailure e) {
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
            logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), new Object[]{"if-swapping"});     
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
            logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                new Object[]{"block-fusion1"});     
            return `AbstractBlock(concInstruction(X1*,Let(var1,term1,AbstractBlock(concInstruction(body1,body2))),X2*));
          } else {
            InfoVariableLet info = new InfoVariableLet();
            //  `findOccurencesUpTo(name1,list,2).visitLight(`body2);
            `computeOccurencesLet(name1,info).visitLight(`body2);
            int mult = info.readCount; 
            if(mult==0){
              logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(),
                  new Object[]{"block-fusion2"});    
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

  %strategy InterBlock(optimizer:TomOptimizer) extends `Identity(){
    visit Instruction {
      /* interleave two incompatible conditions */
      AbstractBlock(concInstruction(X1*,
            If(cond1,suc1,fail1),
            If(cond2,suc2,Nop()),
            X2*)) -> {
        if(optimizer.incompatible(`cond1,`cond2)) {
          logger.log( Level.INFO, TomMessage.tomOptimizationType.getMessage(), new Object[]{"inter-block"});
          return `AbstractBlock(concInstruction(X1*,If(cond1,suc1,AbstractBlock(concInstruction(fail1,If(cond2,suc2,Nop())))),X2*));
        }  
      }
    }      
  }

  %strategy NormExpr(optimizer:TomOptimizer) extends `Identity() {
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
        //System.out.println("kid1 = " + `kid1);
        //System.out.println("kid2 = " + `kid2);
        if(`compare(kid1,kid2)){
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
