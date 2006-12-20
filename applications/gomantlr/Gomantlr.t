/*
 *
 * Gomantlr
 *
 * Copyright (c) 2006, INRIA
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

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.Tree;

import org.antlr.tool.GrammarAST;
import org.antlr.tool.Grammar;
import org.antlr.tool.ANTLRLexer;
import org.antlr.tool.ANTLRParser;

import org.antlr.Tool;

import antlr.TokenStreamRewriteEngine;

import aterm.*;
import aterm.pure.*;

import antlr.types.*;
//import tom.gom.adt.gom.types.*;

import java.io.*;

import utils.AST2ATerm;
//import utils.Tree2ATerm;

public class Gomantlr {

    %include { antlr/antlr.tom }
    //%include { tom/gom/adt/gom/Gom.tom}
    
    public static void main(String[] args) {
        try {
            String grammarFileName = args[0];
            
            FileReader fr = null;
            try {
                fr = new FileReader(grammarFileName);
            }
            catch (IOException ioe) {
                System.err.println("IO exception on file "+grammarFileName+": " + ioe);
            }
            
            BufferedReader br = new BufferedReader(fr);
            String tool_args[]= new String[1];
            tool_args[0]=grammarFileName;
            
            Tool tool=new Tool(tool_args);
            
            Grammar grammar=new Grammar(tool,grammarFileName,br);
            Grammar lexergrammar=new Grammar(grammar.getLexerGrammar());
            
            GrammarAST ast=grammar.getGrammarTree();
            GrammarAST lexerast=lexergrammar.getGrammarTree();
            
            ATerm astaterm=AST2ATerm.getATerm(ast,ANTLRParser._tokenNames);
            ATerm lexerastaterm=AST2ATerm.getATerm(lexerast,ANTLRParser._tokenNames);
            
            Antlr_grammar antlr_grammar=getGOMANTLRTree(astaterm);
            Antlr_grammar antlr_lexergrammar=getGOMANTLRTree(lexerastaterm);
            
            System.out.println(antlr_grammar);
            System.out.println(antlr_lexergrammar);
        } catch (Exception e) {
            System.err.println("exception: " + e);
            e.printStackTrace();
        }
    }
    
    %include { antlr.tom }

    private static Antlr_grammar getGOMANTLRTree(ATerm t) {
        %match(t) {
            COMBINED_GRAMMAR(_,(ID(NodeInfo(x,_,_),_),y*)) -> {
                Antlr_id id=`get_antlr_id(x);
                Antlr_comment comment=`get_antlr_comment(locate_antlr_comment(y));
                Antlr_options options=`get_antlr_options(locate_antlr_options(y));
                Antlr_tokens tokens=`get_antlr_tokens(locate_antlr_tokens(y));
                Antlr_scopes scopes=`get_antlr_scopes(locate_antlr_scopes(y));
                Antlr_actions actions=`get_antlr_actions(locate_antlr_actions(y));
                Antlr_rules rules=`get_antlr_rules(locate_antlr_rules(y));
                return `antlr_COMBINED_GRAMMAR(id,comment,options,tokens,scopes,actions,rules);
            }
            LEXER_GRAMMAR(_,(ID(NodeInfo(x,_,_),_),y*)) -> {
                Antlr_id id=`get_antlr_id(x);
                Antlr_comment comment=`get_antlr_comment(locate_antlr_comment(y));
                Antlr_options options=`get_antlr_options(locate_antlr_options(y));
                Antlr_tokens tokens=`get_antlr_tokens(locate_antlr_tokens(y));
                Antlr_scopes scopes=`get_antlr_scopes(locate_antlr_scopes(y));
                Antlr_actions actions=`get_antlr_actions(locate_antlr_actions(y));
                Antlr_rules rules=`get_antlr_rules(locate_antlr_rules(y));
                return `antlr_LEXER_GRAMMAR(id,comment,options,tokens,scopes,actions,rules);
            }
        }
        return null;
    }

    private static Antlr_id get_antlr_id(String s) {
        return `antlr_ID(s);
    }

    private static String locate_antlr_comment(ATermList t) {
        %match(ATermList t) {
            (_*,DOC_COMMENT(NodeInfo(comment,_,_),_),_*) -> {
                return `comment;
            }
        }
        return null;
    }

    private static Antlr_comment get_antlr_comment(String s) {
        if(s!=null) {
            return `antlr_comment(s);
        } else {
            return `antlr_nilcomment();
        }
    }

    private static ATermList locate_antlr_options(ATermList t) {
        %match(ATermList t) {
            (_*,BLOCK(_,(OPTIONS(_,x),_)),_*) -> {
                return `x;
            }
            (_*,OPTIONS(_,x),_*) -> {
                return `x;
            }
        }
        return `concATerm();
    }

    private static Antlr_options get_antlr_options(ATermList t) {
        %match(ATermList t) {
            (x,y*) -> {
                Antlr_options options=`get_antlr_options(y);
                if(`x.getChildCount()!=0) {
                    return `antlr_options(get_antlr_option(x),options*);
                } else {
                    return options;
                }
            }
        }
        return `antlr_options();
    }

    private static Antlr_option get_antlr_option(ATerm t) {
        %match(ATerm t) {
            ASSIGN(_,(ID(NodeInfo(name,_,_),_),ID(NodeInfo(src,_,_),_))) -> {
                return `antlr_assign_id(get_antlr_id(name),get_antlr_id(src));
            }
            ASSIGN(_,(ID(NodeInfo(name,_,_),_),INT(NodeInfo(value,_,_),_))) -> {
                return `antlr_assign_value(get_antlr_id(name),antlr_int(Integer.parseInt(value)));
            }
        }
        return `antlr_option(antlr_unrecognized(t));
    }

    private static ATermList locate_antlr_tokens(ATermList t) {
        %match(ATermList t) {
            (_*,TOKENS(_,x),_*) -> {
                return `x;
            }
        }
        return `concATerm();
    }

    private static Antlr_tokens get_antlr_tokens(ATermList t) {
        if(t!=null && t.getChildCount()!=0) {
            return `antlr_tokens(antlr_unrecognized(t));
        } else {
            return `antlr_niltokens();
        }
    }

    private static ATermList locate_antlr_scopes(ATermList t) {
        %match(ATermList t) {
            (x*,SCOPE(_,y),z*) -> {
                ATermList lx=`locate_antlr_scopes(x);
                ATermList lz=`locate_antlr_scopes(z);
                return `concATerm(lx*,y,lz*);
            }
        }
        return `concATerm();
    }

    private static Antlr_scopes get_antlr_scopes(ATermList t) {
        %match(ATermList t) {
            (x,y*) -> {
                Antlr_scopes scopes=`get_antlr_scopes(y);
                if(`x.getChildCount()!=0) {
                    return `antlr_scopes(antlr_scope(get_antlr_unrecognized(x)),scopes*);
                } else {
                    return scopes;
                }
            }
        }
        return `antlr_scopes();
    }

    private static ATermList locate_antlr_actions(ATermList t) {
        %match(ATermList t) {
            (x*,AMPERSAND(_,y),z*) -> {
                ATermList lx=`locate_antlr_actions(x);
                ATermList lz=`locate_antlr_actions(z);
                return `concATerm(lx*,y,lz*);
            }
        }
        return `concATerm();
    }

    private static Antlr_actions get_antlr_actions(ATermList t) {
        %match(ATermList t) {
            (x,y*) -> {
                %match(ATermList x) {
                    (ID(NodeInfo(name,_,_),_),ACTION(NodeInfo(action,_,_),_)) -> {
                        Antlr_actions actions=`get_antlr_actions(y);
                        return `antlr_actions(antlr_action(get_antlr_id(name),action),actions*);
                    }
                }
            }
        }
        return `antlr_actions();
    }

    private static ATermList locate_antlr_rules(ATermList t) {
        %match(ATermList t) {
            (x*,RULE(_,(y*,_)),z*) -> {
                ATermList lx=`locate_antlr_rules(x);
                ATermList lz=`locate_antlr_rules(z);
                return `concATerm(lx*,y,lz*);
            }
        }
        return `concATerm();
    }

    private static Antlr_rules get_antlr_rules(ATermList t) {
        %match(ATermList t) {
            (x,y*) -> {
                Antlr_rule rule=`get_antlr_rule((ATermList)x);
                if(rule!=null) {
                    Antlr_rules rules=`get_antlr_rules(y);
                    return `antlr_rules(rule,rules*);
                } else {
                    return `get_antlr_rules(y);
                }
            }
        }
        // we should not get here
        return `antlr_rules();
    }

    private static Antlr_rule get_antlr_rule(ATermList t) {
        //System.out.println("rule: "+t);
        %match(ATermList t) {
            (ID(NodeInfo(x,_,_),_),y*) -> {
                if(!`x.regionMatches(0,"synpred",0,7)) {
                    Antlr_id id=`get_antlr_id(x);
                    Antlr_modifier modifier=`get_antlr_modifier(locate_antlr_modifier(y));
                    Antlr_args args=`get_antlr_args(locate_antlr_args(y));
                    Antlr_ret ret=`get_antlr_ret(locate_antlr_ret(y));
                    Antlr_options options=`get_antlr_options(locate_antlr_options(y));
                    Antlr_scopes scopes=`get_antlr_scopes(locate_antlr_scopes(y));
                    Antlr_actions actions=`get_antlr_actions(locate_antlr_actions(y));
                    Antlr_element element=`get_antlr_orelement(locate_antlr_element(y));
                    Antlr_exceptions exceptions=`get_antlr_exceptions(locate_antlr_exceptions(y));
                    Antlr_rule resultat=`antlr_rule(id,modifier,args,ret,options,scopes,actions,element,exceptions);
                    System.out.println("rule: ("+id+","+element+")");
                    return resultat;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private static String locate_antlr_modifier(ATermList t) {
        %match(ATermList t) {
            (PROTECTED(_,_),_*) -> {
                return "protected";
            }
            (PUBLIC(_,_),_*) -> {
                return "public";
            }
            (PRIVATE(_,_),_*) -> {
                return "private";
            }
            (FRAGMENT(_,_),_*) -> {
                return "fragment";
            }
        }
        return null;
    }

    private static Antlr_modifier get_antlr_modifier(String s) {
        if(s!=null) {
            return `antlr_modifier(s);
        } else {
            return `antlr_nilmodifier();
        }
    }

    private static ATerm locate_antlr_args(ATermList t) {
        %match(ATermList t) {
            (_*,ARG(_,x),_*) -> {
                return `x;
            }
        }
        return null;
    }

    private static Antlr_args get_antlr_args(ATerm t) {
        if(t!=null && t.getChildCount()!=0) {
            return `antlr_args(antlr_unrecognized(t));
        } else {
            return `antlr_nilargs();
        }
    }

    private static ATerm locate_antlr_ret(ATermList t) {
        %match(ATermList t) {
            (_*,RET(_,x),_*) -> {
                return `x;
            }
        }
        return null;
    }

    private static Antlr_ret get_antlr_ret(ATerm t) {
        if(t!=null && t.getChildCount()!=0) {
            return `antlr_ret(antlr_unrecognized(t));
        } else {
            return `antlr_nilret();
        }
    }

    private static ATerm locate_antlr_exceptions(ATermList t) {
        %match(ATermList t) {
            (_*,EXCEPTION(_,x)) -> {
                return `x;
            }
        }
        return null;
    }

    private static Antlr_exceptions get_antlr_exceptions(ATerm t) {
        if(t!=null) {
            return `antlr_exceptions(antlr_unrecognized(t));
        } else {
            return `antlr_nilexceptions();
        }
    }

    private static ATermList locate_antlr_element(ATermList t) {
        %match(ATermList t) {
            (_*,BLOCK(_,(OPTIONS(_,_),x*,_)),_*) -> {
                return `x;
            }
            (_*,BLOCK(_,(x*,_)),_*) -> {
                return `x;
            }
        }
        return `concATerm();
    }

    private static Antlr_element inner_get_antlr_orelement(ATermList t) {
        Antlr_element e=`antlr_element(antlr_unrecognized(t));
        %match(ATermList t) {
            () -> {
                e=`antlr_or();
            }
            (x,y*) -> {
                Antlr_element element=`inner_get_antlr_orelement(y);
                e=`antlr_or(get_antlr_element(x),element*);
            }
        }
        return e;
    }

    private static Antlr_element get_antlr_orelement(ATermList t) {
        Antlr_element e=`antlr_element(antlr_unrecognized(t));
        %match(ATermList t) {
            () -> {
                e=`antlr_or();
            }
            (x,y*) -> {
                Antlr_element element=`inner_get_antlr_orelement(y);
                e=`antlr_or(get_antlr_element(x),element*);
            }
        }
        return normalize_element(e);
    }

    private static Antlr_element inner_get_antlr_andelement(ATermList t) {
        Antlr_element e=`antlr_element(antlr_unrecognized(t));
        %match(ATermList t) {
            () -> {
                e=`antlr_and();
            }
            (x,y*) -> {
                Antlr_element element=`inner_get_antlr_andelement(y);
                e=`antlr_and(get_antlr_element(x),element*);
            }
        }
        return e;
    }

    private static Antlr_element get_antlr_andelement(ATermList t) {
        Antlr_element e=`antlr_element(antlr_unrecognized(t));
        %match(ATermList t) {
            () -> {
                e=`antlr_and();
            }
            (x,y*) -> {
                Antlr_element element=`inner_get_antlr_andelement(y);
                e=`antlr_and(get_antlr_element(x),element*);
            }
        }
        return normalize_element(e);
    }

    private static Antlr_element normalize_element(Antlr_element e) {
        Antlr_element e1=e;
        %match(e) {
            antlr_and(antlr_sempred(_),y*) -> {
                e1=`antlr_and(y*);
            }
            antlr_or(antlr_sempred(_),y*) -> {
                e1=`antlr_or(y*);
            }
        }
        %match(e1) {
            antlr_and(x) -> {
                return `x;
            }
            antlr_or(x) -> {
                return `x;
            }
        }
        return e1;
    }

    private static Antlr_element get_antlr_element(ATerm t) {
        %match(ATerm t) {
            ALT(_,(x*,_)) -> {
                return `get_antlr_andelement(x);
            }
            BLOCK(_,(x*,_)) -> {
                return `get_antlr_orelement(x);
            }
            SET(_,(x*)) -> {
                return `get_antlr_orelement(x);
            }
            CLOSURE(_,(x*)) -> {
                Antlr_options options=`get_antlr_options(locate_antlr_options(x));
                Antlr_element element=`get_antlr_orelement(locate_antlr_element(x));
                return `antlr_closure(options,element);
            }
            POSITIVE_CLOSURE(_,(x*)) -> {
                Antlr_options options=`get_antlr_options(locate_antlr_options(x));
                Antlr_element element=`get_antlr_orelement(locate_antlr_element(x));
                return `antlr_positiveclosure(options,element);
            }
            OPTIONAL(_,(x*)) -> {
                Antlr_options options=`get_antlr_options(locate_antlr_options(x));
                Antlr_element element=`get_antlr_orelement(locate_antlr_element(x));
                return `antlr_optional(options,element);
            }
            OPTIONS(_,x) -> {
                return `antlr_eltoptions(antlr_unrecognized(x));
            }
            NOT(_,(x*)) -> {
                return `antlr_not(get_antlr_orelement(x));
            }
            CHAR_RANGE(_,(CHAR_LITERAL(NodeInfo(x,_,_),_),CHAR_LITERAL(NodeInfo(y,_,_),_))) -> {
                return `antlr_charrange(x,y);
            }
            WILDCARD(_,_) -> {
                return `antlr_wildcard();
            }
            SYN_SEMPRED(NodeInfo(x,_,_),_) -> {
                return `antlr_sempred(x);
            }
            RULE_REF(NodeInfo(x,_,_),_) -> {
                return `antlr_ruleref(x);
            }
            TOKEN_REF(NodeInfo(x,_,_),_) -> {
                return `antlr_token(x);
            }
            STRING_LITERAL(NodeInfo(x,_,_),_) -> {
                return `antlr_string(x);
            }
            CHAR_LITERAL(NodeInfo(x,_,_),_) -> {
                return `antlr_char(x);
            }
            ACTION(NodeInfo(y,_,_),_) -> {
                return `antlr_eltaction(y);
            }
            EPSILON(_,_) -> {
                return `antlr_epsilon();
            }
            _ -> {
                return `antlr_element(antlr_unrecognized(t));
            }
        }
        return null;
    }

    private static Antlr_unrecognized get_antlr_unrecognized(ATerm t) {
        return `antlr_unrecognized(t);
    }
}
