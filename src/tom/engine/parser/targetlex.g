class NewTargetLexer extends Lexer;
options {
	k=6;
    exportVocab = Target;
    filter=TARGET;
    charVocabulary='\u0000'..'\uffff';
}

{
    public StringBuffer target = new StringBuffer("");
    
    public void clearTarget(){
        target.delete(0,target.length());
    }
}

RULE
    :   "%rule" {Main.selector.push("tomlexer");}
    ;
INCLUDE
    :   "%include" 
    ;
MATCH
	:	"%match" {Main.selector.push("tomlexer");}
    ;
VARIABLE
    :   "%variable" 
	;
VAS
    :   "%vas"  
    ;
OPERATOR
    :   "%op"   {Main.selector.push("tomlexer");}
    ;
TYPE
    :   "%type" {Main.selector.push("tomlexer");}
    ;
TYPETERM
    :   "%typeterm" {Main.selector.push("tomlexer");}
    ;
TYPELIST
    :   "%typelist" {Main.selector.push("tomlexer");}
    ;
TYPEARRAY
    :   "%typearray" {Main.selector.push("tomlexer");}
    ;   
OPERATORLIST
    :   "%oplist"   {Main.selector.push("tomlexer");}
    ;
OPERATORARRAY
    :   "%oparray"  {Main.selector.push("tomlexer");}
    ;

LBRACE  :   '{' {target.append($getText);}  ;
RBRACE  :   '}' {target.append($getText);}  ; 

WS	:	(	' '
		|	'\t'
		|	'\f'
		// handle newlines
		|	(	"\r\n"  // Evil DOS
			|	'\r'    // Macintosh
			|	'\n'    // Unix (the right way)
			)
			{ newline(); }
		){  target.append($getText);
            $setType(Token.SKIP);}
		
	;


protected
ESC
	:	'\\'
		(	'n'
		|	'r'
		|	't'
		|	'b'
		|	'f'
		|	'"'
		|	'\''
		|	'\\'
		|	('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
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
HEX_DIGIT
	:	('0'..'9'|'A'..'F'|'a'..'f')
	;

COMMENT 
    :
        ( SL_COMMENT | t:ML_COMMENT {$setType(t.getType());} )
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
		:	'\r' '\n'
		|	'\r'
		|	'\n'
        )
        { newline(); }
	;

protected
ML_COMMENT 
    :
        "/*"        
        (	{ LA(2)!='/' }? '*' 
        |
        )
        (
            options {
                greedy=false;  // make it exit upon "*/"
                generateAmbigWarnings=false; // shut off newline errors
            }
        :	'\r' '\n'	{newline();}
        |	'\r'		{newline();}
        |	'\n'		{newline();}
        |	~('\n'|'\r')
        )*
        "*/"
	;

protected 
TARGET 
    :
        (   
            . 
        )
        {target.append($getText);}
    ;
