/*
 * 
 * TOM - To One Matching Compiler
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
 * Jean-Christophe Bach  e-mail: jeanchristophe.bach@inria.fr
 *
 **/

package tom.engine.transformer;

import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tominstruction.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomdeclaration.types.declarationlist.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.code.types.bqtermlist.*;
import tom.engine.adt.tomtype.types.tomtypelist.concTomType;
import tom.engine.adt.tomterm.types.tomlist.concTomTerm;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.engine.tools.SymbolTable;
import tom.engine.xml.Constants;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;

import tom.library.sl.*;

/**
 * The Transmorfer plugin.
 * Performs tree transformation and code expansion.
 *
 * TO BE DETAILED /!\
 * 1st step: TransformationDecl - 
 * 
 * 
 * 
 */
public class TransformerPlugin extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }
  %include { ../../library/mapping/java/util/types/List.tom }

  //%typeterm TransformerPlugin { implement { tom.engine.transformer.TransformerPlugin } }
  %typeterm TransformerPlugin { implement { TransformerPlugin } }

  private static Logger logger = Logger.getLogger("tom.engine.transformer.TransformerPlugin");
  /** some output suffixes */
  public static final String TRANSFORMED_SUFFIX = ".tfix.transformed";
  public static final String REFCLASS_PREFIX = "tom__reference_class_";
  private SymbolTable symbolTable;
  private int freshCounter = 0;

  public void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  public TransformerPlugin() {
    super("TransformerPlugin");
  }

  public void run(Map informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    Code transformedTerm = (Code)getWorkingTerm();
    setSymbolTable(getStreamManager().getSymbolTable());
    //System.out.println("###DEBUG1### SymbolTable =\n"+getSymbolTable().getMapSymbolName()); 
    try {
      // replace content of TransformationDecl by StrategyDecl
      transformedTerm = `TopDown(ProcessTransformation(this)).visitLight(transformedTerm);

      setWorkingTerm(transformedTerm);
      //System.out.println("###DEBUG2### SymbolTable =\n"+getSymbolTable().getMapSymbolName()); 
      // verbose
      TomMessage.info(logger,null,0,TomMessage.tomTransformingPhase,
          Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() +
            TRANSFORMED_SUFFIX, transformedTerm);
      }
    }  catch(VisitFailure e) {
      throw new TomRuntimeException("TransformerPlugin.process: fail on " + transformedTerm);
    } catch (Exception e) {
      TomMessage.error(logger, getStreamManager().getInputFileName(), 0,
          TomMessage.exceptionMessage, e.getMessage());
      e.printStackTrace();
    }
  }


  %strategy ProcessTransformation(transformer:TransformerPlugin) extends Identity() {
    /* 
     * Compilation of %transformation
     */
    visit Declaration {
      Transformation(transfoName,domain,declList,elemTransfoList,fileFrom,fileTo,orgTrack) ->
       {
         return `AbstractDecl(processSubDecl(transformer,transfoName,domain,declList,elemTransfoList,fileFrom,fileTo,orgTrack));
      }
    }
  }


  private static DeclarationList processSubDecl(TransformerPlugin transformer, TomName transfoName, TomTypeList domain, DeclarationList declList, ElementaryTransformationList elemTransfoList, String fileFrom, String fileTo, Option orgTrack) {
    List bqlist = new LinkedList<BQTerm>();
    List<Declaration> result = new LinkedList<Declaration>();
    String stringName = transfoName.getString();
    TomSymbol symbol = transformer.getSymbolTable().getSymbolFromName(stringName);
    //elementary strategies generation
    %match(elemTransfoList) {
      concElementaryTransformation(_*,ElementaryTransformation[ETName=etName,Traversal=traversal,AstRuleInstructionList=riList,Options=concOption(_*,ot@OriginTracking(_,_,_),_*)],_*) -> {
        //generate elementary `Strategy, `ReferenceClass and `SymbolDecl
        result.addAll(genElementaryStrategy(transformer,`etName,`traversal,`riList,`ot));
        //Generate symbol for elementary strategy and put it into symbol table
        genAndPutElementaryStratSymbol(transformer, `ot, `etName, domain, symbol);
        //add the part of CompositeBQTerm transformer to bqlist
        bqlist.add(`traversal);
      }
    }
    //generate resolve strategy
    Declaration resolveStratDecl = null;
    Declaration inverseLinks = null;
    /*
    %match(declList) {
      concDeclaration(_*,ResolveStratDecl[TransfoName=name,ResList=reslist,ResolveNameList=resolveNameList,OriginTracking=rot],_*) -> {
        resolveStratDecl = buildResolveStrat(transformer, transfoName, domain, bqlist, symbol, `name, `reslist, `resolveNameList, `rot);
        inverseLinks = `ResolveInverseLinksDecl(resolveNameList,fileFrom,fileTo);
      }
    }
    */

    %match(declList) {
      concDeclaration(_*,decl,_*) -> {
        %match(decl) {
          ResolveStratDecl[TransfoName=name,ResList=reslist,ResolveNameList=resolveNameList,OriginTracking=rot] -> {
            resolveStratDecl = buildResolveStrat(transformer, transfoName, domain, bqlist, symbol, `name, `reslist, `resolveNameList, `rot);
            inverseLinks = `ResolveInverseLinksDecl(resolveNameList,fileFrom,fileTo);
          }
          d@(SymbolDecl|ResolveClassDecl|TypeTermDecl)[] -> {
            result.add(`d);
          }
        }
      
      }
    }


    //add them to DeclarationList
    result.add(resolveStratDecl);
    result.add(inverseLinks);
    //let's change the transformation MakeDecl
    BQTerm composite = makeComposedStrategy(bqlist,declList,domain);
    try {
      TomSymbol newSymbol = `TopDown(ReplaceMakeDecl(composite)).visitLight(symbol);
      transformer.getSymbolTable().putSymbol(stringName,newSymbol);
    } catch (VisitFailure e) {
      throw new TomRuntimeException("TransformerPlugin.processSubDecl: fail on " + symbol);
    }
    return ASTFactory.makeDeclarationList(result);
  }//processSubDecl


  /**
   * Generate the `Strategy corresponding to an `ElementaryTransformation
   * @return DeclarationList containeng the `Strategy, the `SymbolDecl and a
   * `ReferenceClass
   */
  private static List<Declaration> genElementaryStrategy(TransformerPlugin transformer,
                                                         TomName strategyName,
                                                         BQTerm traversal,
                                                         RuleInstructionList riList,
                                                         Option orgTrack) {
    String strName = strategyName.getString();
    TomName refClassName = `Name(transformer.REFCLASS_PREFIX+strName);
    Declaration symbol = `SymbolDecl(strategyName);

    //build visitList
    List<ConstraintInstruction> ciList = new LinkedList<ConstraintInstruction>();
    List<RefClassTracelinkInstruction> refclassTInstructionList = new LinkedList<RefClassTracelinkInstruction>();

    TomType vType = null;
    //String vTypeStr = null;
    %match(riList) {
      concRuleInstruction(_*,RuleInstruction[TypeName=type,Term=term,Action=instr,Options=opts],_*) -> {
        List<TomName> nameList = new LinkedList<TomName>();
        //collect Tracelink instructions in order to build the ReferenceClass
        %match(InstructionList instr) {
          concInstruction(_*,Tracelink[Type=t,Name=n],_*) -> {
            refclassTInstructionList.add(`RefClassTracelinkInstruction(t,n));
            nameList.add(`n);
          }
        }
        //links to be set
        TomNameList tracedLinks = ASTFactory.makeNameList(nameList);
        BQTerm current = genTracelinkPopulateResolveCurrent(`term);
        //TODO: change that
        if(current==null){
          throw new TomRuntimeException("TransformerPlugin.process: current is null");
        } 
        Instruction tracelinkPopResolveInstruction = `TracelinkPopulateResolve(refClassName,tracedLinks,current);
        //add instruction which populates RefClass (set..),
        //instr=`concInstruction(instr*,tracelinkPopResolveInstruction)

        vType = `Type(concTypeOption(),type,EmptyTargetLanguageType());
        //add a test on vType here: if(vType!=null && vType!=)
        BQTerm subject = `BQVariable(concOption(),
                                     Name("tom__arg"),
                                     vType);
        Constraint constraint = `AndConstraint(TrueConstraint(),
                                               MatchConstraint(term,subject,vType));
        ciList.add(`ConstraintInstruction(
                         constraint,
                         RawAction(If(
                           TrueTL(),
                           AbstractBlock(concInstruction(instr*,tracelinkPopResolveInstruction)),
                           Nop())),
                         opts)
                );
      }
    }
    //InstructionList instructions = ASTFactory.makeInstructionListFromInstructionCollection(instructionList);
    RefClassTracelinkInstructionList refclassInstructions = ASTFactory.makeRefClassTracelinkInstructionList(refclassTInstructionList);
    Declaration refClass = `ReferenceClass(refClassName,refclassInstructions);

    LinkedList<Option> list = new LinkedList<Option>();
    list.add(orgTrack);
    OptionList options = ASTFactory.makeOptionList(list);

    ConstraintInstructionList astCiList = ASTFactory.makeConstraintInstructionList(ciList);
    List<TomVisit> visitList = new LinkedList<TomVisit>();

    visitList.add(`VisitTerm(vType,astCiList,options));
    TomVisitList astVisitList = ASTFactory.makeTomVisitList(visitList);

    BQTerm extendsTerm = `BQAppl(concOption(OriginTracking(strategyName,orgTrack.getLine(),orgTrack.getFileName())),Name("Identity"),concBQTerm());

    Declaration strategy = `Strategy(strategyName,extendsTerm,astVisitList,orgTrack);

    List<Declaration> result = new LinkedList<Declaration>();
    result.add(refClass);
    result.add(strategy);
    result.add(symbol);
    return result;
  }//genElementaryStrategy

  //TODO: change this, to specific, NOK with complex patterns
  private static BQTerm genTracelinkPopulateResolveCurrent(TomTerm term) {
    BQTerm result = null;
    %match(term) {
      RecordAppl[Constraints=concConstraint(_*,AliasTo(t),_*)] -> {
        %match(t) {
          Variable[Options=options,AstName=name,AstType=type] -> {
            result = `BQVariable(options,name,type);
          }
        }
      }
    }
    return result;
  }

  /**
   * 
   */
  private static void genAndPutElementaryStratSymbol(TransformerPlugin transformer,
                                                     Option orgTrack,
                                                     TomName stratName,
                                                     TomTypeList domain,
                                                     TomSymbol symbol) {
    List<Option> optionList = new LinkedList<Option>();
    optionList.add(orgTrack);
    
    TomType strategyType = `Type(concTypeOption(),"Strategy",EmptyTargetLanguageType());
    BQTermList makeArgs = `concBQTerm();
    BQTermList params = `concBQTerm();
    //parameters
    String stringStratName = stratName.getString();
    String makeTlCode = "new "+stringStratName+"(";
    int index = 0;
    TomTypeList makeTypes = domain; //`concTomType();
    while(!makeTypes.isEmptyconcTomType()) {
      String argName = "t"+index;
      if (index>0) {//if many parameters
        makeTlCode = makeTlCode.concat(",");
      }
      makeTlCode += argName;

      BQTerm arg = `BQVariable(concOption(),Name(argName),makeTypes.getHeadconcTomType());
      makeArgs = `concBQTerm(makeArgs*,arg);
      params = `concBQTerm(params*,Composite(CompositeBQTerm(arg)));//build part of CompositeBQTerm

      makeTypes = makeTypes.getTailconcTomType();
      index++;
    }
    makeTlCode += ")";
    Option makeOption = `OriginTracking(stratName,orgTrack.getLine(),orgTrack.getFileName());
    Declaration makeDecl = `MakeDecl(stratName, strategyType, makeArgs,
        CodeToInstruction(TargetLanguageToCode(ITL(makeTlCode))), makeOption);
    optionList.add(`DeclarationToOption(makeDecl));

    Option fsymOption = `OriginTracking(stratName,orgTrack.getLine(),orgTrack.getFileName());
    String varname = "t";
    BQTerm fsymVar = `BQVariable(concOption(fsymOption),Name(varname),strategyType);
    String code = ASTFactory.abstractCode("($"+varname+" instanceof "+stringStratName+")",varname);
    Declaration fsymDecl = `IsFsymDecl(stratName,fsymVar,Code(code),fsymOption);
    optionList.add(`DeclarationToOption(fsymDecl));

    TomTypeList transfoDomain = TomBase.getSymbolDomain(symbol);
    PairNameDeclList paramDecl =
      genStratPairNameDeclListFromTransfoSymbol(stratName,symbol);
    TomSymbol astSymbol = ASTFactory.makeSymbol(stringStratName,
        strategyType, transfoDomain, paramDecl, optionList);
    transformer.getSymbolTable().putSymbol(stringStratName,astSymbol);
  }//genAndPutElementaryStratSymbol


  %strategy ReplaceMakeDecl(composite:BQTerm) extends Identity() {
    visit Declaration {
      MakeDecl[AstName=name,AstType=type,Args=args,OrgTrack=ot] -> {
        return `MakeDecl(name,type,args,BQTermToInstruction(composite),ot);
      }
    }
  }

  private static BQTerm makeComposedStrategy(List<BQTerm> bqlist, DeclarationList declList, TomTypeList domain) {
    BQTermList bql = removeDuplicate(ASTFactory.makeBQTermList(bqlist));
    /*concOption() for the moment, to change*/
    BQTerm transfos = `Composite(CompositeBQTerm(BQAppl(
            concOption(),
            Name("Sequence"),
            bql)
          ));
    //add condition: call it only if resolve is needed
    BQTermList res = `concBQTerm(transfos, makeResolveBQTerm(declList, domain));
    //Declaration transformerBQTerm = `BQTermToDeclaration(
    BQTerm transformerBQTerm = `Composite(CompositeBQTerm(BQAppl(
            concOption(),
            Name("Sequence"),
            res)
          ));
    return transformerBQTerm;
  }

  private static BQTerm makeResolveBQTerm(DeclarationList declList, TomTypeList domain) {
    Option option = `noOption();
    String stringRSname = "";
    %match(declList) {
      concDeclaration(_*, ResolveStratDecl[TransfoName=name,OriginTracking=ot] ,_*) -> {
        option = `ot;
        stringRSname = "tom__StratResolve_"+`name;
      }
    }
    //build parameters
    BQTermList params = `concBQTerm();
    int index = 0;
    TomTypeList makeTypes = domain;
    while(!makeTypes.isEmptyconcTomType()) {
      String argName = "t"+index;
      BQTerm arg = `BQVariable(concOption(),Name(argName),makeTypes.getHeadconcTomType());
      params = `concBQTerm(params*,Composite(CompositeBQTerm(arg)));
      makeTypes = makeTypes.getTailconcTomType();
      index++;
    }
    //build resolve part of the BQComposite
    //TODO?: change that to avoid to have a hardcoded traversal?
    BQTerm bqtrans = `Composite(CompositeBQTerm(BQAppl(
            concOption(option), Name("TopDown"), concBQTerm(Composite(
                CompositeBQTerm(
                  BQAppl(concOption(option),Name(stringRSname),params)
                  ))))));
    return bqtrans;
  }

  private static boolean partialEqualsBQTerm(BQTerm bqt1, BQTerm bqt2) {
    boolean result = false;
    %match {
      Composite(CompositeBQTerm[term=BQAppl[Options=concOption(_*,OriginTracking[AstName=n],_*)]]) << bqt1
      && Composite(CompositeBQTerm[term=BQAppl[Options=concOption(_*,OriginTracking[AstName=n],_*)]]) << bqt2 -> { result = true; }
    }
    return result;
  }

  private static BQTermList removeDuplicate(BQTermList bqlist) {
    BQTermList result = `concBQTerm();
    for(BQTerm bqterm : bqlist.getCollectionconcBQTerm()) {
      boolean duplicate = false;
      for(BQTerm bqt : result.getCollectionconcBQTerm()) {
        duplicate = partialEqualsBQTerm(bqterm, bqt);
        if(duplicate) {
          break;
        }
      }
      if(!duplicate) {
        result = `concBQTerm(result*,bqterm);
      }
    }
    return result;
  }



  //TODO
  /*
Rappel :
ResolveStratElement = ResolveStratElement(WithName:String, ResolveOrgTrack:Option)
ResolveStratElementList = concResolveStratElement(ResolveStratElement*)
ResolveStratBlock = ResolveStratBlock(ToName:String, resolveStratElementList:ResolveStratElementList)
ResolveStratBlockList = concResolveStratBlock(ResolveStratBlock*)
   */

  /*
   * pem: make function non-static
   * example of generated code:
   %strategy Resolve(tom__linkClass:LinkClass,model:PetriNet) extends Identity() {
    visit Place {
      tom__arg@ResolveWorkDefinitionPlace[o=o,name=name] -> {
        Place res = (Place) tom__linkClass.get(`o).get(`name);
        `resolveInverseLinks(tom__arg, res, model);
        return res;
      }
    }

    visit Transition {
      tom__arg@ResolveWorkDefinitionTransition[o=o,name=name] -> {
        Transition res = (Transition) tom__linkClass.get(`o).get(`name);
        `resolveInverseLinks(tom__arg, res, model);
        return res;
      }
      tom__arg@ResolveProcessTransition[o=o,name=name] -> {
        Transition res = (Transition) tom__linkClass.get(`o).get(`name);
        `resolveInverseLinks(tom__arg, res, model);
        return res;
      }
    }
   */
  private static Declaration buildResolveStrat(TransformerPlugin transformer, 
                                               TomName toname, 
                                               TomTypeList domain, 
                                               List bqlist, 
                                               TomSymbol transformationSymbol, 
                                               String name, 
                                               ResolveStratBlockList rsbList, 
                                               TomNameList resolveNameList,
                                               Option ot) {
    List<TomVisit> visitList = new LinkedList<TomVisit>();

    %match(rsbList) {
      concResolveStratBlock(_*,xxx@ResolveStratBlock(tname,rseList),_*) -> {
        //System.out.println("###DEBUG### resolve etc xxx=\n"+`xxx);
        TomType ttype = `Type(concTypeOption(),tname,EmptyTargetLanguageType());
        //TODO: to check
        List<ConstraintInstruction> ciList = new LinkedList<ConstraintInstruction>();
        %match(rseList) {
          // "wName" is no longer useful -> signature to change
          concResolveStratElement(_*,ResolveStratElement(wname,rot),_*) -> {
          
            Slot sloto = `PairSlotAppl(
                    Name("o"),
                    Variable(
                      concOption(OriginTracking(Name("o"),rot.getLine(),rot.getFileName())),
                      Name("o"),
                      Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),
                      concConstraint()
                      )
                    );
            Slot slotname = `PairSlotAppl(
                    Name("name"),
                    Variable(
                      concOption(OriginTracking(Name("name"),rot.getLine(),rot.getFileName())),
                      Name("name"),
                      Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),
                      concConstraint()
                      )
                    );
            
            TomTerm pattern = `RecordAppl(
                concOption(rot),
                concTomName(rot.getAstName()),
                concSlot(sloto,slotname),
                concConstraint(
                    AliasTo(
                      Variable(
                        concOption(OriginTracking(
                            Name("tom__resolve"),
                            rot.getLine(),
                            rot.getFileName())),
                        Name("tom__resolve"),
                        Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),
                        concConstraint()
                        )
                      )
                    )
                  );

            //maybe something like that in the future
            //TomType matchType = (getOptionBooleanValue("newtyper")?SymbolTable.TYPE_UNKNOWN:ttype);
            BQTerm subject = `BQVariable(concOption(),Name("tom__arg"),ttype);
            Constraint constraint = `AndConstraint(TrueConstraint(),
                MatchConstraint(pattern,subject,ttype));

            TomName firstArgument = TomBase.getSlotName(transformationSymbol,0);
            TomName secondArgument = TomBase.getSlotName(transformationSymbol,1);
            Instruction referenceStatement /* TODO */;
            BQTerm res = `BQVariable(concOption(),Name("res"),ttype);
            BQTerm model = `BQVariable(concOption(),secondArgument,SymbolTable.TYPE_UNKNOWN);
            //System.out.println("*** transformationSymbol = " + transformationSymbol);

            Instruction resolveStatement = `BQTermToInstruction(FunctionCall(
                  Name("resolveInverseLinks"), transformer.getSymbolTable().getVoidType(),
                  concBQTerm(subject,res,model)));
            
            Instruction returnStatement = `Return(Composite(CompositeTL(ITL("res"))));
            /*referenceStatement,*/ 
            InstructionList instructions = `concInstruction(resolveStatement, CodeToInstruction(TargetLanguageToCode(ITL(";"))), returnStatement);
            ciList.add(`ConstraintInstruction(constraint, AbstractBlock(instructions),concOption(rot)));
                  //ResolveStratInstruction(rot.getAstName(),ttype),concOption(rot)));
            //TODO: GetSlot ou autre maniere de recuperer les elements
          }
        }

        visitList.add(`VisitTerm(ttype,
              ASTFactory.makeConstraintInstructionList(ciList),
              concOption(ot)));
        ciList.clear(); //reset ConstraintInstructionList
      }
    }
    String stringRSname = "tom__StratResolve_"+name;
    TomName rsname = `Name(stringRSname);
    BQTerm extendsTerm = `BQAppl(concOption(ot),Name("Identity"),concBQTerm());
    TomVisitList astVisitList = `ASTFactory.makeTomVisitList(visitList);
    Option orgTrack = `OriginTracking(Name("Strategy"),
        ot.getLine(),
        ot.getFileName());
    Declaration resolve = `Strategy(rsname,extendsTerm,astVisitList,orgTrack);

    //Many questions/problems about this TomSymbol:
    //TomTypeList & PairNameDeclList: empty for the moment, but will need something 
    //(current type as in %strategy Resolve(translator:SimplePDLToPetriNet3c)
    //OptionList: ot (good choice?) + IsFsymDecl + GetSlotDecl
    // => should be ok
    
    List<Option> options = new LinkedList<Option>();
    options.add(ot);
    //definition of make.
    //code generation of IsFsymDecl
    TomType strategyType = `Type(concTypeOption(),"Strategy",EmptyTargetLanguageType());
    /* Arguments of the strategy. For the moment, nothing, but it will be
       necessary */
    BQTermList makeArgs = `concBQTerm();
    BQTermList params = `concBQTerm();
    //parameters
    //factorize this code with other buildStrat functions?
    String makeTlCode = "new "+stringRSname+"(";
    int index = 0;
    TomTypeList makeTypes = domain; //`concTomType();
    while(!makeTypes.isEmptyconcTomType()) {
      String argName = "t"+index;
      if (index>0) {//if many parameters
        makeTlCode = makeTlCode.concat(",");
      }
      makeTlCode += argName;

      BQTerm arg = `BQVariable(concOption(),Name(argName),makeTypes.getHeadconcTomType());
      makeArgs = `concBQTerm(makeArgs*,arg);
      params = `concBQTerm(params*,Composite(CompositeBQTerm(arg)));//build part of CompositeBQTerm

      makeTypes = makeTypes.getTailconcTomType();
      index++;
    }
    makeTlCode += ")";
    //should not be ot, since the line number is different, but here, we use ot
    Option makeOption = `OriginTracking(rsname,ot.getLine(),ot.getFileName());
    
    Declaration makeDecl = `MakeDecl(rsname, strategyType, makeArgs,
        CodeToInstruction(TargetLanguageToCode(ITL(makeTlCode))), makeOption);
    //System.out.println("###DEBUG### buildResolveStrat - makeDecl = "+makeDecl);
    options.add(`DeclarationToOption(makeDecl));

    //definition of the is_fsym method.
    //should not be ot, since the line number is different, but here, we use ot
    Option fsymOption = `OriginTracking(rsname,ot.getLine(),ot.getFileName());
    String varname = "t";
    BQTerm fsymVar = `BQVariable(concOption(fsymOption),Name(varname),strategyType);
    String code = ASTFactory.abstractCode("($"+varname+" instanceof "+stringRSname+")",varname);
    Declaration fsymDecl = `IsFsymDecl(rsname,fsymVar,Code(code),fsymOption);
    options.add(`DeclarationToOption(fsymDecl));

    //TomType transfoCodomain = TomBase.getSymbolCodomain(transformationSymbol);
    //types <-> `concTomType() (no param)
    TomTypeList transfoDomain = TomBase.getSymbolDomain(transformationSymbol);
    //slots <-> `concPairNameDecl() (no param)
    //PairNameDeclList paramDecl = transformationSymbol.getPairNameDeclList();
    PairNameDeclList paramDecl = genStratPairNameDeclListFromTransfoSymbol(rsname,transformationSymbol);

    TomSymbol astSymbol = ASTFactory.makeSymbol(stringRSname,
        strategyType, transfoDomain, paramDecl, options);
    transformer.getSymbolTable().putSymbol(stringRSname,astSymbol);

    //build here the part of BQTermComposite transformer
/*    BQTerm bqtrans = `Composite(CompositeBQTerm(BQAppl(
            concOption(ot), Name("TopDown"), concBQTerm(Composite(
                CompositeBQTerm(
                  BQAppl(concOption(ot),Name(stringRSname),params)
                  ))))));
    bqlist.add(bqtrans);*/
    //result.add(`AbstractDecl(concDeclaration(resolve,SymbolDecl(rsname))));
    return `AbstractDecl(concDeclaration(resolve,SymbolDecl(rsname)));
  } //buildResolveStrat

  private static PairNameDeclList genStratPairNameDeclListFromTransfoSymbol(TomName stratName, 
                                                                            TomSymbol transformationSymbol) {
    PairNameDeclList result = `concPairNameDecl();
    PairNameDeclList transformationParameters = transformationSymbol.getPairNameDeclList();
    %match(transformationParameters) {
      concPairNameDecl(_*,PairNameDecl[SlotName=name,
                                       SlotDecl=decl@GetSlotDecl[SlotName=slotname,Variable=bq,Expr=expr,OrgTrack=ot]],_*) -> {
        PairNameDecl pnd = `PairNameDecl(name,GetSlotDecl(stratName,slotname,bq,expr,ot));
        result = `concPairNameDecl(result*,pnd);
      }
    }
    return result;
  }

//OriginTracking(Name("Transformation"),19,"Transfo3.t")
//OriginTracking(AstName:TomName,Line:int,FileName:String)
/*
  %strategy genResolveElement(declList:DeclarationList) extends Identity() {
    visit Instruction {
      Resolve[Src=sname,SType=stypename,Target=tname,TType=ttypename,OrgTrack=ot] -> {
        //TODO:
        //BQTerm: OK 
        //TypeTermDecl: OK
        //SymbolDecl: OK
        //ResolveClassDecl: OK
        //ResolveOp: OK
        //what else?

        int line = `ot.getLine();
        String fileName = `ot.getFileName();

        String resolveStringName = "Resolve"+sname+tname;

        DeclarationList resolveTTDecl= `concDeclaration(
            IsSortDecl(
              BQVariable(
                concOption(OriginTracking(Name("t"),line,fileName)),
                Name("t"),
                Type(concTypeOption(),resolveStringName,EmptyTargetLanguageType())),
              Code(resolveStringName),
              OriginTracking(Name("is_sort"),line,fileName)
              )
            );

        TomName resolveName = `Name(resolveStringName);
        resolveOrgTrack = `OriginTracking(resolveName,line,fileName);

        TomType stype = `Type(concTypeOption(),stypename,EmptyTargetLanguageType());
        TomType stringType = `Type(concTypeOption(),"String",EmptyTargetLanguageType()); //  /!\ Java dependant
        rtypes = `concTomType(stype, stringType);
        TomType tType = `Type(concTypeOption(),ttypename,EmptyTargetLanguageType());

        PairNameDecl objectPNDecl = `PairNameDecl(
                                       Name("o"),
                                       ResolveGetSlotDecl(
                                         Name(resolveStringName),
                                         Name("o"), 
                                         BQVariable(
                                           concOption(OriginTracking(Name("t"),line,fileName)),
                                           Name("t"),
                                           tType 
                                           ),
                                         OriginTracking(Name("get_slot"),line,fileName)
                                         )
                                       );
        PairNameDecl namePNDecl = `PairNameDecl(
                                       Name("name"),
                                       ResolveGetSlotDecl(
                                         Name(resolveStringName),
                                         Name("name"), 
                                         BQVariable(
                                           concOption(OriginTracking(Name("t"),line,fileName)),
                                           Name("t"),
                                           tType 
                                           ),
                                         OriginTracking(Name("get_slot"),line,fileName)
                                         )
                                       );


        List<PairNameDecl> resolvePairNameDeclList = new LinkedList<PairNameDecl>();
        resolvePairNameDeclList.add(objectPNDecl);
        resolvePairNameDeclList.add(namePNDecl);

        Declaration isfsymDecl = `ResolveIsFsymDecl(
            resolveName,
            BQVariable(concOption(OriginTracking(Name("t"),line,fileName)),Name("t"),tType),
            OriginTracking(Name("is_fsym"),line,fileName)
);

        BQTermList makeArgs = `concBQTerm(
            BQVariable(concOption(OriginTracking(Name("o"),line,fileName)),Name("o"),sType),
            BQVariable(concOption(OriginTracking(Name("name"),line,fileName)),Name("name"),stringType)
            );

        Declaration makeDecl = `ResolveMakeDecl(
            resolveName,
            tType,
            makeArgs,
            OriginTracking(Name("make"),line,fileName)
            );

        List<Option> symbolOptions = new LinkedList<Option>();
        symbolOptions.add(orgTrack);
        symbolOptions.add(`DeclarationToOption(makeDecl));
        symbolOptions.add(`DeclarationToOption(isfsymDecl));

        //create Resolve constructor symbol ("ResolveOp")
        TomSymbol astSymbol = ASTFactory.makeSymbol(
            resolveStringName,
            tType,
            rtypes,
            ASTFactory.makePairNameDeclList(resolvePairNameDeclList),
            symbolOptions
            );
        //add "ResolveOp" symbol into SymbolTable
        putSymbol(resolveStringName,astSymbol);

        declList.add(`TypeTermDecl(resolveName,resolveTTDecl,resolveOrgTrack));
        declList.add(`SymbolDecl(Name(resolveStringName)));

        String extendsName = null;
        TomSymbol symb = symbolTable.getSymbolFromName(ttypename);
        //retrieve classimpl
        %match(symb.getOptions()) {
          concOption(_*,DeclarationToOption(ImplementDecl[Expr=code]),_*) -> {
            extendsName = `code.getCode();
          }
        }
        declList.add(`ResolveClassDecl(.., stypename, ttypename, extendsName));

        //build the `ResolveXY(src,"dst");
        BQTerm bqterm = `Composite(CompositeBQTerm(BQAppl(
                concOption(OriginTracking(Name(resolveStringName),line,fileName),ModuleName("default")),
                Name(resolveStringName),
                concBQTerm(
                  Composite(CompositeBQTerm(BQVariable(
                        concOption(OriginTracking(Name(sname),line,fileName),ModuleName("default")),
                        Name(sname),
                        Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())
                        ))),
                  Composite(CompositeTL(ITL("\""+tname+"\"")))
                ))));
        return `BQTermToInstruction(bqterm);
      }
      Tracelink[] -> {
        //TODO:
      }
    }
  }*/
}
/*
                  Composite(CompositeBQTerm(
                      BQVariable(
                        concOption(OriginTracking(Name(tname),line,fileName),ModuleName("default")),
                        Name(tname),
                        Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())
                        )
                      ))*/

