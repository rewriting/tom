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
import antlrgrammar.antlrelement.types.antlrelement.AntlrAndElement;
import antlrgrammar.antlrelement.types.antlrwrongelement.AntlrIncorrectAndElement;

import aterm2antlrgrammar.exceptions.AntlrWrongElementException;

public class ATerm2AntlrAndElement {
   
    %include { ../antlrgrammar/AntlrGrammar.tom }

    private static class Container {
        public AntlrElement element=`AntlrAndElement();

        public boolean goodParse=true;
    }

    %include { ../antlr.tom }

    /*
     *
     * AndElement: Element+
     *
     * When we only have one Element, we just return it.
     *
     */

    public static AntlrElement getAntlrAndElement(ATerm t) throws AntlrWrongElementException {
        %match(t) {
            ALT(_,l) -> {
                Container container=new Container();
                parseArgs(`l,container);
                AntlrElement element=container.element;
                if(container.goodParse) {
                    // Here, element *must* be some AntlrAndElement().
                    %match(element) {
                        AntlrAndElement() -> {
                            throw new AntlrWrongElementException(`AntlrAndElementNoElement(ATerm2AntlrWrong.getAntlrWrong(t)));
                        }
                        AntlrAndElement(x) -> {
                            return `x;
                        }
                    }
                    // java is unhappy with "_ ->"
                    return element;
                } else {
                    // would be great to write `AntlrIncorrectAndElement(element)
                    throw new AntlrWrongElementException(AntlrIncorrectAndElement.fromArray(((AntlrAndElement)element).toArray()));
                }
            }
        }

        throw new AntlrWrongElementException(`AntlrPlainWrongAndElement(ATerm2AntlrWrong.getAntlrWrong(t)));
    }

    private static void parseArgs(ATermList l,Container container) {
        %match(l) {
            concATerm(EOA[]) -> {
                return;
            }
            concATerm(x,y*) -> {
                AntlrElement element=container.element;
                AntlrElement element2;
                %match(x) {
                    // Sempreds are added by ANTLR, and we don't want them.
                    SYN_SEMPRED[] -> {
                        parseArgs(`y,container);
                        return;
                    }
                    _ -> {
                        try {
                            element2=ATerm2AntlrElement.getAntlrElement(`x);
                        } catch (AntlrWrongElementException e) {
                            container.goodParse=false;
                            element2=e.getAntlrElement();
                        }
                        container.element=`AntlrAndElement(element*,element2);
                        parseArgs(`y,container);
                        return;
                    }
                }
            }
            _ -> {
                AntlrElement element=container.element;
                AntlrElement element2;
                container.goodParse=false;
                // We expected EOA and we don't have it, so make an empty wrong element.
                try {
                    element2=ATerm2AntlrElement.getAntlrElement(`concATerm());
                } catch (AntlrWrongElementException e) {
                    element2=e.getAntlrElement();
                }
            }
        }
    }
}
