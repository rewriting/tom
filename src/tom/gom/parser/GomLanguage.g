/*
 * Gom
 *
 * Copyright (c) 2007-2016, Universite de Lorraine, Inria
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
grammar GomLanguage;

options {
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=GomTokens;
}

@header {
package tom.gom.parser;
import tom.gom.GomStreamManager;
}

@lexer::header {
package tom.gom.parser;
}

@members {
  private GomStreamManager streamManager;
  public GomLanguageParser(TokenStream input, GomStreamManager streamManager) {
    super(input);
    this.streamManager = streamManager;
  }
}

module :
 MODULE modulename (imps=imports)? section EOF
  -> {imps!=null}? ^(GomModule modulename ^(ConcSection imports section))
  -> ^(GomModule modulename ^(ConcSection section))
  ;

modulename
@init {
  StringBuilder packagePrefix = new StringBuilder("");
} :
  (mod=ID DOT { packagePrefix.append($mod.text+"."); })*
  moduleName=ID
  {
    if (packagePrefix.length() > 0) {
      packagePrefix.deleteCharAt(packagePrefix.length()-1);
      if (null != streamManager) {
        streamManager.associatePackagePath($moduleName.text,packagePrefix.toString());
      }
    }
  }
  -> ^(GomModuleName $moduleName)
  ;

imports :
  IMPORTS (importedModuleName)* -> ^(Imports ^(ConcImportedModule (importedModuleName)*))
  ;
importedModuleName :
  ID -> ^(GomModuleName ID)
  ;

section :
  (PUBLIC)? adtgrammar -> ^(Public adtgrammar)
  ;

adtgrammar :
  (gr+=syntax)* -> $gr
  ;

syntax :
  (ABSTRACT SYNTAX) (gr1+=hookConstruct | gr2+=typedecl | gr3+=atomdecl)*
    -> ^(ConcProduction ($gr1)* ($gr2)* ($gr3)*)
  ;

atomdecl :
  ATOM atom=ID -> ^(AtomDecl ID[atom])
  ;

typedecl :
    typename=ID EQUALS alts=alternatives[typename]
      -> ^(SortType ^(GomType ^(ExpressionType) $typename) ^(ConcAtom) $alts)
  |  ptypename=ID BINDS b=atoms EQUALS palts=pattern_alternatives[ptypename]
      -> ^(SortType ^(GomType ^(PatternType) $ptypename) $b $palts)
  ;

atoms :
  (atom+=ID)+ -> ^(ConcAtom ($atom)+)
  ;

alternatives[Token typename] :
  ((jd1=JAVADOC ALT) | (ALT jd1=JAVADOC) | jd1=JAVADOC | (ALT)?)
  opdecl[typename,jd1]
  (
   ((jd2=JAVADOC ALT) | (ALT jd2=JAVADOC) | ALT {jd2=null;})
   opdecl[typename,jd2]
  )* (SEMI)?
  -> ^(ConcAlternative (opdecl)+)
  ;

/* Used by Freshgom, as all rules beginning by "pattern" */
pattern_alternatives[Token typename] :
  (ALT)? pattern_opdecl[typename] (ALT pattern_opdecl[typename])* (SEMI)?
  -> ^(ConcAlternative (pattern_opdecl)+)
  ;

opdecl[Token type, Token JAVADOC] :
 ID fieldlist
  -> {JAVADOC!=null}? ^(Alternative ID fieldlist ^(GomType ^(ExpressionType) ID[type])
      ^(OptionList ^(Origin ID[""+input.LT(1).getLine()]) ^(Details ID[JAVADOC])))
  -> ^(Alternative ID fieldlist ^(GomType ^(ExpressionType) ID[type])
      ^(Origin ID[""+input.LT(1).getLine()]))
  ;

/* Used by Freshgom, as all rules beginning by "pattern" */
pattern_opdecl[Token type] :
 ID pattern_fieldlist
  -> ^(Alternative ID pattern_fieldlist ^(GomType ^(PatternType) ID[type])
      ^(Origin ID[""+input.LT(1).getLine()]))
  ;

