grammar MiniTom;
options {
  backtrack=true;
  memoize=true;
  output=AST;
  ASTLabelType=Tree;
}

tokens {
  %include { ast/MiniTomAstTokenList.txt }
}

@header {
  package minitom;
  import org.antlr.runtime.tree.Tree;
}

@lexer::header {
  package minitom;
}

// my temporary starting point 
compilationUnit
  : blockList* EOF -> ^(CompilationUnit ^(BlockList blockList* ));

blockList
  : LBRACE blockList RBRACE -> ^(BlockList blockList)
  | matchConstruct
  | compositeTerm
  ;

matchConstruct
  : MATCH LPAREN matchArguments RPAREN LBRACE patternAction* RBRACE
    -> ^(MatchConstruct matchArguments ^(PatternActionList patternAction* ))
  | MATCH LBRACE constraintAction* RBRACE
    -> ^(MatchConstructWithoutArgs ^(ConstraintActionList constraintAction* ))
  ;

matchArguments
  : termList //pairTerm (COMMA pairTerm)* -> ^(TomTermList pairTerm+)
  ;

patternAction
  : patternList ARROW LBRACE blockList RBRACE
    -> ^(PatternAction patternList ^(BlockList blockList ))
  | labelName=ID COLON patternList ARROW LBRACE blockList RBRACE
    -> ^(LabelledPatternAction patternList ^(BlockList blockList ) $labelName)
  ;

compositeTerm
  : exp=ID -> ^(CompositeTerm ID[exp] ) //temporary rule
  ;

// Is it necessary to create a node for a plainPattern ?
pattern
  : plainPattern -> ^(Pattern plainPattern )//^(PlainPattern plainPattern ))
  | annotatedName=ID AT plainPattern -> ^(AnnotedPattern plainPattern ID[annotatedName])//^(PlainPattern plainPattern ) ID[annotatedName])
  ;

patternList
  : pattern (COMMA pattern)* -> ^(PatternList pattern+)
  ;

plainPattern
  :
   UNDERSCORE STAR -> ^(PlainPattern ^(UnamedVariableStar ) )
  | UNDERSCORE -> ^(PlainPattern ^(UnamedVariable ) )
//  | ANTI_SYM ID STAR -> ^(PlainPattern ^(AntiVariableStar ^(Name ID)  ^(EmptyType )) )
  | ANTI_SYM ID -> ^(PlainPattern ^(AntiVariable ^(Name ID)  ^(EmptyType )) )
  | ID STAR -> ^(PlainPattern ^(VariableStar ^(Name ID )  ^(EmptyType )) )
  | ID -> ^(PlainPattern ^(Variable ^(Name ID )  ^(EmptyType )) )
  | ANTI_SYM headSymbolList tail -> ^(AntiSymbolList headSymbolList tail)
  | headSymbolList tail -> ^(SymbolList headSymbolList tail)
  | explicitTermList -> ^(ExplicitTermList explicitTermList ) //^(PPExplicitTermList explicitTermList )
  | xmlTerm
  ;

constraintAction
  : constraint ARROW LBRACE blockList RBRACE -> ^(ConstraintAction constraint blockList)
  ;

constraint
  : matchConstraint //left=pattern MATCH_CONSTRAINT /*t=ID? right=term[t]*/ right=term -> ^(MatchConstraint $left $right)
/*  | left=constraint AND_CONNECTOR right=constraint -> ^(AndConstraint $left $right)*/
  | andConstraint
  | simpleAndConstraint
/*  | left=constraint OR_CONNECTOR right=constraint -> ^(OrConstraint $left $right) */
  | orConstraint
  | simpleOrConstraint
  | LPAREN constraint RPAREN -> constraint
  | operator
  ;

// 'or' and 'and' constraints have the same priority as in Java
orConstraint
  : simpleOrConstraint OR_CONNECTOR andConstraint -> ^(OrConstraint simpleOrConstraint andConstraint)
  | matchConstraint OR_CONNECTOR andConstraint -> ^(OrConstraint matchConstraint andConstraint)
  | operator OR_CONNECTOR andConstraint -> ^(OrConstraint operator andConstraint)
  | simpleOrConstraint
  ;

simpleOrConstraint
  : m1=matchConstraint OR_CONNECTOR m2=matchConstraint -> ^(OrConstraint $m1 $m2 )
  | o1=operator OR_CONNECTOR o2=operator -> ^(OrConstraint $o1 $o2 )
  | operator OR_CONNECTOR matchConstraint -> ^(OrConstraint operator matchConstraint )
  | matchConstraint OR_CONNECTOR operator -> ^(OrConstraint matchConstraint operator )
  ;

