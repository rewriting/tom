header{
    package jtom.parser;
    
    import jtom.adt.tomsignature.*;
    import jtom.adt.tomsignature.types.*;

    import antlr.*;

    import java.util.*;
}

class NewBQParser extends Parser;

{
    NewBQLexer bqlexer = null;

    NewTomParser tomparser = null;

    String currentFile(){
        return tomparser.currentFile();
    }

    public NewBQParser(ParserSharedInputState state, NewTomParser tomparser){
        this(state);
        this.tomparser = tomparser;
        bqlexer = (NewBQLexer) selector().getStream("bqlexer");
        // this.filename = tomparser.filename;
    }

    private NewTomBackQuoteParser tomBQ(){
        return tomparser.tomBQ();
    }

    private void pushLine(int line){
        tomparser.pushLine(line);
    }

    private void pushColumn(int column){
        tomparser.pushColumn(column);
    }

    private void addTargetCode(Token t){
        tomparser.addTargetCode(t);
    }

    private TokenStreamSelector selector(){
        return tomparser.selector();
    }

}

//Handle any code between '(' and ')'
//called recursively to take care of the imbricated parenthesis
bqCode [LinkedList list]
    :
        (
            l:BQ_LPAREN {list.add(l);}
            bqCode[list] 
            r:BQ_RPAREN {list.add(r);}
        |   any[list]
        |   t:ANY {list.add(t);}
        )*
    ;

any [LinkedList list]
    :
        c:BQ_COMMA {list.add(c);}
    |   s:BQ_STAR {list.add(s);} 
    |   i:BQ_ID {list.add(i);}
    |   in:BQ_INTEGER {list.add(in);}
    |   str:BQ_STRING {list.add(str);}
    ;

//Handle (...)
//We already read the '(' token in the tom parser
bqTarget [LinkedList list] returns [TomTerm result]
{ 
    result = null;
}
    :
        bqCode[list]
        t:BQ_RPAREN 
        {
            list.add(t);

            result = tomBQ().buildBackQuoteTerm(list,currentFile());

            pushLine(t.getLine());
            pushColumn(t.getColumn());
            //returns to tom parser
            selector().pop();
        }
    ;

targetCode returns [Token result]
{
    result = null;
}
    :
        c:BQ_COMMA {result = c;} 
    |   i:BQ_ID {result = i;}
    |   in:BQ_INTEGER {result = in;}
    |   str:BQ_STRING {result = str;}
    |   r:BQ_RPAREN {result = r;}
    |   m:BQ_MINUS {result = m;}
    |   a:ANY {result = a;}
    ;

//Handle ID* | ID(...) | ID
//we Already read the ID token in the Tom Parser
bqTargetAppl [LinkedList list] returns [TomTerm result]
{
    result = null;
    Token t = null;
}
    :
        (
            s:BQ_STAR 
            {
                list.add(s);

                result = tomBQ().buildVariableStar(list,currentFile());
                
                pushLine(s.getLine());
                pushColumn(s.getColumn());
            }
        |
            l:BQ_LPAREN {list.add(l);} 
            bqCode[list] 
            r:BQ_RPAREN 
            {
                list.add(r);
                
                result = tomBQ().buildBackQuoteTerm(list,currentFile());

                pushLine(r.getLine());
                pushColumn(r.getColumn());
            }
        |   
            (   t=targetCode
                {
                    addTargetCode(t);

                    result = tomBQ().buildBackQuoteAppl(list,currentFile());

                    pushLine(t.getLine());
                    pushColumn(t.getColumn());
                }
            )?
        )
        {
            //returns to the Tom parser
            selector().pop();
        }
    ;


class NewBQLexer extends Lexer;
options {
    charVocabulary = '\u0000'..'\uffff'; // each character can be read
    k=2;
}

{
    StringBuffer buffer = new StringBuffer("");

    public void clearBuffer(){
        buffer.delete(0,buffer.length());
    }
}

BQ_LPAREN      :   '('   ;
BQ_RPAREN      :   ')'   ;
BQ_COMMA       :   ','   ;
BQ_STAR        :   '*'   ;

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
        { $setType(Token.SKIP); }
	;


BQ_ID
    options{ testLiterals = true; }   
    :   ('a'..'z' | 'A'..'Z') 
        ( 
            ('a'..'z' | 'A'..'Z') 
        |   BQ_DIGIT 
        |   BQ_UNDERSCORE 
        |   ( BQ_MINUS ('a'..'z' | 'A'..'Z') ) 
        )*  
    ;

BQ_INTEGER :   ( BQ_MINUS )? ( BQ_DIGIT )+     ;

BQ_STRING  :   '"' (BQ_ESC|~('"'|'\\'|'\n'|'\r'))* '"'
    ;

ANY 
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
