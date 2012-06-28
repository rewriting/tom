lexer grammar TomLexer;

@header {
package tom.lexer;


import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

@members{


boolean lookAheadMatch(String s) {
	int i=0;
	while (i<s.length()) {
		if (input.LA(1)!=s.charAt(i) ) {
			return false;
		}
	}
	return true;
}

private int depth = 0;

}

RULE_FIRST_LEVEL_LBRACKET : '{';

RULE_FIRST_LEVEL_RBRACKET : '}';

RULE_BRCKTSTMT : '{' ( options {greedy=false;} : . )*'}';

RULE_IMPLEMENT_KW : 'implement';

RULE_ISFSYM_KW : 'is_fsym';

RULE_ISSORT_KW : 'is_sort';

RULE_MAKE_APPEND_KW : 'make_append';

RULE_MAKE_EMPTY_KW : 'make_empty';

RULE_MAKE_KW : 'make';

RULE_GET_SLOT_KW : 'get_slot';

RULE_GET_SIZE_KW : 'get_size';

RULE_GET_ELEMENT_KW : 'get_element';

RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_ANY_OTHER : .;


RULE_FIRST_LEVEL_LBRACKET : {(depth<=0)}? '{' {depth=1;};

RULE_BRCKTSTMT : {depth>0}? '{' ( options {greedy=false;} : . )+ '}';

RULE_FIRST_LEVEL_RBRACKET : '}' {depth=0;};


RULE_IMPLEMENT_KW : 'implement';

RULE_ISFSYM_KW : 'is_fsym';

RULE_ISSORT_KW : 'is_sort';

RULE_MAKE_APPEND_KW : 'make_append';

RULE_MAKE_EMPTY_KW : 'make_empty';

RULE_MAKE_KW : 'make';

RULE_GET_SLOT_KW : 'get_slot';

RULE_GET_SIZE_KW : 'get_size';

RULE_GET_ELEMENT_KW : 'get_element';

RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_ANY_OTHER : .;

