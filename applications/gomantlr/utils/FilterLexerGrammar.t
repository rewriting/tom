package utils;

import antlrgrammar.types.AntlrGrammar;
import antlrgrammar.antlrrules.types.AntlrRules;

public class FilterLexerGrammar {

    %include { ../antlrgrammar/AntlrGrammar.tom }

    public static AntlrGrammar filterLexerGrammar(AntlrGrammar grammar) {
        %match(grammar) {
            AntlrGrammar(a,b,c,d,e,f,g,rules) -> {
                AntlrRules lexerRules=filterLexerRules(`rules);

                return `AntlrGrammar(a,b,c,d,e,f,g,lexerRules);
            }
        }

        return grammar;
    }

    public static AntlrRules filterLexerRules(AntlrRules rules) {
        %match(rules) {
            AntlrRules(x@AntlrRule(AntlrId(name),_,_,_,_,_,_,_,_),y,z*) -> {
                // T<n> rules are added by ANTLR, and we don't want them.
                AntlrRules antlrRules=filterLexerRules(`AntlrRules(y,z*));

                if(`name.charAt(0)=='T') {
                    try {
                        Integer.parseInt(`name.substring(1));
                        return antlrRules;
                    } catch (NumberFormatException e) {
                        return `AntlrRules(x,antlrRules*);
                    }
                } else {
                    return `AntlrRules(x,antlrRules*);
                }
            }
            // The last rule of the lexer grammar is added by ANTLR, and we don't want it.
            AntlrRules(x) -> {
                return `AntlrRules();
            }
        }

        return rules;
    }
}
