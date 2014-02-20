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

public class ATerm2AntlrCharRange {
   
    %include { ../antlrgrammar/AntlrGrammar.tom }

    private static class Container {
        public AntlrElement element=null;

        public boolean goodParse=true;
        public AntlrWrong wrong=null;
    }

    %include { ../antlr.tom }

    /*
     *
     * CharRange: Char ".."^ Char
     *
     */

    public static AntlrElement getAntlrCharRange(ATerm t) throws AntlrWrongElementException {
        %match(t) {
            CHAR_RANGE(_,(CHAR_LITERAL(NodeInfo(x,_,_),_),CHAR_LITERAL(NodeInfo(y,_,_),_))) -> {
                return `AntlrCharRange(x,y);
            }
        }

        // CharRange is too simple to go into details.
        throw new AntlrWrongElementException(`AntlrWrongCharRange(ATerm2AntlrWrong.getAntlrWrong(t)));
    }
}
