/*
 *
 * Gomantlr
 *
 * Copyright (c) 2006-2007, INRIA
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

import antlrgrammar.types.AntlrGrammar;
//import tom.gom.adt.gom.types.*;

import java.io.*;

import utils.AST2ATerm;
//import utils.Tree2ATerm;

import gomantlr.ATerm2AntlrGrammar;

import gomantlr.exceptions.AntlrWrongGrammarException;

public class Gomantlr {

    %include { antlrgrammar/AntlrGrammar.tom }
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
            String toolArgs[]= new String[1];
            toolArgs[0]=grammarFileName;
            
            Tool tool=new Tool(toolArgs);
            
            Grammar grammar=new Grammar(tool,grammarFileName,br);
            Grammar lexerGrammar=new Grammar(grammar.getLexerGrammar());
            
            GrammarAST ast=grammar.getGrammarTree();
            GrammarAST lexerAST=lexerGrammar.getGrammarTree();
            
            ATerm aterm=AST2ATerm.getATerm(ast,ANTLRParser._tokenNames);
            ATerm lexerATerm=AST2ATerm.getATerm(lexerAST,ANTLRParser._tokenNames);

            System.out.println(aterm);
            System.out.println(lexerATerm);
            
            AntlrGrammar antlrGrammar;
            AntlrGrammar antlrLexerGrammar;
            try {
                antlrGrammar=ATerm2AntlrGrammar.getAntlrGrammar(aterm);
            } catch (AntlrWrongGrammarException e) {
                antlrGrammar=e.getAntlrGrammar();
            }
            try {
                antlrLexerGrammar=ATerm2AntlrGrammar.getAntlrGrammar(lexerATerm);
            } catch (AntlrWrongGrammarException e) {
                antlrLexerGrammar=e.getAntlrGrammar();
            }
            
            System.out.println(antlrGrammar);
            System.out.println(antlrLexerGrammar);
        } catch (Exception e) {
            System.err.println("exception: " + e);
            e.printStackTrace();
        }
    }
    
    /*

    %include { antlr.tom }

    private static AntlrGrammar getGOMANTLRTree(ATerm t) {
        %match(t) {
            COMBINED_GRAMMAR(_,(ID(NodeInfo(x,_,_),_),y*)) -> {
                AntlrId id=`getAntlrId(x);
                AntlrComment comment=`getAntlrComment(locateAntlrComment(y));
                AntlrOptions options=`getAntlrOptions(locateAntlrOptions(y));
                AntlrTokens tokens=`getAntlrTokens(locateAntlrTokens(y));
                AntlrScopes scopes=`getAntlrScopes(locateAntlrScopes(y));
                AntlrActions actions=`getAntlrActions(locateAntlrActions(y));
                AntlrRules rules=`getAntlrRules(locateAntlrRules(y));
                return `antlrCombinedGrammar(id,comment,options,tokens,scopes,actions,rules);
            }
            LEXER_GRAMMAR(_,(ID(NodeInfo(x,_,_),_),y*)) -> {
                AntlrId id=`getAntlrId(x);
                AntlrComment comment=`getAntlrComment(locateAntlrComment(y));
                AntlrOptions options=`getAntlrOptions(locateAntlrOptions(y));
                AntlrTokens tokens=`getAntlrTokens(locateAntlrTokens(y));
                AntlrScopes scopes=`getAntlrScopes(locateAntlrScopes(y));
                AntlrActions actions=`getAntlrActions(locateAntlrActions(y));
                AntlrRules rules=`getAntlrRules(locateAntlrRules(y));
                return `antlrLexerGrammar(id,comment,options,tokens,scopes,actions,rules);
            }
        }
        return `antlrWrongGrammar(getAntlrUnrecognized(t));
    }

    private static AntlrId getAntlrId(String s) {
        return `antlrId(s);
    }

    private static String locateAntlrComment(ATermList t) {
        %match(ATermList t) {
            (_*,DOC_COMMENT(NodeInfo(comment,_,_),_),_*) -> {
                return `comment;
            }
        }
        return null;
    }

    private static AntlrComment getAntlrComment(String s) {
        if(s!=null) {
            return `antlrComment(s);
        } else {
            return `antlrNilComment();
        }
    }

    private static ATermList locateAntlrOptions(ATermList t) {
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

    private static AntlrOptions getAntlrOptions(ATermList t) {
        %match(ATermList t) {
            (x,y*) -> {
                AntlrOptions options=`getAntlrOptions(y);
                if(`x.getChildCount()!=0) {
                    return `antlrOptions(getAntlrOption(x),options*);
                } else {
                    return options;
                }
            }
        }
        return `antlrOptions();
    }

    private static AntlrOption getAntlrOption(ATerm t) {
        %match(ATerm t) {
            ASSIGN(_,(ID(NodeInfo(name,_,_),_),ID(NodeInfo(src,_,_),_))) -> {
                return `antlrAssignId(getAntlrId(name),getAntlrId(src));
            }
            ASSIGN(_,(ID(NodeInfo(name,_,_),_),INT(NodeInfo(value,_,_),_))) -> {
                return `antlrAssignValue(getAntlrId(name),antlrInt(Integer.parseInt(value)));
            }
        }
        return `antlrOption(antlrUnrecognized(t));
    }

    private static ATermList locateAntlrTokens(ATermList t) {
        %match(ATermList t) {
            (_*,TOKENS(_,x),_*) -> {
                return `x;
            }
        }
        return `concATerm();
    }

    private static AntlrTokens getAntlrTokens(ATermList t) {
        if(t!=null && t.getChildCount()!=0) {
            return `antlrTokens(antlrUnrecognized(t));
        } else {
            return `antlrNilTokens();
        }
    }

    private static ATermList locateAntlrScopes(ATermList t) {
        %match(ATermList t) {
            (x*,SCOPES(_,y),z*) -> {
                ATermList lx=`locateAntlrScopes(x);
                ATermList lz=`locateAntlrScopes(z);
                return `concATerm(lx*,y,lz*);
            }
        }
        return `concATerm();
    }

    private static AntlrScopes getAntlrScopes(ATermList t) {
        %match(ATermList t) {
            (x,y*) -> {
                AntlrScopes scopes=`getAntlrScopes(y);
                if(`x.getChildCount()!=0) {
                    return `antlrScopes(antlrScope(getAntlrUnrecognized(x)),scopes*);
                } else {
                    return scopes;
                }
            }
        }
        return `antlrScopes();
    }

    private static ATermList locateAntlrActions(ATermList t) {
        %match(ATermList t) {
            (x*,AMPERSAND(_,y),z*) -> {
                ATermList lx=`locateAntlrActions(x);
                ATermList lz=`locateAntlrActions(z);
                return `concATerm(lx*,y,lz*);
            }
        }
        return `concATerm();
    }

    private static AntlrActions getAntlrActions(ATermList t) {
        %match(ATermList t) {
            (x,y*) -> {
                %match(ATermList x) {
                    (ID(NodeInfo(name,_,_),_),ACTION(NodeInfo(action,_,_),_)) -> {
                        AntlrActions actions=`getAntlrActions(y);
                        return `antlrActions(antlrAction(getAntlrId(name),action),actions*);
                    }
                }
            }
        }
        return `antlrActions();
    }

    private static ATermList locateAntlrRules(ATermList t) {
        %match(ATermList t) {
            (x*,RULE(_,(y*,_)),z*) -> {
                ATermList lx=`locateAntlrRules(x);
                ATermList lz=`locateAntlrRules(z);
                return `concATerm(lx*,y,lz*);
            }
        }
        return `concATerm();
    }

    private static AntlrRules getAntlrRules(ATermList t) {
        %match(ATermList t) {
            (x,y*) -> {
                AntlrRule rule=`getAntlrRule((ATermList)x);
                if(rule!=null) {
                    AntlrRules rules=`getAntlrRules(y);
                    return `antlrRules(rule,rules*);
                } else {
                    return `getAntlrRules(y);
                }
            }
        }
        // we should not get here
        return `antlrRules();
    }

    private static AntlrRule getAntlrRule(ATermList t) {
        //System.out.println("rule: "+t);
        %match(ATermList t) {
            (ID(NodeInfo(x,_,_),_),y*) -> {
                if(!`x.regionMatches(0,"synpred",0,7)) {
                    AntlrId id=`getAntlrId(x);
                    AntlrModifier modifier=`getAntlrModifier(locateAntlrModifier(y));
                    AntlrArgs args=`getAntlrArgs(locateAntlrArgs(y));
                    AntlrRet ret=`getAntlrRet(locateAntlrRet(y));
                    AntlrOptions options=`getAntlrOptions(locateAntlrOptions(y));
                    AntlrScopes scopes=`getAntlrScopes(locateAntlrScopes(y));
                    AntlrActions actions=`getAntlrActions(locateAntlrActions(y));
                    AntlrElement element=`getAntlrOrElement(locateAntlrElement(y));
                    AntlrExceptions exceptions=`getAntlrExceptions(locateAntlrExceptions(y));
                    AntlrRule resultat=`antlrRule(id,modifier,args,ret,options,scopes,actions,element,exceptions);
                    System.out.println("rule: ("+id+","+element+")");
                    return resultat;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private static String locateAntlrModifier(ATermList t) {
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

    private static AntlrModifier getAntlrModifier(String s) {
        if(s!=null) {
            return `antlrModifier(s);
        } else {
            return `antlrNilModifier();
        }
    }

    private static ATerm locateAntlrArgs(ATermList t) {
        %match(ATermList t) {
            (_*,ARG(_,x),_*) -> {
                return `x;
            }
        }
        return null;
    }

    private static AntlrArgs getAntlrArgs(ATerm t) {
        if(t!=null && t.getChildCount()!=0) {
            return `antlrArgs(antlrUnrecognized(t));
        } else {
            return `antlrNilArgs();
        }
    }

    private static ATerm locateAntlrRet(ATermList t) {
        %match(ATermList t) {
            (_*,RET(_,x),_*) -> {
                return `x;
            }
        }
        return null;
    }

    private static AntlrRet getAntlrRet(ATerm t) {
        if(t!=null && t.getChildCount()!=0) {
            return `antlrRet(antlrUnrecognized(t));
        } else {
            return `antlrNilRet();
        }
    }

    private static ATerm locateAntlrExceptions(ATermList t) {
        %match(ATermList t) {
            (_*,EXCEPTION(_,x)) -> {
                return `x;
            }
        }
        return null;
    }

    private static AntlrExceptions getAntlrExceptions(ATerm t) {
        if(t!=null) {
            return `antlrExceptions(antlrUnrecognized(t));
        } else {
            return `antlrNilExceptions();
        }
    }

    private static ATermList locateAntlrElement(ATermList t) {
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

    private static AntlrElement inner_getAntlrOrElement(ATermList t) {
        AntlrElement e=`antlrElement(antlrUnrecognized(t));
        %match(ATermList t) {
            () -> {
                e=`antlrOr();
            }
            (x,y*) -> {
                AntlrElement element=`inner_getAntlrOrElement(y);
                e=`antlrOr(getAntlrElement(x),element*);
            }
        }
        return e;
    }

    private static AntlrElement getAntlrOrElement(ATermList t) {
        AntlrElement e=`antlrElement(antlrUnrecognized(t));
        %match(ATermList t) {
            () -> {
                e=`antlrOr();
            }
            (x,y*) -> {
                AntlrElement element=`inner_getAntlrOrElement(y);
                e=`antlrOr(getAntlrElement(x),element*);
            }
        }
        return normalizeElement(e);
    }

    private static AntlrElement inner_getAntlrAndElement(ATermList t) {
        AntlrElement e=`antlrElement(antlrUnrecognized(t));
        %match(ATermList t) {
            () -> {
                e=`antlrAnd();
            }
            (x,y*) -> {
                AntlrElement element=`inner_getAntlrAndElement(y);
                e=`antlrAnd(getAntlrElement(x),element*);
            }
        }
        return e;
    }

    private static AntlrElement getAntlrAndElement(ATermList t) {
        AntlrElement e=`antlrElement(antlrUnrecognized(t));
        %match(ATermList t) {
            () -> {
                e=`antlrAnd();
            }
            (x,y*) -> {
                AntlrElement element=`inner_getAntlrAndElement(y);
                e=`antlrAnd(getAntlrElement(x),element*);
            }
        }
        return normalizeElement(e);
    }

    private static AntlrElement normalizeElement(AntlrElement e) {
        AntlrElement e1=e;
        %match(e) {
            antlrAnd(antlrSempred(_),y*) -> {
                e1=`antlrAnd(y*);
            }
            antlrOr(antlrSempred(_),y*) -> {
                e1=`antlrOr(y*);
            }
        }
        %match(e1) {
            antlrAnd(x) -> {
                return `x;
            }
            antlrOr(x) -> {
                return `x;
            }
        }
        return e1;
    }

    private static AntlrElement getAntlrElement(ATerm t) {
        %match(ATerm t) {
            ALT(_,(x*,_)) -> {
                return `getAntlrAndElement(x);
            }
            BLOCK(_,(x*,_)) -> {
                return `getAntlrOrElement(x);
            }
            SET(_,(x*)) -> {
                return `getAntlrOrElement(x);
            }
            CLOSURE(_,(x*)) -> {
                AntlrOptions options=`getAntlrOptions(locateAntlrOptions(x));
                AntlrElement element=`getAntlrOrElement(locateAntlrElement(x));
                return `antlrClosure(options,element);
            }
            POSITIVE_CLOSURE(_,(x*)) -> {
                AntlrOptions options=`getAntlrOptions(locateAntlrOptions(x));
                AntlrElement element=`getAntlrOrElement(locateAntlrElement(x));
                return `antlrPositiveClosure(options,element);
            }
            OPTIONAL(_,(x*)) -> {
                AntlrOptions options=`getAntlrOptions(locateAntlrOptions(x));
                AntlrElement element=`getAntlrOrElement(locateAntlrElement(x));
                return `antlrOptional(options,element);
            }
            OPTIONS(_,x) -> {
                return `antlrEltOptions(antlrUnrecognized(x));
            }
            NOT(_,(x*)) -> {
                return `antlrNot(getAntlrOrElement(x));
            }
            CHAR_RANGE(_,(CHAR_LITERAL(NodeInfo(x,_,_),_),CHAR_LITERAL(NodeInfo(y,_,_),_))) -> {
                return `antlrCharRange(x,y);
            }
            WILDCARD(_,_) -> {
                return `antlrWildcard();
            }
            SYN_SEMPRED(NodeInfo(x,_,_),_) -> {
                return `antlrSempred(x);
            }
            RULE_REF(NodeInfo(x,_,_),_) -> {
                return `antlrRuleRef(x);
            }
            TOKEN_REF(NodeInfo(x,_,_),_) -> {
                return `antlrToken(x);
            }
            STRING_LITERAL(NodeInfo(x,_,_),_) -> {
                return `antlrString(x);
            }
            CHAR_LITERAL(NodeInfo(x,_,_),_) -> {
                return `antlrChar(x);
            }
            ACTION(NodeInfo(y,_,_),_) -> {
                return `antlrEltAction(y);
            }
            EPSILON(_,_) -> {
                return `antlrEpsilon();
            }
            _ -> {
                return `antlrElement(antlrUnrecognized(t));
            }
        }
        return null;
    }

    private static AntlrUnrecognized getAntlrUnrecognized(ATerm t) {
        return `antlrUnrecognized(t);
    }
    */
}
