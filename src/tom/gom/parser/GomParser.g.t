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
  import java.util.List;
  import java.util.LinkedList;
  import java.util.Iterator;
  import java.util.logging.Logger;
  import java.util.logging.Level;

  import aterm.*;
  import tom.gom.GomMessage;
  import tom.gom.tools.GomEnvironment;
  import tom.gom.tools.error.GomRuntimeException;
  import tom.gom.adt.gom.*;
  import tom.gom.adt.gom.types.*;
  import antlr.LexerSharedInputState;
}
class GomParser extends Parser;
options {
  k=2; // because of field definition
  defaultErrorHandler = false;
}
{
  %include { tom/gom/adt/gom/Gom.tom}

  private static final String REAL ="real";
  private static final String DOUBLE ="double";

  private LexerSharedInputState lexerstate = null;

  public GomParser(GomLexer lexer, String name) {
    this(lexer);
    /* the name attribute is used for constructor disambiguation */
    this.lexerstate = lexer.getInputState();
  }

  private GomTypeList typeListFromList(List list) {
    GomTypeList typeList = `concGomType();
    Iterator it = list.iterator();
    while(it.hasNext()) {
      GomType type = (GomType) it.next();
      typeList = `concGomType(type,typeList*);
    }
    return typeList;
  }
}

module returns [GomModule module]
{
  module = null;
  GomModuleName gomModuleName = null;
  ImportList impor = `concImportedModule();
  Section parsedsection = null;
}
: MODULE moduleName:IDENTIFIER
{ gomModuleName = `GomModuleName(moduleName.getText()); }
(impor = imports)?
  parsedsection = section
{
  module = `GomModule(gomModuleName,
      concSection(Imports(impor),parsedsection));
}
;

imports returns [ImportList imports]
{
  imports = `concImportedModule();
}
: IMPORTS
  (importedModuleName:IDENTIFIER
   {
   imports = `concImportedModule(
       Import(GomModuleName(importedModuleName.getText())), imports*);
   }
   )*
;

section returns [Section parsedsection]
{
  parsedsection = null;
  GrammarList grammarlist = `concGrammar();
}
: (PUBLIC)?
(grammarlist = grammar)
{
  parsedsection = `Public(grammarlist);
}
;

grammar returns [GrammarList grammars]
{
  grammars = `concGrammar();
  ProductionList prods = `concProduction();
  GomTypeList sorts = `concGomType();
  List sortList = new LinkedList();
}
: ( sorts = sortdef
    { grammars = `concGrammar(grammars*,Sorts(sorts)); }
    |
    prods = syntax[sortList]
    {
    if(!sortList.isEmpty()) {
      sorts = typeListFromList(sortList);
      grammars = `concGrammar(grammars*,Sorts(sorts));
    }
    grammars = `concGrammar(grammars*,Grammar(prods));
    }
  )+
;

sortdef returns [GomTypeList definedSorts]
{
  definedSorts = `concGomType();
  String sortName = null;
}
: SORTS
(
 sortName = type
 {
 /* Warns the user about deprecated syntax */
 Logger.getLogger(getClass().getName()).log(Level.WARNING,
   GomMessage.deprecatedSyntax.getMessage(),
   new Object[]{});
 definedSorts = `concGomType(GomType(sortName), definedSorts*);
 }
 )*
;

type returns [String id]
{ id=null; }
: i:IDENTIFIER {
  if(i.getText().equals(DOUBLE)) {
    id=REAL;
    Logger.getLogger(getClass().getName()).log(Level.WARNING,
        "Please prefer to use real in place of double\n"+
        "Automatic conversion done since double is a reserved word");
  } else {
    id=i.getText();
  }
}
;

syntax [List sortlist] returns [ProductionList prods]
{
  prods = `concProduction();
  Production prod = null;
  ProductionList productions = null;
}
: ABSTRACT SYNTAX
(
 prod = production
 { prods = `concProduction(prod,prods*); }
 |
 prod = hook
 { prods = `concProduction(prod,prods*); }
 |
 productions = typedecl[sortlist]
 { prods = `concProduction(productions*,prods*); }
 )*
;

production returns [Production prod]
{
  prod = null;
  String opName=null, typeName =null;
  FieldList fieldList = `concField();
}
: id:IDENTIFIER { opName = id.getText(); }
  fieldList = fieldlist
  ARROW typeName = type
{
  prod = `Production(opName,fieldList,GomType(typeName));
}
;

typedecl [List sortList] returns [ProductionList list]
{
  list = null;
  GomType type = null;
}
: id:IDENTIFIER
  { type=`GomType(id.getText()); sortList.add(type); }
  EQUALS
  list = alternatives[type]
;

alternatives [GomType type] returns [ProductionList list]
{
  list = `concProduction();
  String opName = null;
  FieldList fieldList = null;
}
: (ALT)? id:IDENTIFIER { opName = id.getText(); }
  fieldList = fieldlist
{ list = `concProduction(Production(opName,fieldList,type),list*); }
( ALT altid:IDENTIFIER { opName = altid.getText(); }
 fieldList = fieldlist
 { list = `concProduction(Production(opName,fieldList,type),list*); }
 )*
(SEMI)?
;

fieldlist returns [FieldList list]
{
  list = `concField();
  Field field = null;
}
: LEFT_BRACE (field = field
    { list = `concField(list*,field); }
    ( COMMA field = field
      { list = `concField(list*,field); }
    )* )?
RIGHT_BRACE
;

hook returns [Production prod]
{
  prod = null;
  String opName = null;
  Hookkind hooktype = null;
  ArgList argList = `concArg();
  String code = "";
}
: id:IDENTIFIER { opName = id.getText(); } COLON hooktype = hooktype
(
 LEFT_BRACE (arg:IDENTIFIER
   { argList = `concArg(argList*,Arg(arg.getText())); }
   ( COMMA supplarg:IDENTIFIER
     { argList = `concArg(argList*,Arg(supplarg.getText())); }
     )* )?
 RIGHT_BRACE
 )
{
  BlockParser blockparser = BlockParser.makeBlockParser(lexerstate);
  code = blockparser.block();
}
{
  prod = `Hook(KindOperator(),opName,hooktype,argList,code);
}
;

hooktype returns [Hookkind type]
{
  type = null;
  String typeName = null;
}
: tp:IDENTIFIER { typeName = tp.getText(); }
{
  if (typeName.equals("make")) {
    type = `KindMakeHook();
  } else if (typeName.equals("make_insert")) {
    type = `KindMakeinsertHook();
  } else {
    Logger.getLogger(getClass().getName()).log(Level.SEVERE,
          GomMessage.unknownHookKind.getMessage(),
          new Object[]{typeName});
    throw new GomRuntimeException("parsing problem");
  }
};

field returns [Field field]
{
  field = null;
  String t=null;
}
: t=type STAR
{ field = `StarredField(GomType(t)); }
| id:IDENTIFIER COLON t=type
{ field = `NamedField(id.getText(),GomType(t)); }
;

{
  import antlr.TokenStreamSelector;
}
class GomLexer extends Lexer;
options {
  k = 2; // \r and \r\n
}

tokens{
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

IDENTIFIER options{ testLiterals = true; }
: ('a'..'z' | 'A'..'Z')
  ('a'..'z' | 'A'..'Z' | '0'..'9' | '_' | '-')* ;
