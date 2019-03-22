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
  public Object getValue(ParseTree node) { 
    return (node==null)?null:values.get(node); 
  }

  private ParseTreeProperty<Object> values2 = new ParseTreeProperty<Object>();
  private void setValue2(ParseTree node, Object value) { values2.put(node, value); } 
  public Object getValue2(ParseTree node) { return values2.get(node); }

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
    String n = ctx.ID().getText();
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
    setValue(ctx,`Param(ctx.ID().getText()));
  }

  /*
   * typedecl
   *   : ID EQUALS alternatives
   *   ;
   */
  @Override public void enterTypedecl(ProgramSyntaxParser.TypedeclContext ctx) { 
    setValue2(ctx, ctx.ID().getText());
  }

	@Override public void exitTypedecl(ProgramSyntaxParser.TypedeclContext ctx) { 
    AlternativeList l = (AlternativeList) getValue(ctx.alternatives());
    setValue(ctx,`SortType(GomType(ctx.ID().getText()),l));
  }

  /*
   * alternatives
   *   : (ALT)? opdecl (ALT opdecl)* 
   *   ;
   */
	@Override public void enterAlternatives(ProgramSyntaxParser.AlternativesContext ctx) {
    setValue2(ctx, (String)getValue2(ctx.getParent()));
  }

	@Override public void exitAlternatives(ProgramSyntaxParser.AlternativesContext ctx) {
    setValue(ctx,buildAlternativeList(ctx.opdecl()));
  }

  /*
   * opdecl
   *   : ID fieldlist
   *   ;
   */
	@Override public void exitOpdecl(ProgramSyntaxParser.OpdeclContext ctx) { 
   GomType type = `GomType((String)getValue2(ctx.getParent()));
   setValue(ctx, `Alternative(ctx.ID().getText(), (FieldList)getValue(ctx.fieldlist()), type));
  }

  /*
   * fieldlist
   *   : LPAR (field (COMMA field)* )? RPAR 
   *   ;
   */
	@Override public void exitFieldlist(ProgramSyntaxParser.FieldlistContext ctx) {
    FieldList fields = `ConcField();
    for(ParserRuleContext e : ctx.field()) {
      fields = `ConcField((Field)getValue(e), fields*);
    }
    setValue(ctx, fields.reverse());
  }

  /*
   * field
   *   : type 
   *   ;
   */
	@Override public void exitField(ProgramSyntaxParser.FieldContext ctx) {
    setValue(ctx, `UnamedField((GomType)getValue(ctx.type())));
  }

  /*
   * type
   *   : ID 
   *   ;
   */
	@Override public void exitType(ProgramSyntaxParser.TypeContext ctx) {
    setValue(ctx, `GomType(ctx.ID().getText()));
  }

  /*
   * strategy
   *   : s1=elementarystrategy (
   *        SEMICOLON s2=strategy
   *      | CHOICE s3=strategy
   *      )?
   *   | LBRACE (rwrule (COMMA? rwrule)*) RBRACE 
   *   | LBRACKET (rwrule (COMMA? rwrule)*) RBRACKET 
   *   ;
   */
  @Override public void exitStrategy(ProgramSyntaxParser.StrategyContext ctx) { 
    if(ctx.s1 != null) {
      Strat s1 = (Strat) getValue(ctx.s1);
      if(ctx.s2 != null) {
        Strat s2 = (Strat) getValue(ctx.s2);
        setValue(ctx, `StratSequence(s1,s2));
      } else if(ctx.s3 != null) {
        Strat s3 = (Strat) getValue(ctx.s3);
        setValue(ctx, `StratChoice(s1,s3));
      } else {
        setValue(ctx, s1);
      }
    } else {
      RuleList rl = buildRuleList(ctx.rwrule());
      if(ctx.LBRACKET() != null) {
        setValue(ctx,`StratTrs(Otrs(rl)));
      } else {
        setValue(ctx,`StratTrs(Trs(rl)));
      }
    }
  }

  /*
   * elementarystrategy
   *   : IDENTITY 
   *   | FAIL 
   *   | LPAR s=strategy RPAR 
   *   | ALL LPAR all=strategy RPAR 
   *   | ONE LPAR one=strategy RPAR 
   *   | MU ID DOT LPAR mu=strategy RPAR 
   *   | ID LPAR (strategy (COMMA strategy)*)? RPAR
   *   | name=ID 
   *   ;
   */
  @Override public void exitElementarystrategy(ProgramSyntaxParser.ElementarystrategyContext ctx) { 
    if(ctx.IDENTITY() != null) {
      setValue(ctx,`StratIdentity());
    } else if(ctx.FAIL() != null) {
      setValue(ctx,`StratFail());
    } else if(ctx.s != null) {
      setValue(ctx,ctx.s);
    } else if(ctx.all != null) {
      setValue(ctx,`StratAll((Strat)getValue(ctx.all)));
    } else if(ctx.one != null) {
      setValue(ctx,`StratOne((Strat)getValue(ctx.one)));
    } else if(ctx.mu != null) {
      setValue(ctx,`StratMu(ctx.ID().getText(), (Strat)getValue(ctx.mu)));
    } else if(ctx.name != null) {
      setValue(ctx,`StratName(ctx.name.getText()));
    } else {
      StratList l = `ConcStrat();
      for(ParserRuleContext e : ctx.strategy()) {
        l = `ConcStrat((Strat)getValue(e), l*);
      }
      setValue(ctx,`StratAppl(ctx.ID().getText(), l.reverse()));
    }
  }

  /*
   * rwrule
   *   : pattern ARROW term (IF condition)?
   *   | ID ARROW term (IF condition)?
   *   ;
   */
	@Override public void exitRwrule(ProgramSyntaxParser.RwruleContext ctx) {
    Term pattern = (Term) getValue(ctx.pattern());
    Term term = (Term) getValue(ctx.term());
    Condition cond = (Condition) getValue(ctx.condition());

    if(cond == null) {
      if(pattern != null) {
        setValue(ctx,`Rule(pattern,term));
      } else {
        setValue(ctx,`Rule(Var(ctx.ID().getText()),term));
      }
    } else {
      if(pattern != null) {
        setValue(ctx,`ConditionalRule(pattern,term,cond));
      } else {
        setValue(ctx,`ConditionalRule(Var(ctx.ID().getText()),term,cond));
      }
    }
  }

  /*
   * condition
   *   : p1=term DOUBLEEQUALS p2=term
   *   ;
   */
	@Override public void exitCondition(ProgramSyntaxParser.ConditionContext ctx) {
    Term p1 = (Term) getValue(ctx.p1);
    Term p2 = (Term) getValue(ctx.p2);
    setValue(ctx,`CondEquals(p1,p2));
  }

  /*
   * pattern
   *   : ID LPAR (term (COMMA term)*)? RPAR 
   *   | '!' anti=term 
   *   | p1=pattern '+' p2=pattern 
   *   ;
   */
  @Override public void exitPattern(ProgramSyntaxParser.PatternContext ctx) { 
    Term anti = (Term) getValue(ctx.anti);
    Term p1 = (Term) getValue(ctx.p1);
    Term p2 = (Term) getValue(ctx.p2);
    Term pa = (Term) getValue(ctx.pa);

    // constructor pattern or aliased pattern
    if(ctx.ID() != null) {
      if(pa != null){
        setValue(ctx,`At(Var(ctx.ID().getText()),pa));
      }else{
        TermList l = `TermList();
        for(ParserRuleContext e : ctx.term()) {
          l = `TermList((Term)getValue(e), l*);
        }
        setValue(ctx,`Appl(ctx.ID().getText(),l.reverse()));
      }
    } else { // anti-pattern or sum pattern
      if(anti != null){
        setValue(ctx,`Anti(anti));
      }else{
        AddList plus = `ConcAdd();
        plus = `ConcAdd(p2,plus*);
        plus = `ConcAdd(p1,plus*);
        setValue(ctx,`Add(plus));
      }
    }
  }

  /*
   * term
   *   : pattern
   *   | ID 
   *   | builtin
   *   ;
   */
	@Override public void exitTerm(ProgramSyntaxParser.TermContext ctx) {
    if(ctx.pattern() != null) {
      setValue(ctx,(Term)getValue(ctx.pattern()));
    } else if(ctx.ID() != null) {
      setValue(ctx,`Var(ctx.ID().getText()));
    } else {
      setValue(ctx,(Term)getValue(ctx.builtin()));
    }
  }

  /*
   * builtin
   *   : INT 
   *   ;
   */
	@Override public void exitBuiltin(ProgramSyntaxParser.BuiltinContext ctx) {
      setValue(ctx, `BuiltinInt(Integer.parseInt(ctx.INT().getText())));
  }

  /*
   * symbol
   *   : ID COLON INT 
   *   ;
   */
	@Override public void exitSymbol(ProgramSyntaxParser.SymbolContext ctx) { 
    setValue(ctx, `Symbol(ctx.ID().getText(),Integer.parseInt(ctx.INT().getText())));
  }



  private ProductionList buildProductionList(List<? extends ParserRuleContext> ctx) { 
    ProductionList res = `ConcProduction(); 
    if(ctx != null) { 
      for(ParserRuleContext e:ctx) { 
        res = `ConcProduction((Production)getValue(e), res*);
      } 
    } 
    return res.reverse(); 
  } 
  
  private StratDeclList buildStratDeclList(List<? extends ParserRuleContext> ctx) { 
    StratDeclList res = `ConcStratDecl(); 
    if(ctx != null) { 
      for(ParserRuleContext e:ctx) { 
        res = `ConcStratDecl((StratDecl)getValue(e),res*);
      } 
    } 
    return res.reverse(); 
  } 

  private RuleList buildRuleList(List<? extends ParserRuleContext> ctx) { 
    RuleList res = `ConcRule(); 
    if(ctx != null) { 
      for(ParserRuleContext e:ctx) { 
        res = `ConcRule((Rule)getValue(e),res*);
      } 
    } 
    return res.reverse(); 
  } 
  
  private ParamList buildParamList(List<? extends ParserRuleContext> ctx) { 
    ParamList res = `ConcParam(); 
    if(ctx != null) { 
      for(ParserRuleContext e:ctx) { 
        res = `ConcParam((Param)getValue(e),res*);
      } 
    } 
    return res.reverse(); 
  } 

  private AlternativeList buildAlternativeList(List<? extends ParserRuleContext> ctx) { 
    AlternativeList res = `ConcAlternative(); 
    if(ctx != null) { 
      for(ParserRuleContext e:ctx) { 
        res = `ConcAlternative((Alternative)getValue(e),res*);
      } 
    } 
    return res.reverse(); 
  } 
}
