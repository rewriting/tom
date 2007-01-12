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

package gomantlr;

import aterm.*;
import aterm.pure.*;

import antlrgrammar.antlrelement.types.AntlrElement;

import antlrgrammar.antlrcommons.types.AntlrOptions;
import antlrgrammar.antlrcommons.types.AntlrOption;
import antlrgrammar.antlrcommons.types.AntlrId;
import antlrgrammar.antlrcommons.types.AntlrUnrecognized;

public class ATerm2AntlrElement {
   
    %include { ../antlrgrammar/AntlrGrammar.tom }
    %include { ../antlr.tom }

    public static AntlrElement getAntlrElement(ATerm t) {
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
                return `AntlrClosure(options,element);
            }
            POSITIVE_CLOSURE(_,(x*)) -> {
                AntlrOptions options=`getAntlrOptions(locateAntlrOptions(x));
                AntlrElement element=`getAntlrOrElement(locateAntlrElement(x));
                return `AntlrPositiveClosure(options,element);
            }
            OPTIONAL(_,(x*)) -> {
                AntlrOptions options=`getAntlrOptions(locateAntlrOptions(x));
                AntlrElement element=`getAntlrOrElement(locateAntlrElement(x));
                return `AntlrOptional(options,element);
            }
            OPTIONS(_,x) -> {
                return `AntlrEltOptions(AntlrUnrecognized(x));
            }
            NOT(_,(x*)) -> {
                return `AntlrNot(getAntlrOrElement(x));
            }
            CHAR_RANGE(_,(CHAR_LITERAL(NodeInfo(x,_,_),_),CHAR_LITERAL(NodeInfo(y,_,_),_))) -> {
                return `AntlrCharRange(x,y);
            }
            WILDCARD(_,_) -> {
                return `AntlrWildcard();
            }
            SYN_SEMPRED(NodeInfo(x,_,_),_) -> {
                return `AntlrSempred(x);
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
                return `AntlrChar(x);
            }
            ACTION(NodeInfo(y,_,_),_) -> {
                return `AntlrEltAction(y);
            }
            EPSILON(_,_) -> {
                return `AntlrEpsilon();
            }
            _ -> {
                return `AntlrElement(AntlrUnrecognized(t));
            }
        }
        return null;
    }

    private static AntlrUnrecognized getAntlrUnrecognized(ATerm t) {
        return `AntlrUnrecognized(t);
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
        AntlrElement e=`AntlrElement(AntlrUnrecognized(t));
        %match(ATermList t) {
            () -> {
                e=`AntlrOrElement();
            }
            (x,y*) -> {
                AntlrElement element=`inner_getAntlrOrElement(y);
                e=`AntlrOrElement(getAntlrElement(x),element*);
            }
        }
        return e;
    }

    private static AntlrElement getAntlrOrElement(ATermList t) {
        AntlrElement e=`AntlrElement(AntlrUnrecognized(t));
        %match(ATermList t) {
            () -> {
                e=`AntlrOrElement();
            }
            (x,y*) -> {
                AntlrElement element=`inner_getAntlrOrElement(y);
                e=`AntlrOrElement(getAntlrElement(x),element*);
            }
        }
        return normalizeElement(e);
    }

    private static AntlrElement inner_getAntlrAndElement(ATermList t) {
        AntlrElement e=`AntlrElement(AntlrUnrecognized(t));
        %match(ATermList t) {
            () -> {
                e=`AntlrAndElement();
            }
            (x,y*) -> {
                AntlrElement element=`inner_getAntlrAndElement(y);
                e=`AntlrAndElement(getAntlrElement(x),element*);
            }
        }
        return e;
    }

    private static AntlrElement getAntlrAndElement(ATermList t) {
        AntlrElement e=`AntlrElement(AntlrUnrecognized(t));
        %match(ATermList t) {
            () -> {
                e=`AntlrAndElement();
            }
            (x,y*) -> {
                AntlrElement element=`inner_getAntlrAndElement(y);
                e=`AntlrAndElement(getAntlrElement(x),element*);
            }
        }
        return normalizeElement(e);
    }

    private static AntlrElement normalizeElement(AntlrElement e) {
        AntlrElement e1=e;
        %match(e) {
            AntlrAndElement(AntlrSempred(_),y*) -> {
                e1=`AntlrAndElement(y*);
            }
            AntlrOrElement(AntlrSempred(_),y*) -> {
                e1=`AntlrOrElement(y*);
            }
        }
        %match(e1) {
            AntlrAndElement(x) -> {
                return `x;
            }
            AntlrOrElement(x) -> {
                return `x;
            }
        }
        return e1;
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
                    return `AntlrOptions(getAntlrOption(x),options*);
                } else {
                    return options;
                }
            }
        }
        return `AntlrOptions();
    }

    private static AntlrOption getAntlrOption(ATerm t) {
        %match(ATerm t) {
            ASSIGN(_,(ID(NodeInfo(name,_,_),_),ID(NodeInfo(src,_,_),_))) -> {
                return `AntlrAssignId(getAntlrId(name),getAntlrId(src));
            }
            ASSIGN(_,(ID(NodeInfo(name,_,_),_),INT(NodeInfo(value,_,_),_))) -> {
                return `AntlrAssignInt(getAntlrId(name),AntlrInt(Integer.parseInt(value)));
            }
        }
        return `AntlrWrongOption(AntlrPlainWrongOption(ATerm2AntlrWrong.getAntlrWrong(t)));
    }

    private static AntlrId getAntlrId(String s) {
        return `AntlrId(s);
    }
}
