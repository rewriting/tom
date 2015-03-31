/*
 *
 * Gomantlr
 *
 * Copyright (c) 2006-2015, Universite de Lorraine, Inria
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Eric Deplagne <Eric.Deplagne@loria.fr>
 *
 **/

package aterm2antlrgrammar;

import aterm.*;
import aterm.pure.*;

import antlrgrammar.types.*;
import antlrgrammar.antlrcommons.types.*;
import antlrgrammar.antlrrules.types.*;

import aterm2antlrgrammar.*;

import aterm2antlrgrammar.exceptions.*;

public class ATerm2AntlrGrammar {
   
    %include { ../antlrgrammar/AntlrGrammar.tom }

    private static class Container {
        public AntlrGrammarType type=null;
        public AntlrId id=null;
        public AntlrComment comment=`AntlrNilComment();
        public AntlrOptions options=`AntlrOptions();
        public AntlrTokens tokens=`AntlrNilTokens();
        public AntlrScopes scopes=`AntlrScopes();
        public AntlrActions actions=`AntlrActions();
        public AntlrRules rules=`AntlrRules();

        public boolean goodParse=true;
        public AntlrWrong wrong=null;
    }

    %include { ../antlr.tom }

    /*
     *
     * This is the grammar corresponding to the AST,
     * which explains what is done here.
     *
     * The actual grammar is a bit different,
     * but is not what matters here.
     *
     * Grammar: CombinedGrammar | LexerGrammar | ParserGrammar | TreeGrammar
     *
     * CombinedGrammar: "grammar"! Id Comment? Options? Tokens? Scope* Action* Rule+
     *
     * LexerGrammar: "lexer"! "grammar"! Id Comment? Options? Tokens? Scope* Action* Rule+
     * 
     * ParserGrammar: "parser"! "grammar"! Id Comment? Options? Tokens? Scope* Action* Rule+
     * 
     * TreeGrammar: "tree"! "grammar"! Id Comment? Options? Tokens? Scope* Action* Rule+
     *
     */

    /*
     * As the fields are the same, 
     * we in fact use an additional "GrammarType" field
     * in the result.
     * 
     */

