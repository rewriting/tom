class NewTargetParser extends Parser;

options {
    importVocab = Target;
}

{
    TomParser tomparser = new TomParser(getInputState(),this);
    TargetLexer targetlexer = (TargetLexer) Main.selector.getStream("targetlexer");
    
    StringBuffer targetLanguage = new StringBuffer("");

    int compteur = 0;
}

input
	:
        {System.out.println("***** targetinput");}
        blockList() 
        {
            System.out.println("------- buffer :\n"+targetlexer.target);
        }
    ;

blockList
    :
        (
            matchConstruct() 
        |   signature()
        |   localVariable()
        |   operator()
        |   operatorList()
        |   operatorArray()
        |   includeConstruct()
        |   typeTerm()
        |   typeList()
        |   typeArray()
        |   LBRACE //{compteur++;System.out.println(compteur);} 
            blockList() 
            RBRACE //{compteur--;}
        |   TARGET
        )*
    ;

matchConstruct
	:
        {
            System.out.println("target language :"+targetlexer.target);
            targetlexer.clearTarget();
            System.out.println("******** compteur avant match :" + compteur);
        }
        MATCH 
        {         
            tomparser.matchConstruct();
            System.out.println("******** compteur apres match :" + compteur);
        }
    ;

signature
    :
        {
            System.out.println("target language :"+targetlexer.target);
            targetlexer.clearTarget();
        }
        VAS 
        {   
            tomparser.signature();
        } 
    ;

localVariable
    :
        {
            targetlexer.clearTarget();
        }
        VARIABLE
        {
            tomparser.variable();
        }
    ;

operator
    :
        {
            System.out.println("target language :"+targetlexer.target);
            targetlexer.clearTarget();
        }
        OPERATOR 
        {
            tomparser.operator();
        }
    ;

operatorList
    :
        {
            System.out.println("target language :"+targetlexer.target);
            targetlexer.clearTarget();
        }
        OPERATORLIST 
        {
            tomparser.operatorList();
        }
    ;

operatorArray
    :
        {
            System.out.println("target language :"+targetlexer.target);
            targetlexer.clearTarget();
        }
        OPERATORARRAY
        {
            tomparser.operatorArray();
        }
    ;

includeConstruct
    :
        INCLUDE
        {
            tomparser.include();
        }
        
    ;
typeTerm
    :
        (
            TYPETERM
        |   TYPE
        )
        {
            tomparser.typeTerm();
        }

    ;

typeList
    :
        TYPELIST
        {
             tomparser.typeList();
        }
    ;

typeArray
    :
        TYPEARRAY
        {
             tomparser.typeArray();
        }
    ;


goalLanguage returns [String result]
{
    result = null;
}
    :
        LBRACE blockList()
        RBRACE 
        {
            result = targetlexer.target.toString();
            targetlexer.clearTarget();
        }
    ;

targetLanguage returns [String result]
{
    result = null;
}
    :
        blockList() RBRACE
        {
            result = targetlexer.target.toString();
            targetlexer.clearTarget();
     
        }
    ;
