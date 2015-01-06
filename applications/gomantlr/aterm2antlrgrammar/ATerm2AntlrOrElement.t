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

import antlrgrammar.antlrelement.types.AntlrElement;
import antlrgrammar.antlrelement.types.antlrelement.AntlrOrElement;
import antlrgrammar.antlrelement.types.antlrwrongelement.AntlrIncorrectOrElement;

import aterm2antlrgrammar.exceptions.AntlrWrongElementException;

public class ATerm2AntlrOrElement {
   
    %include { ../antlrgrammar/AntlrGrammar.tom }

    private static class Container {
	// enums are not available before java5, lets avoid them
        public static class OrElementType {
            public static final OrElementType OR_ELEMENT=new OrElementType();
	    public static final OrElementType SET_OR_ELEMENT=new OrElementType();

	    private OrElementType() {
	    }
        }

        public OrElementType elementType=null;
        public AntlrElement element=`AntlrOrElement();

        public boolean goodParse=true;

        public Container(OrElementType elementType) {
            this.elementType=elementType;
        }
    }

    %include { ../antlr.tom }

    /*
     *
     * OrElement: Element ('|' Element)* EOB
     * SetOrElement: Element ('|' Element)*
     *
     * When we only have one Element, we just return it.
     *
     */

    public static AntlrElement getAntlrOrElement(ATerm t) throws AntlrWrongElementException {
        Container.OrElementType elementType=null;
        ATermList l=null;
        %match(t) {
            BLOCK(_,x) -> {
                l=`x;
                elementType=Container.OrElementType.OR_ELEMENT;
            }
            SET(_,x) -> {
                l=`x;
                elementType=Container.OrElementType.SET_OR_ELEMENT;
            }
        }
        if(l!=null) {
            Container container=new Container(elementType);
            parseArgs(`l,container);
            AntlrElement element=container.element;
            if(container.goodParse) {
                // Here, element *must* be some AntlrOrElement().
                %match(element) {
                    AntlrOrElement() -> {
                        throw new AntlrWrongElementException(`AntlrOrElementNoElement(ATerm2AntlrWrong.getAntlrWrong(t)));
                    }
                    AntlrOrElement(x) -> {
                        return `x;
                    }
                }
                // java is unhappy with "_ ->"
                return element;
            } else {
                // would be great to write `AntlrIncorrectOrElement(element)
                throw new AntlrWrongElementException(AntlrIncorrectOrElement.fromArray(((AntlrOrElement)element).toArray()));
            }
        } else {
            throw new AntlrWrongElementException(`AntlrPlainWrongOrElement(ATerm2AntlrWrong.getAntlrWrong(t)));
        }
    }

    private static void parseArgs(ATermList l,Container container) {
        %match(l) {
            concATerm(EOB[]) -> {
                if(container.elementType==Container.OrElementType.OR_ELEMENT) {
                    return;
                }
                // else let's continue
            }
            concATerm(x,y*) -> {
                AntlrElement element=container.element;
                AntlrElement element2;
                try {
                    element2=ATerm2AntlrElement.getAntlrElement(`x);
                } catch (AntlrWrongElementException e) {
                    container.goodParse=false;
                    element2=e.getAntlrElement();
                }
                container.element=`AntlrOrElement(element*,element2);
                parseArgs(`y,container);
                return;
            }
            _ -> {
                if(container.elementType==Container.OrElementType.OR_ELEMENT) {
                    AntlrElement element=container.element;
                    AntlrElement element2;
                    container.goodParse=false;
                    // We expected EOB and we don't have it, so make an empty wrong element.
                    try {
                        element2=ATerm2AntlrElement.getAntlrElement(`concATerm());
                    } catch (AntlrWrongElementException e) {
                        element2=e.getAntlrElement();
                    }
                }
            }
        }
    }
}
