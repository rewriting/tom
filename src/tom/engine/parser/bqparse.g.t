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
    k=1;
}

{
    %include{TomSignature.tom}
    
    NewBQLexer bqlexer = null;

    NewTomParser tomparser = null;

    String currentFile(){
        return tomparser.currentFile();
    }

    public NewBQParser(ParserSharedInputState state, NewTomParser tomparser){
        this(state);
        this.tomparser = tomparser;
        bqlexer = (NewBQLexer) selector().getStream("bqlexer");
    }

    private final TomSignatureFactory getTomSignatureFactory(){
        return tsf();
    }
    
    public PluginPlatform getPluginPlatform(){
        return PluginPlatform.getInstance();
    }
    
    private TomEnvironment environment() {
        return TomEnvironment.getInstance();
    }

    private TomSignatureFactory tsf(){
        return environment().getTomSignatureFactory();
    }

    private void addTargetCode(Token t){
        tomparser.addTargetCode(t);
    }

    private TokenStreamSelector selector(){
        return tomparser.selector();
    }

    private boolean addTarget = false;

    private TomTerm buildBqAppl(Token id,LinkedList blockList,TomTerm term,boolean composite){
        TomTerm result = null;
        TomList list = buildList(blockList);

        %match(TomList list){
            emptyTomList() -> {
                if(term == null){
                    if(composite){
                        return `Composite(
                            concTomTerm(
                                BackQuoteAppl(
                                    concOption( 
                                        Constructor(concTomName(Name(id.getText()))),
                                        OriginTracking(
                                            Name(id.getText()), 
                                            id.getLine(), 
                                            Name(currentFile())
                                        )
                                    ),
                                    Name(id.getText()),
                                    emptyTomList()
                                )
                            )
                        );
                    }
                    else{
                        return `BackQuoteAppl(
                            concOption( 
                                OriginTracking(
                                    Name(id.getText()), 
                                    id.getLine(), 
                                    Name(currentFile())
                                )
                            ),
                            Name(id.getText()),
                            emptyTomList()
                        );
                    }
                }
                else{
                    if(composite){
                        return `Composite(
                            concTomTerm(
                                BackQuoteAppl(
                                    concOption( 
                                        Constructor(concTomName(Name(id.getText()))),
                                        OriginTracking(
                                            Name(id.getText()), 
                                            id.getLine(), 
                                            Name(currentFile())
                                        )
                                    ),
                                    Name(id.getText()),
                                    emptyTomList()
                                ),
                                TargetLanguageToTomTerm(ITL(".")),
                                term
                            )
                        );
                    }
                    else{
                        return `Composite(
                            concTomTerm(
                                BackQuoteAppl(
                                    concOption( 
                                        OriginTracking(
                                            Name(id.getText()), 
                                            id.getLine(), 
                                            Name(currentFile())
                                        )
                                    ),
                                    Name(id.getText()),
                                    emptyTomList()
                                ),
                                TargetLanguageToTomTerm(ITL(".")),
                                term
                            )
                        );
                    }
                }
            }
            _ -> {
                if(term == null){
                    return `Composite(
                        concTomTerm(
                            BackQuoteAppl(
                                concOption( 
                                    OriginTracking(
                                        Name(id.getText()), 
                                        id.getLine(), 
                                        Name(currentFile())
                                    )
                                ),
                                Name(id.getText()),
                                list
                            )
                        )
                    );
                }
                else{
                    return `Composite(
                        concTomTerm(
                            BackQuoteAppl(
                                concOption(
                                    OriginTracking(
                                        Name(id.getText()), 
                                        id.getLine(), 
                                        Name(currentFile())
                                    )
                                    ),
                                Name(id.getText()),
                                list
                            ),
                            TargetLanguageToTomTerm(ITL(".")),
                            term
                        )
                    );
                }
            }
        }
    }
    
    private void addTerm(LinkedList list, TomTerm term, boolean newComposite){
        if(list.size() == 0){
            list.add(`Composite(emptyTomList()));
        }
        TomTerm lastElement = (TomTerm) list.getLast();
        
        %match(TomTerm lastElement){
            Composite(l) -> {
                if(!newComposite){
                    list.set(list.size()-1,`Composite(concat(l,term)));
                }
                else{
                    addComposite(list,term);
                }
                return;
            }
            _ -> {
                if(!newComposite){
                    list.add(term);
                }
                else{
                    addComposite(list,term);
                }
            }
        }
    }

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

    private TomList sortAttributeList(TomList list){
        %match(TomList list){
            concTomTerm() -> {
                return list;
            }
            concTomTerm(X1*,e1,X2*,e2,X3*) -> {
                %match(TomTerm e1,TomTerm e2){
/*
                    BackQuoteAppl[args=manyTomList(Appl[nameList=(Name(name1))],_)],
                    BackQuoteAppl[args=manyTomList(Appl[nameList=(Name(name2))],_)] -> {
                        if(`name1.compareTo(`name2) > 0) {
                            return `sortAttributeList(concTomTerm(X1*,e2,X2*,e1,X3*));
                        }
                    }
*/
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

    private TomList buildAttributeList(LinkedList list){/*
        TomList result = `emptyTomList();
        TomList tmp = `emptyTomList();
        for(int i = 0; i < list.size(); i++){
            if(i< 2){
                tmp = (TomList) tmp.append((TomTerm) list.get(i));
            }
            else{
                result = (TomList) result.append((TomTerm) list.get(i));
            }
        }
        result = `concTomTerm(result*,tmp*);*/
        TomList result = buildList(list);
        return sortAttributeList(result);
    }

    private TomList buildList(LinkedList list){
        TomList result = `emptyTomList();
        for(int i = 0; i < list.size(); i++){
            result = (TomList) result.append((TomTerm) list.get(i));
        }
        return result;
    }
  
    private String encodeName(String name) {
        return "\"" + name + "\"";
    }

    private int addTermToList(TomList list, LinkedList target, int i){
        int result = i;
        while(! list.isEmpty()){
            result++;
            target.add(i,list.getHead());
            list = list.getTail();
        }
        return result;
    }

    private void removeComposite(LinkedList list){
        TomTerm term = null;
        for(int i = 0; i < list.size(); i++){
            term = (TomTerm) list.remove(i);
            match:
            {
                %match(TomTerm term){
                    Composite(l) -> {
                        i = addTermToList(l,list,i);
                        break match;
                    }
                    _ -> {
                        list.add(i,term);
                        break match;
                    }
                }
            }
        }
    }


    public void p(String s){
        System.out.println(s);
    }
}



ws
    :
        ( options{ greedy = true; }: BQ_WS )*
    ;

termList [LinkedList list,TomList context]
{
    TomTerm term = null;
}
    :
        (
            term = bqTerm[context]
            { 
                addTerm(list,term,false); }
            ws 
            ( 
                ( c:BQ_COMMA  ws )? 
                term = bqTerm[context] ws
                {
                    if(c != null){
                        addTerm(list,term,true);
                    }
                    else{
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

/*  (
            {LA(1) != XML_START_ENDING}?
            termList[children,context] ws 
        )?*/
/*        {
            removeComposite(children);
            
        }*/
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
//    |   m:BQ_MINUS {result = m;}
    |   s:BQ_STAR {result = s;}
    |   w:BQ_WS {result = w;}
    |   d:BQ_DOT {result = d;} 
    |   t4:DOUBLE_QUOTE {result = t4;}
    |   x:XML_START {result = x;}
    |   t8:XML_EQUAL {result = t8;}
    |   t3:XML_CLOSE  {result = t3;}
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
    |   t1:XML_CLOSE_SINGLETON {result = t1;}
    |   t5:XML_TEXT {result = t5;}
    |   t6:XML_COMMENT {result = t6;}
    |   t7:XML_PROC {result = t7;}
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
                    (bqTerm[null] BQ_COMMA) => 
                    term = bqTerm[context] BQ_COMMA ws
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
                TomList compositeList = buildList(blockList);
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
        result = basicTerm[context]
        
    |   id:BQ_ID
        (
            {LA(1) == BQ_STAR}? BQ_STAR
            {   
                String name = id.getText();
                result = `VariableStar(
                    concOption(
                        OriginTracking(
                            Name(name), 
                            id.getLine(), 
                            Name(currentFile())
                        )
                    ),  
                    Name(name),
                    TomTypeAlone("unknown type"),
                    concConstraint()
                );      
            }
            
        |   ws 
            (
                {LA(1) == BQ_LPAREN}? 
                BQ_LPAREN 
                {arguments = true;}
                ws 
                ( 
                    termList[blockList,context] 
                )? BQ_RPAREN 
            )?
            ( (BQ_DOT term = bqTerm[null] ) => BQ_DOT term = bqTerm[context] )?
            {   
                result = buildBqAppl(id,blockList,term,arguments);
            }
        )
    |    
        (xmlTerm[null]) => result = xmlTerm[context]
        
    |
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
            result = basicTerm[list]            
        |   id1:BQ_ID   
            (
                {LA(1) == BQ_STAR}? BQ_STAR
                {   
                    String name = id1.getText();
                    result = `VariableStar(
                        concOption(
                            OriginTracking(
                                Name(name), 
                                id1.getLine(), 
                                Name(currentFile())
                            )
                        ),  
                        Name(name),
                        TomTypeAlone("unknown type"),
                        concConstraint()
                    );  
                }
                
            |   ws 
                (
                    {LA(1) == BQ_LPAREN}? BQ_LPAREN 
                    ws ( termList[blockList,list] )? BQ_RPAREN 
                    {   
                        result = buildBqAppl(id1,blockList,term,true);
                    }
                |   t = targetCode
                    {
                        addTargetCode(t);
                        
                        result = `BackQuoteAppl(
                            concOption(
                                OriginTracking(
                                    Name(id1.getText()), 
                                    id1.getLine(), 
                                    Name(currentFile())
                                )
                            ),
                            Name(id1.getText()),
                            concTomTerm()
                        );
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










class NewBQLexer extends Lexer;
options {
    charVocabulary = '\u0000'..'\uffff'; // each character can be read
    k=2;
}

tokens {
    XML = "xml";
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
            options{greedy=true;}:
            (
                ('a'..'z' | 'A'..'Z') 
            |   BQ_UNDERSCORE 
            |   BQ_DIGIT
            )
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
   
protected BQ_MINUS   :   '-'  ;

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

ANY 
    :
        '\u0000'..'\uffff'  
    ;
