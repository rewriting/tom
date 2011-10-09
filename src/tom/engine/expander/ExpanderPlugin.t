/*
 * 
 * TOM - To One Matching Expander
 * 
 * Copyright (c) 2000-2011, INPL, INRIA
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
import java.util.logging.Logger;

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
public class ExpanderPlugin extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }
  %include { ../../library/mapping/java/util/types/Collection.tom}
  %include { ../../library/mapping/java/util/types/Map.tom}

  %typeterm ExpanderPlugin { implement { ExpanderPlugin } }

  private static Logger logger = Logger.getLogger("tom.engine.expander.ExpanderPlugin");
  /** some output suffixes */
  public static final String EXPANDED_SUFFIX = ".tfix.expanded";

  /** the declared options string */
  public static final String DECLARED_OPTIONS = 
    "<options>" +
    "<boolean name='expand' altName='' description='Expander (activated by default)' value='true'/>" +
    "<boolean name='genIntrospector' altName='gi' description='Generate a class that implements Introspector to apply strategies on non visitable terms' value='false'/>" +
    "</options>";

  private static final TomType objectType = ASTFactory.makeType(`concTypeOption(),"undefined","Object");
  private static final TomType genericType = ASTFactory.makeType(`concTypeOption(),"undefined","T");
  private static final TomType methodparameterType = ASTFactory.makeType(`concTypeOption(),"undefined","<T> T");
  private static final TomType objectArrayType = ASTFactory.makeType(`concTypeOption(),"undefined","Object[]");
  private static final TomType intType = ASTFactory.makeType(`concTypeOption(),"int","int");
  
  private static final TomType basicStratType = ASTFactory.makeType(`concTypeOption(),"undefined","tom.library.sl.AbstractStrategyBasic");
  private static final TomType introspectorType = ASTFactory.makeType(`concTypeOption(),"undefined","tom.library.sl.Introspector");
  private static final TomType visitfailureType = ASTFactory.makeType(`concTypeOption(),"undefined","tom.library.sl.VisitFailure");
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

  private void setGenIntrospector(boolean genIntrospector) {
    this.genIntrospector = genIntrospector;
  }

  public boolean getGeneratedIntrospector() {
    return generatedIntrospector;
  }

  private void setGeneratedIntrospector(boolean generatedIntrospector) {
    this.generatedIntrospector = generatedIntrospector;
  }

  /** Constructor */
  public ExpanderPlugin() {
    super("ExpanderPlugin");
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
      TomMessage.info(logger,null,0,TomMessage.tomExpandingPhase, Integer.valueOf((int)(System.currentTimeMillis()-startChrono)) );
      setTime(Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      setWorkingTerm(expandedTerm);
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() + EXPANDED_SUFFIX, (Code)getWorkingTerm());
      }
    } catch(Exception e) {
      TomMessage.error(logger,getStreamManager().getInputFileName(),0,TomMessage.exceptionMessage, e.getMessage());
      e.printStackTrace();
    }
  }

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(ExpanderPlugin.DECLARED_OPTIONS);
  }

  private tom.library.sl.Visitable expand(tom.library.sl.Visitable subject) {
    try {
      return `TopDownIdStopOnSuccess(Expand_once(this)).visitLight(subject);
    } catch(VisitFailure e) {
      throw new TomRuntimeException("ExpanderPlugin.expand: fail on " + subject);
    }
  }

  /*
   * Expand_once:
   * compiles %strategy
   * - generate instrospectors if -gi is activated
   * - generate visitLight and visit
   */

  %strategy Expand_once(expander:ExpanderPlugin) extends Identity() {
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

          SymbolTable symbolTable = expander.getSymbolTable();
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
                        inst = `If(IsFsym(opName,var),If(IsEmptyArray(opName,var,ExpressionToBQTerm(Integer(0))),Return(Composite(CompositeTL(ITL("0")))),Return(Composite(CompositeTL(ITL("2"))))),Nop());
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
          l = `concDeclaration(MethodDef(Name(funcName),concBQTerm(objectVar),intType,EmptyType(),AbstractBlock(instructions)),l*);
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
                      Symbol[AstName=symbolName,TypesToType=TypesToType[Domain=domain,Codomain=codomain]] -> {
                        if(TomBase.isListOperator(symbol)) {
                          Instruction return_array = `CodeToInstruction(BQTermToCode(Composite(
                                  CompositeTL(ITL("return new Object[]{")),
                                  CompositeBQTerm(ExpressionToBQTerm(GetHead(symbolName,domain.getHeadconcTomType(),var))),
                                  CompositeTL(ITL(",")),
                                  CompositeBQTerm(ExpressionToBQTerm(GetTail(symbolName,var))),
                                  CompositeTL(ITL("};"))
                                  )));
                          //default case (used for builtins too)                     
                          Instruction return_emptyArray = `CodeToInstruction(TargetLanguageToCode(ITL("return new Object[]{};")));
                          Instruction inst = `If(IsFsym(symbolName,var),If(IsEmptyList(symbolName,var),return_emptyArray,return_array),Nop());
                          instructionsForSort = `concInstruction(instructionsForSort*,inst);
                        } else if (TomBase.isArrayOperator(symbol)) {
                          //TODO
                          // we consider that the children of the array are the first element and the tail
                          //default case (used for builtins too)                     
                          BQTerm emptyArray = `Composite(CompositeTL(ITL("new Object[]{}")));
                          //`CodeToInstruction(TargetLanguageToCode(ITL("new Object[]{}")));
                          BQTerm tail = `BQVariable(concOption(),Name("tail"),codomain);

                          CodeList result = `concCode(BQTermToCode(Composite(
                                  CompositeTL(ITL("new Object[]{")),
                                  CompositeBQTerm(ExpressionToBQTerm(GetElement(symbolName,codomain,var,ExpressionToBQTerm(Integer(0))))),
                                  CompositeTL(ITL(",")),
                                  CompositeBQTerm(tail),
                                  CompositeTL(ITL("}"))
                                  )));

                          //Return(ExpressionToBQTerm(TomInstructionToExpression(emptyArray))),
                          Instruction inst = 
                            `If(IsFsym(symbolName,var),
                                If(IsEmptyArray(symbolName,var,ExpressionToBQTerm(Integer(0))),
                                  Return(emptyArray),
                                  LetRef(
                                    intVar,
                                    Integer(1),
                                    LetRef(
                                      tail,
                                      BQTermToExpression(
                                        BuildEmptyArray(
                                          symbolName,
                                          ExpressionToBQTerm(SubstractOne(ExpressionToBQTerm(GetSize(symbolName,var)))))
                                        ),
                                      AbstractBlock(concInstruction(
                                          WhileDo(
                                            LessThan(BQTermToExpression(intVar),GetSize(symbolName,var)),
                                            AbstractBlock(concInstruction(
                                                Assign(tail, BQTermToExpression(BuildConsArray(symbolName,ExpressionToBQTerm(GetElement(symbolName,codomain,var,intVar)),tail))),
                                                Assign(intVar, AddOne(intVar))
                                                ))
                                            ),
                                          Return(ExpressionToBQTerm(TomInstructionToExpression(CodeToInstruction(Tom(result)))))
                                          ))))
                                    ), 
                                    Nop());
                          instructionsForSort = `concInstruction(instructionsForSort*,inst);

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
          l = `concDeclaration(MethodDef(Name(funcName),concBQTerm(objectVar),objectArrayType,EmptyType(),AbstractBlock(instructions)),l*);

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
                          %match(TypesToType) {
                            TypesToType[Domain=concTomType(domain)] -> {
                              Instruction inst = 
                                `If(IsFsym(symbolName,var),
                                    If(BQTermToExpression(Composite(CompositeTL(ITL("children.length==0")))),
                                      Return(BuildEmptyArray(symbolName,ExpressionToBQTerm(Integer(0)))),
                                      Return(BuildConsArray(
                                          symbolName,
                                          ExpressionToBQTerm(Cast(domain,BQTermToExpression(Composite(CompositeTL(ITL("children[0]")))))),
                                          ExpressionToBQTerm(Cast(type,BQTermToExpression(Composite(CompositeTL(ITL("children[1]"))))))
                                          ))
                                      ),
                                    Nop());
                              instructionsForSort = `concInstruction(instructionsForSort*,inst);
                            }
                          }

                        } else {
                          int arity = TomBase.getArity(symbol);
                          BQTermList slots = `concBQTerm();
                          PairNameDeclList pairNameDeclList = symbol.getPairNameDeclList();
                          for(int i=0; i<arity; i++) {
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
          l = `concDeclaration(MethodDef(Name(funcName),concBQTerm(objectVar,objectArrayVar),objectType,EmptyType(),AbstractBlock(instructions)),l*);

          /**
           * generate code for:
           * public Object getChildAt(Object o, int i);
           */
          funcName = "getChildAt";//function name
          l = `concDeclaration(MethodDef(Name(funcName),concBQTerm(objectVar,intVar),objectType,EmptyType(),Return(Composite(CompositeTL(ITL("getChildren(o)[i]"))))),l*);

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
          l = `concDeclaration(MethodDef(Name(funcName),concBQTerm(objectVar,intVar,childVar),objectType,EmptyType(),CodeToInstruction(TargetLanguageToCode(ITL(code)))),l*);
          introspectorClass = `IntrospectorClass(Name("LocalIntrospector"),AbstractDecl(l));
        }

        /*
         * generate code for a %strategy
         */
        DeclarationList l = `concDeclaration(); // represents compiled Strategy
        HashMap<TomType,String> dispatchInfo = new HashMap<TomType,String>(); // contains info needed for dispatch
        %match(visitList) {
          concTomVisit(_*,VisitTerm(vType@Type[TomType=type],constraintInstructionList,_),_*) -> {              
            BQTerm arg = `BQVariable(concOption(orgTrack),Name("tom__arg"),vType);//arg subjectList
            String funcName = "visit_" + `type; // function name
            BQTermList subjectListAST = `concBQTerm(arg,introspectorVar);
            //return default strategy.visitLight(arg)
            // FIXME: put superclass keyword in backend, in c# 'super' is 'base'
            Instruction returnStatement = `Return(FunctionCall(Name("_" + funcName),vType,subjectListAST));
            Instruction matchStatement = `Match(constraintInstructionList, concOption(orgTrack));
            InstructionList instructions = `concInstruction(matchStatement, returnStatement);
            l = `concDeclaration(MethodDef(Name(funcName),concBQTerm(arg,introspectorVar),vType,visitfailureType,AbstractBlock(instructions)),l*);
            dispatchInfo.put(`vType,funcName);
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
                ExpressionToBQTerm(Bottom(Type(concTypeOption(),"Object",EmptyTargetLanguageType()))),TomBase.convertFromBQVarToVar(environmentVar)));
          Instruction ifThenElse = `If(testEnvNotNull,return1,return2);
          l = `concDeclaration(MethodDef(
                Name("_" + dispatchInfo.get(type)),
                concBQTerm(arg,introspectorVar),
                type,
                visitfailureType,
                ifThenElse),l*);
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
        l = `concDeclaration(visitLightDeclaration,l*);
        return (Declaration) expander.expand(`AbstractDecl(concDeclaration(introspectorClass,Class(name,basicStratType,extendsTerm,AbstractDecl(l)))));
      }        
    }//end visit Declaration
  } // end strategy

}
