header{
    package jtom.parser;
    
    import jtom.*;
    import jtom.adt.tomsignature.*;
    import jtom.adt.tomsignature.types.*;
    
    import aterm.*;
    import aterm.pure.*;

    import antlr.*;

    import java.util.*;
}

class NewBQParser extends Parser;

options{
    defaultErrorHandler = false;
}

{
    %include{TomSignature.tom}
    
    NewBQLexer bqlexer = null;

    NewTomParser tomparser = null;

    String currentFile(){
        return tomparser.currentFile();
    }

    private boolean xmlTerm = false;

    public void setXmlTerm(boolean b){
        xmlTerm = b;
    }

    public NewBQParser(ParserSharedInputState state, NewTomParser tomparser){
        this(state);
        this.tomparser = tomparser;
        bqlexer = (NewBQLexer) selector().getStream("bqlexer");
        // this.filename = tomparser.filename;
    }
/*
    private NewTomBackQuoteParser tomBQ(){
        return tomparser.tomBQ();
    } 
*/
    private final TomSignatureFactory getTomSignatureFactory(){
        return tsf();
    }
    
    public TomServer getServer(){
        return TomServer.getInstance();
    }
    
    private TomEnvironment environment() {
  //      return getServer().getEnvironment();
        return TomEnvironment.getInstance();
    }

    private TomSignatureFactory tsf(){
        return environment().getTomSignatureFactory();
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

    public void p(String s){
	System.out.println(s);
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

    // newComposite = true when we have read a comma before
    // the term 'term' in a list of term
    private void addTerm(LinkedList list, TomTerm term, boolean newComposite){
        if(xmlTerm){
            p("--");
            TomTerm last = (TomTerm) list.getLast();
            p("--");
            %match(TomTerm last){
                Composite(l) -> {
                    list.removeLast();
                    list.add(`Composite(concTomTerm(l*,term)));
                    return;
                }
                _ -> {
                    // should not be here
                    p("!! should not be here !!");
                    return;
                }
            }
        }
        else{       
            if(list.size() > 0) {
                TomTerm last = (TomTerm) list.getLast();
                
                %match(TomTerm term){
                    TargetLanguageToTomTerm(ITL(s)) -> {
                        if(! newComposite){
                            %match(TomTerm last){
                                Composite(l) -> {
                                    list.removeLast();
                                    list.add(`Composite(concTomTerm(l*,term)));
                                    return;
                                }
                                _ -> {
                                    list.add(`Composite(concTomTerm(term)));
                                    return;
                                }
                            }
                        }
                        else{
                            list.add(`Composite(concTomTerm(term)));
                            return;
                        }
                    }
                    _ -> {
                        list.add(`Composite(concTomTerm(term)));
                        return;
                    }
                }
            }
            else{
                %match(TomTerm term){
                    TargetLanguageToTomTerm[] -> {
                        list.add(`Composite(concTomTerm(term)));
                        return;
                    }
                    Composite[] -> {
                        list.add(term);
                        return;
                    }
                    _ -> {
                        list.add(`Composite(concTomTerm(term)));
                    }
                }   
            }
        }
    }

    private void addXmlSubTerm(LinkedList list, TomTerm term){
        if(list.size() > 0){
            %match(TomTerm term){
                Composite(concTomTerm(x)) -> {
                    list.add(x);
                    return;
                }
                _ -> {
                    list.add(term);
                }
            }
        }
    }
    
    private String removeStar(String s){
        return s.substring(0,s.lastIndexOf('*'));
    }
    


}


bqList [LinkedList list]
{
    TomTerm term = null;
}
    :
        term = bqTerm
        {
            
            addTerm(list,term,false); 
        }
        // eat as much WS as possible : be greedy
       // ( options{greedy = true;}: BQ_WS )*
        ( 
            (c:BQ_COMMA ( BQ_WS )* 
            )? term = bqTerm 
            //( BQ_WS )*
            {
                if(c != null){
                    // if a comma is read, build another composite
                    addTerm(list,term,true);
                }
                else{
                    addTerm(list,term,false);
                }
                
                // antlr does not set token to null value after
                // doing one time the loop. We have to do it ourselves
                c = null;
            } 
        )* 
    ;

bqTerm returns [TomTerm result]
{
    result = null;
    LinkedList blockList = new LinkedList();
    Token t = null;
    String s = "";
}
    :
        (     
            {LA(2) == BQ_STAR}? 
            i1:BQ_ID BQ_STAR 
            {
                String name = i1.getText();
                result = `VariableStar(
                    concOption(
                        OriginTracking(
                            Name(name), 
                            i1.getLine(), 
                            Name(currentFile())
                        )
                    ),
                    Name(name),
                    TomTypeAlone("unknown type"),
                    concConstraint()
                );

/*                TomTerm varStar = `Composite(concTomTerm(VariableStar(
                    concOption(
                        OriginTracking(
                            Name(name), 
                            i1.getLine(), 
                            Name(currentFile())
                        )
                    ),
                    Name(name),
                    TomTypeAlone("unknown type"),
                    concConstraint()
                )));*/
//                result = varStar;
            }
            
        |
            (BQ_ID (BQ_WS)* BQ_LPAREN) => i2:BQ_ID ( w:BQ_WS )*
            BQ_LPAREN ( BQ_WS )* (bqList[blockList])? ( BQ_WS )* BQ_RPAREN /*( BQ_WS )**/
            {
                if(blockList.size() > 0) {
                    TomList compositeList = makeCompositeList(blockList);
                     
/*                    TomTerm composite = `BackQuoteAppl(
                        concOption(
                            OriginTracking(
                                Name(i2.getText()), 
                                i2.getLine(), 
                                Name(currentFile())
                            )
                        ),
                        Name(i2.getText()),
                        compositeList
                    );
                    result = composite;*/
                    
                    result = `Composite(
                        concTomTerm(
                            BackQuoteAppl(
                                concOption(
                                    OriginTracking(
                                        Name(i2.getText()), 
                                        i2.getLine(), 
                                        Name(currentFile())
                                    )
                                ),
                                Name(i2.getText()),
                                compositeList
                            )
                        )
                    );
                }
                else {
                    result = `Composite(concTomTerm(BackQuoteAppl(
                                concOption(
                                    Constructor(concTomName(Name(i2.getText()))),
                                    OriginTracking(
                                        Name(i2.getText()), 
                                        i2.getLine(), 
                                        Name(currentFile())
                                    )
                                ),
                                Name(i2.getText()),
                                emptyTomList()
                            ))
                    );
                }
            }
        
     
        |   i3:BQ_ID //( BQ_WS )*
            {
               /* TomTerm appl = `Composite(concTomTerm(BackQuoteAppl(
                    concOption(
                        OriginTracking(
                            Name(i3.getText()), 
                            i3.getLine(), 
                            Name(currentFile())
                        )
                    ),
                    Name(i3.getText()),
                    concTomTerm()
                )));
                result = appl;*/

                result = `BackQuoteAppl(
                    concOption(
                        OriginTracking(
                            Name(i3.getText()), 
                            i3.getLine(), 
                            Name(currentFile())
                        )
                    ),
                    Name(i3.getText()),
                    concTomTerm()
                );
            }
        
        |   BQ_LPAREN ( BQ_WS )*
            ( bqList[blockList] )? ( BQ_WS )* BQ_RPAREN /*( BQ_WS )**/
            {
                TomList compositeList = makeCompositeList(blockList);
                compositeList = `concTomTerm(
                    TargetLanguageToTomTerm(ITL("(")),
                    compositeList*,
                    TargetLanguageToTomTerm(ITL(")"))
                );
                
                result = `Composite(compositeList);
            }

        |   s = targetPlus // ( BQ_WS )*
            {
                result = `TargetLanguageToTomTerm(ITL(s));
            }
        )
    ;


targetPlus returns [String result]
{
    result = "";
}
    :
        (
            in:BQ_INTEGER {result += in.getText();}
        |   str:BQ_STRING {result += str.getText();}
        |   m:BQ_MINUS {result += m.getText();}
        |   s:BQ_STAR {result += s.getText();}
        |   w:BQ_WS {result += w.getText();}
        |   a:ANY {result += a.getText();}
        )
	;
     
target returns [Token result]
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
    |   w:BQ_WS {result = w;}
    |   a:ANY {result = a;}
    ;

beginBqAppl [Token symbol] returns [TomTerm result]
{
    result = null;
    LinkedList blockList = new LinkedList();
    String s = null;
    Token t = null;
}
    :
        (
            BQ_STAR
            {
                result = `VariableStar(
                    concOption(
                        OriginTracking(
				       Name(symbol.getText()), 
				       symbol.getLine(), 
				       Name(currentFile())
				       )
			),
                    Name(symbol.getText()),
                    TomTypeAlone("unknown type"),
                    concConstraint()
		    );
	    }

        |   ( BQ_WS )*

            (
                BQ_LPAREN 
                {
                    if(xmlTerm){
                        blockList.add(`Composite(emptyTomList()));
                    }
                }
               
                ( BQ_WS )* ( bqList[blockList] )? ( BQ_WS )* BQ_RPAREN
                {
                    

                    if(blockList.size() > 0) {
                        TomList compositeList = makeCompositeList(blockList);
                        
                        result = `Composite(concTomTerm(BackQuoteAppl(
                                    concOption(
                                        OriginTracking(
                                            Name(symbol.getText()), 
                                            symbol.getLine(), 
                                            Name(currentFile())
                                        )
                                    ),
                                    Name(symbol.getText()),
                                    compositeList
                                ))
                        );
                    }
                    
                    else {
                        result = `Composite(concTomTerm(BackQuoteAppl(
                                    concOption(
                                        Constructor(concTomName(Name(symbol.getText()))),
                                        OriginTracking(
                                            Name(symbol.getText()), 
                                            symbol.getLine(), 
                                            Name(currentFile())
                                        )
                                    ),
                                    Name(symbol.getText()),
                                    emptyTomList()
                                ))
                        );
                        
                    }
                }
            |   t = target
                {
                    addTargetCode(t);
                    p(t.toString());
                    result = `BackQuoteAppl(concOption(OriginTracking(Name(symbol.getText()), 
                                symbol.getLine(), 
                                Name(currentFile())
                            )
                        ),
                        Name(symbol.getText()),
                        concTomTerm()
                    );
                }
            )
        )
        {
            if(result.isComposite() && result.getArgs().isSingle()) {
                TomTerm backQuoteTerm = result.getArgs().getHead(); 
                if(symbol.getText().equals("xml")) {
                    TomList args = backQuoteTerm.getArgs();
                    result = `DoubleBackQuote(args);
                }   
            } 

            setXmlTerm(false);
            selector().pop();
        }
    ;

beginBqComposite returns[TomTerm result]
{
    result = null;
    LinkedList blockList = new LinkedList();
}
    :
        ( bqList[blockList] )? BQ_RPAREN
        {
            TomList compositeList = makeCompositeList(blockList);
            compositeList = `concTomTerm(
                TargetLanguageToTomTerm(ITL("(")),
                compositeList*,
                TargetLanguageToTomTerm(ITL(")"))
            );
            
            result = `Composite(compositeList);
            
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

BQ_LPAREN      :    '('   ;
BQ_RPAREN      :    ')'   ;
BQ_COMMA       :    ','   ;
BQ_STAR        :    '*'   ;

protected BQ_DOT    :    '.'   ;

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





BQ_ID
    options{ testLiterals = true; }   
    :   ('a'..'z' | 'A'..'Z') 
        ( 
            ('a'..'z' | 'A'..'Z') 
        |   BQ_UNDERSCORE 
        |   BQ_DOT
        |   BQ_DIGIT
        )*
    ;

/*
        (
            BQ_STAR { _ttype = ID_STAR; }
        |   (
                ('a'..'z' | 'A'..'Z') 
            |   BQ_UNDERSCORE 
            |   BQ_DOT
            |   BQ_DIGIT
            )*
        )
    ;

*/
/*
ID_STAR
    options{ testLiterals = true; }   
    :
        BQ_ID BQ_STAR
    ;
*/

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
