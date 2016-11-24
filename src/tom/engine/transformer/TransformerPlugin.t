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
 * Jean-Christophe Bach  e-mail: jeanchristophe.bach@inria.fr
 *
 **/

package tom.engine.transformer;

import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.File;
import java.io.IOException;

import tom.engine.exception.TomException;
import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tominstruction.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomdeclaration.types.declaration.*;
import tom.engine.adt.tomdeclaration.types.declarationlist.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomoption.types.option.*;
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

import tom.library.sl.*;

/**
 * The Transmorfer plugin.
 * Performs tree transformation and code expansion.
 *
 * TO BE DETAILED /!\
 * 1st step: Transformation - 
 * 
 * 
 * 
 */
public class TransformerPlugin extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }
  %include { ../../library/mapping/java/util/types/List.tom }
  %include { ../../library/mapping/java/util/types/HashSet.tom }

  %typeterm TransformerPlugin { implement { TransformerPlugin } }

  private static Logger logger = Logger.getLogger("tom.engine.transformer.TransformerPlugin");
  /** some prefixes and suffixes */
  public static final String TRANSFORMED_SUFFIX = ".tfix.transformed";
  public static final String REFCLASS_PREFIX = "tom__reference_class_";
  public static final String STRAT_RESOLVE_PREFIX = "tom__StratResolve_";
  public static final String RESOLVE_INVERSE_LINK_FUNCTION = "resolveInverseLinks";
  public static final String RESOLVE_ELEMENT_PREFIX = "Resolve";
  public static final String RESOLVE_ELEMENT_ATTRIBUTE_NAME = "tom_resolve_element_attribute_name";
  public static final String RESOLVE_ELEMENT_ATTRIBUTE_O = "tom_resolve_element_attribute_o";
  private static final TomType voidType = ASTFactory.makeType(`concTypeOption(),"undefined","void");

  private SymbolTable symbolTable;
  private int freshCounter = 0;
  private boolean resolveFlag = false;

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
    try {
      // replace content of Transformation declaration by Strategy
      transformedTerm = `TopDown(ProcessTransformation(this)).visitLight(transformedTerm);
      setWorkingTerm(transformedTerm);
      // verbose
      TomMessage.info(logger,null,0,TomMessage.tomTransformingPhase,
          Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() +
            TransformerPlugin.TRANSFORMED_SUFFIX, transformedTerm);
      }
    }  catch(VisitFailure e) {
      throw new TomRuntimeException("TransformerPlugin.process: fail on " + transformedTerm);
    } catch (Exception e) {
      TomMessage.error(logger, getStreamManager().getInputFileName(), 0,
          TomMessage.exceptionMessage, e.getMessage());
      e.printStackTrace();
    }
  }

  /** 
   * This strategy handles each Transformation declaration and transforms it to
   * an AbstractDecl containing a list of declarations.
   */
  %strategy ProcessTransformation(transformer:TransformerPlugin) extends Identity() {
    /* 
     * Compilation of %transformation to strategy: only Transformation
     * constructs are modified in this plugin
     */
    visit Declaration {
      t@Transformation(transfoName,domain,elemTransfoList,fileFrom,fileTo,orgTrack) ->
       {
         return `AbstractDecl(transformer.processSubDecl(t,transfoName,domain,elemTransfoList,fileFrom,fileTo,orgTrack));
      }
    }
  }

  /**
   * This function processes a transformation declarations list.
   * @param transfo the transformation Declaration
   * @param transfoName the name of the transformation
   * @param transfoDomain the domain of the transformation
   * @param elemTransfoList the list of elementary transformations composing
   * the whole transformation
   * @param fileFrom the source metamodel file name
   * @param fileTo the target metamodel file name
   * @param orgTrack an option to track the transformation construct
   * @return a list of declarations related to the transformation
   */
  private DeclarationList processSubDecl(Declaration transfo,TomName transfoName, TomTypeList transfoDomain, ElementaryTransformationList elemTransfoList, String fileFrom, String fileTo, Option orgTrack) {

    List<Declaration> result = new LinkedList<Declaration>();
    HashSet<String> resolveNameSet = new HashSet<String>();

    HashSet<String> withNameSet = new HashSet<String>();
    HashSet<String> toNameSet = new HashSet<String>();

    ElementaryTransformationList etl = `concElementaryTransformation();
    try {
      //This strategy call makes lots of side effects:
      //- on resolveNameSet
      //- on result (add created declarations)
      //- on withNameSet and on toNameSet
      etl = `TopDown(GenResolveElement(this,resolveNameSet,result,withNameSet,toNameSet)).visitLight(elemTransfoList);
    } catch (VisitFailure e) {
      throw new TomRuntimeException("TransformerPlugin.GenResolveElement: fail on " + transfo);
    }

    List bqlist = new LinkedList<BQTerm>();
    String strTransfoName = transfoName.getString();
    TomSymbol transfoSymbol = getSymbolTable().getSymbolFromName(strTransfoName);
    //elementary strategies generation
    //%match(elemTransfoList) {
    %match(etl) {
      concElementaryTransformation(_*,ElementaryTransformation[ETName=etName@Name(eStratName),Traversal=traversalStrat,AstRuleInstructionList=riList,Options=concOption(_*,ot@OriginTracking(_,_,_),_*)],_*) -> {
        //generate elementary `Strategy, `ReferenceClass and `SymbolDecl
        result.addAll(`genElementaryStrategy(etName,traversalStrat,riList,transfoSymbol,ot));
        //Generate symbol for elementary strategy and put it into symbol table
        TomSymbol astElemStratSymbol = `generateElementaryStratSymbol(ot,
            etName, transfoDomain, transfoSymbol);
        getSymbolTable().putSymbol(`eStratName,astElemStratSymbol);
        //add the part of CompositeBQTerm transformer to bqlist
        bqlist.add(`traversalStrat);
      }
    }
     

    if(resolveFlag) {

    //retrieve %transformation line number and current file name to forge
    //Resolve elements options
    int line = orgTrack.getLine();
    String fileName = orgTrack.getFileName();
    List<ResolveStratBlock> resolveStratBlockList = new LinkedList<ResolveStratBlock>();
    //build blocks for resolve strategy
    for(String toName:toNameSet) {
      List<ResolveStratElement> resolveStratElementList = new LinkedList<ResolveStratElement>();
      for(String withName:withNameSet) {
        String resolveStringName = TransformerPlugin.RESOLVE_ELEMENT_PREFIX+withName+toName;
        //do not create a ResolveStratElement if no corresponding resolve
        //element has been created / is necessary (from %resolve)
        if(getSymbolTable().getSymbolFromName(resolveStringName)!=null) {
          Option resolveOrgTrack =
            `OriginTracking(Name(resolveStringName),line,fileName);
          resolveStratElementList.add(`ResolveStratElement(withName, resolveOrgTrack));
        }
      }
      ResolveStratElementList astResolveStratElementList = ASTFactory.makeResolveStratElementList(resolveStratElementList);
      resolveStratBlockList.add(`ResolveStratBlock(toName,astResolveStratElementList));
      //resolveStratElementList.clear();//no need to clear, we create a new one
    }

    ResolveStratBlockList rsblist =
      `ASTFactory.makeResolveStratBlockList(resolveStratBlockList);


    TomNameList resolveNameList = `concTomName();//populate in GenResolveElement strategy
    for(String stringName:resolveNameSet) {
      resolveNameList = `concTomName(Name(stringName),resolveNameList*);
    }

    //TODO: check fileFrom and fileTo
    try {
    fileFrom = checkAndNormalizeFileName(fileFrom, "source", orgTrack);
    fileTo = checkAndNormalizeFileName(fileTo, "target", orgTrack);
    } catch (Exception e) {
      e.printStackTrace();
    }
    //Generation of Resolve strategy
    Declaration resolveStratDecl = `buildResolveStrat(transfoName, bqlist,
        transfoSymbol, rsblist, resolveNameList, fileFrom, fileTo, orgTrack);
    //add it to DeclarationList
    result.add(resolveStratDecl);

    }//end if(resolveFlag)

    //TODO: test hook
    //result.add(inverseLinks);

    //let's change the transformation MakeDecl
    //this first step is necessary due to the fact that an elementary
    //transformation may have many rules, therefore bqlist may have duplicate
    //bqterms
    BQTerm composite = makeComposedStrategy(bqlist,/*declList,*/transfoDomain);
    try {
      TomSymbol newSymbol =
        `TopDown(ReplaceMakeDecl(composite)).visitLight(transfoSymbol);
      getSymbolTable().putSymbol(strTransfoName,newSymbol);
    } catch (VisitFailure e) {
      throw new TomRuntimeException("TransformerPlugin.processSubDecl: fail on " + transfoSymbol);
    }
    return ASTFactory.makeDeclarationList(result);
  }//processSubDecl

  /**
   * Generate the `Strategy corresponding to an `ElementaryTransformation
   * @param strategyName the name of the strategy (elementary transformation)
   * @param traversal the traversal strategy which have been given to the
   * elementary transformation. It is used in the "transformer" strategy
   * composition.
   * @param riList the list of rules composing the elementary transformation
   * @param transfoSymbol the symbol of the transformation
   * @param orgTrack an option to track the construct
   * @return DeclarationList containclieng the `Strategy, the `SymbolDecl 
   * and a `ReferenceClass
   */
  private List<Declaration> genElementaryStrategy(TomName strategyName,
                                                  BQTerm traversal,
                                                  RuleInstructionList riList,
                                                  TomSymbol transfoSymbol,
                                                  Option orgTrack) {
    //build visitList
    List<ConstraintInstruction> ciList = new LinkedList<ConstraintInstruction>();
    List<RefClassTracelinkInstruction> refclassTInstructionList = new
      LinkedList<RefClassTracelinkInstruction>();
    TomType vType = null;
    List<Declaration> result = new LinkedList<Declaration>();
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
        /*cases:
        !tracelink ^  resolve => err
        !tracelink ^ !resolve => OK, but no need to execute this part of code
         tracelink ^  resolve => OK
         tracelink ^ !resolve => OK
        */
        InstructionList instrList = `concInstruction();
        if(current==null) {
          if(resolveFlag) {//if there is a resolve while no tracelink: err
            throw new TomRuntimeException("TransformerPlugin.process: fail to generate tracelink backquote term corresponding to "+`term);
          }
          //in case there is no tracelink
          instrList = `concInstruction(instr*);
        } else {//in case there is at least one tracelink
          TomName firstArgument = TomBase.getSlotName(transfoSymbol,0);
          TomType firstArgType = TomBase.getSlotType(transfoSymbol,
                                                     firstArgument);
          BQTerm link = `BQVariable(concOption(),firstArgument,firstArgType);
          TomName refClassName =
            `Name(TransformerPlugin.REFCLASS_PREFIX+strategyName.getString());
          Instruction tracelinkPopResolveInstruction =
            `TracelinkPopulateResolve(refClassName,tracedLinks,current,link);
          instrList = `concInstruction(instr*,tracelinkPopResolveInstruction);
          RefClassTracelinkInstructionList refclassInstructions = ASTFactory.makeRefClassTracelinkInstructionList(refclassTInstructionList);
        //TODO: use Class(AstName:TomName,ExtendsType:TomType,SuperTerm:BQTerm,Declaration:Declaration)
        Declaration refClass = `ReferenceClass(refClassName,refclassInstructions);
        result.add(refClass);
        }//case tracelink
        vType = `Type(concTypeOption(),type,EmptyTargetLanguageType());
        //add a test on vType here: if(vType!=null && vType!=)
        BQTerm subject = `BQVariable(concOption(),
                                     Name("tom__arg"),
                                     vType);
        Constraint constraint = `AndConstraint(TrueConstraint(),
                                               MatchConstraint(term,
                                                               subject,
                                                               vType)
                                               );
        ciList.add(`ConstraintInstruction(
                         constraint,
                         RawAction(If(
                           TrueTL(),
                           AbstractBlock(instrList),
                           Nop())),
                         opts)
                );
      }
    }
    ConstraintInstructionList astCiList =
      ASTFactory.makeConstraintInstructionList(ciList);
    //build the strategy (elementary transformation)
    LinkedList<Option> list = new LinkedList<Option>();
    list.add(orgTrack);
    OptionList options = ASTFactory.makeOptionList(list);
    List<TomVisit> visitList = new LinkedList<TomVisit>();
    visitList.add(`VisitTerm(vType,astCiList,options));
    TomVisitList astVisitList = ASTFactory.makeTomVisitList(visitList);

    BQTerm extendsTerm = `BQAppl(concOption(OriginTracking(strategyName, 
                                               orgTrack.getLine(), 
                                               orgTrack.getFileName())), 
                                            Name("Identity"),concBQTerm());
    Declaration strategy = `Strategy(strategyName, 
                                     extendsTerm,
                                     astVisitList,
                                     concDeclaration(),
                                     orgTrack);
    //add forged strategy + symbol to the declarations list
    result.add(strategy);
    result.add(`SymbolDecl(strategyName));
    return result;
  }//genElementaryStrategy

  /**
   *
   * @param term the term on which the tracelink BQTerm is based
   * @return the Backquote term representing 
   */
  //TODO: change this, to specific, NOK with complex patterns
  private BQTerm genTracelinkPopulateResolveCurrent(TomTerm term) {
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
   * Generate TomSymbol for each strategy representing an elementary
   * transformation
   * @param orgTrack an option to track the construct
   * @param stratName the name of the strategy
   * @param transfoDomain the domain of the strategy (the same as the
   * transformation domain)
   * @param symbol the transformation symbol where parameters are retrieved
   * @return The generated symbol of an elementary strategy
   */
  private TomSymbol generateElementaryStratSymbol(Option orgTrack,
                                                  TomName stratName,
                                                  TomTypeList transfoDomain,
                                                  TomSymbol transfoSymbol) {
    List<Option> optionList = new LinkedList<Option>();
    optionList.add(orgTrack);
    
    TomType strategyType = `Type(concTypeOption(),"Strategy",EmptyTargetLanguageType());
    BQTermList makeArgs = `concBQTerm();
    BQTermList params = `concBQTerm();
    //parameters
    String stringStratName = stratName.getString();
    String makeTlCode = "new "+stringStratName+"(";
    int index = 0;
    TomTypeList makeTypes = transfoDomain; //`concTomType();
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

    //TomTypeList transfoDomain = TomBase.getSymbolDomain(transfoSymbol);
    PairNameDeclList paramDecl =
      genStratPairNameDeclListFromTransfoSymbol(stratName,transfoSymbol);
    return ASTFactory.makeSymbol(stringStratName, strategyType, transfoDomain,
        paramDecl, optionList);
  }//generateElementaryStratSymbol


  /**
   * This strategy modifies the MakeDecl of a transformation symbol by adding
   * a forged BQTerm
   */
  %strategy ReplaceMakeDecl(composite:BQTerm) extends Identity() {
    visit Declaration {
      MakeDecl[AstName=name,AstType=type,Args=args,OrgTrack=ot] -> {
        return `MakeDecl(name,type,args,BQTermToInstruction(composite),ot);
      }
    }
  }

  /**
   * This function generates the Backquote term of the transformation
   * @param bqlist the list of BQTerm representing the elementary
   * transformation strategies, and composing the whole transformation
   * @param declList a declaration list necessary to build the Resolve BQTerm
   * @param transfoDomain the domain of the transformation, used to build the
   * Resolve BQTerm
   * @return a Backquote term representing the whole transformation strategy
   */
  private BQTerm makeComposedStrategy(List<BQTerm> bqlist, 
                                      /*DeclarationList declList, */
                                      TomTypeList transfoDomain) {
    BQTermList bql = removeDuplicate(ASTFactory.makeBQTermList(bqlist));
    /*concOption() for the moment, to change*/
    BQTerm transfos = `Composite(CompositeBQTerm(BQAppl(
            concOption(),
            Name("Sequence"),
            bql)
          ));
    //TODO
    //add condition: call it only if resolve is needed
    /*BQTermList res = `concBQTerm(transfos, 
                                 makeResolveBQTerm(declList, transfoDomain));
    BQTerm transformerBQTerm = `Composite(CompositeBQTerm(BQAppl(
            concOption(),
            Name("Sequence"),
            res)
          ));
    return transformerBQTerm;*/
    return transfos;
  }

  /**
   * This function generates the Resolve BQTerm used in the whole
   * transformation BQTerm
   * @param declList the declaration list containing ResolveStratDecl() nodes
   * @param transfoDomain the domain of the transformation
   * @return the generated Resolve BQTerm
   */
  private BQTerm makeResolveBQTerm(DeclarationList declList, 
                                   TomTypeList transfoDomain) {
    Option option = `noOption();
    String stringRSname = "";
    %match(declList) {
      concDeclaration(_*, ResolveStratDecl[TransfoName=name,OriginTracking=ot] ,_*) -> {
        option = `ot;
        stringRSname = TransformerPlugin.STRAT_RESOLVE_PREFIX+`name;
      }
    }
    //build parameters
    BQTermList params = `concBQTerm();
    int index = 0;
    TomTypeList makeTypes = transfoDomain;
    while(!makeTypes.isEmptyconcTomType()) {
      String argName = "t"+index;
      BQTerm arg = `BQVariable(concOption(),Name(argName),makeTypes.getHeadconcTomType());
      params = `concBQTerm(params*,Composite(CompositeBQTerm(arg)));
      makeTypes = makeTypes.getTailconcTomType();
      index++;
    }
    //build resolve part of the BQComposite
    //TODO?: change that to avoid to have a hardcoded default traversal?
    BQTerm bqtrans = `Composite(CompositeBQTerm(BQAppl(
            concOption(option), Name("TopDown"), concBQTerm(Composite(
                CompositeBQTerm(
                  BQAppl(concOption(option),Name(stringRSname),params)
                  ))))));
    return bqtrans;
  }

  /**
   * This function compares (partly) two backquote terms. This "equality" is
   * only based on the AstName contained in the OriginTracking option.
   * @param bqt1 a backquote term
   * @param bqt2 a backquote term to compare to the first one
   * @return the result of the "test of equality"
   */
  private boolean partialEqualsBQTerm(BQTerm bqt1, BQTerm bqt2) {
    %match {
      Composite(CompositeBQTerm[term=BQAppl[Options=concOption(_*,OriginTracking[AstName=n],_*)]]) << bqt1
      &&
      Composite(CompositeBQTerm[term=BQAppl[Options=concOption(_*,OriginTracking[AstName=n],_*)]]) << bqt2 -> { return true; }
    }
    return false;
  }

  /**
   * This function removes duplicate BQTerm from a BQTermList
   * @param bqlist a BQTermList which may contain duplicate BQTerm
   * @return a BQTermList without any duplicate BQTerm
   */
  private BQTermList removeDuplicate(BQTermList bqlist) {
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



  /**
   * Generates the Resolve strategy
   * Here is an example of code for this strategy:
   * %strategy Resolve(tom__linkClass:LinkClass,model:PetriNet) extends Identity() {
   *   visit Place {
   *     tom__arg@ResolveWorkDefinitionPlace[o=o,name=name] -> {
   *       Place res = (Place) tom__linkClass.get(`o).get(`name);
   *       `resolveInverseLinks(tom__arg, res, model);
   *       return res;
   *     }
   *   }
   * 
   *   visit Transition {
   *     tom__arg@ResolveWorkDefinitionTransition[o=o,name=name] -> {
   *       Transition res = (Transition) tom__linkClass.get(`o).get(`name);
   *       `resolveInverseLinks(tom__arg, res, model);
   *       return res;
   *     }
   *     tom__arg@ResolveProcessTransition[o=o,name=name] -> {
   *       Transition res = (Transition) tom__linkClass.get(`o).get(`name);
   *       `resolveInverseLinks(tom__arg, res, model);
   *       return res;
   *     }
   *   }
   * }
   * @param transfoName the name of the transformation
   * @param bqlist
   * @param transformationSymbol
   * @param rsbList
   * @param resolveNameList
   * @param fileFrom the source metamodel file name
   * @param fileTo the target metamodel file name
   * @param ot 
   * @return the AbstractDecl containing the Resolve Strategy and its
   * SymbolDecl
   */
  private Declaration buildResolveStrat(TomName transfoName,
                                        List bqlist, 
                                        TomSymbol transformationSymbol, 
                                        ResolveStratBlockList rsbList, 
                                        TomNameList resolveNameList,
                                        String fileFrom,
                                        String fileTo,
                                        Option ot) {
    List<TomVisit> visitList = new LinkedList<TomVisit>();

    %match(rsbList) {
      concResolveStratBlock(_*,ResolveStratBlock(tname,rseList),_*) -> {
        TomType ttype = `Type(concTypeOption(),tname,EmptyTargetLanguageType());
        //TODO: to check
        List<ConstraintInstruction> ciList = new LinkedList<ConstraintInstruction>();
        %match(rseList) {
          // "wName" is no longer useful -> signature to change
          concResolveStratElement(_*,ResolveStratElement(wname,rot),_*) -> {
            int rline = `rot.getLine();
            String rfileName = `rot.getFileName();
            Slot sloto = genPairSlotAppl(TransformerPlugin.RESOLVE_ELEMENT_ATTRIBUTE_O, rline, rfileName);
            Slot slotname = genPairSlotAppl(TransformerPlugin.RESOLVE_ELEMENT_ATTRIBUTE_NAME, rline, rfileName);
            
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
                        getSymbolTable().TYPE_UNKNOWN,
                        concConstraint()
                        )
                      )
                    )
                  );
            //getSymbolTable().TYPE_UNKNOWN <->
            //Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),

            //maybe something like that in the future
            //TomType matchType = (getOptionBooleanValue("newtyper")?SymbolTable.TYPE_UNKNOWN:ttype);
            BQTerm subject = `BQVariable(concOption(),Name("tom__arg"),ttype);
            Constraint constraint = `AndConstraint(TrueConstraint(),
                MatchConstraint(pattern,subject,ttype));

            TomName firstArgument = TomBase.getSlotName(transformationSymbol,0);
            TomName secondArgument = TomBase.getSlotName(transformationSymbol,1);
            TomType firstArgType = TomBase.getSlotType(transformationSymbol,firstArgument);
            BQTerm link = `BQVariable(concOption(),firstArgument,firstArgType);
            BQTerm res = `BQVariable(concOption(),Name("res"),ttype);
            BQTerm first = `BQVariable(concOption(),Name(TransformerPlugin.RESOLVE_ELEMENT_ATTRIBUTE_O),SymbolTable.TYPE_UNKNOWN);
            BQTerm second = `BQVariable(concOption(),Name(TransformerPlugin.RESOLVE_ELEMENT_ATTRIBUTE_NAME),SymbolTable.TYPE_UNKNOWN);
            //replace ITL() by FunctionCall()?
            //to change: language specific
            //Instruction referenceStatement = `LetRef(res,
            Instruction referenceStatement = `AbstractBlock(concInstruction(
                  CodeToInstruction(TargetLanguageToCode(ITL(TomBase.getTLType(getSymbolTable().getType(tname))))),
                  Assign(res,
                    Cast(ttype,BQTermToExpression(Composite(
                          CompositeBQTerm(link),
                          CompositeTL(ITL(".get(")),
                          CompositeBQTerm(first),
                          CompositeTL(ITL(").get(")),
                          CompositeBQTerm(second),
                          CompositeTL(ITL(")"))
                          )))
                    )
                  ));

            BQTerm model = `BQVariable(concOption(),secondArgument,SymbolTable.TYPE_UNKNOWN);
            Instruction resolveStatement = `BQTermToInstruction(FunctionCall(
                  Name(TransformerPlugin.RESOLVE_INVERSE_LINK_FUNCTION), getSymbolTable().getVoidType(),
                  concBQTerm(subject,res,model)));
            Instruction returnStatement = `Return(Composite(CompositeTL(ITL("res"))));
            /*referenceStatement,*/ 
            InstructionList instructions = `concInstruction(referenceStatement,resolveStatement, CodeToInstruction(TargetLanguageToCode(ITL(";"))), returnStatement);
            ciList.add(`ConstraintInstruction(constraint, AbstractBlock(instructions),concOption(rot)));
          }
        }

        visitList.add(`VisitTerm(ttype,
              ASTFactory.makeConstraintInstructionList(ciList),
              concOption(ot)));
        ciList.clear(); //reset ConstraintInstructionList
      }
    }
    String stringRSname = TransformerPlugin.STRAT_RESOLVE_PREFIX+transfoName.getString();
    TomName rsname = `Name(stringRSname);
    BQTerm extendsTerm = `BQAppl(concOption(ot),Name("Identity"),concBQTerm());
    TomVisitList astVisitList = `ASTFactory.makeTomVisitList(visitList);
    int line = `ot.getLine();
    String fileName = `ot.getFileName();
    Option orgTrack = `OriginTracking(Name("Strategy"),line,fileName);
    Declaration inverseLinks = `ResolveInverseLinksDecl(resolveNameList,fileFrom,fileTo);

    Declaration resolve = `Strategy(rsname, extendsTerm, astVisitList,
        concDeclaration(inverseLinks), orgTrack);

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
    TomTypeList transfoDomain = TomBase.getSymbolDomain(transformationSymbol);
    TomTypeList makeTypes = transfoDomain; //`concTomType();
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
    Option makeOption = `OriginTracking(rsname,line,fileName);
    
    Declaration makeDecl = `MakeDecl(rsname, strategyType, makeArgs,
        CodeToInstruction(TargetLanguageToCode(ITL(makeTlCode))), makeOption);
    options.add(`DeclarationToOption(makeDecl));

    //definition of the is_fsym method.
    //should not be ot, since the line number is different, but here, we use ot
    Option fsymOption = `OriginTracking(rsname,line,fileName);
    String varname = "t";
    BQTerm fsymVar = `BQVariable(concOption(fsymOption),Name(varname),strategyType);
    String code = ASTFactory.abstractCode("($"+varname+" instanceof "+stringRSname+")",varname);
    Declaration fsymDecl = `IsFsymDecl(rsname,fsymVar,Code(code),fsymOption);
    options.add(`DeclarationToOption(fsymDecl));

    //TomType transfoCodomain = TomBase.getSymbolCodomain(transformationSymbol);
    //types <-> `concTomType() (no param)
    //slots <-> `concPairNameDecl() (no param)
    //PairNameDeclList paramDecl = transformationSymbol.getPairNameDeclList();
    PairNameDeclList paramDecl =
      genStratPairNameDeclListFromTransfoSymbol(rsname,transformationSymbol);
    TomSymbol astSymbol = ASTFactory.makeSymbol(stringRSname, strategyType,
        transfoDomain, paramDecl, options);
    getSymbolTable().putSymbol(stringRSname,astSymbol);

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

  /**
   * Generates and returns the list of the strategy parameters
   * @param stratName the name of the strategy
   * @param transformationSymbol the symbol of the transformation, needed to
   * retrieve parameters
   * @return the PairNameDeclList containing the strategy parameters
   */
  private static PairNameDeclList genStratPairNameDeclListFromTransfoSymbol(TomName stratName, 
                                                                            TomSymbol transformationSymbol) {
    PairNameDeclList result = `concPairNameDecl();
    PairNameDeclList transformationParameters = transformationSymbol.getPairNameDeclList();
    %match(transformationParameters) {
      concPairNameDecl(_*,PairNameDecl(name,
                                       GetSlotDecl[SlotName=slotname,Variable=bq,Expr=expr,OrgTrack=ot]),_*) -> {
        PairNameDecl pnd = `PairNameDecl(name,GetSlotDecl(stratName,slotname,bq,expr,ot));
        result = `concPairNameDecl(result*,pnd);
      }
    }
    return result;
  }

  /**
   * Generates a PairSlotAppl with an unknown type
   * @param name the name of the slot
   * @param line the line number for origin tracking
   * @param fileName the fileName for origin tracking
   * @return the generated PairSlotAppl
   */
  private Slot genPairSlotAppl(String name, int line, String fileName) {
    return genPairSlotAppl(name,line,fileName,getSymbolTable().TYPE_UNKNOWN);
  }

   /**
   * Generates a PairSlotAppl with a known type
   * @param name the name of the slot
   * @param line the line number for origin tracking
   * @param fileName the fileName for origin tracking
   * @param type the type of the Slot
   * @return the generated PairSlotAppl
   */
  private Slot genPairSlotAppl(String name, int line, String fileName, TomType type) {
    return `PairSlotAppl(
        Name(name),
        Variable(
          concOption(OriginTracking(Name(name),line,fileName)),
          Name(name),
          type, 
          concConstraint()
          )
        );
  }


  %strategy GenResolveElement(transformer:TransformerPlugin,resolveNameSet:HashSet,result:List,withNameSet:HashSet,toNameSet:HashSet) extends Identity() {
    visit Instruction {
      Resolve[Src=sname,SType=stypename,Target=tname,TType=ttypename,OrgTrack=ot] -> {
        //try to say that a resolve node has been found
        transformer.resolveFlag = true;

        String resolveStringName = transformer.RESOLVE_ELEMENT_PREFIX+`stypename+`ttypename;
        int line = `ot.getLine();
        String fileName = `ot.getFileName();
if(!resolveNameSet.contains(resolveStringName)) {
        resolveNameSet.add(resolveStringName);
        //used for generation of ResolveStratElement and ResolveStratBlock
        withNameSet.add(`stypename);
        toNameSet.add(`ttypename);

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
        Option resolveOrgTrack = `OriginTracking(resolveName,line,fileName);
        TomType stype = `Type(concTypeOption(),stypename,EmptyTargetLanguageType());
        //  /!\ Java dependant: ("String")
        TomType stringType = `Type(concTypeOption(),"String",EmptyTargetLanguageType());
        TomTypeList rtypes = `concTomType(stype, stringType);
        TomType tType = `Type(concTypeOption(),ttypename,EmptyTargetLanguageType());

        //generate PairNameDecl for parameters of Resolve elements
        PairNameDecl objectPNDecl = transformer.genResolvePairNameDecl(transformer.RESOLVE_ELEMENT_ATTRIBUTE_O, resolveStringName, line, fileName, tType);
        PairNameDecl namePNDecl = transformer.genResolvePairNameDecl(transformer.RESOLVE_ELEMENT_ATTRIBUTE_NAME, resolveStringName, line, fileName, tType);

        List<PairNameDecl> resolvePairNameDeclList = new LinkedList<PairNameDecl>();
        resolvePairNameDeclList.add(objectPNDecl);
        resolvePairNameDeclList.add(namePNDecl);

        Declaration isfsymDecl = `ResolveIsFsymDecl(
            resolveName,
            BQVariable(concOption(OriginTracking(Name("t"),line,fileName)),Name("t"),tType),
            OriginTracking(Name("is_fsym"),line,fileName)
);

        BQTermList makeArgs = `concBQTerm(
            BQVariable(concOption(OriginTracking(Name(transformer.RESOLVE_ELEMENT_ATTRIBUTE_O),line,fileName)),Name(transformer.RESOLVE_ELEMENT_ATTRIBUTE_O),stype),
            BQVariable(concOption(OriginTracking(Name(transformer.RESOLVE_ELEMENT_ATTRIBUTE_NAME),line,fileName)),Name(transformer.RESOLVE_ELEMENT_ATTRIBUTE_NAME),stringType)
            );

        Declaration makeDecl = `ResolveMakeDecl(
            resolveName,
            tType,
            makeArgs,
            OriginTracking(Name("make"),line,fileName)
            );

        List<Option> symbolOptions = new LinkedList<Option>();
        symbolOptions.add(`ot);
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
        transformer.getSymbolTable().putSymbol(resolveStringName,astSymbol);

        result.add(`TypeTermDecl(resolveName,resolveTTDecl,resolveOrgTrack));
        result.add(`SymbolDecl(Name(resolveStringName)));

        String extendsName = null;
        TomSymbol symb = transformer.getSymbolTable().getSymbolFromName(`ttypename);
        //retrieve classimpl
        OptionList ol = symb.getOptions();
        %match(ol) {
          concOption(_*,DeclarationToOption(ImplementDecl[Expr=code]),_*) -> {
            extendsName = `code.getCode();
          }
        }
        result.add(`ResolveClassDecl(Name(resolveStringName), stypename, ttypename, extendsName));
      }

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
      //Tracelink[] -> {
        //TODO: ?
      //}
    }
  }//GenResolveElement

  private Declaration genResolveGetSlotDecl(String astName, String slotName, int line, String fileName, TomType type) {
    return `ResolveGetSlotDecl(
        Name(astName),
        Name(slotName), 
        BQVariable(
          concOption(OriginTracking(Name("t"),line,fileName)),
          Name("t"),
          type 
          ),
        OriginTracking(Name("get_slot"),line,fileName)
        );
  }

  private PairNameDecl genPairNameDecl(String slotName, Declaration slotDecl) {
    return `PairNameDecl(Name(slotName), slotDecl);
  }

  private PairNameDecl genResolvePairNameDecl(String slotName, String astName, int line, String fileName, TomType type) {
    return `PairNameDecl(
        Name(slotName),
        ResolveGetSlotDecl(
          Name(astName),
          Name(slotName), 
          BQVariable(
            concOption(OriginTracking(Name("t"),line,fileName)),
            Name("t"),
            type 
            ),
          OriginTracking(Name("get_slot"),line,fileName)
          )
        );
  }

  private String checkAndNormalizeFileName(String fileName, String prefix,
      Option orgTrack) throws TomException {
    //first, normalize file name: spaces and separators
    fileName = fileName.trim();
    fileName = fileName.replace('\\',File.separatorChar);
    fileName = fileName.replace('/',File.separatorChar);

    File file = new File(fileName);
    //isFile(): what is a "normal" file? ("not a directory" + which other
    //condition?)
    if(file.exists() && file.isFile()) {
      try {
        return file.getCanonicalPath();
      } catch (IOException e) {
        System.out.println("IO Exception when computing "+prefix+" metamodel file "+fileName);
        e.printStackTrace();
      }
    } else {
      throw new TomException(TomMessage.mmFileNotFound, new Object[]{prefix,
          fileName, ((OriginTracking)orgTrack).getFileName(),
          Integer.valueOf(((OriginTracking)orgTrack).getLine()),
          ((OriginTracking)orgTrack).getFileName()});
    }
    //well, nothing better?
    return null;
  }

}//class
