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

  /*private tom.library.sl.Visitable process(tom.library.sl.Visitable workingTerm) {
    try {
      //return `TopDownIdStopOnSuccess(ProcessTransformation(this)).visitLight(subject);
      return `TopDown(ProcessTransformation(this)).visitLight(workingTerm);
    } catch(VisitFailure e) {
      throw new TomRuntimeException("TransformerPlugin.process: fail on " + workingTerm);
    }
  }*/


  %strategy ProcessTransformation(transformer:TransformerPlugin) extends Identity() {
    /* 
     * Compilation of %transformation
     */
    visit Declaration {
      TransformationDecl(toName,declList,orgTrack) -> {
        // orgTrack no longer needed ?
        return `AbstractDecl(processSubDecl(transformer,toName,declList));
      }
    }
  }

  private static DeclarationList processSubDecl(TransformerPlugin transformer, TomName toname, DeclarationList declList) {
    List bqlist = new LinkedList<BQTerm>();
    DeclarationList result = null;
    try {
      result = `TopDown(ProcessSubTransformation(transformer,toname,bqlist)).visitLight(declList);
    } catch(VisitFailure e) {
      throw new TomRuntimeException("TransformerPlugin.processSubDecl: fail on " + declList);
    }

    //let's change the transformation MakeDecl
    String stringName = toname.getString();
    TomSymbol symbol = transformer.getSymbolTable().getSymbolFromName(stringName);
    BQTerm composite = makeComposedStrategy(bqlist,declList);
    try {
      //TomSymbol newSymbol = `TopDown(ReplaceMakeDecl(bqlist,declList)).visitLight(symbol);
      TomSymbol newSymbol = `TopDown(ReplaceMakeDecl(composite)).visitLight(symbol);
      transformer.getSymbolTable().putSymbol(stringName,newSymbol);
    } catch (VisitFailure e) {
      throw new TomRuntimeException("TransformerPlugin.processSubDecl: fail on " + symbol);
    }

    ///2makeTransformationDeclMake(toname,bqlist,declList);
    //1test: will be reused in another way
    //Declaration transformerBQTerm = makeComposedStrategy(bqlist,declList);
    //return `concDeclaration(result*,transformerBQTerm);
    return result;
  }//processSubDecl

  //2
  /*private static void makeTransformationDeclMake(TomName tname, List bqlist, DeclarationList declList) {
    TomSymbol symbol = getSymbolTable().getSymbolFromName(tname.getString());
    try {
      TomSymbol newSymbol = `TopDown(ReplaceMakeDecl(bqlist,declList)).visitLight(symbol);
      getSymbolTable().putSymbol(tname,newSymbol);
    } catch (VisitFailure e) {
      throw new TomRuntimeException("TransformerPlugin.makeTransformationDeclMake: fail on " + symbol);
    }
  }*/

  //%strategy ReplaceMakeDecl(bqlist:List,declList:DeclarationList) extends Identity() {
  %strategy ReplaceMakeDecl(composite:BQTerm) extends Identity() {
    visit Declaration {
      MakeDecl[AstName=name,AstType=type,Args=args,OrgTrack=ot] -> {
        //BQTerm composite = makeComposedStrategy(bqlist,declList);
        return `MakeDecl(name,type,args,BQTermToInstruction(composite),ot);
      }
    }
  }

  //private static Declaration makeComposedStrategy(List<BQTerm> bqlist, DeclarationList declList) {
  private static BQTerm makeComposedStrategy(List<BQTerm> bqlist, DeclarationList declList) {
    BQTermList bql = removeDuplicate(ASTFactory.makeBQTermList(bqlist));
    /*concOption() for the moment, to change*/
    BQTerm transfos = `Composite(CompositeBQTerm(BQAppl(
            concOption(),
            Name("Sequence"),
            bql)
          ));
    //add condition: call it only if resolve is needed
    BQTermList res = `concBQTerm(transfos, makeResolveBQTerm(declList));
    //Declaration transformerBQTerm = `BQTermToDeclaration(
    BQTerm transformerBQTerm = `Composite(CompositeBQTerm(BQAppl(
            concOption(),
            Name("Sequence"),
            res)
          ));
    return transformerBQTerm;
  }//makeComposedStrategy

  private static BQTerm makeResolveBQTerm(DeclarationList declList) {
    Option option = `noOption();
    String stringRSname = "";
    %match(declList) {
      concDeclaration(_*, ResolveStratDecl[TransfoName=name,OriginTracking=ot] ,_*) -> {
        option = `ot;
        stringRSname = "tom__StratResolve_"+`name;
      }
    }
    BQTerm bqtrans = `Composite(CompositeBQTerm(BQAppl(
            concOption(option), Name("TopDown"), concBQTerm(Composite(
                CompositeBQTerm(
                  BQAppl(concOption(option),Name(stringRSname),concBQTerm())
                  ))))));
    return bqtrans;
  }//makeResolveBQTerm

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

  //%strategy ProcessSubTransformation(transformer:TransformerPlugin,toName:TomName,bqlist:List,result:List) extends Identity() {
  %strategy ProcessSubTransformation(transformer:TransformerPlugin,toName:TomName,bqlist:List) extends Identity() {
    visit Declaration {
      /*ResolveDecl[] -> {*/
        /* ResolveTypeTermDecl => nope , SymbolDecl, /ResResolveClassDecl*/

      /*}*/

      /* the order is important:
         - the ReferenceDecl rule has to be before the TransfoStratDecl one
         - the ResolveStratDecl one has to be at the last position -> why?
       */
      ReferenceDecl[Info=info,RName=rname,Declarations=decl,OrgTrack=ot] -> {
        /*+ ReferenceClassDecl*/
        //need to gener a ReferenceClassDecl (not yet in adt)
        return buildTransfoStratFromReference(transformer, toName, bqlist, `info, `rname, `decl, `ot);
        //DeclarationList stratsym = buildTransfoStratFromReference(transformer, toName, bqlist, `info, `rname, `decl, `ot);
        //return `AbstractDecl(stratsym*,..);
      }
      TransfoStratDecl[Info=info,TSName=wName,Term=term,Instructions=instr,Options=options,OrgTrack=ot] -> {
        return buildTransfoStrat(transformer, toName, bqlist, `info, `wName, `term, `instr, `options, `ot);
        //buildTransfoStrat(transformer, toName, bqlist, result, `info, `wName, `term, `instr, `options, `ot);
      }
      ResolveStratDecl[TransfoName=name,ResList=reslist,OriginTracking=ot] -> {
        return buildResolveStrat(transformer, toName, bqlist, `name, `reslist, `ot);
        //buildResolveStrat(transformer, toName, bqlist, result, `name, `reslist, `ot);
      }

    }
  }

  //TODO
  /*
Rappel :
ResolveStratElement = ResolveStratElement(WithName:String, ResolveOrgTrack:Option)
ResolveStratElementList = concResolveStratElement(ResolveStratElement*)
ResolveStratBlock = ResolveStratBlock(ToName:String, resolveStratElementList:ResolveStratElementList)
ResolveStratBlockList = concResolveStratBlock(ResolveStratBlock*)
   */
  //private static void buildResolveStrat(TransformerPlugin transformer, TomName toname, List bqlist, List result, String name, ResolveStratBlockList rsbList, Option ot) {
  private static Declaration buildResolveStrat(TransformerPlugin transformer, TomName toname, List bqlist, String name, ResolveStratBlockList rsbList, Option ot) {

    List<TomVisit> visitList = new LinkedList<TomVisit>();

    %match(rsbList) {
      concResolveStratBlock(_*,ResolveStratBlock(tname,rseList),_*) -> {

        TomType ttype = `Type(concTypeOption(),tname,EmptyTargetLanguageType());
        //WARNING: is this correct?
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
    //problem when several transfo, the name will be composed of resolve and
    //transformation name
    //String stringRSname = "tom__StratResolve";
    String stringRSname = "tom__StratResolve_"+name;//better?
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
    
    List<Option> options = new LinkedList<Option>();
    options.add(ot);

    //definition of make.
    //change this TLType("") -> EmptyTargetLanguageTyupe(), be careful during
    //code generation of IsFsymDecl
    TomType strategyType = `Type(concTypeOption(),"Strategy",TLType(""));//EmptyTargetLanguageType());
    /* Arguments of the strategy. For the moment, nothing, but it will be
       necessary */
    BQTermList makeArgs = `concBQTerm();
    //to be uncommented when arguments will be used
    String makeTlCode = "new "+stringRSname+"(";
    /*int index = 0;
    TomTypeList makeTypes = `concTomType(); //for the moment //types;//keep a copy of types
    while(!makeTypes.isEmptyconcTomType()) {
      String argName = "t"+index;
      if (index>0) {//if many parameters
        makeTlCode = makeTlCode.concat(",");
      }
      makeTlCode += argName;

      BQTerm arg = `BQVariable(concOption(),Name(argName),makeTypes.getHeadconcTomType());
      makeArgs = `concBQTerm(makeArgs*,arg);

      makeTypes = makeTypes.getTailconcTomType();
      index++;
    }*/
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

    //types <-> `concTomType() (no param)
    //slots <-> `concPairNameDecl() (no param)
    TomSymbol astSymbol = ASTFactory.makeSymbol(stringRSname,
        strategyType, `concTomType(), `concPairNameDecl(), options);
    transformer.getSymbolTable().putSymbol(stringRSname,astSymbol);

    //build here the part of BQTermComposite transformer
    BQTerm bqtrans = `Composite(CompositeBQTerm(BQAppl(
            concOption(ot), Name("TopDown"), concBQTerm(Composite(
                CompositeBQTerm(
                  BQAppl(concOption(ot),Name(stringRSname),concBQTerm())
                  ))))));
    bqlist.add(bqtrans);
    //result.add(`AbstractDecl(concDeclaration(resolve,SymbolDecl(rsname))));
    return `AbstractDecl(concDeclaration(resolve,SymbolDecl(rsname)));
  }
 
  //TODO
  //incohérence : pourquoi rname est une chaîne ?
  //private static void buildTransfoStratFromReference(TransformerPlugin transformer, TomName tname, List bqlist, List result, TransfoStratInfo info, String rname, DeclarationList declList, Option orgTrack) {
  private static Declaration buildTransfoStratFromReference(TransformerPlugin transformer, TomName tname, List bqlist, TransfoStratInfo info, String rname, DeclarationList declList, Option orgTrack) {
    //build here the part of CompositeBQTerm transformer
    Option opt = info.getOrgTrack();
    BQTerm bqtrans = `Composite(CompositeBQTerm(BQAppl(
            concOption(opt), Name(info.getTraversal()), concBQTerm(Composite(
                CompositeBQTerm(
                  BQAppl(concOption(opt),Name(info.getName()),concBQTerm())
                  ))))));
    bqlist.add(bqtrans);

    //String stringStratName = rname+"To"+((Name)tname).getString();
    String stringStratName = info.getName();
    TomName sname = `Name(stringStratName);

    //pas bon, corriger l'originTracking
    BQTerm extendsTerm = `BQAppl(concOption(OriginTracking(tname,orgTrack.getLine(),orgTrack.getFileName())),Name("Identity"),concBQTerm());

    /*
     * which AlgebraicType should it be? (-> visit T )
     * let's try with a simple case
     */
    //retrieve the first TransfoStratDecl
    Declaration tsd = declList.getHeadconcDeclaration();
    String typeString ="";
    //retrieve the name
    %match(tsd) {
      TransfoStratDecl[TSName=Name(n)] -> { typeString = `n; }
    }

    /* correct this to have the right type */
    TomType vType = `Type(concTypeOption(),
                          typeString,
                          EmptyTargetLanguageType());

    //return buildTransfoStrat(`toName, `wName, `term, `instr, `options, `ot);
    ///ConstraintInstructionList ciList = `concConstraintInstruction();
    List<ConstraintInstruction> ciList = new LinkedList<ConstraintInstruction>();
    //add a check: each TSName has to be the same (or a subtype)
    %match(declList) {
      concDeclaration(_*,TransfoStratDecl[TSName=wName,Term=term,Instructions=instr,Options=options,OrgTrack=ot],_*) -> {
        
        BQTerm subject = `BQVariable(concOption(),
                                 Name("tom__arg"),
                                 vType); //SymbolTable.TYPE_UNKNOWN);//vType: change this
//getTypeFromName
        Constraint constraint = `AndConstraint(TrueConstraint(),
                                           MatchConstraint(term,subject,vType));//matchType
       //avec :
       //TomType matchType = (getOptionBooleanValue("newtyper")?SymbolTable.TYPE_UNKNOWN:rhsType);

        //ciList = `concConstraintInstruction(
        ciList.add(`ConstraintInstruction(
                         constraint,
                         RawAction(If(
                           TrueTL(),
                           AbstractBlock(instr),
                           Nop())),
                         options)
          );

      }
    }
    
    LinkedList<Option> list = new LinkedList<Option>();
    list.add(orgTrack);
    OptionList options = ASTFactory.makeOptionList(list);

    ConstraintInstructionList ciList2 = ASTFactory.makeConstraintInstructionList(ciList);
    List<TomVisit> visitList = new LinkedList<TomVisit>();
    visitList.add(`VisitTerm(vType,ciList2,options));
    //TomVisitList astVisitList = `concTomVisitList(tomVisit);//pb avec l'option
    TomVisitList astVisitList = ASTFactory.makeTomVisitList(visitList);

    List<Option> optionList = new LinkedList<Option>();
    optionList.add(orgTrack);
    //args?; //ceux de la transformation => manque qqch dans les nœuds
    //problem here: should be EmptyTargetLanguageType()
    TomType strategyType = `Type(concTypeOption(),"Strategy",TLType(""));//EmptyTargetLanguageType());
    BQTermList makeArgs = `concBQTerm();
    String makeTlCode = "new "+stringStratName+"(";
    //insert code linked to parameters here
    makeTlCode += ")";
    Option makeOption = `OriginTracking(sname,orgTrack.getLine(),orgTrack.getFileName());
    Declaration makeDecl = `MakeDecl(sname, strategyType, makeArgs,
        CodeToInstruction(TargetLanguageToCode(ITL(makeTlCode))), makeOption);
    optionList.add(`DeclarationToOption(makeDecl));

    Option fsymOption = `OriginTracking(sname,orgTrack.getLine(),orgTrack.getFileName());
    String varname = "t";
    BQTerm fsymVar = `BQVariable(concOption(fsymOption),Name(varname),strategyType);
    String code = ASTFactory.abstractCode("($"+varname+" instanceof "+stringStratName+")",varname);
    Declaration fsymDecl = `IsFsymDecl(sname,fsymVar,Code(code),fsymOption);
    optionList.add(`DeclarationToOption(fsymDecl));

    TomSymbol astSymbol = ASTFactory.makeSymbol(stringStratName,
        strategyType, `concTomType(), `concPairNameDecl(), optionList);
    transformer.getSymbolTable().putSymbol(stringStratName,astSymbol);

    Declaration strategy = `Strategy(sname,extendsTerm,astVisitList,orgTrack);
    //result.add(`AbstractDecl(concDeclaration(strategy,SymbolDecl(sname))));
    return `AbstractDecl(concDeclaration(strategy,SymbolDecl(sname)));
  }

  //TODO
  //private static void buildTransfoStrat(TransformerPlugin transformer, TomName toname, List bqlist, List result, TransfoStratInfo info, TomName wname, TomTerm lhs, InstructionList instr, OptionList options, Option orgTrack) {
  private static Declaration buildTransfoStrat(TransformerPlugin transformer, TomName toname, List bqlist, TransfoStratInfo info, TomName wname, TomTerm lhs, InstructionList instr, OptionList options, Option orgTrack) {

    Option opt = info.getOrgTrack();
    BQTerm bqtrans = `Composite(CompositeBQTerm(BQAppl(
            concOption(opt), Name(info.getTraversal()), concBQTerm(Composite(
                CompositeBQTerm(
                  BQAppl(concOption(opt),Name(info.getName()),concBQTerm())
                  ))))));
    bqlist.add(bqtrans);

    //naze, à changer en utilisant le nommage toto#TopDown
    //String stringStratName = wname.getString()+"To"+((Name)toname).getString();
    String stringStratName = info.getName();
    TomName sname = `Name(stringStratName);

    BQTerm extendsTerm = 
      `BQAppl(concOption(OriginTracking(wname,orgTrack.getLine(),
                orgTrack.getFileName())),Name("Identity"),concBQTerm());

    TomType vType = `Type(concTypeOption(),
                          wname.getString(),
                          EmptyTargetLanguageType());
    BQTerm subject = `BQVariable(concOption(),
                                 Name("tom__arg"),
                                 vType);//?
                                 //SymbolTable.TYPE_UNKNOWN);
    Constraint constraint = `AndConstraint(TrueConstraint(),
                                           MatchConstraint(lhs,subject,vType));
    ConstraintInstructionList ciList = `concConstraintInstruction(
                                          ConstraintInstruction(
                                            constraint,
                                            RawAction(If(
                                              TrueTL(),
                                              AbstractBlock(instr),
                                              Nop())),
                                            options));
    TomVisitList astVisitList = `concTomVisit(VisitTerm(vType,ciList,options));

    List<Option> optionList = new LinkedList<Option>();
    optionList.add(orgTrack);
    //args?; //ceux de la transformation => manque qqch dans les nœuds
    //problem here: should be EmptyTargetLanguageType()
    TomType strategyType = `Type(concTypeOption(),"Strategy",TLType(""));//EmptyTargetLanguageType());
    BQTermList makeArgs = `concBQTerm();
    String makeTlCode = "new "+stringStratName+"(";
    //insert code linked to parameters here
    makeTlCode += ")";
    Option makeOption = `OriginTracking(sname,orgTrack.getLine(),orgTrack.getFileName());
    Declaration makeDecl = `MakeDecl(sname, strategyType, makeArgs,
        CodeToInstruction(TargetLanguageToCode(ITL(makeTlCode))), makeOption);
    optionList.add(`DeclarationToOption(makeDecl));

    Option fsymOption = `OriginTracking(sname,orgTrack.getLine(),orgTrack.getFileName());
    String varname = "t";
    BQTerm fsymVar = `BQVariable(concOption(fsymOption),Name(varname),strategyType);
    String code = ASTFactory.abstractCode("($"+varname+" instanceof "+stringStratName+")",varname);
    Declaration fsymDecl = `IsFsymDecl(sname,fsymVar,Code(code),fsymOption);
    optionList.add(`DeclarationToOption(fsymDecl));

    TomSymbol astSymbol = ASTFactory.makeSymbol(stringStratName,
        strategyType, `concTomType(), `concPairNameDecl(), optionList);
    transformer.getSymbolTable().putSymbol(stringStratName,astSymbol);

    Declaration strategy = `Strategy(sname,extendsTerm,astVisitList,orgTrack);
    //result.add(`AbstractDecl(concDeclaration(strategy,SymbolDecl(sname))));
    return `AbstractDecl(concDeclaration(strategy,SymbolDecl(sname)));
  }

         //TODO
         //build the BQTerm representing the composed transformation strategy
         /*
           we want somthing like this:
           BQTermToCode(
             Composite(BQTermComposite(
               BQAppl(..,Name("Sequence"),concBQTerm(
                 Composite(Composite(BQTermComposite(
                   BQAppl(..,Name(optional_traversal),concBQTerm(
                     Composite(Composite(BQTermComposite(
                       BQAppl(..,Name(name),concBQTerm())
                     )))
                   ))
                 ))),
                 ..,
                 ..))
             ))
           )
          
          */

}
