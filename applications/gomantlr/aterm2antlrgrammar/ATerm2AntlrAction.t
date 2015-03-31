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

import antlrgrammar.antlrcommons.types.AntlrAction;
import antlrgrammar.antlrcommons.types.AntlrId;
import antlrgrammar.antlrcommons.types.AntlrString;
import antlrgrammar.antlrcommons.types.AntlrWrong;

import aterm2antlrgrammar.exceptions.AntlrWrongActionException;
import aterm2antlrgrammar.exceptions.AntlrWrongIdException;

public class ATerm2AntlrAction {
   
    %include { ../antlrgrammar/AntlrGrammar.tom }

    private static class Container {
        public AntlrId id=null;
        public AntlrId scope=null;
        public String action=null;

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
     * Action: "@" Id? Id String
     *
     * Id? is scope, Id is really the id.
     *
     */

    public static AntlrAction getAntlrAction(ATerm t) throws AntlrWrongActionException {
        %match(t) {
            AMPERSAND(_,x) -> {
                Container container=new Container();
                parseArgs(`x,container);
                if(container.goodParse) {
		    if(container.scope!=null) {
			return `AntlrScopedAction(container.id,container.scope,container.action);
		    } else {
			return `AntlrGlobalAction(container.id,container.action);
		    }
                } else {
                    if(container.wrong!=null) {
                        if(container.scope!=null) {
                            throw new AntlrWrongActionException(
                                `AntlrIncorrectScopedAction(
                                    container.id,
                                    container.scope,
                                    String2AntlrString.getAntlrString(container.action),
                                    container.wrong));
                        }
                        else {
                            throw new AntlrWrongActionException(
                                `AntlrIncorrectGlobalAction(
                                    container.id,
                                    String2AntlrString.getAntlrString(container.action),
                                    container.wrong));
                        }
                    } else {
                        if(container.scope!=null) {
                            throw new AntlrWrongActionException(
                                `AntlrIncorrectScopedActionArgs(
                                    container.id,
                                    container.scope,
                                    String2AntlrString.getAntlrString(container.action)));
                        } else {
                            throw new AntlrWrongActionException(
                                `AntlrIncorrectGlobalActionArgs(
                                    container.id,
                                    String2AntlrString.getAntlrString(container.action)));
                        }
                    }
                }
            }
        }
        throw new AntlrWrongActionException(`AntlrPlainWrongAction(ATerm2AntlrWrong.getAntlrWrong(t)));
    }

    private static void parseArgs(ATermList l,Container container) {
        %match(l) {
            // id or scope, we'll know later.
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

    private static void parseArgs2(ATermList l,Container container) {
        %match(l) {
            // Was indeed id.
            concATerm(x@ACTION(NodeInfo(action,_,_),_),y*) -> {
                container.action=`action;
                parseArgs4(`y,container);
                return;
            }
            // Was scope, move it.
            concATerm(x@ID[],y*) -> {
                container.scope=container.id;
                try {
                    container.id=ATerm2AntlrId.getAntlrId(`x);
                } catch (AntlrWrongIdException e) {
                    container.goodParse=false;
                    container.id=e.getAntlrId();
                }
                parseArgs3(`y,container);
                return;
            }
            _ -> {
                container.goodParse=false;
                parseArgs4(l,container);
            }
        }
    }

    private static void parseArgs3(ATermList l,Container container) {
        %match(l) {
            concATerm(x@ACTION(NodeInfo(action,_,_),_),y*) -> {
                container.action=`action;
                parseArgs4(`y,container);
                return;
            }
            _ -> {
                container.goodParse=false;
                parseArgs4(l,container);
            }
        }
    }

    private static void parseArgs4(ATermList l,Container container) {
        %match(l) {
            concATerm(x,y*) -> {
                container.goodParse=false;
                container.wrong=ATerm2AntlrWrong.getAntlrWrong(l);
            }
        }
    }
}
