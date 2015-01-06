/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the Inria nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
header { 
  package xquery.parser;

  import aterm.*;
  import aterm.pure.*;
    

  import xquery.parser.xquery.*;
  import xquery.parser.xquery.types.*;
}



//   %vas {
//     // extension of adt syntax
//     module Peano
//     imports 
//     public
//       sorts Nat
      
//     abstract syntax
//       zero -> Nat
//       suc(pred:Nat) -> Nat
//       fib(val:Nat) -> Nat
//       plus(x1:Nat, x2:Nat) -> Nat
//    }

//     // rule fib
//   %rule {
//     fib(zero())        -> suc(zero())
//     fib(suc(zero()))   -> suc(zero())
//     fib(suc(suc(x)))   -> plus(fib(x),fib(suc(x)))
//   }

//     // rule plus
//   %rule {
//     plus(x, zero())    -> x
//     plus(x, suc(y))    -> suc(plus(x,y))
//   } // rule


class XQueryParser extends Parser;
options {
  k = 3; 
}

%include { xquery.tom }


// [1]     	Pragma  	   ::=     	"(::" "pragma" QName  PragmaContents* "::)"  	/* gn: parens */

pragma: 
  L_PAR_FOUR_DOT PRAGMA QNAME (PragmaContents)* R_PAR_FOUR_DOT; 


// [2]    	MUExtension 	   ::=    	"(::" "extension" QName ExtensionContents* "::)" 	/* gn: parens */

mUExtension: 
  L_PAR_FOUR_DOT PRAGMA EXTENSION QNAME (extensionContents)* R_PAR_FOUR_DOT; 
// [3]    	ExprComment 	   ::=    	"(:" (ExprCommentContent | ExprComment)* ":)" 	/* gn: comments */

exprComment : 
  L_PAR_TWO_DOT (exprCommentContent | exprComment)* R_PAR_TWO_DOT; 

// [4]    	ExprCommentContent 	   ::=    	Char 	/* gn: parens */
exprCommentContent: 
  CHARS; 

// [5]    	PragmaContents 	   ::=    	Char

pragmaContents: 
  CHARS; 
// [6]    	ExtensionContents 	   ::=    	Char

extensionContents: 
  CHARS; 
// [7]    	IntegerLiteral 	   ::=    	Digits

// integerLiteral: 
//   DIGITS; 
integerLiteral         : DIGITS;  


// [8]    	DecimalLiteral 	   ::=    	("." Digits) | (Digits "." [0-9]*) 	/* ws: explicit */
//decimalLiteral: (DOT DIGITS) | (DIGITS DOT DIGITS);
decimalLiteral         : (DOT DIGITS) | (DIGITS DOT DIGITS);


// [9]    	DoubleLiteral 	   ::=    	(("." Digits) | (Digits ("." [0-9]*)?)) ("e" | "E") ("+" | "-")? Digits 	/* ws: explicit */
doubleLiteral          : ((DOT DIGITS) 
						  | (DIGITS (options {greedy=true;}: DOT DIGITS)?)) 
  ("e" | "E") (options {greedy=true;}: "+" | "-")? DIGITS;


// [10]    	StringLiteral 	   ::=    	('"' (PredefinedEntityRef | CharRef | ('"' '"') | [^"&])* '"') | ("'" (PredefinedEntityRef | CharRef | ("'" "'") | [^'&])* "'") 	/* ws: significant */
// STRINGLITERAL: 
//   (DOUBLE_QUOTE (predefinedEntityRef | charRef | (DOUBLE_QUOTE DOUBLE_QUOTE))* DOUBLE_QUOTE) 
// 	| (SINGLE_QUOTE (predefinedEntityRef | charRef | (SINGLE_QUOTE SINGLE_QUOTE))* SINGLE_QUOTE) ;
  

// [11]    	S 	   ::=    	[http://www.w3.org/TR/REC-xml#NT-S] XML 	/* gn: xml-version */
// [12]    	SchemaMode 	   ::=    	"lax" | "strict" | "skip"

schemaMode: 
  LAX | STRICT | SKIP_KEYWORD;

// [13]    	SchemaGlobalTypeName 	   ::=    	"type" "(" QName ")"
schemaGlobalTypeName: 
  TYPE L_PAR QNAME R_PAR;

// [14]    	SchemaGlobalContext 	   ::=    	QName | SchemaGlobalTypeName

schemaGlobalContext: 
  QNAME | schemaGlobalTypeName; 

// [15]    	SchemaContextStep 	   ::=    	QName
schemaContextStep: 
  QNAME; 

// [16]    	Digits 	   ::=    	[0-9]+
// digits: 
//   (DIGIT)+;

// [17]    	EscapeQuot 	   ::=    	'"' '"'
escapeQuot: 
  L_PAR R_PAR; 

// [18]    	PITarget 	   ::=    	NCName
piTarget: 
  NCNAME;

// [19]    	NCName 	   ::=    	[http://www.w3.org/TR/REC-xml-names/#NT-NCName] Names 	/* gn: xml-version */

// [20]    	VarName 	   ::=    	QName

varName returns [String name=""]: 
  qname=QNAME 
{
  name=qname.getText();
};

// [21]    	QName 	   ::=    	[http://www.w3.org/TR/REC-xml-names/#NT-QName] Names 	/* gn: xml-version */
// QNAME:   
//   NCNAME (options {greedy=true;}: TWO_DOT  NCNAME)?;

// [22]    	PredefinedEntityRef 	   ::=    	"&" ("lt" | "gt" | "amp" | "quot" | "apos") ";" 	/* ws: explicit */
predefinedEntityRef: 
  "&" (LT | GT | AMP | QUOT | APOS) SEMI_COLON; 

// [23]    	HexDigits 	   ::=    	([0-9] | [a-f] | [A-F])+
// hexDigits: 
//   (HEX_DIGIT)+;

// [24]    	CharRef 	   ::=    	"&#" (DIGITS | ("x" HexDigits)) ";" 	/* ws: explicit */
charRef: 
  "&#" (DIGITS | ("x" HEX_Digits)) SEMI_COLON; 

// [25]    	EscapeApos 	   ::=    	"''"
escapeApos: 
  "''"; 