fieldlist :
  LPAREN (field (COMMA field)* )? RPAREN -> ^(ConcField (field)*) ;

/* Used by Freshgom, as all rules beginning by "pattern" */
pattern_fieldlist :
  LPAREN (pattern_field (COMMA pattern_field)* )? RPAREN -> ^(ConcField (pattern_field)*) ;

type:
  ID -> ^(GomType ^(ExpressionType) ID)
  ;

/* Used by Freshgom, as all rules beginning by "pattern" */
pattern_type:
  ID -> ^(GomType ^(PatternType) ID)
  ;

field:
    type STAR -> ^(StarredField type ^(None))
  | LDIPLE pattern_type RDIPLE STAR -> ^(StarredField pattern_type ^(Refresh))
  | ID COLON type -> ^(NamedField ID type ^(None))
  | ID COLON LDIPLE pattern_type RDIPLE -> ^(NamedField ID pattern_type ^(Refresh))
  ;

/* Used by Freshgom, as all rules beginning by "pattern" */
pattern_field:
    pattern_type STAR -> ^(StarredField pattern_type ^(None))
  | INNER ID COLON type -> ^(NamedField ID type ^(Inner))
  | OUTER ID COLON type -> ^(NamedField ID type ^(Outer))
  | NEUTRAL ID COLON type -> ^(NamedField ID type ^(Neutral))
  | ID COLON pattern_type -> ^(NamedField ID pattern_type ^(None))
  ;

arglist:
  (LPAREN (arg (COMMA arg)* )? RPAREN)?
  -> ^(ConcArg (arg)*)
  ;

arg : ID -> ^(Arg ID);

hookConstruct :
  (hscope=hookScope)? pointCut=ID COLON hookType=ID arglist LBRACE
  -> {hscope!=null}? ^(Hook $hscope $pointCut ^(HookKind $hookType) arglist LBRACE
                       ^(Origin ID[""+input.LT(1).getLine()]))
  -> ^(Hook ^(KindOperator) $pointCut ^(HookKind $hookType) arglist LBRACE
      ^(Origin ID[""+input.LT(1).getLine()]))
  /* The LBRACE should contain the code */
  ;

hookScope :
  SORT -> ^(KindSort)
  | MODULE -> ^(KindModule)
  | OPERATOR -> ^(KindOperator)
  ;


MODULE   : 'module';
IMPORTS  : 'imports';
PUBLIC   : 'public';
PRIVATE  : 'private';
ABSTRACT : 'abstract';
SYNTAX   : 'syntax';
SORT     : 'sort';
OPERATOR : 'operator';
ATOM     : 'atom';
INNER    : 'inner';
OUTER    : 'outer';
NEUTRAL  : 'neutral';
BINDS    : 'binds';

ARROW    : '->';
COLON    : ':';
COMMA    : ',';
DOT      : '.';
LPAREN   : '(';
RPAREN   : ')';
STAR     : '*';
EQUALS   : '=';
ALT      : '|';
SEMI     : ';;';
LDIPLE   : '<';
RDIPLE   : '>';

LBRACE: '{'
  {
    SimpleBlockLexer lex = new SimpleBlockLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lex);
    SimpleBlockParser parser = new SimpleBlockParser(tokens);
    parser.block();
  }
  ;

RBRACE: '}';

WS : ( ' '
       | '\t'
       | ( '\r\n' // DOS
           | '\n'   // Unix
           | '\r'   // Macintosh
           )
       )
  {$channel=HIDDEN;}
  ;

SLCOMMENT :
  '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
  {$channel=HIDDEN;}
  ;

MLCOMMENT :
  '/*' ~'*'.* '*/'
  {$channel=HIDDEN;}
  ;

JAVADOC :
  '/**' .* '*/'
  ;

ID : ('a'..'z' | 'A'..'Z')
     ('a'..'z' | 'A'..'Z' | '0'..'9' | '_' | '-')* ;
