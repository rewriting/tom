header{
    package jtom.parser;
    
    import jtom.*;
    import jtom.adt.tomsignature.*;
    import jtom.adt.tomsignature.types.*;
    import jtom.xml.*;

    import tom.platform.*;
    
    import aterm.*;
    import aterm.pure.*;

    import antlr.*;

    import java.util.*;
}

class BackQuoteParser extends Parser;

options{
    // antlr does not catch exceptions automaticaly
    defaultErrorHandler = false;
    // default lookahead
    k=1;
}

{
    %include{TomSignature.tom}
    
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

    // --- methods for tom environment ---

    private final TomSignatureFactory getTomSignatureFactory() {
        return TomBase.getTomSignatureFactory();
    }
        
    private TomEnvironment environment() {
        return TomEnvironment.getInstance();
    }

    private TomSignatureFactory tsf() {
        return TomBase.getTomSignatureFactory();
    }
    // --- ---

    // add token t to the buffer containing the target code
    private void addTargetCode(Token t){
        tomparser.addTargetCode(t);
    }

    // returns the selector
    private TokenStreamSelector selector(){
        return tomparser.selector();
    }
    
    // build a TomList from a LinkedList
    private TomList buildList(LinkedList list){
        TomList result = `emptyTomList();
        for(int i = 0; i < list.size(); i++){
            result = (TomList) result.append((TomTerm) list.get(i));
        }
        return result;
    }

    private TomList makeCompositeList(LinkedList list){
        TomTerm term = null;
        TomList compositeList = `emptyTomList();
        Iterator it = list.iterator();
        
        while(it.hasNext()){
            term = (TomTerm) it.next();
            compositeList = (TomList) compositeList.append(term);
        }
        
        return compositeList;
    }

    private TomTerm buildBqAppl(Token id,LinkedList blockList,TomTerm term,boolean composite){
      TomTerm result = null;
      TomList list = makeCompositeList(blockList);
      TomList target = `emptyTomList();
      OptionList option = `concOption(OriginTracking(Name(id.getText()),id.getLine(),Name(currentFile())));
      
      if(term != null) {
        target = `concTomTerm(TargetLanguageToTomTerm(ITL(".")),term);
      }
      if (composite){
        option = `concOption(Constructor(concTomName(Name(id.getText()))),option*);
      }
      return `Composite(concTomTerm(BackQuoteAppl(option,Name(id.getText()),list),target*));
    }
 
    // add a term in the list of term
    // newComposite==true when we read a ',' before the term
    private void addTerm(LinkedList list, TomTerm term, boolean newComposite) {
      // if the list is empty put an empty composite in it
      if(list.size() == 0) {
        list.add(`Composite(emptyTomList()));
      }
      TomTerm lastElement = (TomTerm) list.getLast();
        
      %match(TomTerm lastElement){
        Composite(l) -> {
          if(!newComposite) {
            list.set(list.size()-1,`Composite(concat(l,term)));
          } else {
            addComposite(list,term);
          }
          return;
        }
        
        _ -> {
          if(!newComposite) {
            list.add(term);
          } else {
            addComposite(list,term);
          }
        }
      }
    }

    // concat a TomTerm to a TomList
    private TomList concat(TomList list, TomTerm term){
      %match(TomTerm term){
        Composite(l) -> {
          return `concTomTerm(list*,l*);
        }
        _ -> {
          return `concTomTerm(list*,term);
        }
      }
    }

    // add a composite to a list
    private void addComposite(LinkedList list, TomTerm term){
      %match(TomTerm term){
        Composite[] -> {
          list.add(term);
          return;
        }
        _ -> {
          list.add(`Composite(concTomTerm(term)));
          return;
        }
      }
    }

    // sorts attributes of xml term with lexicographical order
    private TomList sortAttributeList(TomList list){
      %match(TomList list) {
        concTomTerm() -> { return list; }
        concTomTerm(X1*,e1,X2*,e2,X3*) -> {
          %match(TomTerm e1, TomTerm e2) {
            BackQuoteAppl[args=manyTomList(BackQuoteAppl[astName=Name(name1)],_)],
              BackQuoteAppl[args=manyTomList(BackQuoteAppl[astName=Name(name2)],_)] -> {
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
      TomList result = buildList(list);
      return sortAttributeList(result);
    }
    
    // add double quotes around a string
    private String encodeName(String name) {
      return "\"" + name + "\"";
    }

}

ws  :  ( options{ greedy = true; }: BQ_WS )*
    ;

termList [LinkedList list,TomList context]
{
    TomTerm term = null;
}
    :
        (
            term = bqTerm[context]
            { addTerm(list,term,false); }
            ws 
            ( 
                ( c:BQ_COMMA  ws )? 
                term = bqTerm[context] ws
                {
                  //System.out.println("termList: " + term);
                  if(c != null) {
                    addTerm(list,term,true);
                  } else {
                    addTerm(list,term,false);
                  }
                  c = null;
                }
            )*
        )
    ;

termStringIdentifier returns [TomTerm result]
{
    result = null;
}
    :
        (
            id:BQ_ID
            {
                result = `TargetLanguageToTomTerm(ITL(id.getText()));
            }
        |   string:BQ_STRING
            {
                result = `TargetLanguageToTomTerm(ITL(string.getText()));
            }
        )
    ;

attribute [TomList context] returns [TomTerm result]
{
    result = null;
    TomTerm value = null;
}
    :
        (
            id:BQ_ID
            (
                ws XML_EQUAL ws 
                value = termStringIdentifier
                {
                    TomList args = `concTomTerm(
                        BackQuoteAppl(
                            emptyOptionList(),
                            Name(encodeName(id.getText())),
                            emptyTomList()
                        ),
                        BackQuoteAppl(
                            emptyOptionList(),
                            Name("\"true\""),
                            emptyTomList()
                        ),
                        value
                    );
                    
                    if(context == null){
                        result = `BackQuoteAppl(
                            emptyOptionList(),
                            Name(Constants.ATTRIBUTE_NODE),
                            args);
                    }   
                    else{
                        result = `BackQuoteAppl(
                            emptyOptionList(),
                            Name(Constants.ATTRIBUTE_NODE),
                            concTomTerm(
                                context*,
                                args*)
                        );
                    }
                }
            |   BQ_STAR
                {
                    result = `VariableStar(
                        emptyOptionList(),
                        Name(id.getText()),
                        TomTypeAlone("unknown type"),
                        emptyConstraintList()
                    );
                }
            )
        )
    ;

attributeList [LinkedList attributeList, TomList context]
{
    TomTerm term = null;
}
    :
        (
            term = attribute[context] ws
            {
                attributeList.add(term);
            }
        )*
    ;

children[LinkedList children, TomList context]
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
            XML_START ws id:BQ_ID ws attributeList[attributes,context]
            {
                attributeTomList = buildAttributeList(attributes);
            }
                
                ( 
                    XML_CLOSE_SINGLETON ws
                |   XML_CLOSE
                    ws children[children,context]
                {
                        childrenTomList = buildList(children);
                }
                    XML_START_ENDING ws BQ_ID ws XML_CLOSE ws
                )
                {
                    TomList args = `concTomTerm(
                        BackQuoteAppl(
                            emptyOptionList(),
                            Name(encodeName(id.getText())),
                            emptyTomList()
                        ),
                        BackQuoteAppl(
                            emptyOptionList(),
                            Name(Constants.CONC_TNODE),
                            attributeTomList
                        ),
                        BackQuoteAppl(
                            emptyOptionList(),
                            Name(Constants.CONC_TNODE),
                            childrenTomList
                        )
                    );
                    
                    if(context == null){
                        result = `BackQuoteAppl(
                            emptyOptionList(),
                            Name(Constants.ELEMENT_NODE),
                            args
                        );
                    }
                    else{
                    result = `BackQuoteAppl(
                            emptyOptionList(),
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
                    emptyOptionList(),
                    Name(Constants.TEXT_NODE),
                    concTomTerm(
                        context*,
                        term
                    )
                );
            }
        )
        
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
    |   a:ANY {
      /*
      System.out.println("any = '" + a + "'");
      System.out.println("any.text = '" + a.getText() + "'");
      System.out.println("any.type = " + a.getType());
      */
      result = a;
    }
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
                ( 
                    (bqTerm[null] BQ_COMMA) => term = bqTerm[context] BQ_COMMA ws
                    {
                        blockList.add(term);
                    }
                )* 
                {
                    localContext = buildList(blockList);
                }
                result = bqTerm[localContext]
            )
            BQ_RPAREN
            
        |   BQ_LPAREN ws 
            ( 
                termList[blockList,context] 
            )? BQ_RPAREN
            {
                TomList compositeList = buildList(blockList);//makeCompositeList(blockList);
                result = `Composite(
                    concTomTerm(
                        TargetLanguageToTomTerm(ITL("(")),
                        compositeList*,
                        TargetLanguageToTomTerm(ITL(")"))
                    )
                );
            }
            
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
              Option ot = `OriginTracking(Name(name), id.getLine(), Name(currentFile()));
              result = `VariableStar(concOption(ot),Name(name),TomTypeAlone("unknown type"),concConstraint());      
            }
            
        |   ws 
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
          result = `TargetLanguageToTomTerm(ITL(t.getText()));
        }
    ;

firstTerm returns [TomTerm result]
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
        |   id:BQ_ID 
            (
             // `X*
                {LA(1) == BQ_STAR}? BQ_STAR
                {   
                  String name = id.getText();
                  Option ot = `OriginTracking(Name(name), id.getLine(), Name(currentFile()));
                  result = `VariableStar(concOption(ot),Name(name),TomTypeAlone("unknown type"),concConstraint());  
                }
                
            |   ws 
                (
                 // `x(...)
                 {LA(1) == BQ_LPAREN}? BQ_LPAREN ws ( termList[blockList,list] )? BQ_RPAREN 
                 {   
                   result = buildBqAppl(id,blockList,term,true);
                 }
                 // `x
                |   t = targetCode
                 {
                   addTargetCode(t);
                   String name = id.getText();
                   Option ot = `OriginTracking(Name(name), id.getLine(), Name(currentFile()));
                   result = `BackQuoteAppl(concOption(ot),Name(name),concTomTerm());
                 }
                )
            )
            
        )
    ;

