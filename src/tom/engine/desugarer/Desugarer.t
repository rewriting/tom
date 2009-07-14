/*
 * 
 * TOM - To One Matching Compiler
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
import tom.engine.adt.tomtype.types.tomtypelist.concTomType;
import tom.engine.adt.tomterm.types.tomlist.concTomTerm;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.engine.tools.SymbolTable;
import tom.engine.typer.KernelTyper;
import tom.engine.xml.Constants;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;

import tom.library.sl.*;

/**
 * The Desugarer plugin.
 * Perform syntax expansion and more.
 */
public class Desugarer extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }

  /** the kernel typer acting at very low level */
  private KernelTyper kernelTyper;

  public Desugarer() {
    super("Desugarer");
    kernelTyper = new KernelTyper();
  }
  public void run(Map informationTracker) {
     long startChrono = System.currentTimeMillis();
     try {
       //getStreamManager().getSymbolTable().dump();
       kernelTyper.setSymbolTable(getStreamManager().getSymbolTable());
       TomTerm syntaxExpandedTerm = `TopDownIdStopOnSuccess(typeTermApplTomSyntax(this)).visitLight((TomTerm)getWorkingTerm());

       // underscores by fresh variables
       syntaxExpandedTerm = 
         `TopDown(DesugarUnderscore(this)).visitLight(syntaxExpandedTerm);


       // WARNING side effect on the symbol table
       updateSymbolTable();
       //getStreamManager().getSymbolTable().dump();

       // expands known (ie != "unknown type") TypeAlone in the AST into Type
       // TODO: remove this phase ?
       syntaxExpandedTerm = expandType(syntaxExpandedTerm);

      setWorkingTerm(syntaxExpandedTerm);      
    } catch (Exception e) {
      getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
          new Object[]{getClass().getName(), getStreamManager().getInputFileName(), e.getMessage()} );
      e.printStackTrace();
      return;
    }
  }

  // FIXME : generate truly fresh variables
  private int freshCounter = 0;
  private TomName getFreshVariable() {
    freshCounter++;
    return `Name("_t_h_i_s_i_s_a_f_r_e_s_h_v_a_r_" + freshCounter);
  }

  /* replaces  _  by a fresh variable
               _* by a fresh varstar    */
  %strategy DesugarUnderscore(desugarer:Desugarer) extends Identity() {
    visit TomTerm {
       UnamedVariable[Option=opts,AstType=ty,Constraints=constr] -> {
         return `Variable(opts,desugarer.getFreshVariable(),ty,constr);
       }
       UnamedVariableStar[Option=opts,AstType=ty,Constraints=constr] -> {
         return `VariableStar(opts,desugarer.getFreshVariable(),ty,constr);
       }
    }
  }

  %typeterm Desugarer { implement { tom.engine.desugarer.Desugarer }}

  /*
   * The 'typeTermApplTomSyntax' phase replaces:
   * - each 'TermAppl' by its typed record form:
   *    placeholders are not removed
   *    slotName are attached to arguments
   */
  %strategy typeTermApplTomSyntax(desugarer:Desugarer) extends Identity() {
    visit TomTerm {
      TermAppl[Option=option,NameList=nameList,Args=args,Constraints=constraints] -> {
        return desugarer.typeTermAppl(`option,`nameList,`args,`constraints);
      }

      XMLAppl[Option=optionList,NameList=nameList,AttrList=list1,ChildList=list2,Constraints=constraints] -> {
        //System.out.println("typeXML in:\n" + subject);
        return desugarer.typeXMLAppl(`optionList, `nameList, `list1, `list2,`constraints);
      }
    }
  }

  /*
   * updateSymbol is called after a first syntax expansion phase
   * this phase updates the symbolTable according to the typeTable
   * this is performed by recursively traversing each symbol
   * - backquote are typed
   * - each TomTypeAlone is replaced by the corresponding TomType
   * - default IsFsymDecl and MakeDecl are added
   */
  public void updateSymbolTable() {
    SymbolTable symbolTable = getStreamManager().getSymbolTable();
    Iterator<String> it = symbolTable.keySymbolIterator();
    Strategy typeStrategy = `TopDownIdStopOnSuccess(typeTermApplTomSyntax(this));

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

        /* TODO : WHY THIS HERE ? */

        // implicit to explicit  
        tomSymbol = `TopDownIdStopOnSuccess(typeTermApplTomSyntax(this)).visitLight(tomSymbol);
        // Replace a TomTypeAlone by its expanded form (TomType)
        tomSymbol = expandType(`TomSymbolToTomTerm(tomSymbol)).getAstSymbol();
        // assign a type to variables
        tomSymbol = ((TomTerm) kernelTyper.typeVariable(`EmptyType(),`TomSymbolToTomTerm(tomSymbol))).getAstSymbol();
        // replace BackquoteAppl by FunctionCall, BuildTerm etc.
        tomSymbol = `TopDownIdStopOnSuccess(typeBackQuoteAppl(this)).visitLight(`tomSymbol);


      } catch(tom.library.sl.VisitFailure e) {
        System.out.println("should not be there");
      }
      //System.out.println("symbol = " + tomSymbol);
      getStreamManager().getSymbolTable().putSymbol(tomName,tomSymbol);
    }
  }

    /*
     * transform a BackQuoteAppl into its compiled form
     */
    %strategy typeBackQuoteAppl(desugarer:Desugarer) extends Identity() {
      visit TomTerm {
        BackQuoteAppl[Option=optionList,AstName=name@Name(tomName),Args=l] -> {
          TomSymbol tomSymbol = desugarer.getSymbolFromName(`tomName);
          TomList args  = `TopDownIdStopOnSuccess(typeBackQuoteAppl(desugarer)).visitLight(`l);

          //System.out.println("BackQuoteTerm: " + `tomName);
          //System.out.println("tomSymbol: " + tomSymbol);
          if(TomBase.hasConstant(`optionList)) {
            return `BuildConstant(name);
          } else if(tomSymbol != null) {
            if(TomBase.isListOperator(tomSymbol)) {
              return ASTFactory.buildList(`name,args,desugarer.symbolTable());
            } else if(TomBase.isArrayOperator(tomSymbol)) {
              return ASTFactory.buildArray(`name,args,desugarer.symbolTable());
            } else {
              String moduleName = TomBase.getModuleName(`optionList);
              if(moduleName==null) {
                moduleName = TomBase.DEFAULT_MODULE_NAME;
              }
              return `BuildTerm(name,args,moduleName);
            }
          } else {
            return `FunctionCall(name,EmptyType(),args);
          }
        }
      }
    }




  private TomSymbol addDefaultMake(TomSymbol tomSymbol) {
    %match(tomSymbol) {
      Symbol[Option=(_*,DeclarationToOption((MakeDecl|MakeEmptyList|MakeEmptyArray|MakeAddList|MakeAddArray|IsFsymDecl|GetImplementationDecl|GetSlotDecl|GetHeadDecl|GetTailDecl|IsEmptyDecl|GetElementDecl|GetSizeDecl)[]),_*)] -> {
        return tomSymbol;
      }
      Symbol(name,t@TypesToType(domain,codomain),l,concOption(X1*,origin@OriginTracking(_,line,file),X2*)) -> {
        //build variables for make
        TomList argsAST = `concTomTerm();
        int index = 0;
        for(TomType subtermType:(concTomType)`domain) {
          TomTerm variable = `Variable(concOption(),Name("t"+index),subtermType,concConstraint());
          argsAST = `concTomTerm(argsAST*,variable);
          index++;
        }
        TomTerm functionCall = `FunctionCall(name,codomain,argsAST);
        Declaration make = `MakeDecl(name,codomain,argsAST,TomTermToInstruction(functionCall),
            OriginTracking(Name("make"),line,file));
        return `Symbol(name,t,l,concOption(X1*,origin,DeclarationToOption(make),X2*));
      }
    }
    return tomSymbol;
  }

  private TomSymbol addDefaultIsFsym(TomSymbol tomSymbol) {
    %match(tomSymbol) {
      Symbol[Option=(_*,DeclarationToOption(IsFsymDecl[]),_*)] -> {
        return tomSymbol;
      }
      Symbol(name,t@TypesToType(_,codom),l,concOption(X1*,origin@OriginTracking(_,line,file),X2*)) -> {
        Declaration isfsym = `IsFsymDecl(name,Variable(concOption(OriginTracking(Name("t"),line,file)),Name("t"),codom,concConstraint()),FalseTL(),OriginTracking(Name("is_fsym"),line,file));
        return `Symbol(name,t,l,concOption(X1*,origin,DeclarationToOption(isfsym),X2*));
      }
    }
    return tomSymbol;
  }

    /*
     * replaces 'TermAppl' by its 'RecordAppl' form
     * when no slotName exits, the position becomes the slotName
     */
    protected TomTerm typeTermAppl(OptionList option, TomNameList nameList, TomList args, ConstraintList constraints) {
      TomName headName = nameList.getHeadconcTomName();
      if(headName instanceof AntiName) {
        headName = ((AntiName)headName).getName();
      }
      String opName = headName.getString();
      TomSymbol tomSymbol = getSymbolFromName(opName);


      //System.out.println("typeTermAppl: " + tomSymbol);
      //System.out.println("  nameList = " + nameList);

      if(tomSymbol==null && args.isEmptyconcTomTerm()) {
        return `RecordAppl(option,nameList,concSlot(),constraints);
      }

      SlotList slotList = `concSlot();
      Strategy typeStrategy = `TopDownIdStopOnSuccess(typeTermApplTomSyntax(this));
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
      }

      return `RecordAppl(option,nameList,slotList,constraints);
    }

    protected TomTerm typeXMLAppl(OptionList optionList, TomNameList nameList,
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
      Strategy typeStrategy = `TopDownIdStopOnSuccess(typeTermApplTomSyntax(this));
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
        xmlHead = `UnamedVariable(optionList,symbolTable().TYPE_UNKNOWN,concConstraint());
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

        //System.out.println("typeXML out:\n" + result);
        return result;
      } catch(tom.library.sl.VisitFailure e) {
        //must never be executed
        return star;
      }
    }

  /*
   * Replace a TomTypeAlone by its expanded form (TomType)
   */
  private TomTerm expandType(TomTerm subject) {
    try {
      return `TopDownIdStopOnSuccess(expandType(this)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeType: failure on " + subject);
    }
  }

  %strategy expandType(desugarer:Desugarer) extends Identity() {
    visit TomType {
      subject@TomTypeAlone(tomType) -> {
        TomType type = desugarer.symbolTable().getType(`tomType);
        if(type != null) {
          return type;
        } else {
          return `subject; // useful for SymbolTable.TYPE_UNKNOWN
        }
      }
    }
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

            BackQuoteAppl[Args=concTomTerm(RecordAppl[NameList=(Name(name1))],_*)],
              BackQuoteAppl[Args=concTomTerm(RecordAppl[NameList=(Name(name2))],_*)] -> {
                if(`name1.compareTo(`name2) > 0) {
                  return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
                }
              }

            BackQuoteAppl[Args=concTomTerm(TermAppl[NameList=(Name(name1))],_*)],
              BackQuoteAppl[Args=concTomTerm(TermAppl[NameList=(Name(name2))],_*)] -> {
                if(`name1.compareTo(`name2) > 0) {
                  return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
                }
              }

            BackQuoteAppl[Args=concTomTerm(BackQuoteAppl[AstName=Name(name1)],_*)],
              BackQuoteAppl[Args=concTomTerm(BackQuoteAppl[AstName=Name(name2)],_*)] -> {
                if(`name1.compareTo(`name2) > 0) {
                  return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
                }
              }
          }
        }
      }
      return attrList;
    }

}
