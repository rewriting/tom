/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2006, INRIA
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

grammar Tom;


@header { package tom.engine.parser; }
@lexer::members {
  //this is for Token NUM_INT only
  protected boolean isDecimal=false;
  protected Token t=null;
}

//here either we use JAVA_TOM or GENERIC_TOM

//JAVA_TOM
/***************************************************/

javaMatch
  :
            '(' javaMatchArguments ')'
            '{'
            ( 
             patternInstruction
            )* 
      '}' {token = Token.EOF_TOKEN;}
          {System.out.println("exit tom");}
  ;
javaMatchArguments
    :   javaMatchArgument( ',' javaMatchArgument)*
    ;

javaMatchArgument 
    :   ALL_ID '`'? ALL_ID 
    ;


//GENERIC_TOM
/***************************************************/
genericMatch 
  : 
            '(' genericMatchArguments ')' '{'
            ( 
             patternInstruction
            )* 
      '}' {token = Token.EOF_TOKEN;}
          {System.out.println("exit tom");}
  ;
genericMatchArguments
    :   genericMatchArgument( ',' genericMatchArgument)*
    ;

genericMatchArgument 
    :   
    ALL_ID '`'? ALL_ID 
    ;

//KERNEL TOM
/***************************************************/
patternInstruction
    :   
            ( (ALL_ID ':') => ALL_ID ':')?
            matchPattern
            ( 
                '|' matchPattern
            )* 
            ( 'when' matchGuards)?
            '->' '{'
            {
            if (java){
            Java15Lexer lex = new Java15Lexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lex);
            //System.out.println("tokens="+tokens);
            Java15 parser = new Java15(tokens);
            parser.statementList();
            }
            else {
            //FIXME
            }
            }
            {channel=99;}
    ;

matchPattern
    :   annotatedTerm ( ',' annotatedTerm)*
    ;

matchGuards 
    :   annotatedTerm (',' annotatedTerm)*
    ;

// the %include construct :
includeConstruct 
  :  '{' STRING '}'
            {token = Token.EOF_TOKEN;}
          {System.out.println("exit tom");}
  ;

extendsBqTerm
  :   'extends' ALL_ID
;

// The %strategy construct
strategyConstruct
    :
        name=ALL_ID
        (
            '(' (slotName=ALL_ID ':' typeArg=ALL_ID 
            (
               ',' 
                slotName2=ALL_ID ':' typeArg2=ALL_ID
            )*
            )? ')'
        )
        extendsBqTerm
        
       '{' 
        strategyVisitList
      '}' {token = Token.EOF_TOKEN;}
          {System.out.println("exit tom");}
    ;

strategyVisitList
 :   ( strategyVisit)* ;

strategyVisit
 :   visit=ALL_ID type=ALL_ID  '{' (patternInstruction)* '}'
;

// The %rule construct
ruleConstruct
    :
       '{' 
        (
            annotatedTerm 
            ( '|' annotatedTerm
            )*
 
            '->'  plainTerm
            (
                'where' annotatedTerm ':=' annotatedTerm 
            |   'if' annotatedTerm '==' annotatedTerm
            )*
        )*
      '}' {token = Token.EOF_TOKEN;}
          {System.out.println("exit tom");}
    ;

// terms for %match and %rule
annotatedTerm
    : 
            ( 
                (ALL_ID '@') => name=ALL_ID '@' 
            )? 
            
            plainTerm
    ;

plainTerm
options {
backtrack=true;
}
    :  
            xmlTerm

        |   // var* or _*
            (variableStar) => variableStar

        |   // _
            placeHolder

        |   // for a single constant. 
            // ambiguous with the next rule so :
            {LA(2) != '(' && LA(2) != '['}? 
            headSymbol

        |   // for a single anti constant. 
        	// ambiguous with the next rule so :
	        {LA(3) != '(' && LA(3) != '['}? 
	        antiHeadSymbol

        |   // for a single constant. 
            // ambiguous with the next rule so :
            {LA(2) != '(' && LA(2) != '['}? 
            headConstant

        |   // for a single anti constant. 
        	// ambiguous with the next rule so :
            {LA(3) != '(' && LA(3) != '['}? 
            antiHeadConstant

        |   // f(...) or f[...]
            headSymbol
            args
            
       |   // !f(...) or !f[...]
            antiHeadSymbol
            args
            
        |   // (f|g...) 
            // ambiguity with the last rule so use a lookahead
            // if '|' then parse headSymbolList
        {LA(3) == '|'}? headSymbolList
            //( (args[null,null]) => implicit = args[list, secondOptionList] {withArgs = true;})?
            args
            
        |   // (...)
            args
    ;



