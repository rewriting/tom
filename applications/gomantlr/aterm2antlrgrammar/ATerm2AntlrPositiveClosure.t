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
import antlrgrammar.antlrcommons.types.AntlrWrong;

import aterm2antlrgrammar.exceptions.AntlrWrongElementException;

public class ATerm2AntlrPositiveClosure {
   
    %include { ../antlrgrammar/AntlrGrammar.tom }

    private static class Container {
        public AntlrElement element=null;

        public boolean goodParse=true;
        public AntlrWrong wrong=null;
    }

    %include { ../antlr.tom }

    /*
     *
     * PositiveClosure: Element '+'^
     *
     */

    public static AntlrElement getAntlrPositiveClosure(ATerm t) throws AntlrWrongElementException {
        %match(t) {
            POSITIVE_CLOSURE(_,l) -> {
                Container container=new Container();
                parseArgs(`l,container);
                if(container.goodParse) {
                    return `AntlrPositiveClosure(container.element);
                } else {
                    if(container.wrong!=null) {
                        throw new AntlrWrongElementException(
                            `AntlrIncorrectPositiveClosure(container.element,container.wrong));
                    } else {
                        throw new AntlrWrongElementException(
                            `AntlrIncorrectPositiveClosureArg(container.element));
                    }
                }
            }
        }
        
        throw new AntlrWrongElementException(`AntlrPlainWrongPositiveClosure(ATerm2AntlrWrong.getAntlrWrong(t)));
    }
    
    /*
     *
     * Element
     *
     */

    private static void parseArgs(ATermList l,Container container) {
        %match(l) {
            // The element is always a block in the AST.
            concATerm(x@BLOCK[]) -> {
                try {
                    container.element=ATerm2AntlrBlock.getAntlrBlock(`x);
                } catch (AntlrWrongElementException e) {
                    container.goodParse=false;
                    container.element=e.getAntlrElement();
                }
                return;
            }
            _ -> {
                container.goodParse=false;
                container.wrong=ATerm2AntlrWrong.getAntlrWrong(l);
            }
        }
    }
}