    public static AntlrGrammar getAntlrGrammar(ATerm t) throws AntlrWrongGrammarException {
        Container container=new Container();
        ATermList l=null;
        %match(t) {
            COMBINED_GRAMMAR(_,x) -> {
                container.type=`AntlrCombinedGrammar();
                l=`x;
            }
            LEXER_GRAMMAR(_,x) -> {
                container.type=`AntlrLexerGrammar();
                l=`x;
            }
            PARSER_GRAMMAR(_,x) -> {
                container.type=`AntlrParserGrammar();
                l=`x;
            }
            TREE_GRAMMAR(_,x) -> {
                container.type=`AntlrTreeGrammar();
                l=`x;
            }
        }
        if(container.type!=null) {
            parseArgs(l,container);
            if(container.goodParse) {
                return `AntlrGrammar(
                    container.type,
                    container.id,
                    container.comment,
                    container.options,
                    container.tokens,
                    container.scopes,
                    container.actions,
                    container.rules);
            } else {
                if(container.wrong!=null) {
                    throw new AntlrWrongGrammarException(
                        `AntlrIncorrectGrammar(
                            container.type,
                            container.id,
                            container.comment,
                            container.options,
                            container.tokens,
                            container.scopes,
                            container.actions,
                            container.rules,
                            container.wrong));
                } else {
                    throw new AntlrWrongGrammarException(
                        `AntlrIncorrectGrammarArgs(
                            container.type,
                            container.id,
                            container.comment,
                            container.options,
                            container.tokens,
                            container.scopes,
                            container.actions,
                            container.rules));
                }
            }
        } else {
            throw new `AntlrWrongGrammarException(AntlrPlainWrongGrammar(ATerm2AntlrWrong.getAntlrWrong(t)));
        }
    }
    
    /*
     *
     * Id
     *
     */

    private static void parseArgs(ATermList l,Container container) {
        %match(l) {
            concATerm(x@ID[],y*) -> {
                try {
                    container.id=ATerm2AntlrId.getAntlrId(`x);
                } catch (AntlrWrongIdException e) {
                    container.goodParse=false;
                    container.id=e.getAntlrId();
                }
                parseArgs2(`y,container);
                return;
            }
            _ -> {
                container.goodParse=false;
                try {
                    container.id=ATerm2AntlrId.getAntlrId(`concATerm());
                } catch (AntlrWrongIdException e) {
                    container.id=e.getAntlrId();
                }
                parseArgs2(l,container);
            }
        }
    }
    
    /*
     *
     * Comment?
     *
     */
    
    private static void parseArgs2(ATermList l,Container container) {
        %match(l) {
            concATerm(x@DOC_COMMENT[],y*) -> {
                try {
                    container.comment=ATerm2AntlrComment.getAntlrComment(`x);
                } catch (AntlrWrongCommentException e) {
                    container.goodParse=false;
                    container.comment=e.getAntlrComment();
                }
                parseArgs3(`y,container);
                return;
            }
            _ -> {
                parseArgs3(l,container);
            }
        }
    }
    
    /*
     *
     * Options?
     *
     */

    private static void parseArgs3(ATermList l,Container container) {
        %match(l) {
            concATerm(x@OPTIONS[],y*) -> {
                try {
                    container.options=ATerm2AntlrOptions.getAntlrOptions(`x);
                } catch (AntlrWrongOptionsException e) {
                    container.goodParse=false;
                    container.options=e.getAntlrOptions();
                }
                parseArgs4(`y,container);
                return;
            }
            _ -> {
                parseArgs4(l,container);           
            }
        }
    }
    
    /*
     *
     * Tokens?
     *
     */
    
    private static void parseArgs4(ATermList l,Container container) {
        %match(l) {
            concATerm(x@TOKENS[],y*) -> {
                try {
                    container.tokens=ATerm2AntlrTokens.getAntlrTokens(`x);
                } catch (AntlrWrongTokensException e) {
                    container.goodParse=false;
                    container.tokens=e.getAntlrTokens();
                }
                parseArgs5(`y,container);
                return;
            }
            _ -> {
                parseArgs5(l,container);           
            }
        }
    }

    /*
     *
     * Scope*
     *
     */
    
    private static void parseArgs5(ATermList l,Container container) {
        %match(l) {
            concATerm(x@SCOPE[],y*) -> {
                AntlrScopes scopes=container.scopes;
		AntlrScope scope;
		try {
		    scope=ATerm2AntlrScope.getAntlrScope(`x);
		} catch (AntlrWrongScopeException e) {
		    container.goodParse=false;
		    scope=e.getAntlrScope();
		}
                container.scopes=`AntlrScopes(scopes*,scope);
                parseArgs5(`y,container);
                return;
            }
	    _ -> {
		parseArgs6(l,container);
	    }
        }
    }

    /*
     *
     * Action*
     *
     */

    private static void parseArgs6(ATermList l,Container container) {
        %match(l) {
            concATerm(x@AMPERSAND[],y*) -> {
                AntlrActions actions=container.actions;
                AntlrAction action;
                try {
                    action=ATerm2AntlrAction.getAntlrAction(`x);
                } catch (AntlrWrongActionException e) {
                    container.goodParse=false;
                    action=e.getAntlrAction();
                    
                }
                container.actions=`AntlrActions(actions*,action);
                parseArgs6(`y*,container);
                return;
            }
            _ -> {
                parseArgs7(l,container);
            }
        }
    }

    /*
     *
     * Rule+
     *
     */
    
    private static void parseArgs7(ATermList l,Container container) {
        %match(l) {
            concATerm(x@RULE[]) -> {
                AntlrRules rules=container.rules;
                AntlrRule rule;
                try {
                    rule=ATerm2AntlrRule.getAntlrRule(`x);
                } catch (AntlrWrongRuleException e) {
                    container.goodParse=false;
                    rule=e.getAntlrRule();
                    
                }
                boolean keep=true;
                AntlrGrammarType type=container.type;
                %match(type) {
                    // The last rule of the lexer grammar is added by ANTLR, and we don't want it.
                    AntlrLexerGrammar() -> {
                        keep=false;
                    }
                }
                if(keep) {
                    %match(rule) {
                        AntlrRule(AntlrId(name),_,_,_,_,_,_,_,_) -> {
                            // synpred rules are added by ANTLR, and we don't want them.
                            if(`name.regionMatches(0,"synpred",0,7)) {
                                keep=false;
                            }
                        }
                    }
                }
                if(keep) {
                    container.rules=`AntlrRules(rules*,rule);
                }
                return;
            }
            concATerm(x@RULE[],y,z*) -> {
                AntlrRules rules=container.rules;
                AntlrRule rule;
                try {
                    rule=ATerm2AntlrRule.getAntlrRule(`x);
                } catch (AntlrWrongRuleException e) {
                    container.goodParse=false;
                    rule=e.getAntlrRule();
                    
                }
                boolean keep=true;
                %match(rule) {
                    AntlrRule(AntlrId(name),_,_,_,_,_,_,_,_) -> {
                        // synpred rules are added by ANTLR, and we don't want them.
                        if(`name.regionMatches(0,"synpred",0,7)) {
                            keep=false;
                        }
                        // T<n> rules are added by ANTLR, and we don't want them.
                        if(`name.charAt(0)=='T') {
                            try {
                                Integer.parseInt(`name.substring(1));
                                keep=false;
                            } catch (NumberFormatException e) {
                                // Not an integer, keep it.
                            }
                        }
                    }
                }
                if(keep) {
                    container.rules=`AntlrRules(rules*,rule);
                }
                parseArgs7(`concATerm(y,z*),container);
                return;
            }
        }
        parseArgs8(l,container);
    }

    private static void parseArgs8(ATermList l,Container container) {
        %match(l) {
            concATerm(x,y*) -> {
                container.goodParse=false;
                container.wrong=ATerm2AntlrWrong.getAntlrWrong(l);
            }
        }
    }
}