xmlTerm
    :
            // < NODE attributes [ /> | > childs </NODE> ]
         '<'
            xmlNameList
            xmlAttributeList
            
            (   // case: /> 
               '/>' 
            |   // case: > childs  </NODE>
               '>' 
                xmlChilds

               '</' 
                xmlNameList
                t='>'
                
            ) // end choice
            

        |     '#TEXT' '(' annotatedTerm ')'
            
        |    '#COMMENT' '(' termStringIdentifier ')'
            
        |    '#PROCESSING-INSTRUCTION' '('
            termStringIdentifier ',' termStringIdentifier
            ')'
    ;


xmlAttributeList

    :
            '[' 
            (
                xmlAttribute
                (
                    ','
                    xmlAttribute
                )*  
            )?
            ']'
        |
            '('
            (
                xmlAttribute
                (
                    ','
                    xmlAttribute
                )*  
            )?
            ')'
        |
            (
               {LA(1) != '>'}? xmlAttribute
            )*
    ;

xmlAttribute
    :
            // _* | X*
            {LA(2) == '*'}?
            variableStar
        |   // name = [anno2@](_|String|Identifier)
            {LA(2) == '='}?  id=ALL_ID '=' 
            (
                {LA(2) == '@'}? anno2=ALL_ID '@'
                
            )?
            unamedVariableOrTermStringIdentifier
            
        | // [anno1@]_ = [anno2@](_|String|Identifier)
            (
                anno1=ALL_ID '@'
                
            )?
            placeHolder
            e='='
            (
                {LA(2) == '@'}? anno3=ALL_ID '@'
                
            )?
            unamedVariableOrTermStringIdentifier
    ;

// This corresponds to the implicit notation
xmlTermList
    :   (annotatedTerm)*
    ;

xmlNameList
    :
            name=ALL_ID
            
        |   name2=UNDERSCORE
            
        |   '(' name3=ALL_ID
            
            (
                '|' name4=ALL_ID
            )*
            ')'
    ;

termStringIdentifier
    :   nameID=ALL_ID
    |   nameString=STRING
    ;


unamedVariableOrTermStringIdentifier
    :   nameUnderscore=UNDERSCORE
    |   nameID=ALL_ID
    |   nameString=STRING 
    ;

// return true for implicit mode
implicitTermList
    :
            '['
            (
                annotatedTerm
                (
                    ','
                    annotatedTerm
                )*
            )?
            ']'
    ;


xmlChilds
    :
            //(implicitTermList[null]) => 
            {LA(1) == '['}? implicitTermList
        |   xmlTermList
    ;


args
    :   (
            // (term , term , ...)
            t1='('
            ( termList)? 
            t2=')' 
            
        |   // [term = term , term = term , ...]
            t3='['
            ( pairList)? 
            t4=']' 
        )
    ;

termList
    :   annotatedTerm ( ',' annotatedTerm)*
    ;

pairList
    :   name=ALL_ID '=' annotatedTerm ( ',' name2=ALL_ID '=' annotatedTerm)*
    ;
   
// _* or var*       
variableStar
    : 
            ( 
                name1=ALL_ID 
            |   name2=UNDERSCORE 
            ) 
            t='*' 
    ;

// _
placeHolder
    :   UNDERSCORE 
    ;

// ( id | id | ...)
headSymbolList
    :  
            '('
            headSymbolOrConstant

            '|'
            headSymbolOrConstant

            ( 
                '|'
                headSymbolOrConstant
            )* 
            t=')' 
    ;

headSymbolOrConstant
 :   headSymbol
 |   headConstant
 ;

headSymbol 
:   i=ALL_ID 
;

antiHeadSymbol
: 
  '!' headSymbol
;


headConstant
 :  constant // add to symbol table
