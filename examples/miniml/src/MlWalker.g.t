
tree grammar MlWalker;
options {
    tokenVocab=Ml;
    ASTLabelType = Tree;
    //backtrack=true;
}

@members{
  %include { mlsig/mlsig.tom }
}

type returns [mlsig.types.Type t]
     : i=ID { t = `basetype($i.text); }
     | ^(ABS x=type) {t=x;} (^(ABS y=type) {t=`abs(t,y);} )*
     ;

affect returns [mlsig.types.Affect a]
       : ^(AFFECT id=ID t=type v=value) { a = `affect($id.text,t,v); }
       ;

rewrite returns [mlsig.types.Rule r]
        : ^(RARROW lhs=pattern rhs=value) { r = `rule(lhs,rhs); }
        ;

conj returns [mlsig.types.RuleList rl]
@init{ rl = `rulelist(); }
     : ( ^(RULELIST r=rewrite) { rl = `rulelist(rl*,r);} )+
     ;

value returns [mlsig.types.Expr e]
      : id=ID { e = `var($id.text); }
      | ^(CONSTR id=ID tl=value_list) { e = `constr($id.text,tl); }
      | ^(CONSTR id=ID) { e = `constr($id.text,exprlist()); }
      | ^(MATCH id=ID rl=conj) { e = `match($id.text,rl); }
      | ^(FUNCTION id=ID v=value) { e = `function($id.text,v); }
      | ^(LET a=affect v=value) { e = `let(a,v); }
      | ^(FAPPL v1=value v2=value) {e=`appl(v1,v2);} 
      ;

value_list returns [mlsig.types.ExprList tl]
@init{ tl = `exprlist(); }
     : ( ^(VALUELIST t=value) { tl = `exprlist(tl*,t); })+
     ;

pattern_list returns [mlsig.types.PatternList pl]
@init{ pl = `patternlist(); }
     : ( ^(PATTERNLIST p=pattern) { pl = `patternlist(pl*,p); })+
     ;

pattern returns [mlsig.types.Pattern t]
     : id=ID { t = `pvar($id.text); }
     | ^(CONSTR id=ID pl=pattern_list) { t = `pconstr($id.text,pl); }
     | ^(CONSTR id=ID) { t = `pconstr($id.text,patternlist()); }
     ;

prog returns [mlsig.types.Expr e]
     : ^(PROG id=ID t=type v=value) { e = `let(affect($id.text,t,v),skip()); }
     ;

 
