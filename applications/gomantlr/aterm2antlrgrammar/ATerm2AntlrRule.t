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
import antlrgrammar.antlrelement.types.*;

import aterm2antlrgrammar.*;
import aterm2antlrgrammar.exceptions.*;

public class ATerm2AntlrRule {
   
    %include { ../antlrgrammar/AntlrGrammar.tom }

    private static class Container {
        public AntlrId id=null;
        public AntlrModifier modifier=`AntlrNilModifier();
        public AntlrArgs args=`AntlrNilArgs();
        public AntlrRet ret=`AntlrNilRet();
        public AntlrOptions options=`AntlrOptions();
        public AntlrScopes scopes=`AntlrScopes();
        public AntlrActions actions=`AntlrActions();
        public AntlrElement element=null;
        public AntlrExceptions exceptions=`AntlrExceptions();

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
     * Rule: Id Modifier? Args? Ret? Options? Scope* Action* Element Exception* Eor
     *
     */

    public static AntlrRule getAntlrRule(ATerm t) throws AntlrWrongRuleException {
        %match(t) {
            RULE(_,l) -> {
                Container container=new Container();
                parseArgs(`l,container);
                if(container.goodParse) {
                    return `AntlrRule(
                        container.id,
                        container.modifier,
                        container.args,
                        container.ret,
                        container.options,
                        container.scopes, 
                        container.actions,
                        container.element,
                        container.exceptions);
                } else {
                    if(container.wrong!=null) {
                        throw new AntlrWrongRuleException(
                            `AntlrIncorrectRule(
                                container.id,
                                container.modifier,
                                container.args,
                                container.ret,
                                container.options,
                                container.scopes, 
                                container.actions,
                                container.element,
                                container.exceptions,
                                container.wrong));
                    } else {
                        throw new AntlrWrongRuleException(
                            `AntlrIncorrectRuleArgs(
                                container.id,
                                container.modifier,
                                container.args,
                                container.ret,
                                container.options,
                                container.scopes, 
                                container.actions,
                                container.element,
                                container.exceptions));
                    }
                }
            }
        }
        throw new AntlrWrongRuleException(`AntlrPlainWrongRule(ATerm2AntlrWrong.getAntlrWrong(t)));
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
     * Something odd in antlr.g here, hope it's OK.
     *
     * Modifier?
     *
     * Modifier: "public" | "protected" | "private" | "fragment"
     *
     */

    private static void parseArgs2(ATermList l,Container container) {
        %match(l) { 
            concATerm(PUBLIC[],x*) -> {
                container.modifier=`AntlrModifier("public");
                parseArgs3(`x,container);
                return;
            }
            concATerm(PROTECTED[],x*) -> {
                container.modifier=`AntlrModifier("protected");
                parseArgs3(`x,container);
                return;
            }
            concATerm(PRIVATE[],x*) -> {
                container.modifier=`AntlrModifier("private");
                parseArgs3(`x,container);
                return;
            }
            concATerm(FRAGMENT[],x*) -> {
                container.modifier=`AntlrModifier("fragment");
                parseArgs3(`x,container);
                return;
            }
        }
        parseArgs3(l,container);
    }

    /*
     *
     * Args?
     *
     */

    private static void parseArgs3(ATermList l,Container container) {
        %match(l) { 
            concATerm(x@ARGS[],y*) -> {
                try {
                    container.args=ATerm2AntlrArgs.getAntlrArgs(`x);
                } catch (AntlrWrongArgsException e) {
                    container.goodParse=false;
                    container.args=e.getAntlrArgs();
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
     * Ret?
     *
     */

    private static void parseArgs4(ATermList l,Container container) {
        %match(l) { 
            concATerm(x@RET[],y*) -> {
                try {
                    container.ret=ATerm2AntlrRet.getAntlrRet(`x);
                } catch (AntlrWrongRetException e) {
                    container.goodParse=false;
                    container.ret=e.getAntlrRet();
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
     * Options?
     *
     */

    private static void parseArgs5(ATermList l,Container container) {
        %match(l) {
            concATerm(x@OPTIONS[],y*) -> {
                try {
                    container.options=ATerm2AntlrOptions.getAntlrOptions(`x);
                } catch (AntlrWrongOptionsException e) {
                    container.goodParse=false;
                    container.options=e.getAntlrOptions();
                }
                parseArgs6(`y,container);
                return;
            }
            _ -> {
                parseArgs6(l,container);           
            }
        }
    }
    
    /*
     *
     * Scope*
     *
     */
    
    private static void parseArgs6(ATermList l,Container container) {
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
                parseArgs6(`y,container);
                return;
            }
	    _ -> {
		parseArgs7(l,container);
	    }	
	}	
    }

    /*
     *
     * Action*
     *
     */

    private static void parseArgs7(ATermList l,Container container) {
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
                parseArgs7(`y*,container);
                return;
            }
            _ -> {
                parseArgs8(l,container);
            }
        }
    }

    /*
     *
     * Element
     *
     */

    private static void parseArgs8(ATermList l,Container container) {
        %match(l) {
            // The element is always a block in the AST.
            concATerm(x@BLOCK[],y*) -> {
                try {
                    container.element=ATerm2AntlrBlock.getAntlrBlock(`x);
                } catch (AntlrWrongElementException e) {
                    container.goodParse=false;
                    container.element=e.getAntlrElement();
                }
                parseArgs9(`y,container);
                return;
            }
            _ -> {
                container.goodParse=false;
                try {
                    container.element=ATerm2AntlrElement.getAntlrElement(`concATerm());
                } catch (AntlrWrongElementException e) {
                    container.element=e.getAntlrElement();
                }
                parseArgs9(l,container);
            }
        }
    }

    /*
     *
     * Exception*
     *
     */

    private static void parseArgs9(ATermList l,Container container) {
        %match(l) {
            concATerm(x@EXCEPTION[],y*) -> {
                AntlrExceptions exceptions=container.exceptions;
                AntlrException exception;
                try {
                    exception=ATerm2AntlrException.getAntlrException(`x);
                } catch (AntlrWrongExceptionException e) {
                    container.goodParse=false;
                    exception=e.getAntlrException();
                    
                }
                container.exceptions=`AntlrExceptions(exceptions*,exception);
                parseArgs9(`y,container);
                return;
            }
        }
        parseArgs10(l,container);
    }

    /*
     *
     * Eor
     *
     */

    private static void parseArgs10(ATermList l,Container container) {
        %match(l) {
            concATerm(EOR[]) -> {
                return;
            }
            _ -> {
                container.goodParse=false;
                container.wrong=ATerm2AntlrWrong.getAntlrWrong(l);
            }
        }
    }
}
