/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom.backend;
  
import java.util.*;
import java.io.*;

import aterm.*;
import aterm.pure.*;

import jtom.*;
import jtom.tools.*;
import jtom.adt.*;

public class TomGenerator extends TomBase {

  private String debugKey = null;
  
  public TomGenerator(jtom.TomEnvironment environment) {
    super(environment);
  }

// ------------------------------------------------------------
  %include { Tom.signature }
// ------------------------------------------------------------
  
    /*
     * Generate the goal language
     */
  
  public void generate(jtom.tools.OutputCode out, int deep, TomTerm subject)
    throws IOException {
      //System.out.println("Generate: " + subject);
      //%variable

    statistics().numberPartsGoalLanguage++;

    %match(TomTerm subject) {
      
      Tom(l) -> {
        generateList(out,deep,l);
        return;
      }

      TomInclude(l) -> {
        if (Flags.jCode) {
          OutputCode singleLineOutput = new SingleLineOutputCode(out.getFile());
          generateList(singleLineOutput,deep,l);
        } else {
          generateList(out,deep,l);
        }
        return;
      }
     
      BuildVariable(Name(name)) -> {
        out.write(name);
        return;
      }

      BuildVariable(PositionName(l1)) -> {
        out.write("tom" + numberListToIdentifier(l1));
          //out.write("tom");
          //numberListToIdentifier(out,l1);
        return;
      }
      
        //BuildVariableStar(Name(name)) -> {
        //out.write(name);
        //return;
        //}

      BuildBuiltin(Name(name)) -> {
        out.write(name);
        return;
      }

      BuildTerm(Name(name), argList) -> {
        out.write("tom_make_");
        out.write(name);
        if(Flags.eCode && argList.isEmpty()) {
        } else {
          out.writeOpenBrace();
          while(!argList.isEmpty()) {
            generate(out,deep,argList.getHead());
            argList = argList.getTail();
            if(!argList.isEmpty()) {
              out.writeComa();
            }
          }
          out.writeCloseBrace();
        }
        return;
      }

      BuildList(Name(name), argList) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(name);
        String listType = getTLType(getSymbolCodomain(tomSymbol));
        int size = 0;
        while(!argList.isEmpty()) {
          TomTerm elt = argList.getHead();
          if(/* elt.isBuildVariableStar() || */ elt.isVariableStar()) {
            out.write("tom_insert_list_" + name + "(");
            generate(out,deep,elt);
            out.write(",");
          } else {
            out.write("tom_make_insert_" + name + "(");
            generate(out,deep,elt);
            out.write(",");
          }
          argList = argList.getTail();
          size++;
        }
        out.write("(" + listType + ") tom_make_empty_" + name + "()");
        for(int i=0; i<size; i++) {
          out.write(")");
        }
        return;
      }

      BuildArray(Name(name), argList) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(name);
        String listType = getTLType(getSymbolCodomain(tomSymbol));
        int size = 0;
        TomList reverse = reverse(argList);
        while(!reverse.isEmpty()) {
          TomTerm elt = reverse.getHead();
          if(/* elt.isBuildVariableStar() || */ elt.isVariableStar()) {
            out.write("tom_append_array_" + name + "(");
            generate(out,deep,elt);
            out.write(",");
          } else {
            out.write("tom_make_append_" + name + "(");
            generate(out,deep,elt);
            out.write(",");
          }
          reverse = reverse.getTail();
          size++;
        }
        out.write("(" + listType + ") tom_make_empty_" + name + "(" + size + ")"); 
        for(int i=0; i<size; i++) { 
            //out.write("," + i + ")");
          out.write(")"); 
        }
        return;
      }

      FunctionCall(Name(name), argList) -> {
        out.write(name + "(");
        while(!argList.isEmpty()) {
          generate(out,deep,argList.getHead());
          argList = argList.getTail();
          if(!argList.isEmpty()) {
            out.write(", ");
          }
        }
        out.write(")");
        return;
      }

      DotTerm(t1,t2) -> {
        generate(out,deep,t1);
        out.write(".");
        generate(out,deep,t2);
        return;
      }

      CompiledMatch(matchDeclarationList, namedBlockList, Option(list)) -> {
        boolean generated = hasGeneratedMatch(list);
        Option orgTrack = null;
        if(Flags.supportedBlock) {
          generateInstruction(out,deep,`OpenBlock());
        }
        if(Flags.debugMode && !generated) {
          orgTrack = findOriginTracking(list);
          debugKey = orgTrack.getFileName().getString() + orgTrack.getLine().toString();
          out.write("jtom.debug.TomDebugger.debug.entering"+orgTrack.getAstName().getString()+"(\""+debugKey+"\");\n");
        }
        generateList(out,deep+1,matchDeclarationList);
        generateList(out,deep+1,namedBlockList);
        if(Flags.debugMode && !generated) {
          out.write("jtom.debug.TomDebugger.debug.leaving"+orgTrack.getAstName().getString()+"(\""+debugKey+"\");\n");
        }
        if(Flags.supportedBlock) {
          generateInstruction(out,deep,`CloseBlock());
        }
        return;
      }

      CompiledPattern(instList) -> {
        generateList(out,deep,instList);
        return;
      }

      Variable(option1,PositionName(l1),type1) -> {
          //System.out.println("Variable(option1,PositionName(l1),type1)");
          //System.out.println("subject = " + subject);
          //System.out.println("l1      = " + l1);

          /*
           * sans type: re-definition lorsque %variable est utilise
           * avec type: probleme en cas de filtrage dynamique
           */
        out.write("tom" + numberListToIdentifier(l1));
        return;
      }

      Variable(option1,Name(name1),type1) -> {
        out.write(name1);
        return;
      }

      VariableStar(option1,PositionName(l1),type1) -> {
        out.write("tom" + numberListToIdentifier(l1));
        return;  
      }

      VariableStar(option1,Name(name1),type1) -> {
        out.write(name1);
        return;
      }

      Declaration(var@Variable(option1,name1,
                                 Type(TomType(type),tlType@TLType[]))) -> {
        if(Flags.cCode || Flags.jCode) {
          out.write(deep,getTLCode(tlType) + " ");
          generate(out,deep,var);
        } else if(Flags.eCode) {
          generate(out,deep,var);
          out.write(deep,": " + getTLCode(tlType));
        }
        
        if(Flags.jCode && !isBoolType(type) && !isIntType(type)) {
          out.writeln(" = null;");
        } else {
          out.writeln(";");
        }
        return;
      }

