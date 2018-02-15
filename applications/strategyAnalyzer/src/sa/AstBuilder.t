package sa;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.*;

import sa.rule.types.*;
import java.util.*;

/**
 * This class provides an empty implementation of {@link RuleListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public class AstBuilder extends ProgramSyntaxBaseListener {
  %include { rule/Rule.tom }

  private ParseTreeProperty<Object> values = new ParseTreeProperty<Object>();
  private void setValue(ParseTree node, Object value) { values.put(node, value); } 
  public Object getValue(ParseTree node) { return values.get(node); }
  public String getStringValue(ParseTree node) { return (String) getValue(node); }

  /*
   * program
   *  : abstractsyntax (functions)? (strategies)? (trs)? EOF
   *  ;
   */
	@Override public void exitProgram(ProgramSyntaxParser.ProgramContext ctx) { 
    ProductionList as = (ProductionList) getValue(ctx.abstractsyntax()); 
    ProductionList f = `ConcProduction();
    StratDeclList s = `ConcStratDecl();
    Trs t = `EmptyTrs();

    if(ctx.functions()!=null) {
      f = (ProductionList) getValue(ctx.functions()); 
    } 
    if(ctx.strategies()!=null) {
      s = (StratDeclList) getValue(ctx.strategies()); 
    } 
    if(ctx.trs()!= null) {
      t = (Trs) getValue(ctx.trs()); 
    } 
    setValue(ctx,`Program(as,f,s,t));
  }

  /*
   * abstractsyntax
   *   : (ABSTRACT SYNTAX) (typedecl)*
   *   ;
   */
	@Override public void exitAbstractsyntax(ProgramSyntaxParser.AbstractsyntaxContext ctx) {
    setValue(ctx,buildProductionList(ctx.typedecl()));
  }

  /*
   * functions
   *   : (FUNCTIONS) (typedecl)*
   *   ;
   */
	@Override public void exitFunctions(ProgramSyntaxParser.FunctionsContext ctx) { 
    setValue(ctx,buildProductionList(ctx.typedecl()));
  }
	
  /*
   * strategies
   *   : STRATEGIES (stratdecl)*
   *   ;
   */
  @Override public void exitStrategies(ProgramSyntaxParser.StrategiesContext ctx) { 
    setValue(ctx,buildStratDeclList(ctx.stratdecl()));
  }

  /*
   * trs 
   *   : TRS LBRACKET (rwrule (COMMA? rwrule)*) RBRACKET
   *   | TRS (rwrule (COMMA? rwrule)*) 
   *   ;
   */
	@Override public void exitTrs(ProgramSyntaxParser.TrsContext ctx) { 
    RuleList rl = buildRuleList(ctx.rwrule());
    if(ctx.LBRACKET() != null) {
      setValue(ctx,`Otrs(rl));
    } else {
      setValue(ctx,`Trs(rl));
    }
  }

  /*
   * stratdecl
   *   : ID paramlist EQUALS strategy
   *   ;
   */
	@Override public void exitStratdecl(ProgramSyntaxParser.StratdeclContext ctx) {
    String n = getStringValue(ctx.ID());
    ParamList pl = (ParamList) getValue(ctx.paramlist());
    Strat s = (Strat) getValue(ctx.strategy());
    setValue(ctx,`StratDecl(n,pl,s));
  }

	/*
	 * paramlist
   *   : LPAR (param (COMMA param)* )? RPAR
   *   ;
	 */
	@Override public void exitParamlist(ProgramSyntaxParser.ParamlistContext ctx) { 
    setValue(ctx,buildParamList(ctx.param()));
  }

	/*
	 * param
   *   : ID
   *   ;
	 */
	@Override public void exitParam(ProgramSyntaxParser.ParamContext ctx) {
    String p = getStringValue(ctx.ID());
    setValue(ctx,`Param(p));
  }

  /*
   * typedecl
   *   : typename=id equals alts=alternatives
   *   ;
   */
	@Override public void exitTypedecl(ProgramSyntaxParser.TypedeclContext ctx) { 
  }

  /*
   * alternatives
   *   : (alt)? opdecl (alt opdecl)* 
   *   ;
   */
	@Override public void exitAlternatives(ProgramSyntaxParser.AlternativesContext ctx) { 
  }

  /*
   * opdecl
   *   : id fieldlist
   *   ;
   */
	@Override public void exitOpdecl(ProgramSyntaxParser.OpdeclContext ctx) { 
  }

  /*
   * fieldlist
   *   : lpar (field (comma field)* )? rpar 
   *   ;
   */
	@Override public void exitFieldlist(ProgramSyntaxParser.FieldlistContext ctx) { }

  /*
   * field
   *   : type 
   *   ;
   */
	@Override public void exitField(ProgramSyntaxParser.FieldContext ctx) { }

  /*
   * type
   *   : id 
   *   ;
   */
	@Override public void exitType(ProgramSyntaxParser.TypeContext ctx) { }

  /*
   * strategy
   *   : s1=elementarystrategy (
   *        semicolon s2=strategy
   *      | choice s3=strategy
   *      )?
   *   | lbrace (rwrule (comma? rwrule)*) rbrace 
   *   | lbracket (rwrule (comma? rwrule)*) rbraCKET 
   *   ;
   */
	@Override public void exitStrategy(ProgramSyntaxParser.StrategyContext ctx) { }

  /*
   * elementarystrategy
   *   : identity 
   *   | fail 
   *   | lpar strategy rpar 
   *   | all lpar strategy rpar 
   *   | one lpar strategy rpar 
   *   | mu id dot lpar strategy rpar 
   *   | id lpar (strategy (comma strategy)*)? rPAR
   *   | id 
   *   ;
   */
	@Override public void exitElementarystrategy(ProgramSyntaxParser.ElementarystrategyContext ctx) { }

  /*
   * rwrule
   *   : pattern arrow term (if cond=condition)?
   *   | id arrow term (if cond=condition)?
   *   ;
   */
	@Override public void exitRwrule(ProgramSyntaxParser.RwruleContext ctx) { }

  /*
   * condition
   *   : p1=term doubleequals p2=term
   *   ;
   */
	@Override public void exitCondition(ProgramSyntaxParser.ConditionContext ctx) { }

  /*
   * pattern
   *   : id lpar (term (comma term)*)? rpar 
   *   | '!' term 
   *   ;
   */
	@Override public void exitPattern(ProgramSyntaxParser.PatternContext ctx) { }

  /*
   * term
   *   : pattern
   *   | id 
   *   | builtin
   *   ;
   */
	@Override public void exitTerm(ProgramSyntaxParser.TermContext ctx) { }

  /*
   * builtin
   *   : int 
   *   ;
   */
	@Override public void exitBuiltin(ProgramSyntaxParser.BuiltinContext ctx) { }

  /*
   * symbol
   *   : id colon int 
   *   ;
   */
	@Override public void exitSymbol(ProgramSyntaxParser.SymbolContext ctx) { }


	//@Override public void exiteveryrule(parserRuleContext ctx) { }
	//@Override public void visitterminal(terminalNode node) { }
	//@Override public void visiterrornode(errorNode node) { }

  private ProductionList buildProductionList(List<? extends ParserRuleContext> ctx) { 
    ProductionList res = `ConcProduction(); 
    if(ctx != null) { 
      for(ParserRuleContext e:ctx) { 
        res = `ConcProduction(res*, (Production)getValue(e)); 
      } 
    } 
    return res; 
  } 
  
  private StratDeclList buildStratDeclList(List<? extends ParserRuleContext> ctx) { 
    StratDeclList res = `ConcStratDecl(); 
    if(ctx != null) { 
      for(ParserRuleContext e:ctx) { 
        res = `ConcStratDecl(res*, (StratDecl)getValue(e)); 
      } 
    } 
    return res; 
  } 

  private RuleList buildRuleList(List<? extends ParserRuleContext> ctx) { 
    RuleList res = `ConcRule(); 
    if(ctx != null) { 
      for(ParserRuleContext e:ctx) { 
        res = `ConcRule(res*, (Rule)getValue(e)); 
      } 
    } 
    return res; 
  } 
  
  private ParamList buildParamList(List<? extends ParserRuleContext> ctx) { 
    ParamList res = `ConcParam(); 
    if(ctx != null) { 
      for(ParserRuleContext e:ctx) { 
        res = `ConcParam(res*, (Param)getValue(e)); 
      } 
    } 
    return res; 
  } 
}
