/*
 * 
 * TOM - To One Matching Expander
 * 
 * Copyright (c) 2000-2009, INRIA
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
import tom.engine.adt.code.types.*;

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

  private static final TomType objectType = `TLType("Object");
  private static final TomType genericType = `TLType("T");
  private static final TomType methodparameterType = `TLType("<T> T");
  private static final TomType objectArrayType = `TLType("Object[]");
  private static final TomType intType = `TLType("int");
  
  private static final TomType basicStratType = `TLType("tom.library.sl.AbstractStrategyBasic");
  private static final TomType introspectorType = `TLType("tom.library.sl.Introspector");
  private static final TomType visitfailureType = `TLType("tom.library.sl.VisitFailure");
  // introspector argument of visitLight
  private static final BQTerm introspectorVar = `BQVariable(concOption(),Name("introspector"),introspectorType);
  private static final BQTerm objectVar = `BQVariable(concOption(),Name("o"),objectType);
  private static final BQTerm childVar = `BQVariable(concOption(),Name("child"),objectType);
  private static final BQTerm intVar = `BQVariable(concOption(),Name("i"),intType);
  private static final BQTerm objectArrayVar = `BQVariable(concOption(),Name("children"),objectArrayType);

  /** if the flag is true, a class that implements Introspector is generated */
  private boolean genIntrospector = false;
  private boolean generatedIntrospector = false;

  public boolean getGenIntrospector() {
    return genIntrospector;
  }

  public void setGenIntrospector(boolean genIntrospector) {
    this.genIntrospector = genIntrospector;
  }

  public boolean getGeneratedIntrospector() {
    return generatedIntrospector;
  }

  public void setGeneratedIntrospector(boolean generatedIntrospector) {
    this.generatedIntrospector = generatedIntrospector;
  }

  /** Constructor */
  public Expander() {
    super("Expander");
  }

  public void run(Map informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");    
    setGenIntrospector(getOptionBooleanValue("genIntrospector"));
    //System.out.println("(debug) I'm in the Tom expander : TSM"+getStreamManager().toString());
    try {
      //reinit the variable for intropsector generation
      setGeneratedIntrospector(false);
      Code expandedTerm = (Code) this.expand((Code)getWorkingTerm());
      // verbose
      getLogger().log(Level.INFO, TomMessage.tomExpandingPhase.getMessage(),
          Integer.valueOf((int)(System.currentTimeMillis()-startChrono)) );
      setWorkingTerm(expandedTerm);
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() + EXPANDED_SUFFIX, (Code)getWorkingTerm());
      }
    } catch(Exception e) {
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
    visit BQTerm {
      t@(BQVariable|BQVariableStar)[] -> {
        return `Expand_once(expander).visitLight(`BuildReducedTerm(TomBase.convertFromBQVarToVar(t),expander.getTermType(t)));
      }
    }
  }

  %strategy Expand_once(expander:Expander) extends Identity() {
    visit BQTerm {
      BuildReducedTerm[TomTerm=var@(Variable|VariableStar)[]] -> {
        return TomBase.convertFromVarToBQVar(`var);
      }

      BuildReducedTerm[TomTerm=RecordAppl[Option=optionList,NameList=(name@Name(tomName)),Slots=termArgs],AstType=astType] -> {
        TomSymbol tomSymbol = expander.symbolTable().getSymbolFromName(`tomName);
        SlotList newTermArgs = `TopDownIdStopOnSuccess(Expand_makeTerm_once(expander)).visitLight(`termArgs);
        BQTermList tomListArgs = TomBase.slotListToBQTermList(newTermArgs);
        
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

    /*
     * compilation of  %strategy
     */
    visit Declaration {
      Strategy(name,extendsTerm,visitList,orgTrack) -> {
        //Generate only one Introspector for a class if at least one  %strategy is found
        Declaration introspectorClass = `EmptyDeclaration();
        if(expander.getGenIntrospector() && !expander.getGeneratedIntrospector()) {
          expander.setGeneratedIntrospector(true);
          DeclarationList l = `concDeclaration();
          //generate the code for every method of Instrospector interface

          SymbolTable symbolTable = expander.symbolTable();
          Collection<TomType> types = symbolTable.getUsedTypes();

          /**
           * generate code for:
           * public int getChildCount(Object o);
           */
          String funcName = "getChildCount";//function name
            //manage null children: return 0
            InstructionList instructions = `concInstruction(If(BQTermToExpression(Composite(CompositeTL(ITL("o==null")))),Return(Composite(CompositeTL(ITL("0")))),Nop()));
          for (TomType type:types) {
            InstructionList instructionsForSort = `concInstruction();
            %match(type) {
              Type[TomType=typeName] -> {
                if(!(symbolTable.isBuiltinType(`typeName))) {
                  BQTerm var = `BQVariable(concOption(orgTrack),Name("v_"+typeName),type);
                  TomSymbolList list = symbolTable.getSymbolFromType(type);
                  %match(list) {
                    concTomSymbol(_*, symbol@Symbol[AstName=opName], _*) -> {
                      Instruction inst = `Nop();
                      if ( TomBase.isListOperator(`symbol) ) {
                        // manage empty lists and arrays
                        inst = `If(IsFsym(opName,var),If(IsEmptyList(opName,var),Return(Composite(CompositeTL(ITL("0")))),Return(Composite(CompositeTL(ITL("2"))))),Nop());
                      } else if ( TomBase.isArrayOperator(`symbol) ) {
                        inst = `If(IsFsym(opName,var),If(IsEmptyList(opName,var,ExpressionToBQTerm(Integer(0))),Return(Composite(CompositeTL(ITL("0")))),Return(Composite(CompositeTL(ITL("2"))))),Nop());
                      } else {
                        inst = `If(IsFsym(opName,var),Return(Composite(CompositeTL(ITL(""+TomBase.getArity(symbol))))),Nop());
                      } 
                      instructionsForSort = `concInstruction(instructionsForSort*,inst);
                    }
                  }
                  instructions = `concInstruction(instructions*,If(IsSort(type,objectVar),Let(var,Cast(type,BQTermToExpression(objectVar)),AbstractBlock(instructionsForSort)),Nop()));
                }
              }
            }
          }
          //default case (for builtins too): return 0
          instructions = `concInstruction(instructions*,Return(Composite(CompositeTL(ITL("0")))));
          l = `concDeclaration(l*,MethodDef(Name(funcName),concBQTerm(objectVar),intType,EmptyType(),AbstractBlock(instructions)));
          /**
           * generate code for:
           * public Object[] getChildren(Object o);
           */
          funcName = "getChildren";//function name
          instructions = `concInstruction();
          for (TomType type:types) {
            InstructionList instructionsForSort = `concInstruction();
            //cast in concTomSymbol to use the for statement
            %match(type) {
              Type[TomType=typeName] -> {
                if (! symbolTable.isBuiltinType(`typeName)) {
                  BQTerm var = `BQVariable(concOption(orgTrack),Name("v_"+typeName),type);
                  concTomSymbol list = (concTomSymbol) symbolTable.getSymbolFromType(type);
                  for (TomSymbol symbol:list) {
                    %match(symbol) {
                      Symbol[AstName=symbolName,TypesToType=TypesToType[Domain=domain]] -> {
                        if(TomBase.isListOperator(symbol)) {
                          Instruction return_array = `CodeToInstruction(BQTermToCode(Composite(CompositeTL(ITL("return new Object[]{")),CompositeBQTerm(ExpressionToBQTerm(GetHead(symbolName,domain.getHeadconcTomType(),var))),CompositeTL(ITL(",")),CompositeBQTerm(ExpressionToBQTerm(GetTail(symbolName,var))),CompositeTL(ITL("};")))));
                          //default case (used for builtins too)                     
                          Instruction return_emptyArray = `CodeToInstruction(TargetLanguageToCode(ITL("return new Object[]{};")));
                          Instruction inst = `If(IsFsym(symbolName,var),If(IsEmptyList(symbolName,var),return_emptyArray,return_array),Nop());
                          instructionsForSort = `concInstruction(instructionsForSort*,inst);
                        } else if (TomBase.isArrayOperator(symbol)) {
                          //TODO 
                        } else {
                          int arity = TomBase.getArity(symbol);
                          BQTerm composite = `Composite(CompositeTL(ITL("return new Object[]{")));
                          PairNameDeclList pairNameDeclList = symbol.getPairNameDeclList();
                          for(int i=0; i< arity; i++) {
                            PairNameDecl pairNameDecl = pairNameDeclList.getHeadconcPairNameDecl();
                            Declaration decl = pairNameDecl.getSlotDecl();
                            %match (decl) {
                              EmptyDeclaration() -> {
                                // case of undefined getSlot
                                // return null (to be improved)
                                composite =  `Composite(composite*,CompositeTL(ITL("null")));
                                if(i < arity-1) {
                                  composite =  `Composite(composite*,CompositeTL(ITL(",")));
                                } else {
                                }
                              }
                              GetSlotDecl[AstName=AstName,SlotName=SlotName] -> {
                                composite = `Composite(composite*,CompositeBQTerm(ExpressionToBQTerm(GetSlot(TomBase.getSlotType(symbol,SlotName),AstName,SlotName.getString(),var))));
                                if(i < arity-1) {
                                  composite = `Composite(composite*,CompositeTL(ITL(",")));
                                }
                              }
                            }
                            pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
                          }
                          composite = `Composite(composite*,CompositeTL(ITL("};")));
                          Instruction inst = `If(IsFsym(symbolName,var),CodeToInstruction(BQTermToCode(composite)),Nop());
                          instructionsForSort = `concInstruction(instructionsForSort*,inst);
                        }
                      } 
                    }
                  }
                  instructions = `concInstruction(instructions*,If(IsSort(type,objectVar),Let(var,Cast(type,BQTermToExpression(objectVar)),AbstractBlock(instructionsForSort)),Nop()));
                } 
              }
            }
          }
          //default case: return null
          instructions = `concInstruction(instructions*,Return(Composite(CompositeTL(ITL("null")))));
          l = `concDeclaration(l*,MethodDef(Name(funcName),concBQTerm(objectVar),objectArrayType,EmptyType(),AbstractBlock(instructions)));

          /**
           * generate code for:
           *  public Object setChildren(Object o, Object[] children);
           */
          funcName = "setChildren";//function name
          instructions = `concInstruction();
          for (TomType type:types) {
            InstructionList instructionsForSort = `concInstruction();
            //cast in concTomSymbol to use the for statement
            %match(type) {
              Type[TomType=typeName] -> {
                if(! symbolTable.isBuiltinType(`typeName)) {
                  BQTerm var = `BQVariable(concOption(orgTrack),Name("v_"+typeName),type);
                  concTomSymbol list = (concTomSymbol) symbolTable.getSymbolFromType(type);
                  for (TomSymbol symbol:list) {
                    %match(symbol) {
                      Symbol[AstName=symbolName,TypesToType=TypesToType] -> {
                        if (TomBase.isListOperator(symbol)) {
                          %match(TypesToType) {
                            TypesToType[Domain=concTomType(domain)] -> {
                              Instruction inst = 
                                `If(IsFsym(symbolName,var),
                                    If(BQTermToExpression(Composite(CompositeTL(ITL("children.length==0")))),
                                      Return(BuildEmptyList(symbolName)),
                                      Return(BuildConsList(symbolName,ExpressionToBQTerm(Cast(domain,BQTermToExpression(Composite(CompositeTL(ITL("children[0]")))))),ExpressionToBQTerm(Cast(type,BQTermToExpression(Composite(CompositeTL(ITL("children[1]"))))))))
                                      )
                                    ,Nop());
                              instructionsForSort = `concInstruction(instructionsForSort*,inst);
                            }
                          }
                        } else if (TomBase.isArrayOperator(symbol)) {
                          //TODO 
                        } else {
                          int arity = TomBase.getArity(symbol);
                          BQTermList slots = `concBQTerm();
                          PairNameDeclList pairNameDeclList = symbol.getPairNameDeclList();
                          for(int i=0; i< arity; i++) {
                            PairNameDecl pairNameDecl = pairNameDeclList.getHeadconcPairNameDecl();
                            Declaration decl = pairNameDecl.getSlotDecl();
                            TomType slotType = TomBase.getSlotType(symbol,TomBase.getSlotName(symbol,i));
                            String slotTypeName = slotType.getTomType();
                            // manage builtin slots
                            if(symbolTable.isBuiltinType(slotTypeName)) {
                              slots = `concBQTerm(slots*,Composite(CompositeTL(ITL("("+symbolTable.builtinToWrapper(slotTypeName)+")children["+i+"]"))));
                            } else {
                              slots = `concBQTerm(slots*,ExpressionToBQTerm(Cast(slotType,BQTermToExpression(Composite(CompositeTL(ITL("children["+i+"]")))))));
                            }
                            pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
                          }
                          Instruction inst = `If(IsFsym(symbolName,var),Return(BuildTerm(symbolName,slots,"default")),Nop());
                          instructionsForSort = `concInstruction(instructionsForSort*,inst);
                        }
                      }
                    }
                  } 

                  instructions = `concInstruction(instructions*,If(IsSort(type,objectVar),Let(var,Cast(type,BQTermToExpression(objectVar)),AbstractBlock(instructionsForSort)),Nop()));
                }
              }
            }
          }
          //default case: return o
          instructions = `concInstruction(instructions*,Return(objectVar));
          l = `concDeclaration(l*,MethodDef(Name(funcName),concBQTerm(objectVar,objectArrayVar),objectType,EmptyType(),AbstractBlock(instructions)));

          /**
           * generate code for:
           * public Object getChildAt(Object o, int i);
           */
          funcName = "getChildAt";//function name
          l = `concDeclaration(l*,MethodDef(Name(funcName),concBQTerm(objectVar,intVar),objectType,EmptyType(),Return(Composite(CompositeTL(ITL("getChildren(o)[i]"))))));

          /**
           * generate code for:
           * public Object setChildAt( Object o, int i, Object child);
           */
          funcName = "setChildAt";//function name
          String code = %[
            Object[] newChildren = getChildren(o);
            newChildren[i] = child;
            return setChildren(o, newChildren);
          ]%;
          l = `concDeclaration(l*,MethodDef(Name(funcName),concBQTerm(objectVar,intVar,childVar),objectType,EmptyType(),CodeToInstruction(TargetLanguageToCode(ITL(code)))));
          introspectorClass = `IntrospectorClass(Name("LocalIntrospector"),AbstractDecl(l));
        }

        /*
         * generate code for a %strategy
         */
        DeclarationList l = `concDeclaration(); // represents compiled Strategy
        HashMap<TomType,String> dispatchInfo = new HashMap<TomType,String>(); // contains info needed for dispatch
        for(TomVisit visit_ins:(concTomVisit)`visitList) {
          BQTermList subjectListAST = `concBQTerm();
          %match(visit_ins) {
            VisitTerm(vType@Type[TomType=type],constraintInstructionList,_) -> {              
              BQTerm arg = `BQVariable(concOption(orgTrack),Name("tom__arg"),vType);//arg subjectList
              subjectListAST = `concBQTerm(subjectListAST*,arg,introspectorVar);
              String funcName = "visit_" + `type; // function name
              Instruction matchStatement = `Match(constraintInstructionList, concOption(orgTrack));
              //return default strategy.visitLight(arg)
              // FIXME: put superclass keyword in backend, in c# 'super' is 'base'
              Instruction returnStatement = null;
              returnStatement = `Return(FunctionCall(Name("_" + funcName),vType,subjectListAST));
              InstructionList instructions = `concInstruction(matchStatement, returnStatement);
              l = `concDeclaration(l*,MethodDef(Name(funcName),concBQTerm(arg,introspectorVar),vType,visitfailureType,AbstractBlock(instructions)));
              dispatchInfo.put(`vType,funcName);
            }              
          }
        }

        /*
         * Generates the following dispatch mechanism:
         *           
         * public Visitable visitLight(Visitable v) throws VisitFailure {
         *   if(is_sort(v, Term1))
         *     return this.visit_Term1((Term1) v);
         *   ...
         *   if(is_sort(v, Termn))
         *     return this.visit_Termn((Termn) v);               
         *   return any.visitLight(v);
         * }
         *
         * public Term1 _visit_Term1(Term1 arg) throws VisitFailure {
         *   if(environment != null) {
         *     return (Term1) any.visit(environment);
         *   } else {
         *     return (Term1) any.visitLight(arg);
         *   }
         * }
         * ...
         * public Termn _visit_Termn(Termn arg) throws VisitFailure {
         *   if(environment != null) {
         *     return (Termn) any.visit(environment);
         *   } else {
         *     return (Termn) any.visitLight(arg);
         *   }
         * }
         *
         */        
        BQTerm vVar = `BQVariable(concOption(orgTrack),Name("v"),genericType);// v argument of visitLight
        InstructionList ifList = `concInstruction(); // the list of ifs in visitLight
        Expression testEnvNotNull = null;
        // generate the visitLight
        for(TomType type:dispatchInfo.keySet()) {
          BQTermList funcArg = `concBQTerm(ExpressionToBQTerm(Cast(type,BQTermToExpression(vVar))),introspectorVar);            
          Instruction returnStatement = `Return(ExpressionToBQTerm(Cast(genericType,BQTermToExpression(FunctionCall(Name(dispatchInfo.get(type)),type,funcArg)))));
          Instruction ifInstr = `If(IsSort(type,vVar),returnStatement,Nop());
          ifList = `concInstruction(ifList*,ifInstr);
          // generate the _visit_Term
          BQTerm arg = `BQVariable(concOption(orgTrack),Name("arg"),type);
          BQTerm environmentVar = `BQVariable(concOption(orgTrack),Name("environment"),EmptyType());
          Instruction return1 = `Return(ExpressionToBQTerm(Cast(type,TomInstructionToExpression(CodeToInstruction(TargetLanguageToCode(ITL("any.visit(environment,introspector)")))))));
          Instruction return2 = `Return(Composite(CompositeTL(ITL("any.visitLight(arg,introspector)"))));
          testEnvNotNull = `Negation(EqualTerm(expander.getStreamManager().getSymbolTable().getBooleanType(),
                ExpressionToBQTerm(Bottom(Type("Object",EmptyType()))),TomBase.convertFromBQVarToVar(environmentVar)));
          Instruction ifThenElse = `If(testEnvNotNull,return1,return2);
          l = `concDeclaration(l*,MethodDef(
                Name("_" + dispatchInfo.get(type)),
                concBQTerm(arg,introspectorVar),
                type,
                visitfailureType,
                ifThenElse));
        }
        ifList = `concInstruction(ifList*,              
            If(testEnvNotNull,
              Return(ExpressionToBQTerm(Cast(genericType,BQTermToExpression(Composite(CompositeTL(ITL("any.visit(environment,introspector)"))))))),
              Return(Composite(CompositeTL(ITL("any.visitLight(v,introspector)"))))));
        Declaration visitLightDeclaration = `MethodDef(
            Name("visitLight"),
            concBQTerm(vVar,introspectorVar),
            methodparameterType,
            visitfailureType,
            AbstractBlock(ifList));
        l = `concDeclaration(l*,visitLightDeclaration);
        return (Declaration) expander.expand(`AbstractDecl(concDeclaration(introspectorClass,Class(name,basicStratType,extendsTerm,AbstractDecl(l)))));
      }        
    }//end visit Declaration
  } // end strategy

}