      Declaration(var@VariableStar(option1,name1,
                                   Type(TomType(type),tlType@TLType[]))) -> {
        if(Flags.cCode || Flags.jCode) {
          out.write(deep,getTLCode(tlType) + " ");
          generate(out,deep,var);
        } else if(Flags.eCode) {
          generate(out,deep,var);
          out.write(deep,": " + getTLCode(tlType));
        }
        out.writeln(";");
        return;
      }

      MakeFunctionBegin(Name(tomName),SubjectList(varList)) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        String glType = getTLType(getSymbolCodomain(tomSymbol));
        String name = tomSymbol.getAstName().getString();
        
        if(Flags.cCode || Flags.jCode) {
          out.write(deep,glType + " " + name + "(");
        } else if(Flags.eCode) {
          out.write(deep,name + "(");
        }
        while(!varList.isEmpty()) {
          TomTerm localVar = varList.getHead();
          matchBlock: {
            %match(TomTerm localVar) {
              v@Variable(option2,name2,type2) -> {
                if(Flags.cCode || Flags.jCode) {
                  out.write(deep,getTLType(type2) + " ");
                  generate(out,deep,v);
                } else if(Flags.eCode) {
                  generate(out,deep,v);
                  out.write(deep,": " + getTLType(type2));
                }
                break matchBlock;
              }
              _ -> {
                System.out.println("MakeFunction: strange term: " + localVar);
                System.exit(1);
              }
            }
          }
          varList = varList.getTail();
          if(!varList.isEmpty()) {
            if(Flags.cCode || Flags.jCode) {
              out.write(deep,", ");
            } else if(Flags.eCode) {
              out.write(deep,"; ");
            }
          }
        }
        if(Flags.cCode || Flags.jCode) {
          out.writeln(deep,") {");
        } else if(Flags.eCode) {
          out.writeln(deep,"): " + glType + " is");
          out.writeln(deep,"local ");
        }
          //out.writeln(deep,"return null;");
        return;
      }

      MakeFunctionEnd -> {
        if(Flags.cCode || Flags.jCode) {
          out.writeln(deep,"}");
        } else if(Flags.eCode) {
          out.writeln(deep,"end;");
        }
        return;
      }

      EndLocalVariable -> { out.writeln(deep,"do"); return; }
      
      TargetLanguageToTomTerm(t) -> {
        generateTargetLanguage(out,deep,t);
        return;
      }

      DeclarationToTomTerm(t) -> {
        generateDeclaration(out,deep,t);
        return;
      }

      ExpressionToTomTerm(t) -> {
        generateExpression(out,deep,t);
        return;
      }

      InstructionToTomTerm(t) -> {
        generateInstruction(out,deep,t);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for: " + t);
        System.exit(1);
      }
    }
  }

  public void generateExpression(jtom.tools.OutputCode out, int deep, Expression subject)
    throws IOException {
    if(subject==null) { return; }
    
    statistics().numberPartsGoalLanguage++;
    %match(Expression subject) {
      Not(exp) -> {
        if(Flags.cCode || Flags.jCode) {
          out.write("!(");
          generateExpression(out,deep,exp);
          out.write(")");
        } else if(Flags.eCode) {
          out.write("not ");
          generateExpression(out,deep,exp);
        }
        return;
      }

      And(exp1,exp2) -> {
        generateExpression(out,deep,exp1);
        out.write(" && ");
        generateExpression(out,deep,exp2);
        return;
      }

      TrueTL -> {
        if(Flags.cCode) {
          out.write(" 1 ");
        } else if(Flags.jCode) {
          out.write(" true ");
        } else if(Flags.eCode) {
          out.write(" true ");
        }
        return;
      }
      
      FalseTL -> {
        if(Flags.cCode) {
          out.write(" 0 ");
        } else if(Flags.jCode) {
          out.write(" false ");
        } else if(Flags.eCode) {
          out.write(" false ");
        }
        return;
      }

      IsEmptyList(var@Variable[astType=type1]) -> {
        out.write("tom_is_empty_" + getTomType(type1) + "(");
        generate(out,deep,var);
        out.write(")");
        return;
      }

      IsEmptyArray(varArray@Variable[astType=type1], varIndex@Variable[]) -> {
        generate(out,deep,varIndex);
        out.write(" >= ");
        out.write("tom_get_size_" + getTomType(type1) + "(");
        generate(out,deep,varArray);
        out.write(")");
        return;
      }

      EqualFunctionSymbol(var@Variable[astType=type1],Appl(option,Name(tomName),l)) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        TomName termNameAST = tomSymbol.getAstName();
        OptionList termOptionList = tomSymbol.getOption().getOptionList();
        
        Declaration isFsymDecl = getIsFsymDecl(termOptionList);
        if(isFsymDecl != null) {
          generateExpression(out,deep,`IsFsym(termNameAST,var));
        } else {
          String s = (String)getFunSymMap.get(type1);
          if(s == null) {
            s = "tom_cmp_fun_sym_" + getTomType(type1) + "(tom_get_fun_sym_" + getTomType(type1) + "(";
            getFunSymMap.put(type1,s);
          }
          out.write(s);
          generate(out,deep,var);
          out.write(") , " + getSymbolCode(tomSymbol) + ")");
        }
        return;
      }
      
      EqualFunctionSymbol(var1@Variable[astType=type1],var2) -> {
          //System.out.println("EqualFunctionSymbol(...," + var2 + ")");
        out.write("tom_cmp_fun_sym_" + getTomType(type1) + "(");
        out.write("tom_get_fun_sym_" + getTomType(type1) + "(");
        generate(out,deep,var1);
        out.write(") , " + var2 + ")");
        return;
      }

      EqualTerm(var1@Variable[astType=type1],var2) -> {
        out.write("tom_terms_equal_" + getTomType(type1) + "(");
        generate(out,deep,var1);
        out.write(", ");
        generate(out,deep,var2);
        out.write(")");
        return;
      }

      EqualTerm(var1@VariableStar[astType=type1],var2) -> {
        out.write("tom_terms_equal_" + getTomType(type1) + "(");
        generate(out,deep,var1);
        out.write(", ");
        generate(out,deep,var2);
        out.write(")");
        return;
      }

      IsFsym(Name(opname), var@Variable(option1,PositionName(l1),type1)) -> {
        String s = (String)isFsymMap.get(opname);
        if(s == null) {
          s = "tom_is_fun_sym_" + opname + "(";
          isFsymMap.put(opname,s);
        } 
        out.write(s);
        generate(out,deep,var);
        out.write(")");
        return;
      }

      GetSubterm(var@Variable(option1,PositionName(l1),type1),Number(number)) -> {
        String s = (String)getSubtermMap.get(type1);
        if(s == null) {
          s = "tom_get_subterm_" + getTomType(type1) + "(";
          getSubtermMap.put(type1,s);
        } 
        out.write(s);
        generate(out,deep,var);
        out.write(", " + number + ")");
        return;
      }

      GetSlot(Name(opname),slotName, var@Variable[]) -> {
        out.write("tom_get_slot_" + opname + "_" + slotName + "(");
        generate(out,deep,var);
        out.write(")");
        return;
      }

      GetHead(var@Variable(option1,PositionName(l1),type1)) -> {
        out.write("tom_get_head_" + getTomType(type1) + "(");
        generate(out,deep,var);
        out.write(")");
        return;
      }

      GetTail(var@Variable(option1,PositionName(l1),type1)) -> {
        out.write("tom_get_tail_" + getTomType(type1) + "(");
        generate(out,deep,var); 
        out.write(")");
        return;
      }

      GetSize(var@Variable(option1,PositionName(l1),type1)) -> {
        out.write("tom_get_size_" + getTomType(type1) + "(");
        generate(out,deep,var);
        out.write(")");
        return;
      }

      GetElement( varName@Variable(option1,PositionName(l1),type1),
                 varIndex@Variable(option2,PositionName(l2),type2)) -> {
        out.write("tom_get_element_" + getTomType(type1) + "(");
        generate(out,deep,varName);
        out.write(",");
        generate(out,deep,varIndex);
        out.write(")");
        return;
      }

      GetSliceList(Name(name),
                   varBegin@Variable(option1,PositionName(l1),type1),
                     varEnd@Variable(option2,PositionName(l2),type2)) -> {
        
        out.write("tom_get_slice_" + name + "(");
        generate(out,deep,varBegin);
        out.write(",");
        generate(out,deep,varEnd);
        out.write(")");
        return;
      }

      GetSliceArray(Name(name),
                    varArray@Variable(option1,PositionName(l1),type1),
                    varBegin@Variable(option2,PositionName(l2),type2),
                    expEnd) -> {
        
        out.write("tom_get_slice_" + name + "(");
        generate(out,deep,varArray);
        out.write(",");
        generate(out,deep,varBegin);
        out.write(",");
        generate(out,deep,expEnd);
        out.write(")");
        return;
      }

      TomTermToExpression(t) -> {
        generate(out,deep,t);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for expression: " + t);
        System.exit(1);
      }
    }
  }

  public void generateInstruction(jtom.tools.OutputCode out, int deep, Instruction subject)
    throws IOException {
    if(subject==null) { return; }
    
    statistics().numberPartsGoalLanguage++;
    %match(Instruction subject) {

      Assign(var@Variable(Option(list),name1,
                          Type(tomType@TomType(type),tlType@TLType[])),exp) -> {
        out.indent(deep);
        generate(out,deep,var);
        if(Flags.cCode || Flags.jCode) {
          out.write(" = (" + getTLCode(tlType) + ") ");
        } else if(Flags.eCode) {
          if(isBoolType(type) || isIntType(type)) {
            out.write(" := ");
          } else {
              //out.write(" ?= ");
            String assignSign = " := ";
            %match(Expression exp) {
              GetSubterm[] -> {
                assignSign = " ?= ";
              }
            }
            out.write(assignSign);
          }
        }
        generateExpression(out,deep,exp);
        out.writeln(";");
        if(Flags.debugMode && !list.isEmptyOptionList()) {
          out.write("jtom.debug.TomDebugger.debug.addSubstitution(\""+debugKey+"\",\"");
          generate(out,deep,var);
          out.write("\", ");
          generateExpression(out,deep,exp);
          out.write(");\n");
        }
        return;
      }
      
      AssignMatchSubject(var@Variable(option1,name1,
                                      Type(tomType@TomType(type),tlType@TLType[])),exp) -> {
        out.indent(deep);
        generate(out,deep,var);
        if(Flags.cCode || Flags.jCode) {
          out.write(" = (" + getTLCode(tlType) + ") ");
        } else if(Flags.eCode) {
          if(isBoolType(type) || isIntType(type)) {
            out.write(" := ");
          } else {
              //out.write(" ?= ");
            String assignSign = " := ";
            %match(Expression exp) {
              GetSubterm[] -> {
                assignSign = " ?= ";
              }
            }
            out.write(assignSign);
          }
        }
        generateExpression(out,deep,exp);
        out.writeln(";");
        if (Flags.debugMode) {
          out.write("jtom.debug.TomDebugger.debug.specifySubject(\""+debugKey+"\",\"");
          generateExpression(out,deep,exp);
          out.writeln("\",");
          generateExpression(out,deep,exp);
          out.writeln(");");
        }
        return;
      }
      
      NamedBlock(blockName,instList) -> {
        if(Flags.cCode) {
          out.writeln("{");
          generateList(out,deep+1,instList);
          out.writeln("}" + blockName +  ":;");
        } else if(Flags.jCode) {
          out.writeln(blockName + ": {");
          generateList(out,deep+1,instList);
          out.writeln("}");
        } else if(Flags.eCode) {
          System.out.println("NamedBlock: Eiffel code not yet implemented");
            //System.exit(1);
        }  
        return;
      }
      
        //IfThenElse(exp,succesList,conc()) -> {
      IfThenElse(exp,succesList,Empty()) -> {
        statistics().numberIfThenElseTranformed++;
        if(Flags.cCode || Flags.jCode) {
          out.write(deep,"if("); generateExpression(out,deep,exp); out.writeln(") {");
          generateList(out,deep+1,succesList);
          out.writeln(deep,"}");
        } else if(Flags.eCode) {
          out.write(deep,"if "); generateExpression(out,deep,exp); out.writeln(" then ");
          generateList(out,deep+1,succesList);
          out.writeln(deep,"end;");
        }
        return;
      }

      IfThenElse(exp,succesList,failureList) -> {
        statistics().numberIfThenElseTranformed++;
        if(Flags.cCode || Flags.jCode) {
          out.write(deep,"if("); generateExpression(out,deep,exp); out.writeln(") {");
          generateList(out,deep+1,succesList);
          out.writeln(deep,"} else {");
          generateList(out,deep+1,failureList);
          out.writeln(deep,"}");
        } else if(Flags.eCode) {
          out.write(deep,"if "); generateExpression(out,deep,exp); out.writeln(" then ");
          generateList(out,deep+1,succesList);
          out.writeln(deep," else ");
          generateList(out,deep+1,failureList);
          out.writeln(deep,"end;");
        }
        return;
      }

      DoWhile(succesList,exp) -> {
        out.writeln(deep,"do {");
        generateList(out,deep+1,succesList);
        out.write(deep,"} while("); generateExpression(out,deep,exp); out.writeln(");");
        return;
      }

      Assign(var@VariableStar(option1,name1,
                              Type(TomType(type),tlType@TLType[])),exp) -> {
        out.indent(deep);
        generate(out,deep,var);
        if(Flags.cCode || Flags.jCode) {
          out.write(" = (" + getTLCode(tlType) + ") ");
        } else if(Flags.eCode) {
          out.write(" := ");
        }
        generateExpression(out,deep,exp);
        out.writeln(";");
        return;
      }

      Increment(var@Variable[]) -> {
        generate(out,deep,var);
        out.write(" = ");
        generate(out,deep,var);
        out.writeln(" + 1;");
        return;
      }

      Action(l) -> {
        while(!l.isEmpty()) {
          generate(out,deep,l.getHead());
          l = l.getTail();
        }
          //out.writeln("// ACTION: " + l);
        return;
      }

      ExitAction(numberList) -> {
        if(Flags.cCode) {
          out.writeln(deep,"goto matchlab" + numberListToIdentifier(numberList) + ";");
        } else if(Flags.jCode) {
          out.writeln(deep,"break matchlab" + numberListToIdentifier(numberList) + ";");
        } else if(Flags.eCode) {
          System.out.println("ExitAction: Eiffel code not yet implemented");
            //System.exit(1);
        }
        return;
      }

      Return(exp) -> {
        if(Flags.cCode || Flags.jCode) {
          out.write(deep,"return ");
          generate(out,deep,exp);
          out.writeln(deep,";");
        } else if(Flags.eCode) {
          out.writeln(deep,"if Result = Void then");
          out.write(deep+1,"Result := ");
          generate(out,deep+1,exp);
          out.writeln(deep+1,";");
          out.writeln(deep,"end;");
        }
        return;
      }

      OpenBlock  -> { out.writeln(deep,"{"); return; }
      CloseBlock -> { out.writeln(deep,"}"); return; }

      
      t -> {
        System.out.println("Cannot generate code for instruction: " + t);
        System.exit(1);
      }
    }
  }

      
  
  public void generateTargetLanguage(jtom.tools.OutputCode out, int deep, TargetLanguage subject)
    throws IOException {
    if(subject==null) { return; }
    statistics().numberPartsGoalLanguage++;
    %match(TargetLanguage subject) {
      TL(t,Position[line=startLine], Position[line=endLine]) -> {
        statistics().numberPartsCopied++;
        out.write(deep,t, startLine.intValue(), endLine.intValue()-startLine.intValue());
        return;
      }
      
      ITL(t) -> {
        statistics().numberPartsCopied++;
        out.write(deep,t);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for: " + t);
        System.exit(1);
      }
    }
  }

  public void generateOption(jtom.tools.OutputCode out, int deep, Option subject)
    throws IOException {
    if(subject==null) { return; }
    
    statistics().numberPartsGoalLanguage++;
    %match(Option subject) {
      DeclarationToOption(decl) -> {
        generateDeclaration(out,deep,decl);
        return;
      }
      OriginTracking[] -> { return; }
      DefinedSymbol -> { return; }
      Constructor[] -> { return; }

      t -> {
        System.out.println("Cannot generate code for option: " + t);
        System.exit(1);
      }
    }
  }
  
  public void generateDeclaration(jtom.tools.OutputCode out, int deep, Declaration subject)
    throws IOException {
    if(subject==null) { return; }
    
    statistics().numberPartsGoalLanguage++;
    %match(Declaration subject) {
      EmptyDeclaration() -> {
        return;
      }
      SymbolDecl(Name(tomName)) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        OptionList optionList = tomSymbol.getOption().getOptionList();
        SlotList slotList = tomSymbol.getSlotList();
        TomList l = getSymbolDomain(tomSymbol);
        TomType type1 = getSymbolCodomain(tomSymbol);
        String name1 = tomSymbol.getAstName().getString();

        if(Flags.cCode && isDefinedSymbol(tomSymbol)) {
            // TODO: build an abstract declaration
          int argno=1;
            /*
              String s = "";
          if(!l.isEmpty()) {
            s = getTLType(type1) + " " + name1;
            
            if(!l.isEmpty()) {
              s += "(";
              while (!l.isEmpty()) {
                s += getTLType(l.getHead()) + " _" + argno;
                argno++;
                l = l.getTail() ;
                if(!l.isEmpty()) {
                  s += ",";
                }
              }
              s += ");";
            }
          }
            generate(out,deep,makeTL(s));
            */

          out.indent(deep);
          if(!l.isEmpty()) {
            out.write(getTLType(type1));
            out.writeSpace();
            out.write(name1);
            if(!l.isEmpty()) {
              out.writeOpenBrace();
              while (!l.isEmpty()) {
                out.write(getTLType(l.getHead().getAstType()));
                  //out.writeUnderscore();
                  //out.write(argno);
                argno++;
                l = l.getTail() ;
                if(!l.isEmpty()) {
                  out.writeComa();
                }
              }
              out.writeCloseBrace();
              out.writeSemiColon();
            }
          }
          out.writeln();
        } else if(Flags.jCode) {
            // do nothing
        } else if(Flags.eCode) {
            // do nothing
        }

          // inspect the optionList
        generateOptionList(out, deep, optionList);
          // inspect the slotlist
        generateSlotList(out, deep, slotList);
        return ;
      }
      
      ArraySymbolDecl(Name(tomName)) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        OptionList optionList = tomSymbol.getOption().getOptionList();
        SlotList slotList = tomSymbol.getSlotList();        
        TomList l = getSymbolDomain(tomSymbol);
        TomType type1 = getSymbolCodomain(tomSymbol);
        String name1 = tomSymbol.getAstName().getString();
        
        if(Flags.cCode) {
            // TODO: build an abstract declaration
          int argno=1;
          out.indent(deep);
          if(!l.isEmpty()) {
            out.write(getTLType(type1));
            out.writeSpace();
            out.write(name1);
            if(!l.isEmpty()) {
              out.writeOpenBrace();
              while (!l.isEmpty()) {
                out.write(getTLType(l.getHead().getAstType()));
                out.writeUnderscore();
                out.write(argno);
                argno++;
                l = l.getTail() ;
                if(!l.isEmpty()) {
                  out.writeComa();
                }
              }
              out.writeCloseBrace();
              out.writeSemiColon();
            }
          }
          out.writeln();
        } else if(Flags.jCode) {
            // do nothing
        } else if(Flags.eCode) {
            // do nothing
        }

          // inspect the optionList
        generateOptionList(out, deep, optionList);
          // inspect the slotlist
        generateSlotList(out, deep, slotList);
        return ;
            }

      ListSymbolDecl(Name(tomName)) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        OptionList optionList = tomSymbol.getOption().getOptionList();
        SlotList slotList = tomSymbol.getSlotList();
        TomList l = getSymbolDomain(tomSymbol);
        TomType type1 = getSymbolCodomain(tomSymbol);
        String name1 = tomSymbol.getAstName().getString();
        
        
        if(Flags.cCode) {
            // TODO: build an abstract declaration
          int argno=1;
          out.indent(deep);
          if(!l.isEmpty()) {
            out.write(getTLType(type1));
            out.writeSpace();
            out.write(name1);
            if(!l.isEmpty()) {
              out.writeOpenBrace();
              while (!l.isEmpty()) {
                out.write(getTLType(l.getHead().getAstType()));
                out.writeUnderscore();
                out.write(argno);
                argno++;
                l = l.getTail() ;
                if(!l.isEmpty()) {
                  out.writeComa();
                }
              }
              out.writeCloseBrace();
              out.writeSemiColon();
            }
          }
          out.writeln();
        } else if(Flags.jCode) {
            // do nothing
        } else if(Flags.eCode) {
            // do nothing
        }

          // inspect the optionList
        generateOptionList(out, deep, optionList);
          // inspect the slotlist
        generateSlotList(out, deep, slotList);
        return ;
      }

      GetFunctionSymbolDecl(Variable(option,Name(name),
                                     Type(TomType(type),tlType@TLType[])),
                            tlCode, _) -> {
        String args[];
        if(!Flags.strictType) {
          args = new String[] { getTLType(getUniversalType()), name };
        } else {
          args = new String[] { getTLCode(tlType), name };
        }

        TomType returnType = isIntType(type)?getIntType():getUniversalType();
        generateTargetLanguage(out,deep,
                               genDecl(getTLType(returnType),
                                       "tom_get_fun_sym", type,args,tlCode));
        return;
      }
      
      GetSubtermDecl(Variable(option1,Name(name1),
                              Type(TomType(type1),tlType1@TLType[])),
                     Variable(option2,Name(name2),
                              Type(TomType(type2),tlType2@TLType[])),
                     tlCode, _) -> {
        String args[];
        if(Flags.strictType || Flags.eCode) {
          args = new String[] { getTLCode(tlType1), name1,
                                getTLCode(tlType2), name2 };
        } else {
          args = new String[] { getTLType(getUniversalType()), name1,
                                getTLCode(tlType2), name2 };
        }
        generateTargetLanguage(out,deep, genDecl(getTLType(getUniversalType()), "tom_get_subterm", type1,
                                                 args, tlCode));
        return;
      }
      
      IsFsymDecl(Name(tomName),
		 Variable(option1,Name(name1), Type(TomType(type1),tlType@TLType[])),
                 tlCode@TL[], _) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        String opname = tomSymbol.getAstName().getString();

	  TomType returnType = getBoolType();
	  String argType;
	  if(Flags.strictType) {
	      argType = getTLCode(tlType);
	  } else {
	      argType = getTLType(getUniversalType());
	  }

	  generateTargetLanguage(out,deep, genDecl(getTLType(returnType),
				     "tom_is_fun_sym", opname,
				     new String[] { argType, name1 },
				     tlCode));
	  return;
      }
 
      GetSlotDecl[astName=Name(tomName),
                  slotName=slotName,
                  term=Variable(option1,Name(name1), Type(TomType(type1),tlType@TLType[])),
                  tlCode=tlCode@TL[]] -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        String opname = tomSymbol.getAstName().getString();
        TomList typesList = tomSymbol.getTypesToType().getList();
        
        int slotIndex = getSlotIndex(tomSymbol.getSlotList(),slotName);
        TomList l = typesList;
        for(int index = 0; !l.isEmpty() && index<slotIndex ; index++) {
          l = l.getTail();
        }
        TomType returnType = l.getHead().getAstType();
        
	String argType;
	  if(Flags.strictType) {
	      argType = getTLCode(tlType);
	  } else {
	      argType = getTLType(getUniversalType());
	  }
        generateTargetLanguage(out,deep, genDecl(getTLType(returnType),
                                   "tom_get_slot", opname  + "_" + slotName.getString(),
                                   new String[] { argType, name1 },
                                   tlCode));
        return;
      }

      CompareFunctionSymbolDecl(Variable(option1,Name(name1), Type(TomType(type1),_)),
                                Variable(option2,Name(name2), Type(TomType(type2),_)),
                                tlCode, _) -> {
        TomType argType1 = (isIntType(type1))?getIntType():getUniversalType();
        TomType argType2 = (isIntType(type2))?getIntType():getUniversalType();
        generateTargetLanguage(out,deep, genDecl(getTLType(getBoolType()), "tom_cmp_fun_sym", type1,
                                                 new String[] {
                                                   getTLType(argType1), name1,
                                                   getTLType(argType2), name2
                                                 },
                                                 tlCode));
        return;
      }

      TermsEqualDecl(Variable(option1,Name(name1), Type(TomType(type1),_)),
                     Variable(option2,Name(name2), Type(TomType(type2),_)),
                     tlCode, _) -> {
        TomType argType1 = (isIntType(type1))?getIntType():getUniversalType();
        TomType argType2 = (isIntType(type2))?getIntType():getUniversalType();

        generateTargetLanguage(out,deep, genDecl(getTLType(getBoolType()), "tom_terms_equal", type1,
                                                 new String[] {
                                                   getTLType(argType1), name1,
                                                   getTLType(argType2), name2
                                                 },
                                                 tlCode));
        return;
      }
      
      GetHeadDecl(Variable(option1,Name(name1), Type(TomType(type),tlType@TLType[])),
                  tlCode@TL[], _) -> {
        String argType;
        if(Flags.strictType) {
          argType = getTLCode(tlType);
        } else {
          argType = getTLType(getUniversalType());
        }

        generateTargetLanguage(out,deep,
                               genDecl(getTLType(getUniversalType()),
                                       "tom_get_head", type,
                                       new String[] { argType, name1 },
                                       tlCode));
        return;
      }

      GetTailDecl(Variable(option1,Name(name1), Type(TomType(type),tlType@TLType[])),
                  tlCode@TL[], _) -> {
        String returnType, argType;
        if(Flags.strictType) {
          returnType = getTLCode(tlType);
          argType = getTLCode(tlType);
        } else {
          returnType = getTLType(getUniversalType());
          argType = getTLType(getUniversalType());
        }
        
        generateTargetLanguage(out,deep,
                               genDecl(returnType, "tom_get_tail", type,
                                       new String[] { argType, name1 },
                                       tlCode));
        return;
      }

      IsEmptyDecl(Variable(option1,Name(name1), Type(TomType(type),tlType@TLType[])),
                  tlCode@TL[], _) -> {
        String argType;
        if(Flags.strictType) {
          argType = getTLCode(tlType);
        } else {
          argType = getTLType(getUniversalType());
        }

        generateTargetLanguage(out,deep,
                               genDecl(getTLType(getBoolType()),
                                       "tom_is_empty", type,
                                       new String[] { argType, name1 },
                                       tlCode));
        return;
      }

      MakeEmptyList(Name(opname), tlCode@TL[], _) -> {
        generateTargetLanguage(out,deep,
                               genDecl(getTLType(getUniversalType()),
                                       "tom_make_empty", opname,
                                       new String[] { },
                                       tlCode));
        return;
      }

      MakeAddList(Name(opname),
                  Variable(option1,Name(name1), fullEltType@Type(TomType(type1),tlType1@TLType[])),
                  Variable(option2,Name(name2), fullListType@Type(TomType(type2),tlType2@TLType[])),
                  tlCode@TL[], _) -> {
        String returnType, argListType,argEltType;
        if(Flags.strictType) {
          argEltType = getTLCode(tlType1);
          argListType = getTLCode(tlType2);
          returnType = argListType;
        } else {
          argEltType  = getTLType(getUniversalType());
          argListType = getTLType(getUniversalType());
          returnType  = argListType;
        }
        
        generateTargetLanguage(out,deep, genDecl(returnType,
                                                 "tom_make_insert", opname,
                                                 new String[] {
                                                   argEltType, name1,
                                                   argListType, name2
                                                 },
                                                 tlCode));
        
        generateTargetLanguage(out,deep, genDeclList(opname, fullListType,fullEltType));
        return;
      }

      GetElementDecl(Variable(option1,Name(name1), Type(TomType(type1),tlType1@TLType[])),
                     Variable(option2,Name(name2), Type(TomType(type2),tlType2@TLType[])),
                     tlCode@TL[], _) -> {
        String returnType, argType;
        if(Flags.strictType) {
          returnType = getTLType(getUniversalType());
          argType = getTLCode(tlType1);
        } else {
          returnType = getTLType(getUniversalType());
          argType = getTLType(getUniversalType());
        }
      
        generateTargetLanguage(out,deep, genDecl(returnType,
                               "tom_get_element", type1,
                               new String[] {
                                 argType, name1,
                                 getTLType(getIntType()), name2
                               },
                               tlCode));
        return;
      }
      
      GetSizeDecl(Variable(option1,Name(name1), Type(TomType(type),tlType@TLType[])),
                  tlCode@TL[], _) -> {
        String argType;
        if(Flags.strictType) {
          argType = getTLCode(tlType);
        } else {
          argType = getTLType(getUniversalType());
        }
        
        generateTargetLanguage(out,deep,
                               genDecl(getTLType(getIntType()),
                                       "tom_get_size", type,
                                       new String[] { argType, name1 },
                                       tlCode));
        return;
      }
      
      MakeEmptyArray(Name(opname),
                     Variable(option1,Name(name1), Type(TomType(type1),_)),
                     tlCode@TL[], _) -> {
        generateTargetLanguage(out,deep, genDecl(getTLType(getUniversalType()), "tom_make_empty", opname,
                                   new String[] {
                                     getTLType(getIntType()), name1,
                                   },
                                   tlCode));
        return;
      }

      MakeAddArray(Name(opname),
                   Variable(option1,Name(name1), fullEltType@Type(TomType(type1),tlType1@TLType[])),
                   Variable(option2,Name(name2), fullArrayType@Type(TomType(type2),tlType2@TLType[])),
                  tlCode@TL[], _) -> {

        String returnType, argListType,argEltType;
        if(Flags.strictType) {
          argEltType  = getTLCode(tlType1);
          argListType = getTLCode(tlType2);
          returnType  = argListType;

        } else {
          argEltType  = getTLType(getUniversalType());
          argListType = getTLType(getUniversalType());
          returnType  = argListType;
        }

        generateTargetLanguage(out,deep,
                               genDecl(argListType,
                                       "tom_make_append", opname,
                                       new String[] {
                                         argEltType, name1,
                                         argListType, name2
                                       },
                                       tlCode));
        generateTargetLanguage(out,deep, genDeclArray(opname, fullArrayType, fullEltType));
        return;
      }

      MakeDecl(Name(opname), returnType, argList, tlCode@TL[], _) -> {
        generateTargetLanguage(out,deep, genDeclMake(opname, returnType, argList, tlCode));
        return;
      }
      
      TypeTermDecl[keywordList=declList] -> { 
        TomTerm term;
        while(!declList.isEmpty()) {
          term = declList.getHead();
          %match (TomTerm term){
            DeclarationToTomTerm(declaration) -> {generateDeclaration(out, deep, declaration);}
          }
          declList = declList.getTail();
        }
        return;
      }

      TypeListDecl[keywordList=declList] -> { 
        TomTerm term;
        while(!declList.isEmpty()) {
          term = declList.getHead();
          %match (TomTerm term){
            DeclarationToTomTerm(declaration) -> {generateDeclaration(out, deep, declaration);}
          }
          declList = declList.getTail();
        }
        return;
      }

      TypeArrayDecl[keywordList=declList] -> { 
        TomTerm term;
        while(!declList.isEmpty()) {
          term = declList.getHead();
          %match (TomTerm term){
            DeclarationToTomTerm(declaration) -> {generateDeclaration(out, deep, declaration);}
          }
          declList = declList.getTail();
        }
        return;
      }

      t -> {
        System.out.println("Cannot generate code for declaration: " + t);
        System.exit(1);
      }
    }
  }

  public void generateList(OutputCode out, int deep, TomList subject)
    throws IOException {
      //%variable
    if(subject.isEmpty()) {
      return;
    }

    TomTerm t = subject.getHead();
    TomList l = subject.getTail(); 
    generate(out,deep,t);
    generateList(out,deep,l);
  }

  public void generateOptionList(OutputCode out, int deep, OptionList subject)
    throws IOException {
      //%variable
    if(subject.isEmptyOptionList()) {
      return;
    }

    Option t = subject.getHead();
    OptionList l = subject.getTail(); 
    generateOption(out,deep,t);
    generateOptionList(out,deep,l);
  }

  public void generateSlotList(OutputCode out, int deep, SlotList slotList)
    throws IOException {
    while ( !slotList.isEmptySlotList() ) {
      generateDeclaration(out, deep, slotList.getHeadSlotList().getSlotDecl());
      slotList = slotList.getTailSlotList();
    }
  }
  
  // ------------------------------------------------------------
  private TargetLanguage genDecl(String returnType,
                        String declName,
                        String suffix,
                        String args[],
                        TargetLanguage tlCode) {
    String s = "";
    if(!Flags.genDecl) { return null; }

    if(Flags.cCode) {
      s = "#define " + declName + "_" + suffix + "(";
      for(int i=0 ; i<args.length ; ) {
        s+= args[i+1];
        i+=2;
        if(i<args.length) {
          s+= ", ";
        }
      }
      s += ") (" + tlCode.getCode() +")";
    } else if(Flags.jCode) {
      String modifier ="public ";
      if(Flags.staticFunction) {
        modifier +="static ";
      }

      s = modifier + returnType + " " + declName + "_" + suffix + "(";
      for(int i=0 ; i<args.length ; ) {
        s+= args[i] + " " + args[i+1];
        i+=2;
        if(i<args.length) {
          s+= ", ";
        }
      } 
      s += ") { return " + tlCode.getCode() + "; }";
    } else if(Flags.eCode) {
      s = declName + "_" + suffix + "(";
      for(int i=0 ; i<args.length ; ) {
        s+= args[i+1] + ": " + args[i];
        i+=2;
        if(i<args.length) {
          s+= "; ";
        }
      } 
      s += "): " + returnType + " is do Result := " + tlCode.getCode() + "end;";
    }
    if(tlCode.isTL())
      return `TL(s, tlCode.getStart(), tlCode.getEnd());
    else
      return `ITL(s);
  }

  
  private TargetLanguage genDeclMake(String opname, TomType returnType, TomList argList, TargetLanguage tlCode) {
    //%variable
    String s = "";
    if(!Flags.genDecl) { return null; }

    if(Flags.cCode) {
        //System.out.println("genDeclMake: not yet implemented for C");
        //System.exit(1);
      s = "#define tom_make_" + opname + "(";
      while(!argList.isEmpty()) {
        TomTerm arg = argList.getHead();
        matchBlock: {
          %match(TomTerm arg) {
            Variable[astName=Name(name)] -> {
              s += name;
              break matchBlock;
            }
            
            _ -> {
              System.out.println("genDeclMake: strange term: " + arg);
              System.exit(1);
            }
          }
        }
        argList = argList.getTail();
        if(!argList.isEmpty()) {
          s += ", ";
        }
      }
      s += ") (" + tlCode.getCode() + ")";
    } else if(Flags.jCode) {
      String modifier ="public ";
      if(Flags.staticFunction) {
        modifier +="static ";
      }

      s = modifier + getTLType(returnType) + " tom_make_" + opname + "(";
      while(!argList.isEmpty()) {
        TomTerm arg = argList.getHead();
        matchBlock: {
          %match(TomTerm arg) {
            Variable(option,Name(name), Type(TomType(type),tlType@TLType[])) -> {
              s += getTLCode(tlType) + " " + name;
              break matchBlock;
            }
            
            _ -> {
              System.out.println("genDeclMake: strange term: " + arg);
              System.exit(1);
            }
          }
        }
        argList = argList.getTail();
        if(!argList.isEmpty()) {
          s += ", ";
        }
      }
      s += ") { return " + tlCode.getCode() + "; }";
    } else if(Flags.eCode) {
      boolean braces = !argList.isEmpty();
      s = "tom_make_" + opname;
      if(braces) {
        s = s + "(";
      }
      while(!argList.isEmpty()) {
        TomTerm arg = argList.getHead();
        matchBlock: {
          %match(TomTerm arg) {
            Variable(option,Name(name), Type(TomType(type),tlType@TLType[])) -> {
              s += name + ": " + getTLCode(tlType);
              break matchBlock;
            }
            
            _ -> {
              System.out.println("genDeclMake: strange term: " + arg);
              System.exit(1);
            }
          }
        }
        argList = argList.getTail();
        if(!argList.isEmpty()) {
          s += "; ";
        }
      }
      if(braces) {
        s = s + ")";
      }
      s += ": " + getTLType(returnType) + " is do Result := " + tlCode.getCode() + "end;";
    }
    return `TL(s, tlCode.getStart(), tlCode.getEnd());
  }
  
  private TargetLanguage genDeclList(String name, TomType listType, TomType eltType) {
    //%variable
    String s = "";
    if(!Flags.genDecl) { return null; }

    String tomType = getTomType(listType);
    String glType = getTLType(listType);
    String tlEltType = getTLType(eltType);

    String utype = glType;
    String modifier = "";
    if(Flags.eCode) {
      System.out.println("genDeclList: Eiffel code not yet implemented");
    } else if(Flags.jCode) {
      modifier ="public ";
      if(Flags.staticFunction) {
        modifier +="static ";
      }
      if(!Flags.strictType) {
        utype =  getTLType(getUniversalType());
      }
    }

    String listCast = "(" + glType + ")";
    String eltCast = "(" + getTLType(eltType) + ")";
    String make_empty = listCast +  "tom_make_empty_" + name;
    String is_empty = "tom_is_empty_" + tomType;
    String make_insert = listCast + "tom_make_insert_" + name;
    String get_head = eltCast + "tom_get_head_" + tomType;
    String get_tail = listCast + "tom_get_tail_" + tomType;
    String reverse = listCast + "tom_reverse_" + name;

    s+= modifier + utype + " tom_reverse_" + name + "(" + utype + " l) {\n"; 
    s+= "   " + glType + " result = " + make_empty + "();\n"; 
    s+= "    while(!" + is_empty + "(l) ) {\n"; 
    s+= "      result = " + make_insert + "(" + get_head + "(l),result);\n";    
    s+= "      l = " + get_tail + "(l);\n";  
    s+= "    }\n";
    s+= "    return result;\n";
    s+= "  }\n";
    s+= "\n";

    s+= modifier + utype + " tom_insert_list_" + name +  "(" + utype + " l1, " + utype + " l2) {\n";
    s+= "   if(" + is_empty + "(l1)) {\n";
    s+= "    return l2;\n";  
    s+= "   } else if(" + is_empty + "(l2)) {\n";
    s+= "    return l1;\n";  
    s+= "   } else if(" + is_empty + "(" + get_tail + "(l1))) {\n";  
    s+= "    return " + make_insert + "(" + get_head + "(l1),l2);\n";
    s+= "   } else { \n";  
    s+= "    return " + make_insert + "(" + get_head + "(l1),tom_insert_list_" + name +  "(" + get_tail + "(l1),l2));\n";
    s+= "   }\n";
    s+= "  }\n";
    s+= "\n";


    
    s+= modifier + utype + " tom_get_slice_" + name + "(" + utype + " begin, " + utype + " end) {\n"; 
    s+= "   " + glType + " result = " + make_empty + "();\n"; 
    s+= "    while(!tom_terms_equal_" + tomType + "(begin,end)) {\n";
    s+= "      result = " + make_insert + "(" + get_head + "(begin),result);\n";
    s+= "      begin = " + get_tail + "(begin);\n";
    s+="     }\n";
    s+= "    result = " + reverse + "(result);\n";
    s+= "    return result;\n";
    s+= "  }\n";
    
    TargetLanguage resultTL = `ITL(s);
      //If necessary we remove \n code depending on --pretty option
    resultTL = ast().reworkTLCode(resultTL);
    return resultTL;
  }

  private TargetLanguage genDeclArray(String name, TomType listType, TomType eltType) {
    //%variable
    String s = "";
    if(!Flags.genDecl) { return null; }

    String tomType = getTomType(listType);
    String glType = getTLType(listType);
    String tlEltType = getTLType(eltType);
    String utype = glType;
    String modifier = "";
    if(Flags.eCode) {
      System.out.println("genDeclArray: Eiffel code not yet implemented");
    } else if(Flags.jCode) {
      modifier ="public ";
      if(Flags.staticFunction) {
        modifier +="static ";
      }
      if(!Flags.strictType) {
        utype =  getTLType(getUniversalType());
      }
    }

    String listCast = "(" + glType + ")";
    String eltCast = "(" + getTLType(eltType) + ")";
    String make_empty = listCast + "tom_make_empty_" + name;
    String make_append = listCast + "tom_make_append_" + name;
    String get_element = eltCast + "tom_get_element_" + tomType;

    
    s = modifier + utype + " tom_get_slice_" + name +  "(" + utype + " subject, int begin, int end) {\n";
    s+= "   " + glType + " result = " + make_empty + "(end - begin);\n";
    s+= "    while( begin != end ) {\n";
    s+= "      result = " + make_append + "(" + get_element + "(subject, begin),result);\n";
    s+= "      begin++;\n";
    s+="     }\n";
    s+= "    return result;\n";
    s+= "  }\n";
    s+= "\n";
    
    s+= modifier + utype + " tom_append_array_" + name +  "(" + utype + " l2, " + utype + " l1) {\n";
    s+= "    int size1 = tom_get_size_" + tomType + "(l1);\n";
    s+= "    int size2 = tom_get_size_" + tomType + "(l2);\n";
    s+= "    int index;\n";
    s+= "   " + glType + " result = " + make_empty + "(size1+size2);\n";

    s+= "    index=size1;\n";
    s+= "    while(index > 0) {\n";
    s+= "      result = " + make_append + "(" + get_element + "(l1,(size1-index)),result);\n";
    s+= "      index--;\n";
    s+= "    }\n";

    s+= "    index=size2;\n";
    s+= "    while(index > 0) {\n";
    s+= "      result = " + make_append + "(" + get_element + "(l2,(size2-index)),result);\n";
    s+= "      index--;\n";
    s+= "    }\n";
   
    s+= "    return result;\n";
    s+= "  }\n";

    TargetLanguage resultTL = `ITL(s);
      //If necessary we remove \n code depending on --pretty option
    resultTL = ast().reworkTLCode(resultTL);
    return resultTL;
  }
  
  private HashMap getSubtermMap = new HashMap();
  private HashMap getFunSymMap = new HashMap();
  private HashMap isFsymMap = new HashMap();
}
  
