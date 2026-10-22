/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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
 
 **/
package tom.engine.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import tom.engine.TomBase;
import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.tools.OutputCode;
import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;

/**
 * Go backend. The code is written on one logical line (as the other
 * backends do without -pretty), so every statement ends with ';' and
 * every block with '};' -- except before 'else'.
 */
public class GoGenerator extends GenericGenerator {

  // ------------------------------------------------------------
     private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.InstructionList  tom_append_list_concInstruction( tom.engine.adt.tominstruction.types.InstructionList l1,  tom.engine.adt.tominstruction.types.InstructionList  l2) {     if( l1.isEmptyconcInstruction() ) {       return l2;     } else if( l2.isEmptyconcInstruction() ) {       return l1;     } else if(  l1.getTailconcInstruction() .isEmptyconcInstruction() ) {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,tom_append_list_concInstruction( l1.getTailconcInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.InstructionList  tom_get_slice_concInstruction( tom.engine.adt.tominstruction.types.InstructionList  begin,  tom.engine.adt.tominstruction.types.InstructionList  end, tom.engine.adt.tominstruction.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcInstruction()  ||  (end== tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( begin.getHeadconcInstruction() ,( tom.engine.adt.tominstruction.types.InstructionList )tom_get_slice_concInstruction( begin.getTailconcInstruction() ,end,tail)) ;   }   

  // ------------------------------------------------------------

  /** receiver of the methods generated for a %strategy */
  public static final String RECEIVER = "tom__strat";

  /** name of the %strategy being emitted by buildClass, null otherwise */
  private String currentClass = null;

  /**
   * parameters of the %strategy being emitted by buildClass: they are
   * struct fields, so a use in a generated method reads RECEIVER.name
   * (unless a Let or a method parameter of the same name shadows it)
   */
  private Set<String> classFields = new HashSet<String>();

  /**
   * Go type of the variable whose value is being generated by
   * buildAssign/buildLet/buildLetRef, null otherwise: used by
   * buildExpConditional when the branches do not tell the type
   */
  private String conditionalType = null;

  public GoGenerator(OutputCode output, OptionManager optionManager,
                     SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
  }

  /** Go spelling of a TomType; "any" when no target type is known */
  private String goType(TomType type) {
    { /* unamed block */{ /* unamed block */if ( (type instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )type) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch107_1= (( tom.engine.adt.tomtype.types.TomType )type).getTlType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch107_1) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

 return  tomMatch107_1.getString() ; }}}}}

    return "any";
  }

  private String goTLCode(TargetLanguageType tlType) {
    { /* unamed block */{ /* unamed block */if ( (tlType instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tlType) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

 return  (( tom.engine.adt.tomtype.types.TargetLanguageType )tlType).getString() ; }}}}