andConstraint
  : matchConstraint AND_CONNECTOR andConstraint -> ^(AndConstraint matchConstraint andConstraint)
  | operator AND_CONNECTOR andConstraint -> ^(AndConstraint operator andConstraint )
  | simpleAndConstraint
  ;

simpleAndConstraint
  : m1=matchConstraint AND_CONNECTOR m2=matchConstraint -> ^(AndConstraint $m1 $m2)
  | matchConstraint AND_CONNECTOR operator -> ^(AndConstraint matchConstraint operator)
  | operator AND_CONNECTOR matchConstraint -> ^(AndConstraint operator matchConstraint )
  | o1=operator AND_CONNECTOR o2=operator -> ^(AndConstraint $o1 $o2 )

  ;

matchConstraint
  : left=pattern MATCH_CONSTRAINT right=term -> ^(MatchConstraint $left $right)
  ;

operator
  : left=term LESSOREQUAL_CONSTRAINT right=term
    -> ^(OpConstraint $left $right ^(NumLessOrEqualThan ))
  | left=term GREATEROREQUAL_CONSTRAINT right=term
    -> ^(OpConstraint $left $right ^(NumGreaterOrEqualThan ))
  | left=term DOUBLEEQ right=term
    -> ^(OpConstraint $left $right ^(NumEqual ))
  | left=term DIFFERENT_CONSTRAINT right=term
    -> ^(OpConstraint $left $right ^(NumDifferent))
  | left=term XML_START right=term
    -> ^(OpConstraint $left $right ^(NumLessThan ))
  | left=term XML_CLOSE right=term
    -> ^(OpConstraint $left $right ^(NumGreaterThan ))
  ;

type
  : ID -> ^(Type ID)
  ;

term //why 'ID? ID' and '?ID ID STAR' do not work fine ???
  : t1=ID n1=ID STAR -> ^(VariableStar ^(Name $n1 ) ^(Type $t1) )
  | n2=ID STAR -> ^(VariableStar ^(Name $n2 ) ^(EmptyType ) )
  | t3=ID n3=ID -> ^(Variable ^(Name $n3 ) ^(Type $t3) )
  | n4=ID -> ^(Variable ^(Name $n4 ) ^(EmptyType ) )
  | name=ID LPAREN termList* RPAREN -> ^(NamedTermList ^(Name $name ) termList*)
  ;

/*term[Token type]
  : ID
    -> {type!=null}? ^(Variable ^(Name ID) ^(Type ID[type]))
    -> ^(Variable ^(Name ID) ^(EmptyType ))
  | ID STAR
    -> {type!=null}? ^(VariableStar ^(Name ID) ^(Type ID[type]))
    -> ^(VariableStar ^(Name ID) ^(EmptyType ))
  | ID LPAREN tl=(noTypedTerm (COMMA noTypedTerm)*)? RPAREN -> ^(NamedTermList ^(Name ID) ^(TomTermList $tl))
  ;*/

termList
  : term (COMMA term)* -> ^(TomTermList term+)
  ;

//XML
xmlTerm
  : XML_START xmlNameList xmlAttrList XML_CLOSE_SINGLETON -> ^(XMLTerm xmlNameList xmlAttrList ^(TomTermList ))
  | XML_START xmlNameList xmlAttrList XML_CLOSE xmlChilds XML_START_ENDING xmlNameList XML_CLOSE -> ^(XMLTerm xmlNameList xmlAttrList xmlChilds)
/*  | XML_TEXT LPAREN ( ID | STRING ) RPAREN -> ^(XMLTerm ^(concTomName ) ^(TomTermList ))
  | XML_COMMENT LPAREN ( ID | STRING ) RPAREN -> ^(XMLTerm )
  | XML_PROC LPAREN ( ID | STRING ) COMMA ( ID | STRING ) RPAREN -> ^(XMLTerm )
*/
  ;

xmlAttrList
  : LBRACKET (xmlAttr (COMMA xmlAttr)*)? RBRACKET -> ^(TomTermList xmlAttr+)
  | xmlAttr* -> ^(TomTermList xmlAttr)
  ;

xmlAttr
  : UNDERSCORE STAR -> ^(UnamedVariableStar ) //^(UnderscoreStar )
  //| ID STAR -> ^(VariableStar ^(Name ID) ^(EmptyType ))
/*  | ID EQUAL (id1=ID AT)? id2=( ID | STRING ) -> // ?
  | (id1=ID AT)? UNDERSCORE EQUAL (id2=ID AT)? id3=( ID | STRING ) -> // ?
*/
  ;

xmlChilds //TermList
  : term* -> ^(TomTermList term*)
  | LBRACKET /*term (COMMA term)* */ termList RBRACKET -> termList //^(TomTermList term+)
  ;

