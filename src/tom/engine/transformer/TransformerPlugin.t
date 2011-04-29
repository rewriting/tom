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
  %typeterm TransformerPlugin { implement { tom.engine.transformer.TransformerPlugin }}

  private static Logger logger = Logger.getLogger("tom.engine.transformer.TransformerPlugin");
  /** some output suffixes */
  public static final String TRANSFORMED_SUFFIX = ".tfix.transformed";
  private SymbolTable symbolTable;
  private int freshCounter = 0;

  public SymbolTable getSymbolTable() {
    return this.symbolTable;
  } 

  public void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  public TransformerPlugin() {
    super("TransformerPlugin");
  }

  public void run(Map informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    try {
      Code transformedTerm = (Code) this.process((Code)getWorkingTerm());
      // replace content of TransformationDecl by StrategyDecl
      // verbose
      TomMessage.info(logger,null,0,TomMessage.tomTransformingPhase,
          Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      setWorkingTerm(transformedTerm);      
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() +
            TRANSFORMED_SUFFIX, (Code)getWorkingTerm());
      }
    } catch (Exception e) {
      TomMessage.error(logger, getStreamManager().getInputFileName(), 0,
          TomMessage.exceptionMessage, e.getMessage());
      e.printStackTrace();
    }
  }

  private tom.library.sl.Visitable process(tom.library.sl.Visitable subject) {
    try {
      return `TopDownIdStopOnSuccess(ProcessTransformation(this)).visitLight(subject);
    } catch(VisitFailure e) {
      throw new TomRuntimeException("TransformerPlugin.expand: fail on " + subject);
    }
  }


  %strategy ProcessTransformation(transformer:TransformerPlugin) extends Identity() {
    /* 
     * Compilation of %transformation
     */
    //TransformationDecl(TName:TomName,Declarations:DeclarationList,OrgTrack:Option)
    visit Declaration {
      TransformationDecl(toName,declList,orgTrack) -> {
// orgTrack no longer needed ?
        %match(declList) {
          concDeclaration(_*,ResolveStratDecl[ResList=reslist,OriginTracking=ot],_*) -> {
            return buildResolveStrat(`toName, `reslist, `ot);
          }
          concDeclaration(_*,TransfoStratDecl[TSName=wName,Term=term,Instructions=instr,Options=options,OrgTrack=ot],_*) -> {
            return buildTransfoStrat(`toName, `wName, `term, `instr, `options, `ot);
          }
        }
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
  private static Declaration buildResolveStrat(TomName toname, ResolveStratBlockList
      rsbList, Option ot) {

    BQTerm subject = `BQVariable(concOption(),Name("tom__arg"),
        SymbolTable.TYPE_UNKNOWN);
    List<TomVisit> visitList = new LinkedList<TomVisit>();
    
    %match(rsbList) {
      concResolveStratBlock(_*,ResolveStratBlock(tname,rseList),_*) -> {
        TomType ttype = `Type(concTypeOption(),tname,EmptyTargetLanguageType());
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

            Constraint constraint = `AndConstraint(TrueConstraint(),
                MatchConstraint(pattern,subject,ttype));

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

    TomName rsname = `Name("tom__StratResolve");//problem when several transfo
    BQTerm extendsTerm = `BQAppl(concOption(ot),Name("Identity"),concBQTerm());
    TomVisitList astVisitList = `ASTFactory.makeTomVisitList(visitList);
    Option orgTrack = `OriginTracking(Name("Strategy"),
                                      ot.getLine(),
                                      ot.getFileName());
    Declaration resolve = `Strategy(rsname,extendsTerm,astVisitList,orgTrack);

    return `AbstractDecl(concDeclaration(resolve,SymbolDecl(rsname)));
  }
  
  //TODO
  private static Declaration buildTransfoStrat(TomName toname, TomName wname, TomTerm
      lhs, InstructionList instr, OptionList options, Option orgTrack) {

    TomName sname = `Name(wname.getString()+"To"+toname.getString());

    BQTerm extendsTerm = 
      `BQAppl(concOption(OriginTracking(wname,orgTrack.getLine(),
                orgTrack.getFileName())),Name("Identity"),concBQTerm());

    TomType vType = `Type(concTypeOption(),
                          wname.getString(),
                          EmptyTargetLanguageType());
    BQTerm subject = `BQVariable(concOption(),
                                 Name("tom__arg"),
                                 SymbolTable.TYPE_UNKNOWN);
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
    Declaration strategy = `Strategy(sname,extendsTerm,astVisitList,orgTrack);

    return `AbstractDecl(concDeclaration(strategy,SymbolDecl(sname)));
  }

//// FRom Desugarer

  /* 
   * replaces  _  by a fresh variable _* by a fresh varstar    
   */
/*  %strategy DesugarUnderscore(desugarer:DesugarerPlugin) extends Identity() {
    visit TomTerm {
      Variable[Options=optionList,AstName=EmptyName(),AstType=ty,Constraints=constr] -> {
        return `Variable(optionList,desugarer.getFreshVariable(),ty,constr);
      }
      VariableStar[Options=optionList,AstName=EmptyName(),AstType=ty,Constraints=constr] -> {
        return `VariableStar(optionList,desugarer.getFreshVariable(),ty,constr);
      }
    }
  }*/

}
