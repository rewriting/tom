header{/*
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

package tom.engine.parser;
  
}

{
import java.util.LinkedList;

import tom.engine.TomBase;
import tom.engine.xml.Constants;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.tools.ASTFactory;
import antlr.TokenStreamSelector;
import aterm.*;
}
class BackQuoteParser extends Parser;

options{
    // antlr does not catch exceptions automaticaly
    defaultErrorHandler = false;
    // default lookahead
    k=1;
}

{
	private final static String DEFAULT_MODULE_NAME = "default";
	private final static String TNODE_MODULE_NAME = "tnode";
    %include{ adt/tomsignature/TomSignature.tom }
    
    // the lexer for backquote language
    BackQuoteLexer bqlexer = null;

    // the parser for tom language
    TomParser tomparser = null;

    // the current file's name
    String currentFile(){
      return tomparser.currentFile();
    }

    //constructor
    public BackQuoteParser(ParserSharedInputState state, TomParser tomparser){
      this(state);
      this.tomparser = tomparser;
      bqlexer = (BackQuoteLexer) selector().getStream("bqlexer");
    }

    // add token t to the buffer containing the target code
    private void addTargetCode(Token t){
      tomparser.addTargetCode(t);
    }

    // returns the selector
    private TokenStreamSelector selector(){
      return tomparser.selector();
    }
    
   private TomTerm buildBqAppl(Token id, LinkedList blockList, TomTerm term, boolean composite) {
     OptionList option = `concOption(OriginTracking(Name(id.getText()),id.getLine(),currentFile()),ModuleName(DEFAULT_ MODULE_NAME));
     TomList target = (term==null)?
       `emptyTomList():
       `concTomTerm(TargetLanguageToTomTerm(ITL(".")),term);

     if(composite) {
			 TomList list = ASTFactory.makeList(blockList);
			 return `Composite(concTomTerm(BackQuoteAppl(option,Name(id.getText()),list),target*));
     } else {
			 return `Composite(concTomTerm(Variable(option,Name(id.getText()),TomTypeAlone("unknown type"),concConstraint()),target*));
		 }

   }
 
   /*
    * add a term to a list of term
    * when newComposite is true, this means that a ',' has been read before the term
    */
    private void addTerm(LinkedList list, TomTerm term, boolean newComposite) {
      // if the list is empty put an empty composite in it to simplify the code
      if(list.isEmpty()) {
        list.add(`Composite(emptyTomList()));
      }
      TomTerm lastElement = (TomTerm) list.getLast();
      /*
       * when newComposite is true, we add the term, eventually wrapped by a Composite 
       * otherwise, the term is inserted (eventually unwrapped) into the last Composite of the list
       */
      if(newComposite) {
        %match(TomTerm lastElement, TomTerm term) {
          Composite(l1), t2@Composite[] -> { 
            list.add(`t2); 
            return; 
          }
          Composite(l1), t2 -> { 
            list.add(`Composite(concTomTerm(t2))); 
            return; 
          }
        }
      } else {
        %match(TomTerm lastElement, TomTerm term) {
          Composite(l1), t2@Composite(l2) -> { 
            list.set(list.size()-1,`Composite(concTomTerm(l1*,l2*))); 
            return;
          }
          Composite(l1), t2 -> { 
            list.set(list.size()-1,`Composite(concTomTerm(l1*,t2))); 
            return;
          }
        }
      }
    }

    // sorts attributes of xml term with lexicographical order
    private TomList sortAttributeList(TomList list){
      %match(TomList list) {
        concTomTerm() -> { return list; }
        concTomTerm(X1*,e1,X2*,e2,X3*) -> {
          %match(TomTerm e1, TomTerm e2) {
            BackQuoteAppl[Args=concTomTerm(BackQuoteAppl[AstName=Name(name1)],_*)],
            BackQuoteAppl[Args=concTomTerm(BackQuoteAppl[AstName=Name(name2)],_*)] -> {
              if(`name1.compareTo(`name2) > 0) {
                return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
              }
            }
          }
        }
      }
      return list;
    }
    
    // built a sorted TomList from a LinkedList
    private TomList buildAttributeList(LinkedList list){
      return sortAttributeList(ASTFactory.makeList(list));
    }
    
    // add double quotes around a string
    private String encodeName(String name) {
      return "\"" + name + "\"";
    }

}

/*
 * Backquoted Term
 */
beginBackquote returns [TomTerm result]
{ 
  result = null; 
  TomList context = `emptyTomList();
}
:
( result = mainBqTerm[context] ) { selector().pop(); }
;


mainBqTerm [TomList context] returns [TomTerm result]
{
    result = null;
    TomTerm term = null;
    TomList list = `emptyTomList();

    Token t = null;
    LinkedList blockList = new LinkedList();
}
    :
        (
         // xml(...) or (...)
            result = basicTerm[list]            
        | id:BQ_ID
          (
           // `X*
           {LA(1) == BQ_STAR}? BQ_STAR
           {   
             String name = id.getText();
             Option ot = `OriginTracking(Name(name), id.getLine(), currentFile());
             result = `VariableStar(concOption(ot),Name(name),TomTypeAlone("unknown type"),concConstraint());  
           }
                
           | ws /*ws*/ 
                (
                 // `x(...)
                 {LA(1) == BQ_LPAREN}? BQ_LPAREN ws ( termList[blockList,list] )? BQ_RPAREN 
                 {   
                   result = buildBqAppl(id,blockList,term,true);
                 }
                 // `x
                |   t = targetCode
                 {
                   //System.out.println("targetCode = " + t);
                   addTargetCode(t);
                   String name = id.getText();
                   OptionList ol = `concOption(OriginTracking(Name(name), id.getLine(), currentFile()), ModuleName(DEFAULT_MODULE_NAME));
                   //result = `BackQuoteAppl(ol,Name(name),concTomTerm());
                   result = `Variable(ol,Name(name),TomTypeAlone("unknown type"),concConstraint());
                 }
                )
            )
        )
    ;

bqTerm [TomList context] returns [TomTerm result]
{
    result = null;
    TomTerm term = null;
    TomList xmlTermList = `emptyTomList();

    Token t = null;
    LinkedList blockList = new LinkedList();
    boolean arguments = false;
}
    :
         // xml(...) or (...)
      result = basicTerm[context]
    |   id:BQ_ID
        (
         // X*
            {LA(1) == BQ_STAR}? BQ_STAR
            {   
              String name = id.getText();
              Option ot = `OriginTracking(Name(name), id.getLine(), currentFile());
              result = `VariableStar(concOption(ot),Name(name),TomTypeAlone("unknown type"),concConstraint());      
            }
            
            |  ws /*ws*/ 
            // x(...)
            (
             {LA(1) == BQ_LPAREN}? BQ_LPAREN {arguments = true;} ws (termList[blockList,context])? BQ_RPAREN 
            )?
            ( (BQ_DOT term = bqTerm[null] ) => BQ_DOT term = bqTerm[context] )?
            {   
                result = buildBqAppl(id,blockList,term,arguments);
            }
        )
    |
        // <...> ... </...>
        (xmlTerm[null]) => result = xmlTerm[context]
        
    |
        // x
        t = target
        {
          //System.out.println("target = " + t);
          result = `TargetLanguageToTomTerm(ITL(t.getText()));
        }
    ;


/*
 *    xml(c1,...,cn,t)
 * or (t1 ... tn) 
 */
basicTerm [TomList context] returns [TomTerm result]
{
    result = null;
    TomTerm term = null;
    TomList localContext = `emptyTomList();

    LinkedList blockList = new LinkedList();
}
    :
        (
            XML ws BQ_LPAREN ws 
            ( 
             (bqTerm[null] BQ_COMMA) => term = bqTerm[context] BQ_COMMA ws
             { blockList.add(term); } 
             )* 
            { localContext = ASTFactory.makeList(blockList); }
            result = bqTerm[localContext]
            BQ_RPAREN
            
        |   BQ_LPAREN ws ( termList[blockList,context] )? BQ_RPAREN
            {
              TomList compositeList = ASTFactory.makeList(blockList);
                result = `Composite(concTomTerm(
                        TargetLanguageToTomTerm(ITL("(")),
                        compositeList*,
                        TargetLanguageToTomTerm(ITL(")"))
                    )
                );
            }
        )
    ;

/*
 * termList parses a list of terms, eventually separated by a comma
 */
termList [LinkedList list,TomList context]
{
    TomTerm term = null;
}
    :
      term = bqTerm[context] /*ws*/ { addTerm(list,term,false); }
        ( ( c:BQ_COMMA  ws )? term = bqTerm[context] /*ws*/
        { addTerm(list,term, (c!=null)); c = null; }
        )*
    ;

xmlAttributeStringOrVariable returns [TomTerm result]
  { result = null; } : 
  (
     id:BQ_ID 
		 {
       String name = id.getText();
       OptionList ol = `concOption(OriginTracking(Name(name), id.getLine(), currentFile()), ModuleName(DEFAULT_MODULE_NAME));
       result = `Variable(ol,Name(name),TomTypeAlone("unknown type"),concConstraint());
		   //result = `TargetLanguageToTomTerm(ITL(id.getText())); 
		 }
   | string:BQ_STRING
	   {
		   result = `TargetLanguageToTomTerm(ITL(string.getText()));
		 }
  )
    ;

ws  :  ( options{ greedy = true; }: BQ_WS )*
    ;
 
target returns [Token result]
{
    result = null;
}
    :
        in:BQ_INTEGER {result = in;}
    |   str:BQ_STRING {result = str;}
    |   m:BQ_MINUS {result = m;}
    |   s:BQ_STAR {result = s;}
    |   w:BQ_WS {result = w;}
    |   d:BQ_DOT {result = d;} 
    |   dq:DOUBLE_QUOTE {result = dq;}
    |   xs:XML_START {result = xs;}
    |   xe:XML_EQUAL {result = xe;}
    |   xc:XML_CLOSE  {result = xc;}
    |   a:ANY {result = a;}
    ;

targetCode returns [Token result]
{
    result = null;
}
    :
        result = target
    |   c:BQ_COMMA {result = c;}   
    |   i:BQ_ID {result = i;} 
    |   r:BQ_RPAREN {result = r;}
    |   t:XML_START_ENDING    {result = t;}
    |   xcs:XML_CLOSE_SINGLETON {result = xcs;}
    |   xt:XML_TEXT {result = xt;}
    |   xc:XML_COMMENT {result = xc;}
    |   xp:XML_PROC {result = xp;}
    ;



/*
 * XML
 */

xmlAttribute [TomList context] returns [TomTerm result]
{
    result = null;
    TomTerm value = null;
}
    :
        (
            id:BQ_ID
            (
                ws XML_EQUAL ws value = xmlAttributeStringOrVariable
                {
                    TomList args = `concTomTerm(
                        BackQuoteAppl(
                            concOption(Constant(),ModuleName(TNODE_MODULE_NAME)),
                            Name(encodeName(id.getText())),
                            emptyTomList()
                        ),
                        BackQuoteAppl(
                            concOption(Constant(),ModuleName(TNODE_MODULE_NAME)),
                            Name("\"true\""),
                            emptyTomList()
                        ),
                        value
                    );
										if(context != null) {
										  args = `concTomTerm(context*,args*);
										}
										result = `BackQuoteAppl(
											concOption(ModuleName(TNODE_MODULE_NAME)),
											Name(Constants.ATTRIBUTE_NODE),
											args);
                }
            | BQ_STAR
              {
								result = `VariableStar(
										concOption(),
										Name(id.getText()),
										TomTypeAlone("unknown type"),
										concConstraint());
              }
            )
        )
    ;

xmlAttributeList [LinkedList attributeList, TomList context]
{
    TomTerm term = null;
}
    :
        (
            term = xmlAttribute[context] ws
            {
                attributeList.add(term);
            }
        )*
    ;

xmlChildren[LinkedList children, TomList context]
{
    TomTerm term = null;
}
    :
        (
            {LA(1) != XML_START_ENDING && LA(1) != XML_CLOSE}? 
            term = bqTerm[context]
            {children.add(term);}
        )*
    ;

xmlTerm[TomList context] returns [TomTerm result]
{
    result = null;
    TomList attributeTomList = `emptyTomList();
    TomList childrenTomList = `emptyTomList();
    TomTerm term = null;

    LinkedList attributes = new LinkedList();
    LinkedList children = new LinkedList();
}
    :
        (
            XML_START ws id:BQ_ID ws xmlAttributeList[attributes,context]
            {
                attributeTomList = buildAttributeList(attributes);
            }
                
                ( 
                    XML_CLOSE_SINGLETON ws
                |   XML_CLOSE
                    ws xmlChildren[children,context]
                {
                  childrenTomList = ASTFactory.makeList(children);
                }
                    XML_START_ENDING ws BQ_ID ws XML_CLOSE ws
                )
                {
                    TomList args = `concTomTerm(
                        BackQuoteAppl(
                            concOption(Constant(),ModuleName(TNODE_MODULE_NAME)),
                            Name(encodeName(id.getText())),
                            emptyTomList()
                        ),
                        BackQuoteAppl(
                            concOption(ModuleName(TNODE_MODULE_NAME)),
                            Name(Constants.CONC_TNODE),
                            attributeTomList
                        ),
                        BackQuoteAppl(
                            concOption(ModuleName(TNODE_MODULE_NAME)),
                            Name(Constants.CONC_TNODE),
                            childrenTomList
                        )
                    );
                    
                    if(context == null){
                        result = `BackQuoteAppl(
                            concOption(ModuleName(TNODE_MODULE_NAME)),
                            Name(Constants.ELEMENT_NODE),
                            args
                        );
                    } else {
											result = `BackQuoteAppl(
													concOption(ModuleName(TNODE_MODULE_NAME)),
													Name(Constants.ELEMENT_NODE),
													concTomTerm(
														context*,
														args*)
                        );
                    }

                }

        |   XML_TEXT BQ_LPAREN term = bqTerm[context] BQ_RPAREN
            {
                result = `BackQuoteAppl(
										concOption(ModuleName(TNODE_MODULE_NAME)),
                    Name(Constants.TEXT_NODE),
                    concTomTerm(
                        context*,
                        term
                    )
                );
            }
        )
        
    ;


class BackQuoteLexer extends Lexer;
options {
    charVocabulary = '\u0000'..'\uffff'; // each character can be read
    k=2;
}

tokens {
    XML = "xml";
}

{
  public void uponEOF()
    throws TokenStreamException, CharStreamException
    {
      throw new TokenStreamException("Premature EOF");
    }
}

BQ_LPAREN      :    '('   ;
BQ_RPAREN      :    ')'   ;
BQ_COMMA       :    ','   ;
BQ_STAR        :    '*'   ;

//XML Tokens
XML_EQUAL   :   '=' ;
XML_START_ENDING    : "</" ;
XML_CLOSE_SINGLETON : "/>" ;  
XML_START   :   '<';
XML_CLOSE   :   '>' ;
DOUBLE_QUOTE:   '\"';
XML_TEXT    :   "#TEXT";
XML_COMMENT :   "#COMMENT";
XML_PROC    :   "#PROCESSING-INSTRUCTION";

BQ_DOT    :    '.'   ;

// tokens to skip : white spaces
BQ_WS : ( ' '
    | '\t'
    | '\f'
    // handle newlines
    | ( "\r\n"  // Evil DOS
      | '\r'    // Macintosh
      | '\n'    // Unix (the right way)
      )
      { newline(); }
    )
    ;

XML_SKIP
    :
        "<?" ( ~('>') )* '>'
        {
            $setType(Token.SKIP);
        }
    ;

BQ_ID
options{ testLiterals = true; }   
    :
        (
            (BQ_MINUS_ID) => BQ_MINUS_ID
        |   BQ_SIMPLE_ID
        )
    ;

protected
BQ_SIMPLE_ID
options{ testLiterals = true; }   
    :   ('a'..'z' | 'A'..'Z') 
        ( 
            ('a'..'z' | 'A'..'Z') 
        |   BQ_UNDERSCORE 
//        |   BQ_DOT
        |   BQ_DIGIT
        )*
    ;

protected
BQ_MINUS_ID
    :
        BQ_SIMPLE_ID BQ_MINUS BQ_SIMPLE_ID 
        (BQ_MINUS_ID_PART)?
    ;

protected
BQ_MINUS_ID_PART :
  (
        BQ_MINUS BQ_SIMPLE_ID
  )+ 
;

BQ_INTEGER :   ( BQ_MINUS )? ( BQ_DIGIT )+     ;

BQ_STRING  :   '"' (BQ_ESC|~('"'|'\\'|'\n'|'\r'))* '"'
    ;

ANY 
options{ testLiterals = true; } 
//    :   '\u0000'..'\uffff'  
    :  .
    ;
   
BQ_MINUS   :   '-'  ;

protected
BQ_DIGIT   :   ('0'..'9')  ;

protected 
BQ_UNDERSCORE  :   '_' ;

protected
BQ_ESC
  : '\\'
    ( 'n'
    | 'r'
    | 't'
    | 'b'
    | 'f'
    | '"'
    | '\''
    | '\\'
    | ('u')+ BQ_HEX_DIGIT BQ_HEX_DIGIT BQ_HEX_DIGIT BQ_HEX_DIGIT
    | '0'..'3'
      (
        options {
          warnWhenFollowAmbig = false;
        }
      : '0'..'7'
        (
          options {
            warnWhenFollowAmbig = false;
          }
        : '0'..'7'
        )?
      )?
    | '4'..'7'
      (
        options {
          warnWhenFollowAmbig = false;
        }
      : '0'..'7'
      )?
    )
  ;

protected
BQ_HEX_DIGIT
  : ('0'..'9'|'A'..'F'|'a'..'f')
  ;