xmlNameList
  : ID -> ^(TomNameList ^(Name ID ))
  | LPAREN ID (ALTERNATIVE ID)* RPAREN -> ^(TomNameList ^(Name ID )+)
  ;

//something like that ???
/*headSymbol
  : ID -> ^(HeadSymbol ID )
  | INT -> ^(Integer )
  | DOUBLE -> ^(Double )
  | STRING -> ^(String )
  | CHAR -> ^(Char )
  ;*/

//something like that
headSymbolList
  : ID qm=QMARK?
    -> {qm!=null}? ^(HeadSymbolQMark ID )
    -> ^(HeadSymbol ID )
  | LPAREN ID (ALTERNATIVE ID)+ RPAREN -> ^(HeadSymbolList ID+)
  ;

tail
  : explicitTermList -> ^(ExplicitTermList explicitTermList )
  | implicitPairList -> ^(ImplicitPairList implicitPairList )
  ;

explicitTermList
  : LPAREN (pattern (COMMA pattern)* )? RPAREN -> ^(PatternList pattern+ ) // ???
  ;

implicitPairList
  : LBRACKET (pairPattern (COMMA pairPattern)* )? RBRACKET -> ^(PairPatternList pairPattern+)
  ;

pairPattern
  : ID EQUAL pattern -> ^(PairPattern ID ^(Pattern pattern ))
  ;

LBRACE      :   '{' ;
RBRACE      :   '}' ;
LPAREN      :   '(' ;
RPAREN      :   ')' ;
LBRACKET    :   '[' ;
RBRACKET    :   ']' ;
COMMA       :   ',' ;
ARROW       :   '->';
DOULEARROW  :   '=>';
ALTERNATIVE :   '|' ;
AFFECT      :   ':=';
DOUBLEEQ    :   '==';
COLON       :   ':' ;
SEMI        :   ';' ;
EQUAL       :   '=' ;
AT          :   '@' ;
STAR        :   '*' ;
ANTI_SYM    :    '!';
QMARK       :   '?' ;
UNDERSCORE  :    '_' ;

MATCH : '%''match' ;

//XML Tokens
XML_START   :   '<';
XML_CLOSE   :   '>' ;
DOUBLE_QUOTE:   '\"';
XML_TEXT    :   '#TEXT';
XML_COMMENT :   '#COMMENT';
XML_PROC    :   '#PROCESSING-INSTRUCTION';
XML_START_ENDING    : '</' ;
XML_CLOSE_SINGLETON : '/>' ;

// tokens to skip : white spaces
WS : ( ' '
       | '\t'
       | ( '\r\n' // DOS
           | '\n'   // Unix
           | '\r'   // Macintosh
           )
       )
  {$channel=HIDDEN;}
  ;

// tokens to skip : Single Line Comments
SLCOMMENT:
  '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
  {$channel=HIDDEN;}
  ;

// tokens to skip : Multi Lines Comments
ML_COMMENT:
  '/*' .* '*/'
  {$channel=HIDDEN;}
  ;

/*CHARACTER:
  '\'' ( ESC | ~('\''|'\n'|'\r'|'\\') )+ '\''
  ;*/

STRING:
  '"' (~('"'|'\\'|'\n'|'\r'))* '"'
  /* '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' */
  ;

  // useless
/*SPECIAL_CHAR:
  ( LBRACE | RBRACE | LPAREN | RPAREN | LBRACKET | RBRACKET | COMMA | ARROW | DOULEARROW | ALTERNATIVE | AFFECT | DOUBLEEQ | COLON | SEMI | EQUAL | AT | STAR | QMARK | UNDERSCORE | '%' | XML_START | XML_CLOSE | DOUBLE_QUOTE | ANTI_SYM | DOT )
  ;*/

MATCH_CONSTRAINT  : '<<';
LESSOREQUAL_CONSTRAINT  : '<=';  
GREATEROREQUAL_CONSTRAINT  : '>=';  
DIFFERENT_CONSTRAINT  : '!=';
//LESS_CONSTRAINT  : '<:';
//GREATER_CONSTRAINT  : ':>';
  
AND_CONNECTOR  : '&&';
OR_CONNECTOR  : '||';

fragment
LETTER:   ('a'..'z' | 'A'..'Z' )   ;

fragment
DIGIT:   ('0'..'9')  ;

//fragment
/*ID: 
  ('_')? LETTER
  ( 
   options{greedy = true;}:
   ( LETTER | DIGIT | '_' | '.' )
  )* 
  ;*/

ID: ( LETTER | UNDERSCORE ) ( LETTER | UNDERSCORE | DIGIT )* ;
        
MINUS         :   '-' ;
PLUS          :   '+' ;
DOT           :   '.' ;
