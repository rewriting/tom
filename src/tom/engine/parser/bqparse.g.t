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
    
    public PluginPlatform getPluginPlatform(){
        return PluginPlatform.getInstance();
    }
    
    private TomEnvironment environment() {
  //      return getPluginPlatform().getEnvironment();
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
            TomTerm last = (TomTerm) list.getLast();
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
    /*
    private String removeStar(String s){
        return s.substring(0,s.lastIndexOf('*'));
    }
    */
    private String encodeName(String name) {
        return "\"" + name + "\"";
    }
 
    private TomList buildList(LinkedList list){
        TomList result = `emptyTomList();
        for(int i = 0; i < list.size(); i++){
            result = (TomList) result.append((TomTerm) list.get(i));
        }
        return result;
    }

    private TomTerm removeComposite(TomTerm term){
        %match(TomTerm term){
            Composite(concTomTerm(arg@BackQuoteAppl[])) -> {
                return arg;
            }
            _ -> {return term;}
        }
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
            (
                c:BQ_COMMA ( options{greedy = true;}: BQ_WS )* )? term = bqTerm 
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
    String s;
}
    :
        (   
            result = bqTermShared
        |   s = targetPlus
            {
                result = `TargetLanguageToTomTerm(ITL(s));
            }
        )
    ;

bqTermShared returns [TomTerm result]
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
            }
            
        |
            (BQ_ID (BQ_WS)* BQ_LPAREN) => i2:BQ_ID ( w:BQ_WS )*
            BQ_LPAREN ( options{greedy=true;}: BQ_WS )* (bqList[blockList])? BQ_RPAREN 
            {
                if(blockList.size() > 0) {
                    TomList compositeList = makeCompositeList(blockList);
                    
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
        
        |   BQ_LPAREN ( options{greedy = true;}: BQ_WS )*
            ( bqList[blockList] )?  BQ_RPAREN 
            {
                TomList compositeList = makeCompositeList(blockList);
                compositeList = `concTomTerm(
                    TargetLanguageToTomTerm(ITL("(")),
                    compositeList*,
                    TargetLanguageToTomTerm(ITL(")"))
                );
                
                result = `Composite(compositeList);
            }
        )
    ;

bqTermForXml returns [TomTerm result]
{
    result = null;
    String s;
}
    :
        result = bqTermShared
    |   s = targetPlusShared
        {
            result = `TargetLanguageToTomTerm(ITL(s));
        }
    ;


targetPlusShared returns [String result]
{
    result = "";
}
    :
        in:BQ_INTEGER {result = in.getText();}
    |   str:BQ_STRING {result = str.getText();}
    |   m:BQ_MINUS {result = m.getText();}
    |   s:BQ_STAR {result = s.getText();}
    |   w:BQ_WS {result = w.getText();}
    |   a:ANY {result = a.getText();}
    ;

targetPlus returns [String result]
{
    result = "";
    String x = "";
}
    :
        (
            x = xmlToken {result += x;}
        |   x = targetPlusShared {result += x;}
        )+
	;

xmlToken returns [String result]
{
    result = "";
}
    :
        
        t:XML_START_ENDING    {result = t.getText();}
    |   t1:XML_CLOSE_SINGLETON {result = t1.getText();}
    |   t2:XML_START  {result = t2.getText();}
    |   t3:XML_CLOSE  {result = t3.getText();}
    |   t4:DOUBLE_QUOTE {result = t4.getText();}
    |   t5:XML_TEXT {result = t5.getText();}
    |   t6:XML_COMMENT {result = t6.getText();}
    |   t7:XML_PROC {result = t7.getText();}
    |   t8:XML_EQUAL {result = t8.getText();}
    ;

xmlTokenToken returns [Token result]
{
    result = null;
}
    :
        
        t:XML_START_ENDING    {result = t;}
    |   t1:XML_CLOSE_SINGLETON {result = t1;}
    |   t2:XML_START  {result = t2;}
    |   t3:XML_CLOSE  {result = t3;}
    |   t4:DOUBLE_QUOTE {result = t4;}
    |   t5:XML_TEXT {result = t5;}
    |   t6:XML_COMMENT {result = t6;}
    |   t7:XML_PROC {result = t7;}
    |   t8:XML_EQUAL {result = t8;}
    ;

     
target returns [Token result]
{
    result = null;
}
    :
        result = xmlTokenToken
    |    c:BQ_COMMA {result = c;} 
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

        |   (
                BQ_LPAREN 
                {
                   /* if(xmlTerm){
                        blockList.add(`Composite(emptyTomList()));
                    }*/
                }
                ( options{greedy = true;}: BQ_WS )* ( bqList[blockList] )? BQ_RPAREN
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