// [26]    	Char 	   ::=    	[http://www.w3.org/TR/REC-xml#NT-Char] XML 	/* gn: xml-version */
// char: 
//   (CHAR)+;

// [27]    	ElementContentChar 	   ::=    	Char - [{}<&]
elementContentChar: 
  CHARS;

// [28]    	QuotAttContentChar 	   ::=    	Char - ["{}<&]
quotAttContentChar: 
  CHARS; 

// [29]    	AposAttContentChar 	   ::=    	Char - ['{}<&]
aposAttContentChar:
  CHARS; 

// Non-Terminals
// [30]    	Module 	   ::=    	VersionDecl? (MainModule | LibraryModule)
module: 
  (versionDecl)? (mainModule | libraryModule); 

// [31]    	MainModule 	   ::=    	Prolog QueryBody
mainModule: 
  prolog queryBody; 

// [32]    	LibraryModule 	   ::=    	ModuleDecl Prolog
libraryModule: 
  moduleDecl prolog; 

// [33]    	ModuleDecl 	   ::=    	<"module" "namespace"> NCName "=" StringLiteral Separator
moduleDecl: 
  MODULE NAMESPACE NCNAME EQUAL STRING_LITERAL SEMI_COLON; 

// [34]    	Prolog 	   ::=    	((NamespaceDecl
// | XMLSpaceDecl
// | DefaultNamespaceDecl
// | DefaultCollationDecl
// | BaseURIDecl
// | SchemaImport
// | ModuleImport
// | VarDecl
// | ValidationDecl
// | FunctionDecl) Separator)*
prolog: 
  ((namespaceDecl 
	| xmlSpaceDecl 
	| defaultNamespaceDecl 
	| defaultCollationDecl 
	| baseURIDecl 
	| schemaImport 
	| moduleImport 
	| varDecl 
	| validationDecl 
	| functionDecl
	) SOMI_COLON)*; 

// [35]    	Separator 	   ::=    	";"
// [36]    	VersionDecl 	   ::=    	<"xquery" "version" StringLiteral> Separator
versionDecl : 
  XQUERY VERSION STRINGLITERAL SOMI_COLON; 

// [37]    	ModuleImport 	   ::=    	<"import" "module"> ("namespace" NCName "=")? StringLiteral <"at" StringLiteral>?
moduleImport: 
  IMPORT MODULE (NAMESPACE NCNAME EQUAL)? STRINGLITERAL (AT STRINGLITERAL)?;

// [38]    	VarDecl 	   ::=    	<"declare" "variable" "$"> VarName TypeDeclaration? (("{" Expr "}") | "external")
varDecl: 
  DECLARE VARIABLE DOLLAR varName (typeDeclaration)? ((L_BRACE expr R_BRACE) | EXTERNAL); 

// [39]    	QueryBody 	   ::=    	Expr
queryBody: 
  expr; 


// [40]    	Expr 	   ::=    	ExprSingle ("," ExprSingle)*
expr returns [ATerm node=null]: 
  exprsingle= exprSingle (COMMA moreexprsingle=exprSingle)* 
{
  node = exprsingle;
};


