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
    
    try {
      // replace content of TransformationDecl by StrategyDecl
      transformedTerm = `TopDown(ProcessTransformation(this)).visitLight(transformedTerm);

      setWorkingTerm(transformedTerm);
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
      Transformation(transfoName,domain,declList,elemTransfoList,orgTrack) ->
       {
         return `AbstractDecl(processSubDecl(transformer,transfoName,domain,declList,elemTransfoList,orgTrack));
      }
    }
  }


  private static DeclarationList processSubDecl(TransformerPlugin transformer, TomName transfoName, TomTypeList domain, DeclarationList declList, ElementaryTransformationList elemTransfoList, Option orgTrack) {
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
    %match(declList) {
      concDeclaration(_*,ResolveStratDecl[TransfoName=name,ResList=reslist,OriginTracking=rot],_*) -> {
        resolveStratDecl = buildResolveStrat(transformer, transfoName, domain, bqlist, symbol, `name, `reslist, `rot);
      }
    }
    //add it to DeclarationList
    result.add(resolveStratDecl);
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
    Declaration symbol = `SymbolDecl(strategyName);
    Declaration refClass = `ReferenceClass((Name(transformer.REFCLASS_PREFIX+strName)));

    //build visitList
    List<ConstraintInstruction> ciList = new LinkedList<ConstraintInstruction>();
    TomType vType = null;
    //String vTypeStr = null;
    %match(riList) {
      concRuleInstruction(_*,RuleInstruction[TypeName=type,Term=term,Action=instr,Options=opts],_*) -> {
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
                           AbstractBlock(instr),
                           Nop())),
                         opts)
                );
          }
        }

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
  private static Declaration buildResolveStrat(TransformerPlugin transformer, 
                                               TomName toname, 
                                               TomTypeList domain, 
                                               List bqlist, 
                                               TomSymbol symbol, 
                                               String name, 
                                               ResolveStratBlockList rsbList, 
                                               Option ot) {
    List<TomVisit> visitList = new LinkedList<TomVisit>();

    %match(rsbList) {
      concResolveStratBlock(_*,ResolveStratBlock(tname,rseList),_*) -> {
        TomType ttype = `Type(concTypeOption(),tname,EmptyTargetLanguageType());
        //TODO: to check
        BQTerm subject = `BQVariable(concOption(),Name("tom__arg"),ttype);//SymbolTable.TYPE_UNKNOWN);
        List<ConstraintInstruction> ciList = new LinkedList<ConstraintInstruction>();
        %match(rseList) {
          // "wName" is no longer useful -> signature to change
          concResolveStratElement(_*,ResolveStratElement(wname,rot),_*) -> {

            TomTerm pattern = `Variable(concOption(rot),
                rot.getAstName(),
                Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()),
                concConstraint(
                  AliasTo(
                    Variable(
                      concOption(OriginTracking(
                          Name("tom__resolve"),
                          rot.getLine(),
                          rot.getFileName())),
                      Name("tom__resolve"),
                      Type(concTypeOption(),"unknown type",
                        EmptyTargetLanguageType()),
                      concConstraint()
                      )
                    )
                  )
                );

            //maybe something like that in the future
            //TomType matchType = (getOptionBooleanValue("newtyper")?SymbolTable.TYPE_UNKNOWN:ttype);
            Constraint constraint = `AndConstraint(TrueConstraint(),
                MatchConstraint(pattern,subject,ttype));

            // ??
            ciList.add(`ConstraintInstruction(constraint,
                  ResolveStratInstruction(ttype),concOption(rot)));
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
    options.add(`DeclarationToOption(makeDecl));

    //definition of the is_fsym method.
    //should not be ot, since the line number is different, but here, we use ot
    Option fsymOption = `OriginTracking(rsname,ot.getLine(),ot.getFileName());
    String varname = "t";
    BQTerm fsymVar = `BQVariable(concOption(fsymOption),Name(varname),strategyType);
    String code = ASTFactory.abstractCode("($"+varname+" instanceof "+stringRSname+")",varname);
    Declaration fsymDecl = `IsFsymDecl(rsname,fsymVar,Code(code),fsymOption);
    options.add(`DeclarationToOption(fsymDecl));

    //TomType transfoCodomain = TomBase.getSymbolCodomain(symbol);
    //types <-> `concTomType() (no param)
    TomTypeList transfoDomain = TomBase.getSymbolDomain(symbol);
    //slots <-> `concPairNameDecl() (no param)
    //PairNameDeclList paramDecl = symbol.getPairNameDeclList();
    PairNameDeclList paramDecl = genStratPairNameDeclListFromTransfoSymbol(rsname,symbol);

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
                                                                            TomSymbol symbol) {
    PairNameDeclList result = `concPairNameDecl();
    PairNameDeclList subject = symbol.getPairNameDeclList();
    %match(subject) {
      concPairNameDecl(_*,PairNameDecl[SlotName=name,SlotDecl=decl],_*) -> {
        Declaration slotDecl = `EmptyDeclaration();
        %match(decl) {
          GetSlotDecl[SlotName=slotname,Variable=bq,Expr=expr,OrgTrack=ot] -> {
            slotDecl = `GetSlotDecl(stratName,slotname,bq,expr,ot);
          }
        }
        PairNameDecl pnd = `PairNameDecl(name,slotDecl);
        result = `concPairNameDecl(result*,pnd);
      }
    }
    return result;
  }
  

}
