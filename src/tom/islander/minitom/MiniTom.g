grammar MiniTom;
options {
  backtrack=true;
  memoize=true;
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=AstTokens;
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
  | compositeTerm // to modify
  | operator
  | operatorList
  | operatorArray
  | typeTerm
  | includeConstruct
  | strategyConstruct
/*
  | gomConstruct
*/
  ;

goalLanguageBlock
  : LBRACE blockList /*compositeTerm*/ RBRACE -> /*^(BlockList compositeTerm)*/ ^(BlockList blockList)
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
  : CID -> ^(CompositeTerm CID ) //temporary rule
  | ID -> ^(CompositeTerm ID ) //temporary rule
  ;

// Is it necessary to create a node for a plainPattern ?
pattern
  : plainPattern -> ^(Pattern plainPattern )
  | annotatedName=ID AT plainPattern -> ^(AnnotedPattern plainPattern ID[annotatedName])
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
  | numOperator
  ;

// 'or' and 'and' constraints have the same priority as in Java
orConstraint
  : simpleOrConstraint OR_CONNECTOR andConstraint -> ^(OrConstraint simpleOrConstraint andConstraint)
  | matchConstraint OR_CONNECTOR andConstraint -> ^(OrConstraint matchConstraint andConstraint)
  | numOperator OR_CONNECTOR andConstraint -> ^(OrConstraint numOperator andConstraint)
  | simpleOrConstraint
  ;

simpleOrConstraint
  : m1=matchConstraint OR_CONNECTOR m2=matchConstraint -> ^(OrConstraint $m1 $m2 )
  | o1=numOperator OR_CONNECTOR o2=numOperator -> ^(OrConstraint $o1 $o2 )
  | numOperator OR_CONNECTOR matchConstraint -> ^(OrConstraint numOperator matchConstraint )
  | matchConstraint OR_CONNECTOR numOperator -> ^(OrConstraint matchConstraint numOperator )
  ;

andConstraint
  : matchConstraint AND_CONNECTOR andConstraint -> ^(AndConstraint matchConstraint andConstraint)
  | numOperator AND_CONNECTOR andConstraint -> ^(AndConstraint numOperator andConstraint )
  | simpleAndConstraint
  ;

simpleAndConstraint
  : m1=matchConstraint AND_CONNECTOR m2=matchConstraint -> ^(AndConstraint $m1 $m2)
  | matchConstraint AND_CONNECTOR numOperator -> ^(AndConstraint matchConstraint numOperator)
  | numOperator AND_CONNECTOR matchConstraint -> ^(AndConstraint numOperator matchConstraint )
  | o1=numOperator AND_CONNECTOR o2=numOperator -> ^(AndConstraint $o1 $o2 )
  ;

matchConstraint
  : left=pattern MATCH_CONSTRAINT right=term -> ^(MatchConstraint $left $right)
  ;

numOperator
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

term /* why 'ID? ID' and '?ID ID STAR' do not work fine ??? */
  : t1=ID n1=ID STAR -> ^(VariableStar ^(Name $n1 ) ^(Type $t1) )
  | n2=ID STAR -> ^(VariableStar ^(Name $n2 ) ^(EmptyType ) )
  | t3=ID n3=ID -> ^(Variable ^(Name $n3 ) ^(Type $t3) )
  | n4=ID (LPAREN RPAREN)? -> ^(Variable ^(Name $n4 ) ^(EmptyType )) // '(LPAREN RPAREN)?' in order to regognize correctly a()
  | name=ID LPAREN termList RPAREN -> ^(NamedTermList ^(Name $name ) termList)
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
  | XML_TEXT LPAREN (id=ID | id=STRING ) RPAREN -> ^(XMLTerm ^(TomNameList ^(Name $id)) ^(EmptyList ) ^(EmptyList ))
/*  | XML_COMMENT LPAREN ( ID | STRING ) RPAREN -> ^(XMLTerm )
  | XML_PROC LPAREN ( ID | STRING ) COMMA ( ID | STRING ) RPAREN -> ^(XMLTerm )
*/
  ;

xmlAttrList
  : LBRACKET (xmlAttr (COMMA xmlAttr)*)? RBRACKET -> ^(TomTermList xmlAttr+)
  | xmlAttr* -> ^(TomTermList xmlAttr*)
  ;

xmlAttr
  : UNDERSCORE STAR -> ^(UnamedVariableStar ) //^(UnderscoreStar )
  | ID STAR -> ^(VariableStar ^(Name ID) ^(EmptyType ))
  | id=ID EQUAL id1=ID AT id2=ID/* | STRING )*/ -> ^(XMLAttr ^(Name $id ) ID[$id2] ^(Annotation $id1) ) // ?
  | id=ID EQUAL id2=ID/* | STRING )*/ -> ^(XMLAttr ^(Name $id ) ID[$id2] ^(EmptyAnnotation )) // ?
  //| (id1=ID AT)? UNDERSCORE EQUAL (id2=ID AT)? id3=ID/* | STRING )*/ -> ^(UnamedXMLAttr ID[$id3] (ID[$id1])? (ID[$id2])? )// ?
  | id1=ID AT UNDERSCORE EQUAL id2=ID AT id3=ID/* | STRING )*/ -> ^(UnamedXMLAttr ID[$id3] ^(Annotation $id2) ^(Annotation $id1))// ?
  | id1=ID AT UNDERSCORE EQUAL id3=ID/* | STRING )*/ -> ^(UnamedXMLAttr ID[$id3] ^(Annotation $id1) ^(EmptyAnnotation ))// ?
  | UNDERSCORE EQUAL id2=ID AT id3=ID/* | STRING )*/ -> ^(UnamedXMLAttr ID[$id3] ^(EmptyAnnotation ) ^(Annotation $id2))// ?
  | UNDERSCORE EQUAL id3=ID/* | STRING )*/ -> ^(UnamedXMLAttr ID[$id3] ^(EmptyAnnotation ) ^(EmptyAnnotation ))// ?
  ;

xmlChilds //TermList
  : term* -> ^(TomTermList term*)
  | LBRACKET /*term (COMMA term)* */ termList RBRACKET -> termList //^(TomTermList term+)
  ;

xmlNameList
  : ID -> ^(TomNameList ^(Name ID ))
  | LPAREN ID (ALTERNATIVE ID)* RPAREN -> ^(TomNameList ^(Name ID )+)
  ;

//something like that ?
/*headSymbol
  : ID -> ^(HeadSymbol ID )
  | INT -> ^(Integer )
  | DOUBLE -> ^(Double )
  | STRING -> ^(String )
  | CHAR -> ^(Char )
  ;*/

headSymbolList
  : ID QMARK -> ^(HeadSymbolList ^(HeadSymbolQMark ID ))
  | ID -> ^(HeadSymbolList ^(HeadSymbol ID ))
  | LPAREN ID (ALTERNATIVE ID)+ RPAREN -> ^(HeadSymbolList ID+)
  ;

tail
  : explicitTermList -> ^(ExplicitTermList explicitTermList )
  | implicitPairList -> ^(ImplicitPairList implicitPairList )
  ;

explicitTermList
  : LPAREN (pattern (COMMA pattern)* )? RPAREN -> ^(PatternList (pattern+)? )
  ;

implicitPairList
  : LBRACKET (pairPattern (COMMA pairPattern)* )? RBRACKET -> ^(PairPatternList (pairPattern+)? )
  ;

pairPattern
  : ID EQUAL pattern -> ^(PairPattern ID ^(Pattern pattern ))
  ;

//Include
includeConstruct
  : INCLUDE LBRACE FILENAME -> ^(Include FILENAME)
  ;


//Strategy
strategyConstruct
  : STRATEGY ID LPAREN strategyArguments* RPAREN EXTENDS term LBRACE strategyVisitList RBRACE -> ^(Strategy ^(Name ID) term strategyVisitList strategyArguments* )
  ;

strategyArguments
  : n=ID COLON t=ID ( COMMA n2=ID COLON t2=ID )* -> ^(TomTermList ^(Variable ^(Name $n) ^(Type $t))+ )
  | t=ID n=ID ( COMMA t2=ID n2=ID )* -> ^(TomTermList ^(Variable ^(Name $n) ^(Type $t))+ )
  ;

strategyVisitList
  : strategyVisit* -> ^(StrategyVisitList strategyVisit*)
  ;

strategyVisit
  : VISIT t=ID LBRACE visitAction* RBRACE -> ^(StrategyVisit ^(Type $t) ^(VisitActionList visitAction*))
  ;

visitAction // almost the same as patternAction
  : label=ID COLON patternList ARROW LBRACE blockList RBRACE -> ^(LabelledVisitActionBL patternList blockList $label)
  | label=ID COLON patternList ARROW term -> ^(LabelledVisitActionT patternList term $label)
  | patternList ARROW LBRACE blockList RBRACE -> ^(VisitActionBL patternList blockList)
  | patternList ARROW term -> ^(VisitActionT patternList term)
  ;

//Operator : fix it :  keywordIsFsym is optional since Tom 2.5
operator
  : OPERATOR t=ID n=ID LPAREN slotList? RPAREN LBRACE /*keywordIsFsym*/ l=listKeywordsOp? /*ol=((keywordMake | keywordGetSlot)+)?*/ RBRACE
    -> {l!=null}? ^(Operator ^(Name $n) ^(Type $t) slotList? /*keywordIsFsym*/ $l /*^(OperatorList ($l)? )*/)
    -> ^(Operator ^(Name $n) ^(Type $t) slotList? /*keywordIsFsym*/ ^(OperatorList ))
  ;

/*keywords for %op*/ 
keywordsOp
  : keywordIsFsym
  | keywordMake
  | keywordGetSlot
  ;

listKeywordsOp
  : keywordsOp+ -> ^(OperatorList keywordsOp+)
  ;

/*keywords for %oparray*/
keywordsOpArray
  : keywordIsFsym
  | keywordMakeEmptyArray
  | keywordMakeAppend
  | keywordGetElement
  | keywordGetSize
  ;

listKeywordsOpArray
  : keywordsOpArray+ -> ^(OperatorList keywordsOpArray+)
  ;

/*keywords for %oplist*/
keywordsOpList
  : keywordIsFsym
  | keywordMakeEmptyList
  | keywordMakeInsert
  | keywordGetHead
  | keywordGetTail
  | keywordIsEmpty
  ;

listKeywordsOpList
  : keywordsOpList+ -> ^(OperatorList keywordsOpList+)
  ;
//

keywordIsFsym
  : KW_ISFSYM LPAREN name=ID RPAREN goalLanguageBlock -> ^(IsFsym ^(Name $name) goalLanguageBlock)
  ;

keywordMake 
  : KW_MAKE LPAREN /*nameList*/ l=(ID ( COMMA ID )* )? RPAREN goalLanguageBlock -> ^(Make /*nameList*/ ^(TomNameList $l? ) goalLanguageBlock)
//  | KW_MAKE LPAREN RPAREN -> ^(Make ^(TomNameList ) goalLanguageBlock)
  ;

keywordGetSlot 
  : KW_GETSLOT LPAREN n1=ID COMMA n2=ID RPAREN goalLanguageBlock -> ^(GetSlot ^(Name $n1) ^(Name $n2) goalLanguageBlock)
  ;

keywordGetHead
  : KW_GETHEAD LPAREN name=ID RPAREN goalLanguageBlock -> ^(GetHead ^(Name $name) goalLanguageBlock)
  ;

keywordGetTail
  : KW_GETTAIL LPAREN name=ID RPAREN goalLanguageBlock -> ^(GetTail ^(Name $name) goalLanguageBlock)
  ;

keywordIsEmpty
  : KW_ISEMPTY LPAREN name=ID RPAREN goalLanguageBlock -> ^(IsEmpty ^(Name $name) goalLanguageBlock)
  ;

keywordMakeEmptyList
  : KW_MKEMPTY LPAREN RPAREN goalLanguageBlock -> ^(MakeEmptyList goalLanguageBlock)
  ;

keywordMakeInsert
  : KW_MKINSERT LPAREN n1=ID COMMA n2=ID RPAREN goalLanguageBlock -> ^(MakeInsert ^(Name $n1) ^(Name $n2) goalLanguageBlock)
  ;

keywordGetElement
  : KW_GETELEMENT LPAREN n1=ID COMMA n2=ID RPAREN goalLanguageBlock -> ^(GetElement ^(Name $n1) ^(Name $n2) goalLanguageBlock)
  ;

keywordGetSize
  : KW_GETSIZE LPAREN name=ID RPAREN goalLanguageBlock -> ^(GetSize ^(Name $name) goalLanguageBlock)
  ;

keywordMakeEmptyArray
  : KW_MKEMPTY LPAREN name=ID RPAREN goalLanguageBlock -> ^(MakeEmptyArray ^(Name $name) goalLanguageBlock)
  ;

keywordMakeAppend
  : KW_MKAPPEND LPAREN n1=ID COMMA n2=ID RPAREN goalLanguageBlock -> ^(MakeAppend ^(Name $n1) ^(Name $n2) goalLanguageBlock)
  ;

// 
//  : ID -> ^(TomNameList ^(Name ID ))
//  | LPAREN ID (ALTERNATIVE ID)* RPAREN -> ^(TomNameList ^(Name ID )+)
//nameList
//  : ID ( COMMA ID )* -> ^(TomNameList ^(Name ID)+)
//  ;

slot
  : n=ID COLON t=ID -> ^(Slot ^(Name $n) ^(Type $t))
  ;

slotList
  : slot ( COMMA slot )* -> ^(SlotList slot+)
  ;
//

operatorList
  : OPLIST t=ID n=ID LPAREN t2=ID STAR RPAREN LBRACE /*keywordIsFsym*/ l=listKeywordsOpList? RBRACE
    -> {l!=null}? ^(OpList ^(Name $n) ^(Type $t) ^(Type $t2) /*keywordIsFsym*/ $l)
    -> ^(OpArray ^(Name $n) ^(Type $t) ^(Type $t2) /*keywordIsFsym*/ ^(OperatorList ))
  ;

operatorArray
  : OPARRAY t=ID n=ID LPAREN t2=ID STAR RPAREN LBRACE /*keywordIsFsym*/ l=listKeywordsOpArray? RBRACE
    -> {l!=null}? ^(OpArray ^(Name $n) ^(Type $t) ^(Type $t2) /*keywordIsFsym*/ $l)
    -> ^(OpArray ^(Name $n) ^(Type $t) ^(Type $t2) /*keywordIsFsym*/ ^(OperatorList ))
  ;

typeTerm
  : TYPETERM ID LBRACE keywordImplement keywordIsSort? keywordEquals? RBRACE ->  ^(TypeTerm  ^(Type ID ) keywordImplement keywordIsSort? keywordEquals? )
  ;

keywordImplement
  : KW_IMPLEMENT goalLanguageBlock -> ^(Implement goalLanguageBlock )
  ;

keywordIsSort
  : KW_ISSORT LPAREN ID RPAREN /*goalLanguageSortCheck*/ goalLanguageBlock -> ^(IsSort ^(Name ID) goalLanguageBlock ) //temp
  ;

keywordEquals
  : KW_EQUALS LPAREN n1=ID COMMA n2=ID RPAREN goalLanguageBlock -> ^(Equals ^(Name $n1 ) ^(Name $n2 ) goalLanguageBlock )
  ;

// LEXER
//keywords
MATCH       :   '%''match'    ;
INCLUDE     :   '%''include'  ;
STRATEGY    :   '%''strategy' ;
OPERATOR    :   '%''op'       ;
OPLIST      :   '%''oplist'   ;
OPARRAY     :   '%''oparray'  ;
TYPETERM    :   '%''typeterm' ;
EXTENDS     :   'extends'     ;
VISIT       :   'visit'       ;

KW_ISFSYM       : 'is_fsym'     ;
KW_GETSLOT      : 'get_slot'    ;
KW_MAKE         : 'make'        ;
KW_GETHEAD      : 'get_head'    ;
KW_GETTAIL      : 'get_tail'    ;
KW_ISEMPTY      : 'is_empty'    ;
KW_MKEMPTY      : 'make_empty'  ;
KW_MKINSERT     : 'make_insert' ;
KW_GETELEMENT   : 'get_element' ;
KW_GETSIZE      : 'get_size'    ;
KW_MKAPPEND     : 'make_append' ;
KW_IMPLEMENT    : 'implement'   ;
KW_ISSORT       : 'is_sort'     ;
KW_EQUALS       : 'equals'      ;

// simple symbols
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
UNDERSCORE  :   '_' ;
MINUS       :   '-' ;
PLUS        :   '+' ;
DOT         :   '.' ;
SLASH       :   '/' ;

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

// filename is : ./fileName | ./../../fileName | ../../fileName | fileName | path/to/fileName | ./path/to/fileName | ../../path/to/fileName
fragment
FILENAME // to modify
  : ( DOT SLASH | (DOT DOT SLASH)+ | SLASH )?
    ((DIGIT | LETTER | UNDERSCORE)+ SLASH)*
    (( LETTER | UNDERSCORE ) ( LETTER | UNDERSCORE | DIGIT | DOT )*)
  ;

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

/*fragment
DOUBLE: ( (DIGIT)+ DOT? (DIGIT)* | DOT (DIGIT)+ );

fragment
INT:(DIGIT)+;*/

//fragment
/*ID: 
  ('_')? LETTER
  ( 
   options{greedy = true;}:
   ( LETTER | DIGIT | '_' | '.' )
  )* 
  ;*/


ID: ( LETTER | UNDERSCORE ) ( LETTER | UNDERSCORE | DIGIT )* ;
fragment
CID: ( ID | DOLLARID | WS | STRING | DOT | LPAREN .* RPAREN | LBRACE .* RBRACE )+ ;

fragment
XMLID: (ID | STRING);

fragment
DOLLARID: '$'ID;