beginBackquote returns [TomTerm result]
{
    result = null;
    Token t = null;
}
    :
        (
            result = firstTerm
        )
        {
            selector().pop();
        }
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
BQ_WS	:	(	' '
		|	'\t'
		|	'\f'
		// handle newlines
		|	(	"\r\n"  // Evil DOS
			|	'\r'    // Macintosh
			|	'\n'    // Unix (the right way)
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
        BQ_SIMPLE_ID BQ_MINUS ('a'..'z' | 'A'..'Z') 
        ( 
            BQ_MINUS ('a'..'z' | 'A'..'Z') 
        |   BQ_SIMPLE_ID
        )*
    ;



BQ_INTEGER :   ( BQ_MINUS )? ( BQ_DIGIT )+     ;

BQ_STRING  :   '"' (BQ_ESC|~('"'|'\\'|'\n'|'\r'))* '"'
    ;

ANY 
options{ testLiterals = true; } 
    :   '\u0000'..'\uffff'  
    ;
   
BQ_MINUS   :   '-'  ;

protected
BQ_DIGIT   :   ('0'..'9')  ;

protected 
BQ_UNDERSCORE  :   '_' ;

protected
BQ_ESC
	:	'\\'
		(	'n'
		|	'r'
		|	't'
		|	'b'
		|	'f'
		|	'"'
		|	'\''
		|	'\\'
		|	('u')+ BQ_HEX_DIGIT BQ_HEX_DIGIT BQ_HEX_DIGIT BQ_HEX_DIGIT
		|	'0'..'3'
			(
				options {
					warnWhenFollowAmbig = false;
				}
			:	'0'..'7'
				(
					options {
						warnWhenFollowAmbig = false;
					}
				:	'0'..'7'
				)?
			)?
		|	'4'..'7'
			(
				options {
					warnWhenFollowAmbig = false;
				}
			:	'0'..'7'
			)?
		)
	;

protected
BQ_HEX_DIGIT
	:	('0'..'9'|'A'..'F'|'a'..'f')
	;

