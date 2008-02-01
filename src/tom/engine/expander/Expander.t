/*
 * 
 * TOM - To One Matching Expander
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

package tom.engine.expander;

import java.util.*;
import java.util.logging.Level;

import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomsignature.types.tomsymbollist.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.adt.tominstruction.types.constraintinstructionlist.concConstraintInstruction;
import tom.engine.adt.tomslot.types.slotlist.concSlot;
import tom.engine.adt.tomsignature.types.tomvisitlist.concTomVisit;
import tom.engine.adt.tomdeclaration.types.declaration.IntrospectorClass;
import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import tom.library.sl.*;

/**
 * The Expander plugin.
 */
public class Expander extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }
  %include { ../../library/mapping/java/util/types/Collection.tom}
  %include { ../../library/mapping/java/util/types/Map.tom}

  %typeterm Expander { implement { Expander } }

  /** some output suffixes */
  public static final String EXPANDED_SUFFIX = ".tfix.expanded";

  /** the declared options string */
  public static final String DECLARED_OPTIONS = 
    "<options>" +
    "<boolean name='expand' altName='' description='Expander (activated by default)' value='true'/>" +
    "<boolean name='genIntrospector' altName='gi' description=' Generate a class that implements Introspector to apply strategies on non visitable terms' value='false'/>" +
    "</options>";

  private static final String basicStratName = "tom.library.sl.BasicStrategy";
  private static final TomType objectType = `TomTypeAlone("Object");
  private static final TomType objectArrayType = `TomTypeAlone("Object[]");
  private static final TomType intType = `TomTypeAlone("int");
  private static final TomType introspectorType = `TomTypeAlone("tom.library.sl.Introspector");
  // introspector argument of visitLight
  private static final TomTerm introspectorVar = `Variable(concOption(),Name("introspector"),introspectorType,concConstraint());
  private static final TomTerm objectVar = `Variable(concOption(),Name("o"),objectType,concConstraint());
  private static final TomTerm childVar = `Variable(concOption(),Name("child"),objectType,concConstraint());
  private static final TomTerm intVar = `Variable(concOption(),Name("i"),intType,concConstraint());
  private static final TomTerm objectArrayVar = `Variable(concOption(),Name("children"),objectArrayType,concConstraint());

  /** if the flag is true, a class that implements Introspector is generated */
  private static boolean genIntrospector = false;
  private static boolean generatedIntrospector = false;

  /** Constructor */
  public Expander() {
    super("Expander");
  }

  public void run() {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");    
    genIntrospector = getOptionBooleanValue("genIntrospector");
    try {
      //reinit the variable for intropsector generation
      generatedIntrospector = false;
      TomTerm expandedTerm = (TomTerm) this.expand((TomTerm)getWorkingTerm());
      // verbose
      getLogger().log(Level.INFO, TomMessage.tomExpandingPhase.getMessage(),
          new Integer((int)(System.currentTimeMillis()-startChrono)) );
      setWorkingTerm(expandedTerm);
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() + EXPANDED_SUFFIX, (TomTerm)getWorkingTerm());
      }
    } catch (Exception e) {
      getLogger().log(Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
          new Object[]{getStreamManager().getInputFileName(), "Expander", e.getMessage()} );
      e.printStackTrace();
    }
  }

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(Expander.DECLARED_OPTIONS);
  }

  /*
   * Expand:
   * replaces BuildReducedTerm by BuildList, BuildArray or BuildTerm
   *
   * abstract list-matching patterns
   */

  private tom.library.sl.Visitable expand(tom.library.sl.Visitable subject) {
    try {
      return `TopDownIdStopOnSuccess(Expand_once(this)).visitLight(subject);
    } catch(VisitFailure e) {
      throw new TomRuntimeException("Expander.expand: fail on " + subject);
    }
  }

  %strategy Expand_makeTerm_once(expander:Expander) extends Identity() {
    visit TomTerm {
      t@(Variable|VariableStar|RecordAppl)[] -> {
        return (TomTerm) `Expand_once(expander).visitLight(`BuildReducedTerm(t,expander.getTermType(t)));
      }
    }
  }

  %strategy Expand_once(expander:Expander) extends Identity() {
    visit TomTerm {
      BuildReducedTerm[TomTerm=var@(Variable|VariableStar)[]] -> {
        return `var;
      }

      BuildReducedTerm[TomTerm=RecordAppl[Option=optionList,NameList=(name@Name(tomName)),Slots=termArgs],AstType=astType] -> {
        TomSymbol tomSymbol = expander.symbolTable().getSymbolFromName(`tomName);
        SlotList newTermArgs = (SlotList) `TopDownIdStopOnSuccess(Expand_makeTerm_once(expander)).visitLight(`termArgs);
        TomList tomListArgs = TomBase.slotListToTomList(newTermArgs);
        
        if(TomBase.hasConstant(`optionList)) {
          return `BuildConstant(name);
        } else if(tomSymbol != null) {
          if(TomBase.isListOperator(tomSymbol)) {
            return ASTFactory.buildList(`name,tomListArgs,expander.symbolTable());
          } else if(TomBase.isArrayOperator(tomSymbol)) {
            return ASTFactory.buildArray(`name,tomListArgs,expander.symbolTable());
          } else if(TomBase.isDefinedSymbol(tomSymbol)) {
            return `FunctionCall(name,TomBase.getSymbolCodomain(tomSymbol),tomListArgs);
          } else {
            String moduleName = TomBase.getModuleName(`optionList);
            if(moduleName==null) {
              moduleName = TomBase.DEFAULT_MODULE_NAME;
            }
            return `BuildTerm(name,tomListArgs,moduleName);
          }
        } else {
          return `FunctionCall(name,astType,tomListArgs);
        }

      }

    } // end match

    visit Instruction {
      Match(constraintInstructionList, matchOptionList)  -> {
        Option orgTrack = TomBase.findOriginTracking(`matchOptionList);
        ConstraintInstructionList newConstraintInstructionList = `concConstraintInstruction();
        ConstraintList negativeConstraint = `concConstraint();        
        for(ConstraintInstruction constraintInstruction:(concConstraintInstruction)`constraintInstructionList) {
          /*
           * the call to Expand performs the recursive expansion
           * of nested match constructs
           */
          ConstraintInstruction newConstraintInstruction = (ConstraintInstruction) expander.expand(constraintInstruction);

matchBlock: {
              %match(newConstraintInstruction) {
                ConstraintInstruction(constraint,actionInst, option) -> {
                  Instruction newAction = `actionInst;
                  /* expansion of RawAction into TypedAction */
                  %match(actionInst) {
                    RawAction(x) -> {
                      newAction=`TypedAction(If(TrueTL(),x,Nop()),constraint,negativeConstraint);
                    }
                  }
                  negativeConstraint = `concConstraint(negativeConstraint*,constraint);

                  /* generate equality checks */
                  newConstraintInstruction = `ConstraintInstruction(constraint,newAction, option);
                  /* do nothing */
                  break matchBlock;
                }

                _ -> {
                  System.out.println("Expander.Expand: strange ConstraintInstruction: " + `newConstraintInstruction);
                  throw new TomRuntimeException("Expander.Expand: strange ConstraintInstruction: " + `newConstraintInstruction);
                }
              }
            } // end matchBlock

            newConstraintInstructionList = `concConstraintInstruction(newConstraintInstructionList*,newConstraintInstruction);
        }

        Instruction newMatch = `Match(newConstraintInstructionList, matchOptionList);
        return newMatch;
      }

    } // end visit

    visit Declaration {
      Strategy(name,extendsTerm,visitList,orgTrack) -> {
        //Generate only one Introspector for a class if at least one  %strategy is found
        Declaration introspectorClass = `EmptyDeclaration();
        if(expander.genIntrospector && !generatedIntrospector) {
          generatedIntrospector=true;
          DeclarationList l = `concDeclaration();
          //generate the code for every method of Instrospector interface

          SymbolTable symbolTable = expander.symbolTable();
          Collection<TomTypeDefinition> types = symbolTable.getUsedTypes();

          /**
           * public int getChildCount(Object o);
           */
          String funcName = "getChildCount";//function name
            //manage null children: return 0
            InstructionList instructions = `concInstruction(If(TomTermToExpression(TargetLanguageToTomTerm(ITL("o==null"))),Return(TargetLanguageToTomTerm(ITL("0"))),Nop()));
          for (TomTypeDefinition type:types) {
            InstructionList instructionsForSort = `concInstruction();
            //cast in concTomSymbol to use the for statement
            TomType tomtype = type.getTomType();
            %match(tomtype) {
              Type[TomType=ASTTomType(typeName)] -> {
                if (! (symbolTable.isBuiltinType(`typeName))) {
                  TomTerm var = `Variable(concOption(orgTrack),Name("v_"+typeName),tomtype,concConstraint());
                  concTomSymbol list = (concTomSymbol) symbolTable.getSymbolFromType(tomtype);
                  for (TomSymbol symbol:list) {
                    %match(symbol) {
                      Symbol[AstName=name] -> {
                        //TODO: manage empty lists and arrays
                        Instruction inst = `If(IsFsym(name,var),Return(TargetLanguageToTomTerm(ITL(""+TomBase.getArity(symbol)))),Nop());
                        instructionsForSort = `concInstruction(instructionsForSort*,inst);
                      }
                    }
                  }
                  instructions = `concInstruction(instructions*,If(IsSort(tomtype,objectVar),Let(var,Cast(tomtype,TomTermToExpression(objectVar)),AbstractBlock(instructionsForSort)),Nop()));
                }
              }
            }
          }
          //default case (for builtins too): return 0
          instructions = `concInstruction(instructions*,Return(TargetLanguageToTomTerm(ITL("0"))));
          l = `concDeclaration(l*,MethodDef(Name(funcName),concTomTerm(objectVar),intType,EmptyType(),AbstractBlock(instructions)));
          /**
            public Object[] getChildren(Object o);
           */
          funcName = "getChildren";//function name
          instructions = `concInstruction();
          for (TomTypeDefinition type:types) {
            InstructionList instructionsForSort = `concInstruction();
            //cast in concTomSymbol to use the for statement
            TomType tomtype = type.getTomType();
            %match(tomtype) {
              Type[TomType=ASTTomType(typeName)] -> {
                if (! symbolTable.isBuiltinType(`typeName)) {
                  TomTerm var = `Variable(concOption(orgTrack),Name("v_"+typeName),tomtype,concConstraint());
                  concTomSymbol list = (concTomSymbol) symbolTable.getSymbolFromType(tomtype);
                  for (TomSymbol symbol:list) {
                    %match(symbol) {
                      Symbol[AstName=symbolName,TypesToType=TypesToType] -> {
                        if (TomBase.isListOperator(symbol)) {
                          %match(TypesToType) {
                            TypesToType[Domain=concTomType(domain)] -> {
                              TomList array = `concTomTerm(TargetLanguageToTomTerm(ITL("new Object[]{")),ExpressionToTomTerm(GetHead(symbolName,domain,var)),TargetLanguageToTomTerm(ITL(",")),ExpressionToTomTerm(GetTail(symbolName,var)),TargetLanguageToTomTerm(ITL("}")));
                              //default case (used for builtins too)                     
                              TomTerm emptyArray = `TargetLanguageToTomTerm(ITL("new Object[]{}"));
                              Instruction inst = `If(IsFsym(symbolName,var),If(IsEmptyList(symbolName,var),Return(emptyArray),Return(Tom(array))),Nop());
                              instructionsForSort = `concInstruction(instructionsForSort*,inst);
                            }
                          } 
                        } else {
                          if (TomBase.isArrayOperator(symbol)) {
                            //TODO 
                          } else {
                            int arity = TomBase.getArity(symbol);
                            TomList slotArray = `concTomTerm(TargetLanguageToTomTerm(ITL(" new Object[]{")));
                            PairNameDeclList pairNameDeclList = symbol.getPairNameDeclList();
                            for(int i=0; i< arity; i++) {
                              PairNameDecl pairNameDecl = pairNameDeclList.getHeadconcPairNameDecl();
                              Declaration decl = pairNameDecl.getSlotDecl();
                              %match (decl) {
                                EmptyDeclaration() -> {
                                  // case of undefined getSlot
                                  // return null (to be improved)
                                  if (i< arity-1) {
                                    slotArray =  `concTomTerm(slotArray*,TargetLanguageToTomTerm(ITL("null,")));
                                  } else {
                                    slotArray =  `concTomTerm(slotArray*,TargetLanguageToTomTerm(ITL("null")));
                                  }
                                }
                                GetSlotDecl[AstName=AstName,SlotName=SlotName] -> {
                                  slotArray =  `concTomTerm(slotArray*,ExpressionToTomTerm(GetSlot(TomBase.getSlotType(symbol,SlotName),AstName,SlotName.getString(),var)));
                                  if (i < arity-1) {
                                    slotArray =  `concTomTerm(slotArray*,TargetLanguageToTomTerm(ITL(",")));
                                  }
                                }
                              }
                              pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
                            }
                            slotArray = `concTomTerm(slotArray*,TargetLanguageToTomTerm(ITL("}")));
                            Instruction inst = `If(IsFsym(symbolName,var),Return(Tom(slotArray)),Nop());
                            instructionsForSort = `concInstruction(instructionsForSort*,inst);
                          }
                        }
                      } 
                    }
                  }
                  instructions = `concInstruction(instructions*,If(IsSort(tomtype,objectVar),Let(var,Cast(tomtype,TomTermToExpression(objectVar)),AbstractBlock(instructionsForSort)),Nop()));
                } 
              }
            }
          }
          //default case: return null
          instructions = `concInstruction(instructions*,Return(TargetLanguageToTomTerm(ITL("null"))));
          l = `concDeclaration(l*,MethodDef(Name(funcName),concTomTerm(objectVar),objectArrayType,EmptyType(),AbstractBlock(instructions)));

          /**
            public Object setChildren(Object o, Object[] children);
           */
          funcName = "setChildren";//function name
          instructions = `concInstruction();
          for (TomTypeDefinition type:types) {
            InstructionList instructionsForSort = `concInstruction();
            //cast in concTomSymbol to use the for statement
            TomType tomtype = type.getTomType();
            %match(tomtype) {
              Type[TomType=ASTTomType(typeName)] -> {
                if (! symbolTable.isBuiltinType(`typeName)) {
                  TomTerm var = `Variable(concOption(orgTrack),Name("v_"+typeName),tomtype,concConstraint());
                  concTomSymbol list = (concTomSymbol) symbolTable.getSymbolFromType(tomtype);
                  for (TomSymbol symbol:list) {
                    %match(symbol) {
                      Symbol[AstName=symbolName,TypesToType=TypesToType] -> {
                        if (TomBase.isListOperator(symbol)) {
                          %match(TypesToType) {
                            TypesToType[Domain=concTomType(domain)] -> {
                              Instruction inst = 
                                `If(IsFsym(symbolName,var),
                                    If(TomTermToExpression(TargetLanguageToTomTerm(ITL("children.length==0"))),
                                      Return(BuildEmptyList(symbolName)),
                                      Return(BuildConsList(symbolName,ExpressionToTomTerm(Cast(domain,TomTermToExpression(TargetLanguageToTomTerm(ITL("children[0]"))))),ExpressionToTomTerm(Cast(tomtype,TomTermToExpression(TargetLanguageToTomTerm(ITL("children[1]")))))))
                                      )
                                    ,Nop());
                              instructionsForSort = `concInstruction(instructionsForSort*,inst);
                            }
                          }
                        } else {
                          if (TomBase.isArrayOperator(symbol)) {
                            //TODO 
                          } else {
                            int arity = TomBase.getArity(symbol);
                            TomList slots = `concTomTerm();
                            PairNameDeclList pairNameDeclList = symbol.getPairNameDeclList();
                            for(int i=0; i< arity; i++) {
                              PairNameDecl pairNameDecl = pairNameDeclList.getHeadconcPairNameDecl();
                              Declaration decl = pairNameDecl.getSlotDecl();
                              TomType slotType = TomBase.getSlotType(symbol,TomBase.getSlotName(symbol,i));
                              String slotTypeName = slotType.getTomType().getString();
                              // manage builtin slots
                              if (symbolTable.isBuiltinType(slotTypeName)) {
                                slots =  `concTomTerm(slots*,TargetLanguageToTomTerm(ITL("("+symbolTable.builtinToWrapper(slotTypeName)+")children["+i+"]")));
                              } else {
                                slots =  `concTomTerm(slots*,ExpressionToTomTerm(Cast(slotType,TomTermToExpression(TargetLanguageToTomTerm(ITL("children["+i+"]"))))));
                              }
                              pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
                            }
                            Instruction inst = `If(IsFsym(symbolName,var),Return(BuildTerm(symbolName,slots,"default")),Nop());
                            instructionsForSort = `concInstruction(instructionsForSort*,inst);
                          }
                        }
                      }
                    }
                  } 

                  instructions = `concInstruction(instructions*,If(IsSort(tomtype,objectVar),Let(var,Cast(tomtype,TomTermToExpression(objectVar)),AbstractBlock(instructionsForSort)),Nop()));
                }
              }
            }
          }
          //default case: return o
          instructions = `concInstruction(instructions*,Return(objectVar));
          l = `concDeclaration(l*,MethodDef(Name(funcName),concTomTerm(objectVar,objectArrayVar),objectType,EmptyType(),AbstractBlock(instructions)));

          /**
            public Object getChildAt(Object o, int i);

           */
          funcName = "getChildAt";//function name
          l = `concDeclaration(l*,MethodDef(Name(funcName),concTomTerm(objectVar,intVar),objectType,EmptyType(),Return(TargetLanguageToTomTerm(ITL("getChildren(o)[i]")))));

          /**
            public Object setChildAt( Object o, int i, Object child);
           */
          funcName = "setChildAt";//function name
          String code = %[
            Object[] newChildren = getChildren(o);
          newChildren[i] = child;
          return newChildren;
          ]%;
          l = `concDeclaration(l*,MethodDef(Name(funcName),concTomTerm(objectVar,intVar,childVar),objectType,EmptyType(),TargetLanguageToInstruction(ITL(code))));



          introspectorClass = `IntrospectorClass(Name("LocalIntrospector"),AbstractDecl(l));
        }

        DeclarationList l = `concDeclaration();//represents compiled Strategy
        TomForwardType visitorFwd = null;             
        HashMap<TomType,String> dispatchInfo = new HashMap<TomType,String>(); // contains info needed for dispatch
        for(TomVisit visit:(concTomVisit)`visitList) {
          TomList subjectListAST = `concTomTerm();
          %match(visit) {
            VisitTerm(vType@Type[TomType=ASTTomType(type)],constraintInstructionList,_) -> {              
              if(visitorFwd == null) {//first time in loop
                visitorFwd = expander.symbolTable().getForwardType(`type);//do the job only once
              }
              TomTerm arg = `Variable(concOption(orgTrack),Name("tom__arg"),vType,concConstraint());//arg subjectList
              subjectListAST = `concTomTerm(subjectListAST*,arg,introspectorVar);
              String funcName = "visit_" + `type;//function name
              Instruction matchStatement = `Match(constraintInstructionList, concOption(orgTrack));
              //return default strategy.visitLight(arg)
              // FIXME: put superclass keyword in backend, in c# 'super' is 'base'
              Instruction returnStatement = null;
              returnStatement = `Return(FunctionCall(Name("_" + funcName),vType,subjectListAST));
              InstructionList instructions = `concInstruction(matchStatement, returnStatement);
              l = `concDeclaration(l*,MethodDef(Name(funcName),concTomTerm(arg,introspectorVar),vType,TomTypeAlone("tom.library.sl.VisitFailure"),AbstractBlock(instructions)));
              dispatchInfo.put(`vType,funcName);
            }              
          }
        }
        /*
         * // Generates the following dispatch mechanism
         *           
         * public Visitable visitLight(Visitable v) throws VisitFailure {
         *       if (is_sort(v, Term1))
         *               return this.visit_Term1((Term1) v);
         *       .....................        
         *       if (is_sort(v, Termn))
         *               return this.visit_Termn((Termn) v);               
         *       return any.visitLight(v);
         * }
         *
         * public Term1 _visit_Term1(Term1 arg) throws VisitFailure {
         *        if (environment != null) {
         *                return (Term1) any.visit(environment);
         *        } else {
         *                return (Term1) any.visitLight(arg);
         *        }
         * }
         * ..............
         * public Termn _visit_Termn(Termn arg) throws VisitFailure {
         *        if (environment != null) {
         *                return (Termn) any.visit(environment);
         *        } else {
         *                return (Termn) any.visitLight(arg);
         *        }
         * }
         *
         */        
        visitorFwd = `TLForward(Expander.basicStratName);         
        TomTerm vVar = `Variable(concOption(orgTrack),Name("v"),objectType,concConstraint());// v argument of visitLight
        InstructionList ifList = `concInstruction(); // the list of ifs in visitLight
        Expression testEnvNotNull = null;
        // generate the visitLight
        for(TomType type:dispatchInfo.keySet()){
          TomList funcArg = `concTomTerm(ExpressionToTomTerm(Cast(type,TomTermToExpression(vVar))),introspectorVar);            
          Instruction returnStatement = `Return(FunctionCall(Name(dispatchInfo.get(type)),type,funcArg));
          Instruction ifInstr = `If(IsSort(type,vVar),returnStatement,Nop());
          ifList = `concInstruction(ifList*,ifInstr);
          // generate the _visit_Term
          TomTerm arg = `Variable(concOption(orgTrack),Name("arg"),type,concConstraint());
          TomTerm environmentVar = `Variable(concOption(orgTrack),Name("environment"),EmptyType(),concConstraint());
          Instruction return1 = `Return(ExpressionToTomTerm(Cast(type,TomInstructionToExpression(TargetLanguageToInstruction(ITL("any.visit(environment,introspector)"))))));
          Instruction return2 = `Return(ExpressionToTomTerm(Cast(type,TomInstructionToExpression(TargetLanguageToInstruction(ITL("any.visitLight(arg,introspector)"))))));
          testEnvNotNull = `Negation(EqualTerm(expander.getStreamManager().getSymbolTable().getBooleanType(),
                environmentVar,ExpressionToTomTerm(Bottom(TomTypeAlone("Object")))));
          Instruction ifThenElse = `If(testEnvNotNull,return1,return2);
          l = `concDeclaration(l*,MethodDef(
                Name("_" + dispatchInfo.get(type)),
                concTomTerm(arg,introspectorVar),
                type,
                TomTypeAlone("tom.library.sl.VisitFailure"),
                ifThenElse));
        }
        ifList = `concInstruction(ifList*,              
            If(testEnvNotNull,
              Return(InstructionToTomTerm(TargetLanguageToInstruction(ITL("any.visit(environment,introspector)")))),
              Return(InstructionToTomTerm(TargetLanguageToInstruction(ITL("any.visitLight(v,introspector)"))))));
        Declaration visitLightDeclaration = `MethodDef(
            Name("visitLight"),
            concTomTerm(vVar,introspectorVar),
            objectType,
            TomTypeAlone("tom.library.sl.VisitFailure"),
            AbstractBlock(ifList));
        l = `concDeclaration(l*,visitLightDeclaration);
        return (Declaration) expander.expand(`AbstractDecl(concDeclaration(introspectorClass,Class(name,visitorFwd,extendsTerm,AbstractDecl(l)))));
      }        
    }//end visit Declaration
  } // end strategy

}