    return "any";
  }

  /** Go type of a sort name: builtin, declared %typeterm, or any */
  private String sortGoType(String sort, String moduleName) {
    SymbolTable st = getSymbolTable(moduleName);
    if(st.isBuiltinType(sort)) {
      return goType(st.getBuiltinType(sort));
    }
    TomType t = st.getType(sort);
    return (t == null) ? "any" : goType(t);
  }

  /** Go type of a TomType: its target type, else the one of its sort, else null */
  private String knownGoType(TomType type, String moduleName) {
    { /* unamed block */{ /* unamed block */if ( (type instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )type) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch109_1= (( tom.engine.adt.tomtype.types.TomType )type).getTlType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch109_1) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

 return  tomMatch109_1.getString() ; }}}}}

    String sort = TomBase.getTomType(type);
    if(sort == null) {
      return null;
    }
    SymbolTable st = getSymbolTable(moduleName);
    if(st.isBuiltinType(sort)) {
      return goType(st.getBuiltinType(sort));
    }
    TomType t = st.getType(sort);
    if(t != null) {
      { /* unamed block */{ /* unamed block */if ( (t instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )t) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch110_1= (( tom.engine.adt.tomtype.types.TomType )t).getTlType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch110_1) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

 return  tomMatch110_1.getString() ; }}}}}

    }
    return null;
  }

  /** Go type of the codomain of an operator, null when unknown */
  private String codomainGoType(TomName opName, String moduleName) {
    { /* unamed block */{ /* unamed block */if ( (opName instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )opName) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        TomSymbol symbol = getSymbolTable(moduleName).getSymbolFromName( (( tom.engine.adt.tomname.types.TomName )opName).getString() );
        if(symbol != null) {
          return knownGoType(TomBase.getSymbolCodomain(symbol),moduleName);
        }
      }}}}


    return null;
  }

  /**
   * Go type of an expression, when it can be read from the expression
   * itself (the shapes built for Conditional by the variadic and array
   * generators), null otherwise
   */
  private String expressionGoType(Expression exp, String moduleName) {
    { /* unamed block */{ /* unamed block */if ( (exp instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )exp) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) { tom.engine.adt.code.types.BQTerm  tomMatch112_1= (( tom.engine.adt.tomexpression.types.Expression )exp).getAstTerm() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch112_1) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {

 return knownGoType( tomMatch112_1.getAstType() ,moduleName); }}}}{ /* unamed block */if ( (exp instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )exp) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) { tom.engine.adt.code.types.BQTerm  tomMatch112_8= (( tom.engine.adt.tomexpression.types.Expression )exp).getAstTerm() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch112_8) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {
 return codomainGoType( tomMatch112_8.getAstName() ,moduleName); }}}}{ /* unamed block */if ( (exp instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )exp) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) { tom.engine.adt.code.types.BQTerm  tomMatch112_15= (( tom.engine.adt.tomexpression.types.Expression )exp).getAstTerm() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch112_15) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {
 return codomainGoType( tomMatch112_15.getAstName() ,moduleName); }}}}{ /* unamed block */if ( (exp instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )exp) instanceof tom.engine.adt.tomexpression.types.expression.GetHead) ) {
 return knownGoType( (( tom.engine.adt.tomexpression.types.Expression )exp).getCodomain() ,moduleName); }}}{ /* unamed block */if ( (exp instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )exp) instanceof tom.engine.adt.tomexpression.types.expression.GetTail) ) {
 return codomainGoType( (( tom.engine.adt.tomexpression.types.Expression )exp).getOpname() ,moduleName); }}}{ /* unamed block */if ( (exp instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )exp) instanceof tom.engine.adt.tomexpression.types.expression.GetElement) ) {
 return knownGoType( (( tom.engine.adt.tomexpression.types.Expression )exp).getCodomain() ,moduleName); }}}}

    return null;
  }

  /** generates exp with conditionalType set to goType (restored afterwards) */
  private void generateTypedExpression(int deep, String goType, Expression exp, String moduleName) throws IOException {
    String saved = conditionalType;
    conditionalType = goType;
    try {
      generateExpression(deep,exp,moduleName);
    } finally {
      conditionalType = saved;
    }
  }

  /*
   * Hook code (Code) is written without its surrounding whitespace: Go
   * inserts a ';' at a newline that follows an identifier, a literal or a
   * closing bracket, so "return \n e \n" or "( e \n )" would not parse.
   */
  public void generateExpression(int deep, Expression subject, String moduleName) throws IOException {
    if(subject.isCode()) {
      output.write(subject.getCode().trim());
      return;
    }
    super.generateExpression(deep,subject,moduleName);
  }

  // ---------------------------------------------------------------- instructions

  protected void buildAssign(int deep, BQTerm var, OptionList optionList, Expression exp, String moduleName) throws IOException {
    generateBQTerm(deep,var,moduleName);
    output.write(" = ");
    String varType = null;
    { /* unamed block */{ /* unamed block */if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {

 varType = knownGoType( (( tom.engine.adt.code.types.BQTerm )var).getAstType() ,moduleName); }}}}

    generateTypedExpression(deep,varType,exp,moduleName);
    output.write("; ");
  }

  protected void buildAssignArray(int deep, BQTerm var, OptionList optionList, BQTerm index,
      Expression exp, String moduleName) throws IOException {
    generateArray(deep,var,index,moduleName);
    output.write(" = ");
    generateExpression(deep,exp,moduleName);
    output.write("; ");
  }

  protected void buildComment(int deep, String text) throws IOException {
    output.write("/* " + text.replace("*/","* /") + " */ ");
  }

  protected void buildDoWhile(int deep, Instruction succes, Expression exp, String moduleName) throws IOException {
    output.write("for { ");
    generateInstruction(deep+1,succes,moduleName);
    output.write(" if !(");
    generateExpression(deep,exp,moduleName);
    output.write(") { break; }; }; ");
  }

  protected void buildWhileDo(int deep, Expression exp, Instruction succes, String moduleName) throws IOException {
    output.write("for ");
    generateExpression(deep,exp,moduleName);
    output.write(" { ");
    generateInstruction(deep+1,succes,moduleName);
    output.write(" }; ");
  }

  protected void buildIf(int deep, Expression exp, Instruction succes, String moduleName) throws IOException {
    output.write("if ");
    generateExpression(deep,exp,moduleName);
    output.write(" { ");
    generateInstruction(deep+1,succes,moduleName);
    output.write(" }; ");
  }

  protected void buildIfWithFailure(int deep, Expression exp, Instruction succes, Instruction failure, String moduleName) throws IOException {
    output.write("if ");
    generateExpression(deep,exp,moduleName);
    output.write(" { ");
    generateInstruction(deep+1,succes,moduleName);
    output.write(" } else { ");
    generateInstruction(deep+1,failure,moduleName);
    output.write(" }; ");
  }

  /*
   * When the host line before a %match ends with a backquote term
   * (x := f() written with a backquote, then %match), the whitespace-only water
   * between them yields no host block and the newline ending the Go
   * statement is lost: a "; " separator is written instead (valid after a
   * statement, a "{" or a newline, and it keeps the line numbering).
   */
  public void generateInstruction(int deep, Instruction subject, String moduleName) throws IOException {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.UnamedBlock) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch114_1= (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ;if ( (((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch114_1) instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || ((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch114_1) instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) {if (!( tomMatch114_1.isEmptyconcInstruction() )) {if ( ((( tom.engine.adt.tominstruction.types.Instruction ) tomMatch114_1.getHeadconcInstruction() ) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledMatch) ) {


        output.write("; ");
      }}}}}}}


    super.generateInstruction(deep, subject, moduleName);
  }

  protected void buildInstructionSequence(int deep, InstructionList instructionList, String moduleName) throws IOException {
    generateInstructionList(deep, instructionList, moduleName);
  }

  /* Go rejects unused variables: every Let is followed by "_ = x" */
  protected void buildLet(int deep, BQTerm var, OptionList optionList, TargetLanguageType tlType,
      Expression exp, Instruction body, String moduleName) throws IOException {
    output.write("var ");
    String name = super.getVariableName(var);
    output.write(deep,name);
    output.write(" " + goTLCode(tlType) + " = ");
    generateTypedExpression(deep,goTLCode(tlType),exp,moduleName);
    output.write("; _ = " + name + "; ");
    boolean shadows = classFields.remove(name);
    generateInstruction(deep,body,moduleName);
    if(shadows) { classFields.add(name); }
  }

  protected void buildLetRef(int deep, BQTerm var, OptionList optionList, TargetLanguageType tlType,
      Expression exp, Instruction body, String moduleName) throws IOException {
    buildLet(deep,var,optionList,tlType,exp,body,moduleName);
  }

  /* a labelled block is a one-shot labelled loop: "break label" leaves it */
  protected void buildNamedBlock(int deep, String blockName, InstructionList instList, String moduleName) throws IOException {
    output.write(blockName + ": for { ");
    generateInstructionList(deep+1,instList,moduleName);
    output.write(" break " + blockName + "; }; ");
  }

  protected void buildUnamedBlock(int deep, InstructionList instList, String moduleName) throws IOException {
    output.write("{ ");
    generateInstructionList(deep+1,instList,moduleName);
    output.write(" }; ");
  }

  protected void buildReturn(int deep, BQTerm exp, String moduleName) throws IOException {
    output.write("return ");
    generateBQTerm(deep,exp,moduleName);
    output.write("; ");
  }

  // ---------------------------------------------------------------- expressions

  protected void buildExpTrue(int deep) throws IOException {
    output.write(" true ");
  }

  protected void buildExpFalse(int deep) throws IOException {
    output.write(" false ");
  }

  protected void buildExpBottom(int deep, TomType type, String moduleName) throws IOException {
    SymbolTable st = getSymbolTable(moduleName);
    String sType = TomBase.getTomType(type);
    if(sType != null && (st.isIntType(sType) || st.isLongType(sType) || st.isCharType(sType)
          || st.isFloatType(sType) || st.isDoubleType(sType))) {
      output.write(" 0 ");
    } else if(sType != null && st.isBooleanType(sType)) {
      output.write(" false ");
    } else if(sType != null && st.isStringType(sType)) {
      output.write(" \"\" ");
    } else {
      /* zero value of the sort's Go type, whatever it is (pointer, struct, …) */
      String goType = knownGoType(type,moduleName);
      if(goType == null || goType.trim().length() == 0) {
        output.write(" nil ");
      } else {
        output.write(" *new(" + goType.trim() + ") ");
      }
    }
  }

  protected void buildExpNegation(int deep, Expression exp, String moduleName) throws IOException {
    output.write("!(");
    generateExpression(deep,exp,moduleName);
    output.write(")");
  }

  protected void buildExpAnd(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    output.write(" (");
    generateExpression(deep,exp1,moduleName);
    output.write(" && ");
    generateExpression(deep,exp2,moduleName);
    output.write(") ");
  }

  protected void buildExpOr(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    output.write(" (");
    generateExpression(deep,exp1,moduleName);
    output.write(" || ");
    generateExpression(deep,exp2,moduleName);
    output.write(") ");
  }

  /*
   * Go has no ternary operator: an immediately-called closure. Its result
   * type is read from the branches (variadic/array generators build
   * them from typed variables, GetHead/GetTail/GetElement, empty lists);
   * else it is the type of the variable being assigned; else any.
   */
  protected void buildExpConditional(int deep, Expression cond, Expression exp1, Expression exp2, String moduleName) throws IOException {
    String type = expressionGoType(exp2,moduleName);
    if(type == null) {
      type = expressionGoType(exp1,moduleName);
    }
    if(type == null) {
      type = conditionalType;
    }
    if(type == null || type.trim().length() == 0) {
      type = "any";
    }
    output.write("func() " + type + " { if ");
    generateExpression(deep,cond,moduleName);
    output.write(" { return ");
    generateExpression(deep,exp1,moduleName);
    output.write(" }; return ");
    generateExpression(deep,exp2,moduleName);
    output.write(" }()");
  }

  /* any(e).(T) is valid whatever the static type of e */
  protected void buildExpCast(int deep, TargetLanguageType tlType, Expression exp, String moduleName) throws IOException {
    { /* unamed block */{ /* unamed block */if ( (tlType instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tlType) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {


        output.write("any(");
        generateExpression(deep,exp,moduleName);
        output.write(").(" +  (( tom.engine.adt.tomtype.types.TargetLanguageType )tlType).getString()  + ")");
        return;
      }}}}


    generateExpression(deep,exp,moduleName);
  }

  private boolean useNativeEquality(String sType, String moduleName) {
    SymbolTable st = getSymbolTable(moduleName);
    return sType == null || st.isUnknownType(sType) || st.isBooleanType(sType);
  }

  protected void buildExpEqualTerm(int deep, TomType type, BQTerm exp1, TomTerm exp2, String moduleName) throws IOException {
    String sType = TomBase.getTomType(type);
    if(useNativeEquality(sType,moduleName)) {
      output.write("(");
      generateBQTerm(deep,exp1,moduleName);
      output.write(" == ");
      generateTomTerm(deep,exp2,moduleName);
      output.write(")");
    } else {
      output.write("tom_equal_term_" + sType + "(");
      generateBQTerm(deep,exp1,moduleName);
      output.write(", ");
      generateTomTerm(deep,exp2,moduleName);
      output.write(")");
    }
  }

  protected void buildExpEqualBQTerm(int deep, TomType type, BQTerm exp1, BQTerm exp2, String moduleName) throws IOException {
    String sType = TomBase.getTomType(type);
    if(useNativeEquality(sType,moduleName)) {
      output.write("(");
      generateBQTerm(deep,exp1,moduleName);
      output.write(" == ");
      generateBQTerm(deep,exp2,moduleName);
      output.write(")");
    } else {
      output.write("tom_equal_term_" + sType + "(");
      generateBQTerm(deep,exp1,moduleName);
      output.write(", ");
      generateBQTerm(deep,exp2,moduleName);
      output.write(")");
    }
  }

  // ---------------------------------------------------------------- mapping functions

  protected void genDeclInstr(String returnType, String declName, String suffix,
      String args[], Instruction instr, int deep, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }
    StringBuilder s = new StringBuilder();
    s.append("func " + declName + "_" + suffix + "(");
    for(int i=0 ; i<args.length ; ) {
      s.append(args[i+1] + " " + args[i]);
      i+=2;
      if(i<args.length) {
        s.append(", ");
      }
    }
    s.append(") " + returnType + " { ");
    output.write(s);
    generateInstruction(deep,instr,moduleName);
    output.write(" }; ");
  }

  protected void genDecl(String returnType, String declName, String suffix,
      String args[], TargetLanguage tlCode, String moduleName) throws IOException {
    throw new TomRuntimeException("GoGenerator.genDecl is not used");
  }

  /* equals receives the sort's Go type (not any): mappings call methods on it */
  protected void buildEqualTermDecl(int deep, String varname1, String varname2,
      String type1, String type2, Expression code, String moduleName) throws IOException {
    if(code.isCode()) {
      code = code.setCode(code.getCode().replace("{0}",varname1).replace("{1}",varname2));
    }
    genDeclInstr(goType(getSymbolTable(moduleName).getBooleanType()), "tom_equal_term", type1,
        new String[] { sortGoType(type1,moduleName), varname1, sortGoType(type2,moduleName), varname2 },
         tom.engine.adt.tominstruction.types.instruction.Return.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make(code) ) 
,deep,moduleName);
  }

  protected void genDeclMake(String prefix, String funName, TomType returnType,
      BQTermList argList, Instruction instr, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }
    { /* unamed block */{ /* unamed block */if ( (instr instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )instr) instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch116_1= (( tom.engine.adt.tominstruction.types.Instruction )instr).getExpr() ;if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch116_1) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {


        String ncode =  tomMatch116_1.getCode() ;
        int index = 0;
        { /* unamed block */{ /* unamed block */if ( (argList instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )argList) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )argList) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch117_end_4=(( tom.engine.adt.code.types.BQTermList )argList);do {{ /* unamed block */if (!( tomMatch117_end_4.isEmptyconcBQTerm() )) { tom.engine.adt.code.types.BQTerm  tomMatch117_8= tomMatch117_end_4.getHeadconcBQTerm() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch117_8) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch117_7= tomMatch117_8.getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch117_7) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


            ncode = ncode.replace("{"+index+"}", tomMatch117_7.getString() );
            index++;
          }}}if ( tomMatch117_end_4.isEmptyconcBQTerm() ) {tomMatch117_end_4=(( tom.engine.adt.code.types.BQTermList )argList);} else {tomMatch117_end_4= tomMatch117_end_4.getTailconcBQTerm() ;}}} while(!( (tomMatch117_end_4==(( tom.engine.adt.code.types.BQTermList )argList)) ));}}}}


        instr =  tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction.make( tom.engine.adt.tomexpression.types.expression.Code.make(ncode) ) ;
      }}}}}


    StringBuilder s = new StringBuilder();
    s.append("func " + prefix + funName + "(");
    while(!argList.isEmptyconcBQTerm()) {
      BQTerm arg = argList.getHeadconcBQTerm();
matchBlock: {
      { /* unamed block */{ /* unamed block */if ( (arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch118_1= (( tom.engine.adt.code.types.BQTerm )arg).getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch118_2= (( tom.engine.adt.code.types.BQTerm )arg).getAstType() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch118_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch118_2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch118_8= tomMatch118_2.getTlType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch118_8) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {


          s.append( tomMatch118_1.getString()  + " " + TomBase.getTLCode(tomMatch118_8));
          break matchBlock;
        }}}}}}{ /* unamed block */if ( (arg instanceof tom.engine.adt.code.types.BQTerm) ) {


          throw new TomRuntimeException("genDeclMake: strange term: " + arg);
        }}}


    }
      argList = argList.getTailconcBQTerm();
      if(!argList.isEmptyconcBQTerm()) {
        s.append(", ");
      }
    }
    /* parenthesised: the hook code may start with newlines (line sync), Go would insert ';' after return */
    s.append(") " + goType(returnType) + " { return (");
    output.write(s);
    generateInstruction(0,instr,moduleName);
    output.write(") }; ");
  }

  protected void genDeclList(String name, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(name);
    TomType listType = TomBase.getSymbolCodomain(tomSymbol);
    TomType eltType = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
    String tomType = TomBase.getTomType(listType);
    String t = goType(listType);
    String isEmpty = "tom_is_empty_" + name + "_" + tomType;
    String head = "tom_get_head_" + name + "_" + tomType;
    String tail = "tom_get_tail_" + name + "_" + tomType;
    String cons = "tom_cons_list_" + name;
    String empty = "tom_empty_list_" + name + "()";
    String equal = "tom_equal_term_" + tomType;
    String isConc = "tom_is_fun_sym_" + name;
    String append = "tom_append_list_" + name;
    String slice = "tom_get_slice_" + name;
    StringBuilder s = new StringBuilder();
    s.append("func " + append + "(l1 " + t + ", l2 " + t + ") " + t + " { ");
    s.append("if " + isEmpty + "(l1) { return l2 } else if " + isEmpty + "(l2) { return l1 } ");
    if(listType == eltType) {
      s.append("else if " + isConc + "(l1) { if " + isEmpty + "(" + tail + "(l1)) { return " + cons + "(" + head + "(l1), l2) } else { return "
          + cons + "(" + head + "(l1), " + append + "(" + tail + "(l1), l2)) } } else { return " + cons + "(l1, l2) }; }; ");
    } else {
      s.append("else if " + isEmpty + "(" + tail + "(l1)) { return " + cons + "(" + head + "(l1), l2) } else { return "
          + cons + "(" + head + "(l1), " + append + "(" + tail + "(l1), l2)) }; }; ");
    }
    String headIn = head + "(begin)";
    String tailIn = tail + "(begin)";
    if(listType == eltType) {
      headIn = "func() " + t + " { if " + isConc + "(begin) { return " + head + "(begin) }; return begin }()";
      tailIn = "func() " + t + " { if " + isConc + "(begin) { return " + tail + "(begin) }; return " + empty + " }()";
    }
    s.append("func " + slice + "(begin " + t + ", end " + t + ", tail " + t + ") " + t + " { ");
    s.append("if " + equal + "(begin, end) { return tail } else if " + equal + "(end, tail) && (" + isEmpty + "(end) || "
        + equal + "(end, " + empty + ")) { return begin }; ");
    s.append("return " + cons + "(" + headIn + ", " + slice + "(" + tailIn + ", end, tail)) }; ");
    output.write(s.toString());
  }

  /*
   * get_size returns the Tom int type (int64), but the natural Go hook
   * body is len($l), of type int: convert explicitly.
   */
  protected void buildGetSizeDecl(int deep, TomName opNameAST, String name1, String type,
      Expression code, String moduleName) throws IOException {
    if(code.isCode()) {
      String it = goType(getSymbolTable(moduleName).getIntType());
      String body = code.getCode().trim();
      /* a statement-style body `len($l);` cannot go inside int64(...) */
      while(body.endsWith(";")) {
        body = body.substring(0,body.length()-1).trim();
      }
      code = code.setCode(it + "(" + body + ")");
    }
    super.buildGetSizeDecl(deep,opNameAST,name1,type,code,moduleName);
  }

  protected void genDeclArray(String name, String moduleName) throws IOException {
    if(nodeclMode || !getSymbolTable(moduleName).isUsedSymbolDestructor(name)) {
      return;
    }
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(name);
    TomType listType = TomBase.getSymbolCodomain(tomSymbol);
    String tomType = TomBase.getTomType(listType);
    String t = goType(listType);
    String it = goType(getSymbolTable(moduleName).getIntType());
    String empty = "tom_empty_array_" + name;
    String cons = "tom_cons_array_" + name;
    String elt = "tom_get_element_" + name + "_" + tomType;
    String size = "tom_get_size_" + name + "_" + tomType;
    StringBuilder s = new StringBuilder();
    s.append("func tom_get_slice_" + name + "(subject " + t + ", begin " + it + ", end " + it + ") " + t + " { ");
    s.append("var result " + t + " = " + empty + "(end-begin); for begin != end { result = " + cons + "(" + elt + "(subject, begin), result); begin++ }; return result }; ");
    s.append("func tom_append_array_" + name + "(l2 " + t + ", l1 " + t + ") " + t + " { ");
    s.append("var size1 " + it + " = " + size + "(l1); var size2 " + it + " = " + size + "(l2); var index " + it + "; ");
    s.append("var result " + t + " = " + empty + "(size1+size2); index = size1; ");
    s.append("for index > 0 { result = " + cons + "(" + elt + "(l1, size1-index), result); index-- }; index = size2; ");
    s.append("for index > 0 { result = " + cons + "(" + elt + "(l2, size2-index), result); index-- }; return result }; ");
    output.write(s.toString());
  }

  // ---------------------------------------------------------------- functions and %strategy

  protected void buildFunctionDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    buildMethod(deep,tomName,argList,codomain,instruction,moduleName,false);
  }

  protected void buildMethodDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    buildMethod(deep,tomName,argList,codomain,instruction,moduleName,true);
  }

  private void buildMethod(int deep, String tomName, BQTermList varList, TomType codomain,
      Instruction instruction, String moduleName, boolean isMethod) throws IOException {
    if(isMethod && currentClass != null) {
      output.write("func (" + RECEIVER + " *" + currentClass + ") " + tomName + "(");
    } else {
      output.write("func " + tomName + "(");
    }
    Set<String> savedFields = classFields;
    classFields = new HashSet<String>(classFields);
    while(!varList.isEmptyconcBQTerm()) {
      BQTerm localVar = varList.getHeadconcBQTerm();
matchBlock: {
      { /* unamed block */{ /* unamed block */if ( (localVar instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )localVar) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {


          String name = super.getVariableName((( tom.engine.adt.code.types.BQTerm )localVar));
          output.write(deep,name);
          classFields.remove(name);
          output.write(" " + goType( (( tom.engine.adt.code.types.BQTerm )localVar).getAstType() ));
          break matchBlock;
        }}}{ /* unamed block */if ( (localVar instanceof tom.engine.adt.code.types.BQTerm) ) {


          throw new TomRuntimeException("GoGenerator.buildMethod: strange term: " + localVar);
        }}}


    }
      varList = varList.getTailconcBQTerm();
      if(!varList.isEmptyconcBQTerm()) {
        output.write(", ");
      }
    }
    String ret = goType(codomain);
    output.write(") " + ret + " { ");
    generateInstruction(deep,instruction,moduleName);
    output.write(" }; ");
    classFields = savedFields;
  }

  /*
   * %strategy S(p1:T1,…) extends E -> a struct embedding sl's
   * AbstractStrategyBasic, a constructor tom_new_S, one getter per
   * parameter, an exported VisitLight that converts MustVisit panics back
   * into errors, then the methods generated by the expander.
   * Limitation: Strategy-typed parameters are plain fields, they are not
   * exposed as children (enough for strategies used without mu over them).
   */
  protected void buildClass(int deep, String tomName, TomType extendsType, BQTerm superTerm, Declaration declaration, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    TomTypeList tomTypes = TomBase.getSymbolDomain(tomSymbol);
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> types = new ArrayList<String>();
    int index = 0;
    while(!tomTypes.isEmptyconcTomType()) {
      types.add(goType(tomTypes.getHeadconcTomType()));
      names.add(TomBase.getSlotName(tomSymbol, index).getString());
      tomTypes = tomTypes.getTailconcTomType();
      index++;
    }
    StringBuilder s = new StringBuilder();
    s.append("type " + tomName + " struct { *sl.AbstractStrategyBasic; ");
    for(int i = 0 ; i < names.size() ; i++) {
      s.append(names.get(i) + " " + types.get(i) + "; ");
    }
    s.append("}; func tom_new_" + tomName + "(");
    for(int i = 0 ; i < names.size() ; i++) {
      s.append(names.get(i) + " " + types.get(i) + (i+1 < names.size() ? ", " : ""));
    }
    s.append(") *" + tomName + " { " + RECEIVER + " := &" + tomName + "{");
    for(int i = 0 ; i < names.size() ; i++) {
      s.append(names.get(i) + ": " + names.get(i) + ", ");
    }
    s.append("}; " + RECEIVER + ".AbstractStrategyBasic = sl.NewAbstractStrategyBasic(");
    output.write(s.toString());
    generateBQTerm(deep,superTerm,moduleName);
    output.write("); return " + RECEIVER + " }; ");
    for(int i = 0 ; i < names.size() ; i++) {
      output.write("func (" + RECEIVER + " *" + tomName + ") get" + names.get(i) + "() " + types.get(i)
          + " { return " + RECEIVER + "." + names.get(i) + " }; ");
    }
    output.write("func (" + RECEIVER + " *" + tomName + ") VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { "
        + "defer sl.RecoverVisitFailure(&tom__err); return " + RECEIVER + ".visitLight(v, introspector), nil }; ");
    // environment-based visit (Java AbstractStrategyBasic.visit(Introspector)
    // and AbstractStrategy.visit(T,Introspector) / visit(Environment,Introspector)):
    // Go has no virtual dispatch, so each strategy defines them on itself
    output.write("func (" + RECEIVER + " *" + tomName + ") VisitEnv(introspector sl.Introspector) int { "
        + "return sl.VisitBasic(" + RECEIVER + ", introspector) }; ");
    output.write("func (" + RECEIVER + " *" + tomName + ") Visit(v any, introspector sl.Introspector) (any, error) { "
        + "return sl.VisitStrategy(" + RECEIVER + ", v, introspector) }; ");
    output.write("func (" + RECEIVER + " *" + tomName + ") VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { "
        + "return sl.VisitStrategyWith(" + RECEIVER + ", env, introspector) }; ");
    currentClass = tomName;
    classFields = new HashSet<String>(names);
    generateDeclaration(deep,declaration,moduleName);
    classFields = new HashSet<String>();
    currentClass = null;
  }

  /* a %strategy parameter is a field of the receiver */
  protected String getVariableName(BQTerm var) {
    String name = super.getVariableName(var);
    return classFields.contains(name) ? RECEIVER + "." + name : name;
  }

  protected String getVariableName(TomTerm var) {
    String name = super.getVariableName(var);
    return classFields.contains(name) ? RECEIVER + "." + name : name;
  }

  protected void buildIntrospectorClass(int deep, String tomName, Declaration declaration, String moduleName) throws IOException {
    throw new TomRuntimeException("--genIntrospector is not supported with -go");
  }

  // ---------------------------------------------------------------- %transformation: unsupported

  protected String genResolveIsSortCode(String varName, String resolveStringName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected String genResolveIsFsymCode(String tomName, String varname) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected String genResolveGetSlotCode(String tomName, String varname, String slotName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected void buildResolveClass(String wName, String tName, String extendsName, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected void buildResolveInverseLinks(int deep, String fileFrom, String fileTo, TomNameList resolveNameList, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected void genResolveDeclMake(String prefix, String funName, TomType returnType, BQTermList argList, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected String genResolveMakeCode(String funName, BQTermList argList) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected void buildReferenceClass(int deep, String refname, RefClassTracelinkInstructionList refclassTInstructions, String moduleName) {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected void buildTracelink(int deep, String type, String name, Expression expr, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected void buildTracelinkPopulateResolve(int deep, String refClassName, TomNameList tracedLinks, BQTerm current, BQTerm link, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected void buildResolve(int deep, BQTerm bqterm, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }
}
