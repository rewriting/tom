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

import antlrgrammar.antlrcommons.types.AntlrOptions;
import antlrgrammar.antlrcommons.types.AntlrOption;
import antlrgrammar.antlrcommons.types.AntlrWrongOptions;
import antlrgrammar.antlrcommons.types.antlrwrongoptions.AntlrIncorrectOptions;

import aterm2antlrgrammar.exceptions.AntlrWrongOptionsException;
import aterm2antlrgrammar.exceptions.AntlrWrongOptionException;

public class ATerm2AntlrOptions {
   
    %include { ../antlrgrammar/AntlrGrammar.tom }

    private static class Container {
        public AntlrOptions options=`AntlrOptions();
        
        public boolean goodParse=true;
    }
    
    %include { ../antlr.tom }

    /*
     *
     * Options: "options" Option+
     *
     */

    public static AntlrOptions getAntlrOptions(ATerm t) throws AntlrWrongOptionsException {
        %match(t) {
            OPTIONS(_,x) -> {
                Container container=new Container();
                parseArgs(`x,container);
                AntlrOptions options=container.options;
                if(container.goodParse) {
                    %match(options) {
                        AntlrOptions() -> {
                            // No option is not OK.
                            throw new AntlrWrongOptionsException(`AntlrNoOption(ATerm2AntlrWrong.getAntlrWrong(t)));
                        }
                        _ -> {
                            return options;
                        }
                    }
                } else {
                    // would be great to write `AntlrIncorrectOptions(options)
                    throw new AntlrWrongOptionsException(AntlrIncorrectOptions.fromArray((( antlrgrammar.antlrcommons.types.antlroptions.AntlrOptions)options).toArray()));
                }
            }
        }
        throw new AntlrWrongOptionsException(`AntlrPlainWrongOptions(ATerm2AntlrWrong.getAntlrWrong(t)));
    }

    private static void parseArgs(ATermList l,Container container) {
        %match(l) {
            concATerm(x,y*) -> {
                AntlrOptions options=container.options;
                AntlrOption option;
                try {
                    option=ATerm2AntlrOption.getAntlrOption(`x);
                } catch (AntlrWrongOptionException e) {
                    container.goodParse=false;
                    option=e.getAntlrOption();
                }
                container.options=`AntlrOptions(options*,option);
                parseArgs(`y,container);
            }
        }
    }
}
