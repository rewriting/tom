header{/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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

package tom.engine.parser.antlr2;

}

{
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
}
class TomJavaParser extends Parser;

options {
    defaultErrorHandler = false;
}

{
  public static TomJavaParser createParser(String fileName)
	  throws FileNotFoundException,IOException {
    File file = new File(fileName);
    TomJavaLexer lexer = new TomJavaLexer(
		    new BufferedReader(
			    new FileReader(file)));
    return new TomJavaParser(lexer);
  }
}

javaPackageDeclaration returns [String result]
{
  result = "";
}
:
  (p:JAVA_PACKAGE { result = p.getText().trim(); })?
  ;

class TomJavaLexer extends Lexer;
options {
  k=2;
  // charVocabulary = '\3'..'\177';
  charVocabulary='\u0000'..'\uffff';
}


JAVA_PACKAGE: "package"! (~';')* ';'! ;

STRING
: '"' (~('"'|'\\'|'\n'|'\r'))* '"'
{ $setType(Token.SKIP); }
;

// white spaces
WS :
  ( ' '
  | '\t'
  | '\f'
  // handle newlines
  | ( "\r\n"  // Evil DOS
    | '\r'    // Macintosh
    | '\n'    // Unix (the right way)
    )
  { newline(); }
  )
{ $setType(Token.SKIP); }
;

// comments
COMMENT
  :
  ( SL_COMMENT | ML_COMMENT )
{ $setType(Token.SKIP);}
;

protected
  SL_COMMENT
  :
  "//"
    ( ~('\n'|'\r') )*
    (
     options {
       generateAmbigWarnings=false;
     }
     : '\r' '\n'
     | '\r'
     | '\n'
     )
;

protected
  ML_COMMENT
  :
  "/*"
    ( { LA(2)!='/' }? '*'
      |
      )
    (
     options {
       greedy=false;  // make it exit upon "*/"
       generateAmbigWarnings=false; // shut off newline errors
     }
     : '\r' '\n' {newline();if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");}
     | '\r'  {newline();if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");}
     | '\n'  {newline();if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");}
     | ~('\n'|'\r') {if(LA(1)==EOF_CHAR) throw new TokenStreamException("premature EOF");}
     )*
    "*/"
    ;