;

antiHeadConstant
   :  '!' headConstant// add to symbol table     
  ;

// Operator Declaration
operator
    :
      type=ALL_ID name=ALL_ID 
        (
            '(' (slotName=ALL_ID ':' typeArg=ALL_ID 
            (
                ','
                slotName2=ALL_ID ':' typeArg2=ALL_ID
            )*
            )? ')'
        )
        '{'
        (
            keywordMake

        |   keywordGetSlot
               
        |   keywordIsFsym
        )*
      '}' {token = Token.EOF_TOKEN;}
          {System.out.println("exit tom");}
    ;

operatorList
    :
        type=ALL_ID name=ALL_ID
        '(' typeArg=ALL_ID '*' ')'
        '{'
        (
            keywordMakeEmptyList

        |   keywordMakeAddList

        |   keywordIsFsym

        |   keywordGetHead

        |   keywordGetTail

        |   keywordIsEmpty

        )*
      '}' {token = Token.EOF_TOKEN;}
          {System.out.println("exit tom");}
    ;

operatorArray
    :
        type=ALL_ID name=ALL_ID
        '(' typeArg=ALL_ID '*' ')'
        '{'
        (
            keywordMakeEmptyArray

        |   keywordMakeAddArray

        |   keywordIsFsym

        |   keywordGetElement

        |   keywordGetSize
        )*
      '}' {token = Token.EOF_TOKEN;}
          {System.out.println("exit tom");}
    ;

typeTerm
    : 
            type=ALL_ID
            '{'

            keywordImplement
            (    keywordVisitorFwd
            |   keywordEquals
            |   keywordCheckStamp
            |   keywordSetStamp
            |   keywordGetImplementation
            )*
      '}' {token = Token.EOF_TOKEN;}
          {System.out.println("exit tom");}
    ;

keywordImplement
    : 'implement' 
    ;

keywordVisitorFwd
    :   'visitor_fwd' 
    ;
  
keywordEquals
    :   '=' '(' name1=ALL_ID ',' name2=ALL_ID ')'
    ;
keywordGetHead
    :   'get_head' '(' name=ALL_ID ')'
    ;

keywordGetTail
    :    'get_tail' '(' name=ALL_ID ')'
    ;

keywordIsEmpty
    :    'is_empty' '(' name=ALL_ID ')'
    ;

keywordGetElement
    :     'get_element' '(' name1=ALL_ID ',' name2=ALL_ID ')'
    ;

keywordGetSize
    :    'get_size' '(' name=ALL_ID ')'
    ;

keywordIsFsym
    :   'is_fsym' '(' name=ALL_ID ')'
    ;

keywordCheckStamp
    :   'check_stamp' '(' name=ALL_ID ')'
    ;

keywordSetStamp
    :   'set_stamp' '(' name=ALL_ID ')'
    ;

keywordGetImplementation
    :   'get_implementation' '(' name=ALL_ID ')'
    ;

keywordGetSlot
    :    'get_slot' '(' slotName=ALL_ID ',' name=ALL_ID ')'
    ;

keywordMake
    : 'make' ('(' (typeArg=ALL_ID (',' nameArg=ALL_ID)*)? ')' )? l='{'
    ;

keywordMakeEmptyList
    :  'make_empty' ('(' ')')? '{'
    ;

keywordMakeAddList
    :  'make_insert' '(' elementName=ALL_ID ',' listName=ALL_ID ')' '{'
    ;

keywordMakeEmptyArray
    :  'make_empty' '(' listName=ALL_ID ')' '{'
    ;   

keywordMakeAddArray
    :  'make_append' '(' elementName=ALL_ID ',' listName=ALL_ID ')' '{'
    ;

//LEXER
constant 
  : NUM_INT
  | CHARACTER
  | STRING
  | NUM_FLOAT
  | NUM_LONG
  | NUM_DOUBLE
  ;

UNDERSCORE  :   {!Character.isJavaIdentifierPart(LA(2))}? '_' ; 

// tokens to skip : white spaces
WS  : ( ' '
    | '\t'
    | '\f'
    // handle newlines
    | ( '\r\n'  // Evil DOS
      | '\r'    // Macintosh
      | '\n'    // Unix (the right way)
      )
      { newline(); }
    )
        {channel=99;}
  ;


