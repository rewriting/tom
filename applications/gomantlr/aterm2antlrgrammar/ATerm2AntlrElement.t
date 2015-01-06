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

import aterm2antlrgrammar.exceptions.AntlrWrongElementException;

public class ATerm2AntlrElement {
   
    %include { ../antlrgrammar/AntlrGrammar.tom }
    %include { ../antlr.tom }

    /*
     *
     * This is the grammar corresponding to the AST,
     * which explains what is done here.
     *
     * The actual grammar is a bit different
     * (and more complex here)
     * but is not what matters here.
     *
     * Element: Block
     *        | AndElement
     *        | SetOrElement
     *        | Closure
     *        | PositiveClosure
     *        | Optional
     *        | NotElement
     *        | CharRange
     *        | Wildcard
     *        | Sempred
     *        | RuleRef
     *        | TokenRef
     *        | StringElement
     *        | CharElement
     *        | ActionElement
     *        | Epsilon
     *
     */

    public static AntlrElement getAntlrElement(ATerm t) throws AntlrWrongElementException {
        %match(t) {
            BLOCK[] -> {
                return ATerm2AntlrBlock.getAntlrBlock(t);
            }
            ALT[] -> {
                return ATerm2AntlrAndElement.getAntlrAndElement(t);
            }
            SET[] -> {
                return ATerm2AntlrOrElement.getAntlrOrElement(t);
            }
            CLOSURE[] -> {
                return ATerm2AntlrClosure.getAntlrClosure(t);
            }
            POSITIVE_CLOSURE[] -> {
                return ATerm2AntlrPositiveClosure.getAntlrPositiveClosure(t);
            }
            OPTIONAL[] -> {
                return ATerm2AntlrOptional.getAntlrOptional(t);
            }
            NOT[] -> {
                return ATerm2AntlrNotElement.getAntlrNotElement(t);
            }
            CHAR_RANGE[] -> {
                return ATerm2AntlrCharRange.getAntlrCharRange(t);
            }
            // We can recognize those directly here.
            // (they're not reused anywhere else)
            WILDCARD[] -> {
                return `AntlrWildcard();
            }
            RULE_REF(NodeInfo(x,_,_),_) -> {
                return `AntlrRuleRef(x);
            }
            TOKEN_REF(NodeInfo(x,_,_),_) -> {
                return `AntlrToken(x);
            }
            STRING_LITERAL(NodeInfo(x,_,_),_) -> {
                return `AntlrStringElement(x);
            }
            CHAR_LITERAL(NodeInfo(x,_,_),_) -> {
                return `AntlrCharElement(x);
            }
            ACTION(NodeInfo(y,_,_),_) -> {
                return `AntlrActionElement(y);
            }
            EPSILON[] -> {
                return `AntlrEpsilon();
            }
        }

        throw new AntlrWrongElementException(`AntlrPlainWrongElement(ATerm2AntlrWrong.getAntlrWrong(t)));
    }
}