beginXmlBackquote returns [TomTerm result]
{
    result = null;
    TomTerm term = null;
    TomList termList = `emptyTomList();
    TomList contextList = `emptyTomList();
}

    :
        BQ_LPAREN (BQ_WS)* 
        ( {LA(1) != XML_START}? contextList = context )?
        (
            term = xmlTerm[contextList]//[attributeList,childrenList]
            {
/*
                if(contextList != null){
                    term = updateContext(term,contextList);
                    contextList = null;
                }
*/
                termList = (TomList) termList.append(term);
            }
        )*
        BQ_RPAREN
        { 
            selector().pop();
       //     termList = updateContext(termList,contextList);
            result = `Composite(termList);
        }
    ;

context returns [TomList result]
{
    result = `emptyTomList();
    TomTerm term = null;
}
    :
        {LA(1) != XML_START}? term = bqTermForXml BQ_COMMA (BQ_WS)*
        {
            result = (TomList) result.append(removeComposite(term));
        }
        ( 
            {LA(1) != XML_START}? term = bqTermForXml
                {
                    result = (TomList) result.append(removeComposite(term));
                }
            BQ_COMMA (BQ_WS)*
        )*
    ;

xmlTerm [TomList context] //[LinkedList attributes, LinkedList children] 
returns [TomTerm result]
{
    result = null;
    TomTerm term = null;

    LinkedList attributes = new LinkedList();
    LinkedList children = new LinkedList();
    TomList attributeTomList = `emptyTomList();
    TomList childrenTomList = `emptyTomList();
}
    :
        (
            XML_START (BQ_WS)* name:BQ_ID
            
            (BQ_WS)* xmlAttributeList[attributes,context]
            {
                attributeTomList = buildList(attributes);
            }
            ( 
                XML_CLOSE_SINGLETON (BQ_WS)*
                
            |   XML_CLOSE (BQ_WS)*  
                xmlChildren[children,context]
                {
                    childrenTomList = buildList(children);
                }
                XML_START_ENDING (BQ_WS)* 
                BQ_ID (BQ_WS)* XML_CLOSE
            )
            {
                if(context == null){
                    result = `BackQuoteAppl(
                        emptyOptionList(),
                        Name(Constants.ELEMENT_NODE),
                        concTomTerm(
                        BackQuoteAppl(
                                emptyOptionList(),
                                Name(encodeName(name.getText())),
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
                        )
                    );
                }
                else{
                    result = `BackQuoteAppl(
                        emptyOptionList(),
                        Name(Constants.ELEMENT_NODE),
                        concTomTerm(
                            context*,
                            BackQuoteAppl(
                                emptyOptionList(),
                                Name(encodeName(name.getText())),
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
                        )
                    );
                }
            }
            
            
        |   term = bqTermForXml
            {
              result = term;
            }
        )
    ;

xmlAttributeList [LinkedList attributeList, TomList context]
{
    TomTerm term = null;
}
    :
        ( 
            term = xmlAttribute[context]
            {
                attributeList.add(term);
            }
            (BQ_WS)* 
        )*
    ;

xmlAttribute [TomList context] returns [TomTerm result]
{
    result = null;
    TomTerm value = null;
}
    :
        n:BQ_ID  
        (
            (BQ_WS)* XML_EQUAL (BQ_WS)* value = termStringIdentifier
            {
                if(context == null){
                    result = `BackQuoteAppl(
                        emptyOptionList(),
                        Name(Constants.ATTRIBUTE_NODE),
                        concTomTerm(
                            BackQuoteAppl(
                                emptyOptionList(),
                                Name(encodeName(n.getText())),
                                emptyTomList()
                            ),
                            BackQuoteAppl(
                                emptyOptionList(),
                                Name("\"true\""),
                                emptyTomList()
                            ),
                            value
                        )
                    );
                }   
                else{
                    result = `BackQuoteAppl(
                        emptyOptionList(),
                        Name(Constants.ATTRIBUTE_NODE),
                        concTomTerm(
                            context*,
                            BackQuoteAppl(
                                emptyOptionList(),
                                Name(encodeName(n.getText())),
                                emptyTomList()
                            ),
                            BackQuoteAppl(
                                emptyOptionList(),
                                Name("\"true\""),
                                emptyTomList()
                            ),
                            value
                        )
                    );
                }
            }
        |   BQ_STAR 
            {
                result = `VariableStar(
                    emptyOptionList(),
                    Name(n.getText()),
                    TomTypeAlone("unknown type"),
                    emptyConstraintList()
                );
            }
        )
    ;

termStringIdentifier returns [TomTerm result]
{
    result = null;
}
    :
        name:BQ_ID 
        {
            result = `TargetLanguageToTomTerm(ITL(name.getText()));
        }
    |   string:BQ_STRING
        {
            result = `TargetLanguageToTomTerm(ITL(string.getText()));
        }
    ;

xmlChildren [LinkedList children, TomList context]
{
   // result = null;
    TomTerm term = null;
    
    LinkedList attributeList = new LinkedList();
    LinkedList childrenList = new LinkedList();    
}
    :
        ( 
            term = xmlTerm[context]//[attributeList,childrenList] 
            {
                children.add(term);
            }
            (BQ_WS)*
        )*
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
        |   BQ_DOT
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