// tokens to skip : Single Line Comments
LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' {channel=99;}
  ;

COMMENT
    :   '/*' ( options {greedy=false;} : . )* '*/' {channel=99;}
    ;

CHARACTER
  : '\'' ( ESC | ~('\''|'\n'|'\r'|'\\') )+ '\''
  ;

STRING
  : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"'
  ;

protected
ESC
  : '\\'
    ( 'n'
    | 'r'
    | 't'
    | 'b'
    | 'f'
    | '"'
    | '\''
    | '\\'
    | ('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    | '0'..'3'
      (
      : '0'..'7'
        (
        : '0'..'7'
        )?
      )?
    | '4'..'7'
      (
      : '0'..'7'
      )?
    )
  ;

protected
HEX_DIGIT
  : ('0'..'9'|'A'..'F'|'a'..'f')
  ;

protected LETTER    :   ('a'..'z' | 'A'..'Z')   ;
protected DIGIT     :   ('0'..'9')  ;

ALL_ID
    :   (ID_MINUS) => ID_MINUS
    |   ID
    ;

protected ID
    :
        '_'? LETTER
        ( 
            options{greedy = true;}:
            ( LETTER | DIGIT | '_' )
        )* 
    ;   

protected ID_MINUS
    :
        ID '-' ('a'..'z' | 'A'..'Z') 
        ( 
            '-' ('a'..'z' | 'A'..'Z') 
        |   ID
        )*
    ;

NUM_INT
    :   '-'?
    (
   '.' 
            ( ('0'..'9')+ (EXPONENT)? (f1=FLOAT_SUFFIX {t=f1;})?
                {
        if (t != null && t.getText().toUpperCase().indexOf('F')>=0) {
                  _ttype = NUM_FLOAT;
        }
        else {
                  _ttype = NUM_DOUBLE; // assume double
        }
        }
            )?

  | ( '0' {isDecimal = true;} // special case for just '0'
      ( ('x'|'X')
        (                     // hex
          // the 'e'|'E' and float suffix stuff look
          // like hex digits, hence the (...)+ doesn't
          // know when to stop: ambig.  ANTLR resolves
          // it correctly by matching immediately.  It
          // is therefor ok to hush warning.
        : HEX_DIGIT
        )+

      | //float or double with leading zero
        (('0'..'9')+ ('.'|EXPONENT|FLOAT_SUFFIX)) => ('0'..'9')+

      | ('0'..'7')+                 // octal
      )?
    | ('1'..'9') ('0'..'9')*  {isDecimal=true;}   // non-zero decimal
    )
    ( ('l'|'L') { _ttype = NUM_LONG; }

    // only check to see if it's a float if looks like decimal so far
    | {isDecimal}?
            (   '.' ('0'..'9')* (EXPONENT)? (f2=FLOAT_SUFFIX {t=f2;})?
            |   EXPONENT (f3=FLOAT_SUFFIX {t=f3;})?
            |   f4=FLOAT_SUFFIX {t=f4;}
            )
            {
      if (t != null && t.getText().toUpperCase() .indexOf('F') >= 0) {
                _ttype = NUM_FLOAT;
      }
            else {
              _ttype = NUM_DOUBLE; // assume double
      }
      }
        )?
    )
  ;
protected EXPONENT      :   ('e'|'E') ( '+' | '-' )? ('0'..'9')+  ;
protected FLOAT_SUFFIX  : 'f'|'F'|'d'|'D' ;

JAVA  :   '{'
            {
            System.out.println("enter embedded Java escape");
            Java15Lexer lex = new Java15Lexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lex);
            //System.out.println("tokens="+tokens);
            Java15 parser = new Java15(tokens);
            parser.statement();
            }
            {channel=99;}
        ;

/** When the tom parser sees end-of-comment it just says 'I'm done', which
 *  consumes the tokens and forces this tom parser (feeding
 *  off the input stream currently) to exit.  It returns from
 *  method tom_construct(), which was called from TOM action in the
 *  Java parser's lexer.
 */
END     : '}' {token = Token.EOF_TOKEN;}
          {System.out.println("exit tom");}
        ;

