/*
 *
 * Gomantlr
 *
 * Copyright (c) 2006-2014, Universite de Lorraine, Inria
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

import antlrgrammar.antlrelement.types.AntlrElement;
import antlrgrammar.antlrcommons.types.AntlrOptions;
import antlrgrammar.antlrcommons.types.AntlrActions;
import antlrgrammar.antlrcommons.types.AntlrAction;

import aterm2antlrgrammar.exceptions.AntlrWrongElementException;

import aterm2antlrgrammar.exceptions.AntlrWrongOptionsException;
import aterm2antlrgrammar.exceptions.AntlrWrongActionException;

public class ATerm2AntlrBlock {
   
    %include { ../antlrgrammar/AntlrGrammar.tom }

    private static class Container {
        public AntlrOptions options=null;
        public AntlrActions actions=null;
        public AntlrElement orElement=`AntlrOrElement();

        public boolean goodParse=true;
    }

    %include { ../antlr.tom }

    /*
     * This is the grammar corresponding to the AST,
     * which explains what is done here.
     *
     * The actual grammar is a bit different
     * but is not what matters here.
     *
     * Block: Options? Action* Element ('|' Element)* EOB
     *
     * The "Element ('|' Element)*" part gives an OrElement
     * When there's no Options nor Action, we just return the OrElement
     *
     */

    public static AntlrElement getAntlrBlock(ATerm t) throws AntlrWrongElementException {
        %match(t) {
            BLOCK(_,l) -> {
                Container container=new Container();
                parseArgs(`l,container);
                if(container.options==null && container.actions==null) {
                    return container.orElement;
                } else {
                    if(container.options==null) {
                        container.options=`AntlrOptions();
                    }
                    if(container.actions==null) {
                        container.actions=`AntlrActions();
                    }
                    if(container.goodParse) {
                        return `AntlrBlock(
                            container.options,
                            container.actions,
                            container.orElement);
                    } else {
                        throw new AntlrWrongElementException(
                            `AntlrIncorrectBlock(
                                container.options,
                                container.actions,
                                container.orElement));
                    }
                }
            }
        }
        
        throw new AntlrWrongElementException(`AntlrPlainWrongBlock(ATerm2AntlrWrong.getAntlrWrong(t)));
    }

    /*
     *
     * Options?
     *
     */

    private static void parseArgs(ATermList l,Container container) {
        %match(l) {
            concATerm(x@OPTIONS[],y*) -> {
                try {
                    container.options=ATerm2AntlrOptions.getAntlrOptions(`x);
                } catch (AntlrWrongOptionsException e) {
                    container.goodParse=false;
                    container.options=e.getAntlrOptions();
                }
                parseArgs2(`y,container);
                return;
            }
            _ -> {
                parseArgs2(l,container);           
            }
        }
    }
    
    /*
     *
     * Action*
     *
     */

    private static void parseArgs2(ATermList l,Container container) {
        %match(l) {
            concATerm(x@AMPERSAND[],y*) -> {
                AntlrActions actions=container.actions;
                if (actions==null) {
                    actions=`AntlrActions();
                }
                AntlrAction action;
                try {
                    action=ATerm2AntlrAction.getAntlrAction(`x);
                } catch (AntlrWrongActionException e) {
                    container.goodParse=false;
                    action=e.getAntlrAction();
                    
                }
                container.actions=`AntlrActions(actions*,action);
                parseArgs2(`y*,container);
                return;
            }
            _ -> {
                parseArgs3(l,container);
            }
        }
    }

    /*
     *
     * Element ('|' Element)*
     *
     * The "Element ('|' Element)*" part gives an OrElement
     *
     * We build an BLOCK with no option not action
     * and get it parsed by ATerm2AntlrOrElement
     *
     */

    private static void parseArgs3(ATermList l,Container container) {
        try {
            container.orElement=ATerm2AntlrOrElement.getAntlrOrElement(`BLOCK(NodeInfo("BLOCK",0,0),l));
        } catch (AntlrWrongElementException e) {
            container.goodParse=false;
            container.orElement=e.getAntlrElement();
        }
    }
}
