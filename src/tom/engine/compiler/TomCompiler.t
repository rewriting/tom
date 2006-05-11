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

package tom.engine.compiler;

import java.util.*;
import java.util.logging.Level;

import tom.engine.adt.tomsignature.types.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * The TomCompiler plugin.
 */
public class TomCompiler extends TomGenericPlugin {

  %include { adt/tomsignature/TomSignature.tom }
  %include { mutraveler.tom }
  
  %typeterm Set {
    implement      { java.util.Set }
    equals(l1,l2)  { l1.equals(l2) }
  }

 %typeterm TomCompiler {
    implement { TomCompiler }
  }


  /** some output suffixes */
  public static final String COMPILED_SUFFIX = ".tfix.compiled";

  /** the declared options string*/
  public static final String DECLARED_OPTIONS = "<options><boolean name='compile' altName='' description='Compiler (activated by default)' value='true'/></options>";

  /** unicity var counter*/
  private static int absVarNumber;

  /** Constructor*/
  public TomCompiler() {
    super("TomCompiler");
  }

  public void run() {
    TomKernelCompiler tomKernelCompiler = new TomKernelCompiler(getStreamManager().getSymbolTable());
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    try {
      // reinit absVarNumber to generate reproducible output
      absVarNumber = 0;
      TomTerm preCompiledTerm = (TomTerm)(MuTraveler.init(`preProcessing(this)).visit(((TomTerm)getWorkingTerm())));
      //System.out.println("preCompiledTerm = \n" + preCompiledTerm);
      TomTerm compiledTerm = tomKernelCompiler.compileMatching(preCompiledTerm);
      Set hashSet = new HashSet();
      TomTerm renamedTerm = (TomTerm)MuTraveler.init(`TopDown(findRenameVariable(hashSet))).visit(compiledTerm);
      //TomTerm renamedTerm = compiledTerm;
      // verbose
      getLogger().log( Level.INFO, TomMessage.tomCompilationPhase.getMessage(),
          new Integer((int)(System.currentTimeMillis()-startChrono)) );      
      setWorkingTerm(renamedTerm);
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileNameWithoutSuffix() + COMPILED_SUFFIX, (TomTerm)getWorkingTerm());
      }
    } catch (Exception e) {
      getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
          new Object[]{getStreamManager().getInputFileName(), "TomCompiler", e.getMessage()} );
      e.printStackTrace();
    }
  }

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomCompiler.DECLARED_OPTIONS);
  }

  private static OptionList option() {
    return ASTFactory.makeOption();
  }

  /* 
   * preProcessing:
   * replaces BuildReducedTerm by BuildList, BuildArray or BuildTerm
   *
   * transforms RuleSet into Function + Match + BuildReducedTerm
   * abstract list-matching patterns
   * rename non-linear patterns
   */

  %op VisitableVisitor preProcessing(compiler:TomCompiler){
     make(compiler){
       `(mu(MuVar("x"),Choice(
               preProcessing_once(compiler),
               All(MuVar("x")))))
     }
   }


  %strategy preProcessing_once(compiler:TomCompiler) extends `Fail(){
    visit TomTerm {
      BuildReducedTerm(var@(Variable|VariableStar)[]) -> {
        return `var;
      }    

      BuildReducedTerm(RecordAppl[option=optionList,nameList=(name@Name(tomName)),slots=termArgs]) -> {
        TomSymbol tomSymbol = compiler.symbolTable().getSymbolFromName(`tomName);
        SlotList newTermArgs = (SlotList) (MuTraveler.init(`preProcessing_makeTerm(compiler)).visit(`termArgs));
        TomList tomListArgs = slotListToTomList(newTermArgs);

        if(hasConstant(`optionList)) {
          return `BuildConstant(name);
        } else if(tomSymbol != null) {
          if(isListOperator(tomSymbol)) {
            return ASTFactory.buildList(`name,tomListArgs);
          } else if(isArrayOperator(tomSymbol)) {
            return ASTFactory.buildArray(`name,tomListArgs);
          } else if(isDefinedSymbol(tomSymbol)) {
            return `FunctionCall(name,tomListArgs);
          } else {
            String moduleName = getModuleName(`optionList);
            if(moduleName==null) {
              moduleName = TomBase.DEFAULT_MODULE_NAME;
            }
            return `BuildTerm(name,tomListArgs,moduleName);
          }
        } else {
          return `FunctionCall(name,tomListArgs);
        }

      }

    } // end match

    visit Instruction {
      Match(SubjectList(l1),patternInstructionList, matchOptionList)  -> {
        Option orgTrack = findOriginTracking(`matchOptionList);
        PatternInstructionList newPatternInstructionList = `concPatternInstruction();
        PatternList negativePattern = `concPattern();
        while(!`patternInstructionList.isEmpty()) {
          /*
           * the call to preProcessing performs the recursive expansion
           * of nested match constructs
           */
          PatternInstruction newPatternInstruction = (PatternInstruction) MuTraveler.init(`preProcessing(compiler)).visit(`patternInstructionList.getHead());

matchBlock: {
              %match(PatternInstruction newPatternInstruction) {
                PatternInstruction(pattern@Pattern[subjectList=subjectList,tomList=termList,guards=guardList],actionInst, option) -> {
                  Instruction newAction = `actionInst;
                  /* expansion of RawAction into TypedAction */
                  %match(Instruction actionInst) {
                    RawAction(x) -> { 
                      newAction=`TypedAction(If(TrueTL(),x,Nop()),pattern,negativePattern);
                    }
                  }
                  negativePattern = (PatternList) negativePattern.append(`pattern);

                  /* generate equality checks */
                  ArrayList equalityCheck = new ArrayList();
                  TomList renamedTermList = linearizePattern(`termList,equalityCheck);
                  newPatternInstruction = `PatternInstruction(Pattern(subjectList,renamedTermList,guardList),newAction, option);        
                  /* attach guards to variables or applications*/
                  TomList constrainedTermList = renamedTermList;
                  TomList l = `guardList;
                  while(!l.isEmpty()) {
                    TomTerm guard = l.getHead();
                    //System.out.println("try to attach "+guard+"\nto "+constrainedTermList);
                    constrainedTermList = compiler.attachConstraint(constrainedTermList,guard);
                    l = l.getTail();
                  }
                  TomList emptyGuardList = `empty();
                  newPatternInstruction = `PatternInstruction(Pattern(subjectList,constrainedTermList,emptyGuardList),newAction, option);        

                  /* abstract patterns */
                  ArrayList abstractedPattern  = new ArrayList();
                  ArrayList introducedVariable = new ArrayList();
                  TomList newTermList = compiler.abstractPatternList(renamedTermList, abstractedPattern, introducedVariable);

                  /* newPatternInstruction is overwritten when abstraction is performed */
                  if(abstractedPattern.size() > 0) {
                    /* generate a new match construct */

                    TomList generatedSubjectList = `ASTFactory.makeList(introducedVariable);
                    PatternInstruction generatedPatternInstruction =
                      `PatternInstruction(Pattern(generatedSubjectList, ASTFactory.makeList(abstractedPattern),emptyGuardList),newAction, concOption());        
                    /* We reconstruct only a list of option with orgTrack and GeneratedMatch*/
                    OptionList generatedMatchOptionList = `concOption(orgTrack,GeneratedMatch());
                    Instruction generatedMatch =
                      `Match(SubjectList(generatedSubjectList),
                          concPatternInstruction(generatedPatternInstruction),
                          generatedMatchOptionList);
                    generatedMatch = (Instruction) MuTraveler.init(`preProcessing(compiler)).visit(generatedMatch);
                    /*System.out.println("Generate new Match"+generatedMatch); */
                    newPatternInstruction =
                      `PatternInstruction(Pattern(subjectList,newTermList,emptyGuardList),generatedMatch, option);

                    /*System.out.println("newPatternInstruction = " + newPatternInstruction); */
                  }
                  /* do nothing */
                  break matchBlock;
                }

                _ -> {
                  System.out.println("preProcessing: strange PatternInstruction: " + `newPatternInstruction);
                  throw new TomRuntimeException("preProcessing: strange PatternInstruction: " + `newPatternInstruction);
                }
              }
            } // end matchBlock

            newPatternInstructionList = (PatternInstructionList) newPatternInstructionList.append(newPatternInstruction);
            `patternInstructionList = `patternInstructionList.getTail();
        }

        Instruction newMatch = `Match(SubjectList(l1),
            newPatternInstructionList,
            matchOptionList);
        return newMatch;
      }

    } // end match

    visit Declaration {
      Strategy(name,extendsTerm,visitList,orgTrack) -> {
        DeclarationList l = `concDeclaration();//represents compiled Strategy
        TomVisitList jVisitList = `visitList;
        TomForwardType visitorFwd = null;
        while (!jVisitList.isEmpty()){
          TomList subjectListAST = empty();
          TomVisit visit = jVisitList.getHead();
          %match(TomVisit visit) {
            VisitTerm(vType@Type[tomType = ASTTomType(type)],patternInstructionList,_) -> {
              if (visitorFwd == null) {//first time in loop
                visitorFwd = compiler.symbolTable().getForwardType(`type);//do the job only once
              }
              TomTerm arg = `Variable(option(),Name("tom__arg"),vType,concConstraint());//arg subjectList
              subjectListAST = append(arg,subjectListAST);
              String funcName = "visit_" + `type;//function name
              Instruction matchStatement = `Match(SubjectList(subjectListAST),patternInstructionList, concOption(orgTrack));
              //return default strategy.visit(arg)
              Instruction returnStatement = `Return(FunctionCall(Name("super." + funcName),subjectListAST));
              InstructionList instructions = `concInstruction(matchStatement, returnStatement);
              l = `concDeclaration(l*,MethodDef(Name(funcName),concTomTerm(arg),vType,TomTypeAlone("jjtraveler.VisitFailure"),AbstractBlock(instructions)));
            }
          }
          jVisitList = jVisitList.getTail();
        }
        return (Declaration) MuTraveler.init(`preProcessing(compiler)).visit(`Class(name,visitorFwd,extendsTerm,AbstractDecl(l)));
      }

      RuleSet(rl@manyTomRuleList(RewriteRule[lhs=Term(RecordAppl[nameList=(Name(tomName))])],_),optionList) -> {
        TomSymbol tomSymbol = compiler.symbolTable().getSymbolFromName(`tomName);
        TomName name = tomSymbol.getAstName();
        String moduleName = getModuleName(`optionList);
        PatternInstructionList patternInstructionList  = `concPatternInstruction();

        //build variables list for lhs symbol
        TomTypeList typesList = getSymbolDomain(tomSymbol);
        TomList subjectListAST = empty();
        TomNumberList path = `concTomNumber(RuleVar());
        int index = 0; 
        while(!typesList.isEmpty()) {
          TomType subtermType = typesList.getHead();
          TomTerm variable = `Variable(option(),PositionName(appendNumber(index,path)),subtermType,concConstraint());
          subjectListAST = append(variable,subjectListAST);
          typesList = typesList.getTail();
          index++;
        }

        TomRuleList ruleList = `rl;
        TomList guardList = empty();//no guardlist in pattern
        while(!ruleList.isEmpty()) {
          TomRule rule = ruleList.getHead();
          %match(TomRule rule) {
            RewriteRule(Term(RecordAppl[slots=matchPatternsList]),//lhsTerm
                Term(rhsTerm),
                condList,
                option) -> {
              //transform rhsTerm into Instruction to build PatternInstructionList
              TomTerm newRhs = `BuildReducedTerm(rhsTerm);
              Instruction rhsInst = `If(TrueTL(),Return(newRhs),Nop());
              Instruction newRhsInst = compiler.buildCondition(`condList,`rhsInst);
              Pattern pattern = `Pattern(subjectListAST,slotListToTomList(matchPatternsList),guardList);
              patternInstructionList = (PatternInstructionList) patternInstructionList.append(`PatternInstruction(pattern,RawAction(newRhsInst),option));
            }
          } 
          ruleList = ruleList.getTail();
        }

        Instruction matchAST = `Match(SubjectList(subjectListAST),
            patternInstructionList, optionList);
        //return type `name(subjectListAST)
        Instruction buildAST = `Return(BuildTerm(name,(TomList) MuTraveler.init(preProcessing_makeTerm(compiler)).visit(subjectListAST),moduleName));
        Instruction functionBody =  (Instruction) MuTraveler.init(`preProcessing(compiler)).visit(`AbstractBlock(concInstruction(matchAST,buildAST)));

        //find codomain    
        TomType codomain = getSymbolCodomain(tomSymbol);

        return `FunctionDef(name,subjectListAST,codomain,EmptyType(),functionBody);
      }
    }//end match
    // end apply
  }

  %op VisitableVisitor preProcessing_makeTerm(compiler:TomCompiler){
     make(compiler){`mu(MuVar("x"),Choice(preProcessing_makeTerm_once(compiler),All(MuVar("x"))))}
  }


  %strategy preProcessing_makeTerm_once(compiler:TomCompiler) extends `Fail()  {
    visit TomTerm {
      t -> {return (TomTerm) MuTraveler.init(`preProcessing(compiler)).visit(`BuildReducedTerm((TomTerm)t));}
    }
  } 

  private Instruction buildCondition(InstructionList condList, Instruction action) {
    %match(InstructionList condList) {
      emptyInstructionList() -> { return action; }

      manyInstructionList(MatchingCondition[lhs=pattern,rhs=subject], tail) -> {
        try{
        Instruction newAction = `buildCondition(tail,action);

        TomType subjectType = getTermType(`pattern);
        TomNumberList path = tsf().makeTomNumberList();
        path = (TomNumberList) path.append(`RuleVar());
        TomTerm newSubject = (TomTerm)(MuTraveler.init(`preProcessing(this)).visit(`BuildReducedTerm(subject)));
        TomTerm introducedVariable = newSubject;
        TomList guardList = empty();
        TomList generatedSubjectList = `cons(introducedVariable,empty()); 
        /*
         * we do not use RawAction nor TypedAction here because the generated match should not
         * produce any proof obligation for the verifier
         */
        PatternInstruction generatedPatternInstruction =
          `PatternInstruction(Pattern(generatedSubjectList, cons(pattern,empty()),guardList),newAction, option());        

        // Warning: The options are not good
        Instruction generatedMatch =
          `Match(SubjectList(generatedSubjectList),
              concPatternInstruction(generatedPatternInstruction),
              option());
        return generatedMatch;
        }catch(VisitFailure e){}
      }

      manyInstructionList(TypedEqualityCondition[tomType=type,lhs=lhs,rhs=rhs], tail) -> {
        try{
        Instruction newAction = `buildCondition(tail,action);

        TomTerm newLhs = (TomTerm)(MuTraveler.init(`preProcessing(this)).visit(`BuildReducedTerm(lhs)));
        
        TomTerm newRhs = (TomTerm)(MuTraveler.init(`preProcessing(this)).visit(`BuildReducedTerm(rhs)));
        Expression equality = `EqualTerm(type,newLhs,newRhs);
        Instruction generatedTest = `If(equality,newAction,Nop());
        return generatedTest;
        }catch(VisitFailure e){}
      }
    }
    throw new TomRuntimeException("buildCondition strange term: " + condList);
  }

  private static TomTerm renameVariable(TomTerm subject,
      Map multiplicityMap,
      ArrayList equalityCheck) {
    TomTerm renamedTerm = subject;

    %match(TomTerm subject) {
      var@(UnamedVariable|UnamedVariableStar)[constraints=constraints] -> {
        ConstraintList newConstraintList = `renameVariableInConstraintList(constraints,multiplicityMap,equalityCheck);
        return `var.setConstraints(newConstraintList);
      }

      var@(Variable|VariableStar)[astName=name,constraints=clist] -> {
        ConstraintList newConstraintList = renameVariableInConstraintList(`clist,multiplicityMap,equalityCheck);
        if(!multiplicityMap.containsKey(`name)) {
          // We see this variable for the first time
          multiplicityMap.put(`name,new Integer(1));
          renamedTerm = `var.setConstraints(newConstraintList);
        } else {
          // We have already seen this variable
          Integer multiplicity = (Integer) multiplicityMap.get(`name);
          int mult = multiplicity.intValue(); 
          multiplicityMap.put(`name,new Integer(mult+1));

          TomNumberList path = tsf().makeTomNumberList();
          path = (TomNumberList) path.append(`RenamedVar(name));
          path = (TomNumberList) path.append(makeNumber(mult));

          renamedTerm = `var.setAstName(`PositionName(path));
          renamedTerm = renamedTerm.setConstraints(`concConstraint(Equal(var.setConstraints(concConstraint())),newConstraintList*));
        }

        return renamedTerm;
      }

      RecordAppl[option=optionList, nameList=nameList, slots=arguments, constraints=constraints] -> {
        SlotList args = `arguments;
        SlotList newArgs = `emptySlotList();
        while(!args.isEmpty()) {
          Slot elt = args.getHead();
          TomTerm newElt = renameVariable(elt.getAppl(),multiplicityMap,equalityCheck);
          newArgs = (SlotList) newArgs.append(`PairSlotAppl(elt.getSlotName(),newElt));
          args = args.getTail();
        }
        ConstraintList newConstraintList = renameVariableInConstraintList(`constraints,multiplicityMap,equalityCheck);
        renamedTerm = `RecordAppl(optionList,nameList,newArgs,newConstraintList);
        return renamedTerm;
      }
    }
    return renamedTerm;
  }

  private static ConstraintList renameVariableInConstraintList(ConstraintList constraintList,
      Map multiplicityMap,
      ArrayList equalityCheck) {
    ArrayList list = new ArrayList();
    while(!constraintList.isEmpty()) {
      Constraint cstElt = constraintList.getHead();
      Constraint newCstElt = cstElt;
      %match(Constraint cstElt) {
        AssignTo(var@Variable[]) -> {
          newCstElt = `AssignTo(renameVariable(var,multiplicityMap,equalityCheck));
        }
      }
      list.add(newCstElt);
      constraintList = constraintList.getTail();
    }
    return ASTFactory.makeConstraintList(list);
  }

  private static TomList linearizePattern(TomList subject, ArrayList equalityCheck) {
    Map multiplicityMap = new HashMap();
    // perform the renaming and generate equality checks
    TomList newList = empty();
    while(!subject.isEmpty()) {
      TomTerm elt = subject.getHead();
      TomTerm newElt = renameVariable(elt,multiplicityMap,equalityCheck);
      newList = append(newElt,newList);
      subject = subject.getTail();
    }
    return newList;
  }

  private TomTerm abstractPattern(TomTerm subject,
      ArrayList abstractedPattern,
      ArrayList introducedVariable)  {
    TomTerm abstractedTerm = subject;
    %match(TomTerm subject) {
      RecordAppl[nameList=(Name(tomName),_*), slots=arguments] -> {
        TomSymbol tomSymbol = symbolTable().getSymbolFromName(`tomName);

        SlotList newArgs = `emptySlotList();
        if(isListOperator(tomSymbol) || isArrayOperator(tomSymbol)) {
          SlotList args = `arguments;
          while(!args.isEmpty()) {
            Slot elt = args.getHead();
            TomTerm newElt = elt.getAppl();
            %match(TomTerm newElt) {
              appl@RecordAppl[nameList=(Name(tomName2),_*)] -> {
                /*
                 * we no longer abstract syntactic subterm
                 * they are compiled by the TomKernelCompiler
                 */

                //System.out.println("Abstract: " + appl);
                TomSymbol tomSymbol2 = symbolTable().getSymbolFromName(`tomName2);
                if(isListOperator(tomSymbol2) || isArrayOperator(tomSymbol2)) {
                  TomType type2 = tomSymbol2.getTypesToType().getCodomain();
                  abstractedPattern.add(`appl);

                  TomNumberList path = tsf().makeTomNumberList();
                  //path = append(`AbsVar(makeNumber(introducedVariable.size())),path);
                  absVarNumber++;
                  path = (TomNumberList) path.append(`AbsVar(makeNumber(absVarNumber)));

                  TomTerm newVariable = `Variable(option(),PositionName(path),type2,concConstraint());

                  //System.out.println("newVariable = " + newVariable);

                  introducedVariable.add(newVariable);
                  newElt = newVariable;
                }
              }
            }
            newArgs = (SlotList) newArgs.append(`PairSlotAppl(elt.getSlotName(),newElt));
            args = args.getTail();
          }
        } else {
          newArgs = mergeTomListWithSlotList(abstractPatternList(slotListToTomList(`arguments),abstractedPattern,introducedVariable),`arguments);
        }
        abstractedTerm = subject.setSlots(newArgs);
      }
    } // end match
    return abstractedTerm;
  }

  private TomList abstractPatternList(TomList subjectList,
      ArrayList abstractedPattern,
      ArrayList introducedVariable)  {
    %match(TomList subjectList) {
      emptyTomList() -> { return subjectList; }
      manyTomList(head,tail) -> {
        TomTerm newElt = abstractPattern(`head,abstractedPattern,introducedVariable);
        return `manyTomList(newElt,abstractPatternList(tail,abstractedPattern,introducedVariable));
      }
    }
    throw new TomRuntimeException("abstractPatternList: " + subjectList);
  }


  /***********************************/
  /* functions related to the 'when' */
  /***********************************/


  /*
   * attach the when contraint to the right variable
   */
  private TomList attachConstraint(TomList subjectList,
      TomTerm constraint) {
    HashSet patternVariable = new HashSet();
    HashSet constraintVariable = new HashSet();

    collectVariable(patternVariable,subjectList);
    collectVariable(constraintVariable,constraint);
    Set variableSet = intersection(patternVariable,constraintVariable);

    //System.out.println("attach constraint "+subjectList+" "+patternVariable+" "+constraint);
    TomList newSubjectList = null;
    try{
    newSubjectList = (TomList)(MuTraveler.init(`attachConstraint(variableSet,constraint,this)).visit(subjectList)); 
    }catch(VisitFailure e){}
    return newSubjectList;
  }

  /*
   * build a set with all the variables in the intersection of two sets
   * used by the when
   */
  private static Set intersection(Set patternVariable, Set constraintVariable) {
    Set res = new HashSet();
    for(Iterator it1 = patternVariable.iterator(); it1.hasNext() ; ) {
      TomTerm patternTerm = (TomTerm) it1.next();
itBlock: {
           for(Iterator it2 = constraintVariable.iterator(); it2.hasNext() ; ) {
             TomTerm constraintTerm = (TomTerm) it2.next();
             %match(TomTerm patternTerm, TomTerm constraintTerm) {
               var@Variable[astName=name], Variable[astName=name] -> {
                 res.add(`var);
                 //break itBlock;
               }
               var@VariableStar[astName=name], VariableStar[astName=name] -> {
                 res.add(`var);
                 //break itBlock;
               }
             }
           }
         }
    }
    return res;
  }

  /*
   * find the variable on which we should attach the constraint
   * used by the when
   */

  %op VisitableVisitor attachConstraint(variableSet:Set,constraint:TomTerm,compiler:TomCompiler){
    make(variableSet,constraint,compiler){`mu(MuVar("x"),Choice(attachConstraint_once(variableSet,constraint,compiler),All(MuVar("x"))))}
  }

  %strategy attachConstraint_once(variableSet:Set,constraint:TomTerm,compiler:TomCompiler) extends `Fail(){

    visit TomTerm {
      var@(Variable|VariableStar)[constraints=constraintList] -> {
        if(variableSet.remove(`var) && variableSet.isEmpty()) {
          ConstraintList newConstraintList = (ConstraintList)`constraintList.append(`Ensure((TomTerm) MuTraveler.init(preProcessing(compiler)).visit(BuildReducedTerm(constraint))));
          return `var.setConstraints(newConstraintList);
        }
      }

      appl@RecordAppl[constraints=constraintList] -> {
        if(variableSet.isEmpty()) {
          ConstraintList newConstraintList = (ConstraintList)`constraintList.append(`Ensure((TomTerm) MuTraveler.init(preProcessing(compiler)).visit(BuildReducedTerm(constraint))));
          return `appl.setConstraints(newConstraintList);
        }
      }
    }
  } 

  /*
   * add a prefix (tom_) to back-quoted variables which comes from the lhs
   */
  %strategy findRenameVariable(context:Set) extends `Identity() {
    visit TomTerm {
      var@(Variable|VariableStar)[astName=astName@Name(name)] -> {
        if(context.contains(`astName)) {
          return `var.setAstName(`Name(ASTFactory.makeTomVariableName(name)));
        }
      }
    }

    visit Instruction {
      CompiledPattern(patternList,instruction) -> {
        Map map = TomBase.collectMultiplicity(`patternList);
        Set newContext = new HashSet(map.keySet());
        newContext.addAll(context);
        //System.out.println("newContext = " + newContext);
        return (Instruction)MuTraveler.init(`TopDown(findRenameVariable(newContext))).visit(`instruction);
      }
    }
  }

}
