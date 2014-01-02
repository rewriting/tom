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

import antlrgrammar.antlrcommons.types.AntlrOption;
import antlrgrammar.antlrcommons.types.AntlrId;
import antlrgrammar.antlrcommons.types.AntlrInt;
import antlrgrammar.antlrcommons.types.AntlrWrong;

import aterm2antlrgrammar.ATerm2AntlrWrong;
import aterm2antlrgrammar.ATerm2AntlrId;
import aterm2antlrgrammar.ATerm2AntlrInt;

import aterm2antlrgrammar.exceptions.AntlrWrongOptionException;
import aterm2antlrgrammar.exceptions.AntlrWrongIdException;
import aterm2antlrgrammar.exceptions.AntlrWrongIntException;

public class ATerm2AntlrOption {
   
    %include { ../antlrgrammar/AntlrGrammar.tom }

    private static class Container {
        public AntlrId id=null;
        public AntlrId src=null;
        public AntlrInt i=null;

        public boolean goodParse=true;
        public AntlrWrong wrong=null;
    }

    %include { ../antlr.tom }

    /*
     *
     * Dark corner of antlr.g
     *
     * For now we have:
     *
     * Option: Id "="^ Id
     * Option: Id "="^ Int
     *
     */

    public static AntlrOption getAntlrOption(ATerm t) throws AntlrWrongOptionException {
        %match(t) {
            ASSIGN(_,x) -> {
                Container container=new Container();
                parseArgs(`x,container);
                if(container.goodParse) {
                    if(container.src!=null) {
                        return `AntlrAssignId(container.id,container.src);
                    } else {
                        return `AntlrAssignInt(container.id,container.i);
                    }
                } else {
                    if(container.wrong!=null) {
                    } else {
                        if(container.src!=null) {
                            throw new AntlrWrongOptionException(`AntlrIncorrectAssignId(container.id,container.src,container.wrong));
                        } else {
                            throw new AntlrWrongOptionException(`AntlrIncorrectAssignInt(container.id,container.i,container.wrong));
                        }
                    }
                }
            }
        }
        throw new AntlrWrongOptionException(`AntlrPlainWrongOption(ATerm2AntlrWrong.getAntlrWrong(t)));
    }

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

    private static void parseArgs2(ATermList l,Container container) {
        %match(l) {
            concATerm(x@ID[],y*) -> {
                try {
                    container.src=ATerm2AntlrId.getAntlrId(`x);
                } catch (AntlrWrongIdException e) {
                    container.goodParse=false;
                    container.src=e.getAntlrId();
                }
                parseArgs3(`y,container);
                return;
            }
            concATerm(x,y*) -> {
                try {
                    container.i=ATerm2AntlrInt.getAntlrInt(`x);
                } catch (AntlrWrongIntException e) {
                    container.goodParse=false;
                    container.i=e.getAntlrInt();
                }
                parseArgs3(`y,container);
                return;
            }
            _ -> {
                container.goodParse=false;
                try {
                    container.i=ATerm2AntlrInt.getAntlrInt(`concATerm());
                } catch (AntlrWrongIntException e) {
                    container.i=e.getAntlrInt();
                }
            }
        }
    }

    private static void parseArgs3(ATermList l,Container container) {
        %match(l) {
            concATerm(x,y*) -> {
                container.goodParse=false;
                container.wrong=ATerm2AntlrWrong.getAntlrWrong(l);
            }
        }
    }
}
