grammar parsematch;
 
tokens {
    MATCH = '%match';
    LEFTBRACE='(';
    RIGHTBRACE=')';
    LEFTBRACKET='{';
    RIGHTBRACKET='}';
}
 

program
    :    statement*
    ;
 
statement
    :     MATCH LEFTBRACE RIGHTBRACE LEFTBRACKET LETTER+ RIGHTBRACKET
    ;

 
 LETTER
    :     'A'..'Z' | 'a'..'z' | '_'
    ;
