/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2010, INPL, INRIA
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

package tom.engine.desugarer;

import java.util.Map;
import java.util.logging.Level;
import java.util.Iterator;
import java.util.ArrayList;

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
 * The Desugarer plugin.
 * Perform syntax expansion and more.
 */
public class DesugarerPlugin extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }

  %typeterm DesugarerPlugin { implement { tom.engine.desugarer.DesugarerPlugin }}

  private SymbolTable symbolTable;
  private int freshCounter = 0;

  // FIXME : generate truly fresh variables
  private TomName getFreshVariable() {
    freshCounter++;
    return `Name("_f_r_e_s_h_v_a_r_" + freshCounter);
  }

  public SymbolTable getSymbolTable() {
    return this.symbolTable;
  } 

  public void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  public DesugarerPlugin() {
    super("DesugarerPlugin");
  }

  public void run(Map informationTracker) {
    // long startChrono = System.currentTimeMillis();
    try {
      setSymbolTable(getStreamManager().getSymbolTable());
      updateSymbolTable();
      Code syntaxExpandedTerm = (Code) getWorkingTerm();

      // replace underscores by fresh variables
      syntaxExpandedTerm = 
        `TopDown(DesugarUnderscore(this)).visitLight(syntaxExpandedTerm);

      //replace TermAppl and XmlAppl by RecordAppl
      syntaxExpandedTerm = 
        `TopDownIdStopOnSuccess(replaceTermApplTomSyntax(this)).visitLight(syntaxExpandedTerm);

      setWorkingTerm(syntaxExpandedTerm);      

    } catch (Exception e) {
      getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
          new Object[]{
          getClass().getName(), 
          getStreamManager().getInputFileName(), 
          e.getMessage()} );
      e.printStackTrace();
      return;
    }
  }

  /* replaces  _  by a fresh variable
     _* by a fresh varstar    */
  %strategy DesugarUnderscore(desugarer:DesugarerPlugin) extends Identity() {
    visit TomTerm {
      UnamedVariable[Option=opts,AstType=ty,Constraints=constr] -> {
        return `Variable(opts,desugarer.getFreshVariable(),ty,constr);
      }
      UnamedVariableStar[Option=opts,AstType=ty,Constraints=constr] -> {
        return `VariableStar(opts,desugarer.getFreshVariable(),ty,constr);
      }
    }
  }

  /**
   * updateSymbol is called after a first syntax expansion phase
   * this phase updates the symbolTable according to the typeTable
   * this is performed by recursively traversing each symbol
   * - default IsFsymDecl and MakeDecl are added
   */
  public void updateSymbolTable() {
    SymbolTable symbolTable = getStreamManager().getSymbolTable();
    Iterator<String> it = symbolTable.keySymbolIterator();
    Strategy typeStrategy = `TopDownIdStopOnSuccess(replaceTermApplTomSyntax(this));

    while(it.hasNext()) {
      String tomName = it.next();
      TomSymbol tomSymbol = getSymbolFromName(tomName);
      /*
       * add default IsFsymDecl unless it is a builtin type
       * add default IsFsymDecl and MakeDecl unless:
       *  - it is a builtin type
       *  - another option (if_sfsym, get_slot, etc) is already defined for this operator
       */
      if(!getStreamManager().getSymbolTable().isBuiltinType(TomBase.getTomType(TomBase.getSymbolCodomain(tomSymbol)))) {
        tomSymbol = addDefaultMake(tomSymbol);
        tomSymbol = addDefaultIsFsym(tomSymbol);
      }
      try {
        tomSymbol = `TopDownIdStopOnSuccess(replaceTermApplTomSyntax(this)).visitLight(tomSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        System.out.println("should not be there");
      }
      //System.out.println("symbol = " + tomSymbol);
      getStreamManager().getSymbolTable().putSymbol(tomName,tomSymbol);
    }
  }

  private TomSymbol addDefaultIsFsym(TomSymbol tomSymbol) {
    %match(tomSymbol) {
      Symbol[Option=(_*,DeclarationToOption(IsFsymDecl[]),_*)] -> {
        return tomSymbol;
      }

      Symbol(name,t@TypesToType(_,codom),l,concOption(X1*,origin@OriginTracking(_,line,file),X2*)) -> {
        Declaration isfsym = `IsFsymDecl(name,BQVariable(concOption(OriginTracking(Name("t"),line,file)),Name("t"),codom),FalseTL(),OriginTracking(Name("is_fsym"),line,file));
        return `Symbol(name,t,l,concOption(X1*,origin,DeclarationToOption(isfsym),X2*));
      }
    }
    return tomSymbol;
  }

  private TomSymbol addDefaultMake(TomSymbol tomSymbol) {
    %match(tomSymbol) {
      Symbol[Option=(_*,DeclarationToOption((MakeDecl|MakeEmptyList|MakeEmptyArray|MakeAddList|MakeAddArray|IsFsymDecl|GetImplementationDecl|GetSlotDecl|GetHeadDecl|GetTailDecl|IsEmptyDecl|GetElementDecl|GetSizeDecl)[]),_*)] -> {
        return tomSymbol;
      }
      Symbol(name,t@TypesToType(domain,codomain),l,concOption(X1*,origin@OriginTracking(_,line,file),X2*)) -> {
        //build variables for make
        BQTermList argsAST = `concBQTerm();
        int index = 0;
        for(TomType subtermType:(concTomType)`domain) {
          BQTerm variable = `BQVariable(concOption(),Name("t"+index),subtermType);
          argsAST = `concBQTerm(argsAST*,variable);
          index++;
        }
        BQTerm functionCall = `FunctionCall(name,codomain,argsAST);
        Declaration make = `MakeDecl(name,codomain,argsAST,BQTermToInstruction(functionCall),
            OriginTracking(Name("make"),line,file));
        return `Symbol(name,t,l,concOption(X1*,origin,DeclarationToOption(make),X2*));
      }
    }
    return tomSymbol;
  }

  /**
   * The 'replaceTermApplTomSyntax' phase replaces:
   * - each 'TermAppl' by its typed record form:
   *    placeholders are not removed
   *    slotName are attached to arguments
   */
  %strategy replaceTermApplTomSyntax(desugarer:DesugarerPlugin) extends Identity() {
    visit TomTerm {
      TermAppl[Option=option,NameList=nameList,Args=args,Constraints=constraints] -> {
        return desugarer.replaceTermAppl(`option,`nameList,`args,`constraints);
      }

      XMLAppl[Option=optionList,NameList=nameList,AttrList=list1,ChildList=list2,Constraints=constraints] -> {
        //System.out.println("replaceXML in:\n" + subject);
        return desugarer.replaceXMLAppl(`optionList, `nameList, `list1, `list2,`constraints);
      }
    }
  }

  /**
   * Replace 'TermAppl' by its 'RecordAppl' form
   * when no slotName exits, the position becomes the slotName
   */
  protected TomTerm replaceTermAppl(OptionList option, TomNameList nameList, TomList args, ConstraintList constraints) {
    TomName headName = nameList.getHeadconcTomName();
    if(headName instanceof AntiName) {
      headName = ((AntiName)headName).getName();
    }
    String opName = headName.getString();
    TomSymbol tomSymbol = getSymbolFromName(opName);


    //System.out.println("replaceTermAppl: " + tomSymbol);
    //System.out.println("  nameList = " + nameList);

    if(tomSymbol==null && args.isEmptyconcTomTerm()) {
      return `RecordAppl(option,nameList,concSlot(),constraints);
    }

    SlotList slotList = `concSlot();
    Strategy typeStrategy = `TopDownIdStopOnSuccess(replaceTermApplTomSyntax(this));
    if(opName.equals("") || tomSymbol==null || TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol)) {
      for(TomTerm arg:(concTomTerm)args) {
        try {
          TomTerm subterm = typeStrategy.visitLight(arg);
          TomName slotName = `EmptyName();
          /*
           * we cannot optimize when subterm.isUnamedVariable
           * since it can be constrained
           */	  
          slotList = `concSlot(slotList*,PairSlotAppl(slotName,subterm));
        } catch(tom.library.sl.VisitFailure e) {
          System.out.println("should not be there");
        }
      }
    } else {
      PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
      for(TomTerm arg:(concTomTerm)args) {
        try{
          TomTerm subterm = typeStrategy.visitLight(arg);
          TomName slotName = pairNameDeclList.getHeadconcPairNameDecl().getSlotName();
          /*
           * we cannot optimize when subterm.isUnamedVariable
           * since it can be constrained
           */	  
          slotList = `concSlot(slotList*,PairSlotAppl(slotName,subterm));
          pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
        } catch(tom.library.sl.VisitFailure e) {
          System.out.println("should not be there");
        }
      }
      %match(pairNameDeclList){
        !concPairNameDecl() -> { 
          throw new TomRuntimeException("The symbol '"
                +
                `pairNameDeclList.getHeadconcPairNameDecl().getSlotName().getString() + "' has a bad arity"); }
      }
    }

    return `RecordAppl(option,nameList,slotList,constraints);
  }

  /**
   * Replace 'XMLAppl' by its 'RecordAppl' form
   * when no slotName exits, the position becomes the slotName
   */
  protected TomTerm replaceXMLAppl(OptionList optionList, TomNameList nameList,
      TomList attrList, TomList childList, ConstraintList constraints) {
    boolean implicitAttribute = TomBase.hasImplicitXMLAttribut(optionList);
    boolean implicitChild     = TomBase.hasImplicitXMLChild(optionList);

    TomList newAttrList  = `concTomTerm();
    TomList newChildList = `concTomTerm();
    TomTerm star = `UnamedVariableStar(convertOriginTracking("_*",optionList),symbolTable().TYPE_UNKNOWN,concConstraint());
    if(implicitAttribute) { newAttrList  = `concTomTerm(star,newAttrList*); }
    if(implicitChild)     { newChildList = `concTomTerm(star,newChildList*); }

    /*
     * the list of attributes should not be typed before the sort
     * the sortAttribute is extended to compare RecordAppl
     */

    //System.out.println("attrList = " + attrList);
    attrList = sortAttributeList(attrList);
    //System.out.println("sorted attrList = " + attrList);

    /*
     * Attributes: go from implicit notation to explicit notation
     */
    Strategy typeStrategy = `TopDownIdStopOnSuccess(replaceTermApplTomSyntax(this));
    for(TomTerm attr:(concTomTerm)attrList) {
      try {
        TomTerm newPattern = typeStrategy.visitLight(attr);
        newAttrList = `concTomTerm(newPattern,newAttrList*);
        if(implicitAttribute) {
          newAttrList = `concTomTerm(star,newAttrList*);
        }
      } catch(tom.library.sl.VisitFailure e) {
        System.out.println("should not be there");
      }
    }
    newAttrList = newAttrList.reverse();

    /*
     * Childs: go from implicit notation to explicit notation
     */
    for(TomTerm child:(concTomTerm)childList) {
      try {
        TomTerm newPattern = typeStrategy.visitLight(child);
        newChildList = `concTomTerm(newPattern,newChildList*);
        if(implicitChild) {
          if(newPattern.isVariableStar()) {
            // remove the previously inserted pattern
            newChildList = newChildList.getTailconcTomTerm();
            if(newChildList.getHeadconcTomTerm().isUnamedVariableStar()) {
              // remove the previously inserted star
              newChildList = newChildList.getTailconcTomTerm();
            }
            // re-insert the pattern
            newChildList = `concTomTerm(newPattern,newChildList*);
          } else {
            newChildList = `concTomTerm(star,newChildList*);
          }
        }
      } catch(tom.library.sl.VisitFailure e) {
        System.out.println("should not be there");
      }
    }
    newChildList = newChildList.reverse();

    /*
     * encode the name and put it into the table of symbols
     */
    TomNameList newNameList = `concTomName();
matchBlock: 
    {
      %match(nameList) {
        concTomName(Name("_")) -> {
          break matchBlock;
        }

        concTomName(_*,Name(name),_*) -> {
          newNameList = `concTomName(newNameList*,Name(ASTFactory.encodeXMLString(symbolTable(),name)));
        }
      }
    }

    /*
     * a single "_" is converted into an UnamedVariable to match
     * any XML node
     */
    TomTerm xmlHead;

    if(newNameList.isEmptyconcTomName()) {
      xmlHead = `UnamedVariable(concOption(),symbolTable().TYPE_UNKNOWN,concConstraint());
    } else {
      xmlHead = `TermAppl(convertOriginTracking(newNameList.getHeadconcTomName().getString(),optionList),newNameList,concTomTerm(),concConstraint());
    }
    try {
      SlotList newArgs = `concSlot(
          PairSlotAppl(Name(Constants.SLOT_NAME),
            typeStrategy.visitLight(xmlHead)),
          PairSlotAppl(Name(Constants.SLOT_ATTRLIST),
            typeStrategy.visitLight(TermAppl(convertOriginTracking("CONC_TNODE",optionList),concTomName(Name(Constants.CONC_TNODE)), newAttrList,concConstraint()))),
          PairSlotAppl(Name(Constants.SLOT_CHILDLIST),
            typeStrategy.visitLight(TermAppl(convertOriginTracking("CONC_TNODE",optionList),concTomName(Name(Constants.CONC_TNODE)), newChildList,concConstraint()))));

      TomTerm result = `RecordAppl(optionList,concTomName(Name(Constants.ELEMENT_NODE)),newArgs,constraints);

      //System.out.println("replaceXML out:\n" + result);
      return result;
    } catch(tom.library.sl.VisitFailure e) {
      //must never be executed
      return star;
    }
  }

  /* auxilliary methods */

  private TomList sortAttributeList(TomList attrList) {
    %match(attrList) {
      concTomTerm() -> { return attrList; }
      concTomTerm(X1*,e1,X2*,e2,X3*) -> {
        %match(e1, e2) {
          TermAppl[Args=concTomTerm(RecordAppl[NameList=(Name(name1))],_*)],
            TermAppl[Args=concTomTerm(RecordAppl[NameList=(Name(name2))],_*)] -> {
              if(`name1.compareTo(`name2) > 0) {
                return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
              }
            }

          TermAppl[Args=concTomTerm(TermAppl[NameList=(Name(name1))],_*)],
            TermAppl[Args=concTomTerm(TermAppl[NameList=(Name(name2))],_*)] -> {
              if(`name1.compareTo(`name2) > 0) {
                return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
              }
            }

          RecordAppl[Slots=concSlot(PairSlotAppl(slotName,RecordAppl[NameList=(Name(name1))]),_*)],
            RecordAppl[Slots=concSlot(PairSlotAppl(slotName,RecordAppl[NameList=(Name(name2))]),_*)] -> {
              if(`name1.compareTo(`name2) > 0) {
                return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
              }
            }

          RecordAppl[Slots=concSlot(PairSlotAppl(slotName,TermAppl[NameList=(Name(name1))]),_*)],
            RecordAppl[Slots=concSlot(PairSlotAppl(slotName,TermAppl[NameList=(Name(name2))]),_*)] -> {
              if(`name1.compareTo(`name2) > 0) {
                return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
              }
            }
        }
      }
    }
    return attrList;
  }

  private OptionList convertOriginTracking(String name,OptionList optionList) {
    Option originTracking = TomBase.findOriginTracking(optionList);
    %match(originTracking) {
      OriginTracking[Line=line, FileName=fileName] -> {
        return `concOption(OriginTracking(Name(name),line,fileName));
      }
    }
    System.out.println("Warning: no OriginTracking information");
    return `concOption();
  }

}
