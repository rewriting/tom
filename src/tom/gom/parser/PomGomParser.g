header {
  /*
   * Gom
   *
   * Copyright (c) 2005-2006, INRIA
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
   * Antoine Reilles    e-mail: Antoine.Reilles@loria.fr
   *
   **/
  package tom.gom.parser;
}
{
  import antlr.LexerSharedInputState;
}
//{{{class PomGomParser extends Parser;
class PomGomParser extends Parser;
options {
  buildAST = true;  // uses CommonAST by default
  k=2; // because of field definition
  defaultErrorHandler = false;
}
{
  private LexerSharedInputState lexerstate = null;

  public PomGomParser(PomGomLexer lexer, String name) {
    this(lexer);
    /* the name attribute is used for constructor disambiguation */
    this.lexerstate = lexer.getInputState();
  }
}

module: MODULE^ moduleName:ID (imports)? section ;

imports: IMPORTS^ (importedModuleName:ID)* ;

section : (PUBLIC)? grammar ;

grammar : (sortdef | syntax)+ ;

sortdef: SORTS^ (type)* ;

type: i:ID ;

syntax: ABSTRACT! SYNTAX^ (production | hook | typedecl)* ;

production: id:ID fieldlist ARROW^ type ;

typedecl : id:ID EQUALS^ alternatives ;

alternatives : (ALT)? id:ID fieldlist (ALT altid:ID fieldlist)* (SEMI)? ;

fieldlist: LEFT_BRACE! (field (COMMA field)* )? RIGHT_BRACE! ;

arglist: LEFT_BRACE! (arg:ID(COMMA supplarg:ID)* )? RIGHT_BRACE! ;

hook
{
  String code = "";
}
:! id:ID COLON^ type:hooktype args:arglist 
// '!' turns off auto transform
{
  BlockParser blockparser = BlockParser.makeBlockParser(lexerstate);
  code = blockparser.block();

#hook = #(COLON,id,type,args);
#hook.setText(code);
}
;
hooktype: tp:ID ;

field: type STAR^ | id:ID COLON^ type ;

class PomGomLexer extends Lexer;
options {
  k = 2; // \r and \r\n
}
tokens
{
  MODULE   = "module";
  IMPORTS  = "imports";
  PUBLIC   = "public";
  PRIVATE  = "private";
  SORTS    = "sorts";
  ABSTRACT = "abstract";
  SYNTAX   = "syntax";
}

ARROW       : "->";
COLON       : ':';
COMMA       : ',';
LEFT_BRACE  : '(';
RIGHT_BRACE : ')';
STAR        : '*';
EQUALS      : '=';
ALT         : '|';
SEMI        : ";;";


LBRACE
: '{'
;

RBRACE
: '}'
;

WS : ( ' '
       | '\t'
       | ( "\r\n" // DOS
           | '\n'   // Unix
           | '\r'   // Macintosh
           ){ newline(); }
       ){$setType(Token.SKIP);}
;
SLCOMMENT
  :       "//"
    (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
{
  $setType(Token.SKIP);
  newline();
}
;

ML_COMMENT
  :       "/*"
    (
     options {
       generateAmbigWarnings=false;
     }
     :
    { LA(2)!='/' }? '*'
     | '\r' '\n' {newline();}
     | '\r'      {newline();}
     | '\n'      {newline();}
     | ~('*'|'\n'|'\r')
     )*
    "*/"
{$setType(Token.SKIP);}
;

ID //options{ testLiterals = true; }
: ('a'..'z' | 'A'..'Z')
  ('a'..'z' | 'A'..'Z' | '0'..'9' | '_' | '-')* ;