// [41]    	ExprSingle 	   ::=    	FLWORExpr
// | QuantifiedExpr
// | TypeswitchExpr
// | IfExpr
// | OrExpr
exprSingle  returns [ATerm node] 
{
  node=null;   
}:
flwor = fLWORExpr 
{
  node=`flworexpr(flwor);
}

| quantifiedExpr 
  | typeswitchExpr 
  | ifExpr 
  | orExpr ; 


// [42]    	FLWORExpr 	   ::=    	(ForClause | LetClause)+ WhereClause? OrderByClause? "return" ExprSingle
fLWORExpr returns [ATerm node=null] 
{
  ATermList forletclauselist=null; 
  ATerm forclause=null; 
  Aterm letclause=null;
}
:
  (forletclause=forClause 
  {
	forletclauselist=`concForLetClause(forletclauselist, fromforclause(oneforclauselist));
  }
   | forletclause=letClause
  {
	forletclauselist=`concForLetClause(forletclauselist,   fromletclause(oneletclauselist));
  }
   )+ (whereclause= whereClause)? (orderbyclause=orderByClause)? RETURN expr=exprSingle

{
  if ((whereclause==null) && (orderbyclause==null)) { // null null
	node=`flwor3(forletclauselist, expr);
  }
  else if ((whereclause==null) && (orderbyclause!=null)) {
	node=`flwor1(forletclauselist, orderbyclause, expr);
  }
  else if ((whereclause!=null) && (orderbyclause==null)) {
	node=`flwor2(forletclauselist, whereclause, expr);
  }
  else {
	node=`flwor(forletclauselist, whereclause, orderbyclause, expr);
  }
  
}; 




// [43]    	ForClause 	   ::=    	<"for" "$"> VarName TypeDeclaration? PositionalVar? "in" ExprSingle ("," "$" VarName TypeDeclaration? PositionalVar? "in" ExprSingle)*

forClause returns[ATermList nodelist=null;]  // ForClause (OneForClauseList)
{
  ATerm node=null; 
}
: 
  FOR DOLLAR varname1=varName (typedeclaration1=typeDeclaration)? 
	(positionalvar1=positionalVar)? IN exprsingle1=exprSingle 
{
  if ((typedeclaration1 != null) 
	  && (positionnal1 !=null)) {
	node=`oneforclause4(varname1, positionalvar1, typedeclaration1, exprsingle1); 
  } 
  else if ((typedeclaration1 == null) 
	  && (positionnal1 != null)) {
	node=`oneforclause3(varname1, positionalvar1, exprsingle1); 
  }
  else if ((typedeclaration1 != null) 
	  && (positionnal1 == null)) {
	node=`oneforclause2(varname1, typedeclaration1 , exprsingle1); 
  }
  else {// null null 
	node=`oneforclause1(varname1 , exprsingle1); 
  }
  
  nodelist=`concOneForClauseList(node);
}
	(COMMA DOLLAR varname2=varName (typedeclaration2=typeDeclaration)? 
	 (positionalvar2=positionalVar)? IN exprsingle2=exprSingle
	{
	  if ((typedeclaration2 != null) 
		  && (positionnal2 !=null)) {
		node=`oneforclause4(varname2, positionalvar2, typedeclaration2, exprsingle2); 
	  } 
	  else if ((typedeclaration2 == null) 
			   && (positionnal2 != null)) {
		node=`oneforclause3(varname2, positionalvar2, exprsingle2); 
	  }
	  else if ((typedeclaration2 != null) 
			   && (positionnal2 == null)) {
		node=`oneforclause2(varname2, typedeclaration2 , exprsingle2); 
	  }
	  else {// null null 
		node=`oneforclause1(varname2 , exprsingle2); 
	  }
	  
	  nodelist=`concOneForClauseList(nodelist*, node);
	}
	 
	 )*
;





// [44]    	PositionalVar 	   ::=    	"at" "$" VarName

positionalVar returns [ATerm node=null]:
  AT DOLLAR varname=varName
{
  node=varname; 
}
; 




// [45]    	LetClause 	   ::=    	<"let" "$"> VarName TypeDeclaration? ":=" ExprSingle ("," "$" VarName TypeDeclaration? ":=" ExprSingle)*
letClause returns[ATermList nodelist=null;]
{
  ATerm node=null; 
}
: 
  LET DOLLAR varname1=varName (typedeclaration1=typeDeclaration)? SET_EQUAL_TO exprsingle1=exprSingle 
{
  if (typedeclaration1 != null) {
	node=`oneletclause2(varname1, typedeclaration1, exprsingle1); 
  } 
  else {
	node=`oneletclause1(varname1, exprsingle1); 
  }
	
  nodelist=`concOneLetClauseList(node);
}
(COMMA DOLLAR varName (typeDeclaration)? SET_EQUAL_TO exprSingle
{
  if (typedeclaration1 != null) {
	node=`oneletclause2(varname1, typedeclaration1, exprsingle1); 
  } 
  else {
	node=`oneletclause1(varname1, exprsingle1); 
  }
  
  nodelist=`concOneLetClauseList(nodelist*, node);

)* 
;



// [46]    	WhereClause 	   ::=    	"where" Expr
whereClause returns [ATerm node=null;] : 
 WHERE expression=expr 
{
  node=`returnclause(expression); 
}; 






// [47]    	OrderByClause 	   ::=    	(<"order" "by"> | <"stable" "order" "by">) OrderSpecList
orderByClause returns [ATerm node=null]
{
  int stable=0;
}
: 
 ((ORDER BY) 
 { 
   stable=0; 
 }
  | (STABLE ORDER BY)
 {
   stable = 1; 
 }
  ) orderspeclist=orderSpecList
{
  node=`orderclause(stable, orderspeclist);
}
; 



// [48]    	OrderSpecList 	   ::=    	OrderSpec ("," OrderSpec)*
 orderSpecList returns [ATermList nodelist = null]
{
}
: 
 orderspec1=orderSpec 
{
  nodelist=`concOrderSpec(orderspec1); 
}
(COMMA orderspec2=orderSpec
{
  nodelist=`concOrderSpec(nodelist, orderspec2); 
}
)*;



// [49]    	OrderSpec 	   ::=    	ExprSingle OrderModifier
 orderSpec returns [ATerm node=null;]: 
  exprsingle=exprSingle ordermodifier=orderModifier
{
  node=`orderspec(exprsingle, ordermodifier);
}
; 




// [50]    	OrderModifier 	   ::=    	("ascending" | "descending")? (<"empty" "greatest"> | <"empty" "least">)? ("collation" StringLiteral)?
orderModifier returns [ATerm node=null] 
{
  int ascending=1; 
  int emptygreatest=1; 
}
: 
  (ASCENDING 
  { 
	ascending=1;
  }
   | DESCENDING
  {
	ascending=0;
  }
   )? 
 ((EMPTY GREATEST) 
 {
   emptygreatest=1; 
 }
  | (EMPTY LEAST)
 {
   emptygreatest=0; 
 }
  )? 
{
  node=`ordermodifier1(ascending, emptygreatest); 
}
 (COLLATION collation=STRINGLITERAL 
 {
   String str=collation.getText();
   node=`ordermodifier2(ascending, emptygreatest, str); 
 }
  )?

; 



// [51]    	QuantifiedExpr 	   ::=    	(<"some" "$"> | <"every" "$">) VarName TypeDeclaration? "in" ExprSingle ("," "$" VarName TypeDeclaration? "in" ExprSingle)* "satisfies" ExprSingle
quantifiedExpr: 
  ((SOME DOLLAR) | (EVERY DOLLAR)) varName (typeDeclaration)? IN exprSingle
  (COMMA  DOLLAR varName (typeDeclaration)? IN exprSingle)* SATISFIES exprSingle; 

// [52]    	TypeswitchExpr 	   ::=    	<"typeswitch" "("> Expr ")" CaseClause+ "default" ("$" VarName)? "return" ExprSingle
typeswitchExpr: 
  TYPESWITCH L_PAR expr R_PAR (caseClause)+ DEFAULT_ (DOLLAR varName)? RETURN exprSingle; 

// [53]    	CaseClause 	   ::=    	"case" ("$" VarName "as")? SequenceType "return" ExprSingle
caseClause: 
  CASE (DOLLAR varName AS)? sequenceType RETURN exprSingle; 

// [54]    	IfExpr 	   ::=    	<"if" "("> Expr ")" "then" ExprSingle "else" ExprSingle
ifExpr: 
  IF L_PAR expr R_PAR THEN exprSingle ELSE exprSingle; 

// [55]    	OrExpr 	   ::=    	AndExpr ( "or" AndExpr )*
orExpr: 
  andExpr (OR andExpr)* ;

// [56]    	AndExpr 	   ::=    	InstanceofExpr ( "and" InstanceofExpr )*
andExpr: 
  instanceofExpr (AND instanceofExpr)* ;

// [57]    	InstanceofExpr 	   ::=    	TreatExpr ( <"instance" "of"> SequenceType )?
instanceofExpr: 
  treatExpr (INSTANCEOF sequenceType)?;

// [58]    	TreatExpr 	   ::=    	CastableExpr ( <"treat" "as"> SequenceType )?
treatExpr: 
  castableExpr (TREAT AS sequenceType)? ;

// [59]    	CastableExpr 	   ::=    	CastExpr ( <"castable" "as"> SingleType )?
castableExpr: 
  castExpr (CASTABLE AS singleType)?; 

// [60]    	CastExpr 	   ::=    	ComparisonExpr ( <"cast" "as"> SingleType )?
castExpr: 
  comparisonExpr (CAST AS singleType)?;





// [61]    	ComparisonExpr 	   ::=    	RangeExpr ( (ValueComp
// | GeneralComp
// | NodeComp) RangeExpr )?
 comparisonExpr returns [ATerm node=null;]
{
  int operator=0; 
}
:
 rangeexpr=rangeExpr 
{
  node=rangeexpr;  // still a RangeExpr
}
((operator=valueComp 
  | operator=generalComp 
  | operator=nodeComp) node2=rangeExpr
{
  node=`rangeexpr(node, operator, node2)
} // become comparisonExpr

)?;




// [62]    	RangeExpr 	   ::=    	AdditiveExpr ( "to" AdditiveExpr )?
 rangeExpr returns [ATerm node=null;]: 
  additiveexpr=additiveExpr 
{
  node=additiveExpr;
}
(TO additiveexpr2=additiveExpr
{
  node=`rangeexpr(additiveexpr, additiveexpr2);
}
)?; 




// [63]    	AdditiveExpr 	   ::=    	MultiplicativeExpr ( ("+" | "-") MultiplicativeExpr )*
 additiveExpr returns [ATerm node=null;]
{
  int addorminus=1; 
}
:
 multiplicativeexpr1=multiplicativeExpr
{
  node=additiveexpr1; // still a multiplicativeExpr
}
 (( PLUS {addorminus=1;} | MINUS {addorminus=0;}) multiplicativeexpr2=multiplicativeExpr
 {
   node=`additiveexpr(addorminus, additiveexpr1,  multiplicativeexpr2);
 }
)*;





// [64]    	MultiplicativeExpr 	   ::=    	UnaryExpr ( ("*" | "div" | "idiv" | "mod") UnaryExpr )*
 multiplicativeExpr returns [ATerm node=null;]
{
  int operator = 1; // star *
}
: 
 unaryexpr=unaryExpr ;
{
  node=unaryexpr; 
}
 ((STAR {operator=1;}
  | DIV {operator=2;}
  | IDIV {operator=3;}
  | MOD {operator=4;}
   ) 
  unaryexpr2=unaryExpr
 {
   node=`multiplicativeexpr(node, operator, unaryexpr2);
 }
)*; 




// [65]    	UnaryExpr 	   ::=    	("-" | "+")* UnionExpr
 unaryExpr returns [ATerm node=null;]
{
  int minuscount=0; 
}
: 
 (MINUS {minus++; }
  | PLUS)* unionexpr=unionExpr
{
  if ((minuscount%2) == 1) {
	node=`unaryexpr(1, unionexpr);
  }
  else {
	node=unionexpr;
  }
}
; 




// [66]    	UnionExpr 	   ::=    	IntersectExceptExpr ( ("union" | "|") IntersectExceptExpr )*
 unionExpr returns [ATerm node=null;]
{
  int union=1; 
}
: 
  intersectexpr1=intersectExceptExpr 
{
  node=intersectexpr1; 
}
 ((UNION {union=1;}
   | BAR {union=0; // bar=1
   }) 
  intersectexpr2=intersectExceptExpr
 {
   node=`unionexpr(union, node, intersectexpr2);
 }
)*;



// [67]    	IntersectExceptExpr 	   ::=    	ValueExpr ( ("intersect" | "except") ValueExpr )*
 intersectExceptExpr returns [ATerm node=null;]
{
  int intersect=1;
}
: 
  valueexpr=valueExpr 
{
  node=valueexpr;
} 
 ((INTERSECT {intersect=1;}
   | EXCEPT { intersect=0;}) 
  valueexpr2=valueExpr
 {
   node=`intersectexceptexpr(intersect, node, valueexpr2);
 }
)*; 



// [68]    	ValueExpr 	   ::=    	ValidateExpr | PathExpr
valueExpr returns [ATerm node=null; ]:
  validateExpr | pathexpr=pathExpr
{
  node=pathexpr;
}
; 




// [69]    	PathExpr 	   ::=    	("/" RelativePathExpr?)
// | ("//" RelativePathExpr)
// | RelativePathExpr 	/* gn: leading-lone-slash */
pathExpr returns [ATerm node=null;]:
 (SLASH (options {greedy=true;}: relativepathexpr=relativePathExpr)?
 {
   
 }
)  
 | (SLASH_SLASH relativePathExpr) 
 | relativePathExpr; 



// [70]    	RelativePathExpr 	   ::=    	StepExpr (("/" | "//") StepExpr)*
relativePathExpr returns [ATermList nodelist = null]: 
  stepexpr=stepExpr 
{
  nodelist=`concStepExpr(stepexpr);
}
 
 ((SLASH | SLASH_SLASH) morestepexpr = stepExpr
 {
   nodelist=`concStepExpr(nodelist, morestepexpr);
 }
  )*;
 



// [71]    	StepExpr 	   ::=    	AxisStep | FilterStep
 stepExpr returns [ATerm node=null;]: 
  axisStep | filterStep; 

// [72]    	AxisStep 	   ::=    	(ForwardStep | ReverseStep) Predicates
axisStep: 
  (forwardStep | reverseStep) predicates; 



// [73]    	FilterStep 	   ::=    	PrimaryExpr Predicates
filterStep: 
  primaryExpr predicates; 



// [74]    	ContextItemExpr 	   ::=    	"."
contextItemExpr: 
  DOT; 

// [75]    	PrimaryExpr 	   ::=    	Literal | VarRef | ParenthesizedExpr | ContextItemExpr | FunctionCall | Constructor
primaryExpr: 
  literal | varRef | parenthesizedExpr | contextItemExpr | functionCall | constructor; 

// [76]    	VarRef 	   ::=    	"$" VarName
varRef: 
  DOLLAR varName; 

// [77]    	Predicates 	   ::=    	("[" Expr "]")*
predicates:  
  (options {greedy=true;}:L_BRACKET expr R_BRACKET)*;

// [78]    	ValidateExpr 	   ::=    	(<"validate" "{"> | (<"validate" "global"> "{") | (<"validate" "context"> SchemaContextLoc "{") | (<"validate" SchemaMode> SchemaContext? "{")) Expr "}" 	/* gn: validate */
validateExpr: 
  ((VALIDATE L_BRACE) 
   | (VALIDATE GLOBAL L_BRACE) 
   | (VALIDATE CONTEXT schemaContextLoc L_BRACE) 
   | (VALIDATE schemaMode (schemaContext)? L_BRACE)) 
	expr 
	R_BRACE; 

// [79]    	SchemaContext 	   ::=    	("context" SchemaContextLoc) | "global"
schemaContext: 
  (CONTEXT schemaContextLoc) 
	| GLOBAL; 

// [80]    	Constructor 	   ::=    	DirElemConstructor
// | ComputedConstructor
// | XmlComment
// | XmlPI
// | CdataSection
constructor: 
  dirElemConstructor 
	| computedConstructor
	| xmlComment
	| xmlPI 
	| cdataSection; 

// [81]    	ComputedConstructor 	   ::=    	CompElemConstructor
// | CompAttrConstructor
// | CompDocConstructor
// | CompTextConstructor
// | CompXmlPI
// | CompXmlComment
// | CompNSConstructor
computedConstructor: 
  compElemConstructor
	| compAttrConstructor
	| compDocConstructor
	| compTextConstructor
	| compXmlPI
	| compXmlComment
	| compNSConstructor; 

// [82]    	GeneralComp 	   ::=    	"=" | "!=" | "<" | "<=" | ">" | ">=" 	/* gn: lt */
 generalComp returns [int value=0;]: 
  EQUAL {value=10;}
	| NOT_EQUAL {value=11;}
	| LESS {value=12;}
	| LESS_OR_EQUAL {value=13;}
	| GREATER {value=14;}
	| GREATER_OR_EQUAL {value=15;}; 

// [83]    	ValueComp 	   ::=    	"eq" | "ne" | "lt" | "le" | "gt" | "ge"
 valueComp returns [int value=0;]: 
 EQ {value=1;}
 | NE {value=2;}
 | LT {value=3;}
 | LE {value=4;}
 | GT {value=5;}
 | GE {value=6;}; 

// [84]    	NodeComp 	   ::=    	"is" | "<<" | ">>"
 nodeComp returns [int value=0;]: 
  IS {value=7;}
 | PRECEDE {value=8;}
 | FOLLOW {value=9;}; 

// [85]    	ForwardStep 	   ::=    	(ForwardAxis NodeTest) | AbbrevForwardStep
forwardStep: 
  (forwardAxis nodeTest) | abbrevForwardStep; 

// [86]    	ReverseStep 	   ::=    	(ReverseAxis NodeTest) | AbbrevReverseStep
reverseStep: 
  (reverseAxis nodeTest) | abbrevReverseStep; 

// [87]    	AbbrevForwardStep 	   ::=    	"@"? NodeTest
abbrevForwardStep: 
  (ARROBA)? nodeTest; 

// [88]    	AbbrevReverseStep 	   ::=    	".."
abbrevReverseStep: 
  DOT_DOT; 

// [89]    	ForwardAxis 	   ::=    	<"child" "::">
// | <"descendant" "::">
// | <"attribute" "::">
// | <"self" "::">
// | <"descendant-or-self" "::">
// | <"following-sibling" "::">
// | <"following" "::">
forwardAxis: 
  (CHILD
   | DESCENDANT
   | ATTRIBUTE 
   | SELF 
   | DESCENDANT_OR_SELF 
   | FOLLOWING_SIBLING 
   | FOLLOWING
   ) FOUR_DOT;



// [90]    	ReverseAxis 	   ::=    	<"parent" "::">
// | <"ancestor" "::">
// | <"preceding-sibling" "::">
// | <"preceding" "::">
// | <"ancestor-or-self" "::">
reverseAxis: 
  (PARENT 
   | ANCESTOR 
   | PRECEDING_SIBLING 
   | PRECEDING 
   | ANCESTOR_OR_SELF
   ) FOUR_DOT; 

// [91]    	NodeTest 	   ::=    	KindTest | NameTest
nodeTest: 
  kindTest 
	| nameTest; 

// [92]    	NameTest 	   ::=    	QName | Wildcard
nameTest: 
  QNAME 
	| wildCard; 

// [93]    	Wildcard 	   ::=    	"*"
// | <NCName ":" "*">
// | <"*" ":" NCName> 	/* ws: explicit */
wildCard: 
  STAR 
	| (NCNAME TWO_DOT STAR) 
	| (STAR TWO_DOT NCNAME); 

// [94]    	Literal 	   ::=    	NumericLiteral | StringLiteral
literal: 
  numericLiteral 
	| STRINGLITERAL; 

// [95]    	NumericLiteral 	   ::=    	IntegerLiteral | DecimalLiteral | DoubleLiteral
numericLiteral: 
  integerLiteral 
	| decimalLiteral 
	| doubleLiteral; 

// [96]    	ParenthesizedExpr 	   ::=    	"(" Expr? ")"
parenthesizedExpr: 
  L_PAR (expr)? R_PAR; 

// [97]    	FunctionCall 	   ::=    	<QName "("> (ExprSingle ("," ExprSingle)*)? ")"
functionCall: 
  QNAME L_PAR (exprSingle (COMMA exprSingle)*)? R_PAR;

// [98]    	DirElemConstructor 	   ::=    	"<" QName AttributeList ("/>" | (">" ElementContent* "</" QName S? ">")) 	/* ws: explicit */
dirElemConstructor: LESS QNAME attributeList (SLASH_GREATER | ( GREATER (elementContent)* LESS_SLASH QNAME GREATER));
	
	
// 	/* gn: lt */
// [99]    	CompDocConstructor 	   ::=    	<"document" "{"> Expr "}"
compDocConstructor: 
  DOCUMENT L_BRACE expr R_BRACE; 

// [100]    	CompElemConstructor 	   ::=    	(<"element" QName "{"> | (<"element" "{"> Expr "}" "{")) Expr? "}"
compElemConstructor: 
  ((ELEMENT QNAME L_BRACE) 
   | (ELEMENT L_BRACE expr R_BRACE L_BRACE)) (expr)? R_BRACE;

// [101]    	CompNSConstructor 	   ::=    	<"namespace" NCName "{"> Expr "}"
compNSConstructor: 
  NAMESPACE NCNAME L_BRACE expr R_BRACE;


// [102]    	CompAttrConstructor 	   ::=    	(<"attribute" QName "{"> | (<"attribute" "{"> Expr "}" "{")) Expr? "}"
compAttrConstructor: 
  ((ATTRIBUTE QNAME L_BRACE) 
   | (ATTRIBUTE L_BRACE expr R_BRACE L_BRACE)) (expr)? R_BRACE; 

// [103]    	CompXmlPI 	   ::=    	(<"processing-instruction" NCName "{"> | (<"processing-instruction" "{"> Expr "}" "{")) Expr? "}"
compXmlPI: 
  ((PROCESSING_INSTRUCTION NCNAME L_BRACE) 
   | (PROCESSING_INSTRUCTION L_BRACE expr R_BRACE L_BRACE)) (expr)? R_BRACE; 

// [104]    	CompXmlComment 	   ::=    	<"comment" "{"> Expr "}"
compXmlComment: 
  COMMENT L_BRACE expr R_BRACE; 

// [105]    	CompTextConstructor 	   ::=    	<"text" "{"> Expr? "}"
compTextConstructor: 
  TEXT L_BRACE (expr)? R_BRACE; 

// [106]    	CdataSection 	   ::=    	"<![CDATA[" Char* "]]>" 	/* ws: significant */
cdataSection: 
  "<![CDATA[" (CHARS)* "]]>";

// [107]    	XmlPI 	   ::=    	"<?" PITarget Char* "?>" 	/* ws: explicit */
xmlPI: 
  "<?" piTarget (CHARS)* "?>";

// [108]    	XmlComment 	   ::=    	"<!--" Char* "-->" 	/* ws: significant */
xmlComment: 
  "<!--" (CHARS)* "-->";

// [109]    	ElementContent 	   ::=    	ElementContentChar
// | "{{"
// | "}}"
// | DirElemConstructor
// | EnclosedExpr
// | CdataSection
// | CharRef
// | PredefinedEntityRef
// | XmlComment
// | XmlPI 	/* ws: significant */
elementContent: 
  elementContentChar 
	| L_BRACE_BRACE
	| R_BRACE_BRACE
	| dirElemConstructor 
	| enclosedExpr
	| cdataSection 
	| charRef
	| predefinedEntityRef
	| xmlComment
	| xmlPI; 

// [110]    	AttributeList 	   ::=    	(S (QName S? "=" S? AttributeValue)?)* 	/* ws: explicit */
attributeList: 
  (QNAME EQUAL attributeValue)*;

// [111]    	AttributeValue 	   ::=    	('"' (EscapeQuot | QuotAttrValueContent)* '"')
// | ("'" (EscapeApos | AposAttrValueContent)* "'") 	/* ws: significant */
attributeValue: 
  (DOUBLE_QUOTE (escapeQuot | quotAttrValueContent)* DOUBLE_QUOTE)
	| (SINGLE_QUOTE (escapeApos | aposAttrValueContent)* SINGLE_QUOTE);

// [112]    	QuotAttrValueContent 	   ::=    	QuotAttContentChar
// | CharRef
// | "{{"
// | "}}"
// | EnclosedExpr
// | PredefinedEntityRef 	/* ws: significant */
quotAttrValueContent: 
  quotAttContentChar 
	| charRef 
	| L_BRACE_BRACE 
	| R_BRACE_BRACE 
	| enclosedExpr 
	| predefinedEntityRef; 

// [113]    	AposAttrValueContent 	   ::=    	AposAttContentChar
// | CharRef
// | "{{"
// | "}}"
// | EnclosedExpr
// | PredefinedEntityRef 	/* ws: significant */
aposAttrValueContent: 
  charRef 
	| L_BRACE_BRACE 
	| R_BRACE_BRACE 
	| enclosedExpr
	| predefinedEntityRef; 

// [114]    	EnclosedExpr 	   ::=    	"{" Expr "}"
enclosedExpr:
  L_BRACE expr R_BRACE; 

// [115]    	XMLSpaceDecl 	   ::=    	<"declare" "xmlspace"> ("preserve" | "strip")
xmlSpaceDecl: 
  DECLARE XMLSPACE (PRESERVE | STRIP); 

// [116]    	DefaultCollationDecl 	   ::=    	<"declare" "default" "collation"> StringLiteral
defaultCollationDecl: 
  DECLARE DEFAULT COLLATION STRINGLITERAL;

// [117]    	BaseURIDecl 	   ::=    	<"declare" "base-uri"> StringLiteral
baseURIDecl:
  DECLARE BASE_URI STRINGLITERAL; 

// [118]    	NamespaceDecl 	   ::=    	<"declare" "namespace"> NCName "=" StringLiteral
namespaceDecl:
  DECLARE NAMESPACE NCNAME EQUAL STRINGLITERAL; 

// [119]    	DefaultNamespaceDecl 	   ::=    	(<"declare" "default" "element"> | <"declare" "default" "function">) "namespace" StringLiteral
defaultNamespaceDecl: 
  ((DECLARE DEFAULT ELEMENT) | (DECLARE DEFAULT FUNCTION)) NAMESPACE STRINGLITERAL; 

// [120]    	FunctionDecl 	   ::=    	<"declare" "function"> <QName "("> ParamList? (")" | (<")" "as"> SequenceType)) (EnclosedExpr | "external") 	/* gn: parens */
functionDecl: 
  DECLARE FUNCTION QNAME L_PAR (paramList)? (R_PAR | (R_PAR AS sequenceType)) (enclosedExpr | EXTERNAL);

// [121]    	ParamList 	   ::=    	Param ("," Param)*
paramList: 
  param (COMMA param)*;

// [122]    	Param 	   ::=    	"$" VarName TypeDeclaration?
param: 
  DOLLAR varName (typeDeclaration)?;

// [123]    	TypeDeclaration 	   ::=    	"as" SequenceType
typeDeclaration: 
  AS sequenceType; 

// [124]    	SingleType 	   ::=    	AtomicType "?"?
singleType: 
  atomicType (INTERROGATION)?; 

// [125]    	SequenceType 	   ::=    	(ItemType OccurrenceIndicator?)
// | <"empty" "(" ")">
sequenceType: 
  (itemType (occurrenceIndicator)?) 
  | (EMPTY L_PAR R_PAR); 

// [126]    	AtomicType 	   ::=    	QName
atomicType: 
  QNAME; 

// [127]    	ItemType 	   ::=    	AtomicType | KindTest | <"item" "(" ")">
itemType: 
  atomicType 
	| kindTest 
	| (ITEM L_PAR R_PAR); 

// [128]    	KindTest 	   ::=    	DocumentTest
// | ElementTest
// | AttributeTest
// | PITest
// | CommentTest
// | TextTest
// | AnyKindTest
kindTest: 
  documentTest 
	| elementTest 
	| attributeTest
	| piTest
	| commentTest
	| textTest 
	| anyKindTest; 

// [129]    	ElementTest 	   ::=    	<"element" "("> ((SchemaContextPath ElementName)
// | (ElementNameOrWildcard ("," TypeNameOrWildcard "nillable"?)?))? ")"
elementTest: 
  ELEMENT L_PAR 
	(options { greedy=true;} :
	 (schemaContextPath elementName) 
	 | (elementNameOrWildcard 
		(options {greedy=true;}:
		 COMMA typeNameOrWildcard (options {greedy=true;}: NILLABLE)?
		 )?
		)
	 )? R_PAR; 


// [130]    	AttributeTest 	   ::=    	<"attribute" "("> ((SchemaContextPath AttributeName)
// | (AttribNameOrWildcard ("," TypeNameOrWildcard)?))? ")"
attributeTest: 
  ATTRIBUTE L_PAR 
	(options {greedy=true;}:
	 (schemaContextPath attributeName) 
	 | (attribNameOrWildcard 
		(options {greedy=true;}:
		 COMMA typeNameOrWildcard)?)
	 )? 
	R_PAR; 

// [131]    	ElementName 	   ::=    	QName
elementName:
  QNAME; 

// [132]    	AttributeName 	   ::=    	QName
attributeName:
  QNAME; 

// [133]    	TypeName 	   ::=    	QName
typeName:
  QNAME; 

// [134]    	ElementNameOrWildcard 	   ::=    	ElementName | "*"
elementNameOrWildcard: 
  elementName | STAR; 

// [135]    	AttribNameOrWildcard 	   ::=    	AttributeName | "*"
attribNameOrWildcard:
  attributeName | STAR; 

// [136]    	TypeNameOrWildcard 	   ::=    	TypeName | "*"
typeNameOrWildcard:
  typeName 
	| STAR; 

// [137]    	PITest 	   ::=    	<"processing-instruction" "("> (NCName | StringLiteral)? ")"
piTest: 
  PROCESSING_INSTRUCTION L_PAR (NCNAME | STRINGLITERAL)? R_PAR; 

// [138]    	DocumentTest 	   ::=    	<"document-node" "("> ElementTest? ")"
documentTest:
  DOCUMENT_NODE L_PAR (elementTest)? R_PAR; 

// [139]    	CommentTest 	   ::=    	<"comment" "("> ")"
commentTest:
  COMMENT L_PAR R_PAR;

// [140]    	TextTest 	   ::=    	<"text" "("> ")"
textTest: 
  TEXT L_PAR R_PAR; 

// [141]    	AnyKindTest 	   ::=    	<"node" "("> ")"
anyKindTest: 
  NODE L_PAR R_PAR; 

// [142]    	SchemaContextPath 	   ::=    	<SchemaGlobalContext "/"> <SchemaContextStep "/">*
schemaContextPath: 
  schemaGlobalContext SLASH (options {greedy=true;}:schemaContextStep SLASH)*; 

// [143]    	SchemaContextLoc 	   ::=    	(SchemaContextPath? QName) | SchemaGlobalTypeName
schemaContextLoc: 
  ((options {greedy=true;}:schemaContextPath)? QNAME) | schemaGlobalTypeName; 

// [144]    	OccurrenceIndicator 	   ::=    	"?" | "*" | "+"
occurrenceIndicator: 
  INTERROGATION 
	| STAR
	| PLUS; 

// [145]    	ValidationDecl 	   ::=    	<"declare" "validation"> SchemaMode
validationDecl:
  DECLARE VALIDATION schemaMode; 

// [146]    	SchemaImport 	   ::=    	<"import" "schema"> SchemaPrefix? StringLiteral <"at" StringLiteral>?
schemaImport: 
  IMPORT SCHEMA (schemaPrefix)? STRINGLITERAL (AT STRINGLITERAL)?;

// [147]    	SchemaPrefix 	   ::=    	("namespace" NCName "=") | (<"default" "element"> "namespace")
schemaPrefix: 
  (NAMESPACE NCNAME EQUAL) 
  | (DEFAULT ELEMENT NAMESPACE);  





class XQueryLexer extends Lexer;

options {
    k=4; // needed for newline junk
	testLiterals=false;    // don't automatically test for literals
    charVocabulary='\u0000'..'\u007F'; // allow ascii
}

tokens {
  PRAGMA				= "pragma";
  SKIP_KEYWORD			= "skip" ;
  LAX					= "lax" ;
  STRICT				= "strict" ;
  TYPE					= "type" ;
  NAMESPACE				= "namespace" ;
  ELEMENT				= "element" ;
  DEFAULT_				= "default" ;
  AT					= "at" ;
  SCHEMA				= "schema" ;
  IMPORT				= "import" ;
  VALIDATION			= "validation" ;
  DECLARE				= "declare" ;
  NODE					= "node" ;
  TEXT					= "text" ;
  COMMENT				= "comment" ;
  PROCESSING_INSTRUCTION= "processing-instruction" ;
  DOCUMENT_NODE			= "document-node" ;
  ATTRIBUTE				= "attribute" ;
  NILLABLE				= "nillable" ;
  ITEM					= "item" ;
  EMPTY					= "empty" ;
  AS					= "as" ;
  EXTERNAL				= "external" ;
  FUNCTION				= "function" ;
  BASE_URI				= "base_uri" ;
  STRIP					= "strip" ;
  PRESERVE				= "preserve"; 
  XMLSPACE				= "xmlspace" ;
  COLLATION				= "collation" ;
  DOCUMENT				= "document" ;
  ANCESTOR_OR_SELF		= "ancestor_or_self" ;
  PRECEDING				= "preceding" ;
  PRECEDING_SIBLING		= "preceding_sibling" ;
  ANCESTOR				= "ancestor" ;
  PARENT				= "parent" ;
  FOLLOWING				= "following" ;
  FOLLOWING_SIBLING		= "following_sibling" ;
  DESCENDANT_OR_SELF	= "descendant_or_self" ;
  SELF					= "self" ;
  DESCENDANT			= "descendant" ;
  CHILD					= "child" ;
  CONTEXT				= "context"; 
  VALIDATE				= "validate"; 
  GLOBAL				= "global" ;
  EXCEPT				= "except" ;
  INTERSECT				= "intersect" ;
  UNION					= "union" ;
  MOD					= "mod" ;
  DIV					= "div" ;
  IDIV					= "idiv" ;
  MULTIPLY				= "multiply" ;
  TO					= "to" ;
  CAST					= "cast"; 
  CASTABLE				= "castable" ;
  TREAT					= "treat" ;
  OF					= "of" ;
  INSTANCE				= "instance" ;
  AND					= "and" ;
  OR					= "or" ;
  IF					= "if" ;
  THEN					= "then" ;
  ELSE					= "else"; 
  RETURN				= "return"; 
  CASE					= "case" ;
  TYPESWITCH			= "typeswitch" ;
  SATISFIED				= "satisfied" ;
  IN					= "in" ;
  EVERY					= "every" ;
  SOME					= "some" ;
  LEAST					= "least" ;
  GREATEST				= "greatest" ;
  DESCENDING			= "descending"; 
  ASCENDING				= "ascending" ;
  BY					= "by" ;
  ORDER					= "order"; 
  STABLE				= "stable" ;
  WHERE					= "where" ;
  SET_EQUAL_TO			= ":="; 
  LET					= "let" ;
  FOR					= "for" ;
  MODULE				= "module"; 
  XQUERY				= "xquery" ;
  VERSION				= "version" ;
  VARIABLE				= "variable" ;
  AMP					= "amp" ;
  QUOT					= "quot" ;
  APOS					= "apos" ;
  IS					= "is" ;
  GE					= "ge" ;
  GT					= "gt" ;
  LE					= "le" ;
  LT					= "lt" ;
  NE					= "ne" ;
  EQ					= "eq" ;


} // token definition


DOT						: '.' ;
L_PAR_FOUR_DOT			: "(::";
FOUR_DOT_R_PAR			: "::)" ;
L_PAR_TWO_DOT			: "(:";
TWO_DOT_R_PAR			: ":)" ;
L_PAR					: '(' ;
R_PAR					: ')' ;
EQUAL					: '=' ;
PLUS					: '+' ;
MINUS					: '-' ;
STAR					: '*' ;
INTERROGATION			: '?' ;
SLASH					: '/' ;
protected DOUBLE_QUOTE			: "\"" ;
protected SINGLE_QUOTE			: "\'" ;
protected DOT_DOT				: ".." ;
LESS					: '<' ;
DOLLAR					: '$'; 
ARROBA					: '@' ;
FOLLOW					: ">>" ;
PRECEDE					: "<<" ;
L_BRACE					: '{' ;
R_BRACE					: '}' ;
GREATER					: '>' ;
LESS_SLASH				: "</"; 
TWO_DOT					: ':' ;
FOUR_DOT				: "::" ;
SLASH_GREATER			: "/>"; 
GREATER_OR_EQUAL		: ">=" ;
LESS_OR_EQUAL			: "<=" ;
NOT_EQUAL				: "!=" ;
L_BRACKET				: '[' ;
R_BRACKET				: ']' ;
SLASH_SLASH				: "//"; 
BAR						: '|' ;
L_BRACE_BRACE			: "{{" ;
R_BRACE_BRACE			: "}}" ;
SEMICOLON				: ';' ;
COMMA					: ',' ;



protected DIGIT			: '0'..'9' ;
protected DIGITS        : (DIGIT)+;

protected UNDERLINE		: '_' ;
protected LETTER		: ('a'..'z' | 'A'..'Z') ;

protected CHAR			: ('a'..'z'  | 'A'..'Z' | '0'..'9') ;
protected CHARS         : (CHAR)+;

protected HEX_DIGIT		: ('a'..'f'  | 'A'..'F' | '0'..'9') ;
protected HEX_DIGITS    : (HEX_DIGIT)+ ;

protected NCNAMECHAR 	: LETTER | DIGIT | DOT | MINUS | UNDERLINE;


NCNAME  options {testLiterals = true; }: 
  ( LETTER  | "_") (options{greedy=true;}: NCNAMECHAR )* 
	(TWO_DOT ( LETTER  | "_") (options{greedy=true;}: NCNAMECHAR )* {$setType(QNAME)})?;

// QNAME options {testLiterals = true; } : 
//    ( NCNAME TWO_DOT {$setType(QNAME);})?  NCNAME;


STRING_LITERAL options {testLiterals = true; } 
  : '"'!
    ( '"' '"'!
	  | ~('"'|'\n'|'\r')
	  )*
    ( '"'!
    | // nothing -- write error message
    )
  ;

// Whitespace -- ignored
WS
  : ( ' '
	  | '\t'
	  | '\f'
	  
	  // handle newlines
	  | ( "\r\n"  // DOS
		  | '\r'    // Macintosh
		  | '\n'    // Unix 
		  )  
	{ newline(); }
	  )
	
{ $setType(Token.SKIP); }
; 


